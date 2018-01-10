package com.tuya.smart.android.demo.activity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tuya.smart.android.demo.R;
import com.tuya.smart.android.demo.utils.ActivityUtils;


/**
 * 热点模式: 添加设备页面
 * <p>
 * Created by sunch on 16/6/21.
 */
public class AddDeviceAPTipActivity extends BaseActivity implements View.OnClickListener {
    private static String TAG = "AddDeviceAPTipActivity";
    public static final String FROM_EZ_FAILURE = "FROM_EZ_FAILURE";

    private TextView mStatusLightTip;
    private Button mStatusLightOption;
    private Button mStatusLightHelp;
    private ImageView mStatusLightImageView;
    private AnimationDrawable mStatusLightAnimation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device_tip);
        initToolbar();
        initView();
        setTitle(R.string.choose_wifi);
        setDisplayHomeAsUpEnabled();
    }

    private void initView() {
        mStatusLightTip = (TextView) findViewById(R.id.status_light_tip);
        mStatusLightTip.setText(R.string.ty_add_device_ap_info);

        initTipImageview();

        mStatusLightOption = (Button) findViewById(R.id.status_light_option);
        mStatusLightOption.setText(R.string.ty_add_device_ap_btn_info);
        mStatusLightOption.setOnClickListener(this);
        mStatusLightHelp = (Button) findViewById(R.id.status_light_help);
        mStatusLightHelp.setOnClickListener(this);
        if (getIntent().getBooleanExtra(FROM_EZ_FAILURE, false)) {
            ((TextView) findViewById(R.id.status_light_tip)).setText(R.string.ty_add_device_ez_failure_ap_tip);
        }
    }

    private void initTipImageview() {
        mStatusLightImageView = (ImageView) findViewById(R.id.status_light_imageview);
        mStatusLightAnimation = new AnimationDrawable();
        mStatusLightAnimation.addFrame(ContextCompat.getDrawable(this, R.drawable.ty_adddevice_lighting), 1500);
        mStatusLightAnimation.addFrame(ContextCompat.getDrawable(this, R.drawable.ty_adddevice_light), 1500);
        mStatusLightAnimation.setOneShot(false);
        mStatusLightImageView.setImageDrawable(mStatusLightAnimation);
        mStatusLightAnimation.start();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.status_light_option) {
            // 切换至设备wifi工作页面
            gotoNextActivity();
        } else if (v.getId() == R.id.status_light_help) {
            // 切换至帮助页面
            ActivityUtils.gotoAddDeviceHelpActivity(this, getString(R.string.ty_ez_help));
        }
    }

    private void gotoNextActivity() {
        if (getIntent().getBooleanExtra(FROM_EZ_FAILURE, false)) {
            gotoApTipActivity();
        } else {
            gotoWifiInputActivity();
        }
    }

    private void gotoWifiInputActivity() {
        // 切换至设备wifi工作页面
        Intent intent = new Intent(AddDeviceAPTipActivity.this, ECActivity.class);
        intent.putExtra(ECActivity.CONFIG_MODE, ECActivity.AP_MODE);
        ActivityUtils.startActivity(AddDeviceAPTipActivity.this, intent, ActivityUtils.ANIMATE_FORWARD, true);
    }

    private void gotoApTipActivity() {
        Intent getIntent = getIntent();
        Intent intent = new Intent(this, ECBindActivity.class);
        intent.putExtra(ECActivity.CONFIG_PASSWORD, getIntent.getStringExtra(ECActivity.CONFIG_PASSWORD));
        intent.putExtra(ECActivity.CONFIG_SSID, getIntent.getStringExtra(ECActivity.CONFIG_SSID));
        intent.putExtra(ECActivity.CONFIG_MODE, ECActivity.AP_MODE);
        ActivityUtils.startActivity(this, intent, ActivityUtils.ANIMATE_FORWARD, true);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, AddDeviceTipActivity.class);
        ActivityUtils.startActivity(this, intent, ActivityUtils.ANIMATE_BACK, true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mStatusLightAnimation != null && mStatusLightAnimation.isRunning()) {
            mStatusLightAnimation.stop();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mStatusLightAnimation != null && !mStatusLightAnimation.isRunning()) {
            mStatusLightAnimation.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mStatusLightAnimation != null && mStatusLightAnimation.isRunning()) {
            mStatusLightAnimation.stop();
        }
    }
}
