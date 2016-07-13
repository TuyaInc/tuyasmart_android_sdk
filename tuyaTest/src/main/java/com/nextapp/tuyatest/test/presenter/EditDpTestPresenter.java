package com.nextapp.tuyatest.test.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.nextapp.tuyatest.R;
import com.nextapp.tuyatest.test.bean.AlertPickBean;
import com.nextapp.tuyatest.test.bean.DpTestDataBean;
import com.nextapp.tuyatest.test.event.EventSender;
import com.nextapp.tuyatest.test.model.EditDpTestModel;
import com.nextapp.tuyatest.test.model.IEditDpTestModel;
import com.nextapp.tuyatest.test.utils.DialogUtil;
import com.nextapp.tuyatest.test.utils.SchemaMapper;
import com.nextapp.tuyatest.test.utils.UIFactory;
import com.nextapp.tuyatest.test.view.IEditDpTestView;
import com.nextapp.tuyatest.test.widget.AlertPickDialog;
import com.tuya.smart.android.device.TuyaSmartDevice;
import com.tuya.smart.android.device.bean.BoolSchemaBean;
import com.tuya.smart.android.device.bean.EnumSchemaBean;
import com.tuya.smart.android.device.bean.SchemaBean;
import com.tuya.smart.android.device.bean.StringSchemaBean;
import com.tuya.smart.android.device.bean.ValueSchemaBean;
import com.tuya.smart.android.device.enums.DataTypeEnum;
import com.tuya.smart.android.device.utils.PreferencesUtil;
import com.tuya.smart.android.mvp.presenter.BasePresenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by letian on 16/7/12.
 */
public class EditDpTestPresenter extends BasePresenter {
    private final Context mContext;
    private final IEditDpTestView mView;
    private String mDevId;
    List<SendAndBackData> mSendAndBackDatas;

    private IEditDpTestModel mModel;

    public EditDpTestPresenter(Context context, IEditDpTestView view) {
        mContext = context;
        mView = view;
        mModel = new EditDpTestModel(context, mHandler);
        mSendAndBackDatas = new ArrayList<>();
        initData();
        String string = PreferencesUtil.getString(mDevId);
        if (!TextUtils.isEmpty(string)) {
            mSendAndBackDatas = JSONObject.parseArray(string, SendAndBackData.class);
            for (SendAndBackData sendAndBackData : mSendAndBackDatas) {
                mView.log(JSONObject.toJSONString(sendAndBackData.getSendValue()));
            }
        }
    }


    private void initData() {
        mDevId = ((Activity) (mContext)).getIntent().getStringExtra(DeviceTestPresenter.INTENT_DEVICE_ID);
    }

    public void addDpValue() {

        ArrayList<DpTestDataBean> mDpTestDataBeen = mModel.getDpTestDataBean(mDevId);
        ArrayList<String> rangesKey = new ArrayList<>();
        ArrayList<String> rangesValue = new ArrayList<>();
        for (DpTestDataBean dpTestDataBean : mDpTestDataBeen) {
            rangesKey.add(dpTestDataBean.getKey());
            rangesValue.add(dpTestDataBean.getBean().getName() + " : " + dpTestDataBean.getKey());
        }
        if (rangesKey.size() == 0) return;
        final AlertPickBean alertPickBean = new AlertPickBean();
        alertPickBean.setLoop(true);
        alertPickBean.setCancelText(mContext.getString(R.string.cancel));
        alertPickBean.setConfirmText(mContext.getString(R.string.confirm));
        alertPickBean.setRangeKeys(rangesKey);
        alertPickBean.setRangeValues(rangesValue);
        alertPickBean.setTitle(mContext.getString(R.string.choose_dp));
        AlertPickDialog.showAlertPickDialog((Activity) mContext, alertPickBean, new AlertPickDialog.AlertPickCallBack() {
            @Override
            public void confirm(String value) {
                switchDpDialog(value);
            }

            @Override
            public void cancel() {

            }
        });
    }

    private void switchDpDialog(String dpId) {
        Map<String, SchemaBean> schema = TuyaSmartDevice.getInstance().getSchema(mDevId, mDevId);
        SchemaBean schemaBean = schema.get(dpId);
        if (schemaBean.getType().equals(DataTypeEnum.OBJ.getType())) {
            //obj 类型
            String schemaType = schemaBean.getSchemaType();
            if (TextUtils.equals(schemaType, BoolSchemaBean.type)) {
                showBoolDialog(schemaBean);
            } else if (TextUtils.equals(schemaType, EnumSchemaBean.type)) {
                EnumSchemaBean enumSchemaBean = SchemaMapper.toEnumSchema(schemaBean.getProperty());
                showEnumDialog(schemaBean, enumSchemaBean.getRange());
            } else if (TextUtils.equals(schemaType, StringSchemaBean.type)) {
                StringSchemaBean stringSchemaBean = SchemaMapper.toStringSchema(schemaBean.getProperty());
                showStringDialog(schemaBean, stringSchemaBean.getMaxlen());
            } else if (TextUtils.equals(schemaType, ValueSchemaBean.type)) {
                ValueSchemaBean valueSchemaBean = SchemaMapper.toValueSchema(schemaBean.getProperty());
                showValueDialog(schemaBean, valueSchemaBean.getMax(), valueSchemaBean.getMin());
            }
        } else {
            //raw | file 类型
            showRawDialog(schemaBean);
        }
    }


