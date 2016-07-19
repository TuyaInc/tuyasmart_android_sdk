package com.nextapp.tuyatest.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nextapp.tuyatest.R;
import com.squareup.picasso.Picasso;
import com.tuya.smart.android.base.ViewHolder;
import com.tuya.smart.android.device.bean.GwBean;
import com.tuya.smart.android.device.bean.GwWrapperBean;
import com.tuya.smart.sdk.bean.DeviceBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by letian on 16/7/18.
 */
public class CommonDeviceAdapter extends BaseAdapter {
    private Context mContext;

    public CommonDeviceAdapter(Context context, int list_common_device_item, List<DeviceBean> deviceBeens) {
        mContext = context;

    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    public void setData(List<DeviceBean> myDevices) {

    }

    public class DeviceViewHolder extends ViewHolder<GwWrapperBean> {
        public ImageView connect;
        public ImageView deviceIcon;
        public TextView device;

        public DeviceViewHolder(View contentView) {
            super(contentView);
            connect = (ImageView) contentView.findViewById(R.id.iv_device_list_dot);
            deviceIcon = (ImageView) contentView.findViewById(R.id.iv_device_icon);
            device = (TextView) contentView.findViewById(R.id.tv_device);
        }

        @Override
        public void initData(GwWrapperBean gwWrapperBean) {
            GwBean gwBean = gwWrapperBean.getGwBean();
            Picasso.with(mContext).load(gwWrapperBean.getGwBean().getIconUrl()).into(deviceIcon);
            final int resId;
            if (gwWrapperBean.isOnline()) {
                if (gwBean.getIsShare() != null && gwBean.getIsShare()) {
                    resId = R.drawable.ty_devicelist_share_green;
                } else {
                    resId = R.drawable.ty_devicelist_dot_green;
                }
            } else {
                if (gwBean.getIsShare() != null && gwBean.getIsShare()) {
                    resId = R.drawable.ty_devicelist_share_gray;
                } else {
                    resId = R.drawable.ty_devicelist_dot_gray;
                }
            }
            connect.setImageResource(resId);
        }
    }
}
