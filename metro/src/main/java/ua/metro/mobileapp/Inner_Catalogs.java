package ua.metro.mobileapp;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import ua.metro.mobileapp.HttpMetods.HTTPAsynkTask;
import ua.metro.mobileapp.HttpMetods.UserState;
import ua.metro.mobileapp.UI.BottomDialog;
import ua.metro.mobileapp.datamodel.CatalogData;
import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.squareup.picasso.Picasso;

public class Inner_Catalogs  extends Activity implements OnTaskCompleted{
	
	Typeface fontRegular, fontMedium, fontLight;
	
	HashMap<Integer,CatalogData> dataCatalogs;
	ListView catalogsListView;
	
	ImageButton iconBackImageButton;
	
	RelativeLayout emptyCatalogsLayout;
	
	@Override
	public void onBackPressed() {
		finish();
	}

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        UserState.restoreUserState(this);
        
        setContentView(R.layout.inner_catalogs);      
        
        Utils.showActionBar(this, true);
        Utils.setBottomMenu(this,2);
        
        fontRegular = FontCache.get("fonts/clearsansregular.ttf", getBaseContext());
        fontMedium = FontCache.get("fonts/clearsansmedium.ttf", getBaseContext());	
        fontLight = FontCache.get("fonts/clearsanslight.ttf", getBaseContext());	
        
        emptyCatalogsLayout = (RelativeLayout) findViewById(R.id.emptyCatalogsLayout);
        
        iconBackImageButton = (ImageButton) findViewById(R.id.iconBackImageButton);
        TextView subTitleTextView = (TextView) findViewById(R.id.subTitleTextView);
     	
        subTitleTextView.setTypeface(fontMedium);
        subTitleTextView.setText(getString(R.string.all_catalogs));
        
        HTTPAsynkTask mt = new HTTPAsynkTask(Inner_Catalogs.this,
				GlobalConstants.CATALOG, 
				//null, null, 
				null, 
				Inner_Catalogs.this);
		mt.execute();
        
        catalogsListView = (ListView) findViewById(R.id.catalogsListView);
        
        View.OnClickListener btnListener = new View.OnClickListener(){
	        public void onClick(View v) {
	            if (v == iconBackImageButton){		            	
	            	finish();
	            }
	        }
	    };
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
	
	@Override
    protected void onRestart() {
        super.onRestart();

        Utils.showActionBar(this, true);
    }
	
	private void fillData(){
		if(dataCatalogs.size()>0){
			emptyCatalogsLayout.setVisibility(View.GONE);
			catalogsListView.setVisibility(View.VISIBLE);
		}else{
			emptyCatalogsLayout.setVisibility(View.VISIBLE);
			catalogsListView.setVisibility(View.GONE);			
		}
		
		CatalogsListAdapter adapter = new CatalogsListAdapter(dataCatalogs);
        catalogsListView.setAdapter(adapter);
        
        catalogsListView.setOnItemClickListener(new OnItemClickListener(){
            @Override 
            public void onItemClick(AdapterView<?> arg0, View arg1,int position, long arg3)
            { 
               	Intent intent = new Intent (Inner_Catalogs.this, Inner_CatalogGds.class);
	  			intent.putExtra("idCatalog",dataCatalogs.get(position).getCatalogId());
	  			startActivity(intent);
            }
        });
	}
	
	@SuppressLint("UseSparseArrays")
	private HashMap<Integer,CatalogData> CatalogsListJsonParser(String JsonString){
			 
		 dataCatalogs = new HashMap<Integer,CatalogData>();

		 try
	  		{
	    		JSONObject object = new JSONObject(JsonString);
	    		//Log.v("LOG_TAG","CatalogsLisJsonParser object = "+object);
	    		JSONArray catalogs = object.getJSONArray("items");
			 	new JSONObject(JsonString);
	    		
	    		Log.v("LOG_TAG","CatalogsLisJsonParser catalogs = "+catalogs);
	    		
	    		for (int i=0;i<catalogs.length();i++) {
	  				JSONObject item = catalogs.getJSONObject(i);
	  				
	  				CatalogData catalogItem = new CatalogData();
	  				
	  				if(item.optInt("id")>0)
	  					catalogItem.setCatalogId(item.optInt("id"));
	  				
	  				if(item.optString("title").length()>0)
	  					catalogItem.setCatalogTitle(item.optString("title"));
	  				
	  				if(item.optString("end_date").length()>0)
	  					catalogItem.setCatalogDateEnd(item.optString("end_date").split(" ")[0]);
	  				
	  				if(item.optString("cover").length()>0)
	  					catalogItem.setCatalogUrlImage(item.optString("cover"));
	  				
	  				dataCatalogs.put(i, catalogItem);
	  			}
			}catch(Exception e){
	    		Log.v("LOG_TAG",e.toString());	
			}
		 	return dataCatalogs;		 
	 }
	 
