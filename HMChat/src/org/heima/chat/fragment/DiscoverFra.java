package org.heima.chat.fragment;

import org.heima.chat.R;
import org.heima.chat.base.BaseFragment;
import org.heima.chat.widget.NormalTopBar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DiscoverFra extends BaseFragment {
	private NormalTopBar mTopBar;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fra_discover, container, false);
		initView(view);
		return view;
	}

	private void initView(View view) {
		mTopBar = (NormalTopBar) view.findViewById(R.id.discover_top_bar);

		mTopBar.setBackVisibility(false);
		mTopBar.setTitle("发现");
	}

}
