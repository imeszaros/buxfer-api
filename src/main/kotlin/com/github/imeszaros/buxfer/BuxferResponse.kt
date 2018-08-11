package com.github.imeszaros.buxfer

import au.com.console.kassava.kotlinToString
import com.google.gson.JsonObject
import com.google.gson.JsonParser

class BuxferResponse(source: String) {

    private val root = JsonParser().parse(source).asJsonObject!!
    private val data = root.getAsJsonObject(if (error) "error" else "response")

    val error
        get() = root.has("error")

    val success
        get() = root.has("response")

    val requestId
        get() = data.get("request_id").asString!!

    val status
        get() = data.get("status")?.asString

    val type
        get() = data.get("type")?.asString

    val message
        get() = data.get("message")?.asString

    val token
        get() = data.get("token")?.asString

    val tid
        get() = data.get("tid")?.asInt

    val uploaded
        get() = data.get("uploaded")?.asBoolean

    val balance
        get() = data.get("balance")?.asDouble

    val numTransactions
        get() = data.get("numTransactions")?.asInt

    val transactions
        get() = data.get("transactions")?.asJsonArray?.map { Transaction(it.asJsonObject) }

    val accounts
        get() = data.get("accounts")?.asJsonArray?.map { Account(it.asJsonObject) }

    val loans
        get() = data.get("loans")?.asJsonArray?.map { Loan(it.asJsonObject) }

    val tags
        get() = data.get("tags")?.asJsonArray?.map { Tag(it.asJsonObject) }

    val budgets
        get() = data.get("budgets")?.asJsonArray?.map { Budget(it.asJsonObject) }

    val reminders
        get() = data.get("reminders")?.asJsonArray?.map { Reminder(it.asJsonObject) }

    val groups
        get() = data.get("groups")?.asJsonArray?.map { Group(it.asJsonObject) }

    val contacts
        get() = data.get("contacts")?.asJsonArray?.map { Contact(it.asJsonObject) }

    override fun toString() = kotlinToString(arrayOf(
            BuxferResponse::error,
            BuxferResponse::success,
            BuxferResponse::requestId,
            BuxferResponse::status,
            BuxferResponse::type,
            BuxferResponse::message,
            BuxferResponse::token,
            BuxferResponse::tid,
            BuxferResponse::uploaded,
            BuxferResponse::balance,
            BuxferResponse::numTransactions,
            BuxferResponse::transactions,
            BuxferResponse::accounts,
            BuxferResponse::loans,
            BuxferResponse::tags,
            BuxferResponse::budgets,
            BuxferResponse::reminders,
            BuxferResponse::groups,
            BuxferResponse::contacts))

    inner class Transaction(private val data: JsonObject) {

        val id
            get() = data.get("id").asInt

        val description
            get() = data.get("description").asString!!

        val date
            get() = data.get("date").asString!!

        val normalizedDate
            get() = data.get("normalizedDate").asString.parseLocalDate()

        val type
            get() = data.get("type").asString!!

        val transactionType
            get() = data.get("transactionType").asString!!

        val amount
            get() = data.get("amount").asDouble

        val expenseAmount
            get() = data.get("expenseAmount").asDouble

        val accountId
            get() = data.get("accountId")?.asInt

        val accountName
            get() = data.get("accountName")?.asString

        val tags
            get() = data.get("tags").asString!!

        val tagNames
            get() = data.get("tagNames").asJsonArray.map { it.asString!! }

        val status
            get() = data.get("status").asString.let { s ->
                TransactionStatus.values()
                        .find { it.buxferName == s } ?: throw BuxferException("Invalid transaction status: $s")
            }

        val isFutureDated
            get() = data.get("isFutureDated").asBoolean

        val fromAccountId
            get() = data.getAsJsonObject("fromAccount")?.get("id")?.asInt

        val fromAccountName
            get() = data.getAsJsonObject("fromAccount")?.get("name")?.asString

        val toAccountId
            get() = data.getAsJsonObject("toAccount")?.get("id")?.asInt

        val toAccountName
            get() = data.getAsJsonObject("toAccount")?.get("name")?.asString

        override fun toString() = kotlinToString(arrayOf(
                Transaction::id,
                Transaction::description,
                Transaction::date,
                Transaction::normalizedDate,
                Transaction::type,
                Transaction::transactionType,
                Transaction::amount,
                Transaction::expenseAmount,
                Transaction::accountId,
                Transaction::accountName,
                Transaction::tags,
                Transaction::tagNames,
                Transaction::status,
                Transaction::isFutureDated,
                Transaction::fromAccountId,
                Transaction::fromAccountName,
                Transaction::toAccountId,
                Transaction::toAccountName))
    }

