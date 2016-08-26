package com.tuya.smart.android.demo.model.firmware;


import com.tuya.smart.android.mvp.view.IView;

/**
 * Created by letian on 15/7/3.
 */
public interface IFirmwareUpgradeView extends IView {
    /**
     * 固件无更新
     */
    void noUpdate();

    /**
     * 固件发现新版本
     *
     * @param version
     */
    void hasUpdate(String version);

    /**
     * 获取当前版本
     *
     * @return
     */
    String getNowVersion();


}
