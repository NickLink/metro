package ua.metro.mobileapp;

import java.util.HashMap;

import ua.metro.mobileapp.HttpMetods.HTTPAsynkTask;
import ua.metro.mobileapp.UI.BottomDialog;
import ua.metro.mobileapp.UI.NonSwipeableViewPager;
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
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class Out_AuthCard_2 extends Activity implements OnTaskCompleted {

	Typeface clearSans, clearSansMedium, mediumRoboto;
	Button confirmOneButton, confirmTwoButton;
	//NonSwipeableViewPager authsend_Pager;
	ImageButton iconBackImageButton;
	ProgressDialog loader;
	TextView confirmTextViewOne, confirmTextViewTwo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.out_auth_sent_three);

		// BackButton
		iconBackImageButton = (ImageButton) findViewById(R.id.iconBackImageButton);
		iconBackImageButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Back Button
				finish();
			}
		});
		clearSans = FontCache.get("fonts/clearsansregular.ttf",
				getBaseContext());
		clearSansMedium = FontCache.get("fonts/clearsansmedium.ttf",
				getBaseContext());
		mediumRoboto = FontCache
				.get("fonts/robotomedium.ttf", getBaseContext());

		TextView subTitleTextView = (TextView)findViewById(R.id.subTitleTextView);
		subTitleTextView.setText(getString(R.string.login_authorization));
		subTitleTextView.setTypeface(clearSansMedium);

		
		TextView confirmTextViewThree = (TextView)findViewById(R.id.confirmTextViewThree);
		TextView cardOwnerPhoneText = (TextView)findViewById(R.id.cardOwnerPhoneText);
		TextView error_in_phone = (TextView)findViewById(R.id.error_in_phone);

		Button login_okBig = (Button)findViewById(R.id.login_okBig);
		Button on_previous_screen = (Button)findViewById(R.id.on_previous_screen);
		
		Log.v("", "1111 Phone number page3 =" + MeApp.regData.getPhone());
		cardOwnerPhoneText.setText(MeApp.regData.getPhone());

		// Set fonts
		confirmTextViewThree.setTypeface(clearSans);
		cardOwnerPhoneText.setTypeface(clearSans);
		error_in_phone.setTypeface(clearSans);
		
		login_okBig.setTypeface(clearSansMedium);
		on_previous_screen.setTypeface(clearSansMedium);
		
		login_okBig.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Send API to POST RegisterCard
				HashMap<String, String> params = new HashMap<String, String>();
				params.put("phone", MeApp.regData.getPhone());
				params.put("name", MeApp.regData.getName());
				params.put("email", MeApp.regData.getEmail());
				params.put("card", MeApp.regData.getCard_number());

				loader = new ProgressDialog(Out_AuthCard_2.this);
				loader.setMessage(getString(R.string.app_Loading));
				loader.show();

				HTTPAsynkTask mt = new HTTPAsynkTask(Out_AuthCard_2.this,
						GlobalConstants.REGISTER_CARD,
						// null, null,
						params, Out_AuthCard_2.this);
				mt.execute();
			}
		});
		
		on_previous_screen.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		setAnalytics();
	}


	@Override
	public void onTaskCompleted(int id) {

		if (loader != null && loader.isShowing())
			loader.dismiss();

		switch (id) {
		case GlobalConstants.REGISTER_CARD:
			if (MeApp.regData.isSuccess()) {
				Toast.makeText(Out_AuthCard_2.this,
						getString(R.string.Error_operationSuccess),
						Toast.LENGTH_LONG).show();
				// Save phone number
				SharedPreferences sPref = getSharedPreferences(
						getString(R.string.app_PREFNAME), MODE_PRIVATE);
				sPref.edit()
						.putString(getString(R.string.app_SAVED_PHONE),
								MeApp.regData.getPhone()).apply();

				startActivity(new Intent(Out_AuthCard_2.this,
						Out_RegInStart.class).putExtra("page", 1).putExtra(
						"phone", MeApp.regData.getPhone()));
				finish();

			} else {
				int error_code = MeApp.regData.getCode();
				switch (error_code) {
				case 801:
					Toast.makeText(Out_AuthCard_2.this,
							getString(R.string.Error_801), Toast.LENGTH_LONG)
							.show();
					break;
				case 806:
					Toast.makeText(Out_AuthCard_2.this,
							getString(R.string.Error_806), Toast.LENGTH_LONG)
							.show();
					break;
				case 800:
					Toast.makeText(Out_AuthCard_2.this,
							getString(R.string.Error_800), Toast.LENGTH_LONG)
							.show();
					break;
				}
			}
			break;

		// TODO ============ERRORS DEPLOYMENT==========
		case GlobalConstants.NO_INTERNET_CONNECTION:
			BottomDialog.BottomSelectDialog(Out_AuthCard_2.this,
					noInternetConn, getString(R.string.dialog_NoInternetTitle),
					getString(R.string.dialog_NoInternetText),
					getString(R.string.dialog_NoInternetSettings),
					getString(R.string.dialog_NoInternetQuit));
			break;

		case GlobalConstants.ERROR_401:
			Log.v("TAG", "METRO OUTStart id_request=ERROR_401");
			SharedPreferences sPref = getSharedPreferences(
					getString(R.string.app_PREFNAME), MODE_PRIVATE);
			sPref.edit().remove(getString(R.string.app_TOKEN)).apply();
			startActivity(new Intent(Out_AuthCard_2.this, Out_RegInStart.class));
			finish();
			break;

		case GlobalConstants.ERROR_503:
			BottomDialog.BottomSelectDialog(Out_AuthCard_2.this, error503,
					getString(R.string.dialog_Server503Title),
					getString(R.string.dialog_Server503Text),
					getString(R.string.dialog_Server503Left),
					getString(R.string.dialog_Server503Right));
			break;

		default:
			BottomDialog.BottomSelectDialog(Out_AuthCard_2.this, error503,
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
					Intent viewIntent = new Intent(
							Settings.ACTION_WIFI_SETTINGS);
					startActivityForResult(viewIntent,
							GlobalConstants.REQUEST_CODE_INET);
				} else if (msg.what == GlobalConstants.retCodeRight) {
					Out_AuthCard_2.this.finishAffinity();
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
					Out_AuthCard_2.this.finishAffinity();
				} else if (msg.what == GlobalConstants.retCodeClose) {

				}
			}
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == GlobalConstants.REQUEST_CODE_INET) {
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
		tracker.setScreenName("auth_confirm_three");
		tracker.send(new HitBuilders.EventBuilder().build());
	}

}
