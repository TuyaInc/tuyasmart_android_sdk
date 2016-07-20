package com.nextapp.tuyatest.view;


import com.tuya.smart.android.mvp.bean.Result;
import com.tuya.smart.android.mvp.view.IView;

/**
 * Created by letian on 15/6/22.
 */
public interface IPersonalInfoView extends IView {

    void reNickName(String nickName);

    void onLogout(Result result);
}
