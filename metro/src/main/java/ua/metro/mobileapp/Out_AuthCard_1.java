package ua.metro.mobileapp;

import ua.metro.mobileapp.UI.OnSingleClickListener;
import ua.metro.mobileapp.application.MeApp;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class Out_AuthCard_1 extends Activity {
	ImageButton iconBackImageButton;
	Button confirmTwoButton;
	TextView confirmTextViewTwo;
	EditText cardOwnerPhoneEditText;
	Typeface clearSans, clearSansMedium, mediumRoboto;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.out_auth_sent_two);
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

		// Definition
		confirmTextViewTwo = (TextView) findViewById(R.id.confirmTextViewTwo);
		// TextView yesMyPhoneTextView =
		// (TextView)new_view.findViewById(R.id.yesMyPhoneTextView);
		confirmTwoButton = (Button) findViewById(R.id.confirmTwoButton);
		cardOwnerPhoneEditText = (EditText) findViewById(R.id.cardOwnerPhoneEditText);
		// Small fucking buttons
		final ImageButton phoneEditButton = (ImageButton) findViewById(R.id.phoneEditButton);
		final ImageButton phoneSaveButton = (ImageButton) findViewById(R.id.phoneSaveButton);
		final ImageButton phoneDontSaveButton = (ImageButton) findViewById(R.id.phoneDontSaveButton);
		// Set fonts
		confirmTextViewTwo.setTypeface(clearSans);

		confirmTwoButton.setTypeface(clearSansMedium);
		cardOwnerPhoneEditText.setTypeface(clearSans);

		cardOwnerPhoneEditText.setTextColor(getResources().getColor(
				R.color.metro_black_text));
		// Restriction to edit EditText
		cardOwnerPhoneEditText.setEnabled(false);

		// Set EditText data
		cardOwnerPhoneEditText.setText(MeApp.regData.getPhone());

		// Set OnClickListeners
		confirmTwoButton.setOnClickListener(new OnSingleClickListener() {
			@Override
			public void onSingleClick(View v) {
				// TODO Go to Back to Login Screen
				// First check correctness of entered data

				if (Utils.phoneCheck(MeApp.regData.getPhone())) {
					confirmTextViewTwo
							.setText(getString(R.string.login_phoneconfirm));
					confirmTextViewTwo.setTextColor(getResources().getColor(
							R.color.metro_black_text));
					startActivity(new Intent(Out_AuthCard_1.this,
							Out_AuthCard_2.class));

				} else {
					confirmTextViewTwo
							.setText(getString(R.string.question_errorPhone));
					confirmTextViewTwo.setTextColor(getResources().getColor(
							R.color.metro_red));
				}

			}
		});

		// Set selector change button alpha
		confirmTwoButton.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					confirmTwoButton.setAlpha((float) 0.55);
					return false;
				case MotionEvent.ACTION_UP:
					confirmTwoButton.setAlpha((float) 1.0);
					return false;
				default:
					return false;
				}
			}
		});

		// Small fucking buttons
		phoneEditButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Go to PhoneNumberTextFokus & Show Save&DontSave Buttons
				phoneEditButton.setVisibility(View.GONE);
				phoneSaveButton.setVisibility(View.VISIBLE);
				phoneDontSaveButton.setVisibility(View.VISIBLE);
				cardOwnerPhoneEditText.setEnabled(true);
				cardOwnerPhoneEditText.requestFocus();
			}
		});
		phoneSaveButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Save entered phone number
				if (Utils.phoneCheck(cardOwnerPhoneEditText.getText()
						.toString())) {
					phoneEditButton.setVisibility(View.VISIBLE);
					phoneSaveButton.setVisibility(View.GONE);
					phoneDontSaveButton.setVisibility(View.GONE);

					MeApp.regData.setPhone(cardOwnerPhoneEditText.getText()
							.toString());
					cardOwnerPhoneEditText.setEnabled(false);
					Log.v("",
							"1111 Phone number ok=" + MeApp.regData.getPhone());
				} else {
					confirmTextViewTwo
							.setText(getString(R.string.question_errorPhone));
					confirmTextViewTwo.setTextColor(getResources().getColor(
							R.color.metro_red));
					Log.v("",
							"1111 Phone number WRONG"
									+ MeApp.regData.getPhone());
				}
			}
		});
		phoneDontSaveButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Revert back to phone number from API
				confirmTextViewTwo
						.setText(getString(R.string.login_phoneconfirm));
				confirmTextViewTwo.setTextColor(getResources().getColor(
						R.color.metro_black_text));

				phoneEditButton.setVisibility(View.VISIBLE);
				phoneSaveButton.setVisibility(View.GONE);
				phoneDontSaveButton.setVisibility(View.GONE);

				cardOwnerPhoneEditText.setText(MeApp.regData.getPhone());
				cardOwnerPhoneEditText.setEnabled(false);
			}
		});
		cardOwnerPhoneEditText.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (cardOwnerPhoneEditText.getText().toString() == null
						|| cardOwnerPhoneEditText.getText().toString()
								.equals(""))
					cardOwnerPhoneEditText.setText("380");
				return false;
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
		tracker.setScreenName("auth_confirm_two");
		tracker.send(new HitBuilders.EventBuilder().build());
	}

}
