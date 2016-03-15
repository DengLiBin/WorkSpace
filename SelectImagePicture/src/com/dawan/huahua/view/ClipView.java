package com.dawan.huahua.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * 
 * 
 * @author king
 */
public class ClipView extends View {

	private final float LINE_BORDER_WIDTH = 2f;// 框框宽度
	private final int LINE_COLOR = Color.WHITE;
	private final Paint mPaint = new Paint();
	//距边框距�?
	public static final int BORDERDISTANCE = 100;// 距离屏幕的边�?

	private Context context;

	public ClipView(Context context) {
		this(context, null);
		this.context = context;
	}

	public ClipView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		this.context = context;
	}

	public ClipView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		this.context = context;
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		/***********************************START*分横竖屏***************************************/
		// int width = getWidth();
		// int height = getHeight();
		//
		// mPaint.setColor(Color.parseColor("#76000000"));
		//
		// boolean isHorizontal = false;
		// if (width > height) {
		// isHorizontal = true;
		// }
		// int outLeft = 0;
		// int outTop = 0;
		// int outRight = width;
		// int outBottom = height;

		// int borderlength = isHorizontal ? (height - BORDERDISTANCE * 2) :
		// (width - BORDERDISTANCE * 2);
		// // int borderlength2 = isHorizontal ? (height - BORDERDISTANCE2 * 2)
		// : (width - BORDERDISTANCE * 2);
		//
		// int inLeft = isHorizontal ? ((width - borderlength) / 2) :
		// BORDERDISTANCE;
		// int inTop = isHorizontal ? BORDERDISTANCE : ((height - borderlength)
		// / 2);
		// int inRight = isHorizontal ? (inLeft + borderlength) : borderlength +
		// BORDERDISTANCE;
		// int inBottom = isHorizontal ? borderlength + BORDERDISTANCE : (inTop
		// + borderlength);
		//
		// canvas.drawRect(outLeft, outTop, outRight, inTop, mPaint);
		// canvas.drawRect(outLeft, inBottom, outRight, outBottom, mPaint);
		// canvas.drawRect(outLeft, inTop, inLeft, inBottom, mPaint);
		// canvas.drawRect(inRight, inTop, outRight, inBottom, mPaint);
		//
		// mPaint.setColor(LINE_COLOR);
		// mPaint.setStrokeWidth(LINE_BORDER_WIDTH);
		//
		// canvas.drawLine(inLeft, inTop, inLeft, inBottom, mPaint);
		// canvas.drawLine(inRight, inTop, inRight, inBottom, mPaint);
		// canvas.drawLine(inLeft, inTop, inRight, inTop, mPaint);
		// canvas.drawLine(inLeft, inBottom, inRight, inBottom, mPaint);
		/***********************************END*分横竖屏***************************************/
		int width = getWidth();
		int height = getHeight();

		mPaint.setColor(Color.parseColor("#76000000"));

		int outLeft = 0;
		int outTop = 0;
		int outRight = width;
		int outBottom = height;

		int inLeft = BORDERDISTANCE;
		int inTop = (height - (width - BORDERDISTANCE * 2) * 4 / 3) / 2;
		int inRight = BORDERDISTANCE + (width - BORDERDISTANCE * 2);
		int inBottom = (inTop + (width - BORDERDISTANCE * 2) * 4 / 3);

		canvas.drawRect(outLeft, outTop, outRight, inTop, mPaint);
		canvas.drawRect(outLeft, inBottom, outRight, outBottom, mPaint);
		canvas.drawRect(outLeft, inTop, inLeft, inBottom, mPaint);
		canvas.drawRect(inRight, inTop, outRight, inBottom, mPaint);

		mPaint.setColor(LINE_COLOR);
		mPaint.setStrokeWidth(LINE_BORDER_WIDTH);

		canvas.drawLine(inLeft, inTop, inLeft, inBottom, mPaint);
		canvas.drawLine(inRight, inTop, inRight, inBottom, mPaint);
		canvas.drawLine(inLeft, inTop, inRight, inTop, mPaint);
		canvas.drawLine(inLeft, inBottom, inRight, inBottom, mPaint);
	}
}
