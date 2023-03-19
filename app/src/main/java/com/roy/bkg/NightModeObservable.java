package com.roy.bkg;

import java.util.Observable;


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
