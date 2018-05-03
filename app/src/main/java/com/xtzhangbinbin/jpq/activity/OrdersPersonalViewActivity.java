package com.xtzhangbinbin.jpq.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.adapter.CommonRecyclerAdapter;
import com.xtzhangbinbin.jpq.adapter.ViewHolder;
import com.xtzhangbinbin.jpq.base.BaseActivity;
import com.xtzhangbinbin.jpq.config.Config;
import com.xtzhangbinbin.jpq.entity.OrdersView;
import com.xtzhangbinbin.jpq.utils.JumpUtil;
import com.xtzhangbinbin.jpq.utils.OKhttptils;
import com.xtzhangbinbin.jpq.utils.ToastUtil;
import com.xtzhangbinbin.jpq.view.indicator.IndicatorAdapter;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;
import android.widget.Button;

/**
 * Created by Administrator on 2018/4/28 0028.
 */
@SuppressWarnings("all")
public class OrdersPersonalViewActivity extends BaseActivity{
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.orders_view_comp_name)
    TextView compName;
    @BindView(R.id.orders_view_star1)
    CheckBox star1;
    @BindView(R.id.orders_view_star2)
    CheckBox star2;
    @BindView(R.id.orders_view_star3)
    CheckBox star3;
    @BindView(R.id.orders_view_star4)
    CheckBox star4;
    @BindView(R.id.orders_view_star5)
    CheckBox star5;
//    @BindView(R.id.num_tv)
//    TextView numTv;
    @BindView(R.id.orders_view_loc_tv)
    TextView locTv;
    @BindView(R.id.orders_view_image_dh)
    ImageView imageDh;
    @BindView(R.id.orders_view_image_rz)
    ImageView imageRz;
    @BindView(R.id.orders_view_phone)
    Button phone;
    @BindView(R.id.money_tv)
    TextView money_tv;
    @BindView(R.id.yhPrice_tv)
    TextView yhPrice_tv;
    @BindView(R.id.tolPrice_tv)
    TextView tolPrice_tv;
//    @BindView(R.id.phone)
//    RelativeLayout phone;
//    @BindView(R.id.evaluate)
//    RelativeLayout evaluate;
//    @BindView(R.id.corrdinLayout)
//    CoordinatorLayout corrdinLayout;
//    @BindView(R.id.image_coll)
//    ImageView imageColl;
//    @BindView(R.id.tv_coll)
//    TextView tvColl;
    private ViewGroup viewGroup;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;

    @BindView(R.id.orders_view_recyclerView)
    RecyclerView recyclerViewWXChild;
    private CommonRecyclerAdapter wxChildAdapter;

    private Map<String, String> map = new HashMap<>();
    private List<String> images = new ArrayList<>();//banner

    private IndicatorAdapter<View> tabAdapter;//服务项目导航栏Adapter

    private String order_id = "78d3bcf32a61447592c59bc634e4658f";//商家Id
    private String comp_phone;
    private Double comp_lon, comp_lat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_view);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        if (intent != null && null != intent.getStringExtra("order_id")) {
            order_id = intent.getStringExtra("order_id");
        }
        initView();
        initData();
        getData();
        //设置不加载更多
