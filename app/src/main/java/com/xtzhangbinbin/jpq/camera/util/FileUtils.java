package com.xtzhangbinbin.jpq.camera.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtils {

	private File baseFolder;
	private Activity context;
	private String basePath = "";

	public FileUtils(Activity context) {
		this.context = context;
		/* 获取当前外部存储设备的目录（如sd卡） */
		baseFolder = context.getExternalCacheDir();
		basePath = baseFolder.getPath() + "/";
	}


	/*****
	 * 创建文件
	 * @param fileName 所需创建的文件的名称
	 * @return
	 */
	public File createFile(String fileName) {
		File file = new File(basePath + fileName);
		try {
			file.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return file;
	}

	/*****
	 * 删除文件
	 * @param fileName 删除文件夹的名称
	 * @return
	 */
	public File deleteFile(String fileName) {
		File file = new File(basePath + fileName);
		try {
			file.delete();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return file;
	}

	/*****
	 * 创建目录
	 * @param dirName  所需创建文件夹的名称
	 * @return
	 */
	public File createDir(String dirName) {
		File dir = new File(basePath + dirName);
		dir.mkdirs();
		return dir;
	}

	/**
	 * 删除指定目录
	 * @param dirName
	 * @return
	 */
	public boolean deleteDir(String dirName) {
		File file = new File(basePath +dirName);
		if (file != null && file.isDirectory()) {
			String[] children = file.list();
			for (String aChildren : children) {
				boolean success = deleteDir(new File(file, aChildren).getPath());
				if (!success) {
					return false;
				}
			}
		}
		return file.delete();
	}

	/*****
	 * 判断文件是否存在
	 *
	 * @param fileName
	 * @return
	 */
	public boolean isFileExits(String fileName) {
		File file = new File(basePath + fileName);
		return file.exists();
	}

	/**
	 * 保存文件到指定路径
	 * @param filePath  文件保存路径
	 * @param input  文件流
	 * @return
	 */
	public File saveFile(String filePath, InputStream input) {

		File file = null;
		OutputStream out = null;
		long test = 0;
		try {
			//判断所需保存文件路径中的文件夹是否存在
			if(null != filePath && filePath.contains(File.separator)){
				//创建路径中所需的文件夹
				String dir = filePath.substring(0, filePath.lastIndexOf(File.separator));
				createDir(dir);
			}
			//创建新文件
			file = createFile(filePath);

			out = new FileOutputStream(file);
			/* 创建操作文件的缓存 */
			byte[] buffer = new byte[20 * 1024];
			/* 将文件读入到缓存当中，如果完成读取，则等于-1 */

			int value = 0;

			while ((value = input.read(buffer)) != -1) {
				/* 将缓存当中的数据写入文件当中 */
				out.write(buffer, 0, value);
				/* 清空输入流 */

			}
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
				input.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return file;
	}

	/**
	 * 保存bitmap图片
	 * @param filePath  文件保存路径
	 * @param bm 图片对象
	 * @return
	 */
	public boolean saveFile(String filePath, Bitmap bm){

		File file = null;
		OutputStream out = null;
		boolean flag = false;
		try {
			//判断所需保存文件路径中的文件夹是否存在
			if(null != filePath && filePath.contains(File.separator)){
				//创建路径中所需的文件夹
				String dir = filePath.substring(0, filePath.lastIndexOf(File.separator));
				createDir(dir);
			}
			//创建新文件
			file = createFile(filePath);
			out = new FileOutputStream(file);
			bm.compress(CompressFormat.PNG, 100, out);
			out.flush();
			flag = true;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return flag;
		}
	}

	/**
	 * 保存文件到指定路径
	 * @param filePath 文件路径
	 * @param fromFile  源文件
	 * @return
	 */
	public File saveFile(String filePath, File fromFile) {

		File file = null;
		OutputStream out = null;
		InputStream input = null;
		long test = 0;
		try {
			//判断所需保存文件路径中的文件夹是否存在
			if(null != filePath && filePath.contains(File.separator)){
				//创建路径中所需的文件夹
				String dir = filePath.substring(0, filePath.lastIndexOf(File.separator));
				createDir(dir);
			}
			//创建新文件
			file = createFile(filePath);

			out = new FileOutputStream(file);
			input = new FileInputStream(fromFile);
			/* 创建操作文件的缓存 */
			byte[] buffer = new byte[20 * 1024];
			/* 将文件读入到缓存当中，如果完成读取，则等于-1 */

			int value = 0;

			while ((value = input.read(buffer)) != -1) {
				/* 将缓存当中的数据写入文件当中 */
				out.write(buffer, 0, value);
				/* 清空输入流 */

			}
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
				input.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return file;
	}

	/**
	 * 根据文件名称返回文件对象
	 * @param fileName
	 * @return
	 */
	public File getFile(String fileName){
		File file = new File(basePath + fileName);
		return file;
	}
}
