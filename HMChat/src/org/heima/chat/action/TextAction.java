package org.heima.chat.action;

import java.util.Map;

import org.heima.chat.db.MessageDao;
import org.heima.chat.domain.Message;
import org.heima.chat.receiver.PushReceiver;

import android.content.Context;
import android.content.Intent;

public class TextAction extends Action {

	@Override
	public String getAction() {
		return "text";
	}

	@Override
	public void doAction(Context context, Map<String, Object> data) {
		if (data == null) {
			return;
		}

		String receiver = data.get("receiver").toString();
		String sender = data.get("sender").toString();

		String content = data.get("content").toString();

		// 数据存储
		MessageDao dao = new MessageDao(context);
		Message message = new Message();
		message.setAccount(sender);
		message.setContent(content);
		message.setCreateTime(System.currentTimeMillis());
		message.setDirection(1);
		message.setOwner(receiver);
		message.setRead(false);
		message.setType(0);
		dao.addMessage(message);

		// 发送广播
		Intent intent = new Intent(PushReceiver.ACTION_TEXT);
		intent.putExtra(PushReceiver.KEY_FROM, sender);
		intent.putExtra(PushReceiver.KEY_TO, receiver);
		intent.putExtra(PushReceiver.KEY_TEXT_CONTENT, content);
		context.sendBroadcast(intent);
	}

}
