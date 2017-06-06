package ua.metro.mobileapp.HttpMetods;

import ua.metro.mobileapp.R;
import ua.metro.mobileapp.application.MeApp;
import android.content.Context;
import android.content.SharedPreferences;

public class UserState {

	static SharedPreferences sPref;

	public static void saveUserState(Context context){
		sPref = context.getSharedPreferences(context.getString(R.string.app_PREFNAME), Context.MODE_PRIVATE);
		sPref.edit()
		.putInt("user_id", MeApp.regData.getUser().getId())
		.putString("user_name", MeApp.regData.getUser().getName())
		.putString("user_legal_entity_name", MeApp.regData.getUser().getLegal_entity_name())
		.putString("user_card_num", MeApp.regData.getUser().getCard_num())
		.putString("user_email", MeApp.regData.getUser().getEmail())
		.putString("user_phone", MeApp.regData.getUser().getPhone())
		.putString("user_locale", MeApp.regData.getUser().getLocale())
		.putInt("user_shop_id", MeApp.regData.getUser().getShop_id())
		.putBoolean("user_temp_card", MeApp.regData.getUser().isTemp_card())
		.putString("user_valid_to", MeApp.regData.getUser().getValid_to())
		.putInt("user_region_id", MeApp.regData.getUser().getRegion().getRegionId())
		.putString("user_region_title", MeApp.regData.getUser().getRegion().getRegionTitle())
		.putString("token", MeApp.regData.getToken())
		.putInt("cart_items_count", MeApp.regData.getUser().getCount_basket()).apply();		
	}
	
	public static void restoreUserState(Context context){	
		sPref = context.getSharedPreferences(context.getString(R.string.app_PREFNAME), Context.MODE_PRIVATE);
		if (MeApp.regData.getToken() != null) return;
		MeApp.regData.getUser().setId(sPref.getInt("user_id", 0));
		MeApp.regData.getUser().setName(sPref.getString("user_name", ""));
		MeApp.regData.getUser().setLegal_entity_name(sPref.getString("user_legal_entity_name", ""));
		MeApp.regData.getUser().setCard_num(sPref.getString("user_card_num", ""));
		MeApp.regData.getUser().setEmail(sPref.getString("user_email", ""));
		MeApp.regData.getUser().setPhone(sPref.getString("user_phone", ""));
		MeApp.regData.getUser().setLocale(sPref.getString("user_locale", ""));
		MeApp.regData.getUser().setShop_id(sPref.getInt("user_shop_id", 0));
		MeApp.regData.getUser().setTemp_card(sPref.getBoolean("user_temp_card", false));
		MeApp.regData.getUser().setValid_to(sPref.getString("user_valid_to", ""));
		MeApp.regData.getUser().getRegion().setRegionId(sPref.getInt("user_region_id", 0));
		MeApp.regData.getUser().getRegion().setRegionTitle(sPref.getString("user_region_title", ""));
		MeApp.regData.setToken(sPref.getString("token", ""));
		MeApp.regData.getUser().setCount_basket(sPref.getInt("cart_items_count", 0));
	}
}
