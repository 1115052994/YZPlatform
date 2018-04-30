package com.plt.yzplatform.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;

import com.plt.yzplatform.R;
import com.plt.yzplatform.adapter.GridViewAdapter;
import com.plt.yzplatform.adapter.MyFragmentPagerAdapter;
import com.plt.yzplatform.base.BaseActivity;
import com.plt.yzplatform.fragment.productlist.CarBeauty;
import com.plt.yzplatform.fragment.productlist.SecondCar;
import com.plt.yzplatform.utils.JumpUtil;
import com.plt.yzplatform.view.CustomViewPager;
import com.plt.yzplatform.view.MyGridView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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
        viewpager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(),fragments));
        initView();
    }

    private void initView() {
        mGridView = findViewById(R.id.gridView);
        //添加数据
        mList = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            mList.add("第 " + i + " 个");
        }
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
