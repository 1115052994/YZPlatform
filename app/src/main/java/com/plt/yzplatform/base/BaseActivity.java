package com.plt.yzplatform.base;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.plt.yzplatform.R;
import com.plt.yzplatform.config.Config;
import com.plt.yzplatform.utils.ActivityUtil;
import com.plt.yzplatform.utils.NetUtil;
import com.plt.yzplatform.utils.PhotoUtils;
import com.plt.yzplatform.utils.Prefs;
import com.plt.yzplatform.utils.ToastUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * Created by glp on 2018/4/16.
 */

public class BaseActivity extends AppCompatActivity{
    private static final String TAG = "baseActivit" ;
    //标题
    private TextView title;
    private ImageView leftImage;
    private TextView rightText;


    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        //横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ActivityUtil.addActivity(this);
    }

    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
        title = (TextView) findViewById(R.id.title);
        leftImage = (ImageView) findViewById(R.id.left_back);
        rightText = (TextView) findViewById(R.id.right_text);

        if (leftImage != null) {
            leftImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }
    }

    /**
     * 设置标题
     */
    public void setTitle(@NonNull String title) {
        if (this.title != null && !TextUtils.isEmpty(title)) {
            this.title.setText(title);
        }
    }

    public void setTitleVisibility(int i){
        title.setVisibility(i);
    }

    /**
     * 设置标题字号
     */
    public void setTitleSize(float titleSize) {
        title.setTextSize(titleSize);
    }

    /**
     * 设置返回点击
     */
    public void setLeftImageClickListener(View.OnClickListener clickListener) {
        if (leftImage != null) {
            leftImage.setOnClickListener(clickListener);
        }
    }


    /**
     * 设置右面点击
     */
    public void setRightTextClickListener(View.OnClickListener clickListener) {
        if (rightText != null) {
            rightText.setOnClickListener(clickListener);
        }
    }

    /**
     * 设置右面图片
     */
    public void setRightText(String s) {
        rightText.setVisibility(View.VISIBLE);
        rightText.setText(s);
    }

    /**
     * 隐藏软键
     */
    public void hideInputMethod(final EditText v) {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    /**
     * 显示软键盘
     *
     * @param v
     */
    public void showInputMethod(final EditText v) {
        v.requestFocus();
        InputMethodManager imm = (InputMethodManager) this
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(v, 0);
    }

    /**
     * 获取设备信息
     *
     * @return
     */
    private String getDeviceCode() {
        return Build.FINGERPRINT;
    }


//
//    //6.0权限配置
//    /**
//     * 需要进行检测的权限数组
//     */
//    protected String[] needPermissions = {
//            Manifest.permission.ACCESS_COARSE_LOCATION,
//            Manifest.permission.ACCESS_FINE_LOCATION,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE,
//            Manifest.permission.READ_EXTERNAL_STORAGE,
//            Manifest.permission.READ_PHONE_STATE
//    };
//
//    private static final int PERMISSON_REQUESTCODE = 0;
//
//    /**
//     * 判断是否需要检测，防止不停的弹框
//     */
//    private boolean isNeedCheck = true;
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (isNeedCheck) {
//            checkPermissions(needPermissions);
//        }
//    }
//
//    /**
//     * @param
//     * @since 2.5.0
//     */
//    private void checkPermissions(String... permissions) {
//        List<String> needRequestPermissonList = findDeniedPermissions(permissions);
//        if (null != needRequestPermissonList
//                && needRequestPermissonList.size() > 0) {
//            ActivityCompat.requestPermissions(this,
//                    needRequestPermissonList.toArray(
//                            new String[needRequestPermissonList.size()]), PERMISSON_REQUESTCODE);
//        }
//    }
//
//    /**
//     * 获取权限集中需要申请权限的列表
//     *
//     * @param permissions
//     * @return
//     * @since 2.5.0
//     */
//    private List<String> findDeniedPermissions(String[] permissions) {
//        List<String> needRequestPermissonList = new ArrayList<String>();
//        for (String perm : permissions) {
//            if (ContextCompat.checkSelfPermission(this,
//                    perm) != PackageManager.PERMISSION_GRANTED
//                    || ActivityCompat.shouldShowRequestPermissionRationale(
//                    this, perm)) {
//                needRequestPermissonList.add(perm);
//            }
//        }
//        return needRequestPermissonList;
//    }
//
//    /**
//     * 检测是否说有的权限都已经授权
//     *
//     * @param grantResults
//     * @return
//     * @since 2.5.0
//     */
//    private boolean verifyPermissions(int[] grantResults) {
//        for (int result : grantResults) {
//            if (result != PackageManager.PERMISSION_GRANTED) {
//                return false;
//            }
//        }
//        return true;
//    }
//
////    @Override
////    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] paramArrayOfInt) {
////
////    }
//
//    /**
//     * 显示提示信息
//     *
//     * @since 2.5.0
//     */
//    private void showMissingPermissionDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("权限");
//        builder.setMessage("请打开权限");
//
//        // 拒绝, 退出应用
//        builder.setNegativeButton("拒接", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                finish();
//            }
//        });
//
//        builder.setPositiveButton("接受",
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        startAppSettings();
//                    }
//                });
//
//        builder.setCancelable(false);
//
//        builder.show();
//    }
//
//    /**
//     * 启动应用的设置
//     *
//     * @since 2.5.0
//     */
//    private void startAppSettings() {
//        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//        intent.setData(Uri.parse("package:" + getPackageName()));
//        startActivity(intent);
//    }
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            this.finish();
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }


        /* 关于上传图片 */

    /**
     * 调用相机、相册 上传到服务器
     */

    private String sType;
    private Context mContext;
    private PopupWindow popupWindow;
    private ImageView img;
    private ImageView img1;

    public void upDataPicture(Context context, ImageView imageView, ImageView imageView1, String t) {
        img = imageView;
        img1 = imageView1;
        mContext = context;
        sType = t;
        View view = LayoutInflater.from(mContext).inflate(R.layout.popup_uploading_head, null);
        popupWindow = new PopupWindow(view, RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT, true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);

        RelativeLayout layout = (RelativeLayout) view.findViewById(R.id.rl_icon_popup); //布局
        TextView gallery = (TextView) view.findViewById(R.id.tv_icon_gallery);          //相册
        TextView photograph = (TextView) view.findViewById(R.id.tv_icon_photograph);    //拍照
        TextView cancel = (TextView) view.findViewById(R.id.tv_icon_cancel);            //取消

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        // 相册选择
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getParent(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSIONS_REQUEST_CODE);
                } else {
                    PhotoUtils.openPic((Activity) mContext, CODE_UPDATA_REQUEST);
                }

            }
        });

        // 拍照
        photograph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.shouldShowRequestPermissionRationale(getParent(), Manifest.permission.CAMERA)) {
                        ToastUtil.show(mContext, "您已经拒绝过一次");
                    }
                    ActivityCompat.requestPermissions(getParent(), new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, CAMERA_PERMISSIONS_REQUEST_CODE);
                } else {//有权限直接调用系统相机拍照
                    if (hasSdcard()) {
                        imageUri = Uri.fromFile(fileUri);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                            imageUri = FileProvider.getUriForFile(mContext, "com.plt.yzplatform.fileProvider", fileUri);//通过FileProvider创建一个content类型的Uri
                        PhotoUtils.takePicture((Activity) mContext, imageUri, CODE_UPDATA2_REQUEST);
                    } else {
                        ToastUtil.show(mContext, "设备没有SD卡！");
                    }
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        popupWindow.dismiss();
    }

    private static final int CODE_UPDATA_REQUEST = 0xb0;
    private static final int CODE_UPDATA2_REQUEST = 0xb1;
    private static final int CODE_RESULTS_REQUEST = 0xb2;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CAMERA_PERMISSIONS_REQUEST_CODE = 0x03;
    private static final int STORAGE_PERMISSIONS_REQUEST_CODE = 0x04;
    private File fileUri = new File(Environment.getExternalStorageDirectory().getPath() + "/photo.jpg");
    private File fileCropUri = new File(Environment.getExternalStorageDirectory().getPath() + "/crop_photo.jpg");
    private Uri imageUri;
    private Uri cropImageUri;

    /**
     * 权限返回结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_PERMISSIONS_REQUEST_CODE: {//调用系统相机申请拍照权限回调
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (hasSdcard()) {
                        imageUri = Uri.fromFile(fileUri);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                            imageUri = FileProvider.getUriForFile(this, "com.plt.yzplatform.fileProvider", fileUri);//通过FileProvider创建一个content类型的Uri
                        PhotoUtils.takePicture(this, imageUri, CODE_CAMERA_REQUEST);
                    } else {
                        ToastUtil.show(this, "设备没有SD卡！");
                    }
                } else {

                    ToastUtil.show(this, "请允许打开相机！！");
                }
                break;
            }
//            case PERMISSON_REQUESTCODE:
//                if (!verifyPermissions(grantResults)) {
//                    showMissingPermissionDialog();
//                    isNeedCheck = false;
//                }
//                break;
        }
    }

    /**
     * 裁剪图片，并完成结果返回值
     */
    private int output_X = 480;
    private int output_Y = 480;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                //直接上传图片 调用相册
                case CODE_UPDATA_REQUEST:
                    if (hasSdcard()) {
                        cropImageUri = Uri.fromFile(fileCropUri);
                        Uri newUri = Uri.parse(PhotoUtils.getPath(this, data.getData()));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                            newUri = FileProvider.getUriForFile(this, "com.plt.yzplatform.fileProvider", new File(newUri.getPath()));
                        PhotoUtils.cropImageUri(this, newUri, cropImageUri, 1, 1, output_X, output_Y, CODE_RESULTS_REQUEST);
                    } else {
                        ToastUtil.show(this, "设备没有SD卡！");
                    }
                    break;
                //直接上传图片 调用相机
                case CODE_UPDATA2_REQUEST:
                    cropImageUri = Uri.fromFile(fileCropUri);
                    PhotoUtils.cropImageUri(this, imageUri, cropImageUri, 1, 1, output_X, output_Y, CODE_RESULTS_REQUEST);
                    break;
                //直接上传图片 返回成功值
                case CODE_RESULTS_REQUEST:

                    final Bitmap bitmap1 = PhotoUtils.getBitmapFromUri(cropImageUri, this);
                    //上传图片 获取图片id返回值
                    if (NetUtil.isNetAvailable(mContext)){
                        OkHttpUtils.post()
                                .url(Config.UPLOADFILE)
                                .addHeader("user_token", Prefs.with(mContext).read("user_token"))
                                .addParams("file_content",PhotoUtils.bitmapToBase64(bitmap1))
                                .build()
                                .execute(new StringCallback() {

                                    @Override
                                    public void onError(okhttp3.Call call, Exception e, int id) {
                                        ToastUtil.noNAR(mContext);
                                    }

                                    @Override
                                    public void onResponse(String response, int id) {
                                        Log.d(TAG, "onResponse上传图片: " + response);
                                        /**
                                         * {"data":{"file_id":"437033640d6c4812bf9aba35099b9fc1"},"message":"","status":"1"}
                                         */
                                        try {

                                            JSONObject jsonObject = new JSONObject(response);
                                            if ("1".equals(jsonObject.getString("status"))){
                                                String data = jsonObject.getString("data");
                                                JSONObject object = new JSONObject(data);
                                                String file_id = object.getString("file_id");
                                                Prefs.with(mContext).write(sType,file_id);
                                                if (bitmap1 != null) {
                                                    showImages(bitmap1);
                                                }
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                    }else {
                        ToastUtil.noNetAvailable(mContext);
                    }
//                    if (NetUtil.isNetAvailable(mContext)) {
//                        OkHttpUtils.post()
//                                .url(NetConfig.UPDATA_PIC)
//                                .addParams("base64", PhotoUtils.bitmapToBase64(bitmap1))
//                                .addParams("user_id", user_id)
//                                .build()
//                                .execute(new StringCallback() {
//                                    @Override
//                                    public void onError(Call call, Exception e, int id) {
//                                        ToastUtil.noNAR(mContext);
//                                    }
//
//                                    @Override
//                                    public void onResponse(String response, int id) {
//                                        Log.d(TAG, "onResponse:上传图片返回的id " + response);
//                                        try {
//                                            JSONObject jsonObject = new JSONObject(response);
//                                            if (jsonObject.getString("status").equals("1")) {
//                                                //上传成功
//
//                                                String file_id = jsonObject.getString("data");
//                                                Prefs.with(mContext).write(sType, file_id);
//                                                if (bitmap1 != null) {
//                                                    showImages(bitmap1);
//                                                }
//                                            }
//
//                                        } catch (JSONException e) {
//                                            e.printStackTrace();
//                                        }
//                                    }
//                                });
//                    } else {
//                        ToastUtil.noNetAvailable(mContext);
//                    }
                    break;
            }


        } else if (resultCode == RESULT_CANCELED) {
//            //取消
//            popupWindow.dismiss();
        }
    }

    /**
     * 显示图片
     * layout中使用background 赋值用src 为了判断是否上传图片
     */
    private void showImages(Bitmap bitmap) {
        //设置background
//        img.setBackground(null);
        img.setBackgroundDrawable(null);
        //设置src
        img.setImageBitmap(bitmap);

        /* 按顺序添加图片 */
//        if (img.getDrawable().getCurrent().getConstantState() != getResources().getDrawable(R.drawable.e_f_tsphone).getConstantState()) {
//            if (img1 != null) {
//                img1.setVisibility(View.VISIBLE);
//            }
//        }

        popupWindow.dismiss();
    }


    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }


    /**
     * 获取background   背景为null则拍过照片 背景不为null则没拍过照
     */
    public Bitmap getBitmap(ImageView img) {
        Drawable drawable = img.getBackground();
        if (drawable == null) {
            return null;
        } else {
            return drawableToBitmap(drawable);
        }
    }

    /**
     * imageview转bitmap
     */
    public Bitmap getSrc(ImageView img) {
        Bitmap bitmap = ((BitmapDrawable) ((ImageView) img).getDrawable()).getBitmap();
        return bitmap;
    }

    /**
     * drawable转 bitmap
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(
                drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(),
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        //canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

}


