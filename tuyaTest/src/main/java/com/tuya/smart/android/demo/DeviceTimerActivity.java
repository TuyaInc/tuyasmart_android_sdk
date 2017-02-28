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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tuya.smart.android.common.utils.StringUtils;
import com.tuya.smart.android.demo.activity.BaseActivity;
import com.tuya.smart.sdk.TuyaDevice;
import com.tuya.smart.sdk.TuyaTimerManager;
import com.tuya.smart.sdk.api.IGetAllTimerWithDevIdCallback;
import com.tuya.smart.sdk.api.IGetDeviceTimerStatusCallback;
import com.tuya.smart.sdk.api.IGetTimerWithTaskCallback;
import com.tuya.smart.sdk.api.IResultStatusCallback;
import com.tuya.smart.sdk.bean.Timer;
import com.tuya.smart.sdk.bean.TimerTask;
import com.tuya.smart.sdk.bean.TimerTaskStatus;

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * Created by mikeshou on 15/12/8.
 */
public class DeviceTimerActivity extends BaseActivity {
    private static final int MSG_UPDATE_TV_INFO = 99;
    private static final String INFO_KEY = "info_key";
    private TuyaTimerManager mTuyaTimerManager;

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
        setContentView(R.layout.activity_device_timer);
        ButterKnife.bind(this);
        initToolbar();
        setDisplayHomeAsUpEnabled();
        setTitle(R.string.add_alarm_timer);
        mDevId = mGwId = getIntent().getStringExtra("gwId");
        mTuyaTimerManager = new TuyaTimerManager();
        mTuyaDevice = new TuyaDevice(mDevId);
        mTvInfo = (TextView) findViewById(R.id.tv_info);
        mLVTimer = (ListView) findViewById(R.id.lv_timer);
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
                    Toast.makeText(DeviceTimerActivity.this, R.string.cant_find_alarm_attr, Toast.LENGTH_LONG).show();
                }
                mLVTimer.setVisibility(View.GONE);
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
                        Toast.makeText(DeviceTimerActivity.this, getString(R.string.control_timer_task_switch_status) + getString(R.string.unit_success), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        Toast.makeText(DeviceTimerActivity.this, getString(R.string.control_timer_task_switch_status) + getString(R.string.unit_failure) + " : " + errorMsg, Toast.LENGTH_LONG).show();
                    }
                });
                break;

            case R.id.bt_update_timer_status:
                mTuyaTimerManager.operateTimer(taskName, mDevId, timeId, false, new IResultStatusCallback() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(DeviceTimerActivity.this, getString(R.string.control_timer_switch_status) + getString(R.string.unit_success), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        Toast.makeText(DeviceTimerActivity.this, getString(R.string.control_timer_switch_status) + getString(R.string.unit_failure) + " : " + errorMsg, Toast.LENGTH_LONG).show();
                    }
                });
                break;

            case R.id.bt_del_timer:
                mTuyaTimerManager.removeTimer(taskName, mDevId, timeId, new IResultStatusCallback() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(DeviceTimerActivity.this, getString(R.string.delete_timer) + getString(R.string.unit_success), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        Toast.makeText(DeviceTimerActivity.this, getString(R.string.delete_timer) + getString(R.string.unit_failure) + " : " + errorMsg, Toast.LENGTH_LONG).show();
                    }
                });
                break;

            case R.id.bt_update_timer:
                mTuyaTimerManager.updateTimerStatus(taskName, mDevId, timeId, "0011001", "11:11", false, new IResultStatusCallback() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(DeviceTimerActivity.this, getString(R.string.update_timer) + getString(R.string.unit_success), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        Toast.makeText(DeviceTimerActivity.this, getString(R.string.update_timer) + getString(R.string.unit_failure) + " : " + errorMsg, Toast.LENGTH_LONG).show();
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
                        Toast.makeText(DeviceTimerActivity.this, getString(R.string.get_device_timer_status_failure) + " : " + errorMsg, Toast.LENGTH_LONG).show();
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
                        Toast.makeText(DeviceTimerActivity.this, getString(R.string.add_alarm_timer) + getString(R.string.unit_success), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        Toast.makeText(DeviceTimerActivity.this, getString(R.string.add_alarm_timer) + getString(R.string.unit_failure) + " : " + errorMsg, Toast.LENGTH_LONG).show();
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
                        Toast.makeText(DeviceTimerActivity.this, getString(R.string.get_device_timer_status_failure) + " : " + errorMsg, Toast.LENGTH_LONG).show();
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
                        Toast.makeText(DeviceTimerActivity.this, getString(R.string.get_device_timer_status_failure) + errorMsg, Toast.LENGTH_LONG).show();
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

    static class TimerAdapter extends BaseAdapter {
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

        void updateData(TimerTask task) {
            mTaskArrayList.clear();
            mTaskArrayList.add(task);
            notifyDataSetChanged();
        }

        void updateData(ArrayList<TimerTask> list) {
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
