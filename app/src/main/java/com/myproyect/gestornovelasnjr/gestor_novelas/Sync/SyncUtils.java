package com.myproyect.gestornovelasnjr.gestor_novelas.Sync;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import com.myproyect.gestornovelasnjr.gestor_novelas.Sync.SyncAlarmReceiver;

public class SyncUtils {
    public static void scheduleSyncAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, SyncAlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        long interval = AlarmManager.INTERVAL_HALF_DAY;
        long triggerAtMillis = System.currentTimeMillis() + interval;

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, triggerAtMillis, interval, pendingIntent);
    }
}
