package com.ssafy.hw.member.model.service;

import java.security.MessageDigest;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

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
		memberDto.setUserPwd(encryptPwd(memberDto.getUserPwd()));
		return memberMapper.joinMember(memberDto);
	}

	@Override
	public MemberDto loginMember(Map<String, String> map) throws Exception {
		map.put("userid", map.get("userid"));
		String userPwd = encryptPwd(map.get("userpwd"));
		map.put("userpwd", userPwd);
		// MemberDto memberDto = memberMapper.loginMember(map);
		return memberMapper.loginMember(map);
	}

	@Override
	public int modifyMember(MemberDto memberDto) throws SQLException {
		if(memberDto.getUserPwd() != null) {
			memberDto.setUserPwd(encryptPwd(memberDto.getUserPwd()));
		}
		return memberMapper.modifyMember(memberDto);
	}

	@Override
	public void deleteMember(String UserId) throws SQLException {
		memberMapper.deleteMember(UserId);
	}

	@Override
	public String findpw(MemberDto memberDto) throws SQLException {
		String random = makeRand();
		memberDto.setUserPwd(random);
		if (memberMapper.findpw(memberDto) == 1) {
			return random;
		} else {
			return "";
		}
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

	private String makeRand() {
		Random rnd = new Random();
		StringBuilder key = new StringBuilder();
		for (int i = 0; i < 6; i++) {
			int index = rnd.nextInt(3);
			switch (index) {
			case 0:
				key.append(((int) (rnd.nextInt(26)) + 97));
				break;
			case 1:
				key.append(((int) (rnd.nextInt(26)) + 65));
				break;
			case 2:
				key.append((rnd.nextInt(10)));
				break;
			}
		}
		return key.toString();
	}
	
	@Override
	public void saveRefreshToken(String userid, String refreshToken) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("userid", userid);
		map.put("token", refreshToken);
		memberMapper.saveRefreshToken(map);
	}

	@Override
	public Object getRefreshToken(String userid) throws Exception {
		return memberMapper.getRefreshToken(userid);
	}

	@Override
	public void deleRefreshToken(String userid) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("userid", userid);
		map.put("token", null);
		memberMapper.deleteRefreshToken(map);
	}

	@Override
	public MemberDto getMember(String userId) throws Exception {
		return memberMapper.getMember(userId);
	}

}
