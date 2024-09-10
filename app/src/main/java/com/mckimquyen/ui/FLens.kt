package com.mckimquyen.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import androidx.appcompat.widget.AppCompatSeekBar
import androidx.fragment.app.Fragment
import com.mckimquyen.R
import com.mckimquyen.enums.DrawType
import com.mckimquyen.itf.LensInterface
import com.mckimquyen.util.UtilSettings
import com.mckimquyen.views.LensView

class FLens : Fragment(), LensInterface {
    companion object {
        fun newInstance(): FLens {
            return FLens()
        }
    }

    @JvmField
    var lensViewsSettings: LensView? = null

    @JvmField
    var sbMinIconSize: AppCompatSeekBar? = null

    @JvmField
    var tvValueMinIconSize: TextView? = null

    @JvmField
    var sbDistortionFactor: AppCompatSeekBar? = null

    @JvmField
    var tvValueDistortionFactor: TextView? = null

    @JvmField
    var sbScaleFactor: AppCompatSeekBar? = null

    @JvmField
    var tvValueScaleFactor: TextView? = null

    @JvmField
    var sbAnimationTime: AppCompatSeekBar? = null

    @JvmField
    var tvValueAnimationTime: TextView? = null
    private var utilSettings: UtilSettings? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.frm_lens, container, false)
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
            (activity as ASettings?)?.setLensInterface(this)
        }
    }

    private fun setupViews(view: View) {
        lensViewsSettings = view.findViewById(R.id.lensViewsSettings)
        sbMinIconSize = view.findViewById(R.id.sbMinIconSize)
        tvValueMinIconSize = view.findViewById(R.id.tvValueMinIconSize)
        sbDistortionFactor = view.findViewById(R.id.sbDistortionFactor)
        tvValueDistortionFactor = view.findViewById(R.id.tvValueDistortionFactor)
        sbScaleFactor = view.findViewById(R.id.sbScaleFactor)
        tvValueScaleFactor = view.findViewById(R.id.tvValueScaleFactor)
        sbAnimationTime = view.findViewById(R.id.sbAnimationTime)
        tvValueAnimationTime = view.findViewById(R.id.tvValueAnimationTime)

        view.findViewById<View>(R.id.sbMinIconSizeParent).setOnClickListener { }
        view.findViewById<View>(R.id.rlSbDistortionFactorParent).setOnClickListener { }
        view.findViewById<View>(R.id.rlSbScaleFactorParent).setOnClickListener { }
        view.findViewById<View>(R.id.rlSbAnimationTimeParent).setOnClickListener { }

        lensViewsSettings?.setDrawType(DrawType.CIRCLES)

        sbMinIconSize?.let { sb ->
            sb.max = UtilSettings.MAX_ICON_SIZE
            sb.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    val appropriateProgress = progress + UtilSettings.MIN_ICON_SIZE.toInt()
                    val minIconSize = appropriateProgress.toString() + "dp"
                    tvValueMinIconSize?.text = minIconSize
                    utilSettings?.save(UtilSettings.KEY_ICON_SIZE, appropriateProgress.toFloat())
                    lensViewsSettings?.invalidate()
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {}
                override fun onStopTrackingTouch(seekBar: SeekBar) {}
            })
        }

        sbDistortionFactor?.let { sb ->
            sb.max = UtilSettings.MAX_DISTORTION_FACTOR
            sb.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    val appropriateProgress =
                        progress.toFloat() / 2.0f + UtilSettings.MIN_DISTORTION_FACTOR
                    val distortionFactor = appropriateProgress.toString() + ""
                    tvValueDistortionFactor?.text = distortionFactor
                    utilSettings?.save(UtilSettings.KEY_DISTORTION_FACTOR, appropriateProgress)
                    lensViewsSettings?.invalidate()
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {}
                override fun onStopTrackingTouch(seekBar: SeekBar) {}
            })
        }

        sbScaleFactor?.let { sb ->
            sb.max = UtilSettings.MAX_SCALE_FACTOR
            sb.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    val appropriateProgress =
                        progress.toFloat() / 5.0f + UtilSettings.MIN_SCALE_FACTOR
                    val scaleFactor = appropriateProgress.toString() + ""
                    tvValueScaleFactor?.text = scaleFactor
                    utilSettings?.save(UtilSettings.KEY_SCALE_FACTOR, appropriateProgress)
                    lensViewsSettings?.invalidate()
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {}
                override fun onStopTrackingTouch(seekBar: SeekBar) {}
            })
        }

        sbAnimationTime?.let { sb ->
            sb.max = UtilSettings.MAX_ANIMATION_TIME
            sb.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    val appropriateProgress =
                        progress.toLong() / 2 + UtilSettings.MIN_ANIMATION_TIME
                    val animationTime = appropriateProgress.toString() + "ms"
                    tvValueAnimationTime?.text = animationTime
                    utilSettings?.save(UtilSettings.KEY_ANIMATION_TIME, appropriateProgress)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {}
                override fun onStopTrackingTouch(seekBar: SeekBar) {}
            })
        }
    }

    private fun assignValues() {
        utilSettings?.let { us ->
            sbMinIconSize?.progress =
                us.getFloat(UtilSettings.KEY_ICON_SIZE).toInt() - UtilSettings.MIN_ICON_SIZE.toInt()
            val minIconSize =
                us.getFloat(UtilSettings.KEY_ICON_SIZE).toInt().toString() + "dp"
            tvValueMinIconSize?.text = minIconSize
            sbDistortionFactor?.progress =
                (2.0f * (us.getFloat(UtilSettings.KEY_DISTORTION_FACTOR) - UtilSettings.MIN_DISTORTION_FACTOR)).toInt()

            val distortionFactor =
                us.getFloat(UtilSettings.KEY_DISTORTION_FACTOR).toString() + ""
            tvValueDistortionFactor?.text = distortionFactor
            sbScaleFactor?.progress =
                (5.0f * (us.getFloat(UtilSettings.KEY_SCALE_FACTOR) - UtilSettings.MIN_SCALE_FACTOR)).toInt()

            val scaleFactor = us.getFloat(UtilSettings.KEY_SCALE_FACTOR).toString() + ""
            tvValueScaleFactor?.text = scaleFactor
            sbAnimationTime?.progress =
                (2 * (us.getLong(UtilSettings.KEY_ANIMATION_TIME) - UtilSettings.MIN_ANIMATION_TIME)).toInt()
            val animationTime =
                us.getLong(UtilSettings.KEY_ANIMATION_TIME).toString() + "ms"
            tvValueAnimationTime?.text = animationTime
        }
    }

    override fun onDefaultsReset() {
        resetToDefault()
        assignValues()
    }

    private fun resetToDefault() {
        utilSettings?.let { us ->
            us.save(
                /* name = */ UtilSettings.KEY_ICON_SIZE,
                /* value = */ UtilSettings.DEFAULT_ICON_SIZE
            )
            us.save(
                /* name = */ UtilSettings.KEY_DISTORTION_FACTOR,
                /* value = */ UtilSettings.DEFAULT_DISTORTION_FACTOR
            )
            us.save(
                /* name = */ UtilSettings.KEY_SCALE_FACTOR,
                /* value = */ UtilSettings.DEFAULT_SCALE_FACTOR
            )
            us.save(
                /* name = */ UtilSettings.KEY_ANIMATION_TIME,
                /* value = */ UtilSettings.DEFAULT_ANIMATION_TIME
            )
        }
    }
}
