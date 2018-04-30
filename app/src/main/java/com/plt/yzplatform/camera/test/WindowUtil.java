package com.plt.yzplatform.camera.test;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

public class WindowUtil {

	//默认为480*800
	private static int widthCacle = 720;
	private static int heightCacle = 1280;
	private static WindowManager wm = null;
	private static Point size = null;
	private static Display display = null;

	/*****
	 * 获取不同分辨率与480的比率
	 * @param context
	 * @return
	 */
	public static float getWidthScale(Context context){

		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Point size = new Point();
		Display display = wm.getDefaultDisplay();
		display.getSize(size);

		return ((float) size.x / widthCacle);
	}

	public static float getHeightScale(Context context){

		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Point size = new Point();
		Display display = wm.getDefaultDisplay();
		display.getSize(size);
		return ((float) size.y / heightCacle);
	}

	/*****
	 * 根据不同的分辨率获取值
	 * @param context
	 * @param value
	 * @return
	 */
	public static int getWidthScaleValue(Context context, int value){
		return getWidthScale(context) != 1 ? ((int) (getWidthScale(context) * value) + 10) : value;
	}

	/*****
	 * 根据不同的分辨率获取值
	 * @param context
	 * @param value
	 * @return
	 */
	public static int getWidthScaleValue(Context context, float value){
		return (int) (getWidthScale(context) * value);
	}

	public static int getHeightScaleValue(Context context, int value){
		return getHeightScale(context) != 1 ? ((int) (getHeightScale(context) * value) + 10) : value;
	}

}
