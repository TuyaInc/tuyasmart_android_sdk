package com.nextapp.tuyatest.activity;

import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.nextapp.tuyatest.R;
import com.nextapp.tuyatest.presenter.PersonalInfoPresenter;
import com.nextapp.tuyatest.test.utils.DialogUtil;
import com.nextapp.tuyatest.utils.ProgressUtil;
import com.nextapp.tuyatest.view.IPersonalInfoView;
import com.tuya.smart.android.mvp.bean.Result;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by letian on 15/6/15.
 */
public class PersonalInfoActivity extends BaseActivity implements IPersonalInfoView {

    private PersonalInfoPresenter mPersonalInfoPresenter;

    @Bind(R.id.tv_renickname)
    public TextView mNickName;

    @Bind(R.id.tv_phone)
    public TextView mPhoneView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
        ButterKnife.bind(this);
        initToolbar();
        initView();
        initMenu();
        initPresenter();
        initData();
    }

    private void initData() {
        mPhoneView.setText(mPersonalInfoPresenter.getMobile());
        mNickName.setText(mPersonalInfoPresenter.getNickName());
    }

    private void initMenu() {
        setDisplayHomeAsUpEnabled();
        setTitle(getString(R.string.personal_center));
    }

    @OnClick(R.id.rl_renickname)
    public void onClickRenickname() {
        DialogUtil.simpleInputDialog(this, getString(R.string.reNickName), mNickName.getText().toString(), false, new DialogUtil.SimpleInputDialogInterface() {
            @Override
            public void onPositive(DialogInterface dialog, String inputText) {
                mPersonalInfoPresenter.reNickName(inputText);
            }

            @Override
            public void onNegative(DialogInterface dialog) {

            }
        });

    }


    private void initPresenter() {
        mPersonalInfoPresenter = new PersonalInfoPresenter(this, this);
    }

    @Override
    public void reNickName(String nickName) {
        mNickName.setText(nickName);
    }

    @OnClick(R.id.rl_reset_login_pwd)
    public void onClickResetPassword() {
        mPersonalInfoPresenter.resetPassword();
    }

    private void initView() {
        findViewById(R.id.btn_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.btn_logout) {
                    logout();
                }
            }
        });
    }

    public void logout() {
        ProgressUtil.showLoading(this, R.string.ty_logout_loading);
        mPersonalInfoPresenter.logout();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        mPersonalInfoPresenter.onDestroy();
    }

    @Override
    public void onLogout(Result result) {
        ProgressUtil.hideLoading();
    }
}
