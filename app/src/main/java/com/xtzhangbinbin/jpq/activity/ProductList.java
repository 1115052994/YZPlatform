package com.xtzhangbinbin.jpq.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;

import com.google.gson.Gson;
import com.tencent.mm.opensdk.utils.Log;
import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.adapter.GridViewAdapter;
import com.xtzhangbinbin.jpq.adapter.MyFragmentPagerAdapter;
import com.xtzhangbinbin.jpq.base.BaseActivity;
import com.xtzhangbinbin.jpq.config.Config;
import com.xtzhangbinbin.jpq.entity.Enterprise;
import com.xtzhangbinbin.jpq.entity.ProductBean;
import com.xtzhangbinbin.jpq.fragment.main.MainFragment;
import com.xtzhangbinbin.jpq.fragment.productlist.CarBeauty;
import com.xtzhangbinbin.jpq.fragment.productlist.Maintenance;
import com.xtzhangbinbin.jpq.fragment.productlist.SecondCar;
import com.xtzhangbinbin.jpq.fragment.productlist.SheetMetal;
import com.xtzhangbinbin.jpq.gson.factory.GsonFactory;
import com.xtzhangbinbin.jpq.utils.JumpUtil;
import com.xtzhangbinbin.jpq.utils.OKhttptils;
import com.xtzhangbinbin.jpq.view.CustomViewPager;
import com.xtzhangbinbin.jpq.view.MyGridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        getDate();

    }
    public void getDate(){
        Map<String, String> map = new HashMap<>();
        OKhttptils.post(ProductList.this, Config.SERVERITEMTYPE, map, new OKhttptils.HttpCallBack() {
                    @Override
                    public void success(String response) {
                        Log.d("aaaaa", "onResponse获取数据1111: " + response);
                        Gson gson = GsonFactory.create();
                        ProductBean enterprise = gson.fromJson(response, ProductBean.class);
                        List<ProductBean.DataBean.ResultBean> result = enterprise.getData().getResult();
                        mList = new ArrayList<>();
                        for (int i = 0; i <result.size() ; i++) {
                          switch (result.get(i).getServerDesc()){
                              case "二手车":
                                  fragments.add(new SecondCar());
                                  mList.add(result.get(i).getServerDesc());
                                  break;
                              case "洗车":
                                  fragments.add(new Maintenance());
                                  mList.add(result.get(i).getServerDesc());
                                  break;
                              case "美容":
                                  fragments.add(new CarBeauty());
                                  mList.add(result.get(i).getServerDesc());
                                  break;
                              case "钣金喷漆":
                                  fragments.add(new SheetMetal());
                                  mList.add(result.get(i).getServerDesc());
                                  break;
                              case "维修保养":
                                  fragments.add(new Maintenance());
                                  mList.add(result.get(i).getServerDesc());
                                  break;
                              case "更换电瓶":
                                  fragments.add(new Maintenance());
                                  mList.add(result.get(i).getServerDesc());
                                  break;
                          }

                        }

                        if(mList.size()<5){
                            for (int i = mList.size(); i <5 ; i++) {
                                mList.add("待添加");
                            }
                        }
                        viewpager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(),fragments));
                        initView();

                    }
                    @Override
                    public void fail(String response) {
                        Log.d("aaaa", "fail: " + response);
                    }
                });
    }

    private void initView() {
        mGridView = findViewById(R.id.gridView);
        mGridView.setNumColumns(5);
        //添加数据
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
