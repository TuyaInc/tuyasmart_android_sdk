package com.tuya.smart.android.demo.test.model;

import com.tuya.smart.android.demo.test.bean.DpTestDataBean;

import java.util.ArrayList;

/**
 * Created by letian on 16/7/12.
 */
public interface IEditDpTestModel {
    ArrayList<DpTestDataBean> getDpTestDataBean(String devId);
}
