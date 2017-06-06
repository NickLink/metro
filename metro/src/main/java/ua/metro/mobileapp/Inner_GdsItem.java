package ua.metro.mobileapp;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ua.metro.mobileapp.HttpMetods.HTTPAsynkTask;
import ua.metro.mobileapp.HttpMetods.UserState;
import ua.metro.mobileapp.UI.BottomDialog;
import ua.metro.mobileapp.UI.GetDeviceSize;
import ua.metro.mobileapp.UI.LinearListView;
import ua.metro.mobileapp.application.MeApp;
import ua.metro.mobileapp.datamodel.GdsData;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
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
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

public class Inner_GdsItem extends Activity implements OnTaskCompleted {
	String LOG_TAG = "Inner_GdsItem_LOG_TAG";
	Typeface fontRegular, fontMedium, fontLight, fontBold;
	
	GdsData dataGdsItem;
	HashMap<Integer,GdsData> dataSimilargGds;
	
	//HorizontalListView similarGdsListView;
	//GridViewScrollable similarGdsGridView;
	LinearListView horizontalSGoodsLL;
	
	int idGds, idCatalog;
	//String titleCatalog;
	int idCategory;
	String titleCategory;
	int widthDevice, heightDevice;
	int daysDuration;
	int daysUntil;
	
	TextView subTitleTextView, labelDaysTextView;
	ProgressBar daysUntilprogressBar;
	TextView countDaysLeftTextView;
		
	ImageView gdsImage;
	TextView gdsTitle;
	TextView gdsDiscount;
	TextView gdsPriceOldUah;
	TextView gdsPriceOldCoins;
	TextView gdsPriceUah;
	TextView gdsPriceCoins;
	TextView gdsPriceCurrency;
	WebView gdsDesc;
	Button gdsAddButton;
	TextView categoryTitle;
	ImageButton iconBackImageButton;

	LinearLayout similaGdsLayout;
	
	Configuration config;
	ProgressDialog loader;

	@SuppressWarnings("deprecation")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        UserState.restoreUserState(this);
        
        setContentView(R.layout.inner_gds_item);      
        
        Utils.showActionBar(this, true);
        Utils.setBottomMenu(this,0);
        
        GetDeviceSize a = new GetDeviceSize(this);
		widthDevice = a.getX();
		heightDevice = a.getY();
		
        
        Intent intent = getIntent();
        idCatalog = intent.getIntExtra("idCatalog",0);
        idGds = intent.getIntExtra("idGds",0);
        
        config = getResources().getConfiguration();
        
        //titleCatalog = intent.getStringExtra("titleCatalog");
        //daysUntil = intent.getIntExtra("daysUntil",0);
        //daysDuration = intent.getIntExtra("daysDuration",0);
        
        //Log.v(LOG_TAG,"電電 Inner_GdsItem idCatalog = "+idCatalog);
        //Log.v(LOG_TAG,"電電 Inner_GdsItem idGds = "+idGds);
        
        HashMap<String,String> params = new HashMap<String,String>();
    	params.put("catalog_id",  String.valueOf(idCatalog));
		params.put("product_id", String.valueOf(idGds));
		
		loader = new ProgressDialog(Inner_GdsItem.this);
		loader.setMessage(getString(R.string.app_Loading));
		loader.show();
		
        HTTPAsynkTask mt = new HTTPAsynkTask(Inner_GdsItem.this,
				GlobalConstants.PRODUCT, 
				//null, null, 
				params, 
				Inner_GdsItem.this);
		mt.execute();
        
        fontRegular = FontCache.get("fonts/clearsansregular.ttf", getBaseContext());
        fontMedium = FontCache.get("fonts/clearsansmedium.ttf", getBaseContext());	
        fontLight = FontCache.get("fonts/clearsanslight.ttf", getBaseContext());	
        fontBold = FontCache.get("fonts/clearsansbold.ttf", getBaseContext());	
        
        subTitleTextView = (TextView) findViewById(R.id.subTitleTextView);
        subTitleTextView.setTypeface(fontMedium);
        
