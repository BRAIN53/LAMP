package com.example.slidinglayout3d;

import sz.lamp.interface1.ISetOpacityBar;
import sz.lamp.network.SendMessage;
import sz.lamp.network.State;

import com.larswerkman.colorpicker.ColorPicker;
import com.larswerkman.colorpicker.ColorPicker.OnColorChangedListener;
import com.larswerkman.colorpicker.OpacityBar;
import com.larswerkman.colorpicker.SVBar;
import com.larswerkman.colorpicker.SaturationBar;
import com.larswerkman.colorpicker.ValueBar;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnAttachStateChangeListener;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Configuration extends Activity  {
	OpacityBar opacityBar;
	SaturationBar sb;
	LinearLayout llSetScene;
	Context c;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		setContentView(R.layout.scense_configuration);
		c= this;
	    /*
		Bundle bundle  =  savedInstanceState.getBundle("bundle");
		String lamptag  =  (String) bundle.get("lamptag");
		 */
		
		TextView lampName = (TextView)findViewById(R.id.lampName);
		lampName.setText(" bedroom 1");
		ColorPicker colorPicker = (ColorPicker)findViewById(R.id.colorPicker);
		colorPicker.setColor(5);
		
		colorPicker.getColor();
		
		 opacityBar = (OpacityBar)findViewById(R.id.opacityBar);
		 sb = (SaturationBar)findViewById(R.id.saturationBar);
		 //SVBar svb= (SVBar)findViewById(R.id.SVBar);
		 //ValueBar vb= (ValueBar)findViewById(R.id.ValueBar);
		 //opacityBar.setColor(0x0000ff);
		 colorPicker.addOpacityBar(opacityBar);
		 colorPicker.addSaturationBar(sb);
		 //colorPicker.addSVBar(svb);
		 //colorPicker.addValueBar(vb);
		
		 llSetScene= (LinearLayout)findViewById(R.id.llSetScene);
		 
		 
		 colorPicker.setOnColorChangedListener(new OnColorChangedListener(){

			@Override
			public void onColorChanged(int color) {
				SendMessage sm = new SendMessage(c);
				State state = new State(true,color, sb.getColor(), opacityBar.getColor());
				sm.setLightState("test1", "3", state);	
			}
			 
		 });
		
		
		Button setScence = (Button)findViewById(R.id.btnSetScene);
		setScence.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick( View v) {
					if(llSetScene.getVisibility()==LinearLayout.GONE){
						llSetScene.setVisibility(LinearLayout.VISIBLE);
						
					}else{
						
						llSetScene.setVisibility(LinearLayout.GONE);
						
					}
					
				}
		});
		
		Button okSence = (Button)findViewById(R.id.okSence);
		okSence.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick( View v) {
					
					finish();
				}
		});
		
		Button cancelScence = (Button)findViewById(R.id.cancelScence);
		cancelScence.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick( View v) {

					finish();
				}
		});
		
		
		
		
		
	}

}
