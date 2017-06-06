package ua.metro.mobileapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.HashMap;

import ua.metro.mobileapp.HttpMetods.HTTPAsynkTask;
import ua.metro.mobileapp.UI.BottomDialog;
import ua.metro.mobileapp.UI.OnSingleClickListener;
import ua.metro.mobileapp.application.MeApp;

public class Out_RegInStart extends Activity implements OnTaskCompleted {
	Typeface clearSans, clearSansMedium, mediumRoboto;
	final int num_pages = 2;
	Button regButton, inButton;
	ViewPager regInPager;
	//final String regex = "(\\+[0-9]+[\\- \\.]*)?" + "(\\([0-9]+\\)[\\- \\.]*)?" + "([0-9][0-9\\- \\.]+[0-9])";
	EditText editPhone, editPassword;
	String editPhoneString, editPasswordString;
	ProgressDialog loader;
	//PreloaderDialog preLoader;
	SharedPreferences sPref;
	TextView login_datahasbeensent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.out_reg_in_main);
		
		
		clearSans = FontCache.get("fonts/clearsansregular.ttf", getBaseContext());
		clearSansMedium = FontCache.get("fonts/clearsansmedium.ttf", getBaseContext());
		mediumRoboto = FontCache.get("fonts/robotomedium.ttf", getBaseContext());
		
		regInPager = (ViewPager) findViewById(R.id.regin_Pager);
		
		sPref = getSharedPreferences(getString(R.string.app_PREFNAME), MODE_PRIVATE);
		editPhoneString = sPref.getString(getString(R.string.app_SAVED_PHONE), null);
		editPasswordString = sPref.getString(getString(R.string.app_SAVED_PASSWORD), null);
		
		regButton = (Button) findViewById(R.id.regin_regButton);
		inButton = (Button) findViewById(R.id.regin_inButton);
		regButton.setTypeface(clearSansMedium);
		inButton.setTypeface(clearSansMedium);
		
		regInPager.setAdapter(new reginAdapter());
		regInPager.addOnPageChangeListener(new OnPageChangeListener(){
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub				
			}
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub				
			}
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				if (arg0 == 0){
					Utils.hideKeyboard(Out_RegInStart.this);
					regButton.setBackgroundResource(R.drawable.bg_gray_border);
					regButton.setTextColor(Color.WHITE);
					inButton.setBackgroundResource(0);
					inButton.setTextColor(Color.BLACK);					
				} else {
					inButton.setBackgroundResource(R.drawable.bg_gray_border);
					inButton.setTextColor(Color.WHITE);
					regButton.setBackgroundResource(0);
					regButton.setTextColor(Color.BLACK);
				}
			}        	
        });
		
		regButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				regInPager.setCurrentItem(0);
				//
				regButton.setBackgroundResource(R.drawable.bg_gray_border);
				regButton.setTextColor(Color.WHITE);
				inButton.setBackgroundResource(0);
				inButton.setTextColor(Color.BLACK);
			}			
		});
		inButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				regInPager.setCurrentItem(1);
				//
				inButton.setBackgroundResource(R.drawable.bg_gray_border);
				inButton.setTextColor(Color.WHITE);
				regButton.setBackgroundResource(0);
				regButton.setTextColor(Color.BLACK);
			}			
		});
		
		//Set Page if get from Extras
		int i = getIntent().getIntExtra("page", -1);
		if (i==0){
			regInPager.setCurrentItem(0);
		} else if (i==1){
			regInPager.setCurrentItem(1);
		}
		
		setAnalytics();
	}
	
	
    private class reginAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return num_pages;
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View)object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
        	
            View new_view=null;
            LayoutInflater inflater = getLayoutInflater();

            if (position == 0){
	            new_view = inflater.inflate(R.layout.out_reg_in_regpage, null);
	            //Definition
	            TextView regin_ihavecard = (TextView) new_view.findViewById(R.id.regin_ihavecard);
	            TextView regin_iwouldhavecard = (TextView) new_view.findViewById(R.id.regin_iwouldhavecard);
	            final Button regin_createProfile = (Button) new_view.findViewById(R.id.regin_createProfile);
	            final Button regin_getCard = (Button) new_view.findViewById(R.id.regin_getCard);
	            //Set fonts
	            regin_ihavecard.setTypeface(clearSans);
	            regin_iwouldhavecard.setTypeface(clearSans);         	            
	            regin_createProfile.setTypeface(clearSansMedium);
	            regin_getCard.setTypeface(clearSans);
	            
	            //Set OnClickListeners
	            regin_createProfile.setOnClickListener(new OnSingleClickListener(){
					@Override
					public void onSingleClick(View v) {
						Log.v("TAG", "!!!------>>>>onSingleCLICK!!!");
						startActivity(new Intent(Out_RegInStart.this, Out_ScanIn.class));
					}	            	
	            });       
	            
	            regin_getCard.setOnClickListener(new OnSingleClickListener(){
					@Override
					public void onSingleClick(View v) {
						// TODO Go to getCard
						startActivity(new Intent(Out_RegInStart.this, Out_Register_Common_0.class));
					}	            	
	            });
	            
	            regin_createProfile.setOnTouchListener(new OnTouchListener() {
	                @Override
	                public boolean onTouch(View v, MotionEvent event) {
	                    switch (event.getAction()) {
	                    case MotionEvent.ACTION_DOWN:
	                    	regin_createProfile.setAlpha((float) 0.55);
	                        return false;
	                    case MotionEvent.ACTION_UP:
	                    	regin_createProfile.setAlpha((float) 1.0);
	                        return false;
	                    default:
	                        return false;
	                    }
	                }
	          	});
	            
	            regin_getCard.setOnTouchListener(new OnTouchListener() {
	                @Override
	                public boolean onTouch(View v, MotionEvent event) {
	                    switch (event.getAction()) {
	                    case MotionEvent.ACTION_DOWN:
	                    	regin_getCard.setAlpha((float) 0.55);
	                        return false;
	                    case MotionEvent.ACTION_UP:
	                    	regin_getCard.setAlpha((float) 1.0);
	                        return false;
	                    default:
	                        return false;
	                    }
	                }
	          	}); 
	            
	            container.addView(new_view);
	            
            } else {
            	
	            new_view = inflater.inflate(R.layout.out_reg_in_inpage, null);
	            //Definition
	            login_datahasbeensent = (TextView)new_view.findViewById(R.id.login_datahasbeensent);
	            TextView login_enterinappTitle = (TextView)new_view.findViewById(R.id.login_enterinappTitle);
	            TextView login_forgotPassword = (TextView)new_view.findViewById(R.id.login_forgotPassword);
	            final Button login_enterButton = (Button) new_view.findViewById(R.id.login_enterButton);
	            editPhone = (EditText) new_view.findViewById(R.id.login_phone_enter);
	            editPassword = (EditText) new_view.findViewById(R.id.login_password_enter);
	            //Set fonts
	            login_datahasbeensent.setTypeface(clearSans);
	            login_enterinappTitle.setTypeface(clearSansMedium);
	            login_forgotPassword.setTypeface(clearSans);
	            login_enterButton.setTypeface(clearSansMedium);
	            editPhone.setTypeface(clearSans);
	            editPassword.setTypeface(clearSans);
	            if(editPhoneString!=null)
	            	editPhone.setText(editPhoneString);
				if(editPasswordString!=null)
					editPassword.setText(editPasswordString);
	            
	            //TODO  -------Set data for fast enter--------
	            //editPhone.setText(getString(R.string.app_testPhone));
	            //editPassword.setText(getString(R.string.app_testPassword));
	            
	            editPhone.setOnTouchListener(new OnTouchListener(){
	    			@Override
	    			public boolean onTouch(View v, MotionEvent event) {
	    				if(editPhone.getText().toString() == null
	    						|| editPhone.getText().toString().equals(""))
	    					editPhone.setText("380");
	    				return false;
	    			}    	
	            });
	            
	            //Set OnClickListeners
	            login_enterButton.setOnClickListener(new OnSingleClickListener(){
					@Override
					public void onSingleClick(View v) {
						// TODO Go to API Login
						if (Utils.phoneCheck(editPhone.getText().toString())){
							login_datahasbeensent.setText(getString(R.string.login_datahasbeensent));
							login_datahasbeensent.setTextColor(getResources().getColor(R.color.metro_black_text));
							
							loader = new ProgressDialog(Out_RegInStart.this);
							loader.setMessage(getString(R.string.app_Loading));
							loader.show();

							//Save phone number
							sPref.edit().putString(getString(R.string.app_SAVED_PHONE), editPhone.getText().toString()).apply();
							
							HashMap<String,String> params = new HashMap<String,String>();
			            	params.put("phone", editPhone.getText().toString());
							params.put("password", editPassword.getText().toString());
							HTTPAsynkTask mt = new HTTPAsynkTask(Out_RegInStart.this,
									GlobalConstants.AUTH, 
									//null, null, 
									params, 
									Out_RegInStart.this);
							mt.execute();
							Log.v("123", "VVV + onclick in login_enterButton" );
						} else {
							login_datahasbeensent.setText(getString(R.string.question_errorPhone));
							login_datahasbeensent.setTextColor(getResources().getColor(R.color.metro_red));
						}
						
						
						
						
					}	            	
	            });
	            login_forgotPassword.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						// TODO Go to forgot password
						startActivity(new Intent(Out_RegInStart.this, Out_PasswRestore_0.class));
						
					}	            	
	            });

	            login_enterButton.setOnTouchListener(new OnTouchListener() {
	                @Override
	                public boolean onTouch(View v, MotionEvent event) {
	                    switch (event.getAction()) {
	                    case MotionEvent.ACTION_DOWN:
	                    	GradientDrawable back_phone = (GradientDrawable)login_enterButton.getBackground();
	            			back_phone.setStroke(2, getResources().getColor(R.color.metro_yellow));
	                    	login_enterButton.setAlpha((float) 0.55);
	                        return false;
	                    case MotionEvent.ACTION_UP:
	                    	login_enterButton.setAlpha((float) 1.0);
	                        return false;
	                    default:
	                        return false;
	                    }
	                }
	          	});
	            
	            container.addView(new_view);
            }

            return new_view;
        }

    }


	@Override
	public void onTaskCompleted(int id) {
		Log.v("123", "VVV + onTaskCompleted" );
		
		if( loader != null && loader.isShowing() )
			loader.dismiss();
		try{
		switch(id){
		case GlobalConstants.AUTH:
			if(MeApp.regData.isSuccess()){

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
	        	
	        	//TODO Save card number
	        	sPref.edit().putString(getString(R.string.app_CARD_NUMBER), MeApp.regData.getUser().getCard_num()).apply();
	        	//Log.v("123", "PREF_S =" + MeApp.regData.getUser().getCard_num());

				//TODO Save password
				sPref.edit().putString(getString(R.string.app_SAVED_PASSWORD), editPassword.getText().toString()).apply();

	        	
				startActivity(new Intent(Out_RegInStart.this, Inner_Profile.class));
				finish();
			} else {
				int error_code = MeApp.regData.getCode();
				switch(error_code){
				case 401:
					login_datahasbeensent.setTextColor(getResources().getColor(R.color.metro_red));
					login_datahasbeensent.setText(getString(R.string.Error_401));
					/*Toast.makeText(Out_RegInStart.this, 
							getString(R.string.Error_401), 
							Toast.LENGTH_LONG).show();*/
					break;

				default:
					BottomDialog.BottomSelectDialog(Out_RegInStart.this, 
							error503, 
			    			getString(R.string.dialog_Server503Title), 
			    			getString(R.string.dialog_Server503Text), 
			    			getString(R.string.dialog_Server503Left), 
			    			getString(R.string.dialog_Server503Right));
					break;
						
				}
			}		
			break;
			
			//TODO ============ERRORS DEPLOYMENT==========
			case GlobalConstants.NO_INTERNET_CONNECTION:
				BottomDialog.BottomSelectDialog(Out_RegInStart.this, 
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
	        	startActivity(new Intent(Out_RegInStart.this, Out_RegInStart.class)); 
				finish();
				break;
			
			case GlobalConstants.ERROR_503:
				BottomDialog.BottomSelectDialog(Out_RegInStart.this, 
						error503, 
		    			getString(R.string.dialog_Server503Title), 
		    			getString(R.string.dialog_Server503Text), 
		    			getString(R.string.dialog_Server503Left), 
		    			getString(R.string.dialog_Server503Right));
			break;
			
			default:
				BottomDialog.BottomSelectDialog(Out_RegInStart.this, 
						error503, 
		    			getString(R.string.dialog_ServerUndefinedTitle), 
		    			getString(R.string.dialog_ServerUndefinedText), 
		    			getString(R.string.dialog_ServerUndefinedLeft), 
		    			getString(R.string.dialog_ServerUndefinedRight));
			break;
		}
		
		} catch (Exception e){
			//Do not fall
		}
		//Toast.makeText(getBaseContext(), "?????????????...", Toast.LENGTH_LONG).show();
		
	}


	@Override
	public void onTaskCompletedJSON(String json, int id) {
		//Log.v("TAG", "METRO OUTStart id_request=ERROR_401");
		Log.v("123", "VVV + onTaskCompletedJSON" );
		// TODO Auto-generated method stub
		if( loader != null && loader.isShowing() )
			loader.dismiss();
		switch (id){
		case GlobalConstants.NO_INTERNET_CONNECTION:
			BottomDialog.BottomSelectDialog(Out_RegInStart.this, 
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
        	startActivity(new Intent(Out_RegInStart.this, Out_RegInStart.class)); 
			finish();
			break;
		
		case GlobalConstants.ERROR_503:
			BottomDialog.BottomSelectDialog(Out_RegInStart.this, 
					error503, 
	    			getString(R.string.dialog_Server503Title), 
	    			getString(R.string.dialog_Server503Text), 
	    			getString(R.string.dialog_Server503Left), 
	    			getString(R.string.dialog_Server503Right));
		break;
		
		default:
			BottomDialog.BottomSelectDialog(Out_RegInStart.this, 
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
	            	Out_RegInStart.this.finishAffinity();
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
	            	Out_RegInStart.this.finishAffinity();
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
		tracker.setScreenName("registration_login");
		tracker.send(new HitBuilders.EventBuilder().build());
	}

}
