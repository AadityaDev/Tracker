package com.skybee.tracker.network;

import android.support.annotation.NonNull;

import java.util.concurrent.ThreadFactory;

public class NamedThreadFactory implements ThreadFactory {

    private String name;

    public NamedThreadFactory(@NonNull String name) {
        this.name = name;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(r, name);
        t.setDaemon(true);
        return t;
    }
}
