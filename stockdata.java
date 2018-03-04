package com.example.xunistockcsv;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.grapecity.xuni.core.*;

public class StockData {
	protected String ticker;
	protected Date date;
	protected double open;
	protected double high;
	protected double low;
	protected double close;
	protected int volume;
	
	public StockData(){
		this.ticker = "";
		this.date = new Date();
		this.open = 0.0;
		this.high = 0.0;
		this.low = 0.0;
		this.close = 0.0;
		this.volume = 0;
	}
	public StockData(String ticker, Date date, double open, double high, double low, double close, int volume){
		this.ticker = ticker;
		this.date = date;
		this.open = open;
		this.high = high;
		this.low = low;
		this.close = close;
		this.volume = volume;
	}
	public String getTicker(){
		return ticker;
	}
	public void setTicker(String ticker){
		this.ticker = ticker;
	}
	public Date getDate(){
		return date;
	}
	public void setDate(Date date){
		this.date = date;
	}
	public double getOpen(){
		return open;
	}
	public void setOpen(double open){
		this.open = open;
	}
	public double getHigh(){
		return high;
	}
	public void setHigh(double high){
		this.high = high;
	}
	public double getLow(){
		return low;
	}
	public void setLow(double low){
		this.low = low;
	}
	public double getClose(){
		return close;
	}
	public void setClose(double close){
		this.close = close;
	}
	public int getVolume(){
		return volume;
	}
	public void setVolume(int volume){
		this.volume = volume;
	}
	public ObservableList<StockData> getObservableList(List<String[]> stocks){
		ObservableList<StockData> list = new ObservableList<StockData>();
		String[] sarray;
		SimpleDateFormat sformat = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
		for(int i = 0; i < stocks.size(); i++){
				StockData sd = new StockData();
				sarray = stocks.get(i);
				sd.ticker = sarray[0];
				try {
					sd.date = sformat.parse(sarray[1]);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				sd.open = Double.parseDouble(sarray[2]);
				sd.high = Double.parseDouble(sarray[3]);
				sd.low = Double.parseDouble(sarray[4]);
				sd.close = Double.parseDouble(sarray[5]);
				sd.volume = Integer.parseInt(sarray[6]);
				list.add(sd);
		}
		return list;
	}
	public List<String[]> getStringList(ObservableList<StockData> list){
		List<String[]> stringList = new ArrayList<String[]>();
		SimpleDateFormat sformat = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
		for(int i = 0; i < list.size(); i++){
			String[] row = new String[]{list.get(i).ticker, sformat.format(list.get(i).date), 
					Double.toString(list.get(i).open),Double.toString(list.get(i).high), 
					Double.toString(list.get(i).low), Double.toString(list.get(i).close), 
					Integer.toString(list.get(i).volume)};
			stringList.add(row);
		}
		return stringList;
	}
}