package com.tuya.smart.android.demo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.tuya.smart.android.demo.R;
import com.tuya.smart.android.demo.adapter.GroupDeviceEditAdapter;
import com.tuya.smart.android.demo.presenter.GroupDeviceListPresenter;
import com.tuya.smart.android.demo.utils.WidgetUtils;
import com.tuya.smart.android.demo.view.IGroupDeviceListView;
import com.tuya.smart.sdk.bean.GroupDeviceBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 群组的设备编辑界面
 * Created by chenshixin on 15/12/11.
 */
public class GroupEditDeviceActivity extends BaseActivity implements IGroupDeviceListView {

    public static final String EXTRA_PRODUCT_ID = "extra_product_id";
    public static final String EXTRA_GROUP_ID = "extra_group_id";
    public static final String EXTRA_DEV_ID = "extra_dev_id";

    private static final String TAG = "GroupDeviceListActivity";

    public SwipeRefreshLayout mSwipeRefreshLayout;
    public ListView mDeviceListView;
    public RelativeLayout mEmptyList;

    private GroupDeviceEditAdapter mGroupDeviceAdapter;

    private GroupDeviceListPresenter mGroupDeviceListPresenter;

    /**
     * 启动添加群组设备列表界面
     *
     * @param devId 默认选中的设备
     */
    public static void startAdd(Context context, String productId, String devId) {
        Intent intent = new Intent(context, GroupEditDeviceActivity.class);
        intent.putExtra(EXTRA_PRODUCT_ID, productId);
        intent.putExtra(EXTRA_DEV_ID, devId);
        context.startActivity(intent);
    }

    /**
     * 进去修改群组设备页面
     */
    public static void startEdit(Context context, long groupId) {
        Intent intent = new Intent(context, GroupEditDeviceActivity.class);
        intent.putExtra(EXTRA_GROUP_ID, groupId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_edit);
        initToolbar();
        initView();
        initPresenter();
        initAdapter();
        initSwipeRefreshLayout();
    }

    @Override
    public void loadStart() {
        mSwipeRefreshLayout.post(new Runnable() {

            @Override
            public void run() {
                if (mSwipeRefreshLayout != null) {
                    mSwipeRefreshLayout.setRefreshing(true);
                }
            }
        });
    }

    public void initToolbar() {
        super.initToolbar();
        setDisplayHomeAsUpEnabled();
        setTitle(getString(R.string.group_title_select_device));
        setMenu(R.menu.toolbar_top_back_done, new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return mGroupDeviceListPresenter.onMenuItemClick(item.getItemId());
            }
        });
    }

    private void initView() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.srl_group_edit_container);
        mDeviceListView = (ListView) findViewById(R.id.lv_group_device_list);
        mEmptyList = (RelativeLayout) findViewById(R.id.list_background_tip);
        mEmptyList.setVisibility(View.GONE);
        WidgetUtils.checkNoneContentLayout(this, mPanelTopView, getString(R.string.group_group_list_empty));
    }

    private void initPresenter() {
        mGroupDeviceListPresenter = new GroupDeviceListPresenter(this, this);
    }

    private void initAdapter() {
        mGroupDeviceAdapter = new GroupDeviceEditAdapter(GroupEditDeviceActivity.this, R.layout.list_group_device_item, new ArrayList<GroupDeviceBean>());
        mDeviceListView.setAdapter(mGroupDeviceAdapter);
        mDeviceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GroupDeviceBean groupDevice = (GroupDeviceBean) parent.getAdapter().getItem(position);
                mGroupDeviceListPresenter.onDeviceClick(groupDevice);
            }
        });
    }

    private void initSwipeRefreshLayout() {
        mSwipeRefreshLayout.setColorSchemeResources(
                R.color.tuya_red);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                mGroupDeviceListPresenter.getData();
            }
        });
    }


    @Override
    public void loadFinish() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void updateDeviceList(List<GroupDeviceBean> deviceBeanList) {
        if (deviceBeanList != null && deviceBeanList.size() > 0) {
            mGroupDeviceAdapter.setData(deviceBeanList);
            mEmptyList.setVisibility(View.GONE);
            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
        } else {
            mEmptyList.setVisibility(View.VISIBLE);
            mSwipeRefreshLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void refreshList() {
        if (mGroupDeviceAdapter != null) {
            mGroupDeviceAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public List<GroupDeviceBean> getDeviceList() {
        if (mGroupDeviceAdapter == null) {
            return new ArrayList<>();
        } else {
            return mGroupDeviceAdapter.getData();
        }
    }

    @Override
    public void finishActivity() {
        onBackPressed();
    }


}
