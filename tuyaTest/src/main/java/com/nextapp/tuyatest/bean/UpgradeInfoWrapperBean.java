package com.nextapp.tuyatest.bean;

import com.tuya.smart.android.device.bean.UpgradeInfoBean;
import com.tuya.smart.android.device.enums.RomDevTypeEnum;
import com.tuya.smart.sdk.enums.FirmwareUpgradeEnum;

/**
 * Created by letian on 16/4/21.
 */
public class UpgradeInfoWrapperBean {

    public UpgradeInfoBean upgradeInfo;
    public FirmwareUpgradeEnum romType;

    public UpgradeInfoWrapperBean(UpgradeInfoBean upgradeInfoBean, FirmwareUpgradeEnum romDevTypeEnum) {
        this.upgradeInfo = upgradeInfoBean;
        this.romType = romDevTypeEnum;
    }

}
