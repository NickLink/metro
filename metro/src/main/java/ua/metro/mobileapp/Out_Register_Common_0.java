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
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import ua.metro.mobileapp.HttpMetods.HTTPAsynkTask;
import ua.metro.mobileapp.UI.BottomDialog;
import ua.metro.mobileapp.UI.OnSingleClickListener;
import ua.metro.mobileapp.application.MeApp;

public class Out_Register_Common_0 extends Activity implements OnTaskCompleted{
	
	Typeface clearSans, clearSansMedium, clearSansBold;
	static Typeface clearSansLight;
	ImageButton iconBackImageButton;
	Button reg_regDali;
	TextView subTitleTextView, reg_regError, reg_regPrizvische, reg_regName, reg_regPobatkovi, reg_regMobPhone,
	reg_regEmail, reg_TcGetCard, reg_allFieldsMust;
	EditText reg_regPrizvischeEditText, reg_regNameEditText, reg_regPobatkoviEditText, reg_regMobPhoneEditText,
	reg_regEmailEditText;
	View subheader_layout;
	Spinner spinnerShop;
	ScrollView mainScrollView;
	String[] shops_title;
	int[] shops_id;
	int spiner_textSize = 14, spinner_position = 0;
	Configuration config;

	Spinner spinnerDay, spinnerMonth, spinnerYear;
	ArrayList<String> dayList, monthList, yearList;
	int daySelected = 0, monthSelected = 0, yearSelected = 0;
	TextView reg_regBirchDate;
	ProgressDialog loader;
	
