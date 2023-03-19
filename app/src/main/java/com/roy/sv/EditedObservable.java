package com.roy.sv;

import java.util.Observable;

public class EditedObservable extends Observable {
    private static final EditedObservable instance = new EditedObservable();

    public static EditedObservable getInstance() {
        return instance;
    }

    private EditedObservable() {
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
