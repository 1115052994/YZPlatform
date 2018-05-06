package com.xtzhangbinbin.jpq.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.EdgeEffectCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.base.BaseActivity;
import com.xtzhangbinbin.jpq.camera.ImageTools;
import com.xtzhangbinbin.jpq.camera.PhotographActivity;
import com.xtzhangbinbin.jpq.config.Config;
import com.xtzhangbinbin.jpq.entity.CarPhotos;
import com.xtzhangbinbin.jpq.utils.JumpUtil;
import com.xtzhangbinbin.jpq.utils.OKhttptils;
import com.xtzhangbinbin.jpq.utils.PhotoUtils;
import com.xtzhangbinbin.jpq.utils.Prefs;
import com.xtzhangbinbin.jpq.utils.ToastUtil;
import com.xtzhangbinbin.jpq.view.HackyViewPager;
import com.xtzhangbinbin.jpq.view.MyProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CarPhotoActivity extends BaseActivity {
    private static final int STORAGE_PERMISSIONS_REQUEST_CODE = 0x04;
    private static final String TAG = "添加图片";

    @BindView(R.id.mCarName)
    TextView mCarName;
    @BindView(R.id.radio_button1)
    RadioButton radioButton1;
    @BindView(R.id.radio_button2)
    RadioButton radioButton2;
    @BindView(R.id.number_text)
    TextView numberText;
    @BindView(R.id.sum_text)
    TextView sumText;
    @BindView(R.id.viewpager)
    HackyViewPager viewpager;


    private List<View> viewList = new ArrayList<>();
    private MyAdapter adapter;
    private int position = 0;//点击的哪一个item


    private List<CarPhotos> carPhotos = new ArrayList<>();
    private List<String> photo_ids = new ArrayList<>();
    private List<String> photo_names = new ArrayList<>();
    private ArrayList<String> arr = new ArrayList<>();//24个名称
    private List<String> string_id = new ArrayList<>();
    private String path = "";
    private String car_id = "";
    private String car_name = "";


    private PopupWindow popupWindow;

    private MyProgressDialog dialog;

    private String type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_photo);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        type = bundle.getString("type");
        if (type.equals("新增")) {
            car_id = bundle.getString("car_id");

        } else if (type.equals("修改")) {
            carPhotos = (List<CarPhotos>) getIntent().getSerializableExtra("photo_ids");
            car_id = carPhotos.get(0).getCar_id();
            position = bundle.getInt("position", 0);
            for (int i = 0; i < carPhotos.size(); i++) {
                photo_ids.add(carPhotos.get(i).getCar_1_file_id());
                photo_ids.add(carPhotos.get(i).getCar_2_file_id());
                photo_ids.add(carPhotos.get(i).getCar_3_file_id());
                photo_ids.add(carPhotos.get(i).getCar_4_file_id());
                photo_ids.add(carPhotos.get(i).getCar_5_file_id());
                photo_ids.add(carPhotos.get(i).getCar_6_file_id());
                photo_ids.add(carPhotos.get(i).getCar_7_file_id());
                photo_ids.add(carPhotos.get(i).getCar_8_file_id());
                photo_ids.add(carPhotos.get(i).getCar_9_file_id());
                photo_ids.add(carPhotos.get(i).getCar_10_file_id());
                photo_ids.add(carPhotos.get(i).getCar_11_file_id());
                photo_ids.add(carPhotos.get(i).getCar_12_file_id());
                photo_ids.add(carPhotos.get(i).getCar_13_file_id());
                photo_ids.add(carPhotos.get(i).getCar_14_file_id());
                photo_ids.add(carPhotos.get(i).getCar_15_file_id());
                photo_ids.add(carPhotos.get(i).getCar_16_file_id());
                photo_ids.add(carPhotos.get(i).getCar_17_file_id());
                photo_ids.add(carPhotos.get(i).getCar_18_file_id());
                photo_ids.add(carPhotos.get(i).getCar_19_file_id());
                photo_ids.add(carPhotos.get(i).getCar_20_file_id());
                photo_ids.add(carPhotos.get(i).getCar_21_file_id());
                photo_ids.add(carPhotos.get(i).getCar_22_file_id());
                photo_ids.add(carPhotos.get(i).getCar_23_file_id());
                photo_ids.add(carPhotos.get(i).getCar_24_file_id());
                photo_names.add(carPhotos.get(i).getCar_1_desc());
                photo_names.add(carPhotos.get(i).getCar_2_desc());
                photo_names.add(carPhotos.get(i).getCar_3_desc());
                photo_names.add(carPhotos.get(i).getCar_4_desc());
                photo_names.add(carPhotos.get(i).getCar_5_desc());
                photo_names.add(carPhotos.get(i).getCar_6_desc());
                photo_names.add(carPhotos.get(i).getCar_7_desc());
                photo_names.add(carPhotos.get(i).getCar_8_desc());
                photo_names.add(carPhotos.get(i).getCar_9_desc());
                photo_names.add(carPhotos.get(i).getCar_10_desc());
                photo_names.add(carPhotos.get(i).getCar_11_desc());
                photo_names.add(carPhotos.get(i).getCar_12_desc());
                photo_names.add(carPhotos.get(i).getCar_13_desc());
                photo_names.add(carPhotos.get(i).getCar_14_desc());
                photo_names.add(carPhotos.get(i).getCar_15_desc());
                photo_names.add(carPhotos.get(i).getCar_16_desc());
                photo_names.add(carPhotos.get(i).getCar_17_desc());
                photo_names.add(carPhotos.get(i).getCar_18_desc());
                photo_names.add(carPhotos.get(i).getCar_19_desc());
                photo_names.add(carPhotos.get(i).getCar_20_desc());
                photo_names.add(carPhotos.get(i).getCar_21_desc());
                photo_names.add(carPhotos.get(i).getCar_22_desc());
                photo_names.add(carPhotos.get(i).getCar_23_desc());
                photo_names.add(carPhotos.get(i).getCar_24_desc());
            }

        }
