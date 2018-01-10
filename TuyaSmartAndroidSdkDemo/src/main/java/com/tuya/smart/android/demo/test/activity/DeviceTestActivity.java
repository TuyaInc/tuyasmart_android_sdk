package com.tuya.smart.android.demo.test.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.widget.ScrollView;
import android.widget.TextView;

import com.tuya.smart.android.common.utils.L;
import com.tuya.smart.android.demo.R;
import com.tuya.smart.android.demo.activity.BaseActivity;
import com.tuya.smart.android.demo.test.presenter.DeviceTestPresenter;
import com.tuya.smart.android.demo.test.view.IDeviceTestView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by letian on 16/7/11.
 */
public class DeviceTestActivity extends BaseActivity implements IDeviceTestView {
    private static final String TAG = "DeviceTestActivity ggg";
    private DeviceTestPresenter mPresenter;
    @Bind(R.id.test_log)
    public TextView testLog;

    @Bind(R.id.test_scroll)
    public ScrollView testScroll;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_test);
        ButterKnife.bind(this);
        initToolbar();
        initTitle();
        initMenu();
        initPresenter();
    }


    protected void initTitle() {
        setTitle(getString(R.string.ty_dp_test));
        setDisplayHomeAsUpEnabled();
    }

    private void initMenu() {
        setMenu(R.menu.toolbar_top_dp_test, new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_edit_dp:
                        mPresenter.editTestValue();
                        break;
                    case R.id.action_setup:
                        mPresenter.gotoDeviceTestSetUpActivity();
                        break;
                }
                return false;
            }
        });
    }


    private void initPresenter() {
        mPresenter = new DeviceTestPresenter(this, this);
//        mTime.setText(String.valueOf(PreferencesUtil.getInt(DeviceTestPresenter.TIME_WAIT)));
    }

    protected void setDisplayHomeAsUpEnabled() {
        setDisplayHomeAsUpEnabled(R.drawable.tysmart_back_white, null);
    }

    @OnClick(R.id.btn_start)
    public void startTest() {
        testLog.setText("");
//        PreferencesUtil.set(DeviceTestPresenter.TIME_WAIT, Integer.valueOf(mTime.getText().toString()));
        mPresenter.startTest();
    }

    @OnClick(R.id.btn_cancel)
    public void stopTest() {
        mPresenter.stopTest();
    }


    public void scroll2Bottom() {
        // 内层高度超过外层
        int offset = testLog.getMeasuredHeight()
                - testScroll.getMeasuredHeight();
        if (offset < 0) {
            offset = 0;
        }
        testScroll.scrollTo(0, offset);
    }

    public void log(final String log) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                testLog.append(Html.fromHtml(log) + "\n");
                L.d(TAG, log);
                scroll2Bottom();
            }
        });
    }
}
