package com.movetothebit.newholland.android.charts.model;

import com.movetothebit.newholland.android.model.Brand;

public class ChartDataSet {

	public MonthDataSet[] monthDataSet;
	
	public float total = 0;
	public float known = 0;
	public float offert = 0;
	public float win = 0;	
	public float lost = 0;
	public float[] lostData;
	public Brand[] totalBrand;
	
}
