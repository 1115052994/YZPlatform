package com.xtzhangbinbin.jpq.fragment.main;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.activity.CityActivity;
import com.xtzhangbinbin.jpq.activity.SearchActivity;
import com.xtzhangbinbin.jpq.fragment.BuyCar;
import com.xtzhangbinbin.jpq.fragment.Sellcars;
import com.xtzhangbinbin.jpq.utils.JumpUtil;
import com.xtzhangbinbin.jpq.utils.Prefs;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by glp on 2018/4/18.
 * 描述：车买卖
 */

public class CarBuyFragment extends Fragment{
    @BindView(R.id.tv_buy)
    TextView tvBuy;
    @BindView(R.id.tv_sell)
    TextView tvSell;
    Unbinder unbinder;
    @BindView(R.id.ly_buy)
    LinearLayout lyBuy;
    @BindView(R.id.ly_sell)
    LinearLayout lySell;
    @BindView(R.id.mLocation)
    TextView mLocation;
    private FragmentManager fm;
    private Fragment buyFragment;
    private Sellcars sellFragment;
    private FragmentTransaction transaction;

    private View view;
    private String cityName = "";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_car, container, false);
            buyFragment = new BuyCar();
            sellFragment = new Sellcars();
        }
        unbinder = ButterKnife.bind(this, view);
        //initLoc();
        Bundle bundle = getArguments();
        if (bundle != null) {
            String type = bundle.getString("type", "");
            if ("carbuy".equals(type)) {
                String param = bundle.getString("param");
                lyBuy.setSelected(false);
                lySell.setSelected(false);
                tvSell.setTextColor(Color.parseColor("#ff9696"));
                tvBuy.setTextColor(Color.parseColor("#ffffff"));
                fm = getFragmentManager();
                transaction = fm.beginTransaction();
                transaction.replace(R.id.content, buyFragment);
                transaction.commit();
                ((BuyCar) buyFragment).setParam(param);
            } else if ("searchcar".equals(type)){
                String searchData =
                bundle.getString("brand")+","+
                bundle.getString("brandId")+","+
                bundle.getString("carName")+","+
                bundle.getString("carId");
                lyBuy.setSelected(false);
                lySell.setSelected(false);
                tvSell.setTextColor(Color.parseColor("#ff9696"));
                tvBuy.setTextColor(Color.parseColor("#ffffff"));
                fm = getFragmentManager();
                transaction = fm.beginTransaction();
                transaction.replace(R.id.content, buyFragment);
                transaction.commit();
                ((BuyCar) buyFragment).setParam(searchData);
            } else if ("2".equals(type)){
                lySell.setSelected(true);
                lyBuy.setSelected(true);
                tvBuy.setTextColor(Color.parseColor("#ff9696"));
                tvSell.setTextColor(Color.parseColor("#ffffff"));
                fm = getFragmentManager();
                transaction = fm.beginTransaction();
                transaction.replace(R.id.content, sellFragment);
                transaction.commit();
            }
            // ---------Bundle设置后以后不为空--------
            setArguments(null);
        } else {
            initView();
        }
        // 城市
        String city = Prefs.with(getContext()).read("city");
        String cityid = Prefs.with(getContext()).read("cityId");
        if (city!=null&&!"".equals(city)) {
            if (!city.equals(cityName)) {
                cityName = city;
                mLocation.setText(city);
                Intent intent = new Intent();
                intent.setAction("city");
                intent.putExtra("city", city);
                intent.putExtra("cityId", cityid);
                getContext().sendStickyBroadcast(intent);
            }
        }
        return view;
    }

    private void initView() {
        fm = getFragmentManager();
        transaction = fm.beginTransaction();
        transaction.replace(R.id.content, buyFragment);
        transaction.commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.ly_buy, R.id.ly_sell, R.id.mSearch, R.id.mLocation})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ly_buy:
                lyBuy.setSelected(false);
                lySell.setSelected(false);
                tvSell.setTextColor(Color.parseColor("#ff9696"));
                tvBuy.setTextColor(Color.parseColor("#ffffff"));
                transaction = fm.beginTransaction();
                transaction.replace(R.id.content, buyFragment);
                transaction.commit();
                break;
            case R.id.ly_sell:
                lySell.setSelected(true);
                lyBuy.setSelected(true);
                tvBuy.setTextColor(Color.parseColor("#ff9696"));
                tvSell.setTextColor(Color.parseColor("#ffffff"));
                transaction = fm.beginTransaction();
                transaction.replace(R.id.content, sellFragment);
                transaction.commit();
                break;
            case R.id.mSearch:
                JumpUtil.newInstance().jumpRight(getContext(), SearchActivity.class, "car");
                break;
            case R.id.mLocation:
                JumpUtil.newInstance().jumpRight(getContext(), CityActivity.class);
                CityActivity.setOnBackListener(new CityActivity.BackListener() {
                    @Override
                    public void backData(String city) {
                        mLocation.setText(city);
                        Intent intent = new Intent();
                        intent.setAction("city");
                        intent.putExtra("city", city);
                        getContext().sendBroadcast(intent);
                    }
                });
                break;
        }
    }


}
