package com.tuya.smart.android.demo.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.tuya.smart.android.demo.R;
import com.tuya.smart.android.demo.event.FriendEventModel;
import com.tuya.smart.android.demo.test.event.EventSender;
import com.tuya.smart.android.demo.utils.ToastUtil;
import com.tuya.smart.android.user.bean.PersonBean;
import com.tuya.smart.sdk.TuyaMember;
import com.tuya.smart.sdk.api.share.IModifyMemberNameCallback;

/**
 * Created by letian on 15/3/5.
 */
public class EditFriendActivity extends BaseActivity {

    public static final String DATA_PERSON = "data_person";

    private static final String TAG = "EditFriendActivity";

    public static final String DATA_POSITION = "data_position";

    private EditText mNameET;
    private EditText mInputNumberET;

    private PersonBean mPerson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_friend);
        initToolbar();
        initView();
        initMenu();
        initData();
    }

    protected void initToolbar() {
        super.initToolbar();
        setTitle(getString(R.string.friend));
    }

    private void initMenu() {
        setMenu(R.menu.toolbar_edit_profile, new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.action_done) {
                    finishEditName();
                }

                return false;
            }
        });
        setDisplayHomeAsUpEnabled();
    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        mPerson = bundle.getParcelable(DATA_PERSON);
        String name = mPerson.getName();
        String phoneNumber = mPerson.getMobile();
        if (!TextUtils.isEmpty(name)) {
            mNameET.setText(name);
            mNameET.setSelection(name.length());
        }
        if (!TextUtils.isEmpty(phoneNumber)) {
            mInputNumberET.setText(phoneNumber);
        }
        mInputNumberET.setEnabled(false);
    }

    private void initView() {
        mNameET = (EditText) findViewById(R.id.et_name);
        mInputNumberET = (EditText) findViewById(R.id.et_phone_number);
    }

    private void finishEditName() {
        String name = mNameET.getText().toString();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, R.string.cannot_input_empty_string, Toast.LENGTH_SHORT).show();
            return;
        }

        mPerson.setName(mNameET.getText().toString());

        TuyaMember TuyaMember = new TuyaMember(this);
        TuyaMember.modifyMemberName(mPerson.getId(), mPerson.getName(), new IModifyMemberNameCallback() {
            @Override
            public void onSuccess() {
//                L.d(TAG, "finishEditName" + mPerson.getName() + "");
                EventSender.friendUpdate(mPerson, getIntent().getIntExtra(DATA_POSITION, 0), FriendEventModel.OP_EDIT);
                finish();
            }

            @Override
            public void onError(String s, String s1) {
                ToastUtil.showToast(EditFriendActivity.this, s1);
            }
        });
    }

}
