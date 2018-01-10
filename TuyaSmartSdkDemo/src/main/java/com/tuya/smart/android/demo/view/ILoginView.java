package com.tuya.smart.android.demo.view;

import com.tuya.smart.android.mvp.bean.Result;

/**
 * Created by sunch on 16/6/3.
*/
public interface ILoginView
{
    // 设置国家/地区信息
    void setCountry(String name, String code);

    // 返回数据结果
    void modelResult(int what, Result result);

}
