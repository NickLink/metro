package ua.metro.mobileapp.datamodel;

import java.util.ArrayList;

public class RegYurData {
	int area_activity;
	int direction;
	String edrpou;
	
	//FizData Part
	String first_name;
	String last_name;
	String patronymic;
	String birthday;
	String phone;
	String email;
	
	String company_name;
	
	//Area desc
	int area_id;
	int city_id;
	String city;
	int street_id;
	String street;
	int street_type_id;
	String zip_code;
	String house;
	String apartment;
	
	//CardHolders desc
	ArrayList<Cardholders> cardholders = new ArrayList<Cardholders>();	
	
	int mall;
	boolean address_correspondence;
	Address_correspondence address_correspondence_info = new Address_correspondence();
	boolean address_actual_placement;
	String address_actual_placement_email;
	boolean gold_client;
	boolean bonus;
	boolean consent;
	
	
	public int getArea_activity() {
		return area_activity;
	}
	public void setArea_activity(int area_activity) {
		this.area_activity = area_activity;
	}
	public int getDirection() {
		return direction;
	}
	public void setDirection(int direction) {
		this.direction = direction;
	}
	public String getEdrpou() {
		return edrpou;
	}
	public void setEdrpou(String edrpou) {
		this.edrpou = edrpou;
	}
	public String getFirst_name() {
		return first_name;
	}
	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}
	public String getLast_name() {
		return last_name;
	}
	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}
	public String getPatronymic() {
		return patronymic;
	}
	public void setPatronymic(String patronymic) {
		this.patronymic = patronymic;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCompany_name() {
		return company_name;
	}
	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}
	public int getArea_id() {
		return area_id;
	}
	public void setArea_id(int area_id) {
		this.area_id = area_id;
	}
	public int getCity_id() {
		return city_id;
	}
	public void setCity_id(int city_id) {
		this.city_id = city_id;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public int getStreet_id() {
		return street_id;
	}
	public void setStreet_id(int street_id) {
		this.street_id = street_id;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public int getStreet_type_id() {
		return street_type_id;
	}
	public void setStreet_type_id(int street_type_id) {
		this.street_type_id = street_type_id;
	}
	public String getZip_code() {
		return zip_code;
	}
	public void setZip_code(String zip_code) {
		this.zip_code = zip_code;
	}
	public String getHouse() {
		return house;
	}
	public void setHouse(String house) {
		this.house = house;
	}
	public String getApartment() {
		return apartment;
	}
	public void setApartment(String apartment) {
		this.apartment = apartment;
	}
	public ArrayList<Cardholders> getCardholders() {
		return cardholders;
	}
	public void setCardholders(ArrayList<Cardholders> cardholders) {
		this.cardholders = cardholders;
	}
	public int getMall() {
		return mall;
	}
	public void setMall(int mall) {
		this.mall = mall;
	}
	public boolean isAddress_correspondence() {
		return address_correspondence;
	}
	public void setAddress_correspondence(boolean address_correspondence) {
		this.address_correspondence = address_correspondence;
	}
	public Address_correspondence getAddress_correspondence_info() {
		return address_correspondence_info;
	}
	public void setAddress_correspondence_info(
			Address_correspondence address_correspondence_info) {
		this.address_correspondence_info = address_correspondence_info;
	}
	public boolean isAddress_actual_placement() {
		return address_actual_placement;
	}
	public void setAddress_actual_placement(boolean address_actual_placement) {
		this.address_actual_placement = address_actual_placement;
	}
	public String getAddress_actual_placement_email() {
		return address_actual_placement_email;
	}
	public void setAddress_actual_placement_email(
			String address_actual_placement_email) {
		this.address_actual_placement_email = address_actual_placement_email;
	}
	public boolean isGold_client() {
		return gold_client;
	}
	public void setGold_client(boolean gold_client) {
		this.gold_client = gold_client;
	}
	public boolean isBonus() {
		return bonus;
	}
	public void setBonus(boolean bonus) {
		this.bonus = bonus;
	}
	public boolean isConsent() {
		return consent;
	}
	public void setConsent(boolean consent) {
		this.consent = consent;
	}

}
