package com.app.currencywidget;

import java.util.ArrayList;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;
import android.widget.RemoteViews;

import com.app.R;

public class CurrencyRatesWidget extends AppWidgetProvider {
	
	private static final String ACTION_WIDGET_RECEIVER = "com.app.currencywidget.ACTION_WIDGET_RECEIVER";
	private RemoteViews currencyRatesView = new RemoteViews("com.app", R.layout.widget_layout);
	private ArrayList<RemoteViews> currencyRatesList = new ArrayList<RemoteViews>();
		
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds){		
		updateWidget(context);
		
	}		
	
	@Override
	public void onEnabled(Context context){
		super.onEnabled(context);
		
		Intent fireIntent = new Intent(context, CurrencyRatesWidget.class);
		fireIntent.setAction(ACTION_WIDGET_RECEIVER);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, fireIntent, 0);
		currencyRatesView.setOnClickPendingIntent(R.id.CurrencyRatesWidget, pendingIntent);
		
		AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);		
		alarmManager.setRepeating(AlarmManager.RTC, SystemClock.elapsedRealtime() + 360000, 360000, pendingIntent);
		
		updateWidget(context);		
	}

	@Override
	public void onReceive(Context context, Intent intent){
		super.onReceive(context, intent);
		
		if(intent.getAction().equals(ACTION_WIDGET_RECEIVER)){			
			updateWidget(context);						
		}
	}
	
	public void updateWidget(Context context){
		Log.d("DEBUG","updateWidget");		
		
		//currencyRatesView  = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
		//ArrayList<RemoteViews> currencyRatesList = new ArrayList<RemoteViews>();		
		CurrencyRates currencyRates = new CurrencyRates(context);
		
		currencyRatesList = currencyRates.getCurrencyRates(context);
		
		currencyRatesView.removeAllViews(R.id.widget_layout);
		
		for (int i = 0; i < currencyRatesList.size(); i++){			
			currencyRatesView.addView(R.id.widget_layout, currencyRatesList.get(i));
		}
		
		/*
		Intent fireIntent = new Intent(context, CurrencyRatesWidget.class);
		fireIntent.setAction(ACTION_WIDGET_RECEIVER);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, fireIntent, 0);
		currencyRatesView.setOnClickPendingIntent(R.id.CurrencyRatesWidget, pendingIntent);
		*/
		//AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);		
		//alarmManager.setRepeating(AlarmManager.RTC, SystemClock.elapsedRealtime() + 5000, 5000, pendingIntent);
		// Push update for this widget to the home screen
		ComponentName thisAppWidget = new ComponentName(context.getPackageName(),getClass().getName());	
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
		appWidgetManager.updateAppWidget(thisAppWidget, currencyRatesView);		
	}
}
