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
import com.roy.util.UtilLauncher;
import com.roy.util.UtilNightModeUtil;
import com.roy.util.UtilSettings;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FSettings extends Fragment implements ASettings.SettingsInterface {

    @OnClick(R.id.llHomeLauncher)
    public void onHomeLauncherClick() {
        showHomeLauncherChooser();
    }

    @BindView(R.id.tvSelectedHomeLauncher)
    TextView mHomeLauncherTextView;

    @OnClick(R.id.llIconPack)
    public void onIconPackClick() {
        showIconPackDialog();
    }

    @BindView(R.id.tvSelectedIconPack)
    TextView mIconPackTextView;

    @BindView(R.id.proIconPack)
    View mProIconPack;

    @OnClick(R.id.llNightMode)
    public void onNightModeClick() {
        showNightModeChooser();
    }

    @BindView(R.id.tvSelectedNightMode)
    TextView mNightModeTextView;

    @BindView(R.id.proNightMode)
    View mProNightMode;

    @OnClick(R.id.llBackground)
    public void onBackgroundClick() {
        showBackgroundDialog();
    }

    @BindView(R.id.tvSelectedBackground)
    TextView mBackgroundTextView;

    @BindView(R.id.ivSelectedBackgroundColor)
    ImageView mBackgroundColorImageView;

    @BindView(R.id.proBackground)
    View mProBackground;

    @OnClick(R.id.llHighlightColor)
    public void onHighlightColorClick() {
        showHighlightColorDialog();
    }

    @BindView(R.id.tvSelectedHighlightColor)
    TextView mHighlightColorTextView;

    @BindView(R.id.ivSelectedHighlightColor)
    ImageView mHighlightColorImageView;

    @BindView(R.id.proHighlightColor)
    View mProHighlightColor;

    @OnClick(R.id.rlSwitchVibrateAppHoverParent)
    public void onVibrateAppHoverParentClick() {
    }

    @BindView(R.id.swVibrateAppHover)
    SwitchCompat mVibrateAppHover;

    @OnClick(R.id.rlSwitchVibrateAppLaunchParent)
    public void onVibrateAppLaunchParentClick() {
    }

    @BindView(R.id.swVibrateAppLaunch)
    SwitchCompat mVibrateAppLaunch;

    @OnClick(R.id.rlSwitchShowNameAppHoverParent)
    public void onShowNameAppHoverParentClick() {
    }

    @BindView(R.id.swShowNameAppHover)
    SwitchCompat mShowNameAppHover;

    @OnClick(R.id.swShowNewAppTagParent)
    public void onShowNewAppTagParentClick() {
    }

    @BindView(R.id.swShowNewAppTag)
    SwitchCompat mShowNewAppTag;

    @OnClick(R.id.rlSwitchShowTouchSelectionParent)
    public void onShowTouchSelectionParentClick() {
    }

    @BindView(R.id.swShowTouchSelection)
    SwitchCompat mShowTouchSelection;

    private UtilSettings mUtilSettings;

    public FSettings() {
    }

    public static FSettings newInstance() {
        return new FSettings();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.f_settings, container, false);
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
            ((ASettings) getActivity()).setSettingsInterface(this);
        }
    }

    private void setupViews() {
        mVibrateAppHover.setOnCheckedChangeListener((buttonView, isChecked) -> mUtilSettings.save(UtilSettings.KEY_VIBRATE_APP_HOVER, isChecked));
        mVibrateAppLaunch.setOnCheckedChangeListener((buttonView, isChecked) -> mUtilSettings.save(UtilSettings.KEY_VIBRATE_APP_LAUNCH, isChecked));
        mShowNameAppHover.setOnCheckedChangeListener((buttonView, isChecked) -> mUtilSettings.save(UtilSettings.KEY_SHOW_NAME_APP_HOVER, isChecked));
        mShowNewAppTag.setOnCheckedChangeListener((buttonView, isChecked) -> mUtilSettings.save(UtilSettings.KEY_SHOW_NEW_APP_TAG, isChecked));
        mShowTouchSelection.setOnCheckedChangeListener((buttonView, isChecked) -> mUtilSettings.save(UtilSettings.KEY_SHOW_TOUCH_SELECTION, isChecked));
        mProIconPack.setVisibility(View.GONE);
        mProNightMode.setVisibility(View.GONE);
        mProBackground.setVisibility(View.GONE);
        mProHighlightColor.setVisibility(View.GONE);
    }

    private void assignValues() {
        mIconPackTextView.setText(mUtilSettings.getString(UtilSettings.KEY_ICON_PACK_LABEL_NAME));
        String highlightColor = "#" + mUtilSettings.getString(UtilSettings.KEY_HIGHLIGHT_COLOR).substring(3);
        String homeLauncher = "";
        if (getActivity() != null) {
            homeLauncher = UtilLauncher.getHomeLauncherName(getActivity().getApplication());
        }
        mHomeLauncherTextView.setText(homeLauncher);
        mNightModeTextView.setText(UtilNightModeUtil.getNightModeDisplayName(mUtilSettings.getNightMode()));
        if (mUtilSettings.getString(UtilSettings.KEY_BACKGROUND).equals("Color")) {
            String backgroundColor = "#" + mUtilSettings.getString(UtilSettings.KEY_BACKGROUND_COLOR).substring(3);
            mBackgroundTextView.setText(backgroundColor);
            mBackgroundColorImageView.setVisibility(View.VISIBLE);
            GradientDrawable backgroundColorDrawable = new GradientDrawable();
            backgroundColorDrawable.setColor(Color.parseColor(mUtilSettings.getString(UtilSettings.KEY_BACKGROUND_COLOR)));
            backgroundColorDrawable.setCornerRadius(getResources().getDimension(R.dimen.radius_highlight_color_switch));
            mBackgroundColorImageView.setImageDrawable(backgroundColorDrawable);
        } else {
            mBackgroundTextView.setText(mUtilSettings.getString(UtilSettings.KEY_BACKGROUND));
            mBackgroundColorImageView.setVisibility(View.GONE);
        }
        mHighlightColorTextView.setText(highlightColor);
        GradientDrawable highlightColorDrawable = new GradientDrawable();
        highlightColorDrawable.setColor(Color.parseColor(mUtilSettings.getString(UtilSettings.KEY_HIGHLIGHT_COLOR)));
        highlightColorDrawable.setCornerRadius(getResources().getDimension(R.dimen.radius_highlight_color_switch));
        mHighlightColorImageView.setImageDrawable(highlightColorDrawable);
        mVibrateAppHover.setChecked(mUtilSettings.getBoolean(UtilSettings.KEY_VIBRATE_APP_HOVER));
        mVibrateAppLaunch.setChecked(mUtilSettings.getBoolean(UtilSettings.KEY_VIBRATE_APP_LAUNCH));
        mShowNameAppHover.setChecked(mUtilSettings.getBoolean(UtilSettings.KEY_SHOW_NAME_APP_HOVER));
        mShowNewAppTag.setChecked(mUtilSettings.getBoolean(UtilSettings.KEY_SHOW_NEW_APP_TAG));
        mShowTouchSelection.setChecked(mUtilSettings.getBoolean(UtilSettings.KEY_SHOW_TOUCH_SELECTION));
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
        mUtilSettings.save(UtilSettings.KEY_VIBRATE_APP_HOVER, UtilSettings.DEFAULT_VIBRATE_APP_HOVER);
        mUtilSettings.save(UtilSettings.KEY_VIBRATE_APP_LAUNCH, UtilSettings.DEFAULT_VIBRATE_APP_LAUNCH);
        mUtilSettings.save(UtilSettings.KEY_SHOW_NAME_APP_HOVER, UtilSettings.DEFAULT_SHOW_NAME_APP_HOVER);
        mUtilSettings.save(UtilSettings.KEY_SHOW_TOUCH_SELECTION, UtilSettings.DEFAULT_SHOW_TOUCH_SELECTION);
        mUtilSettings.save(UtilSettings.KEY_SHOW_NEW_APP_TAG, UtilSettings.DEFAULT_SHOW_NEW_APP_TAG);
        mUtilSettings.save(UtilSettings.KEY_BACKGROUND, UtilSettings.DEFAULT_BACKGROUND);
        mUtilSettings.save(UtilSettings.KEY_BACKGROUND_COLOR, UtilSettings.DEFAULT_BACKGROUND_COLOR);
        mUtilSettings.save(UtilSettings.KEY_HIGHLIGHT_COLOR, UtilSettings.DEFAULT_HIGHLIGHT_COLOR);
        mUtilSettings.save(UtilSettings.KEY_ICON_PACK_LABEL_NAME, UtilSettings.DEFAULT_ICON_PACK_LABEL_NAME);
        mUtilSettings.save(UtilSettings.KEY_NIGHT_MODE, UtilSettings.DEFAULT_NIGHT_MODE);
        if (getActivity() != null && getActivity() instanceof ASettings) {
            ((ASettings) getActivity()).sendNightModeBroadcast();
        }
    }
}
