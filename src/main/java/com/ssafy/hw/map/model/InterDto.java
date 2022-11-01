package com.ssafy.hw.map.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "", description = "")
public class InterDto {

	@ApiModelProperty(value = "")
	private String userId;
	@ApiModelProperty(value = "")
	private String dongCode;
	@ApiModelProperty(value = "")
	private String dongName;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getDongCode() {
		return dongCode;
	}
	public void setDongCode(String dongCode) {
		this.dongCode = dongCode;
	}
	public String getDongName() {
		return dongName;
	}
	public void setDongName(String dongName) {
		this.dongName = dongName;
	}
	
	

}
