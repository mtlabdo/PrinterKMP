package com.kmp.printer

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform