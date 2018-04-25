package com.plt.yzplatform.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.plt.yzplatform.R;
import com.plt.yzplatform.adapter.CollectAdapter;
import com.plt.yzplatform.base.BaseActivity;
import com.plt.yzplatform.fragment.CollectCardeal;
import com.plt.yzplatform.fragment.CollectFacilitator;
import com.plt.yzplatform.utils.JumpUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.scwang.smartrefresh.layout.util.DensityUtil.dp2px;

public class MyFavorite extends BaseActivity {
    private ArrayList<Fragment> fragment=new ArrayList<>();
    private ArrayList<String> str=new ArrayList<>();
    @BindView(R.id.collect_tab)
    TabLayout collectTab;
    @BindView(R.id.collect_pager)
    ViewPager collectPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_favorite);
        ButterKnife.bind(this);
        fragment.add(new CollectCardeal(this));
        fragment.add(new CollectFacilitator(this));
        str.add("车辆买卖");
        str.add("服务商");
        collectPager.setAdapter(new CollectAdapter(getSupportFragmentManager(),fragment,str));
        collectTab.setupWithViewPager(collectPager);
        collectTab.setTabMode(TabLayout.MODE_FIXED);
        showTabTextAdapteIndicator(collectTab);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setTitle("我的收藏");
        setLeftImageClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JumpUtil.newInstance().finishRightTrans(MyFavorite.this);
            }
        });
    }
    public static void showTabTextAdapteIndicator(final TabLayout tab) {
        tab.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                tab.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                Class<?> tabLayout = tab.getClass();
                Field tabStrip = null;
                try {
                    tabStrip = tabLayout.getDeclaredField("mTabStrip");
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
                tabStrip.setAccessible(true);
                LinearLayout ll_tab = null;
                try {
                    ll_tab = (LinearLayout) tabStrip.get(tab);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                int maxLen = 0;
                int maxTextSize = 0;
                int tabCount = ll_tab.getChildCount();
                for (int i = 0; i < tabCount; i++) {
                    View child = ll_tab.getChildAt(i);
                    child.setPadding(0, 0, 0, 0);
                    if (child instanceof ViewGroup) {
                        ViewGroup viewGroup = (ViewGroup) child;
                        for (int j = 0; j < ll_tab.getChildCount(); j++) {
                            if (viewGroup.getChildAt(j) instanceof TextView) {
                                TextView tabTextView = (TextView) viewGroup.getChildAt(j);
                                int length = tabTextView.getText().length();
                                maxTextSize = (int) tabTextView.getTextSize() > maxTextSize ? (int) tabTextView.getTextSize() : maxTextSize;
                                maxLen = length > maxLen ? length : maxLen;
                            }
                        }

                    }

                    int margin = (tab.getWidth() / tabCount - (maxTextSize + dp2px(0)) * maxLen) / 2 - dp2px(0);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
                    params.leftMargin = margin;
                    params.rightMargin = margin;
                    child.setLayoutParams(params);
                    child.invalidate();
                }
            }
        });
    }
}
