package com.nextapp.tuyatest.event;

/**
 * Created by letian on 16/7/20.
 */
public interface DeviceUpdateEvent {
    void onEventMainThread(DeviceListUpdateModel model);
}
