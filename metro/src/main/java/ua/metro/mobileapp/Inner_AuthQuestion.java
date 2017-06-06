package ua.metro.mobileapp;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.HashMap;

import ua.metro.mobileapp.HttpMetods.HTTPAsynkTask;
import ua.metro.mobileapp.UI.BottomDialog;
import ua.metro.mobileapp.UI.OnSingleClickListener;
import ua.metro.mobileapp.application.MeApp;

public class Inner_AuthQuestion extends Activity implements OnTaskCompleted {
	
	Typeface fontRegular, fontMedium;
	AlertDialog.Builder builderMessage;
	ImageButton iconBackImageButton;
	EditText messageEditText, emailEditText, phoneEditText;
	TextView error_TextView;
	
	RelativeLayout layout_WithFade, layout_ToFade;
	ProgressDialog loader;
	View subHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.inner_auth_question);
        
        Utils.showActionBar(this, true);
        Utils.setBottomMenu(this,MeApp.menuSelected);
        
        fontRegular = FontCache.get("fonts/clearsansregular.ttf", getBaseContext());
        fontMedium = FontCache.get("fonts/clearsansmedium.ttf", getBaseContext());
        
        subHeader = (View) findViewById(R.id.subheader_layout);
        //BackButton
        iconBackImageButton = (ImageButton) subHeader.findViewById(R.id.iconBackImageButton);
        iconBackImageButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Back Button
				exit_back();
			}	            	
        });
        TextView subTitleTextView = (TextView) subHeader.findViewById(R.id.subTitleTextView);
        subTitleTextView.setTypeface(fontMedium);
        subTitleTextView.setText(getString(R.string.inner_sendquetion));
        
               
        TextView requiredFieldTextView = (TextView) findViewById(R.id.requiredFieldTextView);
        error_TextView = (TextView) findViewById(R.id.error_TextView);
        messageEditText = (EditText) findViewById(R.id.messageEditText);
        emailEditText = (EditText) findViewById(R.id.emailEditText);
        phoneEditText = (EditText) findViewById(R.id.phoneEditText);
        Button sendButton = (Button) findViewById(R.id.sendButton);
        
        requiredFieldTextView.setTypeface(fontRegular);
        emailEditText.setTypeface(fontRegular);
        phoneEditText.setTypeface(fontRegular);
        messageEditText.setTypeface(fontRegular);
        sendButton.setTypeface(fontMedium);
        
        //Clear red before        
        ((GradientDrawable) messageEditText.getBackground()).setStroke(1, Color.WHITE);
        ((GradientDrawable) emailEditText.getBackground()).setStroke(1, Color.WHITE);
        ((GradientDrawable) phoneEditText.getBackground()).setStroke(1, Color.WHITE);
        
        phoneEditText.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(phoneEditText.getText().toString() == null
						|| phoneEditText.getText().toString().equals(""))
				phoneEditText.setText("380");
				return false;
			}    	
        });
        
        
        sendButton.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
            	//TODO Get all data & send`em to hell
            	boolean text_ok = false, email_ok = false, phone_ok = false;
            	if (messageEditText.getText().toString().length() > 0){
            		text_ok = true;
            		GradientDrawable back_phone = (GradientDrawable)messageEditText.getBackground();
        			back_phone.setStroke(1, Color.WHITE);
            	} else {
            		GradientDrawable back_phone = (GradientDrawable)messageEditText.getBackground();
        			back_phone.setStroke(1, Color.RED);
            	}
            	if (Utils.isEmailCorrect(emailEditText.getText().toString())){
            		email_ok = true;
            		GradientDrawable back_phone = (GradientDrawable)emailEditText.getBackground();
        			back_phone.setStroke(1, Color.WHITE);
            	} else {
            		GradientDrawable back_phone = (GradientDrawable)emailEditText.getBackground();
        			back_phone.setStroke(1, Color.RED);
            	}
            	if (Utils.phoneCheck(phoneEditText.getText().toString())) {
            		phone_ok = true;
            		GradientDrawable back_phone = (GradientDrawable)phoneEditText.getBackground();
        			back_phone.setStroke(1, Color.WHITE);
            	}else {          		
            		GradientDrawable back_phone = (GradientDrawable)phoneEditText.getBackground();
        			back_phone.setStroke(1, Color.RED);
            	}
            	
            	if(text_ok&email_ok&phone_ok) {
            		error_TextView.setVisibility(View.GONE);
            		
	            	HashMap<String,String> params = new HashMap<String,String>();
	            	params.put("form[message]", messageEditText.getText().toString());
					params.put("form[email]", emailEditText.getText().toString());
					params.put("form[phone]", phoneEditText.getText().toString());
					
					loader = new ProgressDialog(Inner_AuthQuestion.this);
					loader.setMessage(getString(R.string.app_Loading));
					loader.show();
					
					HTTPAsynkTask mt = new HTTPAsynkTask(Inner_AuthQuestion.this,
							GlobalConstants.FEEDBACK, 
							//null, null, 
							params, 
							Inner_AuthQuestion.this);
					mt.execute();
            	} else {
            		error_TextView.setVisibility(View.VISIBLE);
            		error_TextView.setText(getString(R.string.reg_regError));
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
		case GlobalConstants.FEEDBACK:
			if(MeApp.regData.isSuccess()){
				BottomDialog.BottomMessageDialog(Inner_AuthQuestion.this, messageSent, 
						getString(R.string.question_sendthanks), 
						getString(R.string.question_sendthankstext));
				//slideDialog();
			} else {
				int error_code = MeApp.regData.getCode();
				switch(error_code){
				case 801:
					Toast.makeText(Inner_AuthQuestion.this, 
							getString(R.string.Error_801), 
							Toast.LENGTH_LONG).show();
					break;
				case 806:
					Toast.makeText(Inner_AuthQuestion.this, 
							getString(R.string.Error_806), 
							Toast.LENGTH_LONG).show();				
					break;
				case 800:
					Toast.makeText(Inner_AuthQuestion.this, 
							getString(R.string.Error_800), 
							Toast.LENGTH_LONG).show();			
					break;
				default:
					Toast.makeText(Inner_AuthQuestion.this, 
							getString(R.string.Error_operationUnSuccess), 
							Toast.LENGTH_LONG).show();
					break;				
				}		
			}		
			break;
			
			//TODO ============ERRORS DEPLOYMENT==========
			case GlobalConstants.NO_INTERNET_CONNECTION:
				BottomDialog.BottomSelectDialog(Inner_AuthQuestion.this, 
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
	        	startActivity(new Intent(Inner_AuthQuestion.this, Out_RegInStart.class)); 
				finish();
				break;
			
			case GlobalConstants.ERROR_503:
				BottomDialog.BottomSelectDialog(Inner_AuthQuestion.this, 
						error503, 
		    			getString(R.string.dialog_Server503Title), 
		    			getString(R.string.dialog_Server503Text), 
		    			getString(R.string.dialog_Server503Left), 
		    			getString(R.string.dialog_Server503Right));
			break;
			
			default:
				BottomDialog.BottomSelectDialog(Inner_AuthQuestion.this, 
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
	            	Inner_AuthQuestion.this.finishAffinity();
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
	            	Inner_AuthQuestion.this.finishAffinity();
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
	
	Handler messageSent = new Handler() {

	    @Override
	    public void handleMessage(Message msg) {
	        if (msg != null) {
	            // Check for dialog box responses
	        	finish();
	        }
	    }
	};

	private void setAnalytics() {
		GoogleAnalytics analytics;
		final Tracker tracker;
		analytics = GoogleAnalytics.getInstance(this);
		analytics.setLocalDispatchPeriod(1800);
		tracker = analytics.newTracker(getString(R.string.app_GA_CODE));
		tracker.enableExceptionReporting(true);
		tracker.enableAdvertisingIdCollection(true);
		tracker.enableAutoActivityTracking(true);
		tracker.setScreenName("question");
		tracker.send(new HitBuilders.EventBuilder().build());
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		exit_back();
	}

	private void exit_back(){
		if(MeApp.menuSelected == 1) {
			startActivity(new Intent(Inner_AuthQuestion.this, Inner_Profile.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
		} else {
			startActivity(new Intent(Inner_AuthQuestion.this, Inner_Map.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
		}
		finish();
	}
	
}
