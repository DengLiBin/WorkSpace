package com.bin.zhbj.utils;

import android.content.Context;

public class DensityUtils {
	
	/**
	 * dp转成px
	 * @param ctx
	 */
	public static int dp2px(Context ctx,float dp){
		//设备密度
		float density=ctx.getResources().getDisplayMetrics().density;
		
		int px=(int) (dp*density+0.5f);//保证（四舍五入），不然算出来即使是4.9也会直接转成4，不合理，所以要加0.5
		return px;
	}
	
	/**
	 * px转dp
	 * @return
	 */
	public static float px2dp(Context ctx,int px){
		float density=ctx.getResources().getDisplayMetrics().density;
		return px/density;
	}
}
