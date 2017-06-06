package ua.metro.mobileapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import ua.metro.mobileapp.HttpMetods.HTTPAsynkTask;
import ua.metro.mobileapp.UI.BottomDialog;
import ua.metro.mobileapp.UI.OnSingleClickListener;
import ua.metro.mobileapp.application.MeApp;

public class Out_Register_Yur_3_3 extends Activity implements OnTaskCompleted {
	Typeface clearSans, clearSansMedium, clearSansBold;
	static Typeface clearSansLight;
	ImageButton iconBackImageButton;
	TextView reg_regError;
	ScrollView mainScrollView;
	RadioGroup reg_yurRadioGroup1, reg_yurRadioGroup2;
	View subHeader;
	Spinner spinnerRegion, spinnerStreetType;
	EditText reg_CityEditText, reg_StreetEditText, reg_HouseEditText, 
		reg_FlatEditText, reg_IndexEditText, reg_EmailAddressEditText;
	int selected_Id1 = 1, selected_Id2 = 1;
	String [] regionArrayTitle, streettypeArrayTitle;
	int [] regionArrayID, streettypeArrayID;
	int spinnerRegionPosition = 0, spinnerStreetTypePosition = 0, spiner_textSize = 14;
	CheckBox getGoldStatus;
	HashMap<String,String> params = new HashMap<String,String>();
	boolean address_correspondence = true, address_actual_placement = true;
	ProgressDialog loader;
	Configuration config;
	LinearLayout reg_yurGetProposal2Layout, reg_yurGetProposal4Layout;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.out_registration_yur_3_3);
		
		clearSans = FontCache.get("fonts/clearsans.ttf", getBaseContext());
		clearSansBold = FontCache.get("fonts/clearsansbold.ttf", getBaseContext());
		clearSansMedium = FontCache.get("fonts/clearsansmedium.ttf", getBaseContext());
		clearSansLight = FontCache.get("fonts/clearsanslight.ttf", getBaseContext());
		
		//Load Data for spinnerRegion & spinnerStreetType
        HTTPAsynkTask mt = new HTTPAsynkTask(Out_Register_Yur_3_3.this,
				GlobalConstants.GET_GEO_AREA, 
				//null, null, 
				null, 
				Out_Register_Yur_3_3.this);
		mt.execute();
		HTTPAsynkTask mt2 = new HTTPAsynkTask(Out_Register_Yur_3_3.this,
				GlobalConstants.GET_GEO_STREETTYPE, 
				//null, null, 
				null, 
				Out_Register_Yur_3_3.this);
		mt2.execute();
		
		config = getResources().getConfiguration();
        if (config.smallestScreenWidthDp >= 720) {
		    // sw720dp
        	spiner_textSize = 18;
		  }
		  else if (config.smallestScreenWidthDp >= 600) {
		    // sw600dp
			  spiner_textSize = 17;;
		  }
		  else if (config.smallestScreenWidthDp >= 480) {
			    // sw600dp
			  spiner_textSize = 16;
		  }
		  else {
		    // all other
			  spiner_textSize = 14;		  
		  }
		
		reg_yurGetProposal2Layout = (LinearLayout)findViewById(R.id.reg_yurGetProposal2Layout);
		reg_yurGetProposal2Layout.setVisibility(View.GONE);
		reg_yurGetProposal4Layout = (LinearLayout)findViewById(R.id.reg_yurGetProposal4Layout);
		reg_yurGetProposal4Layout.setVisibility(View.GONE);
		subHeader = (View) findViewById(R.id.subheader_layout);
		// Back button
		iconBackImageButton = (ImageButton) subHeader.findViewById(R.id.iconBackImageButton);
		iconBackImageButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Back Button
				finish();
			}
		});	
		
		mainScrollView = (ScrollView) findViewById(R.id.mainScrollView);
		reg_yurRadioGroup1 = (RadioGroup) findViewById(R.id.reg_yurRadioGroup1);
		reg_yurRadioGroup2 = (RadioGroup) findViewById(R.id.reg_yurRadioGroup2);
		RadioButton reg_yurGetProposal1 = (RadioButton)findViewById(R.id.reg_yurGetProposal1);
		RadioButton reg_yurGetProposal2 = (RadioButton)findViewById(R.id.reg_yurGetProposal2);
		RadioButton reg_yurGetProposal3 = (RadioButton)findViewById(R.id.reg_yurGetProposal3);
		RadioButton reg_yurGetProposal4 = (RadioButton)findViewById(R.id.reg_yurGetProposal4);
		
		TextView subTitleTextView = (TextView) subHeader.findViewById(R.id.subTitleTextView);		
		TextView textViewRegion = (TextView) findViewById(R.id.textViewRegion);
		TextView textViewCity = (TextView) findViewById(R.id.textViewCity);
		TextView textViewStreetType = (TextView) findViewById(R.id.textViewStreetType);
		TextView textViewStreet = (TextView) findViewById(R.id.textViewStreet);
		TextView textViewHouse = (TextView) findViewById(R.id.textViewHouse);
		TextView textViewFlat = (TextView) findViewById(R.id.textViewFlat);
		TextView textViewIndex = (TextView) findViewById(R.id.textViewIndex);
		TextView textViewEmailAddress = (TextView) findViewById(R.id.textViewEmailAddress);
		TextView reg_allFieldsMust = (TextView) findViewById(R.id.reg_allFieldsMust);
		TextView reg_regAgreement = (TextView) findViewById(R.id.reg_regAgreement);		
		TextView reg_regThreeThree = (TextView) findViewById(R.id.reg_regThreeThree);
		final Button reg_regCONTINUE = (Button) findViewById(R.id.reg_regCONTINUE);
		reg_regError = (TextView) findViewById(R.id.reg_regError);
		
		reg_CityEditText = (EditText) findViewById(R.id.reg_CityEditText);
		reg_StreetEditText = (EditText) findViewById(R.id.reg_StreetEditText);
		reg_HouseEditText = (EditText) findViewById(R.id.reg_HouseEditText);
		reg_FlatEditText = (EditText) findViewById(R.id.reg_FlatEditText);
		reg_IndexEditText = (EditText) findViewById(R.id.reg_IndexEditText);
		reg_EmailAddressEditText = (EditText) findViewById(R.id.reg_EmailAddressEditText);
		
		spinnerRegion = (Spinner) findViewById(R.id.spinnerRegion);
		spinnerStreetType = (Spinner) findViewById(R.id.spinnerStreetType);
		getGoldStatus = (CheckBox) findViewById(R.id.getGoldStatus);

		//Set fonts
		subTitleTextView.setTypeface(clearSansMedium);
		textViewRegion.setTypeface(clearSansBold);
		textViewCity.setTypeface(clearSansBold);
		textViewStreetType.setTypeface(clearSansBold);
		textViewStreet.setTypeface(clearSansBold);
		textViewHouse.setTypeface(clearSansBold);
		textViewFlat.setTypeface(clearSansBold);
		textViewIndex.setTypeface(clearSansBold);
		textViewEmailAddress.setTypeface(clearSansBold);
		reg_allFieldsMust.setTypeface(clearSansBold);
		reg_regAgreement.setTypeface(clearSansBold);
		reg_regThreeThree.setTypeface(clearSansBold);
		reg_regCONTINUE.setTypeface(clearSansMedium); 

		reg_yurGetProposal1.setTypeface(clearSansBold);
		reg_yurGetProposal2.setTypeface(clearSansBold);
		reg_yurGetProposal3.setTypeface(clearSansBold);
		reg_yurGetProposal4.setTypeface(clearSansBold);

		
		reg_yurRadioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {		
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.reg_yurGetProposal1:
					selected_Id1 = 1;
					reg_yurGetProposal2Layout.setVisibility(View.GONE);
					address_correspondence = true;
					break;
				case R.id.reg_yurGetProposal2:
					selected_Id1 =2;				
					reg_yurGetProposal2Layout.setVisibility(View.VISIBLE);
					address_correspondence = false;
					break;
				}
			}
		});
		
		reg_yurRadioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {		
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.reg_yurGetProposal3:
					selected_Id2 = 1;
					reg_yurGetProposal4Layout.setVisibility(View.GONE);
					address_actual_placement = true;
					break;
				case R.id.reg_yurGetProposal4:
					selected_Id2 =2;
					reg_yurGetProposal4Layout.setVisibility(View.VISIBLE);
					address_actual_placement = false;
					break;
				}
			}
		});

		// TODO 
		reg_regCONTINUE.setOnClickListener(new OnSingleClickListener() {
			@Override
			public void onSingleClick(View v) {
				
				if (checkAllFieldsCorrect()){
					reg_regError.setVisibility(View.GONE);
					putAllDataInFields();					
					//TODO AsynkTask task REGISTER YUR_OSOBA
					
					loader = new ProgressDialog(Out_Register_Yur_3_3.this);
					loader.setMessage(getString(R.string.app_Loading));
					loader.show();
					
					HTTPAsynkTask yurReg = new HTTPAsynkTask(Out_Register_Yur_3_3.this, 
							GlobalConstants.POST_REGISTRATION_YUR, 
							//null, null, 
							params, 
							Out_Register_Yur_3_3.this);
					yurReg.execute();
					
					
				} else {
					if (!reg_regError.isShown()){
						reg_regError.setVisibility(View.VISIBLE);
					}
					runOnUiThread(new Runnable() {
				        @Override
				        public void run() {
				        	mainScrollView.fullScroll(View.FOCUS_UP);
				        }
				    });
				}			
			}
		});
		reg_regCONTINUE.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                	reg_regCONTINUE.setAlpha((float) 0.55);
                    return false;
                case MotionEvent.ACTION_UP:
                	reg_regCONTINUE.setAlpha((float) 1.0);
                    return false;
                default:
                    return false;
                }
            }
      	}); 
		
		setAnalytics();
	}
	
	void putAllDataInFields(){
		
		params.put("area_activity", Integer.toString(MeApp.regYurData.getArea_activity()));		
		params.put("direction", Integer.toString(MeApp.regYurData.getDirection()));
		params.put("edrpou", MeApp.regYurData.getEdrpou());
		params.put("first_name", MeApp.regYurData.getFirst_name());
		params.put("last_name", MeApp.regYurData.getLast_name());
		params.put("patronymic", MeApp.regYurData.getPatronymic());
		//params.put("birthday", "11-11-2011"); //?????????????????????????????????????????????????
		params.put("phone", MeApp.regYurData.getPhone());
		//params.put("formatted_phone", "30999999999"); //?????????????????????????????????????????????????
		params.put("email",  MeApp.regYurData.getEmail());
		params.put("company_name",  MeApp.regYurData.getCompany_name());
		params.put("area_id",  Integer.toString(MeApp.regYurData.getArea_id()));
		//params.put("city_id", "999999"); //?????????????????????????????????????????????????
		params.put("city", MeApp.regYurData.getCity());
		//params.put("street_id", "999999"); //?????????????????????????????????????????????????
		params.put("street", MeApp.regYurData.getStreet());
		params.put("street_type_id", Integer.toString(MeApp.regYurData.getStreet_type_id()));
		params.put("zip_code", MeApp.regYurData.getZip_code());
		params.put("house", MeApp.regYurData.getHouse());
		
		if(MeApp.regYurData.getApartment() != null && MeApp.regYurData.getApartment().length() > 0)
			params.put("apartment", MeApp.regYurData.getApartment());
		else 
			params.put("apartment", "1");
		
		//Cardholders cycle
		for (int i = 0; i < MeApp.regYurData.getCardholders().size(); i++){
			params.put("cardholders[" + i + "][first_name]", MeApp.regYurData.getCardholders().get(i).getFirst_name());
			params.put("cardholders[" + i + "][last_name]", MeApp.regYurData.getCardholders().get(i).getLast_name());
			params.put("cardholders[" + i + "][patronymic]", MeApp.regYurData.getCardholders().get(i).getPatronymic());
			params.put("cardholders[" + i + "][phone]", MeApp.regYurData.getCardholders().get(i).getPhone());
			params.put("cardholders[" + i + "][email]", MeApp.regYurData.getCardholders().get(i).getEmail());	
		}
		
		params.put("mall", Integer.toString(MeApp.regYurData.getMall()));
		
		if (!address_correspondence) {
			params.put("address_correspondence", "false");
			//
			params.put("address_correspondence_info[area_id]", Integer.toString(regionArrayID[spinnerRegionPosition]));
			//params.put("address_correspondence_info[city_id]", "999999"); //?????????????????????????????????????????????????
			params.put("address_correspondence_info[city]", reg_CityEditText.getText().toString());
			//params.put("address_correspondence_info[street_id]", "999999"); //?????????????????????????????????????????????????
			params.put("address_correspondence_info[street]", reg_StreetEditText.getText().toString());
			params.put("address_correspondence_info[street_type_id]", Integer.toString(streettypeArrayID[spinnerStreetTypePosition]));
			params.put("address_correspondence_info[zip_code]", reg_IndexEditText.getText().toString());
			params.put("address_correspondence_info[house]", reg_HouseEditText.getText().toString());
			if (reg_FlatEditText.getText().toString() != null && reg_FlatEditText.getText().toString().length() > 0)
				params.put("address_correspondence_info[apartment]", reg_FlatEditText.getText().toString());
			else 
				params.put("address_correspondence_info[apartment]", "1");
		} else {
			params.put("address_correspondence", "true");
		}
		
		if (!address_actual_placement) {			
			params.put("address_actual_placement", "false");
			params.put("address_actual_placement_email", reg_EmailAddressEditText.getText().toString());
		} else {
			params.put("address_actual_placement", "true");
		}		
		
		if (getGoldStatus.isChecked()){
			params.put("gold_client", "true");
		} else {
			params.put("gold_client", "false");
		}		
		
		//TODO Unknown Parameters
		params.put("bonus", "true");
		params.put("consent", "true");
		
	}
	
	boolean checkAllFieldsCorrect(){
		boolean result1 = false;
		boolean result2 = false;
		switch(selected_Id1){
		case 1:
			result1 = true;
			break;
		case 2:
			if(reg_CityEditText.getText().toString().length()>0) {
				GradientDrawable back_phone = (GradientDrawable)reg_CityEditText.getBackground();
				back_phone.setStroke(1, Color.WHITE);
			} else {
				GradientDrawable back_phone = (GradientDrawable)reg_CityEditText.getBackground();
				back_phone.setStroke(1, Color.RED);
			}
			if(reg_StreetEditText.getText().toString().length()>0) {
				GradientDrawable back_phone = (GradientDrawable)reg_StreetEditText.getBackground();
				back_phone.setStroke(1, Color.WHITE);
			} else {
				GradientDrawable back_phone = (GradientDrawable)reg_StreetEditText.getBackground();
				back_phone.setStroke(1, Color.RED);
			}
			if(reg_HouseEditText.getText().toString().length()>0) {
				GradientDrawable back_phone = (GradientDrawable)reg_HouseEditText.getBackground();
				back_phone.setStroke(1, Color.WHITE);
			} else {
				GradientDrawable back_phone = (GradientDrawable)reg_HouseEditText.getBackground();
				back_phone.setStroke(1, Color.RED);
			}
			if(reg_IndexEditText.getText().toString().length()>0) {
				GradientDrawable back_phone = (GradientDrawable)reg_IndexEditText.getBackground();
				back_phone.setStroke(1, Color.WHITE);
			} else {
				GradientDrawable back_phone = (GradientDrawable)reg_IndexEditText.getBackground();
				back_phone.setStroke(1, Color.RED);
			}		
			if(reg_CityEditText.getText().toString().length()>0&&
					reg_StreetEditText.getText().toString().length()>0&&
					reg_HouseEditText.getText().toString().length()>0&&
					//reg_FlatEditText.getText().toString().length()>0&&
					reg_IndexEditText.getText().toString().length()>0) {
				result1 = true;
			} //else {if(!reg_yurGetProposal2Layout.isShown()) reg_yurGetProposal2Layout.setVisibility(View.VISIBLE);}
			break;
		}
		switch(selected_Id2){
			case 1:
				result2 = true;
				break;
			case 2:
				if(Utils.isEmailCorrect(reg_EmailAddressEditText.getText().toString())) {
					GradientDrawable back_phone = (GradientDrawable)reg_EmailAddressEditText.getBackground();
					back_phone.setStroke(1, Color.WHITE);
				} else {
					GradientDrawable back_phone = (GradientDrawable)reg_EmailAddressEditText.getBackground();
					back_phone.setStroke(1, Color.RED);
				}
				if (Utils.isEmailCorrect(reg_EmailAddressEditText.getText().toString())){
					result2 = true;
				} //else {if(!reg_yurGetProposal4Layout.isShown()) reg_yurGetProposal4Layout.setVisibility(View.VISIBLE);}
				break;
		}
		
		return result1&&result2;
	}

	@Override
	public void onTaskCompleted(int id_request) {
		// TODO Auto-generated method stub
		
	}

	@SuppressLint("NewApi")
	@Override
	public void onTaskCompletedJSON(String json, int id_request) {
		
		if( loader != null && loader.isShowing() )
			loader.dismiss();
		
		switch(id_request){
		case GlobalConstants.POST_REGISTRATION_YUR:
			
			Log.v("TAG", "METRO POST_REGISTRATION_YUR = " + json);
			try {
				JSONObject yurData = new JSONObject(json);
				if(yurData.has("success")){
					reg_regError.setVisibility(View.GONE);
					
					MeApp.regData.setSuccess(true);
					MeApp.regData.setToken(yurData.optString("token"));
					MeApp.regData.getUser().setCount_basket(yurData.optInt("cart_items_count"));
					
					JSONObject user_data = yurData.getJSONObject("user");
					MeApp.regData.getUser().setId(user_data.optInt("id"));
					MeApp.regData.getUser().setName(user_data.optString("name"));
					MeApp.regData.getUser().setLegal_entity_name(user_data.optString("legal_entity_name"));
					MeApp.regData.getUser().setCard_num(user_data.optString("card_num"));
					MeApp.regData.getUser().setEmail(user_data.optString("email"));
					MeApp.regData.getUser().setPhone(user_data.optString("phone"));
					MeApp.regData.getUser().setLocale(user_data.optString("locale"));
					MeApp.regData.getUser().setShop_id(user_data.optInt("shop_id"));
					MeApp.regData.getUser().setTemp_card(user_data.optBoolean("temp_card"));
					if (!user_data.isNull("temp_card_valid_to")){
						MeApp.regData.getUser().setValid_to(user_data.optString("temp_card_valid_to"));
					}
					if (!user_data.isNull("region")){
						JSONObject regionData = user_data.getJSONObject("region");
						MeApp.regData.getUser().getRegion().setRegionId(regionData.optInt("id"));
						MeApp.regData.getUser().getRegion().setRegionTitle(regionData.optString("title"));
					} else {
						MeApp.regData.getUser().getRegion().setRegionId(-1);
						MeApp.regData.getUser().getRegion().setRegionTitle(null);
					}				
					//TODO Save token
					SharedPreferences sPref = getSharedPreferences(getString(R.string.app_PREFNAME), MODE_PRIVATE);
		        	sPref.edit().putString(getString(R.string.app_TOKEN), MeApp.regData.getToken()).apply();
		        	
		        	//TODO Save card number
		        	sPref.edit().putString(getString(R.string.app_CARD_NUMBER), MeApp.regData.getUser().getCard_num()).apply();
		        	//TODO Save card valid date
		        	if(MeApp.regData.getUser().getValid_to() != null){
		        		sPref.edit().putString(getString(R.string.app_CARD_VALID_TO), 
		        				MeApp.regData.getUser().getValid_to()).apply();
		        	}
		        	
		        	//Save phone number
		        	sPref.edit().putString(getString(R.string.app_SAVED_PHONE), MeApp.regData.getUser().getPhone()).apply();
		        	//Save app_TEMP_JUSTREGISTERED
		        	//sPref.edit().putString(getString(R.string.app_TEMP_JUSTREGISTERED), "Yes").apply();
		        	
		        	//TODO Create Facebook Analytics event
		        	AppEventsLogger logger = AppEventsLogger.newLogger(Out_Register_Yur_3_3.this);
		        	logger.logEvent("new_yur_user_registered");
		        	
		        	//Run Splash
		        	startActivity(new Intent(Out_Register_Yur_3_3.this, Inner_Profile.class));
					finish();
					
				} else if (yurData.has("error")){
					reg_regError.setVisibility(View.VISIBLE);
					int errorCode = yurData.getInt("code");
					switch(errorCode){
					case 902:
						reg_regError.setText(getString(R.string.Error_902));	
						break;
					case 903:
						reg_regError.setText(getString(R.string.Error_903));
						break;
					case 905:
						reg_regError.setText(getString(R.string.Error_905));
						break;
					}
					
						runOnUiThread(new Runnable() {
					        @Override
					        public void run() {
					        	mainScrollView.fullScroll(View.FOCUS_UP);
					        }
					    });
					
				}
				
				
				
				
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			break;
			
		case GlobalConstants.GET_GEO_AREA:
			Log.v("TAG", "METRO GET_GEO_AREA=" + json);
			try {
				JSONArray geoAreaData = new JSONArray(json);
				int geoAreaDataLength = geoAreaData.length();
				regionArrayTitle = new String [geoAreaDataLength];
				regionArrayID = new int[geoAreaDataLength];
				for(int i=0;i<geoAreaDataLength;i++){
					regionArrayTitle[i] = geoAreaData.getJSONObject(i).optString("title");
					regionArrayID[i] = geoAreaData.getJSONObject(i).optInt("id");
				}
				
				ArrayAdapter<String> geoarea_adapter = new ArrayAdapter<String>(this, R.layout.my_spinner_textview, 
						R.id.spinnerItem, regionArrayTitle)
						{
					         public View getView(int position, View convertView, ViewGroup parent) {
					                 View v = super.getView(position, convertView, parent);
					                 TextView text = (TextView)v.findViewById(R.id.spinnerItem);
					                 text.setTypeface(clearSansLight);
					                 text.setTextSize(TypedValue.COMPLEX_UNIT_SP, spiner_textSize);
					                 text.setTextColor(getResources().getColor(R.color.metro_navy));			
					                 return v;
					         }
							
					         public View getDropDownView(int position,  View convertView,  ViewGroup parent) {
					                  View v =super.getDropDownView(position, convertView, parent);	
					                  TextView text = (TextView)v.findViewById(R.id.spinnerItem);
					                  text.setTypeface(clearSansLight);
					                  text.setTextSize(TypedValue.COMPLEX_UNIT_SP, spiner_textSize);
					                  text.setTextColor(getResources().getColor(R.color.metro_navy));
					                 v.setBackgroundColor(getResources().getColor(R.color.metro_gray));			
					                 return v;
					         }
						};
						geoarea_adapter.setDropDownViewResource(R.layout.my_spinner_textview);
						spinnerRegion.setDropDownWidth(android.view.ViewGroup.LayoutParams.MATCH_PARENT);
						spinnerRegion.setAdapter(geoarea_adapter);
						spinnerRegion.setOnItemSelectedListener(new OnItemSelectedListener(){
						@Override
						public void onItemSelected(AdapterView<?> parent, View view,
								int position, long id) {
							spinnerRegionPosition = position;
						}
						@Override
						public void onNothingSelected(AdapterView<?> parent) {
							// TODO Auto-generated method stub			
						}	        	
			        });

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			break;
			
		case GlobalConstants.GET_GEO_STREETTYPE:
			Log.v("TAG", "METRO GET_GEO_STREETTYPE=" + json);
			try {
				JSONArray geoStreetTypeData = new JSONArray(json);
				int geoStreetTypeLength = geoStreetTypeData.length();
				streettypeArrayTitle = new String [geoStreetTypeLength];
				streettypeArrayID = new int[geoStreetTypeLength];
				for(int i=0;i<geoStreetTypeLength;i++){
					streettypeArrayTitle[i] = geoStreetTypeData.getJSONObject(i).optString("title");
					streettypeArrayID[i] = geoStreetTypeData.getJSONObject(i).optInt("id");
				}
				
				ArrayAdapter<String> streettype_adapter = new ArrayAdapter<String>(this, R.layout.my_spinner_textview, 
						R.id.spinnerItem, streettypeArrayTitle)
						{
					         public View getView(int position, View convertView, ViewGroup parent) {
					                 View v = super.getView(position, convertView, parent);
					                 TextView text = (TextView)v.findViewById(R.id.spinnerItem);
					                 text.setTypeface(clearSansLight);
					                 text.setTextSize(TypedValue.COMPLEX_UNIT_SP, spiner_textSize);
					                 text.setTextColor(getResources().getColor(R.color.metro_navy));			
					                 return v;
					         }
							
					         public View getDropDownView(int position,  View convertView,  ViewGroup parent) {
					                  View v =super.getDropDownView(position, convertView, parent);	
					                  TextView text = (TextView)v.findViewById(R.id.spinnerItem);
					                  text.setTypeface(clearSansLight);
					                  text.setTextSize(TypedValue.COMPLEX_UNIT_SP, spiner_textSize);
					                  text.setTextColor(getResources().getColor(R.color.metro_navy));
					                 v.setBackgroundColor(getResources().getColor(R.color.metro_gray));			
					                 return v;
					         }
						};
						streettype_adapter.setDropDownViewResource(R.layout.my_spinner_textview);
						spinnerStreetType.setDropDownWidth(android.view.ViewGroup.LayoutParams.MATCH_PARENT);
						spinnerStreetType.setAdapter(streettype_adapter);
						spinnerStreetType.setOnItemSelectedListener(new OnItemSelectedListener(){
						@Override
						public void onItemSelected(AdapterView<?> parent, View view,
								int position, long id) {
							spinnerStreetTypePosition = position;
						}
						@Override
						public void onNothingSelected(AdapterView<?> parent) {
							// TODO Auto-generated method stub			
						}	        	
			        });
				
				
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			break;
			
			//TODO ============ERRORS DEPLOYMENT==========
			case GlobalConstants.NO_INTERNET_CONNECTION:
				BottomDialog.BottomSelectDialog(Out_Register_Yur_3_3.this, 
	    			noInternetConn, 
	    			getString(R.string.dialog_NoInternetTitle), 
	    			getString(R.string.dialog_NoInternetText), 
	    			getString(R.string.dialog_NoInternetSettings), 
	    			getString(R.string.dialog_NoInternetQuit));
			break;
			
			case GlobalConstants.ERROR_401:
				Log.v("TAG", "METRO OUTStart id_request=ERROR_401");
				SharedPreferences sPref = getSharedPreferences(getString(R.string.app_PREFNAME), MODE_PRIVATE);
	        	sPref.edit().remove(getString(R.string.app_TOKEN)).apply();
	        	startActivity(new Intent(Out_Register_Yur_3_3.this, Out_RegInStart.class)); 
				finish();
				break;
			
			case GlobalConstants.ERROR_503:
				BottomDialog.BottomSelectDialog(Out_Register_Yur_3_3.this, 
						error503, 
		    			getString(R.string.dialog_Server503Title), 
		    			getString(R.string.dialog_Server503Text), 
		    			getString(R.string.dialog_Server503Left), 
		    			getString(R.string.dialog_Server503Right));
			break;
			
			default:
				BottomDialog.BottomSelectDialog(Out_Register_Yur_3_3.this, 
						error503, 
		    			getString(R.string.dialog_ServerUndefinedTitle), 
		    			getString(R.string.dialog_ServerUndefinedText), 
		    			getString(R.string.dialog_ServerUndefinedLeft), 
		    			getString(R.string.dialog_ServerUndefinedRight));
			break;
		}
		
	}
	
	Handler noInternetConn = new Handler() {
	    @Override
	    public void handleMessage(Message msg) {
	        if (msg != null) {
	            // Check for dialog box responses
	            if (msg.what == GlobalConstants.retCodeLeft) {
	            	Intent viewIntent = new Intent(Settings.ACTION_WIFI_SETTINGS);
	            	startActivityForResult(viewIntent, GlobalConstants.REQUEST_CODE_INET);
	            } else if (msg.what == GlobalConstants.retCodeRight) {
	            	Out_Register_Yur_3_3.this.finishAffinity();
	            } else if (msg.what == GlobalConstants.retCodeClose) {
	                
	            }
	        }
	    }
	};
	
	Handler error503 = new Handler() {
	    @Override
	    public void handleMessage(Message msg) {
	        if (msg != null) {
	            // Check for dialog box responses
	            if (msg.what == GlobalConstants.retCodeLeft) {
	            	recreate();
	            } else if (msg.what == GlobalConstants.retCodeRight) {
	            	Out_Register_Yur_3_3.this.finishAffinity();
	            } else if (msg.what == GlobalConstants.retCodeClose) {

	            }
	        }
	    }
	};
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
     super.onActivityResult(requestCode, resultCode, data);
     Log.v("TAG", "METRO ORC0 onActivityResult");
     if(requestCode==GlobalConstants.REQUEST_CODE_INET){	
    	 Log.v("TAG", "METRO ORC0 onActivityResult requestCode==1");
    	 new Handler().postDelayed(new Runnable() {
             public void run() {
              // Actions to do after 1 seconds
            	 recreate();
             }
         }, GlobalConstants.REFRESH_INET_TIME);
     }
    }

	private void setAnalytics() {
		GoogleAnalytics analytics;
		final Tracker tracker;
		analytics = GoogleAnalytics.getInstance(this);
		analytics.setLocalDispatchPeriod(1800);
		tracker = analytics.newTracker(getString(R.string.app_GA_CODE));
		tracker.enableExceptionReporting(true);
		tracker.enableAdvertisingIdCollection(true);
		tracker.enableAutoActivityTracking(true);
		tracker.setScreenName("register_yur_three");
		tracker.send(new HitBuilders.EventBuilder().build());
	}
	
}
