package com.roy.itf;

import com.roy.model.App;

import java.util.ArrayList;

public interface AppsInterface {
    void onDefaultsReset();

    void onAppsUpdated(ArrayList<App> apps);
}
