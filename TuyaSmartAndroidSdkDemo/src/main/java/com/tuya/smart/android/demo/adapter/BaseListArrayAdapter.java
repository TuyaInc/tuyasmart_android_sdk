package com.tuya.smart.android.demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

public abstract class BaseListArrayAdapter<E> extends ArrayAdapter<E> {

    protected Context mContext;

    protected int currentPosition;

    protected int mResource;

    protected LayoutInflater mInflater;

    protected List<E> mData;

    public BaseListArrayAdapter(Context context, int resource, List<E> data) {
        super(context, resource, data);
        mContext = context;
        mResource = resource;
        mData = data;
        mInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    protected abstract ViewHolder view2Holder(View view);

    protected abstract void bindView(ViewHolder viewHolder, E data);

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = mInflater.inflate(mResource, null);
            convertView.setTag(view2Holder(convertView));
        }
        currentPosition = position;
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        bindView(viewHolder, getItem(position));
        return convertView;
    }

    public List<E> getData() {
        return mData;
    }

    /**
     * 获取当前的view的position
     *
     * @return
     */
    public int getCurrentPosition() {
        return currentPosition;
    }

    /**
     * 获取数据信息
     *
     * @param id
     * @return
     */
    public String getString(int id) {
        return mContext.getString(id);
    }

    public void setData(List<E> personBeans) {
        mData = personBeans;
        clear();
        addAll(personBeans);
    }
}
