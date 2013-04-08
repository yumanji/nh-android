package com.movetothebit.newholland.android.charts.model;

import com.movetothebit.newholland.android.model.Brand;

public class MonthDataSet {

	public String month;
	public String year;
	
	public float total = 0;
	public float known = 0;
	public float offert = 0;
	public float win = 0;	
	public float lost = 0;	
	public float[] lostData;
	public Brand[] totalBrand;
	
	
	
	public float getLost() {
		return lost;
	}
	public void setLost(float lost) {
		this.lost = lost;
	}
	public float[] getLostData() {
		return lostData;
	}
	public void setLostData(float[] lostData) {
		this.lostData = lostData;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public float getTotal() {
		return total;
	}
	public void setTotal(float total) {
		this.total = total;
	}
	public float getKnown() {
		return known;
	}
	public void setKnown(float known) {
		this.known = known;
	}
	public float getOffert() {
		return offert;
	}
	public void setOffert(float offert) {
		this.offert = offert;
	}
	public float getWin() {
		return win;
	}
	public void setWin(float win) {
		this.win = win;
	}
	public Brand[] getTotalBrand() {
		return totalBrand;
	}
	public void setTotalBrand(Brand[] totalBrand) {
		this.totalBrand = totalBrand;
	}
	
	
	
	
}
