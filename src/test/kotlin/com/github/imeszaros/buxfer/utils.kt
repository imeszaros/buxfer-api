package com.github.imeszaros.buxfer

import java.util.*

fun props() = ClassLoader.getSystemResourceAsStream("test.properties")?.use { Properties().apply { load(it) } }