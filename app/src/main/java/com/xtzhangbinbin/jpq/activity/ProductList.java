package com.xtzhangbinbin.jpq.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;

import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.adapter.GridViewAdapter;
import com.xtzhangbinbin.jpq.adapter.MyFragmentPagerAdapter;
import com.xtzhangbinbin.jpq.base.BaseActivity;
import com.xtzhangbinbin.jpq.fragment.main.MainFragment;
import com.xtzhangbinbin.jpq.fragment.productlist.CarBeauty;
import com.xtzhangbinbin.jpq.fragment.productlist.Maintenance;
import com.xtzhangbinbin.jpq.fragment.productlist.SecondCar;
import com.xtzhangbinbin.jpq.fragment.productlist.SheetMetal;
import com.xtzhangbinbin.jpq.utils.JumpUtil;
import com.xtzhangbinbin.jpq.view.CustomViewPager;
import com.xtzhangbinbin.jpq.view.MyGridView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
/* 商家 - 产品列表 */
public class ProductList extends BaseActivity {
    @BindView(R.id.viewpager)
    CustomViewPager viewpager;
    private MyGridView mGridView;
    private List<String> mList;
    private GridViewAdapter mAdapter;
    int selectorPosition = 0;
    private ArrayList<Fragment> fragments= new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        ButterKnife.bind(this);
        fragments.add(new SecondCar());
        fragments.add(new CarBeauty());
        fragments.add(new SheetMetal());
        fragments.add(new Maintenance());
        fragments.add(new Maintenance());
        viewpager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(),fragments));

        initView();
    }

    private void initView() {
        mGridView = findViewById(R.id.gridView);
        //添加数据
        mList = new ArrayList<>();
        mList.add("二手车");
        mList.add("汽车美容");
        mList.add("钣金维修");
        mList.add("维修保养");
        mList.add("维修保养");
        mAdapter = new GridViewAdapter(this, mList);
        mGridView.setAdapter(mAdapter);
        //gridView的点击事件
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //把点击的position传递到adapter里面去
                mAdapter.changeState(position);
                selectorPosition = position;
                viewpager.setCurrentItem((position));
            }
        });
        viewpager.setOffscreenPageLimit(mList.size());


    }


    @Override
    protected void onStart() {
        super.onStart();
        setTitle("产品列表");
        setLeftImageClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JumpUtil.newInstance().finishRightTrans(ProductList.this);
            }
        });
    }

}
