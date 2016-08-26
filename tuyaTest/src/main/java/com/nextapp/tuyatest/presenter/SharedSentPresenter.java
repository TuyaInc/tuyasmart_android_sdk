package com.nextapp.tuyatest.presenter;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;

import com.nextapp.tuyatest.R;
import com.nextapp.tuyatest.activity.EditFriendActivity;
import com.nextapp.tuyatest.activity.SharedMemberAddActivity;
import com.nextapp.tuyatest.event.FriendEventModel;
import com.nextapp.tuyatest.model.SharedModel;
import com.nextapp.tuyatest.test.utils.DialogUtil;
import com.nextapp.tuyatest.utils.ActivityUtils;
import com.nextapp.tuyatest.utils.ProgressUtil;
import com.nextapp.tuyatest.utils.ToastUtil;
import com.nextapp.tuyatest.view.FriendUpdateEvent;
import com.nextapp.tuyatest.view.ISharedSentView;
import com.tuya.smart.sdk.TuyaSdk;
import com.tuya.smart.android.mvp.bean.Result;
import com.tuya.smart.android.mvp.presenter.BasePresenter;
import com.tuya.smart.android.user.bean.PersonBean;

import java.util.ArrayList;

/**
 * Created by leaf on 15/12/21.
 * 收到的共享
 */
public class SharedSentPresenter extends BasePresenter implements FriendUpdateEvent {
    private static final String TAG = "SharedSentPresenter";

    private Activity mActivity;
    protected ISharedSentView mView;
    protected SharedModel mModel;

    public SharedSentPresenter(Activity activity, ISharedSentView view) {
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
                break;
            case SharedModel.WHAT_GET_SENT_LIST_SUCCESS:
                ProgressUtil.hideLoading();
                mView.finishLoad();
                mView.updateList((ArrayList<PersonBean>) ((Result) msg.obj).getObj());
                break;
            case SharedModel.WHAT_ERROR:
                mView.finishLoad();
                mView.reloadBaseView();
                ToastUtil.showToast(mActivity, ((Result) msg.obj).error);
                break;
        }

        return super.handleMessage(msg);
    }

    public void remove(String id) {
        mModel.removeMember(id);
    }

    public void list() {
        mModel.getSentList();
    }

    public void gotoAddNewPersonActivity() {
        ActivityUtils.gotoActivity(mActivity, SharedMemberAddActivity.class, ActivityUtils.ANIMATE_SLIDE_TOP_FROM_BOTTOM, false);
    }

    public void gotoEditFriendActivity(PersonBean person, int position) {
        //MX3取getParcelable出现空指针 情况不明。
        if (person == null) return;
        Bundle bundle = new Bundle();
        bundle.putParcelable(EditFriendActivity.DATA_PERSON, person);
        Intent intent = new Intent(mActivity, EditFriendActivity.class);
        intent.putExtras(bundle);
        intent.putExtra(EditFriendActivity.DATA_POSITION, position);
        ActivityUtils.startActivity(mActivity, intent, ActivityUtils.ANIMATE_FORWARD, false);
    }

    public void onFriendLongClick(final PersonBean person) {
        DialogUtil.listSelectDialog(mActivity, person.getName(), new String[]{mActivity.getString(R.string.operation_delete)}, new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int p, long id) {
                        DialogUtil.simpleConfirmDialog(mActivity, mActivity.getString(R.string.ty_simple_confirm_title), mActivity.getString(R.string.delete_member_tips, person.getName()), new DialogInterface.OnClickListener() {
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
            case FriendEventModel.OP_ADD:
                mView.updateList(event.getPerson());
                break;
            case FriendEventModel.OP_EDIT:
                mView.updateList(event.getPerson(), event.getPosition());
                break;
            case FriendEventModel.OP_QUERY:
                list();
                break;
            default:
                break;
        }
    }
}
