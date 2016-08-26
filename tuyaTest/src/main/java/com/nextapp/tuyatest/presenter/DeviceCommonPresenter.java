package com.nextapp.tuyatest.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;

import com.nextapp.tuyatest.R;
import com.nextapp.tuyatest.activity.DpSendActivity;
import com.nextapp.tuyatest.presenter.firmware.FirmwareUpgradePresenter;
import com.nextapp.tuyatest.test.activity.DeviceTestActivity;
import com.nextapp.tuyatest.test.presenter.DeviceTestPresenter;
import com.nextapp.tuyatest.test.utils.DialogUtil;
import com.nextapp.tuyatest.utils.ActivityUtils;
import com.nextapp.tuyatest.utils.ProgressUtil;
import com.nextapp.tuyatest.utils.ToastUtil;
import com.nextapp.tuyatest.view.IDeviceCommonView;
import com.tuya.smart.android.device.bean.SchemaBean;
import com.tuya.smart.android.device.enums.ModeEnum;
import com.tuya.smart.android.hardware.model.IControlCallback;
import com.tuya.smart.android.mvp.presenter.BasePresenter;
import com.tuya.smart.sdk.TuyaDevice;
import com.tuya.smart.sdk.TuyaUser;
import com.tuya.smart.sdk.bean.DeviceBean;

import junit.framework.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Created by letian on 16/8/4.
 */
public class DeviceCommonPresenter extends BasePresenter {
    public static final String INTENT_DEVID = "intent_gwid";
    private final IDeviceCommonView mView;
    private Context mContext;
    private String mDevId;
    private TuyaDevice mTuyaDevice;
    private FirmwareUpgradePresenter mFirmwareUpgradePresenter;

    public DeviceCommonPresenter(Context context, IDeviceCommonView commonView) {
        mContext = context;
        mView = commonView;
        initData();
        initUpgrade();
    }

    private void initList() {
        List<SchemaBean> schemaList = getSchemaList();
        mView.setSchemaData(schemaList);
    }

    private void initData() {
        mDevId = ((Activity) mContext).getIntent().getStringExtra(INTENT_DEVID);

        mTuyaDevice = new TuyaDevice(mDevId);
    }

    private void initUpgrade() {
        mFirmwareUpgradePresenter = new FirmwareUpgradePresenter(mContext, mDevId);
        mFirmwareUpgradePresenter.autoCheck();
    }


    public List<SchemaBean> getSchemaList() {
        DeviceBean dev = TuyaUser.getDeviceInstance().getDev(mDevId);
        if (dev == null) return new ArrayList<>();
        Map<String, SchemaBean> schemaMap = dev.getSchemaMap();
        List<SchemaBean> schemaBeanArrayList = new ArrayList<>();
        for (Map.Entry<String, SchemaBean> entry : schemaMap.entrySet()) {
            schemaBeanArrayList.add(entry.getValue());
        }

        Collections.sort(schemaBeanArrayList, new Comparator<SchemaBean>() {
            @Override
            public int compare(SchemaBean lhs, SchemaBean rhs) {
                return Integer.valueOf(lhs.getId()) < Integer.valueOf(rhs.getId()) ? -1 : 1;
            }
        });
        return schemaBeanArrayList;
    }

    public String getDevName() {
        DeviceBean dev = TuyaUser.getDeviceInstance().getDev(mDevId);
        if (dev == null) return "";
        return dev.getName();
    }

    public void getData() {
        initList();
    }

    public void onItemClick(SchemaBean schemaBean) {
        if (schemaBean == null) return;
        if (!TextUtils.equals(schemaBean.getMode(), ModeEnum.RO.getType())) {
            Intent intent = new Intent(mContext, DpSendActivity.class);
            intent.putExtra(DpSendPresenter.INTENT_DPID, schemaBean.getId());
            intent.putExtra(DpSendPresenter.INTENT_DEVID, mDevId);
            mContext.startActivity(intent);
        }
    }

    public void renameDevice() {
        DialogUtil.simpleInputDialog(mContext, mContext.getString(R.string.rename), getDevName(), false, new DialogUtil.SimpleInputDialogInterface() {
            @Override
            public void onPositive(DialogInterface dialog, String inputText) {
                int limit = mContext.getResources().getInteger(R.integer.change_device_name_limit);
                if (inputText.length() > limit) {
                    ToastUtil.showToast(mContext, R.string.ty_modify_device_name_length_limit);
                } else {
                    renameTitleToServer(inputText);
                }
            }

            @Override
            public void onNegative(DialogInterface dialog) {

            }
        });
    }

    private void renameTitleToServer(final String titleName) {
        ProgressUtil.showLoading(mContext, R.string.loading);
        mTuyaDevice.renameDevice(titleName, new IControlCallback() {
            @Override
            public void onError(String code, String error) {
                ProgressUtil.hideLoading();
                ToastUtil.showToast(mContext, error);
            }

            @Override
            public void onSuccess() {
                ProgressUtil.hideLoading();
                mView.updateTitle(titleName);
            }
        });
    }

    public void checkUpdate() {
        mFirmwareUpgradePresenter.upgradeCheck();
    }

    public void resetFactory() {
        DialogUtil.simpleConfirmDialog(mContext, mContext.getString(R.string.ty_control_panel_factory_reset_info),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == DialogInterface.BUTTON_POSITIVE) {
                            ProgressUtil.showLoading(mContext, R.string.ty_control_panel_factory_reseting);
                            mTuyaDevice.resetFactory(new IControlCallback() {
                                @Override
                                public void onError(String code, String error) {
                                    ProgressUtil.hideLoading();
                                    ToastUtil.shortToast(mContext, R.string.ty_control_panel_factory_reset_fail);
                                }

                                @Override
                                public void onSuccess() {
                                    ProgressUtil.hideLoading();
                                    ToastUtil.shortToast(mContext, R.string.ty_control_panel_factory_reset_succ);
                                    ((Activity) mContext).finish();
                                }
                            });
                        }
                    }
                });
    }


    public void removeDevice() {
        ProgressUtil.showLoading(mContext, R.string.loading);
        mTuyaDevice.removeDevice(new IControlCallback() {
            @Override
            public void onError(String code, String error) {
                ProgressUtil.hideLoading();
                ToastUtil.showToast(mContext, error);
            }

            @Override
            public void onSuccess() {
                ProgressUtil.hideLoading();
                ((Activity) mContext).finish();
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mFirmwareUpgradePresenter.onDestroy();
    }

    public void testMode() {
        Intent intent = new Intent(mContext, DeviceTestActivity.class);
        intent.putExtra(DeviceTestPresenter.INTENT_DEVICE_ID, mDevId);
        ActivityUtils.startActivity((Activity) mContext, intent, ActivityUtils.ANIMATE_FORWARD, true);
    }
}
