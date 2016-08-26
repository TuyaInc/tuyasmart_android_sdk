package com.tuya.smart.android.demo.fragment;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tuya.smart.android.demo.R;
import com.tuya.smart.android.demo.activity.SharedActivity;
import com.tuya.smart.android.demo.presenter.PersonalCenterFragmentPresenter;
import com.tuya.smart.android.demo.utils.ActivityUtils;
import com.tuya.smart.android.demo.view.IPersonalCenterView;

/**
 * Created by letian on 16/7/18.
 */
public class PersonalCenterFragment extends BaseFragment implements IPersonalCenterView {

    public Toolbar mToolBar;
    public TextView mUserName;
    public TextView mNickName;
    private View mContentView;

    protected PersonalCenterFragmentPresenter mPersonalCenterFragmentPresenter;
    private static PersonalCenterFragment mPersonalCenterFragment;

    public static synchronized Fragment newInstance() {
        if (mPersonalCenterFragment == null) {
            mPersonalCenterFragment = new PersonalCenterFragment();
        }
        return mPersonalCenterFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_personal_center, container, false);
        initToolbar(mContentView);
        initView();
        initMenu();
        initPresenter();
        return mContentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPersonalCenterFragmentPresenter.setPersonalInfo();
    }


    private void initView() {
        mToolBar = (Toolbar) mContentView.findViewById(R.id.toolbar_top_view);
        mUserName = (TextView) mContentView.findViewById(R.id.tv_username);
        mNickName = (TextView) mContentView.findViewById(R.id.tv_nickname);

        mContentView.findViewById(R.id.rl_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoShareActivity();
            }
        });
        mContentView.findViewById(R.id.rl_question).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoQuestionActivity();
            }
        });
        mContentView.findViewById(R.id.rl_edit_person).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPersonalCenterFragmentPresenter.gotoPersonalInfoActivity();
            }
        });
        TypedArray a = getActivity().obtainStyledAttributes(new int[]{
                R.attr.user_default_portrait});
        int portraitRes = a.getResourceId(0, -1);
        if (portraitRes != -1) {
            mContentView.findViewById(R.id.iv_head_photo).setBackgroundResource(portraitRes);
        }
        a.recycle();
    }

    private void gotoShareActivity() {
        ActivityUtils.gotoActivity(getActivity(), SharedActivity.class, ActivityUtils.ANIMATE_FORWARD, false);
    }

    private void gotoQuestionActivity() {

    }

    private void initPresenter() {
        mPersonalCenterFragmentPresenter = new PersonalCenterFragmentPresenter(getActivity(), this);
    }

    private void initMenu() {
        setTitle(getString(R.string.personal_center));
    }

    @Override
    public void setUserName(String userName) {
        if (TextUtils.isEmpty(userName)) {
            mUserName.setText(R.string.click_bind_phone);
        } else {
            mUserName.setText(userName);
        }
    }

    @Override
    public void setNickName(String nickName) {
        if (TextUtils.isEmpty(nickName)) {
            mNickName.setText(R.string.click_set_neekname);
        } else {
            mNickName.setText(nickName);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPersonalCenterFragmentPresenter.onDestroy();
    }


}
