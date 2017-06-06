package ua.metro.mobileapp.datamodel;

public class ProfileData {
	
	int id;
	String name;
	String legal_entity_name;
	String card_num;
	String email;
	String phone;
	String locale;
	int shop_id;
	boolean temp_card;
	String original_user_info;
	regionInfo region = new regionInfo();
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLegal_entity_name() {
		return legal_entity_name;
	}
	public void setLegal_entity_name(String legal_entity_name) {
		this.legal_entity_name = legal_entity_name;
	}
	public String getCard_num() {
		return card_num;
	}
	public void setCard_num(String card_num) {
		this.card_num = card_num;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getLocale() {
		return locale;
	}
	public void setLocale(String locale) {
		this.locale = locale;
	}
	public int getShop_id() {
		return shop_id;
	}
	public void setShop_id(int shop_id) {
		this.shop_id = shop_id;
	}
	public boolean isTemp_card() {
		return temp_card;
	}
	public void setTemp_card(boolean temp_card) {
		this.temp_card = temp_card;
	}
	public String getOriginal_user_info() {
		return original_user_info;
	}
	public void setOriginal_user_info(String original_user_info) {
		this.original_user_info = original_user_info;
	}
	public regionInfo getRegion() {
		return region;
	}
	public void setRegion(regionInfo region) {
		this.region = region;
	}
	
	public class regionInfo{
		
		int regionId;
		String regionTitle;
		
		public int getRegionId() {
			return regionId;
		}
		public void setRegionId(int regionId) {
			this.regionId = regionId;
		}
		public String getRegionTitle() {
			return regionTitle;
		}
		public void setRegionTitle(String regionTitle) {
			this.regionTitle = regionTitle;
		}
		
	}
}
