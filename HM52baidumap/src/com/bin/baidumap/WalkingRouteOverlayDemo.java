package com.bin.baidumap;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.WalkingRouteOverlay;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;

import android.content.Context;
import android.widget.Toast;

/**
 * 步行路线
 * @author Administrator
 *
 */
public class WalkingRouteOverlayDemo {
	private RoutePlanSearch routePlanSearch;
	private Context context;
	private BaiduMap baiduMap;
	public WalkingRouteOverlayDemo(RoutePlanSearch routePlanSearch,Context context,BaiduMap baiduMap){
		this.routePlanSearch=routePlanSearch;
		this.context=context;
		this.baiduMap=baiduMap;
	}
	private void search(LatLng hmPos) {
		routePlanSearch = RoutePlanSearch.newInstance();
		routePlanSearch.setOnGetRoutePlanResultListener(new MyListener());
		
		WalkingRoutePlanOption walkOption = new WalkingRoutePlanOption();
		PlanNode from = PlanNode.withLocation(hmPos);// 创建起点
		PlanNode to = PlanNode.withLocation(new LatLng(40.065796,116.349868));// 创建终点
		walkOption.from(from);
		walkOption.to(to);
		routePlanSearch.walkingSearch(walkOption);
	}
	
	class MyListener implements OnGetRoutePlanResultListener{

		@Override
		public void onGetDrivingRouteResult(DrivingRouteResult result) {}

		@Override
		public void onGetTransitRouteResult(TransitRouteResult result) {
			
		}

		@Override
		public void onGetWalkingRouteResult(WalkingRouteResult result) {

			if (result == null
					|| SearchResult.ERRORNO.RESULT_NOT_FOUND == result.error) {
				Toast.makeText(context, "未搜索到结果", 0).show();
				return;
			}
			WalkingRouteOverlay overlay = new WalkingRouteOverlay(baiduMap);
			baiduMap.setOnMarkerClickListener(overlay);// 把事件传递给overlay
			overlay.setData(result.getRouteLines().get(0));// 设置线路为第一条
			overlay.addToMap();
			overlay.zoomToSpan();
			
		
		}
	}
}
