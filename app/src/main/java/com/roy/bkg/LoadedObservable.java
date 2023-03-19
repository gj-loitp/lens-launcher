package com.roy.bkg;

import java.util.Observable;

public class LoadedObservable extends Observable {
    private static final LoadedObservable instance = new LoadedObservable();

    public static LoadedObservable getInstance() {
        return instance;
    }

    private LoadedObservable() {
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
