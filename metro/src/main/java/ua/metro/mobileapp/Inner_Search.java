package ua.metro.mobileapp;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import ua.metro.mobileapp.HttpMetods.HTTPAsynkTask;
import ua.metro.mobileapp.HttpMetods.UserState;
import ua.metro.mobileapp.UI.BottomDialog;
import ua.metro.mobileapp.adapters.GdsListAdapter;
import ua.metro.mobileapp.datamodel.GdsData;
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
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.squareup.picasso.Picasso;

public class Inner_Search extends Activity implements OnTaskCompleted{
	
	String LOG_TAG = "Inner_Search_LOG_TAG";
	Typeface fontRegular, fontMedium, fontBold, fontLight;
	
	HashMap<Integer,GdsData> dataSearchGds;
	ListView searchGdsListView;
	View footerListView;
	boolean loadMore = true;
	
	int idCatalog;
	
	//int widthDevice;
	
	TextView textSearchTextView;
	TextView countResultTextView;

	ImageButton iconBackImageButton;
	EditText searchEditText;
	ImageButton searchButton;

	//Button loadMoreBtn;
	LinearLayout totalLayout;
	
	int resultCount, pageCount;
	String searchText;
	int page = 1;
	ProgressDialog loader;
	SearchGdsListAdapter adapter;


	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        UserState.restoreUserState(this);
        
        setContentView(R.layout.inner_search);  
        
        Log.v(LOG_TAG,"GGGG onCreate ");	
        
        Utils.showActionBar(this, true);
        Utils.setBottomMenu(this,0);
   
    	fontRegular = FontCache.get("fonts/clearsansregular.ttf", getBaseContext());
        fontMedium = FontCache.get("fonts/clearsansmedium.ttf", getBaseContext());	
        fontBold = FontCache.get("fonts/clearsansbold.ttf", getBaseContext());
        fontLight = FontCache.get("fonts/clearsanslight.ttf", getBaseContext());
        
        iconBackImageButton = (ImageButton) findViewById(R.id.iconBackImageButton);
        TextView subTitleTextView = (TextView) findViewById(R.id.subTitleTextView);
   	 	
        totalLayout = (LinearLayout) findViewById(R.id.totalLayout);
        
        TextView label1TextView = (TextView) findViewById(R.id.label1TextView);
        TextView label2TextView = (TextView) findViewById(R.id.label2TextView);
        textSearchTextView = (TextView) findViewById(R.id.textSearchTextView);
        countResultTextView = (TextView) findViewById(R.id.countResultTextView);
        searchEditText = (EditText) findViewById(R.id.searchEditText);
        searchButton = (ImageButton) findViewById(R.id.searchButton);
	    
	    subTitleTextView.setTypeface(fontMedium);
	    subTitleTextView.setText(getString(R.string.search_result));
    
	    label1TextView.setTypeface(fontRegular);
	    label2TextView.setTypeface(fontRegular);
	    textSearchTextView.setTypeface(fontBold);
	    countResultTextView.setTypeface(fontBold);
	  
	    searchGdsListView = (ListView) findViewById(R.id.searchGdsListView);
	    
	    footerListView = ((LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.load_more, null, false);
	    searchGdsListView.addFooterView(footerListView);
        //loadMoreBtn = (Button) footerListView.findViewById(R.id.loadMoreButton);
        
        searchGdsListView.setOnScrollListener(new OnScrollListener(){

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				if(footerListView.isShown()&&loadMore){
					loadMore = false;
					page++;
					if (page == pageCount || pageCount==0)
						footerListView.setVisibility(View.GONE);
					else
						footerListView.setVisibility(View.VISIBLE);
					LoadMore();
				}
			}
        	
        });
	    
	    View.OnClickListener btnListener = new View.OnClickListener(){
	        public void onClick(View v) {
		        if (v == iconBackImageButton){		            	
	            	finish();
	            }
		        if (v == searchButton){
		        	
		        	page = 1;
		        	
		        	textSearchTextView.setText(null);
					countResultTextView.setText(null);
		        	
		        	//searchGdsListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
		        	//searchGdsListView.setStackFromBottom(true);
	            	
					toSearch(1);					
					
					View view = Inner_Search.this.getCurrentFocus();
					if (view != null) {  
					    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
					    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
					}
	            }
		        /*
		        if (v == loadMoreBtn){		            	
	            	page++;
	            	
	            	toSearch(page);
	            }
		        */
	        }
	        
	    };
	    iconBackImageButton.setOnClickListener(btnListener);
	    searchButton.setOnClickListener(btnListener);
	    //loadMoreBtn.setOnClickListener(btnListener);	
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
	
	private void toSearch(int numberPage){
		
		loader = new ProgressDialog(Inner_Search.this);
		loader.setMessage(getString(R.string.app_Loading));
		loader.show();
		
	    HashMap<String,String> params = new HashMap<String,String>();
    	params.put("query", searchEditText.getText().toString());
		params.put("page", String.valueOf(numberPage));
		
        HTTPAsynkTask mt = new HTTPAsynkTask(Inner_Search.this, 
				GlobalConstants.POST_SEARCH, 
				//null, null, 
				params, 
				Inner_Search.this);
		mt.execute();

	}
	