    inner class Account(private val data: JsonObject) {

        val id
            get() = data.get("id").asInt

        val name
            get() = data.get("name").asString!!

        val bank
            get() = data.get("bank").asString!!

        val balance
            get() = data.get("balance").asDouble

        val currency
            get() = data.get("currency").asString!!

        val lastSynced
            get() = data.get("lastSynced")?.asString?.parseLocalDateTime()

        override fun toString() = kotlinToString(arrayOf(
                Account::id,
                Account::name,
                Account::bank,
                Account::balance,
                Account::currency,
                Account::lastSynced))
    }

    inner class Loan(private val data: JsonObject) {

        val entity
            get() = data.get("entity").asString!!

        val type
            get() = data.get("type").asString!!

        val balance
            get() = data.get("balance").asDouble

        val description
            get() = data.get("description").asString!!

        override fun toString() = kotlinToString(arrayOf(
                Loan::entity,
                Loan::type,
                Loan::balance,
                Loan::description))
    }

    inner class Tag(private val data: JsonObject) {

        val id
            get() = data.get("id").asInt

        val name
            get() = data.get("name").asString!!

        val relativeName
            get() = data.get("relativeName").asString!!

        val parentId
            get() = data.get("parentId")?.let {
                if (it.isJsonNull) null else it.asInt
            }

        fun parent() = parentId?.let { parentId -> tags?.find { it.id == parentId } }

        override fun toString() = kotlinToString(arrayOf(
                Tag::id,
                Tag::name,
                Tag::relativeName,
                Tag::parentId))
    }

    inner class Budget(private val data: JsonObject) {

        val id
            get() = data.get("id").asInt

        val tagId
            get() = data.get("tagId").asInt

        val name
            get() = data.get("name").asString!!

        val limit
            get() = data.get("limit").asDouble

        val spent
            get() = data.get("spent").asDouble

        val balance
            get() = data.get("balance").asDouble

        val period
            get() = data.get("period").asString!!

        override fun toString() = kotlinToString(arrayOf(
                Budget::id,
                Budget::tagId,
                Budget::name,
                Budget::limit,
                Budget::spent,
                Budget::balance,
                Budget::period))
    }

    inner class Reminder(private val data: JsonObject) {

        val id
            get() = data.get("id").asInt

        val nextExecution
            get() = data.get("nextExecution").asString.parseLocalDate()

        val dueDateDescription
            get() = data.get("dueDateDescription").asString!!

        val numDaysForDueDate
            get() = data.get("numDaysForDueDate").asInt

        val editMode
            get() = data.get("editMode").asString!!

        val periodSize
            get() = data.get("periodSize").asInt

        val periodUnit
            get() = data.get("periodUnit").asString!!

        val startDate
            get() = data.get("startDate").asString.parseLocalDate()

        val stopDate
            get() = data.get("stopDate")?.let {
                if (it.isJsonNull) null else it.asString.parseLocalDate()
            }

        val description
            get() = data.get("description").asString!!

        val type
            get() = data.get("type").asString!!

        val amount
            get() = data.get("amount").asDouble

        val accountId
            get() = data.get("accountId").asInt

        override fun toString() = kotlinToString(arrayOf(
                Reminder::id,
                Reminder::nextExecution,
                Reminder::dueDateDescription,
                Reminder::numDaysForDueDate,
                Reminder::editMode,
                Reminder::periodSize,
                Reminder::periodUnit,
                Reminder::startDate,
                Reminder::stopDate,
                Reminder::description,
                Reminder::type,
                Reminder::amount,
                Reminder::accountId))
    }

    inner class Group(private val data: JsonObject) {

        val id
            get() = data.get("id").asInt

        val name
            get() = data.get("name").asString!!

        val consolidated
            get() = data.get("consolidated").asBoolean

        val members
            get() = data.get("members")?.asJsonArray?.map { Member(it.asJsonObject) }

        override fun toString() = kotlinToString(arrayOf(
                Group::id,
                Group::name,
                Group::consolidated))

        inner class Member(private val data: JsonObject) {

            val id
                get() = data.get("id").asInt

            val name
                get() = data.get("name").asString!!

            val email
                get() = data.get("email")?.let {
                    if (it.isJsonNull) null else it.asString!!
                }

            val balance
                get() = data.get("balance").asDouble

            override fun toString() = kotlinToString(arrayOf(
                    Member::id,
                    Member::name,
                    Member::email,
                    Member::balance))
        }
    }

    inner class Contact(private val data: JsonObject) {

        val id
            get() = data.get("id").asInt

        val name
            get() = data.get("name").asString!!

        val email
            get() = data.get("email")?.let {
                if (it.isJsonNull) null else it.asString!!
            }

        val balance
            get() = data.get("balance").asDouble

        override fun toString() = kotlinToString(arrayOf(
                Contact::id,
                Contact::name,
                Contact::email,
                Contact::balance))
    }
}