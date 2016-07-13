package com.nextapp.tuyatest.test.model;


import com.nextapp.tuyatest.test.bean.DpTestDataBean;

import java.util.ArrayList;

/**
 * Created by letian on 16/7/12.
 */
public interface IEditDpTestModel {
    ArrayList<DpTestDataBean> getDpTestDataBean(String devId);
}
