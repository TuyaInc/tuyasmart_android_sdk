package com.nextapp.tuyatest.fragment;

import android.support.v4.app.Fragment;

/**
 * Created by letian on 16/7/18.
 */
public class PersonalCenterFragment extends BaseFragment {

    private static PersonalCenterFragment mPersonalCenterFragment;

    public static synchronized Fragment newInstance() {
        if (mPersonalCenterFragment == null) {
            mPersonalCenterFragment = new PersonalCenterFragment();
        }
        return mPersonalCenterFragment;
    }

}