    private void showEnumDialog(final SchemaBean schemaBean, Set<String> range) {
        ArrayList<String> rangesKey = new ArrayList<>();
        ArrayList<String> rangesValue = new ArrayList<>();
        for (String str : range) {
            rangesValue.add(str);
            rangesKey.add(str);
        }
        final AlertPickBean alertPickBean = new AlertPickBean();
        alertPickBean.setLoop(true);
        alertPickBean.setCancelText(mContext.getString(R.string.cancel));
        alertPickBean.setConfirmText(mContext.getString(R.string.confirm));
        alertPickBean.setRangeKeys(rangesKey);
        alertPickBean.setRangeValues(rangesValue);
        alertPickBean.setTitle(String.format(mContext.getString(R.string.choose_dp_value), schemaBean.getId()));
        AlertPickDialog.showAlertPickDialog((Activity) mContext, alertPickBean, new AlertPickDialog.AlertPickCallBack() {
            @Override
            public void confirm(String value) {
                String dpId = schemaBean.getId();
                log(dpId, value);
            }

            @Override
            public void cancel() {

            }
        });
    }

    private void log(String dpId, Object dpValue) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(dpId, dpValue);
        SendAndBackData sendAndBackData = new SendAndBackData();
        sendAndBackData.setSendValue(hashMap);
        mSendAndBackDatas.add(sendAndBackData);
        mView.log(JSONObject.toJSONString(hashMap));
    }

    private void showStringDialog(final SchemaBean schemaBean, final int maxlen) {
        DialogUtil.simpleInputDialog(mContext, String.format(mContext.getString(R.string.choose_dp_value), schemaBean.getId()), String.format(mContext.getString(R.string.dp_string_max_len), maxlen), true, new DialogUtil.SimpleInputDialogInterface() {
            @Override
            public void onPositive(DialogInterface dialog, String inputText) {
                if (inputText.length() <= maxlen) {
                    String dpId = schemaBean.getId();
                    log(dpId, inputText);

                } else {
                    Toast.makeText(mContext, "Out of Range", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNegative(DialogInterface dialog) {

            }
        });
    }

    private void showValueDialog(final SchemaBean schemaBean, final int max, final int min) {
        simpleInputDialog(mContext, String.format(mContext.getString(R.string.choose_dp_value), schemaBean.getId()), String.format(mContext.getString(R.string.dp_value_max_min_len), min, max), true, new DialogUtil.SimpleInputDialogInterface() {
            @Override
            public void onPositive(DialogInterface dialog, String inputText) {
                Integer value = Integer.valueOf(inputText);
                if (value <= max && value >= min) {
                    String dpId = schemaBean.getId();
                    Integer dpValue = Integer.valueOf(inputText);
                    log(dpId, dpValue);
                } else {
                    Toast.makeText(mContext, "Out of Range", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNegative(DialogInterface dialog) {

            }
        });
    }

    private void showRawDialog(SchemaBean schemaBean) {
        DialogUtil.simpleInputDialog(mContext, String.format(mContext.getString(R.string.choose_dp_value), schemaBean.getId()), null, false, new DialogUtil.SimpleInputDialogInterface() {
            @Override
            public void onPositive(DialogInterface dialog, String inputText) {

            }

            @Override
            public void onNegative(DialogInterface dialog) {

            }
        });
    }

    private void showBoolDialog(final SchemaBean schemaBean) {
        String[] newStr = new String[2];
        newStr[0] = mContext.getString(R.string.open);
        newStr[1] = mContext.getString(R.string.close);
        DialogUtil.listSelectDialog(mContext, String.format(mContext.getString(R.string.choose_dp_value), schemaBean.getId()), newStr, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String dpId = schemaBean.getId();
                boolean value = position == 0;
                log(dpId, value);
            }
        });

    }

    public static void simpleInputDialog(Context context, String title, CharSequence text,
                                         boolean isHint,
                                         final DialogUtil.SimpleInputDialogInterface listener) {
        AlertDialog.Builder dialog = UIFactory.buildAlertDialog(context);
        final EditText inputEditText = (EditText) LayoutInflater.from(context).inflate(
                R.layout.ty_dialog_simple_input_with_number, null);
        DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        if (listener != null) {
                            listener.onPositive(dialog, inputEditText.getEditableText().toString());
                        }
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        if (listener != null) {
                            listener.onNegative(dialog);
                        }
                        break;
                    default:
                        break;
                }
            }
        };
        dialog.setNegativeButton(R.string.ty_cancel, onClickListener);
        dialog.setPositiveButton(R.string.ty_confirm, onClickListener);
        dialog.setTitle(title);
        if (!TextUtils.isEmpty(text)) {
            if (isHint) {
                inputEditText.setHint(text);
            } else {
                inputEditText.setText(text);
            }
        }
        dialog.setView(inputEditText);
        inputEditText.requestFocus();
        dialog.setCancelable(false);
        dialog.create().show();
    }

    public void clear() {
        mSendAndBackDatas.clear();
        mView.clearLog();
    }

    public void save() {
        if (mSendAndBackDatas.size() == 0) {
            Toast.makeText(mContext, mContext.getString(R.string.please_input_dp_value), Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(mContext, mContext.getString(R.string.save_success), Toast.LENGTH_SHORT).show();
        EventSender.sendTestDpValue(mSendAndBackDatas);
        ((Activity) mContext).finish();
    }
}
