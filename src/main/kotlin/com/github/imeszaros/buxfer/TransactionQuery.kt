package com.github.imeszaros.buxfer

import java.time.LocalDate

data class TransactionQuery(
        val page: Int? = null,
        val accountId: Int? = null,
        val accountName: String? = null,
        val tagId: Int? = null,
        val tagName: String? = null,
        val interval: Pair<LocalDate, LocalDate>? = null,
        val budgetId: Int? = null,
        val budgetName: String? = null,
        val contactId: Int? = null,
        val contactName: String? = null,
        val groupId: Int? = null,
        val groupName: String? = null) {

    fun parameters() = arrayOf(
            "page" to page?.toString(),
            "accountId" to accountId?.toString(),
            "accountName" to accountName,
            "tagId" to tagId?.toString(),
            "tagName" to tagName,
            "startDate" to interval?.first?.format(),
            "endDate" to interval?.second?.format(),
            "budgetId" to budgetId?.toString(),
            "budgetName" to budgetName,
            "contactId" to contactId?.toString(),
            "contactName" to contactName,
            "groupId" to groupId?.toString(),
            "groupName" to groupName)

            .filter { it.second != null }
            .map { it.first to it.second!! }
            .toMap()
}