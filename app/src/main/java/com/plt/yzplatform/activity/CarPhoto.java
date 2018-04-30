package com.plt.yzplatform.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.plt.yzplatform.R;
import com.plt.yzplatform.adapter.CarPhotoAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CarPhoto extends AppCompatActivity {
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.radio_button1)
    RadioButton radioButton1;
    @BindView(R.id.radio_button2)
    RadioButton radioButton2;
    @BindView(R.id.number_text)
    TextView numberText;
    @BindView(R.id.sum_text)
    TextView sumText;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    private ArrayList<Fragment> fragments = new ArrayList<>();
    private ArrayList<String> arr = new ArrayList<>();
    private int index=1;
    //保存上一次数量
    private int op;
    private int dex=2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_photo);
        ButterKnife.bind(this);
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

        for (int i = 0; i < 24; i++) {
            fragments.add(new com.plt.yzplatform.fragment.CarPhoto());
        }
       ;
        viewPager.setAdapter(new CarPhotoAdapter(getSupportFragmentManager(), fragments));
        initView();
        Intent intent = getIntent();
        String result = intent.getStringExtra("result");

    }



    private void initView() {
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d("aaaaa", "onPageScrolled: 1111111");
                op=index;
                if(dex==2){
                    dex=3;
                }else {
                    index=position+1;
                    name.setText(arr.get(index-1));
                    numberText.setText(index+"");
                }

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        radioButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(index>1){
                    index--;
                    numberText.setText(index+"");
                    name.setText(arr.get(index-1));
                    viewPager.setCurrentItem(index-1);
                    Log.d("aaaaa", "onClick:11111111 ");
                    dex=2;
                }
            }
        });
        radioButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(index<24){
                    index++;
                    numberText.setText(index+"");
                    name.setText(arr.get(index-1));
                    viewPager.setCurrentItem(index-1);
                    Log.d("aaaaa", "onClick:22222222 ");
                    dex=2;
                }
            }
        });


    }


    //    @Override
//    protected void onStart() {
//        super.onStart();
//        setTitle("车辆照片");
//        setLeftImageClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                JumpUtil.newInstance().finishRightTrans(CarPhoto.this);
//            }
//        });
//    }
}
