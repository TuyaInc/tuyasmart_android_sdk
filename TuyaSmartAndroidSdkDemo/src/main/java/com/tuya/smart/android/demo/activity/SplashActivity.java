package com.tuya.smart.android.demo.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.tuya.smart.android.demo.R;
import com.tuya.smart.android.demo.test.utils.DialogUtil;
import com.tuya.smart.android.demo.utils.ActivityUtils;
import com.tuya.smart.android.demo.utils.ApplicationInfoUtil;
import com.tuya.smart.android.demo.utils.LoginHelper;
import com.tuya.smart.sdk.TuyaSdk;
import com.tuya.smart.sdk.TuyaUser;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by letian on 16/7/19.
 */
public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //监测appkey是否存在
        if(isInitAppkey()){
            gotoLogin();
        }else {
            showTipDialog();
        }
    }

    public void gotoLogin(){
        if (TuyaUser.getUserInstance().isLogin()) {//已登录，跳主页
            LoginHelper.afterLogin();
            ActivityUtils.gotoHomeActivity(this);
        } else {
            ActivityUtils.gotoActivity(this, LoginActivity.class, ActivityUtils.ANIMATE_FORWARD, true);
        }
    }


    private void showTipDialog() {
        DialogUtil.simpleConfirmDialog(this, "appkey or appsecret is empty. \nPlease check your configuration", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
    }

    private boolean isInitAppkey() {
        String appkey= ApplicationInfoUtil.getInfo("TUYA_SMART_APPKEY",this);
        String appSecret = ApplicationInfoUtil.getInfo("TUYA_SMART_SECRET",this);
        if(TextUtils.isEmpty(appkey)||TextUtils.isEmpty(appSecret)){
            return false;
        }
        return true;
    }
}
