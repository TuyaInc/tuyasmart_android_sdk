package com.tuya.smart.android.demo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tuya.smart.android.common.utils.ValidatorUtil;
import com.tuya.smart.android.demo.R;
import com.tuya.smart.android.demo.presenter.AccountInputPresenter;
import com.tuya.smart.android.demo.utils.ActivityUtils;
import com.tuya.smart.android.demo.utils.ToastUtil;
import com.tuya.smart.android.demo.view.IAccountInputView;

public class AccountInputActivity extends BaseActivity implements TextWatcher, IAccountInputView {

    public static final String EXTRA_ACCOUNT_INPUT_MODE = "extra_account_input_mode";
    public static final int MODE_REGISTER = 0;
    public static final int MODE_PASSWORD_FOUND = 1;

    private int mMode = 0;

    private AccountInputPresenter mPresenter;

    private int mAccountType = AccountConfirmActivity.PLATFORM_PHONE;
    private TextView mCountryName;
    private EditText mEtAccount;
    private Button mNextStepBtn;

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.bt_next) {
                if (mAccountType == AccountConfirmActivity.PLATFORM_PHONE
                        && mCountryName.getText().toString().contains("+86")
                        && mEtAccount.getText().length() != 11) {
                    ToastUtil.shortToast(AccountInputActivity.this, getString(R.string.ty_phone_num_error));
                    return;
                }

                mPresenter.gotoNext(mAccountType);
            } else if (v.getId() == R.id.country_name) {
                mPresenter.selectCountry();
            }
        }
    };

    public static void gotoAccountInputActivity(Activity activity, int mode, int requestCode) {
        Intent intent = new Intent(activity, AccountInputActivity.class);
        intent.putExtra(EXTRA_ACCOUNT_INPUT_MODE, mode);
        ActivityUtils.startActivityForResult(activity, intent, requestCode, 0, false);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_input);
        initData();
        initToolbar();
        initTitle();
        initView();
        initPresenter();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mPresenter.onActivityResult(requestCode, resultCode, data);
    }

    private void initData() {
        mMode = getIntent().getIntExtra(EXTRA_ACCOUNT_INPUT_MODE, MODE_REGISTER);
    }

    private void initPresenter() {
        mPresenter = new AccountInputPresenter(this, this);
    }

    private void initView() {
        mCountryName = (TextView) findViewById(R.id.country_name);
        mCountryName.setOnClickListener(mOnClickListener);
        mNextStepBtn = (Button) findViewById(R.id.bt_next);
        mNextStepBtn.setOnClickListener(mOnClickListener);
        mNextStepBtn.setEnabled(false);
        mEtAccount = (EditText) findViewById(R.id.et_account);
        mEtAccount.addTextChangedListener(this);
    }

    private void initTitle() {
        switch (mMode) {
            case MODE_REGISTER:
                setTitle(R.string.ty_login_register);
                break;

            case MODE_PASSWORD_FOUND:
                setTitle(R.string.ty_login_forget_keyword_find);
                break;
        }
        setDisplayHomeAsUpEnabled();
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
        String userName = mEtAccount.getText().toString();

        if (TextUtils.isEmpty(userName)) {
            mNextStepBtn.setEnabled(false);
        } else {
            if (ValidatorUtil.isEmail(userName)) {
                // 邮箱
                mAccountType = AccountConfirmActivity.PLATFORM_EMAIL;
                mNextStepBtn.setEnabled(true);
            } else {
                // 手机号码
                try {
                    Long.valueOf(userName);
                    mAccountType = AccountConfirmActivity.PLATFORM_PHONE;
                    mNextStepBtn.setEnabled(true);
                } catch (Exception e) {
                    mNextStepBtn.setEnabled(false);
                }
            }
        }
    }

    @Override
    public void setCountryInfo(String countryName, String countryNum) {
        mCountryName.setText(String.format("%s +%s", countryName, countryNum));
    }

    @Override
    public String getAccount() {
        return mEtAccount.getText().toString();
    }

    @Override
    public int getMode() {
        return mMode;
    }

    @Override
    public boolean needLogin() {
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
    }
}
