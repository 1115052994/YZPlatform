package com.xtzhangbinbin.jpq.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.adapter.CollectAdapter;
import com.xtzhangbinbin.jpq.base.BaseActivity;
import com.xtzhangbinbin.jpq.fragment.BrowseCarRecord;
import com.xtzhangbinbin.jpq.fragment.BrowseServerRecord;
import com.xtzhangbinbin.jpq.utils.JumpUtil;
import com.xtzhangbinbin.jpq.utils.TabUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyBrowsing extends BaseActivity {
    @BindView(R.id.browsing_tab)
    TabLayout browsingTab;
    @BindView(R.id.browsing_pager)
    ViewPager browsingPager;
    private ArrayList<Fragment> fragment = new ArrayList<>();
    private ArrayList<String> str = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_browsing);
        ButterKnife.bind(this);
        fragment.add(new BrowseCarRecord(MyBrowsing.this));
        fragment.add(new BrowseServerRecord(MyBrowsing.this));
        str.add("车辆买卖");
        str.add("服务商");
        browsingPager.setAdapter(new CollectAdapter(getSupportFragmentManager(),fragment,str));
        browsingTab.setupWithViewPager(browsingPager);
        browsingTab.setTabMode(TabLayout.MODE_FIXED);
        TabUtil.showTabTextAdapteIndicator(browsingTab);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setTitle("浏览记录");
        setLeftImageClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JumpUtil.newInstance().finishRightTrans(MyBrowsing.this);
            }
        });
    }
}
