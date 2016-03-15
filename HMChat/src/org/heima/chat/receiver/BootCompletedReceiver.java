package org.heima.chat.receiver;

import org.heima.chat.service.ChatCoreService;
import org.heima.chat.utils.CommonUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootCompletedReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// 开机启动服务
		if (!CommonUtil.isServiceRunning(context, ChatCoreService.class)) {
			context.startService(new Intent(context, ChatCoreService.class));
		}
	}
}