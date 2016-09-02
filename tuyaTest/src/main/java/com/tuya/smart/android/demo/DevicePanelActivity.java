package com.tuya.smart.android.demo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tuya.smart.android.common.utils.StringUtils;
import com.tuya.smart.android.device.TuyaSmartDevice;
import com.tuya.smart.android.device.api.IGetDataPointStatCallback;
import com.tuya.smart.android.device.api.IHardwareUpdateInfo;
import com.tuya.smart.android.device.bean.DataPointStatBean;
import com.tuya.smart.android.device.bean.HardwareUpgradeBean;
import com.tuya.smart.android.device.bean.UpgradeInfoBean;
import com.tuya.smart.android.device.enums.DataPointTypeEnum;
import com.tuya.smart.android.hardware.model.IControlCallback;
import com.tuya.smart.sdk.TuyaDevice;
import com.tuya.smart.sdk.TuyaTimerManager;
import com.tuya.smart.sdk.api.IFirmwareUpgradeListener;
import com.tuya.smart.sdk.api.IGetAllTimerWithDevIdCallback;
import com.tuya.smart.sdk.api.IGetDeviceTimerStatusCallback;
import com.tuya.smart.sdk.api.IGetTimerWithTaskCallback;
import com.tuya.smart.sdk.api.IResultStatusCallback;
import com.tuya.smart.sdk.bean.Timer;
import com.tuya.smart.sdk.bean.TimerTask;
import com.tuya.smart.sdk.bean.TimerTaskStatus;
import com.tuya.smart.sdk.enums.FirmwareUpgradeEnum;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by mikeshou on 15/12/8.
 */
public class DevicePanelActivity extends Activity {
    private static final int MSG_UPDATE_TV_INFO = 99;
    private static final String INFO_KEY = "info_key";
    private TuyaTimerManager mTuyaTimerManager;


    @Bind(R.id.device_status)
    TextView mDeviceStatus;
    @Bind(R.id.check_updage)
    Button mCheckUpdage;
    @Bind(R.id.update_start)
    Button mUpdateStart;
    @Bind(R.id.update_end)
    Button mUpdateEnd;
    @Bind(R.id.update)
    LinearLayout mUpdate;
    @Bind(R.id.command_text)
    EditText mCommandText;
    @Bind(R.id.command_submit)
    Button mCommandSubmit;
    @Bind(R.id.command)
    LinearLayout mCommand;
    @Bind(R.id.delete_device)
    Button mDeleteDevice;
    @Bind(R.id.get_history_data)
    Button mGetHistoryData;
    private TuyaDevice mTuyaDevice;
    private TextView mTvInfo;
    private ListView mLVTimer;

    private TimerAdapter adapter;

