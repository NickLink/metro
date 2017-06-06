package ua.metro.mobileapp;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import ua.metro.mobileapp.HttpMetods.HTTPAsynkTask;
import ua.metro.mobileapp.HttpMetods.UserState;
import ua.metro.mobileapp.UI.BottomDialog;
import ua.metro.mobileapp.UI.OnSingleClickListener;
import ua.metro.mobileapp.application.MeApp;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.dm.zbar.android.scanner.ZBarConstants;
import com.dm.zbar.android.scanner.ZBarScannerActivity;
import com.facebook.appevents.AppEventsLogger;

public class Out_ScanIn extends Activity implements OnTaskCompleted{
	    String barcodeString;
	    int bartypeInt, RUN_FROM;
	    
		Typeface clearSansMedium;
		ViewPager scanInPager;
		final int num_pages = 2;
		Button sw_scanButton, sw_inputButton;
		
		View subheader_layout;
		EditText cardNumberEditText;
		ProgressDialog loader;
		TextView inputTextView;

	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        requestWindowFeature(Window.FEATURE_NO_TITLE);
	        
	        UserState.restoreUserState(this);
	        
	        setContentView(R.layout.out_scanin_main);
	        
	        
	        clearSansMedium = FontCache.get("fonts/clearsansmedium.ttf", getBaseContext());
	        
	        
	        
	        View subheader_layout = (View) findViewById(R.id.subheader_layout);
	        TextView subTitleTextView = (TextView) subheader_layout.findViewById(R.id.subTitleTextView);
	        
	        
	        RUN_FROM = getIntent().getIntExtra("RUN_FROM", 0);
	        
	        if (RUN_FROM == 0){
	        	//Set title Authorization
	        	subTitleTextView.setText(getString(R.string.login_authorization));
	        } else {
	        	//Set title get Permanent card
	        	subTitleTextView.setText(getString(R.string.app_PermanentSubHeader));
	        }
	        
