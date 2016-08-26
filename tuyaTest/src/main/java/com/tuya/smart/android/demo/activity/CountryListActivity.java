package com.tuya.smart.android.demo.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;


import com.tuya.smart.android.demo.R;
import com.tuya.smart.android.demo.adapter.CountryAdpater;
import com.tuya.smart.android.demo.bean.CountryViewBean;
import com.tuya.smart.android.demo.utils.CountryUtils;
import com.tuya.smart.android.demo.widget.contact.ContactItemInterface;
import com.tuya.smart.android.demo.widget.contact.CountryListView;

import java.util.ArrayList;
import java.util.List;

public class CountryListActivity extends BaseActivity implements TextWatcher {

    private final static String TAG = "ContactListActivity";

    public final static String PHONE_CODE = "PHONE_CODE";
    public final static String COUNTRY_NAME = "COUNTRY_NAME";

    private CountryListView listview;
    private EditText searchBox;
    private String searchString;
    private Object searchLock = new Object();
    boolean inSearchMode = false;
    public static final String COUNTRYNUM = "country_num";

    List<ContactItemInterface> contactList;
    List<ContactItemInterface> filterList;
    private SearchListTask curSearchTask = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.country_list);
        initToolbar();
        setTitle(getString(R.string.choose_country_code));
        setDisplayHomeAsUpEnabled();

        filterList = new ArrayList<ContactItemInterface>();
        contactList = CountryUtils.getSampleContactList();
        CountryAdpater adapter = new CountryAdpater(this, R.layout.country_list_item, contactList);
        listview = (CountryListView) findViewById(R.id.listview);
        listview.setFastScrollEnabled(true);
        listview.setAdapter(adapter);

        // use this to process individual clicks
        // cannot use OnClickListener as the touch event is overrided by IndexScroller
        // use last touch X and Y if want to handle click for an individual item within the row
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position,
                                    long id) {
                List<ContactItemInterface> searchList = inSearchMode ? filterList : contactList;
                CountryViewBean countryViewBean = (CountryViewBean) searchList.get(position);
                if (countryViewBean != null) {
                    Intent intent = new Intent();
                    intent.putExtra(PHONE_CODE, countryViewBean.getNumber());
                    intent.putExtra(COUNTRY_NAME, countryViewBean.getCountryName());
                    setResult(RESULT_OK, intent);
                } else {
                    setResult(RESULT_CANCELED);
                }
                finish();
            }
        });

        searchBox = (EditText) findViewById(R.id.input_search_query);
        searchBox.addTextChangedListener(this);
    }

    @Override
    public void afterTextChanged(Editable s) {
        searchString = searchBox.getText().toString().trim().toUpperCase();

        if (curSearchTask != null && curSearchTask.getStatus() != AsyncTask.Status.FINISHED) {
            try {
                curSearchTask.cancel(true);
            } catch (Exception e) {
                Log.i(TAG, "Fail to cancel running search task");
            }

        }
        curSearchTask = new SearchListTask();
        curSearchTask.execute(searchString); // put it in a task so that ui is not freeze
    }

    protected void onResume() {
        super.onResume();
    }

    protected void onPause() {
        super.onPause();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
        // do nothing
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // do nothing
    }

    private class SearchListTask extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... params) {
            filterList.clear();

            String keyword = params[0];

            inSearchMode = (keyword.length() > 0);

            if (inSearchMode) {
                // get all the mItems matching this
                for (ContactItemInterface item : contactList) {
                    CountryViewBean contact = (CountryViewBean) item;
//                    System.out.println(contact.getCountryName());
                    if ((contact.getCountryName().toUpperCase().indexOf(keyword) > -1) ||
                            (contact.isChinese() && contact.getPinyin().toUpperCase().indexOf(keyword) > -1)) {
                        filterList.add(item);
                    }

                }


            }
            return null;
        }

        protected void onPostExecute(String result) {

            synchronized (searchLock) {

                if (inSearchMode) {

                    CountryAdpater adapter = new CountryAdpater(CountryListActivity.this, R.layout.country_list_item, filterList);
                    adapter.setInSearchMode(true);
                    listview.setInSearchMode(true);
                    listview.setAdapter(adapter);
                } else {
                    CountryAdpater adapter = new CountryAdpater(CountryListActivity.this, R.layout.country_list_item, contactList);
                    adapter.setInSearchMode(false);
                    listview.setInSearchMode(false);
                    listview.setAdapter(adapter);
                }
            }

        }


    }

    public boolean needLogin() {
        return false;
    }

}
