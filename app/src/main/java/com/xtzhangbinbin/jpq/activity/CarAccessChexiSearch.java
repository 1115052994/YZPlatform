package com.xtzhangbinbin.jpq.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.adapter.CommonRecyclerAdapter;
import com.xtzhangbinbin.jpq.adapter.ViewHolder;
import com.xtzhangbinbin.jpq.base.BaseActivity;
import com.xtzhangbinbin.jpq.config.Config;
import com.xtzhangbinbin.jpq.entity.AccessChexiBean;
import com.xtzhangbinbin.jpq.utils.JumpUtil;
import com.xtzhangbinbin.jpq.utils.OKhttptils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CarAccessChexiSearch extends BaseActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private String brandId = "";
    // (id_carbrand,tv_carbrand)
    private List<Map<String, String>> recyclerList = new ArrayList<>();
    private CommonRecyclerAdapter recyclerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_access_chexi);
        Intent intent = getIntent();
        if (intent!=null){
            Bundle bundle = intent.getExtras();
            brandId = bundle.getString("string");
            Log.i("brandId",brandId);
        }
        ButterKnife.bind(this);
        initView();
        getChexi();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setTitle("选择车系");
        setLeftImageClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JumpUtil.newInstance().finishRightTrans(CarAccessChexiSearch.this);
            }
        });
    }

    private void initView() {
        // 品牌列表
        recyclerAdapter = new CommonRecyclerAdapter(this,recyclerList,R.layout.item_access_carbrandorchexi) {
            @Override
            public void convert(ViewHolder holder, Object item, int position) {
                // 品牌
                TextView name = holder.getView(R.id.tv_carbrand);
                if (recyclerList.get(position).get("tv_carbrand") != null) {
                    name.setText(recyclerList.get(position).get("tv_carbrand"));
                }
            }
        };
        recyclerAdapter.setOnItemClickListener(new CommonRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                String letter = recyclerList.get(position).get("id_carbrand");
                if (!"letter".equals(letter)) {
                    // 搜索年款
                    JumpUtil.newInstance().jumpRight(CarAccessChexiSearch.this,CarAccessModeSearch.class,letter);
                    JumpUtil.newInstance().finishRightTrans(CarAccessChexiSearch.this);
                }
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerAdapter);
    }

    private void getChexi(){
        Map<String, String> map = new HashMap<>();
        map.put("brand",brandId);
        OKhttptils.post(this, Config.GETJUHECARTRIAN, map, new OKhttptils.HttpCallBack() {
            @Override
            public String success(String response) {
                Log.i("acceschexi",response);
                try {
                    JSONObject object = new JSONObject(response);
                    if ("1".equals(object.getString("status"))) {
                        Gson gson = new Gson();
                        AccessChexiBean chexi = gson.fromJson(response,AccessChexiBean.class);
                        List<AccessChexiBean.DataBean.ResultBean> list = chexi.getData().getResult();
                        recyclerList.clear();
                        for (AccessChexiBean.DataBean.ResultBean bean:list){
                            Map<String,String> map = new HashMap<>();
                            map.put("id_carbrand",bean.getTid());
                            map.put("tv_carbrand",bean.getTname());
                            recyclerList.add(map);
                        }
                        recyclerAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return response;
            }

            @Override
            public void fail(String response) {

            }
        });
    }
}
