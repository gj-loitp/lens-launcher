package com.mckimquyen.sv;

import java.util.Observable;

//2023.03.19 tried to convert kotlin but failed
public class VisibilityChangedObservable extends Observable {
    private static final VisibilityChangedObservable instance = new VisibilityChangedObservable();

    public static VisibilityChangedObservable getInstance() {
        return instance;
    }

    private VisibilityChangedObservable() {
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
