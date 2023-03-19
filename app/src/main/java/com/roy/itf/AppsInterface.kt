package com.roy.itf

import com.roy.model.App

interface AppsInterface {
    fun onDefaultsReset()

    fun onAppsUpdated(apps: ArrayList<App?>?)
}
