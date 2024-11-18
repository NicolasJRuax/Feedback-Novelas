package com.myproyect.gestornovelasnjr.gestor_novelas.Novelas;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.myproyect.gestornovelasnjr.gestor_novelas.Sync.SyncDataTask;

import java.util.List;

public class NovelViewModel extends AndroidViewModel {
    private NovelRepository repository;
    private LiveData<List<Novel>> allNovels;

    public NovelViewModel(@NonNull Application application) {
        super(application);
        repository = new NovelRepository();
        allNovels = repository.getAllNovels();
    }

    public void insert(Novel novel) {
        repository.insert(novel);
    }

    public void delete(Novel novel) {
        repository.delete(novel);
    }

    public LiveData<List<Novel>> getAllNovels() {
        return allNovels;
    }

    // Método para sincronizar las novelas
    public void syncNovels() {
        new SyncDataTask(getApplication()).execute();
    }
}
