package com.tuya.smart.android.demo;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.tuya.smart.android.demo.activity.LoginActivity;
import com.tuya.smart.sdk.TuyaSdk;
import com.tuya.smart.sdk.api.INeedLoginListener;

public class TuyaSmartApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        /**
         * 初始化sdk
         */
        TuyaSdk.init(this);
        TuyaSdk.setOnNeedLoginListener(new INeedLoginListener() {
            @Override
            public void onNeedLogin(Context context) {
                Intent intent = new Intent(context, LoginActivity.class);
                if(!(context instanceof Activity)){
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                startActivity(intent);
            }
        });
        TuyaSdk.setDebugMode(true);
    }
}
