package com.example.heimaweixin;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;



public class MainActivity extends FragmentActivity implements OnTabChangeListener {
    private final  static String TAG_CHAT="chat";
    private final  static String TAG_CON="contacts";
    private final  static String TAG_FIND="find";
    private final  static String TAG_ME="me";
    
    private FragmentTabHost tabHost;
	private TabSpec spec;
	private TabSpec spec2;
	private TabSpec spec3;
	private TabSpec spec4;
	private TabIndicatorView indicatorView;
	private TabIndicatorView indicatorView2;
	private TabIndicatorView indicatorView3;
	private TabIndicatorView indicatorView4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //1初始化Tabhost,id是系统的id,不能自己定义
        tabHost= (FragmentTabHost) findViewById(android.R.id.tabhost);
        tabHost.setup(this,getSupportFragmentManager(),android.R.id.tabcontent);
        spec = tabHost.newTabSpec(TAG_CHAT);
        spec2 = tabHost.newTabSpec(TAG_CON);
        spec3 = tabHost.newTabSpec(TAG_FIND);
        spec4 = tabHost.newTabSpec(TAG_ME);
       
        indicatorView = new TabIndicatorView(this);        
        //初始化indicatorView
        indicatorView.setTitle("消息");
        indicatorView.setTabIcon(R.drawable.tab_icon_chat_normal, R.drawable.tab_icon_chat_focus);
        spec.setIndicator(indicatorView);//在顶部显示这个布局
        tabHost.addTab(spec,MyFragment.class,null);
       
        indicatorView2 = new TabIndicatorView(this);        
        //初始化indicatorView
        indicatorView2.setTitle("通讯录");
        indicatorView2.setTabIcon(R.drawable.tab_icon_contact_normal, R.drawable.tab_icon_contact_focus);
        indicatorView2.setTabUnreadCount(10);
        spec2.setIndicator(indicatorView2);//在顶部显示这个布局
        tabHost.addTab(spec2,MyFragment.class,null);
      
        indicatorView3 = new TabIndicatorView(this);        
        //初始化indicatorView
        indicatorView3.setTitle("发现");
        indicatorView3.setTabIcon(R.drawable.tab_icon_discover_normal, R.drawable.tab_icon_discover_focus);
        indicatorView3.setTabUnreadCount(10);
        spec3.setIndicator(indicatorView3);//在顶部显示这个布局
        tabHost.addTab(spec3,MyFragment.class,null);
       
        indicatorView4 = new TabIndicatorView(this);        
        //初始化indicatorView
        indicatorView4.setTitle("我");
        indicatorView4.setTabIcon(R.drawable.tab_icon_me_normal, R.drawable.tab_icon_me_focus);
        spec4.setIndicator(indicatorView4);//在顶部显示这个布局
        tabHost.addTab(spec4,MyFragment.class,null);
       
        //隐藏tab之间分割线
        tabHost.getTabWidget().setDividerDrawable(android.R.color.transparent);
        
        tabHost.setCurrentTabByTag(TAG_CHAT);
        indicatorView.setTabSelected(true);
        tabHost.setOnTabChangedListener(this);//添加监听器
    }
	@Override
	public void onTabChanged(String tabId) {
		//先全部设为没有选中
		indicatorView.setTabSelected(false);
		indicatorView2.setTabSelected(false);
		indicatorView3.setTabSelected(false);
		indicatorView4.setTabSelected(false);
		
		if(TAG_CHAT.equals(tabId)){
			indicatorView.setTabSelected(true);
		}else if(TAG_CON.equals(tabId)){
			indicatorView2.setTabSelected(true);
		}else if(TAG_FIND.equals(tabId)){
			indicatorView3.setTabSelected(true);
		}else if(TAG_ME.equals(tabId)){
			indicatorView4.setTabSelected(true);
		}
		
	}
   

}
