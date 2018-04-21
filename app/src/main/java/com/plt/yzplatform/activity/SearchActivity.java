package com.plt.yzplatform.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.plt.yzplatform.R;
import com.plt.yzplatform.adapter.GrideViewAdapter;
import com.plt.yzplatform.config.Config;
import com.plt.yzplatform.entity.HotCar;
import com.plt.yzplatform.utils.JumpUtil;
import com.plt.yzplatform.utils.NetUtil;
import com.plt.yzplatform.utils.Prefs;
import com.plt.yzplatform.utils.ToastUtil;
import com.plt.yzplatform.view.ExpandableGridView;
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

    private String secleted = "";
    private GrideViewAdapter hotAdapter;
    private GrideViewAdapter hisGvAapter;
    private List<String> hotList = new ArrayList<>();
    private List<String> hotListId = new ArrayList<>();
    private List<String> hisList = new ArrayList<>();
    private List<String> hisListId = new ArrayList<>();

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
                secleted(hisListId.get(position));
            }
        });
        hot.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                secleted(hotListId.get(position));
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
        if (NetUtil.isNetAvailable(SearchActivity.this)) {
            OkHttpUtils.post()
                    .url(Config.GETSEARCHHOTWORD)
                    .addHeader("user_token", Prefs.with(getApplicationContext()).read("user_token"))
                    .addParams("hotword_type", type)//car/comp
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
                                    hotList.clear();
                                    hotListId.clear();
                                    HotCar hotCar = gson.fromJson(response, HotCar.class);
                                    List<HotCar.DataBean.ResultBean> list = hotCar.getData().getResult();
                                    for (int i = 0; i < list.size(); i++) {
                                        if (i < 8) {
                                            hotList.add(list.get(i).getHotword_name());
                                            hotListId.add(list.get(i).getHotword_id());
                                        }
                                    }
                                    hotAdapter.notifyDataSetChanged();
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

    public void getHis() {
        Set<String> value = new HashSet<>();
        value.add("大众");
        value.add("宝马");
        Prefs.with(getApplicationContext()).putStringSet("historyCars", value);
        Set<String> set = Prefs.with(getApplicationContext()).getStringSet("historyCars", null);
        for (String s : set) {
            hisList.add(s);
        }
        Set<String> valueId = new HashSet<>();
        value.add("1");
        value.add("3");
        Prefs.with(getApplicationContext()).putStringSet("historyCarsId", value);
        Set<String> setId = Prefs.with(getApplicationContext()).getStringSet("historyCarsId", null);
        for (String s : set) {
            hisListId.add(s);
        }
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

    private void secleted(String str) {
        secleted = str;
    }

    @OnClick(R.id.cancle)
    public void onViewClicked() {
        JumpUtil.newInstance().finishRightTrans(this);
    }
}
