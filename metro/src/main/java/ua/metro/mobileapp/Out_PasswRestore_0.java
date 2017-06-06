package ua.metro.mobileapp;

import java.util.HashMap;

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

public class Out_PasswRestore_0 extends Activity implements OnTaskCompleted{
	Typeface clearSans, clearSansMedium, mediumRoboto;
	Button passw_GetPasswButton;
	ImageButton iconBackImageButton;
	EditText passw_phone_enter;
	String phone;
	ProgressDialog loader;
	TextView restore_PasswText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.out_passw_restore_zero);
		//BackButton
		iconBackImageButton = (ImageButton) findViewById(R.id.iconBackImageButton);
		iconBackImageButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Back Button
				finish();				
			}	            	
        });
		
		clearSans = FontCache.get("fonts/clearsansregular.ttf", getBaseContext());
		clearSansMedium = FontCache.get("fonts/clearsansmedium.ttf", getBaseContext());
		mediumRoboto = FontCache.get("fonts/robotomedium.ttf", getBaseContext());		
		
		restore_PasswText = (TextView)findViewById(R.id.restore_PasswText);
		TextView subTitleTextView = (TextView)findViewById(R.id.subTitleTextView);
		passw_phone_enter = (EditText)findViewById(R.id.passw_phone_enter);
		passw_GetPasswButton = (Button) findViewById(R.id.passw_GetPasswButton);
		
		passw_phone_enter.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(passw_phone_enter.getText().toString() == null
						|| passw_phone_enter.getText().toString().equals(""))
					passw_phone_enter.setText("380");
				return false;
			}    	
        });
		
		restore_PasswText.setTypeface(clearSans);
		subTitleTextView.setTypeface(clearSansMedium);
		passw_GetPasswButton.setTypeface(clearSansMedium);

		passw_GetPasswButton.setOnClickListener(new OnSingleClickListener(){
			@Override
			public void onSingleClick(View v) {
				// TODO Go reset password api
				phone = passw_phone_enter.getText().toString();
				
				if (Utils.phoneCheck(phone)){					
					restore_PasswText.setText(getString(R.string.login_restorepasswordtext));
					restore_PasswText.setTextColor(getResources().getColor(R.color.metro_black_text));
					
					loader = new ProgressDialog(Out_PasswRestore_0.this);
					loader.setMessage(getString(R.string.app_Loading));
					loader.show();
					
					HashMap<String,String> params = new HashMap<String,String>();
	            	params.put("phone", phone);				
					HTTPAsynkTask mt = new HTTPAsynkTask(Out_PasswRestore_0.this,
							GlobalConstants.RESET_PASSWORD,	
							//null, phone, 
							params, 
							Out_PasswRestore_0.this);
					mt.execute();
				} else {
					restore_PasswText.setText(getString(R.string.question_errorPhone));
					restore_PasswText.setTextColor(getResources().getColor(R.color.metro_red));
				}
				
				
			}	            	
        });
		passw_GetPasswButton.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                	passw_GetPasswButton.setAlpha((float) 0.55);
                    return false;
                case MotionEvent.ACTION_UP:
                	passw_GetPasswButton.setAlpha((float) 1.0);
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
		
		switch(id){
		case GlobalConstants.RESET_PASSWORD:
			if(MeApp.regData.isSuccess()){
				Toast.makeText(Out_PasswRestore_0.this, 
						getString(R.string.Error_operationSuccess), 
						Toast.LENGTH_LONG).show();
				//Phone found
				startActivity(new Intent(Out_PasswRestore_0.this, Out_PasswRestore_2.class)
				.putExtra("phone", phone));
				finish();
			} else {
				int error_code = MeApp.regData.getCode();
				switch(error_code){
				case 804:
					//Phone not found
					Toast.makeText(Out_PasswRestore_0.this, 
							getString(R.string.Error_804), 
							Toast.LENGTH_LONG).show();
					break;
				default:
					/*Toast.makeText(Out_PasswRestore_0.this, 
							getString(R.string.Error_operationUnSuccess), 
							Toast.LENGTH_LONG).show();*/
					break;			
				}
				//Phone not found
				startActivity(new Intent(Out_PasswRestore_0.this, Out_PasswRestore_1.class)
				.putExtra("phone", phone));			
			}
			break;
			//TODO ============ERRORS DEPLOYMENT==========
			case GlobalConstants.NO_INTERNET_CONNECTION:
				BottomDialog.BottomSelectDialog(Out_PasswRestore_0.this, 
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
	        	startActivity(new Intent(Out_PasswRestore_0.this, Out_RegInStart.class)); 
				finish();
				break;
			
			case GlobalConstants.ERROR_503:
				BottomDialog.BottomSelectDialog(Out_PasswRestore_0.this, 
						error503, 
		    			getString(R.string.dialog_Server503Title), 
		    			getString(R.string.dialog_Server503Text), 
		    			getString(R.string.dialog_Server503Left), 
		    			getString(R.string.dialog_Server503Right));
			break;
			
			default:
				BottomDialog.BottomSelectDialog(Out_PasswRestore_0.this, 
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
		// TODO Auto-generated method stub
		if( loader != null && loader.isShowing() )
			loader.dismiss();
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
	            	Out_PasswRestore_0.this.finishAffinity();
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
	            	Out_PasswRestore_0.this.finishAffinity();
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
		tracker.setScreenName("restore_pessword_one");
		tracker.send(new HitBuilders.EventBuilder().build());
	}
	
}
