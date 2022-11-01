package com.ssafy.hw.map.model.mapper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.hw.map.model.CoronaDto;
import com.ssafy.hw.map.model.HospitalDto;
import com.ssafy.hw.map.model.InterDto;
import com.ssafy.hw.map.model.MapDto;
import com.ssafy.hw.map.model.MetroDto;
import com.ssafy.hw.map.model.StarBucksDto;
@Mapper
public interface MapMapper {

	ArrayList<MapDto> search(Map<String, String> map) throws SQLException;

	ArrayList<InterDto> getInterDto(String userId) throws SQLException;

	void addinter(Map<String, String> map) throws SQLException;

	void delinter(Map<String, String> map) throws SQLException;

	ArrayList<CoronaDto> corona(Map<String, String> map) throws SQLException;

	ArrayList<HospitalDto> hospital(Map<String, String> map) throws SQLException;

	StarBucksDto getCoffee(Map<String, Double> map) throws SQLException;
	MetroDto getMetro(Map<String, Double> map) throws SQLException;

	
}
