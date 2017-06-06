package ua.metro.mobileapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ua.metro.mobileapp.HttpMetods.ConnectivityInfo;
import ua.metro.mobileapp.HttpMetods.DirectionsJSONParser;
import ua.metro.mobileapp.HttpMetods.GPS_Tracker;
import ua.metro.mobileapp.HttpMetods.HTTPAsynkTask;
import ua.metro.mobileapp.HttpMetods.UserState;
import ua.metro.mobileapp.UI.BottomDialog;
import ua.metro.mobileapp.application.MeApp;
import ua.metro.mobileapp.datamodel.RegionData;
import ua.metro.mobileapp.datamodel.ShopsData;

public class Inner_Map extends FragmentActivity implements OnTaskCompleted,
		ConnectionCallbacks,
		OnConnectionFailedListener,
        OnMapReadyCallback {
	
	  SupportMapFragment mapFragment;
	  GoogleMap map;
	  GPS_Tracker tracker;
	  double lattitude = 0, longitude = 0;
	  ArrayList<RegionData> regionList;
	  ArrayList<ShopsData> shopsList;
	  ExpandableListView elvMap;
	  MapExpListAdapter elvMapAdapter;
	  Typeface clearSans, clearSansMedium, clearSansLight;
	  int conType = 1;
	  TextView blackButtonText, yellowButtonText;
	  LatLng traceTo, myPos;
	  RelativeLayout yellowButton;
	  ProgressDialog loader;
//	  View subHeader;
	  //private GoogleApiClient mLocationClient;
    private int toMapFrom = 0;
	  
	@Override
	public void onBackPressed() {
		finish();
	}
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		
		UserState.restoreUserState(this);
		
        super.onCreate(savedInstanceState);
        
        if (!checkGooglePlayServiceAvailability(this, 7571000)){
//        	BottomDialog.BottomMessageDialog(this, getGooglePlayStatus,
//        			getString(R.string.map_noServicesTitle),
//        			getString(R.string.map_noServicesText));
        } else {
        	setContentView(R.layout.inner_map);
            weCanRun();           
            setAnalytics();       	
        }

	}
	
	protected void onPause() {
        super.onPause();
        UserState.saveUserState(this);
	}
	
	protected void onResume() {
        super.onResume();
        UserState.restoreUserState(this);
	}
	
	void weCanRun(){
        
        if(ConnectivityInfo.isConnectedFast(Inner_Map.this)){
        	conType = 0;
        } else {
        	conType = 1;
        }
        
        clearSans = FontCache.get("fonts/clearsans.ttf", getBaseContext());
        clearSansMedium = FontCache.get("fonts/clearsansmedium.ttf", getBaseContext());
        clearSansLight = FontCache.get("fonts/clearsanslight.ttf", getBaseContext());
        
        tracker = new GPS_Tracker(this);
        
        HTTPAsynkTask mtShop = new HTTPAsynkTask(Inner_Map.this,
				GlobalConstants.GET_SHOP, 
				//null, null, 
				null, 
				Inner_Map.this);
        mtShop.execute();
        loader = new ProgressDialog(Inner_Map.this);
        loader.setMessage(getString(R.string.app_Loading));
        loader.show();
        
        Utils.showActionBar(this, true);
		MeApp.menuSelected = 3;
		Utils.setBottomMenu(this,MeApp.menuSelected);
        
//        subHeader = (View) findViewById(R.id.subHeaderLayout);
//		// Back button
//        ImageButton iconBackImageButton = (ImageButton) subHeader.findViewById(R.id.iconBackImageButton);
//		iconBackImageButton.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// TODO Back Button
//				finish();
//			}
//		});
//
//		TextView subTitleTextView = (TextView) subHeader.findViewById(R.id.subTitleTextView);


		RelativeLayout blackButton = (RelativeLayout) findViewById(R.id.blackButton);
        yellowButton = (RelativeLayout) findViewById(R.id.yellowButton);        
        
        blackButtonText = (TextView)findViewById(R.id.blackButtonText);
        yellowButtonText = (TextView)findViewById(R.id.yellowButtonText);

//        subTitleTextView.setTypeface(clearSansMedium);
//        subTitleTextView.setText(getString(R.string.map_subHeader));

        blackButtonText.setTypeface(clearSans);
        yellowButtonText.setTypeface(clearSans);
        
        elvMap = (ExpandableListView) findViewById(R.id.elvMap);
        
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapView);

        
//        if (tracker.canGetLocation()){
        	yellowButtonText.setText(getString(R.string.map_findButton));
