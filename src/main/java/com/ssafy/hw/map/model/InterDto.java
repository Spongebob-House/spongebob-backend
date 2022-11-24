package com.ssafy.hw.map.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "", description = "")
public class InterDto {

	@ApiModelProperty(value = "")
	private String userId;
	@ApiModelProperty(value = "")
	private String aptCode;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getAptCode() {
		return aptCode;
	}
	public void setAptCode(String aptCode) {
		this.aptCode = aptCode;
	}
	
	

	
	

}
