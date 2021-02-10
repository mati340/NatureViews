package com.colman.natureviews;

import android.app.Application;
import android.content.Context;

public class NatureViewsApplication extends Application {

    static public Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
}
