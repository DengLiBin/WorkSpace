package com.bin.baidumap;

import java.util.ArrayList;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MapViewLayoutParams;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

/**
 * 添加覆盖物
 * @author Administrator
 *
 */
public class OptionDemo  {
	private MapView mapView;
	private TextView title;
	private View pop;
	public OptionDemo(){
		
	}
	public OptionDemo(MapView mapView,Context context,LatLng hmPos){
		this.mapView=mapView;
		initPop(context,hmPos);
	}
	//圆覆盖物
	public void drawCircle(BaiduMap mBaiduMap,LatLng hmPos) {
		// 定义一个圆
		// 圆心 + 半径
		
		// 颜色 + 是否填充 + 圆的线宽
		
		// 覆盖物的操作 
		// ① 创建自己
		CircleOptions circleOptions = new CircleOptions();
		// ② 给自己设置数据
		circleOptions.center(hmPos)// 圆心
		.radius(1000)// 半径 单位是米
		.fillColor(0x60FF0000);// 透明度 红 绿 蓝
//		.stroke(new Stroke(10, 0x600FF000));// 边框 参数1 线宽 参数2 颜色
		// ③ 把覆盖物添加到地图中
		mBaiduMap.addOverlay(circleOptions);
	}
	
	//文字覆盖物
	public void drawText(BaiduMap mBaiduMap,LatLng hmPos){
		TextOptions textOptions=new TextOptions();
		textOptions.fontColor(0x60FF0000)
		.text("黑马程序员") // 文字内容
		.position(hmPos) // 位置
		.fontSize(24)// 字体大小
		.typeface(Typeface.SERIF);// 字体
//		.rotate(30);// 旋转
		mBaiduMap.addOverlay(textOptions);
	}
	
	//mark覆盖物
	public void drawMark (BaiduMap mBaiduMap,LatLng hmPos){
		double latitude=hmPos.latitude;
		double longitude=hmPos.longitude;
		BitmapDescriptor bitmapDes = BitmapDescriptorFactory
				.fromResource(R.drawable.eat_icon);

		MarkerOptions markerOptions = new MarkerOptions();
		markerOptions.position(hmPos)// 设置位置
				.icon(bitmapDes)// 设置图标
				.draggable(true)// 设置是否可以拖拽 默认是否
				.title("黑马");// 设置标题
		mBaiduMap.addOverlay(markerOptions);//添加mark
		
		markerOptions = new MarkerOptions().title("向北")
				.position(new LatLng(latitude + 0.001, longitude))
				.icon(bitmapDes);
		mBaiduMap.addOverlay(markerOptions);//添加mark

		ArrayList<BitmapDescriptor> bitmaps = new ArrayList<BitmapDescriptor>();
		bitmaps.add(bitmapDes);
		bitmaps.add(BitmapDescriptorFactory.fromResource(R.drawable.icon_geo));
		markerOptions = new MarkerOptions().title("向东")
				.position(new LatLng(latitude, longitude + 0.001))
				.icons(bitmaps)// 显示多个图片来回切换 帧动画
				.period(10);// 设置多少帧刷新一次图片资源，Marker动画的间隔时间，值越小动画越快
		mBaiduMap.addOverlay(markerOptions);//添加mark
		
		markerOptions = new MarkerOptions().title("向西南")
				.position(new LatLng(latitude - 0.001, longitude - 0.001))
				.icon(bitmapDes);
		mBaiduMap.addOverlay(markerOptions);//添加mark
		
		mBaiduMap.setOnMarkerClickListener((OnMarkerClickListener) new MyListener());
	}
	class MyListener implements OnMarkerClickListener{

		@Override
		public boolean onMarkerClick(Marker result) {
			// 当点击时 更新pop的位置 设置为显示
			LayoutParams params = new MapViewLayoutParams.Builder()
			.layoutMode(MapViewLayoutParams.ELayoutMode.mapMode)// 按照经纬度设置位置
			.position(result.getPosition())// 不能传null
			.width(MapViewLayoutParams.WRAP_CONTENT)
			.height(MapViewLayoutParams.WRAP_CONTENT)
			.yOffset(-5)// 距离position的像素 向下是正值 向上是负值
			.build();
			mapView.updateViewLayout(pop, params);
			pop.setVisibility(View.VISIBLE);//显示pop
			title.setText(result.getTitle());
			
			return true;
		}
		
	}
	private void initPop(Context context,LatLng hmPos) {
		// 加载pop 添加到mapview 设置为隐藏
		
		pop = View.inflate(context, R.layout.pop, null);
		LayoutParams params = new MapViewLayoutParams.Builder()
		.layoutMode(MapViewLayoutParams.ELayoutMode.mapMode)// 按照经纬度设置位置
		.position(hmPos)// 不能传null 设置为mapMode时 必须设置position
		.width(MapViewLayoutParams.WRAP_CONTENT)
		.height(MapViewLayoutParams.WRAP_CONTENT)
		.build();
		mapView.addView(pop, params);
		pop.setVisibility(View.INVISIBLE);//默认隐藏
		title = (TextView) pop.findViewById(R.id.title);
	}
}
