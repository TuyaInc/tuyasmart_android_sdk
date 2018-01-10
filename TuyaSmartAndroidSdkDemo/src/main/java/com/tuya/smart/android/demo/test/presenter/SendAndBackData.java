package com.tuya.smart.android.demo.test.presenter;

import java.util.HashMap;

/**
 * Created by letian on 16/7/12.
 */
public class SendAndBackData {
    private HashMap<String, Object> sendValue;
    private HashMap<String, Object> backValue;

    public HashMap<String, Object> getSendValue() {
        return sendValue;
    }

    public void setSendValue(HashMap<String, Object> sendValue) {
        this.sendValue = sendValue;
    }

    public HashMap<String, Object> getBackValue() {
        return backValue;
    }

    public void setBackValue(HashMap<String, Object> backValue) {
        this.backValue = backValue;
    }
}
