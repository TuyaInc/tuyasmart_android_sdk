package com.tuya.smart.android.demo.fragment;

import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.tuya.smart.android.demo.R;


/**
 * Created by mikeshou on 15/6/16.
 */
public abstract class BaseFragment extends Fragment {

    protected Toolbar mToolBar;

    protected void initToolbar(View contentView) {
        if (mToolBar == null) {
            mToolBar = (Toolbar) contentView.findViewById(R.id.toolbar_top_view);
            if (mToolBar == null) {
            }else{
                TypedArray a = getActivity().obtainStyledAttributes(new int[]{
                        R.attr.status_font_color});
                int titleColor = a.getInt(0, Color.WHITE);
                mToolBar.setTitleTextColor(titleColor);
            }
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

    protected void setDisplayHomeAsUpEnabled() {
        if (mToolBar != null) {
            mToolBar.setNavigationIcon(R.drawable.tysmart_back);
            mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().onBackPressed();
                }
            });
        }
    }

    protected void setDisplayHomeAsUpEnabled(final View.OnClickListener listener) {
        if (mToolBar != null) {
            mToolBar.setNavigationIcon(R.drawable.tysmart_back);
            mToolBar.setNavigationOnClickListener(listener);
        }
    }

    protected void hideToolBarView() {
        if (mToolBar != null && mToolBar.isShown()) {
            mToolBar.setVisibility(View.GONE);
        }
    }

    protected void showToolBarView() {
        if (mToolBar != null && !mToolBar.isShown()) {
            mToolBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mToolBar = null;
    }
}
