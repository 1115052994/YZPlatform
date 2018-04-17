package com.plt.yzplatform.activity;

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

import com.google.gson.Gson;
import com.plt.yzplatform.R;
import com.plt.yzplatform.adapter.CommonRecyclerAdapter;
import com.plt.yzplatform.adapter.GrideViewAdapter;
import com.plt.yzplatform.adapter.MultiTypeSupport;
import com.plt.yzplatform.adapter.ViewHolder;
import com.plt.yzplatform.config.Config;
import com.plt.yzplatform.entity.HotCity;
import com.plt.yzplatform.entity.LetterCitysBean;
import com.plt.yzplatform.entity.QueryCityBean;
import com.plt.yzplatform.utils.CommonUtils;
import com.plt.yzplatform.utils.NetUtil;
import com.plt.yzplatform.utils.Prefs;
import com.plt.yzplatform.utils.ToastUtil;
import com.plt.yzplatform.view.ExpandableGridView;
import com.plt.yzplatform.view.LetterSideBar;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class CityActivity extends AppCompatActivity {

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
    private List<String> city = new ArrayList<>();
    private List<String> list = new ArrayList<>();
    private List<String> hisCity = new ArrayList<>();
    private List<String> searchCity = new ArrayList<>();
    private GrideViewAdapter gvAdapter;
    private CommonRecyclerAdapter rvAdapter;
    private GrideViewAdapter hisGvAapter;
    private GridLayoutManager layoutManager;

    //选中的城市
    private String selectedCity = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        ButterKnife.bind(this);
        Window win = getWindow();
        WindowManager.LayoutParams params = win.getAttributes();
        win.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        initView();
    }

    private void initView() {
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
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.i("edittextview", "onTextChanged=" + editSearch.getText().toString());
                getSearchCity(editSearch.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
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
                seleted(city.get(position));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
        getData();
    }

    private void initData() {
        /* girdview绑定adapter */
        //gvAdapter = new ArrayAdapter<String>(CityActivity.this, R.layout.item_city, list);
        gvAdapter = new GrideViewAdapter(this, city);
        hisGvAapter = new GrideViewAdapter(this, hisCity);
        hotPlace.setAdapter(gvAdapter);
        historyPlace.setAdapter(hisGvAapter);

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
                seleted(list.get(position));
            }
        });
        layoutManager = new GridLayoutManager(this, 3);
        // 设置布局管理器
        recyclerViewCity.setLayoutManager(layoutManager);
        recyclerViewCity.setAdapter(rvAdapter);
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
                        letterSideBar.setFirstVisibleLetter(list.get(i).toString());
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
        Set<String> value = new HashSet<>();
        value.add("北京");
        value.add("济南");
        Prefs.with(getApplicationContext()).putStringSet("historyCitys", value);
        Set<String> set = Prefs.with(getApplicationContext()).getStringSet("historyCitys", null);
        for (String s : set) {
            hisCity.add(s);
        }
        hisGvAapter.notifyDataSetChanged();
    }

    public void getCitys() {
        if (NetUtil.isNetAvailable(CityActivity.this)) {
            OkHttpUtils.post()
                    .url(Config.QUERYHEADCITY)
                    .addHeader("user_token", Prefs.with(getApplicationContext()).read("user_token"))
                    .addParams("pageSize", "1000")
                    .addParams("pageIndex", "1")
                    .build()
                    .execute(new StringCallback() {

                        @Override
                        public void onError(Call call, Exception e, int id) {

                        }

                        @Override
                        public void onResponse(String response, int id) {
                            Log.d("onResponse", "onResponse" + response);
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
                    });
        } else {
            ToastUtil.noNetAvailable(CityActivity.this);
        }
    }

    public void getHotCitys() {
        if (NetUtil.isNetAvailable(CityActivity.this)) {
            OkHttpUtils.post()
                    .url(Config.QUERYHOTCITY)
                    .addHeader("user_token", Prefs.with(getApplicationContext()).read("user_token"))
                    .addParams("pageSize", "")
                    .addParams("pageIndex", "")
                    .build()
                    .execute(new StringCallback() {

                        @Override
                        public void onError(Call call, Exception e, int id) {

                        }

                        @Override
                        public void onResponse(String response, int id) {
                            Log.d("onResponse", "onResponse" + response);
                            try {
                                JSONObject object = new JSONObject(response);
                                String data = object.getString("data");
                                if (data != null) {
                                    Gson gson = new Gson();
                                    city.clear();
                                    HotCity hotCity = gson.fromJson(response, HotCity.class);
                                    List<String> list = hotCity.getData().getResult();
//                                    List<String> list1 = new ArrayList<>();
//                                    for (int i = list.size()-1; i >=0 ; i--) {
//                                        list1.add(list.get(i));
//                                    }
                                    for (int i = 0; i < list.size(); i++) {
                                        if (i < 8) {
                                            city.add(list.get(i));
                                        }
                                    }
                                    gvAdapter.notifyDataSetChanged();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } else {
            ToastUtil.noNetAvailable(CityActivity.this);
        }
    }

    private void getSearchCity(String s) {
        if (NetUtil.isNetAvailable(CityActivity.this)) {
            OkHttpUtils.post()
                    .url(Config.QUERYCITY)
                    .addHeader("user_token", Prefs.with(getApplicationContext()).read("user_token"))
                    .addParams("value", s.trim())
                    .addParams("pageSize", "8")
                    .addParams("pageIndex", "1")
                    .build()
                    .execute(new StringCallback() {

                        @Override
                        public void onError(Call call, Exception e, int id) {

                        }

                        @Override
                        public void onResponse(String response, int id) {
                            Log.d("onResponse", "onResponse" + response);
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
                                    PopupWindow();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } else {
            ToastUtil.noNetAvailable(CityActivity.this);
        }
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
        recyclerView.setAdapter(adapter);
        // 创建PopupWindow对象，其中：
        // 第一个参数是用于PopupWindow中的View，第二个参数是PopupWindow的宽度，
        // 第三个参数是PopupWindow的高度，第四个参数指定PopupWindow能否获得焦点
        PopupWindow window = new PopupWindow(contentView, RelativeLayout.LayoutParams.MATCH_PARENT, /*CommonUtils.dip2px(CityActivity.this,150)*/RelativeLayout.LayoutParams.WRAP_CONTENT, true);
        // 设置PopupWindow的背景
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // 设置PopupWindow是否能响应外部点击事件
        window.setOutsideTouchable(true);
        // 设置PopupWindow是否能响应点击事件
        window.setTouchable(true);
        // 显示PopupWindow，其中：
        // 第一个参数是PopupWindow的锚点，第二和第三个参数分别是PopupWindow相对锚点的x、y偏移
        window.showAsDropDown(search, 20, 20);
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
        Log.i("selected", city);
    }

    @OnClick({R.id.loc_place,R.id.clear_history,R.id.cancle})
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
                break;
        }
    }

}
