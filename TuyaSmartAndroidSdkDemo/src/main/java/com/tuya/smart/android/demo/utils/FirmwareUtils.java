package com.tuya.smart.android.demo.utils;

import com.tuya.smart.android.demo.bean.UpgradeInfoWrapperBean;
import com.tuya.smart.android.demo.enums.UpgradeStatusEnum;
import com.tuya.smart.android.device.bean.HardwareUpgradeBean;
import com.tuya.smart.android.device.bean.UpgradeInfoBean;
import com.tuya.smart.android.device.enums.RomUpgradeStatusEnum;
import com.tuya.smart.android.device.enums.RomUpgradeTypeEnum;

import java.util.ArrayList;

/**
 * Created by letian on 16/4/18.
 */
public class FirmwareUtils {


    /**
     * 固件是否有新版本
     *
     * @param upgradeInfoBean
     * @return
     */
    public static boolean hasHardwareUpdate(HardwareUpgradeBean upgradeInfoBean) {
        return hasGWHardwareUpdate(upgradeInfoBean) || hasDeviceHardwareUpdate(upgradeInfoBean);
    }

    public static boolean hasGWHardwareUpdateWithoutCheck(HardwareUpgradeBean upgradeBean) {
        return hasGWHardwareUpdate(upgradeBean) && !isHardwareCheck(upgradeBean.getGw());
    }

    public static boolean hasDeviceHardwareUpdateWithoutCheck(HardwareUpgradeBean upgradeBean) {
        return hasDeviceHardwareUpdate(upgradeBean) && !isHardwareCheck(upgradeBean.getDev());
    }

    public static boolean hasGWHardwareUpdate(HardwareUpgradeBean upgradeBean) {
        UpgradeInfoBean gw = upgradeBean.getGw();
        return gw != null && RomUpgradeStatusEnum.to(gw.getUpgradeStatus()) == RomUpgradeStatusEnum.NEW_VERSION;
    }

    public static boolean hasDeviceHardwareUpdate(HardwareUpgradeBean upgradeBean) {
        UpgradeInfoBean dev = upgradeBean.getDev();
        return dev != null && RomUpgradeStatusEnum.to(dev.getUpgradeStatus()) == RomUpgradeStatusEnum.NEW_VERSION;
    }

    public static boolean isHardwareUpdating(UpgradeInfoBean upgradeBean) {
        return upgradeBean != null && RomUpgradeStatusEnum.to(upgradeBean.getUpgradeStatus()) == RomUpgradeStatusEnum.UPGRADING;
    }

    public static boolean isHardwareUpdating(HardwareUpgradeBean upgradeBean) {
        return upgradeBean != null && (isHardwareUpdating(upgradeBean.getDev()) || isHardwareUpdating(upgradeBean.getGw()));
    }

    public static boolean isHardwareForced(UpgradeInfoBean upgradeInfoBean) {
        return upgradeInfoBean != null && RomUpgradeTypeEnum.to(upgradeInfoBean.getUpgradeType()) == RomUpgradeTypeEnum.FORCED;
    }

    public static boolean isHardwareCheck(UpgradeInfoBean upgradeInfoBean) {
        return upgradeInfoBean != null && RomUpgradeTypeEnum.to(upgradeInfoBean.getUpgradeType()) == RomUpgradeTypeEnum.CHECK;
    }

    public static boolean hasHardwareUpgradeForced(ArrayList<UpgradeInfoWrapperBean> waitForUpgrade) {
        for (UpgradeInfoWrapperBean upgradeInfoBean : waitForUpgrade) {
            if (isHardwareForced(upgradeInfoBean.upgradeInfo))
                return true;
        }
        return false;
    }

    public static UpgradeInfoBean getUpgradingDevice(HardwareUpgradeBean upgradeInfoBean) {
        UpgradeInfoBean dev = upgradeInfoBean.getDev();
        if (FirmwareUtils.isHardwareUpdating(dev)) {
            return dev;
        }
        UpgradeInfoBean gw = upgradeInfoBean.getGw();
        if (FirmwareUtils.isHardwareUpdating(gw)) {
            return gw;
        }
        return null;
    }

    public static boolean hasHardwareUpdateWithoutCheck(HardwareUpgradeBean upgradeInfoBean) {
        return hasGWHardwareUpdateWithoutCheck(upgradeInfoBean) || hasDeviceHardwareUpdateWithoutCheck(upgradeInfoBean);
    }

    public static UpgradeStatusEnum getHardwareUpgradeStatus(HardwareUpgradeBean upgradeBean) {
        UpgradeInfoBean upgradingDevice = FirmwareUtils.getUpgradingDevice(upgradeBean);
        if (upgradingDevice != null) {
            return UpgradeStatusEnum.UPGRADING;
        }
        if (!FirmwareUtils.hasHardwareUpdate(upgradeBean))
            return UpgradeStatusEnum.NO_UPGRADE;
        if (FirmwareUtils.hasHardwareUpdateWithoutCheck(upgradeBean)) {
            return UpgradeStatusEnum.HAS_FORCE_OR_REMIND_UPGRADE;
        }
        return UpgradeStatusEnum.HAS_CHECK_UPGRADE;
    }
}