package com.mckimquyen.app;

import com.orm.SugarApp;
import com.mckimquyen.ext.ApplovinKt;
import com.mckimquyen.services.EditedObservable;
import com.mckimquyen.services.TaskSortApps;
import com.mckimquyen.services.TaskUpdateApps;
import com.mckimquyen.services.UpdatedObservable;

import java.util.Observable;
import java.util.Observer;

//TODO roy93~ app launcher uninstall app
//TODO roy93~ app launcher app infor
//TODO roy93~ ic_launcher
//TODO roy93~ dialog policy first
//TODO roy93~ keystore
//TODO roy93~ apply new logic applovin utils

//done
//setting search icon pack
//check home default khi start launcher
//lock/unlock app
//github
//license
//showMediationDebuggerApplovin
//switch customized like ios
//internal webview
//changelog view

//2023.03.18 tried to convert to kotlin but failed
public class RApplication extends SugarApp implements Observer {

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
