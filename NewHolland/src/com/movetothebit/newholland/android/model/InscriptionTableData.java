package com.movetothebit.newholland.android.model;

public class InscriptionTableData {

	
	public int marketTotal;
	public int knownTotal;
	public int ofertTotal;
	public int winTotal;
	public int[] lostData;
	public int[] winData;
	public int[] brandData;
	
	
	public int[] getWinData() {
		return winData;
	}

	public void setWinData(int[] winData) {
		this.winData = winData;
	}

	public InscriptionTableData() {
		super();
		
	}

	public int[] getBrandData() {
		return brandData;
	}

	public void setBrandData(int[] brandData) {
		this.brandData = brandData;
	}

	public int[] getLostData() {
		return lostData;
	}

	public void setLostData(int[] lostData) {
		this.lostData = lostData;
	}

	public int getMarketTotal() {
		return marketTotal;
	}
	public void setMarketTotal(int marketTotal) {
		this.marketTotal = marketTotal;
	}
	public int getKnownTotal() {
		return knownTotal;
	}
	public void setKnownTotal(int knownTotal) {
		this.knownTotal = knownTotal;
	}
	public int getOfertTotal() {
		return ofertTotal;
	}
	public void setOfertTotal(int ofertTotal) {
		this.ofertTotal = ofertTotal;
	}
	public int getWinTotal() {
		return winTotal;
	}
	public void setWinTotal(int winTotal) {
		this.winTotal = winTotal;
	}
	
}
