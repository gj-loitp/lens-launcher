package com.mckimquyen.ui;

import static com.mckimquyen.ext.ActivityKt.likeFacebookFanpage;
import static com.mckimquyen.ext.ActivityKt.moreApp;
import static com.mckimquyen.ext.ActivityKt.rateApp;
import static com.mckimquyen.ext.ActivityKt.shareApp;
import static com.mckimquyen.ext.ApplovinKt.destroyAdBanner;
import static com.mckimquyen.ext.ApplovinKt.showMediationDebuggerApplovin;
import static com.mckimquyen.ext.ContextKt.openUrlInBrowser;
import static com.mckimquyen.ext.ContextKt.showDialog2;
import static com.mckimquyen.util.CKt.URL_POLICY_NOTION;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.color.ColorChooserDialog;
import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxAdView;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.mckimquyen.BuildConfig;
import com.mckimquyen.R;
import com.mckimquyen.adt.FragmentPagerAdapter;
import com.mckimquyen.app.RAppsSingleton;
import com.mckimquyen.enums.SortType;
import com.mckimquyen.ext.ApplovinKt;
import com.mckimquyen.itf.AppsInterface;
import com.mckimquyen.itf.LensInterface;
import com.mckimquyen.itf.SettingsInterface;
import com.mckimquyen.model.App;
import com.mckimquyen.services.BroadcastReceivers;
import com.mckimquyen.services.LoadedObservable;
import com.mckimquyen.services.NightModeObservable;
import com.mckimquyen.util.UtilIconPackManager;
import com.mckimquyen.util.UtilLauncher;
import com.mckimquyen.util.UtilNightModeUtil;
import com.mckimquyen.util.UtilSettings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.TimeUnit;

//2023.03.19 tried to convert kotlin but failed
public class ActSettings extends ActBase implements Observer, ColorChooserDialog.ColorCallback {

    private static final String TAG_COLOR_BACKGROUND = "BackgroundColor";
    private static final String TAG_COLOR_HIGHLIGHT = "HighlightColor";
    Toolbar toolbar;
    TabLayout tabs;
    ViewPager viewpager;
    FloatingActionButton fabSort;
    private MaxAdView adView;
    private MaxInterstitialAd interstitialAd;
    private int retryAttempt;

    private ArrayList<App> listApp;
    private MaterialDialog dlgSortType;
    private MaterialDialog dlgIconPack;
    private MaterialDialog dlgNightMode;
    private MaterialDialog dlgBackground;
    private LensInterface lensInterface;

    public void setLensInterface(LensInterface lensInterface) {
        this.lensInterface = lensInterface;
    }

    private AppsInterface appsInterface;

    public void setAppsInterface(AppsInterface appsInterface) {
        this.appsInterface = appsInterface;
    }

    private SettingsInterface settingsInterface;

    public void setSettingsInterface(SettingsInterface settingsInterface) {
        this.settingsInterface = settingsInterface;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_settings);

        setupViews();

