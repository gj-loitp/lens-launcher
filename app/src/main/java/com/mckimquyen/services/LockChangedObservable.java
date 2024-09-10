package com.mckimquyen.services;

import java.util.Observable;

//2023.03.19 tried to convert kotlin but failed
public class LockChangedObservable extends Observable {
    private static final LockChangedObservable instance = new LockChangedObservable();

    public static LockChangedObservable getInstance() {
        return instance;
    }

    private LockChangedObservable() {
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
