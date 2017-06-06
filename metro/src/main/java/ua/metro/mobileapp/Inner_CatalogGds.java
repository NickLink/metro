package ua.metro.mobileapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;

import ua.metro.mobileapp.HttpMetods.HTTPAsynkTask;
import ua.metro.mobileapp.HttpMetods.UserState;
import ua.metro.mobileapp.UI.BottomDialog;
import ua.metro.mobileapp.UI.GetDeviceSize;
import ua.metro.mobileapp.adapters.GdsListAdapter;
import ua.metro.mobileapp.application.MeApp;
import ua.metro.mobileapp.datamodel.GdsData;

public class Inner_CatalogGds extends Activity implements OnTaskCompleted{
	String LOG_TAG = Inner_CatalogGds.class.getSimpleName();
	
	Typeface fontRegular, fontMedium, fontLight;
	
	HashMap<Integer,GdsData> dataCatalogGds;
	ListView catalogGdsListView;
	View footerView;
	
	Button allGdsButton;
	Button filterButton;
	//Button loadMoreBtn;
	TextView subTitleTextView;
	ProgressBar daysUntilprogressBar;
	TextView countDaysLeftTextView, labelDaysTextView;
	ImageButton iconBackImageButton;
	
	RelativeLayout emptyCatalogsLayout;
	RelativeLayout mainLayout;
	
	static int idCatalog;
	int idCategory;
	static int page = 1;
		
	int widthDevice, heightDevice;
	String catalogTitle;
	Date catalogDateStart;
	Date catalogDateEnd;
	int daysUntil;
	int daysDuration;
	int pages = 0;
	
	ProgressDialog loader;
	GdsListAdapter adapter;
	
	static final int APPLAY_FILTER = 0;
	boolean loadMore = true;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        UserState.restoreUserState(this);
        
        setContentView(R.layout.inner_catalog_gds);      
        
        Log.v(LOG_TAG," 電電 onCreate");
        
        Utils.showActionBar(this, true);
        Utils.setBottomMenu(this,0);
        
        GetDeviceSize a = new GetDeviceSize(this);
		widthDevice = a.getX();
		heightDevice = a.getY();
		
		
        Intent intent = getIntent();
        idCatalog = intent.getIntExtra("idCatalog",0);
        idCategory = intent.getIntExtra("idCategory",0);        
        page = intent.getIntExtra("page",1);
        
        fontRegular = FontCache.get("fonts/clearsansregular.ttf", getBaseContext());
        fontMedium = FontCache.get("fonts/clearsansmedium.ttf", getBaseContext());	
        fontLight = FontCache.get("fonts/clearsanslight.ttf", getBaseContext());	
        
        subTitleTextView = (TextView) findViewById(R.id.subTitleTextView);
        daysUntilprogressBar = (ProgressBar) findViewById(R.id.daysUntilprogressBar);
        countDaysLeftTextView = (TextView) findViewById(R.id.countDaysLeftTextView);
        labelDaysTextView= (TextView) findViewById(R.id.labelDaysTextView);
        iconBackImageButton = (ImageButton) findViewById(R.id.iconBackImageButton);
        
        emptyCatalogsLayout = (RelativeLayout) findViewById(R.id.emptyCatalogsLayout);
        mainLayout = (RelativeLayout) findViewById(R.id.mainLayout);
        
        filterButton = (Button) findViewById(R.id.filterButton);	
	    allGdsButton = (Button) findViewById(R.id.allGdsButton);
	    
        subTitleTextView.setTypeface(fontMedium);
        countDaysLeftTextView.setTypeface(fontMedium);
        labelDaysTextView.setTypeface(fontMedium);
          
        HashMap<String,String> params = new HashMap<String,String>();
    	params.put("catalog_id", String.valueOf(idCatalog));
    	if(idCategory>0)
    		params.put("category_id", String.valueOf(idCategory));
    	params.put("page", String.valueOf(page));
    	
    	Log.v("123", "result restAPI=> idCatalog=" + String.valueOf(idCatalog) + " category_id=" + 
    			String.valueOf(idCategory) + " page=" + String.valueOf(page));
    	
    	loader = new ProgressDialog(Inner_CatalogGds.this);
		loader.setMessage(getString(R.string.app_Loading));
		loader.show();
		        
