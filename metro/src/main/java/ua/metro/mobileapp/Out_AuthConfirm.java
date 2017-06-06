package ua.metro.mobileapp;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import ua.metro.mobileapp.HttpMetods.HTTPAsynkTask;
import ua.metro.mobileapp.UI.BottomDialog;
import ua.metro.mobileapp.UI.OnSingleClickListener;
import ua.metro.mobileapp.application.MeApp;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class Out_AuthConfirm  extends Activity implements OnTaskCompleted {
	
	Typeface fontRegular, fontMedium;
	Button confirmButton, questionButton;
	EditText cardNumberEditText;
	ImageButton iconBackImageButton;
	int RUN_FROM;
	ProgressDialog loader;
	TextView confirmTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.out_auth_confirm);
        
        fontRegular = FontCache.get("fonts/clearsansregular.ttf", getBaseContext());
        fontMedium = FontCache.get("fonts/clearsansmedium.ttf", getBaseContext());       
        
        View subheader_layout = (View) findViewById(R.id.subheader_layout);
        TextView subTitleTextView = (TextView) subheader_layout.findViewById(R.id.subTitleTextView);
        
        
        RUN_FROM = getIntent().getIntExtra("RUN_FROM", 0);
        
        if (RUN_FROM == 0){
        	//Set title Authorization
        	subTitleTextView.setText(getString(R.string.login_authorization));
        } else {
        	//Set title get Permanent card
        	subTitleTextView.setText(getString(R.string.app_PermanentSubHeader));
        }
        
        //BackButton
        ImageButton iconBackImageButton = (ImageButton) subheader_layout.findViewById(R.id.iconBackImageButton);
        iconBackImageButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Back Button
				finish();				
			}	            	
        });
        
                
        confirmTextView = (TextView) findViewById(R.id.confirmTextView);
        TextView quetionTextView = (TextView) findViewById(R.id.quetionTextView);
        cardNumberEditText = (EditText) findViewById(R.id.cardNumberEditText);
        confirmButton = (Button) findViewById(R.id.confirmButton);
        questionButton = (Button) findViewById(R.id.questionButton);

        subTitleTextView.setTypeface(fontMedium);
        confirmTextView.setTypeface(fontRegular);
        quetionTextView.setTypeface(fontRegular);
        cardNumberEditText.setTypeface(fontRegular);
        confirmButton.setTypeface(fontMedium);
        questionButton.setTypeface(fontMedium);
        
        
        //Set OnClickListeners
        confirmButton.setOnClickListener(new OnSingleClickListener(){
			@Override
			public void onSingleClick(View v) {
				// TODO Go to Confirm & to API again
				
				if (cardNumberEditText.getText().toString().length()!=22){
					confirmTextView.setText(getString(R.string.login_cardnotexisttitle));
					confirmTextView.setTextColor(getResources().getColor(R.color.metro_red));
					
					/*Toast.makeText(Out_AuthConfirm.this, 
							getString(R.string.Error_card_numberIncorrect), 
							Toast.LENGTH_LONG).show();*/
				} else {
					confirmTextView.setText(getString(R.string.login_cardnotexisttitle));
					confirmTextView.setTextColor(getResources().getColor(R.color.metro_red));
					
					loader = new ProgressDialog(Out_AuthConfirm.this);
					loader.setMessage(getString(R.string.app_Loading));
					loader.show();
					
					HashMap<String,String> params = new HashMap<String,String>();
					
					switch(RUN_FROM){
					case 0:					
		            	params.put("card", cardNumberEditText.getText().toString());	            	
						HTTPAsynkTask mt = new HTTPAsynkTask(Out_AuthConfirm.this, 
								GlobalConstants.CARD_HAS_PROFILE, 							
								//cardNumberEditText.getText().toString(), null, 
								params, Out_AuthConfirm.this);
						mt.execute();
						break;
					case 1:
		            	params.put("card", cardNumberEditText.getText().toString());
						HTTPAsynkTask mt_Permanent = new HTTPAsynkTask(Out_AuthConfirm.this, 
								GlobalConstants.POST_PERMANENT_CARD, 
								//null, null, 
								params, Out_AuthConfirm.this);
						mt_Permanent.execute();		
						break;
					}
				}
				
			}	            	
        });
        //Set selector change button alpha
        confirmButton.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                	confirmButton.setAlpha((float) 0.55);
                    return false;
                case MotionEvent.ACTION_UP:
                	confirmButton.setAlpha((float) 1.0);
                    return false;
                default:
                    return false;
                }
            }
      	});	
        
        //Set OnClickListeners
        questionButton.setOnClickListener(new OnSingleClickListener(){
			@Override
			public void onSingleClick(View v) {
				// TODO Go to Login
				startActivity(new Intent(Out_AuthConfirm.this, Out_AuthQuestion.class));
				//finish();
				
			}	            	
        });
        //Set selector change button alpha
        questionButton.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                	questionButton.setAlpha((float) 0.55);
                    return false;
                case MotionEvent.ACTION_UP:
                	questionButton.setAlpha((float) 1.0);
                    return false;
                default:
                    return false;
                }
            }
      	});	
        
        setAnalytics();
    }

	@Override
	public void onTaskCompleted(int id) {

		if( loader != null && loader.isShowing() )
			loader.dismiss();
		
		//--- GO TO GET API RESPONSE
		switch(id){
		case GlobalConstants.CARD_HAS_PROFILE:
			Log.v("Tag", "1111 MeApp.regData.getStatus() =" + MeApp.regData.getStatus());
			switch(MeApp.regData.getStatus()){
			case 0:
				//---Card not exist
				//do fu..ing nothing
				break;
			case 1:
				//---Card exist & confirmed
				startActivity(new Intent(Out_AuthConfirm.this, Out_AuthIn.class));
				break;
			case 2:
				//---Card exist but not confirmed
				startActivity(new Intent(Out_AuthConfirm.this, Out_AuthCard_2.class));
				break;
			default:
				Toast.makeText(Out_AuthConfirm.this, 
						getString(R.string.Error_server_not_responding), Toast.LENGTH_LONG).show();
				break;
			}			
			break;
			
			//TODO ============ERRORS DEPLOYMENT==========
			case GlobalConstants.NO_INTERNET_CONNECTION:
				BottomDialog.BottomSelectDialog(Out_AuthConfirm.this, 
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
	        	startActivity(new Intent(Out_AuthConfirm.this, Out_RegInStart.class)); 
				finish();
				break;
			
			case GlobalConstants.ERROR_503:
				BottomDialog.BottomSelectDialog(Out_AuthConfirm.this, 
						error503, 
		    			getString(R.string.dialog_Server503Title), 
		    			getString(R.string.dialog_Server503Text), 
		    			getString(R.string.dialog_Server503Left), 
		    			getString(R.string.dialog_Server503Right));
			break;
			
			default:
				BottomDialog.BottomSelectDialog(Out_AuthConfirm.this, 
						error503, 
		    			getString(R.string.dialog_ServerUndefinedTitle), 
		    			getString(R.string.dialog_ServerUndefinedText), 
		    			getString(R.string.dialog_ServerUndefinedLeft), 
		    			getString(R.string.dialog_ServerUndefinedRight));
			break;
		}
		
	}

	@Override
	public void onTaskCompletedJSON(String json, int id) {
		
		if( loader != null && loader.isShowing() )
			loader.dismiss();
		
		switch(id){
		case GlobalConstants.POST_PERMANENT_CARD:
		try {
			JSONObject data = new JSONObject(json);
			if(!data.isNull("success")){
				//SUCCESS Card change
				
				//Allow to use barcode
				SharedPreferences sPref = getSharedPreferences(getString(R.string.app_PREFNAME), MODE_PRIVATE);
	        	sPref.edit().remove(getString(R.string.app_TEMP_CARDEXPIRED)).apply();
	        	
				//Need to re-read profile info data 
	        	
				HTTPAsynkTask mt = new HTTPAsynkTask(Out_AuthConfirm.this, 
						GlobalConstants.GET_PROFILE_INFO, 
						//null, null, 
						null, 
						Out_AuthConfirm.this);
				mt.execute();				
				
			} else if (!data.isNull("error")){
				//ERROR Card change
				int code = data.optInt("code");
				switch(code){
					case 500:
						// Ошибка при вводе номера карты (должно быть 22 символа)
						Toast.makeText(Out_AuthConfirm.this, getString(R.string.Error_500), Toast.LENGTH_LONG).show();
						break;
					case 503:
						//Ошибка проверки checksum карты (ошибка при вводе)
						Toast.makeText(Out_AuthConfirm.this, getString(R.string.Error_503), Toast.LENGTH_LONG).show();
						break;
					case 805:
						//Номер телефона владельца карты не совпадает с номером текущего пользователя
						Toast.makeText(Out_AuthConfirm.this, getString(R.string.Error_805), Toast.LENGTH_LONG).show();
						break;
					
				}
				
			}
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		break;

		case GlobalConstants.GET_PROFILE_INFO:			
			
			try {
				JSONObject data = new JSONObject(json);
				if (data.has("user")){
					MeApp.regData.setToken(data.optString("token"));
					MeApp.regData.getUser().setCount_basket(data.optInt("cart_items_count"));
					
					JSONObject user_data = data.getJSONObject("user");
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
					
					Intent intent = new Intent();
				    setResult(RESULT_OK, intent);
				    finish();
					
				} else if (data.has("error")){
					Toast.makeText(Out_AuthConfirm.this, getString(R.string.Error_server_not_responding), 
							Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(Out_AuthConfirm.this, getString(R.string.Error_server_not_responding), 
							Toast.LENGTH_LONG).show();
				}
				
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.v("TAG", "PROFILE JSONException e=" + e);
			}
			
			break;
			
			//TODO ============ERRORS DEPLOYMENT==========
			case GlobalConstants.NO_INTERNET_CONNECTION:
				BottomDialog.BottomSelectDialog(Out_AuthConfirm.this, 
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
	        	startActivity(new Intent(Out_AuthConfirm.this, Out_RegInStart.class)); 
				finish();
				break;
			
			case GlobalConstants.ERROR_503:
				BottomDialog.BottomSelectDialog(Out_AuthConfirm.this, 
						error503, 
		    			getString(R.string.dialog_Server503Title), 
		    			getString(R.string.dialog_Server503Text), 
		    			getString(R.string.dialog_Server503Left), 
		    			getString(R.string.dialog_Server503Right));
			break;
			
			default:
				BottomDialog.BottomSelectDialog(Out_AuthConfirm.this, 
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
	            	Out_AuthConfirm.this.finishAffinity();
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
	            	Out_AuthConfirm.this.finishAffinity();
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
	
	private void setAnalytics() {
		GoogleAnalytics analytics;
		final Tracker tracker;
		analytics = GoogleAnalytics.getInstance(this);
		analytics.setLocalDispatchPeriod(1800);
		tracker = analytics.newTracker(getString(R.string.app_GA_CODE));
		tracker.enableExceptionReporting(true);
		tracker.enableAdvertisingIdCollection(true);
		tracker.enableAutoActivityTracking(true);
		tracker.setScreenName("auth_confirm_one");
		tracker.send(new HitBuilders.EventBuilder().build());
	}
	
}
