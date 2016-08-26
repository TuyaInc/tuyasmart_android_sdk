package com.nextapp.tuyatest.activity;

import android.app.Activity;
import android.os.Bundle;

import com.nextapp.tuyatest.utils.ActivityUtils;
import com.nextapp.tuyatest.utils.LoginHelper;
import com.tuya.smart.sdk.TuyaUser;

/**
 * Created by letian on 16/7/19.
 */
public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (TuyaUser.getUserInstance().isLogin()) {//已登录，跳主页
            LoginHelper.afterLogin();
            ActivityUtils.gotoHomeActivity(this);
        } else {
            ActivityUtils.gotoActivity(this, LoginActivity.class, ActivityUtils.ANIMATE_FORWARD, true);
        }
    }
}
