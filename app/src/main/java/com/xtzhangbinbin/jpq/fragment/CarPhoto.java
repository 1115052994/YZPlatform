package com.xtzhangbinbin.jpq.fragment;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.camera.PhotographActivity;
import com.xtzhangbinbin.jpq.config.Config;
import com.xtzhangbinbin.jpq.utils.OKhttptils;
import com.xtzhangbinbin.jpq.utils.PhotoZDY;
import com.xtzhangbinbin.jpq.utils.ToastUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class CarPhoto extends Fragment {
    private static final String TAG = "拍照";

    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.addition_ed_introduce)
    EditText additionEdIntroduce;
    Unbinder unbinder;
    private String path;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_car_photo, container, false);
        unbinder = ButterKnife.bind(this, inflate);
        Bundle bundle = getArguments();
        if (bundle != null) {
            bundle.getString("path");
            Log.d(TAG, "onCreateView图片路径: " + path);
        }


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //将图片赋值并上传到服务器
                upDataPicture(getActivity(), imageView, null, "员工头像");
//                //得到fileid
//                String file_id = Prefs.with(getActivity()).read("员工头像");
            }
        });
//        bc.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getContext(), "保存", Toast.LENGTH_SHORT).show();
//            }
//        });
        return inflate;


    }

    //数据上传
    private void getData(String car_id, String number, String file_id, String sfile_id, String desc) {
        Map<String, String> map = new HashMap<>();
        map.put("car_id", car_id);
        map.put("number", number);
        map.put("file_id", file_id);
        map.put("sfile_id", sfile_id);
        map.put("desc", desc);
        OKhttptils.post((Activity) getContext(), Config.ACCRETIONPICTURECAR, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                Log.d("aaaaa", "onResponse获取数据: " + response);

            }

            @Override
            public void fail(String response) {
                Log.d("aaaa", "fail: " + response);
            }
        });


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    ////////////////////////////////头疼的地平线//////////////////////////////////
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
                    ActivityCompat.requestPermissions(getActivity().getParent(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSIONS_REQUEST_CODE);
                } else {
                    PhotoZDY.openPic((Activity) mContext, CODE_UPDATA_REQUEST);
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
                    ActivityCompat.requestPermissions(getActivity().getParent(), new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, CAMERA_PERMISSIONS_REQUEST_CODE);
                } else {//有权限直接调用系统相机拍照
                    if (hasSdcard()) {
                        imageUri = Uri.fromFile(fileUri);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                            imageUri = FileProvider.getUriForFile(mContext, "com.xtzhangbinbin.jpq.fileProvider", fileUri);//通过FileProvider创建一个content类型的Uri
                        Log.d("aaaaa", "imageUri: " + imageUri);

                        int checkCallPhonePermission = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA);
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 222);
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 222);
                        if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 222);
                            return;
                        } else {
                            Intent intent = new Intent(getContext(), PhotographActivity.class);
                            startActivityForResult(intent, 000);
                        }


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
                            imageUri = FileProvider.getUriForFile(getActivity(), "com.xtzhangbinbin.jpq.fileProvider", fileUri);//通过FileProvider创建一个content类型的Uri
//                        PhotoZDY.takePicture(CarPhoto.this, imageUri, CODE_CAMERA_REQUEST);
                        Intent intent = new Intent(getContext(), PhotographActivity.class);
                        startActivityForResult(intent, 000);
                    } else {
                        ToastUtil.show(getActivity(), "设备没有SD卡！");
                    }
                } else {

                    ToastUtil.show(getActivity(), "请允许打开相机！！");
                }
                break;
            }
        }
    }


    //图片的质量1-100
    public void compress(Bitmap.CompressFormat format, int quality) {
        FileOutputStream fos = null;
        try {
            //得到一个储存路径
            File file = new File(Environment.getExternalStorageDirectory(), "test.jpg");
            //得到一个文件输入流
            fos = new FileOutputStream(file);
            //得到bitmap
//            Bitmap bmp = BitmapFactory.decodeFile(uri.toString());
            //开始压缩
            //   Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.default_icon);
//            bmp.compress(format, quality, fos);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
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


