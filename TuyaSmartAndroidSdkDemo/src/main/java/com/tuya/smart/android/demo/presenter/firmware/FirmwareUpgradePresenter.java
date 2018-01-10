package com.tuya.smart.android.demo.presenter.firmware;

import android.content.Context;

import com.tuya.smart.android.common.utils.TuyaUtil;
import com.tuya.smart.android.device.TuyaSmartDevice;
import com.tuya.smart.android.device.config.GWConfig;
import com.tuya.smart.android.mvp.presenter.BasePresenter;
import com.tuya.smart.sdk.bean.DeviceBean;

/**
 * Created by letian on 15/6/17.
 */
public class FirmwareUpgradePresenter extends BasePresenter implements IFirmwareUpgrade {

    private static final String TAG = "FirmwareUpgradePresenter";

    public IFirmwareUpgrade mIFirmwareUpgrade;

    public FirmwareUpgradePresenter(Context context, String devId) {
        DeviceBean deviceBean = TuyaSmartDevice.getInstance().getDev(devId);
        if (deviceBean == null) return;
        if (TuyaUtil.checkBvVersion(deviceBean.getBv(), GWConfig.BV)) {
            mIFirmwareUpgrade = new FirmwareUpgradeNewPresenter(context, devId);
        } else {
            mIFirmwareUpgrade = new FirmwareUpgradeOldPresenter(context, devId);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mIFirmwareUpgrade != null) {
            mIFirmwareUpgrade.onDestroy();
        }
    }

    @Override
    public void autoCheck() {
        if (mIFirmwareUpgrade != null) {
            mIFirmwareUpgrade.autoCheck();
        }
    }

    @Override
    public void upgradeCheck() {
        if (mIFirmwareUpgrade != null) {
            mIFirmwareUpgrade.upgradeCheck();
        }
    }
}
