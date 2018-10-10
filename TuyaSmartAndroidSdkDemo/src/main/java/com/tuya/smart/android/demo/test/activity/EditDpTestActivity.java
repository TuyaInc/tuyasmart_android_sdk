package com.tuya.smart.android.demo.test.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.widget.ScrollView;
import android.widget.TextView;

import com.tuya.smart.android.common.utils.L;
import com.tuya.smart.android.demo.R;
import com.tuya.smart.android.demo.test.presenter.EditDpTestPresenter;
import com.tuya.smart.android.demo.test.view.IEditDpTestView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by letian on 16/7/12.
 */
public class EditDpTestActivity extends Activity implements IEditDpTestView {
    public static final String TAG = "EditDpTestActivity ggg";
    private EditDpTestPresenter mPresenter;
    @BindView(R.id.test_log)
    public TextView testLog;

    @BindView(R.id.test_scroll)
    public ScrollView testScroll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_dp_test);
        ButterKnife.bind(this);
        initPresenter();
    }

    private void initPresenter() {
        mPresenter = new EditDpTestPresenter(this, this);
    }

    @OnClick(R.id.btn_add)
    public void btnAdd() {
        mPresenter.addDpValue();
    }

    @OnClick(R.id.btn_clear)
    public void clear() {
        mPresenter.clear();
    }

    @OnClick(R.id.btn_save)
    public void save() {
        mPresenter.save();
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

    @Override
    public void clearLog() {
        testLog.setText("");
    }
}
