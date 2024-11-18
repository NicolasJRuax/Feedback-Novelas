package com.myproyect.gestornovelasnjr.gestor_novelas;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.myproyect.gestornovelasnjr.R;
import com.myproyect.gestornovelasnjr.gestor_novelas.Novelas.Novel;
import com.myproyect.gestornovelasnjr.gestor_novelas.Novelas.NovelAdapter;
import com.myproyect.gestornovelasnjr.gestor_novelas.Novelas.NovelViewModel;
import com.myproyect.gestornovelasnjr.gestor_novelas.Sync.SyncDataTask;

import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {

    private Button buttonAddBook;
    private RecyclerView recyclerView;
    private NovelAdapter novelAdapter;
    private NovelViewModel novelViewModel;
    private BroadcastReceiver syncReceiver;

    // Definir el flag manualmente
    private static final int RECEIVER_NOT_EXPORTED = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Aplicar el tema según la configuración
        ThemeUtils.applyDarkMode(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar vistas
        buttonAddBook = findViewById(R.id.buttonAddBook);
        recyclerView = findViewById(R.id.recyclerView);

        // Configuración del RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        novelAdapter = new NovelAdapter(this, novel -> {
            // Eliminar novela
            novelViewModel.delete(novel);
            Toast.makeText(this, "Novela eliminada", Toast.LENGTH_SHORT).show();
        }, this::showNovelDetails);
        recyclerView.setAdapter(novelAdapter);

        // Inicializar ViewModel
        novelViewModel = new ViewModelProvider(this).get(NovelViewModel.class);
        novelViewModel.getAllNovels().observe(this, novels -> {
            novelAdapter.setNovels(novels);
        });

        // Evento para el botón de agregar novela
        buttonAddBook.setOnClickListener(v -> showAddNovelDialog());

        // Registro del receptor de sincronización
        syncReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Toast.makeText(context, "Sincronización completada", Toast.LENGTH_SHORT).show();
            }
        };

        // Registrar el receptor de manera compatible
        registerSyncReceiver();
    }

    private void registerSyncReceiver() {
        IntentFilter filter = new IntentFilter("com.myproyect.gestornovelasnjr.SYNC_COMPLETE");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            try {
                // Usar reflexión para obtener el método con flags
                Method method = Context.class.getMethod("registerReceiver", BroadcastReceiver.class, IntentFilter.class, int.class);
                method.invoke(this, syncReceiver, filter, RECEIVER_NOT_EXPORTED);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // Usar el método antiguo sin flags
            registerReceiver(syncReceiver, filter);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(syncReceiver);
    }

    private void showAddNovelDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Agregar Novela");

        final View customLayout = LayoutInflater.from(this).inflate(R.layout.dialog_add_novel, null);
        builder.setView(customLayout);

        EditText editTextTitle = customLayout.findViewById(R.id.editTextTitle);
        EditText editTextAuthor = customLayout.findViewById(R.id.editTextAuthor);
        EditText editTextYear = customLayout.findViewById(R.id.editTextYear);
        EditText editTextSynopsis = customLayout.findViewById(R.id.editTextSynopsis);

        builder.setPositiveButton("Agregar", (dialog, which) -> {
            String title = editTextTitle.getText().toString().trim();
            String author = editTextAuthor.getText().toString().trim();
            String yearText = editTextYear.getText().toString().trim();
            String synopsis = editTextSynopsis.getText().toString().trim();

            if (title.isEmpty() || author.isEmpty() || yearText.isEmpty() || synopsis.isEmpty()) {
                Toast.makeText(MainActivity.this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            int year;
            try {
                year = Integer.parseInt(yearText);
            } catch (NumberFormatException e) {
                Toast.makeText(MainActivity.this, "Año inválido", Toast.LENGTH_SHORT).show();
                return;
            }

            Novel novel = new Novel(title, author, year, synopsis);
            novelViewModel.insert(novel);
            Toast.makeText(MainActivity.this, "Novela añadida", Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    private void showNovelDetails(Novel novel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(novel.getTitle());
        builder.setMessage("Autor: " + novel.getAuthor() + "\nAño: " + novel.getYear() + "\n\n" + novel.getSynopsis());
        builder.setPositiveButton("Cerrar", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }
}
