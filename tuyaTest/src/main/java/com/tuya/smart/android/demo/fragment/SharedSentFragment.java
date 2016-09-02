package com.tuya.smart.android.demo.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tuya.smart.android.demo.R;
import com.tuya.smart.android.demo.adapter.SharedSentAdapter;
import com.tuya.smart.android.demo.presenter.SharedSentPresenter;
import com.tuya.smart.android.demo.utils.WidgetUtils;
import com.tuya.smart.android.demo.view.ISharedSentView;
import com.tuya.smart.android.user.bean.PersonBean;

import java.util.ArrayList;


/**
 * Created by leaf on 15/12/21.
 * 发出的共享
 */
public class SharedSentFragment extends BaseFragment implements ISharedSentView, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "SharedSentFragment";

    /**
     * 是否第一次进入
     */
    private boolean mIsFirstIn;

    public SwipeRefreshLayout mSwipeContainer;
    public ListView mSentList;

    View mHasShared;
    View mAddShare;

    private ArrayList<PersonBean> mSentMembers;

    private SharedSentAdapter mAdapter;

    private SharedSentPresenter mPresenter;

    private View mContentView;

    protected static SharedSentFragment mSharedSentFragment;

    public static synchronized Fragment newInstance() {
        if (mSharedSentFragment == null) {
            mSharedSentFragment = new SharedSentFragment();
        }
        return mSharedSentFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = inflater.inflate(getLayoutResId(), container, false);
        initToolbar(mContentView);
        initData();
        initMenu();
        initView();
        initAdapter();
        initSwipeRefreshLayout();
        return mContentView;
    }

    protected int getLayoutResId() {
        return R.layout.fragment_shared_sent;
    }

    private void initData() {
        mIsFirstIn = true;
        mSentMembers = new ArrayList<>();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initPresenter();
    }

    private void initSwipeRefreshLayout() {
        mSwipeContainer.setOnRefreshListener(this);
        mSwipeContainer.setColorSchemeResources(android.R.color.holo_red_light, android.R.color.holo_green_light,
                android.R.color.holo_blue_light, android.R.color.holo_orange_light);
    }

    private void initAdapter() {
        mAdapter = new SharedSentAdapter(getActivity());
        mAdapter.setData(mSentMembers);
        mSentList.setAdapter(mAdapter);
    }

    private void initPresenter() {
        mPresenter = new SharedSentPresenter(getActivity(), this);
    }

    private View.OnClickListener mViewAddShareClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onAddShareClick();
        }
    };

    private void initView() {
        mSwipeContainer = (SwipeRefreshLayout) mContentView.findViewById(R.id.swipe_container);
        mSentList = (ListView) mContentView.findViewById(R.id.list);
        mHasShared = mContentView.findViewById(R.id.has_shared);
        mContentView.findViewById(R.id.tv_empty_func).setOnClickListener(mViewAddShareClickListener);
        mAddShare = mContentView.findViewById(R.id.add_button);
        mAddShare.setOnClickListener(mViewAddShareClickListener);

        mSentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mPresenter.gotoEditFriendActivity(mSentMembers.get(position), position);
            }
        });

        mSentList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                mPresenter.onFriendLongClick(mSentMembers.get(position));
                return true;
            }
        });

        WidgetUtils.checkNoneContentLayout(getActivity(), mContentView,-1,getString(R.string.no_share),true,-1,R.string.new_share);
    }

    protected void onAddShareClick() {
        mPresenter.gotoAddNewPersonActivity();
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
    }

    @Override
    public void updateList(ArrayList<PersonBean> members) {
        mSentMembers.clear();
        mSentMembers.addAll(members);
        mAdapter.notifyDataSetChanged();
        reloadBaseView();
    }

    @Override
    public void updateList(PersonBean member) {
        mSentMembers.add(0, member);
        mAdapter.notifyDataSetChanged();
        reloadBaseView();
    }

    @Override
    public void updateList(PersonBean member, int position) {
        mSentMembers.get(position).setName(member.getName());
        mAdapter.notifyDataSetChanged();
        reloadBaseView();
    }

    @Override
    public void reloadBaseView() {
        if(mSentMembers != null && !mSentMembers.isEmpty()){
            mContentView.findViewById(R.id.no_shared).setVisibility(View.GONE);
            mAddShare.setVisibility(View.VISIBLE);
            mHasShared.setVisibility(View.VISIBLE);
        }else{
            mContentView.findViewById(R.id.no_shared).setVisibility(View.VISIBLE);
            mAddShare.setVisibility(View.GONE);
            mHasShared.setVisibility(View.GONE);
        }

        WidgetUtils.fixListViewHeight(mSentList);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }
}
