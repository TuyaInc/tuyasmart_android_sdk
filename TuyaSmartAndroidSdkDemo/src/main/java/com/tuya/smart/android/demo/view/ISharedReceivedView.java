package com.tuya.smart.android.demo.view;

import com.tuya.smart.android.user.bean.GroupReceivedMemberBean;
import com.tuya.smart.android.user.bean.PersonBean;

import java.util.ArrayList;

/**
 * Created by leaf on 15/12/21.
 * 共享
 */
public interface ISharedReceivedView {
    void finishLoad();

    void updateList(ArrayList<GroupReceivedMemberBean> members);

    void updateList(PersonBean person, int position);

    void reloadBaseView();
}
