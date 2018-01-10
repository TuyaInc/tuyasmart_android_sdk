package com.tuya.smart.android.demo.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.os.Vibrator;
import android.support.v4.view.ViewCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.alibaba.fastjson.JSON;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.tuya.smart.android.base.database.StorageHelper;
import com.tuya.smart.android.demo.R;
import com.tuya.smart.android.user.bean.CountryBean;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

/**
 * Created by mikeshou on 15/5/30.
 */
public class CommonUtil {
    public static final String URL_AY_STATIC = "http://static.airtakeapp.com/";
    public static final String URL_AZ_STATIC = "http://static.getairtake.com/";

    private static PowerManager.WakeLock wakeLock;

    private static int transferSeq = 0;

    public static String getStaticUrl(boolean isEn) {
        return isEn ? URL_AZ_STATIC : URL_AY_STATIC;
    }

    public static void hideIMM(Activity activity) {
        if (activity == null) {
            return;
        }
        View fcs = activity.getCurrentFocus();
        if (fcs == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(fcs.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static String formatTimer(int h) {
        return String.format("%02d", h);
    }

    public static String formatDate(long mill, String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        try {
            return dateFormat.format(new Date(mill));
        } catch (Exception e) {
            //e.printStackTrace();
            return null;
        }
    }

    public static String getAppVersionName(Context context) {
        String versionName = "0";
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionName = packageInfo.versionName;
            if (TextUtils.isEmpty(versionName)) {
                versionName = "0";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionName;
    }

    public static String getApplicationName(Context context, String packageName) {
        PackageManager packageManager = null;
        ApplicationInfo applicationInfo = null;
        try {
            packageManager = context.getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            applicationInfo = null;
        }
        String applicationName =
                (String) packageManager.getApplicationLabel(applicationInfo);
        return applicationName;
    }

    /**
     * 带应用内设置的语言判断
     *
     * @return
     */
    public static boolean isChineseLangWithSetting(String langKey) {
        int langIndex = StorageHelper.getIntValue(langKey, 0);
        boolean isChinese = false;
        switch (langIndex) {
            case 0:
                isChinese = isChineseLang();
                break;

            case 1:
                isChinese = true;
                break;

            default:
                isChinese = false;
                break;
        }

        return isChinese;
    }

    public static boolean isChineseLang() {
        String countryKey = Locale.getDefault().getCountry().toUpperCase();
        if (!TextUtils.isEmpty(countryKey)) {
            if (countryKey.equals("ZH")) return true;
        }
        Locale l = Locale.getDefault();
        String language = l.getLanguage();
        return language.equals("zh");
    }

    /**
     * 判断是否是中国
     * 1、手机卡国家区号
     * 2、时区判断
     *
     * @return
     */
    public static boolean isChina(Context ctx) {
        try {
            String countryCode = getCountryCode(ctx, null);
            if (TextUtils.isEmpty(countryCode)) {
                TimeZone tz = TimeZone.getDefault();
                String id = tz.getID();
                return TextUtils.equals(id, "Asia/Shanghai");
            }
            return TextUtils.equals(countryCode, "CN");
        } catch (Exception e) {
        }
        return false;
    }

    public static int isChinaByTimeZoneAndContryCode(Context ctx) {
        int ret = 0;
        try {
            String countryCode = getCountryCode(ctx, null);
            if (TextUtils.isEmpty(countryCode)) {
                TimeZone tz = TimeZone.getDefault();
                String id = tz.getID();
                return TextUtils.equals(id, "Asia/Shanghai") ? 1 : 0;
            }
            return TextUtils.equals(countryCode, "CN") ? 1 : 0;
        } catch (Exception e) {
            ret = -1;
        }
        return ret;
    }

    /**
     * 获取国家代码
     *
     * @param context
     * @param def     默认值
     * @return
     */
    public static String getCountryCode(Context context, String def) {
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            int phoneType = tm.getPhoneType();
            if (phoneType != TelephonyManager.PHONE_TYPE_CDMA) {
                return tm.getNetworkCountryIso().toUpperCase();
            }
        } catch (Exception e) {
        }
        return def;
    }

    public static PowerManager.WakeLock acquireLock(Context context) {
        if (wakeLock == null || !wakeLock.isHeld()) {
            PowerManager powerManager = (PowerManager) context
                    .getSystemService(Context.POWER_SERVICE);
            wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    "tuya");
            wakeLock.setReferenceCounted(true);
            wakeLock.acquire();
        }
        return wakeLock;
    }

    public static void releaseLock(Context context) {
        if (wakeLock != null && wakeLock.isHeld()) {
            wakeLock.release();
        }
    }

    public synchronized static int transferSeq() {
        return transferSeq < Short.MAX_VALUE ? ++transferSeq : 0;
    }


    public static void playMedia(Context ctx, boolean shake, boolean media) {
        try {
            if (media) {
                Uri actualDefaultRingtoneUri = RingtoneManager.getActualDefaultRingtoneUri(ctx, RingtoneManager.TYPE_NOTIFICATION);
                if (actualDefaultRingtoneUri != null) {
                    RingtoneManager.getRingtone(ctx, actualDefaultRingtoneUri).play();
                }
            }
            if (shake) {
                Vibrator vibrator = (Vibrator) ctx.getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(new long[]{10, 400}, -1);
            }
        } catch (Exception e) {

        }
    }

    public static int loadLocalDrawable(Context context, String name) {
        int resID = context.getResources().getIdentifier(name, "drawable", "com.tuya.smart");
        return resID;
    }

    /**
     * 通过seesionid获取uid
     *
     * @param sessionId
     * @return
     */
    public static String getUidBySessionId(String sessionId) {
        String str = sessionId.substring(0, sessionId.length() - 32);
        String str1 = str.substring(0, str.length() - 1);
        Integer str2 = Integer.valueOf(str.substring(str.length() - 1, str.length()));
        Integer uidLength = str1.length() - str2;
        float ff = (float) uidLength / 8;
        int div = (int) Math.ceil(ff);
        Integer start = 0;
        String str3 = "";
        for (int i = 0; i < div; i++) {
            Integer end = (i + 1) * 8;
            if (end > uidLength) {
                end = uidLength;
            }
            str3 += str1.substring(start, end + i);
            start = end + i + 1;
        }
        return str3;
    }

    /**
     * 是否tuya的url
     *
     * @return
     */
    public static boolean isTuyaUrl(String url) {
        Uri uri = Uri.parse(url);
        if (uri != null && uri.getHost() != null) {
            String host = uri.getHost();
            if (host != null && (host.contains("tuya.com") || host.contains("wgine.com")
                    || host.contains("getairtake.com") || host.contains("airtakeapp.com") || host.contains("airtake.me"))) {
                return true;
            }
        }
        return false;
    }

    /**
     * url检查
     *
     * @param url
     * @return
     */
    public static boolean checkUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return false;
        }
        try {
            url = URLDecoder.decode(url, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        }
        if (url.indexOf("http://") == -1 || url.indexOf("https://") == -1 || url.indexOf("file:///") == -1) {
            return true;
        }

        return false;
    }

    /**
     * 判断当前版本是否兼容目标版本的方法
     *
     * @param VersionCode
     * @return
     */
    public static boolean isMethodsCompat(int VersionCode) {
        return Build.VERSION.SDK_INT >= VersionCode;
    }

    /**
     * 颜色加深处理
     *
     * @param RGBValues RGB的值，由alpha（透明度）、red（红）、green（绿）、blue（蓝）构成，
     *                  Android中我们一般使用它的16进制，
     *                  例如："#FFAABBCC",最左边到最右每两个字母就是代表alpha（透明度）、
     *                  red（红）、green（绿）、blue（蓝）。每种颜色值占一个字节(8位)，值域0~255
     *                  所以下面使用移位的方法可以得到每种颜色的值，然后每种颜色值减小一下，在合成RGB颜色，颜色就会看起来深一些了
     * @return
     */
    public static int colorBurn(int RGBValues) {
        int alpha = RGBValues >> 24;
        int red = RGBValues >> 16 & 0xFF;
        int green = RGBValues >> 8 & 0xFF;
        int blue = RGBValues & 0xFF;
        red = (int) Math.floor(red * (1 - 0.1));
        green = (int) Math.floor(green * (1 - 0.1));
        blue = (int) Math.floor(blue * (1 - 0.1));
        return Color.rgb(red, green, blue);
    }

    //设置应用语言类型
    public static void switchLanguage(Context context, int index) {
        Resources resources = context.getResources();
        Configuration config = resources.getConfiguration();
        switch (index) {
            case 0:
                config.locale = Locale.getDefault();
                break;
            case 1:
                config.locale = Locale.SIMPLIFIED_CHINESE;
                break;
            case 2:
                config.locale = Locale.ENGLISH;
                break;
            case 3:
                config.locale = new Locale("es", "ES");
                break;
        }
        resources.updateConfiguration(config, null);
    }

    /**
     * 重启应用
     *
     * @param ctx
     */
    public static void restartApplication(Context ctx) {
        final Intent intent = ctx.getPackageManager().getLaunchIntentForPackage(ctx.getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ctx.startActivity(intent);
    }

    /**
     * 获取手机本地时区
     */
    public static String getTimeZone() {
        TimeZone tz = TimeZone.getDefault();
        String displayName = "+08:00";
        if (tz != null) {
            String str = tz.getDisplayName();
            if (!TextUtils.isEmpty(str)) {
                int indexOf = str.indexOf("+");
                if (indexOf == -1) indexOf = str.indexOf("-");
                if (indexOf != -1) {
                    displayName = str.substring(indexOf);
                }
                if (!displayName.contains(":")) {
                    displayName = displayName.substring(0, 3) + ":" + displayName.substring(3);
                }

            }
        }
        return displayName;
    }

    public static String getLanguage() {
        Locale l = Locale.getDefault();
        String language = l.getLanguage();
        String country = l.getCountry().toLowerCase();
        if ("zh".equals(language)) {
            if ("cn".equals(country)) {
                language = "zh";
            } else {
                language = "zh_hant";
            }
        }

        return language;
    }

    @SuppressWarnings("ResourceType")
    public static void initSystemBarColor(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(activity, true);
            SystemBarTintManager tintManager = new SystemBarTintManager(activity);
            tintManager.setStatusBarTintEnabled(true);
            TypedArray a = activity.obtainStyledAttributes(new int[]{
                    R.attr.status_system_bg_color, R.attr.status_bg_color});
            int setColor = a.getColor(0, -1);
            int statusBgColor = a.getColor(1, -1);
            //通知栏所需颜色
            if (setColor != -1) {
                tintManager.setStatusBarTintColor(setColor);
            } else if (statusBgColor != -1) {
                tintManager.setStatusBarTintColor(statusBgColor);
            }

            a.recycle();
            ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0).setFitsSystemWindows(true);//不设置全屏
        }
    }

