package com.mckimquyen.services;

import java.util.Observable;

//2023.03.19 tried to convert kotlin but failed
public class NightModeObservable extends Observable {
    private static final NightModeObservable instance = new NightModeObservable();

    public static NightModeObservable getInstance() {
        return instance;
    }

    private NightModeObservable() {
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
