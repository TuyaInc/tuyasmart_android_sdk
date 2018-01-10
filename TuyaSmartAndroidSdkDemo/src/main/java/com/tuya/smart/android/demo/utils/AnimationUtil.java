package com.tuya.smart.android.demo.utils;

/**
 * Created by letian on 15/12/9.
 */

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;


/**
 * Created by letian on 15/9/15.
 */

/**
 * @author letian
 */
public class AnimationUtil {
    public static final int DEFAULT_SLIDE_DURATION = 300;

    public static void fadeInItem(View view, int duration, int startOffset, int visibility, Animation.AnimationListener listener) {
        AlphaAnimation anim = new AlphaAnimation(0.0F, 1.0F);
        anim.setDuration((long) duration);
        anim.setStartOffset((long) startOffset);
        startAnimation(view, anim, visibility, listener);
    }

    public static void fadeInItems(int duration, int startOffset, int visibility, View[] views) {
        AlphaAnimation anim = new AlphaAnimation(0.0F, 1.0F);
        anim.setDuration((long) duration);
        anim.setStartOffset((long) startOffset);
        for (int j = 0; j < views.length; j++) {
            startAnimation(views[j], anim, visibility, null);
        }
    }

    public static void fadeItem(View view, int duration, int startOffset, int visibility, Animation.AnimationListener listener) {
        AlphaAnimation anim = new AlphaAnimation(1.0F, 0.0F);
        anim.setDuration((long) duration);
        anim.setStartOffset((long) startOffset);
        startAnimation(view, anim, visibility, listener);
    }

    public static void fadeItems(int duration, int startOffset, int visibility, View[] views) {
        AlphaAnimation anim = new AlphaAnimation(1.0F, 0.0F);
        anim.setDuration(duration);
        anim.setStartOffset(startOffset);
        for (int j = 0; j < views.length; j++) {
            startAnimation(views[j], anim, visibility, null);
        }
    }

    public static Animation getAbsoluteHorizontalSlideAnimation(float fromXValue, float toXValue, int duration) {
        TranslateAnimation anim = new TranslateAnimation(Animation.ABSOLUTE, fromXValue, Animation.ABSOLUTE, toXValue,
                Animation.RELATIVE_TO_SELF, 0.0F, Animation.RELATIVE_TO_SELF, 0.0F);
        anim.setDuration((long) duration);
        return anim;
    }

