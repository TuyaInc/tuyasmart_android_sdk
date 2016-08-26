package com.nextapp.tuyatest.presenter.firmware;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Message;
import android.support.v7.app.AlertDialog;

import com.nextapp.tuyatest.R;
import com.nextapp.tuyatest.bean.UpgradeInfoWrapperBean;
import com.nextapp.tuyatest.model.firmware.FirmwareUpgradeModel;
import com.nextapp.tuyatest.model.firmware.IFirmwareUpgradeModel;
import com.nextapp.tuyatest.model.firmware.UpgradeTimeOutCheckModel;
import com.nextapp.tuyatest.test.utils.DialogUtil;
import com.nextapp.tuyatest.utils.FirmwareUtils;
import com.nextapp.tuyatest.utils.ProgressUtil;
import com.nextapp.tuyatest.utils.ToastUtil;
import com.tuya.smart.android.common.utils.L;
import com.tuya.smart.android.device.api.IHardwareUpdateAction;
import com.tuya.smart.android.device.bean.HardwareUpgradeBean;
import com.tuya.smart.android.device.bean.UpgradeInfoBean;
import com.tuya.smart.android.device.enums.RomDevTypeEnum;
import com.tuya.smart.android.mvp.bean.Result;
import com.tuya.smart.android.mvp.presenter.BasePresenter;
import com.tuya.smart.sdk.api.IFirmwareUpgradeListener;
import com.tuya.smart.sdk.enums.FirmwareUpgradeEnum;

import java.util.ArrayList;


/**
 * Created by letian on 16/4/19.
 */
public class FirmwareUpgradeNewPresenter extends BasePresenter implements IFirmwareUpgrade {
    private final UpgradeTimeOutCheckModel mTimeOutCheckModel;
    private IFirmwareUpgradeModel mModel;
    private Context mContext;
    private FirmwareUpgradeProgressView mView;
    private final ArrayList<UpgradeInfoWrapperBean> mWaitForUpgrade;
    public static final String TAG = "FirmwareUpgradeNewPresenter";
    private AlertDialog mAlertDialog;


    public FirmwareUpgradeNewPresenter(Context context, String devId) {
        mContext = context;
        mModel = new FirmwareUpgradeModel(context, mHandler, devId);
        mWaitForUpgrade = new ArrayList<>();
        mTimeOutCheckModel = new UpgradeTimeOutCheckModel(context, mHandler);
        mView = new FirmwareUpgradeProgressView(mContext);
        mModel.setUpgradeDeviceUpdateAction(new IFirmwareUpgradeListener() {
            @Override
            public void onSuccess(FirmwareUpgradeEnum firmwareUpgradeEnum) {
                success();
            }

            @Override
            public void onFailure(FirmwareUpgradeEnum firmwareUpgradeEnum, String s, String s1) {
                error();
            }

            @Override
            public void onProgress(FirmwareUpgradeEnum firmwareUpgradeEnum, int i) {
                progress(i, firmwareUpgradeEnum);
            }
        });
    }

    private void sendCommandSuccess() {
        mTimeOutCheckModel.start();
    }

    private void updating(FirmwareUpgradeEnum dev) {
        if (mAlertDialog != null) {
            mAlertDialog.dismiss();
        }
        mView.showUpgradingTip(dev);
        mView.showProgressSpin();
        mTimeOutCheckModel.start();
    }

    private void error() {
        mView.showFailureView();
        mTimeOutCheckModel.cancel();
    }


    private void progress(int progress, FirmwareUpgradeEnum gw) {
        mView.showUpgradingTip(gw);
        mTimeOutCheckModel.start();
        mTimeOutCheckModel.setProgressTime();
        mView.showProgress(progress);
    }

    private void success() {
        mTimeOutCheckModel.cancel();
        L.d(TAG, "waitForUpgrade success size :" + mWaitForUpgrade.size());
        if (mWaitForUpgrade.isEmpty()) {
            mView.showSuccessView();
        } else {
            upgradeNow(mWaitForUpgrade.remove(0));
        }
    }

    private void upgradeNow(UpgradeInfoWrapperBean upgradeInfoBean) {
        if (upgradeInfoBean.romType == FirmwareUpgradeEnum.TY_DEV) {
            mModel.upgradeDevice();
        } else {
            mModel.upgradeGW();
        }
        sendCommandSuccess();
        mView.showUpgradingTip(upgradeInfoBean.romType);
        mView.showProgressSpin();
    }


    // 自动检测升级
    @Override
    public void autoCheck() {
        mModel.autoCheck();
    }

