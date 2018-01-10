package com.tuya.smart.android.demo.test.model;

import com.alibaba.fastjson.JSONObject;
import com.tuya.smart.android.demo.test.presenter.SendAndBackData;

import java.util.HashMap;
import java.util.List;

/**
 * Created by letian on 16/7/11.
 */
public interface IDeviceTestModel {
    List<SendAndBackData> getSendAndBackDpData();

    JSONObject getDpValueWithOutROMode(String devId, String value);

    boolean checkValue(HashMap<String, Object> backValue, String returnValue);
}
