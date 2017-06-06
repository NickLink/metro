package ua.metro.mobileapp;

import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import ua.metro.mobileapp.HttpMetods.HTTPAsynkTask;
import ua.metro.mobileapp.HttpMetods.UserState;
import ua.metro.mobileapp.UI.BottomDialog;
import ua.metro.mobileapp.application.MeApp;
import ua.metro.mobileapp.datamodel.GdsData;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

public class Inner_Basket extends Activity implements OnTaskCompleted{
	
	String LOG_TAG = "Inner_Basket_LOG_TAG";
	Typeface fontRegular, fontMedium, fontBold, birchCTT;
	
	HashMap<Integer,GdsData> dataBacketGds;
	ListView backetGdsListView;
	
	int idCatalog;
	
	//int widthDevice;
	
	TextView countTextView;
	TextView sumUahTextView;
	TextView sumCoinsTextView;
	TextView sumCurencyTextView;
	ImageButton iconBackImageButton;
	Button addGdsButton;
	RelativeLayout emptyBasketLayout;
	RelativeLayout mainLayout;
	
	int basketCount;
	int basketSumUah, basketSumCoins;
	
	ProgressDialog loader;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        UserState.restoreUserState(this);
        
        setContentView(R.layout.inner_basket);  
        
        Utils.showActionBar(this, true);
        Utils.setBottomMenu(this,0);
        
        loader = new ProgressDialog(Inner_Basket.this);
		loader.setMessage(getString(R.string.app_Loading));
		loader.show();
   
        HTTPAsynkTask mt = new HTTPAsynkTask(Inner_Basket.this, 
				GlobalConstants.CART_GET, 
				//null, null, 
				null, 
				Inner_Basket.this);
		mt.execute();
        
		birchCTT = FontCache.get("fonts/bitch_ctt.ttf", getBaseContext());
        fontRegular = FontCache.get("fonts/clearsansregular.ttf", getBaseContext());
        fontMedium = FontCache.get("fonts/clearsansmedium.ttf", getBaseContext());	
        fontBold = FontCache.get("fonts/clearsansbold.ttf", getBaseContext());
        
        iconBackImageButton = (ImageButton) findViewById(R.id.iconBackImageButton);
        TextView subTitleTextView = (TextView) findViewById(R.id.subTitleTextView);
   	 	
        emptyBasketLayout = (RelativeLayout) findViewById(R.id.emptyBasketLayout);
        mainLayout = (RelativeLayout) findViewById(R.id.mainLayout);
        
        TextView emptyBasketTextView = (TextView) findViewById(R.id.emptyBasketTextView);
        addGdsButton = (Button) findViewById(R.id.addGdsButton);
        
        TextView label1TextView = (TextView) findViewById(R.id.countTextView);
        TextView label2TextView = (TextView) findViewById(R.id.label2TextView);
	    countTextView = (TextView) findViewById(R.id.countTextView);
	    sumUahTextView = (TextView) findViewById(R.id.sumUahTextView);
	    sumCoinsTextView = (TextView) findViewById(R.id.sumCoinsTextView);
	    sumCurencyTextView = (TextView) findViewById(R.id.sumCurencyTextView);
	    
	    subTitleTextView.setTypeface(fontMedium);
	    subTitleTextView.setText(getString(R.string.my_shopping_list));
	    
	    emptyBasketTextView.setTypeface(birchCTT);
	    
	    label1TextView.setTypeface(fontRegular);
	    label2TextView.setTypeface(fontRegular);
	    countTextView.setTypeface(fontBold);
	    sumUahTextView.setTypeface(fontBold);
	    sumCoinsTextView.setTypeface(fontBold);
	    sumCurencyTextView.setTypeface(fontBold);
	 
	    backetGdsListView = (ListView) findViewById(R.id.backetGdsListView);
	    
