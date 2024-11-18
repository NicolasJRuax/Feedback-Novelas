package com.myproyect.gestornovelasnjr.gestor_novelas.Widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new NovelRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}
