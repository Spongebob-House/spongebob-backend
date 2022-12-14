package com.ssafy.hw.member.model.mapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.hw.member.model.MemberDto;

@Mapper
public interface MemberMapper {

	int idCheck(String userId) throws Exception; // 아이디 중복검사

	int joinMember(MemberDto memberDto) throws Exception; // 회원가입

	int modifyMember(MemberDto memberDto) throws SQLException;

	void deleteMember(String UserId) throws SQLException;

	int findpw(MemberDto memberDto) throws SQLException;

	MemberDto loginMember(Map<String, String> map) throws Exception;
	
	MemberDto getMember(String userId) throws Exception;

	public void saveRefreshToken(Map<String, String> map) throws SQLException;
	public Object getRefreshToken(String userid) throws SQLException;
	public int deleteRefreshToken(Map<String, String> map) throws SQLException;

}