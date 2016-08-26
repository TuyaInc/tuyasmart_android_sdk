package com.tuya.smart.android.demo;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tuya.smart.android.demo.R;
import com.tuya.smart.android.user.api.IRegisterCallback;
import com.tuya.smart.android.user.bean.User;
import com.tuya.smart.sdk.TuyaUser;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by letian on 16/4/28.
 */
public class RegisterWithUIDActivity extends Activity {
    @Bind(R.id.et_uid)
    EditText mEtUid;
    @Bind(R.id.et_password_code)
    EditText mEtPasswordCode;
    @Bind(R.id.do_register)
    Button mDoRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_uid);
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

    @OnClick(R.id.do_register)
    public void onClickRegister() {
        TuyaUser.getUserInstance().registerAccountWithUid(getCountryCode(), mEtUid.getText().toString(),
                mEtPasswordCode.getText().toString(), new IRegisterCallback() {
                    @Override
                    public void onSuccess(User user) {
                        Toast.makeText(RegisterWithUIDActivity.this, getString(R.string.ty_login_register) + getString(R.string.unit_success), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(String code, String error) {
                        Toast.makeText(RegisterWithUIDActivity.this, "code: " + code + "error:" + error, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
