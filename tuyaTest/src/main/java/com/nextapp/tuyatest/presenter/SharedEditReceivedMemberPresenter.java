package com.nextapp.tuyatest.presenter;

import android.app.Activity;
import android.os.Message;
import android.text.TextUtils;

import com.nextapp.tuyatest.R;
import com.nextapp.tuyatest.event.FriendEventModel;
import com.nextapp.tuyatest.model.SharedModel;
import com.nextapp.tuyatest.test.event.EventSender;
import com.nextapp.tuyatest.utils.CommonUtil;
import com.nextapp.tuyatest.utils.ProgressUtil;
import com.nextapp.tuyatest.utils.ToastUtil;
import com.nextapp.tuyatest.view.ISharedEditReceivedMemberView;
import com.tuya.smart.android.base.TuyaSmartSdk;
import com.tuya.smart.android.device.TuyaSmartDevice;
import com.tuya.smart.android.device.bean.GwWrapperBean;
import com.tuya.smart.android.device.event.GwRelationEvent;
import com.tuya.smart.android.device.event.GwRelationUpdateEventModel;
import com.tuya.smart.android.device.event.GwUpdateEvent;
import com.tuya.smart.android.device.event.GwUpdateEventModel;
import com.tuya.smart.android.mvp.bean.Result;
import com.tuya.smart.android.mvp.presenter.BasePresenter;
import com.tuya.smart.android.user.bean.PersonBean;

import java.util.ArrayList;

/**
 * Created by leaf on 15/12/21.
 * 收到的共享
 */
public class SharedEditReceivedMemberPresenter extends BasePresenter implements GwUpdateEvent,GwRelationEvent {
    private static final String TAG = "SharedEditReceivedMemberPresenter";

    private Activity mActivity;
    protected ISharedEditReceivedMemberView mView;
    private SharedModel mModel;
    private PersonBean mPerson;
    private int mPosition;

    public SharedEditReceivedMemberPresenter(Activity activity, ISharedEditReceivedMemberView view) {
        this.mActivity = activity;
        this.mView = view;
        this.mModel = new SharedModel(activity, mHandler);
        TuyaSmartSdk.getEventBus().register(this);
    }

    @Override
    public void onEventMainThread(GwUpdateEventModel gwUpdateEventModel) {
        updateList();
    }

    @Override
    public void onEventMainThread(GwRelationUpdateEventModel gwRelationUpdateEventModel) {
        updateList();
    }

    private void updateList(){
        ArrayList<GwWrapperBean> data = TuyaSmartDevice.getInstance().getGws();
        if (data != null) {
            mView.updateList(data);
            ProgressUtil.hideLoading();
        } else {
            ToastUtil.showToast(mActivity, R.string.system_error);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        TuyaSmartSdk.getEventBus().unregister(this);
    }

    public void list() {
        TuyaSmartDevice.getInstance().queryGwList();
    }

    public void updateNickname(PersonBean person, String nickname, int position) {
        if (null == person) {
            ToastUtil.showToast(mActivity, R.string.system_error);
            return;
        }
        if (TextUtils.isEmpty(nickname)) {
            ToastUtil.showToast(mActivity, R.string.cannot_input_empty_string);
            return;
        }
        person.setMname(nickname);
        mPerson = person;
        mPosition = position;
        ProgressUtil.showLoading(mActivity, R.string.loading);
        CommonUtil.hideIMM(mActivity);
        mModel.updateMName(person.getId(), nickname);
    }

    @Override
    public boolean handleMessage(Message msg) {
        ProgressUtil.hideLoading();
        switch (msg.what) {
            case SharedModel.WHAT_UPDATE_NICKNAME_SUCCESS:
                EventSender.friendUpdate(mPerson, mPosition, FriendEventModel.OP_EDIT_THIRD);
                mActivity.finish();
                break;
            case SharedModel.WHAT_ERROR:
                ToastUtil.showToast(mActivity, ((Result) msg.obj).error);
                break;
        }

        return super.handleMessage(msg);
    }
}
