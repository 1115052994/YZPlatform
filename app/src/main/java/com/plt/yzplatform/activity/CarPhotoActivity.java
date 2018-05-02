package com.plt.yzplatform.activity;

import android.Manifest;
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
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.plt.yzplatform.R;
import com.plt.yzplatform.base.BaseActivity;
import com.plt.yzplatform.camera.ImageTools;
import com.plt.yzplatform.camera.PhotographActivity;
import com.plt.yzplatform.utils.JumpUtil;
import com.plt.yzplatform.utils.PhotoUtils;
import com.plt.yzplatform.utils.Prefs;
import com.plt.yzplatform.utils.ToastUtil;
import com.plt.yzplatform.view.HackyViewPager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CarPhotoActivity extends BaseActivity {
    private static final int STORAGE_PERMISSIONS_REQUEST_CODE = 0x04;
    private static final String TAG = "添加图片" ;

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


    private ArrayList<String> arr = new ArrayList<>();//24个名称
    private List<String> string_id = new ArrayList<>();
    private String path = "";
    private String car_id = "";
    private String car_name = "";

    private PopupWindow popupWindow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_photo);
        ButterKnife.bind(this);
//        Intent intent = getIntent();
//        car_id = intent.getStringExtra("car_id");
        car_id = "17495";
        initData();
        initView1();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
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

            mSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String file_id = Prefs.with(getApplicationContext()).read(car_name);
                    Log.d(TAG, "onClick图片名: " + car_name);
                    Log.i(TAG, "onClick图片名: " + file_id);
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
                    Intent intent = new Intent(CarPhotoActivity.this, PhotographActivity.class);
                    startActivityForResult(intent, 0);
                }
            }
        });

    }

    /* viewpager滑动监听 */
    private void initView1() {
        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
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
            ImageView iv = view.findViewById(R.id.mPic);
            car_name = mCarName.getText().toString().trim();
            switch (requestCode) {
                case 0:
                    //拍照
                    path = bundle.getString("maximgPath");
                    File file = new File(path);
                    byte[] byimag = getBytes(file);
                    Bitmap map = ImageTools.byteToBitmap(byimag);
                    //上传base64
                    upDataBase64(CarPhotoActivity.this,map,car_name);

                    iv.setImageBitmap(map);
                    popupWindow.dismiss();
                    break;
                case 666:
                    //相册选择
                    if (hasSdcard()) {
                        cropImageUri = Uri.fromFile(fileCropUri);
                        Log.e("拍照", "onActivityResult啦啦啦啦: " + data.getData());
                        Uri newUri = Uri.parse(PhotoUtils.getPath(this, data.getData()));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            newUri = FileProvider.getUriForFile(this, "com.plt.yzplatform.fileProvider", new File(newUri.getPath()));
                        }
                            PhotoUtils.cropImageUri(this, newUri, cropImageUri, 3, 2, 900, 600, 888);
                    } else {
                        ToastUtil.show(this, "设备没有SD卡！");
                    }
                    break;
                case 888:
                        Bitmap bitmap = PhotoUtils.getBitmapFromUri(cropImageUri, this);
                    //上传base64
                    upDataBase64(CarPhotoActivity.this,bitmap,car_name);
                        iv.setImageBitmap(bitmap);
                        popupWindow.dismiss();
                    break;
            }
        }
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
