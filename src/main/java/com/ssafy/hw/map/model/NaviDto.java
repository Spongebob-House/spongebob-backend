package com.ssafy.hw.map.model;

public class NaviDto {
	private String dongCode;
	private String name;
	
	public NaviDto() {
		// TODO Auto-generated constructor stub
	}

	public NaviDto(String dongCode, String name) {
		super();
		this.dongCode = dongCode;
		this.name = name;
	}

	public String getDongCode() {
		return dongCode;
	}

	public void setDongCode(String dongCode) {
		this.dongCode = dongCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "naviDto [dongCode=" + dongCode + ", name=" + name + "]";
	}
	
	
}
