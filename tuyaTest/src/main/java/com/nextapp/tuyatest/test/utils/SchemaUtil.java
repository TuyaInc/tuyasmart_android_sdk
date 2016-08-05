package com.nextapp.tuyatest.test.utils;

import android.text.TextUtils;

import com.tuya.smart.android.common.utils.HexUtil;
import com.tuya.smart.android.device.bean.EnumSchemaBean;
import com.tuya.smart.android.device.bean.SchemaBean;
import com.tuya.smart.android.device.bean.StringSchemaBean;
import com.tuya.smart.android.device.bean.ValueSchemaBean;

/**
 * Created by letian on 16/8/5.
 */
public class SchemaUtil {

    public static boolean checkRawValue(String rawValue) {
        return HexUtil.checkHexString(rawValue) && rawValue.length() % 2 == 0;
    }

    public static boolean checkEnumValue(SchemaBean schemaBean, String enumValue) {
        EnumSchemaBean enumSchemaBean = SchemaMapper.toEnumSchema(schemaBean.getProperty());
        return false;
    }

    public static boolean checkStrValue(SchemaBean schemaBean, String strValue) {
        StringSchemaBean stringSchemaBean = SchemaMapper.toStringSchema(schemaBean.getProperty());
        return !TextUtils.isEmpty(strValue) && strValue.length() <= stringSchemaBean.getMaxlen();
    }

    public static boolean checkValue(SchemaBean schemaBean, String value) {
        int v;
        try {
            v = Integer.valueOf(value);
        } catch (Exception e) {
            return false;
        }
        ValueSchemaBean valueSchemaBean = SchemaMapper.toValueSchema(schemaBean.getProperty());
        int max = valueSchemaBean.getMax();
        int min = valueSchemaBean.getMin();
        return v <= max && v >= min;
    }
}
