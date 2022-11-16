package com.ssafy.hw.memo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "MemoDto : 댓글 정보", description = "댓글의 상세 정보를 나타낸다.")
public class MemoDto {
	@ApiModelProperty(value = "댓글 작성자")
	private String userid;
	@ApiModelProperty(value = "댓글 글 번호")
	private int articleno;

	@ApiModelProperty(value = "댓글 번호")
	private int memono;
	@ApiModelProperty(value = "댓글 내용")
	private String comment;
	@ApiModelProperty(value = "댓글 작성 시간")
	private String memotime;

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public int getArticleno() {
		return articleno;
	}

	public void setArticleno(int articleno) {
		this.articleno = articleno;
	}

	public int getMemono() {
		return memono;
	}

	public void setMemono(int memono) {
		this.memono = memono;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getMemotime() {
		return memotime;
	}

	public void setMemotime(String memotime) {
		this.memotime = memotime;
	}

	@Override
	public String toString() {
		return "MemoDto [memono=" + memono + ", comment=" + comment + ", memotime=" + memotime + "]";
	}

}
