package ua.metro.mobileapp.datamodel;

public class ShopsData {
	int id;
	String region_title;
	int region_id;
	String address;
	String map_embedded;
	String lat;
	String lon;
	double distance;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getRegion_title() {
		return region_title;
	}
	public void setRegion_title(String region_title) {
		this.region_title = region_title;
	}
	public int getRegion_id() {
		return region_id;
	}
	public void setRegion_id(int region_id) {
		this.region_id = region_id;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getMap_embedded() {
		return map_embedded;
	}
	public void setMap_embedded(String map_embedded) {
		this.map_embedded = map_embedded;
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public String getLon() {
		return lon;
	}
	public void setLon(String lon) {
		this.lon = lon;
	}
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
}
