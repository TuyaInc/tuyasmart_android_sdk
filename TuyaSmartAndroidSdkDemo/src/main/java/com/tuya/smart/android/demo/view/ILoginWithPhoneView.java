package com.tuya.smart.android.demo.view;


import com.tuya.smart.android.mvp.bean.Result;
import com.tuya.smart.android.mvp.view.IView;

/**
 * Created by mikeshou on 15/6/17.
 */
public interface ILoginWithPhoneView extends IView {

    /**
     * 获取电话号码
     *
     * @return
     */
    public String getPhone();

    /**
     * 获取验证码
     *
     * @return
     */
    public String getValidateCode();

    /**
     * 设置倒计时
     *
     * @param sec
     */
    public void setCountdown(int sec);

    /**
     * 开启验证码获取按钮
     */
    public void enableGetValidateCode();

    /**
     * 禁止验证码获取按钮
     */
    public void disableGetValidateCode();

    /**
     * 数据获取结果返回
     *
     * @param what
     * @param result
     */
    public void modelResult(int what, Result result);

    /**
     * 设置国家信息
     *
     * @param name
     * @param code
     */
    public void setCountry(String name, String code);


    void checkValidateCode();
}
