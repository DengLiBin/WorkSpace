package com.bin.zhbj.base;

import java.util.ArrayList;

import com.bin.zhbj.NewsDetailActivity;
import com.bin.zhbj.R;
import com.bin.zhbj.domain.NewsData;
import com.bin.zhbj.domain.NewsData.NewsTabData;
import com.bin.zhbj.domain.NewsDetailData;
import com.bin.zhbj.domain.NewsDetailData.TabNewsData;
import com.bin.zhbj.domain.NewsDetailData.TopNewsData;
import com.bin.zhbj.global.GlobalContacts;
import com.bin.zhbj.utils.CacheUtils;
import com.bin.zhbj.utils.PrefUtils;
import com.bin.zhbj.utils.bitmap.MyBitmapUtils;
import com.bin.zhbj.view.RefreshListView;
import com.bin.zhbj.view.RefreshListView.OnRefreshListener;
import com.bin.zhbj.view.TopNewsViewPager;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.viewpagerindicator.CirclePageIndicator;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 页签详情页
 * @author Administrator
 *
 */
public class TabDetailPager extends BaseMenuDetailPager implements OnPageChangeListener {
	private NewsTabData mTabData;
	private TextView text;
	private String mUrl;
	private NewsDetailData mNewsDetailData;
	private TopNewsViewPager mViewPager;
	private BitmapUtils utils;
	private CirclePageIndicator mIndicator;//viewpager切换时的位置指示器（指示当前页）
	private TextView tvTitle;//头条新闻页标题
	private RefreshListView lvList;//新闻页列表
	ArrayList<TabNewsData> mNewsList;//新闻页列表集合
	private String mMoreUrl;//更多页面地址
	private NewsAdapter mNewsAdapter;
	
	private Handler mHandler;
	public TabDetailPager(Activity activity,NewsTabData newsTabData) {
		super(activity);
		mTabData=newsTabData;
		mUrl=GlobalContacts.URL+mTabData.url;
		System.out.println("URL:"+mUrl);
	}

	@Override
	public View initView() {
		View view=View.inflate(mActivity, R.layout.tab_detail_pager, null);
		//加载头布局
		View headView=View.inflate(mActivity, R.layout.list_header_topnews, null);
		mViewPager=(TopNewsViewPager) headView.findViewById(R.id.vp_news);
		tvTitle=(TextView) headView.findViewById(R.id.tv_title);
		mIndicator=(CirclePageIndicator) headView.findViewById(R.id.indicator);//ViewPager当前页的指示器（相当于引导页的那几个小点）
		lvList=(RefreshListView) view.findViewById(R.id.lv_list);
		lvList.addHeaderView(headView);//在该listView前面添加一个头View,上下滑动ListView时，头View也会滑动
		//设置监听器（该方法已经在RefreshListView这个类中定义了，监听器也定义了，需要复写监听器的方法）
		lvList.setOnRefreshListener(new OnRefreshListener(){

			@Override
			public void onRefresh() {
				getDataFromServer();//再次从服务器获取数据
			}

			@Override
			public void onLoadingMore() {
				if(mMoreUrl==null){
					Toast.makeText(mActivity, "最后一页了", Toast.LENGTH_SHORT).show();
					lvList.onRefreshComplete(); //隐藏脚布局
					
					
				}else{
					getMoreDataFromServer();
				}
			}
			
		});
		
		lvList.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				System.out.println("被点击："+position);
				//将被点击的Item的id记录到本地
				String ids=PrefUtils.getString(mActivity, "read_ids", "");//不存在就返回空字符串
				String readId=mNewsList.get(position).id;
				System.out.println("readId:"+readId);
				System.out.println("ids:"+ids);
				if(!ids.contains(readId)){//为了避免重复添加，需要判断一下
					ids=ids+readId+",";
					PrefUtils.setString(mActivity, "read_ids", ids);
				}
				TextView tvTitle=(TextView) view.findViewById(R.id.tv_title);//这里的View就是被点击的布局界面
				tvTitle.setTextColor(Color.GRAY);//将被点的Item的title颜色改为灰色，不需要刷新整个adapter，提高性能
				//mNewsAdapter.notifyDataSetChanged();//刷新Adapter,会重新调用Adapter的几个方法
				
				Intent intent=new Intent();
				intent.setClass(mActivity, NewsDetailActivity.class);
				intent.putExtra("url", mNewsList.get(position).url);
				mActivity.startActivity(intent);//跳转到新闻详情页
			}
			
		});
		return view;
	}
	
	
