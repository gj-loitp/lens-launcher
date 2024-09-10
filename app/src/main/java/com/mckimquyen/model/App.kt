package com.mckimquyen.model

import android.graphics.Bitmap
import androidx.annotation.ColorInt
import androidx.annotation.Keep

@Keep
class App {
    var id = 0
    var label: CharSequence? = null
    var packageName: CharSequence? = null
    var name: CharSequence? = null
    var iconResId = 0
    var icon: Bitmap? = null
    var installDate: Long = 0

    @ColorInt
    var paletteColor = 0
    override fun toString(): String {
        return "App{" +
                "mId=" + id +
                ", mLabel=" + label +
                ", mPackageName=" + packageName +
                ", mName=" + name +
                ", mIconResId=" + iconResId +
                ", mIcon=" + icon +
                ", mInstallDate=" + installDate +
                ", mPaletteColor=" + paletteColor +
                '}'
    }
}
