package com.xtzhangbinbin.jpq.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.adapter.CommonRecyclerAdapter;
import com.xtzhangbinbin.jpq.adapter.ViewHolder;
import com.xtzhangbinbin.jpq.config.Config;
import com.xtzhangbinbin.jpq.entity.HotCar;
import com.xtzhangbinbin.jpq.entity.SearchCarBean;
import com.xtzhangbinbin.jpq.entity.SearchCarBeanNew;
import com.xtzhangbinbin.jpq.entity.SearchCompBean;
import com.xtzhangbinbin.jpq.utils.CommonUtils;
import com.xtzhangbinbin.jpq.utils.JumpUtil;
import com.xtzhangbinbin.jpq.utils.OKhttptils;
import com.xtzhangbinbin.jpq.utils.Prefs;
import com.xtzhangbinbin.jpq.utils.ToastUtil;
import com.xtzhangbinbin.jpq.view.CarTagLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchActivity extends AppCompatActivity {

    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.trangle)
    ImageView trangle;
    @BindView(R.id.edit_search)
    EditText editSearch;
    @BindView(R.id.cancle)
    TextView cancle;
    //    @BindView(R.id.historyPlace)
//    ExpandableGridView history;
//    @BindView(R.id.hotPlace)
//    ExpandableGridView hot;
    @BindView(R.id.search)
    RelativeLayout search;
    @BindView(R.id.cartag_history)
    CarTagLayout cartagHistory;
    @BindView(R.id.cartag_hot)
    CarTagLayout cartagHot;

    //    private GrideViewAdapter hotAdapter;
