package com.myproyect.gestornovelasnjr.gestor_novelas.Sync;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class SyncAlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        new SyncDataTask(context).execute();
    }
}
