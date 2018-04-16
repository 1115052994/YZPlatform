package com.plt.yzplatform.activity;

import android.opengl.Visibility;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
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
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
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
    @BindView(R.id.head)
    RelativeLayout head;
    private List<String> city = new ArrayList<>();
    private List<String> list = new ArrayList<>();
    private GrideViewAdapter gvAdapter;
    private RecyclerView.Adapter rvAdapter;
    private GridLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        ButterKnife.bind(this);
        letter.getBackground().setAlpha(80);
        initData();
        initView();
    }

    private void initView() {
        letterSideBar.setOnLetterTouchListener(new LetterSideBar.LetterTouchListener() {
            @Override
            public void touch(CharSequence letter, boolean isTouch) {
                //if (!isTouch){
                String s = letter.toString();
                int index = list.indexOf(s);
                Log.i("index", "index=" + index);
                recyclerViewCity.scrollToPosition(index);
                //}
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    private void initData() {
//        city.add("A");
//        city.add("乌鲁木齐");
//        for (int i = 0; i < 5; i++) {
//            city.add("list" + i);
//        }
        /* girdview绑定adapter */
        //gvAdapter = new ArrayAdapter<String>(CityActivity.this, R.layout.item_city, list);
        gvAdapter = new GrideViewAdapter(this, city);
        historyPlace.setAdapter(gvAdapter);
        hotPlace.setAdapter(gvAdapter);

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
        layoutManager = new GridLayoutManager(this, 3);
        // 设置布局管理器
        recyclerViewCity.setLayoutManager(layoutManager);
        recyclerViewCity.setAdapter(rvAdapter);
        recyclerViewCity.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int firstVisibleItemPosition=layoutManager.findFirstVisibleItemPosition();//可见范围内的第一项的位置
                if(firstVisibleItemPosition==0){
                    if(head.getVisibility()== View.GONE){
                        head.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch(newState)
                {
                    case 0:
                        //System.out.println("recyclerview已经停止滚动");
//                        if(head.getVisibility()== View.INVISIBLE){
//                            head.setVisibility(View.VISIBLE);
//                        }
                        break;
                    case 1:
                        //System.out.println("recyclerview正在被拖拽");
                        if(head.getVisibility()== View.VISIBLE){
                            head.setVisibility(View.GONE);
                        }
                        break;
                    case 2:
                        //System.out.println("recyclerview正在依靠惯性滚动");
                        if(head.getVisibility()== View.VISIBLE){
                            head.setVisibility(View.GONE);
                        }
                        break;
                }
            }
        });

    }

    public void getData() {
        getCitys();
        getHotCitys();
    }

    public void getCitys() {
        //是省份，通过省获取市
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
        //是省份，通过省获取市
        if (NetUtil.isNetAvailable(CityActivity.this)) {
            OkHttpUtils.post()
                    .url(Config.QUERYHOTCITY)
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
                                    Gson gson = new Gson();
                                    HotCity hotCity = gson.fromJson(response, HotCity.class);
                                    List<String> list = hotCity.getData().getResult();
                                    for(String s:list){
                                        city.add(s);
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
}
