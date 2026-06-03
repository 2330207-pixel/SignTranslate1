package com.example.signtranslate1

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform