package com.plt.yzplatform.fragment.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.plt.yzplatform.R;
import com.plt.yzplatform.fragment.BuyCar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by glp on 2018/4/18.
 * 描述：车买卖
 */

public class CarBuyFragment extends android.support.v4.app.Fragment {
    private FragmentManager fm;
    private Fragment buyFragment;
    private FragmentTransaction transaction;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_car, container,false);
        initView();
        return view;
    }

    private void initView() {
        fm = getFragmentManager();
        transaction = fm.beginTransaction();
        buyFragment = new BuyCar();
        transaction = fm.beginTransaction();
        transaction.add(R.id.content,buyFragment);
        transaction.commit();
    }
}
