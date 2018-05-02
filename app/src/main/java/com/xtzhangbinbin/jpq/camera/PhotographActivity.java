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
package com.xtzhangbinbin.jpq.camera;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.camera.test.ModelEnums;
import com.xtzhangbinbin.jpq.camera.test.WindowUtil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.xtzhangbinbin.jpq.camera.test.ModelEnums.MODEL_CEMIAN;
import static com.xtzhangbinbin.jpq.camera.test.ModelEnums.MODEL_CHELUN;
import static com.xtzhangbinbin.jpq.camera.test.ModelEnums.MODEL_CHEYAOSHI;
import static com.xtzhangbinbin.jpq.camera.test.ModelEnums.MODEL_CHIMENGCAOKONGQU;
import static com.xtzhangbinbin.jpq.camera.test.ModelEnums.MODEL_DANGWEI;
import static com.xtzhangbinbin.jpq.camera.test.ModelEnums.MODEL_DENGGUANGKONGZHI;
import static com.xtzhangbinbin.jpq.camera.test.ModelEnums.MODEL_DIPAN;
import static com.xtzhangbinbin.jpq.camera.test.ModelEnums.MODEL_FADONGJI;
import static com.xtzhangbinbin.jpq.camera.test.ModelEnums.MODEL_FANGXIANGPAN;
import static com.xtzhangbinbin.jpq.camera.test.ModelEnums.MODEL_HOUBEIXIANG;
import static com.xtzhangbinbin.jpq.camera.test.ModelEnums.MODEL_HOUCHEDENG;
import static com.xtzhangbinbin.jpq.camera.test.ModelEnums.MODEL_HOUZUOYI;
import static com.xtzhangbinbin.jpq.camera.test.ModelEnums.MODEL_QIANDADENG;
import static com.xtzhangbinbin.jpq.camera.test.ModelEnums.MODEL_QIANFANG45DU;
import static com.xtzhangbinbin.jpq.camera.test.ModelEnums.MODEL_QIANZUOYI;
import static com.xtzhangbinbin.jpq.camera.test.ModelEnums.MODEL_TIANCHUANG;
import static com.xtzhangbinbin.jpq.camera.test.ModelEnums.MODEL_XIANSHIPING;
import static com.xtzhangbinbin.jpq.camera.test.ModelEnums.MODEL_YAOSHIKONG;
import static com.xtzhangbinbin.jpq.camera.test.ModelEnums.MODEL_YIBIAOPAN;
import static com.xtzhangbinbin.jpq.camera.test.ModelEnums.MODEL_YUSHUAQI;
import static com.xtzhangbinbin.jpq.camera.test.ModelEnums.MODEL_ZHENGHOUFANG;
import static com.xtzhangbinbin.jpq.camera.test.ModelEnums.MODEL_ZHENGQIANFANG;
import static com.xtzhangbinbin.jpq.camera.test.ModelEnums.MODEL_ZHONGKONGQU;
import static com.xtzhangbinbin.jpq.camera.test.ModelEnums.MODEL_ZHUOQIANCHEMENG;


/**
 * 自定义照相机，用于拍摄二手车照片
 */
@SuppressWarnings("all")
public class PhotographActivity extends AppCompatActivity implements SurfaceHolder.Callback, SensorEventListener {
    protected static final String TAG = "PhotographActivity";
    private Camera mCamera;
    private SurfaceHolder mHolder;
    private SurfaceView surface_view;
    private FinderView finder_view;
    private Button flashlightBtn;
    // 定义传感器管理器
    private SensorManager sensorMag = null;
    // 闪光灯默认关闭
    private boolean flashlight = false;
    public boolean bIsFocus = false;
    public boolean bIsEditBySensor = false;
    public boolean bIsFocusing = false;
    private static boolean flagOnce = false;
    //识别参数用全局变量，提高效率
    int[] infoData = new int[6];
    private Button btn_name;
    /**
     * 回调函数开关
     */
    private boolean Enabled = false;
    /**
     * 消息常量值
     */
    private static final int PHOTOGRAPH = Constants.MESSAGE_ONE;
    private static final int TEMPLATE = Constants.MESSAGE_TWO;
    private String fileName;
    private Bitmap mBitmap;

    public static boolean bIsInitServiceNet = false;
    public String imgPath = "";
    private WatermarkTextView remark;
    private WatermarkTextView watermarkone;
    private String minimgpath = "";
    private RelativeLayout cameraLayout;
    private LinearLayout preview_border;
    private ImageView carModel;

