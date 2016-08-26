package com.nextapp.tuyatest.model.firmware;

/**
 * Created by letian on 16/4/18.
 */

import com.nextapp.tuyatest.presenter.firmware.IFirmwareUpgrade;
import com.tuya.smart.android.device.api.IHardwareUpdateAction;
import com.tuya.smart.sdk.api.IFirmwareUpgradeListener;

/**
 * Created by letian on 15/7/3.
 */
public interface IFirmwareUpgradeModel extends IFirmwareUpgrade {

    /**
     * 立即对设备升级
     */
    void upgradeDevice();

    /**
     * 立即对网关升级
     */
    void upgradeGW();

    /**
     *
     */
    void setUpgradeDeviceUpdateAction(IFirmwareUpgradeListener listener);
}
