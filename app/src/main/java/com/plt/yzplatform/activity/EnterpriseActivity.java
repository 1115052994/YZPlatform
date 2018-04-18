package com.plt.yzplatform.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.plt.yzplatform.R;
import com.plt.yzplatform.base.BaseActivity;
import com.plt.yzplatform.config.Config;
import com.plt.yzplatform.entity.Enterprise;
import com.plt.yzplatform.gson.factory.GsonFactory;
import com.plt.yzplatform.utils.JumpUtil;
import com.plt.yzplatform.utils.NetUtil;
import com.plt.yzplatform.utils.Prefs;
import com.plt.yzplatform.utils.ToastUtil;
import com.plt.yzplatform.view.AutoNextLineLinearlayout;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/* 企业认证 */
public class EnterpriseActivity extends BaseActivity {

    private static final String TAG = "企业认证";
    @BindView(R.id.compName)
    EditText compName;
    @BindView(R.id.getLocation)
    TextView getLocation;
    @BindView(R.id.compAddress)
    EditText compAddress;
    @BindView(R.id.business)
    TextView business;
    @BindView(R.id.linkName)
    EditText linkName;
    @BindView(R.id.linkPhone)
    EditText linkPhone;
    @BindView(R.id.headPic)
    TextView headPic;
    @BindView(R.id.parent_layout)
    AutoNextLineLinearlayout parentLayout;
    @BindView(R.id.uploading)
    Button uploading;


    private String lon;//经度
    private String lat;//纬度
    private String address;//地址
    private String file_id;//营业执照id
    private String head_file_id;//门头照片

    private String sheng;//返回的省
    private String shi;//返回的市
    private List<String> types = new ArrayList<>();//存放服务类型的list
    private List<String> ids = new ArrayList<>();//存放服务类型id的list

    private List<String> type_id = new ArrayList<>();//点击的服务类型id
    private String updateType;

