package com.roy.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.color.ColorChooserDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.roy.R;
import com.roy.app.AppsSingleton;
import com.roy.model.App;
import com.roy.sv.BroadcastReceivers;
import com.roy.sv.LoadedObservable;
import com.roy.sv.NightModeObservable;
import com.roy.util.UtilAppSorter;
import com.roy.util.UtilIconPackManager;
import com.roy.util.UtilLauncher;
import com.roy.util.UtilNightModeUtil;
import com.roy.util.UtilSettings;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.Observable;
import java.util.Observer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ASettings extends ABase
        implements Observer, ColorChooserDialog.ColorCallback {

    private static final String COLOR_TAG_BACKGROUND = "BackgroundColor";
    private static final String COLOR_TAG_HIGHLIGHT = "HighlightColor";


    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.tabs)
    TabLayout mTabLayout;

    @BindView(R.id.viewpager)
    ViewPager mViewPager;

    @BindView(R.id.fab_sort)
    FloatingActionButton mSortFab;

    @OnClick(R.id.fab_sort)
    public void onSortClicked() {
        showSortTypeDialog();
    }

    private ArrayList<App> mApps;
    private MaterialDialog mSortTypeDialog;
    private MaterialDialog mIconPackDialog;
    private MaterialDialog mNightModeDialog;
    private MaterialDialog mBackgroundDialog;

    public interface LensInterface {
        void onDefaultsReset();
    }

    private LensInterface mLensInterface;

    public void setLensInterface(LensInterface lensInterface) {
        mLensInterface = lensInterface;
    }

    public interface AppsInterface {
        void onDefaultsReset();

        void onAppsUpdated(ArrayList<App> apps);
    }

    private AppsInterface mAppsInterface;

    public void setAppsInterface(AppsInterface appsInterface) {
        mAppsInterface = appsInterface;
    }

    public interface SettingsInterface {
        void onDefaultsReset();

        void onValuesUpdated();
    }

    private SettingsInterface mSettingsInterface;

    public void setSettingsInterface(SettingsInterface settingsInterface) {
        mSettingsInterface = settingsInterface;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        mSortFab.hide();
        setSupportActionBar(mToolbar);
        FragmentPagerAdapter mPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager(), ASettings.this);
        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1) {
                    mSortFab.show();
                } else {
                    mSortFab.hide();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        mApps = Objects.requireNonNull(AppsSingleton.getInstance()).getApps();
        LoadedObservable.getInstance().addObserver(this);
        NightModeObservable.getInstance().addObserver(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_show_apps:
                if (UtilLauncher.isLauncherDefault(getApplication())) {
                    Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                    homeIntent.addCategory(Intent.CATEGORY_HOME);
                    startActivity(homeIntent);
                } else {
                    Intent homeIntent = new Intent(ASettings.this, AHome.class);
                    startActivity(homeIntent);
                }
                overridePendingTransition(R.anim.a_fade_in, R.anim.a_fade_out);
                return true;
            case R.id.menu_item_about:
                Intent aboutIntent = new Intent(ASettings.this, AAbout.class);
                startActivity(aboutIntent);
                overridePendingTransition(R.anim.a_slide_in_left, R.anim.a_slide_out_right);
                return true;
            case R.id.menu_item_reset_default_settings:
                switch (mViewPager.getCurrentItem()) {
                    case 0:
                        if (mLensInterface != null) {
                            mLensInterface.onDefaultsReset();
                        }
                        break;
                    case 1:
                        if (mAppsInterface != null) {
                            mAppsInterface.onDefaultsReset();
                        }
                        break;
                    case 2:
                        if (mSettingsInterface != null) {
                            mSettingsInterface.onDefaultsReset();
                        }
                        break;
                }
                Snackbar.make(mToolbar, getString(R.string.snackbar_reset_successful), Snackbar.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void update(Observable observable, Object data) {
        if (observable instanceof LoadedObservable) {
            mApps = AppsSingleton.getInstance().getApps();
            if (mAppsInterface != null) {
                mAppsInterface.onAppsUpdated(mApps);
            }
        } else if (observable instanceof NightModeObservable) {
            updateNightMode();
        }
    }

    private void sendUpdateAppsBroadcast() {
        Intent refreshAppsIntent = new Intent(ASettings.this, BroadcastReceivers.AppsUpdatedReceiver.class);
        sendBroadcast(refreshAppsIntent);
    }

    private void sendEditAppsBroadcast() {
        Intent editAppsIntent = new Intent(ASettings.this, BroadcastReceivers.AppsEditedReceiver.class);
        sendBroadcast(editAppsIntent);
    }

    private void sendBackgroundChangedBroadcast() {
        Intent changeBackgroundIntent = new Intent(ASettings.this, BroadcastReceivers.BackgroundChangedReceiver.class);
        sendBroadcast(changeBackgroundIntent);
    }

    public void sendNightModeBroadcast() {
        Intent nightModeIntent = new Intent(ASettings.this, BroadcastReceivers.NightModeReceiver.class);
        sendBroadcast(nightModeIntent);
    }

    private void showSortTypeDialog() {
        final List<UtilAppSorter.SortType> sortTypes = new ArrayList<>(EnumSet.allOf(UtilAppSorter.SortType.class));
        final List<String> sortTypeStrings = new ArrayList<>();
        for (int i = 0; i < sortTypes.size(); i++) {
            sortTypeStrings.add(getApplicationContext().getString(sortTypes.get(i).getDisplayNameResId()));
        }
        UtilAppSorter.SortType selectedSortType = mUtilSettings.getSortType();
        int selectedIndex = sortTypes.indexOf(selectedSortType);
        mSortTypeDialog = new MaterialDialog.Builder(ASettings.this)
                .title(R.string.setting_sort_apps)
                .items(sortTypeStrings)
                .alwaysCallSingleChoiceCallback()
                .itemsCallbackSingleChoice(selectedIndex, (dialog, view, which, text) -> {
                    mUtilSettings.save(sortTypes.get(which));
                    sendEditAppsBroadcast();
                    return true;
                })
                .show();
    }

    public void showIconPackDialog() {
        final ArrayList<UtilIconPackManager.IconPack> availableIconPacks =
                new UtilIconPackManager().getAvailableIconPacksWithIcons(true, getApplication());
        final ArrayList<String> iconPackNames = new ArrayList<>();
        iconPackNames.add(getString(R.string.setting_default_icon_pack));
        for (int i = 0; i < availableIconPacks.size(); i++) {
            if (iconPackNames.size() > 0 && !iconPackNames.contains(availableIconPacks.get(i).mName)) {
                iconPackNames.add(availableIconPacks.get(i).mName);
            }
        }
        String selectedPackageName = mUtilSettings.getString(UtilSettings.KEY_ICON_PACK_LABEL_NAME);
        int selectedIndex = iconPackNames.indexOf(selectedPackageName);
        mIconPackDialog = new MaterialDialog.Builder(ASettings.this)
                .title(R.string.setting_icon_pack)
                .items(iconPackNames)
                .alwaysCallSingleChoiceCallback()
                .itemsCallbackSingleChoice(selectedIndex, (dialog, view, which, text) -> {
                    mUtilSettings.save(UtilSettings.KEY_ICON_PACK_LABEL_NAME, iconPackNames.get(which));
                    if (mSettingsInterface != null) {
                        mSettingsInterface.onValuesUpdated();
                    }
                    sendUpdateAppsBroadcast();
                    return true;
                })
                .show();
    }

    public void showHomeLauncherChooser() {
        UtilLauncher.resetPreferredLauncherAndOpenChooser(getApplicationContext());
    }

    public void showNightModeChooser() {
        String[] availableNightModes = getResources().getStringArray(R.array.night_modes);
        final ArrayList<String> nightModes = new ArrayList<>();
        for (int i = 0; i < availableNightModes.length; i++) {
            nightModes.add(availableNightModes[i]);
        }
        String selectedNightMode = UtilNightModeUtil.getNightModeDisplayName(mUtilSettings.getNightMode());
        int selectedIndex = nightModes.indexOf(selectedNightMode);
        mNightModeDialog = new MaterialDialog.Builder(ASettings.this)
                .title(R.string.setting_night_mode)
                .items(R.array.night_modes)
                .alwaysCallSingleChoiceCallback()
                .itemsCallbackSingleChoice(selectedIndex, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        String selection = nightModes.get(which);
                        mUtilSettings.save(UtilSettings.KEY_NIGHT_MODE, UtilNightModeUtil.getNightModeFromDisplayName(selection));
                        sendNightModeBroadcast();
                        if (mSettingsInterface != null) {
                            mSettingsInterface.onValuesUpdated();
                        }
                        dismissBackgroundDialog();
                        return true;
                    }
                })
                .show();
    }

    public void showBackgroundDialog() {
        String[] availableBackgrounds = getResources().getStringArray(R.array.backgrounds);
        final ArrayList<String> backgroundNames = new ArrayList<>();
        for (int i = 0; i < availableBackgrounds.length; i++) {
            backgroundNames.add(availableBackgrounds[i]);
        }
        String selectedBackground = mUtilSettings.getString(UtilSettings.KEY_BACKGROUND);
        int selectedIndex = backgroundNames.indexOf(selectedBackground);
        mBackgroundDialog = new MaterialDialog.Builder(ASettings.this)
                .title(R.string.setting_background)
                .items(R.array.backgrounds)
                .alwaysCallSingleChoiceCallback()
                .itemsCallbackSingleChoice(selectedIndex, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        String selection = backgroundNames.get(which);
                        if (selection.equals("Wallpaper")) {
                            mUtilSettings.save(UtilSettings.KEY_BACKGROUND, selection);
                            sendBackgroundChangedBroadcast();
                            if (mSettingsInterface != null) {
                                mSettingsInterface.onValuesUpdated();
                            }
                            dismissBackgroundDialog();
                            showWallpaperPicker();
                        } else if (selection.equals("Color")) {
                            dismissBackgroundDialog();
                            showBackgroundColorDialog();
                        }
                        return true;
                    }
                })
                .show();
    }

    public void showWallpaperPicker() {
        Intent intent = new Intent(Intent.ACTION_SET_WALLPAPER);
        startActivity(Intent.createChooser(intent, "Select Wallpaper"));
    }

    public void showBackgroundColorDialog() {
        ColorChooserDialog mBackgroundColorDialog = new ColorChooserDialog.Builder(this, R.string.setting_background_color)
                .titleSub(R.string.setting_background_color)
                .accentMode(false)
                .doneButton(R.string.md_done_label)
                .cancelButton(R.string.md_cancel_label)
                .backButton(R.string.md_back_label)
                .preselect(Color.parseColor(mUtilSettings.getString(UtilSettings.KEY_BACKGROUND_COLOR)))
                .dynamicButtonColor(false)
                .allowUserColorInputAlpha(false)
                .tag(COLOR_TAG_BACKGROUND)
                .show(this);
    }

    public void showHighlightColorDialog() {
        ColorChooserDialog mHighlightColorDialog = new ColorChooserDialog.Builder(this, R.string.setting_highlight_color)
                .titleSub(R.string.setting_highlight_color)
                .accentMode(true)
                .doneButton(R.string.md_done_label)
                .cancelButton(R.string.md_cancel_label)
                .backButton(R.string.md_back_label)
                .preselect(Color.parseColor(mUtilSettings.getString(UtilSettings.KEY_HIGHLIGHT_COLOR)))
                .dynamicButtonColor(false)
                .allowUserColorInputAlpha(false)
                .tag(COLOR_TAG_HIGHLIGHT)
                .show(this);
    }

    @Override
    public void onColorSelection(@NonNull ColorChooserDialog dialog, @ColorInt int selectedColor) {
        String hexColor = String.format("#%06X", selectedColor);
        if (dialog.tag().equals(COLOR_TAG_BACKGROUND)) {
            mUtilSettings.save(UtilSettings.KEY_BACKGROUND, "Color");
            mUtilSettings.save(UtilSettings.KEY_BACKGROUND_COLOR, hexColor);
            sendBackgroundChangedBroadcast();
        } else if (dialog.tag().equals(COLOR_TAG_HIGHLIGHT)) {
            mUtilSettings.save(UtilSettings.KEY_HIGHLIGHT_COLOR, hexColor);
        }
        if (mSettingsInterface != null) {
            mSettingsInterface.onValuesUpdated();
        }
    }

    @Override
    public void onColorChooserDismissed(@NonNull ColorChooserDialog dialog) {
    }

    private void dismissSortTypeDialog() {
        if (mSortTypeDialog != null && mSortTypeDialog.isShowing()) {
            mSortTypeDialog.dismiss();
        }
    }

    private void dismissIconPackDialog() {
        if (mIconPackDialog != null && mIconPackDialog.isShowing()) {
            mIconPackDialog.dismiss();
        }
    }

    private void dismissNightModeDialog() {
        if (mNightModeDialog != null && mNightModeDialog.isShowing()) {
            mNightModeDialog.dismiss();
        }
    }

    private void dismissBackgroundDialog() {
        if (mBackgroundDialog != null && mBackgroundDialog.isShowing()) {
            mBackgroundDialog.dismiss();
        }
    }

    private void dismissAllDialogs() {
        dismissSortTypeDialog();
        dismissIconPackDialog();
        dismissNightModeDialog();
        dismissBackgroundDialog();
        // Color dialogs do not need to be dismissed
    }

    @Override
    protected void onDestroy() {
        dismissAllDialogs();
        LoadedObservable.getInstance().deleteObserver(this);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.a_fade_in, R.anim.a_fade_out);
    }

    private static class FragmentPagerAdapter extends FragmentStatePagerAdapter {

        private static final int NUM_PAGES = 3;

        private final Context mContext;

        public FragmentPagerAdapter(FragmentManager fragmentManager, Context context) {
            super(fragmentManager);
            mContext = context;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return FLens.newInstance();
                case 1:
                    return FApps.newInstance();
                case 2:
                    return FSettings.newInstance();
            }
            return new Fragment();
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return mContext.getResources().getString(R.string.tab_lens);
                case 1:
                    return mContext.getResources().getString(R.string.tab_apps);
                case 2:
                    return mContext.getResources().getString(R.string.tab_settings);
            }
            return super.getPageTitle(position);
        }
    }
}
