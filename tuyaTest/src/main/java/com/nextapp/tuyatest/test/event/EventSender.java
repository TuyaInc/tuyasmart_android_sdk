package com.nextapp.tuyatest.test.event;

import com.nextapp.tuyatest.event.DeviceListUpdateModel;
import com.nextapp.tuyatest.event.FriendEventModel;
import com.tuya.smart.android.base.event.BaseEventSender;
import com.nextapp.tuyatest.test.presenter.SendAndBackData;
import com.tuya.smart.android.user.bean.PersonBean;

import java.util.List;

/**
 * Created by letian on 16/7/12.
 */
public class EventSender extends BaseEventSender {
    public static void sendTestDpValue(List<SendAndBackData> sendAndBackDatas) {
        send(new DpSendDataModel(sendAndBackDatas));
    }

    /**
     * 用户好友编辑更新通知
     *
     * @param person
     * @param position
     * @param operation
     */

    public static void friendUpdate(PersonBean person, int position, int operation) {
        FriendEventModel friendEventModel = new FriendEventModel();
        friendEventModel.setOperation(operation);
        friendEventModel.setPerson(person);
        friendEventModel.setPosition(position);
        send(friendEventModel);
    }

    /**
     * 用户好友添加更新通知
     *
     * @param person
     * @param operation
     */
    public static void friendUpdate(PersonBean person, int operation) {
        friendUpdate(person, 0, operation);
    }

    /**
     * 主页设别列表更新（不携带新的设备列表信息，即需要重新拉取）
     */
    public static void updateDeviceList() {
        send(new DeviceListUpdateModel());
    }

}
