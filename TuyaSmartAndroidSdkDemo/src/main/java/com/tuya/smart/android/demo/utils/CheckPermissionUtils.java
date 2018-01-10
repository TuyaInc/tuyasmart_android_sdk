package com.tuya.smart.android.demo.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.text.TextUtils;

import com.tuya.smart.android.demo.R;
import com.tuya.smart.android.demo.test.utils.DialogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by liusihai on 16/2/25.
 * 针对6.0及以上系统，主动坚持权限以获取对应的权限授权。
 * <p/>
 * 这个类的抽象好奇怪 lee
 */
public class CheckPermissionUtils {

    //注意使用的request_code必须与activity使用的不同
    public static final int ALL_REQUEST_CODE = 0x10; //进行所有所需权限的检查
    private static final int PHONE_REQUEST_CODE = 0x11;
    private static final int STORAGE_REQUEST_CODE = 0x12;
    private static final int LOCATION_REQUEST_CODE = 0x13;
    private static final int CONTACTS_REQUEST_CODE = 0x14;
    private static final int WRITE_SETTINGS_REQUEST_CODE = 0x15;
    private static final int OVERLAY_REQUEST_CODE = 0x16;

    private Activity mActivity;

    public CheckPermissionUtils(Activity activity) {
        mActivity = activity;
    }

    /**
     * 检查当前应用的授权情况
     *
     * @param requestCode 表明已经完成检查某个权限的requestCode
     * @return true: 所有的权限已经得到授权； false : 部分权限未得到授权
     */
    public boolean checkPermission(int requestCode) {
        if (Build.VERSION.SDK_INT < 23) {//低于6.0是直接授权状态
            return true;
        }

        return permissionCheck() && requestOtherPermissions();
    }

    public boolean hasPermission(String permission) {
        int targetSdkVersion = 0;
        try {
            final PackageInfo info = mActivity.getPackageManager().getPackageInfo(
                    mActivity.getPackageName(), 0);
            targetSdkVersion = info.applicationInfo.targetSdkVersion;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        boolean result = true;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (targetSdkVersion >= Build.VERSION_CODES.M) {
                // targetSdkVersion >= Android M, we can
                // use Context#checkSelfPermission
                result = ContextCompat.checkSelfPermission(mActivity, permission)
                        == PackageManager.PERMISSION_GRANTED;
            } else {
                // targetSdkVersion < Android M, we have to use PermissionChecker
                result = PermissionChecker.checkSelfPermission(mActivity, permission)
                        == PermissionChecker.PERMISSION_GRANTED;
            }
        }

        return result;
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean addPermission(List<String> permissionsList, String permission) {
        boolean has = hasPermission(permission);
        if (!has) {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (!ActivityCompat.shouldShowRequestPermissionRationale(mActivity, permission)) {
                return false;
            }
        }

        return has;
    }


    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;

    private boolean permissionCheck() {
        List<String> permissionsNeeded = new ArrayList<String>();

        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList, Manifest.permission.READ_PHONE_STATE)) {
            permissionsNeeded.add("READ_PHONE_STATE");
        }