	    View.OnClickListener btnListener = new View.OnClickListener(){
	        public void onClick(View v) {
	        	if (v == addGdsButton){	
	        		startActivity(new Intent(Inner_Basket.this, Inner_Catalogs.class));
	            	finish();
	            }
	            if (v == iconBackImageButton){		            	
	            	finish();
	            }
	        }
	    };
	    addGdsButton.setOnClickListener(btnListener);
	    iconBackImageButton.setOnClickListener(btnListener);
		
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
	
	private void fillData(){
		countTextView.setText(Utils.plural(basketCount, getString(R.string.gds_title_1), getString(R.string.gds_title_2), getString(R.string.gds_title_3)));
	    sumUahTextView.setText(String.valueOf(basketSumUah));
	    sumCoinsTextView.setText(String.format("%02d",basketSumCoins));	
	    
	}
	
	 @SuppressLint("UseSparseArrays")
	 private HashMap<Integer,GdsData> BasketGdsLisJsonParser(String JsonString){
	
		 dataBacketGds = new HashMap<Integer,GdsData>();
		
		 try{
			 Log.v(LOG_TAG," YOYO BasketGdsLisJsonParser JsonString = "+JsonString);
			 JSONObject object = new JSONObject(JsonString);
		
			 basketCount = object.optInt("count");
			 Double sum = object.optDouble("price");
			 
			 basketSumUah =  object.optInt("price");
			 basketSumCoins = (int) ((sum - basketSumUah)*100);
				
			 JSONObject goods = object.getJSONObject("items");
	    		
	    		//Log.v(LOG_TAG,"GGGG goods ");//+goods);	
	    		Iterator<?> iter = goods.keys();
	    		
	    		//Log.v(LOG_TAG,"GGGG iter ");
	    		int i = 0;
	    		while (iter.hasNext()) {
	    			//JSONObject tmp = iter.next();
	    			//String name = iter.nextName();
	    			
	    			String key = (String) iter.next();
	    			
	    			JSONObject objInner = goods.getJSONObject(key);
	    			Log.v(LOG_TAG,"GGGG objInner =  "+objInner);
	    			
	    			JSONObject item = objInner.getJSONObject("info");
	    			Log.v(LOG_TAG,"GGGG item =  "+item);
	    			
	    			GdsData gdsItem = new GdsData();
	    			
	    			gdsItem.setGdsKey(key);	  				
	    			
	    			Log.v(LOG_TAG,"YOYO item.optInt(product_id) =  "+item.optInt("product_id"));
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
	  				
	  				if(objInner.optString("expired").length()>0)
	  					gdsItem.setExpired(objInner.optBoolean("expired"));
	  				
	  				if(item.optString("discount_type").length()>0)
	  					gdsItem.setDiscountType(item.optString("discount_type"));
	  				if(item.optString("discount_title").length()>0)
	  					gdsItem.setDiscountTitle(item.optString("discount_title"));
	  		  				
	  				dataBacketGds.put(i, gdsItem);
	    			i++;

	    		}
	    		
	    		Log.v(LOG_TAG,"GGGG dataBacketGds = "+dataBacketGds);	
	    		
			}catch(Exception e){
	    		Log.v(LOG_TAG,"json parser error"+e.toString());	
			}
		 	return dataBacketGds;		 
	 }
	 
	 public class BasketGdsListAdapter extends BaseAdapter {
			Typeface fontRegular, fontMedium, fontLight;
			
			String LOG_TAG = this.getClass().getName()+"LOG";
			LayoutInflater inflator;
			HashMap<Integer,GdsData> dataGds;
			
			public BasketGdsListAdapter(Context mContext, HashMap<Integer,GdsData> data){
				 this.dataGds = data;
			}			 
			 
			public class ViewHolder{
				 public ImageView gdsImage;
				 public TextView gdsTitle;
				 public TextView gdsPriceUah;
				 public TextView gdsPriceCoins;
				 public TextView gdsPriceCurrency;
				 public ImageButton gdsDelButton;
				 public ImageView gdsExpiredImage;
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
					  
					  view = inflater.inflate(R.layout.cell_gds_for_basket, parent, false);

