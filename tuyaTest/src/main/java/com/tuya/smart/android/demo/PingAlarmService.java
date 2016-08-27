package com.tuya.smart.android.demo;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import com.tuya.smart.android.common.utils.PhoneUtil;
import com.tuya.smart.android.common.utils.TuyaUtil;

/**
 * Created by letian on 16/8/2.
 */
public class PingAlarmService extends Service {
    private static final String TAG = "PingAlarmService ggggg";
    private Service service;
    private AlarmReceiver alarmReceiver;
    private PendingIntent pendingIntent;
    private boolean hasStarted;
    private int pingTime;

    public void initPing(int pingTime) {
        this.alarmReceiver = new AlarmReceiver();
        service = this;
        this.pingTime = pingTime;

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        initPing(60 * 1000);
        start();
        return super.onStartCommand(intent, flags, startId);
    }

    public void start() {
        String action = "pingTest" + System.currentTimeMillis() + PhoneUtil.getAndroidId(this);
        Log.d(TAG, "Register alarmreceiver to MqttService" + action);
        service.registerReceiver(alarmReceiver, new IntentFilter(action));

        pendingIntent = PendingIntent.getBroadcast(service, 0, new Intent(
                action), PendingIntent.FLAG_UPDATE_CURRENT);

        schedule(pingTime);
        hasStarted = true;
    }

    public void stop() {
        // Cancel Alarm.
        AlarmManager alarmManager = (AlarmManager) service
                .getSystemService(Service.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);

        Log.d(TAG, "Unregister alarmreceiver");

        if (hasStarted) {
            hasStarted = false;
            try {
                service.unregisterReceiver(alarmReceiver);
            } catch (IllegalArgumentException e) {
                //Ignore unregister errors.
            }
        }
    }

    public void schedule(long delayInMilliseconds) {
        long nextAlarmInMilliseconds = System.currentTimeMillis()
                + delayInMilliseconds;
        Log.d(TAG, "Schedule next alarm at " + TuyaUtil.formatDate(nextAlarmInMilliseconds, "yyyy-MM-dd hh:mm:ss"));
        AlarmManager alarmManager = (AlarmManager) service
                .getSystemService(Service.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, nextAlarmInMilliseconds,
                pendingIntent);
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    class AlarmReceiver extends BroadcastReceiver {
        private PowerManager.WakeLock wakelock;

        @Override
        public void onReceive(Context context, Intent intent) {
            // According to the docs, "Alarm Manager holds a CPU wake lock as
            // long as the alarm receiver's onReceive() method is executing.
            // This guarantees that the phone will not sleep until you have
            // finished handling the broadcast.", but this class still get
            // a wake lock to wait for ping finished.
            int count = intent.getIntExtra(Intent.EXTRA_ALARM_COUNT, -1);
            Log.d(TAG, "Ping " + count + " times.");

            Log.d(TAG, "Check time :" + System.currentTimeMillis());

            // Assign new callback to token to execute code after PingResq
            // arrives. Get another wakelock even receiver already has one,
            // release it until ping response returns.
            if (wakelock == null) {
                PowerManager pm = (PowerManager) service
                        .getSystemService(Service.POWER_SERVICE);
                wakelock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                        "ping mqtt");
            }
            wakelock.acquire();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    schedule(60000);
                    //Release wakelock when it is done.
                    if (wakelock != null && wakelock.isHeld()) {
                        wakelock.release();
                    }
                }
            }).start();
        }
    }
}
