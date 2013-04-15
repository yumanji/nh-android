package com.movetothebit.newholland.android.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.movetothebit.newholland.android.utils.lConstants;

@DatabaseTable
public class InscriptionData implements lConstants{
	
	
	
	
//	@DatabaseField(generatedId = true,id = true, columnName = ID)
//	public int id;	
	
	@DatabaseField(id = true, columnName = INSCRIPTION)
	public String inscription;	
	@DatabaseField(columnName = MACHINE_TYPE)
	public String machineType;
	@DatabaseField(columnName = BRAND)
	public String brand;
	@DatabaseField(columnName = COMMERCIAL_MODEL)
	public String commercialModel;
	@DatabaseField(columnName = MONTH)
	public String month;
	@DatabaseField(columnName = YEAR)
	public String year;	
	@DatabaseField(columnName = HP)
	public String hp;	
	@DatabaseField(columnName = SEGMENT_MODEL)
	public String segmentModel;
	@DatabaseField(columnName = SEGMENT_HP)
	public String SegmentoHP;
	@DatabaseField(columnName = MODEL3)
	public String modelo3;
	@DatabaseField(columnName = AREA)
	public String area;
	@DatabaseField(columnName = MODEL_EQUAL)
	public String modeloComparable;
	@DatabaseField(columnName = PROVINCE)
	public String province;
	@DatabaseField(columnName = POPULATION)
	public String population;	
	@DatabaseField(columnName = DEALER_NAME)
	public String dealerName;	
	@DatabaseField(columnName = SALESMAN_NAME)
	public String salesmanName;
	
	@DatabaseField(columnName = ID_SALESMAN)
	public int idSalesman = 0;

	//dato que nos dira si hemos hecho la encuenta
	@DatabaseField(columnName = HISTORIC)
	public int historico = 0;
	@DatabaseField(columnName = FILL_DATA)
	public int fillData = 0;
	@DatabaseField(columnName = KNOWN_OPERATION)
	public int knownOperation = 0;
	@DatabaseField(columnName = MAKE_OFFER)
	public int makeOffer = 0;
	@DatabaseField(columnName = WIN_OFFER)
	public int winOffer = 0;
	@DatabaseField(columnName = MODEL_OFFER)
	public String modelOffer = "";
	@DatabaseField(columnName = WHY_LOSE)
	public int whyLose = 0;
	@DatabaseField(columnName = WHY_WIN)
	public int whyWin = 0;
	@DatabaseField(columnName = PRICE)
	public Float price = 0.0f;
	@DatabaseField(columnName = NAME_CLIENT)
	public String nameClient = "";
	@DatabaseField(columnName = SURNAME_CLIENT)
	public String lastnameClient = "";
	@DatabaseField(columnName = EMAIL_CLIENT)
	public String mailClient = "";
	@DatabaseField(columnName = PHONE_CLIENT)
	public String phoneClient = "";
	@DatabaseField(columnName = OBSERVATIONS)
	public String observations = "No hay observaciones";
	
	
	
	public int getHistorico() {
		return historico;
	}
	public void setHistorico(int historico) {
		this.historico = historico;
	}
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

	public int getWhyLose() {
		return whyLose;
	}
	public void setWhyLose(int whyLose) {
		this.whyLose = whyLose;
	}
	public int getWhyWin() {
		return whyWin;
	}
	public void setWhyWin(int whyWin) {
		this.whyWin = whyWin;
	}

	public String getPhoneClient() {
		return phoneClient;
	}
	public void setPhoneClient(String phoneClient) {
		this.phoneClient = phoneClient;
	}
	public String getSegmentoHP() {
		return SegmentoHP;
	}
	public void setSegmentoHP(String segmentoHP) {
		SegmentoHP = segmentoHP;
	}
	public String getModelo3() {
		return modelo3;
	}
	public void setModelo3(String modelo3) {
		this.modelo3 = modelo3;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getModeloComparable() {
		return modeloComparable;
	}
	public void setModeloComparable(String modeloComparable) {
		this.modeloComparable = modeloComparable;
	}
	public String getLastnameClient() {
		return lastnameClient;
	}
	public void setLastnameClient(String lastnameClient) {
		this.lastnameClient = lastnameClient;
	}
	public String getMailClient() {
		return mailClient;
	}
	public void setMailClient(String mailClient) {
		this.mailClient = mailClient;
	}

	
	
	
}
