package com.tuya.smart.android.demo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;

import com.alibaba.fastjson.JSONObject;
import com.tuya.smart.android.demo.R;
import com.tuya.smart.android.demo.adapter.SharedThirdAdapter;
import com.tuya.smart.android.demo.presenter.SharedEditReceivedMemberPresenter;
import com.tuya.smart.android.demo.view.ISharedEditReceivedMemberView;
import com.tuya.smart.android.user.bean.PersonBean;
import com.tuya.smart.sdk.bean.DeviceBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leaf on 15/12/22.
 * 别人的共享
 */
public class SharedEditReceivedMemberActivity extends BaseActivity implements ISharedEditReceivedMemberView {

    private static final String TAG = "SharedOtherActivity";

    public static final String EXTRA_DEVICE_LIST = "extra_device_list";

    public static final String EXTRA_PERSON = "extra_person";

    public static final String EXTRA_POSITION = "extra_position";

    public ListView mListView;
    public EditText mNumber;
    public EditText mName;
    private PersonBean mPerson;

    protected ArrayList<String> mDeviceList;

    private int mPosition;

    private SharedThirdAdapter mAdapter;

    private SharedEditReceivedMemberPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_edit_received_member);
        initToolbar();
        initMenu();
        initData();
        initView();
        initPresenter();
        initAdapter();
    }

    private void initMenu() {
        setTitle(getString(R.string.friend));
        setMenu(R.menu.toolbar_edit_profile, new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.action_done) {
                    updateNickname();
                }

                return false;
            }
        });
        setDisplayHomeAsUpEnabled();
    }

    private void initPresenter() {
        mPresenter = new SharedEditReceivedMemberPresenter(this, this);
    }

    private void initData() {
        Intent intent = getIntent();
        mDeviceList = intent.getStringArrayListExtra(EXTRA_DEVICE_LIST);
        mPerson = JSONObject.parseObject(intent.getExtras().getString(EXTRA_PERSON), PersonBean.class);
        mPosition = intent.getIntExtra(EXTRA_POSITION, 0);
    }

    private void initAdapter() {
        mAdapter = getSharedThirdAdapter();
        mListView.setAdapter(mAdapter);
    }

    protected SharedThirdAdapter getSharedThirdAdapter() {
        return new SharedThirdAdapter(this);
    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.lv_device_list);
        mNumber = (EditText) findViewById(R.id.et_phone_number);
        mName = (EditText) findViewById(R.id.et_name);

        if (null != mPerson) {
            String name = mPerson.getMobile();
            if (TextUtils.isEmpty(name)) {
                name = mPerson.getUsername();
            }
            mNumber.setText(name);
            mNumber.setEnabled(false);
            mName.setText(TextUtils.isEmpty(mPerson.getMname()) ?
                    String.format(getString(R.string.ty_add_share_tab2_name), mPerson.getMobile()) :
                    mPerson.getMname());
        }
    }

    @Override
    public void updateList(List<DeviceBean> list) {
        if (mAdapter != null && 0 < mDeviceList.size()) {
            ArrayList<DeviceBean> sharedList = new ArrayList<>();
            for (DeviceBean deviceBean : list) {
                if (mDeviceList.contains(deviceBean.getDevId())) {
                    sharedList.add(deviceBean);
                }
            }
            mAdapter.setData(sharedList);
        }
    }

    private void updateNickname() {
        mPresenter.updateNickname(mPerson, mName.getText().toString(), mPosition);
    }
}