//        car_id = "17495";
        initData();
        initView();
        initView1();
    }


    /* 准备数据 */
    private void initData() {
        arr.add("正45度");
        arr.add("正面");
        arr.add("前大灯");
        arr.add("侧面");
        arr.add("后车灯");
        arr.add("背面");
        arr.add("钥匙");
        arr.add("左前车门");
        arr.add("车门操控区");
        arr.add("前座椅");
        arr.add("中控区");
        arr.add("方向盘");
        arr.add("仪表盘");
        arr.add("显示屏");
        arr.add("档位");
        arr.add("内车顶");
        arr.add("后座椅");
        arr.add("后备箱");
        arr.add("发动机舱");
        arr.add("底盘");
        arr.add("车轮");
        arr.add("灯光控制");
        arr.add("雨刷器");
        arr.add("钥匙孔");
        for (int i = 1; i < 25; i++) {
            string_id.add(String.valueOf(i));
        }
    }

    /* 初始化界面数据 */
    private void initView() {
        for (int i = 0; i < 24; i++) {
            final int a = i;
            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.fragment_car_photo, null);
            final ImageView iv = view.findViewById(R.id.mPic);
            Button mSave = view.findViewById(R.id.mSave);
            final EditText mContent = view.findViewById(R.id.mContent);
            WindowManager wm = (WindowManager) this
                    .getSystemService(Context.WINDOW_SERVICE);
            int width = wm.getDefaultDisplay().getWidth();
            int height = wm.getDefaultDisplay().getHeight();
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (width / 3 * 2));
            iv.setLayoutParams(params);
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //选择拍照或者选择图片
                    showPopup(iv);
                    position = a;
                }
            });

            if (type.equals("新增")) {

            } else if (type.equals("修改")) {
                if (photo_ids.get(i) == null) {

                } else {
                    mContent.setText(photo_names.get(i));
                    OKhttptils.getPic(this, photo_ids.get(i), iv);
                }
            }

            mSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String file_id = Prefs.with(getApplicationContext()).read(car_name);
                    String sfile_id = Prefs.with(getApplicationContext()).read(car_name + "压缩");
                    String content = mContent.getText().toString().trim();

                    if (file_id.isEmpty() || sfile_id.isEmpty()) {
                        ToastUtil.show(CarPhotoActivity.this, "图片上传中，请稍后保存");

                    } else if (!file_id.isEmpty() && !sfile_id.isEmpty() && !content.isEmpty()) {
                        /* 上传 */
                        Log.d(TAG, "onClick车id: " + car_id);
                        Log.e(TAG, "onClick: " + string_id.get(position));
                        Log.w(TAG, "onClick: " + file_id);
                        Log.i(TAG, "onClick: " + sfile_id);
                        Log.d(TAG, "onClick: " + content);
                        Map<String, String> map = new HashMap<>();
                        map.put("car_id", car_id);
                        map.put("number", string_id.get(position));
                        map.put("file_id", file_id);
                        map.put("sfile_id", sfile_id);
                        map.put("desc", content);
                        OKhttptils.post(CarPhotoActivity.this, Config.ACCRETIONPICTURECAR, map, new OKhttptils.HttpCallBack() {
                            @Override
                            public String success(String response) {
                                Log.e(TAG, "success上传图片: " + response);
                                ToastUtil.show(CarPhotoActivity.this, "保存成功");
                                if (position == 23) {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("car_id", car_id);
                                    JumpUtil.newInstance().jumpRight(CarPhotoActivity.this, UpdateCarActivity.class, bundle);
                                }

                                return response;
                            }

                            @Override
                            public void fail(String response) {

                            }
                        });
                    } else {
                        ToastUtil.show(CarPhotoActivity.this, "请完善信息后保存");
                    }

                    Log.d(TAG, "onClick图片名: " + car_name);
                    Log.i(TAG, "onClick图片名: " + Prefs.with(getApplicationContext()).read(car_name));
                    Log.w(TAG, "onClick压缩id: " + Prefs.with(getApplicationContext()).read(car_name + "压缩"));


                }
            });

            viewList.add(view);
        }
        Log.e("拍照", "initView: " + position);
        adapter = new MyAdapter(viewList);
        viewpager.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        viewpager.setCurrentItem(position);
    }

    /* 弹出拍照、选图popupwindow */
    private void showPopup(ImageView iv) {
        View view = LayoutInflater.from(CarPhotoActivity.this).inflate(R.layout.popup_uploading_head, null);
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
                if (ContextCompat.checkSelfPermission(CarPhotoActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getParent(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSIONS_REQUEST_CODE);
                } else {
                    PhotoUtils.openPic(CarPhotoActivity.this, 666);
                }

            }
        });

        // 拍照
        photograph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int checkCallPhonePermission = ContextCompat.checkSelfPermission(CarPhotoActivity.this, Manifest.permission.CAMERA);
                ActivityCompat.requestPermissions(CarPhotoActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 222);
                ActivityCompat.requestPermissions(CarPhotoActivity.this, new String[]{Manifest.permission.CAMERA}, 222);
                if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(CarPhotoActivity.this, new String[]{Manifest.permission.CAMERA}, 222);
                    return;
                } else {
                    String s = arr.get(position);
                    Intent intent = new Intent(CarPhotoActivity.this, PhotographActivity.class);
                    intent.putExtra("index",position);
                    intent.putExtra("s",s);
                    startActivityForResult(intent, 0);
                }
            }
        });

    }

    private EdgeEffectCompat leftEdge;

    private EdgeEffectCompat rightEdge;

    /* viewpager滑动监听 */
    private void initView1() {

        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                try {
                    Field leftEdgeField = viewpager.getClass().getDeclaredField("mLeftEdge");
                    Field rightEdgeField = viewpager.getClass().getDeclaredField("mRightEdge");
                    if (leftEdgeField != null && rightEdgeField != null) {
                        leftEdgeField.setAccessible(true);
                        rightEdgeField.setAccessible(true);
                        leftEdge = (EdgeEffectCompat) leftEdgeField.get(viewpager);
                        rightEdge = (EdgeEffectCompat) rightEdgeField.get(viewpager);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (leftEdge != null && rightEdge != null) {
                    leftEdge.finish();
                    rightEdge.finish();
                    leftEdge.setSize(0, 0);
                    rightEdge.setSize(0, 0);
                }

                if (position < 24 && position >= 0) {
                    car_name = arr.get(position);
                    mCarName.setText(car_name);
                    numberText.setText(string_id.get(position));
                    if (position == 0) {
                        radioButton1.setBackground(null);
                        radioButton2.setBackground(getResources().getDrawable(R.drawable.radio_type_2));
                    } else if (position == 23) {
                        radioButton1.setBackground(getResources().getDrawable(R.drawable.radio_type));
                        radioButton2.setBackground(null);
                    } else {
                        radioButton1.setBackground(getResources().getDrawable(R.drawable.radio_type));
                        radioButton2.setBackground(getResources().getDrawable(R.drawable.radio_type_2));
                    }
                } else {
                    viewpager.setHorizontalScrollBarEnabled(false);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    /* viewpager自定义adapter */
    class MyAdapter extends PagerAdapter {
        private List<View> list;

        public MyAdapter(List<View> viewList) {
            this.list = viewList;
        }

        @Override
        public int getCount() {
            return list == null ? 0 : list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(list.get(position), LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            return list.get(position);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }


    private static final int CODE_RESULTS_REQUEST = 0xb2;
    private File fileCropUri = new File(Environment.getExternalStorageDirectory().getPath() + "/cropPhoto.jpg");
    private Uri cropImageUri;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            Bundle bundle = data.getExtras();
            View view = viewList.get(position);
            final ImageView iv = view.findViewById(R.id.mPic);
            car_name = mCarName.getText().toString().trim();
            switch (requestCode) {
                case 0:
                    //拍照
                    popupWindow.dismiss();
                    path = bundle.getString("maximgPath");
                    File file = new File(path);
                    byte[] byimag = getBytes(file);
                    final Bitmap map = ImageTools.byteToBitmap(byimag);
                    /* 压缩原图 */
                    final Bitmap newmap = PhotoUtils.imageCompressL(map);

                    dialog = MyProgressDialog.createDialog(CarPhotoActivity.this);
                    dialog.setMessage("正在上传图片");
                    dialog.show();
                    final Map<String, String> m = new HashMap<>();
                    m.put("file_content", PhotoUtils.bitmapToBase64(map));
                    OKhttptils.post(CarPhotoActivity.this, Config.UPLOADFILE, m, new OKhttptils.HttpCallBack() {
                        @Override
                        public String success(String response) {
                            try {
                                Prefs.with(getApplicationContext()).remove(car_name);
                                JSONObject jsonObject = new JSONObject(response);
                                String data = jsonObject.getString("data");
                                JSONObject object = new JSONObject(data);
                                String file_id = object.getString("file_id");
                                Prefs.with(getApplicationContext()).write(car_name, file_id);

                                /* 上传压缩图片 */
                                m.clear();
                                m.put("file_content", PhotoUtils.bitmapToBase64(newmap));
                                OKhttptils.post(CarPhotoActivity.this, Config.UPLOADFILE, m, new OKhttptils.HttpCallBack() {
                                    @Override
                                    public String success(String response) {
                                        Prefs.with(getApplicationContext()).remove(car_name + "压缩");
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            String data = jsonObject.getString("data");
                                            JSONObject object = new JSONObject(data);
                                            String sfile_id = object.getString("file_id");
                                            Prefs.with(getApplicationContext()).write(car_name + "压缩", sfile_id);
                                            dialog.dismiss();
                                            iv.setImageBitmap(map);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        return response;
                                    }

                                    @Override
                                    public void fail(String response) {

                                    }
                                });


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            return response;
                        }

                        @Override
                        public void fail(String response) {

                        }
                    });


                    break;
                case 666:
                    //相册选择
                    if (hasSdcard()) {
                        cropImageUri = Uri.fromFile(fileCropUri);
                        Log.e("拍照", "onActivityResult啦啦啦啦: " + data.getData());
                        Uri newUri = Uri.parse(PhotoUtils.getPath(this, data.getData()));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            newUri = FileProvider.getUriForFile(this, "com.xtzhangbinbin.jpq.fileProvider", new File(newUri.getPath()));
                        }
                        PhotoUtils.cropImageUri(this, newUri, cropImageUri, 3, 2, 900, 600, 888);
                    } else {
                        ToastUtil.show(this, "设备没有SD卡！");
                    }
                    break;
                case 888:
                    final Bitmap bitmap = PhotoUtils.getBitmapFromUri(cropImageUri, this);
                    //上传base64
                    popupWindow.dismiss();

                    /* 压缩原图 */
                    final Bitmap newmap2 = PhotoUtils.imageCompressL(bitmap);

                    dialog = MyProgressDialog.createDialog(CarPhotoActivity.this);
                    dialog.setMessage("正在上传图片");
                    dialog.show();
                    final Map<String, String> mm = new HashMap<>();
                    mm.put("file_content", PhotoUtils.bitmapToBase64(bitmap));
                    OKhttptils.post(CarPhotoActivity.this, Config.UPLOADFILE, mm, new OKhttptils.HttpCallBack() {
                        @Override
                        public String success(String response) {
                            try {
                                Prefs.with(getApplicationContext()).remove(car_name);
                                JSONObject jsonObject = new JSONObject(response);
                                String data = jsonObject.getString("data");
                                JSONObject object = new JSONObject(data);
                                String file_id = object.getString("file_id");
                                Prefs.with(getApplicationContext()).write(car_name, file_id);

                                /* 上传压缩图片 */
                                mm.clear();
                                mm.put("file_content", PhotoUtils.bitmapToBase64(newmap2));
                                OKhttptils.post(CarPhotoActivity.this, Config.UPLOADFILE, mm, new OKhttptils.HttpCallBack() {
                                    @Override
                                    public String success(String response) {
                                        Prefs.with(getApplicationContext()).remove(car_name + "压缩");
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            String data = jsonObject.getString("data");
                                            JSONObject object = new JSONObject(data);
                                            String sfile_id = object.getString("file_id");
                                            Prefs.with(getApplicationContext()).write(car_name + "压缩", sfile_id);
                                            dialog.dismiss();
                                            iv.setImageBitmap(bitmap);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        return response;
                                    }

                                    @Override
                                    public void fail(String response) {

                                    }
                                });


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            return response;
                        }

                        @Override
                        public void fail(String response) {

                        }
                    });
                    break;
            }
        }
    }


    /* 上传图片base64并获取图片id */


    public void upDataBase64(final Context context, final Bitmap bitmap, final String sType) {
        Map<String, String> map = new HashMap<>();
        map.put("file_content", PhotoUtils.bitmapToBase64(bitmap));
        OKhttptils.post((Activity) context, Config.UPLOADFILE, map, new OKhttptils.HttpCallBack() {
            @Override
            public String success(String response) {
                try {
                    Prefs.with(context).remove(sType);
                    JSONObject jsonObject = new JSONObject(response);
                    String data = jsonObject.getString("data");
                    JSONObject object = new JSONObject(data);
                    String file_id = object.getString("file_id");
                    Prefs.with(context).write(sType, file_id);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return response;
            }

            @Override
            public void fail(String response) {

            }
        });
    }


    /* 将文件转换为byte[] */
    private byte[] getBytes(File file) {
        byte[] buffer = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1024 * 8);
            byte[] b = new byte[1024 * 8];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    @Override
    protected void onStart() {
        super.onStart();
        setTitle("上传图片");
        setLeftImageClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JumpUtil.newInstance().finishRightTrans(CarPhotoActivity.this);
            }
        });
    }
}