//        smartRefreshLayout.setEnableLoadmore(false);
    }



    @Override
    protected void onStart() {
        super.onStart();
        setTitle("订单详情");
        setLeftImageClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JumpUtil.newInstance().finishRightTrans(OrdersPersonalViewActivity.this);
            }
        });
    }

    private void initView() {
        banner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                //此处可以自行选择，我直接用的Picasso
//                Picasso.with(OrdersPersonalViewActivity.this).load("http://pic.58pic.com/58pic/14/54/14/09E58PICUpb_1024.jpg").into(imageView);
                getPic(OrdersPersonalViewActivity.this, (String) path, imageView);
            }
        });

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(null != comp_phone){
                    call(comp_phone);
                }
            }
        });

        recyclerViewWXChild.setNestedScrollingEnabled(false);//不滑动
        recyclerViewWXChild.setLayoutManager(new LinearLayoutManager(OrdersPersonalViewActivity.this));
    }

    private void initData() {
    }

    private void getData() {
        Map<String,String> map = new HashMap<>();
        map.put("order_id",order_id);
        OKhttptils.post(this, Config.ORDERS_GET_BYCODE, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("1")) {
                        Gson gson = new Gson();
                        OrdersView ordersView = gson.fromJson(response, OrdersView.class);
                        OrdersView.DataBean.ResultBean resultBean = ordersView.getData().getResult();
                        //获取banner
                        images.add(resultBean.getAuth_comp_img_head_file_id());
                        banner.setImages(images);
                        banner.start();
//                        //公司名字星级地址
                        compName.setText(resultBean.getAuth_comp_name());
                        locTv.setText(resultBean.getAuth_comp_addr());
                        money_tv.setText("￥" + resultBean.getProd_reduced_price());
                        yhPrice_tv.setText("优惠价￥" + resultBean.getProd_reduced_price());
                        tolPrice_tv.setText("总价￥" + resultBean.getProd_price());
                        tolPrice_tv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                        comp_phone = resultBean.getAuth_comp_phone();
                        comp_lon = resultBean.getAuth_comp_lon();
                        comp_lat = resultBean.getAuth_comp_lat();
                        switch (resultBean.getComp_eval_level()) {
                            case 5:
                                star5.setChecked(true);
                            case 4:
                                star4.setChecked(true);
                            case 3:
                                star3.setChecked(true);
                            case 2:
                                star2.setChecked(true);
                            case 1:
                                star1.setChecked(true);
                                break;
                        }
                        if("0".equals(resultBean.getAuth_flag())){
                            imageRz.setVisibility(View.GONE);
                        }

                        final List<OrdersView.DataBean.ResultBean.ItemsBean> itemList = resultBean.getItems();
                        Log.w("test", itemList.size() + ",");
                        wxChildAdapter = new CommonRecyclerAdapter(OrdersPersonalViewActivity.this, itemList, R.layout.item_wx_childs) {
                            @Override
                            public void convert(ViewHolder holder, Object item, int position) {
                                TextView wxChildName = holder.getView(R.id.wxName1_tv);
                                wxChildName.setText(itemList.get(position).getItem_name());
                                TextView childPrice = holder.getView(R.id.price1_tv);
                                childPrice.setText((int) itemList.get(position).getItem_price() + "");
                            }
                        };
                        recyclerViewWXChild.setAdapter(wxChildAdapter);
                    } else {
                        ToastUtil.noNAR(OrdersPersonalViewActivity.this);
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


    @OnClick({R.id.orders_view_image_dh})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.orders_view_image_dh:
                ToastUtil.show(this, "正在进入导航请稍等...");
                navigation(comp_lat, comp_lon);
                break;
//            case R.id.phone:
//                call(comp_phone);
//                break;
//            case R.id.evaluate:
//                Bundle loc = new Bundle();
//                loc.putString("comp_id", comp_id);
//                loc.putString("type", "comp");
//                JumpUtil.newInstance().jumpRight(this, AppraiseActivity.class, loc);
//                break;
        }
    }


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
                            OrdersPersonalViewActivity.this.startActivity(intent); // 激活Activity组件
                        } else {
                            // 未获取权限
                            Toast.makeText(OrdersPersonalViewActivity.this, "您没有授权该权限，请在设置中打开授权", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent();
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            if(Build.VERSION.SDK_INT >= 9){
                                intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                                intent.setData(Uri.fromParts("package", getPackageName(), null));
                            } else if(Build.VERSION.SDK_INT <= 8){
                                intent.setAction(Intent.ACTION_VIEW);
                                intent.setClassName("com.android.settings","com.android.settings.InstalledAppDetails");
                                intent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
                            }
                            startActivity(intent);
                        }
                    }
                });

    }

}
