package com.nextapp.tuyatest;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.tuya.smart.android.device.TuyaSmartEasyConnect;
import com.tuya.smart.android.device.api.response.GwDevResp;
import com.tuya.smart.android.device.link.EZConfigBuilder;
import com.tuya.smart.android.device.link.IConnectListener;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by letian on 16/7/13.
 */
public class EZActivityTest extends Activity implements IConnectListener {
    private TuyaSmartEasyConnect mTuyaSmartEasyConnect;

    @Bind(R.id.demo_log)
    public TextView mDemoLogTextView;

    @Bind(R.id.tv_ssid)
    public TextView mSSid;

    @Bind(R.id.et_password)
    public EditText mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ez_test);
        ButterKnife.bind(this);

    }


    public String getSsid() {

        ConnectivityManager connManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (networkInfo.isConnected()) {
            final WifiManager wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
            final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
            if (connectionInfo != null) {
                String ssid = connectionInfo.getSSID();
                if (Build.VERSION.SDK_INT >= 17 && ssid.startsWith("\"") && ssid.endsWith("\""))
                    ssid = ssid.replaceAll("^\"|\"$", "");
                return ssid;
            }
        }
        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSSid.setText(getSsid());
    }

    @OnClick(R.id.btn_ez_cancel)
    public void onClickCancelConfig() {
        if (mTuyaSmartEasyConnect != null) mTuyaSmartEasyConnect.stopConfig();

    }

    @OnClick(R.id.btn_ez_start)
    public void onClickStartConfig() {
        mDemoLogTextView.setText("");
        mTuyaSmartEasyConnect = new TuyaSmartEasyConnect(new EZConfigBuilder(EZActivityTest.this)
                .setSsid(mSSid.getText().toString())
                .setPasswd(mPassword.getText().toString())
                .setConnectListener(EZActivityTest.this));
        mTuyaSmartEasyConnect.startConfig();

    }

    @Override
    public void onConfigStart() {
        mDemoLogTextView.append("onConfigStart\n");
    }

    @Override
    public void onConfigEnd() {
        mDemoLogTextView.append("onConfigEnd\n");
    }

    @Override
    public void onWifiError(String errorCode) {
        mDemoLogTextView.append("wifi error: " + errorCode + "\n");
    }

    @Override
    public void onDeviceFind(String devId) {
        //发现设备
    }

    @Override
    public void onDeviceBindSuccess(GwDevResp device) {
        //设备注册到机智云
    }

    @Override
    public void onActiveSuccess(GwDevResp device) {
        mDemoLogTextView.append("onActiveSuccess: " + device.getGwId() + " \n");
    }

    @Override
    public void onActiveError(String code, String error) {
        mDemoLogTextView.append("onActiveError: " + error + " \n");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /**
         * 配置结束后请调用销毁方法
         */
        if (mTuyaSmartEasyConnect != null) mTuyaSmartEasyConnect.onDestroy();

    }
}
