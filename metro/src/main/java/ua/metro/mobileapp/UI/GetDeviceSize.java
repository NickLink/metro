package ua.metro.mobileapp.UI;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

public class GetDeviceSize {
	Point size;
	
	public GetDeviceSize(Context context){
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		size = new Point();
		display.getSize(size);
	}
	public int getX(){
		return size.x;
	}
	public int getY(){
		return size.y;
	}
}