	@Override
	public void onBackPressed() {
		finish();
	}
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.out_registration_common_0);
        findAllViewsById();
		InitArrays();
		setSpinners();
                        
        clearSans = FontCache.get("fonts/clearsans.ttf", getBaseContext());
        clearSansMedium = FontCache.get("fonts/clearsansmedium.ttf", getBaseContext());
        clearSansBold = FontCache.get("fonts/clearsansbold.ttf", getBaseContext());
        clearSansLight = FontCache.get("fonts/clearsanslight.ttf", getBaseContext());
        
        setAllFontsColors();
        
        //Load Data for Shops
        HTTPAsynkTask mt = new HTTPAsynkTask(Out_Register_Common_0.this,
				GlobalConstants.GET_REG_META, 
				//null, null, 
				null, 
				Out_Register_Common_0.this);
		mt.execute();
		
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
        
        
        iconBackImageButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Back Button
				finish();				
			}	            	
        });
        
        //TODO Continue to register
        reg_regDali.setOnClickListener(new OnSingleClickListener(){
			@Override
			public void onSingleClick(View v) {
				if (checkAllFieldsCorrect()){
					reg_regError.setVisibility(View.GONE);
					putAllDataInFields();

					if(null != isDateExist(daySelected, monthSelected, yearSelected)){
						//Post FIZ OSOBA data to server
						HashMap<String,String> params = new HashMap<String,String>();
						params.put("first_name", MeApp.regFizData.getFirst_name());
						params.put("last_name", MeApp.regFizData.getLast_name());
						params.put("patronymic", MeApp.regFizData.getPatronymic());
						params.put("birthday", isDateExist(daySelected, monthSelected, yearSelected));
						params.put("phone", MeApp.regFizData.getPhone());
						params.put("email", MeApp.regFizData.getEmail());
						params.put("receive_info_email", "true");
						params.put("mall", Integer.toString(MeApp.regFizData.getMall()));
						params.put("bonus", "true");

						loader = new ProgressDialog(Out_Register_Common_0.this);
						loader.setMessage(getString(R.string.app_Loading));
						loader.show();

						HTTPAsynkTask mt = new HTTPAsynkTask(Out_Register_Common_0.this,
								GlobalConstants.POST_REGISTRATION_FIZ,
								//null, null,
								params,
								Out_Register_Common_0.this);
						mt.execute();


					} else {

						Toast.makeText(Out_Register_Common_0.this, getString(R.string.reg_regDateError), Toast.LENGTH_LONG).show();


					}

//				startActivity(new Intent(Out_Register_Common_0.this, Out_Register_Common_1.class));
				} else {
					if (!reg_regError.isShown()){
						reg_regError.setVisibility(View.VISIBLE);
						reg_regError.setText(getString(R.string.reg_regError));
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
        reg_regDali.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
        			reg_regDali.setAlpha((float) 0.55);
                    return false;
                case MotionEvent.ACTION_UP:
                	reg_regDali.setAlpha((float) 1.0);
                    return false;
                default:
                    return false;
                }
            }
      	}); 
        
        setAnalytics();
	}
	
	private void putAllDataInFields() {
		//Put data for FIZ_OSOBA
		MeApp.regFizData.setFirst_name(reg_regPrizvischeEditText.getText().toString());
		MeApp.regFizData.setLast_name(reg_regNameEditText.getText().toString());
		MeApp.regFizData.setPatronymic(reg_regPobatkoviEditText.getText().toString());
		MeApp.regFizData.setPhone(reg_regMobPhoneEditText.getText().toString());
		MeApp.regFizData.setEmail(reg_regEmailEditText.getText().toString());
		MeApp.regFizData.setMall(shops_id[spinner_position]);
		////Put data for YUR_OSOBA
		MeApp.regYurData.setFirst_name(reg_regPrizvischeEditText.getText().toString());
		MeApp.regYurData.setLast_name(reg_regNameEditText.getText().toString());
		MeApp.regYurData.setPatronymic(reg_regPobatkoviEditText.getText().toString());
		MeApp.regYurData.setPhone(reg_regMobPhoneEditText.getText().toString());
		MeApp.regYurData.setEmail(reg_regEmailEditText.getText().toString());
		MeApp.regYurData.setMall(shops_id[spinner_position]);	
	}	
	
	private boolean checkAllFieldsCorrect(){
		r_PrizShow();
		r_NameShow();
		r_PobaShow();
		r_PhonShow();
		r_EmaiShow();
		
		if(reg_regPrizvischeEditText.getText().toString().length()>0&
				reg_regNameEditText.getText().toString().length()>0&
				reg_regPobatkoviEditText.getText().toString().length()>0&
				Utils.phoneCheck(reg_regMobPhoneEditText.getText().toString())&
				Utils.isEmailCorrect(reg_regEmailEditText.getText().toString())) {
			return true;
		} else 
			//TODO FALSE
			return false;		
	}
	void r_PrizShow(){
		if(reg_regPrizvischeEditText.getText().toString().length()>0) {
			GradientDrawable back_phone = (GradientDrawable)reg_regPrizvischeEditText.getBackground();
			back_phone.setStroke(1, Color.WHITE);
		} else {
			GradientDrawable back_phone = (GradientDrawable)reg_regPrizvischeEditText.getBackground();
			back_phone.setStroke(1, Color.RED);
		}
	}
	void r_NameShow(){
		if(reg_regNameEditText.getText().toString().length()>0) {
			GradientDrawable back_phone = (GradientDrawable)reg_regNameEditText.getBackground();
			back_phone.setStroke(1, Color.WHITE);
		} else {
			GradientDrawable back_phone = (GradientDrawable)reg_regNameEditText.getBackground();
			back_phone.setStroke(1, Color.RED);
		}
	}
	void r_PobaShow(){
		if(reg_regPobatkoviEditText.getText().toString().length()>0) {
			GradientDrawable back_phone = (GradientDrawable)reg_regPobatkoviEditText.getBackground();
			back_phone.setStroke(1, Color.WHITE);
		} else {
			GradientDrawable back_phone = (GradientDrawable)reg_regPobatkoviEditText.getBackground();
			back_phone.setStroke(1, Color.RED);
		}
	}
	void r_PhonShow(){
		if(Utils.phoneCheck(reg_regMobPhoneEditText.getText().toString())) {
			GradientDrawable back_phone = (GradientDrawable)reg_regMobPhoneEditText.getBackground();
			back_phone.setStroke(1, Color.WHITE);
		} else {
			GradientDrawable back_phone = (GradientDrawable)reg_regMobPhoneEditText.getBackground();
			back_phone.setStroke(1, Color.RED);
		}
	}
	void r_EmaiShow(){
		if(Utils.isEmailCorrect(reg_regEmailEditText.getText().toString())) {
			GradientDrawable back_phone = (GradientDrawable)reg_regEmailEditText.getBackground();
			back_phone.setStroke(1, Color.WHITE);
		} else {
			GradientDrawable back_phone = (GradientDrawable)reg_regEmailEditText.getBackground();
			back_phone.setStroke(1, Color.RED);
		}
	}

	private void findAllViewsById() {
		// TODO Auto-generated method stub
		mainScrollView = (ScrollView) findViewById(R.id.mainScrollView);
		View subheader_layout = (View) findViewById(R.id.subheader_layout);
		iconBackImageButton = (ImageButton) subheader_layout.findViewById(R.id.iconBackImageButton);
		subTitleTextView = (TextView) subheader_layout.findViewById(R.id.subTitleTextView);
		reg_regError = (TextView) findViewById(R.id.reg_regError);
		reg_regPrizvische = (TextView) findViewById(R.id.reg_regPrizvische);
		reg_regName = (TextView) findViewById(R.id.reg_regName);		
		reg_regPobatkovi = (TextView) findViewById(R.id.reg_regPobatkovi);
		reg_regMobPhone = (TextView) findViewById(R.id.reg_regMobPhone);
		reg_regEmail = (TextView) findViewById(R.id.reg_regEmail);
		reg_TcGetCard = (TextView) findViewById(R.id.reg_TcGetCard);

		reg_regBirchDate = (TextView) findViewById(R.id.reg_regBirchDate);
		
		//EditTexts
		reg_regPrizvischeEditText = (EditText) findViewById(R.id.reg_regPrizvischeEditText);
		reg_regNameEditText = (EditText) findViewById(R.id.reg_regNameEditText);
		reg_regPobatkoviEditText = (EditText) findViewById(R.id.reg_regPobatkoviEditText);
		reg_regMobPhoneEditText = (EditText) findViewById(R.id.reg_regMobPhoneEditText);
		reg_regEmailEditText = (EditText) findViewById(R.id.reg_regEmailEditText);
		
		//Check For Focus
		reg_regPrizvischeEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
	        public void onFocusChange(View v, boolean hasFocus) {
	            if (!hasFocus) {
	            	r_PrizShow();
	            }
	        }
	    });
		reg_regNameEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
	        public void onFocusChange(View v, boolean hasFocus) {
	            if (!hasFocus) {
	            	r_NameShow();
	            }
	        }
	    });
		reg_regPobatkoviEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
	        public void onFocusChange(View v, boolean hasFocus) {
	            if (!hasFocus) {
	            	r_PobaShow();
	            }
	        }
	    });
		reg_regMobPhoneEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
	        public void onFocusChange(View v, boolean hasFocus) {
	            if (!hasFocus) {
	            	r_PhonShow();
	            }
	        }
	    });
		reg_regEmailEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
	        public void onFocusChange(View v, boolean hasFocus) {
	            if (!hasFocus) {
	            	r_EmaiShow();
	            }
	        }
	    });
		
		//Spinner
		spinnerShop = (Spinner) findViewById(R.id.spinnerShop);
		reg_allFieldsMust = (TextView) findViewById(R.id.reg_allFieldsMust);
		reg_regDali = (Button) findViewById(R.id.reg_regDali);
		
		reg_regMobPhoneEditText.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(reg_regMobPhoneEditText.getText().toString() == null
						|| reg_regMobPhoneEditText.getText().toString().equals(""))
					reg_regMobPhoneEditText.setText("380");
				return false;
			}    	
        });
	}
	

	private void setAllFontsColors() {
		// TODO Auto-generated method stub
		subTitleTextView.setTypeface(clearSansMedium);
		reg_regError.setTypeface(clearSansBold);
		reg_regError.setTextColor(getResources().getColor(R.color.metro_red));
		reg_regPrizvische.setTypeface(clearSansBold);
		reg_regName.setTypeface(clearSansBold);
		reg_regPobatkovi.setTypeface(clearSansBold);
		reg_regMobPhone.setTypeface(clearSansBold);
		reg_regEmail.setTypeface(clearSansBold);
		reg_TcGetCard.setTypeface(clearSansBold);
		reg_allFieldsMust.setTypeface(clearSansBold);
		reg_regDali.setTypeface(clearSansMedium);

		reg_regBirchDate.setTypeface(clearSansBold);
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
		// TODO Auto-generated method stub
		switch(id_request){
		case GlobalConstants.GET_REG_META:
			try {
				JSONObject data = new JSONObject(json);
				JSONArray shops = data.getJSONArray("shops");
				int shopsLength = shops.length();
				shops_id = new int[shopsLength];
				shops_title = new String[shopsLength];
				for (int i=0;i<shopsLength;i++){
					shops_id[i] = shops.getJSONObject(i).optInt("id");
					shops_title[i] = shops.getJSONObject(i).optString("title");
				}
				
				ArrayAdapter<String> Shops_adapter = new ArrayAdapter<String>(this, R.layout.my_spinner_textview, 
						R.id.spinnerItem, shops_title)
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
					                 //v.setBackgroundColor(getResources().getColor(R.color.metro_gray));			
					                 return v;
					         }
						};
						Shops_adapter.setDropDownViewResource(R.layout.my_spinner_textview);
						spinnerShop.setDropDownWidth(android.view.ViewGroup.LayoutParams.MATCH_PARENT);
						spinnerShop.setAdapter(Shops_adapter);
						spinnerShop.setOnItemSelectedListener(new OnItemSelectedListener(){
						@Override
						public void onItemSelected(AdapterView<?> parent, View view,
								int position, long id) {
							spinner_position = position;
							
						}
						@Override
						public void onNothingSelected(AdapterView<?> parent) {
							// TODO Auto-generated method stub			
						}	        	
			        });
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				BottomDialog.BottomSelectDialog(Out_Register_Common_0.this, 
						error503, 
		    			getString(R.string.dialog_Server503Title), 
		    			getString(R.string.dialog_Server503Text), 
		    			getString(R.string.dialog_Server503Left), 
		    			getString(R.string.dialog_Server503Right));
			}		
			break;

			case GlobalConstants.POST_REGISTRATION_FIZ:
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
						SharedPreferences sPref = getSharedPreferences(getString(R.string.app_PREFNAME), MODE_PRIVATE);

						//TODO Save token not needed now!
