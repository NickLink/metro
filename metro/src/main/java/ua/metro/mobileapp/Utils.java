package ua.metro.mobileapp;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ua.metro.mobileapp.application.MeApp;

public class Utils {
	
	static void showActionBar(final Activity activity, boolean enable){
//		final ImageView metro_logo;
		final ImageButton menu_info;
//		final ImageButton menu_search;
//		final ImageButton menu_basket;
//		TextView basket_count;
		
		ActionBar actionBar = activity.getActionBar();
		
		actionBar.setHomeButtonEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayUseLogoEnabled(false);
		
		actionBar.setDisplayShowTitleEnabled(false);
		
  	    View customActionBarView = activity.getLayoutInflater().inflate(R.layout.ab_main, null, true);
		
 	 	actionBar.setCustomView(customActionBarView, new ActionBar.LayoutParams(
 	 			ViewGroup.LayoutParams.MATCH_PARENT,
						ViewGroup.LayoutParams.MATCH_PARENT));
//   		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
   		
//   		metro_logo = (ImageView) customActionBarView.findViewById(R.id.metrologoImageView);
   		menu_info = (ImageButton) customActionBarView.findViewById(R.id.feedback_menu_item);
//   		menu_search = (ImageButton) customActionBarView.findViewById(R.id.search_menu_item);
//   		menu_basket = (ImageButton) customActionBarView.findViewById(R.id.basket_menu_item);
//   		basket_count = (TextView) customActionBarView.findViewById(R.id.basket_count);
   		
//   		if(activity.getClass() == Inner_Basket.class){
//   			menu_basket.setImageResource(R.drawable.btn_cart_yellow_normal);
//   		}
//   		Log.v("LOG_TAG","GGGG activity.getClass() =  "+activity.getClass());
//
//   		basket_count = (TextView) customActionBarView.findViewById(R.id.basket_count);
//
//   		basket_count.setText(String.valueOf(MeApp.regData.getUser().getCount_basket()));
   		
   		if (enable){
	   		View.OnClickListener menuBtnListener = new View.OnClickListener(){
		        public void onClick(View v) {
		        	
		        	if (v == menu_info){
		        		activity.startActivity(new Intent(activity, Inner_AuthQuestion.class));
						activity.finish();
		            } 
//		        	if (v == metro_logo){
//		        		if(activity.getClass().getSimpleName().equals("Inner_Main")){
//		        			Log.v("TAG", "METROLOGO =" + activity.getClass().getSimpleName());
//		        		} else {
//		        			activity.startActivity(new Intent(activity, Inner_Profile.class)
//		        			.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
//			        		activity.finish();
//		        		}
//		            }
//		            if (v == menu_search){
//		            	activity.startActivity(new Intent(activity, Inner_Search.class)
//		            	.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
//		            }
//		            if (v == menu_basket){
//		            	activity.startActivity(new Intent(activity, Inner_Basket.class)
//		            	.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
//		            	//activity.finish();
//		            }
		             
		        }
		    };
//		    metro_logo.setOnClickListener(menuBtnListener);
		    menu_info.setOnClickListener(menuBtnListener);
//		    menu_search.setOnClickListener(menuBtnListener);
//		    menu_basket.setOnClickListener(menuBtnListener);
   		}
	}
	
