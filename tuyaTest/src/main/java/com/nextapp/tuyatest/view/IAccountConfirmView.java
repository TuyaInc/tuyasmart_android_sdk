package com.nextapp.tuyatest.view;

import android.text.Spanned;

import com.tuya.smart.android.mvp.bean.Result;

/**
 * Created by lee on 16/6/3.
 */
public interface IAccountConfirmView {
    /**
     * 获取验证码
     *
     * @return
     */
    String getValidateCode();

    String getPassword();
    /**
     * 设置倒计时
     *
     * @param sec
     */
    void setCountdown(int sec);

    /**
     * 开启验证码获取按钮
     */
    void enableGetValidateCode();

    /**
     * 禁止验证码获取按钮
     */
    void disableGetValidateCode();

    void modelResult(int what, Result result);
    void checkValidateCode();

    void setValidateTip(Spanned tip);

    int getMode();

    int getPlatform();
}
