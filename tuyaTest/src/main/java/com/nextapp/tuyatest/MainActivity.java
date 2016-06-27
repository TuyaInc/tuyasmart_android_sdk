package com.nextapp.tuyatest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.tuya.smart.android.device.TuyaSmartDevice;
import com.tuya.smart.android.device.TuyaSmartEasyConnect;
import com.tuya.smart.android.device.api.response.GwDevResp;
import com.tuya.smart.android.device.link.EZConfigBuilder;
import com.tuya.smart.android.device.link.IConnectListener;
import com.tuya.smart.android.device.mqtt.IMqttCallback;
import com.tuya.smart.android.user.TuyaSmartUserManager;
import com.tuya.smart.android.user.api.ILogoutCallback;
import com.tuya.smart.android.user.api.IReNickNameCallback;

public class MainActivity extends Activity implements IConnectListener {

    private TextView mDemoLogTextView;

    private TuyaSmartEasyConnect mTuyaSmartEasyConnect;

    private TuyaSmartDevice mTuyaSmartDevice;
    private String mSsid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDemoLogTextView = (TextView) findViewById(R.id.demo_log);

        /**
         * 创建smartlink配网对象
         *
         * 在配网之前请确保用户已经登录
         *
         * bssid从当前连接的wifi中获取。
         *
         * passwd当前WIFI的密码
         */
//        Context mContext = MainActivity.this;
//        //初始化回调接口
//        IConnectListener mConnectListener = new IConnectListener() {
//            @Override
//            public void onConfigStart() {
//                //开始配置
//            }
//
//            @Override
//            public void onConfigEnd() {
//                //配置结束
//            }
//
//            @Deprecated
//            @Override
//            public void onWifiError(String errorCode) {
//
//            }
//
//            @Override
//            public void onDeviceFind(String devId) {
//                //发现设备
//            }
//
//            @Override
//            public void onDeviceBindSuccess(GwDevResp device) {
//                //设备注册到智能云
//            }
//
//            @Override
//            public void onActiveSuccess(GwDevResp device) {
//                //绑定成功且设备上线
//            }
//
//            @Override
//            public void onActiveError(String code, String error) {
//                //激活失败
//            }
//        };
//
//        //配置相应参数
//        EZConfigBuilder builder = new EZConfigBuilder(mContext)
//                //wifi 的ssid名称
//                .setSsid("airtake")
//                //wifi 的密码
//                .setPasswd("20112012pw")
//                //TuyaLink配网监听回调
//                .setConnectListener(mConnectListener);
//
//        //初始化Tuyalink配网对象
//        mTuyaSmartEasyConnect = new TuyaSmartEasyConnect(builder);
//
//        //开始配置
//        mTuyaSmartEasyConnect.startConfig();
//
//        //停止配置
//        mTuyaSmartEasyConnect.stopConfig();
//
//        //回调销毁
//        mTuyaSmartEasyConnect.onDestroy();

        mTuyaSmartEasyConnect = new TuyaSmartEasyConnect(new EZConfigBuilder(MainActivity.this).setSsid(getSsid()).setPasswd("20112012pw").setConnectListener(MainActivity.this));
        findViewById(R.id.demo_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDemoLogTextView.setText("");
                mTuyaSmartEasyConnect.startConfig();
//
            }
        });

        findViewById(R.id.ap_demo_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, APActivityTest.class));
            }
        });

        findViewById(R.id.demo_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTuyaSmartEasyConnect.stopConfig();
            }
        });


        findViewById(R.id.goto_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ShareActivity.class));
            }
        });
        /**
         * 用户登录成功才能初始化设备对象
         */
        mTuyaSmartDevice = TuyaSmartDevice.getInstance().initDeviceList(new IMqttCallback() {
            @Override
            public void onConnectSuccess() {
                //设备通信初始化成功
            }

            @Override
            public void onConnectError(int code, String error) {
                //设备通信初始化失败
            }

            @Override
            public void onSubscribeSuccess(String topic) {
            }


            @Override
            public void onSubscribeError(String topic, int code, String error) {
            }

        });
        findViewById(R.id.do_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 退出登录接口。请在用户退出的时候调用。
                 */
                TuyaSmartUserManager.getInstance().logout(new ILogoutCallback() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(MainActivity.this, "退出登录成功", Toast.LENGTH_SHORT).show();
                        TuyaSmartUserManager.getInstance().removeUser();
                        finish();
                    }

                    @Override
                    public void onError(String code, String error) {
                        Toast.makeText(MainActivity.this, "退出登录失败", Toast.LENGTH_SHORT).show();
                        TuyaSmartUserManager.getInstance().removeUser();
                        finish();
                    }

                });
            }
        });

        findViewById(R.id.btn_rename).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TuyaSmartUserManager.getInstance().reRickName("小李", new IReNickNameCallback() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(MainActivity.this, "修改昵称成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(String code, String error) {
                        Toast.makeText(MainActivity.this, "修改昵称失败", Toast.LENGTH_SHORT).show();
                    }
                });
