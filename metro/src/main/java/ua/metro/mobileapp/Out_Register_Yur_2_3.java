package ua.metro.mobileapp;

import org.json.JSONArray;
import org.json.JSONException;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import ua.metro.mobileapp.HttpMetods.HTTPAsynkTask;
import ua.metro.mobileapp.UI.BottomDialog;
import ua.metro.mobileapp.UI.OnSingleClickListener;
import ua.metro.mobileapp.application.MeApp;
import ua.metro.mobileapp.datamodel.Cardholders;
import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Out_Register_Yur_2_3 extends Activity implements OnTaskCompleted {
	Typeface clearSans, clearSansMedium, clearSansBold;
	static Typeface clearSansLight;
	ImageButton iconBackImageButton;
	TextView reg_regError;
	ScrollView mainScrollView;
	EditText reg_CityEditText, reg_StreetEditText, reg_HouseEditText, reg_FlatEditText, reg_IndexEditText;
	View header_CardOwner1, header_CardOwner2, header_CardOwner3, header_CardOwner4;
	EditText reg_regPrizvischeEditText1, reg_regNameEditText1, reg_regPobatkoviEditText1, reg_regMobPhoneEditText1, reg_regEmailEditText1;
	EditText reg_regPrizvischeEditText2, reg_regNameEditText2, reg_regPobatkoviEditText2, reg_regMobPhoneEditText2, reg_regEmailEditText2;
	EditText reg_regPrizvischeEditText3, reg_regNameEditText3, reg_regPobatkoviEditText3, reg_regMobPhoneEditText3, reg_regEmailEditText3;
	EditText reg_regPrizvischeEditText4, reg_regNameEditText4, reg_regPobatkoviEditText4, reg_regMobPhoneEditText4, reg_regEmailEditText4;
	Spinner spinnerRegion, spinnerStreetType;
	boolean owner1, owner2, owner3, owner4;
	String [] regionArrayTitle, streettypeArrayTitle;
	int [] regionArrayID, streettypeArrayID;
	int spinnerRegionPosition = 0, spinnerStreetTypePosition = 0, spiner_textSize = 14;
	Configuration config;
	Button reg_regAddCardOwners;
	LinearLayout reg_CardOwner_1LL, reg_CardOwner_2LL, reg_CardOwner_3LL, reg_CardOwner_4LL;
	
	@Override
	public void onBackPressed() {
		finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.out_registration_yur_2_3);
		// Back button
		iconBackImageButton = (ImageButton) findViewById(R.id.iconBackImageButton);
		iconBackImageButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Back Button
				finish();
			}
		});
		
		//Load Data for spinnerRegion & spinnerStreetType
        HTTPAsynkTask mt = new HTTPAsynkTask(Out_Register_Yur_2_3.this,
				GlobalConstants.GET_GEO_AREA, 
				//null, null, 
				null, 
				Out_Register_Yur_2_3.this);
		mt.execute();
		HTTPAsynkTask mt2 = new HTTPAsynkTask(Out_Register_Yur_2_3.this,
				GlobalConstants.GET_GEO_STREETTYPE, 
				//null, null, 
				null, 
				Out_Register_Yur_2_3.this);
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

		clearSans = FontCache.get("fonts/clearsans.ttf", getBaseContext());
		clearSansBold = FontCache.get("fonts/clearsansbold.ttf", getBaseContext());
		clearSansMedium = FontCache.get("fonts/clearsansmedium.ttf", getBaseContext());
		clearSansLight = FontCache.get("fonts/clearsanslight.ttf", getBaseContext());
		
		//Root layout for included layouts
		header_CardOwner1 = (View) findViewById(R.id.header_CardOwner1);
		header_CardOwner2 = (View) findViewById(R.id.header_CardOwner2);
		header_CardOwner3 = (View) findViewById(R.id.header_CardOwner3);
		header_CardOwner4 = (View) findViewById(R.id.header_CardOwner4);
		
		//FindViewsById for main Layout
		mainScrollView = (ScrollView) findViewById(R.id.mainScrollView);
		reg_regError = (TextView) findViewById(R.id.reg_regError);
		TextView textViewRegion = (TextView) findViewById(R.id.textViewRegion);
		TextView textViewCity = (TextView) findViewById(R.id.textViewCity);
		TextView textViewStreetType = (TextView) findViewById(R.id.textViewStreetType);
		TextView textViewStreet = (TextView) findViewById(R.id.textViewStreet);
		TextView textViewHouse = (TextView) findViewById(R.id.textViewHouse);
		TextView textViewFlat = (TextView) findViewById(R.id.textViewFlat);
		TextView textViewIndex = (TextView) findViewById(R.id.textViewIndex);
		TextView reg_allFieldsMust = (TextView) findViewById(R.id.reg_allFieldsMust);
		TextView reg_regTwoThree = (TextView) findViewById(R.id.reg_regTwoThree);
		
		//EditTexts
		reg_CityEditText = (EditText) findViewById(R.id.reg_CityEditText);
		reg_StreetEditText = (EditText) findViewById(R.id.reg_StreetEditText);
		reg_HouseEditText = (EditText) findViewById(R.id.reg_HouseEditText);
		reg_FlatEditText = (EditText) findViewById(R.id.reg_FlatEditText);
		reg_IndexEditText = (EditText) findViewById(R.id.reg_IndexEditText);
		//Check For Focus
		reg_CityEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
	        public void onFocusChange(View v, boolean hasFocus) {
	            if (!hasFocus) {
	            	r_CityShow();
	            }
	        }
	    });
		reg_StreetEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
	        public void onFocusChange(View v, boolean hasFocus) {
	            if (!hasFocus) {
	            	r_StreShow();
	            }
	        }
	    });
		reg_HouseEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
	        public void onFocusChange(View v, boolean hasFocus) {
	            if (!hasFocus) {
	            	r_HousShow();
	            }
	        }
	    });
		reg_IndexEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
	        public void onFocusChange(View v, boolean hasFocus) {
	            if (!hasFocus) {
	            	r_IndeShow();
	            }
	        }
	    });
		
		spinnerRegion = (Spinner) findViewById(R.id.spinnerRegion);
		spinnerStreetType = (Spinner) findViewById(R.id.spinnerStreetType);
		
		//Set fonts for main layout
		textViewRegion.setTypeface(clearSansBold);
		textViewCity.setTypeface(clearSansBold);
		textViewStreetType.setTypeface(clearSansBold);
		textViewStreet.setTypeface(clearSansBold);
		textViewHouse.setTypeface(clearSansBold);
		textViewFlat.setTypeface(clearSansBold);
		textViewIndex.setTypeface(clearSansBold);
		reg_allFieldsMust.setTypeface(clearSansBold);
		reg_regTwoThree.setTypeface(clearSansBold);			
		
		reg_regAddCardOwners = (Button) findViewById(R.id.reg_regAddCardOwners);
		reg_regAddCardOwners.setOnClickListener(new OnSingleClickListener() {
			@Override
			public void onSingleClick(View v) {
				boolean pressed = true;
				
				if(!header_CardOwner1.isShown()){
					setOwner1();
					owner1 = true;
					pressed = false;
				}
				
				if(pressed&!header_CardOwner2.isShown()){
					pressed = false;
					if (checkOwner1FieldsCorrect()) {
						setOwner2();
						owner2 = true;		
					} else {
						Toast.makeText(Out_Register_Yur_2_3.this, 
								getString(R.string.reg_CardOwner1_notComplete), Toast.LENGTH_SHORT).show();
					}
				}
				
				if(pressed&!header_CardOwner3.isShown()){
					pressed = false;
					if (checkOwner2FieldsCorrect()) {
						setOwner3();
						owner3 = true;
					} else {
						Toast.makeText(Out_Register_Yur_2_3.this, 
								getString(R.string.reg_CardOwner2_notComplete), Toast.LENGTH_SHORT).show();
					}
				}
				
				if(pressed&!header_CardOwner4.isShown()){
					pressed = false;
					if (checkOwner3FieldsCorrect()) {
						setOwner4();
						owner4 = true;
						reg_regAddCardOwners.setEnabled(false);
						GradientDrawable back_phone = (GradientDrawable)reg_regAddCardOwners.getBackground();
						back_phone.setColor(getResources().getColor(R.color.metro_light_gray));						
					} else {
						Toast.makeText(Out_Register_Yur_2_3.this, 
								getString(R.string.reg_CardOwner3_notComplete), Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
		reg_regAddCardOwners.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                	reg_regAddCardOwners.setAlpha((float) 0.55);
                    return false;
                case MotionEvent.ACTION_UP:
                	reg_regAddCardOwners.setAlpha((float) 1.0);
                    return false;
                default:
                    return false;
                }
            }
      	}); 
		
		final Button reg_regCONTINUE = (Button) findViewById(R.id.reg_regCONTINUE);
		reg_regCONTINUE.setTypeface(clearSansMedium);
		reg_regCONTINUE.setOnClickListener(new OnSingleClickListener() {
			@Override
			public void onSingleClick(View v) {
				//TODO CHECKS for continue button 
				if(checkMainFieldsCorrect()&&checkExtraOwnerCorrect()){
					reg_regError.setVisibility(View.GONE);
					//TODO Put data in YURDATA fields
					putAllDataInFields();
					startActivity(new Intent(Out_Register_Yur_2_3.this,
							Out_Register_Yur_3_3.class));
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
		MeApp.regYurData.setArea_id(regionArrayID[spinnerRegionPosition]);
		MeApp.regYurData.setCity(reg_CityEditText.getText().toString());
		MeApp.regYurData.setStreet_type_id(streettypeArrayID[spinnerStreetTypePosition]);
		MeApp.regYurData.setStreet(reg_StreetEditText.getText().toString());
		MeApp.regYurData.setHouse(reg_HouseEditText.getText().toString());
		
		if(reg_FlatEditText.getText().toString() != null)
			MeApp.regYurData.setApartment(reg_FlatEditText.getText().toString());
		else
			MeApp.regYurData.setApartment("1");
		
		MeApp.regYurData.setZip_code(reg_IndexEditText.getText().toString());		
		if (owner1){
			Cardholders o1 = new Cardholders();
			o1.setFirst_name(reg_regPrizvischeEditText1.getText().toString());
			o1.setLast_name(reg_regNameEditText1.getText().toString());
			o1.setPatronymic(reg_regPobatkoviEditText1.getText().toString());
			o1.setPhone(reg_regMobPhoneEditText1.getText().toString());
			o1.setEmail(reg_regEmailEditText1.getText().toString());
			MeApp.regYurData.getCardholders().add(o1);
		}
		if (owner2){
			Cardholders o2 = new Cardholders();
			o2.setFirst_name(reg_regPrizvischeEditText2.getText().toString());
			o2.setLast_name(reg_regNameEditText2.getText().toString());
			o2.setPatronymic(reg_regPobatkoviEditText2.getText().toString());
			o2.setPhone(reg_regMobPhoneEditText2.getText().toString());
			o2.setEmail(reg_regEmailEditText2.getText().toString());
			MeApp.regYurData.getCardholders().add(o2);
		}
		if (owner3){
			Cardholders o3 = new Cardholders();
			o3.setFirst_name(reg_regPrizvischeEditText3.getText().toString());
			o3.setLast_name(reg_regNameEditText3.getText().toString());
			o3.setPatronymic(reg_regPobatkoviEditText3.getText().toString());
			o3.setPhone(reg_regMobPhoneEditText3.getText().toString());
			o3.setEmail(reg_regEmailEditText3.getText().toString());
			MeApp.regYurData.getCardholders().add(o3);
		}
		if (owner4){
			Cardholders o4 = new Cardholders();
			o4.setFirst_name(reg_regPrizvischeEditText4.getText().toString());
			o4.setLast_name(reg_regNameEditText4.getText().toString());
			o4.setPatronymic(reg_regPobatkoviEditText4.getText().toString());
			o4.setPhone(reg_regMobPhoneEditText4.getText().toString());
			o4.setEmail(reg_regEmailEditText4.getText().toString());
			MeApp.regYurData.getCardholders().add(o4);
		}
		
		
	}

	void setOwner1(){
		header_CardOwner1.setVisibility(View.VISIBLE);
		
		//Views by ID for TextViews
		final LinearLayout reg_yurCardOpenLL1 = (LinearLayout) header_CardOwner1.findViewById(R.id.reg_yurCardOpenLL1);		
		reg_CardOwner_1LL = (LinearLayout) header_CardOwner1.findViewById(R.id.reg_CardOwner_1LL);	
		final TextView reg_yurCardOpen1 = (TextView) header_CardOwner1.findViewById(R.id.reg_yurCardOpen1);
		final TextView reg_yurCardOwner1 = (TextView) header_CardOwner1.findViewById(R.id.reg_yurCardOwner1);
		final TextView reg_yurCardOwnerDelete1 = (TextView) header_CardOwner1.findViewById(R.id.reg_yurCardOwnerDelete1);
		
		TextView reg_iRegisterAs1  = (TextView) header_CardOwner1.findViewById(R.id.reg_iRegisterAs1);		
		TextView reg_regName1  = (TextView) header_CardOwner1.findViewById(R.id.reg_regName1);
		TextView reg_regPobatkovi1  = (TextView) header_CardOwner1.findViewById(R.id.reg_regPobatkovi1);
		TextView reg_regMobPhone1  = (TextView) header_CardOwner1.findViewById(R.id.reg_regMobPhone1);	
		TextView reg_regEmail1  = (TextView) header_CardOwner1.findViewById(R.id.reg_regEmail1);
		//Set Fonts for TextViews
		reg_yurCardOpen1.setTypeface(clearSansMedium);
		reg_yurCardOwner1.setTypeface(clearSansMedium);
		reg_yurCardOwnerDelete1.setTypeface(clearSansBold);
		reg_yurCardOwnerDelete1.setTextColor(getResources().getColor(R.color.metro_red));
		
		reg_iRegisterAs1.setTypeface(clearSansBold);
		reg_regName1.setTypeface(clearSansBold);
		reg_regPobatkovi1.setTypeface(clearSansBold);
		reg_regMobPhone1.setTypeface(clearSansBold);
		reg_regEmail1.setTypeface(clearSansBold);
		
		
		//EditText parts
		reg_regPrizvischeEditText1 = (EditText) header_CardOwner1.findViewById(R.id.reg_regPrizvischeEditText1);
		reg_regNameEditText1 = (EditText) header_CardOwner1.findViewById(R.id.reg_regNameEditText1);
		reg_regPobatkoviEditText1 = (EditText) header_CardOwner1.findViewById(R.id.reg_regPobatkoviEditText1);
		reg_regMobPhoneEditText1 = (EditText) header_CardOwner1.findViewById(R.id.reg_regMobPhoneEditText1);
		reg_regEmailEditText1 = (EditText) header_CardOwner1.findViewById(R.id.reg_regEmailEditText1);

		reg_regMobPhoneEditText1.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(reg_regMobPhoneEditText1.getText().toString() == null
						|| reg_regMobPhoneEditText1.getText().toString().equals(""))
					reg_regMobPhoneEditText1.setText("380");
				return false;
			}    	
        });
		
		reg_yurCardOpenLL1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(reg_CardOwner_1LL.isShown()) {
					reg_CardOwner_1LL.setVisibility(View.GONE);
					reg_yurCardOpen1.setText(getString(R.string.reg_yurCardOpen));
				} else {
					reg_CardOwner_1LL.setVisibility(View.VISIBLE);
					reg_yurCardOpen1.setText(getString(R.string.reg_yurCardClose));
				}
			}
		});
		reg_yurCardOwnerDelete1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!owner2) {
					header_CardOwner1.setVisibility(View.GONE);
					owner1 = false;
				}
			}
		});
	}
	
	void setOwner2(){
		header_CardOwner2.setVisibility(View.VISIBLE);
		final LinearLayout reg_yurCardOpenLL2 = (LinearLayout) header_CardOwner2.findViewById(R.id.reg_yurCardOpenLL2);		
		reg_CardOwner_2LL = (LinearLayout) header_CardOwner2.findViewById(R.id.reg_CardOwner_2LL);	
		final TextView reg_yurCardOpen2 = (TextView) header_CardOwner2.findViewById(R.id.reg_yurCardOpen2);
		final TextView reg_yurCardOwner2 = (TextView) header_CardOwner2.findViewById(R.id.reg_yurCardOwner2);
		final TextView reg_yurCardOwnerDelete2 = (TextView) header_CardOwner2.findViewById(R.id.reg_yurCardOwnerDelete2);
		
		TextView reg_iRegisterAs2  = (TextView) header_CardOwner2.findViewById(R.id.reg_iRegisterAs2);		
		TextView reg_regName2  = (TextView) header_CardOwner2.findViewById(R.id.reg_regName2);
		TextView reg_regPobatkovi2  = (TextView) header_CardOwner2.findViewById(R.id.reg_regPobatkovi2);
		TextView reg_regMobPhone2  = (TextView) header_CardOwner2.findViewById(R.id.reg_regMobPhone2);	
		TextView reg_regEmail2  = (TextView) header_CardOwner2.findViewById(R.id.reg_regEmail2);
		//Set Fonts for TextViews
		reg_yurCardOpen2.setTypeface(clearSansMedium);
		reg_yurCardOwner2.setTypeface(clearSansMedium);
		reg_yurCardOwnerDelete2.setTypeface(clearSansBold);
		reg_yurCardOwnerDelete2.setTextColor(getResources().getColor(R.color.metro_red));
		
		reg_iRegisterAs2.setTypeface(clearSansBold);
		reg_regName2.setTypeface(clearSansBold);
		reg_regPobatkovi2.setTypeface(clearSansBold);
		reg_regMobPhone2.setTypeface(clearSansBold);
		reg_regEmail2.setTypeface(clearSansBold);
		
		//EditText parts
		reg_regPrizvischeEditText2 = (EditText) header_CardOwner2.findViewById(R.id.reg_regPrizvischeEditText2);
		reg_regNameEditText2 = (EditText) header_CardOwner2.findViewById(R.id.reg_regNameEditText2);
		reg_regPobatkoviEditText2 = (EditText) header_CardOwner2.findViewById(R.id.reg_regPobatkoviEditText2);
		reg_regMobPhoneEditText2 = (EditText) header_CardOwner2.findViewById(R.id.reg_regMobPhoneEditText2);
		reg_regEmailEditText2 = (EditText) header_CardOwner2.findViewById(R.id.reg_regEmailEditText2);
		
		reg_regMobPhoneEditText2.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(reg_regMobPhoneEditText2.getText().toString() == null
						|| reg_regMobPhoneEditText2.getText().toString().equals(""))
					reg_regMobPhoneEditText2.setText("380");
				return false;
			}    	
        });
		
		reg_yurCardOpenLL2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(reg_CardOwner_2LL.isShown()) {
					reg_CardOwner_2LL.setVisibility(View.GONE);
					reg_yurCardOpen2.setText(getString(R.string.reg_yurCardOpen));
				} else {
					reg_CardOwner_2LL.setVisibility(View.VISIBLE);
					reg_yurCardOpen2.setText(getString(R.string.reg_yurCardClose));
				}
			}
		});
		reg_yurCardOwnerDelete2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!owner3) {
					header_CardOwner2.setVisibility(View.GONE);
					owner2 = false;
				}
			}
		});
	}
	
	void setOwner3(){
		header_CardOwner3.setVisibility(View.VISIBLE);
		
		final LinearLayout reg_yurCardOpenLL3 = (LinearLayout) header_CardOwner3.findViewById(R.id.reg_yurCardOpenLL3);		
		reg_CardOwner_3LL = (LinearLayout) header_CardOwner3.findViewById(R.id.reg_CardOwner_3LL);	
		final TextView reg_yurCardOpen3 = (TextView) header_CardOwner3.findViewById(R.id.reg_yurCardOpen3);
		final TextView reg_yurCardOwner3 = (TextView) header_CardOwner3.findViewById(R.id.reg_yurCardOwner3);
		final TextView reg_yurCardOwnerDelete3 = (TextView) header_CardOwner3.findViewById(R.id.reg_yurCardOwnerDelete3);
		
		TextView reg_iRegisterAs3  = (TextView) header_CardOwner3.findViewById(R.id.reg_iRegisterAs3);		
		TextView reg_regName3  = (TextView) header_CardOwner3.findViewById(R.id.reg_regName3);
		TextView reg_regPobatkovi3  = (TextView) header_CardOwner3.findViewById(R.id.reg_regPobatkovi3);
		TextView reg_regMobPhone3  = (TextView) header_CardOwner3.findViewById(R.id.reg_regMobPhone3);	
		TextView reg_regEmail3  = (TextView) header_CardOwner3.findViewById(R.id.reg_regEmail3);
		
		//Set Fonts for TextViews
		reg_yurCardOpen3.setTypeface(clearSansMedium);
		reg_yurCardOwner3.setTypeface(clearSansMedium);
		reg_yurCardOwnerDelete3.setTypeface(clearSansBold);
		reg_yurCardOwnerDelete3.setTextColor(getResources().getColor(R.color.metro_red));
		
		reg_iRegisterAs3.setTypeface(clearSansBold);
		reg_regName3.setTypeface(clearSansBold);
		reg_regPobatkovi3.setTypeface(clearSansBold);
		reg_regMobPhone3.setTypeface(clearSansBold);
		reg_regEmail3.setTypeface(clearSansBold);
		
		//EditText parts
		reg_regPrizvischeEditText3 = (EditText) header_CardOwner3.findViewById(R.id.reg_regPrizvischeEditText3);
		reg_regNameEditText3 = (EditText) header_CardOwner3.findViewById(R.id.reg_regNameEditText3);
		reg_regPobatkoviEditText3 = (EditText) header_CardOwner3.findViewById(R.id.reg_regPobatkoviEditText3);
		reg_regMobPhoneEditText3 = (EditText) header_CardOwner3.findViewById(R.id.reg_regMobPhoneEditText3);
		reg_regEmailEditText3 = (EditText) header_CardOwner3.findViewById(R.id.reg_regEmailEditText3);
		
		reg_regMobPhoneEditText3.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(reg_regMobPhoneEditText3.getText().toString() == null
						|| reg_regMobPhoneEditText3.getText().toString().equals(""))
					reg_regMobPhoneEditText3.setText("380");
				return false;
			}    	
        });

		reg_yurCardOpenLL3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(reg_CardOwner_3LL.isShown()) {
					reg_CardOwner_3LL.setVisibility(View.GONE);
					reg_yurCardOpen3.setText(getString(R.string.reg_yurCardOpen));
				} else {
					reg_CardOwner_3LL.setVisibility(View.VISIBLE);
					reg_yurCardOpen3.setText(getString(R.string.reg_yurCardClose));
				}
			}
		});
		reg_yurCardOwnerDelete3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!owner4) {
					header_CardOwner3.setVisibility(View.GONE);
					owner3 = false;
				}
			}
		});
		
		
	}
	
	void setOwner4(){
		header_CardOwner4.setVisibility(View.VISIBLE);
		
		final LinearLayout reg_yurCardOpenLL4 = (LinearLayout) header_CardOwner4.findViewById(R.id.reg_yurCardOpenLL4);		
		reg_CardOwner_4LL = (LinearLayout) header_CardOwner4.findViewById(R.id.reg_CardOwner_4LL);	
		final TextView reg_yurCardOpen4 = (TextView) header_CardOwner4.findViewById(R.id.reg_yurCardOpen4);
		final TextView reg_yurCardOwner4 = (TextView) header_CardOwner4.findViewById(R.id.reg_yurCardOwner4);
		final TextView reg_yurCardOwnerDelete4 = (TextView) header_CardOwner4.findViewById(R.id.reg_yurCardOwnerDelete4);
		
		TextView reg_iRegisterAs4  = (TextView) header_CardOwner4.findViewById(R.id.reg_iRegisterAs4);		
		TextView reg_regName4  = (TextView) header_CardOwner4.findViewById(R.id.reg_regName4);
		TextView reg_regPobatkovi4  = (TextView) header_CardOwner4.findViewById(R.id.reg_regPobatkovi4);
		TextView reg_regMobPhone4  = (TextView) header_CardOwner4.findViewById(R.id.reg_regMobPhone4);	
		TextView reg_regEmail4  = (TextView) header_CardOwner4.findViewById(R.id.reg_regEmail4);
		
		//Set Fonts for TextViews
		reg_yurCardOpen4.setTypeface(clearSansMedium);
		reg_yurCardOwner4.setTypeface(clearSansMedium);
		reg_yurCardOwnerDelete4.setTypeface(clearSansBold);
		reg_yurCardOwnerDelete4.setTextColor(getResources().getColor(R.color.metro_red));
		
		reg_iRegisterAs4.setTypeface(clearSansBold);
		reg_regName4.setTypeface(clearSansBold);
		reg_regPobatkovi4.setTypeface(clearSansBold);
		reg_regMobPhone4.setTypeface(clearSansBold);
		reg_regEmail4.setTypeface(clearSansBold);
		
		//EditText parts
		reg_regPrizvischeEditText4 = (EditText) header_CardOwner4.findViewById(R.id.reg_regPrizvischeEditText4);
		reg_regNameEditText4 = (EditText) header_CardOwner4.findViewById(R.id.reg_regNameEditText4);
		reg_regPobatkoviEditText4 = (EditText) header_CardOwner4.findViewById(R.id.reg_regPobatkoviEditText4);
		reg_regMobPhoneEditText4 = (EditText) header_CardOwner4.findViewById(R.id.reg_regMobPhoneEditText4);
		reg_regEmailEditText4 = (EditText) header_CardOwner4.findViewById(R.id.reg_regEmailEditText4);
		
		reg_regMobPhoneEditText4.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(reg_regMobPhoneEditText4.getText().toString() == null
						|| reg_regMobPhoneEditText4.getText().toString().equals(""))
					reg_regMobPhoneEditText4.setText("380");
				return false;
			}    	
        });

		reg_yurCardOpenLL4.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(reg_CardOwner_4LL.isShown()) {
					reg_CardOwner_4LL.setVisibility(View.GONE);
					reg_yurCardOpen4.setText(getString(R.string.reg_yurCardOpen));
				} else {
					reg_CardOwner_4LL.setVisibility(View.VISIBLE);
					reg_yurCardOpen4.setText(getString(R.string.reg_yurCardClose));
				}
			}
		});
		reg_yurCardOwnerDelete4.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				header_CardOwner4.setVisibility(View.GONE);
				owner4 = false;
				reg_regAddCardOwners.setEnabled(true);
				GradientDrawable back_phone = (GradientDrawable)reg_regAddCardOwners.getBackground();
				back_phone.setColor(getResources().getColor(R.color.metro_yellow));
				
			}
		});
	}
	
	private boolean checkMainFieldsCorrect(){
		r_CityShow();
		r_StreShow();
		r_HousShow();
		r_IndeShow();
		
		
		if(reg_CityEditText.getText().toString().length()>0&&
				reg_StreetEditText.getText().toString().length()>0&&
				reg_HouseEditText.getText().toString().length()>0&&
				//reg_FlatEditText.getText().toString().length()>0&&
				(reg_IndexEditText.getText().toString().length()>3 &
				reg_IndexEditText.getText().toString().length()<6)) {
			return true;
		} else
			return false;		
	}
	
	void r_CityShow(){
		if(reg_CityEditText.getText().toString().length()>0) {
			GradientDrawable back_phone = (GradientDrawable)reg_CityEditText.getBackground();
			back_phone.setStroke(1, Color.WHITE);
		} else {
			GradientDrawable back_phone = (GradientDrawable)reg_CityEditText.getBackground();
			back_phone.setStroke(1, Color.RED);
		}
	}
	void r_StreShow(){
		if(reg_StreetEditText.getText().toString().length()>0) {
			GradientDrawable back_phone = (GradientDrawable)reg_StreetEditText.getBackground();
			back_phone.setStroke(1, Color.WHITE);
		} else {
			GradientDrawable back_phone = (GradientDrawable)reg_StreetEditText.getBackground();
			back_phone.setStroke(1, Color.RED);
		}
	}
	void r_HousShow(){
		if(reg_HouseEditText.getText().toString().length()>0) {
			GradientDrawable back_phone = (GradientDrawable)reg_HouseEditText.getBackground();
			back_phone.setStroke(1, Color.WHITE);
		} else {
			GradientDrawable back_phone = (GradientDrawable)reg_HouseEditText.getBackground();
			back_phone.setStroke(1, Color.RED);
		}
	}
	void r_IndeShow(){
		if(reg_IndexEditText.getText().toString().length()>3 &
				reg_IndexEditText.getText().toString().length()<6){
				GradientDrawable back_phone = (GradientDrawable)reg_IndexEditText.getBackground();
				back_phone.setStroke(1, Color.WHITE);
			} else {
				GradientDrawable back_phone = (GradientDrawable)reg_IndexEditText.getBackground();
				back_phone.setStroke(1, Color.RED);
			}
	}
	
	private boolean checkExtraOwnerCorrect(){
		//TODO Check for extra owner data correctness
		if(checkOwner1FieldsCorrect() && checkOwner2FieldsCorrect() &&
				checkOwner3FieldsCorrect() && checkOwner4FieldsCorrect()){						
			return true;
		} else return false;
	}
	
	private boolean checkOwner1FieldsCorrect(){
		if (owner1){
			if(reg_regPrizvischeEditText1.getText().toString().length()>0) {
				GradientDrawable back_phone = (GradientDrawable)reg_regPrizvischeEditText1.getBackground();
				back_phone.setStroke(1, Color.WHITE);
			} else {
				GradientDrawable back_phone = (GradientDrawable)reg_regPrizvischeEditText1.getBackground();
				back_phone.setStroke(1, Color.RED);
			}
			if(reg_regNameEditText1.getText().toString().length()>0) {
				GradientDrawable back_phone = (GradientDrawable)reg_regNameEditText1.getBackground();
				back_phone.setStroke(1, Color.WHITE);
			} else {
				GradientDrawable back_phone = (GradientDrawable)reg_regNameEditText1.getBackground();
				back_phone.setStroke(1, Color.RED);
			}
			if(reg_regPobatkoviEditText1.getText().toString().length()>0) {
				GradientDrawable back_phone = (GradientDrawable)reg_regPobatkoviEditText1.getBackground();
				back_phone.setStroke(1, Color.WHITE);
			} else {
				GradientDrawable back_phone = (GradientDrawable)reg_regPobatkoviEditText1.getBackground();
				back_phone.setStroke(1, Color.RED);
			}
			if(Utils.phoneCheck(reg_regMobPhoneEditText1.getText().toString())) {
				GradientDrawable back_phone = (GradientDrawable)reg_regMobPhoneEditText1.getBackground();
				back_phone.setStroke(1, Color.WHITE);
			} else {
				GradientDrawable back_phone = (GradientDrawable)reg_regMobPhoneEditText1.getBackground();
				back_phone.setStroke(1, Color.RED);
			}
			if(Utils.isEmailCorrect(reg_regEmailEditText1.getText().toString())) {
				GradientDrawable back_phone = (GradientDrawable)reg_regEmailEditText1.getBackground();
				back_phone.setStroke(1, Color.WHITE);
			} else {
				GradientDrawable back_phone = (GradientDrawable)reg_regEmailEditText1.getBackground();
				back_phone.setStroke(1, Color.RED);
			}
			if(reg_regPrizvischeEditText1.getText().toString().length()>0&
					reg_regNameEditText1.getText().toString().length()>0&
					reg_regPobatkoviEditText1.getText().toString().length()>0&
					Utils.phoneCheck(reg_regMobPhoneEditText1.getText().toString())&
					Utils.isEmailCorrect(reg_regEmailEditText1.getText().toString())) {
				return true;
			} else {
				if(!reg_CardOwner_1LL.isShown())reg_CardOwner_1LL.setVisibility(View.VISIBLE);
				return false;		
			}
		} else return true;
	}
	
	private boolean checkOwner2FieldsCorrect(){
		if (owner2){
			if(reg_regPrizvischeEditText2.getText().toString().length()>0) {
				GradientDrawable back_phone = (GradientDrawable)reg_regPrizvischeEditText2.getBackground();
				back_phone.setStroke(1, Color.WHITE);
			} else {
				GradientDrawable back_phone = (GradientDrawable)reg_regPrizvischeEditText2.getBackground();
				back_phone.setStroke(1, Color.RED);
			}
			if(reg_regNameEditText2.getText().toString().length()>0) {
				GradientDrawable back_phone = (GradientDrawable)reg_regNameEditText2.getBackground();
				back_phone.setStroke(1, Color.WHITE);
			} else {
				GradientDrawable back_phone = (GradientDrawable)reg_regNameEditText2.getBackground();
				back_phone.setStroke(1, Color.RED);
			}
			if(reg_regPobatkoviEditText2.getText().toString().length()>0) {
				GradientDrawable back_phone = (GradientDrawable)reg_regPobatkoviEditText2.getBackground();
				back_phone.setStroke(1, Color.WHITE);
			} else {
				GradientDrawable back_phone = (GradientDrawable)reg_regPobatkoviEditText2.getBackground();
				back_phone.setStroke(1, Color.RED);
			}
			if(Utils.phoneCheck(reg_regMobPhoneEditText2.getText().toString())) {
				GradientDrawable back_phone = (GradientDrawable)reg_regMobPhoneEditText2.getBackground();
				back_phone.setStroke(1, Color.WHITE);
			} else {
				GradientDrawable back_phone = (GradientDrawable)reg_regMobPhoneEditText2.getBackground();
				back_phone.setStroke(1, Color.RED);
			}
			if(Utils.isEmailCorrect(reg_regEmailEditText2.getText().toString())) {
				GradientDrawable back_phone = (GradientDrawable)reg_regEmailEditText2.getBackground();
				back_phone.setStroke(1, Color.WHITE);
			} else {
				GradientDrawable back_phone = (GradientDrawable)reg_regEmailEditText2.getBackground();
				back_phone.setStroke(1, Color.RED);
			}
			if(reg_regPrizvischeEditText2.getText().toString().length()>0&
					reg_regNameEditText2.getText().toString().length()>0&
					reg_regPobatkoviEditText2.getText().toString().length()>0&
					Utils.phoneCheck(reg_regMobPhoneEditText2.getText().toString())&
					Utils.isEmailCorrect(reg_regEmailEditText2.getText().toString())) {
				return true;
			} else {
				if(!reg_CardOwner_2LL.isShown())reg_CardOwner_2LL.setVisibility(View.VISIBLE);
				return false;		
			}	
		} else return true;	
	}
	
	private boolean checkOwner3FieldsCorrect(){
		if (owner3){
			if(reg_regPrizvischeEditText3.getText().toString().length()>0) {
				GradientDrawable back_phone = (GradientDrawable)reg_regPrizvischeEditText3.getBackground();
				back_phone.setStroke(1, Color.WHITE);
			} else {
				GradientDrawable back_phone = (GradientDrawable)reg_regPrizvischeEditText3.getBackground();
				back_phone.setStroke(1, Color.RED);
			}
			if(reg_regNameEditText3.getText().toString().length()>0) {
				GradientDrawable back_phone = (GradientDrawable)reg_regNameEditText3.getBackground();
				back_phone.setStroke(1, Color.WHITE);
			} else {
				GradientDrawable back_phone = (GradientDrawable)reg_regNameEditText3.getBackground();
				back_phone.setStroke(1, Color.RED);
			}
			if(reg_regPobatkoviEditText3.getText().toString().length()>0) {
				GradientDrawable back_phone = (GradientDrawable)reg_regPobatkoviEditText3.getBackground();
				back_phone.setStroke(1, Color.WHITE);
			} else {
				GradientDrawable back_phone = (GradientDrawable)reg_regPobatkoviEditText3.getBackground();
				back_phone.setStroke(1, Color.RED);
			}
			if(Utils.phoneCheck(reg_regMobPhoneEditText3.getText().toString())) {
				GradientDrawable back_phone = (GradientDrawable)reg_regMobPhoneEditText3.getBackground();
				back_phone.setStroke(1, Color.WHITE);
			} else {
				GradientDrawable back_phone = (GradientDrawable)reg_regMobPhoneEditText3.getBackground();
				back_phone.setStroke(1, Color.RED);
			}
			if(Utils.isEmailCorrect(reg_regEmailEditText3.getText().toString())) {
				GradientDrawable back_phone = (GradientDrawable)reg_regEmailEditText3.getBackground();
				back_phone.setStroke(1, Color.WHITE);
			} else {
				GradientDrawable back_phone = (GradientDrawable)reg_regEmailEditText3.getBackground();
				back_phone.setStroke(1, Color.RED);
			}
			if(reg_regPrizvischeEditText3.getText().toString().length()>0&
					reg_regNameEditText3.getText().toString().length()>0&
					reg_regPobatkoviEditText3.getText().toString().length()>0&
					Utils.phoneCheck(reg_regMobPhoneEditText3.getText().toString())&
					Utils.isEmailCorrect(reg_regEmailEditText3.getText().toString())) {
				return true;
			} else {
				if(!reg_CardOwner_3LL.isShown())reg_CardOwner_3LL.setVisibility(View.VISIBLE);
				return false;		
			}
		} else return true;
	}
	
	private boolean checkOwner4FieldsCorrect(){
		if (owner4){
			if(reg_regPrizvischeEditText4.getText().toString().length()>0) {
				GradientDrawable back_phone = (GradientDrawable)reg_regPrizvischeEditText4.getBackground();
				back_phone.setStroke(1, Color.WHITE);
			} else {
				GradientDrawable back_phone = (GradientDrawable)reg_regPrizvischeEditText4.getBackground();
				back_phone.setStroke(1, Color.RED);
			}
			if(reg_regNameEditText4.getText().toString().length()>0) {
				GradientDrawable back_phone = (GradientDrawable)reg_regNameEditText4.getBackground();
				back_phone.setStroke(1, Color.WHITE);
			} else {
				GradientDrawable back_phone = (GradientDrawable)reg_regNameEditText4.getBackground();
				back_phone.setStroke(1, Color.RED);
			}
			if(reg_regPobatkoviEditText4.getText().toString().length()>0) {
				GradientDrawable back_phone = (GradientDrawable)reg_regPobatkoviEditText4.getBackground();
				back_phone.setStroke(1, Color.WHITE);
			} else {
				GradientDrawable back_phone = (GradientDrawable)reg_regPobatkoviEditText4.getBackground();
				back_phone.setStroke(1, Color.RED);
			}
			if(Utils.phoneCheck(reg_regMobPhoneEditText4.getText().toString())) {
				GradientDrawable back_phone = (GradientDrawable)reg_regMobPhoneEditText4.getBackground();
				back_phone.setStroke(1, Color.WHITE);
			} else {
				GradientDrawable back_phone = (GradientDrawable)reg_regMobPhoneEditText4.getBackground();
				back_phone.setStroke(1, Color.RED);
			}
			if(Utils.isEmailCorrect(reg_regEmailEditText4.getText().toString())) {
				GradientDrawable back_phone = (GradientDrawable)reg_regEmailEditText4.getBackground();
				back_phone.setStroke(1, Color.WHITE);
			} else {
				GradientDrawable back_phone = (GradientDrawable)reg_regEmailEditText4.getBackground();
				back_phone.setStroke(1, Color.RED);
			}
			if(reg_regPrizvischeEditText4.getText().toString().length()>0&
					reg_regNameEditText4.getText().toString().length()>0&
					reg_regPobatkoviEditText4.getText().toString().length()>0&
					Utils.phoneCheck(reg_regMobPhoneEditText4.getText().toString())&
					Utils.isEmailCorrect(reg_regEmailEditText4.getText().toString())) {
				return true;
			} else {
				if(!reg_CardOwner_4LL.isShown())reg_CardOwner_4LL.setVisibility(View.VISIBLE);
				return false;		
			}
		} else return true;
	}

	@Override
	public void onTaskCompleted(int id_request) {
		// TODO Auto-generated method stub
		
	}

	@SuppressLint("NewApi")
	@Override
	public void onTaskCompletedJSON(String json, int id_request) {
		// TODO Auto-generated method stub
		switch(id_request){
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
				BottomDialog.BottomSelectDialog(Out_Register_Yur_2_3.this, 
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
	        	startActivity(new Intent(Out_Register_Yur_2_3.this, Out_RegInStart.class)); 
				finish();
				break;
			
			case GlobalConstants.ERROR_503:
				BottomDialog.BottomSelectDialog(Out_Register_Yur_2_3.this, 
						error503, 
		    			getString(R.string.dialog_Server503Title), 
		    			getString(R.string.dialog_Server503Text), 
		    			getString(R.string.dialog_Server503Left), 
		    			getString(R.string.dialog_Server503Right));
			break;
			
			default:
				BottomDialog.BottomSelectDialog(Out_Register_Yur_2_3.this, 
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
	            	Out_Register_Yur_2_3.this.finishAffinity();
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
	            	Out_Register_Yur_2_3.this.finishAffinity();
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
		tracker.setScreenName("register_yur_two");
		tracker.send(new HitBuilders.EventBuilder().build());
	}
	
}
