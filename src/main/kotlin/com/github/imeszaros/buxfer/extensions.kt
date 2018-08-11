package com.github.imeszaros.buxfer

import okhttp3.*
import java.time.LocalDate
import java.time.LocalDateTime

fun String.parseLocalDate() = LocalDate.parse(this, Buxfer.dateFormatter)!!
fun String.parseLocalDateTime() = LocalDateTime.parse(this, Buxfer.dateTimeFormatter)!!

fun LocalDate.format() = format(Buxfer.dateFormatter)!!

fun HttpUrl.Builder.params(params: Map<String, String>) = apply {
    params.forEach { key, value -> addQueryParameter(key, value) }
}

fun HttpUrl.Builder.params(vararg pairs: Pair<String, String>) = params(mapOf(*pairs))

fun HttpUrl.requestBuilder() = Request.Builder().url(this)!!

fun Request.Builder.postFormData(data: Map<String, String>) = post(FormBody.Builder()
        .apply { data.forEach { key, value -> add(key, value) } }
        .build())!!

fun Request.executeWith(client: OkHttpClient) = client.newCall(this).execute()!!

fun Response.asBuxferResponse() = BuxferResponse(this
        .body()
        .let { it ?: throw RuntimeException("Response body was null.") }
        .string())
        .apply { if (error) throw BuxferException(message ?: "Unknown error.", type) }

fun HttpUrl.Builder.buxferGet(client: OkHttpClient) = build()
        .requestBuilder()
        .get()
        .build()
        .executeWith(client)
        .asBuxferResponse()

fun HttpUrl.Builder.buxferPost(client: OkHttpClient, data: Map<String, String>) = build()
        .requestBuilder()
        .postFormData(data)
        .build()
        .executeWith(client)
        .asBuxferResponse()