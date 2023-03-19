package com.roy.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.fragment.app.Fragment;

import com.roy.R;
import com.roy.util.UtilSettings;
import com.roy.views.LensView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FLens extends Fragment implements ASettings.LensInterface {

    @BindView(R.id.lens_view_settings)
    LensView mLensView;

    @OnClick(R.id.seek_bar_min_icon_size_parent)
    public void onMinIconSizeParentClick() {
    }

    @BindView(R.id.seek_bar_min_icon_size)
    AppCompatSeekBar mMinIconSize;

    @BindView(R.id.value_min_icon_size)
    TextView mValueMinIconSize;

    @OnClick(R.id.seek_bar_distortion_factor_parent)
    public void onDistortionFactorParentClick() {
    }

    @BindView(R.id.seek_bar_distortion_factor)
    AppCompatSeekBar mDistortionFactor;

    @BindView(R.id.value_distortion_factor)
    TextView mValueDistortionFactor;

    @OnClick(R.id.seek_bar_scale_factor_parent)
    public void onScaleFactorParentClick() {
    }

    @BindView(R.id.seek_bar_scale_factor)
    AppCompatSeekBar mScaleFactor;

    @BindView(R.id.value_scale_factor)
    TextView mValueScaleFactor;

    @OnClick(R.id.seek_bar_animation_time_parent)
    public void onAnimationTimeParentClick() {
    }

    @BindView(R.id.seek_bar_animation_time)
    AppCompatSeekBar mAnimationTime;

    @BindView(R.id.value_animation_time)
    TextView mValueAnimationTime;

    private UtilSettings mUtilSettings;

    public FLens() {
    }

    public static FLens newInstance() {
        return new FLens();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lens, container, false);
        ButterKnife.bind(this, view);
        mUtilSettings = new UtilSettings(getActivity());
        setupViews();
        assignValues();
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (getActivity() != null && getActivity() instanceof ASettings) {
            ((ASettings) getActivity()).setLensInterface(this);
        }
    }

    private void setupViews() {
        mLensView.setDrawType(LensView.DrawType.CIRCLES);
        mMinIconSize.setMax(UtilSettings.MAX_ICON_SIZE);
        mMinIconSize.setOnSeekBarChangeListener(new AppCompatSeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int appropriateProgress = progress + (int) UtilSettings.MIN_ICON_SIZE;
                String minIconSize = appropriateProgress + "dp";
                mValueMinIconSize.setText(minIconSize);
                mUtilSettings.save(UtilSettings.KEY_ICON_SIZE, (float) appropriateProgress);
                mLensView.invalidate();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        mDistortionFactor.setMax(UtilSettings.MAX_DISTORTION_FACTOR);
        mDistortionFactor.setOnSeekBarChangeListener(new AppCompatSeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float appropriateProgress = (float) progress / 2.0f + UtilSettings.MIN_DISTORTION_FACTOR;
                String distortionFactor = appropriateProgress + "";
                mValueDistortionFactor.setText(distortionFactor);
                mUtilSettings.save(UtilSettings.KEY_DISTORTION_FACTOR, appropriateProgress);
                mLensView.invalidate();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        mScaleFactor.setMax(UtilSettings.MAX_SCALE_FACTOR);
        mScaleFactor.setOnSeekBarChangeListener(new AppCompatSeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float appropriateProgress = (float) progress / 5.0f + UtilSettings.MIN_SCALE_FACTOR;
                String scaleFactor = appropriateProgress + "";
                mValueScaleFactor.setText(scaleFactor);
                mUtilSettings.save(UtilSettings.KEY_SCALE_FACTOR, appropriateProgress);
                mLensView.invalidate();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        mAnimationTime.setMax(UtilSettings.MAX_ANIMATION_TIME);
        mAnimationTime.setOnSeekBarChangeListener(new AppCompatSeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                long appropriateProgress = (long) progress / 2 + UtilSettings.MIN_ANIMATION_TIME;
                String animationTime = appropriateProgress + "ms";
                mValueAnimationTime.setText(animationTime);
                mUtilSettings.save(UtilSettings.KEY_ANIMATION_TIME, appropriateProgress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private void assignValues() {
        mMinIconSize.setProgress((int) mUtilSettings.getFloat(UtilSettings.KEY_ICON_SIZE) - (int) UtilSettings.MIN_ICON_SIZE);
        String minIconSize = (int) mUtilSettings.getFloat(UtilSettings.KEY_ICON_SIZE) + "dp";
        mValueMinIconSize.setText(minIconSize);
        mDistortionFactor.setProgress((int) (2.0f * (mUtilSettings.getFloat(UtilSettings.KEY_DISTORTION_FACTOR) - UtilSettings.MIN_DISTORTION_FACTOR)));
        String distortionFactor = mUtilSettings.getFloat(UtilSettings.KEY_DISTORTION_FACTOR) + "";
        mValueDistortionFactor.setText(distortionFactor);
        mScaleFactor.setProgress((int) (5.0f * (mUtilSettings.getFloat(UtilSettings.KEY_SCALE_FACTOR) - UtilSettings.MIN_SCALE_FACTOR)));
        String scaleFactor = mUtilSettings.getFloat(UtilSettings.KEY_SCALE_FACTOR) + "";
        mValueScaleFactor.setText(scaleFactor);
        mAnimationTime.setProgress((int) (2 * (mUtilSettings.getLong(UtilSettings.KEY_ANIMATION_TIME) - UtilSettings.MIN_ANIMATION_TIME)));
        String animationTime = mUtilSettings.getLong(UtilSettings.KEY_ANIMATION_TIME) + "ms";
        mValueAnimationTime.setText(animationTime);
    }

    @Override
    public void onDefaultsReset() {
        resetToDefault();
        assignValues();
    }

    private void resetToDefault() {
        mUtilSettings.save(UtilSettings.KEY_ICON_SIZE, UtilSettings.DEFAULT_ICON_SIZE);
        mUtilSettings.save(UtilSettings.KEY_DISTORTION_FACTOR, UtilSettings.DEFAULT_DISTORTION_FACTOR);
        mUtilSettings.save(UtilSettings.KEY_SCALE_FACTOR, UtilSettings.DEFAULT_SCALE_FACTOR);
        mUtilSettings.save(UtilSettings.KEY_ANIMATION_TIME, UtilSettings.DEFAULT_ANIMATION_TIME);
    }
}