        LoadedObservable.getInstance().addObserver(this);
        NightModeObservable.getInstance().addObserver(this);
    }

    private void setupViews() {
        toolbar = findViewById(R.id.toolbar);
        tabs = findViewById(R.id.tabs);
        viewpager = findViewById(R.id.viewpager);
        fabSort = findViewById(R.id.fabSort);

        fabSort.setOnClickListener(view -> showSortTypeDialog());
        findViewById(R.id.btStart).setOnClickListener(view -> launchApps());

        fabSort.hide();
        setSupportActionBar(toolbar);
        FragmentPagerAdapter mPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager(), ActSettings.this);
        viewpager.setAdapter(mPagerAdapter);
        tabs.setupWithViewPager(viewpager);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1) {
                    fabSort.show();
                } else {
                    fabSort.hide();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        listApp = Objects.requireNonNull(RAppsSingleton.getInstance()).getApps();

        adView = ApplovinKt.createAdBanner(this,
                ActSettings.class.getSimpleName(),
                Color.TRANSPARENT,
                findViewById(R.id.flAd),
                true);
        createAdInter();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (utilSettings != null) {
            boolean hasRead = utilSettings.getBoolean(UtilSettings.KEY_READ_POLICY);
            if (!hasRead) {
                showDialog2(this, getString(R.string.terms_and_privacy_policy), getString(R.string.read_policy), getString(R.string.agree_and_continue), getString(R.string.cancel), () -> {
                    openUrlInBrowser(this, URL_POLICY_NOTION, getString(R.string.terms_and_privacy_policy), false);
                    utilSettings.save(UtilSettings.KEY_READ_POLICY, true);
                }, () -> {
                    utilSettings.save(UtilSettings.KEY_READ_POLICY, true);
                });
            }
        }
    }

    private void launchApps() {
        boolean isDefaultLauncher = UtilLauncher.isDefaultLauncher(getApplication());
        if (isDefaultLauncher) {
            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
            homeIntent.addCategory(Intent.CATEGORY_HOME);
            startActivity(homeIntent);
        } else {
//            Intent homeIntent = new Intent(ASettings.this, AHome.class);
//            startActivity(homeIntent);

            showHomeLauncherChooser();
        }
        overridePendingTransition(R.anim.a_fade_in, R.anim.a_fade_out);
    }

    void showAd() {
        if (interstitialAd != null && interstitialAd.isReady()) {
            interstitialAd.showAd();
        }
    }

    private void createAdInter() {
        interstitialAd = new MaxInterstitialAd(getString(R.string.INTER), this);
        interstitialAd.setListener(new MaxAdListener() {
            @Override
            public void onAdLoaded(@NonNull MaxAd maxAd) {
                retryAttempt = 0;
            }

            @Override
            public void onAdDisplayed(@NonNull MaxAd maxAd) {

            }

            @Override
            public void onAdHidden(@NonNull MaxAd maxAd) {
                // Interstitial ad is hidden. Pre-load the next ad
                interstitialAd.loadAd();
            }

            @Override
            public void onAdClicked(@NonNull MaxAd maxAd) {

            }

            @Override
            public void onAdLoadFailed(@NonNull String s, @NonNull MaxError maxError) {
                retryAttempt++;
                long delayMillis = TimeUnit.SECONDS.toMillis((long) Math.pow(2, Math.min(6, retryAttempt)));

                new Handler().postDelayed(() -> interstitialAd.loadAd(), delayMillis);
            }

            @Override
            public void onAdDisplayFailed(@NonNull MaxAd maxAd, @NonNull MaxError maxError) {
                // Interstitial ad failed to display. AppLovin recommends that you load the next ad.
                interstitialAd.loadAd();
            }
        });

        // Load the first ad
        interstitialAd.loadAd();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menuItemShowApps) {
            launchApps();
            return true;
        } else if (id == R.id.menuItemAbout) {
            Intent aboutIntent = new Intent(ActSettings.this, ActAbout.class);
            startActivity(aboutIntent);
            overridePendingTransition(R.anim.a_slide_in_left, R.anim.a_slide_out_right);
            return true;
        } else if (id == R.id.menuItemResetDefaultSettings) {
            switch (viewpager.getCurrentItem()) {
                case 0:
                    if (lensInterface != null) {
                        lensInterface.onDefaultsReset();
                    }
                    break;
                case 1:
                    if (appsInterface != null) {
                        appsInterface.onDefaultsReset();
                    }
                    break;
                case 2:
                    if (settingsInterface != null) {
                        settingsInterface.onDefaultsReset();
                    }
                    break;
            }
            Snackbar.make(toolbar, getString(R.string.snackbar_reset_successful), Snackbar.LENGTH_LONG).show();
            return true;
        } else if (id == R.id.menuRateApp) {
            rateApp(this, this.getPackageName());
            return true;
        } else if (id == R.id.menuMoreApp) {
            moreApp(this, "mckimquyen");
            return true;
        } else if (id == R.id.menuApplovinConfig) {
            if (BuildConfig.DEBUG) {
                showMediationDebuggerApplovin(this);
            } else {
                Toast.makeText(
                        /* context = */ this,
                        /* resId = */ "This feature is only available in Debug mode",
                        /* duration = */ Toast.LENGTH_SHORT
                ).show();
            }
            return true;
        } else if (id == R.id.menuShareApp) {
            shareApp(this);
            return true;
        } else if (id == R.id.menuFacebookFanPage) {
            likeFacebookFanpage(this);
            return true;
        } else if (id == R.id.menuPolicy) {
            openUrlInBrowser(this, URL_POLICY_NOTION, getString(R.string.terms_and_privacy_policy), false);
            return true;
        } else if (id == R.id.menuGithub) {
            openUrlInBrowser(this, "https://github.com/gj-loitp/lens-launcher", getString(R.string.github), true);
            return true;
        } else if (id == R.id.menuLicense) {
            openUrlInBrowser(this, "https://raw.githubusercontent.com/ricknout/lens-launcher/master/LICENSE.md", getString(R.string.license), true);
            return true;
        } else if (id == R.id.menuChangelog) {
            openUrlInBrowser(this, "https://raw.githubusercontent.com/gj-loitp/lens-launcher/dev/CHANGE_LOG.md", getString(R.string.changelog), true);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void update(Observable observable, Object data) {
        if (observable instanceof LoadedObservable) {
            if (RAppsSingleton.getInstance() != null) {
                listApp = RAppsSingleton.getInstance().getApps();
            }
            if (appsInterface != null) {
                appsInterface.onAppsUpdated(listApp);
            }
        } else if (observable instanceof NightModeObservable) {
            updateNightMode();
        }
    }

    private void sendUpdateAppsBroadcast() {
        Intent refreshAppsIntent = new Intent(ActSettings.this, BroadcastReceivers.AppsUpdatedReceiver.class);
        sendBroadcast(refreshAppsIntent);
    }

    private void sendEditAppsBroadcast() {
        Intent editAppsIntent = new Intent(ActSettings.this, BroadcastReceivers.AppsEditedReceiver.class);
        sendBroadcast(editAppsIntent);
    }

    private void sendBackgroundChangedBroadcast() {
        Intent changeBackgroundIntent = new Intent(ActSettings.this, BroadcastReceivers.BackgroundChangedReceiver.class);
        sendBroadcast(changeBackgroundIntent);
    }

    public void sendNightModeBroadcast() {
        Intent nightModeIntent = new Intent(ActSettings.this, BroadcastReceivers.NightModeReceiver.class);
        sendBroadcast(nightModeIntent);
    }

    private void showSortTypeDialog() {
        showAd();
        final List<SortType> lSortType = new ArrayList<>(EnumSet.allOf(SortType.class));
        final List<String> lSortTypeString = new ArrayList<>();
        for (int i = 0; i < lSortType.size(); i++) {
            lSortTypeString.add(getApplicationContext().getString(lSortType.get(i).getDisplayNameResId()));
        }
        assert utilSettings != null;
        SortType selectedSortType = utilSettings.getSortType();
        int selectedIndex = lSortType.indexOf(selectedSortType);
        dlgSortType = new MaterialDialog.Builder(ActSettings.this).title(R.string.setting_sort_apps).items(lSortTypeString).alwaysCallSingleChoiceCallback().itemsCallbackSingleChoice(selectedIndex, (dialog, view, which, text) -> {
            utilSettings.save(lSortType.get(which));
            sendEditAppsBroadcast();
            return true;
        }).show();
    }

    public void showIconPackDialog() {
        final ArrayList<UtilIconPackManager.IconPack> lAvailableIconPack = new UtilIconPackManager().getAvailableIconPacksWithIcons(true, getApplication());
        final ArrayList<String> lIconPackName = new ArrayList<>();
        lIconPackName.add(getString(R.string.setting_default_icon_pack));
        for (int i = 0; i < lAvailableIconPack.size(); i++) {
            if (lIconPackName.size() > 0 && !lIconPackName.contains(lAvailableIconPack.get(i).mName)) {
                lIconPackName.add(lAvailableIconPack.get(i).mName);
            }
        }
        assert utilSettings != null;
        String selectedPackageName = utilSettings.getString(UtilSettings.KEY_ICON_PACK_LABEL_NAME);
        int selectedIndex = lIconPackName.indexOf(selectedPackageName);
        dlgIconPack = new MaterialDialog.Builder(ActSettings.this).title(R.string.setting_icon_pack).items(lIconPackName).alwaysCallSingleChoiceCallback().itemsCallbackSingleChoice(selectedIndex, (dialog, view, which, text) -> {
            utilSettings.save(UtilSettings.KEY_ICON_PACK_LABEL_NAME, lIconPackName.get(which));
            if (settingsInterface != null) {
                settingsInterface.onValuesUpdated();
            }
            sendUpdateAppsBroadcast();
            return true;
        }).show();
    }

    public void showHomeLauncherChooser() {
        UtilLauncher.resetPreferredLauncherAndOpenChooser(getApplicationContext());
    }

    public void showNightModeChooser() {
        String[] arrAvailableNightMode = getResources().getStringArray(R.array.night_modes);
        final ArrayList<String> nightModes = new ArrayList<>();
        Collections.addAll(nightModes, arrAvailableNightMode);
        assert utilSettings != null;
        String selectedNightMode = UtilNightModeUtil.getNightModeDisplayName(utilSettings.getNightMode());
        int selectedIndex = nightModes.indexOf(selectedNightMode);
        dlgNightMode = new MaterialDialog.Builder(ActSettings.this).title(R.string.setting_night_mode).items(R.array.night_modes).alwaysCallSingleChoiceCallback().itemsCallbackSingleChoice(selectedIndex, (dialog, view, which, text) -> {
            String selection = nightModes.get(which);
            utilSettings.save(UtilSettings.KEY_NIGHT_MODE, UtilNightModeUtil.getNightModeFromDisplayName(selection));
            sendNightModeBroadcast();
            if (settingsInterface != null) {
                settingsInterface.onValuesUpdated();
            }
            dismissBackgroundDialog();
            return true;
        }).show();
    }

    public void showBackgroundDialog() {
        String[] arrAvailableBackground = getResources().getStringArray(R.array.backgrounds);
        final ArrayList<String> backgroundNames = new ArrayList<>();
        Collections.addAll(backgroundNames, arrAvailableBackground);
        assert utilSettings != null;
        String selectedBackground = utilSettings.getString(UtilSettings.KEY_BACKGROUND);
        int selectedIndex = backgroundNames.indexOf(selectedBackground);
        dlgBackground = new MaterialDialog.Builder(ActSettings.this).title(R.string.setting_background).items(R.array.backgrounds).alwaysCallSingleChoiceCallback().itemsCallbackSingleChoice(selectedIndex, (dialog, view, which, text) -> {
            String selection = backgroundNames.get(which);
            if (selection.equals("Wallpaper")) {
                utilSettings.save(UtilSettings.KEY_BACKGROUND, selection);
                sendBackgroundChangedBroadcast();
                if (settingsInterface != null) {
                    settingsInterface.onValuesUpdated();
                }
                dismissBackgroundDialog();
                showWallpaperPicker();
            } else if (selection.equals("Color")) {
                dismissBackgroundDialog();
                showBackgroundColorDialog();
            }
            return true;
        }).show();
    }

    public void showWallpaperPicker() {
        Intent intent = new Intent(Intent.ACTION_SET_WALLPAPER);
        startActivity(Intent.createChooser(intent, "Select Wallpaper"));
    }

    public void showBackgroundColorDialog() {
        if (utilSettings == null) {
            return;
        }
        ColorChooserDialog mBackgroundColorDialog = new ColorChooserDialog.Builder(this, R.string.setting_background_color).titleSub(R.string.setting_background_color).accentMode(false).doneButton(R.string.done).cancelButton(R.string.cancel).backButton(R.string.back).preselect(Color.parseColor(utilSettings.getString(UtilSettings.KEY_BACKGROUND_COLOR))).dynamicButtonColor(false).allowUserColorInputAlpha(false).tag(TAG_COLOR_BACKGROUND).show(this);
    }

    public void showHighlightColorDialog() {
        if (utilSettings == null) {
            return;
        }
        ColorChooserDialog mHighlightColorDialog = new ColorChooserDialog.Builder(this, R.string.setting_highlight_color).titleSub(R.string.setting_highlight_color).accentMode(true).doneButton(R.string.done).cancelButton(R.string.cancel).backButton(R.string.back).preselect(Color.parseColor(utilSettings.getString(UtilSettings.KEY_HIGHLIGHT_COLOR))).dynamicButtonColor(false).allowUserColorInputAlpha(false).tag(TAG_COLOR_HIGHLIGHT).show(this);
    }

    @Override
    public void onColorSelection(@NonNull ColorChooserDialog dialog, @ColorInt int selectedColor) {
        if (utilSettings == null) {
            return;
        }
        String hexColor = String.format("#%06X", selectedColor);
        if (dialog.tag().equals(TAG_COLOR_BACKGROUND)) {
            utilSettings.save(UtilSettings.KEY_BACKGROUND, "Color");
            utilSettings.save(UtilSettings.KEY_BACKGROUND_COLOR, hexColor);
            sendBackgroundChangedBroadcast();
        } else if (dialog.tag().equals(TAG_COLOR_HIGHLIGHT)) {
            utilSettings.save(UtilSettings.KEY_HIGHLIGHT_COLOR, hexColor);
        }
        if (settingsInterface != null) {
            settingsInterface.onValuesUpdated();
        }
    }

    @Override
    public void onColorChooserDismissed(@NonNull ColorChooserDialog dialog) {
    }

    private void dismissSortTypeDialog() {
        if (dlgSortType != null && dlgSortType.isShowing()) {
            dlgSortType.dismiss();
        }
    }

    private void dismissIconPackDialog() {
        if (dlgIconPack != null && dlgIconPack.isShowing()) {
            dlgIconPack.dismiss();
        }
    }

    private void dismissNightModeDialog() {
        if (dlgNightMode != null && dlgNightMode.isShowing()) {
            dlgNightMode.dismiss();
        }
    }

    private void dismissBackgroundDialog() {
        if (dlgBackground != null && dlgBackground.isShowing()) {
            dlgBackground.dismiss();
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
        if (adView != null) {
//            adView.destroy();
            destroyAdBanner(findViewById(R.id.flAd), adView);
        }
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
}