	 public class CatalogsListAdapter extends BaseAdapter {
			
			String LOG_TAG = this.getClass().getName()+"LOG";
			LayoutInflater inflator;
			HashMap<Integer, CatalogData> dataCatalogs;
			
			public CatalogsListAdapter(HashMap<Integer,CatalogData> data){
				 this.dataCatalogs = data;
			}			 
			 
			public class ViewHolder{
				 public ImageView catalogImage;
				 public TextView catalogTitle;
				 public TextView catalogLabel;
				 public TextView catalogDateEnd;
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
					  
					  view =	inflater.inflate(R.layout.cell_catalog, parent, false);
					  holder.catalogImage = (ImageView) view.findViewById(R.id.catalogImage);
					  holder.catalogTitle = (TextView) view.findViewById(R.id.catalogTitle);
					  holder.catalogLabel = (TextView) view.findViewById(R.id.catalogLabel);
					  holder.catalogDateEnd = (TextView) view.findViewById(R.id.catalogDateEnd);
					
					  holder.catalogTitle.setTypeface(fontMedium);
					  holder.catalogLabel.setTypeface(fontLight);
					  holder.catalogDateEnd.setTypeface(fontLight);
								  		
					  view.setTag(holder);
					  
				  } else{
					  holder = (ViewHolder) view.getTag();
				  }

				  holder.catalogTitle.setText(dataCatalogs.get(position).getCatalogTitle());
					  
				  holder.catalogDateEnd.setText(dataCatalogs.get(position).getCatalogDateEnd());
				 
				  /*
				  Transformation transformation = new Transformation() {
				        @Override
				        public Bitmap transform(Bitmap source) {
				            int targetWidth = holder.catalogImage.getWidth();
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
				        .load(dataCatalogs.get(position).getCatalogUrlImage())
				        .placeholder(R.drawable.no_photo)
				        .error(R.drawable.no_photo)
				        //.error(android.R.drawable.stat_notify_error)
				        .transform(transformation)
				        .into(holder.catalogImage, new Callback() {
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
				  
				  
				  Picasso.with(view.getContext()).load(dataCatalogs.get(position).getCatalogUrlImage())
				  	.placeholder(R.drawable.no_photo)
				  	.error(R.drawable.no_photo)
				  	.into(holder.catalogImage);
				  
				  return view;
				}
			 
			
				@Override
				public int getCount() {	
					if (dataCatalogs!=null)
						return dataCatalogs.size();
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
		
		Log.v("LOG_TAG","Inner_Catalogs onTaskCompletedJSON json = "+json);
		switch(id_request){
		
			case GlobalConstants.CATALOG:
				// TODO Auto-generated method stub
				dataCatalogs = CatalogsListJsonParser(json);
				fillData();
			break;
		
			//TODO ============ERRORS DEPLOYMENT==========
			case GlobalConstants.NO_INTERNET_CONNECTION:
				BottomDialog.BottomSelectDialog(Inner_Catalogs.this, 
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
	        	startActivity(new Intent(Inner_Catalogs.this, Out_RegInStart.class)); 
				finish();
				break;
			
			case GlobalConstants.ERROR_503:
				BottomDialog.BottomSelectDialog(Inner_Catalogs.this, 
						error503, 
		    			getString(R.string.dialog_Server503Title), 
		    			getString(R.string.dialog_Server503Text), 
		    			getString(R.string.dialog_Server503Left), 
		    			getString(R.string.dialog_Server503Right));
			break;
			
			default:
				BottomDialog.BottomSelectDialog(Inner_Catalogs.this, 
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
	            	Inner_Catalogs.this.finishAffinity();
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
	            	Inner_Catalogs.this.finishAffinity();
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
		tracker.setScreenName("catalogs");
		tracker.send(new HitBuilders.EventBuilder().build());
	}
}
