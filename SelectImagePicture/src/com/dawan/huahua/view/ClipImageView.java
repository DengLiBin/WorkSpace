package com.dawan.huahua.view;


import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

/**
 * @author king
 */
public class ClipImageView extends ImageView implements View.OnTouchListener,
		ViewTreeObserver.OnGlobalLayoutListener {

	private static final int BORDERDISTANCE = ClipView.BORDERDISTANCE;

	public static final float DEFAULT_MAX_SCALE = 4.0f;
	public static final float DEFAULT_MID_SCALE = 2.0f;
	public static final float DEFAULT_MIN_SCALE = 1.0f;

	private float minScale = DEFAULT_MIN_SCALE;
	private float midScale = DEFAULT_MID_SCALE;
	private float maxScale = DEFAULT_MAX_SCALE;

	private MultiGestureDetector multiGestureDetector;

	private int borderlength;
	private int borderlength2;

	private boolean isJusted;

	private final Matrix baseMatrix = new Matrix();
	private final Matrix drawMatrix = new Matrix();
	private final Matrix suppMatrix = new Matrix();
	private final RectF displayRect = new RectF();
	private final float[] matrixValues = new float[9];

	// private Matrix defaultMatrix = new Matrix();// 初始化的图片矩阵，控制图片撑满屏幕及显示区域
	// private Matrix dragMatrix = new Matrix();// 拖拽放大过程中动态的矩阵
	// private Matrix finalMatrix = new Matrix();// �?��显示的矩�?
	// private final RectF displayRect = new RectF();// 图片的真实大�?
	// private final float[] matrixValues = new float[9];

	// private boolean isIniting;// 正在初始�?

	public ClipImageView(Context context) {
		this(context, null);
	}

	public ClipImageView(Context context, AttributeSet attr) {
		this(context, attr, 0);
	}

	public ClipImageView(Context context, AttributeSet attr, int defStyle) {
		super(context, attr, defStyle);

		super.setScaleType(ScaleType.MATRIX);

		setOnTouchListener(this);

		multiGestureDetector = new MultiGestureDetector(context);

	}

	// /**
	// * 初始化图片位�?
	// */
	// private void initBmpPosition() {
	// isIniting = true;
	// super.setScaleType(ScaleType.MATRIX);
	// Drawable drawable = getDrawable();
	//
	// if(drawable == null) {
	// return;
	// }
	//
	// final float viewWidth = getWidth();
	// final float viewHeight = getHeight();
	// final int drawableWidth = drawable.getIntrinsicWidth();
	// final int drawableHeight = drawable.getIntrinsicHeight();
	// if(viewWidth < viewHeight) {
	// borderlength = (int) (viewWidth - 2 * BORDERDISTANCE);
	// } else {
	// borderlength = (int) (viewHeight - 2 * BORDERDISTANCE);
	// }
	//
	// float screenScale = 1f;
	// // 小于屏幕的图片会被撑满屏�?
	// if(drawableWidth <= drawableHeight) {// 竖图�?
	// screenScale = (float) borderlength / drawableWidth;
	// } else {// 横图�?
	// screenScale = (float) borderlength / drawableHeight;
	// }
	//
	// defaultMatrix.setScale(screenScale, screenScale);
	//
	// if(drawableWidth <= drawableHeight) {// 竖图�?
	// float heightOffset = (viewHeight - drawableHeight * screenScale) / 2.0f;
	// if(viewWidth <= viewHeight) {// 竖照片竖屏幕
	// defaultMatrix.postTranslate(BORDERDISTANCE, heightOffset);
	// } else {// 竖照片横屏幕
	// defaultMatrix.postTranslate((viewWidth - borderlength) / 2.0f,
	// heightOffset);
	// }
	// } else {
	// float widthOffset = (viewWidth - drawableWidth * screenScale) / 2.0f;
	// if(viewWidth <= viewHeight) {// 横照片，竖屏�?
	// defaultMatrix.postTranslate(widthOffset, (viewHeight - borderlength) /
	// 2.0f);
	// } else {// 横照片，横屏�?
	// defaultMatrix.postTranslate(widthOffset, BORDERDISTANCE);
	// }
	// }
	//
	// resetMatrix();
	// }

	// /**
	// * Resets the Matrix back to FIT_CENTER, and then displays it.s
	// */
	// private void resetMatrix() {
	// if (dragMatrix == null) {
	// return;
	// }
	//
	// dragMatrix.reset();
	// setImageMatrix(getDisplayMatrix());
	// }
	//
	// private Matrix getDisplayMatrix() {
	// finalMatrix.set(defaultMatrix);
	// finalMatrix.postConcat(dragMatrix);
	// return finalMatrix;
	// }

	/**
	 * 获取图片 缩放比例
	 */
	private void configPosition() {
		super.setScaleType(ScaleType.MATRIX);
		
		Drawable d = getDrawable();
		if (d == null) {
			return;
		}
		final float viewWidth = getWidth();
		final float viewHeight = getHeight();
		final int drawableWidth = d.getIntrinsicWidth();
		final int drawableHeight = d.getIntrinsicHeight();

		borderlength = (int) (viewWidth - BORDERDISTANCE * 2);
		borderlength2 = (int) (borderlength * 4 / 3);

		float scale = 1.0f;

		// 小于屏幕的图片会被撑满屏�?
		if (drawableWidth <= drawableHeight) {// 竖图�?
			scale = (float) borderlength / drawableWidth;
		} else {// 横图�?
			scale = (float) borderlength / drawableHeight;
		}

//		baseMatrix.setScale(scale, scale);
//		// 显示的是竖向的图片还是横向的图片
//		if (drawableWidth <= drawableHeight) {// 竖图�?
//			//
//			if (drawableWidth < borderlength) {
//				baseMatrix.reset();
//				scale = (float) borderlength / drawableWidth;
//				//
//				baseMatrix.postScale(scale, scale);
//			}
//			//
//		} else {// 横图�?
//			if (drawableHeight < borderlength) {
//				baseMatrix.reset();
//				scale = (float) borderlength / drawableHeight;
//				//
//				baseMatrix.postScale(scale, scale);
//			}
//		}
//		//
//		baseMatrix.postTranslate((viewWidth - drawableWidth * scale) / 2,
//				(viewHeight - drawableHeight * scale) / 2);
		
		baseMatrix.setScale(scale, scale);

        if(drawableWidth <= drawableHeight) {// 竖图�?
            float heightOffset = (viewHeight - drawableHeight * scale) / 2.0f;
            if(viewWidth <= viewHeight) {// 竖照片竖屏幕
            	baseMatrix.postTranslate(BORDERDISTANCE, heightOffset);
            } else {// 竖照片横屏幕
            	baseMatrix.postTranslate((viewWidth - borderlength) / 2.0f, heightOffset);
            }
        } else {
            float widthOffset = (viewWidth - drawableWidth * scale) / 2.0f;
            if(viewWidth <= viewHeight) {// 横照片，竖屏�?
            	baseMatrix.postTranslate(widthOffset, (viewHeight - borderlength) / 2.0f);
            } else {// 横照片，横屏�?
            	baseMatrix.postTranslate(widthOffset, BORDERDISTANCE);
            }
        }

		resetMatrix();
		isJusted = true;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return multiGestureDetector.onTouchEvent(event);
	}

	private class MultiGestureDetector extends
			GestureDetector.SimpleOnGestureListener implements
			OnScaleGestureListener {

		private final ScaleGestureDetector scaleGestureDetector;
		private final GestureDetector gestureDetector;
		private final float scaledTouchSlop;

		private VelocityTracker velocityTracker;
		private boolean isDragging;

		private float lastTouchX;
		private float lastTouchY;
		private float lastPointerCount;

		public MultiGestureDetector(Context context) {
			scaleGestureDetector = new ScaleGestureDetector(context, this);

			gestureDetector = new GestureDetector(context, this);
			gestureDetector.setOnDoubleTapListener(this);

			final ViewConfiguration configuration = ViewConfiguration
					.get(context);
			scaledTouchSlop = configuration.getScaledTouchSlop();
		}

		@Override
		public boolean onScale(ScaleGestureDetector detector) {
			float scale = getScale();
			float scaleFactor = detector.getScaleFactor();
			if (getDrawable() != null
					&& ((scale < maxScale && scaleFactor > 1.0f) || (scale > minScale && scaleFactor < 1.0f))) {
				if (scaleFactor * scale < minScale) {
					scaleFactor = minScale / scale;
				}
				if (scaleFactor * scale > maxScale) {
					scaleFactor = maxScale / scale;
				}
				suppMatrix.postScale(scaleFactor, scaleFactor, getWidth() / 2,
						getHeight() / 2);
				checkAndDisplayMatrix();
			}
			return true;
		}

		@Override
		public boolean onScaleBegin(ScaleGestureDetector detector) {
			return true;
		}

		@Override
		public void onScaleEnd(ScaleGestureDetector detector) {
		}

		public boolean onTouchEvent(MotionEvent event) {
			if (gestureDetector.onTouchEvent(event)) {
				return true;
			}

			scaleGestureDetector.onTouchEvent(event);

			/*
			 * Get the center x, y of all the pointers
			 */
			float x = 0, y = 0;
			final int pointerCount = event.getPointerCount();
			for (int i = 0; i < pointerCount; i++) {
				x += event.getX(i);
				y += event.getY(i);
			}
			x = x / pointerCount;
			y = y / pointerCount;

			/*
			 * If the pointer count has changed cancel the drag
			 */
			if (pointerCount != lastPointerCount) {
				isDragging = false;
				if (velocityTracker != null) {
					velocityTracker.clear();
				}
				lastTouchX = x;
				lastTouchY = y;
			}
			lastPointerCount = pointerCount;

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if (velocityTracker == null) {
					velocityTracker = VelocityTracker.obtain();
				} else {
					velocityTracker.clear();
				}
				velocityTracker.addMovement(event);

				lastTouchX = x;
				lastTouchY = y;
				isDragging = false;
				break;

			case MotionEvent.ACTION_MOVE: {
				final float dx = x - lastTouchX, dy = y - lastTouchY;

				if (isDragging == false) {
					// Use Pythagoras to see if drag length is larger than
					// touch slop
					isDragging = Math.sqrt((dx * dx) + (dy * dy)) >= scaledTouchSlop;
				}

				if (isDragging) {
					if (getDrawable() != null) {
						suppMatrix.postTranslate(dx, dy);
						checkAndDisplayMatrix();
					}

					lastTouchX = x;
					lastTouchY = y;

					if (velocityTracker != null) {
						velocityTracker.addMovement(event);
					}
				}
				break;
			}
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
				lastPointerCount = 0;
				if (velocityTracker != null) {
					velocityTracker.recycle();
					velocityTracker = null;
				}
				break;
			}

			return true;
		}

		@Override
		public boolean onDoubleTap(MotionEvent event) {
			try {
				float scale = getScale();
				float x = getWidth() / 2;
				float y = getHeight() / 2;

				if (scale < midScale) {
					post(new AnimatedZoomRunnable(scale, midScale, x, y));
				} else if ((scale >= midScale) && (scale < maxScale)) {
					post(new AnimatedZoomRunnable(scale, maxScale, x, y));
				} else {
					post(new AnimatedZoomRunnable(scale, minScale, x, y));
				}
			} catch (Exception e) {
				// Can sometimes happen when getX() and getY() is called
			}

			return true;
		}
	}

	private class AnimatedZoomRunnable implements Runnable {
		// These are 'postScale' values, means they're compounded each iteration
		static final float ANIMATION_SCALE_PER_ITERATION_IN = 1.07f;
		static final float ANIMATION_SCALE_PER_ITERATION_OUT = 0.93f;

		private final float focalX, focalY;
		private final float targetZoom;
		private final float deltaScale;

		public AnimatedZoomRunnable(final float currentZoom,
				final float targetZoom, final float focalX, final float focalY) {
			this.targetZoom = targetZoom;
			this.focalX = focalX;
			this.focalY = focalY;

			if (currentZoom < targetZoom) {
				deltaScale = ANIMATION_SCALE_PER_ITERATION_IN;
			} else {
				deltaScale = ANIMATION_SCALE_PER_ITERATION_OUT;
			}
		}

		public void run() {
			suppMatrix.postScale(deltaScale, deltaScale, focalX, focalY);
			checkAndDisplayMatrix();

			final float currentScale = getScale();

			if (((deltaScale > 1f) && (currentScale < targetZoom))
					|| ((deltaScale < 1f) && (targetZoom < currentScale))) {
				// We haven't hit our target scale yet, so post ourselves
				// again
				postOnAnimation(ClipImageView.this, this);

			} else {
				// We've scaled past our target zoom, so calculate the
				// necessary scale so we're back at target zoom
				final float delta = targetZoom / currentScale;
				suppMatrix.postScale(delta, delta, focalX, focalY);
				checkAndDisplayMatrix();
			}
		}
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	private void postOnAnimation(View view, Runnable runnable) {
		if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN) {
			view.postOnAnimation(runnable);
		} else {
			view.postDelayed(runnable, 16);
		}
	}

	/**
	 * Returns the current scale value
	 * 
	 * @return float - current scale value
	 */
	public final float getScale() {
		suppMatrix.getValues(matrixValues);
		return matrixValues[Matrix.MSCALE_X];
	}

	@Override
	public void onGlobalLayout() {
		if (isJusted) {
			return;
		}
		// 调整视图位置
		configPosition();
		// 调整视图位置
		// initBmpPosition();
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();

		getViewTreeObserver().addOnGlobalLayoutListener(this);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		getViewTreeObserver().removeGlobalOnLayoutListener(this);
	}

	/**
	 * Helper method that simply checks the Matrix, and then displays the result
	 */
	private void checkAndDisplayMatrix() {
		checkMatrixBounds();
		setImageMatrix(getDisplayMatrix());
	}

	/**
	 * 图片可移动范�?
	 * */
	private void checkMatrixBounds() {
		final RectF rect = getDisplayRect(getDisplayMatrix());
		if (null == rect) {
			return;
		}

		float deltaX = 0, deltaY = 0;
		final float viewWidth = getWidth();
		final float viewHeight = getHeight();
		// 判断移动或缩放后，图片显示是否超出裁剪框边界
		if (rect.top > (viewHeight - borderlength2) / 2) {
			deltaY = (viewHeight - borderlength2) / 2 - rect.top;
		}
		if (rect.bottom < (viewHeight + borderlength2) / 2) {
			deltaY = (viewHeight + borderlength2) / 2 - rect.bottom;
		}
		if (rect.left > (viewWidth - borderlength) / 2) {
			deltaX = (viewWidth - borderlength) / 2 - rect.left;
		}
		if (rect.right < (viewWidth + borderlength) / 2) {
			deltaX = (viewWidth + borderlength) / 2 - rect.right;
		}
		// Finally actually translate the matrix
		suppMatrix.postTranslate(deltaX, deltaY);
	}

	/**
	 * Helper method that maps the supplied Matrix to the current Drawable
	 * 获取图片相对Matrix的距�?
	 * 
	 * @param matrix
	 *            - Matrix to map Drawable against
	 * @return RectF - Displayed Rectangle
	 */
	private RectF getDisplayRect(Matrix matrix) {
		Drawable d = getDrawable();
		if (null != d) {
			displayRect
					.set(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
			matrix.mapRect(displayRect);
			return displayRect;
		}

		return null;
	}

	/**
	 * Resets the Matrix back to FIT_CENTER, and then displays it.s
	 */
	private void resetMatrix() {
		if (suppMatrix == null) {
			return;
		}
		suppMatrix.reset();
		setImageMatrix(getDisplayMatrix());
	}

	protected Matrix getDisplayMatrix() {
		drawMatrix.set(baseMatrix);
		drawMatrix.postConcat(suppMatrix);
		return drawMatrix;
	}

	/**
	 * 截取图片生产的大�?
	 * 
	 * @return
	 */
	public Bitmap clip() {
		Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(),
				Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		draw(canvas);
		int inLeft = BORDERDISTANCE;
		int inTop = (getHeight() - (getWidth() - BORDERDISTANCE * 2) * 4 / 3) / 2;
		// return Bitmap.createBitmap(bitmap, (getWidth() - borderlength) / 2,
		// (getHeight() - borderlength2) / 2, borderlength, borderlength2);
		return Bitmap.createBitmap(bitmap, inLeft, inTop,
				(getWidth() - BORDERDISTANCE * 2),
				(getWidth() - BORDERDISTANCE * 2) * 4 / 3);
	}
}
