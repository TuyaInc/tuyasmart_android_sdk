package com.nextapp.tuyatest.presenter.firmware;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Message;

import com.nextapp.tuyatest.R;
import com.nextapp.tuyatest.model.firmware.FirmwareUpgradeModel;
import com.nextapp.tuyatest.test.utils.DialogUtil;
import com.nextapp.tuyatest.utils.ProgressUtil;
import com.nextapp.tuyatest.utils.ToastUtil;
import com.tuya.smart.android.device.bean.HardwareUpgradeBean;
import com.tuya.smart.android.device.bean.UpgradeInfoBean;
import com.tuya.smart.android.mvp.bean.Result;
import com.tuya.smart.android.mvp.presenter.BasePresenter;
import com.tuya.smart.sdk.api.IFirmwareUpgradeListener;
import com.tuya.smart.sdk.enums.FirmwareUpgradeEnum;

/**
 * Created by letian on 16/4/19.
 */
public class FirmwareUpgradeOldPresenter extends BasePresenter implements IFirmwareUpgrade {
    private static final int WHAT_UPGRADE_OUT_OF_TIME = 1001;
    private final Context mContext;
    private final FirmwareUpgradeModel mModel;
    private UpgradeInfoBean mUpgradeInfoBean;

    public FirmwareUpgradeOldPresenter(Context context, String devId) {
        mContext = context;
        mModel = new FirmwareUpgradeModel(context, mHandler, devId);
        mModel.setUpgradeDeviceUpdateAction(new IFirmwareUpgradeListener() {
            @Override
            public void onSuccess(FirmwareUpgradeEnum firmwareUpgradeEnum) {
                mHandler.removeMessages(WHAT_UPGRADE_OUT_OF_TIME);
                ProgressUtil.hideLoading();
                ToastUtil.showToast(mContext, R.string.firmware_upgrade_success);
            }

            @Override
            public void onFailure(FirmwareUpgradeEnum firmwareUpgradeEnum, String s, String s1) {
                onUpdateError();
            }

            @Override
            public void onProgress(FirmwareUpgradeEnum firmwareUpgradeEnum, int i) {
                ProgressUtil.showLoading(mContext, R.string.firmware_upgrading);
            }
        });
    }

    @Override
    public void autoCheck() {
        mModel.autoCheck();
    }

    @Override
    public void upgradeCheck() {
        ProgressUtil.showLoading(mContext, mContext.getString(R.string.upgrade_get_infoing));
        mModel.upgradeCheck();
    }

    private void upgradeNow() {
        ProgressUtil.showLoading(mContext, mContext.getString(R.string.firmware_upgrading));
        mModel.upgradeDevice();
        mHandler.sendEmptyMessageDelayed(WHAT_UPGRADE_OUT_OF_TIME, mUpgradeInfoBean.getTimeout() * 1000);
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case FirmwareUpgradeModel.WHAT_GET_UPGRADE_INFO_FAILURE:
                ProgressUtil.hideLoading();
                ToastUtil.showToast(mContext, ((Result) msg.obj).getError());
                break;
            case FirmwareUpgradeModel.WHAT_UPGRADE_GW_OR_DEV:
                mUpgradeInfoBean = ((HardwareUpgradeBean) ((Result) msg.obj).getObj()).getDev();
                ProgressUtil.hideLoading();
                showUpgradeInfoDialog(mUpgradeInfoBean);
                break;
            case FirmwareUpgradeModel.WHAT_UPGRADING:
                ProgressUtil.showLoading(mContext, R.string.firmware_upgrading);
                break;
            case WHAT_UPGRADE_OUT_OF_TIME:
                onUpdateError();
                break;
            case FirmwareUpgradeModel.WHAT_UPGRADE_NO_NEW_VERSION:
                ProgressUtil.hideLoading();
                showCurrentVersionDialog(((HardwareUpgradeBean) ((Result) msg.obj).getObj()).getDev());
        }
        return super.handleMessage(msg);
    }

    private void showCurrentVersionDialog(UpgradeInfoBean mUpgradeInfoBean) {
        DialogUtil.simpleTipDialog(mContext, mContext.getString(R.string.firmware_no_update, mUpgradeInfoBean.getCurrentVersion()), null);
    }

    /**
     * 显示升级信息Dialog
     *
     * @param bizResult
     */
    private void showUpgradeInfoDialog(UpgradeInfoBean bizResult) {
        DialogUtil.simpleConfirmDialog(mContext, mContext.getString(R.string.new_version_title, bizResult.getVersion()),
                bizResult.getDesc(),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                upgradeNow();
                                break;
                        }
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ProgressUtil.hideLoading();
        mModel.onDestroy();
    }

    private void onUpdateError() {
        mHandler.removeMessages(WHAT_UPGRADE_OUT_OF_TIME);
        ProgressUtil.hideLoading();
        DialogUtil.customDialog(mContext, mContext.getString(R.string.firmware_upgrade_failure),
                mContext.getString(R.string.firmware_upgrade_failure_description),
                mContext.getString(R.string.retry), mContext.getString(R.string.cancel),
                null, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                upgradeNow();
                                break;
                        }
                        dialog.cancel();
                    }
                }).show();
    }

}
