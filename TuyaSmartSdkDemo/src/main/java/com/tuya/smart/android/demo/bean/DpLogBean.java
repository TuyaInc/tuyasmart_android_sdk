package com.tuya.smart.android.demo.bean;

/**
 * Created by letian on 16/8/29.
 */
public class DpLogBean {
    private String errorMsg;
    private long timeStart;
    private long timeEnd;
    private String dpSend;
    private String dpReturn;

    public DpLogBean(long timeStart, long timeEnd, String dpSend, String dpReturn, String errorMsg) {
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.dpSend = dpSend;
        this.dpReturn = dpReturn;
        this.errorMsg = errorMsg;
    }

    public long getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(long timeStart) {
        this.timeStart = timeStart;
    }

    public long getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(long timeEnd) {
        this.timeEnd = timeEnd;
    }

    public String getDpSend() {
        return dpSend;
    }

    public void setDpSend(String dpSend) {
        this.dpSend = dpSend;
    }

    public String getDpReturn() {
        return dpReturn;
    }

    public void setDpReturn(String dpReturn) {
        this.dpReturn = dpReturn;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
}
