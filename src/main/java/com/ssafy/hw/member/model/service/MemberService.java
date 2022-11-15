package com.ssafy.hw.member.model.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ssafy.hw.member.model.MemberDto;

public interface MemberService {
	int idCheck(String userId) throws Exception; // 아이디 중복검사

	int joinMember(MemberDto memberDto) throws Exception; // 회원가입

	MemberDto loginMember(Map<String, String> map) throws Exception; // 로그인

	int modifyMember(MemberDto memberDto) throws SQLException;

	void deleteMember(String UserId) throws SQLException;

	String findpw(MemberDto memberDto) throws SQLException;
}
