package com.example.slidinglayout3d;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import sz.lamp.interface1.IScrollEvent;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ValidFragment")
public class Groups extends Fragment {  
	private static int CONFIGURATION_RESULT_CODE=1;
	private ListView  groupsList;
	
	private View mHereView;
	IScrollEvent mI;
	private static Boolean isRVGroupOpened = false;
	private RelativeLayout RVGroup;
	List<Map<String, String>> list ; 
	private Context mContext;
	

	public Groups(MainActivity mainActivity) {
			mI=mainActivity;
	}

	@Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {  
		mHereView = inflater.inflate(R.layout.groups, container, false); 
        
        RVGroup = (RelativeLayout) mHereView.findViewById(R.id.RVGroup);
        groupsList = (ListView) mHereView.findViewById(R.id.lampsList);
        
        mContext= inflater.getContext();
        loadGroups();
        mI.setScrollEvent(mHereView);
        return mHereView;
    }
    
    public View getView(){
    	return mHereView;
    }
    
    public void openRVGroup(){
    	RVGroup.setVisibility(!isRVGroupOpened ? RelativeLayout.VISIBLE : RelativeLayout.GONE);
    	isRVGroupOpened= !isRVGroupOpened;
    }
    
    public void loadGroups(){
    	list = new ArrayList<Map<String, String>>();
    	HashMap<String, String> groupmap;
    	for(int j=0; j<6; j++)
		{
    		groupmap= new HashMap<String, String>();  
    		groupmap.put("name", "group "+j);
		    list.add(groupmap); 
		}
    	
    	
    	GroupListAdapter adapter = new GroupListAdapter(mContext);
    	groupsList.setAdapter(adapter);
    	
    }
    
	private  class GroupListAdapter extends BaseAdapter  {
		private LayoutInflater inflater;
		ImageView lampState, lampSetting ;
		Button groupSwitch;
		TextView groupName;
		Context mC;
		Button groupSet;
		GridView groupGrid;
		
		 public GroupListAdapter(Context c) {
			 mC=c;
             this.inflater = LayoutInflater.from(c);
         }
		
		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			 View myView = inflater.inflate(R.layout.group_item, null);
             groupName = (TextView) myView.findViewById(R.id.groupName);
             groupSwitch = (Button) myView.findViewById(R.id.groupSwitch);
             groupSet = (Button) myView.findViewById(R.id.groupSet);
             groupGrid = (GridView) myView.findViewById(R.id.groupGrid);
             
             groupName.setTag(arg0);
             groupSwitch.setTag(arg0);
             groupSet.setTag(arg0);
             groupGrid.setTag(arg0);
             
             groupName.setText(list.get(arg0).get("name"));
             
             List<Map<String, String>> list1  =  new ArrayList<Map<String, String>>();
             
             HashMap<String, String> groupmap;
         	 for(int j=0; j<6; j++)
     		 {
         		groupmap= new HashMap<String, String>();  
         		groupmap.put("name", "lampG "+j);
     		    list1.add(groupmap); 
     		 }
             
             LampOfGridAdapter adapter = new LampOfGridAdapter(mContext,  list1);
             groupGrid.setAdapter(adapter);
             
             //groupSwitch.setChecked( (list.get(arg0).get("isOn")+""=="1") ? true : false);
             
            /* lampState.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick( View v) {
					Toast.makeText(mC, "state "+v.getTag(), Toast.LENGTH_SHORT).show();
				}
			  });*/
             groupName.setOnClickListener(new OnClickListener() {
 				@Override
 				public void onClick( View v) {
 					
 					new AlertDialog.Builder(mC).setTitle("请输入新名字").setIcon(	android.R.drawable.ic_dialog_info).setView(new EditText(mC)).setPositiveButton("确定", null).setNegativeButton("取消", null).show();

 					Toast.makeText(mC, "Name "+v.getTag(), Toast.LENGTH_SHORT).show();
 				}
 			  });
             groupSwitch.setOnClickListener(new OnClickListener() {
 				@Override
 				public void onClick( View v) {
 					Toast.makeText(mC, "Switch "+v.getTag(), Toast.LENGTH_SHORT).show();
 				}
 			  });
             groupSet.setOnClickListener(new OnClickListener() {
				@SuppressLint("NewApi")
				@Override
 				public void onClick( View v) {
 					Intent configuration = new Intent(mC, ConfigurationForGroup.class);
 					Bundle bundle=new Bundle();
	                bundle.putString("lamptag",  groupName.getTag().toString());
	                configuration.putExtra("bundle", bundle);
 					startActivityForResult(configuration, CONFIGURATION_RESULT_CODE, bundle);
	                //startActivity(configuration);
	                
 					Toast.makeText(mC, "Setting "+v.getTag(), Toast.LENGTH_SHORT).show();
 				}
 			  });
             
             //mI.setScrollEvent(myView);
             mI.setScrollEvent(groupName);
             /*lampState.setOnClickListener(this);
             lampName.setOnClickListener(this);
             lampSwitch.setOnClickListener(this);
             lampSetting.setOnClickListener(this);*/
             
             AbsListView.LayoutParams p = new AbsListView.LayoutParams(     
            		 AbsListView.LayoutParams.MATCH_PARENT,     
            		 AbsListView.LayoutParams.WRAP_CONTENT
             ); 
             
             myView.setLayoutParams(p);
            
             return myView;
		}
	}
	
	private  class LampOfGridAdapter extends BaseAdapter  {
		private LayoutInflater inflater;
		ImageView lampState, lampSetting ,glampState;
		Switch groupSwitch;
		TextView glampName;
		Context mC;
		Button groupSet;
		GridView groupGrid;
		CheckBox lampCheckB;
		List<Map<String, String>> list1 ; 
		
		 public LampOfGridAdapter(Context c, List<Map<String, String>> list2) {
			 mC=c;
			 this.list1=list2;
             this.inflater = LayoutInflater.from(c);
         }
		
		@Override
		public int getCount() {
			return list1.size();
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			 View myView = inflater.inflate(R.layout.lamp_item_of_gridview, null);
			 glampName = (TextView) myView.findViewById(R.id.glampName);
             lampCheckB = (CheckBox) myView.findViewById(R.id.glampcb);
             glampState = (ImageView) myView.findViewById(R.id.glampState);
             //groupSet = (Button) myView.findViewById(R.id.groupSet);
             //groupGrid = (GridView) myView.findViewById(R.id.groupGrid);
             glampName.setTag(arg0);
             lampCheckB.setTag(arg0);
             glampState.setTag(arg0);
             
             glampName.setText(list1.get(arg0).get("name"));
             
             //mI.setScrollEvent(myView);
             //mI.setScrollEvent(groupGrid);
             AbsListView.LayoutParams p = new AbsListView.LayoutParams(     
            		 AbsListView.LayoutParams.MATCH_PARENT,     
            		 AbsListView.LayoutParams.WRAP_CONTENT
             ); 
             myView.setLayoutParams(p);
             return myView;
		}
	}
}  