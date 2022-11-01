package com.ssafy.hw.member.mapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.hw.member.model.MemberDto;

@Mapper
public interface MemberMapper {

	int idCheck(String userId) throws SQLException;

	void joinMember(MemberDto memberDto) throws SQLException;

	MemberDto loginMember(Map<String, String> map) throws SQLException;

	/* Admin */
	List<MemberDto> listMember(Map<String, Object> map) throws SQLException;

	MemberDto getMember(String userId) throws SQLException;

	void updateMember(MemberDto memberDto) throws SQLException;

	void deleteMember(String userId) throws SQLException;

}