    public static Animation getHorizontalSlideAnimation(float fromXValue, float toXValue, int duration) {
        TranslateAnimation anim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, fromXValue,
                Animation.RELATIVE_TO_SELF, toXValue, Animation.RELATIVE_TO_SELF, 0.0F, Animation.RELATIVE_TO_SELF,
                0.0F);
        anim.setDuration((long) duration);
        return anim;
    }

    public static Animation getIdentityAnimation() {
        TranslateAnimation anim = new TranslateAnimation(0.0F, 0.0F, 0.0F, 0.0F);
        anim.setDuration(1L);
        return anim;
    }

    public static Animation getOffsetHorizontalSlideAnimation(int fromYValue, int fromXValue, int toXValue,
                                                              int duration, int startOffset) {
        TranslateAnimation anim = new TranslateAnimation(Animation.ABSOLUTE, (float) fromXValue, Animation.ABSOLUTE,
                (float) toXValue, Animation.ABSOLUTE, (float) fromYValue, Animation.ABSOLUTE, (float) fromYValue);
        anim.setDuration((long) duration);
        anim.setStartOffset((long) startOffset);
        anim.setInterpolator(new DecelerateInterpolator());
        return anim;
    }

    public static Animation getOffsetVerticalSlideAnimation(int fromXValue, int fromYValue, int toYValue, int duration,
                                                            int startOffset) {
        TranslateAnimation anim = new TranslateAnimation(Animation.ABSOLUTE, (float) fromXValue, Animation.ABSOLUTE,
                (float) fromXValue, Animation.ABSOLUTE, (float) fromYValue, Animation.ABSOLUTE, (float) toYValue);
        anim.setDuration((long) duration);
        anim.setStartOffset((long) startOffset);
        return anim;
    }

    public static Animation getRelativeHorizontalSlideAnimation(float fromXValue, float toXValue, int duration) {
        TranslateAnimation anim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, fromXValue,
                Animation.RELATIVE_TO_SELF, toXValue, Animation.RELATIVE_TO_SELF, 0.0F, Animation.RELATIVE_TO_SELF,
                0.0F);
        anim.setDuration(duration);
        return anim;
    }

    public static Animation getRelativeHorizontalSlideAnimation(float fromXValue, float toXValue) {
        return getRelativeHorizontalSlideAnimation(fromXValue, toXValue, DEFAULT_SLIDE_DURATION);
    }

    public static Animation getRelativeVerticalSlideAnimation(float fromYValue, float toYValue, int duration) {
        TranslateAnimation anim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0F, Animation.RELATIVE_TO_SELF,
                0.0F, Animation.RELATIVE_TO_SELF, fromYValue, Animation.RELATIVE_TO_SELF, toYValue);
        anim.setDuration(duration);
        return anim;
    }

    public static Animation getRelativeVerticalSlideAnimation(float fromYValue, float toYValue) {
        return getRelativeVerticalSlideAnimation(fromYValue, toYValue, DEFAULT_SLIDE_DURATION);
    }

    public static Animation getVerticalSlideAnimation(int fromYPos, int toYPos, int duration, int startOffset) {
        TranslateAnimation anim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0F, Animation.RELATIVE_TO_SELF,
                0.0F, Animation.ABSOLUTE, (float) fromYPos, Animation.ABSOLUTE, (float) toYPos);
        anim.setDuration((long) duration);
        anim.setStartOffset((long) startOffset);
        return anim;
    }

    public static Animation getAlphaAnimation(float fromAlpha, float toAlpha, int duration) {
        AlphaAnimation alphaAnim = new AlphaAnimation(fromAlpha, toAlpha);
        alphaAnim.setDuration(duration);
        return alphaAnim;
    }

    public static Animation getScaleAnimation(float fromX, float toX, float fromY, float toY, int duration) {
        ScaleAnimation scaleAnim = new ScaleAnimation(fromX, toX, fromY, toY, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnim.setDuration(duration);
        return scaleAnim;
    }

    public static Animation getRotateAnimation(float fromDegrees, float toDegrees, float pivotX, float pivotY,
                                               int duration, int repeatCount, Interpolator interpolator, boolean fillAfter) {
        RotateAnimation anim = new RotateAnimation(fromDegrees, toDegrees, Animation.RELATIVE_TO_SELF, pivotX,
                Animation.RELATIVE_TO_SELF, pivotY);
        anim.setRepeatCount(repeatCount);
        anim.setDuration(duration);
        anim.setInterpolator(interpolator == null ? new LinearInterpolator() : interpolator);
        anim.setFillAfter(fillAfter);
        return anim;
    }

    public static Animation getRelativeToParetnVerticalSlideAnimation(float fromX, float toX, float fromY, float toY,
                                                                      int duration, long startOff) {
        TranslateAnimation verticalSlideAnimation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, fromX,
                Animation.RELATIVE_TO_PARENT, toX, Animation.RELATIVE_TO_PARENT, fromY, Animation.RELATIVE_TO_PARENT,
                toY);
        verticalSlideAnimation.setDuration(duration);
        verticalSlideAnimation.setStartOffset(startOff);
        return verticalSlideAnimation;

    }

    public static void startAnimation(View view, Animation anim, int visibility, Animation.AnimationListener listener) {
        if (listener != null) {
            anim.setAnimationListener(listener);
        }
        if (view != null) { // 此处出现空指针异常.
            view.startAnimation(anim);
            view.setVisibility(visibility);

        }
    }

    public static void AlphaView(View view, float fromAlpha, float toAlpha, long durationMillis, boolean fillAfter,
                                 Animation.AnimationListener listener) {
        AlphaAnimation anim = new AlphaAnimation(fromAlpha, toAlpha);
        anim.setDuration(durationMillis);
        anim.setFillAfter(fillAfter);
        anim.setAnimationListener(listener);
        view.startAnimation(anim);
    }

    public static void scaleView(View view, float fromX, float toX, float fromY, float toY, long durationMillis,
                                 boolean fillAfter, Animation.AnimationListener listener) {
        ScaleAnimation anim = new ScaleAnimation(fromX, toX, fromY, toY, 1, 0.5F, 1, 0.5F);
        anim.setDuration(durationMillis);
        anim.setFillAfter(fillAfter);
        anim.setAnimationListener(listener);
        view.startAnimation(anim);
    }

    public static void animateView(View view, float fromXDelta, float toXDelta, float fromYDelta, float toYDelta,
                                   long durationMillis, boolean fillAfter, Animation.AnimationListener listener) {
        TranslateAnimation localTranslateAnimation = new TranslateAnimation(fromXDelta, toXDelta, fromYDelta, toYDelta);
        localTranslateAnimation.setDuration(durationMillis);
        localTranslateAnimation.setFillAfter(fillAfter);
        localTranslateAnimation.setAnimationListener(listener);
        view.startAnimation(localTranslateAnimation);
    }

    public static void translateView(View view, float dxFrom, float dxTo, float dyFrom, float dyTo, long duration,
                                     boolean fillafter, Animation.AnimationListener animListener) {
        TranslateAnimation anim = new TranslateAnimation(dxFrom, dxTo, dyFrom, dyTo);
        anim.setDuration(duration);
        anim.setFillAfter(fillafter);
        anim.setAnimationListener(animListener);
        view.startAnimation(anim);
    }

    public static void translateView(View view, int fromXType, float fromXValue, int toXType, float toXValue,
                                     int fromYType, float fromYValue, int toYType, float toYValue, long duration, boolean fillafter,
                                     Animation.AnimationListener animListener) {
        TranslateAnimation anim = new TranslateAnimation(fromXType, fromXValue, toXType, toXValue, fromYType,
                fromYValue, toYType, toYValue);
        anim.setDuration(duration);
        anim.setFillAfter(fillafter);
        anim.setAnimationListener(animListener);
        view.startAnimation(anim);
    }
}