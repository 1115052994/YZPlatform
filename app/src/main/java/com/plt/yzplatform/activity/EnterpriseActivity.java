package com.plt.yzplatform.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private String lon;//经度
    private String lat;//纬度
    private String address;//地址
    private String file_id;//营业执照id
    private String head_file_id;//门头照片
    private List<String> type_id = new ArrayList<>();//服务类型
    private List<String> types = new ArrayList<>();
    private List<String> ids = new ArrayList<>();
    private String dic_id;
    private String type;
    private String sheng;
    private String shi;
    private String province_id;
    private String city_id;
    private Map<String,Boolean> map = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enterprise);
        ButterKnife.bind(this);
        //获取企业认证数据
        getData();

    }

    /* 获取企业认证信息 */
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
                            Log.e(TAG, "onResponse获取数据: " + response);
                            Gson gson = GsonFactory.create();
                            Enterprise enterprise = gson.fromJson(response, Enterprise.class);
                            if ("1".equals(enterprise.getStatus())) {
                                Enterprise.DataBean dataBean = enterprise.getData();
                                if (null == dataBean.getResult()) {
                                    //获取服务类型
                                    getService();
                                    //没有获取到数据 则新添加数据

                                } else {
                                    //获取到数据 填充数据
                                    Enterprise.DataBean.ResultBean resultBean = enterprise.getData().getResult();
                                    compName.setText(resultBean.getAuth_comp_name());
                                    getLocation.setText(resultBean.getAuth_comp_lon() + "," + resultBean.getAuth_comp_lat());
                                    compAddress.setText(resultBean.getAuth_comp_addr());
                                    if (!resultBean.getAuth_comp_file_id().isEmpty()) {
                                        file_id = resultBean.getAuth_comp_file_id();
                                        Prefs.with(getApplicationContext()).write("营业执照",file_id);
                                        business.setText("已上传");
                                    } else {

                                    }

                                    linkName.setText(resultBean.getAuth_comp_linkman());
                                    linkPhone.setText(resultBean.getAuth_comp_phone());
                                    if (!resultBean.getAuth_comp_img_head_file_id().isEmpty()) {
                                        head_file_id = resultBean.getAuth_comp_img_head_file_id();
                                        Prefs.with(getApplicationContext()).write("门头照",head_file_id);
                                        headPic.setText("已上传");
                                    }
                                    //获取到填写的服务类型  与所有服务类型做比较 找出相同的
                                    type = resultBean.getAuth_comp_service_type();
                                    String as[] = type.split(",");
                                    for (int i = 0; i < as.length ; i++) {
                                        String s = as[i];
                                        for (int j = 0; j < ids.size() ; j++) {
                                            if (s.equals(ids.get(j))){
                                                Log.i(TAG, "onResponse: " + j);

                                            }
                                        }
                                    }
//                                    List<String> list = new ArrayList<>();
//                                    for (int i = 0; i < as.length; i++) {
//                                        list.add(as[i]);
//                                    }
//                                    Collection exists = new ArrayList<String>(list);
//                                    Collection notexists = new ArrayList<String>(list);
//                                    exists.remove(ids);
//                                    //去掉不同的项
//                                    notexists.remove(exists);
                                }
                            }
                        }
                    });
        } else {
            ToastUtil.noNetAvailable(this);
        }
    }

    /* 获取服务类型 */
    private void getService() {
        if (NetUtil.isNetAvailable(this)) {
            OkHttpUtils.post()
                    .url(Config.GETSERVERTYPE)
                    .addHeader("user_token", Prefs.with(getApplicationContext()).read("user_token"))
                    .build()
                    .execute(new StringCallback() {
                        private CheckBox item_service_type;

                        @Override
                        public void onError(Call call, Exception e, int id) {
                            ToastUtil.noNAR(EnterpriseActivity.this);
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            Log.e(TAG, "onResponse: " + response);
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
                                        dic_id = o.getString("dic_id");
                                        type = o.getString("dic_value");
                                        ids.add(dic_id);
                                        types.add(type);
                                    }
                                    for (int i = 0; i < types.size(); i++) {
                                        type = types.get(i);
                                        addView(parentLayout, type, ids.get(i),false);

                                    }

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
        } else {
            ToastUtil.noNAR(this);
        }

    }

    private void addView(LinearLayout linearLayout, final String s, final String id,boolean isC) {
        final CheckBox item = (CheckBox) getLayoutInflater().inflate(R.layout.item_service_types, linearLayout, false);
        map.put(id,false);
        item.setText(s);
        item.setChecked(map.get(id));
        item.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (item.isChecked()) {
                    type_id.add(id);
                    map.put(id,true);
                    Log.d(TAG, "onCheckedChanged: " + map.toString());
                } else {
                    type_id.remove(id);
                    map.put(id,false);
                    Log.d(TAG, "onCheckedChanged: " + map.toString());
                }
            }
        });
        linearLayout.addView(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setTitle("企业认证");
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
                JumpUtil.newInstance().jumpRight(this, MapActivity.class, 001);
                break;
            case R.id.business:
                JumpUtil.newInstance().jumpRight(this, BusinessActivity.class, 002);
                break;
            case R.id.headPic:
                JumpUtil.newInstance().jumpRight(this, HeadPictureActivity.class, 003);
                break;
            case R.id.uploading:
                String s = type_id.toString();
                type = s.substring(1, s.length() - 1);
                getCityId();
//                upLoading();
                break;
        }
    }

    /* 获取省市id */
    private void getCityId() {
        Log.w(TAG, "getCityId省市id: " + shi.substring(0,shi.length()-1) );
        if (NetUtil.isNetAvailable(this)){
            OkHttpUtils.post()
                    .url(Config.GET_CITY_ID)
                    .addHeader("user_token",Prefs.with(getApplicationContext()).read("user_token"))
                    .addParams("cityName",shi.substring(0,shi.length()-1))
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            ToastUtil.noNAR(EnterpriseActivity.this);
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            Log.i(TAG, "onResponse获取省市id: " + response);
                            /**
                             * {"data":{"result":{"province_id":"shandong","city_id":"jinan"}},"message":"","status":"1"}
                             */
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))){
                                    String data = jsonObject.getString("data");
                                    JSONObject object = new JSONObject(data);
                                    String result = object.getString("result");
                                    JSONObject o = new JSONObject(result);
                                    province_id = o.getString("province_id");
                                    city_id = o.getString("city_id");
                                    upLoading();
                                }else {
                                    ToastUtil.show(EnterpriseActivity.this,jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

        }else {
            ToastUtil.noNetAvailable(this);
        }
    }

    /* 提交 */
    private void upLoading() {
        file_id = Prefs.with(getApplicationContext()).read("营业执照");
        head_file_id = Prefs.with(getApplicationContext()).read("门头照");
        Log.w(TAG, "upLoading服务类型: " + type);
        Log.i(TAG, "upLoading营业执照: " + file_id);
        Log.e(TAG, "upLoading门头照: " + head_file_id);
        if (!compName.getText().toString().trim().isEmpty() && !lat.isEmpty() && !lon.isEmpty() && file_id != null
                && !linkName.getText().toString().trim().isEmpty() && !linkPhone.getText().toString().trim().isEmpty()
                && head_file_id != null && !type.isEmpty()) {
            if (NetUtil.isNetAvailable(this)) {
                OkHttpUtils.post()
                        .url(Config.SUBMIT_COMPANY)
                        .addHeader("user_token", Prefs.with(getApplicationContext()).read("user_token"))
                        .addParams("auth_comp_name", compName.getText().toString().trim())
                        .addParams("auth_comp_lon", lon)
                        .addParams("auth_comp_lat", lat)
                        .addParams("auth_comp_addr", address)
                        .addParams("auth_comp_img_head_file_id", head_file_id)
                        .addParams("auth_comp_file_id", file_id)
                        .addParams("auth_comp_service_type", type)
                        .addParams("auth_comp_phone", linkPhone.getText().toString().trim())
                        .addParams("auth_comp_linkman", linkName.getText().toString().trim())
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

        }
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
                    sheng = bundle.getString("省");
                    shi = bundle.getString("市");
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

}