        HTTPAsynkTask mt = new HTTPAsynkTask(Inner_CatalogGds.this, 
				GlobalConstants.CATALOG_ID, 
				//null, null, 
				params, 
				Inner_CatalogGds.this);
		mt.execute();
        
        catalogGdsListView = (ListView) findViewById(R.id.catalogGdsListView);
        footerView = ((LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.load_more, null, false);
        catalogGdsListView.addFooterView(footerView);
        //TODO         loadMoreBtn = (Button) footerView.findViewById(R.id.loadMoreButton);
        catalogGdsListView.setOnScrollListener(new OnScrollListener(){

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				if(footerView.isShown()&&loadMore){
					loadMore = false;
					page++;
					if (page == pages)
						footerView.setVisibility(View.GONE);
					else
						footerView.setVisibility(View.VISIBLE);
	            	HashMap<String,String> params = new HashMap<String,String>();
	             	params.put("catalog_id", String.valueOf(idCatalog));
	             	
	             	if(idCategory>0)
	             		params.put("category_id", String.valueOf(idCategory));
	             	params.put("page", String.valueOf(page));
	         		        
	                 HTTPAsynkTask mt = new HTTPAsynkTask(Inner_CatalogGds.this, 
	         				GlobalConstants.CATALOG_ID, 
	         				//null, null, 
	         				params, 
	         				Inner_CatalogGds.this);
	         		mt.execute();
				}
			}
        	
        });
        
    	View.OnClickListener btnListener = new View.OnClickListener(){
	        public void onClick(View v) {
	        	
	            if (v == iconBackImageButton){		            	
	            	finish();
	            }
	        	if (v == subTitleTextView){		            	
	            	finish();
	            }
	            /*if (v == loadMoreBtn){		            	
	            	page++;
	            	
	            	HashMap<String,String> params = new HashMap<String,String>();
	             	params.put("catalog_id", String.valueOf(idCatalog));
	             	
	             	if(idCategory>0)
	             		params.put("category_id", String.valueOf(idCategory));
	             	params.put("page", String.valueOf(page));
	         		        
	                 HTTPAsynkTask mt = new HTTPAsynkTask(Inner_CatalogGds.this, 
	         				GlobalConstants.CATALOG_ID, 
	         				//null, null, 
	         				params, 
	         				Inner_CatalogGds.this);
	         		mt.execute();
	            } */
	            if (v == allGdsButton){		            	
	            	page = 1;
	            	idCategory = 0;
	            	
	            	//catalogGdsListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
	            	//catalogGdsListView.setStackFromBottom(true);
	            	
	            	HashMap<String,String> params = new HashMap<String,String>();
	             	params.put("catalog_id", String.valueOf(idCatalog));
	               	params.put("page", String.valueOf(page));
	               	
	               	loader = new ProgressDialog(Inner_CatalogGds.this);
	        		loader.setMessage(getString(R.string.app_Loading));
	        		loader.show();
	         		        
	                 HTTPAsynkTask mt = new HTTPAsynkTask(Inner_CatalogGds.this, 
	         				GlobalConstants.CATALOG_ID, 
	         				//null, null, 
	         				params, 
	         				Inner_CatalogGds.this);
	         		mt.execute();
	            }	            
	        }
	    };

	    subTitleTextView.setOnClickListener(btnListener);	
	    iconBackImageButton.setOnClickListener(btnListener);
	    allGdsButton.setOnClickListener(btnListener);
	    //TODO loadMoreBtn.setOnClickListener(btnListener);	
	    	
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
	
