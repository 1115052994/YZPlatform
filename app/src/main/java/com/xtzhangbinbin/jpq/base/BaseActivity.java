package com.xtzhangbinbin.jpq.base;

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

import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Poi;
import com.amap.api.navi.AmapNaviPage;
import com.amap.api.navi.AmapNaviParams;
import com.amap.api.navi.AmapNaviType;
import com.amap.api.navi.INaviInfoCallback;
import com.amap.api.navi.model.AMapNaviLocation;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.config.Config;
import com.xtzhangbinbin.jpq.utils.ActivityUtil;
import com.xtzhangbinbin.jpq.utils.NetUtil;
import com.xtzhangbinbin.jpq.utils.OKhttptils;
import com.xtzhangbinbin.jpq.utils.PhotoUtils;
import com.xtzhangbinbin.jpq.utils.Prefs;
import com.xtzhangbinbin.jpq.utils.ToastUtil;
import com.squareup.picasso.Picasso;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

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
        //禁止横屏
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
     * 导航
     */
    public void navigation(double lat,double lon){

        //Poi start = new Poi("三元桥", new LatLng(39.96087,116.45798), "");
/**终点传入的是北京站坐标,但是POI的ID "B000A83M61"对应的是北京西站，所以实际算路以北京西站作为终点**/
        Poi end = new Poi("",new LatLng(lat,lon), "");
//        Poi end = new Poi("北京站", new LatLng(39.904556, 116.427231), "");
//        List<Poi> wayList = new ArrayList();//途径点目前最多支持3个。
//        wayList.add(new Poi("团结湖", new LatLng(39.93413,116.461676), ""));
//        wayList.add(new Poi("呼家楼", new LatLng(39.923484,116.461327), ""));
//        wayList.add(new Poi("华润大厦", new LatLng(39.912914,116.434247), ""));
        AmapNaviPage.getInstance().showRouteActivity(this, new AmapNaviParams(null, null, end, AmapNaviType.DRIVER), new INaviInfoCallback() {
            @Override
            public void onInitNaviFailure() {

            }

            @Override
            public void onGetNavigationText(String s) {

            }

            @Override
            public void onLocationChange(AMapNaviLocation aMapNaviLocation) {

            }

            @Override
            public void onArriveDestination(boolean b) {

            }

            @Override
            public void onStartNavi(int i) {

            }

            @Override
            public void onCalculateRouteSuccess(int[] ints) {

            }

            @Override
            public void onCalculateRouteFailure(int i) {

            }

            @Override
            public void onStopSpeaking() {

            }

            @Override
            public void onReCalculateRoute(int i) {

            }

            @Override
            public void onExitPage(int i) {

            }
        });

    }


    public String setNumberText(EditText numberText){
        String s = numberText.getText().toString().trim();
        if (s.isEmpty()){
            return "";
        }else {
            double d = Double.valueOf(s);
            DecimalFormat df = new DecimalFormat("#0.00");
            return df.format(d);
        }
    }


    /* json转map */
    public static Map<String, String> json2map(String str_json) {
        Map<String, String> res = null;
        try {
            Gson gson = new Gson();
            res = gson.fromJson(str_json, new TypeToken<Map<String, Object>>() {
            }.getType());
        } catch (JsonSyntaxException e) {
        }
        return res;
    }

    /**
     * 根据token获取用户信息
     */
    public void getUserToken(final Context context){
        String user_token = Prefs.with(getApplicationContext()).read("user_token");
        if (!user_token.isEmpty()){
            if (NetUtil.isNetAvailable(context)){
                OkHttpUtils.post()
                        .url(Config.GET_USERTOKEN)
                        .addHeader("user_token",user_token)
                        //
                        .addParams("","")
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                ToastUtil.noNAR(context);
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                Log.e(TAG, "onResponse用户信息: " + response );
                                /**
                                 * {"data":
                                 * {"result":
                                 * {"login_name":"17685416552",
                                 * "user_type":"comp",
                                 * "user_id":"15",
                                 * "phone_number":"17685416552",
                                 * "user_token":"96730A47BBCD8F345203CFAB9A2CA83A768A3A7A1150AC667DA0330D0B94B04602AACF19E9AB5C98A977CA377CD73B6F8CBAECEE459C76A0EF20E0615C5498E8",
                                 * "invi_code":"",
                                 * "user_state":"active"}},
                                 * "message":"","status":"1"}
                                 * */
                            }
                        });
            }else {
                ToastUtil.noNetAvailable(context);
            }
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

    public String getRightText(){
        return rightText.getText().toString().trim();
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

//                    if (ActivityCompat.shouldShowRequestPermissionRationale(getParent(), Manifest.permission.CAMERA)) {
//                        ToastUtil.show(mContext, "您已经拒绝过一次");
//                    }
                    ActivityCompat.requestPermissions(getParent(), new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, CAMERA_PERMISSIONS_REQUEST_CODE);
                } else {//有权限直接调用系统相机拍照
                    if (hasSdcard()) {
                        imageUri = Uri.fromFile(fileUri);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                            imageUri = FileProvider.getUriForFile(mContext, "com.xtzhangbinbin.jpq.fileProvider", fileUri);//通过FileProvider创建一个content类型的Uri
                        PhotoUtils.takePicture((Activity) mContext, imageUri, CODE_UPDATA2_REQUEST);
                    } else {
                        ToastUtil.show(mContext, "设备没有SD卡！");
                    }
                }
            }
        });

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
                            imageUri = FileProvider.getUriForFile(this, "com.xtzhangbinbin.jpq.fileProvider", fileUri);//通过FileProvider创建一个content类型的Uri
                        PhotoUtils.takePicture(this, imageUri, CODE_CAMERA_REQUEST);
                    } else {
                        ToastUtil.show(this, "设备没有SD卡！");
                    }
                } else {

                    ToastUtil.show(this, "请允许打开相机！！");
                }
                break;
            }
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
                            newUri = FileProvider.getUriForFile(this, "com.xtzhangbinbin.jpq.fileProvider", new File(newUri.getPath()));
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
                    Map<String,String> map = new HashMap<>();
                    map.put("file_content",PhotoUtils.bitmapToBase64(bitmap1));
                    OKhttptils.post((Activity) mContext, Config.UPLOADFILE, map, new OKhttptils.HttpCallBack() {
                        @Override
                        public void success(String response) {
                            try {

                                JSONObject jsonObject = new JSONObject(response);
                                String data = jsonObject.getString("data");
                                JSONObject object = new JSONObject(data);
                                Prefs.with(mContext).remove(sType);
                                String file_id = object.getString("file_id");
                                Prefs.with(mContext).write(sType,file_id);
                                if (bitmap1 != null) {
                                    showImages(bitmap1);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void fail(String response) {

                        }
                    });
                    break;
            }


        } else if (resultCode == RESULT_CANCELED) {
//            //取消
//            popupWindow.dismiss();
        }
    }

    /* 上传图片base64并获取图片id */
    public void upDataBase64(final Context context, final Bitmap bitmap, final String sType) {
        Map<String,String> map = new HashMap<>();
        map.put("file_content",PhotoUtils.bitmapToBase64(bitmap));
        OKhttptils.post((Activity) context, Config.UPLOADFILE, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    String data = jsonObject.getString("data");
                    JSONObject object = new JSONObject(data);
                    Prefs.with(context).remove(sType);
                    String file_id = object.getString("file_id");
                    Prefs.with(context).write(sType,file_id);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void fail(String response) {

            }
        });
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
     * 通过头像id获取头像
     **/
    public void getPic(final Context context, String file_id, final ImageView icon) {
        mContext = context;
        String url = Config.GET_Pic + file_id + "&type=showbase64thumbnail&name="+file_id+".jpg";
        Log.i("url","url==="+url);
        Picasso.with(mContext).load(Uri.parse(url)).into(icon);
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