//                Map<String, Object> map = new HashMap<>();
//                map.put("nickname","昵称");
//                TuyaSmartRequest.getInstance().requestWithApiName("s.m.user.update", "1.0", map, new IRequestCallback() {
//                    @Override
//                    public void onSuccess(Object result) {
//                        if ((Boolean) result) {
//                            Toast.makeText(MainActivity.this, "修改昵称成功", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(String errorCode, String errorMsg) {
//                        Toast.makeText(MainActivity.this, "修改昵称失败", Toast.LENGTH_SHORT).show();
//                    }
//                });
            }
        });


        /**
         * 发送控制指令。回调函数会通知指令是否下发成功。注意：下发不代表硬件会执行该命令。
         */
        findViewById(R.id.command_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TuyaSmartUserManager.getInstance().isLogin()) {

                    startActivity(new Intent(MainActivity.this, DeviceListActivity.class));
                } else {
                    Toast.makeText(MainActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
                }
            }
        });


//        //从云端获取设备列表
//        TuyaSmartDevice.getInstance().queryGwList();
//        //云端查询单个设备信息
//        TuyaSmartDevice.getInstance().queryGw(String gwId);
//        //销毁监听事件，清理缓存数据，需要在退出时进行调用
//        TuyaSmartDevice.getInstance().onDestroy();
//        //获取设备所有dp数据
//        TuyaSmartDevice.getInstance().getDps(String gwId, String devId);
//        //获取设备单个dp点数据
//        TuyaSmartDevice.getInstance().getDp(String gwId, String devId, String dpId);
//        //从本地缓存中获取所有设备列表
//        TuyaSmartDevice.getInstance().getGws();
//        //获取设备schema信息
//        TuyaSmartDevice.getInstance().getSchema(String gwId, String devId);
        //移除设备
//        TuyaSmartDevice.getInstance().removeDevice(Context context, final String gwId, String devId, final IControlCallback callback);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /**
         * 配置结束后请调用销毁方法
         */
        mTuyaSmartEasyConnect.onDestroy();

        /**
         * 退出设备管理界面的时候请调用销毁方法
         */
        if (mTuyaSmartDevice != null) {
            mTuyaSmartDevice.onDestroy();
        }
    }

    @Override
    public void onConfigStart() {
        mDemoLogTextView.append("onConfigStart\n");
    }

    @Override
    public void onConfigEnd() {
        mDemoLogTextView.append("onConfigEnd\n");
    }

    @Override
    public void onWifiError(String errorCode) {
        mDemoLogTextView.append("wifi error: " + errorCode + "\n");
    }

    @Override
    public void onDeviceFind(String devId) {
        //发现设备
    }

    @Override
    public void onDeviceBindSuccess(GwDevResp device) {
        //设备注册到机智云
    }

    @Override
    public void onActiveSuccess(GwDevResp device) {
        mDemoLogTextView.append("onActiveSuccess: " + device.getGwId() + " \n");
    }

    @Override
    public void onActiveError(String code, String error) {
        mDemoLogTextView.append("onActiveError: " + error + " \n");
    }

    @Override
    protected void onStop() {
        super.onStop();

    }
//
//    private String getBssid() {
//        WifiManager mWiFiMgr = (WifiManager) getSystemService(Context.WIFI_SERVICE);
//        WifiInfo mWiFiInf = mWiFiMgr.getConnectionInfo();
//        return mWiFiInf == null ? "" : mWiFiInf.getBSSID();
//    }

    public String getSsid() {

        ConnectivityManager connManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (networkInfo.isConnected()) {
            final WifiManager wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
            final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
            if (connectionInfo != null) {
                String ssid = connectionInfo.getSSID();
                if (Build.VERSION.SDK_INT >= 17 && ssid.startsWith("\"") && ssid.endsWith("\""))
                    ssid = ssid.replaceAll("^\"|\"$", "");
                return ssid;
            }
        }
        return null;
    }
}

