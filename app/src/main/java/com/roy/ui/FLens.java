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
import com.roy.itf.LensInterface;
import com.roy.util.UtilSettings;
import com.roy.views.LensView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FLens extends Fragment implements LensInterface {

    @BindView(R.id.lensViewsSettings)
    LensView lensViewsSettings;

    @OnClick(R.id.sbMinIconSizeParent)
    public void onMinIconSizeParentClick() {
    }

    @BindView(R.id.sbMinIconSize)
    AppCompatSeekBar sbMinIconSize;

    @BindView(R.id.tvValueMinIconSize)
    TextView tvValueMinIconSize;

    @OnClick(R.id.rlSbDistortionFactorParent)
    public void onDistortionFactorParentClick() {
    }

    @BindView(R.id.sbDistortionFactor)
    AppCompatSeekBar sbDistortionFactor;

    @BindView(R.id.tvValueDistortionFactor)
    TextView tvValueDistortionFactor;

    @OnClick(R.id.rlSbScaleFactorParent)
    public void onScaleFactorParentClick() {
    }

    @BindView(R.id.sbScaleFactor)
    AppCompatSeekBar sbScaleFactor;

    @BindView(R.id.tvValueScaleFactor)
    TextView tvValueScaleFactor;

    @OnClick(R.id.rlSbAnimationTimeParent)
    public void onAnimationTimeParentClick() {
    }

    @BindView(R.id.sbAnimationTime)
    AppCompatSeekBar sbAnimationTime;

    @BindView(R.id.tvValueAnimationTime)
    TextView tvValueAnimationTime;

    private UtilSettings utilSettings;

    public FLens() {
    }

    public static FLens newInstance() {
        return new FLens();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.f_lens, container, false);
        ButterKnife.bind(this, view);
        utilSettings = new UtilSettings(getActivity());
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
        lensViewsSettings.setDrawType(LensView.DrawType.CIRCLES);
        sbMinIconSize.setMax(UtilSettings.MAX_ICON_SIZE);
        sbMinIconSize.setOnSeekBarChangeListener(new AppCompatSeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int appropriateProgress = progress + (int) UtilSettings.MIN_ICON_SIZE;
                String minIconSize = appropriateProgress + "dp";
                tvValueMinIconSize.setText(minIconSize);
                utilSettings.save(UtilSettings.KEY_ICON_SIZE, (float) appropriateProgress);
                lensViewsSettings.invalidate();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        sbDistortionFactor.setMax(UtilSettings.MAX_DISTORTION_FACTOR);
        sbDistortionFactor.setOnSeekBarChangeListener(new AppCompatSeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float appropriateProgress = (float) progress / 2.0f + UtilSettings.MIN_DISTORTION_FACTOR;
                String distortionFactor = appropriateProgress + "";
                tvValueDistortionFactor.setText(distortionFactor);
                utilSettings.save(UtilSettings.KEY_DISTORTION_FACTOR, appropriateProgress);
                lensViewsSettings.invalidate();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        sbScaleFactor.setMax(UtilSettings.MAX_SCALE_FACTOR);
        sbScaleFactor.setOnSeekBarChangeListener(new AppCompatSeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float appropriateProgress = (float) progress / 5.0f + UtilSettings.MIN_SCALE_FACTOR;
                String scaleFactor = appropriateProgress + "";
                tvValueScaleFactor.setText(scaleFactor);
                utilSettings.save(UtilSettings.KEY_SCALE_FACTOR, appropriateProgress);
                lensViewsSettings.invalidate();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        sbAnimationTime.setMax(UtilSettings.MAX_ANIMATION_TIME);
        sbAnimationTime.setOnSeekBarChangeListener(new AppCompatSeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                long appropriateProgress = (long) progress / 2 + UtilSettings.MIN_ANIMATION_TIME;
                String animationTime = appropriateProgress + "ms";
                tvValueAnimationTime.setText(animationTime);
                utilSettings.save(UtilSettings.KEY_ANIMATION_TIME, appropriateProgress);
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
        sbMinIconSize.setProgress((int) utilSettings.getFloat(UtilSettings.KEY_ICON_SIZE) - (int) UtilSettings.MIN_ICON_SIZE);
        String minIconSize = (int) utilSettings.getFloat(UtilSettings.KEY_ICON_SIZE) + "dp";
        tvValueMinIconSize.setText(minIconSize);
        sbDistortionFactor.setProgress((int) (2.0f * (utilSettings.getFloat(UtilSettings.KEY_DISTORTION_FACTOR) - UtilSettings.MIN_DISTORTION_FACTOR)));
        String distortionFactor = utilSettings.getFloat(UtilSettings.KEY_DISTORTION_FACTOR) + "";
        tvValueDistortionFactor.setText(distortionFactor);
        sbScaleFactor.setProgress((int) (5.0f * (utilSettings.getFloat(UtilSettings.KEY_SCALE_FACTOR) - UtilSettings.MIN_SCALE_FACTOR)));
        String scaleFactor = utilSettings.getFloat(UtilSettings.KEY_SCALE_FACTOR) + "";
        tvValueScaleFactor.setText(scaleFactor);
        sbAnimationTime.setProgress((int) (2 * (utilSettings.getLong(UtilSettings.KEY_ANIMATION_TIME) - UtilSettings.MIN_ANIMATION_TIME)));
        String animationTime = utilSettings.getLong(UtilSettings.KEY_ANIMATION_TIME) + "ms";
        tvValueAnimationTime.setText(animationTime);
    }

    @Override
    public void onDefaultsReset() {
        resetToDefault();
        assignValues();
    }

    private void resetToDefault() {
        utilSettings.save(UtilSettings.KEY_ICON_SIZE, UtilSettings.DEFAULT_ICON_SIZE);
        utilSettings.save(UtilSettings.KEY_DISTORTION_FACTOR, UtilSettings.DEFAULT_DISTORTION_FACTOR);
        utilSettings.save(UtilSettings.KEY_SCALE_FACTOR, UtilSettings.DEFAULT_SCALE_FACTOR);
        utilSettings.save(UtilSettings.KEY_ANIMATION_TIME, UtilSettings.DEFAULT_ANIMATION_TIME);
    }
}
