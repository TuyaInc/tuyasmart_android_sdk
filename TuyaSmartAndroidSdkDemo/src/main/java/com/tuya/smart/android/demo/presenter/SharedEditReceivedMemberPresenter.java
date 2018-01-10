package com.tuya.smart.android.demo.presenter;

import android.app.Activity;
import android.os.Message;
import android.text.TextUtils;

import com.tuya.smart.android.demo.R;
import com.tuya.smart.android.demo.event.FriendEventModel;
import com.tuya.smart.android.demo.model.SharedModel;
import com.tuya.smart.android.demo.test.event.EventSender;
import com.tuya.smart.android.demo.utils.CommonUtil;
import com.tuya.smart.android.demo.utils.ProgressUtil;
import com.tuya.smart.android.demo.utils.ToastUtil;
import com.tuya.smart.android.demo.view.ISharedEditReceivedMemberView;
import com.tuya.smart.android.mvp.bean.Result;
import com.tuya.smart.android.mvp.presenter.BasePresenter;
import com.tuya.smart.android.user.bean.PersonBean;
import com.tuya.smart.sdk.TuyaUser;
import com.tuya.smart.sdk.bean.DeviceBean;

import java.util.List;

/**
 * Created by leaf on 15/12/21.
 * 收到的共享
 */
public class SharedEditReceivedMemberPresenter extends BasePresenter {
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
        updateList();
    }

    private void updateList() {
        List<DeviceBean> data = TuyaUser.getDeviceInstance().getDevList();
        if (data != null) {
            mView.updateList(data);
        } else {
            ToastUtil.showToast(mActivity, R.string.system_error);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
