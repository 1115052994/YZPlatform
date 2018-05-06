package com.xtzhangbinbin.jpq.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.adapter.CallStar;
import com.xtzhangbinbin.jpq.adapter.StarAdapter;
import com.xtzhangbinbin.jpq.base.BaseActivity;
import com.xtzhangbinbin.jpq.config.Config;
import com.xtzhangbinbin.jpq.entity.Querystar;
import com.xtzhangbinbin.jpq.gson.factory.GsonFactory;
import com.xtzhangbinbin.jpq.utils.JumpUtil;
import com.xtzhangbinbin.jpq.utils.OKhttptils;
import com.xtzhangbinbin.jpq.utils.ToastUtil;
import com.xtzhangbinbin.jpq.view.OrdinaryDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Starperformers extends BaseActivity {
    private static final String TAG = "明星员工";
    @BindView(R.id.star_list)
    ListView starList;
    @BindView(R.id.layout)
    LinearLayout layout;
    @BindView(R.id.no_information_image)
    ImageView noInformationImage;
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    private List<Querystar.DataBean.ResultBean> result = new ArrayList<>();
    public StarAdapter starAdapter;
    private int pageIndex = 1;
    private int pageTotal = 1;//总页数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starperformers);
        ButterKnife.bind(this);
        starList = findViewById(R.id.star_list);
        layout = findViewById(R.id.layout);
        getData();
        starAdapter = new StarAdapter(this, result);
        starList.setAdapter(starAdapter);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (result.size() >= 3) {
                    final OrdinaryDialog ordinaryDialog = OrdinaryDialog.newInstance(Starperformers.this).setCancel("取消").setConfirm("知道了").setMessage1("不能添加超过3个员工").showDialog();
                    ordinaryDialog.setNoOnclickListener(new OrdinaryDialog.onNoOnclickListener() {
                        @Override
                        public void onNoClick() {
                            ordinaryDialog.dismiss();
                        }
                    });
                    ordinaryDialog.setYesOnclickListener(new OrdinaryDialog.onYesOnclickListener() {
                        @Override
                        public void onYesClick() {
                            ordinaryDialog.dismiss();
                        }
                    });
                } else {
                    startActivityForResult(new Intent(Starperformers.this, Addstar.class), 1);
                }
            }
        });
        starAdapter.getcall(new CallStar() {
            @Override
            public void Call(View view, String id, int i, String file_id,int position) {
                if (i > -1 && id != null && file_id != null) {
                    Intent intent = new Intent(Starperformers.this, Addstar.class);
                    intent.putExtra("id", id);
                    intent.putExtra("file_id", file_id);
                    intent.putExtra("name", result.get(i).getStaff_name());
                    intent.putExtra("info", result.get(i).getStaff_info());
                    Log.d("Call", "Call: " + id);
                    startActivityForResult(intent, 3);
                }
                result.remove(position);
                starAdapter.notifyDataSetChanged();


            }
        });

        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                pageIndex = 1;
                result.clear();
                getData();
            }
        });
        smartRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
//                if(pageIndex < pageTotal){
//                    getData(++pageIndex,dict_id,auth_comp_city, refreshlayout);
//                }else {
//                    refreshlayout.finishLoadmore();
//                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        setTitle("明星员工");
        setLeftImageClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JumpUtil.newInstance().finishRightTrans(Starperformers.this);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getData();
    }

    //得到数据并且更新数据
    private void getData() {
        result.clear();
        OKhttptils.post(Starperformers.this, Config.SELECTSTAR, null, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                Log.d("aaaaa", "getData1: " + response);
                Gson gson = GsonFactory.create();
                Querystar querystar = gson.fromJson(response, Querystar.class);
                Log.d("aaaaa", "getData2: " + response);
                if (querystar.getData().getResult() == null) {
                    ToastUtil.show(Starperformers.this, "数据为空");
                }
                result.addAll(querystar.getData().getResult());
                starAdapter.notifyDataSetChanged();
                Log.d("aaaaa", "getData3: " + response);
                if (result.size() <= 0) {
                    noInformationImage.setVisibility(View.VISIBLE);
                }
                starAdapter.notifyDataSetChanged();

            }

            @Override
            public void fail(String response) {
                Toast.makeText(Starperformers.this, "获取失败", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
