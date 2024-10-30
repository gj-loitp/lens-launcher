package com.mckimquyen.util

import android.graphics.Bitmap
import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.palette.graphics.Palette
import com.mckimquyen.model.App

object UtilColor {
    @ColorInt
    fun getPaletteColorFromApp(app: App): Int {
        return getPaletteColorFromBitmap(app.icon)
    }

    @JvmStatic
    @ColorInt
    fun getPaletteColorFromBitmap(bitmap: Bitmap?): Int {
        try {
            if (bitmap == null) {
                return Color.BLACK
            }
            val palette: Palette = try {
                Palette.from(bitmap).generate()
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
                return Color.BLACK
            }
            return if (palette.swatches.size > 0) {
                var swatchIndex = 0
                for (i in 1 until palette.swatches.size) {
                    if (palette.swatches[i].population > palette.swatches[swatchIndex].population) {
                        swatchIndex = i
                    }
                }
                palette.swatches[swatchIndex].rgb
            } else {
                Color.BLACK
            }
        } catch (e: Exception) {
            return Color.BLACK
        }
    }

    @JvmStatic
    fun getHueColorFromApp(app: App): Float {
        return getHueColorFromColor(app.paletteColor)
    }

    @JvmStatic
    fun getHueColorFromColor(@ColorInt color: Int): Float {
        val hsvValues = FloatArray(3)
        Color.colorToHSV(
            /* color = */ color,
            /* hsv = */ hsvValues
        )
        return hsvValues[0]
    }
}
