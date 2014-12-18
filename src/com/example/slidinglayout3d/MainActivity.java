package com.example.slidinglayout3d;

import sz.lamp.interface1.IScrollEvent;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity implements IScrollEvent, OnClickListener{

	private ThreeDSlidingLayout slidingLayout;

	private Button menuButton, btnLamps,btnGroups,btnScenes, btnUpdate, btnAboutus;
	private Button menuButtonRight,btnLightControl;

	private ListView contentListView;
	
	private int currentPage=1;
	private Lamps lamps;
	Groups groups;
	private Context c;
	

	private ArrayAdapter<String> contentListAdapter;

	private String[] contentItems = { "Content Item 1", "Content Item 2", "Content Item 3",
			"Content Item 4", "Content Item 5", "Content Item 6", "Content Item 7",
			"Content Item 8", "Content Item 9", "Content Item 10", "Content Item 11",
			"Content Item 12", "Content Item 13", "Content Item 14", "Content Item 15",
			"Content Item 16" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		setContentView(R.layout.activity_main);
		 StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		 StrictMode.setThreadPolicy(policy);
		c=this;
		
		slidingLayout = (ThreeDSlidingLayout) findViewById(R.id.slidingLayout);
		menuButton = (Button) findViewById(R.id.menuButton);
		menuButtonRight = (Button) findViewById(R.id.menuButtonRight);
		btnLamps = (Button) findViewById(R.id.btnLamps);
		btnGroups = (Button) findViewById(R.id.btnGroups);
		btnScenes = (Button) findViewById(R.id.btnScenes);
		btnLightControl=(Button) findViewById(R.id.btnLightControl);
		//btnUpdate = (Button) findViewById(R.id.btnUpdate);
		//btnAboutus = (Button) findViewById(R.id.btnAboutUs);
		btnLamps.setOnClickListener(this);
		btnGroups.setOnClickListener(this);
		btnScenes.setOnClickListener(this);
		btnLightControl.setOnClickListener(this);
		//btnUpdate.setOnClickListener(this);
		//btnAboutus.setOnClickListener(this);
		
		
	/*	contentListView = (ListView) findViewById(R.id.contentList);
		contentListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, contentItems);
		contentListView.setAdapter(contentListAdapter);
		slidingLayout.setScrollEvent(contentListView);*/
		
		menuButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (slidingLayout.isLeftLayoutVisible()) {
					slidingLayout.scrollToRightLayout();
				} else {
					slidingLayout.scrollToLeftLayout();
				}
			}
		});
		
		
		menuButtonRight.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if( currentPage==1){
					if(lamps!=null){
						lamps.openRVLamp();
					}
				}else if(currentPage == 2){
					if(groups!=null){
						groups.openRVGroup();
					}
				}
				
			}
		});
		
	/*	contentListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String text = contentItems[position];
				Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
			}
		});*/
		
		 lamps = new Lamps(this);  
        getFragmentManager().beginTransaction().replace(R.id.content1, lamps).commit();  
        
        //menuButton.performClick();
      /*  LinearLayout content1 = (LinearLayout) findViewById(R.id.content1);
        
        slidingLayout.setScrollEvent(content1);*/
		
		
	}

	@Override
	public void setScrollEvent(View bindView) {
		slidingLayout.setScrollEvent(bindView);
		
	}

	@Override
	public void onClick(View arg0) {
		 menuButton.performClick();
		int vId=arg0.getId();
		if(vId==btnLamps.getId()){
			if(currentPage!=1){
				 lamps = new Lamps(this);  
		        getFragmentManager().beginTransaction().replace(R.id.content1, lamps).commit(); 
		        currentPage=1;
			}
			//menuButton.performClick();
		}else if(vId==btnGroups.getId()){
			if(currentPage!=2){
				 groups = new Groups(this);  
		        getFragmentManager().beginTransaction().replace(R.id.content1, groups).commit(); 
		        currentPage=2;
			}
			 //menuButton.performClick();
		}else if(vId==btnScenes.getId()){
			if(currentPage!=3){
				Scenes scenes = new Scenes(this);  
		        getFragmentManager().beginTransaction().replace(R.id.content1, scenes).commit();  
		        currentPage=3;
			}
			 //menuButton.performClick();
		}
		else if(vId==btnLightControl.getId()){
				Intent PanelControl = new Intent(c , ColorPanel.class);
				/*Bundle bundle=new Bundle();
                bundle.putString("lamptag",  lampSetting.getTag().toString());
                configuration.putExtra("bundle", bundle);*/
				startActivityForResult(PanelControl, 0);
				
		}
		
/*		else if(vId==btnUpdate.getId()){
			if(currentPage!=4){
				UpdateSoft lamps = new UpdateSoft(this);  
		        getFragmentManager().beginTransaction().replace(R.id.content1, lamps).commit();
		        currentPage=4;
			}
			 //menuButton.performClick();
		}else if(vId==btnAboutus.getId()){
			if(currentPage!=5){
				Aboutus lamps = new Aboutus(this);  
		        getFragmentManager().beginTransaction().replace(R.id.content1, lamps).commit(); 
		        currentPage=5;
			}
			
		}*/
		
		
	}
	
	
	
	
}