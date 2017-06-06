package ua.metro.mobileapp;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import ua.metro.mobileapp.HttpMetods.HTTPAsynkTask;
import ua.metro.mobileapp.HttpMetods.UserState;
import ua.metro.mobileapp.UI.BottomDialog;
import ua.metro.mobileapp.UI.OnSingleClickListener;
import ua.metro.mobileapp.application.MeApp;
import ua.metro.mobileapp.datamodel.ProfileData;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class Inner_Profile extends Activity implements OnTaskCompleted {
	
	Typeface clearSans, clearSansMedium, clearSansBold;
	static Typeface clearSansLight;
	View subHeader;
	ImageButton iconBackImageButton;
	ImageButton nameEditButton, nameSaveButton, nameDontSaveButton, 
				emailEditButton, emailSaveButton, emailDontSaveButton; //,
//				fopEditButton, fopSaveButton, fopDontSaveButton;
	//private long mLastClickTime = 0;
	ProfileData profile = new ProfileData();
	EditText cardOwnerNameEditText, cardOwnerEmailEditText; //, cardOwnerFopEditText;
	TextView cardOwnerPhone, buttonRegion;
	ArrayList<String> regionTitle;
	ArrayList<Integer> regionID;
	Spinner spinnerRegion;
	//TextView buttonRegion;
	int spiner_textSize = 14;
	ProgressDialog loader;
	Button switch_UA, switch_RU;
	SharedPreferences sPref;
	StyleSpan region_text1, region_text2;
	RelativeLayout cardOwnerNameEditTextRL, cardOwnerEmailEditTextRL; //, cardOwnerFopEditTextRL;
	
	Configuration config;
	
	@Override
	public void onBackPressed() {
		finish();
	}
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        UserState.restoreUserState(this);
        
        setContentView(R.layout.inner_profile);
        
        loader = new ProgressDialog(Inner_Profile.this);
		loader.setMessage(getString(R.string.app_Loading));
		loader.show();
		
        HTTPAsynkTask mt = new HTTPAsynkTask(Inner_Profile.this, 
				GlobalConstants.GET_PROFILE_INFO, 
				//null, null, 
				null, 
				Inner_Profile.this);
		mt.execute();
		
		sPref = getSharedPreferences(getString(R.string.app_PREFNAME), MODE_PRIVATE);
        
        clearSans = FontCache.get("fonts/clearsans.ttf", getBaseContext());
        clearSansBold = FontCache.get("fonts/clearsansbold.ttf", getBaseContext());
        clearSansMedium = FontCache.get("fonts/clearsansmedium.ttf", getBaseContext());
        clearSansLight = FontCache.get("fonts/clearsanslight.ttf", getBaseContext());
        
        Utils.showActionBar(this, true);
		MeApp.menuSelected = 1;
        Utils.setBottomMenu(this,MeApp.menuSelected);
        
        config = getResources().getConfiguration();
        if (config.smallestScreenWidthDp >= 720) {
		    // sw720dp
        	spiner_textSize = 18;
		  }
		  else if (config.smallestScreenWidthDp >= 600) {
		    // sw600dp
			  spiner_textSize = 17;;
		  }
		  else if (config.smallestScreenWidthDp >= 480) {
			    // sw600dp
			  spiner_textSize = 16;
		  }
		  else {
		    // all other
			  spiner_textSize = 14;		  
		  }
        
        switch_UA = (Button) findViewById(R.id.switch_UA);
		switch_RU = (Button) findViewById(R.id.switch_RU);
        
//        showsubHeader(getString(R.string.my_kard));
        
        showBarCode(MeApp.regData.getUser().getCard_num());
		        
//        addPermanentCard();
        
        editProfileDataFields();
        
        //RelativeLayout slide_switch = (RelativeLayout) findViewById(R.id.slide_switch);
        switch_UA.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(profile.getLocale().equals("uk")){
					/*setLocal("ru");
					recreate();
					profile.setLocale("ru");
					setLocalSwitch("ru");*/
				} else if (profile.getLocale().equals("ru")){
					setLocal("");
					recreate();
					profile.setLocale("uk");
					setLocalSwitch("uk");					
				}								
				
				//loader = new ProgressDialog(Inner_Profile.this);
				//loader.setMessage(getString(R.string.app_Loading));
				//loader.show();
				
				HashMap<String,String> params = new HashMap<String,String>();
            	params.put("locale_name", profile.getLocale());
				HTTPAsynkTask mt = new HTTPAsynkTask(Inner_Profile.this, 
						GlobalConstants.POST_LOCALE, 
						//null, null, 
						params, 
						Inner_Profile.this);
				mt.execute();
				
				sPref.edit().putBoolean(getString(R.string.app_LANG_CHANGE), true).apply();
			}       	
        });
        
        switch_RU.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(profile.getLocale().equals("uk")){
					setLocal("ru");
					recreate();
					profile.setLocale("ru");
					setLocalSwitch("ru");
				} else if (profile.getLocale().equals("ru")){
					/*setLocal("");
					recreate();
					profile.setLocale("uk");
					setLocalSwitch("uk");*/					
				}								
				
				//loader = new ProgressDialog(Inner_Profile.this);
				//loader.setMessage(getString(R.string.app_Loading));
				//loader.show();
				
				HashMap<String,String> params = new HashMap<String,String>();
            	params.put("locale_name", profile.getLocale());
				HTTPAsynkTask mt = new HTTPAsynkTask(Inner_Profile.this, 
						GlobalConstants.POST_LOCALE, 
						//null, null, 
						params, 
						Inner_Profile.this);
				mt.execute();
				
				sPref.edit().putBoolean(getString(R.string.app_LANG_CHANGE), true).apply();
			}       	
        });
        
        //setRegion();
        
        setAppRules();
        
        setExitButton();
        
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
	
	@Override
    protected void onRestart() {
        super.onRestart();

        Utils.showActionBar(this, true);
    }
	
	@SuppressLint("NewApi")
	void setRegionSpinner(){
		
		buttonRegion = (TextView) findViewById(R.id.buttonRegion);
		spinnerRegion = (Spinner) findViewById(R.id.spinnerRegion);
		
		ArrayAdapter<String> Area_adapter = new ArrayAdapter<String>(this, R.layout.my_spinner_textview, 
				R.id.spinnerItem, regionTitle)
				{
			         public View getView(int position, View convertView, ViewGroup parent) {
			                 View v = super.getView(position, convertView, parent);
			                 TextView text = (TextView)v.findViewById(R.id.spinnerItem);
			                 text.setTypeface(clearSansBold);
			                 text.setTextSize(TypedValue.COMPLEX_UNIT_SP, spiner_textSize);
			                 text.setTextColor(getResources().getColor(R.color.metro_navy));			
			                 return v;
			         }
					
			         public View getDropDownView(int position,  View convertView,  ViewGroup parent) {
			                  View v =super.getDropDownView(position, convertView, parent);	
			                  TextView text = (TextView)v.findViewById(R.id.spinnerItem);
			                  text.setTypeface(clearSansLight);
			                  text.setTextSize(TypedValue.COMPLEX_UNIT_SP, spiner_textSize);
			                  text.setTextColor(getResources().getColor(R.color.metro_navy));
			                 v.setBackgroundColor(getResources().getColor(R.color.metro_gray));			
			                 return v;
			         }
				};
		Area_adapter.setDropDownViewResource(R.layout.my_spinner_textview);
		spinnerRegion.setDropDownWidth(android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		spinnerRegion.setAdapter(Area_adapter);
		
		//String RegionSet;
		SpannableStringBuilder region_text = null;
		if (profile.getRegion().getRegionId() != -1){	
			buttonRegion.setVisibility(View.GONE);
			spinnerRegion.setSelection(regionID.indexOf(profile.getRegion().getRegionId()));
			spinnerRegion.setVisibility(View.VISIBLE);
			Log.v("TAG", "METRO   REGID=" + profile.getRegion().getRegionId());
			//RegionSet = profile.getRegion().getRegionTitle(); //getString(R.string.your_Region) + " " + 
			//buttonRegion.setText(profile.getRegion().getRegionTitle());
			//buttonRegion.setTypeface(clearSansBold);
		} else {
			Log.v("TAG", "METRO   REGID=" + profile.getRegion().getRegionId());
			//RegionSet = getString(R.string.your_Region) + " " + getString(R.string.your_RegionNotSelected);
			region_text = new SpannableStringBuilder(getString(R.string.your_Region) + " " + getString(R.string.your_RegionNotSelected)); //RegionSet
			region_text1 = new StyleSpan(Typeface.NORMAL);
			region_text2 = new StyleSpan(Typeface.BOLD);
			region_text.setSpan(region_text1, 0, 11, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
			region_text.setSpan(region_text2, 12, region_text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
			buttonRegion.setText(region_text);
			Log.v("TAG", "METRO   buttonRegion=" + region_text);
			buttonRegion.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					buttonRegion.setVisibility(View.GONE);
					spinnerRegion.setVisibility(View.VISIBLE);
					spinerH.sendEmptyMessage(0);
					//spinnerRegion.performClick();
				}
			});
			
		}
	
		spinnerRegion.setOnItemSelectedListener(new OnItemSelectedListener(){
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				
				//loader = new ProgressDialog(Inner_Profile.this);
				//loader.setMessage(getString(R.string.app_Loading));
				//loader.show();
				//position
				HashMap<String,String> params = new HashMap<String,String>();
            	params.put("region_id", Integer.toString(regionID.get(position)));
				HTTPAsynkTask set_region = new HTTPAsynkTask(Inner_Profile.this, 
						GlobalConstants.POST_USER_REGION, 
						//null, null, 
						params, 
						Inner_Profile.this);
				set_region.execute();
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub			
			}	        	
		});
		
	}	
	
	Handler spinerH = new Handler(){
		@Override
	    public void handleMessage(Message msg) {
			spinnerRegion.performClick();
	        }
	};
	
	void setAppRules(){
		TextView appRules = (TextView) findViewById(R.id.appRules);
		appRules.setTypeface(clearSans);
		appRules.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO setAppRules
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.app_RulesURL)));
				startActivity(browserIntent);
				//startActivity(new Intent(Inner_Profile.this, Inner_ProfileAppRules.class));
			}		
		});
	}
	
	void setExitButton(){
		Button app_Exit = (Button) findViewById(R.id.app_Exit);
		app_Exit.setTextColor(getResources().getColor(R.color.metro_red));
		app_Exit.setTypeface(clearSansMedium);
		app_Exit.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				BottomDialog.BottomSelectDialog(Inner_Profile.this, 
						uiMsgHandler, 
						getString(R.string.dialog_ExitTitle), 
						null, 
						getString(R.string.dialog_ExitLeft), 
						getString(R.string.dialog_ExitRight));
			}			
		});
	}
	
	private Handler uiMsgHandler = new Handler() {

	    @Override
	    public void handleMessage(Message msg) {
	        if (msg != null) {
	            // Check for dialog box responses
	            if (msg.what == GlobalConstants.retCodeLeft) {

	            }
	            else if (msg.what == GlobalConstants.retCodeRight) {
	            	
	            	sPref.edit().remove(getString(R.string.app_TOKEN)).apply();
	            	sPref.edit().remove(getString(R.string.app_CARD_NUMBER)).apply();
	            	sPref.edit().remove(getString(R.string.app_TEMP_CARDEXPIRED)).apply();
	            	sPref.edit().putInt(getString(R.string.app_RESTARTED), 1).apply();
	            		            	
					Intent i = getBaseContext().getPackageManager()
							.getLaunchIntentForPackage(
									getBaseContext().getPackageName());
					i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(i);
					Inner_Profile.this.finishAffinity();			
	            }
	            else if (msg.what == GlobalConstants.retCodeClose) {
	            }
	        }
	    }
	};
	
	void setLocalSwitch(String locale){
		
		if(locale.equals("uk")){
			switch_UA.setBackgroundResource(R.drawable.bg_navy_height_26);
			switch_UA.setTextColor(Color.WHITE);
			switch_RU.setBackgroundResource(R.drawable.bg_grey_height_26);
			switch_RU.setTextColor(Color.BLACK);
		} else if (locale.equals("ru")){
			switch_RU.setBackgroundResource(R.drawable.bg_navy_height_26);
			switch_RU.setTextColor(Color.WHITE);
			switch_UA.setBackgroundResource(R.drawable.bg_grey_height_26);
			switch_UA.setTextColor(Color.BLACK);		
		} else {
			
		}
		
	}
	
	void editProfileDataFields(){
		//RL definitions 
		cardOwnerNameEditTextRL = (RelativeLayout)findViewById(R.id.cardOwnerNameEditTextRL);
		cardOwnerEmailEditTextRL = (RelativeLayout)findViewById(R.id.cardOwnerEmailEditTextRL);
//		cardOwnerFopEditTextRL = (RelativeLayout)findViewById(R.id.cardOwnerFopEditTextRL);
		//Name edit block
		cardOwnerNameEditText = (EditText) findViewById(R.id.cardOwnerNameEditText);
		nameEditButton = (ImageButton) findViewById(R.id.nameEditButton);
		nameSaveButton = (ImageButton) findViewById(R.id.nameSaveButton);
		nameDontSaveButton = (ImageButton) findViewById(R.id.nameDontSaveButton);
		cardOwnerNameEditText.setEnabled(false);
		cardOwnerNameEditText.setText(getString(R.string.app_Loading));
		cardOwnerNameEditText.setTextColor(getResources().getColor(R.color.metro_dark_gray));
		cardOwnerNameEditText.setTypeface(clearSansBold);
		
		//Phone show block
        cardOwnerPhone = (TextView) findViewById(R.id.cardOwnerPhone);
        cardOwnerPhone.setText(getString(R.string.app_Loading));
        cardOwnerPhone.setTextColor(getResources().getColor(R.color.metro_dark_gray));
        cardOwnerPhone.setTypeface(clearSans);
        
		//Email edit block
		cardOwnerEmailEditText = (EditText) findViewById(R.id.cardOwnerEmailEditText);
		emailEditButton = (ImageButton) findViewById(R.id.emailEditButton);
		emailSaveButton = (ImageButton) findViewById(R.id.emailSaveButton);
		emailDontSaveButton = (ImageButton) findViewById(R.id.emailDontSaveButton);
		cardOwnerEmailEditText.setEnabled(false);
		cardOwnerEmailEditText.setText(getString(R.string.app_Loading));
		cardOwnerEmailEditText.setTextColor(getResources().getColor(R.color.metro_dark_gray));
		cardOwnerEmailEditText.setTypeface(clearSans);
		
		//FOP edit block
//  		cardOwnerFopEditText = (EditText) findViewById(R.id.cardOwnerFopEditText);
//  		fopEditButton = (ImageButton) findViewById(R.id.fopEditButton);
//  		fopSaveButton = (ImageButton) findViewById(R.id.fopSaveButton);
//  		fopDontSaveButton = (ImageButton) findViewById(R.id.fopDontSaveButton);
//  		cardOwnerFopEditText.setEnabled(false);
//  		cardOwnerFopEditText.setText(getString(R.string.app_Loading));
//  		cardOwnerFopEditText.setTextColor(getResources().getColor(R.color.metro_dark_gray));
//  		cardOwnerFopEditText.setTypeface(clearSans);

	}
	
	void addPermanentCard(){
		final Button add_RegularCard = (Button) findViewById(R.id.add_RegularCard);
        if(MeApp.regData.getUser().isTemp_card()){
        	//Log.v("TAG", "PROFILE profile.isTemp_card()=" + profile.isTemp_card());
        	add_RegularCard.setTypeface(clearSansMedium);
        	add_RegularCard.setVisibility(View.VISIBLE);
        	add_RegularCard.setOnClickListener(new OnSingleClickListener(){
				@Override
				public void onSingleClick(View v) {
					// TODO ADD Regular Card
					startActivityForResult(new Intent(Inner_Profile.this, Out_ScanIn.class).putExtra("RUN_FROM", 1),
							GlobalConstants.REQUEST_CODE_SCAN);					
				}     		
        	});
        } else {
        	add_RegularCard.setVisibility(View.GONE);
        }
        add_RegularCard.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                	add_RegularCard.setAlpha((float) 0.55);
                    return false;
                case MotionEvent.ACTION_UP:
                	add_RegularCard.setAlpha((float) 1.0);
                    return false;
                default:
                    return false;
                }
            }
      	});
	}
	
	void showsubHeader(String subheaderTitle){
		
		// Back button
//		View subHeader;
//		ImageButton iconBackImageButton;
//		TextView subTitleTextView;
//		subHeader = (View) findViewById(R.id.subHeaderLayout);
//		iconBackImageButton = (ImageButton) subHeader.findViewById(R.id.iconBackImageButton);
//		subTitleTextView = (TextView) subHeader.findViewById(R.id.subTitleTextView);
//		subTitleTextView.setText(subheaderTitle);
//		subTitleTextView.setTypeface(clearSansMedium);
//
//		iconBackImageButton.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// TODO Back Button
//				finish();
//			}
//		});
		
	}
	
	void showBarCode(String barcode_data){
		
		RelativeLayout barCodeButton = (RelativeLayout) findViewById(R.id.barCodeButton);
		TextView getYourCard = (TextView) findViewById(R.id.getYourCard);
		
		if(barcode_data != null) {			
			//Get using barcode
			//SharedPreferences sPref = getSharedPreferences(getString(R.string.app_PREFNAME), MODE_PRIVATE);
        	String allowBarCode  = sPref.getString(getString(R.string.app_TEMP_CARDEXPIRED), null);
			if (allowBarCode == null){
				        
				barCodeButton.setVisibility(View.VISIBLE);				
				getYourCard.setVisibility(View.GONE);
				
				ImageView barCodeView = (ImageView) findViewById(R.id.barCodeView);
		        TextView barCodeText = (TextView) findViewById(R.id.barCodeText);
		        barCodeText.setTypeface(clearSansMedium);
		        
		        Bitmap bitmap = null;
		        try {

		            bitmap = encodeAsBitmap(barcode_data, BarcodeFormat.CODE_128, 600, 250);
		            barCodeView.setImageBitmap(bitmap);

		        } catch (WriterException e) {
		            e.printStackTrace();
		        }     
		        barCodeText.setText(barcode_data);
		        
		        barCodeButton.setOnClickListener(new OnSingleClickListener(){
					@Override
					public void onSingleClick(View v) {
						startActivity(new Intent(Inner_Profile.this, Inner_ProfileFullScreenCode.class));						
					}
				});
			} else {
				//Hide BarCode USAGE & delete it from use
				barCodeButton.setVisibility(View.GONE);				
				getYourCard.setVisibility(View.VISIBLE);
				//sPref.edit().remove(getString(R.string.app_CARD_NUMBER)).apply();
			}
		} 			
	}
	
	private static final int WHITE = 0xFFFFFFFF;
    private static final int BLACK = 0xFF000000;

    Bitmap encodeAsBitmap(String contents, BarcodeFormat format, int img_width, int img_height) throws WriterException {
    String contentsToEncode = contents;
    if (contentsToEncode == null) {
        return null;
    }
    Map<EncodeHintType, Object> hints = null;
    String encoding = guessAppropriateEncoding(contentsToEncode);
    if (encoding != null) {
        hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
        hints.put(EncodeHintType.CHARACTER_SET, encoding);
    }
    MultiFormatWriter writer = new MultiFormatWriter();
    BitMatrix result;
    try {
        result = writer.encode(contentsToEncode, format, img_width, img_height, hints);
    } catch (IllegalArgumentException iae) {
        // Unsupported format
        return null;
    }
    int width = result.getWidth();
    int height = result.getHeight();
    int[] pixels = new int[width * height];
    for (int y = 0; y < height; y++) {
        int offset = y * width;
        for (int x = 0; x < width; x++) {
        pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
        }
    }

    Bitmap bitmap = Bitmap.createBitmap(width, height,
        Bitmap.Config.ARGB_8888);
    bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
    return bitmap;
    }

    private static String guessAppropriateEncoding(CharSequence contents) {
    // Very crude at the moment
    for (int i = 0; i < contents.length(); i++) {
        if (contents.charAt(i) > 0xFF) {
        return "UTF-8";
        }
    }
    return null;
    }

	@Override
	public void onTaskCompleted(int id_request) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTaskCompletedJSON(String json, int id_request) {
		
		if( loader != null && loader.isShowing() )
			loader.dismiss();
		
		switch(id_request){
		case GlobalConstants.GET_PROFILE_INFO:			
			
			try {
				JSONObject data = new JSONObject(json);
				if (data.has("user")){
					Log.v("TAG", "PROFILE GET_PROFILE_INFO json=" + json);
					JSONObject profileData = data.getJSONObject("user");
					profile.setId(profileData.optInt("id"));
					profile.setName(profileData.optString("name"));
					profile.setLegal_entity_name(profileData.optString("legal_entity_name"));
					profile.setCard_num(profileData.optString("card_num"));
					profile.setEmail(profileData.optString("email"));
					profile.setPhone(profileData.optString("phone"));
					profile.setLocale(profileData.optString("locale"));
					profile.setShop_id(profileData.optInt("shop_id"));
					profile.setTemp_card(profileData.optBoolean("temp_card"));
					Log.v("TAG", "PROFILE Temp_card=" + profile.isTemp_card());
					profile.setOriginal_user_info(profileData.optString("original_user_info"));
					if (!profileData.isNull("region")){
						profile.getRegion().setRegionId(profileData.getJSONObject("region").optInt("id"));
						profile.getRegion().setRegionTitle(profileData.getJSONObject("region").optString("title"));
					} else {
						profile.getRegion().setRegionId(-1);
						profile.getRegion().setRegionTitle(null);
					}
					
					setDataAfterLoad();
					
					//Run get Region data load
					HTTPAsynkTask mt_region = new HTTPAsynkTask(Inner_Profile.this, 
							GlobalConstants.GET_REGION,	
							//null, null,
							null, 
							Inner_Profile.this);
					mt_region.execute();
					
				} else if (data.has("error")){
					Toast.makeText(Inner_Profile.this, getString(R.string.Error_server_not_responding), 
							Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(Inner_Profile.this, getString(R.string.Error_server_not_responding), 
							Toast.LENGTH_LONG).show();
				}
				
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.v("TAG", "PROFILE JSONException e=" + e);
				BottomDialog.BottomSelectDialog(Inner_Profile.this, 
						error503, 
		    			getString(R.string.dialog_Server503Title), 
		    			getString(R.string.dialog_Server503Text), 
		    			getString(R.string.dialog_Server503Left), 
		    			getString(R.string.dialog_Server503Right));
			}
			
			break;
			
		case GlobalConstants.POST_PROFILE_INFO:
			Log.v("TAG", "PROFILE POST_PROFILE_INFO json=" + json);
			break;
			
		case GlobalConstants.GET_REGION:
			try {
				JSONObject data = new JSONObject(json);
				if (data.has("items")) {
					JSONArray itemsData = data.getJSONArray("items");
					regionTitle = new ArrayList<String>();
					regionID = new ArrayList<Integer>();
					for (int i=0;i<itemsData.length();i++){
						regionTitle.add(itemsData.getJSONObject(i).optString("title"));
						regionID.add(itemsData.getJSONObject(i).optInt("id"));
					}
					setRegionSpinner();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				BottomDialog.BottomSelectDialog(Inner_Profile.this, 
						error503, 
		    			getString(R.string.dialog_Server503Title), 
		    			getString(R.string.dialog_Server503Text), 
		    			getString(R.string.dialog_Server503Left), 
		    			getString(R.string.dialog_Server503Right));
			}
	
			break;
		
		case GlobalConstants.GET_USER_REGION:
			break;
			
		case GlobalConstants.POST_USER_REGION:
			//TODO check for set region
			Log.v("TAG", "PROFILE POST_USER_REGION json=" + json);
			break;
			
		case GlobalConstants.GET_LOCALE:
			break;
		
		case GlobalConstants.POST_LOCALE:
			try {
				JSONObject data = new JSONObject(json);
				if (data.has("success")){
					//Toast.makeText(Inner_Profile.this, getString(R.string.Error_operationSuccess), 
					//		Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(Inner_Profile.this, getString(R.string.Error_operationUnSuccess), 
							Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				BottomDialog.BottomSelectDialog(Inner_Profile.this, 
						error503, 
		    			getString(R.string.dialog_Server503Title), 
		    			getString(R.string.dialog_Server503Text), 
		    			getString(R.string.dialog_Server503Left), 
		    			getString(R.string.dialog_Server503Right));
			}
			break;
			
			//TODO ============ERRORS DEPLOYMENT==========
		case GlobalConstants.NO_INTERNET_CONNECTION:
			BottomDialog.BottomSelectDialog(Inner_Profile.this, 
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
        	startActivity(new Intent(Inner_Profile.this, Out_RegInStart.class)); 
			finish();
			break;
		
		case GlobalConstants.ERROR_503:
			BottomDialog.BottomSelectDialog(Inner_Profile.this, 
					error503, 
	    			getString(R.string.dialog_Server503Title), 
	    			getString(R.string.dialog_Server503Text), 
	    			getString(R.string.dialog_Server503Left), 
	    			getString(R.string.dialog_Server503Right));
		break;
		
		default:
			BottomDialog.BottomSelectDialog(Inner_Profile.this, 
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
	            	Inner_Profile.this.finishAffinity();
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
	            	Inner_Profile.this.finishAffinity();
	            } else if (msg.what == GlobalConstants.retCodeClose) {

	            }
	        }
	    }
	};
    
	void setDataAfterLoad(){
		if (profile.getName()!=null && profile.getName()!="null"){
			cardOwnerNameEditText.setText(profile.getName());
		} else {
			cardOwnerNameEditText.setText(getString(R.string.app_EnterData));
		}
		cardOwnerNameEditText.setTextColor(Color.BLACK);
		
		if (profile.getPhone()!=null && profile.getPhone()!="null"){
			cardOwnerPhone.setText(profile.getPhone());
		} else {
			cardOwnerPhone.setText(getString(R.string.app_EnterData));
		}
		cardOwnerPhone.setTextColor(Color.BLACK);
		
		if (profile.getEmail()!=null || profile.getEmail()!="null" || profile.getEmail()!=""){
			cardOwnerEmailEditText.setText(profile.getEmail());
		} else {
			cardOwnerEmailEditText.setText(getString(R.string.app_EnterData));
		}
		cardOwnerEmailEditText.setTextColor(Color.BLACK);
		
//		if (profile.getLegal_entity_name()!=null || profile.getLegal_entity_name()!="null"){
//			cardOwnerFopEditText.setText(profile.getLegal_entity_name());
//		} else {
//			cardOwnerFopEditText.setText(getString(R.string.app_EnterData));
//		}
//		cardOwnerFopEditText.setTextColor(Color.BLACK);
		
		setLocalSwitch(profile.getLocale());
		
		//------------Edit Mode---------------
		//Small fucking buttons
				nameEditButton.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						// TODO Go to EmailEditTextFokus & Show Save&DontSave Buttons
						nameEditButton.setVisibility(View.GONE);
						nameSaveButton.setVisibility(View.VISIBLE);
						nameDontSaveButton.setVisibility(View.VISIBLE);
						cardOwnerNameEditText.setEnabled(true);
						String cardOwnerNameCheck = cardOwnerNameEditText.getText().toString();
						if (cardOwnerNameCheck!=null
								&&!cardOwnerNameCheck.equals("")
								&&!cardOwnerNameCheck.equals("null"))
							cardOwnerNameEditText.setSelection(cardOwnerNameEditText.length());
						
						cardOwnerNameEditText.requestFocus();
						Utils.showKeyboard(Inner_Profile.this);
					}	            	
		        });
				nameSaveButton.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						//Chack for name correctness
						String cardOwnerNameCheck = cardOwnerNameEditText.getText().toString();
						if (cardOwnerNameCheck!=null
								&&!cardOwnerNameCheck.equals("")
								&&!cardOwnerNameCheck.equals("null")) {
							Log.v("TAG", "PROF cardOwnerNameEditText=" + cardOwnerNameCheck);
							GradientDrawable back_phone = (GradientDrawable)cardOwnerNameEditTextRL.getBackground();
							back_phone.setStroke(1, Color.WHITE);
							// TODO Save entered Email
							nameEditButton.setVisibility(View.VISIBLE);
							nameSaveButton.setVisibility(View.GONE);
							nameDontSaveButton.setVisibility(View.GONE);
							
							profile.setName(cardOwnerNameCheck);
							cardOwnerNameEditText.setEnabled(false);
							//Go to api
							HashMap<String,String> params = new HashMap<String,String>();
			            	params.put("name", profile.getName());
							params.put("email", profile.getEmail());
							params.put("legal_entity_name", profile.getLegal_entity_name());
							Log.v("TAG", "PROFILE HashMap name=" + profile.getName());
							Log.v("TAG", "PROFILE HashMap email=" + profile.getEmail());
							Log.v("TAG", "PROFILE HashMap legal_entity_name=" + profile.getLegal_entity_name());
							
							loader = new ProgressDialog(Inner_Profile.this);
							loader.setMessage(getString(R.string.app_Loading));
							loader.show();
							
							HTTPAsynkTask mt = new HTTPAsynkTask(Inner_Profile.this, 
									GlobalConstants.POST_PROFILE_INFO, 
									//null, null, 
									params, 
									Inner_Profile.this);
							mt.execute();
							Utils.hideKeyboard(Inner_Profile.this);
						} else {
							Log.v("TAG", "PROF cardOwnerNameEditText=" + cardOwnerNameCheck);
							GradientDrawable back_phone = (GradientDrawable)cardOwnerNameEditTextRL.getBackground();
							back_phone.setStroke(1, Color.RED);
						}
						
						
					}	            	
		        });
		        nameDontSaveButton.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						// TODO Revert back to name from API
						GradientDrawable back_phone = (GradientDrawable)cardOwnerNameEditTextRL.getBackground();
						back_phone.setStroke(1, Color.WHITE);
						nameEditButton.setVisibility(View.VISIBLE);
						nameSaveButton.setVisibility(View.GONE);
						nameDontSaveButton.setVisibility(View.GONE);
						cardOwnerNameEditText.setText(profile.getName());
						cardOwnerNameEditText.setEnabled(false);
					}	            	
		        });
		        
				//Small fucking buttons
				emailEditButton.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						// TODO Go to EmailEditTextFokus & Show Save&DontSave Buttons
						emailEditButton.setVisibility(View.GONE);
						emailSaveButton.setVisibility(View.VISIBLE);
						emailDontSaveButton.setVisibility(View.VISIBLE);
						cardOwnerEmailEditText.setEnabled(true);
						
						if (Utils.isEmailCorrect(cardOwnerEmailEditText.getText().toString()))
							cardOwnerEmailEditText.setSelection(cardOwnerEmailEditText.length());
						
						cardOwnerEmailEditText.requestFocus();
						Utils.showKeyboard(Inner_Profile.this);
					}	            	
		        });
				emailSaveButton.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {		
						//Check email for correctness
						if (Utils.isEmailCorrect(cardOwnerEmailEditText.getText().toString())) {
							// TODO Save entered Email
							emailEditButton.setVisibility(View.VISIBLE);
							emailSaveButton.setVisibility(View.GONE);
							emailDontSaveButton.setVisibility(View.GONE);
							GradientDrawable back_phone = (GradientDrawable)cardOwnerEmailEditTextRL.getBackground();
							back_phone.setStroke(1, Color.WHITE);
							profile.setEmail(cardOwnerEmailEditText.getText().toString());
							cardOwnerEmailEditText.setEnabled(false);
							//Go to api 
							HashMap<String,String> params = new HashMap<String,String>();
			            	params.put("name", profile.getName());
							params.put("email", profile.getEmail());
							params.put("legal_entity_name", profile.getLegal_entity_name());
							
							loader = new ProgressDialog(Inner_Profile.this);
							loader.setMessage(getString(R.string.app_Loading));
							loader.show();
							
							HTTPAsynkTask mt = new HTTPAsynkTask(Inner_Profile.this, 
									GlobalConstants.POST_PROFILE_INFO, 
									//null, null, 
									params, 
									Inner_Profile.this);
							mt.execute();
							Utils.hideKeyboard(Inner_Profile.this);
						} else {
							GradientDrawable back_phone = (GradientDrawable)cardOwnerEmailEditTextRL.getBackground();
							back_phone.setStroke(1, Color.RED);
						}

					}	            	
		        });
				emailDontSaveButton.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						// TODO Revert back to Email from API
						GradientDrawable back_phone = (GradientDrawable)cardOwnerEmailEditTextRL.getBackground();
						back_phone.setStroke(1, Color.WHITE);
						emailEditButton.setVisibility(View.VISIBLE);
						emailSaveButton.setVisibility(View.GONE);
						emailDontSaveButton.setVisibility(View.GONE);
						if (profile.getEmail()!=null||profile.getEmail()!=""||profile.getEmail()!="null")
							cardOwnerEmailEditText.setText(profile.getEmail());
						else 
							cardOwnerEmailEditText.setText(getString(R.string.app_EnterData));
						cardOwnerEmailEditText.setEnabled(false);
					}	            	
		        });   
				
		  		//Small fucking buttons
