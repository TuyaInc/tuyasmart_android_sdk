package com.tuya.smart.android.demo.utils;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.tuya.smart.android.demo.bean.CountryViewBean;
import com.tuya.smart.android.common.utils.L;
import com.tuya.smart.android.common.utils.TyCommonUtil;
import com.tuya.smart.android.user.bean.CountryBean;
import com.tuya.smart.sdk.TuyaSdk;
import com.tuya.smart.android.demo.widget.contact.ContactItemInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;


public class CountryUtils {
    public static final HashMap<String, String> mIdWithNumHashMap = new HashMap<>();
    public static final HashMap<String, String> mIdWithEngHashMap = new HashMap<>();
    public static final HashMap<String, String> mIdWithChiHashMap = new HashMap<>();
    public static final HashMap<String, String> mIdWithPinHashMap = new HashMap<>();
    public static final String TAG = "CountryData";
    public static boolean isPutInMap = false;

    public static void initCountryData(final CountryDataGetListener listener) {
        putCountryDataToMap(CommonUtil.getDefaultCountryData());
        if (listener != null) {
            listener.onSuccess();
        }
    }

    public static List<ContactItemInterface> getSampleContactList() {
        if (!isPutInMap) {
            isPutInMap = true;
            putCountryDataToMap(CommonUtil.getDefaultCountryData());
        }
        List<ContactItemInterface> list = new ArrayList<ContactItemInterface>();
        Iterator iter = null;
        if (TyCommonUtil.isZh(TuyaSdk.getApplication())) {
            iter = mIdWithChiHashMap.entrySet().iterator();
        } else {
            iter = mIdWithEngHashMap.entrySet().iterator();
        }
        while (iter.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry) iter.next();
            String key = entry.getKey();
            String val = entry.getValue();
            list.add(new CountryViewBean(key, val, mIdWithNumHashMap.get(key), mIdWithPinHashMap.get(key)));
        }
        return list;
    }

    public static void putCountryDataToMap(ArrayList<CountryBean> countryDatas) {
        int size = countryDatas.size();
        L.d(TAG, "size" + size);
        for (int i = 0; i < size; i++) {
            CountryBean country = countryDatas.get(i);
            String key = country.getAbbr();
            mIdWithChiHashMap.put(key, country.getChinese());
            mIdWithEngHashMap.put(key, country.getEnglish());
            mIdWithNumHashMap.put(key, country.getCode());
            mIdWithPinHashMap.put(key, country.getSpell());
        }
    }

    public static String getCountryNum(String key) {
        if (!isPutInMap) {
            isPutInMap = true;
            putCountryDataToMap(CommonUtil.getDefaultCountryData());
        }

        if (TextUtils.isEmpty(key)) return "";

        return mIdWithNumHashMap.get(key);
    }

    public static String getCountryTitle(String key) {
        if (!isPutInMap) {
            isPutInMap = true;
            putCountryDataToMap(CommonUtil.getDefaultCountryData());
        }

        if (TextUtils.isEmpty(key)) return "";

        if (TyCommonUtil.isZh(TuyaSdk.getApplication())) {
            return mIdWithChiHashMap.get(key);
        } else {
            return mIdWithEngHashMap.get(key);
        }
    }

    public static String getCountryKey(Context context) {
        String countryKey = "";
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            countryKey = tm.getNetworkCountryIso().toUpperCase();
        } catch (Exception e) {
        }

        return countryKey;
    }

    public static String getCountryDefault(Context context) {
        TimeZone tz = TimeZone.getDefault();
        String id = tz.getID();
        if (TextUtils.equals(id, "Asia/Shanghai")) {
            return "CN";
        }
        if (id.startsWith("Europe")) {
            return "DE";
        } else {
            return "US";
        }
//        if (TextUtils.isEmpty(countryKey)) {
//            countryKey = Locale.getDefault().getCountry().toUpperCase();
//        }
    }

    public interface CountryDataGetListener {
        public void onSuccess();
    }
}
