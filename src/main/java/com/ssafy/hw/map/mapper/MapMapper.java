package com.ssafy.hw.map.mapper;

import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.hw.map.model.CoronaDto;
import com.ssafy.hw.map.model.HospitalDto;
import com.ssafy.hw.map.model.InterDto;
import com.ssafy.hw.map.model.MapDto;
import com.ssafy.hw.map.model.MetroDto;
import com.ssafy.hw.map.model.StarBucksDto;

@Mapper
public interface MapMapper {
	ArrayList<MapDto> search(String regCode, int year, int month) throws SQLException;

	ArrayList<InterDto> getInterDto(String userId) throws SQLException;

	void addinter(String regCode, String userId) throws SQLException;

	int interDupCheck(String regCode, String userId) throws SQLException;

	void delinter(String regCode, String userId) throws SQLException;

	ArrayList<CoronaDto> corona(String sido, String gugun) throws SQLException;

	ArrayList<HospitalDto> hospital(String sido, String gugun) throws SQLException;

	StarBucksDto getCoffeeDto(String lat, String lng) throws SQLException;

	MetroDto getMetroDto(String lat, String lng) throws SQLException;

}
