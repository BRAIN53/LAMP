package com.example.slidinglayout3d;

import sz.lamp.interface1.IScrollEvent;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

@SuppressLint("ValidFragment")
public class Aboutus extends Fragment {  
	
	private View mHereView;
	IScrollEvent mI;

	public Aboutus(MainActivity mainActivity) {
			mI=mainActivity;
	}

	@Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {  
		mHereView = inflater.inflate(R.layout.aboutus, container, false); 
        mI.setScrollEvent(mHereView);
        return mHereView;
    }
    
    public View getView(){
    	return mHereView;
    }
    
  
}  