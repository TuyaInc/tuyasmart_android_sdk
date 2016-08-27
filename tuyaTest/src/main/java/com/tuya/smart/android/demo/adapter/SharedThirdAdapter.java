package com.tuya.smart.android.demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tuya.smart.android.demo.R;
import com.tuya.smart.android.demo.widget.ViewHolder;
import com.tuya.smart.sdk.bean.DeviceBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leaf on 16/1/11.
 * 第三方共享
 */
public class SharedThirdAdapter extends BaseAdapter {

    private static final String TAG = "SharedThirdAdapter";

    protected final LayoutInflater mInflater;
    protected final Context mContext;
    protected List<DeviceBean> mBeans;

    public SharedThirdAdapter(Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        mBeans = new ArrayList<>();
    }

    public void setData(List<DeviceBean> beans) {
        mBeans.clear();
        if (beans != null) {
            mBeans.addAll(beans);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mBeans.size();
    }

    @Override
    public DeviceBean getItem(int position) {
        return mBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView)
            convertView = mInflater.inflate(R.layout.list_shared_third, null);

        ImageView icon = ViewHolder.get(convertView, R.id.icon);
        TextView name = ViewHolder.get(convertView, R.id.name);
        View line = ViewHolder.get(convertView, R.id.line);
        View bottomLine = ViewHolder.get(convertView, R.id.bottom_line);

        DeviceBean deviceBean = mBeans.get(position);
        name.setText(deviceBean.getName());
        Picasso.with(mContext).load(deviceBean.getIconUrl()).into(icon);
        line.setVisibility(mBeans.size() == position + 1 ? View.GONE : View.VISIBLE);
        bottomLine.setVisibility(mBeans.size() == position + 1 ? View.VISIBLE : View.GONE);
        return convertView;
    }
}