    private String province_id;
    private String city_id;

//    private String dic_id;
//    private String type;
//    private String updateType;
//    private List<CheckBox> boxList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enterprise);
        ButterKnife.bind(this);
        //获取企业认证数据
        getData();
    }

    /* 获取数据 */
    private void getData() {
        if (NetUtil.isNetAvailable(this)) {
            OkHttpUtils.post()
                    .url(Config.GETCOMP_INFO)
                    .addHeader("user_token", Prefs.with(getApplicationContext()).read("user_token"))
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            ToastUtil.noNAR(EnterpriseActivity.this);
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            Log.d(TAG, "onResponse获取数据: " + response);
                            Gson gson = GsonFactory.create();
                            Enterprise enterprise = gson.fromJson(response, Enterprise.class);
                            if ("1".equals(enterprise.getStatus())) {
                                Enterprise.DataBean dataBean = enterprise.getData();
                                if (null == dataBean.getResult()) {
                                    //如果为空 只加载服务类型
                                    getService1();
                                    uploading.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Log.w(TAG, "onClick城市城市！！！: " + shi  );
                                            getCityId(shi.substring(0,shi.length()-1));
                                            uploading1();
                                        }
                                    });
                                } else {
                                    //获取到数据 填充数据
                                    Enterprise.DataBean.ResultBean resultBean = enterprise.getData().getResult();
                                    //审核状态
                                    if ("1".equals(resultBean.getAuth_audit_state())) {
                                        setRightText("审核拒绝");
                                        setEdit(compAddress, true);
                                        setEdit(getLocation, true);
                                        setEdit(compAddress, true);
                                        setEdit(business, true);
                                        setEdit(linkName, true);
                                        setEdit(linkPhone, true);
                                        setEdit(headPic, true);
                                        uploading.setVisibility(View.VISIBLE);
                                    } else if ("2".equals(resultBean.getAuth_audit_state())) {
                                        setRightText("审核通过");
                                        uploading.setVisibility(View.VISIBLE);
                                    } else if ("3".equals(resultBean.getAuth_audit_state())) {
                                        setRightText("正在审核");
                                        setEdit(compAddress, true);
                                        setEdit(getLocation, true);
                                        setEdit(compAddress, true);
                                        setEdit(business, true);
                                        setEdit(linkName, true);
                                        setEdit(linkPhone, true);
                                        setEdit(headPic, true);
                                        uploading.setVisibility(View.GONE);
                                    }
                                    //赋值
                                    compName.setText(resultBean.getAuth_comp_name());
                                    getLocation.setText(resultBean.getAuth_comp_lon() + "," + resultBean.getAuth_comp_lat());
                                    compAddress.setText(resultBean.getAuth_comp_addr());
                                    String yingye = resultBean.getAuth_comp_file_id();
                                    Prefs.with(getApplicationContext()).write("营业执照",yingye);
                                    business.setText("已上传");
                                    linkName.setText(resultBean.getAuth_comp_linkman());
                                    linkPhone.setText(resultBean.getAuth_comp_phone());
                                    String mentou = resultBean.getAuth_comp_img_head_file_id();
                                    Prefs.with(getApplicationContext()).write("门头照",mentou);
                                    headPic.setText("已上传");
                                    updateType = resultBean.getAuth_comp_service_type();
                                    lon = String.valueOf(resultBean.getAuth_comp_lon());
                                    lat = String.valueOf(resultBean.getAuth_comp_lat());
                                    province_id = resultBean.getAuth_comp_province();
                                    city_id = resultBean.getAuth_comp_city();

//                                    type_id = StringUtil.stringToList(updateType);
                                    String as [] = updateType.split(", ");
                                    for (int i = 0; i < as.length ; i++) {
                                        type_id.add(as[i]);
                                    }

                                    Log.i(TAG, "onClick看看哪些服务类型: " +type_id.toString());

                                    getService2();
                                    uploading.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            uploading2();
                                        }
                                    });
                                }
                            }
                        }
                    });
        } else {
            ToastUtil.noNetAvailable(this);
        }
    }


    /* 修改数据 */
    private void uploading1() {
//        String type = StringUtil.listToString(type_id);
        String t = type_id.toString().substring(1,type_id.toString().length()-1);
        String type = t.replace(" ","");
        Log.i(TAG, "uploading1集合长度: " + type_id.size());
        file_id = Prefs.with(getApplicationContext()).read("营业执照");
        head_file_id = Prefs.with(getApplicationContext()).read("门头照");
        Log.e(TAG, "uploading1公司名！！！: " +compName.getText().toString().trim() );
        Log.i(TAG, "uploading1纬度！！！: " + lat);
        Log.d(TAG, "uploading1: 经度！！！"+lon);
        Log.w(TAG, "uploading1: 营业执照id！！！" + file_id );
        Log.e(TAG, "uploading1:门头照id！！！ " + head_file_id );
        Log.i(TAG, "uploading1服务类型！！！: " + type);
        Log.d(TAG, "uploading1省！！！: " + province_id);
        Log.w(TAG, "uploading1市！！！: " + city_id );

        if (!compName.getText().toString().trim().isEmpty() && !lat.isEmpty() && !lon.isEmpty() && file_id != null
                && !linkName.getText().toString().trim().isEmpty() && !linkPhone.getText().toString().trim().isEmpty()
                && head_file_id != null && !type.isEmpty()) {
            if (NetUtil.isNetAvailable(this)) {
                OkHttpUtils.post()
                        .url(Config.SUBMIT_COMPANY)
                        .addHeader("user_token", Prefs.with(getApplicationContext()).read("user_token"))
                        .addParams("auth_comp_name",compName.getText().toString().trim())
                        .addParams("auth_comp_lon",lon)
                        .addParams("auth_comp_lat",lat)
                        .addParams("auth_comp_addr",compAddress.getText().toString().trim())
                        .addParams("auth_comp_img_head_file_id",head_file_id)
                        .addParams("auth_comp_file_id",file_id)
                        .addParams("auth_comp_service_type",type)
                        .addParams("auth_comp_phone",linkPhone.getText().toString().trim())
                        .addParams("auth_comp_linkman",linkName.getText().toString().trim())
                        .addParams("auth_comp_province",province_id)
                        .addParams("auth_comp_city",city_id)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                ToastUtil.noNAR(EnterpriseActivity.this);
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                Log.d(TAG, "onResponse提交企业认证: " + response);
                                /**
                                 * {"data":{},"message":"","status":"1"}
                                 * */
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if ("1".equals(jsonObject.getString("status"))) {
                                        ToastUtil.show(EnterpriseActivity.this, "提交成功");
                                        /* 提交成功之后 界面不可修改 右上角有一个认证状态 */
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
            } else {
                ToastUtil.noNAR(this);
            }

        }else {
            ToastUtil.show(EnterpriseActivity.this,"请填写完整后提交");
        }
    }


    /* 修改数据 */
    private void uploading2() {
//        String type = StringUtil.listToString(type_id);
        String t = type_id.toString().substring(1,type_id.toString().length()-1);
        String type = t.replace(" ","");
        Log.i(TAG, "uploading1集合长度: " + type_id.size());
        file_id = Prefs.with(getApplicationContext()).read("营业执照");
        head_file_id = Prefs.with(getApplicationContext()).read("门头照");
        Log.e(TAG, "uploading1公司名！！！: " +compName.getText().toString().trim() );
        Log.i(TAG, "uploading1纬度！！！: " + lat);
        Log.d(TAG, "uploading1: 经度！！！"+lon);
        Log.w(TAG, "uploading1: 营业执照id！！！" + file_id );
        Log.e(TAG, "uploading1:门头照id！！！ " + head_file_id );
        Log.i(TAG, "uploading1服务类型！！！: " + type);

        if (!compName.getText().toString().trim().isEmpty() && !lat.isEmpty() && !lon.isEmpty() && file_id != null
                && !linkName.getText().toString().trim().isEmpty() && !linkPhone.getText().toString().trim().isEmpty()
                && head_file_id != null && !type.isEmpty()) {
            if (NetUtil.isNetAvailable(this)) {
                OkHttpUtils.post()
                        .url(Config.SUBMIT_COMPANY)
                        .addHeader("user_token", Prefs.with(getApplicationContext()).read("user_token"))
                        .addParams("auth_comp_name",compName.getText().toString().trim())
                        .addParams("auth_comp_lon",lon)
                        .addParams("auth_comp_lat",lat)
                        .addParams("auth_comp_addr",compAddress.getText().toString().trim())
                        .addParams("auth_comp_img_head_file_id",head_file_id)
                        .addParams("auth_comp_file_id",file_id)
                        .addParams("auth_comp_service_type",type)
                        .addParams("auth_comp_phone",linkPhone.getText().toString().trim())
                        .addParams("auth_comp_linkman",linkName.getText().toString().trim())
                        .addParams("auth_comp_province",province_id)
                        .addParams("auth_comp_city",city_id)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                ToastUtil.noNAR(EnterpriseActivity.this);
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                Log.d(TAG, "onResponse提交企业认证: " + response);
                                /**
                                 * {"data":{},"message":"","status":"1"}
                                 * */
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if ("1".equals(jsonObject.getString("status"))) {
                                        ToastUtil.show(EnterpriseActivity.this, "提交成功");
                                        /* 提交成功之后 界面不可修改 右上角有一个认证状态 */
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
            } else {
                ToastUtil.noNAR(this);
            }

        }else {
            ToastUtil.show(EnterpriseActivity.this,"请填写完整后提交");
        }
    }

    /* 获取服务类型-新增时 */
    private void getService1() {

        if (NetUtil.isNetAvailable(this)) {
            OkHttpUtils.post()
                    .url(Config.GETSERVERTYPE)
                    .addHeader("user_token", Prefs.with(getApplicationContext()).read("user_token"))
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            ToastUtil.noNAR(EnterpriseActivity.this);
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            Log.d(TAG, "onResponse获取数据: " + response);
                            /**
                             * {"data":
                             * {"result":[
                             *
                             * {"dic_id":"YZcompbaoyang",
                             * "dic_value":"保养"},
                             *
                             * {"dic_id":"YZcompmeirong",
                             * "dic_value":"美容"}]}
                             *
                             * ,"message":"",
                             * "status":"1"}
                             * */
                            try {

                                JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    String data = jsonObject.getString("data");
                                    JSONObject object = new JSONObject(data);
                                    String result = object.getString("result");
                                    JSONArray jsonArray = new JSONArray(result);
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject o = jsonArray.getJSONObject(i);
                                        ids.add(o.getString("dic_id").trim());
                                        types.add(o.getString("dic_value").trim());
                                        addView1(parentLayout, types.get(i), ids.get(i));
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
        } else

        {
            ToastUtil.noNetAvailable(this);
        }

    }

    /* 获取服务类型-修改时 */
    private void getService2() {

        if (NetUtil.isNetAvailable(this)) {
            OkHttpUtils.post()
                    .url(Config.GETSERVERTYPE)
                    .addHeader("user_token", Prefs.with(getApplicationContext()).read("user_token"))
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            ToastUtil.noNAR(EnterpriseActivity.this);
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            Log.d(TAG, "onResponse获取数据: " + response);
                            /**
                             * {"data":
                             * {"result":[
                             *
                             * {"dic_id":"YZcompbaoyang",
                             * "dic_value":"保养"},
                             *
                             * {"dic_id":"YZcompmeirong",
                             * "dic_value":"美容"}]}
                             *
                             * ,"message":"",
                             * "status":"1"}
                             * */
                            try {

                                JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    String data = jsonObject.getString("data");
                                    JSONObject object = new JSONObject(data);
                                    String result = object.getString("result");
                                    JSONArray jsonArray = new JSONArray(result);
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject o = jsonArray.getJSONObject(i);
                                        ids.add(o.getString("dic_id").trim());
                                        types.add(o.getString("dic_value").trim());
                                        addView2(parentLayout, types.get(i), ids.get(i));
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
        } else

        {
            ToastUtil.noNetAvailable(this);
        }

    }

//    /* 获取企业认证信息 */
//    private void getData() {
//        if (NetUtil.isNetAvailable(this)) {
//            OkHttpUtils.post()
//                    .url(Config.GETCOMP_INFO)
//                    .addHeader("user_token",Prefs.with(getApplicationContext()).read("user_token"))
//                    .build()
//                    .execute(new StringCallback() {
//                        @Override
//                        public void onError(Call call, Exception e, int id) {
//                            ToastUtil.noNAR(EnterpriseActivity.this);
//                        }
//
//                        @Override
//                        public void onResponse(String response, int id) {
//                            Log.e(TAG, "onResponse获取数据: " + response);
//                            Gson gson = GsonFactory.create();
//                            Enterprise enterprise = gson.fromJson(response, Enterprise.class);
//                            if ("1".equals(enterprise.getStatus())) {
//                                Enterprise.DataBean dataBean = enterprise.getData();
//                                if (null == dataBean.getResult()) {
//                                    //获取服务类型
//                                    getService();
//                                    //没有获取到数据 则新添加数据
//
//                                } else {
//                                    //获取到数据 填充数据
//                                    Enterprise.DataBean.ResultBean resultBean = enterprise.getData().getResult();
//                                    if ("1".equals(resultBean.getAuth_audit_state())) {
//                                        setRightText("审核拒绝");
//                                        setEdit(compAddress, true);
//                                        setEdit(getLocation, true);
//                                        setEdit(compAddress, true);
//                                        setEdit(business, true);
//                                        setEdit(linkName, true);
//                                        setEdit(linkPhone, true);
//                                        setEdit(headPic, true);
//                                        for (int i = 0; i < boxList.size(); i++) {
//                                            boxList.get(i).setEnabled(true);
//                                            boxList.get(i).setClickable(true);
//                                        }
////                                        uploading.setClickable(false);
//                                        uploading.setVisibility(View.VISIBLE);
//                                    } else if ("2".equals(resultBean.getAuth_audit_state())) {
//                                        setRightText("审核通过");
//                                        uploading.setClickable(true);
//                                    } else if ("3".equals(resultBean.getAuth_audit_state())) {
//
//                                        setRightText("正在审核");
//                                        setEdit(compName, false);
//                                        setEdit(getLocation, false);
//                                        setEdit(compAddress, false);
//                                        setEdit(business, false);
//                                        setEdit(linkName, false);
//                                        setEdit(linkPhone, false);
//                                        setEdit(headPic, false);
////                                        uploading.setClickable(false);
//                                        uploading.setVisibility(View.GONE);
//                                    }
//                                    compName.setText(resultBean.getAuth_comp_name());
//                                    getLocation.setText(resultBean.getAuth_comp_lon() + "," + resultBean.getAuth_comp_lat());
//                                    compAddress.setText(resultBean.getAuth_comp_addr());
//                                    if (!resultBean.getAuth_comp_file_id().isEmpty()) {
//                                        file_id = resultBean.getAuth_comp_file_id();
//                                        Prefs.with(getApplicationContext()).write("营业执照", file_id);
//                                        business.setText("已上传");
//                                    } else {
//
//                                    }
//
//                                    province_id = resultBean.getAuth_comp_province();
//                                    city_id = resultBean.getAuth_comp_city();
//
//                                    linkName.setText(resultBean.getAuth_comp_linkman());
//                                    linkPhone.setText(resultBean.getAuth_comp_phone());
//                                    lon = String.valueOf(resultBean.getAuth_comp_lon());
//                                    lat = String.valueOf(resultBean.getAuth_comp_lat());
//                                    if (!resultBean.getAuth_comp_img_head_file_id().isEmpty()) {
//                                        head_file_id = resultBean.getAuth_comp_img_head_file_id();
//                                        Prefs.with(getApplicationContext()).write("门头照", head_file_id);
//                                        headPic.setText("已上传");
//                                    }
//
//                                    //获取到填写的服务类型  与所有服务类型做比较 找出相同的
//                                    updateType = resultBean.getAuth_comp_service_type();
//                                    getService();
//
//
//                                }
//                            }
//                        }
//                    });
//        } else {
//            ToastUtil.noNetAvailable(this);
//        }
//    }
//
    /**/
//    /* 获取服务类型 */
//    private void getService() {
//        if (NetUtil.isNetAvailable(this)) {
//            OkHttpUtils.post()
//                    .url(Config.GETSERVERTYPE)
//                    .addHeader("user_token", Prefs.with(getApplicationContext()).read("user_token"))
//                    .build()
//                    .execute(new StringCallback() {
//                        private CheckBox item_service_type;
//
//                        @Override
//                        public void onError(Call call, Exception e, int id) {
//                            ToastUtil.noNAR(EnterpriseActivity.this);
//                        }
//
//                        @Override
//                        public void onResponse(String response, int id) {
//                            Log.e(TAG, "onResponse获取服务类型: " + response);
//                            /**
//                             * {"data":
//                             * {"result":[
//                             *
//                             * {"dic_id":"YZcompbaoyang",
//                             * "dic_value":"保养"},
//                             *
//                             * {"dic_id":"YZcompmeirong",
//                             * "dic_value":"美容"}]}
//                             *
//                             * ,"message":"",
//                             * "status":"1"}
//                             * */
//                            try {
//
//                                JSONObject jsonObject = new JSONObject(response);
//                                if ("1".equals(jsonObject.getString("status"))) {
//                                    String data = jsonObject.getString("data");
//                                    JSONObject object = new JSONObject(data);
//                                    String result = object.getString("result");
//                                    JSONArray jsonArray = new JSONArray(result);
//                                    for (int i = 0; i < jsonArray.length(); i++) {
//                                        JSONObject o = jsonArray.getJSONObject(i);
//                                        dic_id = o.getString("dic_id").trim();
//                                        type = o.getString("dic_value").trim();
//                                        ids.add(dic_id);
//                                        types.add(type);
//                                        addView(parentLayout, type, ids.get(i));
//                                    }
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//
//                        }
//                    });
//        } else {
//            ToastUtil.noNAR(this);
//        }
//
//    }
//
//

    private void addView1(LinearLayout linearLayout, final String s, final String id) {
        final CheckBox item = (CheckBox) getLayoutInflater().inflate(R.layout.item_service_types, linearLayout, false);
        item.setText(s);
        item.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (item.isChecked()) {
                    id.trim();
                    type_id.add(id);
                } else {
                    id.trim();
                    type_id.remove(id);
                }
            }
        });
        linearLayout.addView(item);
    }

    private void addView2(LinearLayout linearLayout, final String s, final String id) {

        final CheckBox item = (CheckBox) getLayoutInflater().inflate(R.layout.item_service_types, linearLayout, false);
        item.setText(s);
        item.setChecked(updateType.contains(id));
        item.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (item.isChecked()) {
                    id.trim();
                    type_id.add(id);
                } else {
                    id.trim();
                    if (type_id.size() == 1){
                        type_id.clear();
                    }else {
                        type_id.remove(id);
                    }
                }
            }
        });
        linearLayout.addView(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setTitle("商家详情");
        setLeftImageClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JumpUtil.newInstance().finishRightTrans(EnterpriseActivity.this);
            }
        });
        getUserToken(this);
    }

    @OnClick({R.id.getLocation, R.id.business, R.id.headPic, R.id.uploading})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.getLocation:
                JumpUtil.newInstance().jumpRight(EnterpriseActivity.this, MapActivity.class, 001);
                break;
            case R.id.business:
                JumpUtil.newInstance().jumpRight(EnterpriseActivity.this, BusinessActivity.class, 002);
                break;
            case R.id.headPic:
                JumpUtil.newInstance().jumpRight(EnterpriseActivity.this, HeadPictureActivity.class, 003);
                break;
        }
    }

