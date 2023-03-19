package com.roy.app;

import com.orm.SugarApp;
import com.roy.bkg.EditedObservable;
import com.roy.bkg.SortAppsTask;
import com.roy.bkg.UpdateAppsTask;
import com.roy.bkg.UpdatedObservable;

import java.util.Observable;
import java.util.Observer;

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
        new UpdateAppsTask(
                getPackageManager(),
                getApplicationContext(),
                this)
                .execute();
    }

    private void editApps() {
        new SortAppsTask(
                getApplicationContext(),
                this)
                .execute();
    }
}
