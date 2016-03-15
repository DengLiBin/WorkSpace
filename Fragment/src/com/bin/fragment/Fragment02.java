package com.bin.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Fragment02 extends Fragment {
	private TextView tv;
	//返回的view对象会作为fragment02的内容显示在屏幕上
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.fragment02, null);
		tv=(TextView) v.findViewById(R.id.tv);
		return v;
	}
	public void setText(String text){
		tv.setText(text);
	}
}
