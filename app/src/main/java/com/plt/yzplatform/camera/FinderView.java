///*
// * Copyright by Deppon and the original author or authors.
// *
// *
// *
// *
// *
// *
// *
// *
// *
// *
// * This document only allow internal use ,Any of your behaviors using the file
// * not internal will pay legal responsibility.
// *
// *
// *
// *
// *
// *
// *
// *
// *
// * You may learn more information about Deppon from
// *
// *
// *
// *      http://www.deppon.com
// *
// *
// */
//package com.express.accept.newimgbilling;
//
//
//import android.content.Context;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.Paint;
//import android.graphics.Rect;
//import android.graphics.RectF;
//import android.util.AttributeSet;
//import android.util.Log;
//import android.view.View;
//
//import com.express.R;
//import com.express.util.other.Constants;
//
//
///**
// *@date 创建时间 2017/4/15
// *@author 328798
// *@company 德邦物流
// *@name：zhongshuiping
// *@Description 自定义扫描取景框
// */
//
//public class FinderView extends View {
//	public static boolean bFocused = false;
//	/**
//	 * 常量ANIMATION_DELAY
//	 */
//	private static final long ANIMATION_DELAY =30;
//	/**
//	 * finderMaskPaint
//	 */
//	private Paint finderMaskPaint;
//	/**
//	 * measureedWidth
//	 */
//	private int measureedWidth;
//	/**
//	 * measureedHeight
//	 */
//	private int measureedHeight;
//
//	/***
//	 * 四个角的线
//	 */
//	private Paint linerPaint;
//	/**
//	 * 画框
//	 */
//	private Paint framePaint;
//
//	private RectF mRectF;
//	/**
//	 * 上边半透明矩形框的高
//	 */
//	private int topHeight;
//	/**
//	 * 中间对焦框的高
//	 */
//	private  int middleHeight;
//	/**
//	 * 左边对焦框的宽
//	 */
//	private  int weight;
//
//	/**
//	 *@date 创建时间 2017/4/15
//	 *@author 328798
//	 *@company 德邦物流
//	 *@name：zhongshuiping
//	 *@Description 构造方法
//	 */
//	public FinderView(Context context) {
//		super(context);
//		init(context);
//	}
//	/**
//	 *@date 创建时间 2017/4/15
//	 *@author 328798
//	 *@company 德邦物流
//	 *@name：zhongshuiping
//	 *@Description
//	 */
//	public FinderView(Context context, AttributeSet attrs) {
//		super(context, attrs);
//		init(context);
//	}
//
//	/**
//	 *@date 创建时间 2017/4/15
//	 *@author 328798
//	 *@company 德邦物流
//	 *@name：zhongshuiping
//	 *@Description onDraw
//	 */
//	@Override
//	protected void onDraw(Canvas canvas) {
//		super.onDraw(canvas);
//		canvas.drawRect(leftRect, finderMaskPaint);
//		canvas.drawRect(topRect, finderMaskPaint);
//		canvas.drawRect(rightRect, finderMaskPaint);
//		canvas.drawRect(bottomRect, finderMaskPaint);
//		canvas.drawRect(middleRect, framePaint);
//		if (bFocused){
//			linerPaint.setColor(Color.GREEN);//设置颜色
//			linerPaint.setStrokeWidth(Constants.FOCUS_FRAME_EIGHT);
////
//
//		}
//		else {
//			linerPaint.setColor(Color.GRAY);//设置颜色
//			linerPaint.setStrokeWidth(0.8f);//设置线条大小
//
//		}
//
////			/**
////			 * 左上角
////			 */
////			//Log.i("=======zuoTOP","x=="+weight+"y=="+topHeight);
//			canvas.drawLine(weight,topHeight,weight,topHeight+middleHeight,linerPaint);//左边线
////			canvas.drawLine(weight- Constants.FOCUS_FRAME_HEIGHT,topHeight,weight+(weight/ Constants.FOCUS_FRAME_THREE),topHeight,linerPaint);//上边线
////
////			/**
////			 * 左下角
////			 */
////			//Log.i("=======zuobottom","x=="+weight+"y=="+topHeight+middleHeight);
////			float leftheigt=(topHeight+middleHeight)-(topHeight/ Constants.FOCUS_FRAME_HEIGHT);
////			float lefthweight=measureedWidth-weight-(weight/ Constants.FOCUS_FRAME_THREE);
//			canvas.drawLine(weight,topHeight+middleHeight,measureedWidth-weight,topHeight+middleHeight,linerPaint);//左边线
//			canvas.drawLine(weight,topHeight,measureedWidth-weight,topHeight,linerPaint);//左边线
////			canvas.drawLine(weight- Constants.FOCUS_FRAME_HEIGHT,topHeight+middleHeight,weight+(weight/ Constants.FOCUS_FRAME_THREE),topHeight+middleHeight,linerPaint);//下边线
////
////			/**
////			 * 右上角
////			 */
////			//Log.i("=======ys","x=="+(measureedWidth-weight)+"y=="+topHeight);
//			canvas.drawLine(measureedWidth-weight,topHeight,measureedWidth-weight,topHeight+middleHeight,linerPaint);//左边线
////			canvas.drawLine(measureedWidth-weight,topHeight- Constants.FOCUS_FRAME_HEIGHT,measureedWidth-weight,topHeight+(topHeight/ Constants.FOCUS_FRAME_HEIGHT),linerPaint);//下边线
////
////			/**
////			 * 右下角
////			 */
////			canvas.drawLine(measureedWidth-weight,topHeight+middleHeight+ Constants.FOCUS_FRAME_HEIGHT,measureedWidth-weight,leftheigt,linerPaint);//左边线
////			canvas.drawLine(measureedWidth-weight,topHeight+middleHeight,lefthweight,topHeight+middleHeight,linerPaint);//下边线
//	}
//
//	/*
//	 * 上、下、左、右矩阵
//	 */
//	private Rect topRect = new Rect();
//	private Rect bottomRect = new Rect();
//	private Rect rightRect = new Rect();
//	private Rect leftRect = new Rect();
//	private Rect middleRect = new Rect();
//
//
//
//
//
//	/**
//	 *@date 创建时间 2017/4/15
//	 *@author 328798
//	 *@company 德邦物流
//	 *@name：zhongshuiping
//	 *@Description 初始化
//	 */
//	private void init(Context context) {
//		int finderMask = context.getResources().getColor(R.color.cover_color);
//		/**
//		 * 创建四个半透明矩形框的画笔
//		 */
//		finderMaskPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//		finderMaskPaint.setColor(finderMask);
////		finderMaskPaint.setAlpha(80);\
//		finderMaskPaint.setARGB(128,30,30,30);
//
//		/**
//		 * 创建中间对焦框的画笔
//		 */
//		framePaint=new Paint();
//		framePaint.setAntiAlias(true);// 抗锯齿
//		framePaint.setDither(true);// 防抖动
//		framePaint.setColor(Color.WHITE);//设置颜色
//		framePaint.setStrokeWidth(0.8f);//设置线条大小
//		framePaint.setStyle(Paint.Style.STROKE);// 实心
//
//
//		/**
//		 * 创建四个角的画笔
//		 */
//		linerPaint=new Paint();
//		linerPaint.setAntiAlias(true);// 抗锯齿
//		linerPaint.setDither(true);// 防抖动
//		linerPaint.setColor(Color.GREEN);//设置颜色
//		linerPaint.setStrokeWidth(Constants.FOCUS_FRAME_EIGHT);
//		linerPaint.setStyle(Paint.Style.FILL);// 实心
//
//	}
//
//
//
//
////	/**
////	 *@date 创建时间 2017/4/15
////	 *@author 328798
////	 *@company 德邦物流
////	 *@name：zhongshuiping
////	 *@Description 获取扫描框的大小
////	 */
////	public Rect getScanImageRect(int w, int h) {
////		Rect rect = new Rect();
////		double tempheight = h / ((double) measureedHeight);
////		double tempwidth=w /((double) measureedWidth);
////
////		Log.i("=====aaa",tempheight+"==="+measureedHeight);
////		/**
////		 *
////		 * 获取扫描框左边值
////		 */
////		rect.left =  (int) (middleRect.top* tempwidth);
////		Log.i("=====aaa",rect.left+"");
////		/**
////		 * 获取扫描框右边的值
////		 */
////		rect.right =  (int) (middleRect.bottom* tempwidth);
////		Log.i("=====bbb", rect.right+"");
////		/**
////		 * 获取扫描框上边的值
////		 */
////		rect.top = (int) (middleRect.left * tempheight);
////		Log.i("=====dddd", middleRect.top+"====top="+rect.top);
////		/**
////		 * 获取扫描框下边的值
////		 */
////		rect.bottom = (int) (middleRect.right * tempheight);
////		Log.i("=====eeee", middleRect.bottom+"====bottom="+rect.bottom);
////		return rect;
////	}
////
//
//	/**
//	 *
//	 * <p>TODO(获取扫描框大小)</p>
//	 * @author sjyuan
//	 * @date Sep 23, 2014 12:20:28 PM
//	 * @param w
//	 * @param h
//	 * @return
//	 * @see
//	 *
//	 *
//	 */
//	public Rect getScanImageRect(int w, int h) {
//		Rect rect = new Rect();
//		double heightRate = h / ((double) measureedHeight);
//		double widthRate = w / ((double) measureedWidth);
//
//		/**
//		 * 获取扫描框左边值
//		 */
//		rect.top =  (int) (middleRect.left* widthRate);
//		//Log.i("=====aaa",rect.left+"");
//		/**
//		 * 获取扫描框右边的值
//		 */
//		rect.bottom =  (int) (middleRect.right* widthRate);
//		//Log.i("=====bbb", rect.right+"");
//		/**
//		 * 获取扫描框上边的值
//		 */
//		rect.left = (int) (middleRect.top * heightRate);
//		///Log.i("=====dddd", middleRect.top+"====top="+rect.top);
//		/**
//		 * 获取扫描框下边的值
//		 */
//		rect.right = (int) (middleRect.bottom * heightRate);
//		//Log.i("=====eeee", middleRect.bottom+"====bottom="+rect.bottom);
//
//		return rect;
//	}
//
//	/**
//	 *@date 创建时间 2017/4/15
//	 *@author 328798
//	 *@company 德邦物流
//	 *@name：zhongshuiping
//	 *@Description 计算控件的大小
//	 */
//	@Override
//	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//		/**
//		 * 获取宽度
//		 */
//			measureedWidth = MeasureSpec.getSize(widthMeasureSpec);
//		/**
//		 * 获取高度
//		 */
//			measureedHeight = MeasureSpec.getSize(heightMeasureSpec);
//		/**
//		 * 获取中间对焦框的高度值
//		 */
//		middleHeight=(int)(measureedHeight* Constants.FOCUS_FRAME_WIDE/ Constants.FOCUS_FRAME_FIVE) ;
//		/**
//		 * 获取上面半透明矩形的高度值
//		 */
//		topHeight = (int)(measureedHeight-middleHeight)/2;
//
//		/**
//		 * 获取左边半透明矩形的宽度值==对焦框的x起点坐标
//		 */
//		weight=(measureedWidth-(middleHeight* Constants.FOCUS_FRAME_HEIGHT/ Constants.FOCUS_FRAME_THREE))/ Constants.FOCUS_FRAME_HEIGHT;
//		/**
//		 * 中间矩形对焦框
//		 */
//		middleRect.set(weight, topHeight, measureedWidth-weight,topHeight+middleHeight);
//
//		/**
//		 * 上面半透明矩形框
//		 */
//		topRect.set(Constants.FOCUS_FRAME_ZERO, Constants.FOCUS_FRAME_ZERO, measureedWidth, topHeight);
//		/**
//		 * 左边半透明矩形框
//		 */
//		leftRect.set(Constants.FOCUS_FRAME_ZERO,topHeight, weight, topHeight+middleHeight);
//		/**
//		 * 右边半透明矩形框
//		 */
//		rightRect.set(measureedWidth-weight, topHeight, measureedWidth, topHeight+middleHeight);
//		/**
//		 * 下边半透明矩形框
//		 */
//		bottomRect.set(Constants.FOCUS_FRAME_ZERO, topHeight+middleHeight, measureedWidth, measureedHeight);
//	}
//}





















