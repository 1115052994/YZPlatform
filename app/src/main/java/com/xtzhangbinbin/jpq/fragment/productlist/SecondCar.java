package com.xtzhangbinbin.jpq.fragment.productlist;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.adapter.CollectAdapter;
import com.xtzhangbinbin.jpq.utils.TabUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class SecondCar extends Fragment {


    @BindView(R.id.browsing_tab)
    TabLayout browsingTab;
    @BindView(R.id.secondcar_viewpager)
    ViewPager secondcarViewpager;
    Unbinder unbinder;
    private ArrayList<Fragment> fragment = new ArrayList<>();
    private ArrayList<String> str = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_second_car, container, false);
        unbinder = ButterKnife.bind(this, inflate);
        str.add("未上架");
        str.add("已上架");
        str.add("已下架");
        str.add("交易中");
        str.add("已完成");
        fragment.add(new WeiSJ());
        fragment.add(new YiSJ());
        fragment.add(new YiXJ());
        fragment.add(new JiaoYZ());
        fragment.add(new YiWC());

        secondcarViewpager.setAdapter(new CollectAdapter(getFragmentManager(), fragment, str));
        browsingTab.setupWithViewPager(secondcarViewpager);
        browsingTab.setTabMode(TabLayout.MODE_FIXED);
        TabUtil.showTabTextAdapteIndicator(browsingTab);



        return inflate;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
