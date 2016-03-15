package com.bin.zhbj;

import java.util.ArrayList;

import com.bin.zhbj.utils.DensityUtils;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class GuideActivity extends Activity {
	private ViewPager vpGuide;
	private static final int[] mImageIds=new int[]{R.drawable.guide_1,R.drawable.guide_2,R.drawable.guide_3};
	private ArrayList<ImageView> mImageViewList;
	private LinearLayout llPointGroup;//引导圆点的父控件
	private int mPointWidth;//两个圆点之间的距离
	private View viewRedPoint;//小红点
	private Button btnStart;
	private SharedPreferences sp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏，在setContentView()之前调用
		setContentView(R.layout.activity_guide);
		sp = getSharedPreferences("config",MODE_PRIVATE);
		vpGuide=(ViewPager) findViewById(R.id.vp_guide);
		btnStart=(Button) findViewById(R.id.button1);
		btnStart.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(GuideActivity.this,MainActivity.class));
				sp.edit().putBoolean("is_user_guide_showed", true).commit();
				finish();
			}
		});
		llPointGroup=(LinearLayout) findViewById(R.id.ll_point_group);
		viewRedPoint=findViewById(R.id.view_red_point);
		initView();
		vpGuide.setAdapter(new GuideAdapter());
		
		vpGuide.setOnPageChangeListener(new GuideListener());
	}
	/**
	 * 初始化界面
	 */
	private void initView(){
		mImageViewList = new ArrayList<ImageView>();
		//初始化引导页的三个页面
		for(int i=0;i<mImageIds.length;i++){
			ImageView image=new ImageView(this);
			image.setBackgroundResource(mImageIds[i]);//设置背景引导页
			mImageViewList.add(image);//将所有的ImageView添加到集合中
		}
		
		//初始化引导页的小圆点
		for(int i=0;i<mImageIds.length;i++){
			View point=new View(this);
			point.setBackgroundResource(R.drawable.shape_point_gray);//设置引导页默认圆点
			LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(
				DensityUtils.dp2px(this, 10), DensityUtils.dp2px(this, 10));//将10dp转成px
			if(i>0){
				params.leftMargin=10;//设置圆点间隔
			}
			point.setLayoutParams(params);//设置圆点大小，父控件是LinearLayout，所以用LinearLayout的LayoutParams
			
			llPointGroup.addView(point);//将圆点添加给线性布局
		}
		
		
		//获取视图树,对layout结束事件进行监听
		llPointGroup.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			

			//当layout执行结束后回调此方法
			@Override
			public void onGlobalLayout() {
				System.out.println("layout结束");
				llPointGroup.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				mPointWidth = llPointGroup.getChildAt(1).getLeft()-llPointGroup.getChildAt(0).getLeft();
				System.out.println("圆点距离："+mPointWidth);
			}
		});
	}
	
	/**
	 * ViewPager的数据适配器
	 * @author Administrator
	 *
	 */
	class GuideAdapter extends PagerAdapter{

		@Override
		public int getCount() {
			
			//return mImageIds.length;
			return mImageViewList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
		
			return arg0==arg1;
		}
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(mImageViewList.get(position));
			return mImageViewList.get(position);		
		}
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View)object);;
			
		}
	}
	
	/**
	 * ViewPager的滑动监听
	 * @author Administrator
	 *
	 */
	class GuideListener implements OnPageChangeListener{
		//滑动监听
		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			
			System.out.println("当前位置："+position+";百分比："+positionOffset+";移动距离："+positionOffsetPixels);
			int len=(int) (mPointWidth * positionOffset)+position*mPointWidth;//滑到第二页时positionOffset的值会归零
			//获取当前红点的布局参数
			RelativeLayout.LayoutParams params= (RelativeLayout.LayoutParams) viewRedPoint.getLayoutParams();
			System.out.println("leftMagin:"+params.leftMargin);
			params.leftMargin=len;
			viewRedPoint.setLayoutParams(params);//重新给小红点设置布局参数
		}
		//某个页面被选中
		@Override
		public void onPageSelected(int position) {
			if(position==mImageIds.length-1){//最后一个页面
				btnStart.setVisibility(View.VISIBLE);//设置为可见
			}else{
				btnStart.setVisibility(View.INVISIBLE);
			}
			
		}
		//滑动状态发生变化
		@Override
		public void onPageScrollStateChanged(int state) {
			// TODO Auto-generated method stub
			
		}

	}

}
