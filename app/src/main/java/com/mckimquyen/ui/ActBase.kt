package com.mckimquyen.ui

import android.app.ActivityManager.TaskDescription
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.mckimquyen.R
import com.mckimquyen.util.UtilSettings

open class ActBase : BaseActivity() {
    @JvmField
    protected var utilSettings: UtilSettings? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        utilSettings = UtilSettings(this)
        if (savedInstanceState == null) {
            updateNightMode()
        }
        super.onCreate(savedInstanceState)
    }


    override fun setContentView(@LayoutRes layoutResID: Int) {
        super.setContentView(layoutResID)
        setTaskDescription()
    }

    private fun setTaskDescription() {
        val appIconBitmap = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)
        val taskDescription = TaskDescription(
            /* label = */ getString(/* resId = */ R.string.app_name),
            /* icon = */ appIconBitmap,
            /* colorPrimary = */ ContextCompat.getColor(baseContext, R.color.colorPrimaryDark)
        )
        setTaskDescription(taskDescription)
    }

    protected fun updateNightMode() {
        if (utilSettings == null) {
            utilSettings = UtilSettings(this)
        }
        utilSettings?.let {
            delegate.localNightMode = it.nightMode
        }
    }
}