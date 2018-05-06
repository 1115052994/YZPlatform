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
import com.xtzhangbinbin.jpq.entity.AccessModeBean;
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

public class CarAccessModeSearch extends BaseActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private String chexiId = "";
    // (id_carbrand,tv_carbrand)
    private List<Map<String, String>> recyclerList = new ArrayList<>();
    private CommonRecyclerAdapter recyclerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_access_mode_search);
        Intent intent = getIntent();
        if (intent!=null){
            Bundle bundle = intent.getExtras();
            chexiId = bundle.getString("string");
            Log.i("series",chexiId);
        }
        ButterKnife.bind(this);
        initView();
        getMode();
    }
    @Override
    protected void onStart() {
        super.onStart();
        setTitle("选择车系");
        setLeftImageClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JumpUtil.newInstance().finishRightTrans(CarAccessModeSearch.this);
            }
        });
    }

    private void initView() {
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
                    Log.i("backData",letter+"---"+recyclerList.get(position).get("tv_carbrand"));
                    if (listener!=null) {
                        listener.backData(recyclerList.get(position).get("id_carbrand"), recyclerList.get(position).get("tv_carbrand"));
                        JumpUtil.newInstance().finishRightTrans(CarAccessModeSearch.this);
                    }
                }
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerAdapter);
    }

    private void getMode(){
        Map<String, String> map = new HashMap<>();
        map.put("series",chexiId);
        OKhttptils.post(this, Config.GETJUHEMODEL, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                Log.i("series",response);
                try {
                    JSONObject object = new JSONObject(response);
                    if ("1".equals(object.getString("status"))) {
                        Gson gson = new Gson();
                        AccessModeBean bean = gson.fromJson(response, AccessModeBean.class);
                        recyclerList.clear();
                        List<AccessModeBean.DataBean.ResultBean> resultBeanList = bean.getData().getResult();
                        for (AccessModeBean.DataBean.ResultBean resultBean:resultBeanList){
                            Map<String,String> map = new HashMap<>();
                            map.put("id_carbrand",resultBean.getMid());
                            map.put("tv_carbrand",resultBean.getMname());
                            recyclerList.add(map);
                        }
                        recyclerAdapter.notifyDataSetChanged();
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

    static DataBackListener listener;
    public static void setDataBackListener(DataBackListener databackListener){
        listener = databackListener;
    }
    interface DataBackListener{
        void backData(String id,String name);
    }
}
