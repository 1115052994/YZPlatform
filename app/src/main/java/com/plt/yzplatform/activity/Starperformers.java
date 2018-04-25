package com.plt.yzplatform.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.plt.yzplatform.R;
import com.plt.yzplatform.adapter.CallStar;
import com.plt.yzplatform.adapter.StarAdapter;
import com.plt.yzplatform.base.BaseActivity;
import com.plt.yzplatform.config.Config;
import com.plt.yzplatform.entity.Querystar;
import com.plt.yzplatform.gson.factory.GsonFactory;
import com.plt.yzplatform.utils.JumpUtil;
import com.plt.yzplatform.utils.OKhttptils;
import com.plt.yzplatform.utils.ToastUtil;
import com.plt.yzplatform.view.OrdinaryDialog;

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
    private List<Querystar.DataBean.ResultBean> result = new ArrayList<>();
    public StarAdapter starAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starperformers);
        ButterKnife.bind(this);
        starList = findViewById(R.id.star_list);
        layout = findViewById(R.id.layout);
        starAdapter = new StarAdapter(this, result);
        starList.setAdapter(starAdapter);
        getData();
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
            public void Call(View view, String id, int i, String file_id) {
                if (i > -1 && id != null && file_id != null) {
                    Intent intent = new Intent(Starperformers.this, Addstar.class);
                    intent.putExtra("id", id);
                    intent.putExtra("file_id", file_id);
                    intent.putExtra("name", result.get(i).getStaff_name());
                    intent.putExtra("info", result.get(i).getStaff_info());
                    startActivityForResult(intent, 3);
                }
                getData();
                starAdapter.notifyDataSetChanged();
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
        starAdapter.notifyDataSetChanged();
    }

    //得到数据并且更新数据
    private void getData() {
        result.clear();
        OKhttptils.post(Starperformers.this, Config.SELECTSTAR, null, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                Gson gson = GsonFactory.create();
                Querystar querystar = gson.fromJson(response, Querystar.class);
                if (querystar.getData().getResult() == null) {
                    ToastUtil.show(Starperformers.this, "数据为空");
                }
                result.addAll(querystar.getData().getResult());
                if(result.size()<=0){
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
