package com.mckimquyen.util

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import com.mckimquyen.ui.AFakeLauncher

object UtilLauncher {
    @JvmStatic
    fun isDefaultLauncher(application: Application): Boolean {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        val res = application.packageManager.resolveActivity(intent, 0)
        return if (res?.activityInfo == null) {
            false
        } else if ("android" == res.activityInfo.packageName) {
            false
        } else {
            res.activityInfo.packageName == application.packageName
        }
    }

    fun getNameHomeLauncher(application: Application): String {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        val res = application.packageManager.resolveActivity(intent, 0)
        return if (res?.activityInfo == null) {
            ""
        } else res.activityInfo.loadLabel(application.packageManager).toString()
    }

    @JvmStatic
    fun resetPreferredLauncherAndOpenChooser(context: Context) {
        val packageManager = context.packageManager
        val componentName = ComponentName(context, AFakeLauncher::class.java)
        packageManager.setComponentEnabledSetting(
            /* componentName = */ componentName,
            /* newState = */ PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            /* flags = */ PackageManager.DONT_KILL_APP
        )
        val selector = Intent(Intent.ACTION_MAIN)
        selector.addCategory(Intent.CATEGORY_HOME)
        selector.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(selector)
        packageManager.setComponentEnabledSetting(
            /* componentName = */ componentName,
            /* newState = */ PackageManager.COMPONENT_ENABLED_STATE_DEFAULT,
            /* flags = */ PackageManager.DONT_KILL_APP
        )
    }
}
