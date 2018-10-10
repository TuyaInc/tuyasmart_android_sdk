package com.tuya.smart.android.demo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tuya.smart.android.common.utils.ValidatorUtil;
import com.tuya.smart.android.demo.R;
import com.tuya.smart.android.demo.presenter.LoginPresenter;
import com.tuya.smart.android.demo.utils.ProgressUtil;
import com.tuya.smart.android.demo.utils.ToastUtil;
import com.tuya.smart.android.demo.view.ILoginView;
import com.tuya.smart.android.mvp.bean.Result;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by letian on 16/7/15.
 */
public class LoginActivity extends BaseActivity implements ILoginView, TextWatcher {

    @BindView(R.id.login_submit)
    public Button mLoginSubmit;

    @BindView(R.id.country_name)
    public TextView mCountryName;

    @BindView(R.id.password)
    public EditText mPassword;

    @BindView(R.id.password_switch)
    public ImageButton mPasswordSwitch;
    @BindView(R.id.user_name)
    public TextView mUserName;


    private LoginPresenter mLoginPresenter;

    private boolean passwordOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initToolbar();
        initView();
        initTitle();
        initMenu();
        disableLogin();
        mLoginPresenter = new LoginPresenter(this, this);
    }


    // 注册按钮
    private void initMenu() {
        setMenu(R.menu.toolbar_login_menu, new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_login_reg_onclick) {
                    AccountInputActivity.gotoAccountInputActivity(LoginActivity.this, AccountInputActivity.MODE_REGISTER, 0);
                }
                return false;
            }
        });
    }

    private void initTitle() {
        setTitle(getString(R.string.login));
    }

    private void initView() {
        passwordOn = false;
        mUserName.addTextChangedListener(this);
        mPassword.addTextChangedListener(this);
        mPasswordSwitch.setImageResource(R.drawable.ty_password_off);
        mPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
    }


    // 输入账号监听
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String userName = mUserName.getText().toString();
        String password = mPassword.getText().toString();
        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)) {
            disableLogin();
        } else {
            if (ValidatorUtil.isEmail(userName)) {
                // 邮箱
                enableLogin();
            } else {
                // 手机号码
                try {
                    Long.valueOf(userName);
                    enableLogin();
                } catch (Exception e) {
                    disableLogin();
                }
            }
        }
    }

    @OnClick(R.id.option_validate_code)
    public void loginWithPhoneCode() {
        startActivity(new Intent(LoginActivity.this, LoginWithPhoneActivity.class));
    }

    @OnClick(R.id.option_forget_password)
    public void retrievePassword() {
        Intent intent = new Intent(LoginActivity.this, AccountInputActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.country_name)
    public void onClickSelectCountry() {
        mLoginPresenter.selectCountry();
    }

    @OnClick(R.id.password_switch)
    public void onClickPasswordSwitch() {
        passwordOn = !passwordOn;

        // 切换显示图标
        if (passwordOn) {
            mPasswordSwitch.setImageResource(R.drawable.ty_password_on);
            mPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            mPasswordSwitch.setImageResource(R.drawable.ty_password_off);
            mPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }

        // 更新光标位置
        if (mPassword.getText().length() > 0) {
            mPassword.setSelection(mPassword.getText().length());
        }
    }

    @OnClick(R.id.login_submit)
    public void onClickLogin() {
        // 登录
        if (mLoginSubmit.isEnabled()) {
            String userName = mUserName.getText().toString();
            if (!ValidatorUtil.isEmail(userName) && mCountryName.getText().toString().contains("+86") && mUserName.getText().length() != 11) {
                ToastUtil.shortToast(LoginActivity.this, getString(R.string.ty_phone_num_error));
                return;
            }
            hideIMM();
            disableLogin();
            ProgressUtil.showLoading(LoginActivity.this, R.string.logining);
            mLoginPresenter.login(userName, mPassword.getText().toString());
        }
    }

    @Override
    public void setCountry(String name, String code) {
        mCountryName.setText(String.format("%s +%s", name, code));
    }

    @Override
    public void modelResult(int what, Result result) {
        switch (what) {
            case LoginPresenter.MSG_LOGIN_SUCCESS:
                ProgressUtil.hideLoading();
                break;
            case LoginPresenter.MSG_LOGIN_FAILURE:
                ProgressUtil.hideLoading();
                ToastUtil.shortToast(this, result.error);
                enableLogin();
                break;
            default:
                break;
        }
    }

    // 登录按钮状态
    public void enableLogin() {
        if (!mLoginSubmit.isEnabled()) mLoginSubmit.setEnabled(true);
    }

    public void disableLogin() {
        if (mLoginSubmit.isEnabled()) mLoginSubmit.setEnabled(false);
    }

    @Override
    public boolean needLogin() {
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mLoginPresenter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLoginPresenter.onDestroy();
    }
}