	private void LoadMore(){
		HashMap<String,String> params = new HashMap<String,String>();
    	params.put("query", searchEditText.getText().toString());
		params.put("page", String.valueOf(page));
		
        HTTPAsynkTask mt = new HTTPAsynkTask(Inner_Search.this, 
				GlobalConstants.POST_SEARCH, 
				//null, null, 
				params, 
				Inner_Search.this);
		mt.execute();
	}
	
	
	private void fillData(){
		
		textSearchTextView.setText(searchEditText.getText().toString());
		countResultTextView.setText(Utils.plural(resultCount, getString(R.string.search_result1), getString(R.string.search_result2), getString(R.string.search_result3)));
		
		if (searchEditText.getText().toString().length()>0 || resultCount>0)
			totalLayout.setVisibility(View.VISIBLE);
		else
			totalLayout.setVisibility(View.GONE);
		
		/*
		if (page>1){
			searchGdsListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
			searchGdsListView.setStackFromBottom(true);
		}else
			searchGdsListView.setStackFromBottom(false);
			*/

	}
	
	 @SuppressLint("UseSparseArrays")
	 private HashMap<Integer,GdsData> SearchGdsLisJsonParser(String JsonString){
		 
		 if (page==1)
			 dataSearchGds = new HashMap<Integer,GdsData>();
		 
		 try {
			 Log.v(LOG_TAG," ееее BasketGdsLisJsonParser JsonString = "+JsonString);
			 JSONObject object = new JSONObject(JsonString);
			 Log.v(LOG_TAG,"GGGG JsonString "+JsonString);
			 resultCount = object.optInt("total_count");
			 pageCount = object.optInt("pages_count");
				
			 JSONArray goods = object.getJSONArray("items");

			 int dataCount = dataSearchGds.size();
			 
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
  				
  				if(item.getJSONObject("images").optString("list").length()>0)
  					gdsItem.setGdsUrlImage(item.getJSONObject("images").optString("list"));
  				
  				/*if(item.optDouble("old_price")>0)
  					gdsItem.setGdsPriceOld(item.optDouble("old_price"));  				
  				
  				if(item.optString("discount_type").length()>0)
  					gdsItem.setDiscountType(item.optString("discount_type"));
  				if(item.optString("discount_title").length()>0)
  					gdsItem.setDiscountTitle(item.optString("discount_title"));*/
  		  				
  				dataSearchGds.put(dataCount+i, gdsItem);
    		}
    		
    		Log.v(LOG_TAG,"GGGG dataSearchGds = "+dataSearchGds);	
    		
		}catch(Exception e){
    		Log.v(LOG_TAG,"json parser error"+e.toString());
    		BottomDialog.BottomSelectDialog(Inner_Search.this, 
					error503, 
	    			getString(R.string.dialog_Server503Title), 
	    			getString(R.string.dialog_Server503Text), 
	    			getString(R.string.dialog_Server503Left), 
	    			getString(R.string.dialog_Server503Right));
		}
	 	return dataSearchGds;		 
	 }
	 
	 public class SearchGdsListAdapter extends BaseAdapter {
			Typeface fontRegular, fontMedium, fontLight;
			
			String LOG_TAG = this.getClass().getName()+"LOG";
			LayoutInflater inflator;
			HashMap<Integer,GdsData> dataGds;
			
			public SearchGdsListAdapter(Context mContext, HashMap<Integer,GdsData> data){
				 this.dataGds = data;
			}
			
			@Override
			  public void notifyDataSetChanged() {
			    super.notifyDataSetChanged();
			  }
			 
			public class ViewHolder{
				 public ImageView gdsImage;
				 public TextView gdsTitle;
				 public TextView gdsPriceUah;
				 public TextView gdsPriceCoins;
				 public TextView gdsPriceCurrency;
			}
			
			 public Object getItem(int position){
				 return position;
			 }
			 
			 public View getView(final int position, View convertView, ViewGroup parent){
				 View view = convertView;
				 final ViewHolder holder;
				 
				  if (convertView == null) {
					  holder = new ViewHolder();
					  
					  LayoutInflater inflater = LayoutInflater.from(parent.getContext());
					  
					  view =	inflater.inflate(R.layout.cell_gds_for_search, parent, false);

					  holder.gdsImage = (ImageView) view.findViewById(R.id.gdsImage);
					  holder.gdsTitle = (TextView) view.findViewById(R.id.gdsTitle);
					  holder.gdsPriceUah = (TextView) view.findViewById(R.id.gdsPriceUah);
					  holder.gdsPriceCoins = (TextView) view.findViewById(R.id.gdsPriceCoins);
					  holder.gdsPriceCurrency = (TextView) view.findViewById(R.id.gdsPriceCurrency);
	
					  holder.gdsTitle.setTypeface(fontRegular);
					  holder.gdsPriceUah.setTypeface(fontMedium);
					  holder.gdsPriceCoins.setTypeface(fontMedium);
					  holder.gdsPriceCurrency.setTypeface(fontRegular);
								  		
					  view.setTag(holder);
					  
				  } else{
					  holder = (ViewHolder) view.getTag();
				  }
				  
				  holder.gdsTitle.setText(dataGds.get(position).getGdsTitle());
				  
				  holder.gdsPriceUah.setText(dataGds.get(position).getGdsPriceUah());
				  holder.gdsPriceCoins.setText(dataGds.get(position).getGdsPriceCoins());
				 
				 /* Transformation transformation = new Transformation() {
				        @Override
				        public Bitmap transform(Bitmap source) {
				            int targetWidth = holder.gdsImage.getWidth();
				            double aspectRatio = (double) source.getHeight() / (double) source.getWidth();
				            int targetHeight = (int) (targetWidth * aspectRatio);
				            Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
				            if (result != source) {
				                // Same bitmap is returned if sizes are the same
				                source.recycle();
				            }
				            return result;
				        }
				        @Override
				        public String key() {
				            return "transformation" + " desiredWidth";
				        }
				    };

				    Picasso.with(view.getContext())
				        .load(dataGds.get(position).getGdsUrlImage())
				        //.error(android.R.drawable.stat_notify_error)
				        .placeholder(R.drawable.no_photo)
						.error(R.drawable.no_photo)
				        .transform(transformation)
				        .into(holder.gdsImage, new Callback() {
				            @Override
				            public void onSuccess() {
				                //holder.progressBar_picture.setVisibility(View.GONE);
				            }
				            @Override
				            public void onError() {
				                Log.e("LOGTAG", "error");
				                //holder.progressBar_picture.setVisibility(View.GONE);
				            }
				    });*/
				  
				  
				  Picasso.with(view.getContext()).load(dataGds.get(position).getGdsUrlImage())
				  	.placeholder(R.drawable.no_photo)
				  	.error(R.drawable.no_photo)
				  	.into(holder.gdsImage);
				  	
				  
				  return view;
				}
			
				@Override
				public int getCount() {	
					if (dataGds!=null)
						return dataGds.size();
					else
						return 0;				
				}

				@Override
				public long getItemId(int arg0) {
					return arg0;
				}	 
		}

	@Override
	public void onTaskCompleted(int id_request) {
		// TODO Auto-generated method stub			
		if( loader != null && loader.isShowing() )
			loader.dismiss();
	}

	@Override
	public void onTaskCompletedJSON(String json, int id_request) {
		// TODO Auto-generated method stub
		if( loader != null && loader.isShowing() )
			loader.dismiss();
		
		switch(id_request){
			case GlobalConstants.POST_SEARCH:
				
				dataSearchGds = SearchGdsLisJsonParser(json);
				
				fillData();				
							    
			    if(page == 1) {
			    	adapter = new SearchGdsListAdapter(this, dataSearchGds);
				    searchGdsListView.setAdapter(adapter);
				    if(pageCount>1){
				    	footerListView.setVisibility(View.VISIBLE);
				    } else {
				    	footerListView.setVisibility(View.GONE);
				    }
				} else {
					adapter.notifyDataSetChanged();
					loadMore = true;
				}
			    
			    searchGdsListView.setOnItemClickListener(new OnItemClickListener(){
		            @Override 
		            public void onItemClick(AdapterView<?> arg0, View arg1,int position, long arg3){ 
		               	Intent intent = new Intent (Inner_Search.this,Inner_GdsItem.class);
			  			intent.putExtra("idGds",dataSearchGds.get(position).getGdsId());
			  			intent.putExtra("idCatalog",dataSearchGds.get(position).getCatalogId());
			  			startActivity(intent);	  			
			  	    }
		        });	
		    break;
		    
		  //TODO ============ERRORS DEPLOYMENT==========
			case GlobalConstants.NO_INTERNET_CONNECTION:
				BottomDialog.BottomSelectDialog(Inner_Search.this, 
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
	        	startActivity(new Intent(Inner_Search.this, Out_RegInStart.class)); 
				finish();
				break;
			
			case GlobalConstants.ERROR_503:
				BottomDialog.BottomSelectDialog(Inner_Search.this, 
						error503, 
		    			getString(R.string.dialog_Server503Title), 
		    			getString(R.string.dialog_Server503Text), 
		    			getString(R.string.dialog_Server503Left), 
		    			getString(R.string.dialog_Server503Right));
			break;
			
			default:
				BottomDialog.BottomSelectDialog(Inner_Search.this, 
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
	            	Inner_Search.this.finishAffinity();
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
	            	Inner_Search.this.finishAffinity();
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
		tracker.setScreenName("search");
		tracker.send(new HitBuilders.EventBuilder().build());
	}
	
}
