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
import java.text.DecimalFormat;

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
                            imageUri = FileProvider.getUriForFile(mContext, "com.plt.yzplatform.fileProvider", fileUri);//通过FileProvider创建一个content类型的Uri
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
                                                Prefs.with(mContext).remove(sType);
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
     * 通过头像id获取头像
     **/
    public void getPic(final Context context, String file_id, final ImageView icon) {
        mContext = context;
        if (NetUtil.isNetAvailable(context)) {
            OkHttpUtils.post()
                    .url(Config.GET_BASE64)
                    .addHeader("user_token",Prefs.with(mContext).read("user_token"))
                    .addParams("file_id", file_id)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            ToastUtil.noNAR(mContext);
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            Log.w(TAG, "onResponse获取base64: " + response );
                            /**
                             * {"data":{"file_content":"/9j/4AAQSkZJRgABAQAAAQABAAD/2wBDAAEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEB\nAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQH/2wBDAQEBAQEBAQEBAQEBAQEBAQEBAQEB\nAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQH/wAARCAHgAeADASIA\nAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQA\nAAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3\nODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWm\np6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4 Tl5ufo6erx8vP09fb3 Pn6/8QAHwEA\nAwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSEx\nBhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElK\nU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3\nuLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3 Pn6/9oADAMBAAIRAxEAPwD7q0jT\n927cd2MduvX368ds445557SwtPJDd3OPbozfXrgcfXnkZzdOj2buc569unX1/D8j612GnW3mbu2M\nepz97H8Xofr053A5/CMPa6vt73f 9b9T9M5ve309P6ZYs7Rxv29SV/RjzknAHynOfzz13bS035Qo\nSgx6c8uMcHJz8uM /JGTV62tThsdeP5tk9fYd/XnkA7ENvhZT9AevqRj16Dnj3ycHOVObhfVrs10\n1d u1ld qWrTOdVlK7XNdel//Snv6/Mpxacu0ny 47n/AD/nqacbBVST936eueGfHfr8o9eD7HO6\nkLsD5fGOSMcnOQDyT/dP/wBfqZjaOc89/UZ/i9/f XPBzF33f3v/ADNDk3t32n/9R4Lj1PbnPp6n\nOKD2zjzuf7vI r/989c89eehFdsLP/Wb139M/wDj CeTjPOP1Bzxjz2j/vuvb cg9O/b3/vdaRlh\nvhf NHMTWu4Bm /Hjg47MR1ye2D19ec1g3gESnzH6f8ALPg9z9f69eTkA11Nxbsglijh8 RseTHH\nkdS/OSfRe5/iPPBzzd1FDCHnup/OuRnEWP8AU/MwPOfx/LkkgjxszzHCZfRdSrbSyu07W99K2uvd\nJaNNN3Wr  4K4PzTi3Fv2P8AsOEV7ZpzOzac m6um99dtnKV K1CzuLrzQ8 y248iKMCCebBfIye\nR/Ceee2ehrEittOtFJx5cn/LKOQ5xyw7/XP9CCK273V45YZYU5f/AFQPPqw7nvjPqMEA8k1hWejX\nN3MqP5hmlH7tI8joWxxk56n36fMSRX55js7q41SpJ/7HbXe1 aWlm762W9t1rqf19wtkuS8J4D2e\nFw/1KN0nv3aaevS71W musmRW0807yQlJAVlHleXk4yW755yMe4BUZYhq qfhJ zX4m IR/tW88z\nw5oTgeTeXlp5Nxeg5z9ktMnHIzjvg9xlva/2ff2ftDsol8TeM7SO81JIraW00y4iBt7P5mz83TkY\nYg56hcggtX2fcX0VrapbWSeRBbYii8uIweXy4PU5GCvTBJOOcA5eDwlJ3q4nbpK1la8tdWmrPbXR\ntJN 9f8AP M/EussX9QyC31uy/4VUlveVrXa0slra22rk234dpv7J3wV0O3H9radJr90Ij5suoL5\n46vntj259Tg5DZW  D3wOWzfTU8B HGtz/BPoelT5wzjOL044Cgc9 55J9BvNZOx1keSSPjB6Z5Y\nZ4OQCSOpOMDrljXC6hqURnNwXHlyDPGeeSBzkHgq3/fRySRz1TzOlhl7PCYdYPol3fvK2rta7TUV\n2W97r86wOP4pqVHVxed5pv8A9Da93edn5tNbarXe92/nnx3 zF zdrSr5vwm8HNIn72K4h8P6bEO\nrji5ayYA8HBHfHJYsT8c/Fz4KeHvDdpdr4Q1P xrb7JPF/Z32Qz2/VsY xY789eM9SAGP3b4k8U2\ndt5 bo db4zHg/Ny46A8YHX3B5JJJ QvGuptqE2rzzzRiKb7N9k6/wAJcccnHXGCcZ9SRWFLG5hj\naj9q3jnaNndX0c XdvWydtXu7Wcfe/f/AA8z7iiFWhh8bneZYvB4F3tmeaLMo6vT3VLT4W/5rdWm\nfhx4g HXhz4UeJL9/GVtGn9oXVzdQ IfJ 3aRD9rvG/4  cWOccH6ZyVLV7N4W8M6QwhurVbe9t7\n3bLa3NuLQQT22ZB9qsxn0/8ArEnmvoT4oeAtP8UaPqMNzFG4uMAP5P7gEnUf9L6n 6e QCec4rT/\nAOCd3wD0D4vfCfxJLrutR6XpXgj4teNfBWm3luBPqlxp9s m6tZjOc/KNTBOSAAfvMQ2f3KWd1sR\nwpLNauJX1rArKcrzWSae8pxvvrq47q90173xH99Yvxyy3CcD/wCsnEuafUv7HeU5TmaXM7vM1OeV\nRSu3tCbSvZ3Sv0l4Vf8AhXRfMlhmsLdJvN/1scXH3nHTnsPqeM4IyWr4Y0u1ibdDbqkmBC78d39T\n34PXIORyd1fsBH wd8GJ5o3m8c KpSkolkijS2M82C/XFnz2zzxkgEndn6M H37Lf7Ong2eK7Twp\nb K9UixJFqXilW1E2WC PsdoH02yU4IyVYMDgk4YV8fU4kT1hi35K0rv4uiVvXX W7a0PxrN/pk \nHmV5fKrldPinOsXZuOWPK45XHm5pWTeZzlNc7mm1ytpWbvJH872ifCzxr4u12DS/h74I8Q ODPL5\nUsXh/wAO3OpCIjcT/ptpYgcBNx5yAB1ALH6yl/4JzftQRaPDr9v8DtcuTcRCWWxgvvDk qwncwx/\nZP2/rkcjPJPXK1/Rroeq2el2KWWiWen6Lp8ZMMdlo/2WzsPs 49bSzbP057qOCpz3eleIZVkYLIQ\n5hXnzcjgnOfmyOoz HGRzzR4hqYhOisS8HK6s aPw3lZtWbUtddWno7NpH4FxN9P/wAU6lWj/qrw\nVwnkuU4NRUVmuZZxmma5nK80/eyyrlClHSyioxaX2mkk/wCNbx18GvGXgm vrHxn4B8W DWtwDKf\nEPhzVdNtzcEnBF6LH zz8oznnAKjOCGbx 48PPbW11JBP9qS3x 7j6cFh6d8Z/mTgE/3mWWqaTrN\nlJpOu2Vpqlpc4iu7O/jtb2xkwZACLW9Ofm4yPoDuBYn5k KP/BPT9jn4ywXUmqfDWw8L63cxjyvE\nXgh5/DuoMMEHFray/YbvhT1045YlsFi273sDjcZUT9hiXjE2rKzaSvUT1tslezS1cmul39twV 0k\nwmFq4bCeIfh3mOUbKea8KZr/AGhdqTi TKM2nlmltW3nGYOKS92Unr/HTpmjaPc2KyXKW6TSRDPl\n5xDjeB39wfyJJIrgLnR7a3M/mW1uqiW4Pl/8Cb/S uecfz5O3Nfuv8fv CJHxf0d7nW/2dvHum O\nbF2nKeGvE0tj4a8RQQjcGFpqbSNo15kksF1AWIUK26QkR1 THin9m39oL4b63ceHfiT8MvFnhzUI\nZvIQ6jp1w1vIfmP2q01cN9gvbHBDY0/nay54KlvUw0qWBeLrYvM/qdrJvNOXLPtSs290/JtNLlSj\ndM/unwt kB4KeJ9LFY/g3xNyrF4yXKlwrmv\
                             */
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getString("status").equals("1")) {
                                    String data = jsonObject.getString("data");
                                    JSONObject object = new JSONObject(data);
                                    String file_content = object.getString("file_content");
                                    if (file_content.contains("base64,"))
                                        file_content = file_content.split("base64,")[1];
                                    Bitmap bitmap = PhotoUtils.base64ToBitmap(file_content);
                                    if (bitmap!=null)
                                        icon.setImageBitmap(bitmap);
                                    else
                                        icon.setImageResource(R.drawable.qy_heat);
                                } else {
                                    ToastUtil.noNAR(mContext);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } else {
            ToastUtil.noNetAvailable(context);
        }
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


