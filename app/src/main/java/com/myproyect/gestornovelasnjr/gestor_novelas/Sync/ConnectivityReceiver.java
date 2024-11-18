package com.myproyect.gestornovelasnjr.gestor_novelas.Sync;



import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

public class ConnectivityReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Implementar la lógica para manejar la conexión a una red Wi-Fi
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            // Comprobar el estado de la conexión y sincronizar datos si está conectado a Wi-Fi
            if (NetworkUtil.isConnectedToWifi(context)) {
                new SyncDataTask(context).execute();
            }
        }
    }
}
