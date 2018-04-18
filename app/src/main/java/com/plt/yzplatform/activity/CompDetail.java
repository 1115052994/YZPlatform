package com.plt.yzplatform.activity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.plt.yzplatform.R;
import com.plt.yzplatform.adapter.CommonRecyclerAdapter;
import com.plt.yzplatform.adapter.ViewHolder;
import com.plt.yzplatform.base.BaseActivity;
import com.plt.yzplatform.utils.JumpUtil;
import com.plt.yzplatform.view.indicator.IndicatorAdapter;
import com.plt.yzplatform.view.indicator.TrackIndicatorView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.squareup.picasso.Picasso;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CompDetail extends BaseActivity {

    @BindView(R.id.staffsView)
    TrackIndicatorView staffsView;
    @BindView(R.id.tabsView)
    TrackIndicatorView tabsView;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;


    private List<String> test = new ArrayList<>();
    private List<String> images = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comp_detail);
        ButterKnife.bind(this);
        initView();
        initData();
        //设置不加载更多
//        smartRefreshLayout.setEnableLoadmore(false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setTitle("企业认证");
        setLeftImageClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JumpUtil.newInstance().finishRightTrans(CompDetail.this);
            }
        });
    }

    private void initView() {
        IndicatorAdapter<View> staffAdapter = new IndicatorAdapter<View>() {

            @Override
            public int getCount() {
                return 5;
            }

            @Override
            public View getView(int position, ViewGroup parent) {
                View view = LayoutInflater.from(CompDetail.this).inflate(R.layout.item_staff, null);
                TextView tv = view.findViewById(R.id.name);
                tv.setText("111");
                return view;
            }
        };
        staffsView.setAdapter(staffAdapter);
        IndicatorAdapter<View> tabAdapter = new IndicatorAdapter<View>() {

            @Override
            public int getCount() {
                return 5;
            }
            @Override
            public View getView(int position, ViewGroup parent) {
                View view = LayoutInflater.from(CompDetail.this).inflate(R.layout.item_tab, null);
                TextView tv = view.findViewById(R.id.title);
                tv.setText("tab");
                return view;
            }
            @Override
            public void highLightIndicator(View view,int position) {
                super.highLightIndicator(view,position);
                TextView tv = view.findViewById(R.id.title);
                tv.setTextColor(Color.parseColor("#ff9696"));
                LinearLayout bg = view.findViewById(R.id.bg);
                bg.setVisibility(View.VISIBLE);
                Log.i("highLightIndicator","position="+position);
            }

            @Override
            public void restoreIndicator(View view) {
                super.restoreIndicator(view);
                LinearLayout bg = view.findViewById(R.id.bg);
                bg.setVisibility(View.INVISIBLE);
                TextView tv = view.findViewById(R.id.title);
                tv.setTextColor(Color.parseColor("#222222"));
            }
        };
        tabsView.setAdapter(tabAdapter);

        banner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                //此处可以自行选择，我直接用的Picasso
                Log.i("banner", "path=" + (String) path);
                Picasso.with(CompDetail.this).load((String) path).into(imageView);
            }
        });
        //广告图片从服务器中获取 如果当地没有广告 则显示默认的广告图
        images.add(getUriFromDrawableRes(this, R.drawable.text).toString());
        images.add(getUriFromDrawableRes(this, R.drawable.text).toString());
        images.add(getUriFromDrawableRes(this, R.drawable.text).toString());
        images.add(getUriFromDrawableRes(this, R.drawable.text).toString());
        banner.setImages(images);
        banner.start();
    }

    /* 获取资源中的url */
    public Uri getUriFromDrawableRes(Context context, int id) {
        Resources resources = context.getResources();
        String path = ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                + resources.getResourcePackageName(id) + "/"
                + resources.getResourceTypeName(id) + "/"
                + resources.getResourceEntryName(id);
        return Uri.parse(path);
    }

    private void initData() {
        for (int i = 0; i < 40; i++) {
            test.add("i=" + i);
        }
        CommonRecyclerAdapter adapter = new CommonRecyclerAdapter(this, test, R.layout.item_popup) {
            @Override
            public void convert(ViewHolder holder, Object item, int position) {
                TextView tv = holder.getView(R.id.city);
                tv.setText((String) item);
            }
        };
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}
