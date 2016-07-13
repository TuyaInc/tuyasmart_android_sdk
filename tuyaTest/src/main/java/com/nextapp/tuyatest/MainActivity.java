package com.nextapp.tuyatest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.tuya.smart.android.device.TuyaSmartDevice;
import com.tuya.smart.android.device.mqtt.IMqttCallback;
import com.tuya.smart.android.user.TuyaSmartUserManager;
import com.tuya.smart.android.user.api.ILogoutCallback;
import com.tuya.smart.android.user.api.IReNickNameCallback;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends Activity {

    private TuyaSmartDevice mTuyaSmartDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        /**
         * 用户登录成功才能初始化设备对象
         */
        mTuyaSmartDevice = TuyaSmartDevice.getInstance().initDeviceList(new IMqttCallback() {
            @Override
            public void onConnectSuccess() {
                //设备通信初始化成功
            }

            @Override
            public void onConnectError(int code, String error) {
                //设备通信初始化失败
            }

            @Override
            public void onSubscribeSuccess(String topic) {
            }


            @Override
            public void onSubscribeError(String topic, int code, String error) {
            }

        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /**
         * 退出设备管理界面的时候请调用销毁方法
         */
        if (mTuyaSmartDevice != null) {
            mTuyaSmartDevice.onDestroy();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @OnClick(R.id.btn_ez_start)
    public void onClickEZStart() {
        startActivity(new Intent(MainActivity.this, EZActivityTest.class));
    }


    /**
     * 发送控制指令。回调函数会通知指令是否下发成功。注意：下发不代表硬件会执行该命令。
     */
    @OnClick(R.id.btn_dev_list)
    public void onClickDevList() {
        if (TuyaSmartUserManager.getInstance().isLogin()) {

            startActivity(new Intent(MainActivity.this, DeviceListActivity.class));
        } else {
            Toast.makeText(MainActivity.this, R.string.please_login, Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.btn_rename)
    public void onClickRNName() {
        TuyaSmartUserManager.getInstance().reRickName("Xiao Li", new IReNickNameCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(MainActivity.this, R.string.reNickName + R.string.unit_success, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String code, String error) {
                Toast.makeText(MainActivity.this, R.string.reNickName + R.string.unit_failure, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick(R.id.btn_share_list)
    public void onClickShareList() {
        startActivity(new Intent(MainActivity.this, ShareActivity.class));
    }

    @OnClick(R.id.btn_logout)
    public void onClickLogout() {
        /**
         * 退出登录接口。请在用户退出的时候调用。
         */
        TuyaSmartUserManager.getInstance().logout(new ILogoutCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(MainActivity.this, R.string.logout + R.string.unit_success, Toast.LENGTH_SHORT).show();
                TuyaSmartUserManager.getInstance().removeUser();
                TuyaSmartDevice.getInstance().onDestroy();
                finish();
            }

            @Override
            public void onError(String code, String error) {
                Toast.makeText(MainActivity.this, R.string.logout + R.string.unit_failure, Toast.LENGTH_SHORT).show();
                TuyaSmartUserManager.getInstance().removeUser();
                TuyaSmartDevice.getInstance().onDestroy();
                finish();
            }
        });
    }

    @OnClick(R.id.btn_ap_start)
    public void onClickAPStart() {
        startActivity(new Intent(MainActivity.this, APActivityTest.class));
    }


}

