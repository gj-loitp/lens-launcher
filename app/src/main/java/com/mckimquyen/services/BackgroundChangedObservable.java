package com.mckimquyen.services;

import java.util.Observable;

//2023.03.19 tried to convert kotlin but failed
public class BackgroundChangedObservable extends Observable {
    private static final BackgroundChangedObservable instance = new BackgroundChangedObservable();

    public static BackgroundChangedObservable getInstance() {
        return instance;
    }

    private BackgroundChangedObservable() {
    }

    public void updateValue(Object data) {
        synchronized (this) {
            setChanged();
            notifyObservers(data);
        }
    }

    public void update() {
        updateValue(null);
    }
}
