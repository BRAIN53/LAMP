package com.example.slidinglayout3d;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

public class SelectScene extends Activity {
	private Context c;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_scene);
		c=this;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			overridePendingTransition(R.anim.in_from_left, R.anim.out_to_left);
		} else {
			return false;
		}
		return true;
	}
	
	public void pic(View v){
		Intent i = new Intent(c,SketchPad.class);
		startActivityForResult(i,3);
		overridePendingTransition(R.anim.in_from_left, R.anim.out_to_left); 
		
	}
	
	public void weather(View v){
		Intent i = new Intent(c,WeatherPad.class);
		startActivityForResult(i,4);
		overridePendingTransition(R.anim.in_from_left, R.anim.out_to_left); 
	}
	
	public void voice(View v){
		Intent i = new Intent(c,VoicePad.class);
		startActivityForResult(i,5);
		overridePendingTransition(R.anim.in_from_left, R.anim.out_to_left); 
	}
	

}