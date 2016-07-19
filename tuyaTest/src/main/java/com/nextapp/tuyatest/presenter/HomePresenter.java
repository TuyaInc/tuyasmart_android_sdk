package com.nextapp.tuyatest.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.net.wifi.WifiManager;
import android.support.v4.app.Fragment;

import com.nextapp.tuyatest.R;
import com.nextapp.tuyatest.activity.AddDeviceTipActivity;
import com.nextapp.tuyatest.fragment.DeviceListFragment;
import com.nextapp.tuyatest.fragment.PersonalCenterFragment;
import com.nextapp.tuyatest.test.utils.DialogUtil;
import com.nextapp.tuyatest.utils.ActivityUtils;
import com.nextapp.tuyatest.view.IHomeView;
import com.tuya.smart.android.mvp.presenter.BasePresenter;

/**
 * Created by letian on 16/7/18.
 */
public class HomePresenter extends BasePresenter {

    public static final String TAG = "HomePresenter";

    public static final String TAB_FRGMENT = "TAB_FRGMENT";

    private IHomeView mHomeView;
    protected Activity mActivity;

    public static final int TAB_MY_DEVICE = 0;
    public static final int TAB_ADD_DEVICE = 1;
    public static final int TAB_PERSONAL_CENTER = 2;
    public static final int TAB_SCENE = 3;

    protected int mCurrentTab = -1;

    public HomePresenter(IHomeView homeView, Activity ctx) {
        mHomeView = homeView;
        mActivity = ctx;
    }


    //添加设备
    public void addDevice() {
        final WifiManager mWifiManager = (WifiManager) mActivity.getSystemService(Context.WIFI_SERVICE);
        if (!mWifiManager.isWifiEnabled()) {
            DialogUtil.simpleConfirmDialog(mActivity, mActivity.getString(R.string.open_wifi), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            mWifiManager.setWifiEnabled(true);
                            gotoAddDevice();
                            break;
                    }
                }
            });
        } else {
            gotoAddDevice();
        }
    }

    //个人中心
    public void showPersonalCenterPage() {
        showTab(TAB_PERSONAL_CENTER);
    }

    public void showScene() {
        showTab(TAB_SCENE);
    }

    //我的设备
    public void showMyDevicePage() {
        showTab(TAB_MY_DEVICE);
    }

    public void gotoAddDevice() {
        ActivityUtils.gotoActivity(mActivity, AddDeviceTipActivity.class, ActivityUtils.ANIMATE_SLIDE_TOP_FROM_BOTTOM, false);
    }

    public void showTab(int tab) {
        if (tab == mCurrentTab) {
            return;
        }

        mHomeView.offItem(mCurrentTab);

        mHomeView.onItem(tab);

        mCurrentTab = tab;
    }

    public int getFragmentCount() {
        return 3;
    }

    public Fragment getFragment(int type) {
        if (type == TAB_MY_DEVICE) {
            return DeviceListFragment.newInstance();
        } else if (type == TAB_PERSONAL_CENTER) {
            return PersonalCenterFragment.newInstance();
        }
        return null;
    }
}
