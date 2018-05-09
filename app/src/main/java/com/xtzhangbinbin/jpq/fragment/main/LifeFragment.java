package com.xtzhangbinbin.jpq.fragment.main;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.squareup.picasso.Picasso;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.activity.CityActivity;
import com.xtzhangbinbin.jpq.activity.ZhongLife;
import com.xtzhangbinbin.jpq.activity.ZhongLifeDetail;
import com.xtzhangbinbin.jpq.adapter.CommonRecyclerAdapter;
import com.xtzhangbinbin.jpq.adapter.ViewHolder;
import com.xtzhangbinbin.jpq.config.Config;
import com.xtzhangbinbin.jpq.entity.MeishiListBean;
import com.xtzhangbinbin.jpq.utils.JumpUtil;
import com.xtzhangbinbin.jpq.utils.OKhttptils;
import com.xtzhangbinbin.jpq.utils.Prefs;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by glp on 2018/4/18.
 * 描述：众生活
 */

public class LifeFragment extends Fragment {

    @BindView(R.id.mLocation)
    TextView mLocation;
    Unbinder unbinder;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;

    private CommonRecyclerAdapter adapter;
    private List<MeishiListBean.DataBean.ResultBean> list = new ArrayList<>();

    // 轮播图
    private List<String> bannersImage = new ArrayList<>();

    private View view;
    private String cityId = "";
    private String cityName = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_life, container, false);
        }
        unbinder = ButterKnife.bind(this, view);
        // 城市
        String city = Prefs.with(getContext()).read("city");
        if (city!=null&&!"".equals(city)){
            mLocation.setText(city);
            cityName = city;
            // 获取城市ID
            String cityid = Prefs.with(getContext()).read("cityId");
            if (cityid!=null&&!"".equals(cityid)) {
                cityId = cityid;
                // 通过城市查询列表信息
                getPageInfo();
            }else{
                getCityId(cityName);
            }
        }
        //initLoc();
        initView();
        getData();
        return view;
    }

    private void initView() {
        // banner
        banner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                OKhttptils.getPic(getActivity(), (String) path, imageView);
            }
        });
//        if (banner != null) {
//            banner.setImages(bannersImage);
//            banner.start();
//        }

        adapter = new CommonRecyclerAdapter(getContext(), list, R.layout.item_zhongshenghuo) {

            @Override
            public void convert(ViewHolder holder, Object item, int position) {
                MeishiListBean.DataBean.ResultBean bean = list.get(position);
                ImageView imageView = holder.getView(R.id.imageView);
//                OKhttptils.getPic(getActivity(),bean.getZsh_img(),imageView);
//                Picasso.with(getContext()).load(bean.getZsh_img()).into(imageView);
                Picasso.get().load(bean.getZsh_img()).into(imageView);
                TextView tvName = holder.getView(R.id.tv_name);
                tvName.setText(bean.getZsh_name());
                TextView tvPrice = holder.getView(R.id.tv_price);
                tvPrice.setText("人均￥" + bean.getZsh_avg_price() + "元");
                TextView tvLoc = holder.getView(R.id.tv_loc);
                tvLoc.setText(bean.getZsh_addr());
                int level = (int) Double.parseDouble(bean.getZsh_level());
                ImageView star1 = holder.getView(R.id.star1);
                ImageView star2 = holder.getView(R.id.star2);
                ImageView star3 = holder.getView(R.id.star3);
                ImageView star4 = holder.getView(R.id.star4);
                ImageView star5 = holder.getView(R.id.star5);
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
            }
        };
        adapter.setOnItemClickListener(new CommonRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Bundle bundle = new Bundle();
                bundle.putString("String",list.get(position).getZsh_id());
                JumpUtil.newInstance().jumpLeft(getActivity(), ZhongLifeDetail.class, bundle);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        smartRefreshLayout.setEnableRefresh(false);
        smartRefreshLayout.setEnableLoadmore(false);
    }

    private void getData() {
        getBanner();
    }

    private void getBanner() {
        Map<String, String> map = new HashMap<>();
        OKhttptils.post(getActivity(), Config.SELECTZSHCENTER, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    JSONObject data = object.getJSONObject("data");
                    JSONArray result = data.getJSONArray("result");
                    bannersImage.clear();
                    for (int i = 0; i < result.length(); i++) {
                        JSONObject object1 = result.getJSONObject(i);
                        String fileId = object1.getString("adve_file_id");
                        bannersImage.add(fileId);
                    }
                    if (banner != null) {
                        banner.setImages(bannersImage);
                        banner.start();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void fail(String response) {

            }
        });
    }

    private void getPageInfo() {
        Map<String, String> map = new HashMap<>();
        map.put("dict_value", "dg");
        map.put("zsh_city", cityId);
        map.put("pageIndex", "1");
        map.put("pageSize", "20");
        OKhttptils.post(getActivity(), Config.GETPAGEINFO, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                Log.i("getData===", response);
                Gson gson = new Gson();
                MeishiListBean meishiListBean = gson.fromJson(response, MeishiListBean.class);
                list.clear();
                List<MeishiListBean.DataBean.ResultBean> resultBeanList = meishiListBean.getData().getResult();
                for (MeishiListBean.DataBean.ResultBean bean : resultBeanList) {
                    list.add(bean);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void fail(String response) {

            }
        });
    }

    //通过城市名获取城市Id
    public void getCityId(final String cityName) {
        Map<String, String> map = new HashMap<>();
        map.put("cityName", cityName);
        OKhttptils.post(getActivity(), Config.GET_CITY_ID, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    JSONObject data = object.getJSONObject("data");
                    JSONObject result = data.getJSONObject("result");
                    cityId = result.getString("city_id");
//                    /**
//                     * 存储城市Id
//                     */
//                    Prefs.with(getActivity()).remove("cityId");
//                    Prefs.with(getActivity()).write("cityId",cityId);
                    // 通过城市查询列表信息
                    getPageInfo();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void fail(String response) {

            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.ly_ms, R.id.ly_xxyl, R.id.ly_shfw, R.id.ly_jiudian})
    public void onViewClicked(View view) {
        Bundle bundle = new Bundle();
        bundle.putString("cityId",cityId);
        switch (view.getId()) {
            case R.id.ly_ms:
                bundle.putString("String","YZzshms");
                JumpUtil.newInstance().jumpLeft(getActivity(), ZhongLife.class,bundle );
                break;
            case R.id.ly_xxyl:
                bundle.putString("String","YZzshxxyl");
                JumpUtil.newInstance().jumpLeft(getActivity(), ZhongLife.class, bundle);
                break;
            case R.id.ly_shfw:
                bundle.putString("String","YZzshshfu");
                JumpUtil.newInstance().jumpLeft(getActivity(), ZhongLife.class, bundle);
                break;
            case R.id.ly_jiudian:
                bundle.putString("String","YZzshjd");
                JumpUtil.newInstance().jumpLeft(getActivity(), ZhongLife.class, bundle);
                break;
        }
    }

    @OnClick(R.id.mLocation)
    public void onViewClicked() {
        // 城市
        JumpUtil.newInstance().jumpRight(getContext(), CityActivity.class);
        CityActivity.setOnBackListener(new CityActivity.BackListener() {
            @Override
            public void backData(String city) {
                mLocation.setText(city);
                cityName = city;
                // 查询城市Id
                getCityId(cityName);
            }
        });
    }
}
