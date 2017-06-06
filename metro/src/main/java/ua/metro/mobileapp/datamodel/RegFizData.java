package ua.metro.mobileapp.datamodel;

public class RegFizData {
	String first_name;
	String last_name;
	String patronymic;
	String birthday;
	String phone;
	String email;
	boolean receive_info_email;
	int mall;
	int bonus = 1;
	
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
	public boolean isReceive_info_email() {
		return receive_info_email;
	}
	public void setReceive_info_email(boolean receive_info_email) {
		this.receive_info_email = receive_info_email;
	}
	public int getMall() {
		return mall;
	}
	public void setMall(int mall) {
		this.mall = mall;
	}
	public int getBonus() {
		return bonus;
	}
	public void setBonus(int bonus) {
		this.bonus = bonus;
	}
}
