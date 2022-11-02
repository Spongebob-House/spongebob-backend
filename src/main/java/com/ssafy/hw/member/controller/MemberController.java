package com.ssafy.hw.member.controller;

import java.sql.SQLException;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ssafy.hw.member.model.MemberDto;
import com.ssafy.hw.member.model.service.MemberService;

@Controller
@RequestMapping("/user")
public class MemberController {
	private final Logger logger = LoggerFactory.getLogger(MemberController.class);

	private final MemberService memberService;

	@Autowired
	public MemberController(MemberService memberService) {
		logger.info("MemberCotroller 생성자 호출");
		this.memberService = memberService;
	}

	@GetMapping("/join")
	public String join() {
		return "user/join";
	}

	@GetMapping("/search/{userid}")
	@ResponseBody
	public String idCheck(@PathVariable("userid") String userId) throws Exception {
		logger.debug("idCheck userid : {}", userId);
		int cnt = memberService.idCheck(userId);
		return cnt + "";
	}

	@GetMapping("/login")
	public String login() {
		return "user/";

	}

	@PostMapping("/login")
	public String login(@RequestParam Map<String, String> map, Model model, HttpSession session,
			HttpServletResponse response) {
		logger.debug("map : {}", map.get("userid"));
		try {
			MemberDto memberDto = memberService.loginMember(map);
			logger.debug("memberDto : {}", memberDto);
			if (memberDto != null) {
				session.setAttribute("userinfo", memberDto);
				Cookie cookie = new Cookie("ssafy_id", map.get("userid"));
				if ("ok".equals(map.get("saveid"))) {
					cookie.setMaxAge(60 * 60 * 24 * 365 * 40);
				} else {
					System.out.println("cookie :" + cookie.getValue());
					cookie.setMaxAge(0);
				}
				response.addCookie(cookie);
				logger.debug("ddd");
				return "/index";
			} else {
				model.addAttribute("msg", "아이디 또는 비밀번호 확인 후 다시 로그인하세요!");
				logger.debug("dddfsfd");
				return "user/login";
			}
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("msg", "로그인 중 문제 발생!!!");
			return "error/error";
		}
	}

	@PostMapping("/join")
	public String join(MemberDto memberDto, Model model) {
		logger.debug("memberDto info : {}", memberDto);
		try {
			memberService.joinMember(memberDto);
			return "result/join";
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("msg", "회원 가입 중 문제 발생!!!");
			return "error/error";
		}
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "/index";
	}

	@GetMapping("/mypage")
	public String mypage(MemberDto memberDto) {
		return "user/mypage";
	}

	@GetMapping("/mypagemodify")
	public String mypageModify() {
		return "user/mypagemodify";
	}

	@PostMapping("/mypagemodify")
	public String modifyMypage(MemberDto memberDto, HttpSession session, Model model) {
		logger.debug("mypage modify {}");
//		MemberDto memberDto = (MemberDto) session.getAttribute("userinfo");
		try {
			System.out.println(memberDto);
			memberService.modifyMember(memberDto);
			return "user/mypage";
		} catch (SQLException e) {
			e.printStackTrace();
			model.addAttribute("msg", "마이페이지 수정 중 문제 발생 !");
			return "error/error";
		}

	}

	@GetMapping("/findpw")
	public String mvFindPwd() {
		return "user/findpw";
	}

	@PostMapping("/findpw")
	public String findPwd(@RequestParam Map<String, String> map, Model model) {
		try {
			String result = memberService.findpw(map);
			if ("".equals(result)) {
				model.addAttribute("msg2", "사용자 정보가 일치하지 않습니다.");
			} else {
				model.addAttribute("msg2", map.get("userName") + "님의 임시 비밀번호가 '" + result + "' 로 설정되었습니다.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "result/findpw";
	}

	@GetMapping("/delete")
	public String deleteMember(HttpSession session) {
		logger.debug("delete modify ");
		try {
			MemberDto memberDto = (MemberDto) session.getAttribute("userinfo");
			memberService.deleteMember(memberDto.getUserId());
			return "result/delete";
		} catch (SQLException e) {
			e.printStackTrace();
			return "error/error";
		}
	}

}
