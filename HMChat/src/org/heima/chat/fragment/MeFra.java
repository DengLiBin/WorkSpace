package org.heima.chat.fragment;

import org.heima.chat.R;
import org.heima.chat.activity.PersonalInfoActivity;
import org.heima.chat.activity.SettingActivity;
import org.heima.chat.base.BaseFragment;
import org.heima.chat.db.AccountDao;
import org.heima.chat.domain.Account;
import org.heima.chat.widget.NormalTopBar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class MeFra extends BaseFragment implements OnClickListener {
	public static final int REQUEST_CODE_PERSONAL = 0x0011;

	private NormalTopBar mTopBar;

	private TextView tvName;
	private TextView tvAccount;
	private ImageView ivIcon;
	private View mAccountView;
	private View mSettingView;

	private AccountDao dao;
	private Account account;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		dao = new AccountDao(getActivity());
		account = dao.getCurrentAccount();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fra_me, container, false);
		initView(view);
		initEvent();
		return view;
	}

	private void initView(View view) {
		mTopBar = (NormalTopBar) view.findViewById(R.id.me_top_bar);

		tvName = (TextView) view.findViewById(R.id.me_tv_name);
		tvAccount = (TextView) view.findViewById(R.id.me_tv_account);
		ivIcon = (ImageView) view.findViewById(R.id.me_iv_icon);
		mAccountView = view.findViewById(R.id.me_item_account);
		mSettingView = view.findViewById(R.id.me_item_setting);

		mTopBar.setBackVisibility(false);
		mTopBar.setTitle("我");

	}

	private void loadAccountInfo() {
		account = dao.getCurrentAccount();
		tvAccount.setText(account.getAccount());
		tvName.setText(account.getName());

		Bitmap bitmap = BitmapFactory.decodeFile(account.getIcon());
		if (bitmap != null) {
			ivIcon.setImageBitmap(bitmap);
		}
	}

	private void initEvent() {
		mAccountView.setOnClickListener(this);
		mSettingView.setOnClickListener(this);
	}

	@Override
	public void onStart() {
		super.onStart();

		loadAccountInfo();
	}

	@Override
	public void onClick(View v) {
		if (v == mAccountView) {
			clickAccount();
		} else if (v == mSettingView) {
			clickSetting();
		}
	}

	private void clickSetting() {
		Intent intent = new Intent(getActivity(), SettingActivity.class);
		startActivity(intent);
	}

	private void clickAccount() {
		Intent intent = new Intent(getActivity(), PersonalInfoActivity.class);
		intent.putExtra(PersonalInfoActivity.KEY_INTENT, account);
		startActivityForResult(intent, REQUEST_CODE_PERSONAL);
	}
}
