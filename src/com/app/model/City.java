package com.app.model;

import java.util.ArrayList;

public class City {

	ArrayList<Weather> weather_data;
	ArrayList<Index> index;
	String pm25;
	String currentCity;
	
	public ArrayList<Weather> getWeather_data() {
		return weather_data;
	}
	public void setWeather_data(ArrayList<Weather> weather_data) {
		this.weather_data = weather_data;
	}
	public ArrayList<Index> getIndex() {
		return index;
	}
	public void setIndex(ArrayList<Index> index) {
		this.index = index;
	}
	public String getPm25() {
		return pm25;
	}
	public void setPm25(String pm25) {
		this.pm25 = pm25;
	}
	public String getCurrentCity() {
		return currentCity;
	}
	public void setCurrentCity(String currentCity) {
		this.currentCity = currentCity;
	}
	
	
}
