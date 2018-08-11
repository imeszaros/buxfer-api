package com.github.imeszaros.buxfer

enum class TransactionType(val buxferName: String) {

    Income("income"),
    Expense("expense"),
    Transfer("transfer"),
    Refund("refund"),
    SharedBill("sharedBill"),
    PaidForFriend("paidForFriend"),
    Loan("loan")
}