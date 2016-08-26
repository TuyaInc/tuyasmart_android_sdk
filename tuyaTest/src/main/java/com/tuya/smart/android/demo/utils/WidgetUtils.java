package com.tuya.smart.android.demo.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.tuya.smart.android.demo.R;


/**
 * Created by lee on 16/3/18.
 */
public class WidgetUtils {

    /**
     * 必须保证传入的布局有 tv_none_content 存在
     *
     * @param context
     * @param contentView
     */
    public static void checkNoneContentLayout(Context context, View contentView, String tipText) {
        checkNoneContentLayout(context,contentView,-1,tipText,false,-1,-1);
    }

    /**
     *  按需初始化空内容布局  tv_none_content 存在
     * @param context 上下文
     * @param contentView 父布局
     * @param noneIconAttrId 无数据图片的 attr 风格配置id
     * @param tipText 无数据提示文本
     * @param funcAttrId 功能按钮是否开启的 attr 风格配置id
     * @param funcBtTextId 功能按钮的文本id
     */
    public static void checkNoneContentLayout(Context context, View contentView, int noneIconAttrId, String tipText,int funcAttrId,int funcBtTextId) {
        checkNoneContentLayout(context,contentView,noneIconAttrId,tipText,false,funcAttrId,funcBtTextId);
    }

    public static void checkNoneContentLayout(Context context, View contentView, int noneIconAttrId, String  tipText,boolean useFuncButton,int funcAttrId,int funcBtTextId) {
        TypedArray a = context.obtainStyledAttributes(new int[]{
                noneIconAttrId, R.attr.icon_none_content,funcAttrId});
        int noneId = -1;
        if(noneIconAttrId != -1){
            noneId = a.getResourceId(0, -1);
        }

        int commonId = a.getResourceId(1, -1);
        if (noneId != -1) {
            setNoneContentIcon(context, contentView, noneId,tipText);
        } else {
            setNoneContentIcon(context, contentView, commonId,tipText);
        }

        TextView fucTextView = (TextView)contentView.findViewById(R.id.tv_empty_func);
        if(funcBtTextId != -1 || useFuncButton){
            boolean isFunctionSupport = a.getBoolean(2,false);
            if(isFunctionSupport || useFuncButton){
                fucTextView.setText(funcBtTextId);
                fucTextView.setVisibility(View.VISIBLE);
            }else{
                fucTextView.setVisibility(View.GONE);
            }
        }else{
            fucTextView.setVisibility(View.GONE);
        }


        a.recycle();
    }

    public static void checkNoneContentLayout(Context context, View contentView, int noneIconAttrId, String tipText) {
        checkNoneContentLayout(context,contentView,noneIconAttrId,tipText,-1,-1);
    }

    private static void setNoneContentIcon(Context context, View contentView, int noneIconResId,String tipText) {
        if (noneIconResId != -1) {
            ImageView tv = ((ImageView) contentView.findViewById(R.id.iv_none_data));
            if (tv == null) {
                return;
            }
            Drawable noneTip = context.getResources().getDrawable(noneIconResId);
            tv.setImageDrawable(noneTip);
        }

        if(!TextUtils.isEmpty(tipText)){
            TextView tip = (TextView)contentView.findViewById(R.id.tv_none_content);
            tip.setText(tipText);
        }
    }

    /**
     * 计算屏幕大小
     *
     * @param context
     * @return
     */
    public static final DisplayMetrics getDisplayMetrics(Context context) {
        if (context == null) return null;
        return context.getResources().getDisplayMetrics();
    }

    /**
     * 创建底部弹出的dialog
     *
     * @param context
     * @param dialogView
     * @return
     */
    public static Dialog createBottomDialog(Context context, View dialogView, int styleId) {
        final Dialog customDialog = new Dialog(context, styleId);

        //底部开始
        WindowManager.LayoutParams localLayoutParams = customDialog.getWindow().getAttributes();
        localLayoutParams.gravity = Gravity.BOTTOM | Gravity.LEFT;
        customDialog.onWindowAttributesChanged(localLayoutParams);

        //设置宽度
        int screenWidth = getDisplayMetrics(context).widthPixels;
        dialogView.setMinimumWidth(screenWidth);

        customDialog.setCanceledOnTouchOutside(false);
        customDialog.setCancelable(true);
        customDialog.setCanceledOnTouchOutside(true);
        customDialog.setContentView(dialogView);

        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            if (!activity.isFinishing()) {
                customDialog.show();
            }
        }

        return customDialog;
    }

    /**
     * 动态设置ListView的高度,解决与ScrollView滚动冲突问题
     */
    public static void fixListViewHeight(ListView listView) {
        if (listView == null) return;

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null || listAdapter.getCount() <= 0) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            if (listItem == null) continue;
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        if (params == null) {
            return;
        }
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
