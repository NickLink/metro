package ua.metro.mobileapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ua.metro.mobileapp.adapters.FilterExpListAdapter;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MenuFilter{ 	
	
	static Typeface fontRegular;

	static Typeface fontMedium;

	static Typeface fontLight;
	 
	 public static Dialog menuDialog;
	 
	 static int idCatalog;

	 public static void showMenu(final Context mContext, String jsonData, int deviceWidth){
		 
	        fontRegular = FontCache.get("fonts/clearsansregular.ttf", mContext);
	        fontMedium = FontCache.get("fonts/clearsansmedium.ttf", mContext);	
	        //fontLight = FontCache.get("fonts/clearsanslight.ttf", mContext);
				
	        // коллекция для групп
	        final ArrayList<Map<String, String>> groupData;
	        
	        // коллекция для элементов одной группы
	     	//ArrayList<Map<String, String>> childDataItem = null;	     	
	     	final HashMap<Integer, ArrayList<Map<String, String>>> childDataItems;
	        
	        // заполняем коллекцию групп из массива с названиями групп
			groupData = new ArrayList<Map<String, String>>();
			
			// list attr Group or category
			HashMap<String, String> m;
			
			// создаем коллекцию для коллекций элементов  
			//childData = new ArrayList<ArrayList<Map<String, String>>>(); 
			
			childDataItems = new HashMap<Integer,ArrayList<Map<String, String>>>();
			
			try {
				
				JSONObject object = new JSONObject(jsonData);
				
				JSONObject catalog = object.getJSONObject("catalog");
				 //Log.v(LOG_TAG," ЕЕЕЕ CatalogGdsLisJsonParser catalog = "+catalog);
				 
				if(catalog.optInt("id")>0)
					idCatalog = catalog.optInt("id");
				
				JSONArray categories = object.getJSONArray("categories");
	    		
	  			for (int i=0;i<categories.length();i++) {
	  				JSONObject item = categories.getJSONObject(i);
	  				
	  				m = new HashMap<String, String>();
	  				//Groups
	  				if(item.optInt("parent_id")==0){		  				
		  				if(item.optInt("id")>0)
		  					m.put("idGroup",item.optString("id")); 
		  				if(item.optString("title").length()>0)
		  					m.put("titleGroup",item.optString("title")); 

		  				groupData.add(m); 
	  				
		  			//Categories
	  				}else{

	  					if(item.optInt("id")>0)
		  					m.put("idCategory",item.optString("id")); 
		  				if(item.optString("title").length()>0)
		  					m.put("titleCategory",item.optString("title")); 
		  				
		  				ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();	
	  					
		  				if (childDataItems.containsKey(item.optInt("parent_id"))){
	  						list = childDataItems.get(item.optInt("parent_id"));
	  					}
	  					
	 					list.add(m);
	 	  				childDataItems.put(item.optInt("parent_id"), list);
	  				}	  				
	  			}
	  			
	  			
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		
	        final FilterExpListAdapter adapter = new FilterExpListAdapter(mContext, idCatalog, groupData, childDataItems);
	        
	        menuDialog = new Dialog(mContext);
	        menuDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
	        menuDialog.setContentView(R.layout.filter_list);
	        
	        TextView bigTitleTextView = (TextView) menuDialog.findViewById(R.id.bigTitleTextView);
	        TextView smallTiltleTextView = (TextView) menuDialog.findViewById(R.id.smallTiltleTextView);
	        
	        bigTitleTextView.setTypeface(fontMedium);
	        smallTiltleTextView.setTypeface(fontRegular);
	        
	        final ExpandableListView menuList = (ExpandableListView) menuDialog.findViewById(R.id.filterExpandableListView);
	        menuList.setGroupIndicator(new ColorDrawable(Color.TRANSPARENT));
	        menuList.setDividerHeight(0);
	        menuList.setPadding(0, 0, 0, 0);	        
	        menuList.setAdapter(adapter);
	        
	        menuList.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {	        	 
	            @Override
	            public void onGroupExpand(int groupPosition) {
	                //Toast.makeText(mContext,
	                //		Integer.parseInt(groupData.get(groupPosition).get("idGroup")) + " Список раскрыт.",
	                //        Toast.LENGTH_SHORT).show();
	                if(adapter.getChildrenCount(groupPosition)==0){
		            	Intent intent = new Intent (mContext,Inner_CatalogGds.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
						intent.putExtra("idCatalog", idCatalog);
						intent.putExtra("idCategory", Integer.parseInt(groupData.get(groupPosition).get("idGroup"))); 
						intent.putExtra("page",1);
					   
					    mContext.startActivity(intent);
		       
		            	MenuFilter.menuDialog.dismiss();
	                }
	            }
	        });
	 
	        menuList.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
	 
	            @Override
	            public void onGroupCollapse(int groupPosition) {
	                //Toast.makeText(mContext,
	                //		groupData.get(groupPosition) + " Список скрыт.",
	                //        Toast.LENGTH_SHORT).show();
	 
	            }
	        });
	        
	        menuList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
	            @Override
	            public boolean onChildClick(ExpandableListView parent, View v,
	                                        int groupPosition, int childPosition, long id) {
	                //Toast.makeText(mContext,
	                //		groupData.get(groupPosition).get("idGroup")
	                //                + " : " + childDataItems.get(adapter.getParentId(groupPosition))
	                //                .get(childPosition), Toast.LENGTH_SHORT).show();
	                
	                
	                Intent intent = new Intent (mContext,Inner_CatalogGds.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
					intent.putExtra("idCatalog", idCatalog);
					intent.putExtra("idCategory", Integer.parseInt(childDataItems.get(adapter.getParentId(groupPosition)).get(childPosition).get("idCategory")));
					intent.putExtra("page",1);
				   
				    mContext.startActivity(intent);
	       
	            	MenuFilter.menuDialog.dismiss();
	            	return false;
	            }
	        });
	        
	        menuDialog.getWindow().setLayout((int) (deviceWidth*0.8), WindowManager.LayoutParams.WRAP_CONTENT);
	        menuDialog.getWindow().setGravity(Gravity.TOP | Gravity.RIGHT);
	        menuDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
	        menuDialog.show();    
	        
	        ImageButton menuButton = (ImageButton) menuDialog.findViewById(R.id.filterButton);
	        menuButton.setOnClickListener(new OnClickListener() {
	            public void onClick(View v) {
	            	 menuDialog.dismiss();
	            }
	        });
	    }

}
