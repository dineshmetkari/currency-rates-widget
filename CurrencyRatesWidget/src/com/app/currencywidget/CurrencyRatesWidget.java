package com.app.currencywidget;

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
	private CurrencyRates currencyRates;
		
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds){	
		Log.d("DEBUG","onUpdate");	
		
		updateWidget(context);	
	}		
	
	@Override
	public void onEnabled(Context context){
		Log.d("DEBUG","onEnabled");	
		super.onEnabled(context);
		
		AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);		
		alarmManager.setRepeating(AlarmManager.RTC, SystemClock.elapsedRealtime() + 360000, 360000, createPendingIntent(context));		
	}
	
	@Override 
	public void onDisabled(Context context){
		Log.d("DEBUG","onDisabled");			
		super.onDisabled(context);
		AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(createPendingIntent(context));
	}
	
	@Override
	public void onReceive(Context context, Intent intent){
		Log.d("DEBUG","onReceive");			
		super.onReceive(context, intent);
		
		if(intent.getAction().equals(ACTION_WIDGET_RECEIVER)){			
			updateWidget(context);	
		}
	}
	
	public void updateWidget(Context context){
		Log.d("DEBUG","updateWidget");		
		currencyRates = new CurrencyRates(context);		
		currencyRatesView = currencyRates.getCurrencyRates(context);
		
		currencyRatesView.setOnClickPendingIntent(R.id.CurrencyRatesWidget, createPendingIntent(context));
		
		// Push update for this widget to the home screen
		ComponentName thisAppWidget = new ComponentName(context.getPackageName(),getClass().getName());	
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
		appWidgetManager.updateAppWidget(thisAppWidget, currencyRatesView);		
	}
	
	private PendingIntent createPendingIntent(Context context){
		Intent intent = new Intent(context, CurrencyRatesWidget.class);
		intent.setAction(ACTION_WIDGET_RECEIVER);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		return pendingIntent;
	}
}
