package com.nextapp.tuyatest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tuya.smart.android.base.TuyaSmartSdk;
import com.tuya.smart.android.device.TuyaSmartDevice;
import com.tuya.smart.android.device.bean.GwWrapperBean;
import com.tuya.smart.android.device.event.GwRelationEvent;
import com.tuya.smart.android.device.event.GwRelationUpdateEventModel;
import com.tuya.smart.android.device.event.GwUpdateEvent;
import com.tuya.smart.android.device.event.GwUpdateEventModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mikeshou on 15/12/8.
 */
public class DeviceListActivity extends Activity implements GwRelationEvent, GwUpdateEvent {

    public SwipeRefreshLayout swipeRefreshLayout;

    public ListView mDevListView;

    DeviceListAdapter mDevAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);

        TuyaSmartSdk.getEventBus().register(this);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mDevListView = (ListView) findViewById(R.id.lv_device_list);

        swipeRefreshLayout.setColorSchemeResources(R.color.google_blue,
                R.color.google_green,
                R.color.google_red,
                R.color.google_yellow);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                TuyaSmartDevice.getInstance().queryGwList();
            }
        });

        initAdapter();

        loadStart();

        TuyaSmartDevice.getInstance().queryGwList();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TuyaSmartSdk.getEventBus().unregister(this);
    }

    private void initAdapter() {
        mDevAdapter = new DeviceListAdapter(this, R.layout.list_device_item, new ArrayList<GwWrapperBean>());
        mDevListView.setAdapter(mDevAdapter);
        mDevListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GwWrapperBean gwWrapperBean = (GwWrapperBean) parent.getAdapter().getItem(position);
                if (gwWrapperBean != null) {
//                    mDeviceListFragmentPresenter.onDeviceClick(gwWrapperBean);

                    Intent intent = new Intent(DeviceListActivity.this, DevicePanelActivity.class);
                    intent.putExtra("gwId", gwWrapperBean.getGwId());
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

    public void updateData(List<GwWrapperBean> gwWrapperBeans) {
        if (mDevAdapter != null) {
            mDevAdapter.setData(gwWrapperBeans);
        }
        loadFinish();
    }

    @Override
    public void onEventMainThread(GwRelationUpdateEventModel event) {
        updateData(TuyaSmartDevice.getInstance().getGws());
    }

    @Override
    public void onEventMainThread(GwUpdateEventModel event) {
        updateData(TuyaSmartDevice.getInstance().getGws());
    }
}