    // 手动检测更新
    @Override
    public void upgradeCheck() {
        ProgressUtil.showLoading(mContext, mContext.getString(R.string.upgrade_get_infoing));
        mModel.upgradeCheck();
    }


    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case FirmwareUpgradeModel.WHAT_UPGRADE_GW_OR_DEV:
                ProgressUtil.hideLoading();
                //升级网关或者设备
                upgradeGWOrDev((HardwareUpgradeBean) ((Result) msg.obj).getObj());
                break;
            case FirmwareUpgradeModel.WHAT_GET_UPGRADE_INFO_FAILURE:
                ProgressUtil.hideLoading();
                //检测升级失败提醒
                ToastUtil.showToast(mContext, ((Result) msg.obj).getError());
                break;
            case UpgradeTimeOutCheckModel.WHAT_UPGRADE_OUT_OF_TIME:
                L.d(TAG, "timeout onError");
                error();
                break;
            case FirmwareUpgradeModel.WHAT_UPGRADE_NO_NEW_VERSION:
                ProgressUtil.hideLoading();
                //没有新版本
                showCurrentVersionDialog((HardwareUpgradeBean) ((Result) msg.obj).getObj());
                break;
            case FirmwareUpgradeModel.WHAT_UPGRADING:
                //升级中
                L.d(TAG, "升级中 onUpdating");
                updating(((UpgradeInfoWrapperBean) ((Result) msg.obj).getObj()).romType);
                break;
        }

        return super.handleMessage(msg);
    }

    /**
     * 主要处理逻辑: 固件是否升级 固件是否升级中 分享不做升级
     *
     * @param upgradeBean
     */
    private void upgradeGWOrDev(HardwareUpgradeBean upgradeBean) {
        ArrayList<UpgradeInfoWrapperBean> upgradeInfoWrapperBeen = new ArrayList<>();
        if (FirmwareUtils.hasGWHardwareUpdate(upgradeBean)) {
            upgradeInfoWrapperBeen.add(new UpgradeInfoWrapperBean(upgradeBean.getGw(), FirmwareUpgradeEnum.TY_GW));
        }

        if (FirmwareUtils.hasDeviceHardwareUpdate(upgradeBean)) {
            upgradeInfoWrapperBeen.add(new UpgradeInfoWrapperBean(upgradeBean.getDev(), FirmwareUpgradeEnum.TY_DEV));
        }

        if (!upgradeInfoWrapperBeen.isEmpty()) {
            showUpgradeInfoDialog(upgradeInfoWrapperBeen);
        }
    }

    private void showCurrentVersionDialog(HardwareUpgradeBean mUpgradeInfoBean) {
        StringBuilder message = new StringBuilder();
        UpgradeInfoBean gw = mUpgradeInfoBean.getGw();
        UpgradeInfoBean dev = mUpgradeInfoBean.getDev();
        if (dev != null && gw != null) {
            message.append(mContext.getString(R.string.firmware_upgrade_dev));
            message.append(dev.getCurrentVersion());
            message.append("\n");
            message.append(mContext.getString(R.string.firmware_upgrade_gw));
            message.append(gw.getCurrentVersion());
        } else {
            message.append(mContext.getString(R.string.firmware_no_update_one));
            if (dev != null) {
                message.append(dev.getCurrentVersion());
            } else if (gw != null) {
                message.append(gw.getCurrentVersion());
            }
        }

        AlertDialog alertDialog = DialogUtil.customDialog(mContext, mContext.getString(R.string.firmware_no_update_title), message, mContext.getString(R.string.ty_confirm), null, null, null);
        alertDialog.show();
    }

    /**
     * 显示升级信息Dialog
     *
     * @param upgradeInfoWrapperBeen
     */
    private void showUpgradeInfoDialog(final ArrayList<UpgradeInfoWrapperBean> upgradeInfoWrapperBeen) {
        StringBuilder message = new StringBuilder();
        for (UpgradeInfoWrapperBean upgradeInfoBean : upgradeInfoWrapperBeen) {
            if (upgradeInfoBean.romType == FirmwareUpgradeEnum.TY_DEV) {
                message.append(mContext.getString(R.string.firmware_upgrade_dev));
            } else {
                message.append(mContext.getString(R.string.firmware_upgrade_gw));
            }
            message.append(upgradeInfoBean.upgradeInfo.getDesc()).append("\n");
        }
        L.d(TAG, "waitForUpgrade size :" + upgradeInfoWrapperBeen.size());
        mAlertDialog = DialogUtil.customDialog(mContext, mContext.getString(R.string.firmware_has_upgrade_title), message.toString(),
                mContext.getString(R.string.firmware_upgrade_now),
                mContext.getString(R.string.cancel), null,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                mWaitForUpgrade.clear();
                                mWaitForUpgrade.addAll(upgradeInfoWrapperBeen);
                                if (!mWaitForUpgrade.isEmpty()) {
                                    upgradeNow(mWaitForUpgrade.remove(0));
                                }
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                if (FirmwareUtils.hasHardwareUpgradeForced(upgradeInfoWrapperBeen)) {
                                    ((Activity) mContext).finish();
                                }
                                break;
                        }
                    }
                });
        mAlertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mAlertDialog = null;
            }
        });
        mAlertDialog.show();
        mAlertDialog.setCancelable(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mWaitForUpgrade.clear();
        if (mAlertDialog != null) {
            mAlertDialog.dismiss();
            mAlertDialog = null;
        }
        mView.onDestroy();
        mTimeOutCheckModel.onDestroy();
        mModel.onDestroy();
    }
}
