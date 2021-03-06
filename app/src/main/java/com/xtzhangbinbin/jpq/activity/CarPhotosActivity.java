package com.xtzhangbinbin.jpq.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.chrisbanes.photoview.PhotoView;
import com.google.gson.Gson;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.base.BaseActivity;
import com.xtzhangbinbin.jpq.config.Config;
import com.xtzhangbinbin.jpq.entity.CarDetaile;
import com.xtzhangbinbin.jpq.gson.factory.GsonFactory;
import com.xtzhangbinbin.jpq.utils.DateUtil;
import com.xtzhangbinbin.jpq.utils.JumpUtil;
import com.xtzhangbinbin.jpq.utils.OKhttptils;
import com.xtzhangbinbin.jpq.utils.Prefs;
import com.xtzhangbinbin.jpq.utils.StringUtil;
import com.xtzhangbinbin.jpq.utils.ToastUtil;
import com.xtzhangbinbin.jpq.view.HackyViewPager;
import com.xtzhangbinbin.jpq.view.OnlineOrderDialog;
import com.xtzhangbinbin.jpq.view.OrdinaryDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.DatePicker;
import io.reactivex.functions.Consumer;

public class CarPhotosActivity extends BaseActivity {
    private static final String TAG = "查看大图";
    @BindView(R.id.mCarName)
    TextView mCarName;
    @BindView(R.id.mPrice)
    TextView mPrice;
    @BindView(R.id.mSale)
    ImageView mSale;
    private Activity currtActivity;
    @BindView(R.id.viewpager)
    HackyViewPager viewpager;

    private int posi;
    private String car_id;
    private String file_id;
    private String order_time;

