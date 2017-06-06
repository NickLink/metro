package ua.metro.mobileapp;

import ua.metro.mobileapp.HttpMetods.UserState;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class Inner_ProfileAppRules extends Activity {
	Typeface clearSans, clearSansMedium, clearSansBold;
	static Typeface clearSansLight;
	
	private long mLastClickTime = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		UserState.restoreUserState(this);
		setContentView(R.layout.inner_profile_apprules);
		
		clearSans = FontCache.get("fonts/clearsans.ttf", getBaseContext());
        clearSansBold = FontCache.get("fonts/clearsansbold.ttf", getBaseContext());
        clearSansMedium = FontCache.get("fonts/clearsansmedium.ttf", getBaseContext());
        clearSansLight = FontCache.get("fonts/clearsanslight.ttf", getBaseContext());
		
		Utils.showActionBar(this, false);
        //Utils.setBottomMenu(this,1);
        
        TextView appRulesText = (TextView) findViewById(R.id.appRulesText);
        appRulesText.setTypeface(clearSans);
        
		int i = getIntent().getIntExtra("page", 0);
		if(i == 1){
			showsubHeader(getString(R.string.app_getPermamentSubHeader));
			appRulesText.setText(getString(R.string.app_getPermamentText));
			
		} else {
			showsubHeader(getString(R.string.app_RulesSubHeader));
			appRulesText.setText(getString(R.string.app_rulesAgreement));
			
		}
        
        setAnalytics();
	}
	
	protected void onPause() {
        super.onPause();
        UserState.saveUserState(this);
	}
	
	protected void onResume() {
        super.onResume();
        UserState.restoreUserState(this);
	}
	
	void showsubHeader(String subheaderTitle){
		
		// Back button
		View subHeader;
		ImageButton iconBackImageButton;
		TextView subTitleTextView;
		subHeader = (View) findViewById(R.id.subHeaderLayout);
		iconBackImageButton = (ImageButton) subHeader.findViewById(R.id.iconBackImageButton);
		subTitleTextView = (TextView) subHeader.findViewById(R.id.subTitleTextView);
		subTitleTextView.setText(subheaderTitle);
		subTitleTextView.setTypeface(clearSansMedium);
		
		iconBackImageButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (SystemClock.elapsedRealtime() - mLastClickTime < 500) {
					return;
				}
				mLastClickTime = SystemClock.elapsedRealtime();
				// TODO Back Button
				finish();
			}
		});
		
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
		tracker.setScreenName("profile_rules");
		tracker.send(new HitBuilders.EventBuilder().build());
	}

}
