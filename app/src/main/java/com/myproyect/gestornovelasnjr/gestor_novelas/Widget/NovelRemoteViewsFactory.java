package com.myproyect.gestornovelasnjr.gestor_novelas.Widget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.myproyect.gestornovelasnjr.R;
import com.myproyect.gestornovelasnjr.gestor_novelas.Novelas.Novel;
import java.util.ArrayList;
import java.util.List;

public class NovelRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context context;
    private List<Novel> favoriteNovels = new ArrayList<>();

    public NovelRemoteViewsFactory(Context applicationContext, Intent intent) {
        context = applicationContext;
    }

    @Override
    public void onCreate() {
        // Initial data load
        loadFavoriteNovels();
    }

    @Override
    public void onDataSetChanged() {
        // Refresh data
        loadFavoriteNovels();
    }

    @Override
    public void onDestroy() {
        favoriteNovels.clear();
    }

    @Override
    public int getCount() {
        return favoriteNovels.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Novel novel = favoriteNovels.get(position);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_list_item);
        views.setTextViewText(R.id.widget_item_text, novel.getTitle());

        Intent fillInIntent = new Intent();
        fillInIntent.putExtra("novel_id", novel.getId());
        views.setOnClickFillInIntent(R.id.widget_item_text, fillInIntent);

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null; // Default loading view
    }

    @Override
    public int getViewTypeCount() {
        return 1; // One type of view
    }

    @Override
    public long getItemId(int position) {
        return position; // Or novel IDs if unique
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    private void loadFavoriteNovels() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("novels")
                .whereEqualTo("favorite", true) // Solo favoritos
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        favoriteNovels.clear();
                        for (DocumentSnapshot document : task.getResult()) {
                            Novel novel = document.toObject(Novel.class);
                            favoriteNovels.add(novel);
                        }

                        // Notificar al AppWidgetManager que la lista ha cambiado
                        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                        ComponentName widget = new ComponentName(context, NewAppWidget.class);
                        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(widget);

                        // Notificar el cambio en todos los widgets activos de este tipo
                        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_listview);
                    }
                });
    }


}
