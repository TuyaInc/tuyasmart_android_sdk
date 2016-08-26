package com.nextapp.tuyatest.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.nextapp.tuyatest.R;
import com.nextapp.tuyatest.presenter.SwitchPresenter;
import com.nextapp.tuyatest.utils.CommonUtil;
import com.nextapp.tuyatest.utils.ViewUtils;
import com.nextapp.tuyatest.view.ISwitchView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by letian on 16/7/21.
 */
public class SwitchActivity extends BaseActivity implements ISwitchView {

    @Bind(R.id.iv_switch)
    public ImageView mSwitchButton;

    @Bind(R.id.rl_switch_bg)
    public View mBgView;

    @Bind(R.id.v_title_down_line)
    public View mLine;

    private SwitchPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switch);
        ButterKnife.bind(this);
        initToolbar();
        initPresenter();
        initTitle();
        initView();
    }

    private void initView() {
        showCloseView();
        setViewGone(mLine);
    }

    private void initTitle() {
        setTitle("插座演示");
        mToolBar.setTitleTextColor(Color.WHITE);
    }

    private void initPresenter() {
        mPresenter = new SwitchPresenter(this, this);
    }

    @Override
    public void showOpenView() {
        mSwitchButton.setImageResource(R.drawable.switch_on);
        mBgView.setBackgroundColor(ViewUtils.getColor(this, R.color.switch_on_color));
        mToolBar.setBackgroundColor(ViewUtils.getColor(this, R.color.switch_on_color));
        CommonUtil.initSystemBarColor(this, ViewUtils.getColor(this, R.color.switch_on_color));
    }

    @Override
    public void showCloseView() {
        mSwitchButton.setImageResource(R.drawable.switch_off);
        mBgView.setBackgroundColor(ViewUtils.getColor(this, R.color.switch_off_color));
        mToolBar.setBackgroundColor(ViewUtils.getColor(this, R.color.switch_off_color));
        CommonUtil.initSystemBarColor(this, ViewUtils.getColor(this, R.color.switch_off_color));
    }

    @Override
    public void showErrorTip() {

    }

    @Override
    public void showRemoveTip() {

    }

    @Override
    public void changeNetworkErrorTip(boolean status) {

    }

    @Override
    public void statusChangedTip(boolean status) {

    }

    @Override
    public void devInfoUpdateView() {

    }

    @OnClick(R.id.iv_switch)
    public void onClickSwitch() {
        mPresenter.onClickSwitch();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
