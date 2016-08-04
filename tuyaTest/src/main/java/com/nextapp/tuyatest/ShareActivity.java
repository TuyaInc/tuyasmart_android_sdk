package com.nextapp.tuyatest;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tuya.smart.android.user.api.IAddShareCallback;
import com.tuya.smart.android.user.bean.GroupReceivedMemberBean;
import com.tuya.smart.android.user.bean.PersonBean;
import com.tuya.smart.sdk.TuyaMember;
import com.tuya.smart.sdk.api.share.IAddMemberCallback;
import com.tuya.smart.sdk.api.share.IModifyMemberNameCallback;
import com.tuya.smart.sdk.api.share.IQueryMemberListCallback;
import com.tuya.smart.sdk.api.share.IQueryReceiveMemberListCallback;
import com.tuya.smart.sdk.api.share.IRemoveMemberCallback;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by letian on 16/1/4.
 */
public class ShareActivity extends Activity {

    @Bind(R.id.tv_add_share)
    TextView mTvAddShare;
    @Bind(R.id.tv_get_share)
    TextView mTvGetShare;
    @Bind(R.id.tv_add_uid_share)
    TextView mTvAddUidShare;
    @Bind(R.id.lv_share_list)
    ListView mLvShareList;
    private TuyaMember mTuyaMember;
    private ListView mShareListView;
    private ShareListAdapter shareListAdapter;
    private Context mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_share);
        ButterKnife.bind(this);
        mShareListView = (ListView) findViewById(R.id.lv_share_list);
        mTuyaMember = new TuyaMember(this);
        mActivity = this;

        findViewById(R.id.tv_add_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addShare();
            }
        });
        findViewById(R.id.tv_get_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryShare();
            }
        });
    }

    public void addShare() {

        mTuyaMember.addMember("86", "18888888888", "Xiao Lin", "Father", new IAddMemberCallback() {
            @Override
            public void onSuccess(Integer shareId) {
                Toast.makeText(mActivity, R.string.add_share_success, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String code, String error) {
                Toast.makeText(mActivity, R.string.add_share_failure, Toast.LENGTH_SHORT).show();
            }
        });
        mTuyaMember.modifyMemberName(123, "小张", new IModifyMemberNameCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(mActivity, "修改分享名称成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String code, String error) {
                Toast.makeText(mActivity, "修改分享名称失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick(R.id.tv_add_uid_share)
    public void addUidMember() {
        mTuyaMember.addUidMember("1111", "name", "Friend", new IAddShareCallback() {
            @Override
            public void onSuccess(Integer shareId) {
                Toast.makeText(mActivity, R.string.add_share_success, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String code, String error) {
                Toast.makeText(mActivity, R.string.add_share_failure, Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void removeShare(int id) {
        mTuyaMember.removeMember(id, new IRemoveMemberCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(mActivity, R.string.delete_share_success, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String code, String error) {
                Toast.makeText(mActivity, R.string.delete_share_failure, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void modifyShare(int id) {
        mTuyaMember.modifyReceiveMemberName(id, "Xiao Zhang", new IModifyMemberNameCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(mActivity, R.string.modify_share_success, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String code, String error) {
                Toast.makeText(mActivity, R.string.modify_share_failure, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void queryShare() {
        mTuyaMember.queryMemberList(new IQueryMemberListCallback() {
            @Override
            public void onSuccess(ArrayList<PersonBean> bizResult) {
                if (bizResult != null) {
                    Toast.makeText(mActivity, R.string.get_sharelist_success, Toast.LENGTH_SHORT).show();
                    shareListAdapter.setData(bizResult);
                }
            }

            @Override
            public void onError(String code, String error) {
                Toast.makeText(mActivity, R.string.get_sharelist_failure, Toast.LENGTH_SHORT).show();
            }
        });

        shareListAdapter = new ShareListAdapter(this, R.layout.list_share_item, new ArrayList<PersonBean>());
        mShareListView.setAdapter(shareListAdapter);
        mShareListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                modifyShare(shareListAdapter.getData().get(position).getId());
            }
        });
        mShareListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                removeShare(shareListAdapter.getData().get(position).getId());
                return true;
            }
        });
    }

    public void queryMemberShare() {
        mTuyaMember.queryReceiveMemberList(new IQueryReceiveMemberListCallback() {
            @Override
            public void onError(String code, String error) {
                Toast.makeText(mActivity, error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(ArrayList<GroupReceivedMemberBean> bizResult) {
                Toast.makeText(mActivity, R.string.get_sharelist_success, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
