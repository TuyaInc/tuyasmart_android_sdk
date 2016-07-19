package com.nextapp.tuyatest.model;


import com.tuya.smart.android.mvp.model.IModel;

/**
 * Created by letian on 15/6/19.
 */
public interface IPersonalCenterModel extends IModel {

    /**
     * 获取昵称
     *
     * @return
     */
    String getNickName();

    /**
     * 获取用户名
     *
     * @return
     */
    String getUserName();

}
