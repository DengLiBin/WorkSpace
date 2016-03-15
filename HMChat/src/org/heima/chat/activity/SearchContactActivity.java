package org.heima.chat.activity;

import org.heima.chat.ChatApplication;
import org.heima.chat.R;
import org.heima.chat.base.BaseActivity;
import org.heima.chat.db.FriendDao;
import org.heima.chat.domain.Account;
import org.heima.chat.domain.Friend;
import org.heima.chat.utils.ToastUtil;
import org.heima.chat.widget.DialogLoading;
import org.heima.lib.HMChatManager;
import org.heima.lib.callback.HMObjectCallBack;
import org.heima.lib.future.HttpFuture;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class SearchContactActivity extends BaseActivity implements
		OnClickListener, TextWatcher {
	private ImageView ivBack;
	private EditText etSearch;
	private Button btnClearSearch;

	private View vClickItem;
	private TextView tvSearchContent;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.act_search_contact);

		initView();
		initEvent();
	}

	private void initView() {
		ivBack = (ImageView) findViewById(R.id.bar_btn_back);
		etSearch = (EditText) findViewById(R.id.bar_et_search);
		btnClearSearch = (Button) findViewById(R.id.bar_btn_clear_search);

		vClickItem = findViewById(R.id.search_item);
		tvSearchContent = (TextView) findViewById(R.id.search_tv_content);

		vClickItem.setVisibility(View.GONE);
		btnClearSearch.setVisibility(View.GONE);
	}

	private void initEvent() {
		ivBack.setOnClickListener(this);
		btnClearSearch.setOnClickListener(this);
		vClickItem.setOnClickListener(this);

		etSearch.addTextChangedListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v == ivBack) {
			clickBack();
		} else if (v == btnClearSearch) {
			clickClearSearch();
		} else if (v == vClickItem) {
			clickItem();
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {

	}

	@Override
	public void afterTextChanged(Editable s) {
		String searchContent = etSearch.getText().toString().trim();
		if (TextUtils.isEmpty(searchContent)) {
			vClickItem.setVisibility(View.GONE);
			btnClearSearch.setVisibility(View.GONE);
			return;
		}

		tvSearchContent.setText(searchContent);
		vClickItem.setVisibility(View.VISIBLE);
		btnClearSearch.setVisibility(View.VISIBLE);
	}

	private void clickBack() {
		finish();
	}

	private void clickClearSearch() {
		etSearch.setText("");
	}

	private void clickItem() {
		String account = etSearch.getText().toString().trim();

		Account currentAccount = ((ChatApplication) getApplication())
				.getCurrentAccount();
		String currentUser = currentAccount.getAccount();
		if (currentUser.equals(account)) {
			ToastUtil.show(getApplicationContext(), "不要找自己啦");
			return;
		}

		// 已有的朋友
		FriendDao dao = new FriendDao(this);
		Friend friend = dao.queryFriendByAccount(currentUser, account);
		if (friend != null) {
			Intent intent = new Intent(this, FriendDetailActivity.class);
			intent.putExtra(FriendDetailActivity.KEY_ENTER,
					FriendDetailActivity.ENTER_CONTACT);
			intent.putExtra(FriendDetailActivity.KEY_DATA, friend);
			startActivity(intent);

			return;
		}

		final DialogLoading dialog = new DialogLoading(this);
		dialog.show();

		HttpFuture future = HMChatManager.getInstance().searchContact(account,
				new HMObjectCallBack<Friend>() {

					@Override
					public void onSuccess(Friend t) {
						dialog.dismiss();
						if (t != null) {
							Log.d("", "" + t.toString());

							Intent intent = new Intent(
									SearchContactActivity.this,
									FriendDetailActivity.class);
							intent.putExtra(FriendDetailActivity.KEY_ENTER,
									FriendDetailActivity.ENTER_SEARCH);
							intent.putExtra(FriendDetailActivity.KEY_DATA, t);
							startActivity(intent);

							finish();
						}
					}

					@Override
					public void onError(int error, String msg) {
						dialog.dismiss();
						Log.d("", error + " : " + msg);

						if (error == 200) {
							ToastUtil
									.show(getApplicationContext(), "你搜索的用户不存在");
						}
					}
				});
	}
}
