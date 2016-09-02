package com.tuya.smart.android.demo.model;


import android.content.Context;
import android.os.Message;

import com.tuya.smart.android.common.utils.SafeHandler;
import com.tuya.smart.android.mvp.model.BaseModel;
import com.tuya.smart.android.user.bean.GroupReceivedMemberBean;
import com.tuya.smart.android.user.bean.PersonBean;
import com.tuya.smart.sdk.TuyaMember;
import com.tuya.smart.sdk.api.share.IAddMemberCallback;
import com.tuya.smart.sdk.api.share.IModifyMemberNameCallback;
import com.tuya.smart.sdk.api.share.IQueryMemberListCallback;
import com.tuya.smart.sdk.api.share.IQueryReceiveMemberListCallback;
import com.tuya.smart.sdk.api.share.IRemoveMemberCallback;
import com.tuya.smart.sdk.api.share.ITuyaMember;

import java.util.ArrayList;

/**
 * Created by leaf on 15/12/21.
 * 共享数据
 */
public class SharedModel extends BaseModel implements ISharedModel {
    public static final int WHAT_ERROR = -30;
    public static final int WHAT_ADD_SENT_SUCCESS = 0x030;
    public static final int WHAT_REMOVE_SENT_SUCCESS = 0x031;
    public static final int WHAT_GET_SENT_LIST_SUCCESS = 0x032;
    public static final int WHAT_GET_RECEIVED_LIST_SUCCESS = 0x033;
    public static final int WHAT_UPDATE_NICKNAME_SUCCESS = 0x034;

    private ITuyaMember mITuyaSmartMember;

    public SharedModel(Context ctx, SafeHandler handler) {
        super(ctx, handler);
        mITuyaSmartMember = new TuyaMember(ctx);
    }

    @Override
    public void addMember(String mobile, String name, String countryCode, String relation) {
        mITuyaSmartMember.addMember(countryCode, mobile, name, relation, new IAddMemberCallback() {
            @Override
            public void onSuccess(Integer shareId) {
                resultSuccess(WHAT_ADD_SENT_SUCCESS,shareId);
            }

            @Override
            public void onError(String errorCode, String errorMsg) {
                resultError(WHAT_ERROR, errorCode, errorMsg);
            }
        });
    }

    @Override
    public void removeMember(String id) {
        int intId = Integer.parseInt(id);
        mITuyaSmartMember.removeMember(intId, new IRemoveMemberCallback() {
            @Override
            public void onSuccess() {
                resultSuccess(WHAT_REMOVE_SENT_SUCCESS);
            }

            @Override
            public void onError(String errorCode, String errorMsg) {
                resultError(WHAT_ERROR, errorCode, errorMsg);
            }
        });
    }

    @Override
    public void getSentList() {
        mITuyaSmartMember.queryMemberList(new IQueryMemberListCallback() {
            @Override
            public void onSuccess(ArrayList<PersonBean> arrayList) {
                resultSuccess(WHAT_GET_SENT_LIST_SUCCESS, arrayList);
            }

            @Override
            public void onError(String errorCode, String errorMsg) {
                resultError(WHAT_ERROR, errorCode, errorMsg);
            }
        });
    }

    @Override
    public void getReceivedList() {
        mITuyaSmartMember.queryReceiveMemberList(new IQueryReceiveMemberListCallback() {
            @Override
            public void onError(String errorCode, String errorMsg) {
                resultError(WHAT_ERROR, errorCode, errorMsg);
            }

            @Override
            public void onSuccess(ArrayList<GroupReceivedMemberBean> arrayList) {
                resultSuccess(WHAT_GET_RECEIVED_LIST_SUCCESS, arrayList);
            }
        });
    }

    @Override
    public void updateMName(int id, String mname) {
        mITuyaSmartMember.modifyReceiveMemberName(id, mname, new IModifyMemberNameCallback() {
            @Override
            public void onSuccess() {
                resultSuccess(WHAT_UPDATE_NICKNAME_SUCCESS);
            }

            @Override
            public void onError(String errorCode, String errorMsg) {
                resultError(WHAT_ERROR, errorCode, errorMsg);
            }
        });
    }


    @Override
    public void onDestroy() {
        mITuyaSmartMember.onDestroy();
    }

    protected void resultSuccess(int what) {
        if (mHandler != null) {
            Message message = mHandler.obtainMessage(what);
            mHandler.sendMessage(message);
        }
    }
}
