package com.tuya.smart.android.demo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tuya.smart.android.demo.R;
import com.tuya.smart.android.common.utils.L;
import com.tuya.smart.android.user.api.ILoginCallback;
import com.tuya.smart.android.user.api.IValidateCallback;
import com.tuya.smart.android.user.bean.User;
import com.tuya.smart.sdk.TuyaSdk;
import com.tuya.smart.sdk.TuyaUser;
import com.tuya.smart.sdk.api.INeedLoginListener;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by letian on 16/1/5.
 */
public class Login2Activity extends Activity {
    private static final String TAG = "LoginActivityggg";
    @Bind(R.id.et_phone_number)
    EditText mEtPhoneNumber;
    @Bind(R.id.et_validate_code)
    EditText mEtValidateCode;
    @Bind(R.id.do_get_validate_code)
    Button mDoGetValidateCode;
    @Bind(R.id.do_reset)
    Button mDoLogin;
    @Bind(R.id.do_register)
    Button mDoRegister;
    @Bind(R.id.do_email_register)
    Button mDoEmailRegister;
    @Bind(R.id.do_uid_register)
    Button mDoUidRegister;
    @Bind(R.id.do_phone_login)
    Button mDoPhoneLogin;
    @Bind(R.id.do_email_login)
    Button mDoEmailLogin;
    @Bind(R.id.do_uid_login)
    Button mDoUidLogin;
    @Bind(R.id.do_phone_forget)
    Button mDoPhoneForget;
    @Bind(R.id.do_email_forget)
    Button mDoEmailForget;
    @Bind(R.id.do_uid_forget)
    Button mDoUidForget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login1);
        ButterKnife.bind(this);
        //判断是否登陆
        if (TuyaUser.getUserInstance().isLogin()) {
            startActivity(new Intent(Login2Activity.this, MainActivity.class));
            finish();
            return;
        }

        startActivity(new Intent(this, Login2Activity.class));
        //登陆session失效回调的监听
        TuyaSdk.setOnNeedLoginListener(new INeedLoginListener() {
            @Override
            public void onNeedLogin(Context context) {
                L.d(TAG, "login session out of date");
            }
        });

        final EditText code = (EditText) findViewById(R.id.et_validate_code);
        final EditText phoneNumber = (EditText) findViewById(R.id.et_phone_number);

        findViewById(R.id.do_reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 登录接口。用户登录的时候调用
                 */
                //86是指国内.
                TuyaUser.getUserInstance().loginWithPhone(getCountryCode(), phoneNumber.getText().toString(), code.getText().toString(), new ILoginCallback() {
                    @Override
                    public void onSuccess(User user) {
                        Toast.makeText(Login2Activity.this, getString(R.string.login) + getString(R.string.unit_success) + " : " + TuyaUser.getUserInstance().getUser().getUsername(), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Login2Activity.this, MainActivity.class));
                        Login2Activity.this.finish();
                    }

                    @Override
                    public void onError(String code, String error) {
                        Toast.makeText(Login2Activity.this, error, Toast.LENGTH_SHORT).show();
                    }

                });
            }
        });

        findViewById(R.id.do_get_validate_code).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TuyaUser.getUserInstance().getValidateCode(getCountryCode(), phoneNumber.getText().toString(), new IValidateCallback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(String code, String error) {
                        Toast.makeText(Login2Activity.this, error, Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        L.setLogSwitcher(true);
    }

    private String getCountryCode() {
        final EditText countryCodeEt = (EditText) findViewById(R.id.et_country_code);
        String countryCode = countryCodeEt.getText().toString();
        if (TextUtils.isEmpty(countryCode)) {
            countryCode = "86";
        }

        return countryCode;
    }

    @OnClick(R.id.do_register)
    public void onClickRegisterPhone() {
        startActivity(new Intent(Login2Activity.this, RegisterWithPhoneActivity.class));
    }

    @OnClick(R.id.do_email_register)
    public void onClickRegisterEmail() {
        startActivity(new Intent(Login2Activity.this, RegisterWithEmailActivity.class));
    }

    @OnClick(R.id.do_phone_login)
    public void onClickLoginPhone() {
        startActivity(new Intent(Login2Activity.this, LoginWithPhoneActivity.class));
        Login2Activity.this.finish();
    }

    @OnClick(R.id.do_email_login)
    public void onClickLoginEmail() {
        startActivity(new Intent(Login2Activity.this, LoginWithEmailActivity.class));
        Login2Activity.this.finish();
    }

    @OnClick(R.id.do_phone_forget)
    public void onClickPhoneForget() {
        startActivity(new Intent(Login2Activity.this, FindPasswordPhoneActivity.class));
    }

    @OnClick(R.id.do_email_forget)
    public void onClickEmailForget() {
        startActivity(new Intent(Login2Activity.this, FindPasswordEmailActivity.class));
    }

    @OnClick(R.id.do_uid_forget)
    public void onClickUidForget() {
        startActivity(new Intent(Login2Activity.this, FindPasswordUidActivity.class));
    }

    @OnClick(R.id.do_uid_login)
    public void onClickUidLogin() {
        startActivity(new Intent(Login2Activity.this, LoginWithUidActivity.class));
        Login2Activity.this.finish();
    }

    @OnClick(R.id.do_uid_register)
    public void onClickUidRegister() {
        startActivity(new Intent(Login2Activity.this, RegisterWithUIDActivity.class));
    }
}
