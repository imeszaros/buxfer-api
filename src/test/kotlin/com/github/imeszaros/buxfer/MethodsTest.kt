package com.github.imeszaros.buxfer

import okhttp3.logging.HttpLoggingInterceptor
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import java.time.LocalDate
import java.util.*

@TestInstance(PER_CLASS)
class MethodsTest {

    private var props: Properties? = null
    private var buxfer: Buxfer? = null

    @BeforeAll
    fun setup() {
        props = props()
        buxfer = props?.let { props ->
            Buxfer.login(props.getProperty("buxfer.username"), props.getProperty("buxfer.password")) {
                it.addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
            }
        }
    }

    @Test
    fun addTransactionTest() {
        buxfer?.let {
            val resp = it.addTransaction(Transaction.Expense(
                    props!!.getProperty("buxfer.account.id").toInt(),
                    "Test Transaction 3", 10.0, LocalDate.now(), listOf("Health")))

            assertNotNull(resp.tid)
        }
    }

    @Test
    fun transactionsTest() {
        buxfer?.let {
            val resp = it.transactions()
            assertNotNull(resp.numTransactions)
            assertNotNull(resp.transactions)
        }
    }

    @Test
    fun accountsTest() {
        buxfer?.let {
            val resp = it.accounts()
            assertNotNull(resp.accounts)
        }
    }

    @Test
    fun loansTest() {
        buxfer?.let {
            val resp = it.loans()
            assertNotNull(resp.loans)
        }
    }

    @Test
    fun tagsTest() {
        buxfer?.let {
            val resp = it.tags()
            assertNotNull(resp.tags)
        }
    }

    @Test
    fun budgetsTest() {
        buxfer?.let {
            val resp = it.budgets()
            assertNotNull(resp.budgets)
        }
    }

    @Test
    fun remindersTest() {
        buxfer?.let {
            val resp = it.reminders()
            assertNotNull(resp.reminders)
        }
    }

    @Test
    fun groupsTest() {
        buxfer?.let {
            val resp = it.groups()
            assertNotNull(resp.groups)
        }
    }

    @Test
    fun contactsTest() {
        buxfer?.let {
            val resp = it.contacts()
            assertNotNull(resp.contacts)
        }
    }
}