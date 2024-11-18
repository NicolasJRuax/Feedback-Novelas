package com.myproyect.gestornovelasnjr.gestor_novelas.Novelas;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.myproyect.gestornovelasnjr.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.myproyect.gestornovelasnjr.gestor_novelas.Widget.NewAppWidget;

import java.util.ArrayList;
import java.util.List;

public class NovelAdapter extends RecyclerView.Adapter<NovelAdapter.NovelHolder> {

    private List<Novel> novels = new ArrayList<>();
    private OnDeleteClickListener deleteListener;
    private OnItemClickListener itemClickListener;
    private Context context;
    private FirebaseFirestore db;

    public interface OnItemClickListener {
        void onItemClick(Novel novel);
    }

    public NovelAdapter(Context context, OnDeleteClickListener deleteListener, OnItemClickListener itemClickListener) {
        this.context = context;
        this.deleteListener = deleteListener;
        this.itemClickListener = itemClickListener;
        db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public NovelHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_novel, parent, false);
        return new NovelHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NovelHolder holder, int position) {
        Novel currentNovel = novels.get(position);
        holder.textViewTitle.setText(currentNovel.getTitle());
        holder.textViewAuthor.setText(currentNovel.getAuthor());

        // Update favorite icon
        if (currentNovel.isFavorite()) {
            holder.buttonFavorite.setImageResource(android.R.drawable.btn_star_big_on);
        } else {
            holder.buttonFavorite.setImageResource(android.R.drawable.btn_star_big_off);
        }

        holder.itemView.setOnClickListener(v -> {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(currentNovel);
            }
        });

        holder.buttonDelete.setOnClickListener(v -> {
            deleteListener.onDeleteClick(currentNovel);
        });

        holder.buttonFavorite.setOnClickListener(v -> {
            if (currentNovel.getId() == null) {
                Log.e("Firestore", "ID de la novela es nulo; no se puede actualizar el estado de favorito.");
                Toast.makeText(context, "Error: ID de novela es nulo", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean isFavorite = !currentNovel.isFavorite();
            currentNovel.setFavorite(isFavorite);
            updateFavoriteStatus(currentNovel); // Intentar actualizar el estado en Firestore

            if (isFavorite) {
                holder.buttonFavorite.setImageResource(android.R.drawable.btn_star_big_on);
                Toast.makeText(context, currentNovel.getTitle() + " añadido a favoritos", Toast.LENGTH_SHORT).show();
            } else {
                holder.buttonFavorite.setImageResource(android.R.drawable.btn_star_big_off);
                Toast.makeText(context, currentNovel.getTitle() + " eliminado de favoritos", Toast.LENGTH_SHORT).show();
            }
        });



        holder.buttonDetails.setOnClickListener(v -> {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(currentNovel);
            }
        });
    }

    @Override
    public int getItemCount() {
        return novels.size();
    }

    public void setNovels(List<Novel> novels) {
        this.novels = novels;
        notifyDataSetChanged();
    }

    private void updateFavoriteStatus(Novel novel) {
        if (novel.getId() != null) {
            Log.d("Firestore", "Actualizando estado de favorito para la novela con ID: " + novel.getId());

            db.collection("novels").document(novel.getId())
                    .update("favorite", novel.isFavorite())
                    .addOnSuccessListener(aVoid -> {
                        Log.d("Firestore", "Estado de favorito actualizado correctamente en Firestore.");
                    })
                    .addOnFailureListener(e -> {
                        Log.e("Firestore", "Error al actualizar el estado de favorito", e);
                        Toast.makeText(context, "Error al actualizar favorito", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Log.e("Firestore", "No se puede actualizar el estado de favorito: ID de novela es nulo");
            Toast.makeText(context, "No se pudo actualizar el estado de favorito", Toast.LENGTH_SHORT).show();
        }
    }




    class NovelHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;
        private TextView textViewAuthor;
        private ImageButton buttonDelete;
        private ImageButton buttonFavorite;
        private ImageButton buttonDetails;

        public NovelHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewAuthor = itemView.findViewById(R.id.textViewAuthor);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
            buttonFavorite = itemView.findViewById(R.id.buttonFavorite);
            buttonDetails = itemView.findViewById(R.id.buttonDetails);

            // Assign icons to buttons
            buttonDelete.setImageResource(android.R.drawable.ic_menu_delete);
            buttonDetails.setImageResource(android.R.drawable.ic_menu_info_details);
        }
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(Novel novel);
    }
}
