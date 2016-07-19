package com.nextapp.tuyatest.test.model;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.tuya.smart.android.common.utils.SafeHandler;
import com.nextapp.tuyatest.test.presenter.SendAndBackData;
import com.tuya.smart.android.device.bean.SchemaBean;
import com.tuya.smart.android.device.enums.ModeEnum;
import com.tuya.smart.android.mvp.model.BaseModel;
import com.tuya.smart.sdk.TuyaUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by letian on 16/7/11.
 */
public class DeviceTestModel extends BaseModel implements IDeviceTestModel {
    public DeviceTestModel(Context ctx, SafeHandler handler) {
        super(ctx, handler);
    }


    @Override
    public List<SendAndBackData> getSendAndBackDpData() {
        List<SendAndBackData> sendAndBackDatas = new ArrayList<>();

//        sendAndBackDatas.add(addDpTest("1", true));
//        sendAndBackDatas.add(addDpTest("1", false));
        HashMap<String, Object> sendValue = new HashMap<>();
        HashMap<String, Object> backValue = new HashMap<>();
        sendValue.put("1", true);
        backValue.put("1", true);
        backValue.put("2", 0);
        sendAndBackDatas.add(addDpTest(sendValue, backValue));
//        sendAndBackDatas.add(addDpTest("2", 24));
//        sendAndBackDatas.add(addDpTest("2", 25));
//        sendAndBackDatas.add(addDpTest("2", 26));
//        sendAndBackDatas.add(addDpTest("2", 27));
//        sendAndBackDatas.add(addDpTest("9", true));
//        sendAndBackDatas.add(addDpTest("9", false));
        return sendAndBackDatas;
    }

    @Override
    public JSONObject getDpValueWithOutROMode(String devId, String value) {
        JSONObject jsonObject = JSONObject.parseObject(value);
        ArrayList<String> list = new ArrayList<>();
        for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
            String dpId = entry.getKey();
            Map<String, SchemaBean> schema = TuyaUser.getDeviceInstance().getSchema(devId);
            if (schema != null) {
                SchemaBean schemaBean = schema.get(dpId);
                if (schemaBean != null && TextUtils.equals(schemaBean.getMode(), ModeEnum.RO.getType())) {
                    list.add(dpId);
                }
            }
        }
        for (String dpId : list) {
            jsonObject.remove(dpId);
        }
        return jsonObject;
    }

    @Override
    public boolean checkValue(HashMap<String, Object> sendData, String returnValue) {
        String returnStr = JSONObject.parseObject(returnValue).toJSONString();
        String sendStr = JSONObject.parseObject(JSONObject.toJSONString(sendData)).toJSONString();
        return TextUtils.equals(returnStr, sendStr);
    }

    private SendAndBackData addDpTest(HashMap<String, Object> sendDpData, HashMap<String, Object> backDpData) {
        SendAndBackData sendAndBackData = new SendAndBackData();
        sendAndBackData.setSendValue(sendDpData);
        sendAndBackData.setBackValue(backDpData);
        return sendAndBackData;
    }

    public SendAndBackData addDpTest(String dpId, Object dpValue) {
        SendAndBackData sendAndBackData = new SendAndBackData();
        HashMap<String, Object> sendDpData = new HashMap<>();
        sendDpData.put(dpId, dpValue);
        sendAndBackData.setSendValue(sendDpData);
        return sendAndBackData;
    }

    public SendAndBackData addDpTest(String dpId, Object dpValue, String reDpId, Object reDpValue) {
        SendAndBackData sendAndBackData = new SendAndBackData();
        HashMap<String, Object> sendDpData = new HashMap<>();
        sendDpData.put(dpId, dpValue);
        sendAndBackData.setSendValue(sendDpData);
        HashMap<String, Object> backDpData = new HashMap<>();
        backDpData.put(reDpId, reDpValue);
        sendAndBackData.setBackValue(backDpData);
        return sendAndBackData;
    }

    @Override
    public void onDestroy() {

    }

}
