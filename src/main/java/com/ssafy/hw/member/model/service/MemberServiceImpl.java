package com.ssafy.hw.member.model.service;

import java.security.MessageDigest;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.hw.member.model.MemberDto;
import com.ssafy.hw.member.model.mapper.MemberMapper;

@Service
public class MemberServiceImpl implements MemberService {

	@Autowired
	private MemberMapper memberMapper;

	@Override
	public int idCheck(String userId) throws Exception {
		return memberMapper.idCheck(userId);
	}

	@Override
	public int joinMember(MemberDto memberDto) throws Exception {
		return 0;
	}

	@Override
	public MemberDto loginMember(Map<String, String> map) throws Exception {
		map.put("userid", map.get("userid"));
		String userPwd = encryptPwd(map.get("userpwd"));
		map.put("userpwd", userPwd);
		MemberDto memberDto = memberMapper.loginMember(map);
		return memberMapper.loginMember(map);
	}

	@Override
	public int modifyMember(MemberDto memberDto) throws SQLException {
		return 0;
	}

	@Override
	public void deleteMember(String UserId) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public String findpw(String userId, String userName, String emailId, String emailDomain) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	private String encryptPwd(String base) {
		try {

			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(base.getBytes("UTF-8"));
			StringBuffer hexString = new StringBuffer();

			for (int i = 0; i < hash.length; i++) {
				String hex = Integer.toHexString(0xff & hash[i]);
				if (hex.length() == 1)
					hexString.append('0');
				hexString.append(hex);
			}
			System.out.println(hexString.toString());
			return hexString.toString();

		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

}
