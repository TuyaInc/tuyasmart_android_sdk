package com.tuya.smart.android.demo.event;

/**
 * Created by letian on 16/7/20.
 */
public interface DeviceUpdateEvent {
    void onEventMainThread(DeviceListUpdateModel model);
}
