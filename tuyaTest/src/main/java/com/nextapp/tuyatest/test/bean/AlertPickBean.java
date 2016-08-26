package com.nextapp.tuyatest.test.bean;

import java.util.ArrayList;

/**
 * Created by letian on 15/6/13.
 */
public class AlertPickBean {
    public ArrayList<String> getRangeKeys() {
        return rangeKeys;
    }

    public void setRangeKeys(ArrayList<String> rangeKeys) {
        this.rangeKeys = rangeKeys;
    }

    public ArrayList<String> getRangeValues() {
        return rangeValues;
    }

    public void setRangeValues(ArrayList<String> rangeValues) {
        this.rangeValues = rangeValues;
    }


    public boolean isLoop() {
        return loop;
    }

    public void setLoop(boolean loop) {
        this.loop = loop;
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCancelText(String cancelText) {
        this.cancelText = cancelText;
    }

    public String getConfirmText() {
        return confirmText;
    }

    public void setConfirmText(String confirmText) {
        this.confirmText = confirmText;
    }

    public String getCancelText() {
        return cancelText;
    }
    private ArrayList<String> rangeKeys;
    private ArrayList<String> rangeValues;
    private String cancelText;
    private String confirmText;
    private boolean loop;
    private int selected;
    private String title;
}