package com.nextapp.tuyatest;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tuya.smart.android.user.TuyaSmartShare;
import com.tuya.smart.android.user.TuyaSmartUserManager;
import com.tuya.smart.android.user.api.IAddShareCallback;
import com.tuya.smart.android.user.api.IModifyShareCallback;
import com.tuya.smart.android.user.api.IQueryGroupReceiveCallback;
import com.tuya.smart.android.user.api.IQueryShareCallback;
import com.tuya.smart.android.user.api.IRemoveShareCallback;
import com.tuya.smart.android.user.bean.GroupReceivedMemberBean;
import com.tuya.smart.android.user.bean.PersonBean;

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
    private TuyaSmartShare tuyaSmartShare;
    private ListView mShareListView;
    private ShareListAdapter shareListAdapter;
    private Context mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_share);
        ButterKnife.bind(this);
        mShareListView = (ListView) findViewById(R.id.lv_share_list);
        tuyaSmartShare = new TuyaSmartShare(this);
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
                boolean login = TuyaSmartUserManager.getInstance().isLogin();
                if (login) {
//                    queryMemberShare();
                    queryShare();
                }
            }
        });
    }

    public void addShare() {

        tuyaSmartShare.addShare("86", "18888888888", "小林", "父亲", new IAddShareCallback() {
            @Override
            public void onSuccess(Integer a) {
                Toast.makeText(mActivity, "添加分享成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String code, String error) {
                Toast.makeText(mActivity, "添加分享失败", Toast.LENGTH_SHORT).show();
            }
        });
//        tuyaSmartShare.modifyShareName(123, "小张", new IModifyShareCallback() {
//            @Override
//            public void onSuccess() {
//                Toast.makeText(mActivity, "修改分享名称成功", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onError(String code, String error) {
//                Toast.makeText(mActivity, "修改分享名称失败", Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    @OnClick(R.id.tv_add_uid_share)
    public void addUidMember() {
        tuyaSmartShare.addUidMember("1111", "name", "朋友", new IAddShareCallback() {
            @Override
            public void onSuccess(Integer shareId) {
                Toast.makeText(mActivity, "添加UID分享成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String code, String error) {
                Toast.makeText(mActivity, "添加UID分享失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void removeShare(int id) {
        tuyaSmartShare.removeShare(id, new IRemoveShareCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(mActivity, "删除分享成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String code, String error) {
                Toast.makeText(mActivity, "删除分享失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void modifyShare(int id) {

        tuyaSmartShare.modifyGroupMemberName(id, "小张", new IModifyShareCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(mActivity, "修改分享成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String code, String error) {
                Toast.makeText(mActivity, "修改分享失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void queryShare() {
        tuyaSmartShare.queryShare(new IQueryShareCallback() {
            @Override
            public void onSuccess(ArrayList<PersonBean> bizResult) {
                if (bizResult != null) {
                    Toast.makeText(mActivity, "获取分享列表成功", Toast.LENGTH_SHORT).show();
                    shareListAdapter.setData(bizResult);
                }
            }

            @Override
            public void onError(String code, String error) {
                Toast.makeText(mActivity, "获取分享列表失败", Toast.LENGTH_SHORT).show();
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
        tuyaSmartShare.queryGroupReceiveMember(new IQueryGroupReceiveCallback() {
            @Override
            public void onError(String code, String error) {
                Toast.makeText(mActivity, error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(ArrayList<GroupReceivedMemberBean> bizResult) {
                Toast.makeText(mActivity, "获取分享列表成功", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
