package com.xtzhangbinbin.jpq.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.google.gson.Gson;
import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.adapter.CommonRecyclerAdapter;
import com.xtzhangbinbin.jpq.adapter.GrideViewAdapter;
import com.xtzhangbinbin.jpq.adapter.MultiTypeSupport;
import com.xtzhangbinbin.jpq.adapter.ViewHolder;
import com.xtzhangbinbin.jpq.base.BaseActivity;
import com.xtzhangbinbin.jpq.config.Config;
import com.xtzhangbinbin.jpq.entity.HotCity;
import com.xtzhangbinbin.jpq.entity.LetterCitysBean;
import com.xtzhangbinbin.jpq.entity.QueryCityBean;
import com.xtzhangbinbin.jpq.utils.CommonUtils;
import com.xtzhangbinbin.jpq.utils.JumpUtil;
import com.xtzhangbinbin.jpq.utils.OKhttptils;
import com.xtzhangbinbin.jpq.utils.Prefs;
import com.xtzhangbinbin.jpq.utils.ToastUtil;
import com.xtzhangbinbin.jpq.view.ExpandableGridView;
import com.xtzhangbinbin.jpq.view.LetterSideBar;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

public class CityActivity extends BaseActivity implements AMapLocationListener {

    @BindView(R.id.historyPlace)
    ExpandableGridView historyPlace;
    @BindView(R.id.hotPlace)
    ExpandableGridView hotPlace;
    @BindView(R.id.letter)
    RelativeLayout letter;
    @BindView(R.id.recyclerView_city)
    RecyclerView recyclerViewCity;
    @BindView(R.id.letter_side_bar)
    LetterSideBar letterSideBar;
    @BindView(R.id.edit_search)
    EditText editSearch;
    @BindView(R.id.search)
    RelativeLayout search;
    @BindView(R.id.loc_place)
    TextView locPlace;
    @BindView(R.id.clear_history)
    TextView clearHistory;

    private List<String> list = new ArrayList<>();

    private String type;

    private List<String> hotcity = new ArrayList<>();
    private List<String> hisCity = new ArrayList<>();
    private List<String> searchCity = new ArrayList<>();
    private GrideViewAdapter hotAdapter;
    private CommonRecyclerAdapter rvAdapter;
    private GrideViewAdapter hisGvAapter;
    private GridLayoutManager layoutManager;

    //选中的城市
    private String selectedCity = "";

    //定位需要的声明
    private AMapLocationClient mLocationClient = null;//定位发起端
    private AMapLocationClientOption mLocationOption = null;//定位参数
    //标识，用于判断是否只显示一次定位信息和用户重新定位
    private boolean isFirstLoc = true;
    private String TAG = "CityActivity";

