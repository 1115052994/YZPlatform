package com.plt.yzplatform.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.gson.Gson;
import com.plt.yzplatform.R;
import com.plt.yzplatform.adapter.CallStar;
import com.plt.yzplatform.adapter.StarAdapter;
import com.plt.yzplatform.base.BaseActivity;
import com.plt.yzplatform.config.Config;
import com.plt.yzplatform.entity.Querystar;
import com.plt.yzplatform.gson.factory.GsonFactory;
import com.plt.yzplatform.utils.JumpUtil;
import com.plt.yzplatform.utils.NetUtil;
import com.plt.yzplatform.utils.Prefs;
import com.plt.yzplatform.utils.ToastUtil;
import com.plt.yzplatform.view.OrdinaryDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

public class Starperformers extends BaseActivity {
    private static final String TAG = "明星员工";
    @BindView(R.id.star_list)
    ListView starList;
    @BindView(R.id.layout)
    LinearLayout layout;
    private List<Querystar.DataBean.ResultBean> result = new ArrayList<>();
    public StarAdapter starAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starperformers);
        ButterKnife.bind(this);
        starList = findViewById(R.id.star_list);
        layout = findViewById(R.id.layout);
        starAdapter = new StarAdapter(this,result);
        starList.setAdapter(starAdapter);
        getData();
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(result.size()>=3){
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
                }else {
                    startActivityForResult(new Intent(Starperformers.this, Addstar.class), 1);
                }
            }
        });
        starAdapter.getcall(new CallStar() {
            @Override
            public void Call(View view,String id,int i,String file_id) {
                getData();
                if(i>-1&&id!=null&&file_id!=null){
                    Intent intent = new Intent(Starperformers.this, Addstar.class);
                    intent.putExtra("id",id);
                    intent.putExtra("file_id",file_id);
                    intent.putExtra("name",result.get(i).getStaff_name());
                    intent.putExtra("info",result.get(i).getStaff_info());
                    startActivityForResult(intent, 3);
                }
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
        if (requestCode == 1 && resultCode == 1) {
            getData();
            starAdapter.notifyDataSetChanged();
        }else if(requestCode == 3 && resultCode == 4){
            getData();
            starAdapter.notifyDataSetChanged();
        }
    }
    //得到数据并且更新数据
    private void getData() {
        result.clear();
        if (NetUtil.isNetAvailable(this)) {
            OkHttpUtils.get()
                    .url(Config.SELECTSTAR)
                    .addHeader("user_token", Prefs.with(getApplicationContext()).read("user_token"))
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            ToastUtil.noNAR(Starperformers.this);
                        }
                        @Override
                        public void onResponse(String response, int id) {
                            Log.i("aaaaa", "图片获取数据: " + response);
                            Gson gson = GsonFactory.create();
                            Querystar querystar = gson.fromJson(response, Querystar.class);
                            if(querystar.getData().getResult()==null){
                                ToastUtil.show(Starperformers.this,"数据为空");
                            }
                            result.addAll(querystar.getData().getResult());
                            starAdapter.notifyDataSetChanged();
                        }
                    });
        }
    }


}
