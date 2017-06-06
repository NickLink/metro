package ua.metro.mobileapp.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ua.metro.mobileapp.FontCache;
import ua.metro.mobileapp.R;

public class FilterExpListAdapter extends BaseExpandableListAdapter {

	        private ArrayList<Map<String, String>> mGroups;
	        private HashMap<Integer,ArrayList<Map<String, String>>> mGhildren;

	        private Context mContext;
	        
	        Typeface fontMedium;
	        int idCatalog;
	        
	        public FilterExpListAdapter (Context context, 
	        		int idCatalog,
	        		ArrayList<Map<String, String>> groups,
	        		HashMap<Integer, ArrayList<Map<String, String>>> childData){
	            
	        	mContext = context;
	            mGroups = groups;
	            mGhildren = childData;
	            this.idCatalog = idCatalog;
	            
	            fontMedium = FontCache.get("fonts/clearsansmedium.ttf", mContext);	
	        }
	        
	        @Override
	        public int getGroupCount() {
	            return mGroups.size();
	        }

	        @Override
	        public int getChildrenCount(int groupPosition) {
	         	if ( mGhildren.containsKey(getParentId(groupPosition))){
	        		return mGhildren.get(getParentId(groupPosition)).size();
	        	}
	        	else return 0;
	        }

	        @Override
	        public Object getGroup(int groupPosition) {
	            return mGroups.get(groupPosition);
	        }
	        
	        public int getParentId(int groupPosition) {
	        	if (mGhildren.containsKey(Integer.parseInt(mGroups.get(groupPosition).get("idGroup"))))
	        		return Integer.parseInt(mGroups.get(groupPosition).get("idGroup"));
	        	else
	        		return 0;
	        }

	        @Override
	        public Object getChild(int groupPosition, int childPosition) {
	          	return mGhildren.get(getParentId(groupPosition)).get(childPosition);
	        }

	        @Override
	        public long getGroupId(int groupPosition) {
	            return groupPosition;
	        }

	        @Override
	        public long getChildId(int groupPosition, int childPosition) {
	            return childPosition;
	        }

	        @Override
	        public boolean hasStableIds() {
	            return true;
	        }

	        @Override
	        public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
	        	
	            if (convertView == null) {
	                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	                convertView = inflater.inflate(R.layout.filter_group, null);               
	            }
	            
	            TextView textGroup = (TextView) convertView.findViewById(R.id.titleGroup);
	            textGroup.setTypeface(fontMedium);
		            
	            textGroup.setText(mGroups.get(groupPosition).get("titleGroup"));//+" "+count);
	            
	            ImageView imageGroup = (ImageView) convertView.findViewById(R.id.imageGroup);
	           
	            if(getChildrenCount(groupPosition)==0) 
	            	imageGroup.setVisibility(View.GONE);
	            else
	            	imageGroup.setVisibility(View.VISIBLE);
	            
	            if (isExpanded){
	                //Изменяем что-нибудь, если текущая Group раскрыта
	            	imageGroup.setImageResource(R.drawable.open);
	            }else{
	                 //Изменяем что-нибудь, если текущая Group скрыта
	            	imageGroup.setImageResource(R.drawable.close);
		        }
	            return convertView;
	        }
	        
	        @Override
	        public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild,
	                                 View convertView, ViewGroup parent) {
	            if (convertView == null) {
	                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	                convertView = inflater.inflate(R.layout.filter_child, null);
	            }

	            TextView textChild = (TextView) convertView.findViewById(R.id.titleSubSection);
	            textChild.setTypeface(fontMedium);
	   
	            textChild.setText(mGhildren.get(getParentId(groupPosition)).get(childPosition).get("titleCategory"));
	
	            return convertView;
	        }

	        @Override
	        public boolean isChildSelectable(int groupPosition, int childPosition) {
	            return true;
	        }
	    }