package com.tuya.smart.android.demo.view;


import com.tuya.smart.sdk.bean.GroupDeviceBean;

import java.util.List;

/**
 * 群组设备管理界面接口
 * Created by chenshixin on 15/12/11.
 */
public interface IGroupDeviceListView {

    /**
     * 开始加载数据（显示加载refresh的图标）
     */
    void loadStart();

    /**
     * 加载结束（隐藏加载refresh的图标）
     */
    void loadFinish();

    /**
     * 更新群组设备列表
     */
    void updateDeviceList(List<GroupDeviceBean> deviceBeanList);

    /**
     * 刷新数据
     */
    void refreshList();

    /**
     * 获取当前显示的数据
     */
    List<GroupDeviceBean> getDeviceList();


    void finishActivity();
}
