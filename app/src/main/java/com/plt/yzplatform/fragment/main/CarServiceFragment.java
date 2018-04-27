package com.plt.yzplatform.fragment.main;

import android.Manifest;
import android.content.Context;
import android.graphics.Bitmap;
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
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.google.gson.Gson;
import com.plt.yzplatform.R;
import com.plt.yzplatform.activity.CityActivity;
import com.plt.yzplatform.activity.SearchActivity;
import com.plt.yzplatform.adapter.CarServiceListAdapter;
import com.plt.yzplatform.adapter.MainGridViewAdapter;
import com.plt.yzplatform.adapter.MainViewPagerAdapter;
import com.plt.yzplatform.config.Config;
import com.plt.yzplatform.entity.CarServiceImage;
import com.plt.yzplatform.entity.CarServiceList;
import com.plt.yzplatform.gson.factory.GsonFactory;
import com.plt.yzplatform.utils.JumpUtil;
import com.plt.yzplatform.utils.OKhttptils;
import com.plt.yzplatform.utils.PhotoUtils;
import com.plt.yzplatform.utils.ToastUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.scwang.smartrefresh.layout.util.DensityUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.functions.Consumer;

/**
 * Created by glp on 2018/4/18.
 * 描述：车服务
 */

public class CarServiceFragment extends Fragment implements View.OnClickListener, AMapLocationListener {
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
    /* 定位 */
    private static final int LOCATION_PERMISSION_CODE = 100;
    private static final int STORAGE_PERMISSION_CODE = 101;
    //定位需要的声明
    private AMapLocationClient mLocationClient = null;//定位发起端
    private AMapLocationClientOption mLocationOption = null;//定位参数
    //标识，用于判断是否只显示一次定位信息和用户重新定位
    private boolean isFirstLoc = true;

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

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 004:
                    city_id = (String) msg.obj;
                    /* 初始化数据源 banner图+菜单图 */
                    initData();
                    initView();
                    break;
                case 001:
                    bannerList = (List<CarServiceImage.DataBean.ResultBean.BannerListBean>) msg.obj;
                    initBanner();
                    break;
                case 002:
                    itemListBeans = (List<CarServiceImage.DataBean.ResultBean.ItemListBean>) msg.obj;
                    initMenu();
                    break;
                case 003:
                    beanList = (List<CarServiceList.DataBean.ResultBean>) msg.obj;
                    setAdapter();
                    break;
            }
        }
    };

    /* 填充数据 */
    private void setAdapter() {
        adapter = new CarServiceListAdapter(beanList, getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }


    /* 填充菜单 */
    private void initMenu() {
         /* 首页分类 */
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
//                    Toast.makeText(getContext(), itemListBeans.get(pos).getServerDesc(), Toast.LENGTH_SHORT).show();

                    //inner为条件筛选 其他为跳转界面
                    if ("inner".equals(itemListBeans.get(pos).getServerLinkType())) {
                        dict_id = itemListBeans.get(pos).getServerId();
                        getData(dict_id, order_by, sort, city_id, 1);
                    } else {
                        ToastUtil.show(getContext(), "跳一跳");

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
        banner.setImages(images);
        banner.start();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_car_service, null);
        }
        unbinder = ButterKnife.bind(this, view);
        /* 请求定位权限 */
        initLoc();
        /* 初始化数据源 */
//        initData();
//        initView();
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
                        Log.e(TAG, "onResponse上拉加载更多: " + response);
                        Gson gson = GsonFactory.create();
                        CarServiceList list = gson.fromJson(response, CarServiceList.class);
                                        /* 最大页数 */
                        Log.d(TAG, "onResponse总数: " + pageTotal);
                        Log.i(TAG, "onResponse加载的页数: " + pageIndex);
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
                Log.d(TAG, "onResponse: " + response);
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
                getPic(getContext(), (String) path, imageView);
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

    /* 通过头像id获取头像 */
    public void getPic(final Context context, String file_id, final ImageView icon) {
        Map<String, String> map = new HashMap<>();
        map.put("file_id", file_id);

        OKhttptils.post(getActivity(), Config.GET_BASE64, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String data = jsonObject.getString("data");
                    JSONObject object = new JSONObject(data);
                    String file_content = object.getString("file_content");
                    if (file_content.contains("base64,"))
                        file_content = file_content.split("base64,")[1];
                    Bitmap bitmap = PhotoUtils.base64ToBitmap(file_content);
                    if (bitmap != null)
                        icon.setImageBitmap(bitmap);
                    else
                        icon.setImageResource(R.drawable.qy_heat);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void fail(String response) {
                ToastUtil.noNAR(getContext());

            }
        });
    }

    /* 地图定位 */
    private void initLoc() {
        //初始化定位
        mLocationClient = new AMapLocationClient(getActivity());
        //设置定位回调监听
        mLocationClient.setLocationListener(this);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(true);
        ////设置定位间隔,单位毫秒,默认为2000ms
        //mLocationOption.setInterval(2000);
        //获取一次定位结果：
        //该方法默认为false。
        mLocationOption.setOnceLocation(true);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        // 申请定位权限
        rxPermission = new RxPermissions(getActivity());
        rxPermission.request(Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean granted) throws Exception {
                        if (granted) { // 在android 6.0之前会默认返回true
                            // 已经获取权限
                            //启动定位
                            mLocationClient.startLocation();
                        } else {
                            // 未获取权限
                            Toast.makeText(getActivity(), "您没有授权该权限，请在设置中打开授权", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /* 定位回调 */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        //str.substring(0,str.length()-1) 去掉字符串最后一个字符
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见官方定位类型表
                amapLocation.getLatitude();//获取纬度
                amapLocation.getLongitude();//获取经度
                amapLocation.getAccuracy();//获取精度信息
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(amapLocation.getTime());
                df.format(date);//定位时间
                amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                amapLocation.getCountry();//国家信息
                amapLocation.getProvince();//省信息
                amapLocation.getCity();//城市信息
                amapLocation.getDistrict();//城区信息
                amapLocation.getStreet();//街道信息
                amapLocation.getStreetNum();//街道门牌号信息
                amapLocation.getCityCode();//城市编码
                amapLocation.getAdCode();//地区编码
                // 如果不设置标志位，此时再拖动地图时，它会不断将地图移动到当前的位置
                if (isFirstLoc) {
                    //获取定位信息
                    StringBuffer buffer = new StringBuffer();
                    buffer.append(amapLocation.getCountry() + "" + amapLocation.getProvince() + "" + amapLocation.getCity() + "" + amapLocation.getProvince() + "" + amapLocation.getDistrict() + "" + amapLocation.getStreet() + "" + amapLocation.getStreetNum());
                    isFirstLoc = false;
//                    city = amapLocation.getCity().substring(0, amapLocation.getCity().length() - 1);
//                    lon = String.valueOf(amapLocation.getLongitude());
//                    lat = String.valueOf(amapLocation.getLatitude());
                    city = "济南";
                    mLocation.setText(city);
                    lon = "116.988";
                    lat = "36.6883";
                    //
                    getCityId(city);
                }
                Log.i("Location", "log=" + lon);
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + amapLocation.getErrorCode() + ", errInfo:"
                        + amapLocation.getErrorInfo());
                Toast.makeText(getContext(), "定位失败", Toast.LENGTH_LONG).show();
                /* 定位失败 获取默认城市 - 北京 */
                city = "北京";
                mLocation.setText(city);
                lon = "116.38";
                lat = "39.9";
                getCityId(city);
            }
        }
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
                View v = LayoutInflater.from(getContext()).inflate(R.layout.activity_main, null);
                //一半减去两个高度   DensityUtil.dp2px(-height/2)
                popupWindow.showAtLocation(rootview, Gravity.RIGHT | Gravity.TOP, 0, v.findViewById(R.id.rl_titleBar).getHeight() + rank.getHeight() + DensityUtil.dp2px(getStatusBarHeight(getContext())));
                View view = LinearLayout.inflate(getContext(), R.layout.activity_main, null);
                popupWindow.showAsDropDown(view.findViewById(R.id.rl_titleBar), R.dimen.common264dp, 0);
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
                Log.d(TAG, "onResponse获取数据: " + response);
                Gson gson = GsonFactory.create();
                CarServiceList serviceList = gson.fromJson(response, CarServiceList.class);
                List<CarServiceList.DataBean.ResultBean> beanList = serviceList.getData().getResult();
                pageTotal = serviceList.getData().getPageCount();
                Message message = new Message();
                message.what = 003;
                message.obj = beanList;
                handler.sendMessage(message);
            }

            @Override
            public void fail(String response) {
                ToastUtil.noNAR(getContext());
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
                Log.e(TAG, "onResponse上拉加载更多: " + response);
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
        OKhttptils.post(getActivity(), Config.GET_CITY_ID, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String data = jsonObject.getString("data");
                    JSONObject object = new JSONObject(data);
                    String result = object.getString("result");
                    JSONObject o = new JSONObject(result);
                    city_id = o.getString("city_id");
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
                ToastUtil.noNAR(getContext());

            }
        });
    }

    @OnClick({R.id.mLocation, R.id.mSearch})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mLocation:
                Bundle bundle = new Bundle();
                bundle.putString("type", "车服务");
                JumpUtil.newInstance().jumpRight(getContext(), CityActivity.class, 01, bundle);
                break;
            case R.id.mSearch:
                JumpUtil.newInstance().jumpRight(getContext(), SearchActivity.class);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        images.clear();
        Bundle bundle = getArguments();
        String city = bundle.getString("selected_city");
        if (null != city) {
            mLocation.setText(city);
            getCityId(city);
//            getData(dict_id,"apart","ASC",city_id,1);
        }
        Log.i(TAG, "onResume: " + bundle.getString("selected_city"));
    }

}
