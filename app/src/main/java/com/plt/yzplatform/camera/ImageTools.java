package com.plt.yzplatform.camera;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * 
 * @ClassName: ImageTools
 * @Description: 图片工具类
 * @author
 * @date 2015-5-16 下午5:32:35
 * 
 */
public class ImageTools {
	/**
	 * 读取图片属性：旋转的角度
	 * @param path 图片绝对路径
	 * @return degree旋转的角度
	 */
	public static int readPictureDegree(String path) {
		int degree  = Constants.NUMBER_ZERO;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
				case ExifInterface.ORIENTATION_ROTATE_90:
					degree = Constants.NUMBER_NINETY;
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					degree = Constants.NUMBER_HUNDRED_EIGHTY;
					break;
				case ExifInterface.ORIENTATION_ROTATE_270:
					degree = Constants.NUMBER_270;
					break;
				default:
					break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	/**
	 * 把图片byte流转换成bitmap
	 */
	public static Bitmap byteToBitmap(byte[] data) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inTempStorage = new byte[100 * 1024];
		options.inJustDecodeBounds = true;
		Bitmap b = BitmapFactory.decodeByteArray(data, 0, data.length, options);
		int i = 0;
		while (true) {
			if ((options.outWidth >> i <= 1000)
					&& (options.outHeight >> i <= 1000)) {
				options.inSampleSize = (int) Math.pow(2.0D, i);
				options.inJustDecodeBounds = false;
				b = BitmapFactory.decodeByteArray(data, 0, data.length, options);
				break;
			}
			i += 1;
		}
		return b;
	}
	/**
	 * 按照一定的比例 将图片的大小压缩到想要的大小
	 * @param bm
	 * @param filePath
	 * @param fileName
	 * @param q
	 * @return
	 * @throws IOException
	 */
	public  static String compressImage(Bitmap bm, String filePath, String fileName, int q) throws IOException {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.JPEG, Constants.NUMBER_HUNDRED, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = q;
		while (baos.toByteArray().length / Constants.NUMBER_1024 > Constants.NUMBER_EIETY) {    //循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();//重置baos即清空baos
			options -= Constants.NUMBER_TEN;//每次都减少10
			//如果这个参数值不符合要求  会报错  跑出异常
			if(options < Constants.NUMBER_ZERO || options > Constants.NUMBER_HUNDRED){
				break;
			}
			bm.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中

		}

		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中


		File outputFile=new File(filePath,fileName);



		FileOutputStream out = new FileOutputStream(outputFile);


		byte[] b = new byte[Constants.NUMBER_1024 * Constants.NUMBER_EIGHT ];
		int len;
		while((len = isBm.read(b)) != Constants.NUMBER_NEGATIVE_ONE){
			out.write(b,Constants.NUMBER_ZERO,len);
		}

		return outputFile.getPath();
	}

}
