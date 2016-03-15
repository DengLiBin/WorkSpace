package com.dawan.huahua.adapter;

import com.dawan.huahua.R;
import com.dawan.huahua.R.drawable;
import com.dawan.huahua.R.id;
import com.dawan.huahua.R.layout;
import com.dawan.huahua.utils.ABimp;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;

public class SelectAGridAdapter extends BaseAdapter {
	private LayoutInflater listContainer;
	private Context mContext;
	private int selectedPosition = -1;
	private boolean shape;

	public boolean isShape() {
		return shape;
	}

	public void setShape(boolean shape) {
		this.shape = shape;
	}

	public class ViewHolder {
		public ImageView image;
		public Button bt;
	}

	public SelectAGridAdapter(Context context) {
		this.mContext = context;
		listContainer = LayoutInflater.from(context);
	}

	public int getCount() {
		if (ABimp.bmp.size() < 5) {
			return ABimp.bmp.size() + 1;
		} else {
			return ABimp.bmp.size();
		}
	}

	public Object getItem(int arg0) {

		return null;
	}

	public long getItemId(int arg0) {

		return 0;
	}

	public void setSelectedPosition(int position) {
		selectedPosition = position;
	}

	public int getSelectedPosition() {
		return selectedPosition;
	}

	/**
	 * ListView Item设置
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		final int sign = position;
		// 自定义视�?
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			// 获取list_item布局文件的视�?

			convertView = listContainer.inflate(
					R.layout.activity_app_pic_grid_item, null);

			// 获取控件对象
			holder.image = (ImageView) convertView
					.findViewById(R.id.item_grida_image);
			holder.bt = (Button) convertView.findViewById(R.id.item_grida_bt);
			// 设置控件集到convertView
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (position == ABimp.bmp.size()) {
			holder.image.setImageBitmap(BitmapFactory.decodeResource(
					mContext.getResources(), R.drawable.icon_addpic_unfocused));
			holder.bt.setVisibility(View.GONE);
			// 限定能上传几张图�?
			if (position == 5) {
				holder.image.setVisibility(View.GONE);
			}
		} else {
			holder.image.setImageBitmap(ABimp.bmp
					.get(position));
			holder.bt.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					ABimp.bmp.get(sign).recycle();
					ABimp.bmp.remove(sign);
					ABimp.drr.remove(sign);

					notifyDataSetChanged();
				}
			});
		}

		return convertView;
	}
}
