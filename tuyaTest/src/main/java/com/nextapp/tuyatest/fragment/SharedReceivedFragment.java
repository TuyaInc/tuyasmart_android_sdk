package com.nextapp.tuyatest.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.nextapp.tuyatest.R;
import com.nextapp.tuyatest.adapter.SharedReceivedAdapter;
import com.nextapp.tuyatest.presenter.SharedReceivedPresenter;
import com.nextapp.tuyatest.utils.ProgressUtil;
import com.nextapp.tuyatest.utils.WidgetUtils;
import com.nextapp.tuyatest.view.ISharedReceivedView;
import com.tuya.smart.android.user.bean.GroupReceivedMemberBean;
import com.tuya.smart.android.user.bean.PersonBean;

import java.util.ArrayList;

/**
 * Created by leaf on 15/12/21.
 * 收到的共享
 */
public class SharedReceivedFragment extends BaseFragment implements ISharedReceivedView, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "SharedSentFragment";

    /**
     * 是否第一次进入
     */
    private boolean mIsFirstIn;

    public SwipeRefreshLayout mSwipeContainer;
    public ListView mReceivedList;
    TextView mNoShared;
    View mHasShared;

    private SharedReceivedAdapter mAdapter;

    private SharedReceivedPresenter mPresenter;

    private View mContentView;

    private static SharedReceivedFragment mSharedReceivedFragment;

    public static synchronized Fragment newInstance() {
        if (mSharedReceivedFragment == null) {
            mSharedReceivedFragment = new SharedReceivedFragment();
        }
        return mSharedReceivedFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_shared_received, container, false);
        initToolbar(mContentView);
        initData();
        initMenu();
        initView();
        initAdapter();
        initSwipeRefreshLayout();
        initNoListText();
        return mContentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initPresenter();
    }

    private void initData() {
        mIsFirstIn = true;
    }

    private void initSwipeRefreshLayout() {
        mSwipeContainer.setOnRefreshListener(this);
        mSwipeContainer.setColorSchemeResources(android.R.color.holo_red_light, android.R.color.holo_green_light,
                android.R.color.holo_blue_light, android.R.color.holo_orange_light);
    }

    private void initAdapter() {
        mAdapter = new SharedReceivedAdapter(getActivity());
        mReceivedList.setAdapter(mAdapter);
    }

    private void initPresenter() {
        mPresenter = new SharedReceivedPresenter(getActivity(), this);
    }

    private void initView() {
        mSwipeContainer = (SwipeRefreshLayout) mContentView.findViewById(R.id.swipe_container);
        mReceivedList = (ListView) mContentView.findViewById(R.id.list);
        mReceivedList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GroupReceivedMemberBean bean = mAdapter.getItem(position);
                mPresenter.gotoSharedEditReceivedMemberActivity(bean.getDevice(),
                        bean.getUser(), position);
            }
        });
        mReceivedList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                mPresenter.onFriendLongClick(mAdapter.getItem(position).getUser());
                return true;
            }
        });
        mNoShared = (TextView) mContentView.findViewById(R.id.tv_none_content);
        mHasShared = mContentView.findViewById(R.id.has_shared);
        WidgetUtils.checkNoneContentLayout(getActivity(), mContentView, getString(R.string.ty_share_empty_device));
    }

    private void initNoListText() {
        mNoShared.setText(R.string.ty_share_empty_device);
    }

    private void initMenu() {
        setTitle(getString(R.string.my_smart_home));
    }


    @Override
    public void onResume() {
        if (mIsFirstIn) {
            mIsFirstIn = false;
            loadStart();
            mPresenter.list();
        }

        super.onResume();
    }

    public void loadStart() {
        mSwipeContainer.post(new Runnable() {
            @Override
            public void run() {
                if (mSwipeContainer != null) {
                    mSwipeContainer.setRefreshing(true);
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onRefresh() {
        mPresenter.list();
    }

    @Override
    public void finishLoad() {
        mSwipeContainer.setRefreshing(false);
        ProgressUtil.hideLoading();
    }

    @Override
    public void updateList(ArrayList<GroupReceivedMemberBean> members) {
        mAdapter.setData(members);
        reloadBaseView();
    }

    @Override
    public void updateList(PersonBean person, int position) {
        GroupReceivedMemberBean memberBean = mAdapter.getItem(position);
        memberBean.setUser(person);
        mAdapter.notifyDataSetChanged();
        reloadBaseView();
    }

    @Override
    public void reloadBaseView() {
        int count = mAdapter.getCount();
        int noSharedVisible = 0 >= count ? View.VISIBLE : View.GONE;
        mNoShared.setVisibility(noSharedVisible);
        mContentView.findViewById(R.id.iv_none_data).setVisibility(noSharedVisible);
        mHasShared.setVisibility(0 < count ? View.VISIBLE : View.GONE);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }
}
