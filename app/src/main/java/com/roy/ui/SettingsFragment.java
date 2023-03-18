package com.roy.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.roy.R;
import com.roy.util.LauncherUtil;
import com.roy.util.NightModeUtil;
import com.roy.util.Settings;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nicholasrout on 2016/06/08.
 */
public class SettingsFragment extends Fragment implements SettingsActivity.SettingsInterface {

    private static final String TAG = "SettingsFragment";

    @OnClick(R.id.layout_home_launcher)
    public void onHomeLauncherClick() {
        showHomeLauncherChooser();
    }

    @BindView(R.id.text_view_selected_home_launcher)
    TextView mHomeLauncherTextView;

    @OnClick(R.id.layout_icon_pack)
    public void onIconPackClick() {
        showIconPackDialog();
    }

    @BindView(R.id.text_view_selected_icon_pack)
    TextView mIconPackTextView;

    @BindView(R.id.pro_icon_pack)
    View mProIconPack;

    @OnClick(R.id.layout_night_mode)
    public void onNightModeClick() {
        showNightModeChooser();
    }

    @BindView(R.id.text_view_selected_night_mode)
    TextView mNightModeTextView;

    @BindView(R.id.pro_night_mode)
    View mProNightMode;

    @OnClick(R.id.layout_background)
    public void onBackgroundClick() {
        showBackgroundDialog();
    }

    @BindView(R.id.text_view_selected_background)
    TextView mBackgroundTextView;

    @BindView(R.id.image_view_selected_background_color)
    ImageView mBackgroundColorImageView;

    @BindView(R.id.pro_background)
    View mProBackground;

    @OnClick(R.id.layout_highlight_color)
    public void onHighlightColorClick() {
        showHighlightColorDialog();
    }

    @BindView(R.id.text_view_selected_highlight_color)
    TextView mHighlightColorTextView;

    @BindView(R.id.image_view_selected_highlight_color)
    ImageView mHighlightColorImageView;

    @BindView(R.id.pro_highlight_color)
    View mProHighlightColor;

    @OnClick(R.id.switch_vibrate_app_hover_parent)
    public void onVibrateAppHoverParentClick() {
    }

    @BindView(R.id.switch_vibrate_app_hover)
    SwitchCompat mVibrateAppHover;

    @OnClick(R.id.switch_vibrate_app_launch_parent)
    public void onVibrateAppLaunchParentClick() {
    }

    @BindView(R.id.switch_vibrate_app_launch)
    SwitchCompat mVibrateAppLaunch;

    @OnClick(R.id.switch_show_name_app_hover_parent)
    public void onShowNameAppHoverParentClick() {
    }

    @BindView(R.id.switch_show_name_app_hover)
    SwitchCompat mShowNameAppHover;

    @OnClick(R.id.switch_show_new_app_tag_parent)
    public void onShowNewAppTagParentClick() {
    }

    @BindView(R.id.switch_show_new_app_tag)
    SwitchCompat mShowNewAppTag;

    @OnClick(R.id.switch_show_touch_selection_parent)
    public void onShowTouchSelectionParentClick() {
    }

    @BindView(R.id.switch_show_touch_selection)
    SwitchCompat mShowTouchSelection;

    private Settings mSettings;

    public SettingsFragment() {
    }

