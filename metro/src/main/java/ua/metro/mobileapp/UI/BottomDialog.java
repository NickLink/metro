package ua.metro.mobileapp.UI;

import ua.metro.mobileapp.FontCache;
import ua.metro.mobileapp.GlobalConstants;
import ua.metro.mobileapp.Inner_ProfileAppRules;
import ua.metro.mobileapp.Inner_ProfileFullScreenCode;
import ua.metro.mobileapp.R;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class BottomDialog {
	
	static Typeface fontRegular;
	static Typeface fontMedium;
	public static Dialog bottomDialog, preloaderDialog, expiredDialog;
	private static Handler mResponseHandler;
	
	
	public static void BottomMessageDialog(Context mContext, Handler reponseHandler, String title, String message){
		
		 	bottomDialog = new Dialog(mContext);
		 	mResponseHandler = reponseHandler;
		 	fontRegular = FontCache.get("fonts/clearsansregular.ttf", mContext);
	        fontMedium = FontCache.get("fonts/clearsansmedium.ttf", mContext);
	        bottomDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
	        bottomDialog.setContentView(R.layout.dialog_message);
	        bottomDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
	        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
	        bottomDialog.getWindow().setBackgroundDrawable
	        	(new ColorDrawable(mContext.getResources().getColor(R.color.metro_transparent_black)));
	        		//Color.metro_transparent_black)); TRANSPARENT
	        //bottomDialog.setCancelable(false);
	        
	        TextView titleTextView = (TextView) bottomDialog.findViewById(R.id.subTitleTextView);
	        TextView messageTextView = (TextView) bottomDialog.findViewById(R.id.messageTextView);
	        
	        titleTextView.setTypeface(fontMedium);
	        messageTextView.setTypeface(fontRegular);
	        
	        titleTextView.setText(title);
	        messageTextView.setText(message);
	        
	        ImageButton iconCloseImageButton = (ImageButton) bottomDialog.findViewById(R.id.iconCloseImageButton);
	        iconCloseImageButton.setOnClickListener(new OnClickListener() {
	            public void onClick(View v) {
	            	 bottomDialog.dismiss();
	            	 mResponseHandler.sendEmptyMessage(GlobalConstants.retCodeClose);
	            }
	        });
	        
	        bottomDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {         
	            @Override
	            public void onCancel(DialogInterface dialog) {
	            	bottomDialog.dismiss();
	            	mResponseHandler.sendEmptyMessage(GlobalConstants.retCodeClose);
	            }
	        });
	        
	        bottomDialog.show();	        
	}
	
	public static void BottomMessageDialogExpired(Context mContext, String title, String message){
		
	 	bottomDialog = new Dialog(mContext);
	 	fontRegular = FontCache.get("fonts/clearsansregular.ttf", mContext);
        fontMedium = FontCache.get("fonts/clearsansmedium.ttf", mContext);
        bottomDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        bottomDialog.setContentView(R.layout.dialog_message_ex);
        bottomDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomDialog.getWindow().setBackgroundDrawable
        	(new ColorDrawable(mContext.getResources().getColor(R.color.metro_transparent_black)));
        		//Color.metro_transparent_black)); TRANSPARENT
        //bottomDialog.setCancelable(false);
        
        TextView titleTextView = (TextView) bottomDialog.findViewById(R.id.subTitleTextView);
        TextView messageTextView = (TextView) bottomDialog.findViewById(R.id.messageTextView);
        
        titleTextView.setTypeface(fontMedium);
        messageTextView.setTypeface(fontRegular);
        
        titleTextView.setText(title);
        messageTextView.setText(message);
        
        ImageButton iconCloseImageButton = (ImageButton) bottomDialog.findViewById(R.id.iconCloseImageButton);
        iconCloseImageButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	 bottomDialog.dismiss();
            }
        });
        
        bottomDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {         
            @Override
            public void onCancel(DialogInterface dialog) {
            	bottomDialog.dismiss();
            }
        });
        
        bottomDialog.show();	        
}
	
	public static void BottomSelectDialog(final Context mContext, Handler reponseHandler, String title, String message, 
			String leftButton, String rightButton){
	 	bottomDialog = new Dialog(mContext);
	 	mResponseHandler = reponseHandler;
	 	fontRegular = FontCache.get("fonts/clearsansregular.ttf", mContext);
        fontMedium = FontCache.get("fonts/clearsansmedium.ttf", mContext);
        
        bottomDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        bottomDialog.setContentView(R.layout.dialog_select);
        bottomDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomDialog.getWindow().setBackgroundDrawable
        	(new ColorDrawable(mContext.getResources().getColor(R.color.metro_transparent_black)));
        		//Color.metro_transparent_black)); TRANSPARENT
        bottomDialog.setCancelable(false);
        
        TextView titleTextView = (TextView) bottomDialog.findViewById(R.id.subTitleTextView);
        TextView messageTextView = (TextView) bottomDialog.findViewById(R.id.messageTextView);
//        TextView show_bar_code = (TextView) bottomDialog.findViewById(R.id.show_bar_code);
        
        
        titleTextView.setTypeface(fontMedium);
        messageTextView.setTypeface(fontRegular);
//        show_bar_code.setTypeface(fontRegular);
//        show_bar_code.setPaintFlags(show_bar_code.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
//        show_bar_code.setVisibility(View.GONE);
        
        titleTextView.setText(title);
        if(message!=null){
        	messageTextView.setText(message);
        } else {
        	messageTextView.setVisibility(View.GONE);
        }
        //Buttons set
        Button buttonLeft = (Button)bottomDialog.findViewById(R.id.buttonLeft);
        Button buttonRight = (Button)bottomDialog.findViewById(R.id.buttonRight);      
        buttonLeft.setTypeface(fontMedium);
        buttonRight.setTypeface(fontMedium);
        buttonLeft.setTextColor(mContext.getResources().getColor(R.color.metro_green));
        buttonRight.setTextColor(mContext.getResources().getColor(R.color.metro_red));
        buttonLeft.setText(leftButton);
        buttonRight.setText(rightButton);
        
        buttonLeft.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mResponseHandler.sendEmptyMessage(GlobalConstants.retCodeLeft);
				bottomDialog.dismiss();
			}     	
        });
        
        buttonRight.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mResponseHandler.sendEmptyMessage(GlobalConstants.retCodeRight);
				bottomDialog.dismiss();
			}     	
        });
            
        ImageButton iconCloseImageButton = (ImageButton) bottomDialog.findViewById(R.id.iconCloseImageButton);
        iconCloseImageButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	mResponseHandler.sendEmptyMessage(GlobalConstants.retCodeClose);
            	bottomDialog.dismiss();   	 
            }
        });
        
        bottomDialog.setOnDismissListener(new OnDismissListener(){
			@Override
			public void onDismiss(DialogInterface dialog) {
				mResponseHandler.sendEmptyMessage(GlobalConstants.retCodeClose);
            	//bottomDialog.dismiss();
			}     	
        });
        
