package com.bin.zhbj.domain;

import java.util.ArrayList;

/**
 * 网络分类信息的封装
 * 字段名字要和服务器返回的字段名一致，方便Gson解析
 * @author Administrator
 *
 */
public class NewsData {
	
	public int retcode;
	public ArrayList<NewMenuData> data;//存的是NewsMenuData对象
	
	//侧边栏对象
	public class NewMenuData{
		
		public String id;
		public String title;
		public String url;
		public int type;
		public ArrayList<NewsTabData> children;//存的是NewsTabData对象
		@Override
		public String toString() {
			return "NewMenuData [title=" + title + ", children=" + children + "]";
		}
	}
	//新闻页下11个子页签的数据对象
	public class NewsTabData{
		
		public String id;
		public String title;
		public String url;
		public int type;
		@Override
		public String toString() {
			return "NewsTabData [title=" + title + "]";
		}
	}
	@Override
	public String toString() {
		return "NewsData [Data=" + data + "]";
	}
}
