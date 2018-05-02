package com.plt.yzplatform;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.plt.yzplatform.base.BaseActivity;
import com.plt.yzplatform.camera.ImageTools;
import com.plt.yzplatform.camera.PhotographActivity;
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

public class DemoActivity extends BaseActivity {

    @BindView(R.id.viewpager)
    HackyViewPager viewpager;

    private List<View> viewList = new ArrayList<>();
    private MyAdapter adapter;
    private int index = 0;//点击的哪一个item

    private String path = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        ButterKnife.bind(this);
    }


    private void initView() {
        for (int i = 0; i < 24; i++) {
            final int a = i;
            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.fragment_car_photo, null);
            ImageView iv = view.findViewById(R.id.mPic);
            WindowManager wm = (WindowManager) this
                    .getSystemService(Context.WINDOW_SERVICE);
            int width = wm.getDefaultDisplay().getWidth();
            int height = wm.getDefaultDisplay().getHeight();
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (width / 3 * 2));
            iv.setLayoutParams(params);
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int checkCallPhonePermission = ContextCompat.checkSelfPermission(DemoActivity.this, Manifest.permission.CAMERA);
                    ActivityCompat.requestPermissions(DemoActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 222);
                    ActivityCompat.requestPermissions(DemoActivity.this, new String[]{Manifest.permission.CAMERA}, 222);
                    if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(DemoActivity.this, new String[]{Manifest.permission.CAMERA}, 222);
                        return;
                    } else {
                        index = a;
                        Intent intent = new Intent(DemoActivity.this, PhotographActivity.class);
                        startActivityForResult(intent, 0);
                    }
                }
            });

            viewList.add(view);
        }
        adapter = new MyAdapter(viewList);
        viewpager.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        viewpager.setCurrentItem(index);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null) {
            return;
        }
        Bundle extras = data.getExtras();
        if (extras != null) {
            String path = extras.getString("maximgPath");
            if (path != null) {
                Log.d("哒哒哒", "handleMessage图片路径: " + path);
                File file = new File(path);
                byte[] byimag = getBytes(file);
                Bitmap map = ImageTools.byteToBitmap(byimag);
                View view = viewList.get(index);
                ImageView iv = view.findViewById(R.id.mPic);
                iv.setImageBitmap(map);
            }
        }
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

    /**
     * 将文件转换为byte[]
     *
     * @param file
     * @return
     * @throws IOException
     */
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
}
