package com.tuya.smart.android.demo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tuya.smart.android.demo.R;
import com.tuya.smart.android.demo.presenter.LoginWithPhonePresenter;
import com.tuya.smart.android.demo.utils.ProgressUtil;
import com.tuya.smart.android.demo.utils.ToastUtil;
import com.tuya.smart.android.demo.view.ILoginWithPhoneView;
import com.tuya.smart.android.mvp.bean.Result;

/**
 * Created by letian on 15/2/11.
 */
public class LoginWithPhoneActivity extends BaseActivity implements TextWatcher, ILoginWithPhoneView {
    private static final String TAG = "LoginWithPhoneActivity";

    public Button mLoginSubmit;
    public Button mGetValidateCode;
    public EditText mPhone;
    public EditText mValidateCode;
    public TextView mCountryName;
    protected LoginWithPhonePresenter mLoginWithPhonePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_with_phone);
        initToolbar();
        initView();
        initTitle();
        initPresenter();
        addTextChangedListener();
        initButtonState();
    }

    private void initButtonState() {
        disableGetValidateCode();
        disableLogin();
    }

    protected void initTitle() {
        setTitle(getString(R.string.ty_login_sms));
        setDisplayHomeAsUpEnabled();
    }

    private void initPresenter() {
        mLoginWithPhonePresenter = getPresenter();
    }

    protected LoginWithPhonePresenter getPresenter() {
        return new LoginWithPhonePresenter(this, this);
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.login_submit) {
                login(v);
            } else if (v.getId() == R.id.get_validate_code) {
                sendValidateCode(v);
            } else if (v.getId() == R.id.country_name) {
                selectCountry();
            }
        }
    };

    public void login(View view) {
        if (mLoginSubmit.isEnabled()) {
            hideIMM();
            ProgressUtil.showLoading(LoginWithPhoneActivity.this, R.string.logining);
            disableLogin();
            mLoginWithPhonePresenter.login();
        }
    }

    public void sendValidateCode(View view) {
        if (mGetValidateCode.isEnabled()) {
            hideIMM();
            //验证码发送
            disableGetValidateCode();
            mLoginWithPhonePresenter.getValidateCode();
        }
    }

    public void selectCountry() {
        mLoginWithPhonePresenter.selectCountry();
    }

    protected void initView() {
        mLoginSubmit = (Button) findViewById(R.id.login_submit);
        mLoginSubmit.setOnClickListener(mOnClickListener);
        mGetValidateCode = (Button) findViewById(R.id.get_validate_code);
        mGetValidateCode.setOnClickListener(mOnClickListener);
        mPhone = (EditText) findViewById(R.id.phone);
        mValidateCode = (EditText) findViewById(R.id.validate_code);
        mCountryName = (TextView) findViewById(R.id.country_name);
        mCountryName.setOnClickListener(mOnClickListener);

        mRunnable = new Runnable() {
            @Override
            public void run() {
                mPhone.requestFocus();
                InputMethodManager imm = (InputMethodManager) mPhone.getContext()
                        .getSystemService(
                                Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
            }
        };
    }

    private void addTextChangedListener() {
        mPhone.addTextChangedListener(this);
        mValidateCode.addTextChangedListener(this);
    }

    private Runnable mRunnable;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLoginWithPhonePresenter.onDestroy();
        if (mRunnable != null && mPhone != null) {
            mPhone.removeCallbacks(mRunnable);
        }
    }

    @Override
    public boolean needLogin() {
        return false;
    }

    @Override
    public String getPhone() {
        return mPhone.getText().toString();
    }

    @Override
    public String getValidateCode() {
        return mValidateCode.getText().toString();
    }

    @Override
    public void setCountdown(int sec) {
        mGetValidateCode.setText(getString(R.string.reget_validation_second, sec));
    }

    @Override
    public void enableGetValidateCode() {
//        mGetValidateCode.setEnabled(true);
        mGetValidateCode.setText(R.string.login_reget_code);
    }

    @Override
    public void disableGetValidateCode() {
        if (mGetValidateCode.isEnabled()) mGetValidateCode.setEnabled(false);
    }

    public void enableLogin() {
        if (!mLoginSubmit.isEnabled()) mLoginSubmit.setEnabled(true);
    }

    public void disableLogin() {
        if (mLoginSubmit.isEnabled()) mLoginSubmit.setEnabled(false);
    }

    @Override
    public void modelResult(int what, Result result) {
        switch (what) {
            case LoginWithPhonePresenter.MSG_SEND_VALIDATE_CODE_ERROR:
                resetGetValidateCode();
                ToastUtil.shortToast(this, result.error);
                break;
            case LoginWithPhonePresenter.MSG_SEND_VALIDATE_CODE_SUCCESS:
                disableGetValidateCode();
                mValidateCode.requestFocus();
                break;
            case LoginWithPhonePresenter.MSG_LOGIN_ERROR:
                ProgressUtil.hideLoading();
                ToastUtil.shortToast(this, result.error);
                enableLogin();
                break;
            case LoginWithPhonePresenter.MSG_LOGIN_SUCCESS:
                ProgressUtil.hideLoading();
                break;
        }
    }

    private void resetGetValidateCode() {
        if (!mGetValidateCode.isEnabled()) mGetValidateCode.setEnabled(true);
    }

    @Override
    public void setCountry(String name, String code) {
        mCountryName.setText(String.format("%s +%s", name, code));
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        checkPhoneNumber();
        checkValidateCode();
    }

    public void checkValidateCode() {
        String phone = mPhone.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            disableGetValidateCode();
        } else if (!mLoginWithPhonePresenter.isSended()) {
            resetGetValidateCode();
        }
    }

    private void checkPhoneNumber() {
        String phoneNumber = mPhone.getText().toString();
        if (TextUtils.isEmpty(phoneNumber) || TextUtils.isEmpty(mValidateCode.getText().toString())) {
            disableLogin();
        } else {
            enableLogin();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mLoginWithPhonePresenter != null) {
            mLoginWithPhonePresenter.onActivityResult(requestCode, resultCode, data);
        }
    }


}