    public static void initSystemBarColor(Activity activity, String color, boolean isFits) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(activity, true);
            SystemBarTintManager tintManager = new SystemBarTintManager(activity);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintColor(Color.parseColor(color));//通知栏所需颜色
            if (isFits)
                ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0).setFitsSystemWindows(true);//不设置全屏
        }
    }

    @TargetApi(19)
    protected static void setTranslucentStatus(Activity activity, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    public static void initSystemBarColor(Activity activity, int color) {
        if (activity == null) return;
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = activity.getWindow();
            //取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏颜色
            window.setStatusBarColor(color);
            ViewGroup mContentView = (ViewGroup) activity.findViewById(Window.ID_ANDROID_CONTENT);
            View mChildView = mContentView.getChildAt(0);
            if (mChildView != null) {
                mChildView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                //注意不是设置 ContentView 的 FitsSystemWindows, 而是设置 ContentView 的第一个子 View . 预留出系统 View 的空间.
                ViewCompat.setFitsSystemWindows(mChildView, true);
            }
        } else if (Build.VERSION.SDK_INT >= 19) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            SystemBarTintManager tintManager = new SystemBarTintManager(activity);
            tintManager.setStatusBarTintColor(color);
            tintManager.setStatusBarTintEnabled(true);
        }
    }

    /**
     * 获取顶部状态栏高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
            Log.v("@@@@@@", "the status bar height is : " + statusBarHeight);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }

    public static boolean isEmail(String data) {
        if (!TextUtils.isEmpty(data)) {
            Pattern p = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
            return p.matcher(data).matches();
        }

        return false;
    }

    /**
     * 去除 ’86-‘ 前缀
     *
     * @param mobile
     * @return
     */
    public static String getPhoneNumberFormMobile(String mobile) {
        if (TextUtils.isEmpty(mobile)) return "";
        int i = mobile.indexOf("-");
        if (i >= 0) {
            mobile = mobile.substring(i + 1);
        }
        return mobile;
    }

    /**
     * 获取正确的 phoneCode 格式
     *
     * @param phoneCode
     * @return
     */
    public static String getRightPhoneCode(String phoneCode) {
        return phoneCode.replace("-", "").replace("+", "");
    }

    public static ArrayList<CountryBean> getDefaultCountryData() {
        String countryData = "[{\"abbr\":\"AF\",\"chinese\":\"阿富汗\",\"code\":\"93\",\"english\":\"Afghanistan\",\"spell\":\"afuhan\"},{\"abbr\":\"AL\",\"chinese\":\"阿尔巴尼亚\",\"code\":\"355\",\"english\":\"Albania\",\"spell\":\"aerbaniya\"},{\"abbr\":\"DZ\",\"chinese\":\"阿尔及利亚\",\"code\":\"213\",\"english\":\"Algeria\",\"spell\":\"aerjiliya\"},{\"abbr\":\"AO\",\"chinese\":\"安哥拉\",\"code\":\"244\",\"english\":\"Angola\",\"spell\":\"angela\"},{\"abbr\":\"AR\",\"chinese\":\"阿根廷\",\"code\":\"54\",\"english\":\"Argentina\",\"spell\":\"agenting\"},{\"abbr\":\"AM\",\"chinese\":\"亚美尼亚\",\"code\":\"374\",\"english\":\"Armenia\",\"spell\":\"yameiniya\"},{\"abbr\":\"AU\",\"chinese\":\"澳大利亚\",\"code\":\"61\",\"english\":\"Australia\",\"spell\":\"aodaliya\"},{\"abbr\":\"AT\",\"chinese\":\"奥地利\",\"code\":\"43\",\"english\":\"Austria\",\"spell\":\"aodili\"},{\"abbr\":\"AZ\",\"chinese\":\"阿塞拜疆\",\"code\":\"994\",\"english\":\"Azerbaijan\",\"spell\":\"asaibaijiang\"},{\"abbr\":\"BH\",\"chinese\":\"巴林\",\"code\":\"973\",\"english\":\"Bahrain\",\"spell\":\"balin\"},{\"abbr\":\"BD\",\"chinese\":\"孟加拉国\",\"code\":\"880\",\"english\":\"Bangladesh\",\"spell\":\"mengjialaguo\"},{\"abbr\":\"BY\",\"chinese\":\"白俄罗斯\",\"code\":\"375\",\"english\":\"Belarus\",\"spell\":\"baieluosi\"},{\"abbr\":\"BE\",\"chinese\":\"比利时\",\"code\":\"32\",\"english\":\"Belgium\",\"spell\":\"bilishi\"},{\"abbr\":\"BZ\",\"chinese\":\"伯利兹\",\"code\":\"501\",\"english\":\"Belize\",\"spell\":\"bolizi\"},{\"abbr\":\"BJ\",\"chinese\":\"贝宁\",\"code\":\"229\",\"english\":\"Benin\",\"spell\":\"beining\"},{\"abbr\":\"BT\",\"chinese\":\"不丹\",\"code\":\"975\",\"english\":\"Bhutan\",\"spell\":\"budan\"},{\"abbr\":\"BO\",\"chinese\":\"玻利维亚\",\"code\":\"591\",\"english\":\"Bolivia\",\"spell\":\"boliweiya\"},{\"abbr\":\"BA\",\"chinese\":\"波斯尼亚和黑塞哥维那\",\"code\":\"387\",\"english\":\"Bosnia and Herzegovina\",\"spell\":\"bosiniyaheheisaigeweinei\"},{\"abbr\":\"BW\",\"chinese\":\"博茨瓦纳\",\"code\":\"267\",\"english\":\"Botswana\",\"spell\":\"bociwana\"},{\"abbr\":\"BR\",\"chinese\":\"巴西\",\"code\":\"55\",\"english\":\"Brazil\",\"spell\":\"baxi\"},{\"abbr\":\"VG\",\"chinese\":\"英属维京群岛\",\"code\":\"1284\",\"english\":\"British Virgin Islands\",\"spell\":\"yingshuweijingqundao\"},{\"abbr\":\"BN\",\"chinese\":\"文莱\",\"code\":\"673\",\"english\":\"Brunei\",\"spell\":\"wenlai\"},{\"abbr\":\"BG\",\"chinese\":\"保加利亚\",\"code\":\"359\",\"english\":\"Bulgaria\",\"spell\":\"baojialiya\"},{\"abbr\":\"BF\",\"chinese\":\"布基纳法索\",\"code\":\"226\",\"english\":\"Burkina-faso\",\"spell\":\"bujinafasuo\"},{\"abbr\":\"BI\",\"chinese\":\"布隆迪\",\"code\":\"257\",\"english\":\"Burundi\",\"spell\":\"bulongdi\"},{\"abbr\":\"KH\",\"chinese\":\"柬埔寨\",\"code\":\"855\",\"english\":\"Cambodia\",\"spell\":\"jianpuzhai\"},{\"abbr\":\"CM\",\"chinese\":\"喀麦隆\",\"code\":\"237\",\"english\":\"Cameroon\",\"spell\":\"kamailong\"},{\"abbr\":\"CA\",\"chinese\":\"加拿大\",\"code\":\"1\",\"english\":\"Canada\",\"spell\":\"jianada\"},{\"abbr\":\"CV\",\"chinese\":\"佛得角\",\"code\":\"238\",\"english\":\"Cape Verde\",\"spell\":\"fodejiao\"},{\"abbr\":\"KY\",\"chinese\":\"开曼群岛\",\"code\":\"1345\",\"english\":\"Cayman Islands\",\"spell\":\"kaimanqundao\"},{\"abbr\":\"CF\",\"chinese\":\"中非共和国\",\"code\":\"236\",\"english\":\"Central African Republic\",\"spell\":\"zhongfeigongheguo\"},{\"abbr\":\"TD\",\"chinese\":\"乍得\",\"code\":\"235\",\"english\":\"Chad\",\"spell\":\"zhade\"},{\"abbr\":\"CL\",\"chinese\":\"智利\",\"code\":\"56\",\"english\":\"Chile\",\"spell\":\"zhili\"},{\"abbr\":\"CN\",\"chinese\":\"中国\",\"code\":\"86\",\"english\":\"China\",\"spell\":\"zhongguo\"},{\"abbr\":\"CO\",\"chinese\":\"哥伦比亚\",\"code\":\"57\",\"english\":\"Colombia\",\"spell\":\"gelunbiya\"},{\"abbr\":\"KM\",\"chinese\":\"科摩罗\",\"code\":\"269\",\"english\":\"Comoros\",\"spell\":\"kemoluo\"},{\"abbr\":\"CG\",\"chinese\":\"刚果(布)\",\"code\":\"242\",\"english\":\"Congo - Brazzaville\",\"spell\":\"gangguo(bu)\"},{\"abbr\":\"CD\",\"chinese\":\"刚果(金)\",\"code\":\"243\",\"english\":\"Congo - Kinshasa\",\"spell\":\"gangguo(jin)\"},{\"abbr\":\"CR\",\"chinese\":\"哥斯达黎加\",\"code\":\"506\",\"english\":\"Costa Rica\",\"spell\":\"gesidalijia\"},{\"abbr\":\"HR\",\"chinese\":\"克罗地亚\",\"code\":\"385\",\"english\":\"Croatia\",\"spell\":\"keluodiya\"},{\"abbr\":\"CY\",\"chinese\":\"塞浦路斯\",\"code\":\"357\",\"english\":\"Cyprus\",\"spell\":\"saipulusi\"},{\"abbr\":\"CZ\",\"chinese\":\"捷克共和国\",\"code\":\"420\",\"english\":\"Czech Republic\",\"spell\":\"jiekegongheguo\"},{\"abbr\":\"DK\",\"chinese\":\"丹麦\",\"code\":\"45\",\"english\":\"Denmark\",\"spell\":\"danmai\"},{\"abbr\":\"DJ\",\"chinese\":\"吉布提\",\"code\":\"253\",\"english\":\"Djibouti\",\"spell\":\"jibuti\"},{\"abbr\":\"DO\",\"chinese\":\"多米尼加共和国\",\"code\":\"1809\",\"english\":\"Dominican Republic\",\"spell\":\"duominijiagongheguo\"},{\"abbr\":\"EC\",\"chinese\":\"厄瓜多尔\",\"code\":\"593\",\"english\":\"Ecuador\",\"spell\":\"eguaduoer\"},{\"abbr\":\"EG\",\"chinese\":\"埃及\",\"code\":\"20\",\"english\":\"Egypt\",\"spell\":\"aiji\"},{\"abbr\":\"SV\",\"chinese\":\"萨尔瓦多\",\"code\":\"503\",\"english\":\"EI Salvador\",\"spell\":\"saerwaduo\"},{\"abbr\":\"GQ\",\"chinese\":\"赤道几内亚\",\"code\":\"240\",\"english\":\"Equatorial Guinea\",\"spell\":\"chidaojineiya\"},{\"abbr\":\"ER\",\"chinese\":\"厄立特里亚\",\"code\":\"291\",\"english\":\"Eritrea\",\"spell\":\"eliteliya\"},{\"abbr\":\"EE\",\"chinese\":\"爱沙尼亚\",\"code\":\"372\",\"english\":\"Estonia\",\"spell\":\"aishaniya\"},{\"abbr\":\"ET\",\"chinese\":\"埃塞俄比亚\",\"code\":\"251\",\"english\":\"Ethiopia\",\"spell\":\"aisaiebiya\"},{\"abbr\":\"FJ\",\"chinese\":\"斐济\",\"code\":\"679\",\"english\":\"Fiji\",\"spell\":\"feiji\"},{\"abbr\":\"FI\",\"chinese\":\"芬兰\",\"code\":\"358\",\"english\":\"Finland\",\"spell\":\"fenlan\"},{\"abbr\":\"FR\",\"chinese\":\"法国\",\"code\":\"33\",\"english\":\"France\",\"spell\":\"faguo\"},{\"abbr\":\"GA\",\"chinese\":\"加蓬\",\"code\":\"241\",\"english\":\"Gabon\",\"spell\":\"jiapeng\"},{\"abbr\":\"GM\",\"chinese\":\"冈比亚\",\"code\":\"220\",\"english\":\"Gambia\",\"spell\":\"gangbiya\"},{\"abbr\":\"GE\",\"chinese\":\"格鲁吉亚\",\"code\":\"995\",\"english\":\"Georgia\",\"spell\":\"gelujiya\"},{\"abbr\":\"DE\",\"chinese\":\"德国\",\"code\":\"49\",\"english\":\"Germany\",\"spell\":\"deguo\"},{\"abbr\":\"GH\",\"chinese\":\"加纳\",\"code\":\"233\",\"english\":\"Ghana\",\"spell\":\"jiana\"},{\"abbr\":\"GR\",\"chinese\":\"希腊\",\"code\":\"30\",\"english\":\"Greece\",\"spell\":\"xila\"},{\"abbr\":\"GL\",\"chinese\":\"格陵兰\",\"code\":\"299\",\"english\":\"Greenland\",\"spell\":\"gelinglan\"},{\"abbr\":\"GT\",\"chinese\":\"危地马拉\",\"code\":\"502\",\"english\":\"Guatemala\",\"spell\":\"weidimala\"},{\"abbr\":\"GN\",\"chinese\":\"几内亚\",\"code\":\"224\",\"english\":\"Guinea\",\"spell\":\"jineiya\"},{\"abbr\":\"GY\",\"chinese\":\"圭亚那\",\"code\":\"592\",\"english\":\"Guyana\",\"spell\":\"guiyanei\"},{\"abbr\":\"HT\",\"chinese\":\"海地\",\"code\":\"509\",\"english\":\"Haiti\",\"spell\":\"haidi\"},{\"abbr\":\"HN\",\"chinese\":\"洪都拉斯\",\"code\":\"504\",\"english\":\"Honduras\",\"spell\":\"hongdoulasi\"},{\"abbr\":\"HK\",\"chinese\":\"中国香港特别行政区\",\"code\":\"852\",\"english\":\"Hongkong SAR China\",\"spell\":\"zhongguoxianggangtebiexingzhengqu\"},{\"abbr\":\"HU\",\"chinese\":\"匈牙利\",\"code\":\"36\",\"english\":\"Hungary\",\"spell\":\"xiongyali\"},{\"abbr\":\"IS\",\"chinese\":\"冰岛\",\"code\":\"354\",\"english\":\"Iceland\",\"spell\":\"bingdao\"},{\"abbr\":\"IN\",\"chinese\":\"印度\",\"code\":\"91\",\"english\":\"India\",\"spell\":\"yindu\"},{\"abbr\":\"ID\",\"chinese\":\"印度尼西亚\",\"code\":\"62\",\"english\":\"Indonesia\",\"spell\":\"yindunixiya\"},{\"abbr\":\"IR\",\"chinese\":\"伊朗\",\"code\":\"98\",\"english\":\"Iran\",\"spell\":\"yilang\"},{\"abbr\":\"IQ\",\"chinese\":\"伊拉克\",\"code\":\"964\",\"english\":\"Iraq\",\"spell\":\"yilake\"},{\"abbr\":\"IE\",\"chinese\":\"爱尔兰\",\"code\":\"353\",\"english\":\"Ireland\",\"spell\":\"aierlan\"},{\"abbr\":\"IM\",\"chinese\":\"马恩岛\",\"code\":\"44\",\"english\":\"Isle of Man\",\"spell\":\"maendao\"},{\"abbr\":\"IL\",\"chinese\":\"以色列\",\"code\":\"972\",\"english\":\"Israel\",\"spell\":\"yiselie\"},{\"abbr\":\"IT\",\"chinese\":\"意大利\",\"code\":\"39\",\"english\":\"Italy\",\"spell\":\"yidali\"},{\"abbr\":\"CI\",\"chinese\":\"科特迪瓦\",\"code\":\"225\",\"english\":\"Ivory Coast\",\"spell\":\"ketediwa\"},{\"abbr\":\"JM\",\"chinese\":\"牙买加\",\"code\":\"1876\",\"english\":\"Jamaica\",\"spell\":\"yamaijia\"},{\"abbr\":\"JP\",\"chinese\":\"日本\",\"code\":\"81\",\"english\":\"Japan\",\"spell\":\"riben\"},{\"abbr\":\"JO\",\"chinese\":\"约旦\",\"code\":\"962\",\"english\":\"Jordan\",\"spell\":\"yuedan\"},{\"abbr\":\"KZ\",\"chinese\":\"哈萨克斯坦\",\"code\":\"7\",\"english\":\"Kazakstan\",\"spell\":\"hasakesitan\"},{\"abbr\":\"KE\",\"chinese\":\"肯尼亚\",\"code\":\"254\",\"english\":\"Kenya\",\"spell\":\"kenniya\"},{\"abbr\":\"KR\",\"chinese\":\"韩国\",\"code\":\"82\",\"english\":\"Korea\",\"spell\":\"hanguo\"},{\"abbr\":\"KW\",\"chinese\":\"科威特\",\"code\":\"965\",\"english\":\"Kuwait\",\"spell\":\"keweite\"},{\"abbr\":\"KG\",\"chinese\":\"吉尔吉斯斯坦\",\"code\":\"996\",\"english\":\"Kyrgyzstan\",\"spell\":\"jierjisisitan\"},{\"abbr\":\"LA\",\"chinese\":\"老挝\",\"code\":\"856\",\"english\":\"Laos\",\"spell\":\"laowo\"},{\"abbr\":\"LV\",\"chinese\":\"拉脱维亚\",\"code\":\"371\",\"english\":\"Latvia\",\"spell\":\"latuoweiya\"},{\"abbr\":\"LB\",\"chinese\":\"黎巴嫩\",\"code\":\"961\",\"english\":\"Lebanon\",\"spell\":\"libanen\"},{\"abbr\":\"LS\",\"chinese\":\"莱索托\",\"code\":\"266\",\"english\":\"Lesotho\",\"spell\":\"laisuotuo\"},{\"abbr\":\"LR\",\"chinese\":\"利比里亚\",\"code\":\"231\",\"english\":\"Liberia\",\"spell\":\"libiliya\"},{\"abbr\":\"LY\",\"chinese\":\"利比亚\",\"code\":\"218\",\"english\":\"Libya\",\"spell\":\"libiya\"},{\"abbr\":\"LT\",\"chinese\":\"立陶宛\",\"code\":\"370\",\"english\":\"Lithuania\",\"spell\":\"litaowan\"},{\"abbr\":\"LU\",\"chinese\":\"卢森堡\",\"code\":\"352\",\"english\":\"Luxembourg\",\"spell\":\"lusenbao\"},{\"abbr\":\"MO\",\"chinese\":\"中国澳门特别行政区\",\"code\":\"853\",\"english\":\"Macao SAR China\",\"spell\":\"zhongguoaomentebiexingzhengqu\"},{\"abbr\":\"MK\",\"chinese\":\"马其顿\",\"code\":\"389\",\"english\":\"Macedonia\",\"spell\":\"maqidun\"},{\"abbr\":\"MG\",\"chinese\":\"马达加斯加\",\"code\":\"261\",\"english\":\"Madagascar\",\"spell\":\"madajiasijia\"},{\"abbr\":\"MW\",\"chinese\":\"马拉维\",\"code\":\"265\",\"english\":\"Malawi\",\"spell\":\"malawei\"},{\"abbr\":\"MY\",\"chinese\":\"马来西亚\",\"code\":\"60\",\"english\":\"Malaysia\",\"spell\":\"malaixiya\"},{\"abbr\":\"MV\",\"chinese\":\"马尔代夫\",\"code\":\"960\",\"english\":\"Maldives\",\"spell\":\"maerdaifu\"},{\"abbr\":\"ML\",\"chinese\":\"马里\",\"code\":\"223\",\"english\":\"Mali\",\"spell\":\"mali\"},{\"abbr\":\"MT\",\"chinese\":\"马耳他\",\"code\":\"356\",\"english\":\"Malta\",\"spell\":\"maerta\"},{\"abbr\":\"MR\",\"chinese\":\"毛利塔尼亚\",\"code\":\"222\",\"english\":\"Mauritania\",\"spell\":\"maolitaniya\"},{\"abbr\":\"MU\",\"chinese\":\"毛里求斯\",\"code\":\"230\",\"english\":\"Mauritius\",\"spell\":\"maoliqiusi\"},{\"abbr\":\"MX\",\"chinese\":\"墨西哥\",\"code\":\"52\",\"english\":\"Mexico\",\"spell\":\"moxige\"},{\"abbr\":\"MD\",\"chinese\":\"摩尔多瓦\",\"code\":\"373\",\"english\":\"Moldova\",\"spell\":\"moerduowa\"},{\"abbr\":\"MC\",\"chinese\":\"摩纳哥\",\"code\":\"377\",\"english\":\"Monaco\",\"spell\":\"monage\"},{\"abbr\":\"MN\",\"chinese\":\"蒙古\",\"code\":\"976\",\"english\":\"Mongolia\",\"spell\":\"menggu\"},{\"abbr\":\"ME\",\"chinese\":\"黑山共和国\",\"code\":\"382\",\"english\":\"Montenegro\",\"spell\":\"heishangongheguo\"},{\"abbr\":\"MA\",\"chinese\":\"摩洛哥\",\"code\":\"212\",\"english\":\"Morocco\",\"spell\":\"moluoge\"},{\"abbr\":\"MZ\",\"chinese\":\"莫桑比克\",\"code\":\"258\",\"english\":\"Mozambique\",\"spell\":\"mosangbike\"},{\"abbr\":\"MM\",\"chinese\":\"缅甸\",\"code\":\"95\",\"english\":\"Myanmar(Burma)\",\"spell\":\"miandian\"},{\"abbr\":\"NA\",\"chinese\":\"纳米比亚\",\"code\":\"264\",\"english\":\"Namibia\",\"spell\":\"namibiya\"},{\"abbr\":\"NP\",\"chinese\":\"尼泊尔\",\"code\":\"977\",\"english\":\"Nepal\",\"spell\":\"niboer\"},{\"abbr\":\"NL\",\"chinese\":\"荷兰\",\"code\":\"31\",\"english\":\"Netherlands\",\"spell\":\"helan\"},{\"abbr\":\"NZ\",\"chinese\":\"新西兰\",\"code\":\"64\",\"english\":\"New Zealand\",\"spell\":\"xinxilan\"},{\"abbr\":\"NI\",\"chinese\":\"尼加拉瓜\",\"code\":\"505\",\"english\":\"Nicaragua\",\"spell\":\"nijialagua\"},{\"abbr\":\"NE\",\"chinese\":\"尼日尔\",\"code\":\"227\",\"english\":\"Niger\",\"spell\":\"nirier\"},{\"abbr\":\"NG\",\"chinese\":\"尼日利亚\",\"code\":\"234\",\"english\":\"Nigeria\",\"spell\":\"niriliya\"},{\"abbr\":\"KP\",\"chinese\":\"朝鲜\",\"code\":\"850\",\"english\":\"North Korea\",\"spell\":\"chaoxian\"},{\"abbr\":\"NO\",\"chinese\":\"挪威\",\"code\":\"47\",\"english\":\"Norway\",\"spell\":\"nuowei\"},{\"abbr\":\"OM\",\"chinese\":\"阿曼\",\"code\":\"968\",\"english\":\"Oman\",\"spell\":\"aman\"},{\"abbr\":\"PK\",\"chinese\":\"巴基斯坦\",\"code\":\"92\",\"english\":\"Pakistan\",\"spell\":\"bajisitan\"},{\"abbr\":\"PA\",\"chinese\":\"巴拿马\",\"code\":\"507\",\"english\":\"Panama\",\"spell\":\"banama\"},{\"abbr\":\"PY\",\"chinese\":\"巴拉圭\",\"code\":\"595\",\"english\":\"Paraguay\",\"spell\":\"balagui\"},{\"abbr\":\"PE\",\"chinese\":\"秘鲁\",\"code\":\"51\",\"english\":\"Peru\",\"spell\":\"milu\"},{\"abbr\":\"PH\",\"chinese\":\"菲律宾\",\"code\":\"63\",\"english\":\"Philippines\",\"spell\":\"feilvbin\"},{\"abbr\":\"PL\",\"chinese\":\"波兰\",\"code\":\"48\",\"english\":\"Poland\",\"spell\":\"bolan\"},{\"abbr\":\"PT\",\"chinese\":\"葡萄牙\",\"code\":\"351\",\"english\":\"Portugal\",\"spell\":\"putaoya\"},{\"abbr\":\"PR\",\"chinese\":\"波多黎各\",\"code\":\"1787\",\"english\":\"Puerto Rico\",\"spell\":\"boduolige\"},{\"abbr\":\"QA\",\"chinese\":\"卡塔尔\",\"code\":\"974\",\"english\":\"Qatar\",\"spell\":\"kataer\"},{\"abbr\":\"RE\",\"chinese\":\"留尼旺\",\"code\":\"262\",\"english\":\"Reunion\",\"spell\":\"liuniwang\"},{\"abbr\":\"RO\",\"chinese\":\"罗马尼亚\",\"code\":\"40\",\"english\":\"Romania\",\"spell\":\"luomaniya\"},{\"abbr\":\"RU\",\"chinese\":\"俄罗斯\",\"code\":\"7\",\"english\":\"Russia\",\"spell\":\"eluosi\"},{\"abbr\":\"RW\",\"chinese\":\"卢旺达\",\"code\":\"250\",\"english\":\"Rwanda\",\"spell\":\"luwangda\"},{\"abbr\":\"SM\",\"chinese\":\"圣马力诺\",\"code\":\"378\",\"english\":\"San Marino\",\"spell\":\"shengmalinuo\"},{\"abbr\":\"SA\",\"chinese\":\"沙特阿拉伯\",\"code\":\"966\",\"english\":\"Saudi Arabia\",\"spell\":\"shatealabo\"},{\"abbr\":\"SN\",\"chinese\":\"塞内加尔\",\"code\":\"221\",\"english\":\"Senegal\",\"spell\":\"saineijiaer\"},{\"abbr\":\"RS\",\"chinese\":\"塞尔维亚\",\"code\":\"381\",\"english\":\"Serbia\",\"spell\":\"saierweiya\"},{\"abbr\":\"SL\",\"chinese\":\"塞拉利昂\",\"code\":\"232\",\"english\":\"Sierra Leone\",\"spell\":\"sailaliang\"},{\"abbr\":\"SG\",\"chinese\":\"新加坡\",\"code\":\"65\",\"english\":\"Singapore\",\"spell\":\"xinjiapo\"},{\"abbr\":\"SK\",\"chinese\":\"斯洛伐克\",\"code\":\"421\",\"english\":\"Slovakia\",\"spell\":\"siluofake\"},{\"abbr\":\"SI\",\"chinese\":\"斯洛文尼亚\",\"code\":\"386\",\"english\":\"Slovenia\",\"spell\":\"siluowenniya\"},{\"abbr\":\"SO\",\"chinese\":\"索马里\",\"code\":\"252\",\"english\":\"Somalia\",\"spell\":\"suomali\"},{\"abbr\":\"ZA\",\"chinese\":\"南非\",\"code\":\"27\",\"english\":\"South Africa\",\"spell\":\"nanfei\"},{\"abbr\":\"ES\",\"chinese\":\"西班牙\",\"code\":\"34\",\"english\":\"Spain\",\"spell\":\"xibanya\"},{\"abbr\":\"LK\",\"chinese\":\"斯里兰卡\",\"code\":\"94\",\"english\":\"Sri Lanka\",\"spell\":\"sililanka\"},{\"abbr\":\"SD\",\"chinese\":\"苏丹\",\"code\":\"249\",\"english\":\"Sudan\",\"spell\":\"sudan\"},{\"abbr\":\"SR\",\"chinese\":\"苏里南\",\"code\":\"597\",\"english\":\"Suriname\",\"spell\":\"sulinan\"},{\"abbr\":\"SZ\",\"chinese\":\"斯威士兰\",\"code\":\"268\",\"english\":\"Swaziland\",\"spell\":\"siweishilan\"},{\"abbr\":\"SE\",\"chinese\":\"瑞典\",\"code\":\"46\",\"english\":\"Sweden\",\"spell\":\"ruidian\"},{\"abbr\":\"CH\",\"chinese\":\"瑞士\",\"code\":\"41\",\"english\":\"Switzerland\",\"spell\":\"ruishi\"},{\"abbr\":\"SY\",\"chinese\":\"叙利亚\",\"code\":\"963\",\"english\":\"Syria\",\"spell\":\"xuliya\"},{\"abbr\":\"TW\",\"chinese\":\"中国台湾\",\"code\":\"886\",\"english\":\"Taiwan\",\"spell\":\"zhongguotaiwan\"},{\"abbr\":\"TJ\",\"chinese\":\"塔吉克斯坦\",\"code\":\"992\",\"english\":\"Tajikstan\",\"spell\":\"tajikesitan\"},{\"abbr\":\"TZ\",\"chinese\":\"坦桑尼亚\",\"code\":\"255\",\"english\":\"Tanzania\",\"spell\":\"tansangniya\"},{\"abbr\":\"TH\",\"chinese\":\"泰国\",\"code\":\"66\",\"english\":\"Thailand\",\"spell\":\"taiguo\"},{\"abbr\":\"TG\",\"chinese\":\"多哥\",\"code\":\"228\",\"english\":\"Togo\",\"spell\":\"duoge\"},{\"abbr\":\"TO\",\"chinese\":\"汤加\",\"code\":\"676\",\"english\":\"Tonga\",\"spell\":\"tangjia\"},{\"abbr\":\"TT\",\"chinese\":\"特立尼达和多巴哥\",\"code\":\"1868\",\"english\":\"Trinidad and Tobago\",\"spell\":\"telinidaheduobage\"},{\"abbr\":\"TN\",\"chinese\":\"突尼斯\",\"code\":\"216\",\"english\":\"Tunisia\",\"spell\":\"tunisi\"},{\"abbr\":\"TR\",\"chinese\":\"土耳其\",\"code\":\"90\",\"english\":\"Turkey\",\"spell\":\"tuerqi\"},{\"abbr\":\"TM\",\"chinese\":\"土库曼斯坦\",\"code\":\"993\",\"english\":\"Turkmenistan\",\"spell\":\"tukumansitan\"},{\"abbr\":\"VI\",\"chinese\":\"美属维尔京群岛\",\"code\":\"1340\",\"english\":\"U.S. Virgin Islands\",\"spell\":\"meishuweierjingqundao\"},{\"abbr\":\"UG\",\"chinese\":\"乌干达\",\"code\":\"256\",\"english\":\"Uganda\",\"spell\":\"wuganda\"},{\"abbr\":\"UA\",\"chinese\":\"乌克兰\",\"code\":\"380\",\"english\":\"Ukraine\",\"spell\":\"wukelan\"},{\"abbr\":\"AE\",\"chinese\":\"阿拉伯联合酋长国\",\"code\":\"971\",\"english\":\"United Arab Emirates\",\"spell\":\"alabolianheqiuzhangguo\"},{\"abbr\":\"GB\",\"chinese\":\"英国\",\"code\":\"44\",\"english\":\"United Kiongdom\",\"spell\":\"yingguo\"},{\"abbr\":\"US\",\"chinese\":\"美国\",\"code\":\"1\",\"english\":\"USA\",\"spell\":\"meiguo\"},{\"abbr\":\"UY\",\"chinese\":\"乌拉圭\",\"code\":\"598\",\"english\":\"Uruguay\",\"spell\":\"wulagui\"},{\"abbr\":\"UZ\",\"chinese\":\"乌兹别克斯坦\",\"code\":\"998\",\"english\":\"Uzbekistan\",\"spell\":\"wuzibiekesitan\"},{\"abbr\":\"VA\",\"chinese\":\"梵蒂冈城\",\"code\":\"379\",\"english\":\"Vatican City\",\"spell\":\"fandigangcheng\"},{\"abbr\":\"VE\",\"chinese\":\"委内瑞拉\",\"code\":\"58\",\"english\":\"Venezuela\",\"spell\":\"weineiruila\"},{\"abbr\":\"VN\",\"chinese\":\"越南\",\"code\":\"84\",\"english\":\"Vietnam\",\"spell\":\"yuenan\"},{\"abbr\":\"YE\",\"chinese\":\"也门\",\"code\":\"967\",\"english\":\"Yemen\",\"spell\":\"yemen\"},{\"abbr\":\"YU\",\"chinese\":\"南斯拉夫\",\"code\":\"381\",\"english\":\"Yugoslavia\",\"spell\":\"nansilafu\"},{\"abbr\":\"ZR\",\"chinese\":\"扎伊尔\",\"code\":\"243\",\"english\":\"Zaire\",\"spell\":\"zhayier\"},{\"abbr\":\"ZM\",\"chinese\":\"赞比亚\",\"code\":\"260\",\"english\":\"Zambia\",\"spell\":\"zanbiya\"},{\"abbr\":\"ZW\",\"chinese\":\"津巴布韦\",\"code\":\"263\",\"english\":\"Zimbabwe\",\"spell\":\"jinbabuwei\"}]";
        return (ArrayList<CountryBean>) JSON.parseArray(countryData, CountryBean.class);
    }
}
