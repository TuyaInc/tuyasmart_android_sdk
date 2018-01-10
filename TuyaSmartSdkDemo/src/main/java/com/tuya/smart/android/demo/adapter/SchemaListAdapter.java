package com.tuya.smart.android.demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tuya.smart.android.demo.R;
import com.tuya.smart.android.device.bean.SchemaBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by letian on 16/8/4.
 */
public class SchemaListAdapter extends BaseAdapter {
    private List<SchemaBean> mSchemaBeen;
    private LayoutInflater mInflater;

    public SchemaListAdapter(Context context) {
        mSchemaBeen = new ArrayList<>();
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mSchemaBeen.size();
    }

    @Override
    public SchemaBean getItem(int position) {
        return mSchemaBeen.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SchemaViewHolder holder;
        if (null == convertView) {
            convertView = mInflater.inflate(R.layout.list_schema, null);
            holder = new SchemaViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (SchemaViewHolder) convertView.getTag();
        }
        holder.initData(mSchemaBeen.get(position));
        return convertView;
    }

    public void setData(List<SchemaBean> schemaBeen) {
        mSchemaBeen.clear();
        if (schemaBeen != null) {
            mSchemaBeen.addAll(schemaBeen);
        }
        notifyDataSetChanged();
    }

    public class SchemaViewHolder extends ViewHolder<SchemaBean> {
        public TextView schemaName;
        public TextView schemaId;
        private TextView schemaMode;

        public SchemaViewHolder(View contentView) {
            super(contentView);
            schemaName = (TextView) contentView.findViewById(R.id.tv_name);
            schemaId = (TextView) contentView.findViewById(R.id.tv_id);
            schemaMode = (TextView) contentView.findViewById(R.id.tv_mode);
        }

        @Override
        public void initData(SchemaBean schemaBean) {
            schemaId.setText(schemaBean.getId());
            schemaName.setText(schemaBean.getName());
            schemaMode.setText(schemaBean.getMode().toString());
        }
    }
}
