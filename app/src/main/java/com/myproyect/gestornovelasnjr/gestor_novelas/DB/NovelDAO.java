package com.myproyect.gestornovelasnjr.gestor_novelas.DB;

import androidx.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentReference;
import com.myproyect.gestornovelasnjr.gestor_novelas.Novelas.Novel;

public class NovelDAO {
    private FirebaseFirestore db;

    public NovelDAO() {
        db = FirebaseFirestore.getInstance();
    }

    public void addNovel(Novel novel, OnCompleteListener<Void> listener) {
        // Añadir la novela a la colección "novels"
        db.collection("novels")
                .add(novel)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            // Si la tarea fue exitosa, llamar al listener con null
                            listener.onComplete(Tasks.forResult(null)); // Éxito
                        } else {
                            // Si hubo un error, loguear y llamar al listener con el task fallido
                            Log.e("Firestore", "Error al añadir novela", task.getException());
                            // Se invoca el listener con una excepción
                            listener.onComplete(Tasks.forException(task.getException())); // Error
                        }
                    }
                });
    }
}
