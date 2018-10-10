package com.tuya.smart.android.demo.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tuya.smart.android.demo.R;
import com.tuya.smart.android.demo.adapter.SchemaListAdapter;
import com.tuya.smart.android.demo.presenter.GroupCommonPresenter;
import com.tuya.smart.android.demo.view.IDeviceCommonView;
import com.tuya.smart.android.device.bean.SchemaBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by letian on 16/8/27.
 */
public class GroupActivity extends BaseActivity implements IDeviceCommonView {
    @BindView(R.id.lv_dp_list)
    public ListView mListView;
    private SchemaListAdapter mAdapter;
    private GroupCommonPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_common);
        ButterKnife.bind(this);
        initToolbar();
        initPresenter();
        initTitle();
        initMenu();
        initAdapter();
        getSchemaData();
    }


    protected void setDisplayHomeAsUpEnabled() {
        setDisplayHomeAsUpEnabled(R.drawable.tysmart_back_white, null);
    }

    private void getSchemaData() {
        mPresenter.getData();
    }

    private void initTitle() {
        setTitle(mPresenter.getDevName());
    }

    private void initMenu() {
        setDisplayHomeAsUpEnabled();
        setMenu(R.menu.toolbar_top_group_smart_device, new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_rename:
                        mPresenter.renameDevice();
                        break;
                    case R.id.action_close:
                        finish();
                        break;
                    case R.id.action_resume_factory_reset:
                        break;
                    case R.id.action_unconnect:
                        mPresenter.removeDevice();
                        break;
                }
                return false;
            }
        });
    }

    private void initPresenter() {
        mPresenter = new GroupCommonPresenter(this, this);
    }

    private void initAdapter() {
        mAdapter = new SchemaListAdapter(this);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mPresenter.onItemClick(mAdapter.getItem(position));
            }
        });
    }

    @Override
    public void setSchemaData(List<SchemaBean> schemaList) {
        mAdapter.setData(schemaList);
    }

    @Override
    public void updateTitle(String titleName) {
        setTitle(titleName);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }
}
