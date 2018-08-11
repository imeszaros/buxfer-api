package com.github.imeszaros.buxfer

class BuxferException(message: String, val type: String? = null) : RuntimeException(message)