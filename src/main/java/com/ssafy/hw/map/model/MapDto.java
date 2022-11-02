package com.ssafy.hw.map.model;

public class MapDto {
	private String no;
	private String aptCode;
	private String dong;
	private String dongCode;
	private String apartmentName;
	private String area;
	private String dealAmount;
	private int dealYear;
	private int dealMonth;
	private int dealDay;
	private String lat; // 위도
	private String lng; // 경도
	private int buildYear; // 건축년도
	private String jibun; // 지번주소
	private StarBucksDto coffee;
	private MetroDto metro;

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getAptCode() {
		return aptCode;
	}

	public void setAptCode(String aptCode) {
		this.aptCode = aptCode;
	}

	public String getDong() {
		return dong;
	}

	public String getDongCode() {
		return dongCode;
	}

	public void setDong(String dong) {
		this.dong = dong;
	}

	public void setDongCode(String dongCode) {
		this.dongCode = dongCode;
	}

	public String getApartmentName() {
		return apartmentName;
	}

	public void setApartmentName(String apartmentName) {
		this.apartmentName = apartmentName;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getDealAmount() {
		return dealAmount;
	}

	public void setDealAmount(String dealAmount) {
		this.dealAmount = dealAmount;
	}

	public int getDealYear() {
		return dealYear;
	}

	public void setDealYear(int dealYear) {
		this.dealYear = dealYear;
	}

	public int getDealMonth() {
		return dealMonth;
	}

	public void setDealMonth(int dealMonth) {
		this.dealMonth = dealMonth;
	}

	public int getDealDay() {
		return dealDay;
	}

	public void setDealDay(int dealDay) {
		this.dealDay = dealDay;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	public int getBuildYear() {
		return buildYear;
	}

	public void setBuildYear(int buildYear) {
		this.buildYear = buildYear;
	}

	public String getJibun() {
		return jibun;
	}

	public void setJibun(String jibun) {
		this.jibun = jibun;
	}

	public StarBucksDto getCoffee() {
		return coffee;
	}

	public void setCoffee(StarBucksDto coffee) {
		this.coffee = coffee;
	}

	public MetroDto getMetro() {
		return metro;
	}

	public void setMetro(MetroDto metro) {
		this.metro = metro;
	}

	@Override
	public String toString() {
		return "MapDto [no=" + no + ", aptCode=" + aptCode + ", dong=" + dong + ", dongCode=" + dongCode
				+ ", apartmentName=" + apartmentName + ", area=" + area + ", dealAmount=" + dealAmount + ", dealYear="
				+ dealYear + ", dealMonth=" + dealMonth + ", dealDay=" + dealDay + ", lat=" + lat + ", lng=" + lng
				+ ", buildYear=" + buildYear + ", jibun=" + jibun + "]";
	}

}
