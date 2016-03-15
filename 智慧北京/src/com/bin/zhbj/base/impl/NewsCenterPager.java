package com.bin.zhbj.base.impl;

import java.util.ArrayList;

import com.bin.zhbj.MainActivity;
import com.bin.zhbj.base.BaseMenuDetailPager;
import com.bin.zhbj.base.BasePager;
import com.bin.zhbj.base.impl.menudetail.InteractMenuDetailPager;
import com.bin.zhbj.base.impl.menudetail.NewsMenuDetailPager;
import com.bin.zhbj.base.impl.menudetail.PhotoMenuDetailPager;
import com.bin.zhbj.base.impl.menudetail.TopicMenuDetailPager;
import com.bin.zhbj.domain.NewsData;
import com.bin.zhbj.domain.NewsData.NewMenuData;
import com.bin.zhbj.fragment.LeftMenuFragment;
import com.bin.zhbj.global.GlobalContacts;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.app.Activity;
import android.view.View;
import android.widget.Toast;

/**
 * @author Administrator
 *
 */
public class NewsCenterPager extends BasePager {
	public ArrayList<BaseMenuDetailPager> mPagers;//�ĸ��˵�����ҳ�ļ���
	private NewsData mNewsdata;
	public NewsCenterPager(Activity activity) {
		super(activity);
	}

//ContentFragment�е���
	@Override
	public void initData() {
		super.setSlidingMenuEnable(true);
		//tvTitle.setText("��������");
		/*
		TextView text=new TextView(mActivity);
		text.setText("����");
		text.setTextColor(Color.RED);
		text.setTextSize(25);
		text.setGravity(Gravity.CENTER);
		flContent.addView(text);//��textView��ӵ�frameLayout��
		*/
		getDataFromServer();
		
		
	}
	/**
	 * �ӷ�������ȡ����
	 */
	private void getDataFromServer(){
		HttpUtils utils=new HttpUtils();
		
		utils.send(HttpMethod.GET, GlobalContacts.CATEGORIES, new RequestCallBack<String>(){
			//���ʳɹ�
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result=(String)responseInfo.result;
				System.out.println("���ؽ����"+result);
				parseData(result);
			}
			//����ʧ��
			@Override
			public void onFailure(HttpException error, String msg) {
				Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
				error.printStackTrace();
			}
			
		});
	}
	
	/**
	 * ������������
	 * @param result
	 */
	protected void parseData(String result){
		Gson gson=new Gson();
		mNewsdata = gson.fromJson(result, NewsData.class);
		System.out.println("���������"+mNewsdata);
		MainActivity mainUi=(MainActivity) mActivity;
		
		//��ȡ�����Fragment����
		LeftMenuFragment leftMenuFragment=mainUi.getLeftMenuFragment();
		//���ò����Fragment������
		leftMenuFragment.setMenuData(mNewsdata);
		
		//׼��4���˵�����ҳ
		mPagers=new ArrayList<BaseMenuDetailPager>();
		mPagers.add(new NewsMenuDetailPager(mActivity,mNewsdata.data.get(0).children));//get(0)��ʾ��ȡ������������ҳ����һ��ҳ�棩
		mPagers.add(new TopicMenuDetailPager(mActivity));
		mPagers.add(new PhotoMenuDetailPager(mActivity, btnPhoto));
		mPagers.add(new InteractMenuDetailPager(mActivity));
		
		setCurrentMenuDetailPage(0);//���õ�ǰҳ��Ϊ�˵���������ҳ
	}
	/**
	 * ���õ�ǰ�˵�����ҳ
	 */
	public void setCurrentMenuDetailPage(int positon){
		BaseMenuDetailPager pager=mPagers.get(positon);
		flContent.removeAllViews();//�Ƴ�ǰ��Ĳ���
		flContent.addView(pager.mRootView);//flContent,�ǻ����е�FrameLayout���ڹ��캯���оͳ�ʼ���ˣ�view����ӣ�Ӧ�ð�֮ǰ��view�Ƴ�
		
		NewMenuData menuData=mNewsdata.data.get(positon);
		tvTitle.setText(menuData.title);
		pager.initData();//��ʼ����ǰҳ�������
		if(pager instanceof PhotoMenuDetailPager){//�������ͼҳ������ʾ��ͼ�л���ť
			btnPhoto.setVisibility(View.VISIBLE);
		}else{
			btnPhoto.setVisibility(View.GONE);
		}
	}

}
