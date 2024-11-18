package com.myproyect.gestornovelasnjr.gestor_novelas.Sync;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.myproyect.gestornovelasnjr.gestor_novelas.Novelas.Novel;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class SyncDataTask extends AsyncTask<Void, Void, Void> {

    private WeakReference<Context> contextRef;
    private FirebaseFirestore db;

    public SyncDataTask(Context context) {
        this.contextRef = new WeakReference<>(context.getApplicationContext());
        db = FirebaseFirestore.getInstance();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        db.collection("novels")
                .get()
                .addOnCompleteListener(task -> {
                    Context context = contextRef.get();
                    if (context == null) {
                        return;
                    }

                    if (task.isSuccessful()) {
                        List<Novel> novelsList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Novel novel = document.toObject(Novel.class);
                            novel.setId(document.getId());
                            novelsList.add(novel);
                            Log.d("SyncDataTask", "Novela sincronizada: " + novel.getTitle());
                        }
                        sendSyncCompleteBroadcast(context, novelsList);
                    } else {
                        Log.e("SyncDataTask", "Error al sincronizar datos: ", task.getException());
                    }
                });

        return null;
    }

    private void sendSyncCompleteBroadcast(Context context, List<Novel> novelsList) {
        Intent intent = new Intent("com.myproyect.gestornovelasnjr.SYNC_COMPLETE");
        intent.putParcelableArrayListExtra("novels", new ArrayList<>(novelsList));
        context.sendBroadcast(intent);
    }
}
