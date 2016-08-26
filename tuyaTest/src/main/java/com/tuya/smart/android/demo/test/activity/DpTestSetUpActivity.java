package com.tuya.smart.android.demo.test.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.kyleduo.switchbutton.SwitchButton;
import com.tuya.smart.android.demo.R;
import com.tuya.smart.android.demo.activity.BaseActivity;
import com.tuya.smart.android.demo.test.bean.DpTestSetUpBean;
import com.tuya.smart.android.demo.test.presenter.DpTestSetUpPresenter;
import com.tuya.smart.android.demo.test.view.IDpTestSetUpView;
import com.tuya.smart.android.demo.utils.ToastUtil;

import butterknife.Bind;

/**
 * Created by letian on 16/8/6.
 */
public class DpTestSetUpActivity extends BaseActivity implements IDpTestSetUpView, CompoundButton.OnCheckedChangeListener {

    public static final String DP_TEST_SETUP_VALUE = "dp_test_setup_value";
    private DpTestSetUpPresenter mPresenter;

    @Bind(R.id.sb_show_log_window)
    public SwitchButton mShowLogSBView;
    @Bind(R.id.sb_needpool)
    public SwitchButton mNeedPoolSBView;
    @Bind(R.id.sb_show_report)
    public SwitchButton mShowTestReportSBView;
    @Bind(R.id.sb_lan_send)
    public SwitchButton mLanSendSBView;
    @Bind(R.id.sb_cloud_send)
    public SwitchButton mCloudSendSBView;
    private DpTestSetUpBean mBean;
    @Bind(R.id.et_time_interval)
    public EditText mTimeInterval;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dp_test_set_up);
        initToolbar();
        initTitle();
        initMenu();
        initPresenter();
        initListener();
        initView();
    }

    private void initView() {
        mBean = mPresenter.getTestSetUpData();
        mTimeInterval.setText(mBean.getTimeInterval());
        mShowLogSBView.setChecked(mBean.isShowLogWindow());
        mShowTestReportSBView.setChecked(mBean.isShowTestReport());
        mNeedPoolSBView.setChecked(mBean.isNeedPool());
        mLanSendSBView.setChecked(mBean.isSendByLan());
        mCloudSendSBView.setChecked(mBean.isSendByCloud());
    }

    private void initListener() {
        mShowLogSBView.setOnCheckedChangeListener(this);
    }

    private void initTitle() {
        setTitle(R.string.ty_dp_test_title);
    }

    private void initMenu() {
        setDisplayHomeAsUpEnabled();
        setMenu(R.menu.toolbar_top_dp_test, new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                mPresenter.saveSetUp();
                return false;
            }
        });
    }


    public void initPresenter() {
        mPresenter = new DpTestSetUpPresenter(this, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.sb_show_log_window:
                break;
            case R.id.sb_needpool:
                break;
            case R.id.sb_show_report:
                break;
            case R.id.sb_lan_send:
                if (!isChecked && !mCloudSendSBView.isChecked()) {
                    ToastUtil.showToast(DpTestSetUpActivity.this, R.string.cannot_set_no_cloud_and_no_lan);
                    ((SwitchButton) buttonView).setCheckedNoEvent(true);
                }
                break;
            case R.id.sb_cloud_send:
                if (!isChecked && !mLanSendSBView.isChecked()) {
                    ToastUtil.showToast(DpTestSetUpActivity.this, R.string.cannot_set_no_cloud_and_no_lan);
                    ((SwitchButton) buttonView).setCheckedNoEvent(true);
                }
                break;
        }
    }

    @Override
    public DpTestSetUpBean getSetData() {
        DpTestSetUpBean bean = new DpTestSetUpBean();
        String timeInter = mTimeInterval.getText().toString();
        if (TextUtils.isEmpty(timeInter)) return null;
        else {
            try {
                Integer integer = Integer.valueOf(timeInter);
                if (integer < 0) return null;
                bean.setTimeInterval(integer);
            } catch (Exception e) {
                return null;
            }
        }
        bean.setNeedPool(mNeedPoolSBView.isChecked());
        bean.setSendByCloud(mCloudSendSBView.isChecked());
        bean.setShowLogWindow(mShowLogSBView.isChecked());
        bean.setShowTestReport(mShowTestReportSBView.isChecked());
        bean.setSendByLan(mLanSendSBView.isChecked());
        return bean;
    }
}
