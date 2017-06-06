package ua.metro.mobileapp.datamodel;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by User on 29.05.2017.
 */

public class ProfileDatabaseImpl {

    private static final String NAME = "NAME";
    private static final String PHONE = "PHONE";
    private static final String CARD = "CARD";
    private static final String EMAIL = "EMAIL";
    private static final String LEGAL = "LEGAL";
    private static final String LOCALE = "LOCALE";
    private static final String SHOP = "SHOP";
    private static final String REGION = "REGION";
    private SharedPreferences prefs;

    public ProfileDatabaseImpl(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void saveProfileData(ProfileData pData){
        prefs.edit().putString(NAME, pData.getName()).apply();
        prefs.edit().putString(PHONE, pData.getPhone()).apply();
        prefs.edit().putString(CARD, pData.getCard_num()).apply();
        prefs.edit().putString(EMAIL, pData.getEmail()).apply();
        prefs.edit().putString(LEGAL, pData.getLegal_entity_name()).apply();
        prefs.edit().putString(LOCALE, pData.getLocale()).apply();
        prefs.edit().putInt(SHOP, pData.getRegion().getRegionId()).apply();
        prefs.edit().putString(REGION, pData.getRegion().getRegionTitle()).apply();
    }


    public ProfileData getProfileData(){
        ProfileData pData = new ProfileData();
        pData.setName(prefs.getString(NAME, ""));
        pData.setPhone(prefs.getString(PHONE, ""));
        pData.setCard_num(prefs.getString(CARD, ""));
        pData.setEmail(prefs.getString(EMAIL, ""));
        pData.setLegal_entity_name(prefs.getString(LEGAL, ""));
        pData.setLocale(prefs.getString(LOCALE, ""));
        pData.getRegion().setRegionId(prefs.getInt(SHOP, 0));
        pData.getRegion().setRegionTitle(prefs.getString(REGION, ""));
        return pData;
    }
}
