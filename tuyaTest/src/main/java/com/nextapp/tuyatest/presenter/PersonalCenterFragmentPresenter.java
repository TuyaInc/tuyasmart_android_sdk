package com.nextapp.tuyatest.presenter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.nextapp.tuyatest.event.PersonalInfoEvent;
import com.nextapp.tuyatest.event.PersonalInfoEventModel;
import com.nextapp.tuyatest.model.IPersonalCenterModel;
import com.nextapp.tuyatest.model.PersonalCenterModel;
import com.nextapp.tuyatest.utils.ActivityUtils;
import com.nextapp.tuyatest.view.IPersonalCenterView;
import com.tuya.smart.android.base.TuyaSmartSdk;
import com.tuya.smart.android.mvp.presenter.BasePresenter;
import com.tuya.smart.android.user.TuyaSmartUserManager;
import com.tuya.smart.android.user.bean.User;


/**
 * Created by letian on 15/6/1.
 */
public class PersonalCenterFragmentPresenter extends BasePresenter implements PersonalInfoEvent {
    private static final String TAG = "PersonalCenterFragmentPresenter";
    private final Context mContext;
    private final IPersonalCenterView mView;
    private final IPersonalCenterModel mModel;


    public PersonalCenterFragmentPresenter(Context context, IPersonalCenterView view) {
        mContext = context;
        mView = view;
        mModel = new PersonalCenterModel(context, mHandler);
        initEventBus();
    }

    private void initEventBus() {
        TuyaSmartSdk.getEventBus().register(this);
    }

    public void setPersonalInfo() {
        mView.setNickName(mModel.getNickName());
        mView.setUserName(mModel.getUserName());
    }

    public void gotoPersonalInfoActivity() {
//        ActivityUtils.gotoActivity((Activity) mContext, PersonalInfoActivity.class, ActivityUtils.ANIMATE_FORWARD, false);
    }

    @Override
    public void onEvent(PersonalInfoEventModel event) {
        mView.setNickName(mModel.getNickName());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mModel.onDestroy();
        TuyaSmartSdk.getEventBus().unregister(this);
    }


}
