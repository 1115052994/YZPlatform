package com.xtzhangbinbin.jpq.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.adapter.CommonRecyclerAdapter;
import com.xtzhangbinbin.jpq.adapter.ViewHolder;
import com.xtzhangbinbin.jpq.base.BaseActivity;
import com.xtzhangbinbin.jpq.entity.WeizhangResultBean;
import com.xtzhangbinbin.jpq.utils.JumpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WeizhangResult extends BaseActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private CommonRecyclerAdapter adapter;
    private List<WeizhangResultBean.ListsBean> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weizhang_result);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setTitle("查询结果");
        setLeftImageClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JumpUtil.newInstance().finishRightTrans(WeizhangResult.this);
            }
        });
    }

    private void initData(){
        Intent intent = getIntent();
        if (intent!=null){
            Bundle bundle = intent.getExtras();
            if (bundle!=null){
                String result = bundle.getString("String","");
                if (!"".equals(result)){
                    try {
                        Gson gson = new Gson();
                        WeizhangResultBean weizhangResultBean = gson.fromJson(result, WeizhangResultBean.class);
                        list.clear();
                        List<WeizhangResultBean.ListsBean> list1 = weizhangResultBean.getLists();
                        for (int i=0;i<list1.size();i++){
                            list.add(list1.get(i));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void initView(){
        adapter = new CommonRecyclerAdapter(this,list,R.layout.item_weizhang) {
            @Override
            public void convert(ViewHolder holder, Object item, int position) {
                WeizhangResultBean.ListsBean bean = list.get(position);
                CardView cardView = (CardView)holder.getView(R.id.cardView);
                cardView.setRadius(6);//设置图片圆角的半径大小
                cardView.setCardElevation(8);//设置阴影部分大小
                cardView.setContentPadding(5,5,5,5);//设置图片距离阴影大小
                TextView tvDate = holder.getView(R.id.tv_date);
                tvDate.setText(bean.getDate());
                TextView tvLoc = holder.getView(R.id.tv_loc);
                tvLoc.setText(bean.getAddr());
                TextView tvDes = holder.getView(R.id.tv_des);
                tvDes.setText(bean.getContent());
                TextView tvJifen = holder.getView(R.id.tv_jifen);
                tvJifen.setText(bean.getGrade()+"分");
                TextView tvFk = holder.getView(R.id.tv_fk);
                tvFk.setText(bean.getMoney()+"元");
            }
        };
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}
