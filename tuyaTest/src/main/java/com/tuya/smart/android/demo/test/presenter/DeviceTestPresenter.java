package com.tuya.smart.android.demo.test.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.tuya.smart.android.common.utils.L;
import com.tuya.smart.android.demo.R;
import com.tuya.smart.android.demo.test.activity.DpTestSetUpActivity;
import com.tuya.smart.android.demo.test.activity.EditDpTestActivity;
import com.tuya.smart.android.demo.test.event.DpSendDataEvent;
import com.tuya.smart.android.demo.test.event.DpSendDataModel;
import com.tuya.smart.android.demo.test.model.DeviceTestModel;
import com.tuya.smart.android.demo.test.model.IDeviceTestModel;
import com.tuya.smart.android.demo.test.view.IDeviceTestView;
import com.tuya.smart.android.demo.utils.ActivityUtils;
import com.tuya.smart.android.device.utils.PreferencesUtil;
import com.tuya.smart.android.hardware.model.IControlCallback;
import com.tuya.smart.android.mvp.presenter.BasePresenter;
import com.tuya.smart.sdk.TuyaDevice;
import com.tuya.smart.sdk.TuyaSdk;
import com.tuya.smart.sdk.api.IDevListener;
import com.tuya.smart.sdk.api.ITuyaDevice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by letian on 16/7/11.
 */
public class DeviceTestPresenter extends BasePresenter implements DpSendDataEvent {

    public static final String INTENT_DEVICE_ID = "intent_device_id";
    private static final String TAG = "DeviceTestPresenter ggg";
    public static final String TIME_WAIT = "TIME_WAIT";
    private final Context mContext;
    private final IDeviceTestView mView;
    private final ITuyaDevice mDevice;

    private IDeviceTestModel mModel;
    private List<SendAndBackData> mSendAndBackDataList;
    private String mDevId;
    private Thread mThread;
    private DpCountDownLatch mLatch;
    private boolean mStart;
    private volatile boolean mStop;
    private long mTimeWait = 0;

    public DeviceTestPresenter(Context context, IDeviceTestView view) {
        mContext = context;
        mModel = new DeviceTestModel(context, mHandler);
        mSendAndBackDataList = new ArrayList<>();
        mView = view;
        initIntentData();
//        initTestData();
        initEventBus();
        mDevice = new TuyaDevice(mDevId);
        initDevicePanel();

    }

    private void initEventBus() {
        TuyaSdk.getEventBus().register(this);
    }

    private void initDevicePanel() {
        mDevice.registerDevListener(new IDevListener() {
            @Override
            public void onDpUpdate(String devId, String dpStr) {
                mView.log("onDpUpdate: " + dpStr);
                JSONObject jsonObject = mModel.getDpValueWithOutROMode(mDevId, dpStr);
                if (mLatch != null && mLatch.getCount() > 0 && !jsonObject.isEmpty()) {
                    mLatch.setReturnValue(jsonObject.toJSONString());
                    mLatch.setStatus(DpCountDownLatch.STATUS_SUCCESS);
                    mLatch.countDown();
                }
            }

            @Override
            public void onRemoved(String devId) {

            }

            @Override
            public void onStatusChanged(String devId, boolean online) {
                L.d(TAG, "devId: " + online);

            }

            @Override
            public void onNetworkStatusChanged(String devId, boolean status) {

            }

            @Override
            public void onDevInfoUpdate(String devId) {

            }
        });
    }

    private void initIntentData() {
        mDevId = ((Activity) mContext).getIntent().getStringExtra(INTENT_DEVICE_ID);
    }

    public void initTestData() {
        mSendAndBackDataList.addAll(mModel.getSendAndBackDpData());
    }

    public void startTest() {
        mTimeWait = PreferencesUtil.getInt(TIME_WAIT);
        String string = PreferencesUtil.getString(mDevId);
        if (TextUtils.isEmpty(string)) {
            Toast.makeText(mContext, mContext.getString(R.string.please_input_dp_value), Toast.LENGTH_SHORT).show();
            return;
        }
        mSendAndBackDataList.clear();
        mSendAndBackDataList.addAll(JSONObject.parseArray(string, SendAndBackData.class));
        if (mSendAndBackDataList.size() == 0) {
            Toast.makeText(mContext, mContext.getString(R.string.please_input_dp_value), Toast.LENGTH_SHORT).show();
            return;
        }
        if (mStart) return;

        mStop = false;
        mStart = true;
        mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    doQueue();
                }
            }
        });
        mThread.start();
    }


    private void doQueue() {
        for (SendAndBackData sendAndBackData : mSendAndBackDataList) {
            if (mStop) return;
            mView.log("\n");
            mView.log("send value: " + JSONObject.toJSONString(sendAndBackData.getSendValue()));
            mLatch = new DpCountDownLatch(1);
            sendCommand(sendAndBackData.getSendValue());
            try {
                mLatch.await(2, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (mStop) return;
            if (mLatch.getStatus() == DpCountDownLatch.STATUS_SUCCESS) {
                HashMap<String, Object> backValue = sendAndBackData.getBackValue();
                if (backValue == null) {
                    backValue = sendAndBackData.getSendValue();
                }
                if (mModel.checkValue(backValue, mLatch.getReturnValue())) {
                    mView.log("return value is right");
                } else {
                    mView.log("return error value: " + mLatch.getReturnValue());
                }
            } else if (mLatch.getStatus() == DpCountDownLatch.STATUS_ERROR) {
                mView.log("send Time out !!");
            } else {
                mView.log("send Failure !!");
            }

            try {
                Thread.sleep(mTimeWait);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (mStop) return;
        }
        mStart = false;
    }


    public void stopTest() {
        if (mStop) return;
        mView.log("Stop Test!!");
        mStop = true;
        mStart = false;
        while (mLatch != null && mLatch.getCount() > 0) {
            mLatch.countDown();
        }
        if (mThread != null) {
            mThread.interrupt();
        }
    }

    private void sendCommand(HashMap<String, Object> command) {
        String commandStr = JSONObject.toJSONString(command);
        mDevice.publishDps(commandStr, new IControlCallback() {
            @Override
            public void onError(String code, String error) {
                mLatch.setStatus(DpCountDownLatch.STATUS_ERROR);
                mLatch.countDown();
            }

            @Override
            public void onSuccess() {

            }
        });
    }

    public void editTestValue() {
        Intent intent = new Intent(mContext, EditDpTestActivity.class);
        intent.putExtra(DeviceTestPresenter.INTENT_DEVICE_ID, mDevId);
        mContext.startActivity(intent);
    }

    @Override
    public void onEvent(DpSendDataModel model) {
        PreferencesUtil.set(mDevId, JSONObject.toJSONString(model.getData()));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        TuyaSdk.getEventBus().unregister(this);
    }

    public void gotoDeviceTestSetUpActivity() {
        ActivityUtils.gotoActivity((Activity) mContext, DpTestSetUpActivity.class, ActivityUtils.ANIMATE_FORWARD, false);
    }
}