    private int cameraWidth = 9;
    private int cameraHeight = 16;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case PHOTOGRAPH:
                    if (!Enabled) {
                        mCamera.takePicture(null, null, myJpegCallback);
                        Enabled = false;
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // 设置视图
        setContentView(R.layout.activity_photograph);
        WindowManager WM = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        WM.getDefaultDisplay().getMetrics(outMetrics);

        //初始化模板
        onLoadResources();
        //初始化界面
        init();
        Monitor();
    }

    /**
     * 传感器注销事件
     */
    private void SensorCancellation() {
        if (SensorUtil.isStart()) {
            SensorUtil.setIsStart(false);
            sensorMag.unregisterListener(PhotographActivity.this);
        }
    }

    /**
     * 拍照按钮处理事件
     */
    private void Monitor() {
        /**拍照按钮*/
        btn_name.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCamera != null) {
                    SoundUtils.playerScanOkWav(getApplicationContext(), 1);
                    SensorCancellation();
                    btn_name.setEnabled(false);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Message message = new Message();
                            message.what = PHOTOGRAPH;
                            handler.sendMessage(message);
                        }
                    }).start();
                }
            }
        });
        /**点击屏幕对焦*/
        finder_view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mCamera != null) {
                    mCamera.autoFocus(autoFocusCB);
                }

            }
        });
        /**闪光灯按钮*/
        flashlightBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                openFlashLamp();
            }
        });
    }

    /**
     * 初始化模板
     */
    public void onLoadResources() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = TEMPLATE;
                handler.sendMessage(message);
            }
        }).start();
    }


    /**
     * 初始化界面组件
     */
    private void init() {
        //获取相机布局组件
        cameraLayout = (RelativeLayout) findViewById(R.id.cameraLayout);
        //设置宽和高
        cameraLayout.setLayoutParams(new LinearLayout.LayoutParams(getWidth(), getHeight()));
        //获取预览视图组件
        surface_view = (SurfaceView) findViewById(R.id.surface_view);
        //设置预览视图组件的宽和高
        surface_view.setLayoutParams(new RelativeLayout.LayoutParams(getWidth(), getHeight()));
        btn_name = (Button) findViewById(R.id.btn_name);
        /**自定义textview*/
        remark = (WatermarkTextView) findViewById(R.id.remark);
        watermarkone = (WatermarkTextView) findViewById(R.id.watermarkone);
        /**设置textview旋转90度*/
        remark.setDegrees(90);
        watermarkone.setDegrees(90);

        finder_view = (FinderView) findViewById(R.id.finder_view);
        finder_view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mHolder = surface_view.getHolder();
        mHolder.setFormat(PixelFormat.TRANSPARENT);//translucent半透明 transparent透明
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mHolder.addCallback(this);
        restartSensor();
        updateCameraParameters();
        // 闪光灯
        flashlightBtn = (Button) findViewById(R.id.flashlightBtn);
        //初始化模板
        initModel();
    }

    /**
     * 初始化模板
     * 将各种预览模板图片加载到相机前面
     */
    public void initModel() {

        carModel = (ImageView) findViewById(R.id.car_model);
        try {
            //获取所以车辆模板的枚举对象
            //获取指定车辆的模板
            ModelEnums model = ModelEnums.getByName(MODEL_YAOSHIKONG.name);
            switch (model) {
                case MODEL_ZHENGQIANFANG:
                    setModel(MODEL_ZHENGQIANFANG, 800, 800);
                    break;
                case MODEL_QIANFANG45DU:
                    setModel(MODEL_QIANFANG45DU, 900, 900);
                    break;
                case MODEL_QIANDADENG:
                    setModel(MODEL_QIANDADENG, 900, 900);
                    break;
                case MODEL_CEMIAN:
                    setModel(MODEL_CEMIAN, 900, 900);
                    break;
                case MODEL_HOUCHEDENG:
                    setModel(MODEL_HOUCHEDENG, 1000, 1000);
                    break;
                case MODEL_ZHENGHOUFANG:
                    setModel(MODEL_ZHENGHOUFANG, 1000, 1000);
                    break;
                case MODEL_CHEYAOSHI:
                    setModel(MODEL_CHEYAOSHI, 800, 800);
                    break;
                case MODEL_ZHUOQIANCHEMENG:
                    setModel(MODEL_ZHUOQIANCHEMENG, 1000, 1000);
                    break;
                case MODEL_CHIMENGCAOKONGQU:
                    setModelRemark(MODEL_CHIMENGCAOKONGQU);
                    break;
                case MODEL_QIANZUOYI:
                    setModel(MODEL_QIANZUOYI, 1100, 1100);
                    break;
                case MODEL_ZHONGKONGQU:
                    setModel(MODEL_ZHONGKONGQU, 1100, 1100);
                    break;
                case MODEL_FANGXIANGPAN:
                    setModel(MODEL_FANGXIANGPAN, 900, 900);
                    break;
                case MODEL_YIBIAOPAN:
                    setModel(MODEL_YIBIAOPAN, 1000, 1000);
                    break;
                case MODEL_XIANSHIPING:
                    setXianShiPingModel(MODEL_XIANSHIPING, 500, 550);
                    break;
                case MODEL_DANGWEI:
                    setModel(MODEL_DANGWEI, 1000, 1000);
                    break;
                case MODEL_TIANCHUANG:
                    setModel(MODEL_TIANCHUANG, 1000, 1000);
                    break;
                case MODEL_HOUZUOYI:
                    setModel(MODEL_HOUZUOYI, 1200, 1200);
                    break;
                case MODEL_HOUBEIXIANG:
                    setModel(MODEL_HOUBEIXIANG, 1200, 1200);
                    break;
                case MODEL_FADONGJI:
                    remark.setText(MODEL_FADONGJI.remark);
                    break;
                case MODEL_DIPAN:
                    remark.setText(MODEL_DIPAN.remark);
                    break;
                case MODEL_CHELUN:
                    setModel(MODEL_CHELUN, 1200, 1200);
                    break;
                case MODEL_DENGGUANGKONGZHI:
                    setModel(MODEL_DENGGUANGKONGZHI, 900, 900);
                    break;
                case MODEL_YUSHUAQI:
                    setModel(MODEL_YUSHUAQI, 900, 900);
                    break;
                case MODEL_YAOSHIKONG:
                    setModel(MODEL_YAOSHIKONG, 900, 900);
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.w("error", e);
        }
    }

    /**
     * 设置模板图片
     * 用于通用模板
     */
    public void setModel(ModelEnums model, int height, int wdith) throws IOException {
        Bitmap modelImg;
        RelativeLayout.LayoutParams param;
        modelImg = BitmapFactory.decodeStream(this.getAssets().open("carmodel/" + model.name + ".png"));
        carModel.setImageBitmap(modelImg);
        remark.setText(model.remark);
        //控制图片的高度，宽度自适应
        param = new RelativeLayout.LayoutParams(WindowUtil.getHeightScaleValue(this, height), WindowUtil.getWidthScaleValue(this, wdith));
        param.addRule(RelativeLayout.CENTER_IN_PARENT);
        carModel.setLayoutParams(param);
    }

    /**
     * 设置模板图片
     * 用于无需设置宽和高的模板
     */
    public void setModel(ModelEnums model) throws IOException {
        Bitmap modelImg;
        RelativeLayout.LayoutParams param;
        modelImg = BitmapFactory.decodeStream(this.getAssets().open("carmodel/" + model.name + ".png"));
        carModel.setImageBitmap(modelImg);
        remark.setText(model.remark);
        //控制图片的高度，宽度自适应
        param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        param.addRule(RelativeLayout.CENTER_IN_PARENT);
        carModel.setLayoutParams(param);
    }

    /**
     * 设置显示屏
     * 因为显示屏在拍摄时，模板不在正中间，所以单独设置
     */
    public void setXianShiPingModel(ModelEnums model, int height, int wdith) throws IOException {
        Bitmap modelImg;
        RelativeLayout.LayoutParams param;
        modelImg = BitmapFactory.decodeStream(this.getAssets().open("carmodel/" + model.name + ".png"));
        carModel.setImageBitmap(modelImg);
        remark.setText(model.remark);
        //控制图片的高度，宽度自适应
        param = new RelativeLayout.LayoutParams(WindowUtil.getHeightScaleValue(this, height), WindowUtil.getWidthScaleValue(this, wdith));
        param.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        param.addRule(RelativeLayout.CENTER_VERTICAL);
        carModel.setLayoutParams(param);
    }

    /**
     * 设置无模板图片的拍摄说明
     *
     * @param image
     * @param height
     * @param wdith
     * @throws IOException
     */
    public void setModelRemark(ModelEnums model) throws IOException {
        remark.setText(model.remark);
    }

    /**
     * 传感器管理器
     */
    public void restartSensor() {


        // 获取系统传感器管理器
        if (sensorMag == null) {
            sensorMag = (SensorManager) getSystemService(SENSOR_SERVICE);
        }
        // 判断系统是否存在所需的传感器
        if (sensorMag != null) {
            SensorUtil.startSensor(sensorMag, this);
        }
    }


    /**
     * 开启相机
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        bIsEditBySensor = false;
        //nHaveDataCnt = 0;

        try {
            mCamera = Camera.open();
            bIsFocus = false;
            finder_view.bFocused = false;
//            watermark.setVisibility(View.VISIBLE);
            //finder_view.invalidate();
            Log.i(TAG, "===========================surfaceCreated bIsFocus = false");
            //myCamera.setPreviewDisplay(mHolder);
            // 打开相机异常
        } catch (Exception e) {
            // 打开相机异常
            if (null != mCamera) {
                mCamera.release();
                mCamera = null;
            }
        }
    }


    /**
     * 相机预览数据
     */
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        try {

            // 旋转镜头
            mCamera.setDisplayOrientation(90);
            mCamera.setPreviewDisplay(mHolder);
            updateCameraParameters();
            restartSensor();
            mCamera.startPreview();
            // 开始预览
            mCamera.autoFocus(autoFocusCB);
        } catch (Exception e) {
            // 设置相机参数异常

        }
    }


    /**
     * 被暂停或收回cpu和其他资源时调用时调stopPreview释放资源
     */
    public void onPause() {
        super.onPause();
        sensorMag.unregisterListener(this);
        stopPreview();
    }


    /**
     * 释放资源
     */
    private void stopPreview() {
        if (mCamera != null) {
            try {
                mCamera.setPreviewDisplay(null);
                mCamera.stopPreview();
            } catch (Exception e) {

            }
        }
    }


    /**
     * 闪光灯操作
     */
    private void openFlashLamp() {
        if (mCamera == null) {
            mCamera = getCameraInstance();
        }
        Parameters params = mCamera.getParameters();
        /**
         * 闪光灯
         */
        if (flashlight) {
            // 关闭闪光灯
            params.setFlashMode(Parameters.FLASH_MODE_OFF);
            flashlightBtn.setBackgroundResource(R.drawable.zx_code_closelight);
            flashlight = false;
        } else {
            // 打开闪光灯
            params.setFlashMode(Parameters.FLASH_MODE_TORCH);
            flashlightBtn.setBackgroundResource(R.drawable.zx_code_openlight);
            flashlight = true;
        }
        mCamera.setParameters(params);
    }

    /**
     * 打开相机
     */
    public Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open();
            // 打开相机异常
        } catch (Exception e) {

        }
        return c;
    }


    /**
     * 关闭相机
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (flashlight) {
            if (mCamera != null) {
                Parameters params = mCamera.getParameters();
                // 关闭闪光灯
                params.setFlashMode(Parameters.FLASH_MODE_OFF);
            }
            flashlightBtn.setBackgroundResource(R.drawable.flashlightclose);
            flashlight = false;
        }
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
        // 判断是否开启传感器监听并注销监听
        SensorCancellation();
    }
    /**
     * 传感器精度改变事件
     */
    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1) {

    }
    /**
     * 传感器改变事件
     */
    @Override
    public void onSensorChanged(SensorEvent event) {

        // 判断相机是否准备好并且手机移动超过一定的范围
        if (mCamera != null && SensorUtil.isStart() && SensorUtil.isOverRange(event) && !bIsFocusing) {
            // 调用自动聚焦回调
            bIsFocus = false;
            bIsFocusing = true;
            finder_view.bFocused = false;
//            watermark.setVisibility(View.VISIBLE);
            finder_view.invalidate();
            //Log.i(TAG, "==================================onSensorChanged bIsFocus = false");

            mCamera.cancelAutoFocus();// 只有加上了这一句，才会自动对焦
            mCamera.autoFocus(autoFocusCB);
        }
    }

    /**
     * AutoFocusCallback自动对焦
     */
    AutoFocusCallback autoFocusCB = new AutoFocusCallback() {
        public void onAutoFocus(boolean success, Camera camera) {
            bIsFocusing = false;
            if (success) {
                bIsFocus = true;
                finder_view.bFocused = true;
//                watermark.setVisibility(View.GONE);
                finder_view.invalidate();
            } else {
                bIsFocus = false;
                finder_view.bFocused = false;
//                watermark.setVisibility(View.VISIBLE);
                finder_view.invalidate();
            }
        }
    };

    /**
     * 自定义相机所需要的参数
     */
    private void updateCameraParameters() {
        if (mCamera != null) {
            Parameters p = mCamera.getParameters();
            Size picSize = p.getPreviewSize();
            Size previewSize = getOptimalPreviewSize(p.getSupportedPreviewSizes(), (double) picSize.width / picSize.height);
            if (previewSize != null) {
                p.setPreviewSize(previewSize.width, previewSize.height);
            }
            picSize = p.getPictureSize();
            Size pictureSize = getOptimalPictureSize(p.getSupportedPictureSizes(), (double) picSize.width / picSize.height);

            if (pictureSize != null) {
                p.setPictureSize(pictureSize.width, pictureSize.height);
            }
            mCamera.setParameters(p);

        }
    }

    /**
     * 设定的屏幕的比例不是图片的比例
     * 匹配分辨率
     */
    private Size getOptimalPreviewSize(List<Size> sizes, double targetRatio) {
        if (sizes == null)
            return null;
        Size optimalSize = null;
        Collections.sort(sizes, new Comparator<Size>() {
            @Override
            public int compare(Size lhs, Size rhs) {
                return new Double(lhs.width).compareTo(new Double(rhs.width));
            }
        });
        for (int i = sizes.size() - 1; i >= 0; i--) {
            Size size = sizes.get(i);
            if (((Constants.EIGHT_HUNDRED < size.width && size.width < Constants.TWO_THOUSAND)
                    || (Constants.EIGHT_HUNDRED < size.height && size.height < Constants.TWO_THOUSAND))
                    && ((size.width * cameraWidth) == (size.height * cameraHeight))) {
                optimalSize = size;
                break;
            }
        }
        return optimalSize;
    }

    /**
     * 设置的是拍照的图片的比例
     * 匹配分辨率
     */
    private Size getOptimalPictureSize(List<Size> sizes, double targetRatio) {
        if (sizes == null)
            return null;
        Size optimalSize = null;
        Collections.sort(sizes, new Comparator<Size>() {
            @Override
            public int compare(Size lhs, Size rhs) {
                return new Double(lhs.width).compareTo(new Double(rhs.width));
            }
        });
        for (int i = sizes.size() - 1; i >= 0; i--) {
            Size size = sizes.get(i);
            if (((Constants.NUMBER_ONE_THOUSAN < size.width && size.width < Constants.NUMBER_TWO_THOUSAND)
                    || (Constants.NUMBER_ONE_THOUSAN < size.height && size.height < Constants.NUMBER_TWO_THOUSAND))
                    && ((size.width * cameraWidth) == (size.height * cameraHeight))) {
                optimalSize = size;
                break;
            }
        }
        /**如果没找到16/9的就选择最接近的*/
        if (optimalSize == null) {
            double dMin = 100.0;
            Size RightSize = null;
            for (Size size : sizes) {
                double fRate = size.width / (float) size.height;
                double fDistance = Math.abs(fRate - (double) cameraHeight / cameraWidth);
                //找最接近16比9的size;
                if (fDistance < dMin) {
                    dMin = fDistance;
                    RightSize = size;
                }
            }
            //最接近的值赋给变量optimalSize
            optimalSize = RightSize;
        }
        return optimalSize;
    }

    /**
     * 为了实现拍照的快门声音及拍照保存照片需要下面三个回调变量
     */
    Camera.ShutterCallback myShutterCallback = new Camera.ShutterCallback() {
        public void onShutter() {
            // TODO Auto-generated method stub
            //Log.i(tag, "myShutterCallback:onShutter...");
        }
    };

    /**
     * 无意中按返回键时要释放内存
     */
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        onPause();
        finish();
    }

    /**
     * 销毁
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        File file = new File(minimgpath);
        file.delete();
    }

    /**
     * 获取手机型号
     *
     * @return
     */
    public String getSystemModel() {
        return android.os.Build.MODEL;
    }


    /*给定一个Bitmap，进行保存*/
    public String saveJpeg() {
        String path = FileUtils.saveMaxImgFile();
        File folder = new File(path);
        if (!folder.exists()) //如果文件夹不存在则创建
        {
            folder.mkdirs();
        }
        /***
         * 路径
         */
        String fileName = path + System.currentTimeMillis() + ".jpg";
        Log.i(TAG, "saveJpeg:jpegName--" + fileName);

        return fileName;
    }

    /*给定一个Bitmap，进行保存*/
    public String saveminJpeg() {
        String path = FileUtils.saveMinImgFile();
        File folder = new File(path);
        if (!folder.exists()) //如果文件夹不存在则创建
        {
            folder.mkdirs();
        }
        /***
         * 路径
         */
        String fileName = path + System.currentTimeMillis() + ".jpg";
        Log.i(TAG, "saveJpeg:jpegName--" + fileName);

        return fileName;
    }

    /**
     * 拍照回调
     */

    Camera.PictureCallback myJpegCallback = new Camera.PictureCallback()
            //对jpeg图像数据的回调,最重要的一个回调
    {
        public void onPictureTaken(byte[] data, Camera camera) {
            if (null != data && data.length > 0) {
                mBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);//data是字节数据，将其解析成位图
                mCamera.stopPreview();
                Bitmap aa = centerSquareScaleBitmap(mBitmap, getPreviewHeight(), getWidth());
                String maximgPath = saveJpeg(aa);
                Intent intent = new Intent();
                intent.putExtra("maximgPath", maximgPath);
                Log.d(TAG, "onPictureTaken图片路径: " + maximgPath);
                setResult(0, intent);
                finish();
            }


        }
    };


    /*给定一个Bitmap，进行保存*/
    public String saveJpeg(Bitmap bm) {
        File externalStorageDirectory = Environment.getExternalStorageDirectory();
        String absolutePath = externalStorageDirectory.getAbsolutePath();
        String savePath = absolutePath + "/Customcamera/";
        File folder = new File(savePath);
        if (!folder.exists()) //如果文件夹不存在则创建
        {
            Log.w("test", folder.mkdir() + "");
        }
        long dataTake = System.currentTimeMillis();
        String jpegName = savePath + dataTake + ".jpg";
        Log.w("test", jpegName);
        File jpegFile = new File(jpegName);
        try {
            FileOutputStream fout = new FileOutputStream(jpegFile);
            BufferedOutputStream bos = new BufferedOutputStream(fout);

            //			//如果需要改变大小(默认的是宽960×高1280),如改成宽600×高800
            //			Bitmap newBM = bm.createScaledBitmap(bm, 600, 800, false);

            bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block

            e.printStackTrace();
        }
        return jpegName;
    }

    /**
     * 获取屏幕宽度
     *
     * @return
     */
    public int getWidth() {
        WindowManager manager = getWindowManager();
        DisplayMetrics metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);
        return (int) (metrics.widthPixels * 0.85);
    }

    /**
     * 获取屏幕高度
     *
     * @return
     */
    public int getHeight() {
        WindowManager manager = getWindowManager();
        DisplayMetrics metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);
        return (int) (getWidth() * ((double) cameraHeight / cameraWidth));
    }

    /**
     * 保存图片时，指定高度
     *
     * @return
     */
    public int getPreviewHeight() {
        WindowManager manager = getWindowManager();
        DisplayMetrics metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);
        return (int) (getWidth() * ((double) 6 / 4));
    }

    public static Bitmap centerSquareScaleBitmap(Bitmap bitmap, int width, int height) {
        if (null == bitmap || width <= 0 || height <= 0) {
            return null;
        }

        Bitmap result = bitmap;
        int widthOrg = bitmap.getWidth();
        int heightOrg = bitmap.getHeight();
        if (widthOrg >= width && heightOrg >= height) {
            //压缩到一个最小长度是edgeLength的bitmap
            int longerEdge = (int) (height * Math.max(widthOrg, heightOrg) / Math.min(widthOrg, heightOrg));
            int scaledWidth = widthOrg > heightOrg ? longerEdge : width;
            int scaledHeight = widthOrg > heightOrg ? height : longerEdge;
            Bitmap scaledBitmap;

            try {
                scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true);
            } catch (Exception e) {
                return null;
            }

            //从图中截取正中间的正方形部分。
            int xTopLeft = (scaledWidth - width) / 2;
            int yTopLeft = (scaledHeight - height) / 2;

            try {
                result = Bitmap.createBitmap(scaledBitmap, xTopLeft, yTopLeft, width, height);
                scaledBitmap.recycle();
            } catch (Exception e) {
                return null;
            }
        }

        return result;
    }
}



