package com.roy.bkg

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BroadcastReceivers {
    class AppsUpdatedReceiver : BroadcastReceiver() {
        override fun onReceive(
            context: Context,
            intent: Intent
        ) {
            UpdatedObservable.getInstance().update()
        }
    }

    class AppsEditedReceiver : BroadcastReceiver() {
        override fun onReceive(
            context: Context,
            intent: Intent
        ) {
            EditedObservable.getInstance().update()
        }
    }

    class AppsVisibilityChangedReceiver : BroadcastReceiver() {
        override fun onReceive(
            context: Context,
            intent: Intent
        ) {
            VisibilityChangedObservable.getInstance().update()
        }
    }

    class AppsLoadedReceiver : BroadcastReceiver() {
        override fun onReceive(
            context: Context,
            intent: Intent
        ) {
            LoadedObservable.getInstance().update()
        }
    }

    class BackgroundChangedReceiver : BroadcastReceiver() {
        override fun onReceive(
            context: Context,
            intent: Intent
        ) {
            BackgroundChangedObservable.getInstance().update()
        }
    }

    class NightModeReceiver : BroadcastReceiver() {
        override fun onReceive(
            context: Context,
            intent: Intent
        ) {
            NightModeObservable.getInstance().update()
        }
    }
}
