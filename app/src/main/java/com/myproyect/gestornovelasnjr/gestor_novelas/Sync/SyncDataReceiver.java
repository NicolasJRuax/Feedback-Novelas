package com.myproyect.gestornovelasnjr.gestor_novelas.Sync;



import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class SyncDataReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && intent.getAction().equals("com.myproyect.gestornovelasnjr.SYNC_COMPLETE")) {
            NotificationHelper.showNotification(context, "Sincronización Completa", "Los datos han sido sincronizados exitosamente.");
        }
    }
}


