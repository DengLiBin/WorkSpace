package com.libin.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity{
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		String url=data.getExtras().getString("url");
		webView.loadUrl(url);
	}
	private final String HOME_PAGE="http://www.2345.com";
	private String currentUrl=HOME_PAGE;
	private TextView textView;
	private Button btnBack,btnForward,btnHome,btnRefresh,btnFinish;
	private WebView webView;
	private WebSettings webSetting;
	private SharedPreferences mShare;
	private RelativeLayout rlview;
	private ProgressBar progressBar;
	private View videoView;
	private long cacheSize=5l*1024l*1024l;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏per.onCreate(savedInstanceState);
		MainActivity.this.getWindow().requestFeature(Window.FEATURE_PROGRESS);
		setContentView(R.layout.activity_main);
		mShare=getSharedPreferences("config", Context.MODE_PRIVATE);
		initView();
		initData();
	}
	
	//拿到布局文件
	public void initView(){
		rlview=(RelativeLayout) findViewById(R.id.rl_view);
		textView=(TextView) findViewById(R.id.tv_url);
		textView.setText("请输入网址");
		textView.setOnClickListener(new OnClickListener(){
				
			@Override
			public void onClick(View v) {
				//得到新打开Activity关闭后返回的数据
                //第二个参数为请求码，可以根据业务需求自己编号
				Intent intent=new Intent();
				intent.putExtra("currentUrl",webView.getUrl());
				intent.setClass(MainActivity.this, UrlActivity.class);
                startActivityForResult(intent, 1);
				
			}
			
		});
		webView = (WebView) findViewById(R.id.webView);
		progressBar=(ProgressBar) findViewById(R.id.pb_progress);
		btnBack=(Button) findViewById(R.id.btn_back);
		btnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				goBack();//返回上一页
			}
		});
		btnForward=(Button) findViewById(R.id.btn_forward);
		btnForward.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
					goForward();
			}
		});
		btnHome=(Button) findViewById(R.id.btn_home);
		btnHome.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				webView.loadUrl(HOME_PAGE);//加载主页
				System.out.println("加载主页");
			}
		});
		btnRefresh=(Button) findViewById(R.id.btn_refresh);
		btnRefresh.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				webView.clearCache(true);
				webView.loadUrl(getCurrrentUrl());
			}
		});
		
		btnFinish=(Button) findViewById(R.id.btn_finish);
		btnFinish.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MainActivity.this.finish();//退出程序
			}
		});
	}
	//加载数据
	public void initData(){
		
		webView.setVisibility(View.VISIBLE);
		webView.setWebChromeClient(new WebChromeClient());
		webSetting = webView.getSettings();
		webSetting.setJavaScriptEnabled(true);//设置能够执行JavaScript脚本
		webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
		webSetting.setPluginState(PluginState.ON);
		webSetting.setUseWideViewPort(true);
		//webSetting.setBuiltInZoomControls(true);//支持缩放
	
		webSetting.setDomStorageEnabled(true);
		webSetting.setDatabasePath(Environment.getExternalStorageDirectory().toString()+"/binViewDatabase");
		webSetting.setAppCacheEnabled(true);//是否使用缓存
		webSetting.setAppCachePath(Environment.getExternalStorageDirectory().toString()+"/binVieCache");
		webSetting.setAppCacheMaxSize(cacheSize);
		//启用支持视窗meta标记（可实现双击缩放）
		webSetting.setUseWideViewPort(true);
		//以缩略图模式加载页面
		webSetting.setLoadWithOverviewMode(true);
			
		webSetting.setLoadWithOverviewMode(true);
		webSetting.setUseWideViewPort(true);//设置此属性，可任意比例缩放。大视图模式
		webSetting.setLoadWithOverviewMode(true);//和setUseWideViewPort(true)一起解决网页自适应问题
		webSetting.setAppCacheEnabled(true);//是否使用缓存
		webSetting.setDomStorageEnabled(true);//DOM Storage
		webView.loadUrl(currentUrl);//加载主页
		webView.setWebViewClient(new WebViewClient(){
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				progressBar.setVisibility(View.VISIBLE);
				currentUrl=url;
				textView.setText(url);
				super.onPageStarted(view, url, favicon);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				currentUrl=url;
				progressBar.setVisibility(View.INVISIBLE);
				super.onPageFinished(view, url);
				
			}

			@Override
	        public boolean shouldOverrideUrlLoading(WebView view, String url) {
	            // TODO Auto-generated method stub
	               //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
				currentUrl=url;
	             view.loadUrl(url);
	            return true;
	        }
		});//设置web视图
		
		webView.setDownloadListener((DownloadListener) new MyWebViewDownLoadListener()); //下载监听
		
		webView.setWebChromeClient(new WebChromeClient(){
			  // 一个回调接口使用的主机应用程序通知当前页面的自定义视图已被撤职  
	        CustomViewCallback customViewCallback;
			private FrameLayout video;  
	        // 进入全屏的时候  
	        @Override  
	        public void onShowCustomView(View view, CustomViewCallback callback) {  
	            // 赋值给callback  
	            customViewCallback = callback;  
	            // 设置webView隐藏  
	           // webView.setVisibility(View.INVISIBLE);  
	            video = (FrameLayout) findViewById(R.id.video);  
	            // 将video放到当前视图中 
	            videoView=view;
	            video.addView(videoView);  
	            // 横屏显示  
	            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);  
	            // 设置全屏  
	            setFullScreen();  
	        }  
	        // 退出全屏的时候  
	        @Override  
	        public void onHideCustomView() {  
	            if (customViewCallback != null) {  
	                // 隐藏掉  
	            	
	                customViewCallback.onCustomViewHidden();  
	            }  
	            // 用户当前的首选方向  
	            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);  
	            // 退出全屏  
	            video.removeView(videoView);
	            quitFullScreen();  
	            // 设置WebView可见  
	            //webView.setVisibility(View.VISIBLE); 
	            
	        }  
	  
	        @Override  
	        public void onProgressChanged(WebView view, int newProgress) {  
	            super.onProgressChanged(view, newProgress);  
	        } 
			
		});
	}
	/**
	 * 下载连接的监听器
	 * @author DengLibin
	 *
	 */
	private class MyWebViewDownLoadListener implements DownloadListener{   
		 
		       @Override  
		        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,   
		                                    long contentLength) {              
		         
		            Uri uri = Uri.parse(url);   
		            Intent intent = new Intent(Intent.ACTION_VIEW, uri);   
		            startActivity(intent);
		       }
	}  
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event){
		switch(keyCode){
		case KeyEvent.KEYCODE_BACK:
			goBack();
			break;
			
		}
		return false;
	}

	 
	 
	 /**
	 * 返回上一页，没有，就提示退出浏览器
	 */
	public void goBack(){
		 if(webView.canGoBack()){
				webView.goBack();//让WebView回退到上一个网页

			}else{
				new AlertDialog.Builder(MainActivity.this).setTitle("提示").
				setMessage("确定退出浏览器吗？").setPositiveButton("确定", new DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
						webView.clearCache(true);
						MainActivity.this.finish();//退出程序
					}
					
				}).setNegativeButton("取消", null).show();	
			}
	 }
	
	public void goForward(){
		if(webView.canGoForward()){
			webView.goForward();
		}else{
			return;
		}
	}
	/**
	 * 地址栏设置当前url
	 */
	public String getCurrrentUrl(){
		String url=webView.getUrl();
		
		return url;
	}
	@Override
	 protected void onDestroy() {
	  webView.destroy();
	  super.onDestroy();
	 }
	 @Override
	 protected void onPause() {//失去焦点
	  // TODO Auto-generated method stub
		 webView.reload();//重新加载一下，解决播放视频，退出后还继续播放的问题
	  webView.pauseTimers();
	  super.onPause();
	 }
	 @Override
	 public void onResume() {
	  webView.resumeTimers();
	  super.onResume();
	 }
	 
	 /** 
	     * 设置全屏 
	     */  
	    private void setFullScreen() {  
	        // 设置全屏的相关属性，获取当前的屏幕状态，然后设置全屏  
	        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  
	                WindowManager.LayoutParams.FLAG_FULLSCREEN);  
	        // 全屏下的状态码：1098974464  
	        // 窗口下的状态吗：1098973440  
	    }  
	  
	    /** 
	     * 退出全屏 
	     */  
	    private void quitFullScreen() {  
	        // 声明当前屏幕状态的参数并获取  
	        final WindowManager.LayoutParams attrs = getWindow().getAttributes();  
	        attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);  
	        getWindow().setAttributes(attrs);  
	        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);  
	    }  
	
}
