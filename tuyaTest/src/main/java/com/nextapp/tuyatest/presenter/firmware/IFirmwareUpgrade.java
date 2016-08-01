package com.nextapp.tuyatest.presenter.firmware;

import com.tuya.smart.android.mvp.model.IModel;

/**
 * Created by letian on 16/4/19.
 */
public interface IFirmwareUpgrade extends IModel {
    /**
     * 自动检测
     */
    void autoCheck();

    /**
     * 手动检测升级
     */
    void upgradeCheck();
}
