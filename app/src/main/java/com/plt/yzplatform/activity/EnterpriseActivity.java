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
import com.plt.yzplatform.utils.OKhttptils;
import com.plt.yzplatform.utils.Prefs;
import com.plt.yzplatform.utils.ToastUtil;
import com.plt.yzplatform.view.AutoNextLineLinearlayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
        Log.i(TAG, "getData: " + "===============");
        Map<String, String> map = new HashMap<>();
        OKhttptils.post(EnterpriseActivity.this, Config.GETCOMP_INFO, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                Log.d(TAG, "onResponse获取数据: " + response);
                Gson gson = GsonFactory.create();
                Enterprise enterprise = gson.fromJson(response, Enterprise.class);
                Enterprise.DataBean dataBean = enterprise.getData();
                if (null == dataBean.getResult()) {
                    Log.e(TAG, "success: ！！！！" + "走的新增啊啊啊啊啊" );
                    //如果为空 只加载服务类型
                    getService1();
                    uploading.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Log.w(TAG, "onClick城市城市！！！: " + shi);
                            String t = type_id.toString().substring(1, type_id.toString().length() - 1);
                            String type = t.replace(" ", "");
                            file_id = Prefs.with(getApplicationContext()).read("营业执照");
                            head_file_id = Prefs.with(getApplicationContext()).read("门头照");
                            if (!compName.getText().toString().trim().isEmpty() && !lat.isEmpty() && !lon.isEmpty() && file_id != null
                                    && !linkName.getText().toString().trim().isEmpty() && !linkPhone.getText().toString().trim().isEmpty()
                                    && head_file_id != null && !type.isEmpty()) {
                                getCityId(shi.substring(0, shi.length() - 1));
                                Map<String, String> map = new HashMap<>();
                                map.put("auth_comp_name", compName.getText().toString().trim());
                                map.put("auth_comp_lon", lon);
                                map.put("auth_comp_lat", lat);
                                map.put("auth_comp_addr", compAddress.getText().toString().trim());
                                map.put("auth_comp_img_head_file_id", head_file_id);
                                map.put("auth_comp_file_id", file_id);
                                map.put("auth_comp_service_type", type);
                                map.put("auth_comp_phone", linkPhone.getText().toString().trim());
                                map.put("auth_comp_linkman", linkName.getText().toString().trim());
                                map.put("auth_comp_province", province_id);
                                map.put("auth_comp_city", city_id);
                                OKhttptils.post(EnterpriseActivity.this, Config.SUBMIT_COMPANY, map, new OKhttptils.HttpCallBack() {
                                    @Override
                                    public void success(String response) {
                                        Log.i(TAG, "success: " + response);
                                        ToastUtil.show(EnterpriseActivity.this, "提交成功");
                    /* 提交成功之后 界面不可修改 右上角有一个认证状态 */
                                        setRightText("正在审核");
                                        uploading.setVisibility(View.GONE);
                                    }

                                    @Override
                                    public void fail(String response) {
ToastUtil.noNAR(EnterpriseActivity.this);
                                    }
                                });
                            } else {
                                ToastUtil.show(EnterpriseActivity.this, "请填写完整后提交");
                            }
                        }
                    });

                } else {
                    Log.e(TAG, "success:！！！！ " + "走的修改啊啊啊啊" );

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
                    Prefs.with(getApplicationContext()).write("营业执照", yingye);
                    business.setText("已上传");
                    linkName.setText(resultBean.getAuth_comp_linkman());
                    linkPhone.setText(resultBean.getAuth_comp_phone());
                    String mentou = resultBean.getAuth_comp_img_head_file_id();
                    Prefs.with(getApplicationContext()).write("门头照", mentou);
                    headPic.setText("已上传");
                    updateType = resultBean.getAuth_comp_service_type();
                    lon = String.valueOf(resultBean.getAuth_comp_lon());
                    lat = String.valueOf(resultBean.getAuth_comp_lat());
                    province_id = resultBean.getAuth_comp_province();
                    city_id = resultBean.getAuth_comp_city();

                    String as[] = updateType.split(", ");
                    for (int i = 0; i < as.length; i++) {
                        type_id.add(as[i]);
                    }

                    getService2();
                    uploading.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            uploading2();
                        }
                    });
                }
            }

            @Override
            public void fail(String response) {
                Log.i(TAG, "fail: " + response);
                ToastUtil.noNAR(EnterpriseActivity.this);
            }
        });
