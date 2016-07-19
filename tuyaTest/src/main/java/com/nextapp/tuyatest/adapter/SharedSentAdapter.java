package com.nextapp.tuyatest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nextapp.tuyatest.R;
import com.nextapp.tuyatest.widget.ViewHolder;
import com.tuya.smart.android.user.bean.PersonBean;

import java.util.ArrayList;

/**
 * Created by leaf on 15/12/18.
 * 发出的共享
 */
public class SharedSentAdapter extends BaseAdapter {

    private static final String TAG = "SharedSentAdapter";

    private final LayoutInflater mInflater;
    private ArrayList<PersonBean> mSentMembers;

    public SharedSentAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
    }

    public void setData(ArrayList<PersonBean> groupMembers) {
        this.mSentMembers = groupMembers;
    }

    @Override
    public int getCount() {
        if (null == mSentMembers)
            return 0;
        return mSentMembers.size();
    }

    @Override
    public Object getItem(int position) {
        if (null == mSentMembers)
            return null;
        return mSentMembers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView)
            convertView = mInflater.inflate(R.layout.list_shared_sent, null);

        TextView name = ViewHolder.get(convertView, R.id.name);
        TextView phone = ViewHolder.get(convertView, R.id.phone);
        View line = ViewHolder.get(convertView, R.id.line);
        View topLine = ViewHolder.get(convertView, R.id.top_line);
        View bottomLine = ViewHolder.get(convertView, R.id.bottom_line);

        PersonBean person = mSentMembers.get(position);

        name.setText(person.getName());
        phone.setText(person.getMobile());
        line.setVisibility(mSentMembers.size() == position + 1 ? View.GONE : View.VISIBLE);
        topLine.setVisibility(0 == position ? View.VISIBLE : View.GONE);
        bottomLine.setVisibility(mSentMembers.size() == position + 1 ? View.VISIBLE : View.GONE);

        return convertView;
    }
}
