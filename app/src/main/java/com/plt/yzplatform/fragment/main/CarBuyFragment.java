package com.plt.yzplatform.fragment.main;

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

import com.plt.yzplatform.R;
import com.plt.yzplatform.fragment.BuyCar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by glp on 2018/4/18.
 * 描述：车买卖
 */

public class CarBuyFragment extends Fragment {
    @BindView(R.id.tv_buy)
    TextView tvBuy;
    @BindView(R.id.tv_sell)
    TextView tvSell;
    Unbinder unbinder;
    @BindView(R.id.ly_buy)
    LinearLayout lyBuy;
    @BindView(R.id.ly_sell)
    LinearLayout lySell;
    private FragmentManager fm;
    private Fragment buyFragment;
    private FragmentTransaction transaction;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_car, container, false);
        initView();
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    private void initView() {
        fm = getFragmentManager();
        transaction = fm.beginTransaction();
        buyFragment = new BuyCar();
        transaction = fm.beginTransaction();
        transaction.add(R.id.content, buyFragment);
        transaction.commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.ly_buy, R.id.ly_sell, R.id.mSearch})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ly_buy:
                lyBuy.setSelected(false);
                lySell.setSelected(false);
                tvSell.setTextColor(Color.parseColor("#ff9696"));
                tvBuy.setTextColor(Color.parseColor("#ffffff"));
                break;
            case R.id.ly_sell:
                lySell.setSelected(true);
                lyBuy.setSelected(true);
                tvBuy.setTextColor(Color.parseColor("#ff9696"));
                tvSell.setTextColor(Color.parseColor("#ffffff"));
                break;
            case R.id.mSearch:
                break;
        }
    }
}