	private void fillData(){
		
		if(dataCatalogGds.size()>0){
			emptyCatalogsLayout.setVisibility(View.GONE);
			mainLayout.setVisibility(View.VISIBLE);
		}else{
			emptyCatalogsLayout.setVisibility(View.VISIBLE);
			mainLayout.setVisibility(View.GONE);			
		}
		
		subTitleTextView.setText(catalogTitle);
		countDaysLeftTextView.setText(String.valueOf(daysUntil));
		
		labelDaysTextView.setText(Utils.pluralNoInt(daysUntil, 
				getString(R.string.days1), 
				getString(R.string.days2_4), 
				getString(R.string.days)));
		
		Log.v(LOG_TAG," 電電 CatalogGdsLisJsonParser daysDuration = "+daysDuration);
		Log.v(LOG_TAG," 電電 CatalogGdsLisJsonParser daysUntil = "+daysUntil);
		daysUntilprogressBar.setMax(daysDuration);
		daysUntilprogressBar.setProgress(daysUntil); //daysDuration-daysUntil
		
		catalogGdsListView.setOnItemClickListener(new OnItemClickListener(){
            @Override 
            public void onItemClick(AdapterView<?> arg0, View arg1,int position, long arg3)
            { 
               	Intent intent = new Intent (Inner_CatalogGds.this,Inner_GdsItem.class);
               	intent.putExtra("idGds",dataCatalogGds.get(position).getGdsId());
	  			intent.putExtra("idCatalog",dataCatalogGds.get(position).getCatalogId());
	  			//intent.putExtra("titleCatalog",catalogTitle);
	  			//intent.putExtra("daysUntil",daysUntil);
	  			//intent.putExtra("daysDuration",daysDuration);
	  			startActivity(intent);
            }
        });
		//======================!!!!!!!!!!!!!!===========================
		/*
		if (page>1){
			catalogGdsListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
			catalogGdsListView.setStackFromBottom(true);
		}else
			catalogGdsListView.setStackFromBottom(false);
			*/

		
	}
	
	@SuppressLint("UseSparseArrays")
	private void CatalogGdsLisJsonParser(String JsonString){
		 String catalogDateStart_str = null;
		 String catalogDateEnd_str = null;
		// Log.v(LOG_TAG," 電電 CatalogGdsLisJsonParser JsonString = "+JsonString);
		 
		 if (page == 1)
			 dataCatalogGds = new HashMap<Integer,GdsData>();
		 
		 try{
			 JSONObject object = new JSONObject(JsonString);
			 
			 JSONObject catalog = object.getJSONObject("catalog");
			 //Log.v(LOG_TAG," 電電 CatalogGdsLisJsonParser catalog = "+catalog);
			 
			 if(catalog.optInt("id")>0)
				 idCatalog = catalog.optInt("id");
			 
			 if(catalog.optString("start_date").length()>0)
				catalogDateStart_str = catalog.optString("start_date");
			 
			 if(catalog.optString("end_date").length()>0)
				catalogDateEnd_str = catalog.optString("end_date");
			 
			 daysUntil = Utils.getDaysUntil(catalogDateEnd_str);
			 daysDuration = Utils.getDuration(catalogDateStart_str,catalogDateEnd_str);
			 
			 if(catalog.optString("title").length()>0)
				catalogTitle = catalog.optString("title");			 
	      		 
	    	 JSONObject products = object.getJSONObject("products");
	    	 //Log.v(LOG_TAG," 電電 CatalogGdsLisJsonParser products = "+products);
	    	 
	    	 pages = products.getInt("pages");
	    	 Log.v(LOG_TAG," 電電 CatalogGdsLisJsonParser pages = "+pages);
	    	 
	    	 //String next = products.getString("next");
	    	 //Log.v(LOG_TAG," 電電 CatalogGdsLisJsonParser next = "+next);
	    	 
	    	 JSONArray goods = products.getJSONArray("items");
	    	 //Log.v(LOG_TAG," 電電 CatalogGdsLisJsonParser goods = "+goods);
    		
	    	 int dataCount = dataCatalogGds.size();
  			 for (int i=0;i<goods.length();i++) {
  				JSONObject item = goods.getJSONObject(i);
    		
  				GdsData gdsItem = new GdsData();
  				if(item.optInt("catalog_product_id")>0)
  					gdsItem.setGdsId(item.optInt("catalog_product_id"));
  				if(item.optInt("catalog_id")>0)
  					gdsItem.setCatalogId(item.optInt("catalog_id"));
  				if(item.optString("title").length()>0)
  					gdsItem.setGdsTitle(item.optString("title"));
  				
  				if(item.optDouble("price")>0)
  					gdsItem.setGdsPrice(item.optDouble("price"));
  				if(item.optDouble("old_price")>0)
  					gdsItem.setGdsPriceOld(item.optDouble("old_price"));
  				
  				if(item.getJSONObject("images").optString("list").length()>0)
  					gdsItem.setGdsUrlImage(item.getJSONObject("images").optString("list"));
  				
  				if(item.optString("discount").length()>0)
  					gdsItem.setDiscount(item.optString("discount"));
  				if(item.optString("discount_type").length()>0)
  					gdsItem.setDiscountType(item.optString("discount_type"));
  				if(item.optString("discount_title").length()>0)
  					gdsItem.setDiscountTitle(item.optString("discount_title"));
	  		  				
	  			dataCatalogGds.put(dataCount+i, gdsItem);
	  		}
		}catch(Exception e){
			Log.v(LOG_TAG," 電電 CatalogGdsLisJsonParser error = "+e.toString());	
		}
		 
		//Log.v(LOG_TAG," 電電 CatalogGdsLisJsonParser dataCatalogGds = "+dataCatalogGds);
	 }

