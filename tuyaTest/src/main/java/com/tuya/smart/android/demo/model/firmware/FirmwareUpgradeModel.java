package com.tuya.smart.android.demo.model.firmware;

import android.content.Context;

import com.tuya.smart.android.common.utils.SafeHandler;
import com.tuya.smart.android.demo.bean.UpgradeInfoWrapperBean;
import com.tuya.smart.android.demo.enums.UpgradeStatusEnum;
import com.tuya.smart.android.demo.utils.FirmwareUtils;
import com.tuya.smart.android.device.api.IHardwareUpdateInfo;
import com.tuya.smart.android.device.bean.HardwareUpgradeBean;
import com.tuya.smart.android.device.bean.UpgradeInfoBean;
import com.tuya.smart.android.mvp.model.BaseModel;
import com.tuya.smart.sdk.TuyaDevice;
import com.tuya.smart.sdk.api.IFirmwareUpgradeListener;
import com.tuya.smart.sdk.enums.FirmwareUpgradeEnum;

/**
 * Created by letian on 16/4/18.
 */
public class FirmwareUpgradeModel extends BaseModel implements IFirmwareUpgradeModel {

    public static final int WHAT_GET_UPGRADE_INFO_FAILURE = 0x01;
    public static final int WHAT_UPGRADE_GW_OR_DEV = 0x02;
    public static final int WHAT_UPGRADE_NO_NEW_VERSION = 0x03;
    public static final int WHAT_UPGRADING = 0x04;

    private final TuyaDevice mTuyaDevice;


    public FirmwareUpgradeModel(Context ctx, SafeHandler handler, String devId) {
        super(ctx, handler);
        mTuyaDevice = new TuyaDevice(devId);
    }

    @Override
    public void onDestroy() {
        if (mTuyaDevice != null) {
            mTuyaDevice.onDestroy();
        }
    }

    @Override
    public void upgradeDevice() {
        mTuyaDevice.upgradeFirmware(FirmwareUpgradeEnum.TY_DEV);
    }

    @Override
    public void upgradeGW() {
        mTuyaDevice.upgradeFirmware(FirmwareUpgradeEnum.TY_GW);
    }

    @Override
    public void setUpgradeDeviceUpdateAction(IFirmwareUpgradeListener listener) {
        mTuyaDevice.setHardwareUpgradeListener(listener);
    }

    @Override
    public void autoCheck() {
        mTuyaDevice.getFirmwareUpgradeInfo(new IHardwareUpdateInfo() {

            @Override
            public void onError(String s, String s1) {
            }

            @Override
            public void onSuccess(HardwareUpgradeBean upgradeInfoBean) {
                UpgradeStatusEnum hardwareUpgradeStatus = FirmwareUtils.getHardwareUpgradeStatus(upgradeInfoBean);
                switch (hardwareUpgradeStatus) {
                    case UPGRADING:
                        //升级中
                        upgrading(upgradeInfoBean);
                        break;
                    case NO_UPGRADE:
                        break;
                    case HAS_FORCE_OR_REMIND_UPGRADE:
                        //有提醒、强制升级进入升级
                        resultSuccess(WHAT_UPGRADE_GW_OR_DEV, upgradeInfoBean);
                        break;
                    case HAS_CHECK_UPGRADE:
                        break;
                }
            }
        });
    }

    @Override
    public void upgradeCheck() {
        mTuyaDevice.getFirmwareUpgradeInfo(new IHardwareUpdateInfo() {
            @Override
            public void onError(String s, String s1) {
                resultError(WHAT_GET_UPGRADE_INFO_FAILURE, s, s1);
            }

            @Override
            public void onSuccess(HardwareUpgradeBean upgradeInfoBean) {
                UpgradeStatusEnum hardwareUpgradeStatus = FirmwareUtils.getHardwareUpgradeStatus(upgradeInfoBean);
                switch (hardwareUpgradeStatus) {
                    case UPGRADING:
                        //升级中
                        upgrading(upgradeInfoBean);
                        break;
                    case NO_UPGRADE:
                        resultSuccess(WHAT_UPGRADE_NO_NEW_VERSION, upgradeInfoBean);
                        break;
                    case HAS_FORCE_OR_REMIND_UPGRADE:
                    case HAS_CHECK_UPGRADE:
                        resultSuccess(WHAT_UPGRADE_GW_OR_DEV, upgradeInfoBean);
                        break;
                }
            }


        });
    }

    private void upgrading(HardwareUpgradeBean upgradeInfoBean) {
        UpgradeInfoBean upgradingDevice = FirmwareUtils.getUpgradingDevice(upgradeInfoBean);
        FirmwareUpgradeEnum upgradeEnum;
        if (upgradeInfoBean.getGw() != null)
            upgradeEnum = FirmwareUpgradeEnum.TY_GW;
        else if (upgradeInfoBean.getDev() != null) {
            upgradeEnum = FirmwareUpgradeEnum.TY_DEV;
        } else return;
        resultSuccess(WHAT_UPGRADING, new UpgradeInfoWrapperBean(upgradingDevice, upgradeEnum));
    }


}