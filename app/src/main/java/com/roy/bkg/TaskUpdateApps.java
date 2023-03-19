package com.roy.bkg;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.roy.app.AppsSingleton;
import com.roy.model.App;
import com.roy.util.AppUtil;
import com.roy.util.Settings;

import java.util.ArrayList;
import java.util.Objects;

//2023.03.19 tried to convert kotlin but failed
public class TaskUpdateApps extends AsyncTask<Void, Void, Void> {

    private final PackageManager mPackageManager;
    private final Context mContext;
    private final Application mApplication;
    private final Settings mSettings;

    private ArrayList<App> mApps;
    private ArrayList<Bitmap> mAppIcons;

    public TaskUpdateApps(PackageManager packageManager,
                          Context context,
                          Application application) {
        this.mPackageManager = packageManager;
        this.mContext = context;
        this.mApplication = application;
        this.mSettings = new Settings(context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... params) {
        ArrayList<App> apps = AppUtil.getApps(
                mPackageManager,
                mContext,
                mApplication,
                mSettings.getString(Settings.KEY_ICON_PACK_LABEL_NAME),
                mSettings.getSortType());
        mApps = new ArrayList<>();
        mAppIcons = new ArrayList<>();
        for (int i = 0; i < apps.size(); i++) {
            App app = apps.get(i);
            Bitmap appIcon = app.getIcon();
            if (appIcon != null) {
                mApps.add(app);
                mAppIcons.add(appIcon);
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        Objects.requireNonNull(AppsSingleton.getInstance()).setApps(mApps);
        AppsSingleton.getInstance().setAppIcons(mAppIcons);
        Intent appsLoadedIntent = new Intent(mApplication, BroadcastReceivers.AppsLoadedReceiver.class);
        mApplication.sendBroadcast(appsLoadedIntent);
        super.onPostExecute(result);
    }
}