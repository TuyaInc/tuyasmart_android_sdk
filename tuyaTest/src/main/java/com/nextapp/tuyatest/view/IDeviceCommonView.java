package com.nextapp.tuyatest.view;

import com.tuya.smart.android.device.bean.SchemaBean;

import java.util.List;

/**
 * Created by letian on 16/8/4.
 */
public interface IDeviceCommonView {
    void setSchemaData(List<SchemaBean> schemaList);

    void updateTitle(String titleName);
}
