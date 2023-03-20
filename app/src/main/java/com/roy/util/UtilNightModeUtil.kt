package com.roy.util

import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.NightMode

object UtilNightModeUtil {
    @JvmStatic
    fun getNightModeDisplayName(@NightMode nightMode: Int): String {
        return when (nightMode) {
            AppCompatDelegate.MODE_NIGHT_NO -> "Light"
            AppCompatDelegate.MODE_NIGHT_YES -> "Dark"
            AppCompatDelegate.MODE_NIGHT_AUTO -> "Auto"
            else -> "Follow System"
        }
    }

    @JvmStatic
    @NightMode
    fun getNightModeFromDisplayName(displayName: String?): Int {
        return when (displayName) {
            "Light" -> AppCompatDelegate.MODE_NIGHT_NO
            "Dark" -> AppCompatDelegate.MODE_NIGHT_YES
            "Auto" -> AppCompatDelegate.MODE_NIGHT_AUTO
            else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }
    }
}
