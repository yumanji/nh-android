package com.movetothebit.newholland.android.model;

import java.util.List;

public class InscriptionTableData {

	
	public int marketTotal;
	public int knownTotal;
	public int ofertTotal;
	public int winTotal;
	public int lostTotal;
	public int  marketCuote;
	public int knownMarket;
	public int presence;
	public int efectivity;
	public int presenceTotal;
	public int[] lostData;
	public int[] winData;
	public List<Brand> brandData;
	
	
	public int getLostTotal() {
		return lostTotal;
	}

	public void setLostTotal(int lostTotal) {
		this.lostTotal = lostTotal;
	}

	public int getMarketCuote() {
		return marketCuote;
	}

	public void setMarketCuote(int marketCuote) {
		this.marketCuote = marketCuote;
	}

	public int getKnownMarket() {
		return knownMarket;
	}

	public void setKnownMarket(int kwnownMarket) {
		this.knownMarket = kwnownMarket;
	}

	public int getPresence() {
		return presence;
	}

	public void setPresence(int presence) {
		this.presence = presence;
	}

	public int getEfectivity() {
		return efectivity;
	}

	public void setEfectivity(int efectivity) {
		this.efectivity = efectivity;
	}

	public int getPresenceTotal() {
		return presenceTotal;
	}

	public void setPresenceTotal(int presenceTotal) {
		this.presenceTotal = presenceTotal;
	}

	public int[] getWinData() {
		return winData;
	}

	public void setWinData(int[] winData) {
		this.winData = winData;
	}

	public InscriptionTableData() {
		super();
		
	}

	

	public List<Brand> getBrandData() {
		return brandData;
	}

	public void setBrandData(List<Brand> brandData) {
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
