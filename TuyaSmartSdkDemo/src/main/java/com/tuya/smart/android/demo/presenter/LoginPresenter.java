package com.tuya.smart.android.demo.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.text.TextUtils;

import com.tuya.smart.android.common.utils.ValidatorUtil;
import com.tuya.smart.android.demo.activity.CountryListActivity;
import com.tuya.smart.android.demo.app.Constant;
import com.tuya.smart.android.demo.utils.ActivityUtils;
import com.tuya.smart.android.demo.utils.CountryUtils;
import com.tuya.smart.android.demo.utils.LoginHelper;
import com.tuya.smart.android.demo.utils.MessageUtil;
import com.tuya.smart.android.demo.view.ILoginView;
import com.tuya.smart.android.mvp.bean.Result;
import com.tuya.smart.android.mvp.presenter.BasePresenter;
import com.tuya.smart.android.user.api.ILoginCallback;
import com.tuya.smart.android.user.bean.User;
import com.tuya.smart.sdk.TuyaSdk;
import com.tuya.smart.sdk.TuyaUser;

/**
 * 登录逻辑
 * <p/>
 * Created by sunch on 16/6/4.
 */
public class LoginPresenter extends BasePresenter {
    protected Activity mContext;
    protected ILoginView mView;

    private String mCountryName;
    private String mCountryCode;

    public static final int MSG_LOGIN_SUCCESS = 15;
    public static final int MSG_LOGIN_FAILURE = 16;

    public LoginPresenter(Context context, ILoginView view) {
        super();
        mContext = (Activity) context;
        mView = view;
        initCountryInfo();
    }

    // 初始化国家/地区信息
    private void initCountryInfo() {
        String countryKey = CountryUtils.getCountryKey(TuyaSdk.getApplication());
        if (!TextUtils.isEmpty(countryKey)) {
            mCountryName = CountryUtils.getCountryTitle(countryKey);
            mCountryCode = CountryUtils.getCountryNum(countryKey);
        } else {
            countryKey = CountryUtils.getCountryDefault(TuyaSdk.getApplication());
            mCountryName = CountryUtils.getCountryTitle(countryKey);
            mCountryCode = CountryUtils.getCountryNum(countryKey);
        }

        mView.setCountry(mCountryName, mCountryCode);
    }

    // 选择国家/地区信息
    public void selectCountry() {
        mContext.startActivityForResult(new Intent(mContext, CountryListActivity.class), 0x01);
    }

    // 登录
    public void login(String userName, String password) {

        if (!ValidatorUtil.isEmail(userName)) {
            TuyaUser.getUserInstance().loginWithPhonePassword(mCountryCode, userName, password, mLoginCallback);
        } else {
            TuyaUser.getUserInstance().loginWithEmail(mCountryCode, userName, password, mLoginCallback);
        }
    }

    private ILoginCallback mLoginCallback = new ILoginCallback() {
        @Override
        public void onSuccess(User user) {
            mHandler.sendEmptyMessage(MSG_LOGIN_SUCCESS);
        }

        @Override
        public void onError(String s, String s1) {
            Message msg = MessageUtil.getCallFailMessage(MSG_LOGIN_FAILURE, s, s1);
            mHandler.sendMessage(msg);
        }
    };

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_LOGIN_SUCCESS:
                // 登录成功
                mView.modelResult(msg.what, null);
                Constant.finishActivity();
                LoginHelper.afterLogin();
                ActivityUtils.gotoHomeActivity(mContext);
                break;
            case MSG_LOGIN_FAILURE:
                // 登录失败
                mView.modelResult(msg.what, (Result) msg.obj);
                break;
            default:
                break;
        }

        return super.handleMessage(msg);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 0x01:
                if (resultCode == Activity.RESULT_OK) {
                    mCountryName = data.getStringExtra(CountryListActivity.COUNTRY_NAME);
                    mCountryCode = data.getStringExtra(CountryListActivity.PHONE_CODE);
                    mView.setCountry(mCountryName, mCountryCode);
                }
                break;
        }
    }
}
