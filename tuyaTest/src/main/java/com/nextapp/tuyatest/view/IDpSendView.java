package com.nextapp.tuyatest.view;

import java.util.Set;

/**
 * Created by letian on 16/8/4.
 */
public interface IDpSendView {
    void showBooleanView(Boolean value);

    void showStringView(String value);

    void showRawView(String value);

    void showValueView(int value);

    void showEnumView(String value, Set<String> range);

    void showMessage(String msg);

    String getStrValue();

    String getEnumValue();

    String getValue();

    String getRAWValue();

    void showFormatErrorTip();
}
