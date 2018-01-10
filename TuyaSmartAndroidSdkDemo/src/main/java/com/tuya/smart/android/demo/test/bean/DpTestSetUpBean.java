package com.tuya.smart.android.demo.test.bean;

/**
 * Created by letian on 16/8/6.
 */
public class DpTestSetUpBean {

    private boolean needPool = false;
    private int timeInterval = 100;
    private boolean showLogWindow = true;
    private boolean showTestReport = true;
    private boolean sendByLan = true;
    private boolean sendByCloud = true;

    public boolean isNeedPool() {
        return needPool;
    }

    public void setNeedPool(boolean needPool) {
        this.needPool = needPool;
    }

    public int getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(int timeInterval) {
        this.timeInterval = timeInterval;
    }

    public boolean isShowLogWindow() {
        return showLogWindow;
    }

    public void setShowLogWindow(boolean showLogWindow) {
        this.showLogWindow = showLogWindow;
    }

    public boolean isShowTestReport() {
        return showTestReport;
    }

    public void setShowTestReport(boolean showTestReport) {
        this.showTestReport = showTestReport;
    }

    public boolean isSendByLan() {
        return sendByLan;
    }

    public void setSendByLan(boolean sendByLan) {
        this.sendByLan = sendByLan;
    }

    public boolean isSendByCloud() {
        return sendByCloud;
    }

    public void setSendByCloud(boolean sendByCloud) {
        this.sendByCloud = sendByCloud;
    }
}
