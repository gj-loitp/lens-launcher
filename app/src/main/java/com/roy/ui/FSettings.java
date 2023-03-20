package com.roy.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.roy.R;
import com.roy.itf.SettingsInterface;
import com.roy.util.UtilLauncher;
import com.roy.util.UtilNightModeUtil;
import com.roy.util.UtilSettings;

public class FSettings extends Fragment implements SettingsInterface {

    TextView tvSelectedHomeLauncher;
    TextView tvSelectedIconPack;
    View proIconPack;
    TextView tvSelectedNightMode;
    View proNightMode;
    TextView tvSelectedBackground;
    ImageView ivSelectedBackgroundColor;
    View proBackground;
    TextView tvSelectedHighlightColor;
    ImageView ivSelectedHighlightColor;
    View proHighlightColor;
    SwitchCompat swVibrateAppHover;
    SwitchCompat swVibrateAppLaunch;
    SwitchCompat swShowNameAppHover;
    SwitchCompat swShowNewAppTag;
    SwitchCompat swShowTouchSelection;

    private UtilSettings utilSettings;

    public FSettings() {
    }

    public static FSettings newInstance() {
        return new FSettings();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.f_settings, container, false);
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
            ((ASettings) getActivity()).setSettingsInterface(this);
        }
    }

    private void setupViews(View view) {
        tvSelectedHomeLauncher = view.findViewById(R.id.tvSelectedHomeLauncher);
        tvSelectedIconPack = view.findViewById(R.id.tvSelectedIconPack);
        proIconPack = view.findViewById(R.id.proIconPack);
        tvSelectedNightMode = view.findViewById(R.id.tvSelectedNightMode);
        proNightMode = view.findViewById(R.id.proNightMode);
        tvSelectedBackground = view.findViewById(R.id.tvSelectedBackground);
        ivSelectedBackgroundColor = view.findViewById(R.id.ivSelectedBackgroundColor);
        proBackground = view.findViewById(R.id.proBackground);
        tvSelectedHighlightColor = view.findViewById(R.id.tvSelectedHighlightColor);
        ivSelectedHighlightColor = view.findViewById(R.id.ivSelectedHighlightColor);
        proHighlightColor = view.findViewById(R.id.proHighlightColor);
        swVibrateAppHover = view.findViewById(R.id.swVibrateAppHover);
        swVibrateAppLaunch = view.findViewById(R.id.swVibrateAppLaunch);
        swShowNameAppHover = view.findViewById(R.id.swShowNameAppHover);
        swShowNewAppTag = view.findViewById(R.id.swShowNewAppTag);
        swShowTouchSelection = view.findViewById(R.id.swShowTouchSelection);

        view.findViewById(R.id.llHomeLauncher).setOnClickListener(v -> showHomeLauncherChooser());
        view.findViewById(R.id.llIconPack).setOnClickListener(v -> showIconPackDialog());
        view.findViewById(R.id.llNightMode).setOnClickListener(v -> showNightModeChooser());
        view.findViewById(R.id.llBackground).setOnClickListener(v -> showBackgroundDialog());
        view.findViewById(R.id.llHighlightColor).setOnClickListener(v -> showHighlightColorDialog());
        view.findViewById(R.id.rlSwitchVibrateAppHoverParent).setOnClickListener(v -> {

        });
        view.findViewById(R.id.rlSwitchVibrateAppLaunchParent).setOnClickListener(v -> {

        });
        view.findViewById(R.id.rlSwitchShowNameAppHoverParent).setOnClickListener(v -> {

        });
        view.findViewById(R.id.swShowNewAppTagParent).setOnClickListener(v -> {

        });
        view.findViewById(R.id.rlSwitchShowTouchSelectionParent).setOnClickListener(v -> {

        });

        swVibrateAppHover.setOnCheckedChangeListener((buttonView, isChecked) ->
                utilSettings.save(UtilSettings.KEY_VIBRATE_APP_HOVER, isChecked)
        );
        swVibrateAppLaunch.setOnCheckedChangeListener((buttonView, isChecked) ->
                utilSettings.save(UtilSettings.KEY_VIBRATE_APP_LAUNCH, isChecked)
        );
        swShowNameAppHover.setOnCheckedChangeListener((buttonView, isChecked) ->
                utilSettings.save(UtilSettings.KEY_SHOW_NAME_APP_HOVER, isChecked)
        );
        swShowNewAppTag.setOnCheckedChangeListener((buttonView, isChecked) ->
                utilSettings.save(UtilSettings.KEY_SHOW_NEW_APP_TAG, isChecked)
        );
        swShowTouchSelection.setOnCheckedChangeListener((buttonView, isChecked) ->
                utilSettings.save(UtilSettings.KEY_SHOW_TOUCH_SELECTION, isChecked)
        );
        proIconPack.setVisibility(View.GONE);
        proNightMode.setVisibility(View.GONE);
        proBackground.setVisibility(View.GONE);
        proHighlightColor.setVisibility(View.GONE);
    }

    private void assignValues() {
        tvSelectedIconPack.setText(utilSettings.getString(UtilSettings.KEY_ICON_PACK_LABEL_NAME));
        String highlightColor = "#" + utilSettings.getString(UtilSettings.KEY_HIGHLIGHT_COLOR).substring(3);
        String homeLauncher = "";
        if (getActivity() != null) {
            homeLauncher = UtilLauncher.getHomeLauncherName(getActivity().getApplication());
        }
        tvSelectedHomeLauncher.setText(homeLauncher);
        tvSelectedNightMode.setText(UtilNightModeUtil.getNightModeDisplayName(utilSettings.getNightMode()));
        if (utilSettings.getString(UtilSettings.KEY_BACKGROUND).equals("Color")) {
            String backgroundColor = "#" + utilSettings.getString(UtilSettings.KEY_BACKGROUND_COLOR).substring(3);
            tvSelectedBackground.setText(backgroundColor);
            ivSelectedBackgroundColor.setVisibility(View.VISIBLE);
            GradientDrawable backgroundColorDrawable = new GradientDrawable();
            backgroundColorDrawable.setColor(Color.parseColor(utilSettings.getString(UtilSettings.KEY_BACKGROUND_COLOR)));
            backgroundColorDrawable.setCornerRadius(getResources().getDimension(R.dimen.radius_highlight_color_switch));
            ivSelectedBackgroundColor.setImageDrawable(backgroundColorDrawable);
        } else {
            tvSelectedBackground.setText(utilSettings.getString(UtilSettings.KEY_BACKGROUND));
            ivSelectedBackgroundColor.setVisibility(View.GONE);
        }
        tvSelectedHighlightColor.setText(highlightColor);
        GradientDrawable highlightColorDrawable = new GradientDrawable();
        highlightColorDrawable.setColor(Color.parseColor(utilSettings.getString(UtilSettings.KEY_HIGHLIGHT_COLOR)));
        highlightColorDrawable.setCornerRadius(getResources().getDimension(R.dimen.radius_highlight_color_switch));
        ivSelectedHighlightColor.setImageDrawable(highlightColorDrawable);
        swVibrateAppHover.setChecked(utilSettings.getBoolean(UtilSettings.KEY_VIBRATE_APP_HOVER));
        swVibrateAppLaunch.setChecked(utilSettings.getBoolean(UtilSettings.KEY_VIBRATE_APP_LAUNCH));
        swShowNameAppHover.setChecked(utilSettings.getBoolean(UtilSettings.KEY_SHOW_NAME_APP_HOVER));
        swShowNewAppTag.setChecked(utilSettings.getBoolean(UtilSettings.KEY_SHOW_NEW_APP_TAG));
        swShowTouchSelection.setChecked(utilSettings.getBoolean(UtilSettings.KEY_SHOW_TOUCH_SELECTION));
    }

    private void showIconPackDialog() {
        if (getActivity() != null && getActivity() instanceof ASettings) {
            ((ASettings) getActivity()).showIconPackDialog();
        }
    }

    private void showHomeLauncherChooser() {
        if (getActivity() != null && getActivity() instanceof ASettings) {
            ((ASettings) getActivity()).showHomeLauncherChooser();
        }
    }

    private void showNightModeChooser() {
        if (getActivity() != null && getActivity() instanceof ASettings) {
            ((ASettings) getActivity()).showNightModeChooser();
        }
    }

    private void showBackgroundDialog() {
        if (getActivity() != null && getActivity() instanceof ASettings) {
            ((ASettings) getActivity()).showBackgroundDialog();
        }
    }

    private void showHighlightColorDialog() {
        if (getActivity() != null && getActivity() instanceof ASettings) {
            ((ASettings) getActivity()).showHighlightColorDialog();
        }
    }

    @Override
    public void onDefaultsReset() {
        resetToDefault();
        assignValues();
    }

    @Override
    public void onValuesUpdated() {
        assignValues();
    }

    private void resetToDefault() {
        utilSettings.save(UtilSettings.KEY_VIBRATE_APP_HOVER, UtilSettings.DEFAULT_VIBRATE_APP_HOVER);
        utilSettings.save(UtilSettings.KEY_VIBRATE_APP_LAUNCH, UtilSettings.DEFAULT_VIBRATE_APP_LAUNCH);
        utilSettings.save(UtilSettings.KEY_SHOW_NAME_APP_HOVER, UtilSettings.DEFAULT_SHOW_NAME_APP_HOVER);
        utilSettings.save(UtilSettings.KEY_SHOW_TOUCH_SELECTION, UtilSettings.DEFAULT_SHOW_TOUCH_SELECTION);
        utilSettings.save(UtilSettings.KEY_SHOW_NEW_APP_TAG, UtilSettings.DEFAULT_SHOW_NEW_APP_TAG);
        utilSettings.save(UtilSettings.KEY_BACKGROUND, UtilSettings.DEFAULT_BACKGROUND);
        utilSettings.save(UtilSettings.KEY_BACKGROUND_COLOR, UtilSettings.DEFAULT_BACKGROUND_COLOR);
        utilSettings.save(UtilSettings.KEY_HIGHLIGHT_COLOR, UtilSettings.DEFAULT_HIGHLIGHT_COLOR);
        utilSettings.save(UtilSettings.KEY_ICON_PACK_LABEL_NAME, UtilSettings.DEFAULT_ICON_PACK_LABEL_NAME);
        utilSettings.save(UtilSettings.KEY_NIGHT_MODE, UtilSettings.DEFAULT_NIGHT_MODE);
        if (getActivity() != null && getActivity() instanceof ASettings) {
            ((ASettings) getActivity()).sendNightModeBroadcast();
        }
    }
}
