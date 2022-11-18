package com.ssafy.hw.map.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "HospitalDto(병원 정보)", description = "병원명, 병원주소,병원 번호, 종류를 가짐")
public class HospitalDto {

	@ApiModelProperty(value = "병원명")
	private String name;
	@ApiModelProperty(value = "병원 주소")
	private String address;
	@ApiModelProperty(value = "병원 번호")
	private String phonenumber;
	@ApiModelProperty(value = "병원 종류(A/B)")
	private String type;

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

	public String getPhonenumber() {
		return phonenumber;
	}

	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "HospitalDto [name=" + name + ", address=" + address + ", phonenumber=" + phonenumber + ", type=" + type
				+ "]";
	}

}