//
//    @OnClick({R.id.getLocation, R.id.business, R.id.headPic, R.id.uploading})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.getLocation:
//                JumpUtil.newInstance().jumpRight(this, MapActivity.class, 001);
//                break;
//            case R.id.business:
//                JumpUtil.newInstance().jumpRight(this, BusinessActivity.class, 002);
//                break;
//            case R.id.headPic:
//                JumpUtil.newInstance().jumpRight(this, HeadPictureActivity.class, 003);
//                break;
//            case R.id.uploading:
//                if (null == sheng && null == shi) {
//                    type = updateType;
//                    upLoading();
//                } else {
////                    String s = type_id.toString();
////                    type = s.substring(1, s.length() - 1);
////                    String str = shi.substring(0, shi.length() - 1);
////                    getCityId(str);
////                    Log.w(TAG, "upLoading是不是: " + city_id);
////                    Log.e(TAG, "upLoading是不是: " + province_id);
//                }
//
//                break;
//        }
//    }
//
//    /* 获取省市id */
//    private void getCityId(String s) {
//        Log.d(TAG, "getCityId是否为空: " + s);
//        if (NetUtil.isNetAvailable(this)) {
//            OkHttpUtils.post()
//                    .url(Config.GET_CITY_ID)
//                    .addHeader("user_token", Prefs.with(getApplicationContext()).read("user_token"))
//                    .addParams("cityName", s)
//                    .build()
//                    .execute(new StringCallback() {
//                        @Override
//                        public void onError(Call call, Exception e, int id) {
//                            ToastUtil.noNAR(EnterpriseActivity.this);
//                        }
//
//                        @Override
//                        public void onResponse(String response, int id) {
//                            Log.w(TAG, "onResponse获取省市id: " + response);
//                            /**
//                             * {"data":{"result":{"province_id":"shandong","city_id":"jinan"}},"message":"","status":"1"}
//                             */
//                            try {
//                                JSONObject jsonObject = new JSONObject(response);
//                                if ("1".equals(jsonObject.getString("status"))) {
//                                    String data = jsonObject.getString("data");
//                                    JSONObject object = new JSONObject(data);
//                                    String result = object.getString("result");
//                                    JSONObject o = new JSONObject(result);
//                                    province_id = o.getString("province_id");
//                                    city_id = o.getString("city_id");
//                                    if (null != province_id && null != city_id) {
//                                        upLoading();
//                                    }
//                                } else {
//                                    ToastUtil.show(EnterpriseActivity.this, jsonObject.getString("message"));
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    });
//
//        } else {
//            ToastUtil.noNetAvailable(this);
//        }
//    }
//
//    /* 提交 */
//    private void upLoading() {
//        Log.i(TAG, "upLoading: " + lon);
//        Log.d(TAG, "upLoading: " + lat);
//        Log.w(TAG, "upLoading: " + city_id);
//        Log.e(TAG, "upLoading: " + province_id);
//        Log.i(TAG, "upLoading: " + type);
//        file_id = Prefs.with(getApplicationContext()).read("营业执照");
//        head_file_id = Prefs.with(getApplicationContext()).read("门头照");
//        if (!compName.getText().toString().trim().isEmpty() && !lat.isEmpty() && !lon.isEmpty() && file_id != null
//                && !linkName.getText().toString().trim().isEmpty() && !linkPhone.getText().toString().trim().isEmpty()
//                && head_file_id != null && !type.isEmpty()) {
//            if (NetUtil.isNetAvailable(this)) {
//                OkHttpUtils.post()
//                        .url(Config.SUBMIT_COMPANY)
//                        .addHeader("user_token", Prefs.with(getApplicationContext()).read("user_token"))
//                        .addParams("auth_comp_name", compName.getText().toString().trim())
//                        .addParams("auth_comp_lon", lon)
//                        .addParams("auth_comp_lat", lat)
//                        .addParams("auth_comp_addr", address)
//                        .addParams("auth_comp_img_head_file_id", head_file_id)
//                        .addParams("auth_comp_file_id", file_id)
//                        .addParams("auth_comp_service_type", type)
//                        .addParams("auth_comp_phone", linkPhone.getText().toString().trim())
//                        .addParams("auth_comp_linkman", linkName.getText().toString().trim())
//                        .addParams("auth_comp_province", province_id)
//                        .addParams("auth_comp_city", city_id)
//                        .build()
//                        .execute(new StringCallback() {
//                            @Override
//                            public void onError(Call call, Exception e, int id) {
//                                ToastUtil.noNAR(EnterpriseActivity.this);
//                            }
//
//                            @Override
//                            public void onResponse(String response, int id) {
//                                Log.d(TAG, "onResponse提交企业认证: " + response);
//                                /**
//                                 * {"data":{},"message":"","status":"1"}
//                                 * */
//                                try {
//                                    JSONObject jsonObject = new JSONObject(response);
//                                    if ("1".equals(jsonObject.getString("status"))) {
//                                        ToastUtil.show(EnterpriseActivity.this, "提交成功");
//                                        /* 提交成功之后 界面不可修改 右上角有一个认证状态 */
//                                    }
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        });
//            } else {
//                ToastUtil.noNAR(this);
//            }
//
//        }
//    }
//

    /* 设置可编辑状态与不可编辑状态 */
    private void setEdit(TextView edit, boolean b) {
        edit.setFocusable(b);
        edit.setFocusableInTouchMode(b);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            Bundle bundle = data.getExtras();
            switch (requestCode) {
                case 001:
                    lat = bundle.getString("纬度");
                    lon = bundle.getString("经度");
                    address = bundle.getString("address");
                    getLocation.setText(lon + "," + lat);
                    compAddress.setText(address);
//                    sheng = bundle.getString("省");
                    shi = bundle.getString("市");
                    getCityId(shi.substring(0,shi.length()-1));
                    break;
                case 002:
                    file_id = bundle.getString("file_id");
                    business.setText("上传成功");
                    break;
                case 003:
                    head_file_id = bundle.getString("head_file_id");
                    headPic.setText("上传成功");
                    break;
            }
        } else {

        }
    }

    /* 获取省市id */
    private void getCityId(String s) {
        if (NetUtil.isNetAvailable(this)) {
            OkHttpUtils.post()
                    .url(Config.GET_CITY_ID)
                    .addHeader("user_token", Prefs.with(getApplicationContext()).read("user_token"))
                    .addParams("cityName", s)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            ToastUtil.noNAR(EnterpriseActivity.this);
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            Log.w(TAG, "onResponse获取省市id: " + response);
                            /**
                             * {"data":{"result":{"province_id":"shandong","city_id":"jinan"}},"message":"","status":"1"}
                             */
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))) {
                                    String data = jsonObject.getString("data");
                                    JSONObject object = new JSONObject(data);
                                    String result = object.getString("result");
                                    JSONObject o = new JSONObject(result);
                                    province_id = o.getString("province_id");
                                    city_id = o.getString("city_id");
                                    Log.e(TAG, "onResponse获取省！！！: " + province_id );
                                    Log.i(TAG, "onResponse获取市！！！: " +city_id);
//                                    Prefs.with(getApplicationContext()).write("省id",province_id);
//                                    Prefs.with(getApplicationContext()).write("市id",city_id);
                                } else {
                                    ToastUtil.show(EnterpriseActivity.this, jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

        } else {
            ToastUtil.noNetAvailable(this);
        }
    }


}
