package com.github.imeszaros.buxfer

import okhttp3.logging.HttpLoggingInterceptor
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import java.util.*

@TestInstance(PER_CLASS)
class LoginTest {

    private var props: Properties? = null

    @BeforeAll
    fun setup() {
        props = props()
    }

    @Test
    fun rightCredentials() {
        props?.let { props ->
            val buxfer = Buxfer.login(props.getProperty("buxfer.username"), props.getProperty("buxfer.password")) {
                it.addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
            }

            assertTrue(buxfer.token.isNotBlank())
        }
    }

    @Test
    fun wrongCredentials() {
        assertThrows(BuxferException::class.java) {
            Buxfer.login("sillyuser", "sillypass") {
                it.addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
            }
        }
    }
}