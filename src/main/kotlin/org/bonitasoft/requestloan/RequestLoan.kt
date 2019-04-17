package org.bonitasoft.requestloan

import org.bonitasoft.engine.dsl.process.ExpressionDSLBuilder.ExpressionDSLBuilderObject.contractValue
import org.bonitasoft.engine.dsl.process.ExpressionDSLBuilder.ExpressionDSLBuilderObject.dataRef
import org.bonitasoft.engine.dsl.process.Process
import org.bonitasoft.engine.dsl.process.ProcessConfiguration
import org.bonitasoft.engine.dsl.process.configuration
import org.bonitasoft.engine.dsl.process.process
import org.bonitasoft.engine.spring.BonitaProcessBuilder
import org.bonitasoft.engine.spring.annotations.BonitaProcess


@BonitaProcess
class RequestLoan : BonitaProcessBuilder {
    override fun build(): Process = process("Request Loan", "1.0") {
        val requester = initiator("requester")
        val validator = actor("validator")
        data {
            boolean named "accepted"
        }
        contract {
            integer named "amount"
            text named "type"
        }
        val reviewRequest = userTask("Review request") {
            actor = validator
            contract {
                boolean named "accept"
                text named "reason"
            }
            operations {
                update("accepted").with(contractValue("accept"))
            }
        }
        val gateway = exclusiveGateway("accepted")
        val signContract = userTask("Sign contract") {
            actor = requester
        }
        val notifyReject = automaticTask("Notify reject")
        transitions {
            reviewRequest to gateway
            gateway to signContract withCondition dataRef("accepted")
            default from gateway to notifyReject
        }
    }

    override fun configuration(): ProcessConfiguration = configuration {
        actorMapping {
            "requester" toUser "john"
            "validator" toUser "jack"
        }
    }
}