package com.example.slidinglayout3d;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;


import sz.lamp.interface1.IScrollEvent;
import sz.lamp.network.GetMessages;
import sz.lamp.network.SendMessage;
import sz.lamp.network.State;
import sz.m.utils.DB4Lamp;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ValidFragment")
public class Lamps extends Fragment {  
	private static int CONFIGURATION_RESULT_CODE=1;
	
	private View mHereView;
	private ListView  lampsList;
	IScrollEvent mI;
	List<Map<String, String>> list ; 
	Map<String, String> lampmap ;  
	private Context mContext;
	private RelativeLayout RVLamp;
	private static Boolean isRVLampOpened = false;
	public boolean temp=false;

	public Lamps(MainActivity mainActivity) {
			mI=mainActivity;
	}

	@Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);
		
		mHereView = inflater.inflate(R.layout.lamps, container, false); 
		lampsList = (ListView) mHereView.findViewById(R.id.lampsList);
		RVLamp = (RelativeLayout) mHereView.findViewById(R.id.RVLamp);
		
		LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(     
				LinearLayout.LayoutParams.MATCH_PARENT,     
				LinearLayout.LayoutParams.WRAP_CONTENT
         ); 
         
		 lampsList.setLayoutParams(p);
		 mHereView.setLayoutParams(p);
		
		mContext= inflater.getContext();
        mI.setScrollEvent(lampsList);
        
	/*	handler.post(new Runnable() {
			@Override
			public void run() {
				// 在Post中操作UI组件ImageView
				loadLamps();
			}
		});*/
        writeLampsToDB();
		loadLamps();
        
   /*     new Thread(new Runnable(){

			@Override
			public void run() {
				loadLamps();
			}
        	
        }).start();*/
        
        
        return mHereView;
    }
	
	private  Handler handler = new Handler() {
		         @Override
		         public void handleMessage(Message msg) {            
		             if(msg.what==0){
		            	 
		             }
		         }
	 };
	
	public void loadLamps(){
		DB4Lamp db4lamp = new DB4Lamp(mContext,1);
		Cursor cur = db4lamp.loadAllRecord();
		lampsList = (ListView) mHereView.findViewById(R.id.lampsList);
		list = new ArrayList<Map<String, String>>();  
		
		LampItem lamp= new LampItem();
		cur.moveToFirst();
		while (!cur.isAfterLast()) {
			lamp = db4lamp.cursorToLampItem(cur);
			lampmap = new HashMap<String, String>();  
			lampmap.put("state",  lamp.getDeviceId()); 
			lampmap.put("name",   lamp.getName()  );
			lampmap.put("isOn", lamp.getIsOn()+"");
		    list.add(lampmap); 
			
			cur.moveToNext();
		}
		
	/*	for(int j=0; j<12; j++)
		{
			lampmap = new HashMap<String, String>();  
			lampmap.put("state", "1"); 
			lampmap.put("name", "lamp "+j);
		    list.add(lamp); 
		}*/
		
		LampListAdapter adapter = new LampListAdapter(mContext);
		lampsList.setAdapter(adapter);
		
		/*contentListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, contentItems);
		contentListView.setAdapter(contentListAdapter);
		slidingLayout.setScrollEvent(contentListView);*/
		
	}
	
	public void writeLampsToDB(){
		
		
		
		DB4Lamp db4lamp = new DB4Lamp(mContext,1);
		db4lamp.deleteAll();
		
		GetMessages getM= new GetMessages(mContext);
		JSONObject jo = getM.getAllLights("test1");
		if(jo==null)return;
		Iterator<String> it =jo.keys();
        String key;
        JSONObject value;
        while(it.hasNext()){ 
            key = (String)it.next().toString();
            try {
				value = jo.getJSONObject(key);
				LampItem lamp = new LampItem();
				lamp.setDeviceId(key);
				lamp.setName(value.getString("name"));
				db4lamp.add(lamp);
			} catch (JSONException e) {
				e.printStackTrace();
			}
        }
	}
	
	private  class LampListAdapter extends BaseAdapter  {
		private LayoutInflater inflater;
		ImageView lampState, lampSetting ;
		Button lampSwitch;
		TextView lampName;
		Context mC;
		
		 public LampListAdapter(Context c) {
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
			 View myView = inflater.inflate(R.layout.lamp_item, null);
             lampState = (ImageView) myView.findViewById(R.id.lampState);
             lampName = (TextView) myView.findViewById(R.id.lampName);
             lampSwitch = (Button) myView.findViewById(R.id.lampSwitch);
             lampSetting = (ImageView) myView.findViewById(R.id.lampSetting);
             
             lampState.setTag(arg0);
             lampName.setTag(arg0);
             lampSwitch.setTag(arg0);
             lampSetting.setTag(arg0);
             
             lampName.setText(list.get(arg0).get("name"));
             //lampSwitch.setChecked( (list.get(arg0).get("isOn")+""=="1") ? true : false);
             
             lampState.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick( View v) {
					Toast.makeText(mC, "state "+v.getTag(), Toast.LENGTH_SHORT).show();
				}
			  });
             lampName.setOnClickListener(new OnClickListener() {
 				@Override
 				public void onClick( View v) {
 					
 					new AlertDialog.Builder(mC).setTitle("请输入新名字").setIcon(	android.R.drawable.ic_dialog_info).setView(new EditText(mC)).setPositiveButton("确定", null).setNegativeButton("取消", null).show();

 					Toast.makeText(mC, "Name "+v.getTag(), Toast.LENGTH_SHORT).show();
 				}
 			  });
             lampSwitch.setOnClickListener(new OnClickListener() {
 				@Override
 				public void onClick( View v) {
 					temp=!temp;
 					SendMessage sm = new SendMessage(mContext);
 					State state = new State(temp,200,200,200);
 					sm.setLightState("test1", "3", state);
 					Toast.makeText(mC, "Switch "+v.getTag(), Toast.LENGTH_SHORT).show();
 				}
 			  });
             lampSetting.setOnClickListener(new OnClickListener() {
				@SuppressLint("NewApi")
				@Override
 				public void onClick( View v) {
 					Intent configuration = new Intent(mC, Configuration.class);
 					Bundle bundle=new Bundle();
	                bundle.putString("lamptag",  lampSetting.getTag().toString());
	                configuration.putExtra("bundle", bundle);
 					startActivityForResult(configuration, CONFIGURATION_RESULT_CODE, bundle);
	                //startActivity(configuration);
	                
 					Toast.makeText(mC, "Setting "+v.getTag(), Toast.LENGTH_SHORT).show();
 				}
 			  });
             
             //mI.setScrollEvent(myView);
             mI.setScrollEvent(lampState);
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
	
    @Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	
    	
    	
    }
    
    public View getView(){
    	return mHereView;
    }
    
    public void openRVLamp(Boolean b){
    	RVLamp.setVisibility(b ? RelativeLayout.VISIBLE : RelativeLayout.GONE);
    }
    
    public void openRVLamp(){
    	RVLamp.setVisibility(!isRVLampOpened ? RelativeLayout.VISIBLE : RelativeLayout.GONE);
    	isRVLampOpened= !isRVLampOpened;
    }
    
    
  
}  