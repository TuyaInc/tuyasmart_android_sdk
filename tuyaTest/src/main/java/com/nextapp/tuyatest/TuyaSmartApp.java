package com.nextapp.tuyatest;

import android.app.Application;

import com.tuya.smart.android.base.TuyaSmartSdk;
import com.tuya.smart.android.common.utils.L;

public class TuyaSmartApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        TuyaSmartSdk.init(this);
        L.setLogSwitcher(true);
    }
}
