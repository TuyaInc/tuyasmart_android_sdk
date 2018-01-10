package com.tuya.smart.android.demo.activity;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.tuya.smart.android.common.utils.L;
import com.tuya.smart.android.common.utils.TyCommonUtil;
import com.tuya.smart.android.demo.R;
import com.tuya.smart.android.demo.utils.ActivityUtils;
import com.tuya.smart.android.demo.utils.CommonUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mikeshou on 15/6/15.
 */
public class BrowserActivity extends BaseActivity {

    private static final String TAG = "Browser";

    public static final String EXTRA_TITLE = "Title";
    public static final String EXTRA_URI = "Uri";
    public static final String EXTRA_TOOLBAR = "Toolbar";
    public static final String EXTRA_LOGIN = "Login";
    public static final String EXTRA_REFRESH = "Refresh";
    public static final String EXTRA_FROM_PANNEL = "from_pannel";

    protected WebView mWebView;

    /**
     * 是否需要登录
     */
    private boolean needlogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);

        Intent intent = getIntent();
        initTitleBar(intent);
        String url = intent.getStringExtra(EXTRA_URI);

        needlogin = intent.getBooleanExtra(EXTRA_LOGIN, false);
        boolean refresh = intent.getBooleanExtra(EXTRA_REFRESH, true);

        if (TextUtils.isEmpty(url) || !CommonUtil.checkUrl(url)) {
            url = "about:blank";
        }
        setMenu(R.menu.toolbar_top_refresh, new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.menu_refresh) {
                    mWebView.stopLoading();
                    mWebView.reload();
                    return true;
                }
                return false;
            }
        });
        mWebView = (WebView) findViewById(R.id.webview);
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Accept-Language", TyCommonUtil.getLang(this));
        mWebView.loadUrl(url, headers);
    }

    private void initTitleBar(Intent intent) {
        boolean toolbar = intent.getBooleanExtra(EXTRA_TOOLBAR, true);
        String title = intent.getStringExtra(EXTRA_TITLE);
        boolean isFromPannel = intent.getBooleanExtra(EXTRA_FROM_PANNEL, false);

        initToolbar();
        showToolBarView();
        if (TextUtils.isEmpty(title)) {
            TypedArray a = obtainStyledAttributes(new int[]{R.attr.app_name});
            title = a.getString(0);
        }
        setTitle(title);

        if (isFromPannel) {
            setDisplayHomeAsUpEnabled();
            hideTitleBarLine();
            //面板title字体颜色写死白色
            if (mToolBar != null) {
                mToolBar.setTitleTextColor(Color.WHITE);
            }
        } else {
            setDisplayHomeAsUpEnabled();
        }

        if (!toolbar) {
            hideToolBarView();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (null != mWebView) {
            mWebView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (null != mWebView) {
            mWebView.onPause();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (!isFinishing()) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (mWebView.canGoBack()) {
                    mWebView.goBack();
                    return true;
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ((ViewGroup) findViewById(R.id.browser_layout)).removeView(mWebView);
        if (null != mWebView) {
            mWebView.destroy();
            mWebView = null;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (intent != null) {
            String url = intent.getStringExtra(EXTRA_URI);
            L.d(TAG, "Browser : onNewIntent 2:" + url);
            if (mWebView != null && url != null) {
                mWebView.loadUrl(url);
            }
        }
        super.onNewIntent(intent);
    }

    @Override
    protected boolean onPanelKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (mWebView.canGoBack()) {
                return true;
            }
        }
        return false;

    }

    @Override
    public void onBackPressed() {
        if (!mWebView.canGoBack()) {
            super.onBackPressed();
            ActivityUtils.back(this);
        }
    }

    @Override
    public boolean needLogin() {
        return needlogin;
    }
}