        countDaysLeftTextView = (TextView) findViewById(R.id.countDaysLeftTextView);
        labelDaysTextView= (TextView) findViewById(R.id.labelDaysTextView);
        countDaysLeftTextView.setTypeface(fontMedium);
        labelDaysTextView.setTypeface(fontMedium);
        
        daysUntilprogressBar = (ProgressBar) findViewById(R.id.daysUntilprogressBar);
        
        iconBackImageButton = (ImageButton) findViewById(R.id.iconBackImageButton);
        
        dataGdsItem = new GdsData();
        dataSimilargGds = new HashMap<Integer,GdsData>();
        
        categoryTitle = (TextView) findViewById(R.id.filterTextView);
   		
   		View.OnClickListener btnListener = new View.OnClickListener(){
	        public void onClick(View v) {	
	        	if (v == iconBackImageButton){		            	
	            	finish();
	            }
	        	if (v == subTitleTextView){	
	        		Intent intent = new Intent (Inner_GdsItem.this,Inner_CatalogGds.class);
		  			intent.putExtra("idCatalog", idCatalog);
		  			intent.putExtra("page", 1);
		  			startActivity(intent);
	            	finish();
	            }
	            if (v == categoryTitle && idCategory>0){		            	
		            //come back to the product list with this filter
	            	Intent intent = new Intent (Inner_GdsItem.this,Inner_CatalogGds.class);
		  			intent.putExtra("idCatalog", dataGdsItem.getCatalogId());
		  			intent.putExtra("idCategory", idCategory);
		  			intent.putExtra("page", 1);
		  			startActivity(intent);
	            	finish();
		         } 
	        }
	    };
	    subTitleTextView.setOnClickListener(btnListener);	
	    iconBackImageButton.setOnClickListener(btnListener);
	    categoryTitle.setOnClickListener(btnListener);
		
	    gdsImage = (ImageView) findViewById(R.id.gdsImage);
		gdsTitle = (TextView) findViewById(R.id.gdsTitle);
		gdsDiscount = (TextView) findViewById(R.id.gdsDiscount);
		gdsDesc = (WebView) findViewById(R.id.gdsDesc);
		gdsPriceOldUah = (TextView) findViewById(R.id.gdsPriceOldUah);
		gdsPriceOldCoins = (TextView) findViewById(R.id.gdsPriceOldCoins);
		gdsPriceUah = (TextView) findViewById(R.id.gdsPriceUah);
		gdsPriceCoins = (TextView) findViewById(R.id.gdsPriceCoins);
		gdsPriceCurrency = (TextView) findViewById(R.id.gdsPriceCurrency);	
		gdsAddButton = (Button) findViewById(R.id.gdsAddButton);	
		similaGdsLayout = (LinearLayout) findViewById(R.id.similaGdsLayout);	
				
		TextView labelSimilarGds = (TextView) findViewById(R.id.similaGdsTextView);		
		
		WebSettings settings = gdsDesc.getSettings();   
		settings.setPluginState(PluginState.ON);
		settings.setSupportMultipleWindows(true);
	    settings.setAllowFileAccess(true);
	    settings.setJavaScriptEnabled(true); 
	  
		categoryTitle.setTypeface(fontBold);
		gdsTitle.setTypeface(fontBold);
		gdsDiscount.setTypeface(fontMedium);
		gdsPriceOldUah.setTypeface(fontMedium);
		gdsPriceOldCoins.setTypeface(fontMedium);
		gdsPriceUah.setTypeface(fontMedium);
		gdsPriceCoins.setTypeface(fontMedium);
		gdsPriceCurrency.setTypeface(fontRegular);
		labelSimilarGds.setTypeface(fontBold);
	
		/*similarGdsListView = (HorizontalListView) findViewById(R.id.similarGdsListView);
	  
		similarGdsListView.setOnItemClickListener(new OnItemClickListener(){
	          @Override 
	          public void onItemClick(AdapterView<?> arg0, View arg1,int position, long arg3)
	          { 
	             	Intent intent = new Intent (Inner_GdsItem.this,Inner_GdsItem.class);
	             	intent.putExtra("idGds",dataSimilargGds.get(position).getGdsId());
		  			intent.putExtra("idCatalog",dataSimilargGds.get(position).getCatalogId());
		  			startActivity(intent);
	          }
	      });*/
		
