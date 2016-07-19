package com.nextapp.tuyatest.utils;

import android.os.Message;

import com.tuya.smart.android.mvp.bean.Result;
import com.tuya.smart.android.network.http.BusinessResponse;

/**
 * Created by lee on 16/5/12.
 */
public class MessageUtil {
    public static Message getCallFailMessage(int msgWhat, BusinessResponse businessResponse){
        return getCallFailMessage(msgWhat,businessResponse.getErrorCode(),businessResponse.getErrorMsg());
    }

    public static Message getCallFailMessage(int msgWhat, String errorCode,String errorMsg){
        Message msg = new Message();
        msg.what = msgWhat;
        Result result = new Result();
        result.error = errorMsg;
        result.errorCode = errorCode;
        msg.obj = result;
        return msg;
    }

    public static Message getCallFailMessage(int msgWhat, String errorCode,String errorMsg,Object resultObj){
        Message msg = new Message();
        msg.what = msgWhat;
        Result result = new Result();
        result.error = errorMsg;
        result.errorCode = errorCode;
        result.setObj(resultObj);
        msg.obj = result;

        return msg;
    }



    public static Message getCallFailMessage(int msgWhat, BusinessResponse businessResponse, Object resultObj){
        return getCallFailMessage(msgWhat,businessResponse.getErrorCode(),businessResponse.getErrorMsg(),resultObj);
    }

    public static Message getMessage(int msgWhat,Object msgObj){
        Message msg = new Message();
        msg.what = msgWhat;
        msg.obj = msgObj;
        return msg;
    }

    public static Message getResultMessage(int msgWhat,Object msgObj){
        Message msg = new Message();
        msg.what = msgWhat;
        Result result = new Result();
        result.setObj(msgObj);
        msg.obj = result;
        return msg;
    }
}