//						sPref.edit().putString(getString(R.string.app_TOKEN), MeApp.regData.getToken()).apply();

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
						AppEventsLogger logger = AppEventsLogger.newLogger(Out_Register_Common_0.this);
						logger.logEvent("new_fiz_user_registered");

						//Run Splash
						startActivity(new Intent(Out_Register_Common_0.this, Out_RegInStart.class).putExtra("page", 1)); //Out_SplashStart
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
			
			//TODO ============ERRORS DEPLOYMENT==========
			case GlobalConstants.NO_INTERNET_CONNECTION:
				BottomDialog.BottomSelectDialog(Out_Register_Common_0.this, 
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
	        	//
	        	startActivity(new Intent(Out_Register_Common_0.this, Out_RegInStart.class)
	        		.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)); 
				finish();
				break;
			
			case GlobalConstants.ERROR_503:
				BottomDialog.BottomSelectDialog(Out_Register_Common_0.this, 
						error503, 
		    			getString(R.string.dialog_Server503Title), 
		    			getString(R.string.dialog_Server503Text), 
		    			getString(R.string.dialog_Server503Left), 
		    			getString(R.string.dialog_Server503Right));
			break;
			
			default:
				BottomDialog.BottomSelectDialog(Out_Register_Common_0.this, 
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
	            	Out_Register_Common_0.this.finishAffinity();
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
	            	Out_Register_Common_0.this.finishAffinity();
	            } else if (msg.what == GlobalConstants.retCodeClose) {

	            }
	        }
	    }
	};
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
     super.onActivityResult(requestCode, resultCode, data);
     if(requestCode==GlobalConstants.REQUEST_CODE_INET){	
    	 new Handler().postDelayed(new Runnable() {
             public void run() {
              // Actions to do after 1 seconds
            	 recreate();
             }
         }, GlobalConstants.REFRESH_INET_TIME);
     }
    }

	void InitArrays(){
		dayList = new ArrayList<String>();
		monthList = new ArrayList<String>();
		yearList = new ArrayList<String>();
		for(int i=1;i<32;i++){
			dayList.add(Integer.toString(i));
		}

		for(int i=1940;i<2005;i++){
			yearList.add(Integer.toString(i));
		}
	}

	String isDateExist(int day, int month, int year){

		String formatString = "dd.MM.yyyy";

		String input = String.format(String.format("%02d", day) + "." +
				"%02d", month) + "." +
				Integer.toString(year);
		try {
			SimpleDateFormat format = new SimpleDateFormat(formatString);
			format.setLenient(false);
			format.parse(input);
		} catch (ParseException e) {
			return null;
		} catch (IllegalArgumentException e) {
			return null;
		}
		return input;
	}

	private void setSpinners(){
		ArrayAdapter<String> dayAdapter = new ArrayAdapter<String>(this,
				R.layout.my_spinner_textview, R.id.spinnerItem, dayList)
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


		ArrayAdapter<String> monthAdapter = new ArrayAdapter<String>(this,
				R.layout.my_spinner_textview, R.id.spinnerItem, getResources().getStringArray(R.array._Month))
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


		ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(this,
				R.layout.my_spinner_textview, R.id.spinnerItem, yearList)
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

		spinnerDay = (Spinner) findViewById(R.id.spinnerDay);
		spinnerMonth = (Spinner) findViewById(R.id.spinnerMonth);
		spinnerYear = (Spinner) findViewById(R.id.spinnerYear);

		spinnerDay.setAdapter(dayAdapter);
		spinnerMonth.setAdapter(monthAdapter);
		spinnerYear.setAdapter(yearAdapter);

		spinnerDay.setVisibility(View.VISIBLE);
		spinnerMonth.setVisibility(View.VISIBLE);
		spinnerYear.setVisibility(View.VISIBLE);

		dayAdapter.setDropDownViewResource(R.layout.my_spinner_textview);
		spinnerDay.setDropDownWidth(android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		spinnerDay.setOnItemSelectedListener(new OnItemSelectedListener(){
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
									   int position, long id) {
				daySelected = position + 1;
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
			}
		});

		monthAdapter.setDropDownViewResource(R.layout.my_spinner_textview);
		spinnerMonth.setDropDownWidth(android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		spinnerMonth.setOnItemSelectedListener(new OnItemSelectedListener(){
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
									   int position, long id) {
				monthSelected = position + 1;
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
			}
		});

		yearAdapter.setDropDownViewResource(R.layout.my_spinner_textview);
		spinnerYear.setDropDownWidth(android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		spinnerYear.setOnItemSelectedListener(new OnItemSelectedListener(){
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
									   int position, long id) {
				yearSelected = 1940 + position + 1;
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
			}
		});
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
		tracker.setScreenName("register_common_one");
		tracker.send(new HitBuilders.EventBuilder().build());
	}

}
