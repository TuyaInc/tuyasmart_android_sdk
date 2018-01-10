package com.tuya.smart.android.demo.utils;

import android.text.TextUtils;

import com.tuya.smart.android.demo.config.CommonConfig;
import com.tuya.smart.android.device.utils.WiFiUtil;
import com.tuya.smart.sdk.TuyaSdk;

/**
 * Created by letian on 16/6/3.
 */
public class BindDeviceUtils {

    public static boolean isAPMode() {
        String ssid = "";
        if (TextUtils.isEmpty(ssid)) {
            ssid = CommonConfig.DEFAULT_COMMON_AP_SSID;
        }
        String currentSSID = WiFiUtil.getCurrentSSID(TuyaSdk.getApplication()).toLowerCase();
        return !TextUtils.isEmpty(currentSSID) && (currentSSID.startsWith(ssid.toLowerCase()) ||
                currentSSID.startsWith(CommonConfig.DEFAULT_OLD_AP_SSID.toLowerCase()) ||
                currentSSID.contains(CommonConfig.DEFAULT_KEY_AP_SSID.toLowerCase()));
    }
}
