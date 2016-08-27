package com.tuya.smart.android.demo.activity;

import android.os.Bundle;

import com.tuya.smart.android.demo.R;

import butterknife.ButterKnife;

/**
 * Created by letian on 16/8/26.
 */
public class CommonDebugActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_debug);
        ButterKnife.bind(this);
        initToolbar();
        initMenu();
        initTitle();

    }

    private void initMenu() {
        setDisplayHomeAsUpEnabled();
    }

    private void initTitle() {

    }
}
