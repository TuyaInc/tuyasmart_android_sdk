package com.tuya.smart.android.demo.app;

import android.app.Activity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by letian on 15/2/9.
 */
public class Constant {

    private static ArrayList<WeakReference<Activity>> activityStack = new ArrayList<WeakReference<Activity>>();

    public static final String TAG = "tuya";

    public static void exitApplication() {
        finishActivity();
//        SyncServerTask.getInstance().onDestroy();
    }

    public static void detachActivity(Activity activity) {
        WeakReference<Activity> act = new WeakReference<Activity>(activity);
        activityStack.remove(act);
    }

    public static void finishActivity() {
        // System.out.println(activityStack.size());
        for (WeakReference<Activity> activity : activityStack) {
            if (activity != null && activity.get() != null) activity.get().finish();
        }
        activityStack.clear();
    }

    public static void finishLastActivity(int finishNum) {
        if (activityStack == null) return;
        int num = 1;
        ArrayList<WeakReference<Activity>> activityStacks = new ArrayList<WeakReference<Activity>>();
        int size = activityStack.size();
        for (int i = size - 1; i >= 0; i--) {
            WeakReference<Activity> activity = activityStack.get(i);
            if (activity != null && activity.get() != null) {
                activity.get().finish();
                activityStacks.add(activity);
                if (num++ == finishNum) break;
            }
        }

        for (WeakReference<Activity> activity : activityStacks) {
            activityStack.remove(activity);
        }
        activityStacks.clear();
    }

    public static void attachActivity(Activity activity) {
        WeakReference<Activity> act = new WeakReference<Activity>(activity);
        if (activityStack.indexOf(act) == -1) activityStack.add(act);
    }
}
