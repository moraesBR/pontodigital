package com.materdei.pontodigital.di

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.materdei.pontodigital.utils.Constants.Companion.BIOMETRIC_FAILED
import com.materdei.pontodigital.utils.Constants.Companion.BIOMETRIC_KEY
import com.materdei.pontodigital.utils.Constants.Companion.BIOMETRIC_NEGATIVE_BUTTON
import com.materdei.pontodigital.utils.Constants.Companion.BIOMETRIC_SUBTITLE
import com.materdei.pontodigital.utils.Constants.Companion.BIOMETRIC_TITLE
import java.nio.charset.Charset
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

/* TODO 005.07: autenticação biométrica para confirmar o punch */
object Biometric {

    fun authentication(fragment: Fragment, action: (Boolean, String) -> Unit ){

        val authenticationCallback = object : BiometricPrompt.AuthenticationCallback(){
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)

                val encryptedInfo: ByteArray = result.cryptoObject?.cipher?.doFinal(
                    BIOMETRIC_KEY.toByteArray(Charset.defaultCharset()))!!
                action(true,encryptedInfo.toString())
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                action(false,"$errString (${errorCode})")
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                action(false,BIOMETRIC_FAILED)
            }
        }

        val executor = ContextCompat.getMainExecutor(fragment.requireContext())
        val biometricPrompt = BiometricPrompt(fragment, executor, authenticationCallback)

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(BIOMETRIC_TITLE)
            .setSubtitle(BIOMETRIC_SUBTITLE)
            .setNegativeButtonText(BIOMETRIC_NEGATIVE_BUTTON)
            .build()

        biometricPrompt.authenticate(promptInfo,BiometricPrompt.CryptoObject(createCipher()))

    }

    private fun createCipher(): Cipher {
        generateSecretKey(KeyGenParameterSpec.Builder(
            BIOMETRIC_KEY,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
            .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
            .setUserAuthenticationRequired(true)
            .setInvalidatedByBiometricEnrollment(true)
            .build())

        val cipher = getCipher()
        val secretKey = getSecretKey()
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)

        return cipher
    }

    private fun generateSecretKey(keyGenParameterSpec: KeyGenParameterSpec) {
        val keyGenerator = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
        keyGenerator.init(keyGenParameterSpec)
        keyGenerator.generateKey()
    }

    private fun getSecretKey(): SecretKey {
        val keyStore = KeyStore.getInstance("AndroidKeyStore")

        // Before the keystore can be accessed, it must be loaded.
        keyStore.load(null)
        return keyStore.getKey(BIOMETRIC_KEY, null) as SecretKey
    }

    private fun getCipher(): Cipher {
        return Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/"
                + KeyProperties.BLOCK_MODE_CBC + "/"
                + KeyProperties.ENCRYPTION_PADDING_PKCS7)
    }
}