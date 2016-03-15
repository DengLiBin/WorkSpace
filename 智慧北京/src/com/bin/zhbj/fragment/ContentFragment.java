package com.bin.zhbj.fragment;

import java.util.ArrayList;

import com.bin.zhbj.R;
import com.bin.zhbj.base.BasePager;
import com.bin.zhbj.base.impl.GovAffairsPager;
import com.bin.zhbj.base.impl.HomePager;
import com.bin.zhbj.base.impl.NewsCenterPager;
import com.bin.zhbj.base.impl.SettingPager;
import com.bin.zhbj.base.impl.SmartServicePager;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

/**
 * 主页内容
 * @author Administrator
 *
 */
public class ContentFragment extends BaseFragment {
	
	private RadioGroup rgGroup;
	private ViewPager mViewPager;
	private ArrayList<BasePager> mPagerList;
	@Override
	public View initView() {
		View content_view=View.inflate(mActivity, R.layout.fragment_content, null);
		rgGroup=(RadioGroup) content_view.findViewById(R.id.rg_group);
		mViewPager=(ViewPager) content_view.findViewById(R.id.vp_content);
		
		return content_view;
	}
	@Override
	public void initData() {
		rgGroup.check(R.id.rb_home);//默认勾选首页
		//初始化5个页面
		mPagerList = new ArrayList<BasePager>();
		/*
		for(int i=0;i<5;i++){
			BasePager pager=new BasePager(mActivity);
			mPagerList.add(pager);
		}
		*/
		mPagerList.add(new HomePager(mActivity));
		mPagerList.add(new NewsCenterPager(mActivity));
		mPagerList.add(new SmartServicePager(mActivity));
		mPagerList.add(new GovAffairsPager(mActivity));
		mPagerList.add(new SettingPager(mActivity));
		//ViewPager设置适配器
		mViewPager.setAdapter(new ContentAdapter());
		
		//给radioGroup添加监听器，监听各个radioButton
		rgGroup.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
					switch(checkedId){
					case R.id.rb_home:
						mViewPager.setCurrentItem(0,false);//显示第0个页面(首页),去掉切换动画效果
						break;
					case R.id.rb_news:
						mViewPager.setCurrentItem(1,false);//显示第1个页面(新闻)
						break;
					case R.id.rb_smartservice:
						mViewPager.setCurrentItem(2,false);//显示第2个页面(生活)
						break;
					case R.id.rb_govaffairs:
						mViewPager.setCurrentItem(3,false);//显示第3个页面(政务)
						break;
					case R.id.rb_setting:
						mViewPager.setCurrentItem(4,false);//显示第4个页面(设置)
						break;
						
					}
			}
			
		});
		mViewPager.setOnPageChangeListener(new OnPageChangeListener(){

			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
//初始化5个ViewPager的数据
			@Override
			public void onPageSelected(int arg0) {
				mPagerList.get(arg0).initData();//哪个页面被选中就初始化哪个页面的数据
			}
			
		});
	}
	
	class ContentAdapter extends PagerAdapter{

		@Override
		public int getCount() {
			return mPagerList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0==arg1;
		}
		@Override
		//返回一个view
		public Object instantiateItem(ViewGroup container, int position) {
			BasePager pager=mPagerList.get(position);
			container.addView(pager.mRootView);
			if(position==0){
				pager.initData();
			}
			//pager.initData();//初始化数据......不要放在此处，否则会预加载下一个页面
			return pager.mRootView;
		}
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View)object);
		}
		
	}
	/**
	 * 获取pager
	 * @param position
	 * @return
	 */
	public BasePager getBasePager(int position){
		return mPagerList.get(position);
	}

}





