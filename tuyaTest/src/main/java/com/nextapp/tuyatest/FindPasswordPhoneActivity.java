package com.nextapp.tuyatest;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tuya.smart.android.user.TuyaSmartUserManager;
import com.tuya.smart.android.user.api.IResetPasswordCallback;
import com.tuya.smart.android.user.api.IValidateCallback;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by letian on 16/2/19.
 */
public class FindPasswordPhoneActivity extends Activity {

    @Bind(R.id.et_phone_number)
    EditText mEtPhoneNumber;
    @Bind(R.id.et_phone_code)
    EditText mEtPhoneCode;
    @Bind(R.id.et_phone_password)
    EditText mEtPhonePassword;
    @Bind(R.id.do_reset)
    Button mDoLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password_phone);
        ButterKnife.bind(this);

    }

    private String getCountryCode(){
        final EditText countryCodeEt = (EditText)findViewById(R.id.et_country_code);
        String countryCode = countryCodeEt.getText().toString();
        if(TextUtils.isEmpty(countryCode)){
            countryCode = "86";
        }

        return countryCode;
    }

    @OnClick(R.id.do_get_validate_code)
    public void onClickGetCode() {
        TuyaSmartUserManager.getInstance().getValidateCode(getCountryCode(), mEtPhoneNumber.getText().toString(), new IValidateCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(FindPasswordPhoneActivity.this, "获取验证码成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String code, String error) {
                Toast.makeText(FindPasswordPhoneActivity.this, "code: " + code + "error:" + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick(R.id.do_reset)
    public void onClickFindPassword() {
        TuyaSmartUserManager.getInstance().resetPhonePassword(getCountryCode(), mEtPhoneNumber.getText().toString(), mEtPhoneCode.getText().toString(), mEtPhonePassword.getText().toString(), new IResetPasswordCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(FindPasswordPhoneActivity.this, "找回密码成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String code, String error) {
                Toast.makeText(FindPasswordPhoneActivity.this, "code: " + code + "error:" + error, Toast.LENGTH_SHORT).show();

            }
        });
    }
}