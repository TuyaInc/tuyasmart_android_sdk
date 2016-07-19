package com.nextapp.tuyatest.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.nextapp.tuyatest.R;
import com.nextapp.tuyatest.presenter.SharedMemberAddPresenter;
import com.nextapp.tuyatest.test.utils.DialogUtil;
import com.nextapp.tuyatest.utils.ActivityUtils;
import com.nextapp.tuyatest.utils.CheckPermissionUtils;
import com.nextapp.tuyatest.view.ISharedMemberAddView;
import com.tuya.smart.sdk.TuyaSdk;
import com.wnafee.vector.compat.VectorDrawable;

/**
 * Created by leaf on 15/12/23.
 * 添加
 */
public class SharedMemberAddActivity extends BaseActivity implements ISharedMemberAddView {

    private static final String TAG = "SharedMemberAddActivity";
    private static final int REQUEST_CODE_FOR_PERMISSION = 222;
    TextView mCountryName;
    EditText mPhone;
    private CheckPermissionUtils checkPermission;
    private SharedMemberAddPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_member_add);

        mCountryName = (TextView) findViewById(R.id.country_name);
        mPhone = (EditText) findViewById(R.id.phone);
        findViewById(R.id.submit).setOnClickListener(mOnClickListener);
        ImageView view = (ImageView) findViewById(R.id.contacts);
        VectorDrawable drawable = VectorDrawable.getDrawable(TuyaSdk.getApplication(), R.drawable.addshare_contact);
        drawable.setAlpha(128);
        view.setImageDrawable(drawable);


        view.setOnClickListener(mOnClickListener);
        findViewById(R.id.country_name).setOnClickListener(mOnClickListener);
        checkPermission = new CheckPermissionUtils(SharedMemberAddActivity.this);
        initToolbar();
        initMenu();
        initPresenter();
        initAdapter();
    }

    public static Drawable tintDrawable(Drawable drawable, ColorStateList colors) {
        final Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTintList(wrappedDrawable, colors);
        return wrappedDrawable;
    }

    private void initMenu() {
        setTitle(getString(R.string.new_share));
        setDisplayHomeAsUpEnabled();
    }

    private void initPresenter() {
        mPresenter = new SharedMemberAddPresenter(this, this);
        mPresenter.getCountry();
    }

    private void initAdapter() {
        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.spinner_relation_item, mPresenter.getRelationNameList());
        adapter.setDropDownViewResource(R.layout.spinner_relation_dropdown_item);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_FOR_PERMISSION && checkPermission.onRequestPermissionsResult(permissions, grantResults)) {
            mPresenter.gotoPickContact();
        } else {
            DialogUtil.simpleSmartDialog(this, getString(R.string.ty_permission_tip_contact), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent i = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    i.addCategory(Intent.CATEGORY_DEFAULT);
                    i.setData(Uri.parse("package:" + SharedMemberAddActivity.this.getPackageName()));
                    SharedMemberAddActivity.this.startActivity(i);
                    SharedMemberAddActivity.this.finish();
                }
            });
        }
    }


    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.submit) {
                mPresenter.add();
            } else if (v.getId() == R.id.contacts) {
                CheckPermissionUtils checkPermission = new CheckPermissionUtils(SharedMemberAddActivity.this);
                if (checkPermission.checkSiglePermission(Manifest.permission.READ_CONTACTS, REQUEST_CODE_FOR_PERMISSION)) {
                    mPresenter.gotoPickContact();
                }
            } else if (v.getId() == R.id.country_name) {
                mPresenter.selectCountry();
            }
        }
    };

    @Override
    public String getMobile() {
        return mPhone.getText().toString();
    }

    @Override
    public void setMobile(String mobile) {
        mPhone.setText(mobile);
    }

    @Override
    public void setCountry(String name, String code) {
        mCountryName.setText(String.format("%s +%s", name, code));
    }

    @Override
    public void onBackPressed() {
        ActivityUtils.back(this, ActivityUtils.ANIMATE_SLIDE_BOTTOM_FROM_TOP);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPresenter.onActivityResult(requestCode, resultCode, data);
    }
}