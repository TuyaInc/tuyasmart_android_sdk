package com.tuya.smart.android.demo.model;

import android.content.Context;

import com.tuya.smart.android.common.utils.SafeHandler;
import com.tuya.smart.android.mvp.model.BaseModel;
import com.tuya.smart.android.user.api.ILogoutCallback;
import com.tuya.smart.android.user.api.IReNickNameCallback;
import com.tuya.smart.android.user.bean.User;
import com.tuya.smart.sdk.TuyaUser;

/**
 * Created by letian on 15/6/22.
 */
public class PersonalInfoModel extends BaseModel implements IPersonalInfoModel {
    public static final int RENAME_NICKNAME_ERROR = 0x01;
    public static final int RENAME_NICKNAME_SUCCESS = 0x02;

    public static final int WHAT_SETTING_LOGOUT_ERROR = 0x03;

    public static final int WHAT_SETTING_LOGOUT_SUCCESS = 0x04;

    public PersonalInfoModel(Context ctx, SafeHandler handler) {
        super(ctx, handler);
    }

    @Override
    public void onDestroy() {
        //do nothing
    }

    @Override
    public String getNickName() {
        User user = TuyaUser.getUserInstance().getUser();
        if (user == null) return "";
        return user.getNickName();
    }

    @Override
    public String getMobile() {
        User user = TuyaUser.getUserInstance().getUser();
        if (user == null) return "";
        //这里恒定显示手机号
        return user.getMobile();
    }

    @Override
    public void reNickName(final String nickName) {
        TuyaUser.getUserInstance().reRickName(nickName, new IReNickNameCallback() {
            @Override
            public void onSuccess() {
                resultSuccess(RENAME_NICKNAME_SUCCESS, nickName);
            }

            @Override
            public void onError(String s, String s1) {
                resultError(RENAME_NICKNAME_ERROR, s, s1);
            }
        });

    }

    @Override
    public void logout() {
        TuyaUser.getUserInstance().logout(new ILogoutCallback() {
            @Override
            public void onSuccess() {
                resultSuccess(WHAT_SETTING_LOGOUT_SUCCESS, true);
            }

            @Override
            public void onError(String errorCode, String errorMsg) {
                resultError(WHAT_SETTING_LOGOUT_ERROR, errorCode, errorMsg);
            }
        });
    }
}
