package com.tuya.smart.android.demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tuya.smart.android.demo.R;
import com.squareup.picasso.Picasso;
import com.tuya.smart.android.base.ViewHolder;
import com.tuya.smart.sdk.bean.DeviceBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by letian on 16/7/18.
 */
public class CommonDeviceAdapter extends BaseAdapter {
    private final List<DeviceBean> mDevs;
    private final LayoutInflater mInflater;
    private Context mContext;

    public CommonDeviceAdapter(Context context) {
        mDevs = new ArrayList<>();
        mContext = context;
        mInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return mDevs.size();
    }

    @Override
    public DeviceBean getItem(int position) {
        return mDevs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DeviceViewHolder holder;
        if (null == convertView) {
            convertView = mInflater.inflate(R.layout.list_common_device_item, null);
            holder = new DeviceViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (DeviceViewHolder) convertView.getTag();
        }
        holder.initData(mDevs.get(position));
        return convertView;
    }

    public void setData(List<DeviceBean> myDevices) {
        mDevs.clear();
        if (myDevices != null) {
            mDevs.addAll(myDevices);
        }
        notifyDataSetChanged();
    }

    public class DeviceViewHolder extends ViewHolder<DeviceBean> {
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
        public void initData(DeviceBean deviceBean) {
            Picasso.with(mContext).load(deviceBean.getIconUrl()).into(deviceIcon);
            final int resId;
            if (deviceBean.getIsOnline()) {
                if (deviceBean.getIsShare() != null && deviceBean.getIsShare()) {
                    resId = R.drawable.ty_devicelist_share_green;
                } else {
                    resId = R.drawable.ty_devicelist_dot_green;
                }
            } else {
                if (deviceBean.getIsShare() != null && deviceBean.getIsShare()) {
                    resId = R.drawable.ty_devicelist_share_gray;
                } else {
                    resId = R.drawable.ty_devicelist_dot_gray;
                }
            }
            connect.setImageResource(resId);
            device.setText(deviceBean.getName());
        }
    }
}
