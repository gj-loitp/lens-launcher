package com.roy.app;

import com.orm.SugarApp;
import com.roy.ext.Applovin;
import com.roy.sv.EditedObservable;
import com.roy.sv.TaskSortApps;
import com.roy.sv.TaskUpdateApps;
import com.roy.sv.UpdatedObservable;

import java.util.Observable;
import java.util.Observer;

//done
//ic_launcher
//setting search icon pack
//check home default khi start launcher
//dialog policy first
//firebase
//keystore
//lock/unlock app

//https://console.firebase.google.com/u/0/project/lenslauncher-25806/overview
//2023.03.18 tried to convert to kotlin but failed
public class MyApplication extends SugarApp implements Observer {

    @Override
    public void onCreate() {
        super.onCreate();

        UpdatedObservable.getInstance().addObserver(this);
        EditedObservable.getInstance().addObserver(this);
        updateApps();

        Applovin.INSTANCE.setupAd(this);
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
