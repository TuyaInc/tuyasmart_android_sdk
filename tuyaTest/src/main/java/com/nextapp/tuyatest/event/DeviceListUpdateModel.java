package com.nextapp.tuyatest.event;

import java.util.List;

/**
 * 主页设备列表更新
 * Created by chenshixin on 15/12/11.
 */
public class DeviceListUpdateModel {

    public List<Object> data;

    /**
     * 无数据，需要重新拉取
     */
    public DeviceListUpdateModel() {
    }

    /**
     * 携带数据，直接更新
     * @param data 新的设备列表信息
     */
    public DeviceListUpdateModel(List<Object> data) {
        this.data = data;
    }

    public List<Object> getData() {
        return data;
    }

    public void setData(List<Object> data) {
        this.data = data;
    }
}
