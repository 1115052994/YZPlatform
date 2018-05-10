package com.xtzhangbinbin.jpq.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.base.BaseActivity;
import com.xtzhangbinbin.jpq.config.Config;
import com.xtzhangbinbin.jpq.fragment.main.CarBuyFragment;
import com.xtzhangbinbin.jpq.utils.JumpUtil;
import com.xtzhangbinbin.jpq.utils.NetUtil;
import com.xtzhangbinbin.jpq.utils.OKhttptils;
import com.xtzhangbinbin.jpq.utils.PhotoUtils;
import com.xtzhangbinbin.jpq.utils.ToastUtil;
import com.xtzhangbinbin.jpq.view.MyProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WeizhangQuery extends BaseActivity {

    @BindView(R.id.tv_editcity)
    TextView tvEditcity;
    @BindView(R.id.tv_editcph)
    EditText tvEditcph;
    @BindView(R.id.tv_editfdjh)
    EditText tvEditfdjh;
    @BindView(R.id.tv_editcjh)
    EditText tvEditcjh;
    private String cityId = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weizhang_query);
        ButterKnife.bind(this);
        initView();
        getIsCamera();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setTitle("违章查询");
        setLeftImageClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JumpUtil.newInstance().finishRightTrans(WeizhangQuery.this);
            }
        });
    }

    private void initView() {

    }

    @OnClick({R.id.city, R.id.tv_cph_image, R.id.tv_fdjh_image, R.id.tv_cjh_image, R.id.ly_wzcx})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.city:
                JumpUtil.newInstance().jumpLeft(this, WeizhangCity.class);
                WeizhangCity.setOnBackListener(new WeizhangCity.BackListener() {
                    @Override
                    public void backData(String id,String cityName) {
                        tvEditcity.setText(cityName);
                        tvEditcity.setTextColor(Color.parseColor("#333333"));
                        cityId = id;
                        Log.i("cityId",cityId);
                    }
                });
                break;
            case R.id.tv_cph_image:
                if (ContextCompat.checkSelfPermission(WeizhangQuery.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    ActivityCompat.requestPermissions(WeizhangQuery.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                } else {
                    //有权限，直接拍照
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// 启动系统相机
                    startActivityForResult(intent, 1);
                }
                break;
            case R.id.tv_fdjh_image:
                if (ContextCompat.checkSelfPermission(WeizhangQuery.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    ActivityCompat.requestPermissions(WeizhangQuery.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                } else {
                    //有权限，直接拍照
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// 启动系统相机
                    startActivityForResult(intent, 1);
                }
                break;
            case R.id.tv_cjh_image:
                if (ContextCompat.checkSelfPermission(WeizhangQuery.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    ActivityCompat.requestPermissions(WeizhangQuery.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                } else {
                    //有权限，直接拍照
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// 启动系统相机
                    startActivityForResult(intent, 0);
                }
                break;
            case R.id.ly_wzcx:
                // 违章查询
                getResult();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 2) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //获取权限成功，可以拍照
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// 启动系统相机
                startActivityForResult(intent, 0);
            } else {
                Toast.makeText(this, "请在应用管理中打开“相机”访问权限！", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && (requestCode == 0||requestCode==1)) { // 如果返回数据
            Bitmap data1 = (Bitmap) data.getExtras().get("data");
            String base64 = PhotoUtils.bitmapToBase64(data1);
            Map<String, String> map = new HashMap<>();
            if (NetUtil.isNetAvailable(this)) {
                map.put("imgBase64", base64);
                OKhttptils.post(this, Config.GETDRIVERINFO, map, new OKhttptils.HttpCallBack() {
                    @Override
                    public void success(String response) {
                        Log.i("getDriver",response);
                        try {
                            JSONObject object = new JSONObject(response);
                            String status = object.getString("status");
                            if ("1".equals(status)){
                                String strResult = object.getJSONObject("data").getString("result");
                                JSONObject result = new JSONObject(strResult);
                                Log.i("result===",result.toString());
                                String engine = result.getString("engine_num");
                                String vin = result.getString("vin");
                                //tvEditcph.setText(result.getString(""));
                                tvEditcjh.setText(vin);
                                tvEditfdjh.setText(engine);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void fail(String response) {
                    }
                });
            }
        }
    }

    // 违章查询
    private MyProgressDialog dialog;
    private void getResult(){
        if (!"".equals(cityId)&&!"".equals(tvEditcjh.getText().toString().trim()) &&!"".equals(tvEditcph.getText().toString().trim())&&!"".equals(tvEditfdjh.getText().toString().trim())){
            dialog = MyProgressDialog.createDialog(WeizhangQuery.this);
            dialog.setMessage("正在查询中,请稍候");
            dialog.show();
            Map<String,String> map = new HashMap<>();
            map.put("citycarorg",cityId);
            map.put("lsnum",tvEditcph.getText().toString().trim());//车牌号
            map.put("engineno",tvEditfdjh.getText().toString().trim());//发动车号码
            map.put("frameno",tvEditcjh.getText().toString().trim());//车架号
            OKhttptils.post(this, Config.GETVIOLMSG, map, new OKhttptils.HttpCallBack() {
                @Override
                public void success(String response) {
                    Log.i("getResult",response);
                    try {
                        JSONObject object = new JSONObject(response);
                        JSONObject data = object.getJSONObject("data");
                        String result = data.getString("result");
                        JSONObject res = new JSONObject(result);
                        Log.i("result===",res.toString());
                        JSONArray array = res.getJSONArray("lists");
                        if (array.length()>0){
                            JumpUtil.newInstance().jumpLeft(WeizhangQuery.this, WeizhangResult.class,res.toString());
                        }else {
                            ToastUtil.show(WeizhangQuery.this,"恭喜您未有违章记录");
                        }
                        dialog.dismiss();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void fail(String response) {
                    Log.i("getResult","fail="+response);
                }
            });
        }else{
            ToastUtil.show(this,"请填写完整信息!");
        }
    }

    private void getIsCamera(){
        Map<String,String> map = new HashMap<>();
        map.put("para_id","judgeDriverState");
        OKhttptils.post(this, Config.GETPARA, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if ("1".equals(status)){
                        JSONObject data = object.getJSONObject("data");
                        String value = data.getString("para_value");
                        if ("1".equals(value)){
                            tvEditfdjh.setVisibility(View.VISIBLE);
                            tvEditfdjh.setVisibility(View.VISIBLE);
                        }else{
                            tvEditfdjh.setVisibility(View.INVISIBLE);
                            tvEditfdjh.setVisibility(View.INVISIBLE);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void fail(String response) {

            }
        });
    }
}
