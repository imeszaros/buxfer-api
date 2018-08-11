Buxfer API
=

A very lightweight JVM library allowing to access the [Buxfer API](https://www.buxfer.com/help/api). Written in Kotlin.

This library uses [OkHTTP](http://square.github.io/okhttp/) and [GSON](https://github.com/google/gson) under the hood.

License: [Apache 2.0](LICENSE)

Usage
-

Include the library in your build:
```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}

dependencies {
    implementation 'com.github.imeszaros:buxfer-api:v0.1'
}
```

Instantiate and call the API:
```kotlin
fun main(args: Array<String>) {
    val buxfer = Buxfer.login("username", "password")
    buxfer.transactions().transactions?.forEach(::println)
}
```