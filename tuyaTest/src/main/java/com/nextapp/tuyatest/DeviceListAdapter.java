package com.nextapp.tuyatest;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.tuya.smart.android.base.BaseListArrayAdapter;
import com.tuya.smart.android.base.ViewHolder;
import com.tuya.smart.android.device.bean.GwBean;
import com.tuya.smart.android.device.bean.GwWrapperBean;

import java.util.List;

/**
 * Created by letian on 15/2/25.
 */
public class DeviceListAdapter extends BaseListArrayAdapter<GwWrapperBean> {

    private static final String TAG = "DeviceListAdapter";

    public DeviceListAdapter(Context context, int resource, List<GwWrapperBean> data) {
        super(context, resource, data);
    }

    @Override
    protected ViewHolder view2Holder(View view) {
        return new InnerViewHolder(view);
    }

    @Override
    protected void bindView(ViewHolder viewHolder, GwWrapperBean data) {
        viewHolder.initData(data);
    }

    public class InnerViewHolder extends ViewHolder<GwWrapperBean> {
        public TextView tv_device;
        public TextView tv_device_status;

        public InnerViewHolder(View contentView) {
            super(contentView);
            tv_device = (TextView) contentView.findViewById(R.id.tv_device);
            tv_device_status = (TextView) contentView.findViewById(R.id.tv_device_status);
        }

        @Override
        public void initData(GwWrapperBean gwWrapperBean) {
            GwBean gwBean = gwWrapperBean.getGwBean();
            tv_device.setText(gwBean.getName());
            tv_device_status.setText(gwWrapperBean.isIntranetControl() ? getString(R.string.IntranetControl) : (gwWrapperBean.isCloudOnline() ? getString(R.string.OnlineControl) : getString(R.string.offline)));
        }
    }

}