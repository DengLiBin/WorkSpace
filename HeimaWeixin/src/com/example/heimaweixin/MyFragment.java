package com.example.heimaweixin;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * Created by DengLibin on 2016/3/11 0011.
 */
public class MyFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	View view=View.inflate(getContext(), R.layout.my_fragment, null);
        TextView textView=(TextView) view.findViewById(R.id.tv_myfragment);
        textView.setText("我是一个textView,在myFragment中");
        return  view;
    }
}
