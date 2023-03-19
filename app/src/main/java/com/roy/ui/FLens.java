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
import com.roy.util.Settings;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FLens extends Fragment implements SettingsA.LensInterface {

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

    private Settings mSettings;

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
        mSettings = new Settings(getActivity());
        setupViews();
        assignValues();
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (getActivity() != null && getActivity() instanceof SettingsA) {
            ((SettingsA) getActivity()).setLensInterface(this);
        }
    }

    private void setupViews() {
        mLensView.setDrawType(LensView.DrawType.CIRCLES);
        mMinIconSize.setMax(Settings.MAX_ICON_SIZE);
        mMinIconSize.setOnSeekBarChangeListener(new AppCompatSeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int appropriateProgress = progress + (int) Settings.MIN_ICON_SIZE;
                String minIconSize = appropriateProgress + "dp";
                mValueMinIconSize.setText(minIconSize);
                mSettings.save(Settings.KEY_ICON_SIZE, (float) appropriateProgress);
                mLensView.invalidate();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        mDistortionFactor.setMax(Settings.MAX_DISTORTION_FACTOR);
        mDistortionFactor.setOnSeekBarChangeListener(new AppCompatSeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float appropriateProgress = (float) progress / 2.0f + Settings.MIN_DISTORTION_FACTOR;
                String distortionFactor = appropriateProgress + "";
                mValueDistortionFactor.setText(distortionFactor);
                mSettings.save(Settings.KEY_DISTORTION_FACTOR, appropriateProgress);
                mLensView.invalidate();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        mScaleFactor.setMax(Settings.MAX_SCALE_FACTOR);
        mScaleFactor.setOnSeekBarChangeListener(new AppCompatSeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float appropriateProgress = (float) progress / 5.0f + Settings.MIN_SCALE_FACTOR;
                String scaleFactor = appropriateProgress + "";
                mValueScaleFactor.setText(scaleFactor);
                mSettings.save(Settings.KEY_SCALE_FACTOR, appropriateProgress);
                mLensView.invalidate();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        mAnimationTime.setMax(Settings.MAX_ANIMATION_TIME);
        mAnimationTime.setOnSeekBarChangeListener(new AppCompatSeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                long appropriateProgress = (long) progress / 2 + Settings.MIN_ANIMATION_TIME;
                String animationTime = appropriateProgress + "ms";
                mValueAnimationTime.setText(animationTime);
                mSettings.save(Settings.KEY_ANIMATION_TIME, appropriateProgress);
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
        mMinIconSize.setProgress((int) mSettings.getFloat(Settings.KEY_ICON_SIZE) - (int) Settings.MIN_ICON_SIZE);
        String minIconSize = (int) mSettings.getFloat(Settings.KEY_ICON_SIZE) + "dp";
        mValueMinIconSize.setText(minIconSize);
        mDistortionFactor.setProgress((int) (2.0f * (mSettings.getFloat(Settings.KEY_DISTORTION_FACTOR) - Settings.MIN_DISTORTION_FACTOR)));
        String distortionFactor = mSettings.getFloat(Settings.KEY_DISTORTION_FACTOR) + "";
        mValueDistortionFactor.setText(distortionFactor);
        mScaleFactor.setProgress((int) (5.0f * (mSettings.getFloat(Settings.KEY_SCALE_FACTOR) - Settings.MIN_SCALE_FACTOR)));
        String scaleFactor = mSettings.getFloat(Settings.KEY_SCALE_FACTOR) + "";
        mValueScaleFactor.setText(scaleFactor);
        mAnimationTime.setProgress((int) (2 * (mSettings.getLong(Settings.KEY_ANIMATION_TIME) - Settings.MIN_ANIMATION_TIME)));
        String animationTime = mSettings.getLong(Settings.KEY_ANIMATION_TIME) + "ms";
        mValueAnimationTime.setText(animationTime);
    }

    @Override
    public void onDefaultsReset() {
        resetToDefault();
        assignValues();
    }

    private void resetToDefault() {
        mSettings.save(Settings.KEY_ICON_SIZE, Settings.DEFAULT_ICON_SIZE);
        mSettings.save(Settings.KEY_DISTORTION_FACTOR, Settings.DEFAULT_DISTORTION_FACTOR);
        mSettings.save(Settings.KEY_SCALE_FACTOR, Settings.DEFAULT_SCALE_FACTOR);
        mSettings.save(Settings.KEY_ANIMATION_TIME, Settings.DEFAULT_ANIMATION_TIME);
    }
}
