package com.app.model.response;

import java.util.ArrayList;

import com.app.model.City;

/**
 * 百度天气 封装类
 * 
 * @author Ni Guijun
 *
 */
public class WeatherResponse {

	private ArrayList<City> results;
	private String date;
	private int error;
	private String status;
	public ArrayList<City> getResults() {
		return results;
	}
	public void setResults(ArrayList<City> results) {
		this.results = results;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public int getError() {
		return error;
	}
	public void setError(int error) {
		this.error = error;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

}
