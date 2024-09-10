package com.mckimquyen.app;

import com.orm.SugarApp;
import com.mckimquyen.ext.ApplovinKt;
import com.mckimquyen.services.EditedObservable;
import com.mckimquyen.services.TaskSortApps;
import com.mckimquyen.services.TaskUpdateApps;
import com.mckimquyen.services.UpdatedObservable;

import java.util.Observable;
import java.util.Observer;

//TODO app launcher uninstall app
//TODO app launcher app infor
//TODO lock/unlock app

//done
//ic_launcher
//setting search icon pack
//check home default khi start launcher
//dialog policy first
//firebase
//keystore
//lock/unlock app
//github
//license
//apply new logic applovin utils
//showMediationDebuggerApplovin
//switch customized like ios
//internal webview
//changelog view

//https://console.firebase.google.com/u/0/project/lenslauncher-25806/overview
//2023.03.18 tried to convert to kotlin but failed
public class MyApplication extends SugarApp implements Observer {

    @Override
    public void onCreate() {
        super.onCreate();

        UpdatedObservable.getInstance().addObserver(this);
        EditedObservable.getInstance().addObserver(this);
        updateApps();

        ApplovinKt.setupApplovinAd(this);
    }

    @Override
    public void update(Observable observable, Object data) {
        if (observable instanceof UpdatedObservable) {
            updateApps();
        } else if (observable instanceof EditedObservable) {
            editApps();
        }
    }

    private void updateApps() {
        new TaskUpdateApps(
                getPackageManager(),
                getApplicationContext(),
                this)
                .execute();
    }

    private void editApps() {
        new TaskSortApps(
                getApplicationContext(),
                this)
                .execute();
    }
}
