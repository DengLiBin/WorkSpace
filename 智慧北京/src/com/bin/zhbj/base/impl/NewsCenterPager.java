package com.bin.zhbj.base.impl;

import java.util.ArrayList;

import com.bin.zhbj.MainActivity;
import com.bin.zhbj.base.BaseMenuDetailPager;
import com.bin.zhbj.base.BasePager;
import com.bin.zhbj.base.impl.menudetail.InteractMenuDetailPager;
import com.bin.zhbj.base.impl.menudetail.NewsMenuDetailPager;
import com.bin.zhbj.base.impl.menudetail.PhotoMenuDetailPager;
import com.bin.zhbj.base.impl.menudetail.TopicMenuDetailPager;
import com.bin.zhbj.domain.NewsData;
import com.bin.zhbj.domain.NewsData.NewMenuData;
import com.bin.zhbj.fragment.LeftMenuFragment;
import com.bin.zhbj.global.GlobalContacts;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.app.Activity;
import android.view.View;
import android.widget.Toast;

/**
 * @author Administrator
 *
 */
public class NewsCenterPager extends BasePager {
	public ArrayList<BaseMenuDetailPager> mPagers;//四个菜单详情页的集合
	private NewsData mNewsdata;
	public NewsCenterPager(Activity activity) {
		super(activity);
	}

//ContentFragment中调用
	@Override
	public void initData() {
		super.setSlidingMenuEnable(true);
		//tvTitle.setText("新闻中心");
		/*
		TextView text=new TextView(mActivity);
		text.setText("新闻");
		text.setTextColor(Color.RED);
		text.setTextSize(25);
		text.setGravity(Gravity.CENTER);
		flContent.addView(text);//将textView添加到frameLayout中
		*/
		getDataFromServer();
		
		
	}
	/**
	 * 从服务器获取数据
	 */
	private void getDataFromServer(){
		HttpUtils utils=new HttpUtils();
		
		utils.send(HttpMethod.GET, GlobalContacts.CATEGORIES, new RequestCallBack<String>(){
			//访问成功
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result=(String)responseInfo.result;
				System.out.println("返回结果："+result);
				parseData(result);
			}
			//访问失败
			@Override
			public void onFailure(HttpException error, String msg) {
				Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
				error.printStackTrace();
			}
			
		});
	}
	
	/**
	 * 解析网络数据
	 * @param result
	 */
	protected void parseData(String result){
		Gson gson=new Gson();
		mNewsdata = gson.fromJson(result, NewsData.class);
		System.out.println("解析结果："+mNewsdata);
		MainActivity mainUi=(MainActivity) mActivity;
		
		//获取侧边栏Fragment对象
		LeftMenuFragment leftMenuFragment=mainUi.getLeftMenuFragment();
		//设置侧边栏Fragment的数据
		leftMenuFragment.setMenuData(mNewsdata);
		
		//准备4个菜单详情页
		mPagers=new ArrayList<BaseMenuDetailPager>();
		mPagers.add(new NewsMenuDetailPager(mActivity,mNewsdata.data.get(0).children));//get(0)表示获取的是新闻详情页（第一个页面）
		mPagers.add(new TopicMenuDetailPager(mActivity));
		mPagers.add(new PhotoMenuDetailPager(mActivity, btnPhoto));
		mPagers.add(new InteractMenuDetailPager(mActivity));
		
		setCurrentMenuDetailPage(0);//设置当前页面为菜单新闻详情页
	}
	/**
	 * 设置当前菜单详情页
	 */
	public void setCurrentMenuDetailPage(int positon){
		BaseMenuDetailPager pager=mPagers.get(positon);
		flContent.removeAllViews();//移除前面的布局
		flContent.addView(pager.mRootView);//flContent,是基类中的FrameLayout，在构造函数中就初始化了，view会叠加，应该把之前的view移除
		
		NewMenuData menuData=mNewsdata.data.get(positon);
		tvTitle.setText(menuData.title);
		pager.initData();//初始化当前页面的数据
		if(pager instanceof PhotoMenuDetailPager){//如果是组图页，就显示组图切换按钮
			btnPhoto.setVisibility(View.VISIBLE);
		}else{
			btnPhoto.setVisibility(View.GONE);
		}
	}

}
