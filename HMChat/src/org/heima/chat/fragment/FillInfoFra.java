package org.heima.chat.fragment;

import java.io.File;

import org.heima.chat.R;
import org.heima.chat.activity.HomeActivity;
import org.heima.chat.base.BaseFragment;
import org.heima.chat.db.AccountDao;
import org.heima.chat.db.BackTaskDao;
import org.heima.chat.domain.Account;
import org.heima.chat.domain.BackTask;
import org.heima.chat.domain.NetTask;
import org.heima.chat.service.BackgroundService;
import org.heima.chat.utils.BackTaskFactory;
import org.heima.chat.utils.CommonUtil;
import org.heima.chat.utils.DirUtil;
import org.heima.chat.utils.SerializableUtil;
import org.heima.chat.utils.ToastUtil;
import org.heima.chat.widget.DialogChooseImage;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class FillInfoFra extends BaseFragment implements OnClickListener,
		TextWatcher {

	private final static int REQUEST_CODE_GALLERY = 0x11;
	private final static int REQUEST_CODE_CAMERA = 0x12;
	private final static int REQUEST_CODE_CROP = 0x13;

	private ImageView ivIcon;
	private EditText etName;
	private Button btnClear;
	private Button btnOk;

	private int crop = 200;
	private File sdcardTempFile;

	private Account account;
	private AccountDao dao;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dao = new AccountDao(getActivity());
		account = dao.getCurrentAccount();

		String iconDir = DirUtil.getIconDir(getActivity());
		sdcardTempFile = new File(iconDir, CommonUtil.string2MD5(account
				.getAccount()));

		if (!sdcardTempFile.getParentFile().exists()) {
			sdcardTempFile.getParentFile().mkdirs();
		}

		account.setIcon(sdcardTempFile.getAbsolutePath());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fra_fill_info, container, false);
		initView(view);
		initEvent();
		return view;
	}

	private void initView(View view) {
		ivIcon = (ImageView) view.findViewById(R.id.fill_info_iv_icon);
		etName = (EditText) view.findViewById(R.id.fill_info_et_name);
		btnClear = (Button) view.findViewById(R.id.fill_info_btn_clear_name);
		btnOk = (Button) view.findViewById(R.id.fill_info_btn_ok);

		btnClear.setVisibility(View.GONE);
		btnOk.setEnabled(false);
	}

	private void initEvent() {
		ivIcon.setOnClickListener(this);
		etName.addTextChangedListener(this);
		btnClear.setOnClickListener(this);
		btnOk.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v == ivIcon) {
			openChooseDialog();
		} else if (v == btnClear) {
			etName.setText("");
		} else if (v == btnOk) {
			doOk();
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
	}

	@Override
	public void afterTextChanged(Editable s) {
		int length = etName.getText().toString().trim().length();
		if (length > 0) {
			btnClear.setVisibility(View.VISIBLE);
			btnOk.setEnabled(true);
		} else {
			btnClear.setVisibility(View.GONE);
			btnOk.setEnabled(false);
		}
	}

	private void openChooseDialog() {

		final DialogChooseImage dialog = new DialogChooseImage(getActivity());
		dialog.show();

		dialog.setClickCameraListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getImageFromCamera();
				dialog.dismiss();
			}
		});
		dialog.setClickGalleryListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getimageFromGallery();
				dialog.dismiss();
			}
		});
	}

	protected void getimageFromGallery() {

		Intent intent = new Intent("android.intent.action.PICK");
		intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI,
				"image/*");
		intent.putExtra("output", Uri.fromFile(sdcardTempFile));
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);// 裁剪框比例
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", crop);// 输出图片大小
		intent.putExtra("outputY", crop);
		startActivityForResult(intent, REQUEST_CODE_GALLERY);

	}

	protected void getImageFromCamera() {
		Uri uri = Uri.fromFile(sdcardTempFile);
		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		intent.putExtra("output", uri);
		startActivityForResult(intent, REQUEST_CODE_CAMERA);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode != Activity.RESULT_OK) {
			return;
		}
		switch (requestCode) {
		case REQUEST_CODE_GALLERY: {
			performImageBack();
			break;
		}
		case REQUEST_CODE_CAMERA: {
			Uri uri = Uri.fromFile(sdcardTempFile);
			Intent intent = new Intent("com.android.camera.action.CROP");
			intent.setDataAndType(uri, "image/*");
			intent.putExtra("output", uri);
			intent.putExtra("crop", "true");
			intent.putExtra("aspectX", 1);// 裁剪框比例
			intent.putExtra("aspectY", 1);
			intent.putExtra("outputX", crop);// 输出图片大小
			intent.putExtra("outputY", crop);
			startActivityForResult(intent, REQUEST_CODE_CROP);
			break;
		}
		case REQUEST_CODE_CROP: {
			performImageBack();
			break;
		}
		default:
			break;
		}
	}

	private void performImageBack() {
		String path = sdcardTempFile.getAbsolutePath();
		Bitmap bmp = BitmapFactory.decodeFile(path);
		ivIcon.setImageBitmap(bmp);

		// 更新地址
		account.setIcon(path);
		dao.updateAccount(account);

		// 添加icon上传任务
		addIconTask();
	}

	private void doOk() {
		String text = etName.getText().toString().trim();
		if (TextUtils.isEmpty(text)) {
			ToastUtil.show(getActivity(), "名字不能为空");
			return;
		}

		account.setName(text);
		dao.updateAccount(account);

		// 添加name任务
		addNameTask();

		startActivity(new Intent(getActivity(), HomeActivity.class));
		getActivity().finish();
	}

	private void addIconTask() {
		File iconFile = new File(sdcardTempFile.getAbsolutePath());

		if (!iconFile.exists()) {
			return;
		}

		// 存储到后台任务中
		String taskDir = DirUtil.getTaskDir(getActivity());
		String fileName = CommonUtil.string2MD5(account.getAccount() + "_"
				+ SystemClock.currentThreadTimeMillis());
		String path = new File(taskDir, fileName).getAbsolutePath();

		BackTask task = new BackTask();
		task.setOwner(account.getAccount());
		task.setPath(path);
		task.setState(0);
		new BackTaskDao(getActivity()).addTask(task);

		NetTask netTask = BackTaskFactory.userIconChangeTask(account.getIcon());

		try {
			// 写入到缓存
			SerializableUtil.write(netTask, path);

			// 开启后台服务
			getActivity().startService(
					new Intent(getActivity(), BackgroundService.class));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void addNameTask() {
		// 存储到后台任务中
		String taskDir = DirUtil.getTaskDir(getActivity());
		String fileName = CommonUtil.string2MD5(account.getAccount() + "_"
				+ SystemClock.currentThreadTimeMillis());
		String path = new File(taskDir, fileName).getAbsolutePath();

		BackTask task = new BackTask();
		task.setOwner(account.getAccount());
		task.setPath(path);
		task.setState(0);
		new BackTaskDao(getActivity()).addTask(task);

		NetTask netTask = BackTaskFactory.userNameChangeTask(account.getName());

		try {
			// 写入到缓存
			SerializableUtil.write(netTask, path);

			// 开启后台服务
			getActivity().startService(
					new Intent(getActivity(), BackgroundService.class));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
