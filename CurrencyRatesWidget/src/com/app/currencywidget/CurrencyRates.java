package com.app.currencywidget;

import java.net.URL;
import java.text.DecimalFormat;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.app.R;

import android.content.Context;

public class CurrencyRates {
	
	Context context;
	
	private String usdBuy;
	private String usdSell;
	private String eurBuy;
	private String eurSell;
	private DecimalFormat df = new DecimalFormat("0.00");
	
	private void setBuyRate(String currency, String rate){
		if (rate != null){
			rate = df.format(Double.parseDouble(rate));
			if(currency.equals(context.getResources().getText(R.string.USD))){
				usdBuy = rate;
			} else if (currency.equals(context.getResources().getText(R.string.EUR))){
				eurBuy = rate;
			}
		}
	}
	private void setSellRate(String currency, String rate){
		if (rate != null){
			rate = df.format(Double.parseDouble(rate));
			if(currency.equals(context.getResources().getText(R.string.USD))){
				usdSell = rate;
			} else if (currency.equals(context.getResources().getText(R.string.EUR))){
				eurSell = rate;
			}
		}
	}
	public String getBuyRate(String currency){
		if(currency.equals(context.getResources().getText(R.string.USD))){
			return usdBuy;
		} else if (currency.equals(context.getResources().getText(R.string.EUR))){
			return eurBuy;
		}
		return "";
	}
	
	public String getSellRate(String currency){
		if(currency.equals(context.getResources().getText(R.string.USD))){
			return usdSell;
		} else if (currency.equals(context.getResources().getText(R.string.EUR))){
			return eurSell;
		}
		return "";
	}
	
	public CurrencyRates(){
		
	}
	
	public void updateRates(Context context){
		try {
			this.context = context;
        	URL url = new URL("http://ingbank.com.tr/data/doviz.asp");
        	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        	DocumentBuilder db = dbf.newDocumentBuilder();
        	Document doc = db.parse(new InputSource(url.openStream()));
        	doc.getDocumentElement().normalize();
        	
        	NodeList nodeList = doc.getElementsByTagName("currency");
        	
        	for (int i = 0; i < nodeList.getLength(); i++){
        		
        		Element currencyElement = (Element)nodeList.item(i);
        		
        		setBuyRate(currencyElement.getAttribute("name"), 
        				currencyElement.getAttribute("buy").replace(',', '.'));
        		setSellRate(currencyElement.getAttribute("name"), 
        				currencyElement.getAttribute("sell").replace(',', '.'));	        		
        		        		        		
        	}
        	
        } catch (Exception e){
        	System.out.println("XML parsing exception: " + e);
        }
	}

}
