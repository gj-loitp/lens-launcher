package com.roy.app

import com.orm.SugarApp
import com.roy.background.EditedObservable
import com.roy.background.SortAppsTask
import com.roy.background.UpdateAppsTask
import com.roy.background.UpdatedObservable
import java.util.*

class MyApplication : SugarApp(), Observer {

    override fun onCreate() {
        super.onCreate()

        UpdatedObservable.getInstance().addObserver(this)
        EditedObservable.getInstance().addObserver(this)
        updateApps()
    }

    override fun update(
        observable: Observable, data: Any
    ) {
        if (observable is UpdatedObservable) {
            updateApps()
        } else if (observable is EditedObservable) {
            editApps()
        }
    }

    private fun updateApps() {
        UpdateAppsTask(
            /* packageManager = */ packageManager,
            /* context = */ applicationContext,
            /* application = */ this
        ).execute()
    }

    private fun editApps() {
        SortAppsTask(
            /* context = */ applicationContext,
            /* application = */ this
        ).execute()
    }
}
