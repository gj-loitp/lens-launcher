package com.roy.ext

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.roy.R

object Biometric {
    fun isHaveBiometric(
        c: Context
    ): Boolean {
        val biometricManager = BiometricManager.from(c)
        return biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS
    }

    fun toggleLockApp(
        a: AppCompatActivity,
        appName: String,
        packageName: String,
        isAppLock: Boolean,
        onAuthenticationSucceeded: ((String, Boolean) -> Unit),
    ) {
        val description = if (isAppLock) {
            "Unlock $appName?"
        } else {
            "Lock $appName?"
        }

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(a.getString(R.string.verify_your_identity))
            .setDescription(description)
            .setNegativeButtonText(a.getString(R.string.cancel)).build()
        instanceOfBiometricPrompt(
            activity = a,
            packageName = packageName,
            isAppLock = isAppLock,
            onAuthenticationSucceeded = onAuthenticationSucceeded,
        ).authenticate(
            promptInfo
        )

    }

    private fun instanceOfBiometricPrompt(
        activity: AppCompatActivity,
        packageName: String,
        isAppLock: Boolean,
        onAuthenticationSucceeded: ((String, Boolean) -> Unit),
    ): BiometricPrompt {
        val executor = ContextCompat.getMainExecutor(activity)
        val callback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Log.d("BiometricPrompt", "onAuthenticationError $errorCode $errString")
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Log.d("BiometricPrompt", "onAuthenticationFailed")
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                if (isAppLock) {
                    onAuthenticationSucceeded.invoke(packageName, false)
                } else {
                    onAuthenticationSucceeded.invoke(packageName, true)
                }
            }
        }
        return BiometricPrompt(activity, executor, callback)
    }

}
