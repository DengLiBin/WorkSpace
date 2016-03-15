package com.bin.picturns;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	private ViewPager viewPager;
	private TextView tv_intro;
	private LinearLayout dot_layout;
	private ArrayList<Ad> list=new ArrayList<Ad>();
	private Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg){
			viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
			handler.sendEmptyMessageDelayed(0, 4000);
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		
		initListener();
		initData();
		
		
	}
	private void initView(){
		setContentView(R.layout.activity_main);
		viewPager = (ViewPager) findViewById(R.id.viewPager);
		tv_intro = (TextView) findViewById(R.id.tv_intro);
		dot_layout = (LinearLayout) findViewById(R.id.dot_layout);
	}
	@SuppressWarnings("deprecation")
	private void initListener(){
		viewPager.setOnPageChangeListener(new OnPageChangeListener(){

			@Override
			public void onPageScrollStateChanged(int arg0) {
				updateIntro();
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}
	private void initData(){
		list.add(new Ad(R.drawable.a,"功利不低俗，我就不能低俗"));
		list.add(new Ad(R.drawable.b, "朴树又回来了，再唱经典老歌引百万人同唱啊"));
		list.add(new Ad(R.drawable.c, "揭秘北京电影如何升级"));
		list.add(new Ad(R.drawable.d, "乐视网TV版大放送"));
		list.add(new Ad(R.drawable.e, "热血屌丝的反杀"));
		initDots();
		viewPager.setAdapter(new MyPageAdapter());
		handler.sendEmptyMessageDelayed(0, 4000);
		updateIntro();
	}
	
	/**
	 * 更新显示的文本和小点的颜色
	 */
	private void updateIntro(){
		int currentPage=viewPager.getCurrentItem()%list.size();
		tv_intro.setText(list.get(currentPage).getIntro());
		
		for(int i=0;i<dot_layout.getChildCount();i++){//遍历所有的点
			dot_layout.getChildAt(i).setEnabled(i==currentPage);//若果当前点和当前页相等，将当前点设为白色
		}
		
	}
	/**
	 * 初始化图片下面的小点，个数随list大小而改变
	 */
	private void initDots(){
		for(int i=0;i<list.size();i++){
			View view=new View(this);//在当前页面定义一个viwe
			
			//参数表示宽高
			LayoutParams params=new LayoutParams(8,8);//android.widget.LinearLayout.LayoutParams
			if(i!=0){
				params.leftMargin=5;
			}
			view.setLayoutParams(params);
			view.setBackgroundResource(R.drawable.selector_dot);
			dot_layout.addView(view);
		}
	}
	class MyPageAdapter extends PagerAdapter{
		/**
		 * 销毁page
		 * position： 当前需要消耗第几个page
		 * object:当前需要消耗的page
		 */
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			
			//super.destroyItem(container, position, object);
			container.removeView((View) object);
		}
		/**
		 * 类似于BaseAdapger的getView方法
		 * 用了将数据设置给view
		 * 由于它最多就3个界面，不需要viewHolder
		 */
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View view=View.inflate(MainActivity.this, R.layout.adapter_ad,null);
			ImageView imageView=(ImageView) view.findViewById(R.id.image);
			Ad ad=list.get(position%list.size());//因为getCount(),返回100，所以position最大值为99，求模，可以实现循环加载（伪循环）
			
			imageView.setImageResource(ad.getIconResId());
		    container.addView(view);//一定不能少，将view加入到viewPager中
			return view;
		}
		/**
		 * 返回多少个page
		 */
		@Override
		public int getCount() {
			
			//return list.size();
			return 100;//实现循环效果
		}
		/**
		 * true: 表示不去创建，使用缓存  false:去重新创建
		 * view： 当前滑动的view
		 * object：将要进入的新创建的view，由instantiateItem方法创建
		 */
		@Override
		public boolean isViewFromObject(View view, Object object) {
			
			return view==object;
		}
		
	}
	
	
	
}
