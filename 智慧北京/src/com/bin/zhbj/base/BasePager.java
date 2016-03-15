package com.bin.zhbj.base;

import com.bin.zhbj.MainActivity;
import com.bin.zhbj.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * 主页下5个子页面的基类
 * @author Administrator
 *
 */
public class BasePager {
	public Activity mActivity;
	public View mRootView;//布局对象
	public TextView tvTitle;//标题对象
	public ImageButton imgBtn;
	public FrameLayout flContent;//内容
	public ImageButton btnPhoto;//组图切换按钮
	public BasePager(Activity activity){
		this.mActivity=activity;
		initView();
	}
	/**
	 * 初始化布局
	 */
	public void initView(){
		mRootView = View.inflate(mActivity, R.layout.base_pager, null);
		tvTitle=(TextView) mRootView.findViewById(R.id.tv_title);
		flContent=(FrameLayout) mRootView.findViewById(R.id.fl_content);
		imgBtn=(ImageButton) mRootView.findViewById(R.id.btn_menu);
		btnPhoto = (ImageButton) mRootView.findViewById(R.id.btn_photo);
		imgBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				toggleSlidingMenu();
			}
		});
	}
	/**
	 * 初始化数据
	 */
	public void initData(){
		
	}
	
	/**
	 * 控制slidingMenu是否可用
	 * @param enable
	 */
	public void setSlidingMenuEnable(boolean enable){
		MainActivity mainUi=(MainActivity)mActivity;//BasePager在创建对象时，构造函数参数传入的其实就是MainActivity
		SlidingMenu slidingMenu=mainUi.getSlidingMenu();
		if(enable){
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		}else{
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		}
		
	}
	/**
	 * 控制slidingMenu的显示和隐藏
	 * @param show
	 */
	protected void toggleSlidingMenu(){
		MainActivity mainUi=(MainActivity) mActivity;
		SlidingMenu slidingMenu=mainUi.getSlidingMenu();
		slidingMenu.toggle();//状态切换，显示时就隐藏，隐藏时就显示
	}
}
