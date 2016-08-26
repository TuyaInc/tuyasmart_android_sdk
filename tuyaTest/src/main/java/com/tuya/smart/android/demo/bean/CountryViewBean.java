package com.tuya.smart.android.demo.bean;

import com.tuya.smart.android.demo.widget.contact.ContactItemInterface;
import com.tuya.smart.android.common.utils.TyCommonUtil;
import com.tuya.smart.sdk.TuyaSdk;

public class CountryViewBean implements ContactItemInterface {

    private String countryName;
    private String key;
    private String number;
    private String pinyin;


    private boolean isChinese;

    public CountryViewBean(String key, String countryName, String number, String pinyin) {
        super();
        this.countryName = countryName;
        this.key = key;
        this.number = number;
        this.pinyin = pinyin;
        isChinese = TyCommonUtil.isZh(TuyaSdk.getApplication());
    }

    // index the list by nickname
    @Override
    public String getItemForIndex() {
        if (isChinese) return pinyin;
        else return countryName;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    @Override
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public boolean isChinese() {
        return isChinese;
    }

    public void setChinese(boolean isChinese) {
        this.isChinese = isChinese;
    }
}
