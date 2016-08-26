package com.tuya.smart.android.demo.test.event;

import com.tuya.smart.android.demo.test.presenter.SendAndBackData;

import java.util.List;

/**
 * Created by letian on 16/7/12.
 */
public class DpSendDataModel {
    private final List<SendAndBackData> mData;

    public DpSendDataModel(List<SendAndBackData> datas) {
        mData = datas;
    }

    public List<SendAndBackData> getData() {
        return mData;
    }
}