    public static SettingsFragment newInstance() {
        SettingsFragment settingsFragment = new SettingsFragment();
        // Include potential bundle extras here
        return settingsFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        ButterKnife.bind(this, view);
        mSettings = new Settings(getActivity());
        setupViews();
        assignValues();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getActivity() != null && getActivity() instanceof SettingsActivity) {
            ((SettingsActivity) getActivity()).setSettingsInterface(this);
        }
    }

    private void setupViews() {
        mVibrateAppHover.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mSettings.save(Settings.KEY_VIBRATE_APP_HOVER, isChecked);
            }
        });
        mVibrateAppLaunch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mSettings.save(Settings.KEY_VIBRATE_APP_LAUNCH, isChecked);
            }
        });
        mShowNameAppHover.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mSettings.save(Settings.KEY_SHOW_NAME_APP_HOVER, isChecked);
            }
        });
        mShowNewAppTag.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mSettings.save(Settings.KEY_SHOW_NEW_APP_TAG, isChecked);
            }
        });
        mShowTouchSelection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mSettings.save(Settings.KEY_SHOW_TOUCH_SELECTION, isChecked);
            }
        });
        mProIconPack.setVisibility(View.GONE);
        mProNightMode.setVisibility(View.GONE);
        mProBackground.setVisibility(View.GONE);
        mProHighlightColor.setVisibility(View.GONE);
    }

    private void assignValues() {
        mIconPackTextView.setText(mSettings.getString(Settings.KEY_ICON_PACK_LABEL_NAME));
        String highlightColor = "#" + mSettings.getString(Settings.KEY_HIGHLIGHT_COLOR).substring(3);
        String homeLauncher = "";
        if (getActivity() != null) {
            homeLauncher = LauncherUtil.getHomeLauncherName(getActivity().getApplication());
        }
        mHomeLauncherTextView.setText(homeLauncher);
        mNightModeTextView.setText(NightModeUtil.getNightModeDisplayName(mSettings.getNightMode()));
        if (mSettings.getString(Settings.KEY_BACKGROUND).equals("Color")) {
            String backgroundColor = "#" + mSettings.getString(Settings.KEY_BACKGROUND_COLOR).substring(3);
            mBackgroundTextView.setText(backgroundColor);
            mBackgroundColorImageView.setVisibility(View.VISIBLE);
            GradientDrawable backgroundColorDrawable = new GradientDrawable();
            backgroundColorDrawable.setColor(Color.parseColor(mSettings.getString(Settings.KEY_BACKGROUND_COLOR)));
            backgroundColorDrawable.setCornerRadius(getResources().getDimension(R.dimen.radius_highlight_color_switch));
            mBackgroundColorImageView.setImageDrawable(backgroundColorDrawable);
        } else {
            mBackgroundTextView.setText(mSettings.getString(Settings.KEY_BACKGROUND));
            mBackgroundColorImageView.setVisibility(View.GONE);
        }
        mHighlightColorTextView.setText(highlightColor);
        GradientDrawable highlightColorDrawable = new GradientDrawable();
        highlightColorDrawable.setColor(Color.parseColor(mSettings.getString(Settings.KEY_HIGHLIGHT_COLOR)));
        highlightColorDrawable.setCornerRadius(getResources().getDimension(R.dimen.radius_highlight_color_switch));
        mHighlightColorImageView.setImageDrawable(highlightColorDrawable);
        mVibrateAppHover.setChecked(mSettings.getBoolean(Settings.KEY_VIBRATE_APP_HOVER));
        mVibrateAppLaunch.setChecked(mSettings.getBoolean(Settings.KEY_VIBRATE_APP_LAUNCH));
        mShowNameAppHover.setChecked(mSettings.getBoolean(Settings.KEY_SHOW_NAME_APP_HOVER));
        mShowNewAppTag.setChecked(mSettings.getBoolean(Settings.KEY_SHOW_NEW_APP_TAG));
        mShowTouchSelection.setChecked(mSettings.getBoolean(Settings.KEY_SHOW_TOUCH_SELECTION));
    }

    private void showIconPackDialog() {
        if (getActivity() != null && getActivity() instanceof SettingsActivity) {
            ((SettingsActivity) getActivity()).showIconPackDialog();
        }
    }

    private void showHomeLauncherChooser() {
        if (getActivity() != null && getActivity() instanceof SettingsActivity) {
            ((SettingsActivity) getActivity()).showHomeLauncherChooser();
        }
    }

    private void showNightModeChooser() {
        if (getActivity() != null && getActivity() instanceof SettingsActivity) {
            ((SettingsActivity) getActivity()).showNightModeChooser();
        }
    }

    private void showBackgroundDialog() {
        if (getActivity() != null && getActivity() instanceof SettingsActivity) {
            ((SettingsActivity) getActivity()).showBackgroundDialog();
        }
    }

    private void showHighlightColorDialog() {
        if (getActivity() != null && getActivity() instanceof SettingsActivity) {
            ((SettingsActivity) getActivity()).showHighlightColorDialog();
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
        mSettings.save(Settings.KEY_VIBRATE_APP_HOVER, Settings.DEFAULT_VIBRATE_APP_HOVER);
        mSettings.save(Settings.KEY_VIBRATE_APP_LAUNCH, Settings.DEFAULT_VIBRATE_APP_LAUNCH);
        mSettings.save(Settings.KEY_SHOW_NAME_APP_HOVER, Settings.DEFAULT_SHOW_NAME_APP_HOVER);
        mSettings.save(Settings.KEY_SHOW_TOUCH_SELECTION, Settings.DEFAULT_SHOW_TOUCH_SELECTION);
        mSettings.save(Settings.KEY_SHOW_NEW_APP_TAG, Settings.DEFAULT_SHOW_NEW_APP_TAG);
        mSettings.save(Settings.KEY_BACKGROUND, Settings.DEFAULT_BACKGROUND);
        mSettings.save(Settings.KEY_BACKGROUND_COLOR, Settings.DEFAULT_BACKGROUND_COLOR);
        mSettings.save(Settings.KEY_HIGHLIGHT_COLOR, Settings.DEFAULT_HIGHLIGHT_COLOR);
        mSettings.save(Settings.KEY_ICON_PACK_LABEL_NAME, Settings.DEFAULT_ICON_PACK_LABEL_NAME);
        mSettings.save(Settings.KEY_NIGHT_MODE, Settings.DEFAULT_NIGHT_MODE);
        if (getActivity() != null && getActivity() instanceof SettingsActivity) {
            ((SettingsActivity) getActivity()).sendNightModeBroadcast();
        }
    }
}
