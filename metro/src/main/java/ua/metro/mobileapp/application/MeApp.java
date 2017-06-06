package ua.metro.mobileapp.application;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import ua.metro.mobileapp.datamodel.RegData;
import ua.metro.mobileapp.datamodel.RegFizData;
import ua.metro.mobileapp.datamodel.RegYurData;

public class MeApp extends Application{
	public static RegData regData;
	public static RegFizData regFizData;
	public static RegYurData regYurData;
	public static int menuSelected;
	
	GoogleAnalytics analytics;
	Tracker tracker;
	
	@Override
	public void onCreate() {
		super.onCreate();
		regData = new RegData();
		regFizData = new RegFizData();
		regYurData = new RegYurData();
		menuSelected = 1;

		FacebookSdk.sdkInitialize(getApplicationContext());
	    AppEventsLogger.activateApp(this);
	}

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MultiDex.install(this);
	}

}
