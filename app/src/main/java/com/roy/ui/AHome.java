package com.roy.ui;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.roy.R;
import com.roy.app.AppsSingleton;
import com.roy.model.App;
import com.roy.model.AppPersistent;
import com.roy.sv.BackgroundChangedObservable;
import com.roy.sv.LoadedObservable;
import com.roy.sv.NightModeObservable;
import com.roy.sv.VisibilityChangedObservable;
import com.roy.views.LensView;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Observable;
import java.util.Observer;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

//2023.03.19 tried to convert kotlin but failed
public class AHome extends ABase implements Observer {

    LensView lensViews;
    MaterialProgressBar progressBarHome;
    private ArrayList<App> listApp;
    private ArrayList<Bitmap> listAppIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_home);

        setupViews();

        PackageManager mPackageManager = getPackageManager();
        lensViews.setPackageManager(mPackageManager);
        lensViews.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        assignApps(Objects.requireNonNull(Objects.requireNonNull(AppsSingleton.getInstance()).getApps()), AppsSingleton.getInstance().getAppIcons());
        LoadedObservable.getInstance().addObserver(this);
        VisibilityChangedObservable.getInstance().addObserver(this);
        BackgroundChangedObservable.getInstance().addObserver(this);
        NightModeObservable.getInstance().addObserver(this);
    }

    private void setupViews() {
        lensViews = findViewById(R.id.lensViews);
        progressBarHome = findViewById(R.id.progressBarHome);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupTransparentSystemBarsForLollipop();
    }

    private void setupTransparentSystemBarsForLollipop() {
        Window window = getWindow();
        window.getAttributes().systemUiVisibility |= (View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
        window.setNavigationBarColor(Color.TRANSPARENT);
    }

    private void setBackground() {
        lensViews.invalidate();
    }

    private void assignApps(ArrayList<App> lApp, ArrayList<Bitmap> lAppIcon) {
        if (lApp.size() == 0 || lAppIcon.size() == 0) {
            return;
        }
        progressBarHome.setVisibility(View.INVISIBLE);
        lensViews.setVisibility(View.VISIBLE);
        listApp = lApp;
        listAppIcon = lAppIcon;
        removeHiddenApps();
        lensViews.setApps(listApp, listAppIcon);
    }

    private void removeHiddenApps() {
        for (int i = 0; i < listApp.size(); i++) {
            if (!AppPersistent.getAppVisibility(Objects.requireNonNull(listApp.get(i).getPackageName()).toString(), Objects.requireNonNull(listApp.get(i).getName()).toString())) {
                listApp.remove(i);
                listAppIcon.remove(i);
                i--;
            }
        }
    }

    @Override
    public void onBackPressed() {
        //do nothing
    }

    @Override
    protected void onDestroy() {
        LoadedObservable.getInstance().deleteObserver(this);
        super.onDestroy();
    }

    @Override
    public void update(Observable observable, Object data) {
        if (observable instanceof LoadedObservable || observable instanceof VisibilityChangedObservable) {
            assignApps(Objects.requireNonNull(
                            Objects.requireNonNull(AppsSingleton.getInstance()).getApps()),
                    AppsSingleton.getInstance().getAppIcons());
        } else if (observable instanceof BackgroundChangedObservable) {
            setBackground();
        } else if (observable instanceof NightModeObservable) {
            updateNightMode();
        }
    }
}
