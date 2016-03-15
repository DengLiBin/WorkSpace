package com.itheima.xiaozhinews;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.itheima.xiaozhinews.domain.News;
import com.loopj.android.image.SmartImageView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.util.Xml;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {

	List<News> newsList;
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			ListView lv = (ListView) findViewById(R.id.lv);
			lv.setAdapter(new MyAdapter());
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		getNewsInfo();
//		ListView lv = (ListView) findViewById(R.id.lv);
//		//要保证在设置适配器时，新闻xml文件已经解析完毕了
//		lv.setAdapter(new MyAdapter());
	}

	class MyAdapter extends BaseAdapter{

		//得到模型层中元素的数量，用来确定listview需要有多少个条目
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return newsList.size();
		}
		@Override
		//返回一个View对象，作为listview的条目显示至界面
		public View getView(int position, View convertView, ViewGroup parent) {

			News news = newsList.get(position);
			View v = null;
			ViewHolder mHolder;
			if(convertView == null){
				v = View.inflate(MainActivity.this, R.layout.item_listview, null);
				mHolder = new ViewHolder();
				//把布局文件中所有组件的对象封装至ViewHolder对象中
				mHolder.tv_title = (TextView) v.findViewById(R.id.tv_title);
				mHolder.tv_detail = (TextView) v.findViewById(R.id.tv_detail);
				mHolder.tv_comment = (TextView) v.findViewById(R.id.tv_comment);
				mHolder.siv = (SmartImageView) v.findViewById(R.id.iv);
				//把ViewHolder对象封装至View对象中
				v.setTag(mHolder);
			}
			else{
				v = convertView;
				mHolder = (ViewHolder) v.getTag();
			}
			//给三个文本框设置内容
			mHolder.tv_title.setText(news.getTitle());
			
			mHolder.tv_detail.setText(news.getDetail());
			
			mHolder.tv_comment.setText(news.getComment() + "条评论");
			
			//给新闻图片imageview设置内容
			mHolder.siv.setImageUrl(news.getImageUrl());
			return v;
		}
		
		class ViewHolder{
			//条目的布局文件中有什么组件，这里就定义什么属性
			TextView tv_title;
			TextView tv_detail;
			TextView tv_comment;
			SmartImageView siv;
		}
		
		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}
	}
	private void getNewsInfo() {
		Thread t = new Thread(){
			@Override
			public void run() {
				String path = "http://192.168.13.13:8080/news.xml";
				try {
					URL url = new URL(path);
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setRequestMethod("GET");
					conn.setConnectTimeout(5000);
					conn.setReadTimeout(5000);
					//发送http GET请求，获取相应码
					if(conn.getResponseCode() == 200){
						InputStream is = conn.getInputStream();
						//使用pull解析器，解析这个流
						parseNewsXml(is);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		t.start();
	}
	
	private void parseNewsXml(InputStream is) {
		XmlPullParser xp = Xml.newPullParser();
		try {
			xp.setInput(is, "utf-8");
			//对节点的事件类型进行判断，就可以知道当前节点是什么节点
			int type = xp.getEventType();
			News news = null;
			while(type != XmlPullParser.END_DOCUMENT){
				switch (type) {
				case XmlPullParser.START_TAG:
					if("newslist".equals(xp.getName())){
						newsList = new ArrayList<News>();
					}
					else if("news".equals(xp.getName())){
						news = new News();
					}
					else if("title".equals(xp.getName())){
						String title = xp.nextText();
						news.setTitle(title);
					}
					else if("detail".equals(xp.getName())){
						String detail = xp.nextText();
						news.setDetail(detail);
					}
					else if("comment".equals(xp.getName())){
						String comment = xp.nextText();
						news.setComment(comment);
					}
					else if("image".equals(xp.getName())){
						String image = xp.nextText();
						news.setImageUrl(image);
					}
					break;
				case XmlPullParser.END_TAG:
					if("news".equals(xp.getName())){
						newsList.add(news);
					}
					break;

				}
				//解析完当前节点后，把指针移动至下一个节点，并返回它的事件类型
				type = xp.next();
			}
			
			//发消息，让主线程设置listview的适配器，如果消息不需要携带数据，可以发送空消息
			handler.sendEmptyMessage(1);
			
//			for (News n : newsList) {
//				System.out.println(n.toString());
//			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
