package com.roy.ext

import android.content.Context
import androidx.biometric.BiometricManager

fun Context.isHaveBiometric(): Boolean {
    val biometricManager = BiometricManager.from(this)
    return biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS
}
