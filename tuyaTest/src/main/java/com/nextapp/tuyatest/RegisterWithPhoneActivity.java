package com.nextapp.tuyatest;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tuya.smart.android.user.api.IRegisterCallback;
import com.tuya.smart.android.user.api.IValidateCallback;
import com.tuya.smart.android.user.bean.User;
import com.tuya.smart.sdk.TuyaUser;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by letian on 16/2/17.
 */
public class RegisterWithPhoneActivity extends Activity {

    @Bind(R.id.et_phone_number)
    EditText mEtPhoneNumber;
    @Bind(R.id.do_get_validate_code)
    Button mDoGetValidateCode;
    @Bind(R.id.et_validate_code)
    EditText mEtValidateCode;
    @Bind(R.id.do_register)
    Button mDoRegister;
    @Bind(R.id.et_password_code)
    EditText mEtPasswordCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_phone);
        ButterKnife.bind(this);
    }

    private String getCountryCode() {
        final EditText countryCodeEt = (EditText) findViewById(R.id.et_country_code);
        String countryCode = countryCodeEt.getText().toString();
        if (TextUtils.isEmpty(countryCode)) {
            countryCode = "86";
        }

        return countryCode;
    }

    @OnClick(R.id.do_get_validate_code)
    public void onClickGetCode() {
        TuyaUser.getUserInstance().getValidateCode(getCountryCode(), mEtPhoneNumber.getText().toString(), new IValidateCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(RegisterWithPhoneActivity.this, getString(R.string.get_validate_code) + getString(R.string.unit_success), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String code, String error) {
                Toast.makeText(RegisterWithPhoneActivity.this, "code: " + code + "error:" + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick(R.id.do_register)
    public void onClickRegister() {
        TuyaUser.getUserInstance().registerAccountWithPhone(getCountryCode(), mEtPhoneNumber.getText().toString(),
                mEtPasswordCode.getText().toString(),
                mEtValidateCode.getText().toString(), new IRegisterCallback() {
                    @Override
                    public void onSuccess(User user) {
                        Toast.makeText(RegisterWithPhoneActivity.this, getString(R.string.ty_login_register) + getString(R.string.unit_success), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(String code, String error) {
                        Toast.makeText(RegisterWithPhoneActivity.this, "code: " + code + "error:" + error, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
