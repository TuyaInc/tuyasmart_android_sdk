package com.nextapp.tuyatest.fragment;


import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.nextapp.tuyatest.R;
import com.nextapp.tuyatest.adapter.CommonDeviceAdapter;
import com.nextapp.tuyatest.presenter.DeviceListFragmentPresenter;
import com.nextapp.tuyatest.view.IDeviceListFragmentView;
import com.tuya.smart.android.common.utils.NetworkUtil;
import com.tuya.smart.android.device.bean.GwWrapperBean;
import com.tuya.smart.sdk.bean.DeviceBean;
import com.tuya.smart.sdk.bean.GroupBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by letian on 16/7/18.
 */
public class DeviceListFragment extends BaseFragment implements IDeviceListFragmentView {

    private static DeviceListFragment mDeviceListFragment;
    private View mContentView;
    private DeviceListFragmentPresenter mDeviceListFragmentPresenter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private CommonDeviceAdapter mCommonDeviceAdapter;
    private ListView mDevListView;
    private TextView mNetWorkTip;

    public static synchronized Fragment newInstance() {
        if (mDeviceListFragment == null) {
            mDeviceListFragment = new DeviceListFragment();
        }
        return mDeviceListFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_device_list, container, false);
        initToolbar(mContentView);
        initMenu();
        initView();
        initAdapter();
        initSwipeRefreshLayout();
        return mContentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initPresenter();
    }

    private void initSwipeRefreshLayout() {
        TypedArray a = getActivity().obtainStyledAttributes(new int[]{R.attr.navbar_font_color});
        int color = a.getColor(0, Color.TRANSPARENT);
        a.recycle();

        if (Color.TRANSPARENT == color) {
            mSwipeRefreshLayout.setColorSchemeResources(
                    R.color.color_primary);
        } else {
            mSwipeRefreshLayout.setColorSchemeColors(color);
        }
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                if (NetworkUtil.isNetworkAvailable(getContext())) {
                    mDeviceListFragmentPresenter.queryDevListFromServer();
                } else {
                    loadFinish();
                }
            }
        });
    }

    private void initAdapter() {
        mCommonDeviceAdapter = getCommonDeviceAdapter();
        mDevListView.setAdapter(mCommonDeviceAdapter);
        mDevListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return mDeviceListFragmentPresenter.onDeviceLongClick(parent.getAdapter().getItem(position));
            }
        });
        mDevListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onItemClickDeal(parent, position);
            }
        });

    }

    protected CommonDeviceAdapter getCommonDeviceAdapter() {
        return new CommonDeviceAdapter(getActivity(), R.layout.list_common_device_item, new ArrayList<DeviceBean>());
    }

    protected void onItemClickDeal(AdapterView<?> parent, int position) {
        Object item = parent.getAdapter().getItem(position);
        if (item == null) return;
        mDeviceListFragmentPresenter.onDeviceClick(item);
    }

    @Override
    public void updateDeviceData(List<DeviceBean> myDevices) {
        if (mCommonDeviceAdapter != null) {
            mCommonDeviceAdapter.setData(myDevices);
            mCommonDeviceAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void loadStart() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    protected void initView() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) mContentView.findViewById(R.id.swipe_container);
        mNetWorkTip = (TextView) mContentView.findViewById(R.id.network_tip);
        mDevListView = (ListView) mContentView.findViewById(R.id.lv_device_list);
    }

    protected void initPresenter() {
        mDeviceListFragmentPresenter = new DeviceListFragmentPresenter(this, this);
    }


    protected void initMenu() {
        setTitle(getString(R.string.home_my_device));
        setMenu(R.menu.toolbar_add_device, new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_add_device) {
                    mDeviceListFragmentPresenter.addDevice();
                }
                return false;
            }
        });
    }

    @Override
    public void loadFinish() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    public void showNetWorkTipView(int tipRes) {
        mNetWorkTip.setText(tipRes);
        mNetWorkTip.setVisibility(View.VISIBLE);
    }

    public void hideNetWorkTipView() {
        mNetWorkTip.setVisibility(View.GONE);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mDeviceListFragmentPresenter.onDestroy();
    }

}
