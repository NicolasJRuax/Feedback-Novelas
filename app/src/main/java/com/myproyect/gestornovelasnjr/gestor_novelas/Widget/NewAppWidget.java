package com.myproyect.gestornovelasnjr.gestor_novelas.Widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import com.myproyect.gestornovelasnjr.R;
import com.myproyect.gestornovelasnjr.gestor_novelas.MainActivity;

public class NewAppWidget extends AppWidgetProvider {

    public static final String ACTION_ITEM_CLICK = "com.myproyect.gestornovelasnjr.ACTION_ITEM_CLICK";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        for (int appWidgetId : appWidgetIds) {

            Intent intent = new Intent(context, WidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
            views.setRemoteAdapter(R.id.widget_listview, intent);
            views.setEmptyView(R.id.widget_listview, R.id.widget_empty_view);

            // Intent for opening the app when clicking on the widget
            Intent clickIntent = new Intent(context, MainActivity.class);
            PendingIntent clickPendingIntent = PendingIntent.getActivity(context, 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            views.setPendingIntentTemplate(R.id.widget_listview, clickPendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
}
