package com.nextapp.tuyatest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nextapp.tuyatest.R;
import com.nextapp.tuyatest.widget.ViewHolder;
import com.squareup.picasso.Picasso;
import com.tuya.smart.android.device.bean.GwWrapperBean;

import java.util.List;

/**
 * Created by leaf on 16/1/11.
 * 第三方共享
 */
public class SharedThirdAdapter extends BaseAdapter {

    private static final String TAG = "SharedThirdAdapter";

    protected final LayoutInflater mInflater;
    protected final Context mContext;
    protected List<Object> mBeans;

    public SharedThirdAdapter(Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
    }

    public void setData(List<Object> beans) {
        this.mBeans = beans;
    }

    @Override
    public int getCount() {
        if (null == mBeans)
            return 0;
        return mBeans.size();
    }

    @Override
    public Object getItem(int position) {
        if (null == mBeans)
            return null;
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

        Object o = mBeans.get(position);

        if (o instanceof GwWrapperBean) {
            GwWrapperBean gw = (GwWrapperBean) o;
            name.setText(gw.getGwBean().getName());
            Picasso.with(mContext).load(gw.getGwBean().getIconUrl()).into(icon);
        }

        line.setVisibility(mBeans.size() == position + 1 ? View.GONE : View.VISIBLE);
        bottomLine.setVisibility(mBeans.size() == position + 1 ? View.VISIBLE : View.GONE);
        return convertView;
    }
}
