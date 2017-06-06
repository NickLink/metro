package ua.metro.mobileapp;

import ua.metro.mobileapp.UI.OnSingleClickListener;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class Out_PasswRestore_2 extends Activity{
	Typeface clearSans, clearSansMedium, mediumRoboto;
	Button passw_okButton;
	ImageButton iconBackImageButton;
	EditText passw_phone_show;
	String showPhoneString;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.out_passw_restore_two);
		
		showPhoneString = getIntent().getStringExtra("phone");
		
		clearSans = FontCache.get("fonts/clearsansregular.ttf", getBaseContext());
		clearSansMedium = FontCache.get("fonts/clearsansmedium.ttf", getBaseContext());
		mediumRoboto = FontCache.get("fonts/robotomedium.ttf", getBaseContext());		
		
		TextView restore_PasswText = (TextView)findViewById(R.id.restore_PasswText);
		TextView subTitleTextView = (TextView)findViewById(R.id.subTitleTextView);
		passw_phone_show = (EditText)findViewById(R.id.passw_phone_show);
		passw_okButton = (Button) findViewById(R.id.passw_okButton);
		iconBackImageButton = (ImageButton) findViewById(R.id.iconBackImageButton);
		iconBackImageButton.setVisibility(View.GONE);
		
		restore_PasswText.setTypeface(clearSans);
		subTitleTextView.setTypeface(clearSansMedium);
		passw_okButton.setTypeface(clearSansMedium);
		
		passw_phone_show.setEnabled(false);
		passw_phone_show.setText(showPhoneString);
		
		passw_okButton.setOnClickListener(new OnSingleClickListener(){
			@Override
			public void onSingleClick(View v) {
				//Save phone number
				SharedPreferences sPref = getSharedPreferences(getString(R.string.app_PREFNAME), MODE_PRIVATE);
				sPref.edit().putString(getString(R.string.app_SAVED_PHONE), showPhoneString).apply();
				
				startActivity(new Intent(Out_PasswRestore_2.this, Out_RegInStart.class)
				.putExtra("phone", showPhoneString)
				.putExtra("page", 1));
				finish();				
			}	            	
        });
		passw_okButton.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                	passw_okButton.setAlpha((float) 0.55);
                    return false;
                case MotionEvent.ACTION_UP:
                	passw_okButton.setAlpha((float) 1.0);
                    return false;
                default:
                    return false;
                }
            }
      	});  
		
		setAnalytics();
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
		tracker.setScreenName("restore_password_three");
		tracker.send(new HitBuilders.EventBuilder().build());
	}

}
