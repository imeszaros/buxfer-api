package com.github.imeszaros.buxfer

import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import java.time.format.DateTimeFormatter
import java.util.*

class Buxfer private constructor(
        private val client: OkHttpClient, val baseUrl: String, val token: String) {

    fun addTransaction(transaction: Transaction) = urlBuilder("add_transaction").buxferPost(transaction.formData())

    fun transactions(query: TransactionQuery = TransactionQuery()) = urlBuilder("transactions").params(query.parameters()).buxferGet()

    fun accounts() = urlBuilder("accounts").buxferGet()

    fun loans() = urlBuilder("loans").buxferGet()

    fun tags() = urlBuilder("tags").buxferGet()

    fun budgets() = urlBuilder("budgets").buxferGet()

    fun reminders() = urlBuilder("reminders").buxferGet()

    fun groups() = urlBuilder("groups").buxferGet()

    fun contacts() = urlBuilder("contacts").buxferGet()

    private fun urlBuilder(path: String) = urlBuilder(baseUrl, path).params("token" to token)

    private fun HttpUrl.Builder.buxferGet() = buxferGet(client)

    private fun HttpUrl.Builder.buxferPost(data: Map<String, String>) = buxferPost(client, data)

    companion object {

        const val DEFAULT_BASE_URL = "https://www.buxfer.com/api/"

        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH)!!
        val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)!!

        private fun urlBuilder(baseUrl: String, path: String) = HttpUrl
                .parse(baseUrl + path)
                .let { it ?: throw RuntimeException("URL is malformed: $baseUrl$path") }
                .newBuilder()

        fun login(userId: String, password: String,
                  baseUrl: String = DEFAULT_BASE_URL,
                  httpClientConfigurator: (OkHttpClient.Builder) -> OkHttpClient.Builder = { b -> b }) = OkHttpClient.Builder()

                .let { httpClientConfigurator.invoke(it).build()!! }
                .let { urlBuilder(baseUrl, "login")
                        .params("userid" to userId, "password" to password)
                        .buxferGet(it)
                        .token
                        ?.let { token -> Buxfer(it, baseUrl, token) }
                        ?: throw RuntimeException("No token was received.")
                }
    }
}