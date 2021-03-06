package com.example.ak2.checksum.crypto

object EncryptionFactory {
    fun getEncryptionInstance(algorithmType: String?): Encryption {
        return AesEncryption()
    }

    fun getEncryptionInstanceGAE(algorithmType: String?): EncryptionGAE {
        return AesEncryptionGAE()
    }

    fun getEncryptionInstanceIBMJCE(algorithmType: String?): EncryptionIBMJCE {
        return AesEncryptionIBMJCE()
    }
}