//        show_bar_code.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//            	Intent intent = new Intent(mContext, Inner_ProfileFullScreenCode.class);
//            	mContext.startActivity(intent); 	 
//            }
//        });
        
        bottomDialog.show(); 
	}
	
	public static void BottomSelectDialogCode(final Context mContext, Handler reponseHandler, String title, String message, 
			String leftButton, String rightButton){
	 	bottomDialog = new Dialog(mContext);
	 	mResponseHandler = reponseHandler;
	 	fontRegular = FontCache.get("fonts/clearsansregular.ttf", mContext);
        fontMedium = FontCache.get("fonts/clearsansmedium.ttf", mContext);
        
        bottomDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        bottomDialog.setContentView(R.layout.dialog_select_code);
        bottomDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomDialog.getWindow().setBackgroundDrawable
        	(new ColorDrawable(mContext.getResources().getColor(R.color.metro_transparent_black)));
        		//Color.metro_transparent_black)); TRANSPARENT
        bottomDialog.setCancelable(false);
        
        TextView titleTextView = (TextView) bottomDialog.findViewById(R.id.subTitleTextView);
        TextView messageTextView = (TextView) bottomDialog.findViewById(R.id.messageTextView);
//        TextView show_bar_code = (TextView) bottomDialog.findViewById(R.id.show_bar_code);
        
        
        titleTextView.setTypeface(fontMedium);
        messageTextView.setTypeface(fontRegular);
//        show_bar_code.setTypeface(fontRegular);
//        show_bar_code.setPaintFlags(show_bar_code.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
//        show_bar_code.setVisibility(View.GONE);
        
        titleTextView.setText(title);
        if(message!=null){
        	messageTextView.setText(message);
        } else {
        	messageTextView.setVisibility(View.GONE);
        }
        //Buttons set
        Button buttonLeft = (Button)bottomDialog.findViewById(R.id.buttonLeft);
        Button buttonRight = (Button)bottomDialog.findViewById(R.id.buttonRight);      
        buttonLeft.setTypeface(fontMedium);
        buttonRight.setTypeface(fontMedium);
        buttonLeft.setTextColor(mContext.getResources().getColor(R.color.metro_navy));
        buttonRight.setTextColor(mContext.getResources().getColor(R.color.metro_green));
        buttonLeft.setText(leftButton);
        buttonRight.setText(rightButton);
        
        buttonLeft.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Intent intent = new Intent(mContext, Inner_ProfileFullScreenCode.class);
//				mContext.startActivity(intent); 
            	
				mResponseHandler.sendEmptyMessage(GlobalConstants.retCodeLeft);
				//bottomDialog.dismiss();
			}     	
        });
        
        buttonRight.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mResponseHandler.sendEmptyMessage(GlobalConstants.retCodeRight);
				bottomDialog.dismiss();
			}     	
        });
        
        bottomDialog.setOnDismissListener(new OnDismissListener(){
			@Override
			public void onDismiss(DialogInterface dialog) {
				mResponseHandler.sendEmptyMessage(GlobalConstants.retCodeClose);
            	//bottomDialog.dismiss();
			}     	
        });
        
