package com.roy.app;

import com.orm.SugarApp;
import com.roy.sv.EditedObservable;
import com.roy.sv.TaskSortApps;
import com.roy.sv.TaskUpdateApps;
import com.roy.sv.UpdatedObservable;

import java.util.Observable;
import java.util.Observer;

//TODO ic_launcher
//TODO keystore
//TODO admob
//TODO remove butter knife

//2023.03.18 tried to convert to kotlin but failed
public class MyApplication extends SugarApp implements Observer {

    @Override
    public void onCreate() {
        super.onCreate();

        UpdatedObservable.getInstance().addObserver(this);
        EditedObservable.getInstance().addObserver(this);
        updateApps();
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
