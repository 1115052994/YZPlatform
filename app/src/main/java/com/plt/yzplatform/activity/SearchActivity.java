package com.plt.yzplatform.activity;

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
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.plt.yzplatform.R;
import com.plt.yzplatform.adapter.CommonRecyclerAdapter;
import com.plt.yzplatform.adapter.GrideViewAdapter;
import com.plt.yzplatform.adapter.ViewHolder;
import com.plt.yzplatform.config.Config;
import com.plt.yzplatform.entity.HotCar;
import com.plt.yzplatform.utils.CommonUtils;
import com.plt.yzplatform.utils.JumpUtil;
import com.plt.yzplatform.utils.NetUtil;
import com.plt.yzplatform.utils.OKhttptils;
import com.plt.yzplatform.utils.Prefs;
import com.plt.yzplatform.utils.ToastUtil;
import com.plt.yzplatform.view.ExpandableGridView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

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
import okhttp3.Call;

public class SearchActivity extends AppCompatActivity {

    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.trangle)
    ImageView trangle;
    @BindView(R.id.edit_search)
    EditText editSearch;
    @BindView(R.id.cancle)
    TextView cancle;
    @BindView(R.id.historyPlace)
    ExpandableGridView history;
    @BindView(R.id.hotPlace)
    ExpandableGridView hot;
    @BindView(R.id.search)
    RelativeLayout search;


    private String secleted = "";
    private GrideViewAdapter hotAdapter;
    private GrideViewAdapter hisGvAapter;
    private List<String> hotList = new ArrayList<>();
    //    private List<String> hotListId = new ArrayList<>();
    private List<String> hisList = new ArrayList<>();
    //    private List<String> hisListId = new ArrayList<>();
    // 搜索结果
    private List<String> searchList = new ArrayList<>();
    private List<String> searchListId = new ArrayList<>();
    //类别（汽车、商户）car/comp
    private String type = "car";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        Window win = getWindow();
        WindowManager.LayoutParams params = win.getAttributes();
        win.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        initView();
        initData();
        getData();
    }

    private void initView() {

        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.i("edittextview", "onTextChanged=" + editSearch.getText().toString());
                if ("car".equals(type)) {
                    getSeachCar(editSearch.getText().toString().trim());
                } else if ("comp".equals(type)) {
                    getSearchComp(editSearch.getText().toString().trim());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        history.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                preSearch(hisList.get(position));
            }
        });
        hot.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                preSearch(hotList.get(position));
            }
        });
    }

    private void initData() {
        /* girdview绑定adapter */
        //gvAdapter = new ArrayAdapter<String>(CityActivity.this, R.layout.item_city, list);
        hotAdapter = new GrideViewAdapter(this, hotList);
        hisGvAapter = new GrideViewAdapter(this, hisList);
        hot.setAdapter(hotAdapter);
        history.setAdapter(hisGvAapter);
    }

    public void getData() {
        getHis();
        getHot();
    }

    public void getHot() {
//        if (NetUtil.isNetAvailable(SearchActivity.this)) {
//            OkHttpUtils.post()
//                    .url(Config.GETSEARCHHOTWORD)
//                    .addHeader("user_token", Prefs.with(getApplicationContext()).read("user_token"))
//                    .addParams("hotword_type", type)//car/comp
//                    .build()
//                    .execute(new StringCallback() {
//
//                        @Override
//                        public void onError(Call call, Exception e, int id) {
//
//                        }
//
//                        @Override
//                        public void onResponse(String response, int id) {
//                            Log.d("onResponse", "onResponse" + response);
//                            try {
//                                JSONObject object = new JSONObject(response);
//                                String data = object.getString("data");
//                                if (data != null) {
//                                    Gson gson = new Gson();
//                                    hotList.clear();
//                                    //hotListId.clear();
//                                    HotCar hotCar = gson.fromJson(response, HotCar.class);
//                                    List<HotCar.DataBean.ResultBean> list = hotCar.getData().getResult();
//                                    for (int i = 0; i < list.size(); i++) {
//                                        if (i < 8) {
//                                            hotList.add(list.get(i).getHotword_name());
//                                            //hotListId.add(list.get(i).getHotword_id());
//                                        }
//                                    }
//                                    hotAdapter.notifyDataSetChanged();
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    });
//        } else {
//            ToastUtil.noNetAvailable(SearchActivity.this);
//        }
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

    public void getHis() {
        Set<String> value = new HashSet<>();
        value.add("大众");
        value.add("宝马");
        Prefs.with(getApplicationContext()).putStringSet("historyCars", value);
        Set<String> set = Prefs.with(getApplicationContext()).getStringSet("historyCars", null);
        for (String s : set) {
            hisList.add(s);
        }
//        Set<String> valueId = new HashSet<>();
//        value.add("1");
//        value.add("3");
//        Prefs.with(getApplicationContext()).putStringSet("historyCarsId", value);
//        Set<String> setId = Prefs.with(getApplicationContext()).getStringSet("historyCarsId", null);
//        for (String s : set) {
//            hisListId.add(s);
//        }
        hisGvAapter.notifyDataSetChanged();
    }

    private void getSearchComp(String s) {
        if (NetUtil.isNetAvailable(SearchActivity.this)) {
            OkHttpUtils.post()
                    .url(Config.GETSEARCHCOMP)
                    .addHeader("user_token", Prefs.with(getApplicationContext()).read("user_token"))
                    .addParams("comp_name", s)
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
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } else {
            ToastUtil.noNetAvailable(SearchActivity.this);
        }
    }

    private void getSeachCar(String s) {
        if (NetUtil.isNetAvailable(SearchActivity.this)) {
            OkHttpUtils.post()
                    .url(Config.GETSEARCHCOMP)
                    .addHeader("user_token", Prefs.with(getApplicationContext()).read("user_token"))
                    .addParams("car_name", s)
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
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } else {
            ToastUtil.noNetAvailable(SearchActivity.this);
        }
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
        window.showAsDropDown(search, 20, 20);
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
            }
        });
        recyclerView.setAdapter(adapter);
    }

}
