package com.tuya.smart.android.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.tuya.smart.android.user.api.ILogoutCallback;
import com.tuya.smart.android.user.api.IReNickNameCallback;
import com.tuya.smart.sdk.TuyaUser;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TuyaUser.getDeviceInstance().onDestroy();
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
        if (TuyaUser.getUserInstance().isLogin()) {
            startActivity(new Intent(MainActivity.this, DeviceListActivity.class));
        } else {
            Toast.makeText(MainActivity.this, getString(R.string.please_login), Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.btn_rename)
    public void onClickRNName() {
        TuyaUser.getUserInstance().reRickName("Xiao Li", new IReNickNameCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(MainActivity.this, getString(R.string.reNickName) + getString(R.string.unit_success), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String code, String error) {
                Toast.makeText(MainActivity.this, getString(R.string.reNickName) + getString(R.string.unit_failure), Toast.LENGTH_SHORT).show();
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
        TuyaUser.getUserInstance().logout(new ILogoutCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(MainActivity.this, getString(R.string.logout) + getString(R.string.unit_success), Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onError(String code, String error) {
                Toast.makeText(MainActivity.this, getString(R.string.logout) + getString(R.string.unit_failure), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @OnClick(R.id.btn_ap_start)
    public void onClickAPStart() {
        startActivity(new Intent(MainActivity.this, APActivityTest.class));
    }


}

