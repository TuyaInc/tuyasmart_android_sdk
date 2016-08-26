package com.nextapp.tuyatest.presenter;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;

import com.alibaba.fastjson.JSONObject;
import com.nextapp.tuyatest.R;
import com.nextapp.tuyatest.activity.SharedEditReceivedMemberActivity;
import com.nextapp.tuyatest.event.FriendEventModel;
import com.nextapp.tuyatest.model.SharedModel;
import com.nextapp.tuyatest.test.event.EventSender;
import com.nextapp.tuyatest.test.utils.DialogUtil;
import com.nextapp.tuyatest.utils.ActivityUtils;
import com.nextapp.tuyatest.utils.ProgressUtil;
import com.nextapp.tuyatest.utils.ToastUtil;
import com.nextapp.tuyatest.view.FriendUpdateEvent;
import com.nextapp.tuyatest.view.ISharedReceivedView;
import com.tuya.smart.sdk.TuyaSdk;
import com.tuya.smart.android.mvp.bean.Result;
import com.tuya.smart.android.mvp.presenter.BasePresenter;
import com.tuya.smart.android.user.bean.GroupReceivedMemberBean;
import com.tuya.smart.android.user.bean.PersonBean;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by leaf on 15/12/21.
 * 收到的共享
 */
public class SharedReceivedPresenter extends BasePresenter implements FriendUpdateEvent {
    private static final String TAG = "SharedReceivedPresenter";

    private Activity mActivity;
    protected ISharedReceivedView mView;
    protected SharedModel mModel;

    public SharedReceivedPresenter(Activity activity, ISharedReceivedView view) {
        mActivity = activity;
        mView = view;
        mModel = new SharedModel(activity, mHandler);
        TuyaSdk.getEventBus().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mModel.onDestroy();
        TuyaSdk.getEventBus().unregister(this);
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case SharedModel.WHAT_REMOVE_SENT_SUCCESS:
                list();
                EventSender.updateDeviceList();
                break;
            case SharedModel.WHAT_GET_RECEIVED_LIST_SUCCESS:
                mView.finishLoad();
                mView.updateList((ArrayList<GroupReceivedMemberBean>) ((Result) msg.obj).getObj());
                break;
            case SharedModel.WHAT_ERROR:
                mView.finishLoad();
                mView.reloadBaseView();
                ToastUtil.showToast(mActivity, ((Result) msg.obj).error);
        }

        return super.handleMessage(msg);
    }

    public void list() {
        mModel.getReceivedList();
    }

    public void remove(String id) {
        mModel.removeMember(id);
    }

    public void gotoSharedEditReceivedMemberActivity(ArrayList<HashMap<String, String>> devices, PersonBean personBean, int position) {
        ArrayList<String> deviceList = new ArrayList<>();
        if (null != devices)
            for (HashMap<String, String> device : devices) {
                deviceList.add(device.get(device.containsKey(GroupReceivedMemberBean.TYPE_GW) ?
                        GroupReceivedMemberBean.TYPE_GW : GroupReceivedMemberBean.TYPE_GROUP));
            }

        Intent intent = new Intent(mActivity, SharedEditReceivedMemberActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(SharedEditReceivedMemberActivity.EXTRA_PERSON, JSONObject.toJSONString(personBean));
        bundle.putStringArrayList(SharedEditReceivedMemberActivity.EXTRA_DEVICE_LIST, deviceList);
        bundle.putInt(SharedEditReceivedMemberActivity.EXTRA_POSITION, position);
        intent.putExtras(bundle);
        ActivityUtils.startActivity(mActivity, intent, ActivityUtils.ANIMATE_FORWARD, false);
    }

    public void onFriendLongClick(final PersonBean person) {
        DialogUtil.listSelectDialog(mActivity, person.getMobile(), new String[]{mActivity.getString(R.string.operation_delete)}, new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int p, long id) {
                        DialogUtil.simpleConfirmDialog(mActivity, mActivity.getString(R.string.ty_simple_confirm_title), mActivity.getString(R.string.ty_delete_share_pop_android, person.getMobile()), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                if (which == DialogInterface.BUTTON_POSITIVE) {
                                    ProgressUtil.showLoading(mActivity, R.string.loading);
                                    remove(String.valueOf(person.getId()));

                                }
                            }
                        });
                    }
                }
        );
    }

    @Override
    public void onEvent(FriendEventModel event) {
        switch (event.getOperation()) {
            case FriendEventModel.OP_EDIT_THIRD:
                mView.updateList(event.getPerson(), event.getPosition());
                break;
            default:
                break;
        }
    }
}
