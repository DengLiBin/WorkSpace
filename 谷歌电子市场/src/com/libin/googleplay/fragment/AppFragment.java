package com.libin.googleplay.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

//继承v4包下的Fragment，才能兼容低版本
public class AppFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		TextView view=new TextView(getActivity());
		view.setText("我是AppFragment");
		return view;
	}
}
