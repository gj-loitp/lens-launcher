package com.roy.app

import android.graphics.Bitmap
import com.roy.model.App

class AppsSingleton private constructor() {
    private var mApps: ArrayList<App>? = null
    private var mAppIcons: ArrayList<Bitmap>? = null

    var apps: ArrayList<App>?
        get() {
            val apps = ArrayList<App>()
            if (mApps == null) {
                return apps
            }
            mApps?.let {
                apps.addAll(it)
            }
            return apps
        }
        set(apps) {
            mApps = apps
        }

    var appIcons: ArrayList<Bitmap>?
        get() {
            val appIcons = ArrayList<Bitmap>()
            if (mAppIcons == null) {
                return appIcons
            }
            mAppIcons?.let {
                appIcons.addAll(it)
            }
            return appIcons
        }
        set(appIcons) {
            mAppIcons = appIcons
        }

    companion object {
        private var mAppsSingleton: AppsSingleton? = null

        @JvmStatic
        val instance: AppsSingleton?
            get() {
                if (mAppsSingleton == null) {
                    mAppsSingleton = AppsSingleton()
                }
                return mAppsSingleton
            }
    }
}