//        if (NetUtil.isNetAvailable(this)) {
//            OkHttpUtils.get()
//                    .url(Config.GETCOMP_INFO)
//                    .addHeader("user_token", Prefs.with(getApplicationContext()).read("user_token"))
//                    .build()
//                    .execute(new StringCallback() {
//                        @Override
//                        public void onError(Call call, Exception e, int id) {
//                            ToastUtil.noNAR(EnterpriseActivity.this);
//                        }
//
//                        @Override
//                        public void onResponse(String response, int id) {
//                            Log.d(TAG, "onResponse获取数据: " + response);
//                            Gson gson = GsonFactory.create();
//                            Enterprise enterprise = gson.fromJson(response, Enterprise.class);
//                            if ("1".equals(enterprise.getStatus())) {
//                                Enterprise.DataBean dataBean = enterprise.getData();
//                                if (null == dataBean.getResult()) {
//                                    //如果为空 只加载服务类型
//                                    getService1();
//                                    uploading.setOnClickListener(new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View view) {
//                                            Log.w(TAG, "onClick城市城市！！！: " + shi  );
//                                            getCityId(shi.substring(0,shi.length()-1));
//                                            uploading1();
//                                        }
//                                    });
//                                } else {
//                                    //获取到数据 填充数据
//                                    Enterprise.DataBean.ResultBean resultBean = enterprise.getData().getResult();
//                                    //审核状态
//                                    if ("1".equals(resultBean.getAuth_audit_state())) {
//                                        setRightText("审核拒绝");
//                                        setEdit(compAddress, true);
//                                        setEdit(getLocation, true);
//                                        setEdit(compAddress, true);
//                                        setEdit(business, true);
//                                        setEdit(linkName, true);
//                                        setEdit(linkPhone, true);
//                                        setEdit(headPic, true);
//                                        uploading.setVisibility(View.VISIBLE);
//                                    } else if ("2".equals(resultBean.getAuth_audit_state())) {
//                                        setRightText("审核通过");
//                                        uploading.setVisibility(View.VISIBLE);
//                                    } else if ("3".equals(resultBean.getAuth_audit_state())) {
//                                        setRightText("正在审核");
//                                        setEdit(compAddress, true);
//                                        setEdit(getLocation, true);
//                                        setEdit(compAddress, true);
//                                        setEdit(business, true);
//                                        setEdit(linkName, true);
//                                        setEdit(linkPhone, true);
//                                        setEdit(headPic, true);
//                                        uploading.setVisibility(View.GONE);
//                                    }
//                                    //赋值
//                                    compName.setText(resultBean.getAuth_comp_name());
//                                    getLocation.setText(resultBean.getAuth_comp_lon() + "," + resultBean.getAuth_comp_lat());
//                                    compAddress.setText(resultBean.getAuth_comp_addr());
//                                    String yingye = resultBean.getAuth_comp_file_id();
//                                    Prefs.with(getApplicationContext()).write("营业执照",yingye);
//                                    business.setText("已上传");
//                                    linkName.setText(resultBean.getAuth_comp_linkman());
//                                    linkPhone.setText(resultBean.getAuth_comp_phone());
//                                    String mentou = resultBean.getAuth_comp_img_head_file_id();
//                                    Prefs.with(getApplicationContext()).write("门头照",mentou);
//                                    headPic.setText("已上传");
//                                    updateType = resultBean.getAuth_comp_service_type();
//                                    lon = String.valueOf(resultBean.getAuth_comp_lon());
//                                    lat = String.valueOf(resultBean.getAuth_comp_lat());
//                                    province_id = resultBean.getAuth_comp_province();
//                                    city_id = resultBean.getAuth_comp_city();
//
//                                    String as [] = updateType.split(", ");
//                                    for (int i = 0; i < as.length ; i++) {
//                                        type_id.add(as[i]);
//                                    }
//
//                                    getService2();
//                                    uploading.setOnClickListener(new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View view) {
//                                            uploading2();
//                                        }
//                                    });
//                                }
//                            }
//                        }
//                    });
//        } else {
//            ToastUtil.noNetAvailable(this);
//        }
    }



    /* 修改数据 */
    private void uploading2() {
        String t = type_id.toString().substring(1, type_id.toString().length() - 1);
        String type = t.replace(" ", "");
        file_id = Prefs.with(getApplicationContext()).read("营业执照");
        head_file_id = Prefs.with(getApplicationContext()).read("门头照");
        if (!compName.getText().toString().trim().isEmpty() && !lat.isEmpty() && !lon.isEmpty() && file_id != null
                && !linkName.getText().toString().trim().isEmpty() && !linkPhone.getText().toString().trim().isEmpty()
                && head_file_id != null && !type.isEmpty()) {
            Map<String, String> map = new HashMap<>();
            map.put("auth_comp_name", compName.getText().toString().trim());
            map.put("auth_comp_lon", lon);
            map.put("auth_comp_lat", lat);
            map.put("auth_comp_addr", compAddress.getText().toString().trim());
            map.put("auth_comp_img_head_file_id", head_file_id);
            map.put("auth_comp_file_id", file_id);
            map.put("auth_comp_service_type", type);
            map.put("auth_comp_phone", linkPhone.getText().toString().trim());
            map.put("auth_comp_linkman", linkName.getText().toString().trim());
            map.put("auth_comp_province", province_id);
            map.put("auth_comp_city", city_id);

            OKhttptils.post(EnterpriseActivity.this, Config.SUBMIT_COMPANY, map, new OKhttptils.HttpCallBack() {
                @Override
                public void success(String response) {
                    Log.d(TAG, "success: " + response);
//                    ToastUtil.show(EnterpriseActivity.this, "提交成功");
//                    /* 提交成功之后 界面不可修改 右上角有一个认证状态 */
//                    setRightText("正在审核");
//                    uploading.setVisibility(View.GONE);
                }

                @Override
                public void fail(String response) {
                    ToastUtil.noNAR(EnterpriseActivity.this);
                }
            });

//            if (NetUtil.isNetAvailable(this)) {
//                OkHttpUtils.post()
//                        .url(Config.SUBMIT_COMPANY)
//                        .addHeader("user_token", Prefs.with(getApplicationContext()).read("user_token"))
//                        .addParams("auth_comp_name", compName.getText().toString().trim())
//                        .addParams("auth_comp_lon", lon)
//                        .addParams("auth_comp_lat", lat)
//                        .addParams("auth_comp_addr", compAddress.getText().toString().trim())
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
//                                        setRightText("正在审核");
//                                        uploading.setVisibility(View.GONE);
//                                    }
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        });
//            } else {
//                ToastUtil.noNAR(this);
//            }

        } else {
            ToastUtil.show(EnterpriseActivity.this, "请填写完整后提交");
        }
    }

    /* 获取服务类型-新增时 */
    private void getService1() {
        Map<String, String> map = new HashMap<>();
        OKhttptils.post(EnterpriseActivity.this, Config.GETSERVERTYPE, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
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
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void fail(String response) {
                ToastUtil.noNAR(EnterpriseActivity.this);
            }
        });
