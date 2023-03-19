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

public class FLens extends Fragment implements LensInterface {

    LensView lensViewsSettings;
    AppCompatSeekBar sbMinIconSize;
    TextView tvValueMinIconSize;
    AppCompatSeekBar sbDistortionFactor;
    TextView tvValueDistortionFactor;
    AppCompatSeekBar sbScaleFactor;
    TextView tvValueScaleFactor;
    AppCompatSeekBar sbAnimationTime;
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
        utilSettings = new UtilSettings(getActivity());
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupViews(view);
        assignValues();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (getActivity() != null && getActivity() instanceof ASettings) {
            ((ASettings) getActivity()).setLensInterface(this);
        }
    }

    private void setupViews(View view) {
        lensViewsSettings = view.findViewById(R.id.lensViewsSettings);
        sbMinIconSize = view.findViewById(R.id.sbMinIconSize);
        tvValueMinIconSize = view.findViewById(R.id.tvValueMinIconSize);
        sbDistortionFactor = view.findViewById(R.id.sbDistortionFactor);
        tvValueDistortionFactor = view.findViewById(R.id.tvValueDistortionFactor);
        sbScaleFactor = view.findViewById(R.id.sbScaleFactor);
        tvValueScaleFactor = view.findViewById(R.id.tvValueScaleFactor);
        sbAnimationTime = view.findViewById(R.id.sbAnimationTime);
        tvValueAnimationTime = view.findViewById(R.id.tvValueAnimationTime);

        view.findViewById(R.id.sbMinIconSizeParent).setOnClickListener(v -> {
            //do nothing
        });
        view.findViewById(R.id.rlSbDistortionFactorParent).setOnClickListener(v -> {
            //do nothing
        });
        view.findViewById(R.id.rlSbScaleFactorParent).setOnClickListener(v -> {
            //do nothing
        });
        view.findViewById(R.id.rlSbAnimationTimeParent).setOnClickListener(v -> {
            //do nothing
        });

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
