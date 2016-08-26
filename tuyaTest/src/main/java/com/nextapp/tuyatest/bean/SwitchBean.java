package com.nextapp.tuyatest.bean;

/**
 * Created by letian on 16/7/21.
 */
public class SwitchBean {
    public static final String SWITCH_DPID = "1";
    private boolean open;

    public SwitchBean(boolean open) {
        this.open = open;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }
}
