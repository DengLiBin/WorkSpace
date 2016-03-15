package org.heima.chat.service;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.heima.chat.ChatApplication;
import org.heima.chat.base.BaseIntentService;
import org.heima.chat.db.BackTaskDao;
import org.heima.chat.db.HMDB;
import org.heima.chat.domain.Account;
import org.heima.chat.domain.NetTask;
import org.heima.chat.utils.SerializableUtil;
import org.heima.lib.HMChatManager;
import org.heima.lib.HMHttpManaer;
import org.heima.lib.callback.HMObjectCallBack;

import android.content.Intent;
import android.database.Cursor;

public class BackgroundService extends BaseIntentService {

	private Account account;

	public BackgroundService() {
		super("background");
	}

	@Override
	protected void onHandleIntent(Intent intent) {

		account = ((ChatApplication) getApplication()).getCurrentAccount();
		String owner = account.getAccount();
		HMChatManager.getInstance().initAccount(account.getAccount(),
				account.getToken());

		HMHttpManaer.getInstance().initAccount(account.getAccount(),
				account.getToken());

		final BackTaskDao dao = new BackTaskDao(this);

		Map<Long, String> map = new HashMap<Long, String>();

		Cursor cursor = dao.query(owner, 0);
		if (cursor != null) {

			while (cursor.moveToNext()) {
				final long id = cursor.getLong(cursor
						.getColumnIndex(HMDB.BackTask.COLUMN_ID));
				String filePath = cursor.getString(cursor
						.getColumnIndex(HMDB.BackTask.COLUMN_PATH));

				map.put(id, filePath);
			}
			cursor.close();
		}

		for (Map.Entry<Long, String> me : map.entrySet()) {
			try {
				final Long id = me.getKey();
				String filePath = me.getValue();

				NetTask task = (NetTask) SerializableUtil.read(filePath);

				int type = task.getType();
				if (type == NetTask.TYPE_NORMAL) {
					doNormalTask(dao, id, task);
				} else if (type == NetTask.TYPE_UPLOAD) {
					doUploadTask(dao, id, task);
				} else if (type == NetTask.TYPE_DOWNLOAD) {

				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	// private void doUploadTask(final BackTaskDao dao, final Long id, NetTask
	// task) {
	// HashMap<String, String> fileParams = task.getFiles();
	// Map<String, File> files = new HashMap<String, File>();
	//
	// if (fileParams == null) {
	// return;
	// }
	//
	// for (Map.Entry<String, String> p : fileParams.entrySet()) {
	// String key = p.getKey();
	// String value = p.getValue();
	// files.put(key, new File(value));
	// }
	//
	// HMChatManager.getInstance().uploadFile(task.getPath(), files,
	// task.getParams(), new HMObjectCallBack<Void>() {
	//
	// @Override
	// public void onSuccess(Void t) {
	// dao.updateState(id, 2);
	//
	// }
	//
	// @Override
	// public void onError(int error, String msg) {
	//
	// }
	// });
	// }

	// private void doNormalTask(final BackTaskDao dao, final Long id, NetTask
	// task) {
	// HMChatManager.getInstance().postByPath(task.getPath(),
	// task.getParams(), new HMObjectCallBack<Void>() {
	// @Override
	// public void onSuccess(Void t) {
	// dao.updateState(id, 2);
	// }
	//
	// @Override
	// public void onError(int error, String msg) {
	// // TODO Auto-generated method stub
	// }
	// });
	// }

	private void doNormalTask(final BackTaskDao dao, final Long id, NetTask task) {
		boolean result = HMHttpManaer.getInstance().post(task.getPath(),
				task.getParams());

		if (result) {
			dao.updateState(id, 2);
		}
	}

	private void doUploadTask(final BackTaskDao dao, final Long id, NetTask task) {
		HashMap<String, String> files = task.getFiles();
		if (files != null) {
			HMHttpManaer.getInstance().upload(task.getPath(), task.getParams(),
					task.getFiles());
		}
	}
}