    String mGwId, mDevId;

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == MSG_UPDATE_TV_INFO) {
                mTvInfo.setText(msg.getData().getString(INFO_KEY));
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_pannel);
        ButterKnife.bind(this);
        mCommandText.setText("{\"1\":\"MjI=\"}");
        mDevId = mGwId = getIntent().getStringExtra("gwId");

        mTuyaTimerManager = new TuyaTimerManager();
        mTvInfo = (TextView) findViewById(R.id.tv_info);
        mLVTimer = (ListView) findViewById(R.id.lv_timer);
        ((TextView) findViewById(R.id.device_status)).setText(R.string.device_state + (TuyaSmartDevice.getInstance().getGw(mGwId).isOnline() ? "inline " : "offline ") + (TuyaSmartDevice.getInstance().getGw(mGwId).getGwBean().getIsShare() ? "share device" : "manage device"));

        adapter = new TimerAdapter(this);
        mLVTimer.setAdapter(adapter);
        mLVTimer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Timer timer = adapter.getTimer(position);
                TimerTask timerTask = adapter.getTimerTask(position);
                if (timer != null && timerTask != null) {
                    onChooseTimerOver(timerTask.getTimerTaskStatus().getTimerName(), timer.getTimerId());
                } else {
                    Toast.makeText(DevicePanelActivity.this, R.string.cant_find_alarm_attr, Toast.LENGTH_LONG).show();
                }
                mLVTimer.setVisibility(View.GONE);
            }
        });
        /**
         * 设备对象。该设备的所有dp变化都会通过callback返回。
         *
         * 初始化设备之前，请确保已经连接mqtt，否则无法获取到服务端返回信息
         */
        mTuyaDevice = new TuyaDevice(mDevId);
        /**
         * 设置固件升级监听
         * 共享的设备会收到相应的监听
         */

        mTuyaDevice.setHardwareUpgradeListener(new IFirmwareUpgradeListener() {
            @Override
            public void onSuccess(FirmwareUpgradeEnum firmwareUpgradeEnum) {
                //固件升级成功
                //FirmwareUpgradeEnum 固件类型:（网关或者设备）
            }

            @Override
            public void onFailure(FirmwareUpgradeEnum firmwareUpgradeEnum, String code, String error) {
                //固件升级失败
                //FirmwareUpgradeEnum 固件类型:（网关或者设备）
            }

            @Override
            public void onProgress(FirmwareUpgradeEnum firmwareUpgradeEnum, int progress) {
                //FirmwareUpgradeEnum 固件类型:（网关或者设备）
                //固件升级进度
            }
        });


        final EditText editText = (EditText) findViewById(R.id.command_text);
        /**
         * 发送控制指令。回调函数会通知指令是否下发成功。注意：下发不代表硬件会执行该命令。
         */
        findViewById(R.id.command_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /**
                 * 注意：RAW型数据 以十六进制，偶数位的显示发送。
                 */
                mTuyaDevice.publishDps(mCommandText.getText().toString(), new IControlCallback() {
                    @Override
                    public void onError(String code, String error) {
                        Toast.makeText(DevicePanelActivity.this, R.string.send_command + R.string.unit_success + code + error, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess() {
                        Toast.makeText(DevicePanelActivity.this, R.string.send_command + R.string.unit_failure, Toast.LENGTH_SHORT).show();
                    }

                });

            }
        });

        /**
         * 移除设备
         */
        findViewById(R.id.delete_device).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TuyaSmartDevice.getInstance().removeDevice(DevicePanelActivity.this, mGwId, mDevId, new IControlCallback() {

                    @Override
                    public void onError(String code, String error) {
                        Toast.makeText(DevicePanelActivity.this, R.string.remove_device + R.string.unit_failure + " : " + error, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess() {
                        Toast.makeText(DevicePanelActivity.this, R.string.remove_device + R.string.unit_success, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


        /**
         * 开始固件升级
         *
         */
        findViewById(R.id.update_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //升级设备
                mTuyaDevice.upgradeFirmware(FirmwareUpgradeEnum.TY_DEV);
                //升级网关
//                mTuyaDevice.upgradeFirmware(FirmwareUpgradeEnum.TY_GW);
            }
        });

        /**
         * 取消固件升级
         */
        findViewById(R.id.update_end).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //取消固件升级监听
                mTuyaDevice.stopHardwareUpgrade();
            }
        });

//        mTuyaDevice.renameDevice("milking machine", new IControlCallback() {
//            @Override
//            public void onError(String code, String error) {
//
//            }
//
//            @Override
//            public void onSuccess() {
//
//            }
//        });
        /**
         * 获得硬件的固件升级信息
         */
        findViewById(R.id.check_updage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTuyaDevice.getFirmwareUpgradeInfo(new IHardwareUpdateInfo() {
                    @Override
                    public void onError(String code, String error) {
                        Toast.makeText(DevicePanelActivity.this, R.string.version_check + " : " + error, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(HardwareUpgradeBean info) {
                        UpgradeInfoBean dev = info.getDev();
                        if (dev != null) {
                            Toast.makeText(DevicePanelActivity.this, String.format(getString(R.string.firmware_upgrad_tips0), dev.getVersion(), dev.getCurrentVersion(), dev.getDesc()), Toast.LENGTH_SHORT).show();
                        }
                        UpgradeInfoBean gw = info.getGw();
                        //upgradeStatus - 0:无新版本 1:有新版本 2:在升级中
                        //upgradeType - 0:app提醒升级 2-app强制升级 3-检测升级
                        if (gw != null) {
                            Toast.makeText(DevicePanelActivity.this, String.format(getString(R.string.firmware_upgrad_tips1), gw.getVersion(), gw.getCurrentVersion(), dev.getDesc(), gw.getUpgradeStatus(), gw.getUpgradeType()), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

//        mTuyaSmartPanel.renameGw("设备1", new IControlCallback() {
//            @Override
//            public void onError(String code, String error) {
//                //重命名失败
//            }
//
//            @Override
//            public void onSuccess() {
//                //重命名成功
//            }
//        });

    }

    @OnClick(R.id.get_history_data)
    public void setGetHistoryData() {
        // DataPointTypeEnum 获取历史数据结果类型 (hour、day、month)
        //
        long startTime = System.currentTimeMillis(); //startTime起始时间
        int number = 12;//往前获取历史数据结果值的个数 ，最大是50
        String dpId = "1";
        mTuyaDevice.getDataPointStat(DataPointTypeEnum.DAY, startTime, number, dpId, new IGetDataPointStatCallback() {
            @Override
            public void onError(String errorCode, String errorMsg) {
                Toast.makeText(DevicePanelActivity.this, R.string.get_history_data + R.string.unit_failure + " : " + errorMsg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(DataPointStatBean bean) {
                Toast.makeText(DevicePanelActivity.this, R.string.get_history_data + R.string.unit_success, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int mClickId = -1;
    private int mAddCounts = 0;
    private int mTimerTaskCount = 0;
    private String[] times = new String[]{"14:29", "15:12", "1:10"};

    private void sendTextInfoMessage(String info) {
        Message msg = new Message();
        msg.what = MSG_UPDATE_TV_INFO;

        Bundle bundle = new Bundle();
        bundle.putString(INFO_KEY, info);
        msg.setData(bundle);
        mHandler.sendMessage(msg);
    }

    private void showTimerChooseList(int id) {
        if (adapter.getCount() == 0) {
            Toast.makeText(this, R.string.no_timer_data, Toast.LENGTH_LONG).show();
        } else {
            mLVTimer.setVisibility(View.VISIBLE);
            mClickId = id;
        }
    }

    private void onChooseTimerOver(String taskName, String timeId) {
        switch (mClickId) {
            case R.id.bt_update_task_status:
                mTuyaTimerManager.operateTimerInTask(taskName, mDevId, false, new IResultStatusCallback() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(DevicePanelActivity.this, R.string.control_timer_task_switch_status + R.string.unit_success, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        Toast.makeText(DevicePanelActivity.this, R.string.control_timer_task_switch_status + R.string.unit_failure + " : " + errorMsg, Toast.LENGTH_LONG).show();
                    }
                });
                break;

            case R.id.bt_update_timer_status:
                mTuyaTimerManager.operateTimer(taskName, mDevId, timeId, false, new IResultStatusCallback() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(DevicePanelActivity.this, R.string.control_timer_switch_status + R.string.unit_success, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        Toast.makeText(DevicePanelActivity.this, R.string.control_timer_switch_status + R.string.unit_failure + " : " + errorMsg, Toast.LENGTH_LONG).show();
                    }
                });
                break;

            case R.id.bt_del_timer:
                mTuyaTimerManager.removeTimer(taskName, mDevId, timeId, new IResultStatusCallback() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(DevicePanelActivity.this, R.string.delete_timer + R.string.unit_success, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        Toast.makeText(DevicePanelActivity.this, R.string.delete_timer + R.string.unit_failure + " : " + errorMsg, Toast.LENGTH_LONG).show();
                    }
                });
                break;

            case R.id.bt_update_timer:
                mTuyaTimerManager.updateTimerStatus(taskName, mDevId, timeId, "0011001", "11:11", false, new IResultStatusCallback() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(DevicePanelActivity.this, R.string.update_timer + R.string.unit_success, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        Toast.makeText(DevicePanelActivity.this, R.string.update_timer + R.string.unit_failure + " : " + errorMsg, Toast.LENGTH_LONG).show();
                    }
                });
                break;

            case R.id.bt_get_timer_4_task:
                mTuyaTimerManager.getTimerWithTask(taskName, mDevId, new IGetTimerWithTaskCallback() {
                    @Override
                    public void onSuccess(TimerTask timerTask) {
                        sendTextInfoMessage(timerTask.toString());
                        adapter.updateData(timerTask);
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        Toast.makeText(DevicePanelActivity.this, R.string.get_device_timer_status_failure + " : " + errorMsg, Toast.LENGTH_LONG).show();
                    }
                });
                break;
        }
    }

    public void onTimerClick(View v) {
        switch (v.getId()) {
            case R.id.bt_add_timer:
                mTuyaTimerManager.addTimerWithTask("task" + mTimerTaskCount, mDevId, "1111111", "2", times[mAddCounts], new IResultStatusCallback() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(DevicePanelActivity.this, R.string.add_alarm_timer + R.string.unit_success, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        Toast.makeText(DevicePanelActivity.this, R.string.add_alarm_timer + R.string.unit_failure + " : " + errorMsg, Toast.LENGTH_LONG).show();
                    }
                });
                mAddCounts++;
                if (mAddCounts >= 3) {
                    mAddCounts = 0;
                    mTimerTaskCount++;
                }

                break;

            case R.id.bt_get_status_dev:
                mTuyaTimerManager.getTimerTaskStatusWithDeviceId(mDevId, new IGetDeviceTimerStatusCallback() {
                    @Override
                    public void onSuccess(ArrayList<TimerTaskStatus> list) {
                        String info = "";
                        if (list != null && !list.isEmpty()) {
                            for (TimerTaskStatus status : list) {
                                info += status.toString();
                            }
                        } else {
                            info = getString(R.string.get_device_timer_status_null);
                        }
                        sendTextInfoMessage(info);
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        Toast.makeText(DevicePanelActivity.this, R.string.get_device_timer_status_failure + " : " + errorMsg, Toast.LENGTH_LONG).show();
                    }
                });
                break;

            case R.id.bt_get_timer_4_task:
            case R.id.bt_update_timer:
            case R.id.bt_update_task_status:
            case R.id.bt_update_timer_status:
            case R.id.bt_del_timer:
                showTimerChooseList(v.getId());
                break;

            case R.id.bt_get_timer_4_dev:
                mTuyaTimerManager.getAllTimerWithDeviceId(mDevId, new IGetAllTimerWithDevIdCallback() {
                    @Override
                    public void onSuccess(ArrayList<TimerTask> taskArrayList) {
                        sendTextInfoMessage(StringUtils.arrayListToString(taskArrayList));
                        adapter.updateData(taskArrayList);
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        Toast.makeText(DevicePanelActivity.this, R.string.get_device_timer_status_failure + errorMsg, Toast.LENGTH_LONG).show();
                    }
                });
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTuyaDevice.onDestroy();
    }

    class TimerAdapter extends BaseAdapter {
        private ArrayList<TimerTask> mTaskArrayList;
        private Context mContext;

        TimerAdapter(Context context) {
            mTaskArrayList = new ArrayList<>();
            mContext = context;
        }

        @Override
        public int getCount() {
            int count = 0;
            for (TimerTask task : mTaskArrayList) {
                ArrayList<Timer> timers = task.getTimerList();
                if (timers != null && !timers.isEmpty()) {
                    count += timers.size();
                }
            }
            return count;
        }

        public void updateData(TimerTask task) {
            mTaskArrayList.clear();
            mTaskArrayList.add(task);
            notifyDataSetChanged();
        }

        public void updateData(ArrayList<TimerTask> list) {
            mTaskArrayList.clear();
            mTaskArrayList.addAll(list);
            notifyDataSetChanged();
        }

        private TimerTask getTimerTask(int position) {
            TimerTask timerTask = null;
            for (TimerTask task : mTaskArrayList) {
                ArrayList<Timer> timers = task.getTimerList();
                if (timers == null || timers.isEmpty()) {
                    continue;
                }

                if (position < timers.size()) {
                    timerTask = task;
                    break;
                } else {
                    position -= task.getTimerList().size();
                }
            }

            return timerTask;
        }

        private Timer getTimer(int position) {
            Timer desTimer = null;
            for (TimerTask task : mTaskArrayList) {
                ArrayList<Timer> timers = task.getTimerList();
                if (timers == null || timers.isEmpty()) {
                    continue;
                }

                if (position < task.getTimerList().size()) {
                    desTimer = task.getTimerList().get(position);
                    break;
                } else {
                    position -= task.getTimerList().size();
                }
            }

            return desTimer;
        }

        private String getShownText(int position) {
            TimerTask timerTask = getTimerTask(position);
            Timer desTimer = getTimer(position);

            if (desTimer != null) {
                return timerTask.getTimerTaskStatus().getTimerName() + " " + desTimer.toString();
            } else {
                return "not found";
            }
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = new TextView(mContext);
            }

            ((TextView) convertView).setText(getShownText(position));

            return convertView;
        }
    }
}
