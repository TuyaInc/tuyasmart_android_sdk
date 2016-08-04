package com.nextapp.tuyatest;

import android.app.Application;

import com.tuya.smart.sdk.TuyaSdk;

public class TuyaSmartApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        /**
         * 初始化sdk
         */
        TuyaSdk.init(this);
        TuyaSdk.setDebugMode(true);
    }
}
