package com.libin.googleplay;

import com.libin.googleplay.fragment.AppFragment;
import com.libin.googleplay.fragment.HomeFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


/**
 * 用于演示在actionBar下面给actionBar添加tab
 * @author Administrator
 *
 */
public class MainActivityDemo extends BaseActivity implements OnQueryTextListener  {

	private DrawerLayout drawer;
	private ActionBar actionBar;
	private Tab tab1,tab2,tab3,tab4;
	
	private ActionBarDrawerToggle drawerToggle;
	private ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		actionBar = getSupportActionBar();
		
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		
		mViewPager=(ViewPager) findViewById(R.id.vp_pager);
		//要放在tab创建之前，不然在tab切换时，拿不到ViewPager,报空指针异常
		mViewPager.setAdapter(new MainAdapter(getSupportFragmentManager()));
		
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);//在ActionBar下面添加一个tab栏
		tab1 = actionBar.newTab().setText("标签一").setTabListener(new MyTabListener());
		actionBar.addTab(tab1);
		tab2 = actionBar.newTab().setText("标签二").setTabListener(new MyTabListener());
		actionBar.addTab(tab2);
		tab3 = actionBar.newTab().setText("标签三").setTabListener(new MyTabListener());
		actionBar.addTab(tab3);
		tab4 = actionBar.newTab().setText("标签四").setTabListener(new MyTabListener());
		actionBar.addTab(tab4);
		
		drawer = (DrawerLayout) findViewById(R.id.drawer);
		
		drawerToggle = new ActionBarDrawerToggle(this, drawer,
				R.drawable.ic_drawer_am, R.string.open_drawer,R.string.close_drawer){
			@Override
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);
				Toast.makeText(getApplicationContext(), "抽屉关闭了", 0).show();
			}
			
			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				Toast.makeText(getApplicationContext(), "抽屉打开了", 0).show();
			}
		};
		
		//让开关和actionBar建立关系
		drawer.setDrawerListener(drawerToggle);
		drawerToggle.syncState();
		
		//在方法：onOptionsItemSelected(MenuItem item)处理点击
		
		//要放在tab创建之后，不然，切换viewpager时，拿不到tab，报空指针异常
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
			@Override
			public void onPageSelected(int arg0) {
				super.onPageSelected(arg0);
				getSupportActionBar().setSelectedNavigationItem(arg0);
			}
			
		});
		
	}
	
	/**
	 * 标签切换监听器
	 * @author Administrator
	 *
	 */
	private class MyTabListener implements TabListener{

		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			//当tab标签被选中时，把ViwePager设置成相应的页面
			mViewPager.setCurrentItem(tab.getPosition());
		}

		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			
		}

		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
			
		}
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main, menu);
	 // 如果运行的环境 (部署到什么版本的手机 )大于3.0
 		if (android.os.Build.VERSION.SDK_INT > 11) {
 			SearchView searchView = (SearchView) menu.findItem(
 					R.id.action_search).getActionView();
 			searchView.setOnQueryTextListener(this);// 搜索的监听
 		} 
	    return true;
	}
	/**
	 *处理actionBar菜单条目的点击事件
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	   if(item.getItemId()==R.id.action_search){//id已经在menu/main.xml文件中配置
		   Toast.makeText(getApplicationContext(), "搜索", 0).show();
	   }
	   //让drawerToggle去处理选中的item
	   return drawerToggle.onOptionsItemSelected(item)|super.onOptionsItemSelected(item);
	}
	
	//搜索框文本发生变化时调用
	@Override
	public boolean onQueryTextChange(String arg0) {
		
		return true;
	}
	//提交时调用
	@Override
	public boolean onQueryTextSubmit(String arg0) {
		Toast.makeText(getApplicationContext(), arg0, 0).show();
		return true;
	}
	
	public  void onClick(View v){
		startActivity(new Intent(getApplicationContext(),DetailActivity.class));
	}
	
	/**
	 * mViewPager的适配器
	 * @author Administrator
	 *
	 */
	private class MainAdapter extends FragmentStatePagerAdapter{

		public MainAdapter(FragmentManager fm) {
			super(fm);
		}

		/**
		 * 每个标签返回的Fragment,从0开始
		 */
		@Override
		public Fragment getItem(int arg0) {
			if(arg0==0){
				return new HomeFragment();
			}else{
				return new AppFragment();
			}
			
		}
		
		//一共多少个标签
		@Override
		public int getCount() {
			return 4;
		}
		//返回每个pager的标题
		@Override
		public CharSequence getPageTitle(int position) {
			
			return "标题"+position;
			
			//在ViewPager里面加了一个控件：android.support.v4.view.PagerTitleStrip
			//就会在ViewPager的上面显示一个tab栏，这里的返回值就是tab栏的标题，此时的标题是不可点击的，需要点击切换的话，要把PagerTitleStrip换成PagerTabStrip
			//到此，ActionBar添加的几个tab就可以略去了。相关代代码可以删除
		}
	}
}
