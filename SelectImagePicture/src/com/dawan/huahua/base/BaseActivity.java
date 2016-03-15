package com.dawan.huahua.base;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.dawan.huahua.R;
import com.dawan.huahua.R.anim;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public abstract class BaseActivity extends Activity {

	public static final String TAG = BaseActivity.class.getSimpleName();
	public Toast mToast = null;
	protected Handler mHandler = new Handler();

	/**
	 * ��ǰ�����ı���id
	 */
	private Button btnBack;
	private int localId = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		try {
			this.requestWindowFeature(Window.FEATURE_NO_TITLE);// ȥ��������
		} catch (Exception e) {
			// TODO: handle exception
		}

		// CatchExcApplication app = (CatchExcApplication) getApplication();
		// app.init();
		// app.addActivity(this);

		// AppManager.getInstance().addActivity(this);
		// if (!ImageLoader.getInstance().isInited()) {
		// ImageLoaderConfig.initImageLoader(this, Constants.BASE_IMAGE_CACHE);
		// }

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		mHandler = new BaseHandler();
	}

	// ////////////////////////////////////////////////////////////////////////
	protected BaseHandler mBaseHandler;

	public class BaseHandler extends Handler {

		@Override
		public void handleMessage(android.os.Message msg) {
			handleMsg(msg);
		}
	}

	public void handleMsg(Message message) {
	}

	// ////////////////////////////////////////////////////////////////////////

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	/**
	 * �󶨿ؼ�id
	 */
	protected abstract void findViewById();

	/**
	 * ��ʼ���ؼ�
	 */
	protected abstract void initView();

	/**
	 * ͨ����������Activity
	 * 
	 * @param pClass
	 */
	protected void openActivity(Class<?> pClass) {
		openActivity(pClass, null);
	}

	/**
	 * ͨ����������Activity�����Һ���Bundle����
	 * 
	 * @param pClass
	 * @param pBundle
	 */
	protected void openActivity(Class<?> pClass, Bundle pBundle) {
		Intent intent = new Intent(this, pClass);
		if (pBundle != null) {
			intent.putExtras(pBundle);
		}
		startActivity(intent);
	}
	
	/**
	 * ͨ����������Activity�����Һ���Bundle����
	 * 
	 * @param pClass
	 * @param pBundle
	 */
	protected void openActivityByMap(Class<?> pClass, Map<String, String> map) {
		Intent intent = new Intent(this, pClass);
		Bundle mBundle = mapToBundle(map);
		if (mBundle != null) {
			intent.putExtras(mBundle);
		}
		startActivity(intent);
	}
	/**
	 * mapת��Bundle
	 * */
	private Bundle mapToBundle(Map<String, String> map)
	{
		Bundle bundle = new Bundle();
		Set<String> keySet = map.keySet();
		Iterator<String> iter = keySet.iterator();                    
		while(iter.hasNext())
		{
		    String key = iter.next();
		    bundle.putString(key, map.get(key));
		}
		return bundle;
	}

	/**
	 * ͨ��Action����Activity
	 * 
	 * @param pAction
	 */
	protected void openActivity(String pAction) {
		openActivity(pAction, null);
	}

	/**
	 * ͨ��Action����Activity�����Һ���Bundle����
	 * 
	 * @param pAction
	 * @param pBundle
	 */
	protected void openActivity(String pAction, Bundle pBundle) {
		Intent intent = new Intent(pAction);
		if (pBundle != null) {
			intent.putExtras(pBundle);
		}
		startActivity(intent);
	}

	protected void DisPlay(String content) {
		Toast.makeText(this, content, 1).show();
	}

	/** ���ؽ����� */
	@SuppressWarnings("unused")
	public void showProgressDialog() {
		ProgressDialog progressDialog = null;

		if (progressDialog != null) {
			progressDialog.cancel();
		}
		progressDialog = new ProgressDialog(this);
		Drawable drawable = getResources()
				.getDrawable(R.anim.loading_animation);
		progressDialog.setIndeterminateDrawable(drawable);
		progressDialog.setIndeterminate(true);
		progressDialog.setCancelable(true);
		progressDialog.setMessage("���Ժ�����Ŭ�����ء���");
		progressDialog.show();
	}

	public void showToast(String msg) {
		if (mToast == null) {
			mToast = Toast.makeText(this.getApplicationContext(), msg,
					Toast.LENGTH_SHORT);
		} else {
			mToast.setText(msg);
			mToast.setDuration(Toast.LENGTH_SHORT);
		}
		mToast.show();
		mToast = null;
	}

	/**
	 * ��ʼ�����ذ�ť
	 * */
	protected void initBtnBack(final Activity mActivity) {
		Log.e("", "��ûִ��");
//		btnBack = (Button) mActivity.findViewById(R.id.btn_back);
		if (btnBack != null) {
			btnBack.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					BaseActivity.this.finish();
				}
			});
		} else {
			Log.e("", "��Ϊ��");
		}

	}

	/**
	 * ���ı������¼�
	 */
	public void setOnTouchListener(EditText[] editTexts) {
		if (editTexts != null) {
			for (EditText editText : editTexts) {
				if (editText != null) {
					editText.setOnTouchListener(new textTouchLister());
				}
			}
		}
	}

	/**
	 * �ı����������
	 * 
	 * @author wyz
	 * 
	 */
	public class textTouchLister implements OnTouchListener {
		@Override
		public boolean onTouch(View view, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				InputMethodManager imm = (InputMethodManager) BaseActivity.this
						.getSystemService(Context.INPUT_METHOD_SERVICE);

				EditText txt = (EditText) view;
				if (view.hasFocus() && localId == txt.getId()) {
					// ���������
					Log.i("���������:::", "���������");
					imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
					localId = 0;
				} else {
					Log.i("��ʾ�����:::", "��ʾ�����");
					localId = txt.getId();
				}
			}
			if (localId == 0) {
				return true;
			}
			return false;
		}
	}
}
