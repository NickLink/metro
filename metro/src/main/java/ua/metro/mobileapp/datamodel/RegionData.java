package ua.metro.mobileapp.datamodel;

import java.util.ArrayList;

public class RegionData {
	int id;
	String title;
	ArrayList<ShopsData> shops = new ArrayList<ShopsData>();
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public ArrayList<ShopsData> getShops() {
		return shops;
	}
	public void setShops(ArrayList<ShopsData> shops) {
		this.shops = shops;
	}

}
