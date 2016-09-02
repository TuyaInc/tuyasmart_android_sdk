package com.tuya.smart.android.demo.presenter;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Message;
import android.provider.ContactsContract;
import android.text.TextUtils;

import com.tuya.smart.android.demo.R;
import com.tuya.smart.android.demo.activity.CountryListActivity;
import com.tuya.smart.android.demo.event.FriendEventModel;
import com.tuya.smart.android.demo.model.SharedMemberAddModel;
import com.tuya.smart.android.demo.model.SharedModel;
import com.tuya.smart.android.demo.test.event.EventSender;
import com.tuya.smart.android.demo.test.utils.DialogUtil;
import com.tuya.smart.android.demo.utils.CountryUtils;
import com.tuya.smart.android.demo.utils.ProgressUtil;
import com.tuya.smart.android.demo.utils.ToastUtil;
import com.tuya.smart.android.demo.view.ISharedMemberAddView;
import com.tuya.smart.android.mvp.bean.Result;
import com.tuya.smart.android.mvp.presenter.BasePresenter;
import com.tuya.smart.android.user.bean.PersonBean;
import com.tuya.smart.sdk.TuyaSdk;

import java.util.ArrayList;

/**
 * Created by leaf on 15/12/21.
 * 收到的共享
 */
public class SharedMemberAddPresenter extends BasePresenter {
    private static final String TAG = "SharedMemberAddPresenter";

    public static final int REQUEST_TO_PICK_CONTACT = 0x030;
    public static final int REQUEST_TO_COUNTRY_LIST = 0x031;

    private Activity mActivity;
    protected ISharedMemberAddView mView;
    protected SharedModel mSharedModel;
    protected SharedMemberAddModel mModel;

    private PersonBean mPerson;

    private String mCountryCode;

    private String mCountryName;

    public SharedMemberAddPresenter(Activity activity, ISharedMemberAddView view) {
        mActivity = activity;
        mView = view;
        mModel = new SharedMemberAddModel(activity, mHandler);
        mSharedModel = new SharedModel(activity, mHandler);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mModel.onDestroy();
        mSharedModel.onDestroy();
    }

    public void add() {
        String mobile = mView.getMobile();
        String code = mCountryCode;

        if (verify(mobile)) {
            mPerson = new PersonBean();
            mPerson.setMobile(mobile);
            ProgressUtil.showLoading(mActivity, R.string.loading);
            mSharedModel.addMember(mobile, "", code, "");
        }
    }

    private boolean verify(String mobile) {
        if (TextUtils.isEmpty(mobile)) {
            ToastUtil.showToast(mActivity, R.string.username_phone_is_null);
        } else {
            return true;
        }
        return false;
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case SharedModel.WHAT_ADD_SENT_SUCCESS:
                ProgressUtil.hideLoading();
                mPerson.setId((Integer) ((Result) msg.obj).getObj());
                EventSender.friendUpdate(null, FriendEventModel.OP_QUERY);
                mActivity.finish();
                break;
            case SharedModel.WHAT_ERROR:
                ProgressUtil.hideLoading();
                DialogUtil.simpleSmartDialog(mActivity, ((Result) msg.obj).error, null);
                break;
        }

        return super.handleMessage(msg);
    }

    public ArrayList<String> getRelationNameList() {
        return mModel.getRelationNameList();
    }

    public void gotoPickContact() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        mActivity.startActivityForResult(intent, REQUEST_TO_PICK_CONTACT);
    }

    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (reqCode) {
                case (REQUEST_TO_PICK_CONTACT):
                    Uri contactData = data.getData();
                    Cursor c = mActivity.managedQuery(contactData, null, null, null, null);
                    if (c.moveToFirst()) {
                        String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        String hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                        String contactId = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
                        String phoneNumber = null;
                        if (hasPhone.equalsIgnoreCase("1")) {
                            Cursor phones = mActivity.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                            while (phones.moveToNext()) {
                                phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            }
                            phones.close();
                        }
                        if (null != phoneNumber)
                            mView.setMobile(phoneNumber.replace(" ", "").replace("+86", "").replace("-", ""));
                    }
                    break;
                case REQUEST_TO_COUNTRY_LIST:
                    mCountryName = data.getStringExtra(CountryListActivity.COUNTRY_NAME);
                    mCountryCode = data.getStringExtra(CountryListActivity.PHONE_CODE);
                    mView.setCountry(mCountryName, mCountryCode);
                    break;
            }
        }
    }

    public void getCountry() {
        String countryKey = CountryUtils.getCountryKey(TuyaSdk.getApplication());
        if (!TextUtils.isEmpty(countryKey)) {
            mCountryName = CountryUtils.getCountryTitle(countryKey);
            mCountryCode = CountryUtils.getCountryNum(countryKey);
        } else {
            countryKey = CountryUtils.getCountryDefault(TuyaSdk.getApplication());
            mCountryName = CountryUtils.getCountryTitle(countryKey);
            mCountryCode = CountryUtils.getCountryNum(countryKey);
        }

        mView.setCountry(mCountryName, mCountryCode);
    }

    public void selectCountry() {
        mActivity.startActivityForResult(new Intent(mActivity, CountryListActivity.class), REQUEST_TO_COUNTRY_LIST);
    }
}
