package com.ssafy.hw.map.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.hw.map.model.CoronaDto;
import com.ssafy.hw.map.model.HospitalDto;
import com.ssafy.hw.map.model.InterDto;
import com.ssafy.hw.map.model.MapDto;
import com.ssafy.hw.map.model.NaviDto;
import com.ssafy.hw.map.model.service.MapService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/map")
@Api("Map 컨트롤러  API V1")
public class MapController {

private final Logger logger = LoggerFactory.getLogger(MapController.class);
private final MapService mapService;
private static final String SUCCESS = "success";
private static final String FAIL = "fail";

@Autowired
public MapController(MapService mapService) {
    super();
    logger.info("mapController 호출");
    this.mapService = mapService;
}
@ApiOperation(value = "검색 리스트", notes = "사용자가 검색창에 검색한 단어를 기반으로 해당하는 리스트를 반환한다,", response = List.class)
@GetMapping("/navi/{text}")
private ResponseEntity<List<NaviDto>> navi(@PathVariable("text") @ApiParam(value = "검색어", required = true) String text) throws SQLException {
	logger.debug("search call parameter {}", text);
	List<NaviDto> searchList;
	searchList = mapService.navi(text);
	if (searchList.isEmpty()) {
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}
	return new ResponseEntity<List<NaviDto>>(searchList, HttpStatus.OK);
}

@ApiOperation(value = "아파트 리스트", notes = "사용자가 보고 있는 지도에 보이는 아파트 리스트를 반환한다,", response = List.class)
@PostMapping("/aptsearch")
private ResponseEntity<List<MapDto>> aptSearch(@RequestBody Map<String,String> latLng) throws SQLException {
	logger.debug("aptSearch call parameter {}", latLng);
	
	List<MapDto> searchList;
	searchList = mapService.aptSearch(latLng);
	
	if (searchList.isEmpty()) {
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}
	return new ResponseEntity<List<MapDto>>(searchList, HttpStatus.OK);
}

@ApiOperation(value = "코로나 진료소 정보", notes = "코로나 선별진료소 정보를 반환한다,", response = List.class)
@GetMapping("/corona/{gugun}")
private ResponseEntity<List<CoronaDto>> corona(@PathVariable("gugun") @ApiParam(value = "구군코드.", required = true) String gugun) throws SQLException {
    logger.debug("corona call parameter {}", gugun);
    Map<String, String> map = new HashMap<String, String>();
    map.put("gugun", gugun);
    List<CoronaDto> coronaDtoList;
    coronaDtoList = mapService.corona(map);
    if (coronaDtoList.isEmpty()) {
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
    return new ResponseEntity<List<CoronaDto>>(coronaDtoList, HttpStatus.OK);
}

@ApiOperation(value = "병원 정보", notes = "인근 병원 정보를 반환한다.", response = List.class)
@GetMapping("/hospital/{gugun}")
private ResponseEntity<List<HospitalDto>> hospital(@PathVariable("gugun") @ApiParam(value = "구군코드.", required = true) String gugun) throws SQLException {
    logger.debug("hospital call parameter {}", gugun);
    List<HospitalDto> hospitalDtoList;
    Map<String, String> map = new HashMap<String, String>();
    map.put("gugun", gugun);
    hospitalDtoList = mapService.hospital(map);
    if (hospitalDtoList.isEmpty()) {
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
    return new ResponseEntity<List<HospitalDto>>(hospitalDtoList, HttpStatus.OK);
}

@ApiOperation(value = "관심지역 삭제", notes = "선택한 관심 지역을 삭제한다.", response = String.class)
@DeleteMapping("/inter/{userId}/{dong}")
private ResponseEntity<String> delinter(@PathVariable("dong") @ApiParam(value = "동코드.", required = true) String dong, @PathVariable("userId") @ApiParam(value = "유저아이디.", required = true) String userId) throws SQLException {
    Map<String, String> map = new HashMap<String, String>();
    map.put("userid", userId);
    map.put("dong", dong);
    logger.debug("delinter call parameter {}", map);
    if(mapService.delinter(map)) {
		return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
	}
	return new ResponseEntity<String>(FAIL, HttpStatus.NOT_MODIFIED);
}

@ApiOperation(value = "관심지역 추가", notes = "선택한 관심 지역을 추가한다.", response = String.class)
@PostMapping("/inter")
@Transactional
private ResponseEntity<String> addinter(@RequestBody InterDto interDto) throws SQLException{
    Map<String, String> map = new HashMap<String, String>();
    
    logger.debug("addinter call parameter {}", interDto);
    if(mapService.addinter(interDto)) {
		return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
	}
	return new ResponseEntity<String>(FAIL, HttpStatus.NOT_IMPLEMENTED);
}

@ApiOperation(value = "아파트 상세 거래 내역", notes = "아파트 상세 거래 내역을 반환한다.", response = String.class)
@PostMapping("/detail")
@Transactional
private ResponseEntity<Map<String, Object>> detail(@RequestBody MapDto mapDto) throws SQLException{
	Map<String, Object> Resultmap = new HashMap<String, Object>();
	
	logger.debug("detail call parameter {}", mapDto);
	List<MapDto> dealList = mapService.detail(mapDto);
	mapDto.setCoffee(mapService.getCoffeeDto(mapDto));
    mapDto.setMetro(mapService.getMetroDto(mapDto));
    Resultmap.put("dealList", dealList);
    Resultmap.put("mapDto", mapDto);
    return new ResponseEntity<Map<String, Object>>(Resultmap, HttpStatus.OK);
}


@ApiOperation(value = "아파트 정보", notes = "동별 아파트 정보를 반환한다.", response = List.class)
@GetMapping("/search/{dong}/{year}/{month}")
private ResponseEntity<List<MapDto>> search(@PathVariable("dong") @ApiParam(value = "동코드.", required = true) String dong, @PathVariable("year") @ApiParam(value = "연도", required = true) String year, @PathVariable("month") @ApiParam(value = "월", required = true) String month) throws SQLException {
    ArrayList<MapDto> mapDtoList;
    Map<String, String> map = new HashMap<String, String>();
    map.put("year", year);
    map.put("month", month);
    map.put("dong", dong);
    mapDtoList = mapService.search(map);

            
    if (mapDtoList.isEmpty()) {
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
    return new ResponseEntity<List<MapDto>>(mapDtoList, HttpStatus.OK);
} 

@ApiOperation(value = "관심 지역 정보", notes = "관심 지역 리스트를 반환한다.", response = List.class)
@GetMapping("/inter/{userId}")
private ResponseEntity<List<InterDto>> getinter(@PathVariable("userId") @ApiParam(value = "유저아이디", required = true) String userId) throws SQLException {
	List<InterDto> interDtoList;
	interDtoList = mapService.getInterDto(userId);
	if (interDtoList.isEmpty()) {
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}
	return new ResponseEntity<List<InterDto>>(interDtoList, HttpStatus.OK);
} 
}