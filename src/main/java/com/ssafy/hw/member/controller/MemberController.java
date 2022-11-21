package com.ssafy.hw.member.controller;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.hw.member.model.MemberDto;
import com.ssafy.hw.member.model.service.JwtServiceImpl;
import com.ssafy.hw.member.model.service.MemberService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/user")
@Api("사용자 컨트롤러  API V1")
public class MemberController {
	private final Logger logger = LoggerFactory.getLogger(MemberController.class);
	private static final String SUCCESS = "success";
	private static final String FAIL = "fail";

	private final MemberService memberService;
	
	@Autowired
	private JwtServiceImpl jwtService;
	
	@Autowired
	public MemberController(MemberService memberService) {
		logger.info("MemberCotroller 생성자 호출");
		this.memberService = memberService;
	}

	@ApiOperation(value = "로그인", notes = "로그인 결과 메세지를 반환한다.", response = Map.class)
	@PostMapping("/login")
	public ResponseEntity<Map<String, Object>> login(
			@RequestBody @ApiParam(value = "로그인 시 필요한 회원정보(아이디, 비밀번호).", required = true) MemberDto memberDto) {
		Map<String, String> map = new HashMap<>();
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = null;
		map.put("userid", memberDto.getUserId());
		map.put("userpwd", memberDto.getUserPwd());
		logger.debug("map : {}", map.get("userid"));
		try {
			MemberDto curMember = memberService.loginMember(map);
			logger.debug("memberDto : {}", curMember);
			if (curMember != null) {
				String accessToken = jwtService.createAccessToken("userid", curMember.getUserId());// key, data
				String refreshToken = jwtService.createRefreshToken("userid", curMember.getUserId());// key, data
				memberService.saveRefreshToken(memberDto.getUserId(), refreshToken);
				resultMap.put("access-token", accessToken);
				resultMap.put("refresh-token", refreshToken);
				resultMap.put("message", SUCCESS);
				return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
			} else {
//				model.addAttribute("msg", "아이디 또는 비밀번호 확인 후 다시 로그인하세요!");
				resultMap.put("message", FAIL);
				return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.ACCEPTED);
			}
		} catch (Exception e) {
			e.printStackTrace();
//			model.addAttribute("msg", "로그인 중 문제 발생!!!");
			resultMap.put("message", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(resultMap,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@ApiOperation(value = "회원인증", notes = "회원 정보를 담은 Token을 반환한다.", response = Map.class)
	@GetMapping("/info/{userid}")
	public ResponseEntity<Map<String, Object>> getInfo(
			@PathVariable("userid") @ApiParam(value = "인증할 회원의 아이디.", required = true) String userid,
			HttpServletRequest request) {
//		logger.debug("userid : {} ", userid);
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.UNAUTHORIZED;
		if (jwtService.checkToken(request.getHeader("access-token"))) {
			logger.info("사용 가능한 토큰!!!");
			try {
//				로그인 사용자 정보.
				MemberDto memberDto = memberService.getMember(userid);
				resultMap.put("userInfo", memberDto);
				resultMap.put("message", SUCCESS);
				status = HttpStatus.ACCEPTED;
			} catch (Exception e) {
				logger.error("정보조회 실패 : {}", e);
				resultMap.put("message", e.getMessage());
				status = HttpStatus.INTERNAL_SERVER_ERROR;
			}
		} else {
			logger.error("사용 불가능 토큰!!!");
			resultMap.put("message", FAIL);
			status = HttpStatus.UNAUTHORIZED;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}
	
	@ApiOperation(value = "로그아웃", notes = "회원 정보를 담은 Token을 제거한다.", response = Map.class)
	@GetMapping("/logout/{userid}")
	public ResponseEntity<?> removeToken(@PathVariable("userid") String userid) {
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		try {
			memberService.deleRefreshToken(userid);
			resultMap.put("message", SUCCESS);
			status = HttpStatus.ACCEPTED;
		} catch (Exception e) {
			logger.error("로그아웃 실패 : {}", e);
			resultMap.put("message", e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);

	}
	
	@ApiOperation(value = "Access Token 재발급", notes = "만료된 access token을 재발급받는다.", response = Map.class)
	@PostMapping("/refresh")
	public ResponseEntity<?> refreshToken(@RequestBody MemberDto memberDto, HttpServletRequest request)
			throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		String token = request.getHeader("refresh-token");
		logger.debug("token : {}, memberDto : {}", token, memberDto);
		if (jwtService.checkToken(token)) {
			if (token.equals(memberService.getRefreshToken(memberDto.getUserId()))) {
				String accessToken = jwtService.createAccessToken("userid", memberDto.getUserId());
				logger.debug("token : {}", accessToken);
				logger.debug("정상적으로 액세스토큰 재발급!!!");
				resultMap.put("access-token", accessToken);
				resultMap.put("message", SUCCESS);
				status = HttpStatus.ACCEPTED;
			}
		} else {
			logger.debug("리프레쉬토큰도 사용불!!!!!!!");
			status = HttpStatus.UNAUTHORIZED;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}

	@ApiOperation(value = "아이디 확인", notes = "중복된 아이디인지 여부를 반환한다. ", response = Map.class)
	@GetMapping("/search/{userid}")
	public ResponseEntity<String> idCheck(
			@PathVariable("userid") @ApiParam(value = "인증할 회원의 아이디.", required = true) String userId) throws Exception {
		logger.debug("idCheck userid : {}", userId);
		int cnt = memberService.idCheck(userId);
		return new ResponseEntity(cnt + " ", HttpStatus.OK);
	}

	@ApiOperation(value = "회원가입", notes = "회원가입할 유저의 정보", response = Map.class)
	@PostMapping("/join")
	public ResponseEntity<MemberDto> join(
			@RequestBody @ApiParam(value = "회원가입 시 필요한 회원정보", required = true) MemberDto memberDto) {
		logger.debug("memberDto info : {}", memberDto);
		try {
			return new ResponseEntity(memberService.joinMember(memberDto), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
//			model.addAttribute("msg", "회원 가입 중 문제 발생!!!");
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@ApiOperation(value = "마이페이지 수정", notes = "", response = Map.class)
	@PutMapping("/")
	public ResponseEntity<Map<String, Object>> modifyMypage(
			@RequestBody @ApiParam(value = "회원가입 시 필요한 회원정보", required = true) MemberDto memberDto) throws Exception {
		logger.debug("mypage modify {}");
//		MemberDto memberDto = (MemberDto) session.getAttribute("userinfo");
		Map<String, Object> resultMap = new HashMap<>();
		try {
			System.out.println(memberDto);
			memberService.modifyMember(memberDto);
			memberDto = memberService.getMember(memberDto.getUserId());
			resultMap.put("userInfo", memberDto);
			resultMap.put("message", SUCCESS);
			return new ResponseEntity<Map<String,Object>>(resultMap, HttpStatus.OK);
		} catch (SQLException e) {
			e.printStackTrace();
//			model.addAttribute("msg", "마이페이지 수정 중 문제 발생 !");
			resultMap.put("message", "마이페이지 수정 중 문제 발생 !");
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@ApiOperation(value = "비밀번호 찾기", notes = "", response = Map.class)
	@PostMapping("/findpw")
	public ResponseEntity<String> findPwd(@RequestBody MemberDto memberDto) {
		try {
			String result = memberService.findpw(memberDto);
			if ("".equals(result)) {
				return new ResponseEntity(HttpStatus.NO_CONTENT);
//				model.addAttribute("msg2", "사용자 정보가 일치하지 않습니다.");
			} else {
//				model.addAttribute("msg2", map.get("userName") + "님의 임시 비밀번호가 '" + result + "' 로 설정되었습니다.");
				return new ResponseEntity(result, HttpStatus.OK);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@ApiOperation(value = "회원 탈퇴", notes = "", response = Map.class)
	@DeleteMapping("/{userid}")
	public void deleteMember(@PathVariable("userid") String userid) {
		logger.debug("delete modify ");
		try {
			memberService.deleteMember(userid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

//	@GetMapping("/mypage")
//	public String mypage(MemberDto memberDto) {
//		return "user/mypage";
//	}
//
//	@GetMapping("/logout")
//	public String logout(HttpSession session) {
//		session.invalidate();
//		return "/index";
//	}
//
//	@GetMapping("/login")
//	public String login() {
//		return "user/";
//	}
//
//	@GetMapping("/mypagemodify")
//	public String mypageModify() {
//		return "user/mypagemodify";
//	}
//
//	@GetMapping("/findpw")
//	public String mvFindPwd() {
//		return "user/findpw";
//	}
//
//	@GetMapping("/join")
//	public String join() {
//		return "user/join";
//	}
}
