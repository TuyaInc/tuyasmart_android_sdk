package com.tuya.smart.android.demo.view;


import com.tuya.smart.android.mvp.view.IView;

/**
 * Created by letian on 15/6/29.
 */
public interface IECView extends IView {
    /**
     * 设置SSID
     *
     * @param ssId
     */
    void setWifiSSID(String ssId);

    /**
     * 设置WiFi密码
     *
     * @param pass
     */
    void setWifiPass(String pass);


    /**
     * 获取wifi 密码
     *
     * @return
     */
    String getWifiPass();

    /**
     * 获取SSID
     *
     * @return
     */
    String getWifiSsId();

    void showNoWifi();

    /**
     * 显示 只支持 网络提示
     */
    void show5gTip();

    /**
     * 隐藏 5g 网络提示
     */
    void hide5gTip();
}
