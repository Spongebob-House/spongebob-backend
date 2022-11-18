package com.ssafy.hw.map.model;

public class MetroDto {
	private String name;
	private int dist;
	private String lat,lng;
	public MetroDto() {
		// TODO Auto-generated constructor stub
	}
	public MetroDto(String name, int dist, String lat, String lng) {
		super();
		this.name = name;
		this.dist = dist;
		this.lat = lat;
		this.lng = lng;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getDist() {
		return dist;
	}
	public void setDist(int dist) {
		this.dist = dist;
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public String getLng() {
		return lng;
	}
	public void setLng(String lng) {
		this.lng = lng;
	}
	@Override
	public String toString() {
		return "StarBucksDto [name=" + name + ", dist=" + dist + ", lat=" + lat + ", lng=" + lng + "]";
	}
	
	
	
}
