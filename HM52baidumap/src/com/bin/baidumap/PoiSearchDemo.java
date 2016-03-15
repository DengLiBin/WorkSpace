package com.bin.baidumap;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.overlayutil.PoiOverlay;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiBoundSearchOption;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;

import android.content.Context;
import android.widget.Toast;

/**
 * 搜索
 * @author Administrator
 *
 */
public class PoiSearchDemo {
	private PoiSearch poiSearch;
	private Context context;
	private BaiduMap mBaiduMap;
	public  PoiSearchDemo(PoiSearch poiSearch, Context context,BaiduMap mBaiduMap){
		this.context=context;
		this.poiSearch=poiSearch;
		this.mBaiduMap=mBaiduMap;
	}
	//范围内搜索
	public void serach(){
		poiSearch=PoiSearch.newInstance();
		poiSearch.setOnGetPoiSearchResultListener(new MyListener());
		
		PoiBoundSearchOption boundOption = new PoiBoundSearchOption();
		LatLngBounds latlngBounds = new LatLngBounds.Builder()
		.include(new LatLng(40.049233, 116.302675))// 东北的点
		.include(new LatLng(40.050645, 116.303695))// 西南的点
		.build();
		boundOption.bound(latlngBounds);// 设置搜索范围
		boundOption.keyword("加油站");// 搜索关键字
		poiSearch.searchInBound(boundOption);// 只是把范围能搜索到的poi设置为 地图的中心，同时会搜索到范围外的poi
	}
	
	//周边搜索
	public void searchNear(LatLng hmPos){
		poiSearch.setOnGetPoiSearchResultListener(new MyListener());
		
		PoiNearbySearchOption nearbyOption = new PoiNearbySearchOption();
		nearbyOption.location(hmPos);// 设置中心点
		nearbyOption.radius(1000);// 设置半径 单位是米
		nearbyOption.keyword("加油站");// 关键字
		poiSearch.searchNearby(nearbyOption);
	}
	//城市内搜索
	public void searchInCity(String city,String keyword){
		poiSearch.setOnGetPoiSearchResultListener(new SearchCityListener());
		int currentPageIndex = 0;
		PoiCitySearchOption cityOption = new PoiCitySearchOption();
		cityOption.city(city);
		cityOption.keyword(keyword);
		cityOption.pageNum(currentPageIndex);
		poiSearch.searchInCity(cityOption);
	}
	class MyListener implements OnGetPoiSearchResultListener{

		@Override
		public void onGetPoiDetailResult(PoiDetailResult arg0) {
			
		}

		@Override
		public void onGetPoiResult(PoiResult result) {
			if(result==null||SearchResult.ERRORNO.RESULT_NOT_FOUND==result.error){
				Toast.makeText(context, "未搜索到结果", 0).show();
				return;
			}
			PoiOverlay overlay = new MyPoiOverlay(mBaiduMap);// 搜索poi的覆盖物
			mBaiduMap.setOnMarkerClickListener(overlay);// 把事件分发给overlay，overlay才能处理点击事件
			overlay.setData(result);// 设置结果
			overlay.addToMap();// 把搜索的结果添加到地图中
			overlay.zoomToSpan();// 缩放地图，使所有Overlay都在合适的视野内 注： 该方法只对Marker类型的overlay有效
		}
		
	}
	
	class SearchCityListener implements OnGetPoiSearchResultListener {

		@Override
		public void onGetPoiDetailResult(PoiDetailResult result) {

		}

		@Override
		public void onGetPoiResult(PoiResult result) {
			if (result == null
					|| SearchResult.ERRORNO.RESULT_NOT_FOUND == result.error) {
				Toast.makeText(context, "未搜索到结果", 0).show();
				return;
			}
			String text = "共" + result.getTotalPageNum() + "页，共"
					+ result.getTotalPoiNum() + "条，当前第"
					+ result.getCurrentPageNum() + "页，当前页"
					+ result.getCurrentPageCapacity() + "条";
			
			Toast.makeText(context, text, 1).show();
			mBaiduMap.clear();// 清空地图所有的 Overlay 覆盖物以及 InfoWindow
			PoiOverlay overlay = new MyPoiOverlay(mBaiduMap);// 搜索poi的覆盖物
			mBaiduMap.setOnMarkerClickListener(overlay);// 把事件分发给overlay，overlay才能处理点击事件
			overlay.setData(result);// 设置结果
			
			overlay.addToMap();// 把搜索的结果添加到地图中
			overlay.zoomToSpan();// 缩放地图，使所有Overlay都在合适的视野内 注：
									// 该方法只对Marker类型的overlay有效
		}
	}
	class MyPoiOverlay extends PoiOverlay {

		public MyPoiOverlay(BaiduMap arg0) {
			super(arg0);
		}
		
		@Override
		public boolean onPoiClick(int index) {
			PoiResult poiResult = getPoiResult();
			PoiInfo poiInfo = poiResult.getAllPoi().get(index);// 得到点击的那个poi信息
			String text = poiInfo.name + "," + poiInfo.address;
			Toast.makeText(context, text, 0).show();
			return super.onPoiClick(index);
		}
		
	}

}
