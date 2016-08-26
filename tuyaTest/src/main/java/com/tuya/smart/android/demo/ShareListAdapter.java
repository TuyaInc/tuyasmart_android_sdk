package com.tuya.smart.android.demo;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.tuya.smart.android.demo.R;
import com.tuya.smart.android.base.BaseListArrayAdapter;
import com.tuya.smart.android.base.ViewHolder;
import com.tuya.smart.android.user.bean.PersonBean;

import java.util.List;

/**
 * Created by letian on 16/1/5.
 */
public class ShareListAdapter extends BaseListArrayAdapter<PersonBean> {
    public ShareListAdapter(Context context, int resource, List<PersonBean> data) {
        super(context, resource, data);
    }

    @Override
    protected ViewHolder view2Holder(View view) {
        return new InnerViewHolder(view);
    }

    @Override
    protected void bindView(ViewHolder viewHolder, PersonBean data) {
        viewHolder.initData(data);
    }

    public class InnerViewHolder extends ViewHolder<PersonBean> {
        public TextView tv_name;
        public TextView tv_number;

        public InnerViewHolder(View contentView) {
            super(contentView);
            tv_name = (TextView) contentView.findViewById(R.id.tv_name);
            tv_number = (TextView) contentView.findViewById(R.id.tv_number);
        }

        @Override
        public void initData(PersonBean personBean) {
            tv_name.setText(personBean.getName());
            tv_number.setText(personBean.getMobile());
        }
    }
}
