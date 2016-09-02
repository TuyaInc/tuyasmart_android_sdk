package com.tuya.smart.android.demo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tuya.smart.android.demo.*;
import com.tuya.smart.android.demo.adapter.GroupDeviceAdapter;
import com.tuya.smart.android.demo.presenter.GroupCommonPresenter;
import com.tuya.smart.android.device.event.GwRelationEvent;
import com.tuya.smart.android.device.event.GwRelationUpdateEventModel;
import com.tuya.smart.android.device.event.GwUpdateEvent;
import com.tuya.smart.android.device.event.GwUpdateEventModel;
import com.tuya.smart.sdk.TuyaSdk;
import com.tuya.smart.sdk.TuyaUser;
import com.tuya.smart.sdk.bean.GroupBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by letian on 16/8/27.
 */
public class GroupListActivity extends BaseActivity implements GwRelationEvent, GwUpdateEvent {


    public SwipeRefreshLayout swipeRefreshLayout;

    public ListView mDevListView;

    GroupDeviceAdapter mGroupDeviceAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);

        initToolbar();
        setTitle(R.string.group_device);
        setDisplayHomeAsUpEnabled();
        TuyaSdk.getEventBus().register(this);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mDevListView = (ListView) findViewById(R.id.lv_device_list);

        swipeRefreshLayout.setColorSchemeResources(R.color.google_blue,
                R.color.google_green,
                R.color.google_red,
                R.color.google_yellow);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                TuyaUser.getDeviceInstance().queryDevList();
            }
        });

        initAdapter();

        loadStart();

        TuyaUser.getDeviceInstance().queryDevList();


    }

    protected void setDisplayHomeAsUpEnabled() {
        setDisplayHomeAsUpEnabled(R.drawable.tysmart_back_white, null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TuyaSdk.getEventBus().unregister(this);
    }

    private void initAdapter() {
        mGroupDeviceAdapter = new GroupDeviceAdapter(this, R.layout.list_common_device_item, new ArrayList<GroupBean>());
        mDevListView.setAdapter(mGroupDeviceAdapter);
        mDevListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GroupBean groupBean = (GroupBean) parent.getAdapter().getItem(position);
                if (groupBean != null) {
                    Intent intent = new Intent(GroupListActivity.this, GroupActivity.class);
                    intent.putExtra(GroupCommonPresenter.INTENT_GROUP_ID, groupBean.getId());

                    startActivity(intent);
                }
            }
        });

    }

    public void loadStart() {
        swipeRefreshLayout.post(new Runnable() {

            @Override
            public void run() {
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.setRefreshing(true);
                }
            }
        });
    }

    public void loadFinish() {
        swipeRefreshLayout.setRefreshing(false);
    }

    public void updateData(List<GroupBean> groupBeen) {
        if (mGroupDeviceAdapter != null) {
            mGroupDeviceAdapter.setData(groupBeen);
        }
        loadFinish();
    }

    @Override
    public void onEventMainThread(GwRelationUpdateEventModel event) {
        updateData(TuyaUser.getDeviceInstance().getGroupList());
    }

    @Override
    public void onEventMainThread(GwUpdateEventModel event) {
        updateData(TuyaUser.getDeviceInstance().getGroupList());
    }
}
