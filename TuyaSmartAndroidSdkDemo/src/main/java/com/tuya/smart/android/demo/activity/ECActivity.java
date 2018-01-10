package com.tuya.smart.android.demo.activity;

import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.tuya.smart.android.demo.R;
import com.tuya.smart.android.demo.presenter.ECPresenter;
import com.tuya.smart.android.demo.view.IECView;
import com.tuya.smart.sdk.TuyaSdk;
import com.wnafee.vector.compat.VectorDrawable;

/**
 * Created by letian on 15/6/29.
 */
public class ECActivity extends BaseActivity implements IECView {

    public static final String CONFIG_MODE = "config_mode";
    public static final String CONFIG_PASSWORD = "config_password";
    public static final String CONFIG_SSID = "config_ssid";
    private static final String TAG = "ECActivity";
    public static final int AP_MODE = 0;
    public static final int EC_MODE = 1;

    public TextView mSSID;
    public EditText mPassword;
    public ImageButton mPasswordSwitch;
    ImageView mIvWifi;
    TextView mTvWifiStatus;
    TextView mTvOtherWifi;

    private ECPresenter mECPresenter;

    private boolean passwordOn = true;
    private int mWifiOnColor;
    private View m5gNetworkTip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ec_mode);
        initToolbar();
        initView();
        initMenu();
        initPresenter();

        TypedArray a = obtainStyledAttributes(new int[]{
                R.attr.navbar_font_color});
        mWifiOnColor = a.getColor(0, getResources().getColor(R.color.color_primary));
        a.recycle();
    }

    private void initPresenter() {
        mECPresenter = new ECPresenter(this, this);
    }


    public void clickPasswordSwitch(View v) {
        passwordOn = !passwordOn;
        if (passwordOn) {
            mPasswordSwitch.setImageResource(R.drawable.ty_password_on);
            mPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            mPasswordSwitch.setImageResource(R.drawable.ty_password_off);
            mPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
        if (mPassword.getText().length() > 0) {
            mPassword.setSelection(mPassword.getText().length());
        }
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.tv_other_wifi) {
                mECPresenter.userOtherWifi();
            } else if (v.getId() == R.id.ec_password_switch) {
                clickPasswordSwitch(v);
            } else if (v.getId() == R.id.tv_bottom_button) {
                mECPresenter.goNextStep();
            }
        }
    };

    private void initView() {
        mSSID = (TextView) findViewById(R.id.tv_network);
        mSSID.setVisibility(View.VISIBLE);

        mPassword = (EditText) findViewById(R.id.et_password);
        mPasswordSwitch = (ImageButton) findViewById(R.id.ec_password_switch);
        mPasswordSwitch.setOnClickListener(mOnClickListener);
        mIvWifi = (ImageView) findViewById(R.id.iv_wifi);
        setWifiVectorDrawable(mIvWifi);
        mIvWifi.setColorFilter(mWifiOnColor);

        mTvWifiStatus = (TextView) findViewById(R.id.tv_wifi_status);
        mTvOtherWifi = (TextView) findViewById(R.id.tv_other_wifi);
        mTvOtherWifi.setOnClickListener(mOnClickListener);
        findViewById(R.id.tv_bottom_button).setOnClickListener(mOnClickListener);
        m5gNetworkTip = findViewById(R.id.network_tip);
    }

    private void initMenu() {
        setTitle(getString(R.string.ty_ez_wifi_title));
        setDisplayHomeAsUpEnabled();
    }

    private void setWifiVectorDrawable(ImageView view) {
        VectorDrawable drawable = VectorDrawable.getDrawable(TuyaSdk.getApplication(), R.drawable.wifi_status);
        mIvWifi.setBackground(getResources().getDrawable(R.drawable.bg_bt_circle));
        drawable.setAlpha(128);
        view.setImageDrawable(drawable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mECPresenter.checkWifiNetworkStatus();
    }

    @Override
    protected boolean onPanelKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                mECPresenter.closeECModeActivity();
                return true;
        }
        return false;
    }

    @Override
    public void setWifiSSID(String ssId) {
        setViewVisible(mSSID);
        mIvWifi.setColorFilter(mWifiOnColor);
        mTvWifiStatus.setText(getString(R.string.ty_ez_current_wifi_android));
        mSSID.setText(ssId);
    }

    @Override
    public void setWifiPass(String pass) {
        mPassword.setText(pass);
    }

    @Override
    public String getWifiPass() {
        return mPassword.getText().toString();
    }

    @Override
    public String getWifiSsId() {
        return mSSID.getText().toString();
    }

    @Override
    public void showNoWifi() {
        setViewGone(mSSID);
        hide5gTip();
        setWifiPass("");
        mIvWifi.setColorFilter(Color.GRAY);
        mTvWifiStatus.setText(getString(R.string.ty_ez_current_no_wifi));
    }

    @Override
    public void show5gTip() {
        setViewVisible(m5gNetworkTip);
    }

    @Override
    public void hide5gTip() {
        setViewGone(m5gNetworkTip);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mECPresenter.onDestroy();
    }
}
