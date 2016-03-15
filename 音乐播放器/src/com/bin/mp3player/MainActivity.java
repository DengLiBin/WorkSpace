package com.bin.mp3player;

import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.view.Menu;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;

public class MainActivity extends Activity {
	
	private static SeekBar sb;
	MusicInterface mi;
	MyServiceConn conn;
	Intent intent;
	static Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			Bundle bundle = msg.getData();
			int duration = bundle.getInt("duration");
			int currentPostition = bundle.getInt("currentPosition");
			//刷新进度条的进度
			sb.setMax(duration);
			sb.setProgress(currentPostition);
		}
	};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sb = (SeekBar) findViewById(R.id.sb);
        
		sb.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				//根据拖动的进度改变音乐播放进度
				int progress = seekBar.getProgress();
				//改变播放进度
				mi.seekTo(progress);
			}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
			}
		});
        
        intent=new Intent(this,MusicService.class);
        this.startService(intent);
        conn=new MyServiceConn();
        this.bindService(intent, conn, this.BIND_AUTO_CREATE);
    }
    class MyServiceConn implements ServiceConnection{
    	
    	//服务连接建立时调用此方法
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			mi=(MusicInterface) service;
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			
		}
    	
    }
    public void play(View v){
    	mi.play();
    }
    public void continuePlay(View v){
		mi.continuePlay();
	}
	public void pause(View v){
		mi.pause();
	}
	public void exit(View v){
		unbindService(conn);
		stopService(intent);
	}
}