					  holder.gdsImage = (ImageView) view.findViewById(R.id.gdsImage);
					  holder.gdsTitle = (TextView) view.findViewById(R.id.gdsTitle);
					  holder.gdsPriceUah = (TextView) view.findViewById(R.id.gdsPriceUah);
					  holder.gdsPriceCoins = (TextView) view.findViewById(R.id.gdsPriceCoins);
					  holder.gdsPriceCurrency = (TextView) view.findViewById(R.id.gdsPriceCurrency);
					  holder.gdsDelButton = (ImageButton) view.findViewById(R.id.gdsDelImageButton);
					  holder.gdsExpiredImage = (ImageView) view.findViewById(R.id.gdsExpiredImage);
					
					  //RelativeLayout.LayoutParams paramsGdsImage = (LayoutParams) holder.gdsImage.getLayoutParams();
					  //paramsGdsImage.width = (int) widthDevice/2;
					  //paramsGdsImage.height = (int) widthDevice/2;
					  //holder.gdsImage.setLayoutParams(paramsGdsImage);
				  
					  holder.gdsTitle.setTypeface(fontRegular);
					  holder.gdsPriceUah.setTypeface(fontMedium);
					  holder.gdsPriceCoins.setTypeface(fontMedium);
					  holder.gdsPriceCurrency.setTypeface(fontRegular);
								  		
					  view.setTag(holder);
					  
				  } else{
					  holder = (ViewHolder) view.getTag();
				  }
				  
				  holder.gdsDelButton.setOnClickListener(new Button.OnClickListener() {  
					  public void onClick(View v){
		                //perform action

						HashMap<String,String> params = new HashMap<String,String>();
				    	params.put("cartRowId", dataGds.get(position).getGdsKey());
				    	
				    	//loader = new ProgressDialog(Inner_Basket.this);
						//loader.setMessage(getString(R.string.app_Loading));
						//loader.show();
				    		        
				        HTTPAsynkTask mt = new HTTPAsynkTask(Inner_Basket.this, 
								GlobalConstants.CART_DEL, 
								//null, null, 
								params, 
								Inner_Basket.this);
						mt.execute();
			          }
			      });
				  
				  holder.gdsTitle.setText(dataGds.get(position).getGdsTitle());
				  
				  holder.gdsPriceUah.setText(dataGds.get(position).getGdsPriceUah());
				  holder.gdsPriceCoins.setText(dataGds.get(position).getGdsPriceCoins());
				 
				  
				  Transformation transformation = new Transformation() {
				        @Override
				        public Bitmap transform(Bitmap source) {
				            int targetWidth = holder.gdsImage.getWidth();
				            Log.v("TAG", "C1C IF targetWidth=" + targetWidth);
				            if (targetWidth != 0){
				            	double aspectRatio = (double) source.getHeight() / (double) source.getWidth();
					            int targetHeight = (int) (targetWidth * aspectRatio);
					            Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
					            if (result != source) {
					                // Same bitmap is returned if sizes are the same
					                source.recycle();
					            }
					            return result;
				            } else {
				            	Log.v("TAG", "C1C ELSE targetWidth=" + targetWidth);
				            	return source;
				            }
				            
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
				    });
				  
				  /*
				  Picasso.with(view.getContext()).load(dataGds.get(position).getGdsUrlImage())
				  	.placeholder(R.drawable.no_photo)
				  	.error(R.drawable.no_photo)
				  	.into(holder.gdsImage);
				  */	
				  
				  if(dataGds.get(position).getExpired())
					  holder.gdsExpiredImage.setVisibility(View.VISIBLE);
				  else
					  holder.gdsExpiredImage.setVisibility(View.GONE);
				  
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
		
	}

