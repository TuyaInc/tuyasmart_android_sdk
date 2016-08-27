package com.tuya.smart.android.demo.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.tuya.smart.android.common.utils.L;
import com.tuya.smart.android.demo.R;
import com.tuya.smart.android.demo.app.Constant;
import com.tuya.smart.android.demo.utils.ActivityUtils;
import com.tuya.smart.android.demo.utils.CommonUtil;
import com.tuya.smart.android.demo.utils.LoginHelper;
import com.tuya.smart.android.demo.utils.ToastUtil;
import com.tuya.smart.sdk.TuyaUser;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by letian on 16/7/15.
 */
public class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseActivity";

    protected Toolbar mToolBar;

    private boolean mIsPaused = true;

    protected View mPanelTopView;

    private long resumeUptime;

    private GestureDetector mGestureDetector;

    private boolean mNeedDefaultAni = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        GestureDetector.OnGestureListener gestureListener = obtainGestureListener();
        if (gestureListener != null) {
            mGestureDetector = new GestureDetector(this, gestureListener);
        }
        Constant.attachActivity(this);
        checkLogin();
    }

    public void closeDefaultAni() {
        mNeedDefaultAni = false;
    }


    protected boolean isUseCustomTheme() {
        return false;
    }

    private void checkLogin() {
        if (needLogin() && !TuyaUser.getUserInstance().isLogin()) {
            LoginHelper.reLogin(this);
        }
    }

    protected void initToolbar() {
        if (mToolBar == null) {
            mToolBar = (Toolbar) findViewById(R.id.toolbar_top_view);
            if (mToolBar == null) {
            } else {
                TypedArray a = obtainStyledAttributes(new int[]{
                        R.attr.status_font_color});
                int titleColor = a.getInt(0, Color.WHITE);
                mToolBar.setTitleTextColor(titleColor);
            }
        }
    }

    protected void hideTitleBarLine() {
        View line = findViewById(R.id.v_title_down_line);
        if (line != null) {
            line.setVisibility(View.GONE);
        }
    }

    public Toolbar getToolBar() {
        return mToolBar;
    }

    protected void setTitle(String title) {
        if (mToolBar != null) {
            mToolBar.setTitle(title);
        }
    }

    @Override
    public void setTitle(int titleId) {
        if (mToolBar != null) {
            mToolBar.setTitle(titleId);
        }
    }

    protected void setSubTitle(String title) {
        if (mToolBar != null) {
            mToolBar.setSubtitle(title);
        }
    }

    protected void setLogo(Drawable logo) {
        if (mToolBar != null) {
            mToolBar.setLogo(logo);
        }
    }

    protected void setNavigationIcon(Drawable logo) {
        if (mToolBar != null) {
            mToolBar.setNavigationIcon(logo);
        }
    }

    protected void setMenu(int resId, Toolbar.OnMenuItemClickListener listener) {
        if (mToolBar != null) {
            mToolBar.inflateMenu(resId);
            mToolBar.setOnMenuItemClickListener(listener);
        }
    }

    protected void setDisplayHomeAsUpEnabled(int iconResId, final View.OnClickListener listener) {
        if (mToolBar != null) {
            mToolBar.setNavigationIcon(iconResId);
            if (listener != null) {
                mToolBar.setNavigationOnClickListener(listener);
            } else {
                mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });
            }
        }
    }

    protected void setDisplayHomeAsUpEnabled() {
        setDisplayHomeAsUpEnabled(R.drawable.tysmart_back, null);
    }

    protected void setDisplayHomeAsUpEnabled(final View.OnClickListener listener) {
        setDisplayHomeAsUpEnabled(R.drawable.tysmart_back, listener);
    }

    protected void hideToolBarView() {
        if (mToolBar != null) {
            mToolBar.setVisibility(View.GONE);
        }
    }

    protected void showToolBarView() {
        if (mToolBar != null) {
            mToolBar.setVisibility(View.VISIBLE);
        }
    }

    @TargetApi(19)
    protected void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        if (mNeedDefaultAni) {
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
        super.startActivityForResult(intent, requestCode, options);
        if (mNeedDefaultAni) {
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mIsPaused = false;
        resumeUptime = SystemClock.uptimeMillis();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mIsPaused = true;
    }

    @Override
    public void onBackPressed() {
        ActivityUtils.back(this);
        super.onBackPressed();
        if (mNeedDefaultAni) {
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mGestureDetector != null) {
            mGestureDetector.onTouchEvent(ev);
        }
        return super.dispatchTouchEvent(ev);
    }

    private static boolean isExit = false;

    protected void exitBy2Click() {
        Timer tExit = null;
        if (!isExit) {
            isExit = true;
            ToastUtil.shortToast(this, getString(R.string.action_tips_exit_hint) + " "
                    + getString(R.string.app_name));
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false;
                }
            }, 2000);
        } else {
            LoginHelper.exit(this);
        }
    }

    protected GestureDetector.OnGestureListener obtainGestureListener() {
        return null;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (!this.isFinishing()) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                long eventtime = event.getEventTime();
                if (Math.abs(eventtime - resumeUptime) < 400) {
                    L.d(TAG, "baseactivity onKeyDown after onResume to close, do none");
                    return true;
                }
            }

            if (!(event.getRepeatCount() > 0) && !onPanelKeyDown(keyCode, event)) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    ActivityUtils.back(this);
                    return true;
                } else {
                    return super.onKeyDown(keyCode, event);
                }
            } else {
                L.d(TAG, "baseactivity onKeyDown true");
                return true;
            }

        } else {
            return true;
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_SETTINGS) {
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    protected boolean onPanelKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_SETTINGS) {
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
            return true;
        }
        return false;
    }

    @Override
    public void setContentView(int layoutResID) {
        LayoutInflater inflater =
                (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mPanelTopView = inflater.inflate(layoutResID, null);
        super.setContentView(mPanelTopView);
        initSystemBarColor();// 设置系统导航颜色
    }

    @Override
    public void setContentView(View view) {
        mPanelTopView = view;
        super.setContentView(view);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        mPanelTopView = view;
        super.setContentView(view, params);
    }

    /**
     * 是否需要登录，子类根据业务需要决定.
     * 默认所有界面都需要判断是否登录状态。
     */
    public boolean needLogin() {
        return true;
    }

    public boolean isContainFragment() {
        return false;
    }

    public static void setViewVisible(View view) {
        if (view.getVisibility() != View.VISIBLE) {
            view.setVisibility(View.VISIBLE);
        }
    }

    public static void setViewGone(View view) {
        if (view.getVisibility() != View.GONE) {
            view.setVisibility(View.GONE);
        }
    }


    protected boolean isPause() {
        return mIsPaused;
    }

    protected void hideIMM() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public void initSystemBarColor() {
        CommonUtil.initSystemBarColor(this);
    }
}
