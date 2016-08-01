package com.nextapp.tuyatest.presenter;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;

import com.nextapp.tuyatest.R;
import com.nextapp.tuyatest.activity.AccountConfirmActivity;
import com.nextapp.tuyatest.model.PersonalInfoModel;
import com.nextapp.tuyatest.test.event.EventSender;
import com.nextapp.tuyatest.test.utils.DialogUtil;
import com.nextapp.tuyatest.utils.CommonUtil;
import com.nextapp.tuyatest.utils.LoginHelper;
import com.nextapp.tuyatest.utils.ProgressUtil;
import com.nextapp.tuyatest.utils.ToastUtil;
import com.nextapp.tuyatest.view.IPersonalInfoView;
import com.tuya.smart.android.mvp.bean.Result;
import com.tuya.smart.android.mvp.presenter.BasePresenter;
import com.tuya.smart.android.user.bean.User;
import com.tuya.smart.sdk.TuyaUser;

/**
 * Created by letian on 15/6/16.
 */
public class PersonalInfoPresenter extends BasePresenter {

    private final Activity mActivity;
    private PersonalInfoModel mPersonalInfoModel;
    private IPersonalInfoView mView;

    public PersonalInfoPresenter(Activity activity, IPersonalInfoView view) {
        super(activity);
        mActivity = activity;
        mView = view;
        mPersonalInfoModel = new PersonalInfoModel(mActivity, mHandler);
    }

    public void reNickName(String titleName) {
        ProgressUtil.showLoading(mActivity, R.string.loading);
        mPersonalInfoModel.reNickName(titleName);
    }

    public String getNickName() {
        return mPersonalInfoModel.getNickName();
    }

    private void saveNickName(String nickName) {
        EventSender.personalInfoChanged();
        ProgressUtil.hideLoading();
        mView.reNickName(nickName);
    }

    public void logout() {
        mPersonalInfoModel.logout();
        mView.onLogout();
    }

    public String getMobile() {
        return mPersonalInfoModel.getMobile();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPersonalInfoModel.onDestroy();
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case PersonalInfoModel.RENAME_NICKNAME_ERROR:
                ToastUtil.showToast(mActivity, ((Result) msg.obj).error);
                ProgressUtil.hideLoading();
                break;
            case PersonalInfoModel.RENAME_NICKNAME_SUCCESS:
                saveNickName((String) ((Result) msg.obj).getObj());
                break;
        }

        return super.handleMessage(msg);
    }

    public void resetPassword() {
        User user = TuyaUser.getUserInstance().getUser();
        int accountType;
        String strAccount;
        if (!TextUtils.isEmpty(user.getMobile())) {
            accountType = AccountConfirmActivity.PLATFORM_PHONE;
            strAccount = CommonUtil.getPhoneNumberFormMobile(user.getMobile());
            AccountConfirmActivity.gotoAccountConfirmActivityForResult(mActivity, strAccount, user.getPhoneCode(), AccountConfirmActivity.MODE_CHANGE_PASSWORD, accountType, 0);
        } else if (!TextUtils.isEmpty(user.getEmail())) {
            accountType = AccountConfirmActivity.PLATFORM_EMAIL;
            strAccount = user.getEmail();
            AccountConfirmActivity.gotoAccountConfirmActivityForResult(mActivity, strAccount, user.getPhoneCode(), AccountConfirmActivity.MODE_CHANGE_PASSWORD, accountType, 0);
        }
    }
}
