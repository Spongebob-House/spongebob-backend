package com.ssafy.hw.map.model.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.hw.map.model.CoronaDto;
import com.ssafy.hw.map.model.HospitalDto;
import com.ssafy.hw.map.model.InterDto;
import com.ssafy.hw.map.model.MapDto;
import com.ssafy.hw.map.model.MetroDto;
import com.ssafy.hw.map.model.StarBucksDto;
import com.ssafy.hw.map.model.mapper.MapMapper;

@Service
public class MapServiceImpl implements MapService {// 여기서 무엇을 하느냐?

	private MapMapper mapMapper;

	@Autowired
	private MapServiceImpl(MapMapper mapMapper) {
		this.mapMapper = mapMapper;
	}

	@Override
	public ArrayList<MapDto> search(Map<String, String> map) throws SQLException {
		// TODO Auto-generated method stub
		return mapMapper.search(map);
	}

	@Override
	public ArrayList<InterDto> getInterDto(String userId) throws SQLException {
		// TODO Auto-generated method stub
		return mapMapper.getInterDto(userId);
	}

	@Override
	public boolean addinter(InterDto interDto) throws SQLException {
		return mapMapper.addinter(interDto) == 1;
	}

	@Override
	public boolean delinter(Map<String, String> map) throws SQLException {
		return mapMapper.delinter(map) == 1;

	}

	@Override
	public ArrayList<CoronaDto> corona(Map<String, String> map) throws SQLException {
		// TODO Auto-generated method stub
		return mapMapper.corona(map);
	}

	@Override
	public ArrayList<HospitalDto> hospital(Map<String, String> map) throws SQLException {
		// TODO Auto-generated method stub
		return mapMapper.hospital(map);
	}

	@Override

	public StarBucksDto getCoffeeDto(MapDto mapDto) throws SQLException {
		// TODO Auto-generated method stub
		Map<String, Double> map = new HashMap<String, Double>();
		map.put("lat", Double.parseDouble(mapDto.getLat()));
		map.put("lng", Double.parseDouble(mapDto.getLng()));
		StarBucksDto coffee = mapMapper.getCoffee(map);
		if (coffee != null)
			coffee.setDist(getDist(mapDto.getLat(), mapDto.getLng(), coffee.getLat(), coffee.getLng()));
		return coffee;
	}

	@Override
	public MetroDto getMetroDto(MapDto mapDto) throws SQLException {
		// TODO Auto-generated method stub

		Map<String, Double> map = new HashMap<String, Double>();
		map.put("lat", Double.parseDouble(mapDto.getLat()));
		map.put("lng", Double.parseDouble(mapDto.getLng()));
		MetroDto metro = mapMapper.getMetro(map);
		if (metro != null)
			metro.setDist(getDist(mapDto.getLat(), mapDto.getLng(), metro.getLat(), metro.getLng()));
		return metro;
	}

	private int getDist(String lat1, String lng1, String lat2, String lng2) {
		double lt1 = Double.parseDouble(lat1);
		double ln1 = Double.parseDouble(lng1);
		double lt2 = Double.parseDouble(lat2);
		double ln2 = Double.parseDouble(lng2);

		double X = (Math.cos(lt1) * 6400 * 2 * 3.14 / 360) * Math.abs(ln1 - ln2);

		double Y = 111 * Math.abs(lt1 - lt2);

		int D = (int) (Math.sqrt(X * X + Y * Y) * 1000);
		return D;
	}

}