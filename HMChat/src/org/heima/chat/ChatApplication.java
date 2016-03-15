package org.heima.chat;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.heima.chat.db.AccountDao;
import org.heima.chat.domain.Account;
import org.heima.lib.HMChat;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.Intent;
import android.os.Process;
import android.util.Log;

public class ChatApplication extends Application {
	private List<Activity> activitys = new LinkedList<Activity>();
	private List<Service> services = new LinkedList<Service>();
	private Account account;
	private int pid;

	@Override
	public void onCreate() {
		super.onCreate();
		pid = Process.myPid();
		
		Log.d("ChatApplication", "init");
		// 初始化
		HMChat.getInstance().init(this);
	}

	public void addActivity(Activity activity) {
		activitys.add(activity);
	}

	public void removeActivity(Activity activity) {
		activitys.remove(activity);
	}

	public void addService(Service service) {
		services.add(service);
	}

	public void removeService(Service service) {
		services.remove(service);
	}

	public void closeApplication() {
		closeActivitys();
		closeServices();
//		Process.killProcess(pid);
	}

	private void closeActivitys() {
		ListIterator<Activity> iterator = activitys.listIterator();
		while (iterator.hasNext()) {
			Activity activity = iterator.next();
			if (activity != null) {
				activity.finish();
			}
		}
	}

	private void closeServices() {
		ListIterator<Service> iterator = services.listIterator();
		while (iterator.hasNext()) {
			Service service = iterator.next();
			if (service != null) {
				stopService(new Intent(this, service.getClass()));
			}
		}
	}

	public Account getCurrentAccount() {
		if (account == null) {
			AccountDao dao = new AccountDao(this);
			account = dao.getCurrentAccount();
		}
		return account;
	}

}
