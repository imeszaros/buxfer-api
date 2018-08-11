package com.github.imeszaros.buxfer

import com.google.gson.GsonBuilder
import java.time.LocalDate

interface Transaction {

    val type: TransactionType
    var description: String
    var amount: Double
    var date: LocalDate
    var tags: List<String>
    var status: TransactionStatus

    fun formData() = mapOf(
            "type" to type.buxferName,
            "description" to description,
            "amount" to amount.toString(),
            "date" to date.format(),
            "tags" to tags.joinToString(","),
            "status" to status.buxferName)

    interface SingleAccountTransaction : Transaction {

        var accountId: Int

        override fun formData() = super.formData() + mapOf(
                "accountId" to accountId.toString())
    }

    interface MultiAccountTransaction : Transaction {

        var fromAccountId: Int
        var toAccountId: Int

        override fun formData() = super.formData() + mapOf(
                "fromAccountId" to fromAccountId.toString(),
                "toAccountId" to toAccountId.toString())
    }

    data class Income(
            override var accountId: Int,
            override var description: String,
            override var amount: Double,
            override var date: LocalDate = LocalDate.now(),
            override var tags: List<String> = listOf(),
            override var status: TransactionStatus = TransactionStatus.Cleared) : SingleAccountTransaction {

        override val type = TransactionType.Income
    }

    data class Expense(
            override var accountId: Int,
            override var description: String,
            override var amount: Double,
            override var date: LocalDate = LocalDate.now(),
            override var tags: List<String> = listOf(),
            override var status: TransactionStatus = TransactionStatus.Cleared) : SingleAccountTransaction {

        override val type = TransactionType.Expense
    }

    data class Refund(
            override var accountId: Int,
            override var description: String,
            override var amount: Double,
            override var date: LocalDate = LocalDate.now(),
            override var tags: List<String> = listOf(),
            override var status: TransactionStatus = TransactionStatus.Cleared) : SingleAccountTransaction {

        override var type = TransactionType.Refund
    }

    data class Transfer(
            override var fromAccountId: Int,
            override var toAccountId: Int,
            override var description: String,
            override var amount: Double,
            override var date: LocalDate = LocalDate.now(),
            override var tags: List<String> = listOf(),
            override var status: TransactionStatus = TransactionStatus.Cleared) : MultiAccountTransaction {

        override val type = TransactionType.Transfer
    }

    data class SharedBill(
            override var accountId: Int,
            override var description: String,
            override var amount: Double,
            var payers: List<Payer>,
            var sharers: List<Sharer>,
            var isEvenSplit: Boolean,
            override var date: LocalDate = LocalDate.now(),
            override var tags: List<String> = listOf(),
            override var status: TransactionStatus = TransactionStatus.Cleared) : SingleAccountTransaction {

        override val type = TransactionType.SharedBill

        override fun formData() = super.formData() + mapOf(
                "payers" to gson.toJson(payers),
                "sharers" to gson.toJson(sharers),
                "isEvenSplit" to when(isEvenSplit) {
                    true -> "true"
                    false -> "false"
                })
    }

    data class Loan(
            override var accountId: Int,
            override var description: String,
            override var amount: Double,
            var loanedBy: String,
            var borrowedBy: String,
            override var date: LocalDate = LocalDate.now(),
            override var tags: List<String> = listOf(),
            override var status: TransactionStatus = TransactionStatus.Cleared) : SingleAccountTransaction {

        override val type = TransactionType.Loan

        override fun formData() = super.formData() + mapOf(
                "loanedBy" to loanedBy,
                "borrowedBy" to borrowedBy)
    }

    data class PaidForFriend(
            override var accountId: Int,
            override var description: String,
            override var amount: Double,
            var paidBy: String,
            var paidFor: String,
            override var date: LocalDate = LocalDate.now(),
            override var tags: List<String> = listOf(),
            override var status: TransactionStatus = TransactionStatus.Cleared) : SingleAccountTransaction {

        override val type = TransactionType.PaidForFriend

        override fun formData() = super.formData() + mapOf(
                "paidBy" to paidBy,
                "paidFor" to paidFor)
    }

    private companion object {

        private val gson = GsonBuilder().create()!!
    }
}