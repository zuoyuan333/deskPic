package com.zy.deskpic;

import android.app.Application;
import android.util.DisplayMetrics;

public class ZyApplication extends Application {
    private static Application application;
    private static DisplayMetrics mMetrics;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
    }

    public static Application getInstance() {
        return application;
    }

    /**
     * 获取手机Metrics
     */
    public static DisplayMetrics getMetrics() {
        if (mMetrics == null) {
            mMetrics = application.getResources().getDisplayMetrics();
        }
        return mMetrics;
    }
}
