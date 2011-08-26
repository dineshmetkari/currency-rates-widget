package com.app.currencywidget;

import java.util.Timer;
import java.util.TimerTask;

import com.app.R;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.widget.RemoteViews;

public class CurrencyRatesWidget extends AppWidgetProvider {
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds){
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new MyTime(context, appWidgetManager), 1, 360000);		
	}
	
	private class MyTime extends TimerTask{
		RemoteViews remoteViews;
		AppWidgetManager appWidgetManager;
		ComponentName thisWidget;
		CurrencyRates currencyRates;
		Context context;
		
		public MyTime(Context context, AppWidgetManager appWidgetManager){
			this.appWidgetManager = appWidgetManager;
			this.context = context;
			remoteViews = new RemoteViews(context.getPackageName(),R.layout.widget_layout);
			thisWidget = new ComponentName(context,CurrencyRatesWidget.class);
			currencyRates = new CurrencyRates();
		}
				
		public void run(){	
			currencyRates.updateRates(context);			
			
			remoteViews.setTextViewText(R.id.usdBuy, 
					currencyRates.getBuyRate((String)context.getResources().getText(R.string.USD)) + " TL");
			remoteViews.setTextViewText(R.id.usdSell, 
					currencyRates.getSellRate((String)context.getResources().getText(R.string.USD)) + " TL");
			remoteViews.setTextViewText(R.id.eurBuy, 
					currencyRates.getBuyRate((String)context.getResources().getText(R.string.EUR)) + " TL");
			remoteViews.setTextViewText(R.id.eurSell, 
					currencyRates.getSellRate((String)context.getResources().getText(R.string.EUR)) + " TL");
			
			appWidgetManager.updateAppWidget(thisWidget, remoteViews);		
			
		}
	}
}
