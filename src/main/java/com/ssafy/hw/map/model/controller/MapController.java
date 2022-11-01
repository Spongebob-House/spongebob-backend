package com.ssafy.hw.map.model.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ssafy.hw.map.model.CoronaDto;
import com.ssafy.hw.map.model.HospitalDto;
import com.ssafy.hw.map.model.service.MapService;
import com.ssafy.hw.member.model.MemberDto;

@Controller
@RequestMapping("/map")
public class MapController {

	private final Logger logger = LoggerFactory.getLogger(MapController.class);
	private final MapService mapService;
	
	@Autowired
	public MapController(MapService mapService) {
		super();
		this.mapService = mapService;
	}
	@GetMapping("/corona")
	private String corona(@RequestParam Map<String, String> map, Model model) throws SQLException {
		logger.debug("corona call parameter {}", map);
		ArrayList<CoronaDto> coronaDtoList;
		coronaDtoList = mapService.corona(map);
		model.addAttribute("mapList", coronaDtoList);
		return "/corona";
	}
	
	@GetMapping("/hospital")
	private String hospital(@RequestParam Map<String, String> map, Model model) throws SQLException {
		logger.debug("hospital call parameter {}", map);
		ArrayList<HospitalDto> hospitalDtoList;
		hospitalDtoList = mapService.hospital(map);
		model.addAttribute("mapList", hospitalDtoList);
		return "/hospital";
	}
	
	@DeleteMapping("/delinter")
	private String delinter(@RequestParam Map<String, String> map, HttpSession session) throws SQLException {
		logger.debug("delinter call parameter {}", map);
		String userId;
		MemberDto memberDto = (MemberDto)session.getAttribute("userinfo");
		userId = memberDto.getUserId();
		map.put("userid", userId);
		mapService.delinter(map);
		return "/map/search";
	}
	
	@PostMapping("/addinter")
	@Transactional
	private String addinter(@RequestParam Map<String, String> map, HttpSession session, Model model) throws SQLException{
		String userId;
		MemberDto memberDto = (MemberDto)session.getAttribute("userinfo");
		userId = memberDto.getUserId();
		map.put("userid", userId);
		if(mapService.interDupCheck(map) != 0) {
			model.addAttribute("msg", "이미 관심지역에 등록되어있습니다.");
			return "/map?act=search";
		}
		return "/map/search";
	}
	
	
	
}
