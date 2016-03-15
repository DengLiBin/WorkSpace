package com.bin.zhbj;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.TextSize;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;

/**
 * 新闻详情页
 * @author Administrator
 *
 */
public class NewsDetailActivity extends Activity implements OnClickListener {
	private ImageButton btnBack;
	private ImageButton btnSize;
	private ImageButton btnShare;
	private WebView mWebView;
	
	private ProgressBar mProgress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_news_detail);
		
		mWebView = (WebView) findViewById(R.id.wv_web);
		btnBack = (ImageButton) findViewById(R.id.btn_back);
		btnSize = (ImageButton) findViewById(R.id.btn_size);
		btnShare = (ImageButton) findViewById(R.id.btn_share);
		mProgress=(ProgressBar) findViewById(R.id.pb_progress);
		
		btnBack.setOnClickListener(this);
		btnSize.setOnClickListener(this);
		btnShare.setOnClickListener(this);
		
		String url=getIntent().getStringExtra("url");
		WebSettings setting=mWebView.getSettings();
		setting.setJavaScriptEnabled(true);//表示支持js，默认是false
		setting.setBuiltInZoomControls(true);//显示放大所次奥按钮
		setting.setUseWideViewPort(true);//双击缩放
		
		mWebView.setWebViewClient(new WebViewClient(){
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				System.out.println("开始加载网页");
				mProgress.setVisibility(View.VISIBLE);
			}
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				System.out.println("网页加载结束");
				mProgress.setVisibility(View.GONE);
			}
			
			//所有跳转的连接都会在此方法中回调
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				System.out.println("跳转URL:"+url);
				view.loadUrl(url);//强制让自己的WebView加载网页，不调用其他浏览器
				return true;
				//return super.shouldOverrideUrlLoading(view, url);
			}
		});
		
		mWebView.setWebChromeClient(new WebChromeClient(){
			
			//进度变化
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				System.out.println("加载进度："+newProgress);
				super.onProgressChanged(view, newProgress);
			}
			
			
			//获取网页标题
			@Override
			public void onReceivedTitle(WebView view, String title) {
				System.out.println("标题："+title);
				super.onReceivedTitle(view, title);
			}
		});
		mWebView.loadUrl(url);		
		//mWebView.loadUrl("http://www.itheima.com/");//加载网页
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btn_back:
			finish();
			break;
		case R.id.btn_share:
			showShare();
			break;
		case R.id.btn_size:
			showChooseDialog();
			break;
		default:
			break;
		}
	}

	private int mCurrentChooseItem;// 记录当前选中的item, 点击确定前
	private int mCurrentItem=2;// 记录当前选中的item, 点击确定后
	/**
	 * 显示选择字体大小对话框
	 */
	private void showChooseDialog() {
		AlertDialog.Builder builder=new AlertDialog.Builder(this);
		
		String[] items=new String[]{"超大号字体","大号字体","正常字体","小号字体","超小号字体"};
		builder.setTitle("字体设置");
		builder.setSingleChoiceItems(items,mCurrentItem,new DialogInterface.OnClickListener() {//默认选择正常字体
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				System.out.println("选中："+which);
				mCurrentChooseItem=which;
			}
		});
		builder.setPositiveButton("确定",new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				WebSettings setting=mWebView.getSettings();
				switch(mCurrentChooseItem){
				case 0:
					setting.setTextSize(TextSize.LARGEST);
					//setting.setTextZoom(20);
					break;
				case 1:
					setting.setTextSize(TextSize.LARGER);
					break;
				case 2:
					setting.setTextSize(TextSize.NORMAL);
					break;
				case 3:
					setting.setTextSize(TextSize.SMALLER);
					break;
				case 4:
					setting.setTextSize(TextSize.SMALLEST);
					break;
				}
				mCurrentItem=mCurrentChooseItem;
			}
			
		});
		builder.setNegativeButton("取消", null);
		
		builder.show();
	}


	private void showShare() {
	 ShareSDK.initSDK(this);
	 OnekeyShare oks = new OnekeyShare();
	 
	 oks.setTheme(OnekeyShareTheme.SKYBLUE);//风格
	 //关闭sso授权
	 oks.disableSSOWhenAuthorize(); 

	// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
	 //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
	 // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
	 oks.setTitle("分享");
	 // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
	 oks.setTitleUrl("http://sharesdk.cn");
	 // text是分享文本，所有平台都需要这个字段
	 oks.setText("我是分享文本");
	 // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
	 oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
	 // url仅在微信（包括好友和朋友圈）中使用
	 oks.setUrl("http://sharesdk.cn");
	 // comment是我对这条分享的评论，仅在人人网和QQ空间使用
	 oks.setComment("我是测试评论文本");
	 // site是分享此内容的网站名称，仅在QQ空间使用
	 oks.setSite(getString(R.string.app_name));
	 // siteUrl是分享此内容的网站地址，仅在QQ空间使用
	 oks.setSiteUrl("http://sharesdk.cn");

	// 启动分享GUI
	 oks.show(this);
	 }
	
	
}
