package com.bin.zhbj.utils.bitmap;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

public class NetCacheUtils {
	private LocalCacheUtils mLocalCacheUtils;
	private MemoryCacheUtils mMemoryCacheUtils;
	public NetCacheUtils(LocalCacheUtils localCacheUtils,MemoryCacheUtils memoryCacheUtils){
		this.mLocalCacheUtils=localCacheUtils;
		this.mMemoryCacheUtils=memoryCacheUtils;
	}
	/**
	 * url:网络图片
	 * ivPic：ImageView
	 * 将网络图片设置给一个ImageView
	 */
	public void getBitmapFromNet(ImageView ivPic,String url){
		new BitmapTask().execute(ivPic,url);
	}
	
	
	/**
	 *第一个泛型：参数类型； 第二个泛型：更新进度； 第三个泛型：onPostExecute的返回结果
	 *
	 */
	class BitmapTask extends AsyncTask<Object,Void,Bitmap>{

		private ImageView ivPic;
		private String url;
		/**
		 * 后台耗时方法在此执行，子线程
		 */
		@Override
		protected Bitmap doInBackground(Object... params) {
			ivPic = (ImageView) params[0];//new BitmapTask().execute(ivPic,url);
			url = (String) params[1];//new BitmapTask().execute(ivPic,url);
			ivPic.setTag(url);//将url和ImageView绑定
			return downloadBitmap(url);
		}
		/**
		 * 更新进度，主线程
		 */
		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
		}
		/**
		 * 耗时方法结束后，执行该方法，主线程
		 */
		@Override
		protected void onPostExecute(Bitmap result) {//result就是doInBackground()方法的返回值
			if(result!=null){
				String tagurl=(String) ivPic.getTag();
				if(url.equals(tagurl)){//确保图片设定给了正确的imageview
					System.out.println("从网络缓存读取图片");
					ivPic.setImageBitmap(result);
					mMemoryCacheUtils.setBitmapToMemory(url, result);//将图片存到内存中
					System.out.println("将图片保存到了内存中");
					mLocalCacheUtils.setBitmapToLocal(url,result);//将图片保存在本地
					System.out.println("将图片保存到了本地");
					
				}
				
			}	
		}
		
	}
	/**
	 * 下载图片
	 */
	private Bitmap downloadBitmap(String url){
		HttpURLConnection conn=null;
		try{
			conn=(HttpURLConnection)new URL(url).openConnection();
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(5000);
			conn.setRequestMethod("GET");
			conn.connect();
			int responseCode=conn.getResponseCode();
			if(responseCode==200){
				InputStream inputStream=conn.getInputStream();
				//图片压缩处理,可以节省内存
				BitmapFactory.Options option=new BitmapFactory.Options();
				option.inSampleSize=2;//宽高都压缩为原来的二分之一，大小为原来的1/4，该值需要根据要展示的图片的大小来确定
				option.inPreferredConfig=Bitmap.Config.RGB_565;//设置图片的格式
				Bitmap bitmap=BitmapFactory.decodeStream(inputStream,null,option);
				return bitmap;
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			conn.disconnect();
		}
		return null;
	}
}
