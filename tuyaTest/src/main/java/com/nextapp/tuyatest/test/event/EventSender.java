package com.nextapp.tuyatest.test.event;

import com.nextapp.tuyatest.test.presenter.SendAndBackData;
import com.tuya.smart.android.base.event.BaseEventSender;

import java.util.List;

/**
 * Created by letian on 16/7/12.
 */
public class EventSender extends BaseEventSender {
    public static void sendTestDpValue(List<SendAndBackData> sendAndBackDatas) {
        send(new DpSendDataModel(sendAndBackDatas));
    }
}
