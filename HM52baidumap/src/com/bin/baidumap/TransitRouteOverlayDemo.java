package com.bin.baidumap;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.overlayutil.TransitRouteOverlay;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;

import android.content.Context;
import android.widget.Toast;

/**
 * 公交路线
 * @author Administrator
 *
 */
public class TransitRouteOverlayDemo {
	private RoutePlanSearch routePlanSearch;
	private Context context;
	private BaiduMap baiduMap;
	public TransitRouteOverlayDemo(RoutePlanSearch routePlanSearch,Context context,BaiduMap baiduMap){
		this.routePlanSearch=routePlanSearch;
		this.context=context;
		this.baiduMap=baiduMap;
	}
	public void search(LatLng hmPos) {
		routePlanSearch = RoutePlanSearch.newInstance();
		routePlanSearch.setOnGetRoutePlanResultListener(new MyListener());
		
		TransitRoutePlanOption transitOption = new TransitRoutePlanOption();
		PlanNode from = PlanNode.withLocation(hmPos);// 创建起点
		PlanNode to = PlanNode.withLocation(new LatLng(40.065796,116.349868));// 创建终点
		transitOption.from(from);
		transitOption.to(to);
		transitOption.city("北京");
		transitOption.policy(TransitRoutePlanOption.TransitPolicy.EBUS_WALK_FIRST);
		routePlanSearch.transitSearch(transitOption);
	}
	
	class MyListener implements OnGetRoutePlanResultListener{

		@Override
		public void onGetDrivingRouteResult(DrivingRouteResult result) {}

		@Override
		public void onGetTransitRouteResult(TransitRouteResult result) {

			if (result == null
					|| SearchResult.ERRORNO.RESULT_NOT_FOUND == result.error) {
				Toast.makeText(context, "未搜索到结果", 0).show();
				return;
			}
			TransitRouteOverlay overlay = new TransitRouteOverlay(baiduMap);
			baiduMap.setOnMarkerClickListener(overlay);// 把事件传递给overlay
			overlay.setData(result.getRouteLines().get(0));// 设置线路为第一条
			overlay.addToMap();
			overlay.zoomToSpan();
			
		
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
