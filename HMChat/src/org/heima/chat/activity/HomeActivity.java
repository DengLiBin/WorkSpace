package org.heima.chat.activity;

import org.heima.chat.ChatApplication;
import org.heima.chat.R;
import org.heima.chat.base.BaseActivity;
import org.heima.chat.db.MessageDao;
import org.heima.chat.fragment.ChatFra;
import org.heima.chat.fragment.ContactFra;
import org.heima.chat.fragment.DiscoverFra;
import org.heima.chat.fragment.MeFra;
import org.heima.chat.receiver.PushReceiver;
import org.heima.chat.widget.TabIndicator;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;

public class HomeActivity extends BaseActivity implements OnTabChangeListener {
	private FragmentTabHost mTabHost;

	private TabIndicator mChatIndicator;
	private TabIndicator mContactIndicator;
	private TabIndicator mDiscoverIndicator;
	private TabIndicator mMeIndicator;

	private static final String TAB_CHAT = "chat";
	private static final String TAB_CONTACT = "contact";
	private static final String TAB_DISCOVER = "discover";
	private static final String TAB_ME = "me";

	private PushReceiver pushReceiver = new PushReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// String from = intent.getStringExtra(PushReceiver.KEY_FROM);
			// String to = intent.getStringExtra(PushReceiver.KEY_TO);
			// intent.getStringExtra(PushReceiver.KEY_TEXT_CONTENT);

			loadTabData();
		}
	};

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.act_home);

		IntentFilter filter = new IntentFilter();
		filter.addAction(PushReceiver.ACTION_TEXT);
		registerReceiver(pushReceiver, filter);

		initTabHost();
		initIndicator();
		initTab();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		unregisterReceiver(pushReceiver);
	}

	@Override
	protected void onResume() {
		super.onResume();

		loadTabData();
	}

	private void loadTabData() {
		MessageDao dao = new MessageDao(this);
		int allUnread = dao.getAllUnread(((ChatApplication) getApplication())
				.getCurrentAccount().getAccount());
		mChatIndicator.setUnread(allUnread);
	}

	private void initTabHost() {
		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.homeTabContent);
		mTabHost.setOnTabChangedListener(this);
	}

	private void initIndicator() {
		mChatIndicator = new TabIndicator(this);
		mChatIndicator.setTabIcon(R.drawable.tab_icon_chat_normal,
				R.drawable.tab_icon_chat_focus);
		mChatIndicator.setTabHint(R.string.home_tab_chat);

		mContactIndicator = new TabIndicator(this);
		mContactIndicator.setTabIcon(R.drawable.tab_icon_contact_normal,
				R.drawable.tab_icon_contact_focus);
		mContactIndicator.setTabHint(R.string.home_tab_contact);

		mDiscoverIndicator = new TabIndicator(this);
		mDiscoverIndicator.setTabIcon(R.drawable.tab_icon_discover_normal,
				R.drawable.tab_icon_discover_focus);
		mDiscoverIndicator.setTabHint(R.string.home_tab_discover);

		mMeIndicator = new TabIndicator(this);
		mMeIndicator.setTabIcon(R.drawable.tab_icon_me_normal,
				R.drawable.tab_icon_me_focus);
		mMeIndicator.setTabHint(R.string.home_tab_me);
	}

	private void initTab() {
		TabSpec chatSpec = mTabHost.newTabSpec(TAB_CHAT);
		chatSpec.setIndicator(mChatIndicator);
		mTabHost.addTab(chatSpec, ChatFra.class, null);

		TabSpec contactSpec = mTabHost.newTabSpec(TAB_CONTACT);
		contactSpec.setIndicator(mContactIndicator);
		mTabHost.addTab(contactSpec, ContactFra.class, null);

		TabSpec discoverSpec = mTabHost.newTabSpec(TAB_DISCOVER);
		discoverSpec.setIndicator(mDiscoverIndicator);
		mTabHost.addTab(discoverSpec, DiscoverFra.class, null);

		TabSpec meSpec = mTabHost.newTabSpec(TAB_ME);
		meSpec.setIndicator(mMeIndicator);
		mTabHost.addTab(meSpec, MeFra.class, null);

		setCurrentTabByTag(TAB_CHAT);
	}

	private void setCurrentTabByTag(String tag) {
		mChatIndicator.setCurrentFocus(false);
		mContactIndicator.setCurrentFocus(false);
		mDiscoverIndicator.setCurrentFocus(false);
		mMeIndicator.setCurrentFocus(false);

		mTabHost.setCurrentTabByTag(tag);
		if (TAB_CHAT.equals(tag)) {
			mChatIndicator.setCurrentFocus(true);
		} else if (TAB_CONTACT.equals(tag)) {
			mContactIndicator.setCurrentFocus(true);
		} else if (TAB_DISCOVER.equals(tag)) {
			mDiscoverIndicator.setCurrentFocus(true);
		} else if (TAB_ME.equals(tag)) {
			mMeIndicator.setCurrentFocus(true);
		}
	}

	@Override
	public void onTabChanged(String tabId) {
		setCurrentTabByTag(tabId);

	}

}
