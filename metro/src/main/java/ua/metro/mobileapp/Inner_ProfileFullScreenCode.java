package ua.metro.mobileapp;

import java.util.EnumMap;
import java.util.Map;

import ua.metro.mobileapp.HttpMetods.UserState;
import ua.metro.mobileapp.application.MeApp;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class Inner_ProfileFullScreenCode extends Activity {
	Typeface clearSans, clearSansMedium, clearSansBold;
	static Typeface clearSansLight;
	
	private long mLastClickTime = 0;
	int brightness = 255;
	Configuration config;
	
	SharedPreferences sPref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		UserState.restoreUserState(this);
		
		setContentView(R.layout.inner_profile_fullscreen);
		
		sPref = getSharedPreferences(getString(R.string.app_PREFNAME), MODE_PRIVATE);
		
		WindowManager.LayoutParams lp = getWindow().getAttributes();
	    float brightness = 1;
	    lp.screenBrightness = brightness; 
	    getWindow().setAttributes(lp); 
	    
	    config = getResources().getConfiguration();
		
		clearSans = FontCache.get("fonts/clearsans.ttf", getBaseContext());
        clearSansBold = FontCache.get("fonts/clearsansbold.ttf", getBaseContext());
        clearSansMedium = FontCache.get("fonts/clearsansmedium.ttf", getBaseContext());
        clearSansLight = FontCache.get("fonts/clearsanslight.ttf", getBaseContext());

        Utils.showActionBar(this, false);
        //Utils.setBottomMenu(this,1);
        
		showsubHeader(getString(R.string.my_kard));
		
		String prefSaved_barcode = sPref.getString(getString(R.string.app_CARD_NUMBER), null);
		Log.v("123", "PREF_S =" + prefSaved_barcode);
		
		TextView message_text = (TextView) findViewById(R.id.message_text);
		message_text.setVisibility(View.GONE);
		
		if(MeApp.regData.getUser().getCard_num() != null) {
			showBarCode(MeApp.regData.getUser().getCard_num());
		} 
		else {
			if(prefSaved_barcode != null){
				showBarCode(prefSaved_barcode);
			} else {
				message_text.setVisibility(View.VISIBLE);
			}
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

	void showBarCode(String barcode_data) {

		ImageView barCodeView = (ImageView) findViewById(R.id.barCodeView);
		ua.metro.mobileapp.UI.VerticalTextView barCodeText = (ua.metro.mobileapp.UI.VerticalTextView) findViewById(R.id.barCodeText);
		barCodeText.setTypeface(clearSansMedium);

		Bitmap bitmap = null;
		try {

			bitmap = encodeAsBitmap(barcode_data, BarcodeFormat.CODE_128, 1200,
					500);
			barCodeView.setImageBitmap(RotateBitmap(bitmap, 90));

		} catch (WriterException e) {
			e.printStackTrace();
		}
		barCodeText.setText(barcode_data);
		
		int barcodeTextSize = 22;
		
		if (config.smallestScreenWidthDp >= 720) {
		    // sw720dp
			barcodeTextSize = 35;
		  }
		  else if (config.smallestScreenWidthDp >= 600) {
		    // sw600dp
			  barcodeTextSize = 27;
		  }
		  else if (config.smallestScreenWidthDp >= 480) {
			    // sw600dp
				  barcodeTextSize = 24;
		  }
		  else {
		    // all other
			  barcodeTextSize = 22;			  
		  }
		
		/*float density = Utils.getDensity(Inner_ProfileFullScreenCode.this);
		
		if (density >= 4.0) {
			barcodeTextSize = 30;
	    }
	    if (density >= 3.0) {
	    	barcodeTextSize = 26;
	    }
	    if (density >= 2.0) {
	    	barcodeTextSize = 23;
	    }
	    if (density >= 1.5) {
	    	barcodeTextSize = 20;
	    }
	    if (density >= 1.0) {
	    	barcodeTextSize = 17;
	    }	*/
		barCodeText.setTextSize(barcodeTextSize);		
		barCodeText.setBackgroundResource(R.drawable.bg_btn_white);
	}
	
	public static Bitmap RotateBitmap(Bitmap source, float angle)
	{
	      Matrix matrix = new Matrix();
	      matrix.postRotate(angle);
	      return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
	}

	private static final int WHITE = 0xFFFFFFFF;
	private static final int BLACK = 0xFF000000;

	Bitmap encodeAsBitmap(String contents, BarcodeFormat format, int img_width,
			int img_height) throws WriterException {
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
			result = writer.encode(contentsToEncode, format, img_width,
					img_height, hints);
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

	private void setAnalytics() {
		GoogleAnalytics analytics;
		final Tracker tracker;
		analytics = GoogleAnalytics.getInstance(this);
		analytics.setLocalDispatchPeriod(1800);
		tracker = analytics.newTracker(getString(R.string.app_GA_CODE));
		tracker.enableExceptionReporting(true);
		tracker.enableAdvertisingIdCollection(true);
		tracker.enableAutoActivityTracking(true);
		tracker.setScreenName("fullscreen_scancode");
		tracker.send(new HitBuilders.EventBuilder().build());
	}
	
}
