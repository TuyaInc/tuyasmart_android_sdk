package com.tuya.smart.android.demo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tuya.smart.android.demo.R;
import com.tuya.smart.android.user.api.IResetPasswordCallback;
import com.tuya.smart.android.user.api.IValidateCallback;
import com.tuya.smart.sdk.TuyaUser;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by letian on 16/2/19.
 */
public class FindPasswordEmailActivity extends Activity {
    @Bind(R.id.et_phone_number)
    EditText mEtPhoneNumber;
    @Bind(R.id.do_get_validate_code)
    Button mDoGetValidateCode;
    @Bind(R.id.et_phone_code)
    EditText mEtPhoneCode;
    @Bind(R.id.et_phone_password)
    EditText mEtPhonePassword;
    @Bind(R.id.do_reset)
    Button mDoLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password_email);
        ButterKnife.bind(this);
    }


    @OnClick(R.id.do_get_validate_code)
    public void onClickGetCode() {
        TuyaUser.getUserInstance().getEmailValidateCode("86", mEtPhoneNumber.getText().toString(), new IValidateCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(FindPasswordEmailActivity.this, R.string.get_validate_code + R.string.unit_success, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String code, String error) {
                Toast.makeText(FindPasswordEmailActivity.this, "code: " + code + "error:" + error, Toast.LENGTH_SHORT).show();
            }
        });
    }


    @OnClick(R.id.do_reset)
    public void onClickFindPassword() {
        TuyaUser.getUserInstance().resetEmailPassword("86", mEtPhoneNumber.getText().toString(), mEtPhoneCode.getText().toString(), mEtPhonePassword.getText().toString(), new IResetPasswordCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(FindPasswordEmailActivity.this, R.string.login_find_password + R.string.unit_success, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String code, String error) {
                Toast.makeText(FindPasswordEmailActivity.this, "code: " + code + "error:" + error, Toast.LENGTH_SHORT).show();

            }
        });
    }
}
