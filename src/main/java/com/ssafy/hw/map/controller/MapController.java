package com.ssafy.hw.map.controller;

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
import com.ssafy.hw.map.model.InterDto;
import com.ssafy.hw.map.model.MapDto;
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
		logger.info("mapController 호출");
		this.mapService = mapService;
	}
	@GetMapping("/corona")
	private String corona(@RequestParam Map<String, String> map, Model model) throws SQLException {
		logger.debug("corona call parameter {}", map);
		ArrayList<CoronaDto> coronaDtoList;
		coronaDtoList = mapService.corona(map);
		model.addAttribute("mapList", coronaDtoList);
		return "/map/corona";
	}
	
	@GetMapping("/hospital")
	private String hospital(@RequestParam Map<String, String> map, Model model) throws SQLException {
		logger.debug("hospital call parameter {}", map);
		ArrayList<HospitalDto> hospitalDtoList;
		hospitalDtoList = mapService.hospital(map);
		model.addAttribute("mapList", hospitalDtoList);
		return "/map/hospital";
	}
	
	@GetMapping("/delinter")
	private String delinter(@RequestParam Map<String, String> map, HttpSession session, Model model) throws SQLException {
		logger.debug("delinter call parameter {}", map);
		String userId;
		ArrayList<InterDto> interDtoList;
		MemberDto memberDto = (MemberDto)session.getAttribute("userinfo");
		userId = memberDto.getUserId();
		map.put("userid", userId);
		mapService.delinter(map);
		interDtoList = mapService.getInterDto(memberDto.getUserId());
		model.addAttribute("interList", interDtoList);
		return "/map/search";
	}
	
	@PostMapping("/addinter")
	@Transactional
	private String addinter(@RequestParam Map<String, String> map, HttpSession session, Model model) throws SQLException{
		String userId;
		ArrayList<InterDto> interDtoList;
		MemberDto memberDto = (MemberDto)session.getAttribute("userinfo");
		userId = memberDto.getUserId();
		map.put("userid", userId);
		mapService.addinter(map);
		interDtoList = mapService.getInterDto(memberDto.getUserId());
		model.addAttribute("interList", interDtoList);
		return "/map/search";
	}
	
	@GetMapping("/search")
	private String search(@RequestParam Map<String, String> map, HttpSession session, Model model) throws SQLException {
		ArrayList<MapDto> mapDtoList;
		ArrayList<InterDto> interDtoList;
		MemberDto memberDto = (MemberDto)session.getAttribute("userinfo");
		interDtoList = mapService.getInterDto(memberDto.getUserId());
		mapDtoList = mapService.search(map);
		for(MapDto mapDto:mapDtoList) {
			mapDto.setCoffee(mapService.getCoffeeDto(mapDto));
			mapDto.setMetro(mapService.getMetroDto(mapDto));
		}
		model.addAttribute("interList", interDtoList);
		model.addAttribute("mapList", mapDtoList);
				
		return "/map/search";
	} 
}
