package com.movetothebit.newholland.android.model;

public class InscriptionData {
	
	//datos que vienen del servidor
	
	public String inscription;	
	public String machineType;
	public String brand;
	public String commercialModel;
	public String month;
	public String year;	
	public String hp;	
	public String segmentModel;
	public String province;
	public String population;	
	public String dealerName;
	public int idSalesman;
	public String salesmanName;
	
	//dato que nos dira si hemos hecho la encuenta
	public int fillData = 0;
	
	//datos de la encuesta
	public int knownOperation = 0;
	public int makeOffer = 0;
	public int winOffer = 0;
	public String modelOffer = null;
	public String whyLose = null;
	public Float price = 0.0f;
	public String nameClient = "Ningun cliente establecido";
	
	public String observations = "No hay observaciones";
	
	
	
	public int getFillData() {
		return fillData;
	}
	public void setFillData(int fillData) {
		this.fillData = fillData;
	}
	public int getKnownOperation() {
		return knownOperation;
	}
	public void setKnownOperation(int knownOperation) {
		this.knownOperation = knownOperation;
	}
	public int getMakeOffer() {
		return makeOffer;
	}
	public void setMakeOffer(int makeOffer) {
		this.makeOffer = makeOffer;
	}
	public int getWinOffer() {
		return winOffer;
	}
	public void setWinOffer(int winOffer) {
		this.winOffer = winOffer;
	}
	public String getModelOffer() {
		return modelOffer;
	}
	public void setModelOffer(String modelOffer) {
		this.modelOffer = modelOffer;
	}
	public String getWhyLose() {
		return whyLose;
	}
	public void setWhyLose(String whyLose) {
		this.whyLose = whyLose;
	}
	public Float getPrice() {
		return price;
	}
	public void setPrice(Float price) {
		this.price = price;
	}
	public String getNameClient() {
		return nameClient;
	}
	public void setNameClient(String nameClient) {
		this.nameClient = nameClient;
	}
	public String getObservations() {
		return observations;
	}
	public void setObservations(String observations) {
		this.observations = observations;
	}
	public String getInscription() {
		return inscription;
	}
	public void setInscription(String inscription) {
		this.inscription = inscription;
	}
	public String getMachineType() {
		return machineType;
	}
	public void setMachineType(String machineType) {
		this.machineType = machineType;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getCommercialModel() {
		return commercialModel;
	}
	public void setCommercialModel(String commercialModel) {
		this.commercialModel = commercialModel;
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
	public String getHp() {
		return hp;
	}
	public void setHp(String hp) {
		this.hp = hp;
	}
	public String getSegmentModel() {
		return segmentModel;
	}
	public void setSegmentModel(String segmentModel) {
		this.segmentModel = segmentModel;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getPopulation() {
		return population;
	}
	public void setPopulation(String population) {
		this.population = population;
	}
	public String getDealerName() {
		return dealerName;
	}
	public void setDealerName(String dealerName) {
		this.dealerName = dealerName;
	}
	public int getIdSalesman() {
		return idSalesman;
	}
	public void setIdSalesman(int idSalesman) {
		this.idSalesman = idSalesman;
	}
	public String getSalesmanName() {
		return salesmanName;
	}
	public void setSalesmanName(String salesmanName) {
		this.salesmanName = salesmanName;
	}

	
	
	
}
