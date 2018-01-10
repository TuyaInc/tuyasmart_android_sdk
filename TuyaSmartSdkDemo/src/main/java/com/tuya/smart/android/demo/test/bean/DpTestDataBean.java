package com.tuya.smart.android.demo.test.bean;

import com.tuya.smart.android.device.bean.SchemaBean;

/**
 * Created by letian on 16/7/12.
 */
public class DpTestDataBean {
    private String key;
    private SchemaBean bean;

    public DpTestDataBean(String key, SchemaBean value) {
        this.key = key;
        this.bean = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public SchemaBean getBean() {
        return bean;
    }

    public void setBean(SchemaBean bean) {
        this.bean = bean;
    }
}
