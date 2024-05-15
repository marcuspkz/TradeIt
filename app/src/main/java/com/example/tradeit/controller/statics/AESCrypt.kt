package com.example.tradeit.controller.statics

import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import kotlin.random.Random

object AESCrypt {
    private val AES_MODE = "AES/ECB/PKCS5Padding"
    private val SECRET_KEY_ALGORITHM = "AES"

    fun encrypt(textToEncrypt: String, secretKey: String): String {
        try {
            val cipher = Cipher.getInstance(AES_MODE)
            val keySpec = SecretKeySpec(secretKey.toByteArray(), SECRET_KEY_ALGORITHM)
            cipher.init(Cipher.ENCRYPT_MODE, keySpec)
            val encryptedBytes = cipher.doFinal(textToEncrypt.toByteArray())
            return Base64.encodeToString(encryptedBytes, Base64.DEFAULT)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    fun decrypt(encryptedText: String, secretKey: String): String {
        try {
            val cipher = Cipher.getInstance(AES_MODE)
            val keySpec = SecretKeySpec(secretKey.toByteArray(), SECRET_KEY_ALGORITHM)
            cipher.init(Cipher.DECRYPT_MODE, keySpec)
            val encryptedBytes = Base64.decode(encryptedText, Base64.DEFAULT)
            val decryptedBytes = cipher.doFinal(encryptedBytes)
            return String(decryptedBytes)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }
}