//        }
//        else {
//        	LinearLayout.LayoutParams paramYellow = new LinearLayout.LayoutParams(
//                    LayoutParams.MATCH_PARENT,
//                    LayoutParams.MATCH_PARENT, 0.0f);
//        	yellowButton.setVisibility(View.GONE);
//        	yellowButton.setLayoutParams(paramYellow);
//        	LinearLayout.LayoutParams paramBlack = new LinearLayout.LayoutParams(
//                    LayoutParams.MATCH_PARENT,
//                    LayoutParams.MATCH_PARENT, 0.0f);
//        	blackButton.setLayoutParams(paramBlack);
//        }
        
		switch (conType) {		
			case 0://TODO Fast Internet connection
				blackButtonText.setText(getString(R.string.map_closestStore));
				elvMap.setVisibility(View.GONE);
				setMapView();			
				break;
			case 1://TODO Slow Internet connection
				
				blackButtonText.setText(getString(R.string.map_listStore));	
				
		        FragmentManager fm = getSupportFragmentManager();
		        FragmentTransaction ft = fm.beginTransaction();
		        ft.hide(mapFragment).commit(); 
		        
				setListView();
				break;
		}
	}
	
	Handler getGooglePlayStatus = new Handler() {

	    @Override
	    public void handleMessage(Message msg) {
	        if (msg != null) {
	            // Check for dialog box responses
	        	finish();
	        }
	    }
	};
	
	Handler getGeoStatus = new Handler() {

	    @Override
	    public void handleMessage(Message msg) {
	        if (msg != null) {
	            // Check for dialog box responses
	            if (msg.what == GlobalConstants.retCodeLeft) {
	            	//YES
	            	Intent viewIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
	            	startActivity(viewIntent);
	            }
	            else if (msg.what == GlobalConstants.retCodeRight) {
	                //NO

	            }
	            else if (msg.what == GlobalConstants.retCodeClose) {
	                //CLOSED

	            }
	        }
	    }
	};
	
	Handler getGeoError = new Handler() {

	    @Override
	    public void handleMessage(Message msg) {
	        if (msg != null) {
	            // Check for dialog box responses
	            if (msg.what == GlobalConstants.retCodeLeft) {
	            	//YES
	            	Intent viewIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
	            	startActivity(viewIntent);
	            }
	            else if (msg.what == GlobalConstants.retCodeRight) {
	                //NO

	            }
	            else if (msg.what == GlobalConstants.retCodeClose) {
	                //CLOSED

	            }
	        }
	    }
	};
	
	Handler getTraceRoute = new Handler() {

	    @Override
	    public void handleMessage(Message msg) {
	        if (msg != null) {
	            if (msg.what == GlobalConstants.retCodeLeft) {
	            	//New Tracker Cast
	            	tracker = new GPS_Tracker(Inner_Map.this);
	            	
	            	if (tracker.canGetLocation()){ //
		                lattitude = tracker.getLatitude();
		                longitude = tracker.getLongitude();
		                if(lattitude !=0 & longitude != 0){
		                	myPos = new LatLng(lattitude, longitude);
			                elvMap.setVisibility(View.GONE);
                            toMapFrom = 1;
                            mapFragment.getMapAsync(Inner_Map.this);

//			            	map = mapFragment.getMapAsync(Inner_Map.this);
//			                if (map == null) {
//			                  finish();
//			                  return;
//			                }
//			                map.getUiSettings().setMapToolbarEnabled(false);
//			        		CameraPosition cameraPosition = new CameraPosition.Builder()
//			    					.target(new LatLng(49.222234, 31.205269)).zoom((float) 4.8) //.bearing(45).tilt(20)
//			    					.build();
//			    			CameraUpdate cameraUpdate = CameraUpdateFactory
//			    					.newCameraPosition(cameraPosition);
//			    			map.animateCamera(cameraUpdate);
//			            	map.clear();
//			            	//TODO Go to trace route
//			            	RunRoute();

		                } else {
		                	BottomDialog.BottomSelectDialog(Inner_Map.this, 
		                			getGeoError, 
				        			getString(R.string.dialog_GeoErrorTitle), 
				        			getString(R.string.dialog_GeoErrorText),
				        			getString(R.string.dialog_GeoErrorLeft), 
				        			getString(R.string.dialog_GeoErrorRight));
		                }
		                
		            	
		            } else {
		            	//Go to settings for GeoLocation 
		            	BottomDialog.BottomSelectDialog(Inner_Map.this, 
		            			getGeoStatus, 
		            			getString(R.string.dialog_AccessTitle), 
		            			getString(R.string.dialog_AccessText), 
		            			getString(R.string.dialog_AccessLeft), 
		            			getString(R.string.dialog_AccessRight));
		            }
	            	
	            	
	            } else if (msg.what == GlobalConstants.retCodeClose) {
	                //CLOSED
	            }
	        }
	    }
	};
	
	void RunRoute(){
		String url = Utils.getDirectionsUrl(myPos, traceTo);
		DownloadTask downloadTask = new DownloadTask();
		downloadTask.execute(url);
	}
	
	void buildMapObjects(){
		//TODO buildMapObjects
	}
	
	void setMapView(){
		
		yellowButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				tracker = new GPS_Tracker(Inner_Map.this);
			    //TODO Get Current location     
		        if (tracker.canGetLocation() ){ //
		                lattitude = tracker.getLatitude();
		                longitude = tracker.getLongitude();
		                if(lattitude !=0 & longitude != 0){
		                	HashMap<String,String> params = new HashMap<String,String>();
			                params.put("latitude", Double.toString(lattitude));
			                params.put("longitude", Double.toString(longitude));
			                
			                Log.v("TAG", "METRO LAT=" + lattitude + " LON=" + longitude);
			                
			                loader = new ProgressDialog(Inner_Map.this);
							loader.setMessage(getString(R.string.app_Loading));
							loader.show();
							
			                HTTPAsynkTask mtShops = new HTTPAsynkTask(Inner_Map.this,
			        				GlobalConstants.GET_NEAREST_SHOPS, 
			        				//null, null, 
			        				params, 
			        				Inner_Map.this);
			                mtShops.execute();
			                myPos = new LatLng(lattitude, longitude);
		                } else {
		                	BottomDialog.BottomSelectDialog(Inner_Map.this, 
		                			getGeoError, 
				        			getString(R.string.dialog_GeoErrorTitle), 
				        			getString(R.string.dialog_GeoErrorText),
				        			getString(R.string.dialog_GeoErrorLeft), 
				        			getString(R.string.dialog_GeoErrorRight));
		                }
		                
		                
		        } else {
		        	BottomDialog.BottomSelectDialog(Inner_Map.this, 
		        			getGeoStatus, 
		        			getString(R.string.dialog_AccessTitle), 
		        			getString(R.string.dialog_AccessText),
		        			getString(R.string.dialog_AccessLeft), 
		        			getString(R.string.dialog_AccessRight));
		        }		        
				
			}			
		});

        toMapFrom = 2;
        mapFragment.getMapAsync(Inner_Map.this);