	@Override
	public void onTaskCompletedJSON(String json, int id_request) {
		// TODO Auto-generated method stub
		
		if( loader != null && loader.isShowing() )
			loader.dismiss();
		
		Log.v("LOG_TAG","Inner_Basket onTaskCompletedJSON MeApp.regData.getToken() = "+MeApp.regData.getToken());
		
		Log.v("LOG_TAG","Inner_Basket onTaskCompletedJSON json = "+json);
		
		switch(id_request){
			case GlobalConstants.CART_GET:
				
				dataBacketGds = BasketGdsLisJsonParser(json);
				
				fillData();
				
				if(dataBacketGds.size()>0){
					emptyBasketLayout.setVisibility(View.GONE);
					mainLayout.setVisibility(View.VISIBLE);
				}else{
					emptyBasketLayout.setVisibility(View.VISIBLE);
					mainLayout.setVisibility(View.GONE);
					
				}	
				
				BasketGdsListAdapter adapter = new BasketGdsListAdapter(this, dataBacketGds);
			    backetGdsListView.setAdapter(adapter);
			    Log.v("LOG_TAG", "YOYO setOnItemClickListener!");
			    backetGdsListView.setOnItemClickListener(new OnItemClickListener(){
		            @Override 
		            public void onItemClick(AdapterView<?> arg0, View arg1,int position, long arg3){ 
		            	Log.v("LOG_TAG", "YOYO Click!");
						if(!dataBacketGds.get(position).getExpired()){
							Log.v("LOG_TAG", "YOYO dataBacketGds.get(position).getGdsId() = "+dataBacketGds.get(position).getGdsId());
							Log.v("LOG_TAG", "YOYO dataBacketGds.get(position).getCatalogId() = "+dataBacketGds.get(position).getCatalogId());
			            	Intent intent = new Intent (Inner_Basket.this,Inner_GdsItem.class);
				  			intent.putExtra("idGds",dataBacketGds.get(position).getGdsId());
				  			intent.putExtra("idCatalog",dataBacketGds.get(position).getCatalogId());
				  			startActivity(intent);	  	
						}
			  	    }
		        });	
		    break;
			case GlobalConstants.CART_DEL:
				Boolean result = false;
				int countBasket = 0;
				Log.v("LOG_TAG"," ееее Inner_Main CART_DEL json = "+json);
				try {
					JSONObject object = new JSONObject(json);
					result = object.optBoolean("success");
					countBasket = object.optInt("count");
					
					//if(countBasket>0){
						 MeApp.regData.getUser().setCount_basket(countBasket);
						 Utils.showActionBar(this, true);
					//}
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
				if(result){
					
					final Toast toast = Toast.makeText(Inner_Basket.this, 
							getString(R.string.gds_deleted), 
							Toast.LENGTH_SHORT);
					toast.show();
					Handler handler = new Handler();
		            handler.postDelayed(new Runnable() {
		               @Override
		               public void run() {
		                   toast.cancel(); 
		               }
		            }, 1000);
					
					 HTTPAsynkTask mt = new HTTPAsynkTask(Inner_Basket.this, 
								GlobalConstants.CART_GET, 
								//null, null, 
								null, 
								Inner_Basket.this);
					 mt.execute();
				}else{
					Toast.makeText(Inner_Basket.this, getString(R.string.error_del_gds), 
							Toast.LENGTH_SHORT).show();					
				}
		     break;	
		     
		    //TODO ============ERRORS DEPLOYMENT==========
			case GlobalConstants.NO_INTERNET_CONNECTION:
				BottomDialog.BottomSelectDialog(Inner_Basket.this, 
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
	        	startActivity(new Intent(Inner_Basket.this, Out_RegInStart.class)); 
				finish();
				break;
			
			case GlobalConstants.ERROR_503:
				BottomDialog.BottomSelectDialog(Inner_Basket.this, 
						error503, 
		    			getString(R.string.dialog_Server503Title), 
		    			getString(R.string.dialog_Server503Text), 
		    			getString(R.string.dialog_Server503Left), 
		    			getString(R.string.dialog_Server503Right));
			break;
			
			default:
				BottomDialog.BottomSelectDialog(Inner_Basket.this, 
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
	            	Inner_Basket.this.finishAffinity();
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
	            	Inner_Basket.this.finishAffinity();
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
		tracker.setScreenName("basket");
		tracker.send(new HitBuilders.EventBuilder().build());
	}

}
