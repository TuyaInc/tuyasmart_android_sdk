package com.nextapp.tuyatest.test.presenter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.nextapp.tuyatest.R;
import com.nextapp.tuyatest.test.activity.DpTestSetUpActivity;
import com.nextapp.tuyatest.test.bean.DpTestSetUpBean;
import com.nextapp.tuyatest.test.view.IDpTestSetUpView;
import com.nextapp.tuyatest.utils.ToastUtil;
import com.tuya.smart.android.device.utils.PreferencesUtil;
import com.tuya.smart.android.mvp.presenter.BasePresenter;

/**
 * Created by letian on 16/8/6.
 */
public class DpTestSetUpPresenter extends BasePresenter {

    private final Context mContext;
    private final IDpTestSetUpView mView;
    private final DpTestSetUpBean mInitData;

    public DpTestSetUpPresenter(Context context, IDpTestSetUpView view) {
        mContext = context;
        mView = view;
        mInitData = getData();
    }


    public void saveSetUp() {
        DpTestSetUpBean setData = mView.getSetData();
        if (setData == null) {
            ToastUtil.showToast(mContext, R.string.format_error);
            return;
        }
        String data = JSONObject.toJSONString(setData);
        if (!data.equals(JSONObject.toJSONString(mInitData))) {
            PreferencesUtil.set(DpTestSetUpActivity.DP_TEST_SETUP_VALUE, data);
        }
        ((Activity) mContext).finish();
    }

    private DpTestSetUpBean getData() {
        DpTestSetUpBean bean;
        String jsonStr = PreferencesUtil.getString(DpTestSetUpActivity.DP_TEST_SETUP_VALUE);
        if (TextUtils.isEmpty(jsonStr)) {
            bean = new DpTestSetUpBean();
        } else {
            bean = JSONObject.parseObject(jsonStr, DpTestSetUpBean.class);
        }
        return bean;
    }

    public DpTestSetUpBean getTestSetUpData() {
        return mInitData;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
