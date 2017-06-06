package ua.metro.mobileapp.HttpMetods;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import ua.metro.mobileapp.GlobalConstants;
import ua.metro.mobileapp.OnTaskCompleted;
import ua.metro.mobileapp.R;
import ua.metro.mobileapp.UI.BottomDialog;
import ua.metro.mobileapp.Utils;
import ua.metro.mobileapp.application.MeApp;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class HTTPAsynkTask extends AsyncTask<String, Void, String> {
	
	private String EXTRA_PATH;	
	final String LOG_TAG = "METRO";
	//private String cardNumber;
	//private String phoneNumber;
    private int id_request;
    private HashMap<String,String> params = new HashMap<String, String>();
    private int  responseCode;
    private Context context;
    //private ProgressDialog pd;
    private int http_method;
    //private boolean visibleProgressDialog = true;
    private OnTaskCompleted listener;
    String xHash = "";
    
    public HTTPAsynkTask (Context context, 
    		int id_request, 
    		//String cardNumber, 
    		//String phoneNumber, 
    		HashMap<String,String> params,
    		OnTaskCompleted listener) { 
        //this.cardNumber = cardNumber;
        //this.phoneNumber = phoneNumber;
        this.id_request = id_request;
        this.context = context;
        this.params = params;
        this.listener=listener;       
        //pd = new ProgressDialog(context);
	}


	
	@Override
    protected void onPreExecute() {
      super.onPreExecute();
		/*  if (MeApp.regData.getToken() != null) {
			  Log.v("TAG", "METRO TokenExist");
		  } else {
			  Log.v("TAG", "METRO TokenNULL Wake up from sleep");
			  SharedPreferences sPref = context.getSharedPreferences(context.getString(R.string.app_PREFNAME), context.MODE_PRIVATE);
		    	String tempToken = sPref.getString(context.getString(R.string.app_TOKEN), null);
		    	MeApp.regData.setToken(tempToken);
		  }*/
		  //Check for Internet connection
		  if(!ConnectivityInfo.isConnected(context)){
			  id_request = GlobalConstants.NO_INTERNET_CONNECTION;
			  Log.v("123", "VVV + onPreExecute NO_INTERNET_CONNECTION" );
		  } else Log.v("123", "VVV + onPreExecute" );
		  
		  
		  
		  
      //this.pd.setMessage("Зачекайте...");
      //this.pd.setCancelable(false);
      //this.pd.show();	      
		  Log.d(LOG_TAG, "Begin Query");    	
    }


	@Override
	protected String doInBackground(String... param) {
		switch(id_request){
		case GlobalConstants.CARD_HAS_PROFILE:
			EXTRA_PATH = "card_has_profile/" + params.get("card"); //cardNumber;
			params.remove("card");
			http_method = GlobalConstants.HTTP_GET;
			break;		
		case GlobalConstants.REGISTER_CARD:			
			EXTRA_PATH = "register_card/" + params.get("card"); //cardNumber;		
			http_method = GlobalConstants.HTTP_POST;
			break;			
		case GlobalConstants.RESET_PASSWORD:
			EXTRA_PATH = "reset_password/" + params.get("phone"); //phoneNumber;
			params.remove("phone");
			http_method = GlobalConstants.HTTP_POST;
			break;			
		case GlobalConstants.FEEDBACK:
			EXTRA_PATH = "feedback";
			http_method = GlobalConstants.HTTP_POST;
			break;			
		case GlobalConstants.AUTH:
			EXTRA_PATH = "auth";
			http_method = GlobalConstants.HTTP_POST;
			break;		
		case GlobalConstants.CATALOG:
			EXTRA_PATH = "catalog";
			http_method = GlobalConstants.HTTP_GET;
			break;	
		case GlobalConstants.CATALOG_ID:
			String category = "";
			if (params.get("category_id")!=null)
				category = "/category/" + params.get("category_id");
			EXTRA_PATH = "catalog/" + params.get("catalog_id") + category+ "?page=" + params.get("page")+"&lang=uk";
			http_method = GlobalConstants.HTTP_GET;
			break;
		case GlobalConstants.CART_GET:
			EXTRA_PATH = "cart";
			http_method = GlobalConstants.HTTP_GET;
			break;
		case GlobalConstants.CART_ADD:
			EXTRA_PATH = "cart";
			http_method = GlobalConstants.HTTP_POST;
			break;
		case GlobalConstants.CART_DEL:
			EXTRA_PATH = "cart/" + params.get("cartRowId");
			http_method = GlobalConstants.HTTP_DEL;
			break;			
		case GlobalConstants.PRODUCT:
			EXTRA_PATH = "catalog/" + params.get("catalog_id") + "/product/" + params.get("product_id");
			http_method = GlobalConstants.HTTP_GET;
			break;			
		case GlobalConstants.GET_REG_META:
			EXTRA_PATH = "registration/meta";
			http_method = GlobalConstants.HTTP_GET;
			break;
		case GlobalConstants.GET_GEO_AREA:
			EXTRA_PATH = "geo/area";
			http_method = GlobalConstants.HTTP_GET;
			break;
		case GlobalConstants.GET_GEO_STREETTYPE:
			EXTRA_PATH = "geo/street_type";
			http_method = GlobalConstants.HTTP_GET;
			break;
		case GlobalConstants.POST_REGISTRATION_FIZ:
			EXTRA_PATH = "registration/register/employee";
			http_method = GlobalConstants.HTTP_POST;
			break;
		case GlobalConstants.POST_REGISTRATION_YUR:
			EXTRA_PATH = "registration/register/owner";
			http_method = GlobalConstants.HTTP_POST;
			break;
		case GlobalConstants.GET_REGION:
			EXTRA_PATH = "region";
			http_method = GlobalConstants.HTTP_GET;
			break;
		case GlobalConstants.GET_USER_REGION:
			EXTRA_PATH = "region/current";
			http_method = GlobalConstants.HTTP_GET;
			break;
		case GlobalConstants.POST_USER_REGION:
			EXTRA_PATH = "region/current/" + params.get("region_id");
			http_method = GlobalConstants.HTTP_POST;
			break;
		case GlobalConstants.GET_LOCALE:
			EXTRA_PATH = "locale";
			http_method = GlobalConstants.HTTP_GET;
			break;
		case GlobalConstants.POST_LOCALE:
			EXTRA_PATH = "locale/" + params.get("locale_name");
			http_method = GlobalConstants.HTTP_POST;
			break;
		case GlobalConstants.GET_SHOPS:
			EXTRA_PATH = "shop" + "?region_id=" + params.get("region_id");
			http_method = GlobalConstants.HTTP_GET;
			break;
		case GlobalConstants.GET_NEAREST_SHOPS:
			EXTRA_PATH = "shop/nearest" + "?latitude=" + params.get("latitude") 
					 + "&longitude=" + params.get("longitude") + "&distance=100";
			http_method = GlobalConstants.HTTP_GET;
			break;
		case GlobalConstants.POST_PERMANENT_CARD:
			EXTRA_PATH = "set_permanent_card/" + params.get("card");
			http_method = GlobalConstants.HTTP_POST;
			break;
		case GlobalConstants.GET_PROFILE_INFO:
			EXTRA_PATH = "profile_info";
			http_method = GlobalConstants.HTTP_GET;
			break;
		case GlobalConstants.POST_PROFILE_INFO:
			EXTRA_PATH = "profile_info";
			http_method = GlobalConstants.HTTP_POST;
			break;
		case GlobalConstants.GET_SHOP:
			EXTRA_PATH = "shop";
			http_method = GlobalConstants.HTTP_GET;
			break;	
		case GlobalConstants.POST_SEARCH:
			EXTRA_PATH = "search";
			http_method = GlobalConstants.HTTP_POST;
			break;
				
		case GlobalConstants.NO_INTERNET_CONNECTION:
			Log.v("123", "VVV + doInBackground NO_INTERNET_CONNECTION" );
			return null;
		}
		// TODO Auto-generated method stub
		return readAPIObject(GlobalConstants.API_PATH + EXTRA_PATH);
	}
	
	@Override
    protected void onPostExecute(String result) {
      super.onPostExecute(result);

      switch (id_request){
  		case GlobalConstants.CARD_HAS_PROFILE:
  			responceParser(result);
		      listener.onTaskCompleted(id_request);
		      break;
  		case GlobalConstants.REGISTER_CARD:
  			responceParser(result);
		      listener.onTaskCompleted(id_request);
		      break;
  		case GlobalConstants.RESET_PASSWORD:
  			responceParser(result);
		      listener.onTaskCompleted(id_request);
		      break;
  		case GlobalConstants.FEEDBACK:
  			responceParser(result);
		      listener.onTaskCompleted(id_request);
		      break;
  		case GlobalConstants.AUTH:
		      responceParser(result);
		      listener.onTaskCompleted(id_request);
		      break;		      
  		case GlobalConstants.CATALOG:
  			listener.onTaskCompletedJSON(result, id_request);
  			break;
  		case GlobalConstants.CATALOG_ID:
  			listener.onTaskCompletedJSON(result, id_request);
  			break;
  		case GlobalConstants.CART_GET:
  			listener.onTaskCompletedJSON(result, id_request);
  			break;
  		case GlobalConstants.CART_ADD:
  			listener.onTaskCompletedJSON(result, id_request);
  			break;
  		case GlobalConstants.CART_DEL:
  			listener.onTaskCompletedJSON(result, id_request);
  			break;
  		case GlobalConstants.PRODUCT:
  			listener.onTaskCompletedJSON(result, id_request);
  			break;
  		case GlobalConstants.GET_REG_META:
  			listener.onTaskCompletedJSON(result, id_request);
  			break;
  		case GlobalConstants.GET_GEO_AREA:
  			listener.onTaskCompletedJSON(result, id_request);
  			break;
  		case GlobalConstants.GET_GEO_STREETTYPE:
  			listener.onTaskCompletedJSON(result, id_request);
  			break;
  		case GlobalConstants.POST_REGISTRATION_FIZ:
  			listener.onTaskCompletedJSON(result, id_request);
  			break;
  		case GlobalConstants.POST_REGISTRATION_YUR:
  			listener.onTaskCompletedJSON(result, id_request);
  			break;
  		case GlobalConstants.GET_REGION:
  			listener.onTaskCompletedJSON(result, id_request);
  			break;
  		case GlobalConstants.GET_USER_REGION:
  			listener.onTaskCompletedJSON(result, id_request);
  			break;
  		case GlobalConstants.POST_USER_REGION:
  			listener.onTaskCompletedJSON(result, id_request);
  			break;
  		case GlobalConstants.GET_LOCALE:
  			listener.onTaskCompletedJSON(result, id_request);
  			break;
  		case GlobalConstants.POST_LOCALE:
  			listener.onTaskCompletedJSON(result, id_request);
  			break;
  		case GlobalConstants.GET_SHOPS:
  			listener.onTaskCompletedJSON(result, id_request);
  			break;
  		case GlobalConstants.GET_NEAREST_SHOPS:
  			listener.onTaskCompletedJSON(result, id_request);
  			break;
  		case GlobalConstants.POST_PERMANENT_CARD:
  			listener.onTaskCompletedJSON(result, id_request);
  			break;
  		case GlobalConstants.GET_PROFILE_INFO:
  			listener.onTaskCompletedJSON(result, id_request);
  			break;
  		case GlobalConstants.POST_PROFILE_INFO:
  			listener.onTaskCompletedJSON(result, id_request);
  			break;
  		case GlobalConstants.GET_SHOP:
  			listener.onTaskCompletedJSON(result, id_request);
  			break;	
  		case GlobalConstants.POST_SEARCH:
  			listener.onTaskCompletedJSON(result, id_request);
  			break;
  		//TODO Errors part	
  		case GlobalConstants.NO_INTERNET_CONNECTION:
  			Log.v("123", "VVV + onPostExecute NO_INTERNET_CONNECTION" );
  			listener.onTaskCompletedJSON(result, id_request);
			break;
			
  		case GlobalConstants.ERROR_401:
  			listener.onTaskCompletedJSON(result, id_request);
			break;
			
  		case GlobalConstants.ERROR_503:
  			listener.onTaskCompletedJSON(result, id_request);
			break;
			
  		case GlobalConstants.UNDEFINED_ERROR:
  			listener.onTaskCompletedJSON(result, id_request);
			break;
      }
	}
	
	public String readAPIObject(String url) {
		Log.v(LOG_TAG, "METRO readAPIObject URL= " + url);
		HttpGet httpget = null;
		HttpPost httppost = null;
		HttpDelete httpdel = null;
		HttpResponse response = null;
		InputStream content = null;
		Uri.Builder uriBuilder;

		HttpClient client = getHTTPClient(null);
		String UserAgentName = context.getString(R.string.app_Version) + " Android/" + Build.VERSION.RELEASE;
		
		switch (http_method) {
		case GlobalConstants.HTTP_GET:
			uriBuilder = Uri.parse(url).buildUpon();
			httpget = new HttpGet(uriBuilder.build().toString());	
			//Check for token
			if (MeApp.regData.getToken() != null){
				httpget.addHeader("Authorization", "Bearer " + MeApp.regData.getToken());
				Log.v(LOG_TAG, "METRO HTTP_GET getToken= " + MeApp.regData.getToken());	
            }
			//Set build version release			
			httpget.addHeader("User-agent", UserAgentName);			
			Log.v(LOG_TAG, "METRO HTTP_GET UserAgentName= " + UserAgentName);	
			
			/*if(params != null && !params.isEmpty()){
				for ( String key : params.keySet() ) {
					xHash = xHash + key + ",";
				}
			}*/
			httpget.addHeader("X-Hash", getEncodeHeader(xHash));
			Log.v("TAG", "METROX GET String XHASH=" + xHash + " encoded XHASH=" + getEncodeHeader(xHash));
			break;
		case GlobalConstants.HTTP_POST:
			httppost = new HttpPost(url);
			//Check for token
			if (MeApp.regData.getToken() != null){
				httppost.addHeader("Authorization", "Bearer " + MeApp.regData.getToken());
				Log.v(LOG_TAG, "METRO HTTP_POST getToken= " + MeApp.regData.getToken());
            }
			//Set build version release			
			httppost.addHeader("User-agent", UserAgentName);
						
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			
			if(params != null && !params.isEmpty()){
				for(Map.Entry<String, String> entry: params.entrySet()) {
					nameValuePairs.add(new BasicNameValuePair(entry.getKey().toString(), entry.getValue().toString()));
					Log.v("key, value", "METRO key =" + entry.getKey().toString() + "  Value=" + entry.getValue().toString());
				}				
				try {
					httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
			/*if(params != null && !params.isEmpty()){
				for ( String key : params.keySet() ) {
					xHash = xHash + key + ",";
				}
			}*/
			httppost.addHeader("X-Hash", getEncodeHeader(xHash));
			Log.v("TAG", "METROX POST String XHASH=" + xHash + " encoded XHASH=" + getEncodeHeader(xHash));
			
			break;
		case GlobalConstants.HTTP_DEL:
			uriBuilder = Uri.parse(url).buildUpon();
			httpdel = new HttpDelete(uriBuilder.build().toString());	
			//Check for token
			if (MeApp.regData.getToken() != null){
				httpdel.addHeader("Authorization", "Bearer " + MeApp.regData.getToken());
				Log.v(LOG_TAG, "METRO HTTP_DEL getToken= " + MeApp.regData.getToken());
            }
			//Set build version release	
			
			httpdel.addHeader("User-agent", UserAgentName);
			Log.v(LOG_TAG, "METRO HTTP_DEL UserAgentName= " + UserAgentName);
			
			/*if(params != null && !params.isEmpty()){
				for ( String key : params.keySet() ) {
					xHash = xHash + key + ",";
				}
			}*/
			httpdel.addHeader("X-Hash", getEncodeHeader(xHash));
			Log.v("TAG", "METROX DEL String XHASH=" + xHash + " encoded XHASH=" + getEncodeHeader(xHash));
			break;
		}
		
		try {
			Log.e("HTTPAsyncTask params", ""+params);
			switch (http_method) {
			case GlobalConstants.HTTP_POST:
				response = client.execute(httppost); // POST
				break;
			case GlobalConstants.HTTP_GET:	
				response = client.execute(httpget);  // GET
				break;
			case GlobalConstants.HTTP_DEL:	
				response = client.execute(httpdel);  // DEL
				break;
			}
			
				StatusLine statusLine = response.getStatusLine();
				responseCode = statusLine.getStatusCode();
				Log.e("statusCode", " " + responseCode);
				
			if (responseCode == 200 | responseCode == 204 | responseCode == 422) {
				HttpEntity entity = response.getEntity();
				content = entity.getContent();
			} else if (responseCode == GlobalConstants.ERROR_401){
				id_request = GlobalConstants.ERROR_401;
				return null;
			} else if (responseCode == GlobalConstants.ERROR_500 || 
					responseCode == GlobalConstants.ERROR_502 ||
					responseCode == GlobalConstants.ERROR_503){
				id_request = GlobalConstants.ERROR_503;
				return null;
			} else {
				id_request = GlobalConstants.UNDEFINED_ERROR;
				return null;
			}
			
			
		    } catch (ClientProtocolException e) {
		    	id_request = GlobalConstants.UNDEFINED_ERROR;
		    	Log.v(LOG_TAG, "121212 METRO ClientProtocolException " + e);
		    	e.printStackTrace();
		    	return null;
		    } catch (ConnectTimeoutException c) {
		    	id_request = GlobalConstants.UNDEFINED_ERROR;
		    	Log.v(LOG_TAG, "121212 METRO ConnectTimeoutException " + c);
		    	return null;
		    } catch (IOException e) {
		    	id_request = GlobalConstants.UNDEFINED_ERROR;
		    	Log.v(LOG_TAG, "121212 METRO IOException " + e);
		    	e.printStackTrace();
		    	return null;
		    } catch (Exception c) {
		    	id_request = GlobalConstants.UNDEFINED_ERROR;
		    	Log.v(LOG_TAG, "121212 METRO Exception " + c);
		    	c.printStackTrace();
		    	return null;
		    }
		String output = convertStreamToString(content);
		Log.v(LOG_TAG, "METRO statusCode=" + responseCode);
		Log.v(LOG_TAG, "METRO content=" + output);
		return output;// content.toString();
	}

	
	protected HttpClient getHTTPClient(String extraParams) {
		HttpClient client = null;
			try
			{
				SchemeRegistry Current_Scheme = new SchemeRegistry();
				HttpParams Current_Params = new BasicHttpParams();
				HttpConnectionParams.setConnectionTimeout(Current_Params, new Integer(5000));
	            HttpProtocolParams.setVersion(Current_Params, HttpVersion.HTTP_1_1);
	            HttpProtocolParams.setContentCharset(Current_Params, HTTP.UTF_8);
	            
	            //Current_Params.setParameter("X-Hash", getEncodeHeader(extraParams));
	            
	            //Log.v(LOG_TAG, "METRO X-Hash = " + getEncodeHeader(extraParams));
				
				Naive_SSLSocketFactory sf = new Naive_SSLSocketFactory();
				sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
				
				Current_Scheme.register(new Scheme("https", sf, 443));
				//Current_Scheme.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
				ThreadSafeClientConnManager Current_Manager = 
						new ThreadSafeClientConnManager(Current_Params, Current_Scheme);								
				client = new DefaultHttpClient(Current_Manager, Current_Params);
			}
			catch(Exception e)
			{
				Log.v(LOG_TAG, "METRO getHTTPClientException = " + e);
				e.printStackTrace();
			}
			
		return  client;
	}

    void responceParser(String responce){
    	switch (id_request){
    	case GlobalConstants.CARD_HAS_PROFILE:
    		try {
    			Log.v("Tag", "1111 in assynk responce =" + responce);
				JSONObject data = new JSONObject(responce);
				Log.v("Tag", "1111 in assynk data =" + data);
				MeApp.regData.setStatus(data.optInt("status"));
				Log.v("Tag", "1111 in assynk =" + MeApp.regData.getStatus());
				switch(MeApp.regData.getStatus()){
					case 0:						
						MeApp.regData.setCard_number(data.optString("card_number"));
						MeApp.regData.setMessage(data.optString("message"));						
						break;
					case 1:
						MeApp.regData.setCard_number(data.optString("card_number"));
						MeApp.regData.setMessage(data.optString("message"));
						MeApp.regData.setPhone(data.optString("phone"));
						MeApp.regData.setEmail(data.optString("email"));
						MeApp.regData.setIs_temp_card(data.optBoolean("is_temp_card"));
						MeApp.regData.setName(data.optString("name"));						
						break;
					case 2:
						MeApp.regData.setCard_number(data.optString("card_number"));
						MeApp.regData.setMessage(data.optString("message"));
						MeApp.regData.setPhone(data.optString("phone"));
						MeApp.regData.setEmail(data.optString("email"));
						MeApp.regData.setName(data.optString("name"));						
						break;
				}	
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.v("111", "121212  CARD_HAS_PROFILE JSONException=" + e);
			}
    		
			break;		
		case GlobalConstants.REGISTER_CARD:
    		try {
				JSONObject data = new JSONObject(responce);
				boolean status = data.has("success");
				if(status){
					MeApp.regData.setSuccess(status);					
				} else {
					MeApp.regData.setSuccess(status);
					MeApp.regData.setError(data.optBoolean("error"));
					MeApp.regData.setCode(data.optInt("code"));
					MeApp.regData.setMessage(data.optString("message"));
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.v("111", "121212  REGISTER_CARD JSONException=" + e);
			}
			

			break;			
		case GlobalConstants.RESET_PASSWORD:
    		try {
				JSONObject data = new JSONObject(responce);
				boolean status = data.has("success");
				Log.v("1111", "121212 data.has(success)=" + status);
				if(status){
					MeApp.regData.setSuccess(status);					
				} else {
					MeApp.regData.setSuccess(status);
					MeApp.regData.setError(data.optBoolean("error"));
					MeApp.regData.setCode(data.optInt("code"));
					MeApp.regData.setMessage(data.optString("message"));
					Log.v("111", "121212  + MeApp.regData.setCode=" + MeApp.regData.getCode());
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.v("111", "121212  RESET_PASSWORD JSONException=" + e);
			}

			break;			
		case GlobalConstants.FEEDBACK:
			Log.v("111", "121212  FEEDBACK responce=" + responce);
    		try {
				JSONObject data = new JSONObject(responce);
				boolean status = data.has("success");
				if(status){
					MeApp.regData.setSuccess(status);					
				} else {
					MeApp.regData.setSuccess(status);
					MeApp.regData.setError(data.optBoolean("error"));
					MeApp.regData.setCode(data.optInt("code"));
					MeApp.regData.setMessage(data.optString("message"));
				}	
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.v("111", "121212  FEEDBACK JSONException=" + e);
			}

			break;			
		case GlobalConstants.AUTH:
    		try {
				JSONObject data = new JSONObject(responce);
				boolean status = data.has("token");
				if(status){
					
					MeApp.regData.setSuccess(status);
					MeApp.regData.setToken(data.optString("token"));
					MeApp.regData.getUser().setCount_basket(data.optInt("cart_items_count"));
					
					JSONObject user_data = data.getJSONObject("user");
					MeApp.regData.getUser().setId(user_data.optInt("id"));
					MeApp.regData.getUser().setName(user_data.optString("name"));
					MeApp.regData.getUser().setLegal_entity_name(user_data.optString("legal_entity_name"));
					MeApp.regData.getUser().setCard_num(user_data.optString("card_num"));
					MeApp.regData.getUser().setEmail(user_data.optString("email"));
					MeApp.regData.getUser().setPhone(user_data.optString("phone"));
					MeApp.regData.getUser().setLocale(user_data.optString("locale"));
					MeApp.regData.getUser().setShop_id(user_data.optInt("shop_id"));
					MeApp.regData.getUser().setTemp_card(user_data.optBoolean("temp_card"));
					if (!user_data.isNull("temp_card_valid_to")){
						MeApp.regData.getUser().setValid_to(user_data.optString("temp_card_valid_to"));
					}
					if (!user_data.isNull("region")){
						JSONObject regionData = user_data.getJSONObject("region");
						MeApp.regData.getUser().getRegion().setRegionId(regionData.optInt("id"));
						MeApp.regData.getUser().getRegion().setRegionTitle(regionData.optString("title"));
					} else {
						MeApp.regData.getUser().getRegion().setRegionId(-1);
						MeApp.regData.getUser().getRegion().setRegionTitle(null);
					}
					
				} else {
					MeApp.regData.setSuccess(status);
					MeApp.regData.setError(data.optBoolean("error"));
					MeApp.regData.setCode(data.optInt("code"));
					MeApp.regData.setMessage(data.optString("message"));
				}		
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.v("111", "121212  AUTH JSONException=" + e);
			}
			break;
		case GlobalConstants.CATALOG:
			try {
				JSONObject data = new JSONObject(responce);
				boolean status = data.has("items");
				if(status){
					
				} else {

				}		
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
    	break;
    	}
    	
    }    
	
    String getEncodeHeader(String extraParams) {
    	String ps = "Vbp9Tjz4qspYZoI0hTDvBUVbp";
    	if (extraParams!=null){
    		ps = extraParams + ps;
    	}
    	String result = null;
    	byte[] data;
		try {
			data = ps.getBytes("UTF-8");			
			String tmp = Base64.encodeToString(data, Base64.NO_WRAP);						
			result = Utils.SHA1(tmp);	
			Log.v("TAG", "METROX ps=" + ps + "  SHA1=" + result);
			return result;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.v("TAG", "METROX Exception=" + e);
		}
		
    	return result;
    }

    private String convertStreamToString(InputStream is) {
    	if (is==null) return null;
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.v("Tag", "1111 convertStreamToString =" + sb.toString());
        return sb.toString();
    }

    private class X509_Trust_Manager implements X509TrustManager
    {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType)
        throws CertificateException {}
        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            try {
                chain[0].checkValidity();
            } catch (Exception e) {
                throw new CertificateException("Certificate not valid or trusted.");
            }
        }
        
//        public void checkServerTrusted(X509Certificate[] chain, String authType)
//        throws CertificateException {}
        
        @Override
        public X509Certificate[] getAcceptedIssuers()
        {
            return null;
        }
    };

    public class Naive_SSLSocketFactory extends SSLSocketFactory
    {
        protected SSLContext Cur_SSL_Context = SSLContext.getInstance("TLS");
        public Naive_SSLSocketFactory()
        throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException
        {
            super(null, null, null, null, null, null);
            Cur_SSL_Context.init(null, new TrustManager[]{new X509_Trust_Manager()}, null);
        }
        @Override
        public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException
        {
            return Cur_SSL_Context.getSocketFactory().createSocket(socket, host, port, autoClose);
        }
        @Override
        public Socket createSocket() throws IOException
        {
            return Cur_SSL_Context.getSocketFactory().createSocket();
        }
    }

    
    void runNoInternetDialog(){
    	BottomDialog.BottomSelectDialog(context, 
    			noInternetConn, 
    			context.getString(R.string.dialog_NoInternetTitle), 
    			context.getString(R.string.dialog_NoInternetText), 
    			context.getString(R.string.dialog_NoInternetSettings), 
    			context.getString(R.string.dialog_NoInternetQuit));
    }
    
    Handler noInternetConn = new Handler() {

	    @Override
	    public void handleMessage(Message msg) {
	        if (msg != null) {
	            // Check for dialog box responses
	            if (msg.what == GlobalConstants.retCodeLeft) {
	            	//YES
	            	Intent viewIntent = new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS);
	            	context.startActivity(viewIntent);
	            }
	            else if (msg.what == GlobalConstants.retCodeRight) {
	                //Quit
	            	Activity activity = (Activity) context;
	            	activity.finishAffinity();
	            }
	            else if (msg.what == GlobalConstants.retCodeClose) {
	                //CLOSED

	            }
	        }
	    }
	};
}
