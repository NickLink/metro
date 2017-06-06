package ua.metro.mobileapp;

import ua.metro.mobileapp.application.MeApp;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class Out_AuthCard_0 extends Activity {
	ImageButton iconBackImageButton;
	Typeface clearSans, clearSansMedium, mediumRoboto;
	EditText cardOwnerNameEditText, cardOwnerEmailEditText;
	TextView confirmTextViewOne;
	ImageButton emailEditButtonOne, emailSaveButtonOne, emailDontSaveButtonOne;
	Button confirmOneButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.out_auth_sent_one);
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
		confirmTextViewOne = (TextView) findViewById(R.id.confirmTextViewOne);
		cardOwnerNameEditText = (EditText) findViewById(R.id.cardOwnerNameEditText);
		cardOwnerEmailEditText = (EditText) findViewById(R.id.cardOwnerEmailEditText);
		// Small fucking buttons
		emailEditButtonOne = (ImageButton) findViewById(R.id.emailEditButton);
		emailSaveButtonOne = (ImageButton) findViewById(R.id.emailSaveButton);
		emailDontSaveButtonOne = (ImageButton) findViewById(R.id.emailDontSaveButton);

		cardOwnerNameEditText.setTextColor(getResources().getColor(
				R.color.metro_black_text));
		cardOwnerEmailEditText.setTextColor(getResources().getColor(
				R.color.metro_black_text));
		// Restriction to edit EditText
		cardOwnerNameEditText.setEnabled(false);
		cardOwnerEmailEditText.setEnabled(false);

		// Set EditText data
		cardOwnerNameEditText.setText(MeApp.regData.getName());
		if(MeApp.regData.getEmail() != null && MeApp.regData.getEmail().equals("null")){
			cardOwnerEmailEditText.setHint(getString(R.string.login_emaileditHint));
		} else {
			cardOwnerEmailEditText.setText(MeApp.regData.getEmail());
		}

		confirmOneButton = (Button) findViewById(R.id.confirmOneButton);
		// Set fonts
		confirmTextViewOne.setTypeface(clearSans);
		confirmOneButton.setTypeface(clearSansMedium);
		cardOwnerNameEditText.setTypeface(clearSans);
		cardOwnerEmailEditText.setTypeface(clearSans);

		// Set OnClickListeners
		confirmOneButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Go to confirm phone page
				startActivity(new Intent(Out_AuthCard_0.this,
						Out_AuthCard_1.class));
			}
		});

		// Small fucking buttons
		emailEditButtonOne.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Go to EmailEditTextFokus & Show Save&DontSave Buttons
				emailEditButtonOne.setVisibility(View.GONE);
				emailSaveButtonOne.setVisibility(View.VISIBLE);
				emailDontSaveButtonOne.setVisibility(View.VISIBLE);
				cardOwnerEmailEditText.setEnabled(true);
				cardOwnerEmailEditText.requestFocus();
			}
		});
		emailSaveButtonOne.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Save entered Email
				if (Utils.isEmailCorrect(cardOwnerEmailEditText.getText()
						.toString())) {
					confirmTextViewOne
							.setText(getString(R.string.login_cardautorized));
					confirmTextViewOne.setTextColor(getResources().getColor(
							R.color.metro_black_text));

					emailEditButtonOne.setVisibility(View.VISIBLE);
					emailSaveButtonOne.setVisibility(View.GONE);
					emailDontSaveButtonOne.setVisibility(View.GONE);

					MeApp.regData.setEmail(cardOwnerEmailEditText.getText()
							.toString());
					cardOwnerEmailEditText.setEnabled(false);

				} else {
					confirmTextViewOne
							.setText(getString(R.string.question_errorEmail));
					confirmTextViewOne.setTextColor(getResources().getColor(
							R.color.metro_red));
				}

			}
		});
		emailDontSaveButtonOne.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Revert back to Email from API
				confirmTextViewOne
						.setText(getString(R.string.login_cardautorized));
				confirmTextViewOne.setTextColor(getResources().getColor(
						R.color.metro_black_text));

				emailEditButtonOne.setVisibility(View.VISIBLE);
				emailSaveButtonOne.setVisibility(View.GONE);
				emailDontSaveButtonOne.setVisibility(View.GONE);

				cardOwnerEmailEditText.setText(MeApp.regData.getEmail());
				cardOwnerEmailEditText.setEnabled(false);
			}
		});

		// Set selector change button alpha
		confirmOneButton.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					confirmOneButton.setAlpha((float) 0.55);
					return false;
				case MotionEvent.ACTION_UP:
					confirmOneButton.setAlpha((float) 1.0);
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
		tracker.setScreenName("auth_confirm_one");
		tracker.send(new HitBuilders.EventBuilder().build());
	}

}
