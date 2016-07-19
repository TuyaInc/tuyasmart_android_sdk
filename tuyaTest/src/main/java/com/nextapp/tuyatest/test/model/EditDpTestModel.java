package com.nextapp.tuyatest.test.model;

import android.content.Context;
import android.text.TextUtils;

import com.tuya.smart.android.common.utils.SafeHandler;
import com.nextapp.tuyatest.test.bean.DpTestDataBean;
import com.tuya.smart.android.device.bean.SchemaBean;
import com.tuya.smart.android.device.enums.ModeEnum;
import com.tuya.smart.android.mvp.model.BaseModel;
import com.tuya.smart.sdk.TuyaUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

/**
 * Created by letian on 16/7/12.
 */
public class EditDpTestModel extends BaseModel implements IEditDpTestModel {
    public EditDpTestModel(Context ctx, SafeHandler handler) {
        super(ctx, handler);
    }

    @Override
    public void onDestroy() {


    }

    @Override
    public ArrayList<DpTestDataBean> getDpTestDataBean(String devId) {
        ArrayList<DpTestDataBean> mDpTestDataBeen = new ArrayList<>();
        Map<String, SchemaBean> schema = TuyaUser.getDeviceInstance().getSchema(devId);
        if (schema != null) {
            for (Map.Entry<String, SchemaBean> entry : schema.entrySet()) {
                SchemaBean schemaBean = entry.getValue();
                if (!TextUtils.equals(schemaBean.getMode(), ModeEnum.RO.getType())) {
                    mDpTestDataBeen.add(new DpTestDataBean(entry.getKey(), schemaBean));
                }
            }
        }

        Collections.sort(mDpTestDataBeen, new Comparator<DpTestDataBean>() {
            @Override
            public int compare(DpTestDataBean lhs, DpTestDataBean rhs) {
                return Integer.valueOf(lhs.getKey()) < Integer.valueOf(rhs.getKey()) ? -1 : 1;
            }
        });
        return mDpTestDataBeen;
    }
}