/*
 * Copyright by Deppon and the original author or authors.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 * This document only allow internal use ,Any of your behaviors using the file
 * not internal will pay legal responsibility.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 * You may learn more information about Deppon from
 *
 *
 *
 *      http://www.deppon.com
 *
 *
 */
package com.plt.yzplatform.camera;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.plt.yzplatform.R;


public class FinderView extends View {
	/*
         * 上、下、左、右矩阵
         */
	private Rect topRect = new Rect();
	private Rect bottomRect = new Rect();
	private Rect rightRect = new Rect();
	private Rect leftRect = new Rect();
	private Rect middleRect = new Rect();
//	private Rect idcardRect = new Rect();

	public static boolean bFocused = false;
	/**
	 * 常量ANIMATION_DELAY
	 */
	private static final long ANIMATION_DELAY =30;
	/**
	 * finderMaskPaint
	 */
	private Paint finderMaskPaint;
	/**
	 * measureedWidth
	 */
	private int measureedWidth;
	/**
	 * measureedHeight
	 */
	private int measureedHeight;

	/***
	 * 四个角的线
	 */
	private Paint linerPaint;
	/**
	 * 画框
	 */
	private Paint framePaint;

	private Paint idcardPaint;
	/**
	 * 上边半透明矩形框的高
	 */
	private int topHeight;
	/**
	 * 中间对焦框的高
	 */
	private  int middleHeight;
	/**
	 * 左边对焦框的宽
	 */
	private  int leftWidth;
	/**
	 /**
	 *
	 * <p>Title: </p>
	 * <p>Description: </p>
	 * @param context
	 * @author wanggang077@deppon.com/200939
	 * @date 2015-8-4 下午4:18:22
	 */
	public FinderView(Context context) {
		super(context);
		init(context);
	}

