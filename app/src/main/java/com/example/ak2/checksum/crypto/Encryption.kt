package com.example.ak2.checksum.crypto

interface Encryption {
    @Throws(Exception::class)
    fun encrypt(var1: String, var2: String): String

    @Throws(Exception::class)
    fun decrypt(var1: String, var2: String): String
}
