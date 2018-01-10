package com.tuya.smart.android.demo.presenter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.tuya.smart.android.demo.test.utils.SchemaMapper;
import com.tuya.smart.android.demo.test.utils.SchemaUtil;
import com.tuya.smart.android.demo.utils.ToastUtil;
import com.tuya.smart.android.demo.view.IDpSendView;
import com.tuya.smart.android.device.bean.BitmapSchemaBean;
import com.tuya.smart.android.device.bean.BoolSchemaBean;
import com.tuya.smart.android.device.bean.EnumSchemaBean;
import com.tuya.smart.android.device.bean.SchemaBean;
import com.tuya.smart.android.device.bean.StringSchemaBean;
import com.tuya.smart.android.device.bean.ValueSchemaBean;
import com.tuya.smart.android.device.enums.DataTypeEnum;
import com.tuya.smart.android.hardware.model.IControlCallback;
import com.tuya.smart.android.mvp.presenter.BasePresenter;
import com.tuya.smart.sdk.TuyaGroup;
import com.tuya.smart.sdk.TuyaUser;
import com.tuya.smart.sdk.api.IGroupListener;
import com.tuya.smart.sdk.api.ITuyaGroup;
import com.tuya.smart.sdk.bean.DeviceBean;
import com.tuya.smart.sdk.bean.GroupBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by letian on 16/8/27.
 */
public class GroupDpSendPresenter extends BasePresenter implements IGroupListener {

    public static final String INTENT_GROUPID = "intent_devid";
    public static final String INTENT_DPID = "intent_dpid";
    private final Context mContext;
    private final IDpSendView mView;
    private long mGroupId;
    private String mDpId;
    private ITuyaGroup mTuyaGroup;
    private SchemaBean mSchemaBean;
    private DeviceBean mDev;

    public GroupDpSendPresenter(Context context, IDpSendView view) {
        mContext = context;
        mView = view;
        initData();
        initListener();
    }

    private void initListener() {
        mTuyaGroup.registerGroupListener(this);
    }

    private void initData() {
        mGroupId = ((Activity) mContext).getIntent().getLongExtra(INTENT_GROUPID, -1);
        mDpId = ((Activity) mContext).getIntent().getStringExtra(INTENT_DPID);
        mTuyaGroup = TuyaGroup.newGroupInstance(mGroupId);
        GroupBean groupBean = TuyaUser.getDeviceInstance().getGroupBean(mGroupId);
        List<String> devIds = groupBean.getDevIds();
        if (devIds == null || devIds.size() == 0) return;
        mDev = TuyaUser.getDeviceInstance().getDev(devIds.get(0));
        Map<String, SchemaBean> schema = TuyaUser.getDeviceInstance().getSchema(devIds.get(0));
        if (schema != null) {
            mSchemaBean = schema.get(mDpId);
        }
    }

    public String getTitle() {
        return mSchemaBean == null ? "" : mSchemaBean.getName();
    }


    public void showView() {
        if (mSchemaBean == null || mDev == null) return;
        if (mSchemaBean.getType().equals(DataTypeEnum.OBJ.getType())) {
            //obj tpye
            String schemaType = mSchemaBean.getSchemaType();
            if (TextUtils.equals(schemaType, BoolSchemaBean.type)) {
                mView.showBooleanView((Boolean) mDev.getDps().get(mSchemaBean.getId()));
            } else if (TextUtils.equals(schemaType, EnumSchemaBean.type)) {
                EnumSchemaBean enumSchemaBean = SchemaMapper.toEnumSchema(mSchemaBean.getProperty());
                mView.showEnumView((String) mDev.getDps().get(mSchemaBean.getId()), enumSchemaBean.getRange());
            } else if (TextUtils.equals(schemaType, StringSchemaBean.type)) {
                mView.showStringView((String) mDev.getDps().get(mSchemaBean.getId()));
            } else if (TextUtils.equals(schemaType, ValueSchemaBean.type)) {
                mView.showValueView((Integer) mDev.getDps().get(mSchemaBean.getId()));
            } else if (TextUtils.equals(schemaType, BitmapSchemaBean.type)) {
                mView.showBitmapView((Integer) mDev.getDps().get(mSchemaBean.getId()));
            }
        } else if (mSchemaBean.getType().equals(DataTypeEnum.RAW.getType())) {
            //raw | file tpye
            mView.showRawView((String) mDev.getDps().get(mSchemaBean.getId()));
        }
    }

    public void sendDpValue(final Object dps) {
        HashMap<String, Object> map = new HashMap<>();
        map.put(mDpId, dps);
        final String value = JSONObject.toJSONString(map);
        mView.showMessage("\n");
        mView.showMessage("send command: " + value);
        mTuyaGroup.publishDps(value, new IControlCallback() {
            @Override
            public void onError(String s, String s1) {
                mView.showMessage("send command failure");

            }

            @Override
            public void onSuccess() {
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    public String getName() {
        return mSchemaBean == null ? "" : mSchemaBean.getName() + ":";
    }

    public String getDpDes() {
        if (mSchemaBean == null) return "";
        return mSchemaBean.getProperty();
    }

    public void sendDpValue() {
        if (mSchemaBean == null) return;
        if (mSchemaBean.getType().equals(DataTypeEnum.OBJ.getType())) {
            //obj tpye
            String schemaType = mSchemaBean.getSchemaType();
            if (TextUtils.equals(schemaType, StringSchemaBean.type)) {
                String strValue = mView.getStrValue();
                if (SchemaUtil.checkStrValue(mSchemaBean, strValue)) {
                    sendDpValue(strValue);
                } else mView.showFormatErrorTip();
            } else if (TextUtils.equals(schemaType, ValueSchemaBean.type)) {
                String value = mView.getValue();
                if (SchemaUtil.checkValue(mSchemaBean, value)) {
                    sendDpValue(Integer.valueOf(value));
                } else mView.showFormatErrorTip();
            } else if (TextUtils.equals(schemaType, BitmapSchemaBean.type)) {
                String value = mView.getBitmapValue();
                if (SchemaUtil.checkBitmapValue(mSchemaBean, value)) {
                    sendDpValue(Integer.valueOf(value));
                } else {
                    mView.showFormatErrorTip();
                }
            }
        } else if (mSchemaBean.getType().equals(DataTypeEnum.RAW.getType())) {
            //raw | file tpye
            String rawValue = mView.getRAWValue();
            if (SchemaUtil.checkRawValue(rawValue)) {
                sendDpValue(rawValue);
            } else mView.showFormatErrorTip();
        }
    }

    @Override
    public void onDpUpdate(String dps) {
        mView.showMessage("onDpUpdate: " + dps);
    }

    @Override
    public void onStatusChanged(boolean online) {

    }

    @Override
    public void onNetworkStatusChanged(boolean status) {

    }
}
