package com.myproyect.gestornovelasnjr.gestor_novelas.Sync;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.myproyect.gestornovelasnjr.gestor_novelas.Novelas.Novel;

import java.util.ArrayList;
import java.util.List;

public class SyncDataTask extends AsyncTask<Void, Void, Void> {

    private Context context;
    private FirebaseFirestore db;

    public SyncDataTask(Context context) {
        this.context = context;
        db = FirebaseFirestore.getInstance();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        // Aquí usamos el método addOnCompleteListener en lugar de get()
        db.collection("novels")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Novel> novelsList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Novel novel = document.toObject(Novel.class);
                            novel.setId(document.getId());  // Establecer el ID del documento
                            novelsList.add(novel);
                            Log.d("SyncDataTask", "Novela sincronizada: " + novel.getTitle());
                        }
                        // Emitir el broadcast con los datos sincronizados
                        sendSyncCompleteBroadcast(novelsList);
                    } else {
                        Log.e("SyncDataTask", "Error al sincronizar datos: ", task.getException());
                    }
                });

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }

    // Método para emitir el broadcast
    private void sendSyncCompleteBroadcast(List<Novel> novelsList) {
        Intent intent = new Intent("com.myproyect.gestornovelasnjr.SYNC_COMPLETE");
        intent.putParcelableArrayListExtra("novels", (ArrayList<? extends Parcelable>) new ArrayList<>(novelsList)); // Pasar la lista de novelas
        context.sendBroadcast(intent);
    }
}