	        //BackButton
	        ImageButton iconBackImageButton = (ImageButton) subheader_layout.findViewById(R.id.iconBackImageButton);
	        iconBackImageButton.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					// TODO Back Button
					finish();				
				}	            	
	        });
	        
	        
	        scanInPager = (ViewPager) findViewById(R.id.scanin_Pager);	        
	              
	    	sw_scanButton = (Button) findViewById(R.id.scanButton);
	      	sw_inputButton = (Button) findViewById(R.id.inputButton);
	     
	        subTitleTextView.setTypeface(clearSansMedium);
	        sw_scanButton.setTypeface(clearSansMedium);
	        sw_inputButton.setTypeface(clearSansMedium);
	        
	        scanInPager.setAdapter(new scaninAdapter());
	        scanInPager.addOnPageChangeListener(new OnPageChangeListener(){
				@Override
				public void onPageScrollStateChanged(int arg0) {
					// TODO Auto-generated method stub				
				}
				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) {
					// TODO Auto-generated method stub				
				}
				@Override
				public void onPageSelected(int arg0) {
					// TODO Auto-generated method stub
					if (arg0 == 0){
						Utils.hideKeyboard(Out_ScanIn.this);
						
						sw_scanButton.setBackgroundResource(R.drawable.bg_gray_border);
						sw_scanButton.setTextColor(Color.WHITE);
						sw_inputButton.setBackgroundResource(0);
						sw_inputButton.setTextColor(Color.BLACK);					
					} else {
						cardNumberEditText.requestFocus();
						InputMethodManager imm = (InputMethodManager)
	                            getSystemService(Context.INPUT_METHOD_SERVICE);
						if(imm != null){
						   imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
						}
						
						sw_inputButton.setBackgroundResource(R.drawable.bg_gray_border);
						sw_inputButton.setTextColor(Color.WHITE);
						sw_scanButton.setBackgroundResource(0);
						sw_scanButton.setTextColor(Color.BLACK);
					}
				}        	
	        });
	        
	        sw_scanButton.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					scanInPager.setCurrentItem(0);
					//
					sw_scanButton.setBackgroundResource(R.drawable.bg_gray_border);
					sw_scanButton.setTextColor(Color.WHITE);
					sw_inputButton.setBackgroundResource(0);
					sw_inputButton.setTextColor(Color.BLACK);
				}			
			});
	        sw_inputButton.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					scanInPager.setCurrentItem(1);
					//
					sw_inputButton.setBackgroundResource(R.drawable.bg_gray_border);
					sw_inputButton.setTextColor(Color.WHITE);
					sw_scanButton.setBackgroundResource(0);
					sw_scanButton.setTextColor(Color.BLACK);
				}			
			});
	      	 
	    }
	    
	    protected void onPause() {
	        super.onPause();
	        UserState.saveUserState(this);
		}
		
		protected void onResume() {
	        super.onResume();
	        UserState.restoreUserState(this);
		}
	    
	    private class scaninAdapter extends PagerAdapter {
	    	
	    	Button scanButton, confirmButton;
	    	
	        @Override
	        public int getCount() {
	            return num_pages;
	        }

	        @Override
	        public boolean isViewFromObject(View view, Object o) {
	            return view == o;
	        }

	        @Override
	        public void destroyItem(ViewGroup container, int position, Object object) {
	            container.removeView((View)object);
	        }

	        @Override
	        public Object instantiateItem(ViewGroup container, int position) {
	        	
	            View new_view=null;
	            LayoutInflater inflater = getLayoutInflater();

	            if (position == 0){
		            new_view = inflater.inflate(R.layout.out_scanin_scan, null);
		            //Definition
			      	TextView scanTextView = (TextView) new_view.findViewById(R.id.scanTextView);
			      	scanButton = (Button) new_view.findViewById(R.id.scanButton);
			      	
		            //Set fonts
			      	scanTextView.setTypeface(clearSansMedium);
			      	scanButton.setTypeface(clearSansMedium);

		            
		            //Set OnClickListeners
			      	scanButton.setOnClickListener(new OnSingleClickListener(){
						@Override
						public void onSingleClick(View v) {
							// TODO Go to scan barcode
							Intent intent = new Intent(Out_ScanIn.this, ZBarScannerActivity.class);
							startActivityForResult(intent, GlobalConstants.REQUEST_CODE_ZBAR);
						}	            	
		            });
			      	//Set selector change button alpha
			      	scanButton.setOnTouchListener(new OnTouchListener() {
			            @Override
			            public boolean onTouch(View v, MotionEvent event) {
			                switch (event.getAction()) {
			                case MotionEvent.ACTION_DOWN:
			                	scanButton.setAlpha((float) 0.55);
			                    return false;
			                case MotionEvent.ACTION_UP:
			                	scanButton.setAlpha((float) 1.0);
			                    return false;
			                default:
			                    return false;
			                }
			            }
			      	});	      	
		            container.addView(new_view);
		            
	            } else {
		            new_view = inflater.inflate(R.layout.out_scanin_in, null);
		            //Definition
			      	inputTextView = (TextView) new_view.findViewById(R.id.inputTextView);
			      	confirmButton = (Button) new_view.findViewById(R.id.confirmButton);

		            cardNumberEditText = (EditText) new_view.findViewById(R.id.cardNumberEditText);
		            //Set fonts
			      	inputTextView.setTypeface(clearSansMedium);
			      	confirmButton.setTypeface(clearSansMedium);


		            //Set OnClickListeners
			      	confirmButton.setOnClickListener(new OnSingleClickListener(){
						@Override
						public void onSingleClick(View v) {
							// TODO Go to API CONFIRM CARD CODE
							
							if (cardNumberEditText.getText().toString().length()!=22){
								inputTextView.setText(getString(R.string.login_cardnotexisttitle));
								inputTextView.setTextColor(getResources().getColor(R.color.metro_red));
								
								/*Toast.makeText(Out_ScanIn.this, 
										getString(R.string.Error_card_numberIncorrect), 
										Toast.LENGTH_LONG).show();*/
							} else {
								inputTextView.setText(getString(R.string.login_inputcodetitle));
								inputTextView.setTextColor(getResources().getColor(R.color.metro_black_text));
								
								loader = new ProgressDialog(Out_ScanIn.this);
								loader.setMessage(getString(R.string.app_Loading));
								loader.show();
								
								HashMap<String,String> params = new HashMap<String,String>();
								switch(RUN_FROM){
								case 0:
									params.put("card", cardNumberEditText.getText().toString());
									HTTPAsynkTask mt_Profile = new HTTPAsynkTask(Out_ScanIn.this, 
											GlobalConstants.CARD_HAS_PROFILE, 
											//cardNumberEditText.getText().toString(), null, 
											params, Out_ScanIn.this);
									mt_Profile.execute();
									break;
								case 1:
									
					            	params.put("card", cardNumberEditText.getText().toString());
									HTTPAsynkTask mt_Permanent = new HTTPAsynkTask(Out_ScanIn.this, 
											GlobalConstants.POST_PERMANENT_CARD, 
											//null, null, 
											params, Out_ScanIn.this);
									mt_Permanent.execute();		
									break;
								}
																
							}
							
						}	            	
		            });
			      	//Set selector change button alpha
			      	confirmButton.setOnTouchListener(new OnTouchListener() {
			            @Override
			            public boolean onTouch(View v, MotionEvent event) {
			                switch (event.getAction()) {
			                case MotionEvent.ACTION_DOWN:
			                	confirmButton.setAlpha((float) 0.55);
			                    return false;
			                case MotionEvent.ACTION_UP:
			                	confirmButton.setAlpha((float) 1.0);
			                    return false;
			                default:
			                    return false;
			                }
			            }
			      	});


			      	cardNumberEditText.setOnTouchListener(new OnTouchListener() {
						@Override
						public boolean onTouch(View v, MotionEvent event) {
							// TODO Auto-generated method stub
							//editPhone.
							return false;
						}
		            });
		            
		            container.addView(new_view);
	            }

	            return new_view;
	        }

	    }
		 
		 @Override
		 protected void onActivityResult(int requestCode, int resultCode, Intent data){
			 if(requestCode==GlobalConstants.REQUEST_CODE_INET){	
		    	 new Handler().postDelayed(new Runnable() {
		             public void run() {
		              // Actions to do after 1 seconds
		            	 recreate();
		             }
		         }, GlobalConstants.REFRESH_INET_TIME);
		     }
		     if (resultCode == RESULT_OK) 
		     {
		    	 switch(requestCode){
		    	 	case GlobalConstants.REQUEST_CODE_ZBAR:
			    		 // Scan result is available by making a call to data.getStringExtra(ZBarConstants.SCAN_RESULT)
				         // Type of the scan result is available by making a call to data.getStringExtra(ZBarConstants.SCAN_RESULT_TYPE)
				    	 scanInPager.setCurrentItem(1);
				    	 barcodeString = data.getStringExtra(ZBarConstants.SCAN_RESULT);
				    	 bartypeInt = data.getIntExtra(ZBarConstants.SCAN_RESULT_TYPE, 0);
				    	 cardNumberEditText.setText(barcodeString);
			    	break;
			    	 
		    	 	case GlobalConstants.REQUEST_CODE_CONFIRM:
		    	 		//TODO BACK FROM CONFIRM PAGE
		    	 		Intent intent = new Intent();
					    setResult(RESULT_OK, intent);
					    finish();		    	 		
		    	 	break;
		    	 
		    	 }

		         // The value of type indicates one of the symbols listed in Advanced Options below.
		     } else if(resultCode == RESULT_CANCELED) {
		         Toast.makeText(this, "Camera unavailable", Toast.LENGTH_SHORT).show();
		     }
		 }

		@Override
		public void onTaskCompleted(int id) {
			
			if( loader != null && loader.isShowing() )
				loader.dismiss();
			//--- GO TO GET API RESPONSE
			switch(id){
			case GlobalConstants.CARD_HAS_PROFILE:
				Log.v("Tag", "1111 MeApp.regData.getStatus() =" + MeApp.regData.getStatus());
				switch(MeApp.regData.getStatus()){
				case 0:
					//---Card not exist
					startActivity(new Intent(Out_ScanIn.this, Out_AuthConfirm.class));
					break;
				case 1:
					//---Card exist & confirmed
					startActivity(new Intent(Out_ScanIn.this, Out_AuthIn.class));
					break;
				case 2:
					//---Card exist but not confirmed
					startActivity(new Intent(Out_ScanIn.this, Out_AuthCard_0.class)); //Out_AuthCard_2
					break;
				default:
					Toast.makeText(Out_ScanIn.this, 
							getString(R.string.Error_server_not_responding), Toast.LENGTH_LONG).show();
					break;
				}
				
				break;
				
				//TODO ============ERRORS DEPLOYMENT==========
				case GlobalConstants.NO_INTERNET_CONNECTION:
					BottomDialog.BottomSelectDialog(Out_ScanIn.this, 
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
		        	startActivity(new Intent(Out_ScanIn.this, Out_RegInStart.class)); 
					finish();
					break;
				
				case GlobalConstants.ERROR_503:
					BottomDialog.BottomSelectDialog(Out_ScanIn.this, 
							error503, 
			    			getString(R.string.dialog_Server503Title), 
			    			getString(R.string.dialog_Server503Text), 
			    			getString(R.string.dialog_Server503Left), 
			    			getString(R.string.dialog_Server503Right));
				break;
				
				default:
					BottomDialog.BottomSelectDialog(Out_ScanIn.this, 
							error503, 
			    			getString(R.string.dialog_ServerUndefinedTitle), 
			    			getString(R.string.dialog_ServerUndefinedText), 
			    			getString(R.string.dialog_ServerUndefinedLeft), 
			    			getString(R.string.dialog_ServerUndefinedRight));
				break;
			}
			
			
			
			
		}

		@Override
		public void onTaskCompletedJSON(String json, int id) {
			
			if( loader != null && loader.isShowing() )
				loader.dismiss();
			
			switch(id){
				case GlobalConstants.POST_PERMANENT_CARD:
				try {
					JSONObject data = new JSONObject(json);
					if(!data.isNull("success")){
						//SUCCESS Card change
						
						//TODO Create Facebook event
			        	AppEventsLogger logger = AppEventsLogger.newLogger(Out_ScanIn.this);
			        	logger.logEvent("permanent_card_added");
						
						//Allow to use barcode
						SharedPreferences sPref = getSharedPreferences(getString(R.string.app_PREFNAME), MODE_PRIVATE);
			        	sPref.edit().remove(getString(R.string.app_TEMP_CARDEXPIRED)).apply();
			        	
						//Need to re-read profile info data  
						HTTPAsynkTask mt = new HTTPAsynkTask(Out_ScanIn.this, 
								GlobalConstants.GET_PROFILE_INFO,	
								//null, null, 
								null, 
								Out_ScanIn.this);
						mt.execute();					
						
					} else if (!data.isNull("error")){
						//ERROR Card change
						int code = data.optInt("code");
						switch(code){
							case 500:
								// Ошибка при вводе номера карты (должно быть 22 символа)
								startActivityForResult(new Intent(Out_ScanIn.this, Out_AuthConfirm.class).putExtra("RUN_FROM", 1), 
										GlobalConstants.REQUEST_CODE_CONFIRM);
								break;
							case 503:
								//Ошибка проверки checksum карты (ошибка при вводе)
								startActivityForResult(new Intent(Out_ScanIn.this, Out_AuthConfirm.class).putExtra("RUN_FROM", 1), 
										GlobalConstants.REQUEST_CODE_CONFIRM);
								break;
							case 805:
								//Номер телефона владельца карты не совпадает с номером текущего пользователя
								startActivityForResult(new Intent(Out_ScanIn.this, Out_AuthConfirm.class).putExtra("RUN_FROM", 1), 
										GlobalConstants.REQUEST_CODE_CONFIRM);
								break;
							default:
								//Не пойми что
								startActivityForResult(new Intent(Out_ScanIn.this, Out_AuthConfirm.class).putExtra("RUN_FROM", 1), 
										GlobalConstants.REQUEST_CODE_CONFIRM);
								break;
							
						}
						
					}
					
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				break;
				
				case GlobalConstants.GET_PROFILE_INFO:			
					
					try {
						JSONObject data = new JSONObject(json);
						if (data.has("user")){
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
							
							Intent intent = new Intent();
						    setResult(RESULT_OK, intent);
						    finish();
							
						} else if (data.has("error")){
							Toast.makeText(Out_ScanIn.this, getString(R.string.Error_server_not_responding), 
									Toast.LENGTH_LONG).show();
						} else {
							Toast.makeText(Out_ScanIn.this, getString(R.string.Error_server_not_responding), 
									Toast.LENGTH_LONG).show();
						}
						
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Log.v("TAG", "PROFILE JSONException e=" + e);
					}
					
					break;
					
					//TODO ============ERRORS DEPLOYMENT==========
					case GlobalConstants.NO_INTERNET_CONNECTION:
						BottomDialog.BottomSelectDialog(Out_ScanIn.this, 
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
			        	startActivity(new Intent(Out_ScanIn.this, Out_RegInStart.class)); 
						finish();
						break;
					
					case GlobalConstants.ERROR_503:
						BottomDialog.BottomSelectDialog(Out_ScanIn.this, 
								error503, 
				    			getString(R.string.dialog_Server503Title), 
				    			getString(R.string.dialog_Server503Text), 
				    			getString(R.string.dialog_Server503Left), 
				    			getString(R.string.dialog_Server503Right));
					break;
					
					default:
						BottomDialog.BottomSelectDialog(Out_ScanIn.this, 
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
		            	Out_ScanIn.this.finishAffinity();
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
		            	Out_ScanIn.this.finishAffinity();
		            } else if (msg.what == GlobalConstants.retCodeClose) {

		            }
		        }
		    }
		};

}