package com.xtzhangbinbin.jpq.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.adapter.EvaluationListAdapter;
import com.xtzhangbinbin.jpq.base.BaseActivity;
import com.xtzhangbinbin.jpq.config.Config;
import com.xtzhangbinbin.jpq.entity.QueryNum;
import com.xtzhangbinbin.jpq.fragment.EvaluationFragment;
import com.xtzhangbinbin.jpq.gson.factory.GsonFactory;
import com.xtzhangbinbin.jpq.utils.JumpUtil;
import com.xtzhangbinbin.jpq.utils.NetUtil;
import com.xtzhangbinbin.jpq.utils.OKhttptils;
import com.xtzhangbinbin.jpq.utils.TabUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EvaluationList extends BaseActivity {
    @BindView(R.id.evaluation_list_tab)
    TabLayout evaluationListTab;
    @BindView(R.id.evaluation_list_pager)
    ViewPager evaluationListPager;
    private ArrayList<Fragment> fragment = new ArrayList<>();
    private ArrayList<String> str = new ArrayList<>();
    private HashMap<String,String> map=new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluation_list);
        ButterKnife.bind(this);
        String id="24";
        fragment.add(new EvaluationFragment(this,"1",id));
        fragment.add(new EvaluationFragment(this,"2",id));
        fragment.add(new EvaluationFragment(this,"3",id));
        fragment.add(new EvaluationFragment(this,"4",id));
        PostCar(Config.SCOREBYCOMPCOUNT);

    }

    @Override
    protected void onStart() {
        super.onStart();
        setTitle("评价列表");
        setLeftImageClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JumpUtil.newInstance().finishRightTrans(EvaluationList.this);
            }
        });
    }
    public void PostCar(String url){

        map.clear();
        if (NetUtil.isNetAvailable(this)) {
            map.put("comp_id","24");
            OKhttptils.post(this, url, map, new OKhttptils.HttpCallBack() {
                @Override
                public void success(String response) {
                    Log.i("aaaaa", "查询评价: " + response);
                    Gson gson = GsonFactory.create();
                    QueryNum queryNum = gson.fromJson(response, QueryNum.class);
                    List<QueryNum.DataBean.ResultBean> result = queryNum.getData().getResult();
                    str.add("全部"+"("+((int)result.get(0).getStar45num()+(int)result.get(0).getStar3num()+(int)result.get(0).getStar12num())+")");
                    str.add("好评"+"("+result.get(0).getStar45num()+")");
                    str.add("中评"+"("+result.get(0).getStar3num()+")");
                    str.add("差评"+"("+result.get(0).getStar12num()+")");
                    EvaluationListAdapter evaluationListAdapter = new EvaluationListAdapter(getSupportFragmentManager(), fragment, str);
                    evaluationListPager.setAdapter(evaluationListAdapter);
                    evaluationListPager.setOffscreenPageLimit(4);
                    evaluationListTab.setupWithViewPager(evaluationListPager);
                    TabUtil.showTabTextAdapteIndicator(evaluationListTab);
                    evaluationListTab.setTabMode(TabLayout.MODE_FIXED);
                }

                @Override
                public void fail(String response) {
                    Toast.makeText(EvaluationList.this, "查询失败", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
