package com.bin.baidumap;

import java.util.ArrayList;
import java.util.List;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;

import android.content.Context;
import android.widget.Toast;

public class DrivingRouteOverlayDemo {
	private RoutePlanSearch routePlanSearch;
	private Context context;
	private BaiduMap baiduMap;
	public DrivingRouteOverlayDemo(Context context,BaiduMap baiduMap,RoutePlanSearch routePlanSearch){
		this.context=context;
		this.baiduMap=baiduMap;
		this.routePlanSearch=routePlanSearch;
	}
	
	//驾车路线
	public void search(LatLng starthmPos) {
		routePlanSearch = RoutePlanSearch.newInstance();
		routePlanSearch.setOnGetRoutePlanResultListener((OnGetRoutePlanResultListener) new MyListener());
		
		
		DrivingRoutePlanOption drivingOption = new DrivingRoutePlanOption();
		PlanNode from = PlanNode.withLocation(starthmPos);// 创建起点
		PlanNode to = PlanNode.withLocation(new LatLng(40.065796,116.349868));// 创建终点
		drivingOption.from(from);// 设置起点
		drivingOption.to(to);// 设置终点
		List<PlanNode> nodes = new ArrayList<PlanNode>();
		nodes.add(PlanNode.withCityNameAndPlaceName("北京", "天安门"));
		drivingOption.passBy(nodes);
		drivingOption.policy(DrivingRoutePlanOption.DrivingPolicy.ECAR_DIS_FIRST);// 设置策略
		routePlanSearch.drivingSearch(drivingOption);
	}
	
	class MyListener implements OnGetRoutePlanResultListener{

		@Override
		public void onGetDrivingRouteResult(DrivingRouteResult result) {
			if (result == null
					|| SearchResult.ERRORNO.RESULT_NOT_FOUND == result.error) {
				Toast.makeText(context, "未搜索到结果", 0).show();
				return;
			}
			DrivingRouteOverlay overlay = new MyDrivingOverlay(baiduMap);
			baiduMap.setOnMarkerClickListener(overlay);// 把事件传递给overlay
			overlay.setData(result.getRouteLines().get(0));// 设置线路为第一条
			overlay.addToMap();
			overlay.zoomToSpan();
			
		}

		@Override
		public void onGetTransitRouteResult(TransitRouteResult result) {
			
		}

		@Override
		public void onGetWalkingRouteResult(WalkingRouteResult result) {
			
		}
		
	}
	class MyDrivingOverlay extends DrivingRouteOverlay{

		public MyDrivingOverlay(BaiduMap arg0) {
			super(arg0);
		}
		
		@Override
		public BitmapDescriptor getStartMarker() {
			//覆写此方法以改变默认起点图标
			return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
		}
		@Override
		public BitmapDescriptor getTerminalMarker() {
			//覆写此方法以改变默认终点图标
			return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
		}
		
	}
}
