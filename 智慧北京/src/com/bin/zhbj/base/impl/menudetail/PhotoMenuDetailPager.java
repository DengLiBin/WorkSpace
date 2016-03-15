package com.bin.zhbj.base.impl.menudetail;

import java.util.ArrayList;

import com.bin.zhbj.R;
import com.bin.zhbj.base.BaseMenuDetailPager;
import com.bin.zhbj.domain.PhotosData;
import com.bin.zhbj.domain.PhotosData.PhotoInfo;
import com.bin.zhbj.global.GlobalContacts;
import com.bin.zhbj.utils.CacheUtils;
import com.bin.zhbj.utils.bitmap.MyBitmapUtils;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class PhotoMenuDetailPager extends BaseMenuDetailPager {

	private ArrayList<PhotoInfo> mPhotoList;
	private GridView gvPhoto;
	private ListView lvPhoto;
	private PhotoAdapter mAdapter;
	private ImageButton btnPhoto;
	public PhotoMenuDetailPager(Activity activity,ImageButton btnPhoto) {
		super(activity);
		this.btnPhoto=btnPhoto;
		btnPhoto.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				changeDisplay();
			}
			
		});
	}

	@Override
	public View initView() {
		View view=View.inflate(mActivity, R.layout.menu_photo_pager, null);
		lvPhoto = (ListView) view.findViewById(R.id.lv_photo);
		gvPhoto = (GridView) view.findViewById(R.id.gv_photo);
		return view;
	}
	@Override
	public void initData() {
		String cache=CacheUtils.getCache(mActivity, GlobalContacts.PHOTOS_URL);
		if(!TextUtils.isEmpty(cache)){
			 parseData(cache);
		}
		getDataFromServer();
	}
	private void getDataFromServer() {
		HttpUtils http=new HttpUtils();
		http.send(HttpMethod.GET, GlobalContacts.PHOTOS_URL, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result=responseInfo.result;
				System.out.println("图片URL返回结果："+result);
				parseData(result);
				//设置缓存
				CacheUtils.setCache(mActivity,  GlobalContacts.PHOTOS_URL, result);
			}


			@Override
			public void onFailure(HttpException error, String msg) {
				Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT);
				error.printStackTrace();
			}
		});
	}
	
	private void parseData(String result) {
			Gson gson=new Gson();
			PhotosData datas=gson.fromJson(result,PhotosData.class);
			mPhotoList = datas.data.news;//获取组图集合
			if(mPhotoList!=null){
				mAdapter = new PhotoAdapter();
				lvPhoto.setAdapter(mAdapter);
				gvPhoto.setAdapter(mAdapter);
			}
			
	}
	class PhotoAdapter extends BaseAdapter{

		private ViewHolder holder;
		private BitmapUtils utils;
		private MyBitmapUtils myUtils;//自定义的
		public PhotoAdapter(){
			myUtils=new MyBitmapUtils();
			utils = new BitmapUtils(mActivity);
			utils.configDefaultLoadingImage(R.drawable.news_pic_default);//默认加载的图片资源
		}
		@Override
		public int getCount() {
			return mPhotoList.size();
		}

		@Override
		public PhotoInfo getItem(int position) {
			return mPhotoList.get(position);
		}

		@Override
		public long getItemId(int  position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView==null){
				convertView=View.inflate(mActivity, R.layout.list_photo_item, null);
				holder = new ViewHolder();
				holder.ivPic=(ImageView) convertView.findViewById(R.id.iv_pic);
				holder.tvTitle=(TextView) convertView.findViewById(R.id.tv_title);
				convertView.setTag(holder);
			}else{
				holder=(ViewHolder) convertView.getTag();
			}
			PhotoInfo item=getItem(position);
			holder.tvTitle.setText(item.title);
			myUtils.display(holder.ivPic, item.listimage);
			return convertView;
		}
		
	}
	static class ViewHolder{
		public TextView tvTitle;
		public ImageView ivPic;
	}
	
	private boolean isListDisplay=true;//是否是列表展示
	/**
	 * 切换展示方式
	 */
	private void changeDisplay(){
		if(isListDisplay){
			isListDisplay=false;
			lvPhoto.setVisibility(View.GONE);
			gvPhoto.setVisibility(View.VISIBLE);
			btnPhoto.setImageResource(R.drawable.icon_pic_list_type);
		}else{
			isListDisplay=true;
			lvPhoto.setVisibility(View.VISIBLE);
			gvPhoto.setVisibility(View.GONE);
			btnPhoto.setImageResource(R.drawable.icon_pic_grid_type);
		}
	}
}
