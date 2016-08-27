package com.tuya.smart.android.demo.presenter;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;

import com.tuya.smart.android.common.utils.NetworkUtil;
import com.tuya.smart.android.demo.R;
import com.tuya.smart.android.demo.activity.ECActivity;
import com.tuya.smart.android.demo.activity.ECBindActivity;
import com.tuya.smart.android.demo.test.utils.DialogUtil;
import com.tuya.smart.android.demo.utils.ActivityUtils;
import com.tuya.smart.android.demo.utils.ToastUtil;
import com.tuya.smart.android.demo.view.IECView;
import com.tuya.smart.android.device.utils.WiFiUtil;
import com.tuya.smart.android.mvp.presenter.BasePresenter;

/**
 * Created by letian on 15/6/29.
 */
public class ECPresenter extends BasePresenter {
    //    public static final int REQUEST_CODE = 12;
    public static final String TAG = "ECPresenter";
    private final int mMode;

    private Activity mActivity;
    private IECView mView;

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context c, final Intent intent) {
            if (intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
                checkWifiNetworkStatus();
            }
        }
    };

    public void checkWifiNetworkStatus() {
        if (NetworkUtil.isNetworkAvailable(mActivity)) {
            String currentSSID = WiFiUtil.getCurrentSSID(mActivity);
            if (!TextUtils.isEmpty(currentSSID)) {
                mView.setWifiSSID(currentSSID);
                if (is5GHz(currentSSID, mActivity)) {
                    mView.show5gTip();
                } else {
                    mView.hide5gTip();
                }
                return;
            }
        }
        mView.showNoWifi();
    }

    public ECPresenter(Activity activity, IECView view) {
        mActivity = activity;
        mView = view;
        mMode = activity.getIntent().getIntExtra(ECActivity.CONFIG_MODE, ECActivity.EC_MODE);
        initWifi();
    }

    public void closeECModeActivity() {
        mActivity.onBackPressed();
    }

    private void initWifi() {
        registerWifiReceiver();
    }

    public void goNextStep() {
        final String passWord = mView.getWifiPass();
        final String ssid = WiFiUtil.getCurrentSSID(mActivity);
        if (!NetworkUtil.isNetworkAvailable(mActivity) || TextUtils.isEmpty(ssid)) {
            ToastUtil.showToast(mActivity, R.string.connect_phone_to_network);
        } else {
            //对密码进行加密处理

            if (!is5GHz(ssid, mActivity)) {
                gotoBindDeviceActivity(ssid, passWord);
            } else {
                DialogUtil.customDialog(mActivity, null, mActivity.getString(R.string.ez_notSupport_5G_tip)
                        , mActivity.getString(R.string.ez_notSupport_5G_change), mActivity.getString(R.string.ez_notSupport_5G_continue), null, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case DialogInterface.BUTTON_POSITIVE:
                                        userOtherWifi();
                                        break;
                                    case DialogInterface.BUTTON_NEGATIVE:
                                        gotoBindDeviceActivity(ssid, passWord);
                                        break;
                                }
                            }
                        }).show();
            }
        }
    }

    public static boolean is5GHz(String ssid, Context context) {
        WifiManager wifiManger = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManger.getConnectionInfo();
        if (wifiInfo != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int freq = wifiInfo.getFrequency();
            return freq > 4900 && freq < 5900;
        } else return ssid.toUpperCase().endsWith("5G");
    }

    private void gotoBindDeviceActivity(String ssid, String passWord) {
        Intent intent = new Intent(mActivity, ECBindActivity.class);
        intent.putExtra(ECActivity.CONFIG_PASSWORD, passWord);
        intent.putExtra(ECActivity.CONFIG_SSID, ssid);
        intent.putExtra(ECActivity.CONFIG_MODE, mMode);
        ActivityUtils.startActivity(mActivity, intent, ActivityUtils.ANIMATE_FORWARD, true);
    }

    private void registerWifiReceiver() {
        try {
            mActivity.registerReceiver(mBroadcastReceiver, new IntentFilter(
                    WifiManager.NETWORK_STATE_CHANGED_ACTION));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void unRegisterWifiReceiver() {
        try {
            if (mBroadcastReceiver != null) {
                mActivity.unregisterReceiver(mBroadcastReceiver);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unRegisterWifiReceiver();
    }

    public void userOtherWifi() {
        Intent wifiSettingsIntent = new Intent("android.settings.WIFI_SETTINGS");
        if (null != wifiSettingsIntent.resolveActivity(mActivity.getPackageManager())) {
            mActivity.startActivity(wifiSettingsIntent);
        } else {
            wifiSettingsIntent = new Intent(Settings.ACTION_WIFI_SETTINGS);
            if (null != wifiSettingsIntent.resolveActivity(mActivity.getPackageManager())) {
                mActivity.startActivity(wifiSettingsIntent);
            }
        }
    }
}
