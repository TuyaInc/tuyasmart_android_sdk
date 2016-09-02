package com.tuya.smart.android.demo;

import android.app.Activity;
import android.os.Bundle;
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
 * Created by letian on 16/2/17.
 */
public class RegisterWithEmailActivity extends Activity {
    @Bind(R.id.et_email)
    EditText mEtEmail;
    @Bind(R.id.et_password_code)
    EditText mEtPasswordCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_email);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.do_register)
    public void onClickRegister() {
        TuyaUser.getUserInstance().registerAccountWithEmail("86", mEtEmail.getText().toString(),
                mEtPasswordCode.getText().toString(), new IRegisterCallback() {
                    @Override
                    public void onSuccess(User user) {
                        Toast.makeText(RegisterWithEmailActivity.this, getString(R.string.ty_login_register) + getString(R.string.unit_success), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(String code, String error) {
                        Toast.makeText(RegisterWithEmailActivity.this, "code: " + code + "error:" + error, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
