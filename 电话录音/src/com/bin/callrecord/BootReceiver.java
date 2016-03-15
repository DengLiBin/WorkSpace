package com.bin.callrecord;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		//启动录音机服务
				Intent it = new Intent(context, RecoderService.class);
				context.startService(it);
			
	}

}
