package com.xtzhangbinbin.jpq.fragment.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.scwang.smartrefresh.layout.util.DensityUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.activity.CityActivity;
import com.xtzhangbinbin.jpq.activity.ETC;
import com.xtzhangbinbin.jpq.activity.LoginActivity;
import com.xtzhangbinbin.jpq.activity.PoiAroundSearchActivity;
import com.xtzhangbinbin.jpq.activity.SOS;
import com.xtzhangbinbin.jpq.activity.SearchActivity;
import com.xtzhangbinbin.jpq.activity.WeizhangQuery;
import com.xtzhangbinbin.jpq.adapter.CarServiceListAdapter;
import com.xtzhangbinbin.jpq.adapter.MainGridViewAdapter;
import com.xtzhangbinbin.jpq.adapter.MainViewPagerAdapter;
import com.xtzhangbinbin.jpq.config.Config;
import com.xtzhangbinbin.jpq.entity.CarServiceImage;
import com.xtzhangbinbin.jpq.entity.CarServiceList;
import com.xtzhangbinbin.jpq.gson.factory.GsonFactory;
import com.xtzhangbinbin.jpq.utils.JumpUtil;
import com.xtzhangbinbin.jpq.utils.OKhttptils;
import com.xtzhangbinbin.jpq.utils.Prefs;
import com.xtzhangbinbin.jpq.utils.ToastUtil;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

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
 * 描述：车服务
 */
@SuppressWarnings("all")
public class CarServiceFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "车服";
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.viewpager)
    ViewPager mPager;
    @BindView(R.id.ll_dot)
    LinearLayout mLlDot;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    Unbinder unbinder;
    @BindView(R.id.iv)
    ImageView iv;
    @BindView(R.id.rank)
    TextView rank;
    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;
    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.rl_menu)
    RelativeLayout rlMenu;
    @BindView(R.id.mLocation)
    TextView mLocation;
    @BindView(R.id.rl_titleBar)
    RelativeLayout rlTitleBar;
    private TextView mDistance;
    private TextView mApprise;
    private TextView mHot;
    private TextView mSee;

    private View view;

    private PopupWindow popupWindow;

    /* 数据 */
    private CarServiceListAdapter adapter;
    private int pageTotal;//总页数
    private int pageIndex = 1;//第几页
    private String dict_id = "";//服务项id
    private String sort = "ASC";//升序OR降序 升序（ASC）降序（DESC）距离固定ASC
    private String order_by = "apart";//排序字段（距离 apart， 评价comp_eval_level，热度 comp_order_count， 浏览量comp_visit_count）默认传距离
    private String city_id;//城市id
    private String city;//当前城市
    private String lon;//当前经度
    private String lat;//当前纬度


    /* 滑动菜单 */
    private List<String> images = new ArrayList<>();
    private List<CarServiceImage.DataBean.ResultBean.ItemListBean> itemListBeans = new ArrayList<>();
    private List<View> mPagerList;
    private LayoutInflater inflater;
    private int pageCount;    //总的页数
    private int pageSize = 10;    //每一页显示的个数
    private int curIndex = 0;    // 当前显示的是第几页

    private List<CarServiceImage.DataBean.ResultBean.BannerListBean> bannerList = new ArrayList<>();

    private List<CarServiceList.DataBean.ResultBean> beanList = new ArrayList<>();

    private RxPermissions rxPermission;
    private LinearLayoutManager manager;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 004:
                    city_id = (String) msg.obj;
                    /* 初始化数据源 banner图+菜单图 */
                    initData();
                    //initView();
                    break;
                case 001:
                    bannerList = (List<CarServiceImage.DataBean.ResultBean.BannerListBean>) msg.obj;
                    initBanner();
                    break;
                case 002:
                    itemListBeans = (List<CarServiceImage.DataBean.ResultBean.ItemListBean>) msg.obj;
                    initMenu();
                    break;
