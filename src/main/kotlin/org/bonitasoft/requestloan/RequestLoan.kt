package org.bonitasoft.requestloan

import org.bonitasoft.engine.bpm.bar.BusinessArchive
import org.bonitasoft.engine.bpm.bar.BusinessArchiveBuilder
import org.bonitasoft.engine.bpm.bar.actorMapping.Actor
import org.bonitasoft.engine.bpm.bar.actorMapping.ActorMapping
import org.bonitasoft.requestloan.dsl.businessArchive
import org.bonitasoft.requestloan.dsl.process
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RequestLoan {

    @Bean
    fun requestLoanProcess(): BusinessArchive {

        val processDefinition = process("Request Loan", "1.0") {
            val requester = initiator("requester")
            val validator = actor("validator")

            data {
                text("type")
                integer("amount")
                boolean("accepted")
                text("reason")
            }

            contract {
                text("type") describedAs "type of the loan"
                integer("amount") describedAs "amount of the loan"
            }

            userTask("Review request", validator) {
                contract {
                    boolean("accept") describedAs "whether the load is accepted or not"
                    text("reason") describedAs "why the loan was accepted/rejected"
                }
                operations {
                    update("accepted") withBooleanContractValue "accept"
                    update("reason") withStringContractValue "reason"
                }
            }
            userTask("Sign contract", requester)
            exclusiveGateway("isAccepted")
            automaticTask("Notify reject")
            transitions {
                normal() from "Review request" to "isAccepted"
                conditional("accepted") from "isAccepted" to "Sign contract"
                default() from "isAccepted" to "Notify reject"
            }

        }

        return businessArchive {
            process = processDefinition
            actorMapping {
                "requester" mappedToUser "john"
                "validator" mappedToUser "jack"
            }
        }
    }

}