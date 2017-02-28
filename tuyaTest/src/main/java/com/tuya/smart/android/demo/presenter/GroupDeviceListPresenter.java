package com.tuya.smart.android.demo.presenter;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Message;
import android.text.TextUtils;

import com.tuya.smart.android.demo.R;
import com.tuya.smart.android.demo.activity.GroupEditDeviceActivity;
import com.tuya.smart.android.demo.test.utils.DialogUtil;
import com.tuya.smart.android.demo.utils.MessageUtil;
import com.tuya.smart.android.demo.utils.ToastUtil;
import com.tuya.smart.android.demo.view.IGroupDeviceListView;
import com.tuya.smart.android.device.event.DeviceEventSender;
import com.tuya.smart.android.hardware.model.IControlCallback;
import com.tuya.smart.android.mvp.bean.Result;
import com.tuya.smart.android.mvp.presenter.BasePresenter;
import com.tuya.smart.sdk.TuyaGroup;
import com.tuya.smart.sdk.api.ICreateGroupCallback;
import com.tuya.smart.sdk.api.IGetDevicesInGroupCallback;
import com.tuya.smart.sdk.api.IGetDevsFromGroupByPidCallback;
import com.tuya.smart.sdk.api.ITuyaGroup;
import com.tuya.smart.sdk.bean.GroupDeviceBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 群组编辑
 * Created by chenshixin on 15/12/11.
 */
public class GroupDeviceListPresenter extends BasePresenter {
    public static final int WHAT_QUERY_DEVICES_FAILURE = 0x01;
    public static final int WHAT_QUERY_DEVICES_SUCCESS = 0x02;
    public static final int WHAT_ADD_GROUP_FAILURE = 0x03;
    public static final int WHAT_ADD_GROUP_SUCCESS = 0x04;
    public static final int WHAT_EDIT_DEVICE_FAILURE = 0x05;
    public static final int WHAT_EDIT_DEVICE_SUCCESS = 0x06;
    /**
     * 创建群组模式（groupId为空）
     */
    private static final int MODE_CREATE = 1;

    /**
     * 编辑群组模式 （productId为空）
     */
    private static final int MODE_EDIT = 2;
    private ITuyaGroup mITuyaGroup;

    /**
     * 当前模式
     */
    private int MODE = 0;

    private String mProductId;
    private String mDevId;

    private long mGroupId;

    private Activity mActivity;

    private IGroupDeviceListView mView;

    public GroupDeviceListPresenter(GroupEditDeviceActivity activity, IGroupDeviceListView view) {
        mActivity = activity;
        mView = view;
        initMode();
        getData();
    }

    private void initMode() {
        long groupId = mActivity.getIntent().getLongExtra(GroupEditDeviceActivity.EXTRA_GROUP_ID, -1);
        if (groupId != -1) {
            mGroupId = groupId;
            MODE = MODE_EDIT;
            mActivity.setTitle(R.string.group_title_select_device);
            mITuyaGroup = TuyaGroup.newGroupInstance(mGroupId);
            return;
        }
        mProductId = mActivity.getIntent().getStringExtra(GroupEditDeviceActivity.EXTRA_PRODUCT_ID);
        mDevId = mActivity.getIntent().getStringExtra(GroupEditDeviceActivity.EXTRA_DEV_ID);
        MODE = MODE_CREATE;
    }

    public void getData() {
        mView.loadStart();
        if (MODE == MODE_CREATE) {
            queryDevicesByProductId();
        } else {
            queryDevicesByGroupId();
        }
    }


    /**
     * 根据productId 查询当前账号下的所有设备
     */
    public void queryDevicesByProductId() {
        TuyaGroup.getGroupInstance().getGroupDevList(mProductId, new IGetDevsFromGroupByPidCallback() {
            @Override
            public void onSuccess(List<GroupDeviceBean> arrayList) {

                mHandler.sendMessage(MessageUtil.getResultMessage(WHAT_QUERY_DEVICES_SUCCESS, arrayList));
            }

            @Override
            public void onError(String errorCode, String errorMsg) {
                mHandler.sendMessage(MessageUtil.getCallFailMessage(WHAT_QUERY_DEVICES_FAILURE, errorCode, errorMsg));
            }
        });
    }

