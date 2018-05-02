package com.xtzhangbinbin.jpq.camera;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 作者:
 * <p/>
 * 工号: ${USER_NAME}
 */
public class FileUtils {


    /**
     * 复制单个文件
     *
     * @param inStream
     *            String 原文件路径 如：c:/fqf.txt
     * @param newPath
     *            String 复制后路径 如：f:/fqf.txt
     * @return boolean
     */
    public static boolean copyFile(InputStream inStream, String newPath) {
        FileOutputStream fs = null;
        try {
            int bytesum = 0;
            int byteread = 0;

            File file =new File(newPath);
            file.deleteOnExit();
            mkdirs(file.getParentFile());
            fs= new FileOutputStream(file);
            byte[] buffer = new byte[Constants.NUMBER_1024];
            while ((byteread = inStream.read(buffer)) != -1) {
                bytesum += byteread; // 字节数 文件大小
                fs.write(buffer, 0, byteread);
            }
            inStream.close();
            fs.close();
            Log.v("FileUtils", "文件copy成功 总共拷贝" + bytesum
                    / Constants.NUMBER_1024 + " KB");
        } catch (Exception e) {
            e.printStackTrace();
            return false;

        }finally {
            try {

                if(fs!=null){
                    fs.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;

    }


    /**
     *
     * @Title: mkdirs
     * @Description: mkdirs
     * @param @param file
     * @param @return    设定文件
     * @return boolean    返回类型
     * @author
     * @throws
     */
    public static boolean mkdirs(File file) {

        if (!file.exists()) {
            file.mkdirs();
        }

        // file=null;

        return true;
    }


    /**
     *创建大图路径
     * @return
     */
    public static String saveMaxImgFile(){
        /***
         * 设置保存文件夹
         */
        File externalStorageDirectory = Environment.getExternalStorageDirectory();
        String absolutePath = externalStorageDirectory.getAbsolutePath();
        String temp = absolutePath + "/maxIdCardPath/";



        return temp;
    }

    /***
     * 创建小图路径
     * @return
     */
    public static String saveMinImgFile(){
        /***
         * 设置保存文件夹
         */
        File externalStorageDirectory = Environment.getExternalStorageDirectory();
        String absolutePath = externalStorageDirectory.getAbsolutePath();
        String temp = absolutePath + "/minIdCardPath/";

        return temp;
    }


}
