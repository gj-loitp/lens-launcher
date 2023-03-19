package com.roy.sv;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.roy.app.AppsSingleton;
import com.roy.model.App;
import com.roy.util.AppSorter;
import com.roy.util.Settings;

import java.util.ArrayList;
import java.util.Objects;

//2023.03.19 tried to convert kotlin but failed
public class TaskSortApps extends AsyncTask<Void, Void, Void> {

    private final Application mApplication;
    private final Settings mSettings;

    private ArrayList<App> mApps;
    private ArrayList<Bitmap> mAppIcons;

    public TaskSortApps(Context context, Application application) {
        mApplication = application;
        mSettings = new Settings(context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... params) {
        ArrayList<App> apps = Objects.requireNonNull(AppsSingleton.getInstance()).getApps();
        AppSorter.sort(apps, mSettings.getSortType());
        mApps = new ArrayList<>();
        mAppIcons = new ArrayList<>();
        for (int i = 0; i < Objects.requireNonNull(apps).size(); i++) {
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
