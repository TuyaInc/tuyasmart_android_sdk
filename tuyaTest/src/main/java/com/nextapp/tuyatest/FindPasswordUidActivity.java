package com.nextapp.tuyatest;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tuya.smart.android.user.TuyaSmartUserManager;
import com.tuya.smart.android.user.api.IResetPasswordCallback;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by letian on 16/4/28.
 */
public class FindPasswordUidActivity extends Activity {
    @Bind(R.id.et_uid_number)
    EditText mEtUidNumber;
    @Bind(R.id.et_uid_password)
    EditText mEtUidPassword;
    @Bind(R.id.do_reset)
    Button mDoReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password_uid);
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

    @OnClick(R.id.do_reset)
    public void resetPassword() {
        TuyaSmartUserManager.getInstance().resetUidPassword(getCountryCode(), mEtUidNumber.getText().toString(), mEtUidPassword.getText().toString(), new IResetPasswordCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(FindPasswordUidActivity.this, "重置密码成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String code, String error) {
                Toast.makeText(FindPasswordUidActivity.this, "code: " + code + "error:" + error, Toast.LENGTH_SHORT).show();

            }
        });
    }
}
