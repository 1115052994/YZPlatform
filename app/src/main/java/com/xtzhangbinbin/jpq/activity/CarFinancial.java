package com.xtzhangbinbin.jpq.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.androidkun.xtablayout.XTabLayout;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;
import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.adapter.CarFinancialAdapter;
import com.xtzhangbinbin.jpq.fragment.CarFinancialPersonal;
import com.xtzhangbinbin.jpq.utils.OKhttptils;
import com.xtzhangbinbin.jpq.view.CustomViewPagerShow;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CarFinancial extends AppCompatActivity {

    @BindView(R.id.tab)
    XTabLayout tab;
    @BindView(R.id.view_pager)
    CustomViewPagerShow viewPager;
    @BindView(R.id.pagerview)
    RollPagerView pagerview;
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private ArrayList<String> arrayList = new ArrayList<>();
    private ArrayList<String> lun=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_financial);
        ButterKnife.bind(this);
        fragments.add(new CarFinancialPersonal());
//        fragments.add(new CarFinancialPersonal());
        arrayList.add("个人");
//        arrayList.add("企业");
        viewPager.setAdapter(new CarFinancialAdapter(getSupportFragmentManager(), fragments, arrayList));
        tab.setxTabDisplayNum(1);
        tab.setupWithViewPager(viewPager);
        tab.setVisibility(View.GONE);
        //轮播图
        if(lun!=null){
            pagerview.setAdapter(new ImageNormalAdapter());
        }


    }
    private class ImageNormalAdapter extends StaticPagerAdapter {
        @Override
        public View getView(ViewGroup container, int position) {
            ImageView view = new ImageView(container.getContext());
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            OKhttptils.getPicByHttp(container.getContext(),lun.get(position),view);
            return view;
        }


        @Override
        public int getCount() {
            return lun.size();
        }
    }
}

