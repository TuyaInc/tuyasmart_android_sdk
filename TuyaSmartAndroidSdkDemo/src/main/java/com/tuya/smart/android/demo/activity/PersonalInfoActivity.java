package com.tuya.smart.android.demo.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.TextView;

import com.tuya.smart.android.demo.R;
import com.tuya.smart.android.demo.presenter.PersonalInfoPresenter;
import com.tuya.smart.android.demo.test.utils.DialogUtil;
import com.tuya.smart.android.demo.utils.LoginHelper;
import com.tuya.smart.android.demo.utils.ProgressUtil;
import com.tuya.smart.android.demo.view.IPersonalInfoView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by letian on 15/6/15.
 */
public class PersonalInfoActivity extends BaseActivity implements IPersonalInfoView {
    private PersonalInfoPresenter mPersonalInfoPresenter;

    @BindView(R.id.tv_renickname)
    public TextView mNickName;

    @BindView(R.id.tv_phone)
    public TextView mPhoneView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
        ButterKnife.bind(this);
        initToolbar();
        initMenu();
        initPresenter();
        initData();
    }

    private void initMenu() {
        setDisplayHomeAsUpEnabled();
        setTitle(getString(R.string.personal_center));
    }

    private void initPresenter() {
        mPersonalInfoPresenter = new PersonalInfoPresenter(this, this);
    }

    private void initData() {
        mPhoneView.setText(mPersonalInfoPresenter.getMobile());
        mNickName.setText(mPersonalInfoPresenter.getNickName());
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

    @OnClick(R.id.rl_reset_login_pwd)
    public void onClickResetPassword() {
        mPersonalInfoPresenter.resetPassword();
    }

    @OnClick(R.id.btn_logout)
    public void logout() {
        ProgressUtil.showLoading(this, R.string.ty_logout_loading);
        mPersonalInfoPresenter.logout();
    }

    @Override
    public void reNickName(String nickName) {
        mNickName.setText(nickName);
    }

    @Override
    public void onLogout() {
        LoginHelper.reLogin(this, false);
        ProgressUtil.hideLoading();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPersonalInfoPresenter.onDestroy();
    }
}
