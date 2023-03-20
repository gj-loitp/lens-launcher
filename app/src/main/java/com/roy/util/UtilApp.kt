package com.roy.util

import android.app.Application
import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import com.roy.R
import com.roy.model.App
import com.roy.model.AppPersistent
import com.roy.sv.BroadcastReceivers.AppsEditedReceiver
import com.roy.util.UtilAppSorter.SortType
import com.roy.util.UtilIconPackManager.IconPack
import java.util.*

object UtilApp {
    // Get all available apps for launcher
    @JvmStatic
    fun getApps(
        packageManager: PackageManager,
        context: Context?,
        application: Application?,
        iconPackLabelName: String,
        sortType: SortType?
    ): ArrayList<App> {
        val apps = ArrayList<App>()
        val intent = Intent(Intent.ACTION_MAIN, null)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        var availableActivities: List<ResolveInfo>? = null
        try {
            availableActivities = packageManager.queryIntentActivities(intent, 0)
        } catch (e: RuntimeException) {
            e.printStackTrace()
            Toast.makeText(
                /* context = */ context,
                /* resId = */ R.string.error_too_many_apps,
                /* duration = */ Toast.LENGTH_SHORT
            ).show()
        }
        if (availableActivities != null) {
            var selectedIconPack: IconPack? = null
            val iconPacks = UtilIconPackManager().getAvailableIconPacksWithIcons(
                /* forceReload = */ true,
                /* application = */ application
            )
            for (iconPack in iconPacks) {
                if (iconPack.mName == iconPackLabelName) {
                    selectedIconPack = iconPack
                }
            }
            for (i in availableActivities.indices) {
                val resolveInfo = availableActivities[i]
                val app = App()
                app.id = i
                try {
                    app.installDate = packageManager.getPackageInfo(
                        /* p0 = */ resolveInfo.activityInfo.packageName,
                        /* p1 = */ 0
                    ).firstInstallTime
                } catch (e: PackageManager.NameNotFoundException) {
                    app.installDate = 0
                }
                app.label = resolveInfo.loadLabel(packageManager)
                app.packageName = resolveInfo.activityInfo.packageName
                app.name = resolveInfo.activityInfo.name
                app.iconResId = resolveInfo.activityInfo.iconResource
                val defaultBitmap = UtilBitmap.packageNameToBitmap(
                    /* packageManager = */ packageManager,
                    /* packageName = */ resolveInfo.activityInfo.packageName,
                    /* resId = */ resolveInfo.activityInfo.iconResource
                )
                if (selectedIconPack != null) app.icon = selectedIconPack.getIconForPackage(
                    Objects.requireNonNull(app.packageName).toString(), defaultBitmap
                ) else app.icon = defaultBitmap
                app.paletteColor = UtilColor.getPaletteColorFromApp(app)
                apps.add(app)
            }
        }
        UtilAppSorter.sort(/* apps = */ apps, /* sortType = */ sortType)
        return apps
    }

    // Launch apps, for launcher :-P
    @JvmStatic
    fun launchComponent(
        context: Context, packageName: String?, name: String?, view: View?, bounds: Rect?
    ) {
        if (packageName != null && name != null) {
            val componentIntent = Intent()
            componentIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            componentIntent.component = ComponentName(/* pkg = */ packageName, /* cls = */ name)
            if (packageName != "com.roy93group.lenslauncher") {
                componentIntent.action = Intent.ACTION_MAIN
            }
            componentIntent.addCategory(Intent.CATEGORY_LAUNCHER)
            try {
                // Launch Component
                ContextCompat.startActivity(
                    /* context = */ context,
                    /* intent = */ componentIntent,
                    /* options = */ getLauncherOptionsBundle(
                        context = context, source = view, bounds = bounds
                    )
                )
                // Increment app open count
                AppPersistent.incrementAppCount(packageName, name)
                // Resort apps (if open count selected)
                val utilSettings = UtilSettings(context)
                if (utilSettings.sortType == SortType.OPEN_COUNT_ASCENDING || utilSettings.sortType == SortType.OPEN_COUNT_DESCENDING) {
                    val editAppsIntent = Intent(context, AppsEditedReceiver::class.java)
                    context.sendBroadcast(editAppsIntent)
                }
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(
                    /* context = */ context,
                    /* resId = */ R.string.error_app_not_found,
                    /* duration = */ Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            Toast.makeText(
                /* context = */ context,
                /* resId = */R.string.error_app_not_found,
                /* duration = */Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun getLauncherOptionsBundle(
        context: Context, source: View?, bounds: Rect?
    ): Bundle? {
        var optionsBundle: Bundle? = null
        if (source != null) {
            val options: ActivityOptionsCompat =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && bounds != null) {
                    // Clip reveal animation for Marshmallow and above
                    ActivityOptionsCompat.makeClipRevealAnimation(
                        /* source = */ source,
                        /* startX = */ bounds.left,
                        /* startY = */ bounds.top,
                        /* width = */ bounds.width(),
                        /* height = */ bounds.height()
                    )
                } else {
                    // Fade animation otherwise
                    ActivityOptionsCompat.makeCustomAnimation(
                        /* context = */ context,
                        /* enterResId = */ R.anim.a_fade_in,
                        /* exitResId = */ R.anim.a_fade_out
                    )
                }
            optionsBundle = options.toBundle()
        }
        return optionsBundle
    }
}