		//similarGdsGridView = (GridViewScrollable) findViewById(R.id.similarGdsListView);
		//similarGdsListView = (HorizontalListView) findViewById(R.id.similarGdsListView);
		horizontalSGoodsLL = (LinearListView) findViewById(R.id.horizontalSGoodsLL);
		
		horizontalSGoodsLL.setOnItemClickListener(new ua.metro.mobileapp.UI.LinearListView.OnItemClickListener(){

			@Override
			public void onItemClick(LinearListView parent, View view,
					int position, long id) {
				
				Intent intent = new Intent (Inner_GdsItem.this,Inner_GdsItem.class);
             	intent.putExtra("idGds",dataSimilargGds.get(position).getGdsId());
	  			intent.putExtra("idCatalog",dataSimilargGds.get(position).getCatalogId());
	  			startActivity(intent);
				
			}});
		  
		/*
		 * similarGdsListView.setOnItemClickListener(new OnItemClickListener(){
	          @Override 
	          public void onItemClick(AdapterView<?> arg0, View arg1,int position, long arg3)
	          { 
	             	Intent intent = new Intent (Inner_GdsItem.this,Inner_GdsItem.class);
	             	intent.putExtra("idGds",dataSimilargGds.get(position).getGdsId());
		  			intent.putExtra("idCatalog",dataSimilargGds.get(position).getCatalogId());
		  			startActivity(intent);
	          }
	      });
	  
		LinearLayout.LayoutParams paramsView = (LinearLayout.LayoutParams) similarGdsListView.getLayoutParams();
		paramsView.height = (int) widthDevice;
		similarGdsListView.setLayoutParams(paramsView);
		*/
	  
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
		
		subTitleTextView.setText(dataGdsItem.getCatalogTitle());
		
		countDaysLeftTextView.setText(String.valueOf(daysUntil));
		daysUntilprogressBar.setMax(daysDuration);
		daysUntilprogressBar.setProgress(daysUntil);
		
		labelDaysTextView.setText(Utils.pluralNoInt(daysUntil, 
				getString(R.string.days1), 
				getString(R.string.days2_4), 
				getString(R.string.days)));
		
		categoryTitle.setText(titleCategory);
		
		gdsTitle.setText(dataGdsItem.getGdsTitle());
		
		gdsDiscount.setText(dataGdsItem.getDiscountTitle_(this));
		  
	    if(dataGdsItem.getDiscountType()!=null && 
			  dataGdsItem.getDiscountType().length()>0 &&
			  dataGdsItem.getDiscountImgUrl(this)>0){
		  
		  gdsDiscount.setBackgroundResource(dataGdsItem.getDiscountImgUrl(this));
	    }
		
