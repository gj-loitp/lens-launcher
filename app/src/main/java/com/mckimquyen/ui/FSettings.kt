package com.mckimquyen.ui

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.mckimquyen.R
import com.mckimquyen.ext.searchIconPack
import com.mckimquyen.itf.SettingsInterface
import com.mckimquyen.util.UtilLauncher
import com.mckimquyen.util.UtilNightModeUtil
import com.mckimquyen.util.UtilSettings

class FSettings : Fragment(), SettingsInterface {
    companion object {
        fun newInstance(): FSettings {
            return FSettings()
        }
    }

    private var tvSelectedHomeLauncher: TextView? = null
    private var tvSelectedIconPack: TextView? = null
    private var tvSelectedNightMode: TextView? = null
    private var tvSelectedBackground: TextView? = null
    private var ivSelectedBackgroundColor: ImageView? = null
    private var tvSelectedHighlightColor: TextView? = null
    private var ivSelectedHighlightColor: ImageView? = null
    private var swVibrateAppHover: SwitchCompat? = null
    private var swVibrateAppLaunch: SwitchCompat? = null
    private var swShowNameAppHover: SwitchCompat? = null
    private var swShowNewAppTag: SwitchCompat? = null
    private var swShowTouchSelection: SwitchCompat? = null
    private var utilSettings: UtilSettings? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.f_settings, container, false)
        utilSettings = UtilSettings(requireContext())
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews(view)
        assignValues()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (activity != null && activity is ASettings) {
            (activity as ASettings?)?.setSettingsInterface(this)
        }
    }

    private fun setupViews(view: View) {
        tvSelectedHomeLauncher = view.findViewById(R.id.tvSelectedHomeLauncher)
        tvSelectedIconPack = view.findViewById(R.id.tvSelectedIconPack)
        tvSelectedNightMode = view.findViewById(R.id.tvSelectedNightMode)
        tvSelectedBackground = view.findViewById(R.id.tvSelectedBackground)
        ivSelectedBackgroundColor = view.findViewById(R.id.ivSelectedBackgroundColor)
        tvSelectedHighlightColor = view.findViewById(R.id.tvSelectedHighlightColor)
        ivSelectedHighlightColor = view.findViewById(R.id.ivSelectedHighlightColor)
        swVibrateAppHover = view.findViewById(R.id.swVibrateAppHover)
        swVibrateAppLaunch = view.findViewById(R.id.swVibrateAppLaunch)
        swShowNameAppHover = view.findViewById(R.id.swShowNameAppHover)
        swShowNewAppTag = view.findViewById(R.id.swShowNewAppTag)
        swShowTouchSelection = view.findViewById(R.id.swShowTouchSelection)

        view.findViewById<View>(R.id.llHomeLauncher).setOnClickListener {
            showHomeLauncherChooser()
        }
        view.findViewById<View>(R.id.llIconPack).setOnClickListener {
            showIconPackDialog()
        }
        view.findViewById<View>(R.id.btGetMoreIconPacks).setOnClickListener {
            activity?.searchIconPack()
        }
        view.findViewById<View>(R.id.llNightMode).setOnClickListener {
            showNightModeChooser()
        }
        view.findViewById<View>(R.id.llBackground).setOnClickListener {
            showBackgroundDialog()
        }
        view.findViewById<View>(R.id.llHighlightColor).setOnClickListener {
            showHighlightColorDialog()
        }
        view.findViewById<View>(R.id.rlSwitchVibrateAppHoverParent).setOnClickListener {
            //do nothing
        }
        view.findViewById<View>(R.id.rlSwitchVibrateAppLaunchParent).setOnClickListener {
            //do nothing
        }
        view.findViewById<View>(R.id.rlSwitchShowNameAppHoverParent).setOnClickListener {
            //do nothing
        }
        view.findViewById<View>(R.id.swShowNewAppTagParent).setOnClickListener {
            //do nothing
        }
        view.findViewById<View>(R.id.rlSwitchShowTouchSelectionParent).setOnClickListener {
            //do nothing
        }

        swVibrateAppHover?.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            utilSettings?.save(
                /* name = */ UtilSettings.KEY_VIBRATE_APP_HOVER,
                /* value = */ isChecked
            )
        }
        swVibrateAppLaunch?.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            utilSettings?.save(
                /* name = */ UtilSettings.KEY_VIBRATE_APP_LAUNCH,
                /* value = */ isChecked
            )
        }
        swShowNameAppHover?.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            utilSettings?.save(
                /* name = */ UtilSettings.KEY_SHOW_NAME_APP_HOVER,
                /* value = */ isChecked
            )
        }
        swShowNewAppTag?.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            utilSettings?.save(
                /* name = */ UtilSettings.KEY_SHOW_NEW_APP_TAG,
                /* value = */ isChecked
            )
        }
        swShowTouchSelection?.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            utilSettings?.save(
                /* name = */ UtilSettings.KEY_SHOW_TOUCH_SELECTION,
                /* value = */ isChecked
            )
        }
    }

    private fun assignValues() {
        utilSettings?.let { us ->
            tvSelectedIconPack?.text = us.getString(UtilSettings.KEY_ICON_PACK_LABEL_NAME)
            val highlightColor = "#" + us.getString(UtilSettings.KEY_HIGHLIGHT_COLOR)?.substring(3)
            var homeLauncher: String? = ""
            activity?.let {
                homeLauncher = UtilLauncher.getNameHomeLauncher(it.application)
            }
            tvSelectedHomeLauncher?.text = homeLauncher
            tvSelectedNightMode?.text = UtilNightModeUtil.getNightModeDisplayName(us.nightMode)
            if (us.getString(UtilSettings.KEY_BACKGROUND) == "Color") {
                val backgroundColor =
                    "#" + us.getString(UtilSettings.KEY_BACKGROUND_COLOR)?.substring(3)
                tvSelectedBackground?.text = backgroundColor
                ivSelectedBackgroundColor?.isVisible = true
                val backgroundColorDrawable = GradientDrawable()
                backgroundColorDrawable.setColor(
                    Color.parseColor(
                        us.getString(
                            UtilSettings.KEY_BACKGROUND_COLOR
                        )
                    )
                )
                backgroundColorDrawable.cornerRadius =
                    resources.getDimension(R.dimen.radius_highlight_color_switch)
                ivSelectedBackgroundColor?.setImageDrawable(backgroundColorDrawable)
            } else {
                tvSelectedBackground?.text = us.getString(UtilSettings.KEY_BACKGROUND)
                ivSelectedBackgroundColor?.isVisible = false
            }
            tvSelectedHighlightColor?.text = highlightColor
            val highlightColorDrawable = GradientDrawable()
            highlightColorDrawable.setColor(Color.parseColor(us.getString(UtilSettings.KEY_HIGHLIGHT_COLOR)))
            highlightColorDrawable.cornerRadius =
                resources.getDimension(R.dimen.radius_highlight_color_switch)
            ivSelectedHighlightColor?.setImageDrawable(highlightColorDrawable)
            swVibrateAppHover?.isChecked = us.getBoolean(UtilSettings.KEY_VIBRATE_APP_HOVER)
            swVibrateAppLaunch?.isChecked = us.getBoolean(UtilSettings.KEY_VIBRATE_APP_LAUNCH)
            swShowNameAppHover?.isChecked = us.getBoolean(UtilSettings.KEY_SHOW_NAME_APP_HOVER)
            swShowNewAppTag?.isChecked = us.getBoolean(UtilSettings.KEY_SHOW_NEW_APP_TAG)
            swShowTouchSelection?.isChecked = us.getBoolean(UtilSettings.KEY_SHOW_TOUCH_SELECTION)
        }
    }

    private fun showIconPackDialog() {
        if (activity != null && activity is ASettings) {
            (activity as ASettings?)?.showIconPackDialog()
        }
    }

    private fun showHomeLauncherChooser() {
        if (activity != null && activity is ASettings) {
            (activity as ASettings?)?.showHomeLauncherChooser()
        }
    }

    private fun showNightModeChooser() {
        if (activity != null && activity is ASettings) {
            (activity as ASettings?)?.showNightModeChooser()
        }
    }

    private fun showBackgroundDialog() {
        if (activity != null && activity is ASettings) {
            (activity as ASettings?)?.showBackgroundDialog()
        }
    }

    private fun showHighlightColorDialog() {
        if (activity != null && activity is ASettings) {
            (activity as ASettings?)?.showHighlightColorDialog()
        }
    }

    override fun onDefaultsReset() {
        resetToDefault()
        assignValues()
    }

    override fun onValuesUpdated() {
        assignValues()
    }

    private fun resetToDefault() {
        utilSettings?.let { us ->
            us.save(
                /* name = */ UtilSettings.KEY_VIBRATE_APP_HOVER,
                /* value = */ UtilSettings.DEFAULT_VIBRATE_APP_HOVER
            )
            us.save(
                /* name = */ UtilSettings.KEY_VIBRATE_APP_LAUNCH,
                /* value = */ UtilSettings.DEFAULT_VIBRATE_APP_LAUNCH
            )
            us.save(
                /* name = */ UtilSettings.KEY_SHOW_NAME_APP_HOVER,
                /* value = */ UtilSettings.DEFAULT_SHOW_NAME_APP_HOVER
            )
            us.save(
                /* name = */ UtilSettings.KEY_SHOW_TOUCH_SELECTION,
                /* value = */ UtilSettings.DEFAULT_SHOW_TOUCH_SELECTION
            )
            us.save(
                /* name = */ UtilSettings.KEY_SHOW_NEW_APP_TAG,
                /* value = */ UtilSettings.DEFAULT_SHOW_NEW_APP_TAG
            )
            us.save(
                /* name = */ UtilSettings.KEY_BACKGROUND,
                /* value = */ UtilSettings.DEFAULT_BACKGROUND
            )
            us.save(
                /* name = */ UtilSettings.KEY_BACKGROUND_COLOR,
                /* value = */ UtilSettings.DEFAULT_BACKGROUND_COLOR
            )
            us.save(
                /* name = */ UtilSettings.KEY_HIGHLIGHT_COLOR,
                /* value = */ UtilSettings.DEFAULT_HIGHLIGHT_COLOR
            )
            us.save(
                /* name = */ UtilSettings.KEY_ICON_PACK_LABEL_NAME,
                /* value = */ UtilSettings.DEFAULT_ICON_PACK_LABEL_NAME
            )
            us.save(
                /* name = */ UtilSettings.KEY_NIGHT_MODE,
                /* value = */ UtilSettings.DEFAULT_NIGHT_MODE
            )
        }
        if (activity != null && activity is ASettings) {
            (activity as ASettings?)?.sendNightModeBroadcast()
        }
    }
}
