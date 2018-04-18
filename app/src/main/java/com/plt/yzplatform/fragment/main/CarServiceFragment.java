package com.plt.yzplatform.fragment.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.plt.yzplatform.R;

/**
 * Created by glp on 2018/4/18.
 * 描述：车服务
 */

public class CarServiceFragment extends android.support.v4.app.Fragment {
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_car_service,null);
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
