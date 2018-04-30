package com.plt.yzplatform.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.plt.yzplatform.AppraiseInterface;
import com.plt.yzplatform.R;
import com.plt.yzplatform.adapter.AppraiseGVAdapter;
import com.plt.yzplatform.adapter.GrideViewAdapter;
import com.plt.yzplatform.base.BaseActivity;
import com.plt.yzplatform.config.Config;
import com.plt.yzplatform.entity.AppraiseBean;
import com.plt.yzplatform.entity.CompDetailBean;
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
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class AppraiseActivity extends BaseActivity {

    @BindView(R.id.comp_name)
    TextView compName;
    @BindView(R.id.star1)
    CheckBox star1;
    @BindView(R.id.star2)
    CheckBox star2;
    @BindView(R.id.star3)
    CheckBox star3;
    @BindView(R.id.star4)
    CheckBox star4;
    @BindView(R.id.star5)
    CheckBox star5;
    @BindView(R.id.apprase_gv)
    ExpandableGridView appraseGv;
    @BindView(R.id.content)
    EditText content;
    @BindView(R.id.submit)
    Button submit;

    private int star = 0;
    private List<String> preAppraiseList = new ArrayList<>();
    private AppraiseGVAdapter gvAapter;
    private List<String> appraiseList = new ArrayList<>();
    private List<String> appraiseListId = new ArrayList<>();
    private String comp_id = "24";
    private String comp_name = "";
    private String type ="";//car/comp

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appraise);
        ButterKnife.bind(this);
        initData();
        getData();
        Intent intent = getIntent();
        if(intent!=null){
            Bundle bundle = intent.getExtras();
            if(bundle!=null) {
                comp_id = bundle.getString("comp_id");
                Log.i("com_id", "comp_id=" + comp_id);
                type = bundle.getString("type");
            }
        }
    }

    private void initData() {
        gvAapter = new AppraiseGVAdapter(this, appraiseList);
        gvAapter.setOnItemClickListener(new AppraiseInterface() {
            @Override
            public void onClick(View view, int position) {
                TextView tv = view.findViewById(R.id.tv_appraise);
                if(!view.isSelected()) {
                    view.setSelected(true);
                    tv.setTextColor(Color.parseColor("#ff9696"));
                    preAppraiseList.add(appraiseListId.get(position));
                }else{
                    view.setSelected(false);
                    tv.setTextColor(Color.parseColor("#333333"));
                    preAppraiseList.remove(appraiseListId.get(position));
                }
            }
        });
        appraseGv.setAdapter(gvAapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setTitle("评价");
        setLeftImageClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JumpUtil.newInstance().finishRightTrans(AppraiseActivity.this);
            }
        });
    }

    private void getData(){
        if (NetUtil.isNetAvailable(getApplicationContext())) {
            OkHttpUtils.post()
                    .url(Config.COMMENTORCOMPNAME)
                    .addHeader("user_token", Prefs.with(this).read("user_token"))
                    .addParams("comp_id", comp_id)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            ToastUtil.noNAR(AppraiseActivity.this);
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            Log.w("onResponse", "onResponse:" + response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getString("status").equals("1")) {
                                    Gson gson = new Gson();
                                    AppraiseBean appraiseBean = gson.fromJson(response, AppraiseBean.class);
                                    List<AppraiseBean.DataBean.ResultBean.ServiceBean> resultBeanList = appraiseBean.getData().getResult().getService();
                                    comp_name = appraiseBean.getData().getResult().getComp_name().getComp_name();
                                    compName.setText(comp_name);
                                    appraiseList.clear();
                                    appraiseListId.clear();
                                    for (AppraiseBean.DataBean.ResultBean.ServiceBean bean:
                                         resultBeanList) {
                                        appraiseList.add(bean.getDict_desc());
                                        appraiseListId.add(bean.getDict_id());
                                    }
                                    gvAapter.notifyDataSetChanged();
                                } else {
                                    ToastUtil.noNAR(AppraiseActivity.this);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } else {
            ToastUtil.noNetAvailable(AppraiseActivity.this);
        }
    }
    @OnClick({R.id.star1, R.id.star2, R.id.star3, R.id.star4, R.id.star5, R.id.submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.star5:
                Log.i("star5","star5="+star5.isChecked());
                if(!star5.isChecked()){
                    Log.i("star5","star5="+star5.isChecked());
                    star5.setChecked(false);
                }else{
                    star5.setChecked(true);
                    star4.setChecked(true);
                    star3.setChecked(true);
                    star2.setChecked(true);
                    star1.setChecked(true);
                }
                break;
            case R.id.star4:
                if(!star4.isChecked()){
                    star4.setChecked(false);
                    star5.setChecked(false);
                }else{
                    star4.setChecked(true);
                    star3.setChecked(true);
                    star2.setChecked(true);
                    star1.setChecked(true);
                }
                break;
            case R.id.star3:
                if(!star3.isChecked()){
                    star3.setChecked(false);
                    star4.setChecked(false);
                    star5.setChecked(false);
                }else{
                    star3.setChecked(true);
                    star2.setChecked(true);
                    star1.setChecked(true);
                }
                break;
            case R.id.star2:
                if(!star2.isChecked()){
                    star2.setChecked(false);
                    star3.setChecked(false);
                    star4.setChecked(false);
                    star5.setChecked(false);
                }else{
                    star2.setChecked(true);
                    star1.setChecked(true);
                }
                break;
            case R.id.star1:
                if(!star1.isChecked()){
                    star1.setChecked(false);
                    star2.setChecked(false);
                    star3.setChecked(false);
                    star4.setChecked(false);
                    star5.setChecked(false);
                }else{
                    star1.setChecked(true);
                }
                break;
            case R.id.submit:
                preSubmit();
                break;
        }
    }

    private void preSubmit(){
        if(star5.isChecked()){
            star = 5;
        }else if(star4.isChecked()){
            star = 4;
        }else if(star3.isChecked()){
            star = 3;
        }else if(star2.isChecked()){
            star = 2;
        }else if(star1.isChecked()){
            star = 1;
        }
        StringBuffer ids = new StringBuffer();
        for (String s:preAppraiseList
             ) {
            ids.append(s+",");
        }
        if(ids.length()>0){
            ids.deleteCharAt(ids.length()-1);
        }
        if ("".equals(content.getText().toString().trim())){
            ToastUtil.show(AppraiseActivity.this,"请输入评价内容");
            return;
        }
        //ACCRETIONEVALUATESERVICECOMP服务商
        //ACCRETIONEVALUATE车商
        String url = "";
        if("car".equals(type)){
            url = Config.ACCRETIONEVALUATE;
        }else{
            url = Config.ACCRETIONEVALUATESERVICECOMP;
        }
        Log.i("appraise===",type+"---"+url+"----"+comp_id);
        if (NetUtil.isNetAvailable(getApplicationContext())) {
            OkHttpUtils.post()
                    .url(url)
                    .addHeader("user_token", Prefs.with(this).read("user_token"))
                    .addParams("log_to", comp_id)
                    .addParams("log_1", comp_name)//名字
                    .addParams("log_2", star+"")//评分
                    .addParams("log_3", ids.toString())//字典
                    .addParams("log_4", content.getText().toString().trim())//内容
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            ToastUtil.noNAR(AppraiseActivity.this);
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            Log.w("onResponse", "onResponse:" + response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getString("status").equals("1")) {
                                    ToastUtil.show(AppraiseActivity.this,"评价成功");
                                    JumpUtil.newInstance().finishRightTrans(AppraiseActivity.this);
                                } else {
                                    ToastUtil.noNAR(AppraiseActivity.this);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } else {
            ToastUtil.noNetAvailable(AppraiseActivity.this);
        }
    }
}
