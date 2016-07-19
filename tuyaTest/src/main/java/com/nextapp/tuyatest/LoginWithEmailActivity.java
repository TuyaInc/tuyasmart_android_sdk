package com.nextapp.tuyatest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tuya.smart.android.user.TuyaSmartUserManager;
import com.tuya.smart.android.user.api.ILoginCallback;
import com.tuya.smart.android.user.bean.User;
import com.tuya.smart.sdk.TuyaUser;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by letian on 16/2/17.
 */
public class LoginWithEmailActivity extends Activity {

    @Bind(R.id.et_email_number)
    EditText mEtEmailNumber;
    @Bind(R.id.et_validate_code)
    EditText mEtValidateCode;
    @Bind(R.id.do_reset)
    Button mDoLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_with_email);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.do_reset)
    public void onClickLogin() {
        TuyaUser.getUserInstance().loginWithEmail("86", mEtEmailNumber.getText().toString(), mEtValidateCode.getText().toString(), new ILoginCallback() {
            @Override
            public void onSuccess(User user) {
                Toast.makeText(LoginWithEmailActivity.this, getString(R.string.login) + getString(R.string.unit_success) + " : " + TuyaSmartUserManager.getInstance().getUser().getUsername(), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginWithEmailActivity.this, MainActivity.class));
                LoginWithEmailActivity.this.finish();
            }

            @Override
            public void onError(String code, String error) {
                Toast.makeText(LoginWithEmailActivity.this, "code: " + code + "error:" + error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
