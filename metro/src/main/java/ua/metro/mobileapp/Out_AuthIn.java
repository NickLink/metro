package ua.metro.mobileapp;

import ua.metro.mobileapp.UI.OnSingleClickListener;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class Out_AuthIn  extends Activity {
	
	Typeface fontRegular, fontMedium;
	Button cominButton, questionButton;
	ImageButton iconBackImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.out_auth_in);
        
		// BackButton
		iconBackImageButton = (ImageButton) findViewById(R.id.iconBackImageButton);
		iconBackImageButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Back Button
				finish();
			}
		});
        
        fontRegular = FontCache.get("fonts/clearsansregular.ttf", getBaseContext());
        fontMedium = FontCache.get("fonts/clearsansmedium.ttf", getBaseContext());
        
        TextView subTitleTextView = (TextView) findViewById(R.id.subTitleTextView);
        
        TextView cominTextView = (TextView) findViewById(R.id.cominTextView);
        TextView quetionTextView = (TextView) findViewById(R.id.quetionTextView);
        cominButton = (Button) findViewById(R.id.cominButton);
        questionButton = (Button) findViewById(R.id.quetionButton);

        subTitleTextView.setTypeface(fontMedium);
        cominTextView.setTypeface(fontRegular);
        quetionTextView.setTypeface(fontRegular);
        cominButton.setTypeface(fontMedium);
        questionButton.setTypeface(fontMedium);
        
        cominButton.setOnClickListener(new OnSingleClickListener(){
			@Override
			public void onSingleClick(View v) {
				// TODO Go to login page 2
				startActivity(new Intent(Out_AuthIn.this, Out_RegInStart.class)
				.putExtra("page", 1));
				finish();
			}        	
        });
      	//Set selector change button alpha
        cominButton.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                	cominButton.setAlpha((float) 0.55);
                    return false;
                case MotionEvent.ACTION_UP:
                	cominButton.setAlpha((float) 1.0);
                    return false;
                default:
                    return false;
                }
            }
      	});
        
        questionButton.setOnClickListener(new OnSingleClickListener(){
			@Override
			public void onSingleClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(Out_AuthIn.this, Out_AuthQuestion.class));
			}        	
        });
      	//Set selector change button alpha
        questionButton.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                	questionButton.setAlpha((float) 0.55);
                    return false;
                case MotionEvent.ACTION_UP:
                	questionButton.setAlpha((float) 1.0);
                    return false;
                default:
                    return false;
                }
            }
      	});
    }
}