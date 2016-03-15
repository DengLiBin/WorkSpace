package org.heima.chat.fragment;

import org.heima.chat.ChatApplication;
import org.heima.chat.R;
import org.heima.chat.activity.FriendAddActivity;
import org.heima.chat.activity.FriendDetailActivity;
import org.heima.chat.activity.FriendNewActivity;
import org.heima.chat.base.BaseFragment;
import org.heima.chat.db.FriendDao;
import org.heima.chat.db.HMDB;
import org.heima.chat.db.InvitationDao;
import org.heima.chat.domain.Account;
import org.heima.chat.domain.Friend;
import org.heima.chat.receiver.PushReceiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ContactFra extends BaseFragment implements OnClickListener,
		OnItemClickListener {
	private ImageView ivAddFriend;
	private TextView tvTitle;

	private ListView listView;
	private ContactAdapter adapter;

	private View newFriendView;
	private View newFriendViewDot;

	private PushReceiver pushReceiver = new PushReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// intent.getStringExtra(PushReceiver.KEY_FROM);
			String to = intent.getStringExtra(PushReceiver.KEY_TO);
			// intent.getStringExtra(PushReceiver.KEY_TEXT_CONTENT);

			Account account = ((ChatApplication) getActivity().getApplication())
					.getCurrentAccount();
			if (account.getAccount().equalsIgnoreCase(to)) {
				loadData();
			}
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fra_contact, container, false);
		initView(view);
		initEvent();

		IntentFilter filter = new IntentFilter();
		filter.addAction(PushReceiver.ACTION_REINVATION);
		filter.addAction(PushReceiver.ACTION_INVATION);
		filter.addAction(PushReceiver.ACTION_ICON_CHANGE);
		filter.addAction(PushReceiver.ACTION_NAME_CHANGE);
		getActivity().registerReceiver(pushReceiver, filter);

		return view;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		getActivity().unregisterReceiver(pushReceiver);
	}

	@Override
	public void onResume() {
		super.onResume();

		loadData();
	}

	private void initView(View view) {
		ivAddFriend = (ImageView) view.findViewById(R.id.bar_add_friend);
		tvTitle = (TextView) view.findViewById(R.id.bar_title);
		listView = (ListView) view.findViewById(R.id.contact_list_view);
		tvTitle.setText("通讯录");

		View headerView = View.inflate(getActivity(),
				R.layout.layout_contact_top, null);
		listView.addHeaderView(headerView);

		newFriendView = headerView.findViewById(R.id.contact_item_new_friend);
		newFriendViewDot = headerView
				.findViewById(R.id.contact_item_new_friend_dot);

		adapter = new ContactAdapter(getActivity(), null);
		listView.setAdapter(adapter);
	}

	private void initEvent() {
		ivAddFriend.setOnClickListener(this);
		newFriendView.setOnClickListener(this);

		listView.setOnItemClickListener(this);
	}

	private void loadData() {
		if (getActivity() == null) {
			return;
		}
		ChatApplication application = (ChatApplication) getActivity()
				.getApplication();
		Account account = application.getCurrentAccount();

		FriendDao friendDao = new FriendDao(getActivity());
		adapter.changeCursor(friendDao.queryFriends(account.getAccount()));

		InvitationDao dao = new InvitationDao(getActivity());
		boolean hasUnagree = dao.hasUnagree(((ChatApplication) getActivity()
				.getApplication()).getCurrentAccount().getAccount());
		if (hasUnagree) {
			newFriendViewDot.setVisibility(View.VISIBLE);
		} else {
			newFriendViewDot.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		if (v == ivAddFriend) {
			clickAddFriend();
		} else if (v == newFriendView) {
			clickNewFriend();
		}
	}

	private void clickNewFriend() {
		if (getActivity() == null) {
			return;
		}
		getActivity().startActivity(
				new Intent(getActivity(), FriendNewActivity.class));
	}

	private void clickAddFriend() {
		if (getActivity() == null) {
			return;
		}
		getActivity().startActivity(
				new Intent(getActivity(), FriendAddActivity.class));
	}

	private class ContactAdapter extends CursorAdapter {

		public ContactAdapter(Context context, Cursor c) {
			super(context, c);
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			return View.inflate(context, R.layout.item_contact, null);
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			TextView tvName = (TextView) view
					.findViewById(R.id.item_contact_name);
			ImageView ivIcon = (ImageView) view
					.findViewById(R.id.item_contact_icon);

			String name = cursor.getString(cursor
					.getColumnIndex(HMDB.Friend.COLUMN_NAME));

			long id = cursor.getLong(cursor
					.getColumnIndex(HMDB.Friend.COLUMN_ID));
			String account = cursor.getString(cursor
					.getColumnIndex(HMDB.Friend.COLUMN_ACCOUNT));
			String alpha = cursor.getString(cursor
					.getColumnIndex(HMDB.Friend.COLUMN_ALPHA));
			String area = cursor.getString(cursor
					.getColumnIndex(HMDB.Friend.COLUMN_AREA));
			String icon = cursor.getString(cursor
					.getColumnIndex(HMDB.Friend.COLUMN_ICON));
			String nickName = cursor.getString(cursor
					.getColumnIndex(HMDB.Friend.COLUMN_NICKNAME));
			String owner = cursor.getString(cursor
					.getColumnIndex(HMDB.Friend.COLUMN_OWNER));
			int sex = cursor.getInt(cursor
					.getColumnIndex(HMDB.Friend.COLUMN_SEX));
			String sign = cursor.getString(cursor
					.getColumnIndex(HMDB.Friend.COLUMN_SIGN));
			int sort = cursor.getInt(cursor
					.getColumnIndex(HMDB.Friend.COLUMN_SORT));
			Friend friend = new Friend();
			friend.setAccount(account);
			friend.setAlpha(alpha);
			friend.setArea(area);
			friend.setIcon(icon);
			friend.setId(id);
			friend.setName(name);
			friend.setNickName(nickName);
			friend.setOwner(owner);
			friend.setSex(sex);
			friend.setSign(sign);
			friend.setSort(sort);

			tvName.setText(name);

			System.out.println("icon : " + icon);
			ivIcon.setImageResource(R.drawable.default_icon_user);
			if (!TextUtils.isEmpty(icon)) {
				Bitmap bitmap = BitmapFactory.decodeFile(icon);
				if (bitmap != null) {
					ivIcon.setImageBitmap(bitmap);
				}
			}

			view.setTag(friend);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		Object tag = view.getTag();

		if (tag != null && tag instanceof Friend) {
			Friend friend = (Friend) tag;

			Intent intent = new Intent(getActivity(),
					FriendDetailActivity.class);
			intent.putExtra(FriendDetailActivity.KEY_ENTER,
					FriendDetailActivity.ENTER_CONTACT);
			intent.putExtra(FriendDetailActivity.KEY_DATA, friend);
			startActivity(intent);
		}
	}
}
