package com.tuya.smart.android.demo.view;

import com.tuya.smart.android.demo.bean.DpLogBean;

/**
 * Created by letian on 16/8/28.
 */

public interface ICommonDeviceDebugView {
    void updateView(String dpStr);

    void logError(String error);

    void logSuccess(String dpStr);

    void logSuccess(DpLogBean logBean);

    void logError(DpLogBean logBean);

    void logDpReport(String dpStr);

    void deviceRemoved();

    void deviceOnlineStatusChanged(boolean online);

    void onNetworkStatusChanged(boolean status);

    void devInfoUpdate();

    void updateTitle(String titleName);
}
