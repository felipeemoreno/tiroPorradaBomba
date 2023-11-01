package com.nativemodule.clisitefmodule

import br.com.softwareexpress.sitef.android.CliSiTef
import br.com.softwareexpress.sitef.android.ICliSiTefListener

class TefMethods(cliSiTef: CliSiTef): SiTefClient(cliSiTef) {
    var idConfig: Int = 0;

    fun startTransaction(
        listener: ICliSiTefListener,
        functionId: Int,
        trnAmount: String,
        taxInvoiceNumber: String,
        taxInvoiceDate: String,
        taxInvoiceTime: String,
        cashierOperator: String
    ) {
        processTransactionStatus(
            cliSiTef.startTransaction(
                listener,
                functionId,
                trnAmount,
                taxInvoiceNumber,
                taxInvoiceDate,
                taxInvoiceTime,
                cashierOperator,
                ""
            )
        )
    }

    private fun processTransactionStatus(transactionStatus: Int) {
        when (transactionStatus) {
            10000 -> success(true)
            0 -> success(true)
            -1 -> error(transactionStatus.toString(), "Module not initialized")
            -2 -> error(transactionStatus.toString(), "Operation aborted by the operator")
            -3 -> error(transactionStatus.toString(), "Invalid functionId")
            -4 -> error(transactionStatus.toString(), "Low memory to run the function")
            -5 -> error(transactionStatus.toString(), "No communication with SiTef server")
            -6 -> error(transactionStatus.toString(), "Operation aborted by the user")
            in 1..Int.MAX_VALUE -> error(transactionStatus.toString(), "Denied by the authorizer")
            in Int.MIN_VALUE..-7 -> error(
                transactionStatus.toString(),
                "Errors internally detected by the routine"
            )

        }
    }

    private fun error(message: String, s: String) {
        TODO("Not yet implemented")
    }
}
