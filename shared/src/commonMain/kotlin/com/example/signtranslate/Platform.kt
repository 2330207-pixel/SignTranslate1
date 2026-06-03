package com.example.signtranslate

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform