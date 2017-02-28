package com.tuya.smart.android.demo.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tuya.smart.android.demo.R;
import com.tuya.smart.sdk.bean.GroupDeviceBean;

import java.util.List;

/**
 * 群组设备适配
 * Created by chenshixin on 15/12/11.
 */
public class GroupDeviceEditAdapter extends BaseListArrayAdapter<GroupDeviceBean> {

    private static final String TAG = "GroupDeviceAdapter";

    public GroupDeviceEditAdapter(Context context, int resource, List<GroupDeviceBean> data) {
        super(context, resource, data);
    }

    @Override
    protected ViewHolder view2Holder(View view) {
        return new InnerViewHolder(view);
    }

    @Override
    protected void bindView(ViewHolder viewHolder, GroupDeviceBean data) {
        viewHolder.initData(data);
    }

    public class InnerViewHolder extends ViewHolder<GroupDeviceBean> {
        public ImageView selected;
        public ImageView deviceIcon;
        public TextView device;

        public InnerViewHolder(View contentView) {
            super(contentView);
            selected = (ImageView) contentView.findViewById(R.id.iv_device_list_dot);
            deviceIcon = (ImageView) contentView.findViewById(R.id.iv_device_icon);
            device = (TextView) contentView.findViewById(R.id.tv_device);
        }

        @Override
        public void initData(GroupDeviceBean deviceBean) {
            Picasso.with(mContext).load(deviceBean.getDeviceBean().getIconUrl()).into(deviceIcon);
            device.setText(deviceBean.getDeviceBean().getName());
            selected.setImageResource(deviceBean.isChecked()
                    ? R.drawable.ty_group_selected : R.drawable.ty_group_un_select);
        }
    }

}