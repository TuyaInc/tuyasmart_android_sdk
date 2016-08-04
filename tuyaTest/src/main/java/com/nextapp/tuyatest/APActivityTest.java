package com.nextapp.tuyatest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.tuya.smart.android.common.utils.L;
import com.tuya.smart.android.device.TuyaSmartApConnect;
import com.tuya.smart.android.device.api.response.GwDevResp;
import com.tuya.smart.android.device.link.APConfigBuilder;
import com.tuya.smart.android.device.link.IApConnectListener;
import com.tuya.smart.sdk.TuyaActivator;
import com.tuya.smart.sdk.api.ITuyaActivator;
import com.tuya.smart.sdk.api.ITuyaActivatorGetToken;
import com.tuya.smart.sdk.api.ITuyaSmartActivatorListener;
import com.tuya.smart.sdk.builder.ActivatorBuilder;
import com.tuya.smart.sdk.enums.ActivatorModelEnum;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by letian on 15/12/22.
 */
public class APActivityTest extends Activity {
    private static final String TAG = "ApConfigggg";
    @Bind(R.id.et_ssid)
    EditText mEtSsid;
    @Bind(R.id.et_password)
    EditText mEtPassword;
    @Bind(R.id.btn_config_now)
    Button mBtnConfigNow;
    @Bind(R.id.ap_config_text)
    TextView mApConfigText;

    StringBuilder mConfigText = new StringBuilder();
    private ITuyaActivator mITuyaActivator;
    private String mToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ap);
        ButterKnife.bind(this);
        getToken();

    }

    @OnClick(R.id.btn_config_now)
    public void startConfig() {
        mITuyaActivator = TuyaActivator.getInstance().newActivator(new ActivatorBuilder()
                .setActivatorModel(ActivatorModelEnum.TY_AP)
                .setPassword(mEtPassword.getText().toString())
                .setSsid(mEtSsid.getText().toString())
                .setToken(mToken)
                .setTimeOut(90)
                .setContext(APActivityTest.this)
                .setListener(new ITuyaSmartActivatorListener() {
                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mApConfigText.append("onActiveError: " + errorCode + " \n");
                        L.d(TAG, "errorCode: " + errorCode);
                    }

                    @Override
                    public void onActiveSuccess(GwDevResp devResp) {
                        mApConfigText.append("onActiveSuccess: " + devResp.getGwId() + " \n");
                    }

                    @Override
                    public void onStep(String step, Object data) {
                        mApConfigText.append("Step:" + step + "\n");
                    }
                }));
        L.d(TAG, "startConfig");
        mITuyaActivator.start();
    }

    public void getToken() {
        TuyaActivator.getInstance().getActivatorToken(new ITuyaActivatorGetToken() {
            @Override
            public void onSuccess(String token) {
                mToken = token;
                mApConfigText.append("getToken success:  \n");
            }

            @Override
            public void onFailure(String errorCode, String errorMsg) {
                mApConfigText.append("getToken failure:  \n");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //销毁处理
        if (mITuyaActivator != null) mITuyaActivator.onDestroy();

    }
}
