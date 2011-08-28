package com.app.currencywidget;

import java.util.ArrayList;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

import com.app.R;

public class CurrencyRatesWidget extends AppWidgetProvider {
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds){		
		
		RemoteViews view1 = new RemoteViews(context.getPackageName(),R.layout.widget_layout);		
		ArrayList<RemoteViews> views = new ArrayList<RemoteViews>();		
		CurrencyRates currencyRates = new CurrencyRates(context);
		
		views = currencyRates.getCurrencyRates(context);
		
		for (int i = 0; i < views.size(); i++){	
			view1.addView(R.id.widget_layout, views.get(i));
		}
		
		appWidgetManager.updateAppWidget(appWidgetIds, view1);
	}	
}
