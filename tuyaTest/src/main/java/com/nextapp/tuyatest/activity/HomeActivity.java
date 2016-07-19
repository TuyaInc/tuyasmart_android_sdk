package com.nextapp.tuyatest.activity;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nextapp.tuyatest.R;
import com.nextapp.tuyatest.presenter.HomePresenter;
import com.nextapp.tuyatest.view.IHomeView;
import com.tuya.smart.android.common.utils.L;
import com.tuya.smart.sdk.TuyaSdk;
import com.wnafee.vector.compat.VectorDrawable;

/**
 * Created by letian on 16/7/18.
 */
public class HomeActivity extends BaseActivity implements IHomeView {
    private static final String TAG = "HomeActivity";
    private static final int REQUEST_GESTURE_CHECK = 99;

    protected HomePresenter mHomePresenter;
    protected TextView mTvMyDevice;
    protected TextView mTvHomeCenter;
    private ImageView mIvMyDevice;
    private ImageView mIvHomeCenter;

    public ViewPager mFragmentContainer;
    private int mFuncBarTextNormalColor = Color.BLACK;
    private int mFuncBarTextSelectColor = Color.RED;
    private HomeFragmentAdapter mHomeFragmentAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化顺序不要随意变更
        initView();
        initPresenter();
        initTab();
        mHomePresenter.showTab(HomePresenter.TAB_MY_DEVICE);
        initViewPager();
    }

    private void initViewPager() {
        mFragmentContainer.setOffscreenPageLimit(2);
        mFragmentContainer.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int tab = postionToId(position);
                offItem(mHomePresenter.getCurrentTab());
                mHomePresenter.setCurrentTab(tab);
                onItem(tab);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    protected void initView() {
        setContentView(R.layout.activity_home);
        mTvMyDevice = (TextView) findViewById(R.id.tv_home_my_device);
        mTvMyDevice.setOnClickListener(mClickListener);
        mIvMyDevice = (ImageView) findViewById(R.id.iv_my_device);
        mIvMyDevice.setImageDrawable(VectorDrawable.getDrawable(TuyaSdk.getApplication(), R.drawable.ty_mydevice));
        mIvMyDevice.setOnClickListener(mClickListener);
        mIvMyDevice.setColorFilter(mFuncBarTextSelectColor);

        mTvHomeCenter = (TextView) findViewById(R.id.tv_home_center);
        mTvHomeCenter.setOnClickListener(mClickListener);
        mIvHomeCenter = (ImageView) findViewById(R.id.iv_home_center);
        mIvHomeCenter.setImageDrawable(VectorDrawable.getDrawable(TuyaSdk.getApplication(), R.drawable.ty_home_center));
        mIvHomeCenter.setOnClickListener(mClickListener);
        mIvHomeCenter.setColorFilter(mFuncBarTextNormalColor);

    }

    protected View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.tv_home_my_device || v.getId() == R.id.iv_my_device) {
                mHomePresenter.showMyDevicePage();
            } else if (v.getId() == R.id.tv_home_center || v.getId() == R.id.iv_home_center) {
                mHomePresenter.showPersonalCenterPage();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHomePresenter != null) {
            mHomePresenter.onDestroy();
        }
    }


    protected void initTab() {
        mFragmentContainer = (ViewPager) findViewById(R.id.home_fragment_container);
        mHomeFragmentAdapter = new HomeFragmentAdapter(getSupportFragmentManager());
        mFragmentContainer.setAdapter(mHomeFragmentAdapter);
    }

    private void initPresenter() {
        mHomePresenter = new HomePresenter(this, this);
    }

    // 我的设备
    public void showMyDevicePage() {
        mHomePresenter.showMyDevicePage();
    }

    @Override
    public boolean onPanelKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                exitBy2Click();
                return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_GESTURE_CHECK) {
            if (resultCode == RESULT_CANCELED) {
                finish();
            }
        }
    }

    @Override
    public boolean isContainFragment() {
        return true;
    }

    @Override
    public void onItem(int id) {
        switch (id) {
            case HomePresenter.TAB_MY_DEVICE:
                mTvMyDevice.setTextColor(mFuncBarTextSelectColor);
                mIvMyDevice.setColorFilter(mFuncBarTextSelectColor);
                mIvMyDevice.setImageDrawable(VectorDrawable.getDrawable(TuyaSdk.getApplication(), R.drawable.ty_mydevice_selected));
                mFragmentContainer.setCurrentItem(idToPosition(id), true);
                break;
            case HomePresenter.TAB_PERSONAL_CENTER:
                mTvHomeCenter.setTextColor(mFuncBarTextSelectColor);
                mIvHomeCenter.setColorFilter(mFuncBarTextSelectColor);
                mIvHomeCenter.setImageDrawable(VectorDrawable.getDrawable(TuyaSdk.getApplication(), R.drawable.ty_home_center_selected));
                mFragmentContainer.setCurrentItem(idToPosition(id), true);
                break;
        }
    }

    @Override
    public void offItem(int id) {
        switch (id) {
            case HomePresenter.TAB_MY_DEVICE:
                mTvMyDevice.setTextColor(mFuncBarTextNormalColor);
                mIvMyDevice.setColorFilter(mFuncBarTextNormalColor);
                mIvMyDevice.setImageDrawable(VectorDrawable.getDrawable(TuyaSdk.getApplication(), R.drawable.ty_mydevice));
                break;
            case HomePresenter.TAB_PERSONAL_CENTER:
                mTvHomeCenter.setTextColor(mFuncBarTextNormalColor);
                mIvHomeCenter.setColorFilter(mFuncBarTextNormalColor);
                mIvHomeCenter.setImageDrawable(VectorDrawable.getDrawable(TuyaSdk.getApplication(), R.drawable.ty_home_center));
                break;
        }
    }


    private class HomeFragmentAdapter extends FragmentPagerAdapter {

        public HomeFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            int id = postionToId(position);
            return mHomePresenter.getFragment(id);
        }

        @Override
        public int getCount() {
            return mHomePresenter.getFragmentCount();
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }


    protected int postionToId(int pos) {
        switch (pos) {
            case 0:
                return HomePresenter.TAB_MY_DEVICE;
            case 1:
                return HomePresenter.TAB_PERSONAL_CENTER;

        }

        return HomePresenter.TAB_MY_DEVICE;
    }

    protected int idToPosition(int id) {

        switch (id) {
            case HomePresenter.TAB_MY_DEVICE:
                return 0;
            case HomePresenter.TAB_PERSONAL_CENTER:
                return 1;

        }
        return 0;
    }
}
