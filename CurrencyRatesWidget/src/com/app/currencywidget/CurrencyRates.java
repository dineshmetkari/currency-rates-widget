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
import com.app.R;

public class CurrencyRates extends LinearLayout{
	
	private String currencyName;
	private String sellRate;
	private String buyRate;
	private String ext;
	
	public CurrencyRates(Context context){
		super(context);		
	}

	public ArrayList<CurrencyRates> getCurrencyRates(Context context){
		ArrayList<CurrencyRates> currencyRatesList = new ArrayList<CurrencyRates>();
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
        			        				
        				String currencyName = currencyElement.getAttribute("name").replace("EURO",	"EUR"); // fix for EUR
        				String buyRate = currencyElement.getAttribute("buy").replace(',','.');
        				String sellRate = currencyElement.getAttribute("sell").replace(',','.');
        				String ext = currencyElement.getAttribute("ext");

        				CurrencyRates currencyRates = new CurrencyRates(context);
        				currencyRates.setCurrencyName(currencyName);
        				currencyRates.setSellRate(sellRate);
        				currencyRates.setBuyRate(buyRate);
        				currencyRates.setExt(ext);
        				currencyRatesList.add(currencyRates);
        			}
        		}      	
        	}
        	
        } catch (Exception e){
        	Log.d("DEBUG","XML parsing exception: " + e);
        }
		return currencyRatesList;
	}
	
	public String formatRate(String rate){
		DecimalFormat df = new DecimalFormat("0.00");
		
		if (rate != null){			
			Double dRate = Double.parseDouble(rate);
			rate = String.valueOf(df.format(dRate));
		}
		
		return rate;
	}

	public String getCurrencyName() {
		return currencyName;
	}

	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}

	public String getSellRate() {
		return sellRate;
	}

	public void setSellRate(String sellRate) {
		this.sellRate = sellRate;
	}

	public String getBuyRate() {
		return buyRate;
	}

	public void setBuyRate(String buyRate) {
		this.buyRate = buyRate;
	}

	public String getExt() {
		return ext;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}

}
