package com.tuya.smart.android.demo.model;


import android.content.Context;

import com.tuya.smart.android.common.utils.SafeHandler;
import com.tuya.smart.android.demo.R;
import com.tuya.smart.android.mvp.model.BaseModel;

import java.util.ArrayList;

/**
 * Created by leaf on 15/12/21.
 * 添加共享
 */
public class SharedMemberAddModel extends BaseModel implements ISharedMemberAddModel {
    private Context mContext;

    public SharedMemberAddModel(Context ctx, SafeHandler handler) {
        super(ctx, handler);
        this.mContext = ctx;
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public ArrayList<String> getRelationNameList() {
        ArrayList<String> relationList = new ArrayList<>();
        relationList.add(mContext.getString(R.string.ty_add_share_relation));
        relationList.add(mContext.getString(R.string.ty_add_share_relation_item1));
        relationList.add(mContext.getString(R.string.ty_add_share_relation_item2));
        relationList.add(mContext.getString(R.string.ty_add_share_relation_item3));
        relationList.add(mContext.getString(R.string.ty_add_share_relation_item4));
        relationList.add(mContext.getString(R.string.ty_add_share_relation_item5));
        relationList.add(mContext.getString(R.string.ty_add_share_relation_item6));
        relationList.add(mContext.getString(R.string.ty_add_share_relation_item7));

        return relationList;
    }

    @Override
    public ArrayList<String> getRelationList() {
        ArrayList<String> relationList = new ArrayList<>();
        relationList.add("");
        relationList.add("dear");
        relationList.add("father");
        relationList.add("mother");
        relationList.add("son");
        relationList.add("daughter");
        relationList.add("friend");
        relationList.add("others");

        return relationList;
    }
}
