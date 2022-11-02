package com.ssafy.hw.map.model.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import com.ssafy.hw.map.model.CoronaDto;
import com.ssafy.hw.map.model.HospitalDto;
import com.ssafy.hw.map.model.InterDto;
import com.ssafy.hw.map.model.MapDto;
import com.ssafy.hw.map.model.MetroDto;
import com.ssafy.hw.map.model.StarBucksDto;

public interface MapService {

<<<<<<< HEAD
	ArrayList<MapDto> search(String regCode, int year, int month) throws SQLException;
=======

	ArrayList<MapDto> search(Map<String, String> map) throws SQLException;
>>>>>>> e5c4cd4f7952d7f9916469b49fa28f90112572fe

	ArrayList<InterDto> getInterDto(String userId) throws SQLException;

	void addinter(Map<String, String> map) throws SQLException;

	void delinter(Map<String, String> map) throws SQLException;

	ArrayList<CoronaDto> corona(Map<String, String> map) throws SQLException;

	ArrayList<HospitalDto> hospital(Map<String, String> map) throws SQLException;

<<<<<<< HEAD
	StarBucksDto getCoffeeDto(String lat, String lng) throws SQLException;

	MetroDto getMetroDto(String lat, String lng) throws SQLException;

=======
	StarBucksDto getCoffeeDto(MapDto mapDto) throws SQLException;
	MetroDto getMetroDto(MapDto mapDto) throws SQLException;
	
>>>>>>> e5c4cd4f7952d7f9916469b49fa28f90112572fe
}
