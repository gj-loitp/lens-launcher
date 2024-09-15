package com.mckimquyen.app;

import com.orm.SugarApp;
import com.mckimquyen.ext.ApplovinKt;
import com.mckimquyen.services.EditedObservable;
import com.mckimquyen.services.TaskSortApps;
import com.mckimquyen.services.TaskUpdateApps;
import com.mckimquyen.services.UpdatedObservable;

import java.util.Observable;
import java.util.Observer;

//TODO roy93~ keystore
//TODO roy93~ apply new logic applovin utils

//done
//bug khi toggle lock/unlock, hide/unhide ko work
//khi cai ung dung dau tien, bi loi UI light mode
//bug load apps vo cuc o tab APPS
//dialog policy first
//ic_launcher
//setting search icon pack
//check home default khi start launcher
//lock/unlock app
//github
//license
//showMediationDebuggerApplovin
//switch customized like ios
//internal webview
//changelog view
//font scale
//app launcher uninstall app
//app launcher app infor

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
