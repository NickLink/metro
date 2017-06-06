package ua.metro.mobileapp.datamodel;


public class RegData {
	
	String cardNum;
	int status;
	String phone;
	String email;
	String name;
	String message;
	boolean is_temp_card;
	String card_number;	
	boolean success;
	boolean error;
	int code;
	String form_email;
	String form_phone;
	String form_message;	
	String token;
	user_data user = new user_data();
	
	public String getCardNum() {
		return cardNum;
	}

	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isIs_temp_card() {
		return is_temp_card;
	}

	public void setIs_temp_card(boolean is_temp_card) {
		this.is_temp_card = is_temp_card;
	}

	public String getCard_number() {
		return card_number;
	}

	public void setCard_number(String card_number) {
		this.card_number = card_number;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public boolean getError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getForm_email() {
		return form_email;
	}

	public void setForm_email(String form_email) {
		this.form_email = form_email;
	}

	public String getForm_phone() {
		return form_phone;
	}

	public void setForm_phone(String form_phone) {
		this.form_phone = form_phone;
	}

	public String getForm_message() {
		return form_message;
	}

	public void setForm_message(String form_message) {
		this.form_message = form_message;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public user_data getUser() {
		return user;
	}

	public void setUser(user_data user) {
		this.user = user;
	}

	public class user_data {
		
		int id;
		String name;
		String email;
		String card_num;
		String phone;
		String locale;
		int shop_id;
		regionInfo region = new regionInfo();
		boolean error;
		boolean temp_card;
		String code;
		int count_basket;
		String legal_entity_name;
		String valid_to;
		String message;
		
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
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		public String getCard_num() {
			return card_num;
		}
		public void setCard_num(String card_num) {
			this.card_num = card_num;
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
		public regionInfo getRegion() {
			return region;
		}
		public void setRegion(regionInfo region) {
			this.region = region;
		}
		public boolean isError() {
			return error;
		}
		public void setError(boolean error) {
			this.error = error;
		}
		public boolean isTemp_card() {
			return temp_card;
		}
		public void setTemp_card(boolean temp_card) {
			this.temp_card = temp_card;
		}
		public String getCode() {
			return code;
		}
		public void setCode(String code) {
			this.code = code;
		}
		public int getCount_basket() {
			return count_basket;
		}
		public void setCount_basket(int count_basket) {
			this.count_basket = count_basket;
		}
		public String getLegal_entity_name() {
			return legal_entity_name;
		}
		public void setLegal_entity_name(String legal_entity_name) {
			this.legal_entity_name = legal_entity_name;
		}
		public String getValid_to() {
			return valid_to;
		}
		public void setValid_to(String valid_to) {
			this.valid_to = valid_to;
		}
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
		
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