//        if (NetUtil.isNetAvailable(this)) {
//            OkHttpUtils.post()
//                    .url(Config.GETSERVERTYPE)
//                    .addHeader("user_token", Prefs.with(getApplicationContext()).read("user_token"))
//                    .build()
//                    .execute(new StringCallback() {
//                        @Override
//                        public void onError(Call call, Exception e, int id) {
//                            ToastUtil.noNAR(EnterpriseActivity.this);
//                        }
//
//                        @Override
//                        public void onResponse(String response, int id) {
//                            Log.d(TAG, "onResponse获取数据: " + response);
//                            /**
//                             * {"data":
//                             * {"result":[
//                             *
//                             * {"dic_id":"YZcompbaoyang",
//                             * "dic_value":"保养"},
//                             *
//                             * {"dic_id":"YZcompmeirong",
//                             * "dic_value":"product_beauty"}]}
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
//                                        ids.add(o.getString("dic_id").trim());
//                                        types.add(o.getString("dic_value").trim());
//                                        addView1(parentLayout, types.get(i), ids.get(i));
//                                    }
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//
//                        }
//                    });
//        } else
//
//        {
//            ToastUtil.noNetAvailable(this);
//        }

    }

    /* 获取服务类型-修改时 */
    private void getService2() {
        Map<String, String> map = new HashMap<>();
        OKhttptils.post(EnterpriseActivity.this, Config.GETSERVERTYPE, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
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
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void fail(String response) {
                ToastUtil.noNAR(EnterpriseActivity.this);
            }
        });

//        if (NetUtil.isNetAvailable(this)) {
//            OkHttpUtils.post()
//                    .url(Config.GETSERVERTYPE)
//                    .addHeader("user_token", Prefs.with(getApplicationContext()).read("user_token"))
//                    .build()
//                    .execute(new StringCallback() {
//                        @Override
//                        public void onError(Call call, Exception e, int id) {
//                            ToastUtil.noNAR(EnterpriseActivity.this);
//                        }
//
//                        @Override
//                        public void onResponse(String response, int id) {
//                            Log.d(TAG, "onResponse获取数据: " + response);
//                            /**
//                             * {"data":
//                             * {"result":[
//                             *
//                             * {"dic_id":"YZcompbaoyang",
//                             * "dic_value":"保养"},
//                             *
//                             * {"dic_id":"YZcompmeirong",
//                             * "dic_value":"product_beauty"}]}
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
//                                        ids.add(o.getString("dic_id").trim());
//                                        types.add(o.getString("dic_value").trim());
//                                        addView2(parentLayout, types.get(i), ids.get(i));
//                                    }
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//
//                        }
//                    });
//        } else
//
//        {
//            ToastUtil.noNetAvailable(this);
//        }

    }

//    addView1(parentLayout, types.get(i), ids.get(i));
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
                    if (type_id.size() == 1) {
                        type_id.clear();
                    } else {
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
                    getCityId(shi.substring(0, shi.length() - 1));
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
        Map<String, String> map = new HashMap<>();
        map.put("cityName", s);
        OKhttptils.post(EnterpriseActivity.this, Config.GET_CITY_ID, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String data = jsonObject.getString("data");
                    JSONObject object = new JSONObject(data);
                    String result = object.getString("result");
                    JSONObject o = new JSONObject(result);
                    province_id = o.getString("province_id");
                    city_id = o.getString("city_id");
                    Log.e(TAG, "onResponse获取省！！！: " + province_id);
                    Log.i(TAG, "onResponse获取市！！！: " + city_id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void fail(String response) {
                ToastUtil.noNAR(EnterpriseActivity.this);
            }
        });
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
//                                    Log.e(TAG, "onResponse获取省！！！: " + province_id);
//                                    Log.i(TAG, "onResponse获取市！！！: " + city_id);
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
    }


}