//        show_bar_code.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//            	Intent intent = new Intent(mContext, Inner_ProfileFullScreenCode.class);
//            	mContext.startActivity(intent); 	 
//            }
//        });
        
        bottomDialog.show(); 
	}
	
	public static void BottomMapDialog(Context mContext, Handler reponseHandler, String title, String shopName, 
			String shopAddress){
	 	bottomDialog = new Dialog(mContext);
	 	mResponseHandler = reponseHandler;
	 	fontRegular = FontCache.get("fonts/clearsansregular.ttf", mContext);
        fontMedium = FontCache.get("fonts/clearsansmedium.ttf", mContext);
        
        bottomDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        bottomDialog.setContentView(R.layout.dialog_map);
        bottomDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomDialog.getWindow().setBackgroundDrawable
        	(new ColorDrawable(mContext.getResources().getColor(R.color.metro_transparent_black)));
        		//Color.metro_transparent_black)); TRANSPARENT
        bottomDialog.setCancelable(false);
        
        TextView titleTextView = (TextView) bottomDialog.findViewById(R.id.subTitleTextView);
        TextView titleShop = (TextView) bottomDialog.findViewById(R.id.titleShop);
        TextView addressShop = (TextView) bottomDialog.findViewById(R.id.addressShop);
        
        titleTextView.setTypeface(fontMedium);
        titleShop.setTypeface(fontMedium);
        addressShop.setTypeface(fontMedium);
        
        if(title!=null){
        	titleTextView.setText(title);
        } else {
        	titleTextView.setVisibility(View.GONE);
        }
        if(shopName!=null){
        	titleShop.setText(shopName);
        } else {
        	titleShop.setVisibility(View.GONE);
        }
        if(shopAddress!=null){
        	addressShop.setText(shopAddress);
        } else {
        	addressShop.setVisibility(View.GONE);
        }
        
        //Button set
        Button traceButton = (Button)bottomDialog.findViewById(R.id.traceButton);
        traceButton.setTypeface(fontMedium);
        
        traceButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mResponseHandler.sendEmptyMessage(GlobalConstants.retCodeLeft);
				bottomDialog.dismiss();
			}     	
        });       
            
        ImageButton iconCloseImageButton = (ImageButton) bottomDialog.findViewById(R.id.iconCloseImageButton);
        iconCloseImageButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	mResponseHandler.sendEmptyMessage(GlobalConstants.retCodeClose);
            	bottomDialog.dismiss();   	 
            }
        });
        
        bottomDialog.setOnDismissListener(new OnDismissListener(){
			@Override
			public void onDismiss(DialogInterface dialog) {
				mResponseHandler.sendEmptyMessage(GlobalConstants.retCodeClose);
            	//bottomDialog.dismiss();
			}     	
        });
        
        bottomDialog.show(); 
	}
	
	public static void BottomExpiredDialog(final Context mContext, Handler reponseHandler){
	 	expiredDialog = new Dialog(mContext);
	 	mResponseHandler = reponseHandler;
	 	fontRegular = FontCache.get("fonts/clearsansregular.ttf", mContext);
        fontMedium = FontCache.get("fonts/clearsansmedium.ttf", mContext);
        
        expiredDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        expiredDialog.setContentView(R.layout.dialog_expired);
        expiredDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        expiredDialog.getWindow().setGravity(Gravity.BOTTOM);
        expiredDialog.getWindow().setBackgroundDrawable
        	(new ColorDrawable(mContext.getResources().getColor(R.color.metro_transparent_black)));
        		//Color.metro_transparent_black)); TRANSPARENT
        expiredDialog.setCancelable(true);
        
        TextView titleTextView = (TextView) expiredDialog.findViewById(R.id.subTitleTextView);
        TextView textMessage = (TextView) expiredDialog.findViewById(R.id.textMessage);
        TextView buttonTextView = (TextView) expiredDialog.findViewById(R.id.buttonTextView);
        
        titleTextView.setTypeface(fontMedium);
        textMessage.setTypeface(fontRegular);
        
        //Button set
        Button expiredButton = (Button)expiredDialog.findViewById(R.id.expiredButton);
        expiredButton.setTypeface(fontMedium);
        
        expiredButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mResponseHandler.sendEmptyMessage(GlobalConstants.retCodeLeft);
				expiredDialog.dismiss();
			}     	
        });       
        
        expiredDialog.setOnDismissListener(new OnDismissListener(){
			@Override
			public void onDismiss(DialogInterface dialog) {
				mResponseHandler.sendEmptyMessage(GlobalConstants.retCodeClose);
				//expiredDialog.dismiss();
			}     	
        });
        
        buttonTextView.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mContext.startActivity(new Intent(mContext, Inner_ProfileAppRules.class).putExtra("page", 1));
			}     	
        });
        
        expiredDialog.show(); 
	}
	
	public static void BottomSelectTextDialog(final Context mContext, Handler reponseHandler, String title, String message, 
			String leftButton, String rightButton){
	 	bottomDialog = new Dialog(mContext);
	 	mResponseHandler = reponseHandler;
	 	fontRegular = FontCache.get("fonts/clearsansregular.ttf", mContext);
        fontMedium = FontCache.get("fonts/clearsansmedium.ttf", mContext);
        
        bottomDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        bottomDialog.setContentView(R.layout.dialog_select_text);
        bottomDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomDialog.getWindow().setBackgroundDrawable
        	(new ColorDrawable(mContext.getResources().getColor(R.color.metro_transparent_black)));
        		//Color.metro_transparent_black)); TRANSPARENT
        bottomDialog.setCancelable(false);
        
        TextView titleTextView = (TextView) bottomDialog.findViewById(R.id.subTitleTextView);
        TextView messageTextView = (TextView) bottomDialog.findViewById(R.id.messageTextView);
        TextView buttonTextView = (TextView) bottomDialog.findViewById(R.id.buttonTextView);
        
        titleTextView.setTypeface(fontMedium);
        messageTextView.setTypeface(fontRegular);
        buttonTextView.setTypeface(fontRegular);
        
        titleTextView.setText(title);
        buttonTextView.setText(mContext.getResources().getString(R.string.dialog_TempCardButton));
        buttonTextView.setPaintFlags(buttonTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        buttonTextView.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mContext.startActivity(new Intent(mContext, Inner_ProfileAppRules.class).putExtra("page", 1));
			}     	
        });
        if(message!=null){
        	messageTextView.setText(message);
        } else {
        	messageTextView.setVisibility(View.GONE);
        }
        //Buttons set
        Button buttonLeft = (Button)bottomDialog.findViewById(R.id.buttonLeft);
        Button buttonRight = (Button)bottomDialog.findViewById(R.id.buttonRight);      
        buttonLeft.setTypeface(fontMedium);
        buttonRight.setTypeface(fontMedium);
        buttonLeft.setTextColor(mContext.getResources().getColor(R.color.metro_green));
        buttonRight.setTextColor(mContext.getResources().getColor(R.color.metro_red));
        buttonLeft.setText(leftButton);
        buttonRight.setText(rightButton);
        
        buttonLeft.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mResponseHandler.sendEmptyMessage(GlobalConstants.retCodeLeft);
				bottomDialog.dismiss();
			}     	
        });
        
        buttonRight.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mResponseHandler.sendEmptyMessage(GlobalConstants.retCodeRight);
				bottomDialog.dismiss();
			}     	
        });
            
        ImageButton iconCloseImageButton = (ImageButton) bottomDialog.findViewById(R.id.iconCloseImageButton);
        iconCloseImageButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	mResponseHandler.sendEmptyMessage(GlobalConstants.retCodeClose);
            	bottomDialog.dismiss();   	 
            }
        });
        
        bottomDialog.setOnDismissListener(new OnDismissListener(){
			@Override
			public void onDismiss(DialogInterface dialog) {
				mResponseHandler.sendEmptyMessage(GlobalConstants.retCodeClose);
            	//bottomDialog.dismiss();  
			}     	
        });
        
        bottomDialog.show(); 
	}

}