	@SuppressLint("ResourceAsColor")
	public static void setBottomMenu(final Activity activity, int selectedItem){
		
		final LinearLayout profileLinearLayout = (LinearLayout) activity.findViewById(R.id.bottomBtnProfileLinearLayout);
//	    final LinearLayout saleLinearLayout = (LinearLayout) activity.findViewById(R.id.bottomBtnSaleLinearLayout);
	    final LinearLayout mapLinearLayout = (LinearLayout) activity.findViewById(R.id.bottomBtnMapLinearLayout);

		ImageView profileButton = (ImageView) activity.findViewById(R.id.profileButton);
//	    ImageButton saleButton = (ImageButton) activity.findViewById(R.id.saleButton);
		ImageView mapButton = (ImageView) activity.findViewById(R.id.mapButton);
	    
	    TextView profileTextView = (TextView) activity.findViewById(R.id.profileTextView);
//	    TextView saleTextView = (TextView) activity.findViewById(R.id.saleTextView);
	    TextView mapTextView = (TextView) activity.findViewById(R.id.mapTextView);
	    
	    Typeface fontMedium = FontCache.get("fonts/clearsansmedium.ttf", activity);	
	    
	    profileTextView.setTypeface(fontMedium);
//        saleTextView.setTypeface(fontMedium);
        mapTextView.setTypeface(fontMedium);
	    
	    switch(selectedItem){
		    case 1:
		    	profileButton.setImageResource(R.drawable.btn_profile_yellow_normal);
		    	profileTextView.setTextColor(activity.getResources().getColor(R.color.metro_yellow));
				MeApp.menuSelected = 1;
		    	break;
//		    case 2:
//		    	saleButton.setImageResource(R.drawable.btn_sale_yellow_normal);
//		    	saleTextView.setTextColor(activity.getResources().getColor(R.color.metro_yellow));
//		    	break;
		    case 3:
		    	mapButton.setImageResource(R.drawable.btn_map_yellow_normal);
		    	mapTextView.setTextColor(activity.getResources().getColor(R.color.metro_yellow));
				MeApp.menuSelected = 3;
		    	break;	    	
	    }
	    View.OnClickListener bottomMenuBtnListener = new View.OnClickListener(){
	    	public void onClick(View v) {
	        	if (v == profileLinearLayout){
	        		activity.startActivity(new Intent(activity, Inner_Profile.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
//	        		if (!activity.getClass().getSimpleName().equals("Inner_Main"))
	        			activity.finish();
	            } 
//	            if (v == saleLinearLayout){
//	            	activity.startActivity(new Intent(activity, Inner_Catalogs.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
//	            	if (!activity.getClass().getSimpleName().equals("Inner_Main"))
//	        			activity.finish();
//	            }
	            if (v == mapLinearLayout){		            	
	            	activity.startActivity(new Intent(activity, Inner_Map.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
//	            	if (!activity.getClass().getSimpleName().equals("Inner_Main"))
	        			activity.finish();
	            }
	         }
	    };
	    if (selectedItem!=1)
	    	profileLinearLayout.setOnClickListener(bottomMenuBtnListener);
//	    if (selectedItem!=2)
//	    	saleLinearLayout.setOnClickListener(bottomMenuBtnListener);
	    if (selectedItem!=3)
	    	mapLinearLayout.setOnClickListener(bottomMenuBtnListener);  
	}
		    

	public static boolean isEmailCorrect(String email_string) {
		boolean isValid = false;
		if (email_string == null) {
			return false;
		}
		try{
			String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
			CharSequence inputStr = email_string;	
			Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(inputStr);
			if (matcher.matches()) {
				isValid = true;
			}			
		} catch (Exception e){
			isValid = false;
		}
		return isValid;
	}
	
	public static int getDaysUntil(String strDateEnd) {
		Date dateToday = null;
		Date dateEnd = null;
		int daysUntil;
		long diff;
		  
 		if (strDateEnd!=null && strDateEnd.length()>0 ){
	  		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				dateEnd = (Date)formatter.parse(strDateEnd);
					
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
	  		 
     	}
 		
 		dateToday = new Date();
 		if (dateToday.before(dateEnd)){
	 		diff = dateEnd.getTime() - dateToday.getTime();
	 		float i1 = (float)diff;
	 		float i2 = (float)86400000;
	 		
	 		daysUntil = (int) Math.ceil((float)i1/i2);
	 		Log.v("TAG", "XXX float daysUntil=" + (float)diff/(float) 86400000);
	 		Log.v("TAG", "XXX daysUntil=" + daysUntil);
 		}else
 			daysUntil = 1;
 	     
 		return daysUntil;
	}
	
	public static int getDuration(String strDateStart, String strDateEnd) {
		Date dateStart = null;
		Date dateEnd = null;
		int daysDuration;
		long diff;
		  
 		if (strDateStart!=null && strDateStart.length()>0 && strDateEnd!=null && strDateEnd.length()>0 ){
	  		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				dateStart = (Date)formatter.parse(strDateStart);
				dateEnd = (Date)formatter.parse(strDateEnd);
					
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
	  		 
     	}
 		
 		if (dateStart.before(dateEnd)){
	 		diff = dateEnd.getTime() - dateStart.getTime();
	 		float i1 = (float)diff;
	 		float i2 = (float)86400000;
	 		daysDuration = (int) Math.ceil((float)i1/i2);
	 		Log.v("TAG", "XXX float daysDuration=" + (float)diff/(float) 86400000);
	 		Log.v("TAG", "XXX daysDuration=" + daysDuration);
 		}else
 			daysDuration = 1;
 	     
 		return daysDuration;
	}
	
	public static boolean hasActiveInternetConnection(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		if (activeNetworkInfo != null) {
			try {
				HttpURLConnection urlc = (HttpURLConnection) (new URL(
						"http://www.google.com").openConnection());
				urlc.setRequestProperty("User-Agent", "Test");
				urlc.setRequestProperty("Connection", "close");
				urlc.setConnectTimeout(3000);
				urlc.connect();
				return (urlc.getResponseCode() == 200);
			} catch (IOException e) {
				// Log.e(LOG_TAG, "Error checking internet connection", e);
			}
		} else {
			// Log.d(LOG_TAG, "No network available!");
		}
		return false;
	}
	
	public static float getDensity(Context context) {
	    return context.getResources().getDisplayMetrics().density;
	}
	
	public static String plural(int number, String form1, String form2, String form3) {
		String result;
		
		int lastDigit = Math.abs(number) % 10;
		switch(lastDigit){
			case 1 :
				if(number==11)
					result = number + " " + form3;
				else
					result = number + " " + form1;
			break;
				
			case 2 :
				if(number==12)
					result = number + " " + form3;
				else
					result = number + " " + form2;
			break;
				
			case 3 :
				if(number==13)
					result = number + " " + form3;
				else
					result = number + " " + form2;
			break;
				
			case 4 :
				if(number==14)
					result = number + " " + form3;
				else
					result = number + " " + form2;
			break;
				
			default:
				result = number + " " + form3;
			break;
			
		}
	    return result;
	}
	
	public static String pluralNoInt(int number, String form1, String form2, String form3) {
		String result;
		
		int lastDigit = Math.abs(number) % 10;
		switch(lastDigit){
			case 1 :
				if(number==11)
					result = form3;
				else
					result = form1;
			break;
				
			case 2 :
				if(number==12)
					result = form3;
				else
					result = form2;
			break;
				
			case 3 :
				if(number==13)
					result = form3;
				else
					result = form2;
			break;
				
			case 4 :
				if(number==14)
					result = form3;
				else
					result = form2;
			break;
				
			default:
				result = form3;
			break;
			
		}
	    return result;
	}
	
	  public static String getDirectionsUrl(LatLng origin,LatLng dest){
   	 
        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;
 
        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;
 
        // Sensor enabled
        String sensor = "sensor=false";
 
        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+sensor;
 
        // Output format
        String output = "json";
 
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;
 
        return url;
    }
	  
	  /**
	   * Returns the SHA1 hash for the provided String
	   * @param text
	   * @return the SHA1 hash or null if an error occurs
	   */
	   public static String SHA1(String text) {
	   try {	              
	     MessageDigest md;
	     md = MessageDigest.getInstance("SHA-1");
	     md.update(text.getBytes("UTF-8"), 
	               0, text.length());
	     byte[] sha1hash = md.digest();	              
	     return toHex(sha1hash);	              
	   } catch (NoSuchAlgorithmException e) {
	     e.printStackTrace();
	   } catch (UnsupportedEncodingException e) {
	     e.printStackTrace();
	   }	          
	   return null;
	 }
	      
	 public static String toHex(byte[] buf) {
	          
	   if (buf == null) return "";
	  
	   int l = buf.length;
	   StringBuffer result = new StringBuffer(2 * l);
	  
	   for (int i = 0; i < buf.length; i++) {
	     appendHex(result, buf[i]);
	   }
	  
	   return result.toString();
	  
	 }
	      
	 private final static String HEX = "0123456789abcdef";
	      
	 private static void appendHex(StringBuffer sb, byte b) {
	  
	   sb.append(HEX.charAt((b >> 4) & 0x0f))
	     .append(HEX.charAt(b & 0x0f));
	  
	 }
	 
	static boolean phoneCheck(String phone){
		Log.v("TAG", "METROP phone=" + phone.length());
		if(phone == null || phone.length() != 12) {
			Log.v("TAG", "METROP phone == null || phone.length() != 11");
			return false;
		}
			
		if(phone.substring(0, 3).equals("380"))
			return true;
		else {
			Log.v("TAG", "METROP phone.substring(0, 2).equals(380)" + phone.substring(0, 2));
			return false;
		}
	}
	
	public static void hideKeyboard(Activity activity) {
	    InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
	    //Find the currently focused view, so we can grab the correct window token from it.
	    View view = activity.getCurrentFocus();
	    //If no view currently has focus, create a new one, just so we can grab a window token from it
	    if (view == null) {
	        view = new View(activity);
	    }
	    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}
	
	public static void showKeyboard(Activity activity) {
	    InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
	    //Find the currently focused view, so we can grab the correct window token from it.
	    View view = activity.getCurrentFocus();
	    //If no view currently has focus, create a new one, just so we can grab a window token from it
	    if (view == null) {
	        view = new View(activity);
	    }
	    imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
	}
}
