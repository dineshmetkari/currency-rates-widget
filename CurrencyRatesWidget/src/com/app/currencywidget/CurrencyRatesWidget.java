package com.app.currencywidget;

import java.util.ArrayList;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;

import com.app.R;

public class CurrencyRatesWidget extends AppWidgetProvider {

	private static final String ACTION_WIDGET_RECEIVER = "com.app.currencywidget.ACTION_WIDGET_RECEIVER";
	private RemoteViews currencyRatesView = new RemoteViews("com.app",
			R.layout.widget_layout);
	private CurrencyRates currencyRates;
	private ArrayList<CurrencyRates> currencyRatesList = new ArrayList<CurrencyRates>();
	private SharedPreferences prefs;

	private PendingIntent createPendingIntent(Context context) {
		Intent intent = new Intent(context, CurrencyRatesWidget.class);
		intent.setAction(ACTION_WIDGET_RECEIVER);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);
		return pendingIntent;
	}

	@Override
	public void onDisabled(Context context) {
		super.onDisabled(context);
		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(createPendingIntent(context));
	}

	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);
		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		alarmManager.setRepeating(AlarmManager.RTC,
				SystemClock.elapsedRealtime()+AlarmManager.INTERVAL_HOUR, AlarmManager.INTERVAL_HOUR,
				createPendingIntent(context));
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
		if (intent.getAction().equals(ACTION_WIDGET_RECEIVER)) {
			updateWidget(context);
		}
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		updateWidget(context);
	}

	public void updateWidget(Context context) {

		currencyRates = new CurrencyRates(context);
		currencyRatesList = currencyRates.getCurrencyRates(context);

		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor prefEditor = prefs.edit();

		currencyRatesView.removeAllViews(R.id.widget_layout);

		for (int i = 0; i < currencyRatesList.size(); i++) {

			RemoteViews ratesView = new RemoteViews(context.getPackageName(),
					R.layout.rates_frame_layout);

			String currencyName = currencyRatesList.get(i).getCurrencyName();
			String buyRate = currencyRatesList.get(i).getBuyRate();
			String sellRate = currencyRatesList.get(i).getSellRate();
			String ext = currencyRatesList.get(i).getExt();
			String oldCurrencyRate = prefs.getString(currencyName, null);

			if (oldCurrencyRate != null) {			
				Log.d("Debug1", oldCurrencyRate);
				Log.d("Debug2", sellRate);
				// compare old and new rates
				Double newRate = Double.parseDouble(sellRate);
				Double oldRate = Double.parseDouble(oldCurrencyRate);
				
				if (newRate.compareTo(oldRate) > 0) {
					ratesView.setImageViewResource(R.id.arrow, R.drawable.up);
					prefEditor.putString(currencyName, sellRate);
					prefEditor.putString("arrow", "up");
				} else if (newRate.compareTo(oldRate) < 0) {
					ratesView.setImageViewResource(R.id.arrow, R.drawable.down);
					prefEditor.putString(currencyName, sellRate);
					prefEditor.putString("arrow", "down");
				} else {
					if (prefs.getString("arrow", null).equals("down")) {
						ratesView.setImageViewResource(R.id.arrow,
								R.drawable.down);
					} else {
						ratesView.setImageViewResource(R.id.arrow,
								R.drawable.up);
					}
				}

			} else {
				// first run, save rates
				prefEditor.putString(currencyName, sellRate);
				ratesView.setImageViewResource(R.id.arrow,
						R.drawable.up);
			}
			prefEditor.commit();
			
			ratesView.setTextViewText(R.id.currencyName, currencyName);
			ratesView.setTextViewText(R.id.buyRate,
					currencyRates.formatRate(buyRate) + ext);
			ratesView.setTextViewText(R.id.sellRate,
					currencyRates.formatRate(sellRate) + ext);
			currencyRatesView.addView(R.id.widget_layout, ratesView);
		}

		currencyRatesView.setOnClickPendingIntent(R.id.CurrencyRatesWidget,
				createPendingIntent(context));

		// Push update for this widget to the home screen
		ComponentName thisAppWidget = new ComponentName(
				context.getPackageName(), getClass().getName());
		AppWidgetManager appWidgetManager = AppWidgetManager
				.getInstance(context);
		appWidgetManager.updateAppWidget(thisAppWidget, currencyRatesView);
	}
}
