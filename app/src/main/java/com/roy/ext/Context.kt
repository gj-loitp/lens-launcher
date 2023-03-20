package com.roy.ext

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.view.*
import androidx.core.content.ContextCompat
import com.roy.R
import com.roy.util.URL_POLICY_NOTION

//check xem app hien tai co phai la default launcher hay khong
fun Context.isDefaultLauncher(): Boolean {
    val intent = Intent(Intent.ACTION_MAIN)
    intent.addCategory(Intent.CATEGORY_HOME)
    val resolveInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        packageManager.resolveActivity(
            intent,
            PackageManager.ResolveInfoFlags.of(PackageManager.MATCH_DEFAULT_ONLY.toLong())
        )
    } else {
        @Suppress("DEPRECATION")
        packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)
    }

    val currentLauncherName = resolveInfo?.activityInfo?.packageName
    if (currentLauncherName == packageName) {
        return true
    }
    return false
}

//mo app setting default cua device
fun Context.launchSystemSetting(
    packageName: String
) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    intent.data = Uri.parse("package:$packageName")
    this.startActivity(intent)
}

/*
         * send email support
         */
fun Context?.sendEmail(
) {
    val emailIntent = Intent(Intent.ACTION_SENDTO)
    emailIntent.data = Uri.parse("mailto: www.muathu@gmail.com")
    this?.startActivity(Intent.createChooser(emailIntent, "Send feedback"))
}

fun Context.openBrowserPolicy(
) {
    this.openUrlInBrowser(url = URL_POLICY_NOTION)
}

fun Context?.openUrlInBrowser(
    url: String?
) {
    if (this == null || url.isNullOrEmpty()) {
        return
    }
    try {
        val defaultBrowser =
            Intent.makeMainSelectorActivity(Intent.ACTION_MAIN, Intent.CATEGORY_APP_BROWSER)
        defaultBrowser.data = Uri.parse(url)
        this.startActivity(defaultBrowser)
    } catch (e: Exception) {
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        this.startActivity(i)
    }
}

fun Context.showDialog1(
    title: String? = null,
    msg: String? = null,
    button1: String = getString(R.string.confirm),
    onClickButton1: Runnable? = null
): AlertDialog {
    val builder = AlertDialog.Builder(ContextThemeWrapper(this, R.style.LightAlertDialogCustom))

    if (title.isNullOrEmpty()) {
        // do nothing
    } else {
        builder.setTitle(title)
    }
    if (msg.isNullOrEmpty()) {
        // do nothing
    } else {
        builder.setMessage(msg)
    }

    builder.setPositiveButton(button1) { _, _ ->
        onClickButton1?.run()
    }
    val dialog = builder.create()
    dialog.setCancelable(true)
    dialog.show()
    val color = ContextCompat.getColor(this, R.color.colorPrimary)
    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(color)
    return dialog
}