//NewsMenuDetailPager中调用
	@Override
	public void initData() {
		String cache =CacheUtils.getCache(mActivity, mUrl);
		if(!TextUtils.isEmpty(cache)){//如果缓存存在，直接解析数据，不需要访问网络
			System.out.println("从本地缓存解析数据");
			parseData(cache, false);
		}
			getDataFromServer();//从网络获取数据（不管本地有无缓存都从网络获取数据）	
    }
   
		
	/**
	 * 从服务器获取数据
	 */
	private void getDataFromServer(){
		HttpUtils utils=new HttpUtils();
		utils.send(HttpMethod.GET, mUrl, new RequestCallBack<String>() {
			//访问成功
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result=(String)responseInfo.result;
				System.out.println("页签详情页返回结果："+result);
				CacheUtils.setCache(mActivity,mUrl,result);
				parseData(result,false);//不是加载更多数据
				
				lvList.onRefreshComplete();//获取数据完成，隐藏刷新控件
			}
			//访问失败
			@Override
			public void onFailure(HttpException error, String msg) {
				Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
				error.printStackTrace();
				
				lvList.onRefreshComplete();
			}
				
		});
	}
	/**
	 * 加载更多数据
	 */
	private void getMoreDataFromServer(){
		HttpUtils utils=new HttpUtils();
		utils.send(HttpMethod.GET, mMoreUrl, new RequestCallBack<String>() {
			//访问成功
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result=(String)responseInfo.result;
				System.out.println("页签详情页返回结果："+result);
				parseData(result,true);//加载更多数据
				
				lvList.onRefreshComplete();//获取数据完成，隐藏刷新控件
			}
			//访问失败
			@Override 
			public void onFailure(HttpException error, String msg) {
				Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
				error.printStackTrace();
				
				lvList.onRefreshComplete();
			}
				
		});
	}
	/**
	 * 用Gson解析数据
	 * @param string
	 */
	protected void parseData(String result,boolean isMore) {//isMore表示是否是加载下一页
		Gson gson=new Gson();
		mNewsDetailData = gson.fromJson(result, NewsDetailData.class);
		
		
		System.out.println("页签详情页解析结果:"+mNewsDetailData);
		System.out.println("页签数据:"+mNewsList);
		
		//mViewPager.setOnPageChangeListener(this);
		
		String more=mNewsDetailData.data.more;//更多页面地址
		if(!TextUtils.isEmpty(more)){//判断字符串是否是空和空字符串
			mMoreUrl = GlobalContacts.URL+more;
		}else{
			mMoreUrl=null;
		}
		if(isMore==false){
			mNewsList=mNewsDetailData.data.news;
			if(mNewsDetailData.data.topnews!=null){
				mViewPager.setAdapter(new TopNewsAdapter());
				mIndicator.setViewPager(mViewPager);
				mIndicator.setSnap(true);//快照显示
				mIndicator.setOnPageChangeListener(this);
				mIndicator.onPageSelected(0);//让指示器重新定位到第一个点
			}
			if(mNewsList!=null){
				mNewsAdapter = new NewsAdapter();
				lvList.setAdapter(mNewsAdapter);
			}
			
			//ViewPager自动轮播
			if(mHandler==null){
				mHandler=new Handler(){
					public void handleMessage(android.os.Message msg){//接收信息
						System.out.println("handler.....");
						int currentItem=mViewPager.getCurrentItem();
						if(currentItem<mNewsDetailData.data.topnews.size()-1){
							currentItem++;
						}else{
							currentItem=0;
						}
						
						mViewPager.setCurrentItem(currentItem);
						mHandler.sendEmptyMessageDelayed(0, 3000);//延迟3秒再发送一个信息（循环）
					}
				};
				mHandler.sendEmptyMessageDelayed(0, 3000);//延迟3秒发送一个信息
				
			}
		}else{//加载下一页，将数据 追加到原来的集中中
			ArrayList<TabNewsData> moreNews=mNewsDetailData.data.news;
			mNewsList.addAll(moreNews);
			mNewsAdapter.notifyDataSetChanged();//刷新adapter
		}
		
		
		
	}
	
	class TopNewsAdapter extends PagerAdapter{
		public TopNewsAdapter(){
			utils = new BitmapUtils(mActivity);
		}
		@Override
		public int getCount() {
			return mNewsDetailData.data.topnews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0==arg1;
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ImageView image=new ImageView(mActivity);
			//image.setImageResource(R.drawable.topnews_item_default);//默认的图片
			image.setScaleType(ScaleType.FIT_XY);//基于空间大小填充图片
			TopNewsData topNewsData = mNewsDetailData.data.topnews.get(position);
			if(position==0){
				tvTitle.setText(topNewsData.title);
			}
			
			utils.display(image, topNewsData.topimage);//用BitmapUtils给imageView添加图片，topNewsData.topimage：图片的URI
			container.addView(image);
			
			//按住ViewPager时，轮播暂停，设置一个触摸监听，一般情况下最好设置点击监听，为了避免冲突，两个最好只设置一个
			image.setOnTouchListener(new OnTouchListener(){

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					switch(event.getAction()){
					case MotionEvent.ACTION_DOWN:
						mHandler.removeCallbacksAndMessages(null);//删除Handler中所有消息
						/*
						 * mHandler.postDelayed(new Runnable(){

							@Override
							public void run() {
								System.out.println("延迟3秒要做的事情（在主线程中）");
							}
							
						}, 3000);
						*/
						break;
					case MotionEvent.ACTION_CANCEL://动作取消（如：按下时滑动了一下）
						mHandler.sendEmptyMessageDelayed(0, 3000);//继续发消息
						break;
					case MotionEvent.ACTION_UP:
						mHandler.sendEmptyMessageDelayed(0, 3000);//继续发消息
						break;
					
					}
					return true;
				}
				
			});
			return image;
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View)object);
		}
		
	}
	
	/**
	 * 新闻列表的适配器
	 * @author Administrator
	 *
	 */
	class NewsAdapter extends BaseAdapter{
		//private BitmapUtils utils;
		private MyBitmapUtils utils;//用自己的BitmapUtils，实际开发时还是用BitmapUtils
		public NewsAdapter(){
			//utils=new BitmapUtils(mActivity);
			utils=new MyBitmapUtils();
			//utils.configDefaultLoadingImage(R.drawable.pic_item_list_default);//加载图片时显示的默认图片
		}
		@Override
		public int getCount() {
			return mNewsList.size();
		}

		@Override
		public TabNewsData getItem(int position) {
			return mNewsList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if(convertView==null){
				convertView=View.inflate(mActivity, R.layout.list_news_item,null);
				holder=new ViewHolder();
				holder.ivPic=(ImageView) convertView.findViewById(R.id.iv_pic);
				holder.tvTitle=(TextView) convertView.findViewById(R.id.tv_title);
				holder.tvDate=(TextView) convertView.findViewById(R.id.tv_date);
				
				convertView.setTag(holder);
			}else{
				holder=(ViewHolder) convertView.getTag();
			}
			TabNewsData item=getItem(position);
			holder.tvTitle.setText(item.title);
			holder.tvDate.setText(item.pubdate);
			utils.display(holder.ivPic, item.listimage);//给ImageView添加图片
			
			String ids=PrefUtils.getString(mActivity, "read_ids", "");
			if(ids.contains(getItem(position).id)){//判断该id的Item是否是已被点击过
				holder.tvTitle.setTextColor(Color.GRAY);
			}else{
				holder.tvTitle.setTextColor(Color.BLACK);
			}
			return convertView;
		}
		
	}
	static class ViewHolder{
		public TextView tvTitle;
		public TextView tvDate;
		public ImageView ivPic;
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		
	}

	@Override
	public void onPageSelected(int arg0) {
		TopNewsData topNewsData=mNewsDetailData.data.topnews.get(arg0);
		tvTitle.setText(topNewsData.title);
	}
}