//		  		fopEditButton.setOnClickListener(new OnClickListener(){
//		  			@Override
//		  			public void onClick(View v) {
//		  				// TODO Go to FOPEditTextFokus & Show Save&DontSave Buttons
//		  				fopEditButton.setVisibility(View.GONE);
//		  				fopSaveButton.setVisibility(View.VISIBLE);
//		  				fopDontSaveButton.setVisibility(View.VISIBLE);
//		  				cardOwnerFopEditText.setEnabled(true);
//		  				String cardOwnerFopCheck = cardOwnerFopEditText.getText().toString();
//		  				if (cardOwnerFopCheck!=null
//		  						&&!cardOwnerFopCheck.equals("")
//								&&!cardOwnerFopCheck.equals("null"))
//		  					cardOwnerFopEditText.setSelection(cardOwnerFopEditText.length());
//		  				cardOwnerFopEditText.requestFocus();
//		  				Utils.showKeyboard(Inner_Profile.this);
//		  			}
//		          });
//		  		fopSaveButton.setOnClickListener(new OnClickListener(){
//		  			@Override
//		  			public void onClick(View v) {
//		  				//Check for correctness
//		  				String cardOwnerFopCheck = cardOwnerFopEditText.getText().toString();
//						if (cardOwnerFopCheck!=null
//								&&!cardOwnerFopCheck.equals("")
//								&&!cardOwnerFopCheck.equals("null")) {
//		  					GradientDrawable back_phone = (GradientDrawable)cardOwnerFopEditTextRL.getBackground();
//							back_phone.setStroke(1, Color.WHITE);
//							// TODO Save entered Email
//			  				fopEditButton.setVisibility(View.VISIBLE);
//			  				fopSaveButton.setVisibility(View.GONE);
//			  				fopDontSaveButton.setVisibility(View.GONE);
//
//			  				profile.setLegal_entity_name(cardOwnerFopCheck);
//			  				cardOwnerFopEditText.setEnabled(false);
//			  				//Go to api
//			  				HashMap<String,String> params = new HashMap<String,String>();
//			            	params.put("name", profile.getName());
//							params.put("email", profile.getEmail());
//							params.put("legal_entity_name", profile.getLegal_entity_name());
//
//							loader = new ProgressDialog(Inner_Profile.this);
//							loader.setMessage(getString(R.string.app_Loading));
//							loader.show();
//
//							HTTPAsynkTask mt = new HTTPAsynkTask(Inner_Profile.this,
//									GlobalConstants.POST_PROFILE_INFO,
//									//null, null,
//									params,
//									Inner_Profile.this);
//							mt.execute();
//							Utils.hideKeyboard(Inner_Profile.this);
//		  				} else {
//		  					GradientDrawable back_phone = (GradientDrawable)cardOwnerFopEditTextRL.getBackground();
//							back_phone.setStroke(1, Color.RED);
//		  				}
//
//
//		  			}
//		          });
//		          fopDontSaveButton.setOnClickListener(new OnClickListener(){
//		  			@Override
//		  			public void onClick(View v) {
//		  				// TODO Revert back to Email from API
//		  				GradientDrawable back_phone = (GradientDrawable)cardOwnerFopEditTextRL.getBackground();
//						back_phone.setStroke(1, Color.WHITE);
//		  				fopEditButton.setVisibility(View.VISIBLE);
//		  				fopSaveButton.setVisibility(View.GONE);
//		  				fopDontSaveButton.setVisibility(View.GONE);
//		  				cardOwnerFopEditText.setText(profile.getLegal_entity_name());
//		  				cardOwnerFopEditText.setEnabled(false);
//		  			}
//		          });
		
	}
	
	@Override
	  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode==GlobalConstants.REQUEST_CODE_INET){	
	    	 new Handler().postDelayed(new Runnable() {
	             public void run() {
	              // Actions to do after 1 seconds
	            	 recreate();
	             }
	         }, GlobalConstants.REFRESH_INET_TIME);
	     }
		
		if (resultCode == RESULT_OK){
			switch(requestCode){
			case GlobalConstants.REQUEST_CODE_SCAN:
					BottomDialog.BottomMessageDialog(Inner_Profile.this, addRegularCard,
						getString(R.string.dialog_PermanentAddedTitle),
						getString(R.string.dialog_PermanentAddedText));
				break;			
			default:			
				break;
			}
		}
	}
	
	Handler addRegularCard = new Handler() {
	    @Override
	    public void handleMessage(Message msg) {
	        if (msg != null) {
	            //TODO Check for dialog box responses
	        	recreate();
	        }
	    }
	};
	
	private void setLocal(String lang) {
		Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
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
		tracker.setScreenName("profile");
		tracker.send(new HitBuilders.EventBuilder().build());
	}
	
}
