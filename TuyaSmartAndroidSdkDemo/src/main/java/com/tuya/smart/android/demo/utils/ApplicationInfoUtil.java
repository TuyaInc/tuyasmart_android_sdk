package com.tuya.smart.android.demo.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

/**
 * Created by zsg on 17/9/19.
 */

public class ApplicationInfoUtil {
    public static String getInfo(String infoName, Context context){
        ApplicationInfo e = null;
        try {
            e = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128);
            String info = e.metaData.getString(infoName);
            return info;
        } catch (PackageManager.NameNotFoundException e1) {
            e1.printStackTrace();
        }
        return "";
    }

}
