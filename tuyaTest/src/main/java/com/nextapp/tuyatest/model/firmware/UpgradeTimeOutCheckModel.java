package com.nextapp.tuyatest.model.firmware;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.tuya.smart.android.common.utils.L;
import com.tuya.smart.android.common.utils.SafeHandler;
import com.tuya.smart.android.mvp.model.BaseModel;

/**
 * Created by letian on 16/4/20.
 */
public class UpgradeTimeOutCheckModel extends BaseModel implements IUpgradeTimeOutModel {
    private static final long UPGRADE_TIME_OUT_VALUE = 60 * 1000; //60s
    public static final int WHAT_UPGRADE_OUT_OF_TIME = 0x10;
    private static final int MESSAGE_CHECK = 0x11;
    private static final long CHECK_TIME = 5000; //5s
    private boolean mStart;
    private long mProgressTime;
    public static final String TAG = "UpgradeTimeOutCheckModel ggg";


    public UpgradeTimeOutCheckModel(Context ctx, SafeHandler handler) {
        super(ctx, handler);
    }

    @Override
    public void onDestroy() {
        cancel();
    }

    public Handler mHandler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            if (!mStart) return false;
            switch (msg.what) {
                case MESSAGE_CHECK:
                    if (System.currentTimeMillis() - mProgressTime > UPGRADE_TIME_OUT_VALUE) {
                        L.d(TAG, "upgrade time out");
                        cancel();
                        resultError(WHAT_UPGRADE_OUT_OF_TIME, null, null);
                    } else {
                        sendTime();
                    }
                    break;
            }
            return false;
        }
    });

    private void sendTime() {
        L.d(TAG, "sendTime");
        mHandler.sendEmptyMessageDelayed(MESSAGE_CHECK, CHECK_TIME);
    }

    @Override
    public void start() {
        if (!mStart) {
            L.d(TAG, "start");
            mStart = true;
            mProgressTime = System.currentTimeMillis();
            sendTime();
        }
    }

    @Override
    public void setProgressTime() {
        mProgressTime = System.currentTimeMillis();
        L.d(TAG, "setProgressTime: " + mProgressTime);
    }

    public void cancel() {
        mStart = false;
        mHandler.removeMessages(MESSAGE_CHECK);
    }

}