//                case 003:
//                    beanList = (List<CarServiceList.DataBean.ResultBean>) msg.obj;
//                    setAdapter();
//                    break;
            }
        }
    };

    /* 填充数据 */
    private void setAdapter() {
        adapter = new CarServiceListAdapter(beanList, getContext());
        //---------------jiang----防止空指针-------------
        if (adapter!=null&&recyclerView!=null)
            recyclerView.setAdapter(adapter);
    }


    /* 填充菜单 */
    private void initMenu() {
         /* 首页分类 */
        if (getContext()==null) {
            return;
        }
        inflater = LayoutInflater.from(getContext());
        //总的页数=总数/每页数量，并取整
        pageCount = (int) Math.ceil(itemListBeans.size() * 1.0 / pageSize);
        mPagerList = new ArrayList<View>();
        for (int i = 0; i < pageCount; i++) {
            // 每个页面都是inflate出一个新实例
            GridView gridView = (GridView) inflater.inflate(R.layout.gridview, mPager, false);
            gridView.setAdapter(new MainGridViewAdapter(getContext(), itemListBeans, i, pageSize));
            mPagerList.add(gridView);

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    int pos = position + curIndex * pageSize;

                    //inner为条件筛选 其他为跳转界面
                    if ("inner".equals(itemListBeans.get(pos).getServerLinkType())) {
                        dict_id = itemListBeans.get(pos).getServerId();
                        getData(dict_id, order_by, sort, city_id, 1);
                        Log.i(TAG, "onItemClick啦啦啦: " + dict_id);
                    } else {
                        switch (itemListBeans.get(pos).getServerDesc()){
                            case "紧急救援":
                                JumpUtil.newInstance().jumpRight(getContext(), SOS.class);
                                break;
                            case "ETC":
                                JumpUtil.newInstance().jumpRight(getContext(), ETC.class);
                                break;
                            case "加油站":
                                Bundle bundle = new Bundle();
                                bundle.putDouble("lat", Double.parseDouble(lat));
                                bundle.putDouble("lon", Double.parseDouble(lon));
                                JumpUtil.newInstance().jumpLeft(getActivity(), PoiAroundSearchActivity.class,bundle);
                                break;
                            case "违章查询":
                                if(null != Prefs.with(getContext()).read("user_token")&&!"".equals(Prefs.with(getContext()).read("user_token"))){
                                    // 违章查询
                                    JumpUtil.newInstance().jumpLeft(getActivity(), WeizhangQuery.class);
                                } else {
                                    JumpUtil.newInstance().jumpRight(getContext(), LoginActivity.class);
                                }
                                break;
                        }
                    }
                }
            });
        }
        //设置适配器
        mPager.setAdapter(new MainViewPagerAdapter(mPagerList));
        //设置圆点
        setOvalLayout();

        /* 获取数据列表 */
        getData(dict_id, order_by, sort, city_id, 1);
    }

    /* 填充banner */
    private void initBanner() {
        for (int i = 0; i < bannerList.size(); i++) {
            CarServiceImage.DataBean.ResultBean.BannerListBean listBean = bannerList.get(i);
            String file_id = listBean.getAdve_file_id();
            images.add(file_id);
        }
        if (banner!=null) {
            banner.setImages(images);
            banner.start();
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_car_service, null);
        }
        unbinder = ButterKnife.bind(this, view);

        dict_id = getArguments().getString("dict_id","");
        Log.d(TAG, "onCreateView啦啦啦----: " + dict_id);
        // 城市
        String city = Prefs.with(getContext()).read("city");
        if (city!=null&&!"".equals(city)){
            mLocation.setText(city);
            this.city = city;
            lat = Prefs.with(getContext()).read("lat");
            lon = Prefs.with(getContext()).read("lon");
            // 获取城市ID
            String cityid = Prefs.with(getContext()).read("cityId");
            if (cityid!=null&&!"".equals(cityid)) {
                city_id = cityid;
                 /* 初始化数据源 banner图+菜单图 */
                initData();
                //initView();
            }else{
                getCityId(city);
            }
        }
        //-------initView-----
        initView();
        manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        /* 请求定位权限 */
        // initLoc();
        /* 设置下拉刷新、上拉加载 */
        setView();
        return view;
    }

    /* 下拉刷新 上拉加载 */
    private void setView() {
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                pageIndex = 1;
                getData(dict_id, order_by, sort, city_id, 1);
                refreshlayout.finishRefresh(2000);
            }
        });
        smartRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(final RefreshLayout refreshlayout) {

                Map<String, String> map = new HashMap<>();
                map.put("pageIndex", String.valueOf(++pageIndex));
                map.put("pageSize", "");
                map.put("comp_name", "");
                map.put("lat", lat);
                map.put("lon", lon);
                map.put("prod_service_type_item", dict_id);
                map.put("order_by", order_by);
                map.put("sort", sort);
                map.put("auth_comp_city", city_id);
                OKhttptils.post(getActivity(), Config.GET_CAR_SERVICE, map, new OKhttptils.HttpCallBack() {
                    @Override
                    public void success(String response) {
                        Log.e(TAG, "test上拉加载更多: " + response);
                        Gson gson = GsonFactory.create();
                        CarServiceList list = gson.fromJson(response, CarServiceList.class);
                                        /* 最大页数 */
                        Log.d(TAG, "test总数: " + pageTotal);
                        Log.i(TAG, "test加载的页数: " + pageIndex);
                        if (pageIndex > pageTotal) {
                            refreshlayout.finishLoadmore(2000);
                        } else {
                            List<CarServiceList.DataBean.ResultBean> beanList = list.getData().getResult();

                            for (int i = 0; i < beanList.size(); i++) {
                                adapter.insert(beanList.get(i), adapter.getItemCount());
                                adapter.notifyDataSetChanged();
                                refreshlayout.finishLoadmore(2000);
                            }
                        }

                    }

                    @Override
                    public void fail(String response) {
                        ToastUtil.noNAR(getContext());
                    }
                });

            }
        });
    }


    /* 初始化数据源 */
    private void initData() {
        Map<String, String> map = new HashMap<>();
        OKhttptils.post(getActivity(), Config.GET_SERVICE_IMG, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                Log.d(TAG, "test: " + response);
                Gson gson = GsonFactory.create();
                CarServiceImage serviceImage = gson.fromJson(response, CarServiceImage.class);
                List<CarServiceImage.DataBean.ResultBean.BannerListBean> bannerList = serviceImage.getData().getResult().getBannerList();
                Message message = new Message();
                message.what = 001;
                message.obj = bannerList;
                handler.sendMessage(message);

                final List<CarServiceImage.DataBean.ResultBean.ItemListBean> itemListBeans = serviceImage.getData().getResult().getItemList();
                Message message1 = new Message();
                message1.what = 002;
                message1.obj = itemListBeans;
                handler.sendMessage(message1);

            }

            @Override
            public void fail(String response) {
                ToastUtil.noNAR(getContext());
            }
        });

    }

    /* 初始化界面 */
    private void initView() {
        banner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                Log.i("banner", "path=" + (String) path);
                //根据图片id获取图片
                OKhttptils.getPic(getContext(), (String) path, imageView);
            }
        });
    }

    /* 设置圆点 */
    public void setOvalLayout() {
        for (int i = 0; i < pageCount; i++) {
            mLlDot.addView(inflater.inflate(R.layout.dot, null));
        }
        // 默认显示第一页
        mLlDot.getChildAt(0).findViewById(R.id.v_dot)
                .setBackgroundResource(R.drawable.dot_selected);
        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageSelected(int position) {
                // 取消圆点选中
                mLlDot.getChildAt(curIndex)
                        .findViewById(R.id.v_dot)
                        .setBackgroundResource(R.drawable.dot_normal);
                // 圆点选中
                mLlDot.getChildAt(position)
                        .findViewById(R.id.v_dot)
                        .setBackgroundResource(R.drawable.dot_selected);
                curIndex = position;
            }

            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }



    /* 创建popupwindow */
    private void createPopupWindow() {
        if (popupWindow == null) {
            View contentView = getLayoutInflater().inflate(R.layout.service_menu, null);
            popupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT, true);

            mDistance = (TextView) contentView.findViewById(R.id.mDistance);
            mDistance.setOnClickListener(this);
            mApprise = (TextView) contentView.findViewById(R.id.mApprise);
            mApprise.setOnClickListener(this);
            mHot = (TextView) contentView.findViewById(R.id.mHot);
            mHot.setOnClickListener(this);
            mSee = (TextView) contentView.findViewById(R.id.mSee);
            mSee.setOnClickListener(this);

            popupWindow.setBackgroundDrawable(new ColorDrawable(getContext().getResources().getColor(R.color.trans_a)));
            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    WindowManager.LayoutParams wlp = getActivity().getWindow().getAttributes();
                    wlp.alpha = 1.0f;
                    getActivity().getWindow().setAttributes(wlp);
                }
            });

        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        images.clear();
        unbinder.unbind();
    }

    /* 显示智能排序条件 */
    @OnClick(R.id.rank)
    public void onViewClicked() {

//        //自动向上滑动
        int height = DensityUtil.dp2px(rlMenu.getHeight() + banner.getHeight());
        CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams()).getBehavior();
        behavior.onNestedPreScroll(coordinatorLayout, appBarLayout, smartRefreshLayout, 0, height, new int[]{0, 0}, ViewCompat.TYPE_TOUCH);


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                    aaa.sendMessage(aaa.obtainMessage());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    Handler aaa = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            createPopupWindow();
            if (!popupWindow.isShowing()) {
                View rootview = LayoutInflater.from(getContext()).inflate(R.layout.fragment_car_service, null);
                //一半减去两个高度   DensityUtil.dp2px(-height/2)
//                popupWindow.showAtLocation(rootview, Gravity.RIGHT | Gravity.TOP, 0, v.findViewById(R.id.rl_titleBar).getHeight() + rank.getHeight() + DensityUtil.dp2px(getStatusBarHeight(getContext())));
                popupWindow.showAtLocation(rootview, Gravity.RIGHT | Gravity.TOP, 0, rlTitleBar.getHeight() + rank.getHeight() + DensityUtil.dp2px(getStatusBarHeight(getContext())));
                popupWindow.showAsDropDown(null, R.dimen.common264dp, 0);
                WindowManager.LayoutParams wlp = getActivity().getWindow().getAttributes();
                wlp.alpha = 0.7f;
                getActivity().getWindow().setAttributes(wlp);
            } else {
                popupWindow.dismiss();
            }
        }
    };

    /* 获取状态栏高度 */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen",
                "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /* 智能排序 */
    @Override
    public void onClick(View view) {
        /**
         * （距离 apart， 评价comp_eval_level，热度 comp_order_count， 浏览量comp_visit_count）默认传距离
         */
        switch (view.getId()) {
            case R.id.mDistance:
                //按距离排序
                sort = "ASC";
                order_by = "apart";
                getData(dict_id, order_by, sort, city_id, 1);
                popupWindow.dismiss();
                break;
            case R.id.mApprise:
                //按评分排序
                sort = "DESC";
                order_by = "comp_eval_level";
                getData(dict_id, order_by, sort, city_id, 1);
                popupWindow.dismiss();
                break;
            case R.id.mHot:
                //按热度排序
                sort = "DESC";
                order_by = "comp_order_count";
                getData(dict_id, order_by, sort, city_id, 1);
                popupWindow.dismiss();
                break;
            case R.id.mSee:
                //按浏览量排序
                sort = "DESC";
                order_by = "comp_visit_count";
                getData(dict_id, order_by, sort, city_id, 1);
                popupWindow.dismiss();
                break;
        }
    }

    /* 获取车服列表 */
    private void getData(String dict_id, String order_by, String sort, String city_id, int pageIndex) {
        Log.e(TAG, "upLoading经度: " + lon);
        Log.d(TAG, "upLoading纬度: " + lat);
        Log.i(TAG, "upLoading城市id: " + city_id);
        Log.d(TAG, "upLoading服务项id：" + dict_id);
        Log.d(TAG, "upLoading排序方式：" + sort);
        Log.e(TAG, "getData排序条件: " + order_by);
        Map<String, String> map = new HashMap<>();
        map.put("pageIndex", String.valueOf(pageIndex));
        map.put("pageSize", "");
        map.put("comp_name", "");
        map.put("lat", lat);
        map.put("lon", lon);
        map.put("prod_service_type_item", dict_id);
        map.put("order_by", order_by);
        map.put("sort", sort);
        map.put("auth_comp_city", city_id);
        OKhttptils.post(getActivity(), Config.GET_CAR_SERVICE, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                Gson gson = GsonFactory.create();
                CarServiceList serviceList = gson.fromJson(response, CarServiceList.class);
//                List<CarServiceList.DataBean.ResultBean> beanList = serviceList.getData().getResult();
                beanList = serviceList.getData().getResult();
                pageTotal = serviceList.getData().getPageCount();
                setAdapter();
            }

            @Override
            public void fail(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    ToastUtil.show(getContext(),object.getString("message"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    /* 上拉加载更多数据 */
    private void getMoreDate(String dict_id, String order_by, String sort, String city_id, final int pageIndex) {
        Map<String, String> map = new HashMap<>();
        map.put("pageIndex", String.valueOf(pageIndex));
        map.put("pageSize", "");
        map.put("comp_name", "");
        map.put("lat", lat);
        map.put("lon", lon);
        map.put("prod_service_type_item", dict_id);
        map.put("order_by", order_by);
        map.put("sort", sort);
        map.put("auth_comp_city", city_id);
        OKhttptils.post(getActivity(), Config.GET_CAR_SERVICE, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                Log.e(TAG, "test上拉加载更多: " + response);
                Gson gson = GsonFactory.create();
                CarServiceList list = gson.fromJson(response, CarServiceList.class);
                                /* 最大页数 */
                pageTotal = list.getData().getPageCount();
                if (pageIndex > pageTotal) {

                } else {
                    List<CarServiceList.DataBean.ResultBean> beanList = list.getData().getResult();
                    for (int i = 0; i < beanList.size(); i++) {
                        adapter.insert(beanList.get(i), adapter.getItemCount());
                        adapter.notifyDataSetChanged();
                    }
                }

            }

            @Override
            public void fail(String response) {
                ToastUtil.noNAR(getContext());

            }
        });

    }

    /* 获取省市id */
    private void getCityId(String s) {
        Map<String, String> map = new HashMap<>();
        map.put("cityName", s);
        Log.e(TAG, "getCityId城市: " + s );
        OKhttptils.post(getActivity(), Config.GET_CITY_ID, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                Log.w("test",response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String data = jsonObject.getString("data");
                    JSONObject object = new JSONObject(data);
                    String result = object.getString("result");
                    JSONObject o = new JSONObject(result);
                    city_id = o.getString("city_id");
//                    /**
//                     * 存储城市Id
//                     */
//                    Prefs.with(getActivity()).remove("cityId");
//                    Prefs.with(getActivity()).write("cityId",city_id);
                    Message message = new Message();
                    message.what = 004;
                    message.obj = city_id;
                    handler.sendMessage(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void fail(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    ToastUtil.show(getContext(),jsonObject.getString("message"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @OnClick({R.id.mLocation, R.id.mSearch})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mLocation:
                JumpUtil.newInstance().jumpRight(getContext(), CityActivity.class);
                CityActivity.setOnBackListener(new CityActivity.BackListener() {
                    @Override
                    public void backData(String city) {
                        mLocation.setText(city);
                        getCityId(city);
                    }
                });
                break;
            case R.id.mSearch:
                JumpUtil.newInstance().jumpRight(getContext(), SearchActivity.class,"comp");
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        images.clear();
    }
}
