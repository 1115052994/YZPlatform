package com.xtzhangbinbin.jpq.activity;

import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.adapter.CommonRecyclerAdapter;
import com.xtzhangbinbin.jpq.adapter.ViewHolder;
import com.xtzhangbinbin.jpq.base.BaseActivity;
import com.xtzhangbinbin.jpq.utils.JumpUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WeizhangResult extends BaseActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private CommonRecyclerAdapter adapter;
    private List<String> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weizhang_result);
        ButterKnife.bind(this);
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

    private void initView(){
        list.add("111");
        list.add("222");
        adapter = new CommonRecyclerAdapter(this,list,R.layout.item_weizhang) {
            @Override
            public void convert(ViewHolder holder, Object item, int position) {
                CardView cardView = (CardView)holder.getView(R.id.cardView);
                cardView.setRadius(6);//设置图片圆角的半径大小
                cardView.setCardElevation(8);//设置阴影部分大小
                cardView.setContentPadding(5,5,5,5);//设置图片距离阴影大小
            }
        };
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}