    private RxPermissions rxPermission;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        ButterKnife.bind(this);
        Window win = getWindow();
        WindowManager.LayoutParams params = win.getAttributes();
        win.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            type = bundle.getString("type");
            //车服务、二手车等type
        }
        initView();
        initData();
        getData();
        initLoc();
    }

    private void initView() {
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                getSearchCity(editSearch.getText().toString());
                Log.i("edittextview", "afterTextChanged");
            }
        });

        historyPlace.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                seleted(hisCity.get(position));
            }
        });
        hotPlace.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                seleted(hotcity.get(position));
            }
        });
    }

    private void initData() {
        /* girdview绑定adapter */
        //gvAdapter = new ArrayAdapter<String>(CityActivity.this, R.layout.item_city, list);
        hotAdapter = new GrideViewAdapter(this, hotcity);
        hisGvAapter = new GrideViewAdapter(this, hisCity);
        hotPlace.setAdapter(hotAdapter);
        historyPlace.setAdapter(hisGvAapter);

        // 滚动字母条
        letter.getBackground().setAlpha(60);
        letterSideBar.setOnLetterTouchListener(new LetterSideBar.LetterTouchListener() {
            @Override
            public void touch(CharSequence letter, boolean isTouch) {
                String s = letter.toString();
                int index = list.indexOf(s);
                //recyclerViewCity.scrollToPosition(index);
                moveToPosition(index);
            }
        });
        /* recycler绑定adapter*/
        rvAdapter = new CommonRecyclerAdapter(this, list,
                new MultiTypeSupport<String>() {

                    @Override
                    public int getLayoutId(String item, int position) {
                        if (item.matches("[A-Z]")) {
                            return R.layout.item_letter;
                        } else {
                            return R.layout.item_citys;
                        }
                    }
                }) {
            @Override
            public void convert(ViewHolder holder, Object item, int position) {
                if (((String) item).matches("[A-Z]")) {
                    TextView tv = holder.getView(R.id.letter);
                    tv.setText((String) item);
                } else {
                    TextView tv = holder.getView(R.id.city);
                    tv.setText((String) item);
                }
            }
        };
        //添加监听事件
        rvAdapter.setOnItemClickListener(new CommonRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                if (!list.get(position).matches("[A-Z]"))
                    seleted(list.get(position));
            }
        });
        layoutManager = new GridLayoutManager(this, 3);
        // 设置布局管理器
        recyclerViewCity.setLayoutManager(layoutManager);
        recyclerViewCity.setAdapter(rvAdapter);
        //recyclerViewCity.setNestedScrollingEnabled(false);
        recyclerViewCity.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                GridLayoutManager mLinearLayoutManager = (GridLayoutManager) recyclerViewCity.getLayoutManager();
                if (move) {
                    move = false;
                    //获取要置顶的项在当前屏幕的位置，mIndex是记录的要置顶项在RecyclerView中的位置
                    int n = mIndex - mLinearLayoutManager.findFirstVisibleItemPosition();
                    if (0 <= n && n < recyclerViewCity.getChildCount()) {
                        //获取要置顶的项顶部离RecyclerView顶部的距离
                        int top = recyclerViewCity.getChildAt(n).getTop();
                        //最后的移动
                        recyclerViewCity.scrollBy(0, top);
//                        //滑动字母条
//                        letterSideBar.setFirstVisibleLetter(list.get(mIndex));
                    }
                }
                //滚动字母条
                int first = mLinearLayoutManager.findFirstVisibleItemPosition();
                int last = mLinearLayoutManager.findLastVisibleItemPosition();
                for (int i = first; i < last; i++) {
                    if (list.get(i).toString().matches("[A-Z]")) {
                        letterSideBar.setVisibleLetter(list.get(i).toString());
                        break;
                    }
                }
            }
        });

    }

    public void getData() {
        getCitys();
        getHotCitys();
        getHisCitys();
    }

    public void getHisCitys() {
//        Set<String> value = new HashSet<>();
//        value.add("北京");
//        value.add("济南");
//        Prefs.with(getApplicationContext()).putStringSet("historyCitys", value);
        Set<String> set = Prefs.with(getApplicationContext()).getStringSet("historyCitys", null);
        if (set!=null) {
            for (String s : set) {
                if (hisCity.size() < 8)
                    hisCity.add(s);
            }
            hisGvAapter.notifyDataSetChanged();
        }
    }

    public void getCitys() {
        Map<String, String> map = new HashMap<>();
        map.put("pageSize", "100");
        map.put("pageIndex", "1");
        OKhttptils.post(this, Config.QUERYHEADCITY, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    String data = object.getString("data");
                    if (data != null) {
                        list.clear();
                        Gson gson = new Gson();
                        LetterCitysBean letterCitysBean = gson.fromJson(response, LetterCitysBean.class);
                        LetterCitysBean.DataBean.ResultBean resultBean = letterCitysBean.getData().getResult();
                        List<String> A = resultBean.getA();
                        list.add("A");
                        list.addAll(A);
                        List<String> B = resultBean.getB();
                        list.add("B");
                        list.addAll(B);
                        List<String> C = resultBean.getC();
                        list.add("C");
                        list.addAll(C);
                        List<String> D = resultBean.getD();
                        list.add("D");
                        list.addAll(D);
                        List<String> E = resultBean.getE();
                        list.add("E");
                        list.addAll(E);
                        List<String> F = resultBean.getF();
                        list.add("F");
                        list.addAll(F);
                        List<String> G = resultBean.getG();
                        list.add("G");
                        list.addAll(G);
                        List<String> H = resultBean.getH();
                        list.add("H");
                        list.addAll(H);
                        //List<String> I = resultBean.getI();
                        //list.add("I");
                        List<String> J = resultBean.getJ();
                        list.add("J");
                        list.addAll(J);
                        List<String> K = resultBean.getK();
                        list.add("K");
                        list.addAll(K);
                        List<String> L = resultBean.getL();
                        list.add("L");
                        list.addAll(L);
                        List<String> M = resultBean.getM();
                        list.add("M");
                        list.addAll(M);
                        List<String> N = resultBean.getN();
                        list.add("N");
                        list.addAll(N);
                        //list.add("O");
                        List<String> P = resultBean.getP();
                        list.add("P");
                        list.addAll(P);
                        List<String> Q = resultBean.getQ();
                        list.add("Q");
                        list.addAll(Q);
                        List<String> R = resultBean.getR();
                        list.add("R");
                        list.addAll(R);
                        List<String> S = resultBean.getS();
                        list.add("S");
                        list.addAll(S);
                        //List<String> T = resultBean.getT();
                        //list.add("T");
                        //list.addAll(T);
                        //List<String> U = resultBean.getU();
                        //list.add("U");
                        //list.addAll(U);
                        //List<String> V = resultBean.getV();
                        //list.add("V");
                        //list.addAll(V);
                        List<String> W = resultBean.getW();
                        list.add("W");
                        list.addAll(W);
                        List<String> X = resultBean.getX();
                        list.add("X");
                        list.addAll(X);
                        List<String> Y = resultBean.getY();
                        list.add("Y");
                        list.addAll(Y);
                        List<String> Z = resultBean.getZ();
                        list.add("Z");
                        list.addAll(Z);
                        rvAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void fail(String response) {

            }
        });
    }

    public void getHotCitys() {
        Map<String, String> map = new HashMap<>();
        map.put("pageSize", "100");
        map.put("pageIndex", "1");
        OKhttptils.post(this, Config.QUERYHOTCITY, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    String data = object.getString("data");
                    if (data != null) {
                        Gson gson = new Gson();
                        hotcity.clear();
                        HotCity hotCity = gson.fromJson(response, HotCity.class);
                        List<String> list = hotCity.getData().getResult();
//                                    List<String> list1 = new ArrayList<>();
//                                    for (int i = list.size()-1; i >=0 ; i--) {
//                                        list1.add(list.get(i));
//                                    }
                        for (int i = 0; i < list.size(); i++) {
                            if (i < 8) {
                                hotcity.add(list.get(i));
                            }
                        }
                        hotAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void fail(String response) {

            }
        });
    }

    private void getSearchCity(String s) {
        Map<String, String> map = new HashMap<>();
        map.put("pageSize", "100");
        map.put("pageIndex", "1");
        map.put("value", s.trim());
        OKhttptils.post(this, Config.QUERYCITY, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    String data = object.getString("data");
                    if (data != null) {
                        Gson gson = new Gson();
                        searchCity.clear();
                        QueryCityBean bean = gson.fromJson(response, QueryCityBean.class);
                        List<String> list = bean.getData().getResult();
                        for (String s : list) {
                            if (searchCity.size() < 8)
                                searchCity.add(s);
                        }
                        if (searchCity.size() != 0)
                            PopupWindow();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void fail(String response) {

            }
        });
    }

    //弹出查询window
    private void PopupWindow() {
        // 用于PopupWindow的View
        View contentView = LayoutInflater.from(this).inflate(R.layout.popupwindow, null, false);
        RecyclerView recyclerView = contentView.findViewById(R.id.recyclerView_popup);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        CommonRecyclerAdapter adapter = new CommonRecyclerAdapter(this, searchCity, R.layout.item_popup) {
            @Override
            public void convert(ViewHolder holder, Object item, int position) {
                TextView tv = holder.getView(R.id.city);
                tv.setText((String) item);
            }
        };
        // 创建PopupWindow对象，其中：
        // 第一个参数是用于PopupWindow中的View，第二个参数是PopupWindow的宽度，
        // 第三个参数是PopupWindow的高度，第四个参数指定PopupWindow能否获得焦点
        final PopupWindow window = new PopupWindow(contentView, RelativeLayout.LayoutParams.MATCH_PARENT, /*CommonUtils.dip2px(CityActivity.this,150)*/RelativeLayout.LayoutParams.WRAP_CONTENT, true);
        // 设置PopupWindow的背景
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // 设置PopupWindow是否能响应外部点击事件
        window.setOutsideTouchable(true);
        // 设置PopupWindow是否能响应点击事件
        window.setTouchable(true);
        // 显示PopupWindow，其中：
        // 第一个参数是PopupWindow的锚点，第二和第三个参数分别是PopupWindow相对锚点的x、y偏移
        window.showAsDropDown(search, 0, 0);
        // 或者也可以调用此方法显示PopupWindow，其中：
        // 第一个参数是PopupWindow的父View，第二个参数是PopupWindow相对父View的位置，
        // 第三和第四个参数分别是PopupWindow相对父View的x、y偏移
        // window.showAtLocation(parent, gravity, x, y);
        // 设置背景颜色变暗
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = CityActivity.this.getWindow().getAttributes();
                lp.alpha = 1f;
                CityActivity.this.getWindow().setAttributes(lp);
            }
        });
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        lp.verticalMargin = CommonUtils.dip2px(CityActivity.this, 30);
        lp.alpha = 0.9f;
        this.getWindow().setAttributes(lp);
        //设置点击监听
        adapter.setOnItemClickListener(new CommonRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                seleted(searchCity.get(position));
                window.dismiss();
            }
        });
        recyclerView.setAdapter(adapter);
    }


    @OnClick({R.id.loc_place, R.id.clear_history, R.id.cancle})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.loc_place:
                seleted(locPlace.getText().toString().trim());
                break;
            case R.id.clear_history:
                Prefs.with(getApplicationContext()).remove("historyCitys");
                hisCity.clear();
                hisGvAapter.notifyDataSetChanged();
                break;
            case R.id.cancle:
                JumpUtil.newInstance().finishRightTrans(this);
                break;
        }
    }


    /* 地图定位 */
    private void initLoc() {
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
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
        rxPermission = new RxPermissions(this);
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
                            Toast.makeText(CityActivity.this, "您没有授权定位权限，请在设置中打开授权", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /* 定位回调函数 */
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
                locPlace.setText(amapLocation.getCity().split("市")[0]);
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
//                    Toast.makeText(getApplicationContext(), buffer.toString(), Toast.LENGTH_LONG).show();
//                    ToastUtil.show(CityActivity.this, "已定位到当前城市");
                    isFirstLoc = false;
                }
                Prefs.with(this).remove("lat");
                Prefs.with(this).remove("lon");
                Prefs.with(this).write("lat",amapLocation.getLatitude()+"");
                Prefs.with(this).write("lon",amapLocation.getLongitude()+"");
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + amapLocation.getErrorCode() + ", errInfo:"
                        + amapLocation.getErrorInfo());
                Toast.makeText(getApplicationContext(), "定位失败", Toast.LENGTH_LONG).show();
                /* 定位失败 获取默认城市 */
                locPlace.setText("北京");
            }
        }
    }

    // recyclerview滑动到指定位置
    private boolean move = false;
    private int mIndex = 0;
    private void moveToPosition(int n) {
        mIndex = n;
        //先从RecyclerView的LayoutManager中获取第一项和最后一项的Position
        GridLayoutManager mLinearLayoutManager = (GridLayoutManager) recyclerViewCity.getLayoutManager();
        int firstItem = mLinearLayoutManager.findFirstVisibleItemPosition();
        int lastItem = mLinearLayoutManager.findLastVisibleItemPosition();
        //然后区分情况
        if (n <= firstItem) {
            //当要置顶的项在当前显示的第一个项的前面时
            recyclerViewCity.scrollToPosition(n);
        } else if (n <= lastItem) {
            //当要置顶的项已经在屏幕上显示时
            int top = recyclerViewCity.getChildAt(n - firstItem).getTop();
            recyclerViewCity.scrollBy(0, top);
        } else {
            //当要置顶的项在当前显示的最后一项的后面时
            recyclerViewCity.scrollToPosition(n);
            //这里这个变量是用在RecyclerView滚动监听里面的
            move = true;
        }
    }

    private void seleted(String city) {
        selectedCity = city;
        Bundle bundle = new Bundle();
        bundle.putString("selected_city", selectedCity);
        if ("车服务".equals(type)) {
            JumpUtil.newInstance().finishRightTrans(CityActivity.this, bundle, 01);
        }else{
            if (backListener!=null) {
                backListener.backData(selectedCity);
                Prefs.with(this).remove("city");
                Prefs.with(this).write("city", city);
                Prefs.with(this).remove("cityId");
            }
            JumpUtil.newInstance().finishRightTrans(CityActivity.this);
        }
        Set<String> value = new HashSet<>();
        value.add(city);
        Prefs.with(getApplicationContext()).putStringSet("historyCitys", value);
    }

    static BackListener backListener;
    public static void setOnBackListener( BackListener listener){
        backListener = listener;
    }
    public interface BackListener{
        void backData(String city);
    }
}
