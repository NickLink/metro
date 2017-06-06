package ua.metro.mobileapp;

import ua.metro.mobileapp.UI.OnSingleClickListener;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;

public class Out_Register_Common_1 extends Activity{
	
	Typeface clearSans, clearSansMedium;
	ImageButton iconBackImageButton;
	Button reg_fizOsoba, reg_UrOsobaSPD;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.out_registration_common_1);
        
        clearSansMedium = FontCache.get("fonts/clearsansmedium.ttf", getBaseContext());
        clearSans = FontCache.get("fonts/clearsans.ttf", getBaseContext());
        
        
        reg_fizOsoba = (Button) findViewById(R.id.reg_fizOsoba);
        reg_UrOsobaSPD = (Button) findViewById(R.id.reg_UrOsobaSPD);
        
        iconBackImageButton = (ImageButton) findViewById(R.id.iconBackImageButton);
        iconBackImageButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Back Button
				finish();				
			}	            	
        });
        
        //TODO go to register FIZ_OSOBA
        reg_fizOsoba.setOnClickListener(new OnSingleClickListener(){
			@Override
			public void onSingleClick(View v) {
				startActivity(new Intent(Out_Register_Common_1.this, Out_Register_Fiz_1.class));		
			}	            	
        });
      //TODO go to register UR_OSOBA
        reg_UrOsobaSPD.setOnClickListener(new OnSingleClickListener(){
			@Override
			public void onSingleClick(View v) {
				startActivity(new Intent(Out_Register_Common_1.this, Out_Register_Yur_1_3.class));		
			}	            	
        });
        
        
        
        
        reg_fizOsoba.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                	reg_fizOsoba.setAlpha((float) 0.55);
                    return false;
                case MotionEvent.ACTION_UP:
                	reg_fizOsoba.setAlpha((float) 1.0);
                    return false;
                default:
                    return false;
                }
            }
      	}); 
        reg_UrOsobaSPD.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                	reg_UrOsobaSPD.setAlpha((float) 0.55);
                    return false;
                case MotionEvent.ACTION_UP:
                	reg_UrOsobaSPD.setAlpha((float) 1.0);
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
		tracker.setScreenName("register_common_two");
		tracker.send(new HitBuilders.EventBuilder().build());
	}
	
}
