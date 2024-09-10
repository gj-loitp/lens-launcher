package com.mckimquyen.services;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.mckimquyen.app.RAppsSingleton;
import com.mckimquyen.model.App;
import com.mckimquyen.util.UtilApp;
import com.mckimquyen.util.UtilSettings;

import java.util.ArrayList;
import java.util.Objects;

//2023.03.19 tried to convert kotlin but failed
public class TaskUpdateApps extends AsyncTask<Void, Void, Void> {

    private final PackageManager mPackageManager;
    private final Context mContext;
    private final Application mApplication;
    private final UtilSettings mUtilSettings;

    private ArrayList<App> mApps;
    private ArrayList<Bitmap> mAppIcons;

    public TaskUpdateApps(PackageManager packageManager,
                          Context context,
                          Application application) {
        this.mPackageManager = packageManager;
        this.mContext = context;
        this.mApplication = application;
        this.mUtilSettings = new UtilSettings(context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... params) {
        ArrayList<App> apps = UtilApp.getApps(
                mPackageManager,
                mContext,
                mApplication,
                Objects.requireNonNull(mUtilSettings.getString(UtilSettings.KEY_ICON_PACK_LABEL_NAME)),
                mUtilSettings.getSortType());
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
        Objects.requireNonNull(RAppsSingleton.getInstance()).setApps(mApps);
        RAppsSingleton.getInstance().setAppIcons(mAppIcons);
        Intent appsLoadedIntent = new Intent(mApplication, BroadcastReceivers.AppsLoadedReceiver.class);
        mApplication.sendBroadcast(appsLoadedIntent);
        super.onPostExecute(result);
    }
}
