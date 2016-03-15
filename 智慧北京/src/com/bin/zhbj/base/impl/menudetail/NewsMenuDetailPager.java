package com.bin.zhbj.base.impl.menudetail;

import java.util.ArrayList;

import com.bin.zhbj.R;
import com.bin.zhbj.base.BaseMenuDetailPager;
import com.bin.zhbj.base.TabDetailPager;
import com.bin.zhbj.domain.NewsData.NewsTabData;
import com.viewpagerindicator.TabPageIndicator;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class NewsMenuDetailPager extends BaseMenuDetailPager {
	
	private ViewPager mViewPager;
	private ArrayList<TabDetailPager> mPagerList;
	private ArrayList<NewsTabData> mNewsTabData;//新闻详情页的页签数据
	private TabPageIndicator mIndicator;
	private ImageButton mImgBtn;
	public NewsMenuDetailPager(Activity activity,ArrayList<NewsTabData> children) {
		super(activity);
		mNewsTabData=children;
	}

	@Override
	public View initView() {
		View view=View.inflate(mActivity, R.layout.news_menu_detail, null);
		mViewPager=(ViewPager) view.findViewById(R.id.vp_menu_detail);
		//初始化自定义控件
		mIndicator = (TabPageIndicator) view.findViewById(R.id.indicator);
		mImgBtn = (ImageButton) view.findViewById(R.id.btn_next);
		mImgBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int currentItem=mViewPager.getCurrentItem();
				mViewPager.setCurrentItem(++currentItem);
			}
		});
		return view;
	}
	
//NewsCenterPager中调用
	@Override
	public void initData() {
		mPagerList=new ArrayList<TabDetailPager>();
		
		for(int i=0;i<mNewsTabData.size();i++){
			TabDetailPager pager=new TabDetailPager(mActivity,mNewsTabData.get(i));
			mPagerList.add(pager);//将新闻详情页页中的页签添加到集合中
		}
		mViewPager.setAdapter(new MenuDetailAdapter());
		
		mIndicator.setViewPager(mViewPager);//必须在setAdapter之后才能setViewPager
	}
	class MenuDetailAdapter extends PagerAdapter{
		//给indicator设置标题，为了使indicator出现理想的效果，在清单文件中为Activity设置一个主题样式：android:theme="@style/Theme.PageIndicatorDefaults"
		@Override
		public CharSequence getPageTitle(int position) {
			return mNewsTabData.get(position).title;
		}
		@Override
		public int getCount() {
			return mPagerList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0==arg1;
		}
		@Override
		public Object instantiateItem(ViewGroup container, int psition) {
			TabDetailPager pager=mPagerList.get(psition);
			container.addView(pager.mRootView);
			
			pager.initData();//初始化数据（也可以在选中时再初始化数据）
			return pager.mRootView;
		}
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View)object);
		}
		
	}
	
}
