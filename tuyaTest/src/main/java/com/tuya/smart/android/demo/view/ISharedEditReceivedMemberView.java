package com.tuya.smart.android.demo.view;

import com.tuya.smart.sdk.bean.DeviceBean;

import java.util.List;

/**
 * Created by leaf on 15/12/21.
 * 共享
 */
public interface ISharedEditReceivedMemberView {
    void updateList(List<DeviceBean> list);
}
