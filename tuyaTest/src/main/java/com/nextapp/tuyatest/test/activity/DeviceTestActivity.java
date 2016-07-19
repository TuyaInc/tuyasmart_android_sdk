package com.nextapp.tuyatest.test.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.widget.ScrollView;
import android.widget.TextView;

import com.tuya.smart.android.common.utils.L;
import com.nextapp.tuyatest.R;
import com.nextapp.tuyatest.test.presenter.DeviceTestPresenter;
import com.nextapp.tuyatest.test.view.IDeviceTestView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by letian on 16/7/11.
 */
public class DeviceTestActivity extends Activity implements IDeviceTestView {
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
        initPresenter();
    }

    private void initPresenter() {
        mPresenter = new DeviceTestPresenter(this, this);
    }

    @OnClick(R.id.btn_start)
    public void startTest() {
        testLog.setText("");
        mPresenter.startTest();
    }

    @OnClick(R.id.btn_cancel)
    public void stopTest() {
        mPresenter.stopTest();
    }

    @OnClick(R.id.btn_edit_test)
    public void editTestValue() {
        mPresenter.editTestValue();
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
