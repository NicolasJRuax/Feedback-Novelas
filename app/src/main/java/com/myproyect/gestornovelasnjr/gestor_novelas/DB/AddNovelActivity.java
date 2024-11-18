package com.myproyect.gestornovelasnjr.gestor_novelas.DB;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.myproyect.gestornovelasnjr.R;
import com.myproyect.gestornovelasnjr.gestor_novelas.DB.NovelDAO;
import com.myproyect.gestornovelasnjr.gestor_novelas.Novelas.Novel;

public class AddNovelActivity extends AppCompatActivity {
    private EditText editTextTitle, editTextAuthor, editTextYear, editTextSynopsis;
    private Button buttonAddNovel;
    private NovelDAO novelDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_novel); // Cambia al nombre de tu layout

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextAuthor = findViewById(R.id.editTextAuthor);
        editTextYear = findViewById(R.id.editTextYear);
        editTextSynopsis = findViewById(R.id.editTextSynopsis);
        buttonAddNovel = findViewById(R.id.buttonAddNovel);

        novelDAO = new NovelDAO(); // Inicializar el DAO

        buttonAddNovel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener datos de los campos
                String title = editTextTitle.getText().toString();
                String author = editTextAuthor.getText().toString();
                int year = Integer.parseInt(editTextYear.getText().toString());
                String synopsis = editTextSynopsis.getText().toString();

                // Crear una nueva novela
                Novel novel = new Novel(title, author, year, synopsis);

                // Añadir novela a Firestore
                novelDAO.addNovel(novel, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // La novela fue añadida exitosamente
                            Toast.makeText(getApplicationContext(), "Novela añadida!", Toast.LENGTH_SHORT).show();
                            // Limpiar los campos después de añadir (opcional)
                            editTextTitle.setText("");
                            editTextAuthor.setText("");
                            editTextYear.setText("");
                            editTextSynopsis.setText("");
                        } else {
                            // Manejar el error
                            Log.e("Firestore", "Error añadiendo novela", task.getException());
                            Toast.makeText(getApplicationContext(), "Error al añadir novela: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