//    private GrideViewAdapter hisGvAapter;
    private List<String> hotList = new ArrayList<>();
    private List<String> hisList = new ArrayList<>();
    // 搜索结果
    private List<String> searchList = new ArrayList<>();
    private List<String> searchListId = new ArrayList<>();
    // 新添加
    private List<String> searchBrandList = new ArrayList<>();
    private List<String> searchBrandListId = new ArrayList<>();
    //类别（汽车、商户）car/comp
    private String type = "comp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        Window win = getWindow();
        WindowManager.LayoutParams params = win.getAttributes();
        win.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                type = bundle.getString("string", "comp");
                if ("car".equals(type))
                    name.setText("汽车");
                else
                    name.setText("商家");
                Log.i("type", "type=" + type);
            }
        }
        initView();
        initData();
        getData();
    }

    private void initView() {
        editSearch.setText("");
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.i("edittextview", "onTextChanged=" + editSearch.getText().toString());
                if ("" != s.toString()) {
                    if ("car".equals(type)) {
                        getSeachCar(editSearch.getText().toString().trim());
                    } else {
                        getSearchComp(editSearch.getText().toString().trim());
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

//        history.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                preSearch(hisList.get(position));
//            }
//        });
//        hot.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                preSearch(hotList.get(position));
//            }
//        });
        cartagHistory.setOnItemClickListener(new CarTagLayout.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                preSearch(hisList.get(position));
            }
        });
        cartagHot.setOnItemClickListener(new CarTagLayout.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                preSearch(hotList.get(position));
            }
        });
    }

    private void initData() {
        /* girdview绑定adapter */
//        hotAdapter = new GrideViewAdapter(this, hotList);
//        hisGvAapter = new GrideViewAdapter(this, hisList);
//        hot.setAdapter(hotAdapter);
//        history.setAdapter(hisGvAapter);
    }

    public void getData() {
        getHis();
        getHot();
    }

    public void getHot() {
        Map<String, String> map = new HashMap<>();
        //请求参数有几个put几个
        map.put("hotword_type", type);
        OKhttptils.post(SearchActivity.this, Config.GETSEARCHHOTWORD, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    String data = object.getString("data");
                    if (data != null) {
                        Gson gson = new Gson();
                        hotList.clear();
                        //hotListId.clear();
                        HotCar hotCar = gson.fromJson(response, HotCar.class);
                        List<HotCar.DataBean.ResultBean> list = hotCar.getData().getResult();
                        for (int i = 0; i < list.size(); i++) {
                            if (i < 8) {
                                hotList.add(list.get(i).getHotword_name());
                                //hotListId.add(list.get(i).getHotword_id());
                            }
                        }
                        //hotAdapter.notifyDataSetChanged();
                        cartagHot.removeAllViews();
                        for (int i=0;i<hotList.size();i++){
                            View ly = LayoutInflater.from(SearchActivity.this).inflate(R.layout.item_searchtag, null);
                            TextView tv = ly.findViewById(R.id.tv_tag);
                            if (tv.getParent() != null) {
                                ViewGroup group = (ViewGroup) tv.getParent();
                                group.removeAllViews();
                            }
                            tv.setText(hotList.get(i));
                            cartagHot.addView(tv);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void fail(String response) {
                ToastUtil.noNAR(SearchActivity.this);
            }
        });
    }

    public void getHis() {
        hisList.clear();
        if ("car".equals(type)) {
            Set<String> set = Prefs.with(getApplicationContext()).getStringSet("historyCars", null);
            if (set != null) {
                for (String s : set) {
                    if (hisList.size() < 8)
                        hisList.add(s);
                }
            }
            cartagHistory.removeAllViews();
            for (int i=0;i<hisList.size();i++){
                View ly = LayoutInflater.from(this).inflate(R.layout.item_searchtag, null);
                TextView tv = ly.findViewById(R.id.tv_tag);
                if (tv.getParent() != null) {
                    ViewGroup group = (ViewGroup) tv.getParent();
                    group.removeAllViews();
                }
                tv.setText(hisList.get(i));
                cartagHistory.addView(tv);
            }
            //hisGvAapter.notifyDataSetChanged();
        } else if ("comp".equals(type)) {
            Set<String> set = Prefs.with(getApplicationContext()).getStringSet("historyComp", null);
            if (set != null) {
                for (String s : set) {
                    if (hisList.size() < 8)
                        hisList.add(s);
                }
            }
            //hisGvAapter.notifyDataSetChanged();
            cartagHistory.removeAllViews();
            for (int i=0;i<hisList.size();i++){
                View ly = LayoutInflater.from(this).inflate(R.layout.item_searchtag, null);
                TextView tv = ly.findViewById(R.id.tv_tag);
                if (tv.getParent() != null) {
                    ViewGroup group = (ViewGroup) tv.getParent();
                    group.removeAllViews();
                }
                tv.setText(hisList.get(i));
                cartagHistory.addView(tv);
            }
        }
    }

    private void getSearchComp(String s) {
        Map<String, String> map = new HashMap<>();
        map.put("comp_name", s);
        OKhttptils.post(this, Config.GETSEARCHCOMP, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                Log.i("getSearchComp", response);
                try {
                    JSONObject object = new JSONObject(response);
                    String data = object.getString("data");
                    if (data != null) {
                        Gson gson = new Gson();
                        SearchCompBean searchCompBean = gson.fromJson(response, SearchCompBean.class);
                        List<SearchCompBean.DataBean.ResultBean> beans = searchCompBean.getData().getResult();
                        searchList.clear();
                        searchListId.clear();
                        for (SearchCompBean.DataBean.ResultBean bean : beans) {
                            searchList.add(bean.getAuth_comp_name());
                            searchListId.add(bean.getComp_id());
                        }
                        if (searchList.size() > 0) {
                            PopupWindow();
                        } else {
                            ToastUtil.show(SearchActivity.this, "未查询到相关信息");
                        }
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

    private void getSeachCar(String s) {
        Map<String, String> map = new HashMap<>();
        map.put("car_name", s);
        OKhttptils.post(this, Config.GETSEARCHCAR, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    String data = object.getString("data");
                    if (data != null) {
                        Gson gson = new Gson();
//                        SearchCarBean searchCarBean = gson.fromJson(response, SearchCarBean.class);
//                        List<SearchCarBean.DataBean.ResultBean> resultBeans = searchCarBean.getData().getResult();
//                        searchListId.clear();
//                        searchList.clear();
//                        for (SearchCarBean.DataBean.ResultBean bean : resultBeans) {
//                            searchList.add(bean.getCar_name());
//                            searchListId.add(bean.getCar_id());
//                        }
                        /*
                        "item_id": "YZescxgescppcxdlxlcnkcxmy_1_50_700",
                        "item_name": "大众CC",
                        "brand_name": "大众",
                        "brand_id": "YZescxgescppcxdlxlcnkcxmy_1"
                         */
                        SearchCarBeanNew resultBeans = gson.fromJson(response, SearchCarBeanNew.class);
                        List<SearchCarBeanNew.DataBean.ResultBean> resultBeanList = resultBeans.getData().getResult();
                        searchListId.clear();
                        searchList.clear();
                        searchBrandList.clear();
                        searchBrandListId.clear();
                        for (SearchCarBeanNew.DataBean.ResultBean bean : resultBeanList) {
                            searchBrandList.add(bean.getBrand_name());
                            searchBrandListId.add(bean.getBrand_id());
                            searchList.add(bean.getItem_name());
                            searchListId.add(bean.getItem_id());
                        }
                        if (searchList.size() > 0)
                            PopupWindow();
                        else
                            ToastUtil.show(SearchActivity.this, "未查询到相关信息");
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

    private void preSearch(String str) {
        editSearch.setText(str);
    }

    @OnClick(R.id.cancle)
    public void onViewClicked() {
        JumpUtil.newInstance().finishRightTrans(this);
    }


    //弹出查询window
    private void PopupWindow() {
        // 用于PopupWindow的View
        View contentView = LayoutInflater.from(this).inflate(R.layout.popupwindow, null, false);
        RecyclerView recyclerView = contentView.findViewById(R.id.recyclerView_popup);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        CommonRecyclerAdapter adapter = new CommonRecyclerAdapter(this, searchList, R.layout.item_popup) {
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
                WindowManager.LayoutParams lp = SearchActivity.this.getWindow().getAttributes();
                lp.alpha = 1f;
                SearchActivity.this.getWindow().setAttributes(lp);
            }
        });
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        lp.verticalMargin = CommonUtils.dip2px(SearchActivity.this, 30);
        lp.alpha = 0.9f;
        this.getWindow().setAttributes(lp);
        //设置点击监听
        adapter.setOnItemClickListener(new CommonRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                window.dismiss();
                //跳转
                if ("car".equals(type)) {
                    Set<String> value = new HashSet<>();
                    value.add(searchList.get(position));
                    Prefs.with(getApplicationContext()).putStringSet("historyCars", value);
                    Bundle bundle = new Bundle();
                    bundle.putString("String", searchBrandList.get(position)+","+searchBrandListId.get(position)+","+searchList.get(position)+","+searchListId.get(position));
                    JumpUtil.newInstance().jumpLeft(SearchActivity.this, MainActivity.class, bundle);
                    SearchActivity.this.finish();
                } else if ("comp".equals(type)) {
                    Set<String> value = new HashSet<>();
                    value.add(searchList.get(position));
                    Prefs.with(getApplicationContext()).putStringSet("historyComp", value);
                    Bundle bundle = new Bundle();
                    bundle.putString("comp_id", searchListId.get(position));
                    JumpUtil.newInstance().jumpLeft(SearchActivity.this, CompDetail.class, bundle);
                    SearchActivity.this.finish();
                }
            }
        });
        recyclerView.setAdapter(adapter);
    }

    @OnClick({R.id.name, R.id.trangle})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.name:
            case R.id.trangle:
                popupSelect();
                break;
        }
    }

    //弹出选择框
    private void popupSelect() {
        // 用于PopupWindow的View
        View contentView = LayoutInflater.from(this).inflate(R.layout.item_searchselect, null, false);
        final View car = contentView.findViewById(R.id.ly_car);
        final View comp = contentView.findViewById(R.id.ly_comp);
        final TextView tv_car = car.findViewById(R.id.car);
        final TextView tv_comp = comp.findViewById(R.id.comp);
        if ("car".equals(type)) {
            tv_car.setTextColor(Color.parseColor("#ff5656"));
            tv_comp.setTextColor(Color.parseColor("#666666"));
        } else {
            tv_comp.setTextColor(Color.parseColor("#ff5656"));
            tv_car.setTextColor(Color.parseColor("#666666"));
        }
        // 创建PopupWindow对象，其中：
        // 第一个参数是用于PopupWindow中的View，第二个参数是PopupWindow的宽度，
        // 第三个参数是PopupWindow的高度，第四个参数指定PopupWindow能否获得焦点
        final PopupWindow window = new PopupWindow(contentView, RelativeLayout.LayoutParams.WRAP_CONTENT, /*CommonUtils.dip2px(CityActivity.this,150)*/RelativeLayout.LayoutParams.WRAP_CONTENT, true);
        // 设置PopupWindow的背景
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // 设置PopupWindow是否能响应外部点击事件
        window.setOutsideTouchable(true);
        // 设置PopupWindow是否能响应点击事件
        window.setTouchable(true);
        // 显示PopupWindow，其中：
        // 第一个参数是PopupWindow的锚点，第二和第三个参数分别是PopupWindow相对锚点的x、y偏移
        window.showAsDropDown(trangle, -80, 20);
        // 或者也可以调用此方法显示PopupWindow，其中：
        // 第一个参数是PopupWindow的父View，第二个参数是PopupWindow相对父View的位置，
        // 第三和第四个参数分别是PopupWindow相对父View的x、y偏移
        // window.showAtLocation(parent, gravity, x, y);
        // 设置背景颜色变暗
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
//                WindowManager.LayoutParams lp = SearchActivity.this.getWindow().getAttributes();
//                lp.alpha = 1f;
//                SearchActivity.this.getWindow().setAttributes(lp);
            }
        });
//        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
//        lp.verticalMargin = CommonUtils.dip2px(SearchActivity.this, 30);
//        lp.alpha = 0.9f;
//        this.getWindow().setAttributes(lp);
        car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_car.setTextColor(Color.parseColor("#ff5656"));
                tv_comp.setTextColor(Color.parseColor("#666666"));
                type = "car";
                getData();
                name.setText("汽车");
                window.dismiss();
            }
        });
        comp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_comp.setTextColor(Color.parseColor("#ff5656"));
                tv_car.setTextColor(Color.parseColor("#666666"));
                type = "comp";
                name.setText("商家");
                getData();
                window.dismiss();
            }
        });
    }

}
