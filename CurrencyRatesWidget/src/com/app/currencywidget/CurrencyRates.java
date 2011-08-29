package com.app.currencywidget;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import android.content.Context;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RemoteViews;

import com.app.R;

public class CurrencyRates extends LinearLayout{
	
	public CurrencyRates(Context context){
		super(context);		
	}

	public ArrayList<RemoteViews> getCurrencyRates(Context context){
		ArrayList<RemoteViews> views = new ArrayList<RemoteViews>();
		String[] currencyList = context.getResources().getStringArray(R.array.currencyList);
		
		try {
        	URL url = new URL(context.getResources().getString(R.string.serviceUrl));
        	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        	DocumentBuilder db = dbf.newDocumentBuilder();
        	Document doc = db.parse(new InputSource(url.openStream()));
        	doc.getDocumentElement().normalize();
        	
        	NodeList nodeList = doc.getElementsByTagName("currency");
        	        	     	
        	
        	for (int i = 0; i < nodeList.getLength(); i++){
        		
        		Element currencyElement = (Element)nodeList.item(i);       		
        		
        		for (int j = 0; j < currencyList.length; j++){
        			
        			if (currencyList[j].equals(currencyElement.getAttribute("name"))){
        				
        				RemoteViews ratesView = new RemoteViews(context.getPackageName(),R.layout.rates_frame_layout);
        				
        				String currencyName = currencyElement.getAttribute("name").replace("EURO",	"EUR"); // fix for EUR
        				String buyRate = formatRate(currencyElement.getAttribute("buy"));
        				String sellRate = formatRate(currencyElement.getAttribute("sell"));
        				String ext = currencyElement.getAttribute("ext");
        				
        				ratesView.setTextViewText(R.id.currencyName, currencyName);
        				ratesView.setTextViewText(R.id.buyRate, buyRate + ext);
        				ratesView.setTextViewText(R.id.sellRate, sellRate + ext);
        				
        				views.add(ratesView);
        			}
        		}      		       		
        		
        	}
        	
        } catch (Exception e){
        	Log.d("DEBUG","XML parsing exception: " + e);
        }
		return views;
	}
	
	public String formatRate(String rate){
		DecimalFormat df = new DecimalFormat("0.00");
		
		if (rate != null){
			rate = rate.replace(',', '.');
			Double dRate = Double.parseDouble(rate);
			rate = String.valueOf(df.format(dRate));
		}
		
		return rate;
	}

}
