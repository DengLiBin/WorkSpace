package org.heima.chat.base;

import org.heima.chat.ChatApplication;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class BaseActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		((ChatApplication) getApplication()).addActivity(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		((ChatApplication) getApplication()).removeActivity(this);
	}
}
