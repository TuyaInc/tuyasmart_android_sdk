package com.nextapp.tuyatest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tuya.smart.android.user.api.ILoginCallback;
import com.tuya.smart.android.user.bean.User;
import com.tuya.smart.sdk.TuyaUser;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by letian on 16/2/17.
 */
public class LoginWithPhoneActivity extends Activity {
    @Bind(R.id.et_phone_number)
    EditText mEtPhoneNumber;
    @Bind(R.id.et_validate_code)
    EditText mEtValidateCode;
    @Bind(R.id.do_reset)
    Button mDoLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_with_phone);
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

    @OnClick(R.id.do_reset)
    public void onClickLogin() {
        TuyaUser.getUserInstance().loginWithPhonePassword(getCountryCode(), mEtPhoneNumber.getText().toString(), mEtValidateCode.getText().toString(), new ILoginCallback() {
            @Override
            public void onSuccess(User user) {
                Toast.makeText(LoginWithPhoneActivity.this, getString(R.string.login) + getString(R.string.unit_success) + " : " + user.getUsername(), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginWithPhoneActivity.this, MainActivity.class));
                LoginWithPhoneActivity.this.finish();
            }

            @Override
            public void onError(String code, String error) {
                Toast.makeText(LoginWithPhoneActivity.this, "code: " + code + "error:" + error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
