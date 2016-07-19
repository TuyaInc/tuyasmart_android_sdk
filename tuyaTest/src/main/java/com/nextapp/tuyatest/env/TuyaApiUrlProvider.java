package com.nextapp.tuyatest.env;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.tuya.smart.android.common.utils.TyCommonUtil;
import com.tuya.smart.android.network.ApiUrlProvider;
import com.tuya.smart.android.network.TuyaSmartNetWork;
import com.tuya.smart.android.user.bean.Domain;

/**
 * Created by lee on 16/5/7.
 */
public class TuyaApiUrlProvider extends ApiUrlProvider {
    public static final int ONLINE = 0;
    public static final int DAILY = 1;
    private static final String PRIVIEW_DOMAIN = "{\n" +
            "  \"AY\": {\n" +
            "    \"mobileApiUrl\": \"https://a1.cn.wgine.com/api.json\",\n" +
            "    \"mobileMqttUrl\": \"mq.mb.cn.wgine.com\",\n" +
            "    \"gwApiUrl\": \"http://a.gw.cn.wgine.com/gw.json\",\n" +
            "    \"gwMqttUrl\": \"mq.gw.cn.wgine.com\"\n" +
            "  },\n" +
            "  \"AZ\": {\n" +
            "    \"mobileApiUrl\": \"https://a1.us.wgine.com/api.json\",\n" +
            "    \"mobileMqttUrl\": \"mq.mb.us.wgine.com\",\n" +
            "    \"gwApiUrl\": \"http://a.gw.us.wgine.com/gw.json\",\n" +
            "    \"gwMqttUrl\": \"mq.gw.us.wgine.com\"\n" +
            "  },\n" +
            "  \"EU\": {\n" +
            "    \"mobileApiUrl\": \"https://a1.eu.wgine.com/api.json\",\n" +
            "    \"mobileMqttUrl\": \"mq.mb.eu.wgine.com\",\n" +
            "    \"gwApiUrl\": \"http://a.gw.eu.wgine.com/gw.json\",\n" +
            "    \"gwMqttUrl\": \"mq.gw.eu.wgine.com\"\n" +
            "  }\n" +
            "}";

    private int mEnv;
    public TuyaApiUrlProvider(Context cxt, int env) {
        super(cxt);
        mEnv = env;
        switch (mEnv) {
            case ONLINE:
                break;
            case DAILY:
            default:
                TuyaSmartNetWork.RegionConfig mRegion  = TyCommonUtil.getDefaultRegion(cxt);
                JSONObject previewDomains = JSONObject.parseObject(PRIVIEW_DOMAIN);
                Domain mDefaultDomain = previewDomains.getObject(mRegion.getId(),Domain.class);
                setDefaultDomain(mDefaultDomain);
                setDomainJson(PRIVIEW_DOMAIN);
                break;
        }

    }

    @Override
    protected String getOldApiUrl() {
        switch (mEnv) {
            case ONLINE:
                return super.getOldApiUrl();
            case DAILY:
            default:
                return mRegion == TuyaSmartNetWork.RegionConfig.AY ? "https://a1.mb.cn.wgine.com/api.json" : "https://a1.mb.us.wgine.com/api.json";
        }
    }

    @Override
    protected String[] getOldMqttUrl() {
        if(mEnv == ONLINE){
            return super.getOldMqttUrl();
        }else{
            return mRegion == TuyaSmartNetWork.RegionConfig.AY ? new String[]{"mq.mb.cn.wgine.com", "mq.mb.cn.wgine.com"} : new String[]{"mq.mb.us.wgine.com", "mq.mb.us.wgine.com"};
        }
    }

    @Override
    protected String getOldGwApiUrl() {
        if(mEnv == ONLINE){
            return super.getOldGwApiUrl();
        }else{
            return mRegion == TuyaSmartNetWork.RegionConfig.AY ? "http://a.gw.cn.wgine.com/gw.json" : "http://a.gw.us.wgine.com/gw.json";
        }
    }

    @Override
    protected String[] getOldGwMqttUrl() {
        if (mEnv == ONLINE) {
            return super.getOldGwMqttUrl();
        }
        else{
            return mRegion == TuyaSmartNetWork.RegionConfig.AY ? new String[]{"mq.gw.cn.wgine.com", "mq.gw.cn.wgine.com"} : new String[]{"mq.gw.us.wgine.com", "mq.gw.us.wgine.com"};
        }
    }
}
