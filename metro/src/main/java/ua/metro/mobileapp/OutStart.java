package ua.metro.mobileapp;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.webkit.WebView;
import android.widget.ImageView;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import ua.metro.mobileapp.HttpMetods.HTTPAsynkTask;
import ua.metro.mobileapp.UI.BottomDialog;
import ua.metro.mobileapp.UI.FastAnimationContainer;
import ua.metro.mobileapp.application.MeApp;


@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class OutStart extends Activity implements OnTaskCompleted {
	Typeface fontMedium;
	WebView gifView;
	String tempToken;
	SharedPreferences sPref;
	ImageView preloaderImageView;
	FastAnimationContainer mFasterAnimationsContainer;
	private static final int ANIMATION_INTERVAL = 25;
	Handler handler;
	
	//private static final String CAMPAIGN_SOURCE_PARAM = "utm_source";
	

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        setContentView(R.layout.out_login_preload);
        
        preloaderImageView = (ImageView) findViewById(R.id.preloaderImageView);
        
        
        
       sPref = getSharedPreferences(getString(R.string.app_PREFNAME), MODE_PRIVATE);
       tempToken = sPref.getString(getString(R.string.app_TOKEN), null);
       
       if (tempToken != null){
    	   Log.v("TAG", "M START Start with TOKEN! = " + tempToken);
    	   
    	   mFasterAnimationsContainer = FastAnimationContainer.getInstance(preloaderImageView);
           mFasterAnimationsContainer.addAllFrames(GlobalConstants.IMAGE_RESOURCES, ANIMATION_INTERVAL);
           mFasterAnimationsContainer.start();       
           handler = new Handler();
    	   
    	   handler.postDelayed(new Runnable() {
               public void run() {
                // Actions to do after 2 seconds
           		MeApp.regData.setToken(tempToken);
        		HTTPAsynkTask mt = new HTTPAsynkTask(OutStart.this, 
        				GlobalConstants.GET_PROFILE_INFO, 
        				//null, null, 
        				null, 
        				OutStart.this);
        		mt.execute();
               }
           }, 1000);

    	} else {
    		if(sPref.getInt(getString(R.string.app_RESTARTED), 0) == 1){
    			Log.v("TAG", "M START RESTARTED");
    			sPref.edit().remove(getString(R.string.app_RESTARTED)).apply();
            	startActivity(new Intent(OutStart.this, Out_RegInStart.class).putExtra("page", 1)); 
				finish();
    		} else {
    			Log.v("TAG", "M START Start with no token!");
    			
    			mFasterAnimationsContainer = FastAnimationContainer.getInstance(preloaderImageView);
    	        mFasterAnimationsContainer.addAllFrames(GlobalConstants.IMAGE_RESOURCES, ANIMATION_INTERVAL);
    	        mFasterAnimationsContainer.start();       
    	        handler = new Handler();
    			
    			handler.postDelayed(new Runnable() {
                    public void run() {
                     // Actions to do after 2 seconds
                    	mFasterAnimationsContainer.stop();
                    	startActivity(new Intent(OutStart.this, Out_RegInStart.class)); 
        				finish();
                    }
                }, 1500);
    		}
    	}
       setLocal("");
       setAnalytics();
    }

	@Override
	public void onTaskCompleted(int id_request) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTaskCompletedJSON(String json, int id_request) {
		// TODO Auto-generated method stub
		
		Log.v("TAG", "METRO OUTStart id_request=" + id_request);
		switch(id_request){
		case GlobalConstants.GET_PROFILE_INFO:			
			try {
				JSONObject yurData = new JSONObject(json);
				if(yurData.has("user")){
					
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
					//Check for language
					if(MeApp.regData.getUser().getLocale().equalsIgnoreCase("ru")){
						setLocal("ru");
					} else {
						setLocal("");
					}
					
					//TODO Save token
					//SharedPreferences sPref = getSharedPreferences(getString(R.string.app_PREFNAME), MODE_PRIVATE);
		        	sPref.edit().putString(getString(R.string.app_TOKEN), MeApp.regData.getToken()).apply();
		        	//TODO Save card number
		        	sPref.edit().putString(getString(R.string.app_CARD_NUMBER), MeApp.regData.getUser().getCard_num()).apply();
		        	//TODO Save card valid date
		        	if(MeApp.regData.getUser().getValid_to() != null){
		        		sPref.edit().putString(getString(R.string.app_CARD_VALID_TO), 
		        				MeApp.regData.getUser().getValid_to()).apply();
		        	}
		        	
		        	
		        	//Run Splash
		        	mFasterAnimationsContainer.stop();
		        	startActivity(new Intent(OutStart.this, Inner_Profile.class));
					finish();
					
				} else if (yurData.has("error")){
					mFasterAnimationsContainer.stop();
					//SharedPreferences sPref = getSharedPreferences(getString(R.string.app_PREFNAME), MODE_PRIVATE);
	            	sPref.edit().remove(getString(R.string.app_TOKEN)).apply();
	            	startActivity(new Intent(OutStart.this, Out_RegInStart.class)); 
					finish();			
				}
				
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				mFasterAnimationsContainer.stop();
            	startActivity(new Intent(OutStart.this, Out_RegInStart.class)); 
				finish();
			}
			break;
			
			case GlobalConstants.NO_INTERNET_CONNECTION:
				mFasterAnimationsContainer.stop();
				BottomDialog.BottomSelectDialogCode(OutStart.this, 
	    			noInternetConn, 
	    			getString(R.string.dialog_NoInternetTitle), 
	    			getString(R.string.dialog_NoInternetText), 
	    			getString(R.string.my_kard), 	//dialog_NoInternetSettings
	    			getString(R.string.dialog_NoInternetSettings));		//dialog_NoInternetQuit
			break;
			
			case GlobalConstants.ERROR_401:
				Log.v("TAG", "METRO OUTStart id_request=ERROR_401");
				mFasterAnimationsContainer.stop();
				//SharedPreferences sPref = getSharedPreferences(getString(R.string.app_PREFNAME), MODE_PRIVATE);
	        	sPref.edit().remove(getString(R.string.app_TOKEN)).apply();
	        	startActivity(new Intent(OutStart.this, Out_RegInStart.class)); 
				finish();
				break;
			
			case GlobalConstants.ERROR_503:
				mFasterAnimationsContainer.stop();
				BottomDialog.BottomSelectDialog(OutStart.this,
						error503,
						getString(R.string.dialog_Server503Title),
						getString(R.string.dialog_Server503Text),
						getString(R.string.dialog_Server503Left),
						getString(R.string.dialog_Server503Right));
				break;
			default:
				mFasterAnimationsContainer.stop();
				BottomDialog.BottomSelectDialog(OutStart.this,
						error503,
						getString(R.string.dialog_Server503Title),
						getString(R.string.dialog_Server503Text),
						getString(R.string.dialog_Server503Left),
						getString(R.string.dialog_Server503Right));
				break;
		}		

	}
	
	Handler noInternetConn = new Handler() {
	    @Override
	    public void handleMessage(Message msg) {
	        if (msg != null) {
	            // Check for dialog box responses
	            if (msg.what == GlobalConstants.retCodeLeft) {
	            	//Go check temporary card validation
//	            	String v_to = sPref.getString(getString(R.string.app_CARD_VALID_TO), null);
//	            	String expired = sPref.getString(getString(R.string.app_TEMP_CARDEXPIRED), null);
//	            	if(expired != null && expired.equals("Yes")){
//	            		//Show new dialog about card is exired
//	            		BottomDialog.BottomMessageDialogExpired(OutStart.this,
//	            				getString(R.string.dialog_NoInternetTitle),
//	            				getString(R.string.dialog_CardExpiredText));
//
//	            	} else if(v_to != null && !Inner_Main.cardValid(v_to)){
//	            		//Show new dialog about card is exired
//	            		BottomDialog.BottomMessageDialogExpired(OutStart.this,
//	            				getString(R.string.dialog_NoInternetTitle),
//	            				getString(R.string.dialog_CardExpiredText));
//	            	} else {
//	            		Intent intent = new Intent(getBaseContext(), Inner_ProfileFullScreenCode.class);
//		            	startActivity(intent);
//	            	}

					Intent intent = new Intent(getBaseContext(), Inner_ProfileFullScreenCode.class);
					startActivity(intent);
	            	 
	            } else if (msg.what == GlobalConstants.retCodeRight) {
	            	//OutStart.this.finishAffinity();
	            	Intent viewIntent = new Intent(Settings.ACTION_WIFI_SETTINGS);
	            	startActivityForResult(viewIntent, GlobalConstants.REQUEST_CODE_INET);
	            	
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
	            	OutStart.this.finishAffinity();
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
	
	
	/*
	@Override
	  protected void onRestart() {
	    super.onRestart();
	    //Restart assynktask
	    //SharedPreferences sPref = getSharedPreferences(getString(R.string.app_PREFNAME), MODE_PRIVATE);
	       String tempToken = sPref.getString(getString(R.string.app_TOKEN), null);
	       if (tempToken != null){
	    		MeApp.regData.setToken(tempToken);
	    		HTTPAsynkTask mt = new HTTPAsynkTask(OutStart.this, 
	    				GlobalConstants.GET_PROFILE_INFO, 
	    				//null, null, 
	    				null, 
	    				OutStart.this);
	    		mt.execute();
	    	}
	  }
	  */
	
	private void setLocal(String lang) {
		Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
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
		tracker.setScreenName("start");
		tracker.send(new HitBuilders.EventBuilder().build());
	}
}
