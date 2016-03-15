package com.dawan.huahua;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Map;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.Toast;

import com.dawan.huahua.adapter.SelectAGridAdapter;
import com.dawan.huahua.base.BaseActivity;
import com.dawan.huahua.base.ConstantsConfig;
import com.dawan.huahua.http.HttpMultipartPost;
import com.dawan.huahua.image.APhotoActivity;
import com.dawan.huahua.image.ASelectPicActivity;
import com.dawan.huahua.image.ClipImageActivity;
import com.dawan.huahua.utils.ABimp;
import com.dawan.huahua.utils.FileUtils;
import com.dawan.huahua.utils.ParseJsonUtils;

/**
 * @author wyq 2016-03-03
 */

public class MainActivity extends BaseActivity {
	protected void onRestart() {
		super.onRestart();
		gvIndoorInit();
	}

	private Button btnSubmit;
	private GridView gvIndoor;
	private HorizontalScrollView hsvIndoor;
	private SelectAGridAdapter mSelectAGridAdapter;

	private float floatDp;
	private int picNum = 5;
	private int indoorCount = 0;
	private String imgName = "";
	private String strIndoor = "";
	private ProgressDialog mProgressDialog;// 上传进度条

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		findViewById();
		initData();
		
		initView();
		
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		btnSubmit = (Button) findViewById(R.id.btn_submit);
		gvIndoor = (GridView) findViewById(R.id.gv_lease_indoor);
		hsvIndoor = (HorizontalScrollView) findViewById(R.id.hsv_lease_indoor);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		gvIndoor.setSelector(new ColorDrawable(Color.TRANSPARENT));
		gvIndoorInit();
	}

	private void initData() {
		// TODO Auto-generated method stub
		floatDp = getResources().getDimension(R.dimen.dp);
	}

	/**
	 * 室内
	 * */
	private void gvIndoorInit() {
		mSelectAGridAdapter = new SelectAGridAdapter(this);
		mSelectAGridAdapter.setSelectedPosition(0);
		int size = 0;
		if (ABimp.bmp.size() < picNum) {
			size = ABimp.bmp.size() + 1;
		} else {
			size = ABimp.bmp.size();
		}
		LayoutParams params = gvIndoor.getLayoutParams();
		final int width = size * (int) (floatDp * 9.4f);
		params.width = width;
		gvIndoor.setLayoutParams(params);
		gvIndoor.setColumnWidth((int) (floatDp * 9.4f));
		gvIndoor.setStretchMode(GridView.NO_STRETCH);
		gvIndoor.setNumColumns(size);
		gvIndoor.setAdapter(mSelectAGridAdapter);
		gvIndoor.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				try {
					gvOnItemClick(arg2, 0);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});

		hsvIndoor.getViewTreeObserver().addOnPreDrawListener(// 绘制完毕
				new OnPreDrawListener() {
					public boolean onPreDraw() {
						hsvIndoor.scrollTo(width, 0);
						hsvIndoor.getViewTreeObserver()
								.removeOnPreDrawListener(this);
						return false;
					}
				});
	}

	private static final int I_TAKE_PICTURE = 0;// 拍照
	private static final int I_RESULT_LOAD_IMAGE = 1;// 相册选择
	private static final int I_CUT_PHOTO_REQUEST_CODE = 2;// 剪切返回

	/**
	 * GridView点击事件
	 * 
	 * @param num
	 *            (0:室内|1:户型|2:小区)
	 * */
	private void gvOnItemClick(int index, int num) {
		((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
				.hideSoftInputFromWindow(MainActivity.this.getCurrentFocus()
						.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

		switch (num) {
		case 0:
			if (index == ABimp.bmp.size()) {
				String sdcardState = Environment.getExternalStorageState();
				if (Environment.MEDIA_MOUNTED.equals(sdcardState)) {
					showSheetDialog(num);
				} else {
					Toast.makeText(getApplicationContext(), "SD卡已拔出，不能选择照片",
							Toast.LENGTH_SHORT).show();
				}
			} else {
				Intent intent = new Intent(MainActivity.this,
						APhotoActivity.class);
				intent.putExtra("ID", index);
				startActivity(intent);
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 弹出Sheet
	 * 
	 * @param num
	 *            (0:室内|1:户型|2:小区|3:封面)
	 * */
	@SuppressWarnings("deprecation")
	private void showSheetDialog(final int num) {
		View view = getLayoutInflater().inflate(
				R.layout.activity_app_photo_choose_dialog, null);

		final Dialog dialog = new Dialog(this,
				R.style.transparentFrameWindowStyle);
		dialog.setContentView(view, new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));
		Window window = dialog.getWindow();
		window.setWindowAnimations(R.style.main_menu_animstyle);
		WindowManager.LayoutParams wl = window.getAttributes();
		wl.x = 0;
		wl.y = getWindowManager().getDefaultDisplay().getHeight();
		wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
		wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
		dialog.onWindowAttributesChanged(wl);
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();
		Button btnCamera = (Button) view.findViewById(R.id.btn_to_camera);
		btnCamera.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// photo(num);
				photoCamera(num);
				dialog.dismiss();
			}
		});
		Button btnPhoto = (Button) view.findViewById(R.id.btn_to_photo);
		btnPhoto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// photoAlbum(num);
				switch (num) {
				case 0:
					openActivity(ASelectPicActivity.class);
					break;
				default:
					break;
				}
				dialog.dismiss();
			}
		});
		Button btnCancel = (Button) view.findViewById(R.id.btn_to_cancel);
		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
	}

	/**
	 * 照相
	 * */
	private void photoCamera(int num) {
		Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri
				.fromFile(new File(Environment.getExternalStorageDirectory(),
						"temp.jpg")));
		switch (num) {
		case 0:
			startActivityForResult(openCameraIntent, I_TAKE_PICTURE);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case I_TAKE_PICTURE:
			// 照相
			File temp = new File(Environment.getExternalStorageDirectory()
					+ "/temp.jpg");
			startPhotoZoomCut(Uri.fromFile(temp), 0);

			break;
		case I_RESULT_LOAD_IMAGE:
			// 选择照片
			if (data != null) {
				startPhotoZoomCut(data.getData(), 0);
			}
			break;
		case I_CUT_PHOTO_REQUEST_CODE:
			// 剪切返回
			if (data != null) {
				setPicToView(data, 0);
				gvIndoorInit();
			}
			break;

		default:
			break;
		}
	}

	/**
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	public void startPhotoZoomCut(Uri uri, int num) {
		Intent intent = new Intent(MainActivity.this, ClipImageActivity.class);
		intent.setDataAndType(uri, "image/*");
		switch (num) {
		case 0:
			startActivityForResult(intent, I_CUT_PHOTO_REQUEST_CODE);
			break;
		default:
			break;
		}
	}

	/**
	 * 保存裁剪之后的图片数据
	 * 
	 * @param picdata
	 */
	private void setPicToView(Intent picdata, int num) {

		byte[] bis = picdata.getByteArrayExtra("bitmap");
		Bitmap bitmap = BitmapFactory.decodeByteArray(bis, 0, bis.length);

		// Log.i("||", "Edit>>>>>>截取到的图片路径是 = " + path);
		if (bitmap != null) {

			imgName = System.currentTimeMillis() + ".JPEG";
			final String filepath = FileUtils.SDPATH + imgName;
			FileUtils.saveBitmap(bitmap, "" + imgName);

			switch (num) {
			case 0:
				ABimp.drr.add(filepath);
				ABimp.bmp.add(bitmap);
				break;
			default:
				break;
			}
		}
	}

	public static Bitmap getPicFromBytes(byte[] bytes,
			BitmapFactory.Options opts) {
		if (bytes != null)
			if (opts != null)
				return BitmapFactory.decodeByteArray(bytes, 0, bytes.length,
						opts);
			else
				return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		return null;
	}

	public static byte[] readStream(InputStream inStream) throws Exception {
		byte[] buffer = new byte[1024];
		int len = -1;
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		byte[] data = outStream.toByteArray();
		outStream.close();
		inStream.close();
		return data;

	}

	/**
	 * 上传图片
	 * */
	private void uploadImgOpt() {
		picUNum = 0;
		btnSubmit.setText("开始上传第1张图片");

		strIndoor = "";
		// 上传室内
		if (ABimp.drr.size() > 0) {
			indoorCount = ABimp.drr.size();
			for (String str : ABimp.drr) {
				HttpmimeUploadImg(str, 0);
			}
		}

		picUNumSum = indoorCount;
		initProgressDialog();
		mProgressDialog.setMax(picUNumSum);
		if (!mProgressDialog.isShowing()) {
			mProgressDialog.show();
		}
	}

	// 上传图片数量
	private int picUNum = 0;
	private int picUNumSum = 0;// 图片总数

	private void HttpmimeUploadImg(String imgPath, final int num) {
		String URL = ConstantsConfig.APP_IMG_API + "?api=up";
		HttpMultipartPost post = new HttpMultipartPost(this, URL, imgPath,
				new HttpMultipartPost.FileCallback() {

					@Override
					public void getHttpResult(String result) {
						// TODO Auto-generated method stub
						picUNum++;
						btnSubmit.setText("开始上传第" + picUNum + "张图片");

						mProgressDialog.setProgress(picUNum);

						Log.i("S::Upload_result 照片上传成功>>>", result + "");

						if (result != "" && result != null) {
							Map<String, String> map = ParseJsonUtils
									.jsonToMap(result);

							switch (num) {
							case 0:
								if (map.containsKey("msg")) {
									if (map.get("msg") != null
											&& !"".equals(map.get("msg"))) {
										indoorCount--;
										strIndoor += map.get("msg") + ",";
										Log.e("上传图片strIndoor::", strIndoor);
									}
								}
								break;
							default:
								break;
							}
							if (indoorCount == 0) {
								mProgressDialog.dismiss();
							}
						} else {
							switch (num) {
							case 0:
								indoorCount--;
								break;
							default:
								break;
							}
							if (indoorCount == 0) {
								mProgressDialog.dismiss();
							}
						}
					}
				});
		post.execute();
	}

	/**
	 * 初始化上传弹窗
	 * */
	private void initProgressDialog() {
		mProgressDialog = new ProgressDialog(this,
				ProgressDialog.THEME_HOLO_LIGHT);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		mProgressDialog.setMessage("正在上传图片...");
		mProgressDialog.setCancelable(false);
		// mProgressDialog.show();
	}
}