    //图片id
    private List<String> files = new ArrayList<>();
    private List<CarDetaile.DataBean.ResultBean.CarDetailBean.CarImgInfoListBean> carImgInfoListBeans = new ArrayList<>();
    private List<String> names = new ArrayList<>();
    private List<String> contents = new ArrayList<>();
    private List<View> viewList = new ArrayList<>();
    private MyAdapter adapter;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 001:
                    carImgInfoListBeans = (List<CarDetaile.DataBean.ResultBean.CarDetailBean.CarImgInfoListBean>) msg.obj;
                    for (int i = 0; i < carImgInfoListBeans.size(); i++) {
                        files.add(carImgInfoListBeans.get(i).getImg());
                        names.add(carImgInfoListBeans.get(i).getImgName());
                        contents.add(carImgInfoListBeans.get(i).getDetail());
                    }
                    initAdapter();
                    break;

            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_photos);
        ButterKnife.bind(this);
        currtActivity = this;
        Bundle bundle = getIntent().getExtras();
        //第几张图片
        posi = bundle.getInt("position");
        car_id = bundle.getString("car_id");
        mCarName.setText(bundle.getString("car_name"));
        mPrice.setText(bundle.getString("car_price"));
        getData();
        getSale();
    }

    @OnClick({R.id.mBack, R.id.mKefu, R.id.mCut, R.id.mOrder})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mBack:
                JumpUtil.newInstance().finishRightTrans(currtActivity);
                break;
            case R.id.mKefu:
                //拨打客服电话
                call("4001198698");
                break;
            case R.id.mCut:
                //估价
                if(null != Prefs.with(getApplicationContext()).read("user_token")){
                    // 估价
                    JumpUtil.newInstance().jumpRight(CarPhotosActivity.this, AccessCar.class);
                } else {
                    JumpUtil.newInstance().jumpRight(CarPhotosActivity.this, LoginActivity.class);
                }
                break;
            case R.id.mOrder:
                showDialog();
                break;
        }
    }

    /* 弹出预约dialog */
    private void showDialog() {
        final OnlineOrderDialog dialog = OnlineOrderDialog.newInstance(this).showDialog();
        dialog.setTimePickerListener(new OnlineOrderDialog.onTimePickerListener() {
            @Override
            public void onTimePiker() {
                DatePicker picker = new DatePicker(currtActivity);
                picker.setRangeStart(DateUtil.getNextYear(),DateUtil.getNextMonth(),DateUtil.getNextDay());
                picker.setRangeEnd(2050,12,31);
                picker.setDividerColor(getResources().getColor(R.color.theme_coloer));
                picker.setTextColor(getResources().getColor(R.color.theme_coloer));
                picker.setCancelTextColor(getResources().getColor(R.color.theme_coloer));
                picker.setSubmitTextColor(getResources().getColor(R.color.theme_coloer));
                picker.setTopLineColor(getResources().getColor(R.color.theme_coloer));
                picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
                    @Override
                    public void onDatePicked(String year, String month, String day) {
                        order_time = year + "-" + month + "-" + day;
                        dialog.setTime(order_time);
                    }
                });
                picker.show();
            }
        });


        dialog.setYesOnclickListener(new OnlineOrderDialog.onYesOnclickListener() {
            @Override
            public void onYesClick() {
                if (!dialog.getPhone().isEmpty() && StringUtil.isPhoneNum(dialog.getPhone())){
                    if (!dialog.getTime().isEmpty()){
                        Map<String,String> map = new HashMap<>();
                        map.put("car_id",car_id);
                        map.put("online_phone",dialog.getPhone());
                        map.put("online_date",order_time + " 00:00:00");
                        OKhttptils.post(currtActivity, Config.CAR_DETAIL_ONLINE_ORDER, map, new OKhttptils.HttpCallBack() {
                            @Override
                            public void success(String response) {
                                Log.i(TAG, "success在线预约: " + response);
                                /**
                                 * {"data":{},"message":"","status":"1"}
                                 */
                                dialog.dismiss();
                                ToastUtil.show(currtActivity,"预约成功");

                            }

                            @Override
                            public void fail(String response) {
                                Log.d(TAG, "fail在线预约: " + response);
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    ToastUtil.show(currtActivity,jsonObject.getString("message"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }else {
                        ToastUtil.show(currtActivity,"请选择看车时间");
                    }
                }else {
                    ToastUtil.show(currtActivity,"请填写正确的手机号");
                }
            }
        });
    }

    /* 订阅降价通知 */
    private void getSale() {

        mSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, String> map = new HashMap<>();
                map.put("car_id", car_id);
                OKhttptils.post(currtActivity, Config.CAR_DETAIL_IS_SALE, map, new OKhttptils.HttpCallBack() {
                    @Override
                    public void success(String response) {
                        Log.e(TAG, "success: " + response );
                        /**
                         * {"data":{"result":""},"message":"","status":"1"}
                         */
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String data = jsonObject.getString("data");
                            JSONObject object = new JSONObject(data);
                            final String result = object.getString("result");
                            Log.i(TAG, "success: " + result);
                            if (!result.isEmpty()) {
                                ToastUtil.show(currtActivity, "您已订阅过该车降价通知");
                            } else {
                                //未订阅过
                                final OrdinaryDialog dialog = OrdinaryDialog.newInstance(currtActivity).setMessage1("订阅降价通知").setMessage2("确定订阅该车的降价通知吗？").showDialog();
                                dialog.setNoOnclickListener(new OrdinaryDialog.onNoOnclickListener() {
                                    @Override
                                    public void onNoClick() {
                                        dialog.dismiss();
                                    }
                                });
                                dialog.setYesOnclickListener(new OrdinaryDialog.onYesOnclickListener() {
                                    @Override
                                    public void onYesClick() {
                                        dialog.dismiss();
                                        Map<String, String> map = new HashMap<>();
                                        map.put("car_id", car_id);
                                        OKhttptils.post(currtActivity, Config.CAR_DETAIL_ADD_SALE, map, new OKhttptils.HttpCallBack() {
                                            @Override
                                            public void success(String response) {
                                                Log.i(TAG, "success添加订阅: " + response);
                                                /**
                                                 * {"data":{"result":"1"},"message":"","status":"1"}
                                                 */
                                                try {
                                                    JSONObject jsonObject1 = new JSONObject(response);
                                                    String data = jsonObject1.getString("data");
                                                    JSONObject object1 = new JSONObject(data);
                                                    String result = object1.getString("result");
                                                    ToastUtil.show(currtActivity, "订阅成功");

                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }

                                            }

                                            @Override
                                            public void fail(String response) {
                                                Log.i(TAG, "fail添加订阅: " + response);
                                                try {
                                                    JSONObject jsonObject1 = new JSONObject(response);
                                                    ToastUtil.show(currtActivity, jsonObject1.getString("message"));

                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                    }
                                });

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void fail(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            ToastUtil.show(currtActivity, jsonObject.getString("message"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    /* 拨打客服电话 */
    private RxPermissions rxPermission;
    private void call(final String phone) {
        // 申请定位权限
        rxPermission = new RxPermissions(this);
        rxPermission.request(Manifest.permission.CALL_PHONE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean granted) throws Exception {
                        if (granted) { // 在android 6.0之前会默认返回true
                            // 已经获取权限
                            Intent intent = new Intent(); // 意图对象：动作 + 数据
                            intent.setAction(Intent.ACTION_CALL); // 设置动作
                            Uri data = Uri.parse("tel:" + phone); // 设置数据
                            intent.setData(data);
                            currtActivity.startActivity(intent); // 激活Activity组件
                        } else {
                            // 未获取权限
                            Toast.makeText(currtActivity, "您没有授权该权限，请在设置中打开授权", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent();
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            if (Build.VERSION.SDK_INT >= 9) {
                                intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                                intent.setData(Uri.fromParts("package", getPackageName(), null));
                            } else if (Build.VERSION.SDK_INT <= 8) {
                                intent.setAction(Intent.ACTION_VIEW);
                                intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
                                intent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
                            }
                            startActivity(intent);
                        }
                    }
                });

    }

    /* 添加adapter */
    private void initAdapter() {
        Log.i(TAG, "initAdapter: " + names.toString());
        Log.d(TAG, "initAdapter: " + contents.toString());
        for (int i = 0; i < files.size(); i++) {
            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_car_photo, null);
            PhotoView photoView = view.findViewById(R.id.mPic);
            TextView mName = view.findViewById(R.id.mName);
            TextView mContent = view.findViewById(R.id.mContent);
            TextView mIndex = view.findViewById(R.id.mIndex);
            TextView mAll = view.findViewById(R.id.mAll);
            mName.setText(names.get(i));
            mContent.setText(contents.get(i));
            mAll.setText("/" + String.valueOf(files.size()));
            mIndex.setText(String.valueOf(i + 1));

            WindowManager wm = (WindowManager) this
                    .getSystemService(Context.WINDOW_SERVICE);
            int width = wm.getDefaultDisplay().getWidth();
            int height = wm.getDefaultDisplay().getHeight();
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (width / 3 * 2));
            photoView.setLayoutParams(params);

            OKhttptils.getPic(currtActivity, files.get(i), photoView);
            viewList.add(view);
        }
        adapter = new MyAdapter(viewList);
        viewpager.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        viewpager.setCurrentItem(posi);
    }

    /* 获取数据 */
    private void getData() {
        final Map<String, String> map = new HashMap<>();
        map.put("car_id", car_id);
        OKhttptils.post(currtActivity, Config.CAR_DETAIL, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                Log.i(TAG, "success: " + response);

                Gson gson = GsonFactory.create();
                CarDetaile carDetaile = gson.fromJson(response, CarDetaile.class);

                List<CarDetaile.DataBean.ResultBean.CarDetailBean.CarImgInfoListBean> carImgInfoListBeans = carDetaile.getData().getResult().getCarDetail().getCarImgInfoList();
                Log.d(TAG, "success个数: " + carImgInfoListBeans.size());
                Message message = new Message();
                message.what = 001;
                message.obj = carImgInfoListBeans;
                handler.sendMessage(message);


            }

            @Override
            public void fail(String response) {
                Log.i(TAG, "fail: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    ToastUtil.show(currtActivity, jsonObject.getString("message"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /* viewpager自定义adapter */
    class MyAdapter extends PagerAdapter {
        private List<View> list;

        public MyAdapter(List<View> viewList) {
            this.list = viewList;
        }

        @Override
        public int getCount() {
            return list == null ? 0 : list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(list.get(position), LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            return list.get(position);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }

}
