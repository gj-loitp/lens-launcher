package com.mckimquyen.itf

import com.mckimquyen.model.App

interface AppsInterface {
    fun onDefaultsReset()

    fun onAppsUpdated(apps: ArrayList<App>?)
}
