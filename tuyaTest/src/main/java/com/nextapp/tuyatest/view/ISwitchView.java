package com.nextapp.tuyatest.view;

/**
 * Created by letian on 16/7/21.
 */
public interface ISwitchView {

    void showOpenView();

    void showCloseView();

    void showErrorTip();

    void showRemoveTip();

    void changeNetworkErrorTip(boolean status);

    void statusChangedTip(boolean status);

    void devInfoUpdateView();
}
