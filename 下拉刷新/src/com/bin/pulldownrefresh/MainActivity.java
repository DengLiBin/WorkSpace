package com.bin.pulldownrefresh;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bin.pulldownrefresh.view.RefreshListView;
import com.bin.pulldownrefresh.view.RefreshListView.OnRefreshListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import java.util.ArrayList;
public class MainActivity extends Activity {

	  private RefreshListView refreshListView;
	  private ArrayList<String>list=new ArrayList<String>();
	  private MyAdapter myAdapter;
	  private Handler handler=new Handler(){
	        public void handleMessage(android.os.Message msg){
	            myAdapter.notifyDataSetChanged();
	            refreshListView.completeRefresh();
	        }
	    };
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        initView();
	        initData();
	    }
	    private void initView(){

	        setContentView(R.layout.activity_main);
	        refreshListView=(RefreshListView)findViewById(R.id.refreshListView);
	    }
	    private void initData(){
	        for(int i=0;i<15;i++){
	            list.add("listView原来的数据"+i);
	        }
	        myAdapter=new MyAdapter();
	        refreshListView.setAdapter(myAdapter);

	        refreshListView.setOnRefreshListener(new OnRefreshListener() {
	            @Override
	            public void onPullRefresh() {
	                //需要联网请求服务器的数据，然后更新UI
	                requestDataFromServer(false);
	            }

	            @Override
	            public void onLoadingMore() {
	                requestDataFromServer(true);
	            }
	        });
	    }
	    /**
	     * 模拟向服务器请求数据
	     */
	    private void requestDataFromServer(final boolean isLoadingMore){
	        new Thread(){
	            public void run() {
	                SystemClock.sleep(3000);//模拟请求服务器的一个时间长度

	                if(isLoadingMore){
	                    list.add("加载更多的数据-1");
	                    list.add("加载更多的数据-2");
	                    list.add("加载更多的数据-3");
	                }else {
	                    list.add(0, "下拉刷新的数据");// 在0号位置上插入一条数据
	                }

	                //在UI线程更新UI
	                handler.sendEmptyMessage(0);
	            };
	        }.start();
	    }
	    class MyAdapter extends BaseAdapter{

	        @Override
	        public int getCount() {
	            return list.size();
	        }

	        @Override
	        public long getItemId(int position) {
	            return 0;
	        }

	        @Override
	        public Object getItem(int position) {
	            return null;
	        }

	        @Override
	        public View getView(int position, View convertView, ViewGroup parent) {
	            TextView textView=new TextView(MainActivity.this);
	            textView.setPadding(20, 20, 20, 20);
	            textView.setTextSize(18);
	            textView.setText(list.get(position));
	            return  textView;
	        }
	    }
}
