package com.myproyect.gestornovelasnjr.gestor_novelas.Novelas;



import android.content.Context;
import androidx.loader.content.AsyncTaskLoader;

import com.myproyect.gestornovelasnjr.gestor_novelas.Novelas.Novel;
import com.myproyect.gestornovelasnjr.gestor_novelas.Novelas.NovelRepository;

import java.util.List;

public class NovelLoader extends AsyncTaskLoader<List<Novel>> {

    private NovelRepository repository;

    public NovelLoader(Context context) {
        super(context);
        repository = new NovelRepository();
    }

    @Override
    protected void onStartLoading() {
        forceLoad(); // Forzar la carga
    }

    @Override
    public List<Novel> loadInBackground() {
        // Obtener la lista de novelas
        return repository.getAllNovels().getValue();
    }
}
