package ua.metro.mobileapp.adapters;

import java.util.HashMap;

import ua.metro.mobileapp.FontCache;
import ua.metro.mobileapp.GlobalConstants;
import ua.metro.mobileapp.OnTaskCompleted;
import ua.metro.mobileapp.R;
import ua.metro.mobileapp.HttpMetods.HTTPAsynkTask;
import ua.metro.mobileapp.UI.GetDeviceSize;
import ua.metro.mobileapp.application.MeApp;
import ua.metro.mobileapp.datamodel.GdsData;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

public class GdsListAdapter extends BaseAdapter{
	Typeface fontRegular, fontMedium, fontLight;
	
	String LOG_TAG = this.getClass().getName()+"LOG";
	LayoutInflater inflator;
	HashMap<Integer,GdsData> dataGds;
	Context mContext;
	OnTaskCompleted listener;
	int widthDevice, heightDevice;
	
	public GdsListAdapter(Context mContext, HashMap<Integer, GdsData> data) {
		this.dataGds = data;
		this.mContext = mContext;
		this.listener = (OnTaskCompleted) mContext;
		fontRegular = FontCache.get("fonts/clearsansregular.ttf", mContext);
		fontMedium = FontCache.get("fonts/clearsansmedium.ttf", mContext);
		fontLight = FontCache.get("fonts/clearsanslight.ttf", mContext);
		
		GetDeviceSize a = new GetDeviceSize(mContext);
		widthDevice = a.getX();
		heightDevice = a.getY();
		Log.v("TAG", "CXC GdsListAdapter widthDevice=" + widthDevice) ;
	}			 
	 
	public class ViewHolder{
		 public ImageView gdsImage;
		 public TextView gdsTitle;
		 public TextView gdsDiscount;
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
		 
		  if (convertView == null) {
			  holder = new ViewHolder();
			  
			  LayoutInflater inflater = LayoutInflater.from(parent.getContext());
			  
			  view =	inflater.inflate(R.layout.cell_gds, parent, false);

			  holder.gdsImage = (ImageView) view.findViewById(R.id.gdsImage);
			  holder.gdsTitle = (TextView) view.findViewById(R.id.gdsTitle);
			  holder.gdsDiscount = (TextView) view.findViewById(R.id.gdsDiscount);
			  holder.gdsPriceOldUah = (TextView) view.findViewById(R.id.gdsPriceOldUah);
			  holder.gdsPriceOldCoins = (TextView) view.findViewById(R.id.gdsPriceOldCoins);
			  holder.gdsPriceUah = (TextView) view.findViewById(R.id.gdsPriceUah);
			  holder.gdsPriceCoins = (TextView) view.findViewById(R.id.gdsPriceCoins);
			  holder.gdsPriceCurrency = (TextView) view.findViewById(R.id.gdsPriceCurrency);
			  holder.gdsAddButton = (Button) view.findViewById(R.id.gdsAddButton);
			
				  
			  //if (!dataGds.get(position).getGdsIsHitPrice())
				//  holder.gdsHitPrice.setVisibility(View.GONE);
				
			  holder.gdsImage.getLayoutParams().height = (int) widthDevice/2;
			  
			 /* LinearLayout.LayoutParams paramsGdsImage = (LinearLayout.LayoutParams) holder.gdsImage.getLayoutParams();
			  paramsGdsImage.width = (int) widthDevice/2;
			  Log.v("TAG", "CXC GdsListAdapter holder.gdsImage.getWidth()=" + paramsGdsImage.width);
			  paramsGdsImage.height = paramsGdsImage.width;
			  holder.gdsImage.setLayoutParams(paramsGdsImage);
			  */
		  
			  holder.gdsTitle.setTypeface(fontRegular);
			  holder.gdsDiscount.setTypeface(fontMedium);
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
				Log.v("LOG_TAG"," AAAA Inner_Main dataGdsItem.getCatalogId() = " + dataGds.get(position).getCatalogId());
				Log.v("LOG_TAG"," AAAA Inner_Main dataGdsItem.getGdsId() = "+ dataGds.get(position).getGdsId());
				
				if (MeApp.regData.getUser().getCount_basket() >= 99){
					//TODO Dialog for too much items
					
					
				} else {
					
					HashMap<String,String> params = new HashMap<String,String>();
			    	params.put("catalog_id", String.valueOf(dataGds.get(position).getCatalogId()));
			    	params.put("product_id", String.valueOf(dataGds.get(position).getGdsId()));
			    		        
			        HTTPAsynkTask mt = new HTTPAsynkTask(mContext, 
							GlobalConstants.CART_ADD, 
							//null, null, 
							params, 
							listener);
					mt.execute();
					
				}	
				  
	          }
	      });
		  
		  holder.gdsTitle.setText(dataGds.get(position).getGdsTitle());
		  
		  holder.gdsDiscount.setText(dataGds.get(position).getDiscountTitle_(parent.getContext()));
		  
		  if(dataGds.get(position).getDiscountType()!=null && 
				  dataGds.get(position).getDiscountType().length()>0 &&
				  dataGds.get(position).getDiscountImgUrl(parent.getContext())>0){
			  holder.gdsDiscount.setBackgroundResource(dataGds.get(position).getDiscountImgUrl(parent.getContext()));
		  }
		  
		  holder.gdsPriceUah.setText(dataGds.get(position).getGdsPriceUah());
		  holder.gdsPriceCoins.setText(dataGds.get(position).getGdsPriceCoins());
		 
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
		            //Log.v("TAG", "CXC GdsListAdapter int targetWidthBEFORE=" + targetWidth);
		            //if (targetWidth != (int) widthDevice/2) 
		            int	targetWidth = (int) widthDevice/2;
		            Log.v("TAG", "CXC GdsListAdapter int targetWidthAFTER=" + (int) widthDevice/2);
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
		        .into(holder.gdsImage);
		    
		  
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