//        if (map == null) {
//          finish();
//          return;
//        }
//        map.getUiSettings().setMapToolbarEnabled(false);
//		CameraPosition cameraPosition = new CameraPosition.Builder()
//				.target(new LatLng(49.222234, 31.205269)).zoom((float) 4.8) //.bearing(45).tilt(20)
//				.build();
//		CameraUpdate cameraUpdate = CameraUpdateFactory
//				.newCameraPosition(cameraPosition);
//		map.animateCamera(cameraUpdate);
//        //map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

	}
	
	
	void setMarkers() {
		for(int i = 0;i<regionList.size();i++){
			for(int j = 0;j<regionList.get(i).getShops().size();j++){
				double lat, lon;
				String title;
				lat = Double.parseDouble(regionList.get(i).getShops().get(j).getLat());
				lon = Double.parseDouble(regionList.get(i).getShops().get(j).getLon());
				title = "METRO " + regionList.get(i).getTitle();
				try{
				map.addMarker(new MarkerOptions()
			      .position(new LatLng(lat, lon))
			      .title(title).icon(BitmapDescriptorFactory.fromResource(R.drawable.store_pin))
			      .snippet(regionList.get(i).getShops().get(j).getAddress()));
				} catch (Exception e){
					
				/*	//TO DO noGooglePlayServices 
					AlertDialog.Builder builder = new AlertDialog.Builder(Inner_Map.this);
					builder.setTitle(getString(R.string.map_noServicesTitle))
							.setMessage(getString(R.string.map_noServicesText))
							.setIcon(R.drawable.store_pin)
							.setCancelable(false)
							.setPositiveButton(getString(R.string.map_noServicesButton),
									new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog, int id) {
											dialog.cancel();
											finish();
										}
									});
					AlertDialog alert = builder.create();
					alert.show();
					*/
				}
				
			}
		}
		map.setOnMarkerClickListener(new OnMarkerClickListener(){
			@Override
			public boolean onMarkerClick(Marker arg0) {
				// TODO onMarkerClick
				
				if (arg0.getTitle().equalsIgnoreCase(getString(R.string.map_myPlace))){
					
				} else if (arg0.getTitle().equalsIgnoreCase(getString(R.string.map_myDestination))){
					
				} else {
				BottomDialog.BottomMapDialog(Inner_Map.this, getTraceRoute,
						getString(R.string.map_selectedStore),
						arg0.getTitle(),
						arg0.getSnippet());
				traceTo = new LatLng(arg0.getPosition().latitude, arg0.getPosition().longitude);
				
				Log.v("TAG", "METRO Marker click id=" + arg0.getId());
				//String address = arg0.getSnippet();
				}
				
				return false;
			}
			
		});
	}
	
	void setListView(){
		
		yellowButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				tracker = new GPS_Tracker(Inner_Map.this);
			    //TODO Get Current location     
		        if (tracker.canGetLocation()){ //
                    lattitude = tracker.getLatitude();
		                longitude = tracker.getLongitude();
		                if(lattitude !=0 & longitude != 0){
		                	HashMap<String,String> params = new HashMap<String,String>();
			                params.put("latitude", Double.toString(lattitude));
			                params.put("longitude", Double.toString(longitude));
			                
			                Log.v("TAG", "METRO LAT=" + lattitude + " LON=" + longitude);
			                
			                loader = new ProgressDialog(Inner_Map.this);
							loader.setMessage(getString(R.string.app_Loading));
							loader.show();
							
			                HTTPAsynkTask mtShops = new HTTPAsynkTask(Inner_Map.this,
			        				GlobalConstants.GET_NEAREST_SHOPS, 
			        				//null, null, 
			        				params, 
			        				Inner_Map.this);
			                mtShops.execute();
			                myPos = new LatLng(lattitude, longitude);
                            toMapFrom = 3;
                            mapFragment.getMapAsync(Inner_Map.this);

//			                map = mapFragment.getMap();
//			                if (map == null) {
//			                  finish();
//			                  return;
//			                }
//			                map.getUiSettings().setMapToolbarEnabled(false);
//			        		CameraPosition cameraPosition = new CameraPosition.Builder()
//			        				.target(new LatLng(49.222234, 31.205269)).zoom((float) 4.8) //.bearing(45).tilt(20)
//			        				.build();
//			        		CameraUpdate cameraUpdate = CameraUpdateFactory
//			        				.newCameraPosition(cameraPosition);
//			        		map.animateCamera(cameraUpdate);
//
//			        		FragmentManager fm = getSupportFragmentManager();
//					        FragmentTransaction ft = fm.beginTransaction();
//					        ft.show(mapFragment).commit();

		                } else {
		                	BottomDialog.BottomSelectDialog(Inner_Map.this, 
		                			getGeoError, 
				        			getString(R.string.dialog_GeoErrorTitle), 
				        			getString(R.string.dialog_GeoErrorText),
				        			getString(R.string.dialog_GeoErrorLeft), 
				        			getString(R.string.dialog_GeoErrorRight));
		                }
		                
		        } else {
		        	BottomDialog.BottomSelectDialog(Inner_Map.this, 
		        			getGeoStatus, 
		        			getString(R.string.dialog_AccessTitle), 
		        			getString(R.string.dialog_AccessText),
		        			getString(R.string.dialog_AccessLeft), 
		        			getString(R.string.dialog_AccessRight));
		        }		        
				
			}			
		});
		
		blackButtonText.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				elvMap.setVisibility(View.VISIBLE);
				FragmentManager fm = getSupportFragmentManager();
		        FragmentTransaction ft = fm.beginTransaction();
		        ft.hide(mapFragment).commit();
				}
		});
		
		elvMapAdapter = new MapExpListAdapter();
		elvMap.setGroupIndicator(new ColorDrawable(Color.TRANSPARENT));
		
		
		elvMap.setOnChildClickListener(new OnChildClickListener(){

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				// TODO onChildClick
				BottomDialog.BottomMapDialog(Inner_Map.this, getTraceRoute,
						getString(R.string.map_selectedStore),
						"METRO " + regionList.get(groupPosition).getTitle(),
						regionList.get(groupPosition).getShops().get(childPosition).getAddress());
				traceTo = new LatLng(Double.parseDouble(regionList.get(groupPosition).getShops().get(childPosition).getLat()),
						Double.parseDouble(regionList.get(groupPosition).getShops().get(childPosition).getLon()));
				
				FragmentManager fm = getSupportFragmentManager();
		        FragmentTransaction ft = fm.beginTransaction();
		        ft.show(mapFragment).commit(); 
				
				return false;
			}
			
		});
	}
	
	@Override
    protected void onRestart() {
        super.onRestart();

        Utils.showActionBar(this, true);
    }

	@Override
	public void onTaskCompleted(int id_request) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTaskCompletedJSON(String json, int id_request) {
		//onTaskCompletedJSON
		if( loader != null && loader.isShowing() )
			loader.dismiss();
		
		switch(id_request){
		case GlobalConstants.GET_NEAREST_SHOPS:
			
			Log.v("TAG", "METRO GET_NEAREST_SHOPS=" + json);
			
			LatLngBounds.Builder bc = new LatLngBounds.Builder();
			map.clear();
			
			try {
				JSONObject jsonDataGetNearestShops = new JSONObject(json);
				JSONArray itemsData = jsonDataGetNearestShops.getJSONArray("items");
				shopsList = new ArrayList<ShopsData>();
				//for (int j=0;j < itemsData.length();j++){
				int j = 0;
					ShopsData shopsTemp = new ShopsData();
					shopsTemp.setId(itemsData.getJSONObject(j).optInt("id"));
					shopsTemp.setRegion_title(itemsData.getJSONObject(j).optString("region_title"));
					shopsTemp.setRegion_id(itemsData.getJSONObject(j).optInt("region_id"));
					shopsTemp.setAddress(itemsData.getJSONObject(j).optString("address"));
					shopsTemp.setMap_embedded(itemsData.getJSONObject(j).optString("map_embedded"));
					shopsTemp.setLat(itemsData.getJSONObject(j).optString("lat"));
					shopsTemp.setLon(itemsData.getJSONObject(j).optString("lon"));
					shopsTemp.setDistance(itemsData.getJSONObject(j).optDouble("distance"));
					shopsList.add(shopsTemp);
					
					
					//Put data for location array
					try{
						LatLng tempLatLng = new LatLng(Double.parseDouble(shopsTemp.getLat()), 
								Double.parseDouble(shopsTemp.getLon()));
						//map_points.add(tempLatLng);
						bc.include(tempLatLng);
						
						//Set Markers for nearest shops
						Log.v("TAG", "METRO shopsTemp.getRegion_title()=" + shopsTemp.getRegion_title());
						
						map.addMarker(new MarkerOptions()
					      .position(tempLatLng)
					      .title("METRO " + shopsTemp.getRegion_title())
					      .icon(BitmapDescriptorFactory.fromResource(R.drawable.store_pin))
					      )
					      //.snippet(shopsTemp.getAddress()))
					      .showInfoWindow();
						
					}catch(Exception e){
						Log.v("TAG", "METRO Double.parseDouble Exception=" + e);
					}
				//}
				

				switch(conType){
				case 0:
		
					map.addMarker(new MarkerOptions()
				      .position(myPos)
				      .title(getString(R.string.map_myPlace))
				      .icon(BitmapDescriptorFactory.fromResource(R.drawable.store_pin_blue)))
				      .showInfoWindow();
					
					bc.include(myPos);
		            map.moveCamera(CameraUpdateFactory.newLatLngBounds(bc.build(), 150));
					
					break;
					
				case 1:
					elvMap.setVisibility(View.GONE);
					
					map.addMarker(new MarkerOptions()
				      .position(myPos)
				      .title(getString(R.string.map_myPlace))
				      .icon(BitmapDescriptorFactory.fromResource(R.drawable.store_pin_blue)))
				      .showInfoWindow();
					
					bc.include(myPos);
		            map.moveCamera(CameraUpdateFactory.newLatLngBounds(bc.build(), 150));
					
					break;
				}
				buildMapObjects();
				
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//nearestShops
			break;
			
		case GlobalConstants.GET_SHOP:
			try {
				
				JSONObject jsonDataGetShop = new JSONObject(json);
				JSONObject itemsData = jsonDataGetShop.getJSONObject("items");
				regionList = new ArrayList<RegionData>();
				Log.v("TAG", "METRO itemsData.length()=" + itemsData.length());
				for (int i = 0;i < 35; i++){
					if (!itemsData.isNull(Integer.toString(i))){
						JSONObject itemsTemp = itemsData.getJSONObject(Integer.toString(i));						
						RegionData regionTemp = new RegionData();
						
						regionTemp.setId(itemsTemp.optInt("id"));
						regionTemp.setTitle(itemsTemp.optString("title"));
						Log.v("TAG", "METRO GET_Region title=" + regionTemp.getTitle());
						
						JSONArray shopsData = itemsTemp.getJSONArray("shops");
						for (int j = 0;j < shopsData.length(); j++){
							ShopsData shopsTemp = new ShopsData();
							shopsTemp.setId(shopsData.getJSONObject(j).optInt("id"));
							shopsTemp.setRegion_id(shopsData.getJSONObject(j).optInt("region_id"));
							shopsTemp.setAddress(shopsData.getJSONObject(j).optString("address"));
							shopsTemp.setMap_embedded(shopsData.getJSONObject(j).optString("map_embedded"));
							shopsTemp.setLat(shopsData.getJSONObject(j).optString("lat"));
							shopsTemp.setLon(shopsData.getJSONObject(j).optString("lon"));
							regionTemp.getShops().add(shopsTemp);
							Log.v("TAG", "METRO GET_Shop address=" + shopsTemp.getAddress());
						}
						regionList.add(regionTemp);
					}
				}
				
				switch(conType){
				case 0:
					setMarkers();
					break;
				case 1:
					elvMap.setAdapter(elvMapAdapter);
					break;
				}
				
				Log.v("TAG", "METRO GET_SHOP Parsing ok");
				Log.v("TAG", "METRO GET_SHOP regionList size=" + regionList.size());
				//setListView();
				
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				Log.v("TAG", "METRO GET_SHOP Parsing JSONException=" + e);
				e.printStackTrace();
			}
			break;
			
			case GlobalConstants.NO_INTERNET_CONNECTION:
				BottomDialog.BottomSelectDialog(Inner_Map.this, 
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
	        	startActivity(new Intent(Inner_Map.this, Out_RegInStart.class)); 
				finish();
				break;
			
			case GlobalConstants.ERROR_503:
				BottomDialog.BottomSelectDialog(Inner_Map.this, 
						error503, 
		    			getString(R.string.dialog_Server503Title), 
		    			getString(R.string.dialog_Server503Text), 
		    			getString(R.string.dialog_Server503Left), 
		    			getString(R.string.dialog_Server503Right));
			break;
			
			default:
				BottomDialog.BottomSelectDialog(Inner_Map.this, 
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
	            	Inner_Map.this.finishAffinity();
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
	            	Inner_Map.this.finishAffinity();
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
		if (map == null) {
			finish();
			return;
		}
        CameraPosition cameraPosition;
        CameraUpdate cameraUpdate;
        map.getUiSettings().setMapToolbarEnabled(false);
        cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(49.222234, 31.205269)).zoom((float) 4.8) //.bearing(45).tilt(20)
                .build();
        cameraUpdate = CameraUpdateFactory
                .newCameraPosition(cameraPosition);
        map.animateCamera(cameraUpdate);

        switch (toMapFrom){
            case 1:
                map.clear();
                //TODO Go to trace route
                RunRoute();
                break;
            case 2:
                break;
            case 3:
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.show(mapFragment).commit();
                break;
        }

    }

    class MapExpListAdapter extends BaseExpandableListAdapter {

		@Override
		public int getGroupCount() {
			// TODO Auto-generated method stub
			return regionList.size();
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			// TODO Auto-generated method stub
			return regionList.get(groupPosition).getShops().size();
		}

		@Override
		public Object getGroup(int groupPosition) {
			// TODO Auto-generated method stub
			return regionList.get(groupPosition);
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return regionList.get(groupPosition).getShops().get(childPosition);
		}

		@Override
		public long getGroupId(int groupPosition) {
			// TODO Auto-generated method stub
			return groupPosition;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return childPosition;
		}

		@Override
		public boolean hasStableIds() {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			// TODO getGroupView
			if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.map_group, null); 
			}
			 TextView textGroup = (TextView) convertView.findViewById(R.id.titleGroup);
	         textGroup.setTypeface(clearSansMedium);
	         textGroup.setText(regionList.get(groupPosition).getTitle());
	         
	         ImageView imageGroup = (ImageView) convertView.findViewById(R.id.imageGroup);
	         if (isExpanded){
	            	imageGroup.setImageResource(R.drawable.open);
	            }else{
	            	imageGroup.setImageResource(R.drawable.close);
		        }
	         
	         
			return convertView;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			// TODO getChildView
			if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.map_child, null);
            }
			TextView titleShop = (TextView) convertView.findViewById(R.id.titleShop);
			TextView addressShop = (TextView) convertView.findViewById(R.id.addressShop);
			titleShop.setTypeface(clearSansMedium);
			addressShop.setTypeface(clearSansMedium);
			
			titleShop.setText("METRO " + regionList.get(groupPosition).getTitle());
			addressShop.setText(regionList.get(groupPosition).getShops().get(childPosition).getAddress());
			
			return convertView;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return true;
		}
	}
	
    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String>{
 
        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {
 
            // For storing data from web service
            String data = "";
 
            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }
 
        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
 
            ParserTask parserTask = new ParserTask();
 
            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }
	
    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException{
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);
 
            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();
 
            // Connecting to url
            urlConnection.connect();
 
            // Reading data from url
            iStream = urlConnection.getInputStream();
 
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
 
            StringBuffer sb = new StringBuffer();
 
            String line = "";
            while( ( line = br.readLine()) != null){
                sb.append(line);
            }
 
            data = sb.toString();
 
            br.close();
 
        }catch(Exception e){
            Log.v("TAG", "METRO Exception while downloading url" + e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        Log.v("TAG", "METRO data=" + data);
        return data;
    }
	
    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{
 
        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
 
            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;
 
            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();
 
                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
                Log.v("TAG", "METRO ParserTask Exception=" + e);
            }
            return routes;
        }
 
        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();
                      
            LatLngBounds.Builder bc = new LatLngBounds.Builder(); //--
 
            // Traversing through all the routes
            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();
 
                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);
 
                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);
 
                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);
                    
                    bc.include(position); //--
 
                    points.add(position);
                }
 
                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(6);
                lineOptions.color(getResources().getColor(R.color.metro_trace));
            }
            
            List<LatLng> map_points = new ArrayList<LatLng>();
            map_points = lineOptions.getPoints();
            //LatLngBounds.Builder bc = new LatLngBounds.Builder();
            for (LatLng item : map_points) {
                bc.include(item);
            }
            map.moveCamera(CameraUpdateFactory.newLatLngBounds(bc.build(), 50));
            
            // Drawing polyline in the Google Map for the i-th route
            markerOptions.position(myPos);
            map.addMarker(markerOptions.title(getString(R.string.map_myPlace))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.store_pin_blue)));
            markerOptions.position(traceTo);
            map.addMarker(markerOptions.title(getString(R.string.map_myDestination))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.store_pin)));
            map.addPolyline(lineOptions);
            
            /*map.setOnMarkerClickListener(new OnMarkerClickListener() {
                public boolean onMarkerClick(Marker arg0) {
                    return true;
                }
            });*/
        }
    }

	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub
		Log.v("TAG", "METRO GOOGLE onConnected");
		weCanRun();
	}

	@Override
	public void onConnectionSuspended(int arg0) {
		// TODO Auto-generated method stub
		Log.v("TAG", "METRO GOOGLE onConnectionSuspended");
		
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub
		Log.v("TAG", "METRO GOOGLE onConnectionFailed");
		AlertDialog.Builder builder = new AlertDialog.Builder(Inner_Map.this);
		builder.setTitle(getString(R.string.map_noServicesTitle))
				.setMessage(getString(R.string.map_noServicesText))
				.setIcon(R.drawable.store_pin)
				.setCancelable(false)
				.setPositiveButton(getString(R.string.map_noServicesButton),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
								finish();
							}
						});
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	
	
	/**
	 * Check if correct Play Services version is available on the device.
	 *
	 * @param context
	 * @param versionCode
	 * @return boolean
	 */
	public boolean checkGooglePlayServiceAvailability(Context context, int versionCode) {

        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(context);
        if(result != ConnectionResult.SUCCESS) {
            if(googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result,
                        121212).show();
            }
            Log.e("--->>", "checkGooglePlayServiceAvailability false");
            return false;
        } else {
            Log.e("--->>", "checkGooglePlayServiceAvailability true");
            return true;
        }

	    // Query for the status of Google Play services on the device
//	    int statusCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
//
//	    if ((statusCode == ConnectionResult.SUCCESS)
//	            && (GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_VERSION_CODE >= versionCode)) {
//	        return true;
//	    } else {
//	        return false;
//	    }
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
		tracker.setScreenName("map");
		tracker.send(new HitBuilders.EventBuilder().build());
	}
	

}
