
package com.ssafy.hw.map.model.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ssafy.hw.map.model.CoronaDto;
import com.ssafy.hw.map.model.HospitalDto;
import com.ssafy.hw.map.model.InterDto;
import com.ssafy.hw.map.model.MapDto;
import com.ssafy.hw.map.model.MetroDto;
import com.ssafy.hw.map.model.NaviDto;
import com.ssafy.hw.map.model.StarBucksDto;

public interface MapService {

	ArrayList<MapDto> search(Map<String, String> map) throws SQLException;

	boolean addinter(InterDto interDto) throws SQLException;

	boolean delinter(Map<String, String> map) throws SQLException;

	ArrayList<CoronaDto> corona(Map<String, String> map) throws SQLException;

	ArrayList<HospitalDto> hospital(Map<String, String> map) throws SQLException;

	StarBucksDto getCoffeeDto(MapDto mapDto) throws SQLException;

	MetroDto getMetroDto(MapDto mapDto) throws SQLException;

	List<MapDto> getInterDto(String userId) throws SQLException;

	List<NaviDto> navi(String text) throws SQLException;

	List<MapDto> aptSearch(Map<String, String> latLng) throws SQLException;

	List<MapDto> detail(MapDto mapDto) throws SQLException;

}

