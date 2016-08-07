package com.skybee.tracker.network;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ExecutorUtils {
    private static final int BG_POOL_SIZE = 3;
    private static final int DATABASE_POOL_SIZE = 1; // only one database thread for sync issues.
    private static final String APP_THREAD = "app_thread";
    private static final String BACKGROUND_THREAD = "background_thread";
    private static final String DATABASE_THREAD = "db_thread";

    private static Executor uiExecutorThread = new UIThreadExecutor();
    private static ListeningExecutorService appThreadPool = MoreExecutors.listeningDecorator(Executors.newSingleThreadExecutor(new NamedThreadFactory(APP_THREAD)));
    private static ListeningExecutorService backgroundThreadPool = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(BG_POOL_SIZE, new NamedThreadFactory(BACKGROUND_THREAD)));
    private static ListeningExecutorService dbThreadPool = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(DATABASE_POOL_SIZE, new NamedThreadFactory(DATABASE_THREAD)));

    public static ListeningExecutorService getAppThread() {
        return appThreadPool;
    }

    public static Executor getUIThread() {
        return uiExecutorThread;
    }

    public static ListeningExecutorService getBackgroundPool() {
        return backgroundThreadPool;
    }

    public static ListeningExecutorService getDatabaseThread() {
        return dbThreadPool;
    }
}
