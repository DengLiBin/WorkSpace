package com.bin.youkumenu;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class MainActivity extends Activity implements OnClickListener {
	private ImageView iv_home,iv_menu;
	private RelativeLayout level1,level2,level3;
	private boolean isShowLevel2=true;//二级菜单是否已经显示
	private boolean isShowLevel3=true;//三级菜单是否已经显示
	private boolean isShowMenu=true;//是否显示全部菜单
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		initListener();
	}
	@Override
	public boolean onKeyDown(int keyCode,KeyEvent event){
		if(keyCode==KeyEvent.KEYCODE_MENU){
			if(isShowMenu){//至少显示一个菜单
				//隐藏显示的菜单
				int startOffset=0;
				if(isShowLevel3){
					AnimUtils.closeMenu(level3,startOffset);
					isShowLevel3=false;
					startOffset+=200;
				}
				if(isShowLevel2){
					AnimUtils.closeMenu(level2,startOffset);
					isShowLevel2=false;
					startOffset+=200;
				}
				AnimUtils.closeMenu(level1,startOffset);
				isShowMenu=false;
			}else{
				//显示所有菜单
				AnimUtils.showMenu(level1,0);
				
				AnimUtils.showMenu(level2,200);
				isShowLevel2=true;
				AnimUtils.showMenu(level3,400);
				isShowLevel3=true;
				isShowMenu=true;
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	private void initView() {
		setContentView(R.layout.activity_main);
		iv_home=(ImageView) findViewById(R.id.iv_home);
		iv_menu=(ImageView) findViewById(R.id.iv_menu);
		level1=(RelativeLayout) findViewById(R.id.level1);
		level2=(RelativeLayout) findViewById(R.id.level2);
		level3=(RelativeLayout) findViewById(R.id.level3);
	}
	private void initListener(){
		iv_home.setOnClickListener(this);
		iv_menu.setOnClickListener(this);
		
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()){
		case R.id.iv_home:
			if(AnimUtils.animCount!=0){
				//说明有动画在执行
				return;
			}
			if(isShowLevel2){
				int startOffset=0;
				if(isShowLevel3){
					//需要隐藏
					isShowLevel3=false;
					System.out.println("执行隐藏操作");
					AnimUtils.closeMenu(level3,startOffset);
					startOffset+=200;
				}
				//需要隐藏
				isShowLevel2=false;
				System.out.println("执行隐藏操作");
				AnimUtils.closeMenu(level2,startOffset);
			}else{
				//需要显示
				isShowLevel2=true;
				System.out.println("执行显示操作");
				AnimUtils.showMenu(level2,0);
			}
			break;
		case R.id.iv_menu:
			if(AnimUtils.animCount!=0){
				//说明有动画在执行
				return;
			}
			if(isShowLevel3){
				//需要隐藏
				isShowLevel3=false;
				System.out.println("执行隐藏操作");
				AnimUtils.closeMenu(level3,0);
			}else{
				//需要显示
				isShowLevel3=true;
				System.out.println("执行显示操作");
				AnimUtils.showMenu(level3,0);
			}
			break;
		}	
	}
	
}
