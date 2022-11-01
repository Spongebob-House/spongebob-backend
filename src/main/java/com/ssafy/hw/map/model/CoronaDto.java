package com.ssafy.hw.map.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "CoronaDto (코로나 진료소 정보)", description = "코로나 진료소 이름, 주소, 평일/토/일 영업시간, 번호, 예약제를 가짐")
public class CoronaDto {
	@ApiModelProperty(value = "코로나 진료소이름")
	private String name;
	@ApiModelProperty(value = "코로나 진료소 주소")
	private String address;
	@ApiModelProperty(value = "평일 영업시간")
	private String weekday;
	@ApiModelProperty(value = "토요일 영업시간")
	private String saterday;
	@ApiModelProperty(value = "일요일 영업시간")
	private String sunday;
	@ApiModelProperty(value = "전화번호")
	private String phonenumber;
	@ApiModelProperty(value = "예약제 운영여부")
	private String conv;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getWeekday() {
		return weekday;
	}

	public void setWeekday(String weekday) {
		this.weekday = weekday;
	}

	public String getSaterday() {
		return saterday;
	}

	public void setSaterday(String saterday) {
		this.saterday = saterday;
	}

	public String getSunday() {
		return sunday;
	}

	public void setSunday(String sunday) {
		this.sunday = sunday;
	}

	public String getPhonenumber() {
		return phonenumber;
	}

	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}

	public String getConv() {
		return conv;
	}

	public void setConv(String conv) {
		this.conv = conv;
	}

	@Override
	public String toString() {
		return "CoronaDto [name=" + name + ", address=" + address + ", weekday=" + weekday + ", saterday=" + saterday
				+ ", sunday=" + sunday + ", phonenumber=" + phonenumber + ", conv=" + conv + "]";
	}

}
