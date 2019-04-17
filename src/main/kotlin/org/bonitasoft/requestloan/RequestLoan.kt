package org.bonitasoft.requestloan

import org.bonitasoft.engine.dsl.process.Process
import org.bonitasoft.engine.dsl.process.ProcessConfiguration
import org.bonitasoft.engine.dsl.process.configuration
import org.bonitasoft.engine.dsl.process.process
import org.bonitasoft.engine.spring.BonitaProcessBuilder
import org.bonitasoft.engine.spring.annotations.BonitaProcess


@BonitaProcess
class RequestLoan : BonitaProcessBuilder {
    override fun build(): Process = process("Request Loan", "1.0") {
    }

    override fun configuration(): ProcessConfiguration = configuration {
    }
}