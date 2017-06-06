package ua.metro.mobileapp;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import ua.metro.mobileapp.HttpMetods.HTTPAsynkTask;
import ua.metro.mobileapp.UI.BottomDialog;
import ua.metro.mobileapp.UI.OnSingleClickListener;
import ua.metro.mobileapp.application.MeApp;
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

public class Out_Register_Yur_1_3 extends Activity implements OnTaskCompleted{
	Typeface clearSans, clearSansMedium, clearSansBold;
	static Typeface clearSansLight;
	ImageButton iconBackImageButton;
	TextView reg_regError;
	ScrollView mainScrollView;
	ArrayList<org_area_activity> companyArea;
	ArrayList<org_direction> companyDirection;
	ArrayList<String> filteredDirectionTitle;
	ArrayList<Integer> filteredDirectionID;
	Spinner spinnerArea, spinnerDirection;
	EditText reg_regCompanyNameEditText, reg_regEDRPOUEditText;
	int spinnerAreaPosition = 0, spinnerDirectionPosition = 0, spiner_textSize = 14;
	Configuration config;
	
	@Override
	public void onBackPressed() {
		finish();
	}
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.out_registration_yur_1_3);
        //Back button
        iconBackImageButton = (ImageButton) findViewById(R.id.iconBackImageButton);
        iconBackImageButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Back Button
				finish();				
			}	            	
        });
        
        //Load Data for Area & Directions
        HTTPAsynkTask mt = new HTTPAsynkTask(Out_Register_Yur_1_3.this,
				GlobalConstants.GET_REG_META, 
				//null, null, 
				null, 
				Out_Register_Yur_1_3.this);
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
        
        clearSans = FontCache.get("fonts/clearsans.ttf", getBaseContext());
        clearSansBold = FontCache.get("fonts/clearsansbold.ttf", getBaseContext());
        clearSansMedium = FontCache.get("fonts/clearsansmedium.ttf", getBaseContext());
        clearSansLight = FontCache.get("fonts/clearsanslight.ttf", getBaseContext());
        
        mainScrollView = (ScrollView) findViewById(R.id.mainScrollView);
        TextView reg_regCompanyName = (TextView) findViewById(R.id.reg_regCompanyName);
        TextView reg_regCompanySphere = (TextView) findViewById(R.id.reg_regCompanySphere);
        TextView reg_regCompanyNapryam = (TextView) findViewById(R.id.reg_regCompanyNapryam);
        TextView reg_regEDRPOU = (TextView) findViewById(R.id.reg_regEDRPOU);       
        TextView reg_allFieldsMust = (TextView) findViewById(R.id.reg_allFieldsMust);
        TextView reg_regOneThree = (TextView) findViewById(R.id.reg_regOneThree);
        final Button reg_regCONTINUE = (Button) findViewById(R.id.reg_regCONTINUE);
        reg_regError = (TextView) findViewById(R.id.reg_regError);
        
        //Spinners
        spinnerArea = (Spinner) findViewById(R.id.spinnerArea);
        spinnerDirection = (Spinner) findViewById(R.id.spinnerDirection);
        
        //EditText
        reg_regCompanyNameEditText = (EditText) findViewById(R.id.reg_regCompanyNameEditText);
        reg_regEDRPOUEditText = (EditText) findViewById(R.id.reg_regEDRPOUEditText);
        
      //Check For Focus
        reg_regCompanyNameEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
      	        public void onFocusChange(View v, boolean hasFocus) {
      	            if (!hasFocus) {
      	            	r_ComNShow();
      	            }
      	        }
      	    });
        reg_regEDRPOUEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
      	        public void onFocusChange(View v, boolean hasFocus) {
      	            if (!hasFocus) {
      	            	r_EdrpShow();
      	            }
      	        }
      	    });
        
        reg_regCompanyName.setTypeface(clearSansBold);
        reg_regCompanySphere.setTypeface(clearSansBold);
        reg_regCompanyNapryam.setTypeface(clearSansBold);
        reg_regEDRPOU.setTypeface(clearSansBold);
        reg_allFieldsMust.setTypeface(clearSansBold);
        reg_regOneThree.setTypeface(clearSansBold);
        reg_regCONTINUE.setTypeface(clearSansMedium);
        
        //TODO go to page 2
        reg_regCONTINUE.setOnClickListener(new OnSingleClickListener(){
			@Override
			public void onSingleClick(View v) {
				if (checkAllFieldsCorrect()){
					reg_regError.setVisibility(View.GONE);
					putAllDataInFields();
					startActivity(new Intent(Out_Register_Yur_1_3.this, Out_Register_Yur_2_3.class));
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
	
	private boolean checkAllFieldsCorrect(){
		r_ComNShow();
		r_EdrpShow();
		
		if(reg_regCompanyNameEditText.getText().toString().length()>0&
				reg_regEDRPOUEditText.getText().toString().length()>7) {
			return true;
		} else 
			//TODO FALSE
			return false;		
	}
	
	void r_ComNShow(){
		if(reg_regCompanyNameEditText.getText().toString().length()>0) {
			GradientDrawable back_phone = (GradientDrawable)reg_regCompanyNameEditText.getBackground();
			back_phone.setStroke(1, Color.WHITE);
		} else {
			GradientDrawable back_phone = (GradientDrawable)reg_regCompanyNameEditText.getBackground();
			back_phone.setStroke(1, Color.RED);			
		}
	}
	
	void r_EdrpShow(){
		if(reg_regEDRPOUEditText.getText().toString().length()>7) {
			GradientDrawable back_phone = (GradientDrawable)reg_regEDRPOUEditText.getBackground();
			back_phone.setStroke(1, Color.WHITE);
		} else {
			GradientDrawable back_phone = (GradientDrawable)reg_regEDRPOUEditText.getBackground();
			back_phone.setStroke(1, Color.RED);
		}
	}
	
	private void putAllDataInFields() {
		// TODO putAllDataInFields
		MeApp.regYurData.setCompany_name(reg_regCompanyNameEditText.getText().toString());
		MeApp.regYurData.setEdrpou(reg_regEDRPOUEditText.getText().toString());
		MeApp.regYurData.setArea_activity(companyArea.get(spinnerAreaPosition).id);
		MeApp.regYurData.setDirection(filteredDirectionID.get(spinnerAreaPosition));
	}

	@Override
	public void onTaskCompleted(int id_request) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTaskCompletedJSON(String json, int id_request) {
		// TODO Auto-generated method stub
		
		switch(id_request){
		case GlobalConstants.GET_REG_META:
			try {
				JSONObject data = new JSONObject(json);
				
				JSONArray org_area_activityArray = data.getJSONArray("org_area_activity");
				companyArea = new ArrayList<org_area_activity>();
				int org_area_Length = org_area_activityArray.length();
				Log.v("TAG", "METRO org_area_Length =" + org_area_Length);
				for (int i=0;i<org_area_Length;i++){
					org_area_activity companyAreaTemp = new org_area_activity();
					companyAreaTemp.id = org_area_activityArray.getJSONObject(i).optInt("id");
					companyAreaTemp.title = org_area_activityArray.getJSONObject(i).optString("title");
					companyArea.add(companyAreaTemp);
				}	
				
				JSONArray org_direction = data.getJSONArray("org_direction");
				companyDirection = new ArrayList<org_direction>();
				int org_directionLength = org_direction.length();
				Log.v("TAG", "METRO org_directionLength =" + org_directionLength);
				for (int i=0;i<org_directionLength;i++){
					org_direction companyDirectionTemp = new org_direction();
					companyDirectionTemp.id = org_direction.getJSONObject(i).optInt("id");
					companyDirectionTemp.org_area_activity_id = org_direction.getJSONObject(i).optInt("org_area_activity_id");
					companyDirectionTemp.title = org_direction.getJSONObject(i).optString("title");			
					companyDirection.add(companyDirectionTemp);
				}
				
				setAreaSpinnerAdapter(org_area_Length, org_directionLength);
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.v("TAG", "METRO JSONException =" + e);
			}	
			break;
			
			//TODO ============ERRORS DEPLOYMENT==========
			case GlobalConstants.NO_INTERNET_CONNECTION:
				BottomDialog.BottomSelectDialog(Out_Register_Yur_1_3.this, 
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
	        	startActivity(new Intent(Out_Register_Yur_1_3.this, Out_RegInStart.class)); 
				finish();
				break;
			
			case GlobalConstants.ERROR_503:
				BottomDialog.BottomSelectDialog(Out_Register_Yur_1_3.this, 
						error503, 
		    			getString(R.string.dialog_Server503Title), 
		    			getString(R.string.dialog_Server503Text), 
		    			getString(R.string.dialog_Server503Left), 
		    			getString(R.string.dialog_Server503Right));
			break;
			
			default:
				BottomDialog.BottomSelectDialog(Out_Register_Yur_1_3.this, 
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
	            	Out_Register_Yur_1_3.this.finishAffinity();
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
	            	Out_Register_Yur_1_3.this.finishAffinity();
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
	
	@SuppressLint("NewApi")
	void setAreaSpinnerAdapter(final int org_area_Length, final int org_directionLength){
		//Convert to String Array from ArrayList
		String[] AreaString = new String[org_area_Length];		
		for(int i=0;i<org_area_Length;i++) AreaString[i] = companyArea.get(i).title;
		
		ArrayAdapter<String> Area_adapter = new ArrayAdapter<String>(this, R.layout.my_spinner_textview, 
				R.id.spinnerItem, AreaString)
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
				Area_adapter.setDropDownViewResource(R.layout.my_spinner_textview);
				spinnerArea.setDropDownWidth(android.view.ViewGroup.LayoutParams.MATCH_PARENT);
				spinnerArea.setAdapter(Area_adapter);
				spinnerArea.setOnItemSelectedListener(new OnItemSelectedListener(){
				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
					spinnerAreaPosition = position;
					setDirectionSpinnerAdapter(org_directionLength, companyArea.get(position).id);		
				}
				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					// TODO Auto-generated method stub			
				}	        	
	        });
	}
	
	@SuppressLint("NewApi")
	void setDirectionSpinnerAdapter(final int org_directionLength, final int area_selectedID){
		filteredDirectionTitle = new ArrayList<String>();
		filteredDirectionID = new ArrayList<Integer>();
		for(int i=0;i<org_directionLength;i++) {
			if (companyDirection.get(i).org_area_activity_id == area_selectedID){
				filteredDirectionTitle.add(companyDirection.get(i).title);
				filteredDirectionID.add(companyDirection.get(i).id);
			}
		}
				
		ArrayAdapter<String> Direction_adapter = new ArrayAdapter<String>(this, R.layout.my_spinner_textview, 
				R.id.spinnerItem, filteredDirectionTitle)
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
				Direction_adapter.setDropDownViewResource(R.layout.my_spinner_textview);
				spinnerDirection.setDropDownWidth(android.view.ViewGroup.LayoutParams.MATCH_PARENT);
				spinnerDirection.setAdapter(Direction_adapter);
				spinnerDirection.setOnItemSelectedListener(new OnItemSelectedListener(){
				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
					spinnerDirectionPosition = position;
				}
				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					// TODO Auto-generated method stub			
				}	        	
	        });
		
	}
	
	class org_direction{
		int id;
		int org_area_activity_id;
		String title;
	}
	
	class org_area_activity{
		int id;
		String title;
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
		tracker.setScreenName("register_yur_one");
		tracker.send(new HitBuilders.EventBuilder().build());
	}
	
}
