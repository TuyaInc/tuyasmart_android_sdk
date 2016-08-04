package com.nextapp.tuyatest;

import android.app.Activity;
import android.os.Bundle;

import com.tuya.smart.sdk.TuyaGroup;
import com.tuya.smart.sdk.api.ICreateGroupCallback;
import com.tuya.smart.sdk.api.IGetDevsFromGroupByPidCallback;
import com.tuya.smart.sdk.bean.GroupDeviceBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by letian on 16/7/29.
 */
public class GroupActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    public void getGroupList() {
        TuyaGroup.getGroupInstance().getGroupDevList("productId", new IGetDevsFromGroupByPidCallback() {
            @Override
            public void onSuccess(List<GroupDeviceBean> bizResult) {

            }

            @Override
            public void onError(String errorCode, String errorMsg) {

            }
        });
    }

    public void createGroup() {
        List<String> devIds = new ArrayList<>();
        TuyaGroup.getGroupInstance().createNewGroup("productId", "name", devIds, new ICreateGroupCallback() {
            @Override
            public void onSuccess(long groupId) {

            }

            @Override
            public void onError(String errorCode, String errorMsg) {

            }
        });
    }

    public void publishDps() {

    }
}
