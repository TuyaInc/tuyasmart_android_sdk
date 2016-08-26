package com.nextapp.tuyatest.view;

/**
 * Created by letian on 16/1/8.
 */
public interface IECBindView {

    void showSuccessPage();

    void showFailurePage();

    void showConnectPage();

    void setConnectProgress(float progress, int animationDuration);

    void showNetWorkFailurePage();

    void showBindDeviceSuccessTip();

    void showDeviceFindTip(String gwId);

    void showConfigSuccessTip();

    void showBindDeviceSuccessFinalTip();

    void setAddDeviceName(String name);

    void showMainPage();
    void hideMainPage();
    void showSubPage();
    void hideSubPage();
}
