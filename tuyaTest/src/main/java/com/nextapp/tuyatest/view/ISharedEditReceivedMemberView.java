package com.nextapp.tuyatest.view;

import com.tuya.smart.android.device.bean.GwWrapperBean;

import java.util.List;

/**
 * Created by leaf on 15/12/21.
 * 共享
 */
public interface ISharedEditReceivedMemberView {
    void updateList(List<GwWrapperBean> list);
}