        if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            permissionsNeeded.add("WRITE_EXTERNAL_STORAGE");
        }

        if (!addPermission(permissionsList, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            permissionsNeeded.add("ACCESS_COARSE_LOCATION");
        }
        if (!addPermission(permissionsList, Manifest.permission.CAMERA)) {
            permissionsNeeded.add("CAMERA");
        }
        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                // Need Rationale
                ActivityCompat.requestPermissions(mActivity, permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                return false;
            }
            ActivityCompat.requestPermissions(mActivity, permissionsList.toArray(new String[permissionsList.size()]),
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            return false;
        }

        return true;
    }

    public boolean checkSiglePermission(String permission, int resultCode) {
        boolean hasWriteContactsPermission = true;
        if (Build.VERSION.SDK_INT < 23) {
            return true;
        } else {
            hasWriteContactsPermission = hasPermission(permission);
        }

        if (!hasWriteContactsPermission) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.WRITE_CONTACTS)) {
                ActivityCompat.requestPermissions(mActivity, new String[]{permission},
                        resultCode);
                return false;
            }
            ActivityCompat.requestPermissions(mActivity, new String[]{permission},
                    resultCode);
            return false;
        }

        return true;
    }

    public boolean onRequestPermissionsResult(String[] permissions, int[] grantResults) {
        //单个权限的请求过程
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 所有权限的返回处理 for splash
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     * @return
     */
    public boolean onRequestAllPermissionResultNew(int requestCode, String[] permissions, int[] grantResults) {
        if (REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS == requestCode) {
            Map<String, Integer> perms = new HashMap<String, Integer>();
            // Initial
            perms.put(Manifest.permission.READ_PHONE_STATE, PackageManager.PERMISSION_GRANTED);
            perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
            perms.put(Manifest.permission.ACCESS_COARSE_LOCATION, PackageManager.PERMISSION_GRANTED);
            // Fill with results
            for (int i = 0; i < permissions.length; i++) {
                perms.put(permissions[i], grantResults[i]);
            }

            Map<String, String> tipMap = new HashMap<>();
            tipMap.put(Manifest.permission.READ_PHONE_STATE, mActivity.getString(R.string.ty_permission_tip_phone_state));
            tipMap.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, mActivity.getString(R.string.ty_permission_tip_write_storage));
            tipMap.put(Manifest.permission.ACCESS_COARSE_LOCATION, mActivity.getString(R.string.ty_permission_tip_location));

            String permisionTip = "";
            Iterator iter = perms.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                String key = (String) entry.getKey();
                int val = (int) entry.getValue();
                if (PackageManager.PERMISSION_GRANTED != val) {
                    permisionTip += tipMap.get(key) + "\n";
                }
            }

            if (TextUtils.isEmpty(permisionTip)) {
                // All Permissions Granted
                return requestOtherPermissions();
            } else {
                // Permission Denied
                String tipOri = mActivity.getString(R.string.ty_permission_tip_title) + "\n" + permisionTip
                        + mActivity.getString(R.string.ty_permission_tip_tail);
                DialogUtil.simpleSmartDialog(mActivity, formatTip(tipOri), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        i.addCategory(Intent.CATEGORY_DEFAULT);
                        i.setData(Uri.parse("package:" + mActivity.getPackageName()));
                        mActivity.startActivity(i);
                        mActivity.finish();
                    }
                });
            }
        }

        return false;
    }

    private String formatTip(String ori) {
        return String.format(ori, " " + mActivity.getString(R.string.app_name) + " ");
    }

    /**
     * 修改全局系统设置
     *
     * @return true: 拥有权限；false: 未授权，进行授权检查。
     */
    private boolean requestOtherPermissions() {
        boolean isNeedOtherPermissions = true;

        if (Build.VERSION.SDK_INT >= 23) {//大于等于6.0是需要授权
            if (!Settings.System.canWrite(mActivity)) {
                isNeedOtherPermissions = false;
                requestSystemSettingPermission();
            }
        }

        return isNeedOtherPermissions;
    }

    private void gotoSettingPermission() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS,
                Uri.parse("package:" + mActivity.getPackageName()));
        mActivity.startActivityForResult(intent, WRITE_SETTINGS_REQUEST_CODE);
    }

    private void requestSystemSettingPermission() {
        String tipOri = mActivity.getString(R.string.ty_permission_tip_read_set);
        DialogUtil.simpleConfirmDialog(mActivity, formatTip(tipOri), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    gotoSettingPermission();
                } else {
                    mActivity.finish();
                }
            }
        });
    }

    public boolean onActivityResult(int requestCode) {
        if (Build.VERSION.SDK_INT >= 23 && requestCode == WRITE_SETTINGS_REQUEST_CODE) { //大于等于6.0是需要授权
            String errorPermission = "";

            if (!Settings.System.canWrite(mActivity)) {
                errorPermission = "WRITE_SETTINGS";
            }
//            else
//            if (!Settings.canDrawOverlays(mActivity)) {
//                errorPermission = "SYSTEM_ALERT_WINDOW";
//            }

            if (!TextUtils.isEmpty(errorPermission)) {
                requestSystemSettingPermission();
                return false;
            } else {
                return true;
            }
        }

        return false;
    }
}
