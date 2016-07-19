package com.nextapp.tuyatest.presenter;

import android.content.Context;

import com.nextapp.tuyatest.fragment.DeviceListFragment;
import com.nextapp.tuyatest.view.IDeviceListFragmentView;
import com.tuya.smart.android.device.TuyaSmartDevice;
import com.tuya.smart.android.mvp.presenter.BasePresenter;
import com.tuya.smart.sdk.TuyaUser;

/**
 * Created by letian on 16/7/18.
 */
public class DeviceListFragmentPresenter extends BasePresenter {
    public DeviceListFragmentPresenter(DeviceListFragment fragment, IDeviceListFragmentView view) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void queryDevListFromServer() {
        TuyaUser.getDeviceInstance().queryDevList();
    }

    public boolean onDeviceLongClick(Object item) {
        return false;
    }

    public void addDevice() {

    }

    public void onDeviceClick(Object item) {

    }
}
