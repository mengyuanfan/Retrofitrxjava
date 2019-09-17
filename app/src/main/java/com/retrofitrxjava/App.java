package com.retrofitrxjava;

import android.app.Application;

/**
 * Created by Administrator on 2019/9/17.
 */
public class App extends Application{
    private static App instance;
    public static synchronized App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

    }

}
