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
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.plt.yzplatform.R;
import com.plt.yzplatform.adapter.CommonRecyclerAdapter;
import com.plt.yzplatform.adapter.ViewHolder;
import com.plt.yzplatform.base.BaseActivity;
import com.plt.yzplatform.config.Config;
import com.plt.yzplatform.entity.CompDetailBean;
import com.plt.yzplatform.utils.JumpUtil;
import com.plt.yzplatform.utils.NetUtil;
import com.plt.yzplatform.utils.Prefs;
import com.plt.yzplatform.utils.ToastUtil;
import com.plt.yzplatform.view.CircleImageView;
import com.plt.yzplatform.view.indicator.IndicatorAdapter;
import com.plt.yzplatform.view.indicator.TrackIndicatorView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

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
    @BindView(R.id.comp_name)
    TextView compName;
    @BindView(R.id.star1)
    CheckBox star1;
    @BindView(R.id.star2)
    CheckBox star2;
    @BindView(R.id.star3)
    CheckBox star3;
    @BindView(R.id.star4)
    CheckBox star4;
    @BindView(R.id.star5)
    CheckBox star5;
    @BindView(R.id.num_tv)
    TextView numTv;
    @BindView(R.id.loc_tv)
    TextView locTv;

    private List<String> test = new ArrayList<>();

    private IndicatorAdapter<View> staffAdapter;//明星员工
    private List<CompDetailBean.DataBean.ResultBean.StaffListBean> staffList = new ArrayList<>();//staffs
    private List<String> images = new ArrayList<>();//banner
    private String comp_id = "24";//商家Id

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comp_detail);
        ButterKnife.bind(this);
        initView();
        initData();
        getData();
        //设置不加载更多
//        smartRefreshLayout.setEnableLoadmore(false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setTitle("商家详情");
        setLeftImageClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JumpUtil.newInstance().finishRightTrans(CompDetail.this);
            }
        });
    }

    private void initView() {
        staffAdapter = new IndicatorAdapter<View>() {
            @Override
            public int getCount() {
                return 5;
            }
            @Override
            public View getView(int position, ViewGroup parent) {
                View view = LayoutInflater.from(CompDetail.this).inflate(R.layout.item_staff, null);
                TextView name = view.findViewById(R.id.name_tv);
                name.setText("name");
//                TextView des = view.findViewById(R.id.staffDes_tv);
//                des.setText(staffList.get(position).getStaff_info());
//                CircleImageView imageView = view.findViewById(R.id.circleImage);
//                getPic(CompDetail.this, staffList.get(position).getStaff_photo_file_id(), imageView);
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
            public void highLightIndicator(View view, int position) {
                super.highLightIndicator(view, position);
                TextView tv = view.findViewById(R.id.title);
                tv.setTextColor(Color.parseColor("#ff7979"));
                LinearLayout bg = view.findViewById(R.id.bg);
                bg.setVisibility(View.VISIBLE);
                Log.i("highLightIndicator", "position=" + position);
            }
            @Override
            public void restoreIndicator(View view) {
                super.restoreIndicator(view);
                LinearLayout bg = view.findViewById(R.id.bg);
                bg.setVisibility(View.INVISIBLE);
                TextView tv = view.findViewById(R.id.title);
                tv.setTextColor(Color.parseColor("#333333"));
            }
        };
        tabsView.setAdapter(tabAdapter);

        banner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                //此处可以自行选择，我直接用的Picasso
                Log.i("banner", "path=" + (String) path);
//                Picasso.with(CompDetail.this).load("http://pic.58pic.com/58pic/14/54/14/09E58PICUpb_1024.jpg").into(imageView);
                getPic(CompDetail.this, (String) path, imageView);
            }
        });

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
        for (int i = 0; i < 5; i++) {
            test.add("i=" + i);
        }
        CommonRecyclerAdapter adapter = new CommonRecyclerAdapter(this, test, R.layout.item_wx) {
            @Override
            public void convert(ViewHolder holder, Object item, int position) {

            }
        };
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void getData() {
        if (NetUtil.isNetAvailable(getApplicationContext())) {
            OkHttpUtils.post()
                    .url(Config.GETCOMPDETAIL)
                    .addHeader("user_token", Prefs.with(this).read("user_token"))
                    .addParams("comp_id", comp_id)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            ToastUtil.noNAR(CompDetail.this);
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            Log.w("onResponse", "onResponse:" + response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getString("status").equals("1")) {
                                    Gson gson = new Gson();
                                    CompDetailBean detailBean = gson.fromJson(response, CompDetailBean.class);
                                    CompDetailBean.DataBean.ResultBean resultBean = detailBean.getData().getResult();
                                    //获取banner
                                    List<CompDetailBean.DataBean.ResultBean.BannerListBean> bannerListBeans = resultBean.getBannerList();
                                    for (CompDetailBean.DataBean.ResultBean.BannerListBean bannerBean : bannerListBeans) {
                                        images.add(bannerBean.getAdve_file_id());
                                    }
                                    banner.setImages(images);
                                    banner.start();
                                    //公司名字星级地址
                                    CompDetailBean.DataBean.ResultBean.InfoBean infoBean = resultBean.getInfo();
                                    compName.setText(infoBean.getAuth_comp_name());
                                    locTv.setText(infoBean.getAuth_comp_addr());
                                    numTv.setText(infoBean.getComp_order_count()+"人");
                                    switch (infoBean.getComp_eval_level()) {
                                        case 5:
                                            star5.setChecked(true);
                                        case 4:
                                            star4.setChecked(true);
                                        case 3:
                                            star3.setChecked(true);
                                        case 2:
                                            star2.setChecked(true);
                                        case 1:
                                            star1.setChecked(true);
                                            break;
                                    }
                                    // 明星员工
                                    staffList.clear();
                                    for (CompDetailBean.DataBean.ResultBean.StaffListBean staffListBean : resultBean.getStaffList()) {
                                        staffList.add(staffListBean);
                                    }
//                                    staffAdapter = new IndicatorAdapter<View>() {
//                                        @Override
//                                        public int getCount() {
//                                            return staffList.size();
//                                        }
//                                        @Override
//                                        public View getView(int position, ViewGroup parent) {
//                                            View view = LayoutInflater.from(CompDetail.this).inflate(R.layout.item_staff, null);
//                                            TextView name = view.findViewById(R.id.name_tv);
//                                            name.setText(staffList.get(position).getStaff_name());
//                                            TextView des = view.findViewById(R.id.staffDes_tv);
//                                            des.setText(staffList.get(position).getStaff_info());
//                                            CircleImageView imageView = view.findViewById(R.id.circleImage);
//                                            getPic(CompDetail.this, staffList.get(position).getStaff_photo_file_id(), imageView);
//                                            return view;
//                                        }
//                                    };
//                                    staffsView.setAdapter(staffAdapter);
                                } else {
                                    ToastUtil.noNAR(CompDetail.this);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } else {
            ToastUtil.noNetAvailable(CompDetail.this);
        }
    }
}
