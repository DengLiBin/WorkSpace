package com.dawan.huahua.image;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.dawan.huahua.R;
import com.dawan.huahua.adapter.AImageGridAdapter;
import com.dawan.huahua.adapter.AImageGridAdapter.TextCallback;
import com.dawan.huahua.base.AlbumHelper;
import com.dawan.huahua.model.ImageItem;
import com.dawan.huahua.utils.ABimp;
import com.dawan.huahua.utils.FileUtils;

public class AImageGridActivity extends Activity {
	public static final String EXTRA_IMAGE_LIST = "imagelist";

	List<ImageItem> dataList;
	GridView gridView;
	AImageGridAdapter adapter;//
	AlbumHelper helper;
	Button bt;
	int picNum = 5;
	TextView tvCancel;

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				Toast.makeText(AImageGridActivity.this,
						"最多选择" + picNum + "张图片", Toast.LENGTH_LONG).show();
				break;

			default:
				break;
			}
		}
	};

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_image_grid);

		helper = AlbumHelper.getHelper();
		helper.init(getApplicationContext());

		dataList = (List<ImageItem>) getIntent().getSerializableExtra(
				EXTRA_IMAGE_LIST);

		initView();
		tvCancel = (TextView) findViewById(R.id.tv_cancel);
		tvCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		bt = (Button) findViewById(R.id.bt);
		bt.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				ArrayList<String> list = new ArrayList<String>();
				Collection<String> c = adapter.map.values();
				Iterator<String> it = c.iterator();
				for (; it.hasNext();) {
					list.add(it.next());
				}

				// if (ABimp.act_bool) {
				// Intent intent = new Intent(AImageGridActivity.this,
				// FastFocusHourOneActivity.class);
				// startActivity(intent);
				// ABimp.act_bool = false;
				// }

				for (int i = 0; i < list.size(); i++) {
					if (ABimp.drr.size() < picNum) {
						ABimp.drr.add(list.get(i));

						String path = list.get(i);
						Bitmap bm;
						try {
							bm = ABimp.revitionImageSize(path);
							ABimp.bmp.add(bm);
							String newStr = path.substring(
									path.lastIndexOf("/") + 1,
									path.lastIndexOf("."))
									+ ".JPEG";
							FileUtils.saveBitmap(bm, "" + newStr);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				finish();
			}
		});
	}

	/**
	 * 初始化
	 */
	private void initView() {
		gridView = (GridView) findViewById(R.id.gridview);
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		Collections.reverse(dataList);//列表反序
		adapter = new AImageGridAdapter(AImageGridActivity.this, dataList,
				mHandler);
		gridView.setAdapter(adapter);
		adapter.setTextCallback(new TextCallback() {
			public void onListen(int count) {
				bt.setText("完成" + "(" + count + ")");
			}
		});

		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				adapter.notifyDataSetChanged();
			}

		});
	}
}
