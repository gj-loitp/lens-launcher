package com.roy.bkg;

import java.util.Observable;

//2023.03.19 tried to convert kotlin but failed
public class UpdatedObservable extends Observable {
    private static final UpdatedObservable instance = new UpdatedObservable();

    public static UpdatedObservable getInstance() {
        return instance;
    }

    private UpdatedObservable() {
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