	public FinderView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	/*
	 * (非 Javadoc)
	 * <p>Title: onDraw</p>
	 * <p>Description: </p>
	 * @param canvas
	 * @see android.view.View#onDraw(android.graphics.Canvas)
	 * @author wanggang077@deppon.com/200939
	 * @date 2015-8-4 下午4:18:38
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawRect(leftRect, finderMaskPaint);
		canvas.drawRect(topRect, finderMaskPaint);
		canvas.drawRect(rightRect, finderMaskPaint);
		canvas.drawRect(bottomRect, finderMaskPaint);
		canvas.drawRect(middleRect, framePaint);
//		canvas.drawRect(idcardRect, idcardPaint);
		if (bFocused){
			linerPaint.setColor(Color.GREEN);//设置颜色
		}
		else {
			linerPaint.setColor(Color.GRAY);//设置颜色
		}

		/**左边线*/
		canvas.drawLine(leftWidth,topHeight,leftWidth,topHeight+middleHeight,linerPaint);
		/**右边线*/
		canvas.drawLine(leftWidth,topHeight+middleHeight,measureedWidth-leftWidth,topHeight+middleHeight,linerPaint);//左边线
		/**上边线*/
		canvas.drawLine(leftWidth,topHeight,measureedWidth-leftWidth,topHeight,linerPaint);
		/**下边线*/
		canvas.drawLine(measureedWidth-leftWidth,topHeight,measureedWidth-leftWidth,topHeight+middleHeight,linerPaint);//左边线
	}

	private void init(Context context) {
		int finderMask = context.getResources().getColor(R.color.cover_color);
		/**
		 * 创建四个半透明矩形框的画笔
		 */
		finderMaskPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		finderMaskPaint.setColor(finderMask);
//		finderMaskPaint.setAlpha(80);\
		finderMaskPaint.setARGB(128,30,30,30);
		/**
		 * 创建中间对焦框的画笔
		 */
		framePaint=new Paint();
		framePaint.setAntiAlias(true);// 抗锯齿
		framePaint.setDither(true);// 防抖动
		framePaint.setColor(Color.WHITE);//设置颜色
		framePaint.setStrokeWidth(0.8f);//设置线条大小
		framePaint.setStyle(Paint.Style.STROKE);// 实心
		/**
		 * 创建四个角的画笔
		 */
		linerPaint=new Paint();
		linerPaint.setAntiAlias(true);// 抗锯齿
		linerPaint.setDither(true);// 防抖动
		linerPaint.setColor(Color.GREEN);//设置颜色
		linerPaint.setStrokeWidth(Constants.FOCUS_FRAME_EIGHT);
		linerPaint.setStyle(Paint.Style.FILL);// 实心


		/**
		 * 创建中间对焦框的画笔
		 */
		idcardPaint=new Paint();
		idcardPaint.setAntiAlias(true);// 抗锯齿
		idcardPaint.setDither(true);// 防抖动
		idcardPaint.setColor(Color.GREEN);//设置颜色
		idcardPaint.setStrokeWidth(0.8f);//设置线条大小
		idcardPaint.setStyle(Paint.Style.STROKE);// 实心
	}

	public Rect getScanImageRect(int w, int h) {
		Rect rect = new Rect();
		double heightRate = h / ((double) measureedHeight);
		double widthRate = w / ((double) measureedWidth);
		rect.top =  (int) (middleRect.left* widthRate);
		rect.bottom =  (int) (middleRect.right* widthRate);
		rect.left = (int) (middleRect.top * heightRate);
		rect.right = (int) (middleRect.bottom * heightRate);
		return rect;
	}


	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		/**
		 * 获取宽度
		 */
		measureedWidth = MeasureSpec.getSize(widthMeasureSpec);
		/**
		 * 获取高度
		 */
		measureedHeight = MeasureSpec.getSize(heightMeasureSpec);

		/**
		 * 获取中间对焦框的高度值
		 */
		middleHeight=(int)(measureedHeight*Constants.FOCUS_FRAME_WIDE/Constants.FOCUS_FRAME_FIVE);
		/**
		 * 获取上面半透明矩形的高度值
		 */
		topHeight = (int)(measureedHeight-middleHeight)/2;
		/**
		 * 获取左边半透明矩形的宽度值==对焦框的x起点坐标
		 */
		leftWidth=(measureedWidth-(middleHeight*Constants.FOCUS_FRAME_HEIGHT/Constants.FOCUS_FRAME_THREE))/ Constants.FOCUS_FRAME_HEIGHT;
		/**
		 * 中间矩形对焦框
		 */
		middleRect.set(leftWidth, topHeight, measureedWidth-leftWidth,topHeight+middleHeight);
//		idcardRect.set(leftWidth*3,450,210,1020);
//		idcardRect.set(leftWidth*3,middleHeight*5/11,leftWidth*7,middleHeight+leftWidth);
		/**
		 * 上面半透明矩形框
		 */
		topRect.set(Constants.FOCUS_FRAME_ZERO, Constants.FOCUS_FRAME_ZERO, measureedWidth, topHeight);
		/**
		 * 左边半透明矩形框
		 */
		leftRect.set(Constants.FOCUS_FRAME_ZERO,topHeight, leftWidth, topHeight+middleHeight);
		/**
		 * 右边半透明矩形框
		 */
		rightRect.set(measureedWidth-leftWidth, topHeight, measureedWidth, topHeight+middleHeight);
		/**
		 * 下边半透明矩形框
		 */
		bottomRect.set(Constants.FOCUS_FRAME_ZERO, topHeight+middleHeight, measureedWidth, measureedHeight);
	}
}
