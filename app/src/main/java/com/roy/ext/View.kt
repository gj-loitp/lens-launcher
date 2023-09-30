package com.roy.ext

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.*
import android.graphics.drawable.*
import android.graphics.drawable.shapes.RectShape
import android.os.Build
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout

val View.horizontalPadding: Int get() = this.paddingStart + this.paddingEnd
val View.verticalPadding: Int get() = this.paddingTop + this.paddingBottom

inline fun <reified T : View> T.doOnNextNonNullSizeLayout(crossinline action: (view: T) -> Unit) {
    addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
        override fun onLayoutChange(
            view: View,
            left: Int,
            top: Int,
            right: Int,
            bottom: Int,
            oldLeft: Int,
            oldTop: Int,
            oldRight: Int,
            oldBottom: Int,
        ) {
            if (view.width == 0 || view.height == 0) return
            view.removeOnLayoutChangeListener(this)
            action(view as T)
        }
    })
}

inline fun <reified T : View> T.doOnNonNullSizeLayout(crossinline action: (view: T) -> Unit) {
    if (ViewCompat.isLaidOut(this) && !isLayoutRequested && this.width > 0 && this.height > 0) {
        action(this)
    } else {
        doOnNextNonNullSizeLayout { action(it) }
    }
}

fun View.duplicateViewSizeContinuously(
    view: View,
    duplicateWidth: Boolean = true,
    duplicateHeight: Boolean = true,
    overrideMinimumWidth: Boolean = true,
    overrideMinimumHeight: Boolean = true,
    ignoreNullOrInvisibleValues: Boolean = true,
    fallbackLayoutParamWidth: Int = ViewGroup.LayoutParams.WRAP_CONTENT,
    fallbackLayoutParamHeight: Int = ViewGroup.LayoutParams.WRAP_CONTENT,
    transformWidth: Int.() -> Int = { this },
    transformHeight: Int.() -> Int = { this },
) {
    view.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
        val shouldFallbackToWidth =
            ignoreNullOrInvisibleValues && (!view.isVisible || view.width == 0)
        val shouldFallbackToHeight =
            ignoreNullOrInvisibleValues && (!view.isVisible || view.height == 0)
        updateLayoutParams<ViewGroup.LayoutParams> {
            if (duplicateWidth) width = when {
                shouldFallbackToWidth -> fallbackLayoutParamWidth
                overrideMinimumWidth -> transformWidth(view.width)
                else -> transformWidth(view.width.coerceAtLeast(minimumWidth))
            }
            if (duplicateHeight) height = when {
                shouldFallbackToHeight -> fallbackLayoutParamHeight
                overrideMinimumHeight -> transformHeight(view.height)
                else -> transformHeight(view.height.coerceAtLeast(minimumHeight))
            }
        }
    }
}

fun Drawable.toBitmap(): Bitmap {
    if (this is BitmapDrawable) {
        return bitmap
    }

    val width = if (bounds.isEmpty) intrinsicWidth else bounds.width()
    val height = if (bounds.isEmpty) intrinsicHeight else bounds.height()

    return Bitmap.createBitmap(width.nonZero(), height.nonZero(), Bitmap.Config.ARGB_8888).also {
        val canvas = Canvas(it)
        setBounds(0, 0, canvas.width, canvas.height)
        draw(canvas)
    }
}

private fun Int.nonZero() = if (this <= 0) 1 else this

//get mau background current cua view
fun View.getBackgroundColor() = (background as? ColorDrawable?)?.color ?: Color.TRANSPARENT

fun View.setBackgroundTintList(color: Int) {
    this.backgroundTintList = ColorStateList.valueOf(color)
}

//This snippet hides the system bars.
fun View.hideSystemUI() {
    // Set the IMMERSIVE flag.
    // Set the content to appear under the system bars so that the content
    // doesn't resize when the system bars hide and show.
    this.systemUiVisibility = (
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                    or View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                    or View.SYSTEM_UI_FLAG_IMMERSIVE
            )
}

// This snippet shows the system bars. It does this by removing all the flags
// except for the ones that make the content appear under the system bars.
fun View.showSystemUI() {
    this.systemUiVisibility = (
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            )
}

@Suppress("unused")
fun TabLayout?.fixSizeTabLayout(
    titleList: Array<String>,
) {
    if (titleList.size > 3) {
        this?.tabMode = TabLayout.MODE_SCROLLABLE
    } else {
        this?.apply {
            tabMode = TabLayout.MODE_FIXED
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
        }
    }
}

fun View?.setMargins(
    leftPx: Int,
    topPx: Int,
    rightPx: Int,
    bottomPx: Int,
) {
    this?.let {
        if (it.layoutParams is ViewGroup.MarginLayoutParams) {
            val p = it.layoutParams as ViewGroup.MarginLayoutParams
            p.setMargins(leftPx, topPx, rightPx, bottomPx)
            it.requestLayout()
        }
    }
}

fun View.getAllChildren(): ArrayList<View> {
    if (this !is ViewGroup) {
        val viewArrayList = ArrayList<View>()
        viewArrayList.add(this)
        return viewArrayList
    }
    val result = ArrayList<View>()
    for (i in 0 until this.childCount) {
        val child = this.getChildAt(i)
        val viewArrayList = ArrayList<View>()
        viewArrayList.add(this)
        viewArrayList.addAll(child.getAllChildren())
        result.addAll(viewArrayList)
    }
    return result
}

fun View.getWidthOfView(): Int {
    this.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
    return this.measuredWidth
}

fun View.getHeightOfView(): Int {
    this.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
    return this.measuredHeight
}

fun View?.setSizeOfView(
    width: Int? = null,
    height: Int? = null,
) {
    this?.let { v ->
        width?.let {
            v.layoutParams.width = width
        }
        height?.let {
            v.layoutParams.height = height
        }
        v.requestLayout()
    }
}

@SuppressLint("ObsoleteSdkInt")
fun View.setRipple() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
        val outValue = TypedValue()
        this.context.theme.resolveAttribute(
            /* resid = */ android.R.attr.selectableItemBackground,
            /* outValue = */ outValue,
            /* resolveRefs = */ true
        )
        this.setBackgroundResource(outValue.resourceId)
    }
}

fun Snackbar.withBackground(resId: Int): Snackbar {
    this.view.setBackgroundResource(resId)
    return this
}

@Suppress("unused")
fun View?.hideSoftInput(
) {
    this?.let {
        val imm = it.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(this.windowToken, 0)
    }
}