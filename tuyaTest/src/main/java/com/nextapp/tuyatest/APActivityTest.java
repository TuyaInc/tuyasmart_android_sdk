package com.nextapp.tuyatest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tuya.smart.android.common.utils.L;
import com.tuya.smart.android.device.TuyaSmartApConnect;
import com.tuya.smart.android.device.api.response.GwDevResp;
import com.tuya.smart.android.device.link.APConfigBuilder;
import com.tuya.smart.android.device.link.IApConnectListener;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by letian on 15/12/22.
 */
public class APActivityTest extends Activity {
    private static final String TAG = "ApConfigggg";
    TuyaSmartApConnect tuyaSmartApConnect;
    @Bind(R.id.et_ssid)
    EditText mEtSsid;
    @Bind(R.id.et_password)
    EditText mEtPassword;
    @Bind(R.id.btn_config_now)
    Button mBtnConfigNow;
//    @Bind(R.id.btn_config_udp)
//    Button mBtnConfigUDP;
    @Bind(R.id.ap_config_text)
    TextView mApConfigText;

    StringBuilder mConfigText = new StringBuilder();
    private boolean mStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ap);
        ButterKnife.bind(this);
//        IApConnectListener iApConnectListener = new IApConnectListener() {
//            @Override
//            public void onDeviceConnect(String gwId) {
//                //设备连接成功
//            }
//
//            @Override
//            public void onDeviceDisconnect(String gwId) {
//                //设备断开连接
//            }
//
//            @Override
//            public void onConfigSuccess() {
//                //ssid和密码发送成功
//            }
//
//            @Override
//            public void onConfigError(int code) {
//                //ssid和密码发送失败
//            }
//
//            @Override
//            public void onActiveCommandError(int code) {
//                //激活命令发送失败
//            }
//
//            @Override
//            public void onActiveCommandSuccess() {
//                //激活命令发送成功
//            }
//
//            @Override
//            public void onActiveSuccess(GwDevResp device) {
//                //设备激活成功
//            }
//
//            @Override
//            public void onDeviceBindSuccess(GwDevResp device) {
//                //设备注册到智能云
//            }
//
//            @Override
//            public void onActiveError(String code, String error) {
//                //激活失败
//            }
//        };
//        APConfigBuilder builder = new APConfigBuilder()
//                //wifi 的ssid名称
//                .setTargetSSIDPasswd("20112012pw")
//                //wifi 的密码
//                .setTargetSSID("tuya")
//                //TuyaLink配网监听回调
//                .setApConnectListener(iApConnectListener);
//        tuyaSmartApConnect = new TuyaSmartApConnect(builder);
//        //开始配网
//        tuyaSmartApConnect.start();
//        //取消配网
//        tuyaSmartApConnect.cancel();
//        //退出页面时，需要调用该方法，清理相关回调监听等。
//        tuyaSmartApConnect.onDestroy();

        tuyaSmartApConnect = new TuyaSmartApConnect(new APConfigBuilder().setTargetSSIDPasswd(mEtPassword.getText().toString()).setTargetSSID(mEtSsid.getText().toString()).setApConnectListener(new IApConnectListener() {
            @Override
            public void onDeviceConnect(String gwId) {
                //设备连接成功
                L.d(TAG, "onDeviceConnect " + gwId);
                mConfigText.append("onDeviceConnect ").append(gwId).append("\n");
                showApText();
            }


            @Override
            public void onDeviceDisconnect(String gwId) {
                //设备断开连接
                L.d(TAG, "onDeviceDisconnect " + gwId);
                mConfigText.append("onDeviceDisconnect ").append(gwId).append("\n");
                showApText();
            }

            @Override
            public void onConfigSuccess() {
                //ssid和密码发送成功
                L.d(TAG, "onConfigSuccess");
                mConfigText.append("onConfigSuccess \n");
                showApText();
            }

            @Override
            public void onConfigError(int code) {
                //ssid和密码发送失败
                L.d(TAG, "onConfigError: " + code);
                mConfigText.append("onConfigError: ").append(code).append("\n");
                showApText();
            }

            @Override
            public void onActiveCommandError(int code) {
                //激活命令发送失败
                L.d(TAG, "onActiveCommandError " + code);
                mConfigText.append("onActiveCommandError: ").append(code).append("\n");
                showApText();
            }

            @Override
            public void onActiveCommandSuccess() {
                //激活命令发送成功
                L.d(TAG, "onActiveCommandSuccess ");
                mConfigText.append("onActiveCommandSuccess: \n");
                showApText();
            }

            @Override
            public void onActiveSuccess(GwDevResp device) {
                //设备激活成功
                L.d(TAG, "onActiveSuccess  " + device.getGwId());
                mConfigText.append("onActiveSuccess: ").append(device.getGwId()).append("\n");
                mConfigText.append("设备激活成功");
                mBtnConfigNow.setEnabled(true);
                showApText();
            }

            @Override
            public void onDeviceBindSuccess(GwDevResp device) {
                //设备注册到智能云
            }

            @Override
            public void onActiveError(String code, String error) {
                //设备激活失败
                L.d(TAG, "onActiveError code " + code + " error" + error);
                mConfigText.append("设备激活失败 onActiveError code ").append(code).append(" error: ").append(error).append("\n");
                mBtnConfigNow.setEnabled(true);
                showApText();
            }
        }).setContext(this));

        //要先连上SmartTuya 的设备。才能开始配置
        findViewById(R.id.btn_config_now).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mStart) {
                    mStart = false;
                    mBtnConfigNow.setText("开始配置");
                    tuyaSmartApConnect.cancel();
                } else {
                    mStart = true;
                    mBtnConfigNow.setText("取消配置");
                    //联接设备WiFi。
                    tuyaSmartApConnect.start();
                }
            }
        });

//        // 配网协议3.0
//        mBtnConfigUDP.setEnabled(false);
//        tuyaSmartApConnect.prepare(new IPreparedListener() {
//            @Override
//            public void onCompletion() {
//                Toast.makeText(APActivityTest.this, "token获取成功,请先切换网络,再点击按钮配网", Toast.LENGTH_LONG).show();
//                L.d(TAG, "token获取成功");
//                mBtnConfigUDP.setEnabled(true);
//            }
//
//            @Override
//            public void onFailure() {
//                L.d(TAG, "token获取失败");
//                mBtnConfigUDP.setEnabled(false);
//            }
//        });
//
//        findViewById(R.id.btn_config_udp).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                tuyaSmartApConnect.start();
//            }
//        });

//        tuyaSmartApConnect.cancel();
    }


    private void showApText() {
        mApConfigText.setText(mConfigText);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //销毁处理
        tuyaSmartApConnect.onDestroy();
    }
}
