package com.tuya.smart.android.demo;

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

import com.tuya.smart.android.common.utils.L;
import com.tuya.smart.android.device.TuyaSmartEasyConnect;
import com.tuya.smart.android.device.api.response.GwDevResp;
import com.tuya.smart.android.device.config.ConfigDeviceErrorCode;
import com.tuya.smart.android.device.link.IConnectListener;
import com.tuya.smart.sdk.TuyaActivator;
import com.tuya.smart.sdk.api.ITuyaActivator;
import com.tuya.smart.sdk.api.ITuyaActivatorGetToken;
import com.tuya.smart.sdk.api.ITuyaSmartActivatorListener;
import com.tuya.smart.sdk.builder.ActivatorBuilder;
import com.tuya.smart.sdk.enums.ActivatorModelEnum;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by letian on 16/7/13.
 */
public class EZActivityTest extends Activity {
    private static final String TAG = "EZActivityTest ggg";
    @Bind(R.id.demo_log)
    public TextView mDemoLogTextView;

    @Bind(R.id.tv_ssid)
    public TextView mSSid;

    @Bind(R.id.et_password)
    public EditText mPassword;
    private ITuyaActivator mITuyaActivator;

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
        if (mITuyaActivator != null) {
            mITuyaActivator.stop();
            mDemoLogTextView.append("CancelConfig");
        }
    }

    @OnClick(R.id.btn_ez_start)
    public void onClickStartConfig() {
        mDemoLogTextView.setText("");
        TuyaActivator.getInstance().getActivatorToken(new ITuyaActivatorGetToken() {
            @Override
            public void onSuccess(String token) {
                mITuyaActivator = TuyaActivator.getInstance().newActivator(new ActivatorBuilder().setActivatorModel(ActivatorModelEnum.TY_EZ)
                        .setPassword(mPassword.getText().toString())
                        .setSsid(mSSid.getText().toString())
                        .setToken(token)
                        .setContext(EZActivityTest.this)
                        .setListener(new ITuyaSmartActivatorListener() {
                            @Override
                            public void onError(String errorCode, String errorMsg) {
                                mDemoLogTextView.append("onActiveError: " + errorCode + " \n");
                                L.d(TAG, "errorCode: " + errorCode);
                            }

                            @Override
                            public void onActiveSuccess(GwDevResp devResp) {
                                mDemoLogTextView.append("onActiveSuccess: " + devResp.getGwId() + " \n");
                            }

                            @Override
                            public void onStep(String step, Object data) {
                                mDemoLogTextView.append("Step:" + step + "\n");
                            }
                        }));
                L.d(TAG, "startConfig");
                mITuyaActivator.start();
            }

            @Override
            public void onFailure(String errorCode, String errorMsg) {

            }
        });

        mDemoLogTextView.append("onConfigStart\n");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /**
         * 配置结束后请调用销毁方法
         */
        if (mITuyaActivator != null) mITuyaActivator.onDestroy();
    }
}
