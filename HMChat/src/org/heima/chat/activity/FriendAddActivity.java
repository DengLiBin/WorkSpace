package org.heima.chat.activity;

import org.heima.chat.R;
import org.heima.chat.base.BaseActivity;
import org.heima.chat.widget.NormalTopBar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class FriendAddActivity extends BaseActivity implements OnClickListener {
	private NormalTopBar mTopBar;
	private EditText mSearchView;
	private View mScanView;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.act_friend_add);
		initView();
		initEvent();
	}

	private void initView() {
		mTopBar = (NormalTopBar) findViewById(R.id.friend_add_top_bar);
		mSearchView = (EditText) findViewById(R.id.friend_add_et_search);
		mScanView = findViewById(R.id.friend_add_scan);

		mTopBar.setTitle("添加朋友");
	}

	private void initEvent() {
		mTopBar.setOnBackListener(this);
		mScanView.setOnClickListener(this);
		mSearchView.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v == mTopBar.getBackView()) {
			clickBack();
		} else if (v == mScanView) {
			clickScan();
		} else if (v == mSearchView) {
			clickSearch();
		}
	}

	private void clickScan() {
		startActivity(new Intent(this, QRActivity.class));
	}

	private void clickBack() {
		finish();
	}

	private void clickSearch() {
		startActivity(new Intent(this, SearchContactActivity.class));
	}
}
