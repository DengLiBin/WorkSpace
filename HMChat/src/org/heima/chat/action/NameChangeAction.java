package org.heima.chat.action;

import java.util.Map;

import org.heima.chat.db.FriendDao;
import org.heima.chat.domain.Friend;
import org.heima.chat.receiver.PushReceiver;

import android.content.Context;
import android.content.Intent;

public class NameChangeAction extends Action {

	@Override
	public String getAction() {
		return "nameChange";
	}

	@Override
	public void doAction(Context context, Map<String, Object> data) {
		if (data == null) {
			return;
		}

		String receiver = data.get("receiver").toString();
		String sender = data.get("sender").toString();
		String name = data.get("name").toString();

		// 数据存储
		FriendDao friendDao = new FriendDao(context);
		Friend friend = friendDao.queryFriendByAccount(receiver, sender);
		if (friend == null) {
			return;
		}
		friend.setName(name);
		friendDao.updateFriend(friend);

		// 发送广播
		Intent intent = new Intent(PushReceiver.ACTION_NAME_CHANGE);
		intent.putExtra(PushReceiver.KEY_FROM, sender);
		intent.putExtra(PushReceiver.KEY_TO, receiver);
		context.sendBroadcast(intent);
	}

}
