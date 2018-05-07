package com.xtzhangbinbin.jpq.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.adapter.CommonRecyclerAdapter;
import com.xtzhangbinbin.jpq.adapter.ViewHolder;
import com.xtzhangbinbin.jpq.base.BaseActivity;
import com.xtzhangbinbin.jpq.config.Config;
import com.xtzhangbinbin.jpq.entity.MeishiCompDetail;
import com.xtzhangbinbin.jpq.utils.JumpUtil;
import com.xtzhangbinbin.jpq.utils.OKhttptils;
import com.xtzhangbinbin.jpq.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ZhongLifeDetail extends BaseActivity implements GeocodeSearch.OnGeocodeSearchListener{

    @BindView(R.id.star1)
    ImageView star1;
    @BindView(R.id.star2)
    ImageView star2;
    @BindView(R.id.star3)
    ImageView star3;
    @BindView(R.id.star4)
    ImageView star4;
    @BindView(R.id.star5)
    ImageView star5;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.banner)
    ImageView banner;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.loc_tv)
    TextView locTv;

    // 轮播图
    private List<String> bannersImage = new ArrayList<>();

    private CommonRecyclerAdapter adapter;
    private List<MeishiCompDetail.DataBean.ResultBean> list = new ArrayList<>();

    private String compId = "";

    private GeocodeSearch geocodeSearch;
    private float lat,lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhong_life_detail);
        ButterKnife.bind(this);
        initView();
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                compId = bundle.getString("String");
                if (!"".equals(compId)) {
                    getDeatil(compId);
                }
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        setTitle("详情");
        setLeftImageClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JumpUtil.newInstance().finishRightTrans(ZhongLifeDetail.this);
            }
        });
    }

    private void initView() {
        // banner
//        banner.setImageLoader(new ImageLoader() {
//            @Override
//            public void displayImage(Context context, Object path, ImageView imageView) {
//                OKhttptils.getPic(ZhongLifeDetail.this, (String) path, imageView);
//            }
//        });
//        if (banner != null) {
//            banner.setImages(bannersImage);
//            banner.start();
//        }

        adapter = new CommonRecyclerAdapter(this, list, R.layout.item_zhongshenghuo_detail) {
            @Override
            public void convert(ViewHolder holder, Object item, int position) {
                MeishiCompDetail.DataBean.ResultBean bean = list.get(position);
                TextView name = holder.getView(R.id.tv_name);
                name.setText(bean.getZsh_item_name());
                TextView des = holder.getView(R.id.tv_shuoming);
                des.setText(bean.getZsh_item_desc());
                TextView price = holder.getView(R.id.tv_price);
                price.setText("￥"+bean.getZsh_item_price());
                TextView num = holder.getView(R.id.tv_num);
                num.setText("已售"+bean.getZsh_item_count());
            }

        };
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @OnClick(R.id.image_dh)
    public void onViewClicked() {
        // 导航
        ToastUtil.show(this,"正在进入导航，请稍等。");
        navigation(lat,lon);
    }

    private void getDeatil(String compId) {
        Map<String, String> map = new HashMap<>();
        map.put("zsh_id", compId);
        OKhttptils.post(this, Config.GETBUSDETAILS, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                try {
                    Gson gson = new Gson();
                    MeishiCompDetail detail = gson.fromJson(response, MeishiCompDetail.class);
                    list.clear();
                    List<MeishiCompDetail.DataBean.ResultBean> resultBeans = detail.getData().getResult();
                    for (int i = 0; i < resultBeans.size(); i++) {
                        MeishiCompDetail.DataBean.ResultBean bean = resultBeans.get(i);
                        if (i == 0) {
                            tvName.setText(bean.getZsh_name());
                            tvPrice.setText("人均￥"+bean.getZsh_avg_price()+"元");
                            locTv.setText(bean.getZsh_addr());
                            Picasso.with(getApplicationContext()).load(bean.getZsh_img()).into(banner);
                            int level = (int) bean.getZsh_level();
                            switch (level) {
                                case 5:
                                    star5.setImageResource(R.drawable.pj_pinkstar);
                                case 4:
                                    star4.setImageResource(R.drawable.pj_pinkstar);
                                case 3:
                                    star3.setImageResource(R.drawable.pj_pinkstar);
                                case 2:
                                    star2.setImageResource(R.drawable.pj_pinkstar);
                                case 1:
                                    star1.setImageResource(R.drawable.pj_pinkstar);
                                    break;
                            }
                            // 获取经纬度
                            GeocodeSearch(bean.getZsh_addr());
                        }
                        list.add(bean);
                    }
                    adapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void fail(String response) {

            }
        });
    }




    //发起正地理编码搜索
    public void GeocodeSearch(String city) {
        //构造 GeocodeSearch 对象，并设置监听。
        geocodeSearch = new GeocodeSearch(this);
        geocodeSearch.setOnGeocodeSearchListener(this);
        //通过GeocodeQuery设置查询参数,调用getFromLocationNameAsyn(GeocodeQuery geocodeQuery) 方法发起请求。
        //address表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode都ok
        GeocodeQuery query = new GeocodeQuery(city, city);
        geocodeSearch.getFromLocationNameAsyn(query);
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {

    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
        if (i == AMapException.CODE_AMAP_SUCCESS) {
            if (geocodeResult != null && geocodeResult.getGeocodeAddressList() != null
                    && geocodeResult.getGeocodeAddressList().size() > 0) {
                GeocodeAddress address = geocodeResult.getGeocodeAddressList().get(0);
                //获取到的经纬度
                LatLonPoint latLongPoint = address.getLatLonPoint();
                lat = (float)latLongPoint.getLatitude();
                lon = (float)latLongPoint.getLongitude();
            }
        }
    }
}
