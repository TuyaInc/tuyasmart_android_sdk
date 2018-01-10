package com.tuya.smart.android.demo.test.utils;

import com.alibaba.fastjson.JSONObject;
import com.tuya.smart.android.device.bean.BitmapSchemaBean;
import com.tuya.smart.android.device.bean.BoolSchemaBean;
import com.tuya.smart.android.device.bean.EnumSchemaBean;
import com.tuya.smart.android.device.bean.StringSchemaBean;
import com.tuya.smart.android.device.bean.ValueSchemaBean;

/**
 * Created by letian on 16/7/12.
 */
public class SchemaMapper {

    public static BoolSchemaBean toBoolSchema(String data) {
        return JSONObject.parseObject(data, BoolSchemaBean.class);
    }


    public static EnumSchemaBean toEnumSchema(String data) {
        return JSONObject.parseObject(data, EnumSchemaBean.class);
    }


    public static StringSchemaBean toStringSchema(String data) {
        return JSONObject.parseObject(data, StringSchemaBean.class);
    }


    public static ValueSchemaBean toValueSchema(String data) {
        return JSONObject.parseObject(data, ValueSchemaBean.class);
    }

    public static BitmapSchemaBean toBitmapSchema(String data) {
        return JSONObject.parseObject(data, BitmapSchemaBean.class);
    }
}
