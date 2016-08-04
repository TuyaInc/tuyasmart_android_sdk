package com.nextapp.tuyatest;

import android.content.Context;
import android.view.View;
import android.widget.TextView;


import com.tuya.smart.android.base.BaseListArrayAdapter;
import com.tuya.smart.android.base.ViewHolder;
import com.tuya.smart.sdk.bean.DeviceBean;

import java.util.List;

/**
 * Created by letian on 15/2/25.
 */
public class DeviceListAdapter extends BaseListArrayAdapter<DeviceBean> {

    private static final String TAG = "DeviceListAdapter";

    public DeviceListAdapter(Context context, int resource, List<DeviceBean> data) {
        super(context, resource, data);
    }

    @Override
    protected ViewHolder view2Holder(View view) {
        return new InnerViewHolder(view);
    }

    @Override
    protected void bindView(ViewHolder viewHolder, DeviceBean data) {
        viewHolder.initData(data);
    }

    public class InnerViewHolder extends ViewHolder<DeviceBean> {
        public TextView tv_device;
        public TextView tv_device_status;

        public InnerViewHolder(View contentView) {
            super(contentView);
            tv_device = (TextView) contentView.findViewById(R.id.tv_device);
            tv_device_status = (TextView) contentView.findViewById(R.id.tv_device_status);
        }

        @Override
        public void initData(DeviceBean deviceBean) {
            tv_device.setText(deviceBean.getName());
            tv_device_status.setText(deviceBean.getIsOnline() ? getString(R.string.OnlineControl) : getString(R.string.offline));
        }
    }

}