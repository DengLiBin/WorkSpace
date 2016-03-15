package com.bin.zhbj.view;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.bin.zhbj.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 下拉刷新的ListView
 * @author Administrator
 *
 */
public class RefreshListView extends ListView implements OnScrollListener, android.widget.AdapterView.OnItemClickListener{
	//刷新的三种状态
	private static final int STATE_PULL_REFRESH=0;//下拉刷新
	private static final int STATE_RELEASE_REFRESH=1;//松开刷新
	private static final int STATE_REFRESHING=2;//正在刷新
	
	private int mCurrentState=STATE_PULL_REFRESH;//记录当前状态
	
	private View mHeaderView;
	private int startY=-1;//滑动起点Y坐标
	private int mHeaderViewHeight;
	OnRefreshListener mListener;
	private View mFooterView;
	private int mFooterViewHeight;
	private TextView tvTitle;
	private TextView tvTime;
	private ImageView ivArrow;
	private ProgressBar pb_progress;
	private RotateAnimation animUp;//箭头动画，旋转向上
	private RotateAnimation animDown;//箭头动画，旋转向下
	//构造方法
	public RefreshListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initHeaderView();
		initFooterView();
	}
	//构造方法
	public RefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initHeaderView();
		initFooterView();
	}
	//构造方法
	public RefreshListView(Context context) {
		super(context);
		initHeaderView();
		initFooterView();
	}
	//初始化头布局
	private void initHeaderView(){
		initArrowAnim();//初始化动画
		
		mHeaderView = View.inflate(getContext(),R.layout.refresh_header,null);
		this.addHeaderView(mHeaderView);//在该ListView的头部添加一个头View
		
		tvTitle = (TextView) mHeaderView.findViewById(R.id.tv_title);
		tvTime = (TextView) mHeaderView.findViewById(R.id.tv_time);
		tvTime.setText("最后刷新时间："+getCurrentTime());
		ivArrow = (ImageView) mHeaderView.findViewById(R.id.iv_arrow);
		pb_progress = (ProgressBar) mHeaderView.findViewById(R.id.pb_progress);
		  
		mHeaderView.measure(0,0);//测量控件的宽高
		mHeaderViewHeight = mHeaderView.getMeasuredHeight();//获取控件的高
		mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);//可以将view隐藏
	}
	//初始化尾布局
	private void initFooterView(){
		mFooterView = View.inflate(getContext(), R.layout.refresh_footer, null);
		this.addFooterView(mFooterView);
		mFooterView.measure(0, 0);//测量控件的宽高
		mFooterViewHeight = mFooterView.getMeasuredHeight();//获取控件的高
		mFooterView.setPadding(0, -mFooterViewHeight, 0, 0);//隐藏控件
		this.setOnScrollListener(this);//添加一个滚动监听
		
	}
	
	//复写onTouchEvent方法
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch(ev.getAction()){
		case MotionEvent.ACTION_DOWN://手指按下
			startY = (int) ev.getRawY();
			//int startX=(int) ev.getRawX();竖直方向，不用考虑x坐标
			break;
		case MotionEvent.ACTION_MOVE://滑动
			if(mCurrentState==STATE_REFRESHING){//如果处于正在刷新的状态，直接跳出，对滑动不做任何处理
				break;
			}
			if(startY==-1){//确保拿到startY的值
				startY=(int) ev.getRawY();
			}
			int endY=(int) ev.getRawY();//滑动时的Y坐标
			
			int dy=endY-startY;//滑动的垂直距离
			
			if(dy>0&&getFirstVisiblePosition()==0){//保证是向下拉，且拉的距离要足够大，并且处于第1个item时才显示mHeaderView
				int padding=dy-mHeaderViewHeight;//计算padding的值
				mHeaderView.setPadding(0, padding, 0, 0);//设置当前的padding
				
				if(padding>0&&mCurrentState!=STATE_RELEASE_REFRESH){//这种条件下由下拉刷新改为松开刷新
					mCurrentState=STATE_RELEASE_REFRESH;
					refreshState();//刷新控件布局
				}else if(padding<=0&&mCurrentState!=STATE_PULL_REFRESH){//下拉距离不够时，改为下拉刷新
					mCurrentState=STATE_PULL_REFRESH;
					refreshState();//刷新控件布局
				}
				
				return true;//拦截TouchMove，不让listview处理该次move事件,会造成listview无法滑动
			}
			break;
		case MotionEvent.ACTION_UP://手指离开屏幕
			startY=-1;//重置
			if(mCurrentState==STATE_RELEASE_REFRESH){//由松开刷新改为正在刷新
				mCurrentState=STATE_REFRESHING;
				mHeaderView.setPadding(0,0,0,0);//显示
				refreshState();
			}else if(mCurrentState==STATE_PULL_REFRESH){//当下拉的距离不够时，
				mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);//将view隐藏
			}
			break;
		default:
			break;
		}
		return super.onTouchEvent(ev);
	}
	/**
	 * 根据当前状态更新控件布局
	 */
	private void refreshState() {
		switch(mCurrentState){
		case STATE_PULL_REFRESH:
			tvTitle.setText("下拉刷新");
			ivArrow.setVisibility(View.VISIBLE);//显示箭头 
			pb_progress.setVisibility(View.INVISIBLE);//隐藏ProgressBar
			ivArrow.startAnimation(animDown);
		break;
		
		case STATE_RELEASE_REFRESH:
			tvTitle.setText("松开刷新");
			ivArrow.setVisibility(View.VISIBLE);//显示箭头 
			pb_progress.setVisibility(View.INVISIBLE);//隐藏ProgressBar
			ivArrow.startAnimation(animUp);
			break;
			
		case STATE_REFRESHING:
			tvTitle.setText("正在刷新");
			ivArrow.clearAnimation();//清除动画，才能将箭头隐藏
			ivArrow.setVisibility(View.INVISIBLE);//隐藏箭头 
			pb_progress.setVisibility(View.VISIBLE);//显示ProgressBar
			mListener.onRefresh();
			break;
		default:
			break;
		}
	}
	/**
	 * 箭头动画
	 */
	private void initArrowAnim(){
		//箭头向上
		animUp = new RotateAnimation(0,-180,
				Animation.RELATIVE_TO_SELF,0.5f,
				Animation.RELATIVE_TO_SELF,0.5f);
		animUp.setDuration(200);
		animUp.setFillAfter(true);//保存状态
		
		//箭头向下
		animDown = new RotateAnimation(-180,0,
				Animation.RELATIVE_TO_SELF,0.5f,
				Animation.RELATIVE_TO_SELF,0.5f);
		animUp.setDuration(200);
		animUp.setFillAfter(true);//保存状态
	}
	
	//设置监听器的方法
	public void setOnRefreshListener(OnRefreshListener listener){
		mListener=listener;
	}
	//刷新的监听器
	public interface OnRefreshListener{
		public void onRefresh();//头部刷新数据
		
		public void onLoadingMore();//尾部加载更多数据
	}
	
	/**
	 * 刷新完成后，将控件隐藏，在TabDetailPager中，从服务器获取数据完成时，调用
	 */
	public  void onRefreshComplete(){
		if(isLoadingMore){
			mFooterView.setPadding(0, -mFooterViewHeight, 0, 0);//隐藏脚布局
		}else{
			mCurrentState=STATE_PULL_REFRESH;
			tvTitle.setText("下拉刷新");
			ivArrow.setVisibility(View.VISIBLE);//显示箭头 
			pb_progress.setVisibility(View.INVISIBLE);//隐藏ProgressBar
			mHeaderView.setPadding(0, -mHeaderViewHeight, 0,0);//将控件隐藏
			
			tvTime.setText("最后刷新时间："+getCurrentTime());
		}
		
	}
	
	/**
	 * 获取当前时间,本类方法onRefreshComplete调用
	 */
	public String getCurrentTime(){
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(new Date());//安卓系统用的什么时区，就显示哪个时区的时间
	}
	
	
	private boolean isLoadingMore;//一个标记，表示是否是要加载更多，默认是false
	//复写OnScrollListener的方法
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if(scrollState==SCROLL_STATE_IDLE||scrollState==SCROLL_STATE_FLING){
			if(getLastVisiblePosition()==getCount()-1 && !isLoadingMore){//滑动到最后一个item
				System.out.println("到底了");
				mFooterView.setPadding(0, 0, 0, 0);//显示控件
				setSelection(getCount()-1);//设置选中的Item（为最后一个）
				
				isLoadingMore=true;
				if(mListener!=null){
					mListener.onLoadingMore(); 
				}
			}
		}
	}
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		
	}
	
	OnItemClickListener mItemClickListener;
	@Override
	public void setOnItemClickListener(android.widget.AdapterView.OnItemClickListener listener) {
		super.setOnItemClickListener(this);//this表示本类(实现了OnItemClickListener,复写OnItemClick方法)
		mItemClickListener=listener;
	}
	
	//OnItemClickListener的方法
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if(mItemClickListener!=null){
			mItemClickListener.onItemClick(parent, view, position-2, id);
		}
	}
	
}