		gdsPriceUah.setText(dataGdsItem.getGdsPriceUah());
		gdsPriceCoins.setText(dataGdsItem.getGdsPriceCoins());
		
		
		Transformation transformation = new Transformation() {
	        @Override
	        public Bitmap transform(Bitmap source) {
	            //int targetWidth = gdsImage.getWidth();
	            //Log.v("TAG", "CXC in Main int targetWidthBEFORE=" + targetWidth);
	            //if (targetWidth == 0) 
	        	int targetWidth = (int) widthDevice/2;
	            Log.v("TAG", "CXC in Main int targetWidthAFTER=" + targetWidth);
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

	    Picasso.with(this)
	        .load(dataGdsItem.getGdsUrlImage())
	        //.error(android.R.drawable.stat_notify_error)
	        .placeholder(R.drawable.no_photo)
			.error(R.drawable.no_photo)
	        .transform(transformation)
	        .into(gdsImage, new Callback() {
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
	    
	    gdsImage.getLayoutParams().height = (int) widthDevice/2;
		
		/*
	    LinearLayout.LayoutParams paramsGdsImage = (LayoutParams) gdsImage.getLayoutParams();			  
		  paramsGdsImage.width = (int) widthDevice/2;
		  paramsGdsImage.height = paramsGdsImage.width;
		  gdsImage.setLayoutParams(paramsGdsImage);
		  */
		  
		/*
	    Picasso.with(this).load(dataGdsItem.getGdsUrlImage())
			.placeholder(R.drawable.no_photo)
			.fit()
			.error(R.drawable.no_photo)
			.into(gdsImage);
	    */
		
		 gdsAddButton.setOnClickListener(new Button.OnClickListener() {  
			  public void onClick(View v){
	                //perform action
				Log.v("LOG_TAG"," 電電 Inner_Main dataGdsItem.getCatalogId() = " + dataGdsItem.getCatalogId());
				Log.v("LOG_TAG"," 電電 Inner_Main dataGdsItem.getGdsId() = "+ dataGdsItem.getGdsId());
				
				if (MeApp.regData.getUser().getCount_basket() >= 99){
					//TODO Dialog for too much items			
					
				} else {
					
					HashMap<String,String> params = new HashMap<String,String>();
			    	params.put("catalog_id", String.valueOf(dataGdsItem.getCatalogId()));
			    	params.put("product_id", String.valueOf(dataGdsItem.getGdsId()));
			    	
			    	loader = new ProgressDialog(Inner_GdsItem.this);
					loader.setMessage(getString(R.string.app_Loading));
					loader.show();
			    		        
			        HTTPAsynkTask mt = new HTTPAsynkTask(Inner_GdsItem.this, 
							GlobalConstants.CART_ADD, 
							//null, null, 
							params, 
							Inner_GdsItem.this);
					mt.execute();
					
				}
				
				  
	          }
	      });
		
	    String Html = "<html>"
				+ "<head>"
				+ "<meta content=\"text/html; charset=utf-8\" http-equiv=\"Content-Type\"/>"
				//+ style
				+"</head>"
				+ "<body style='margin:0;padding:0;'>"
				+ ((dataGdsItem.getGdsDetails1()!=null) ? dataGdsItem.getGdsDetails1():"")
				+ ((dataGdsItem.getGdsDetails2()!=null) ? dataGdsItem.getGdsDetails2():"")
				+ ((dataGdsItem.getGdsDesc()!=null) ? dataGdsItem.getGdsDesc():"")
				+"</body>"
				+ "</html>";
		
	    gdsDesc.loadDataWithBaseURL("",Html,"text/html","UTF-8",null);

		if(dataGdsItem.getGdsPriceOld()!=null){
			  gdsPriceOldUah.setText(dataGdsItem.getGdsPriceOldUah());
			  gdsPriceOldCoins.setText(dataGdsItem.getGdsPriceOldCoins());
			  gdsPriceOldUah.setVisibility(View.VISIBLE);
			  gdsPriceOldCoins.setVisibility(View.VISIBLE);
		}else{
		  gdsPriceOldUah.setVisibility(View.GONE);
		  gdsPriceOldCoins.setVisibility(View.GONE);
		}		
	}
	
	 @SuppressLint("UseSparseArrays")
	 private void GdsItemJsonParser(String JsonString){
		 Log.v(LOG_TAG," 電電 CatalogsLisJsonParser JsonString = "+JsonString);
		 
		 String catalogDateStart_str = null;
		 String catalogDateEnd_str = null;
		 
		/* try {
		        InputStream is = getAssets().open("product1.json");

		        // We guarantee that the available method returns the total
		        // size of the asset...  of course, this does mean that a single
		        // asset can't be more than 2 gigs.
		        int size = is.available();

		        // Read the entire asset into a local byte buffer.
		        byte[] buffer = new byte[size];
		        is.read(buffer);
		        is.close();

		        // Convert the buffer into a string.
		        JsonString = new String(buffer);
		        
		        //Log.v(LOG_TAG,"CatalogsLisJsonParser JsonString = "+JsonString);

		    } catch (IOException e) {
		        // Should never happen!
		        throw new RuntimeException(e);
		    }*/
	
		try
	  		{
			 	JSONObject object = new JSONObject(JsonString);
			 	Log.v(LOG_TAG,"GGG Inner_GdsItem object = "+object);
			 	
			 	JSONObject item = object.getJSONObject("product");
			 	Log.v(LOG_TAG,"GGG Inner_GdsItem item = "+item);
	    		
			 	if(item.optString("catalog_start").length()>0)
			 		catalogDateStart_str = item.optString("catalog_start");
					 
				if(item.optString("catalog_end").length()>0)
					catalogDateEnd_str = item.optString("catalog_end");
				 
				daysUntil = Utils.getDaysUntil(catalogDateEnd_str);
				daysDuration = Utils.getDuration(catalogDateStart_str,catalogDateEnd_str);
				 
				if(item.optInt("catalog_product_id")>0)
					dataGdsItem.setGdsId(item.optInt("catalog_product_id"));
				Log.v(LOG_TAG,"GGG Inner_GdsItem item.optInt(catalog_product_id) = "+item.optInt("catalog_product_id"));
				
				if(item.optString("title").length()>0)
					dataGdsItem.setGdsTitle(item.optString("title"));
				Log.v(LOG_TAG,"GGG Inner_GdsItem title = "+item.optString("title"));
				
				if(item.optDouble("price")>0)
					dataGdsItem.setGdsPrice(item.optDouble("price"));
				Log.v(LOG_TAG,"GGG Inner_GdsItem price = "+item.optDouble("price"));
				
				if(item.optDouble("old_price")>0)
					dataGdsItem.setGdsPriceOld(item.optDouble("old_price"));
				Log.v(LOG_TAG,"GGG Inner_GdsItem old_price = "+item.optDouble("old_price"));
				
				if(item.getJSONObject("images").optString("list").length()>0)
					dataGdsItem.setGdsUrlImage(item.getJSONObject("images").optString("list"));
				Log.v(LOG_TAG,"GGG Inner_GdsItem img = "+item.getJSONObject("images").optString("list"));
				
				if(item.optString("description").length()>0)
					dataGdsItem.setGdsDesc(item.optString("description"));
				Log.v(LOG_TAG,"GGG Inner_GdsItem desc = "+item.optString("description"));
				
				if(item.optString("details1").length()>0)
					dataGdsItem.setGdsDetails2(item.optString("details1"));
				Log.v(LOG_TAG,"GGG Inner_GdsItem details1 = "+item.optString("details1"));
				
				if(item.optString("details2").length()>0)
					dataGdsItem.setGdsDetails2(item.optString("details2"));
				Log.v(LOG_TAG,"GGG Inner_GdsItem details2 = "+item.optString("details2"));
				
				if(item.optInt("catalog_id")>0)
					dataGdsItem.setCatalogId(item.optInt("catalog_id",0));
				if(item.optString("catalog_title").length()>0)
					dataGdsItem.setCatalogTitle(item.optString("catalog_title"));	
				
				if(item.optInt("category_id")>0)
					idCategory = item.optInt("category_id",0);
				if(item.optString("category_title").length()>0)
					titleCategory = item.optString("category_title");	
				
				if(item.optInt("catalog_id")>0)
					dataGdsItem.setCatalogId(item.optInt("catalog_id",0));
				if(item.optString("catalog_title").length()>0)
					dataGdsItem.setCatalogTitle(item.optString("catalog_title"));	
				
				if(item.optString("discount").length()>0)
					dataGdsItem.setDiscount(item.optString("discount"));
  				if(item.optString("discount_type").length()>0)
  					dataGdsItem.setDiscountType(item.optString("discount_type"));
  				if(item.optString("discount_title").length()>0)
  					dataGdsItem.setDiscountTitle(item.optString("discount_title"));
				
				////
				JSONArray similarList = object.getJSONArray("similar");
				for (int i=0;i<similarList.length();i++) {
	  				JSONObject similarItem = similarList.getJSONObject(i);
	  				
	   				GdsData gdsItem = new GdsData();
	  				if(similarItem.optInt("catalog_product_id")>0)
	  					gdsItem.setGdsId(similarItem.optInt("catalog_product_id"));
	  				if(item.optInt("catalog_id")>0)
	  					gdsItem.setCatalogId(item.optInt("catalog_id"));
	  				if(similarItem.optString("title").length()>0)
	  					gdsItem.setGdsTitle(similarItem.optString("title"));
	  				if(similarItem.optDouble("price")>0)
	  					gdsItem.setGdsPrice(similarItem.optDouble("price"));
	  				//if(similarItem.optDouble("old_price")>0)
	  					//gdsItem.setGdsPriceOld(similarItem.optDouble("old_price"));
	  				if(similarItem.getJSONObject("images").optString("list").length()>0)
	  					gdsItem.setGdsUrlImage(similarItem.getJSONObject("images").optString("list"));
	  				
	  				if(similarItem.optInt("catalog_id")>0)
	  					gdsItem.setCatalogId(similarItem.optInt("catalog_id",0));
					if(similarItem.optString("catalog_title").length()>0)
						gdsItem.setCatalogTitle(similarItem.optString("catalog_title"));
	  				
	  				if(similarItem.optString("discount").length()>0)
	  					gdsItem.setDiscount(similarItem.optString("discount"));
	  				if(similarItem.optString("discount_type").length()>0)
	  					gdsItem.setDiscountType(similarItem.optString("discount_type"));
	  				if(similarItem.optString("discount_title").length()>0)
	  					gdsItem.setDiscountTitle(similarItem.optString("discount_title"));
	  		  				
	  				dataSimilargGds.put(i, gdsItem);
	  			}
				
				Log.v(LOG_TAG,"GGG Inner_GdsItem dataSimilargGds = "+dataSimilargGds);
				/////////

			}catch(Exception e){
	    		Log.v(LOG_TAG,"GGG Inner_GdsItem  error = "+e.toString());	
			}	 
	 }
	 
	 public class SimilarGdsListAdapter extends BaseAdapter {
			Typeface fontRegular, fontMedium, fontLight;
			
			String LOG_TAG = this.getClass().getName()+"LOG";
			LayoutInflater inflator;
			HashMap<Integer,GdsData> dataGds;
			Context mContext;
			OnTaskCompleted listener;
			//int itemsOnList;
			
			public SimilarGdsListAdapter(Context context, HashMap<Integer,GdsData> data){
				 this.dataGds = data;
				 this.mContext = context;
				 this.listener = (OnTaskCompleted) context;
			}			 
			 
			public class ViewHolder{
				 public ImageView gdsImage;
				 public TextView gdsTitle;
				 public ImageView gdsHitPrice;
				 public TextView gdsPriceOldUah;
				 public TextView gdsPriceOldCoins;
				 public TextView gdsPriceUah;
				 public TextView gdsPriceCoins;
				 public TextView gdsPriceCurrency;
				 public Button gdsAddButton;
			}
			 
			 public Object getItem(int position){
				 return position;
			 }
			 
			 public View getView(final int position, View convertView, ViewGroup parent){
				 View view = convertView;
				 final ViewHolder holder;
				 
				/*  if (config.smallestScreenWidthDp >= 720) {
					    // sw720dp
						  itemsOnList = 3;
					  }
					  else if (config.smallestScreenWidthDp >= 600) {
					    // sw600dp
						  itemsOnList = 3;
					  }
					  else {
					    // all other
						  itemsOnList = 2;					  
					  }
					  */
				 
				  if (convertView == null) {
					  holder = new ViewHolder();
					  
					  LayoutInflater inflater = LayoutInflater.from(parent.getContext());
					  
					  view = inflater.inflate(R.layout.cell_gds_similar, parent, false);
					  holder.gdsImage = (ImageView) view.findViewById(R.id.gdsImage);
					  holder.gdsTitle = (TextView) view.findViewById(R.id.gdsTitle);
					  holder.gdsPriceOldUah = (TextView) view.findViewById(R.id.gdsPriceOldUah);
					  holder.gdsPriceOldCoins = (TextView) view.findViewById(R.id.gdsPriceOldCoins);
					  holder.gdsPriceUah = (TextView) view.findViewById(R.id.gdsPriceUah);
					  holder.gdsPriceCoins = (TextView) view.findViewById(R.id.gdsPriceCoins);
					  holder.gdsPriceCurrency = (TextView) view.findViewById(R.id.gdsPriceCurrency);
					  holder.gdsAddButton = (Button) view.findViewById(R.id.gdsAddButton);
					
					  holder.gdsImage.getLayoutParams().height = (int) widthDevice/2;
					  
					 /* LinearLayout.LayoutParams paramsGdsImage = (LayoutParams) holder.gdsImage.getLayoutParams();			  
					  paramsGdsImage.width = (int) widthDevice/2;
					  paramsGdsImage.height = paramsGdsImage.width;
					  holder.gdsImage.setLayoutParams(paramsGdsImage);
					  */
				  
					  holder.gdsTitle.setTypeface(fontMedium);
					  holder.gdsPriceOldUah.setTypeface(fontMedium);
					  holder.gdsPriceOldCoins.setTypeface(fontMedium);
					  holder.gdsPriceUah.setTypeface(fontMedium);
					  holder.gdsPriceCoins.setTypeface(fontMedium);
					  holder.gdsPriceCurrency.setTypeface(fontRegular);
								  		
					  view.setTag(holder);
					  
				  } else{
					  holder = (ViewHolder) view.getTag();
				  }
				  
				  holder.gdsAddButton.setOnClickListener(new Button.OnClickListener() {  
					  public void onClick(View v){
			                //perform action
						Log.v("LOG_TAG"," AAAA Inner_GdsItem  SimilarGdsListAdapter dataGds.getCatalogId() = " + dataGds.get(position).getCatalogId());
						Log.v("LOG_TAG"," AAAA Inner_GdsItem SimilarGdsListAdapter dataGds.getGdsId() = "+ dataGds.get(position).getGdsId());
						if (MeApp.regData.getUser().getCount_basket() >= 99){
							//TODO Dialog for too much items
							
							
						} else {
							
							HashMap<String,String> params = new HashMap<String,String>();
					    	params.put("catalog_id", String.valueOf(dataGds.get(position).getCatalogId()));
					    	params.put("product_id", String.valueOf(dataGds.get(position).getGdsId()));
					    		        
					    	loader = new ProgressDialog(Inner_GdsItem.this);
							loader.setMessage(getString(R.string.app_Loading));
							loader.show();
					    	
					    	HTTPAsynkTask mt = new HTTPAsynkTask(mContext, 
									GlobalConstants.CART_ADD, 
									//null, null, 
									params, 
									listener);
							mt.execute();
							
						}

			          }
			      });

				  ViewGroup.LayoutParams paramsView = (ViewGroup.LayoutParams) view.getLayoutParams();
				  paramsView.width = (int) widthDevice/2;
				  //paramsView.height = paramsView.width;
				  view.setLayoutParams(paramsView);
				  
				  holder.gdsTitle.setText(dataGds.get(position).getGdsTitle());
				  
				  holder.gdsPriceUah.setText(dataGds.get(position).getGdsPriceUah());
				  holder.gdsPriceCoins.setText(dataGds.get(position).getGdsPriceCoins());
				  Log.v(LOG_TAG, "1111 dataGds.get(position).getGdsPriceOld() = "+dataGds.get(position).getGdsPriceOld());
				  
				  if(dataGds.get(position).getGdsPriceOld()!=null){
					  holder.gdsPriceOldUah.setText(dataGds.get(position).getGdsPriceOldUah());
					  holder.gdsPriceOldCoins.setText(dataGds.get(position).getGdsPriceOldCoins());
					  holder.gdsPriceOldUah.setVisibility(View.VISIBLE);
					  holder.gdsPriceOldCoins.setVisibility(View.VISIBLE);
				  }else{
					  holder.gdsPriceOldUah.setVisibility(View.GONE);
					  holder.gdsPriceOldCoins.setVisibility(View.GONE);
				  }
				  	
				  
				  
				  Transformation transformation = new Transformation() {
				        @Override
				        public Bitmap transform(Bitmap source) {
				            //int targetWidth = holder.gdsImage.getWidth();
				            //Log.v("TAG", "CXC in Adapter int targetWidthBEFORE=" + targetWidth);
				            //if (targetWidth == 0) 
				        	int targetWidth = (int) widthDevice/2;
				            Log.v("TAG", "CXC in Adapter int targetWidthAFTER=" + targetWidth);
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
				        .placeholder(R.drawable.no_photo)
				        .error(R.drawable.no_photo)
				        //.error(android.R.drawable.stat_notify_error)
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
	public void onTaskCompleted(int id) {
		// TODO Auto-generated method stub		
	}

	@Override
	public void onTaskCompletedJSON(String json, int id_request) {
		// TODO Auto-generated method stub	
		
		if( loader != null && loader.isShowing() )
			loader.dismiss();
		
		Log.v("LOG_TAG","Inner_GdsItem onTaskCompletedJSON json = "+json);
		
		switch(id_request){
			case GlobalConstants.PRODUCT:
				
				GdsItemJsonParser(json);
				
				fillData();
				
				if (dataSimilargGds!=null && dataSimilargGds.size()>0){
					//similarGdsGridView.setNumColumns(dataSimilargGds.size());
					SimilarGdsListAdapter adapter = new SimilarGdsListAdapter(this, dataSimilargGds);		 
				    
					horizontalSGoodsLL.setAdapter(adapter);
					//similarGdsListView.setAdapter(adapter);
					//similarGdsGridView.setAdapter(adapter);
		
					//Utils.setGridViewHeightBasedOnChildren(similarGdsGridView,2);
				    
					similaGdsLayout.setVisibility(View.VISIBLE);
				}else
					similaGdsLayout.setVisibility(View.GONE);
			break;
			
			case GlobalConstants.CART_ADD:
				Boolean result = false;
				int countBasket = 0;
				
				Log.v("LOG_TAG"," 電電 CART_ADD json = "+json);
				
				try {
					JSONObject object = new JSONObject(json);
					result = object.optBoolean("success");
					countBasket = object.optInt("count");
					
					//if(countBasket>0){
						MeApp.regData.getUser().setCount_basket(countBasket);
						Log.v("LOG_TAG"," 電電 CART_ADD MeApp.regData.getUser().getCount_basket() = "+MeApp.regData.getUser().getCount_basket());
						Utils.showActionBar(this, true);
					//}
					
				} catch (JSONException e) {
					Log.v("LOG_TAG"," 電電 CART_ADD error="+e.toString());
					e.printStackTrace();
				}
				
				if(result){
					final Toast toast = Toast.makeText(Inner_GdsItem.this, 
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
					Toast.makeText(Inner_GdsItem.this, getString(R.string.error_adding_gds), 
							Toast.LENGTH_SHORT).show();					
				}
		     break;	
		     
		   //TODO ============ERRORS DEPLOYMENT==========
			case GlobalConstants.NO_INTERNET_CONNECTION:
				BottomDialog.BottomSelectDialog(Inner_GdsItem.this, 
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
	        	startActivity(new Intent(Inner_GdsItem.this, Out_RegInStart.class)); 
				finish();
				break;
			
			case GlobalConstants.ERROR_503:
				BottomDialog.BottomSelectDialog(Inner_GdsItem.this, 
						error503, 
		    			getString(R.string.dialog_Server503Title), 
		    			getString(R.string.dialog_Server503Text), 
		    			getString(R.string.dialog_Server503Left), 
		    			getString(R.string.dialog_Server503Right));
			break;
			
			default:
				BottomDialog.BottomSelectDialog(Inner_GdsItem.this, 
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
	            	Inner_GdsItem.this.finishAffinity();
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
	            	Inner_GdsItem.this.finishAffinity();
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
		tracker.setScreenName("item");
		tracker.send(new HitBuilders.EventBuilder().build());
	}

}