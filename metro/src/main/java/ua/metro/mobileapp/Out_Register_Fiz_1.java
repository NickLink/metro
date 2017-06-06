package ua.metro.mobileapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.method.LinkMovementMethod;
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
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

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

public class Out_Register_Fiz_1 extends Activity implements OnTaskCompleted{
	Typeface clearSans, clearSansMedium, clearSansBold;
	static Typeface clearSansLight;
	View subHeader;
	ImageButton iconBackImageButton;
	Spinner spinnerDay, spinnerMonth, spinnerYear;
	//Button buttonDay, buttonMonth, buttonYear;
	ArrayList<String> dayList, monthList, yearList;
	int spiner_textSize = 14;
	int daySelected = 0, monthSelected = 0, yearSelected = 0;
	TextView reg_regError;
	ScrollView mainScrollView;
	ProgressDialog loader;
	Configuration config;
	
	@SuppressLint("NewApi")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.out_registration_fiz_1);
        
        //Back button     
        subHeader = (View) findViewById(R.id.subheader_layout);
        iconBackImageButton = (ImageButton) subHeader.findViewById(R.id.iconBackImageButton);
        iconBackImageButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Back Button
				finish();				
			}	            	
        });
        
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
        
        mainScrollView = (ScrollView) findViewById(R.id.mainScrollView);
        InitArrays();
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
				yearSelected = 1920 + position;
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub			
			}	        	
        });
        
        TextView subTitleTextView = (TextView) subHeader.findViewById(R.id.subTitleTextView);
        
        TextView reg_regBirchDate = (TextView) findViewById(R.id.reg_regBirchDate);       
        TextView reg_allFieldsMust = (TextView) findViewById(R.id.reg_allFieldsMust);
        TextView reg_regAgreement = (TextView) findViewById(R.id.reg_regAgreement);
        final Button reg_regGetRegister = (Button) findViewById(R.id.reg_regGetRegister);
        reg_regError = (TextView) findViewById(R.id.reg_regError);
        
      //Set fonts
        subTitleTextView.setTypeface(clearSansMedium);
        reg_regBirchDate.setTypeface(clearSansBold);
        reg_allFieldsMust.setTypeface(clearSansBold);
        reg_regAgreement.setTypeface(clearSansBold);
        reg_regGetRegister.setTypeface(clearSansMedium);
        
        //Set linkify
        reg_regAgreement.setMovementMethod(LinkMovementMethod.getInstance());
        
        //TODO go to register FIZ_OSOBA
        reg_regGetRegister.setOnClickListener(new OnSingleClickListener(){
			@Override
			public void onSingleClick(View v) {
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
					
					loader = new ProgressDialog(Out_Register_Fiz_1.this);
					loader.setMessage(getString(R.string.app_Loading));
					loader.show();
					
					HTTPAsynkTask mt = new HTTPAsynkTask(Out_Register_Fiz_1.this,
							GlobalConstants.POST_REGISTRATION_FIZ, 
							//null, null, 
							params, 
							Out_Register_Fiz_1.this);
					mt.execute();
					
					
				} else {
					
					Toast.makeText(Out_Register_Fiz_1.this, getString(R.string.reg_regDateError), Toast.LENGTH_LONG).show();

					
				}
				//startActivity(new Intent(Out_Register_Fiz_1.this, Out_SplashStart.class));		
			}	            	
        });
        
        reg_regGetRegister.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                	reg_regGetRegister.setAlpha((float) 0.55);
                    return false;
                case MotionEvent.ACTION_UP:
                	reg_regGetRegister.setAlpha((float) 1.0);
                    return false;
                default:
                    return false;
                }
            }
      	});
        
        setAnalytics();
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

	@Override
	public void onTaskCompleted(int id_request) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTaskCompletedJSON(String json, int id_request) {
		
		if( loader != null && loader.isShowing() )
			loader.dismiss();
		
		switch(id_request){
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
		        	AppEventsLogger logger = AppEventsLogger.newLogger(Out_Register_Fiz_1.this);
		        	logger.logEvent("new_fiz_user_registered");
		        	
		        	//Run Splash
		        	startActivity(new Intent(Out_Register_Fiz_1.this, Inner_Profile.class));
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
				BottomDialog.BottomSelectDialog(Out_Register_Fiz_1.this, 
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
	        	startActivity(new Intent(Out_Register_Fiz_1.this, Out_RegInStart.class)); 
				finish();
				break;
			
			case GlobalConstants.ERROR_503:
				BottomDialog.BottomSelectDialog(Out_Register_Fiz_1.this, 
						error503, 
		    			getString(R.string.dialog_Server503Title), 
		    			getString(R.string.dialog_Server503Text), 
		    			getString(R.string.dialog_Server503Left), 
		    			getString(R.string.dialog_Server503Right));
			break;
			
			default:
				BottomDialog.BottomSelectDialog(Out_Register_Fiz_1.this, 
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
	            	Out_Register_Fiz_1.this.finishAffinity();
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
	            	Out_Register_Fiz_1.this.finishAffinity();
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
		tracker.setScreenName("register_fiz");
		tracker.send(new HitBuilders.EventBuilder().build());
	}
	
}
