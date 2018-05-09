package com.xtzhangbinbin.jpq.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.tencent.mm.opensdk.utils.Log;
import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.adapter.GridViewAdapter;
import com.xtzhangbinbin.jpq.adapter.MyFragmentPagerAdapter;
import com.xtzhangbinbin.jpq.adapter.ProductGridViewAdapter;
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
import com.xtzhangbinbin.jpq.view.MyProgressDialog;

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
    private GridView mGridView;
    private List<String> mList;
    private ProductGridViewAdapter mAdapter;
    private ArrayList<Fragment> fragments= new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        ButterKnife.bind(this);
        dialog = MyProgressDialog.createDialog(this);
        getDate();

    }
    public void getDate(){
        showDialog("正在加载产品项");
        Map<String, String> map = new HashMap<>();
        OKhttptils.post(ProductList.this, Config.ALLSERVERITEMTYPE, map, new OKhttptils.HttpCallBack() {
                    @Override
                    public void success(String response) {
                        Log.d("aaaaa", "onResponse获取数据1111: " + response);
                        closeDialog();
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
                                  fragments.add(new Maintenance(result.get(i).getServerId()));
                                  mList.add(result.get(i).getServerDesc());
                                  break;
                              case "美容":
                                  fragments.add(new CarBeauty(result.get(i).getServerId()));
                                  mList.add(result.get(i).getServerDesc());
                                  break;
                              case "钣金喷漆":
                                  fragments.add(new SheetMetal(result.get(i).getServerId()));
                                  mList.add(result.get(i).getServerDesc());
                                  break;
                              case "维修保养":
                                  fragments.add(new Maintenance(result.get(i).getServerId()));
                                  mList.add(result.get(i).getServerDesc());
                                  break;
                              case "更换电瓶":
                                  fragments.add(new Maintenance(result.get(i).getServerId()));
                                  mList.add(result.get(i).getServerDesc());
                                  break;
                          }

                        }
                        viewpager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(),fragments));
                        initView();

                    }
                    @Override
                    public void fail(String response) {
                        closeDialog();
                        Log.d("aaaa", "fail: " + response);
                    }
                });
    }

    private void initView() {
        mGridView = findViewById(R.id.gridView);
        setGridView();
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //把点击的position传递到adapter里面去
                mAdapter.changeState(position);
                viewpager.setCurrentItem(position);
            }
        });
        viewpager.setOffscreenPageLimit(mList.size());



    }
    /**设置GirdView参数，绑定数据*/
    private void setGridView() {
        int size = mList.size();
        int length = 70;
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        float density = dm.density;
        int gridviewWidth = (int) (size * (length + 4) * density);
        int itemWidth = (int) (length * density);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(gridviewWidth, LinearLayout.LayoutParams.FILL_PARENT);
        mGridView.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
        mGridView.setColumnWidth(itemWidth); // 设置列表项宽
        mGridView.setHorizontalSpacing(-1); // 设置列表项水平间距
        mGridView.setStretchMode(GridView.NO_STRETCH);
        mGridView.setNumColumns(size); // 设置列数量=列表集合数
        mAdapter = new ProductGridViewAdapter(this, mList);
        mGridView.setAdapter(mAdapter);
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