	@Override
	public void onTaskCompleted(int id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTaskCompletedJSON(final String json, int id_request) {
		// TODO Auto-generated method stub
		
		if( loader != null && loader.isShowing() )
			loader.dismiss();
		
		Log.v("LOG_TAG","Inner_CatalogGds onTaskCompletedJSON json = "+json);
		
		switch(id_request){
			case GlobalConstants.CATALOG_ID:
				CatalogGdsLisJsonParser(json);
				
				fillData();
				
				if(pages == 1){
					footerView.setVisibility(View.GONE);
				}
				
				Log.v(LOG_TAG," 電電 CatalogGdsLisJsonParser dataCatalogGds.size() = "+dataCatalogGds.size());	
			if(page == 1) {
				adapter = new GdsListAdapter(this, dataCatalogGds);
				catalogGdsListView.setAdapter(adapter);
				
			} else {
				adapter.notifyDataSetChanged();
				loadMore = true;
			}
		    
		        filterButton.setOnClickListener(new OnClickListener() {
			    	@Override
			    	public void onClick(View v) {
			    		// TODO Auto-generated method stub
			    		MenuFilter.showMenu(Inner_CatalogGds.this, json, widthDevice);
			    	}
			    });
		    break;
			case GlobalConstants.CART_ADD:
				Boolean result = false;
				int countBasket = 0;
				Log.v(LOG_TAG," 電電 CART_ADD json = "+json);
				try {
					JSONObject object = new JSONObject(json);
					result = object.optBoolean("success");
					
					countBasket = object.optInt("count");
					
					if(countBasket>0){
						MeApp.regData.getUser().setCount_basket(countBasket);
						Utils.showActionBar(this, true);
					}
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
				if(result){
					final Toast toast = Toast.makeText(Inner_CatalogGds.this, 
							getString(R.string.gds_added), 
							Toast.LENGTH_SHORT);
					toast.show();
					Handler handler = new Handler();
		            handler.postDelayed(new Runnable() {
		               @Override
		               public void run() {
		                   toast.cancel(); 
		               }
		            }, 1000);
				}else{
					Toast.makeText(Inner_CatalogGds.this, getString(R.string.error_adding_gds), 
							Toast.LENGTH_SHORT).show();					
				}
		     break;	
		     
		     //TODO ============ERRORS DEPLOYMENT==========
			case GlobalConstants.NO_INTERNET_CONNECTION:
				BottomDialog.BottomSelectDialog(Inner_CatalogGds.this, 
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
	        	startActivity(new Intent(Inner_CatalogGds.this, Out_RegInStart.class)); 
				finish();
				break;
			
			case GlobalConstants.ERROR_503:
				BottomDialog.BottomSelectDialog(Inner_CatalogGds.this, 
						error503, 
		    			getString(R.string.dialog_Server503Title), 
		    			getString(R.string.dialog_Server503Text), 
		    			getString(R.string.dialog_Server503Left), 
		    			getString(R.string.dialog_Server503Right));
			break;
			
			default:
				BottomDialog.BottomSelectDialog(Inner_CatalogGds.this, 
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
	            	Inner_CatalogGds.this.finishAffinity();
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
	            	Inner_CatalogGds.this.finishAffinity();
	            } else if (msg.what == GlobalConstants.retCodeClose) {

	            }
	        }
	    }
	};
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
     super.onActivityResult(requestCode, resultCode, data);
     if(requestCode==GlobalConstants.REQUEST_CODE_INET){	
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
		tracker.setScreenName("catalog");
		tracker.send(new HitBuilders.EventBuilder().build());
	}
	
}