    public void queryDevicesByGroupId() {
        if (mITuyaGroup == null) return;
        mITuyaGroup.getGroupDevList(new IGetDevicesInGroupCallback() {
            @Override
            public void onSuccess(List<GroupDeviceBean> list) {
                mHandler.sendMessage(MessageUtil.getResultMessage(WHAT_QUERY_DEVICES_SUCCESS, list));
            }

            @Override
            public void onError(String errorCode, String errorMsg) {
                mHandler.sendMessage(MessageUtil.getCallFailMessage(WHAT_QUERY_DEVICES_FAILURE, errorCode, errorMsg));
            }
        });
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case WHAT_QUERY_DEVICES_FAILURE:
                ToastUtil.showToast(mActivity, ((Result) msg.obj).getError());
                break;
            case WHAT_QUERY_DEVICES_SUCCESS:
                ArrayList<GroupDeviceBean> devicesList = (ArrayList<GroupDeviceBean>) ((Result) msg.obj).getObj();
                for (GroupDeviceBean deviceBean : devicesList) {
                    if (deviceBean.getDeviceBean().getDevId().equals(mDevId)) {
                        deviceBean.setChecked(true);
                    }
                }
                updateDeviceList(devicesList);
                break;
            case WHAT_ADD_GROUP_FAILURE:
                ToastUtil.showToast(mActivity, ((Result) msg.obj).getError());
                break;
            case WHAT_ADD_GROUP_SUCCESS:
                ToastUtil.showToast(mActivity, mActivity.getString(R.string.success));
                DeviceEventSender.deviceListChanged();
                mView.finishActivity();
                break;
            case WHAT_EDIT_DEVICE_FAILURE:
                ToastUtil.showToast(mActivity, ((Result) msg.obj).getError());
                break;
            case WHAT_EDIT_DEVICE_SUCCESS:
                ToastUtil.showToast(mActivity, mActivity.getString(R.string.success));
                DeviceEventSender.deviceListChanged();
                mView.finishActivity();
                break;
        }
        return super.handleMessage(msg);
    }


    private void updateDeviceList(ArrayList<GroupDeviceBean> deviceList) {
        mView.updateDeviceList(deviceList);
        mView.loadFinish();
    }

    public void onDeviceClick(GroupDeviceBean groupDevice) {
        groupDevice.setChecked(!groupDevice.isChecked());
        mView.refreshList();
    }

    public boolean onMenuItemClick(int itemId) {
        if (itemId == R.id.action_done) {
            checkSelected();
        }
        return true;
    }

    private void checkSelected() {
        List<String> getSelectedDeviceIds = getSelectedDeviceIds();
        if (getSelectedDeviceIds == null || getSelectedDeviceIds.size() < 1) {
            ToastUtil.shortToast(mActivity, R.string.group_add_no_devices_selected);
        } else {
            saveResult(getSelectedDeviceIds);
        }
    }

    private void saveResult(List<String> getSelectedDeviceIds) {
        if (MODE == MODE_CREATE) {
            showInputNameDialog(getSelectedDeviceIds);
        } else if (MODE == MODE_EDIT) {
            editGroup(getSelectedDeviceIds);
        }
    }


    private void editGroup(List<String> getSelectedDeviceIds) {
        if (mITuyaGroup == null) return;
        mITuyaGroup.updateGroupRelations(getSelectedDeviceIds, new IControlCallback() {
            @Override
            public void onError(String errorCode, String errorMsg) {
                mHandler.sendMessage(MessageUtil.getCallFailMessage(WHAT_EDIT_DEVICE_FAILURE, errorCode, errorMsg));
            }

            @Override
            public void onSuccess() {
                mHandler.sendEmptyMessage(WHAT_EDIT_DEVICE_SUCCESS);
            }
        });
    }


    private void showInputNameDialog(final List<String> getSelectedDeviceIds) {
        String firstDeviceName = getFirstSelectedName();
        DialogUtil.simpleInputDialog(mActivity, mActivity.getString(R.string.group_rename_dialog_title), mActivity.getString(R.string.group_add_default_name, firstDeviceName), false, new DialogUtil.SimpleInputDialogInterface() {
            @Override
            public void onPositive(DialogInterface dialog, String inputText) {
                if (TextUtils.isEmpty(inputText)) {
                    ToastUtil.shortToast(mActivity, R.string.group_add_name_empty);
                    return;
                }
                addGroup(inputText, getSelectedDeviceIds);
            }

            @Override
            public void onNegative(DialogInterface dialog) {

            }
        });
    }

    private void addGroup(final String name, List<String> getSelectedDeviceIds) {
        TuyaGroup.getGroupInstance().createNewGroup(mProductId, name, getSelectedDeviceIds, new ICreateGroupCallback() {
            @Override
            public void onSuccess(long groupId) {
                mHandler.sendMessage(MessageUtil.getMessage(WHAT_ADD_GROUP_SUCCESS, groupId));
            }

            @Override
            public void onError(String errorCode, String errorMsg) {
                mHandler.sendMessage(MessageUtil.getCallFailMessage(WHAT_ADD_GROUP_FAILURE, errorCode, errorMsg));
            }
        });
    }

    private List<String> getSelectedDeviceIds() {
        List<String> selectedDevices = new ArrayList<>();
        List<GroupDeviceBean> deviceBeans = mView.getDeviceList();
        for (GroupDeviceBean deviceBean : deviceBeans) {
            if (deviceBean.isChecked()) {
                selectedDevices.add(deviceBean.getDeviceBean().getDevId());
            }
        }
        return selectedDevices;
    }

    private String getFirstSelectedName() {
        List<GroupDeviceBean> deviceBeans = mView.getDeviceList();
        for (GroupDeviceBean deviceBean : deviceBeans) {
            if (deviceBean.isChecked()) {
                return deviceBean.getDeviceBean().getName();
            }
        }
        return "";
    }


}
