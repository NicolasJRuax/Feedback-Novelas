package com.myproyect.gestornovelasnjr.gestor_novelas.Novelas;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class NovelRepository {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference novelCollection = db.collection("novels");

    // Método para obtener todas las novelas
    public LiveData<List<Novel>> getAllNovels() {
        MutableLiveData<List<Novel>> novelsLiveData = new MutableLiveData<>();

        novelCollection.addSnapshotListener((querySnapshot, e) -> {
            if (e != null) {
                Log.e("Firestore", "Listen failed.", e);
                return;
            }

            if (querySnapshot != null) {
                List<Novel> novels = new ArrayList<>();
                for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                    Novel novel = document.toObject(Novel.class);
                    if (novel != null) {
                        novel.setId(document.getId()); // Asigna el ID de Firestore al objeto Novel
                        novels.add(novel);
                    }
                }
                novelsLiveData.setValue(novels);
            }
        });


        return novelsLiveData;
    }



    // Método para insertar una novela
    public void insert(Novel novel) {
        novelCollection.add(novel).addOnSuccessListener(documentReference ->
                Log.d("Firestore", "DocumentSnapshot added with ID: " + documentReference.getId())
        ).addOnFailureListener(e ->
                Log.e("Firestore", "Error adding novel", e)
        );
    }

    // Método para eliminar una novela
    public void delete(Novel novel) {
        novelCollection.whereEqualTo("title", novel.getTitle())
                .get().addOnSuccessListener(querySnapshot -> {
                    for (com.google.firebase.firestore.DocumentSnapshot doc : querySnapshot.getDocuments()) {
                        novelCollection.document(doc.getId()).delete();
                    }
                }).addOnFailureListener(e -> Log.e("Firestore", "Error deleting novel", e));
    }

    public void syncNovels() {
        novelCollection.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                List<Novel> novels = new ArrayList<>();
                for (DocumentSnapshot document : task.getResult().getDocuments()) {
                    Novel novel = document.toObject(Novel.class);
                    novels.add(novel);
                }
                // Aquí puedes hacer algo con la lista de novelas, como actualizar la UI o guardarlas localmente
                Log.d("Sync", "Datos sincronizados correctamente");
            } else {
                Log.e("Sync", "Error al sincronizar los datos", task.getException());
            }
        });
    }


}
