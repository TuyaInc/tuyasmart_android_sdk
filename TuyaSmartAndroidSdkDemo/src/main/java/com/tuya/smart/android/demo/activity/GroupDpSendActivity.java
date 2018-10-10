package com.tuya.smart.android.demo.activity;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.kyleduo.switchbutton.SwitchButton;
import com.tuya.smart.android.common.utils.L;
import com.tuya.smart.android.demo.R;
import com.tuya.smart.android.demo.presenter.DpSendPresenter;
import com.tuya.smart.android.demo.presenter.GroupDpSendPresenter;
import com.tuya.smart.android.demo.utils.ToastUtil;
import com.tuya.smart.android.demo.view.IDpSendView;

import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by letian on 16/8/27.
 */
public class GroupDpSendActivity extends BaseActivity implements IDpSendView {
    private static final String TAG = "DpSendActivity ggg";
    private GroupDpSendPresenter mPresenter;

    @BindView(R.id.fl_boolean)
    public View mBoolView;
    @BindView(R.id.fl_raw)
    public View mRawView;
    @BindView(R.id.fl_string)
    public View mStrView;
    @BindView(R.id.fl_value)
    public View mValueView;
    @BindView(R.id.fl_enum)
    public View mEnumView;
    @BindView(R.id.fl_bitmap)
    public View mBitmapView;

    @BindView(R.id.sb_boolean)
    public SwitchButton mBoolSBView;

    @BindView(R.id.et_str)
    public EditText mStrETView;

    @BindView(R.id.et_value)
    public EditText mValueETView;

    @BindView(R.id.et_raw)
    public EditText mRawETView;

    @BindView(R.id.tv_bool_name)
    public TextView mBoolTVView;

    @BindView(R.id.tv_raw_name)
    public TextView mRawTVView;

    @BindView(R.id.tv_str_name)
    public TextView mStrTVView;

    @BindView(R.id.tv_value_name)
    public TextView mValueTVView;

    @BindView(R.id.tv_dp_des)
    public TextView mDpDesView;

    @BindView(R.id.test_scroll)
    public ScrollView testScroll;

    @BindView(R.id.test_log)
    public TextView testLog;

    @BindView(R.id.btn_send)
    public TextView mSendView;

    @BindView(R.id.sp_enum)
    public Spinner mNiceSpinner;

    @BindView(R.id.tv_enum_name)
    public TextView mEnumTVView;

    @BindView(R.id.tv_bitmap_name)
    public TextView mBitmapTVView;

    @BindView(R.id.et_bitmap_value)
    public EditText mBitampETView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dp_send);
        ButterKnife.bind(this);
        initToolbar();
        initPresenter();
        initTitle();
        initMenu();
        showView();
        initLisenter();
    }

    private void initLisenter() {
        mBoolSBView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mPresenter.sendDpValue(isChecked);
            }
        });
        mNiceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mPresenter.sendDpValue(mNiceSpinner.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void showView() {
        mPresenter.showView();
        mDpDesView.setText(mPresenter.getDpDes());
    }

    private void initMenu() {
        setDisplayHomeAsUpEnabled();

    }

    protected void setDisplayHomeAsUpEnabled() {
        setDisplayHomeAsUpEnabled(R.drawable.tysmart_back_white, null);
    }

    private void initTitle() {
        setTitle(mPresenter.getTitle());
    }

    private void initPresenter() {
        mPresenter = new GroupDpSendPresenter(this, this);
    }

    @Override
    public void showBooleanView(Boolean value) {
        setViewVisible(mBoolView);
        mBoolSBView.setCheckedNoEvent(value);
        mBoolTVView.setText(mPresenter.getName());
    }

    @Override
    public void showStringView(String value) {
        setViewVisible(mStrView);
        mStrETView.setText(value);
        mStrTVView.setText(mPresenter.getName());
        setViewVisible(mSendView);
    }

    @Override
    public void showRawView(String value) {
        setViewVisible(mRawView);
        mRawETView.setText(value);
        mRawTVView.setText(mPresenter.getName());
        setViewVisible(mSendView);
    }

    @Override
    public void showValueView(int value) {
        setViewVisible(mValueView);
        mValueETView.setText(String.valueOf(value));
        mValueTVView.setText(mPresenter.getName());
        setViewVisible(mSendView);
    }

    @Override
    public void showBitmapView(int bitmap) {
        setViewVisible(mBitmapView);
        mBitampETView.setText(String.valueOf(bitmap));
        mBitmapTVView.setText(mPresenter.getName());
        setViewVisible(mSendView);
    }

    @Override
    public void showEnumView(String vlaue, Set<String> range) {
        setViewVisible(mEnumView);
        mEnumTVView.setText(mPresenter.getName());
        mNiceSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, range.toArray(range.toArray())));
    }

    @OnClick(R.id.btn_send)
    public void onClickSend() {
        mPresenter.sendDpValue();
    }

    @Override
    public void showMessage(final String log) {
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
    public String getStrValue() {
        return mStrETView.getText().toString();
    }

    @Override
    public String getEnumValue() {
        return mNiceSpinner.getSelectedItem().toString();
    }

    @Override
    public String getValue() {
        return mValueETView.getText().toString();
    }

    @Override
    public String getRAWValue() {
        return mRawETView.getText().toString();
    }

    @Override
    public void showFormatErrorTip() {
        ToastUtil.showToast(this, R.string.format_error);
    }

    @Override
    public String getBitmapValue() {
        return mBitampETView.getText().toString();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }
}
