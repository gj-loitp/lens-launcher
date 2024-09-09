package com.mckimquyen.util

import android.content.pm.PackageManager
import android.content.res.Resources
import android.content.res.Resources.NotFoundException
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable

object UtilBitmap {
    // Old method - does not allow for adding options i.e. lower quality
    // Source: http://stackoverflow.com/questions/3035692/how-to-convert-a-drawable-to-a-bitmap
    private fun drawableToBitmap(drawable: Drawable): Bitmap {
        if (drawable is BitmapDrawable) {
            drawable.setAntiAlias(true)
            drawable.setDither(true)
            drawable.setTargetDensity(Int.MAX_VALUE)
            if (drawable.bitmap != null) {
                return drawable.bitmap
            }
        }
        val bitmap: Bitmap = if (drawable.intrinsicWidth <= 0 || drawable.intrinsicHeight <= 0) {
            Bitmap.createBitmap(
                /* width = */ 1,
                /* height = */ 1,
                /* config = */ Bitmap.Config.ARGB_8888
            ) // Single color bitmap will be created of 1x1 pixel
        } else {
            Bitmap.createBitmap(
                /* width = */ drawable.intrinsicWidth,
                /* height = */ drawable.intrinsicHeight,
                /* config = */ Bitmap.Config.ARGB_8888
            )
        }
        val canvas = Canvas(bitmap)
        drawable.setBounds(
            /* left = */ 0,
            /* top = */ 0,
            /* right = */ canvas.width,
            /* bottom = */ canvas.height
        )
        drawable.draw(canvas)
        return bitmap
    }

    // Get bitmap from resource id, with quality options
    private fun resIdToBitmap(
        res: Resources?,
        resId: Int
    ): Bitmap? {
        val options = BitmapFactory.Options()
        options.inScaled = false
        options.inDither = false
        options.inPreferredConfig = Bitmap.Config.ARGB_8888
        options.inPreferQualityOverSpeed = true
        return try {
            BitmapFactory.decodeResource(
                /* res = */ res,
                /* id = */ resId,
                /* opts = */ options
            )
        } catch (e1: OutOfMemoryError) {
            e1.printStackTrace()
            null
        }
    }

    // Get res id from app package name
    fun packageNameToResId(
        packageManager: PackageManager,
        packageName: String
    ): Int {
        return try {
            val applicationInfo = packageManager.getApplicationInfo(
                /* p0 = */ packageName,
                /* p1 = */ PackageManager.GET_META_DATA
            )
            applicationInfo.icon
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            -1
        }
    }

    // Get bitmap from app package name
    fun packageNameToBitmap(
        packageManager: PackageManager,
        packageName: String
    ): Bitmap? {
        return try {
            val applicationInfo =
                packageManager.getApplicationInfo(
                    /* p0 = */ packageName,
                    /* p1 = */ PackageManager.GET_META_DATA
                )
            val resources = packageManager.getResourcesForApplication(applicationInfo)
            val appIconResId = applicationInfo.icon
            resIdToBitmap(resources, appIconResId)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            null
        }
    }

    // Get bitmap from app package name (with res id)
    fun packageNameToBitmap(
        packageManager: PackageManager,
        packageName: String,
        resId: Int
    ): Bitmap? {
        return try {
            val applicationInfo = packageManager.getApplicationInfo(
                /* p0 = */ packageName,
                /* p1 = */ PackageManager.GET_META_DATA
            )
            val resources = packageManager.getResourcesForApplication(applicationInfo)
            var bitmap = resIdToBitmap(
                res = resources,
                resId = resId
            )
            if (bitmap == null) {
                val drawable = packageManager.getApplicationIcon(
                    /* p0 = */ packageName
                )
                bitmap = drawableToBitmap(drawable)
            }
            bitmap
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            null
        } catch (e: NotFoundException) {
            e.printStackTrace()
            null
        }
    }
}
