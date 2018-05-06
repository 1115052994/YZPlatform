package com.xtzhangbinbin.jpq.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.adapter.MultiTypeSupport;
import com.xtzhangbinbin.jpq.adapter.PriceWarnAdapter;
import com.xtzhangbinbin.jpq.adapter.ViewHolder;
import com.xtzhangbinbin.jpq.base.BaseActivity;
import com.xtzhangbinbin.jpq.config.Config;
import com.xtzhangbinbin.jpq.utils.JumpUtil;
import com.xtzhangbinbin.jpq.utils.OKhttptils;
import com.xtzhangbinbin.jpq.view.OrdinaryDialog;
import com.xtzhangbinbin.jpq.view.ZQImageViewRoundOval;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/* 个人中心 - 降价提醒 */
public class PriceWarnActivity extends BaseActivity {

    private static final String TAG = "降价提醒";
    @BindView(R.id.mList)
    RecyclerView mList;
    @BindView(R.id.mRefresh)
    SmartRefreshLayout mRefresh;
    @BindView(R.id.browsing_Cardeal_image)
    ImageView browsingCardealImage;
     int sum=1;

    private PriceWarnAdapter recyclerAdapter;
    private Activity currtAcxtivity;
    private ArrayList<HashMap<String, String>> timearr = new ArrayList<>();//时间集合
    private ArrayList<ArrayList<HashMap<String, String>>> dataarr = new ArrayList<>();//数据集合
    private List<Map<String, String>> recyclerList = new ArrayList<>();//recycler集合
    private int pageIndex;
    private int pageTotal;


//    private Handler handler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//                case 001:
//                    break;
//                case 002:
//                    break;
//            }
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_warn);
        ButterKnife.bind(this);
        currtAcxtivity = this;
        timearr.clear();
        dataarr.clear();
        recyclerList.clear();
        getData(1,null);
        initView();
        mRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                pageIndex = 1;
                timearr.clear();
                dataarr.clear();
                recyclerList.clear();
                getData(1,refreshlayout);
            }
        });
        mRefresh.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                if(pageIndex < pageTotal){
                    getData(++pageIndex,refreshlayout);
                }else {
                    refreshlayout.finishLoadmore();
                }

            }
        });
    }

    private void initView() {
        recyclerAdapter = new PriceWarnAdapter(this, recyclerList, new MultiTypeSupport() {
            @Override
            public int getLayoutId(Object item, int position) {
                String letter = recyclerList.get(position).get("id_carbrand");
                if ("letter".equals(letter)) {
                    return R.layout.item_year_pwa;
                } else {
                    return R.layout.item_year_model_pwa;
                }
            }
        })
        {
            @Override
            public void convert(ViewHolder holder, Object item, final int position) {
                if ("letter".equals(recyclerList.get(position).get("id_carbrand"))) {
                    // 字母
                    TextView letter = holder.getView(R.id.car_letter);
                    if(sum==1){
                        holder.getView(R.id.view).setVisibility(View.GONE);
                    }
                    sum++;
                    letter.setText(recyclerList.get(position).get("time"));
                } else {
                    // 品牌
                    TextView weisj_name = holder.getView(R.id.weisj_name);
                    if (recyclerList.get(position).get("car_name") != null) {
                        weisj_name.setText(recyclerList.get(position).get("car_name"));
                    }
                    String car_sign_date = recyclerList.get(position).get("car_sign_date");
                    String[] split = car_sign_date.split("-");
                    TextView nian = holder.getView(R.id.nian);
                    nian.setText(split[0]+"年"+"/");
                    TextView gongli = holder.getView(R.id.gongli);
                    gongli.setText(recyclerList.get(position).get("car_mileage")+"万公里");
                    TextView jiangjia = holder.getView(R.id.jiangjia);
                    TextView yuanjia = holder.getView(R.id.yuanjia);
                    if(recyclerList.get(position).get("dp_price").isEmpty()){
                        yuanjia.setVisibility(View.GONE);
                        jiangjia.setTextColor(Color.parseColor("#666666"));
                        jiangjia.setText(">>"+recyclerList.get(position).get("depreciate"));
                    }else {
                        yuanjia.setText("原价: "+recyclerList.get(position).get("dp_price"));
                        jiangjia.setText(">>"+recyclerList.get(position).get("depreciate"));
                    }
                    TextView price = holder.getView(R.id.price);
                    price.setText(new DecimalFormat("#0.00").format(Double.valueOf(recyclerList.get(position).get("price").toString()) / 10000)+"万");
                    ZQImageViewRoundOval weisj_image = holder.getView(R.id.weisj_image);
                    //设置图片圆角
                    weisj_image.setType(ZQImageViewRoundOval.TYPE_ROUND);
                    weisj_image.setRoundRadius(5);
                    OKhttptils.getPic(PriceWarnActivity.this,recyclerList.get(position).get("auth_comp_img_head_file_id"),weisj_image);
                    RelativeLayout weisj_shanchu = holder.getView(R.id.weisj_shanchu);
                    weisj_shanchu.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final OrdinaryDialog ordinaryDialog = OrdinaryDialog.newInstance(PriceWarnActivity.this).setMessage1("删除订阅").setMessage2("  删除后不可恢复，确定删除？").setConfirm("确定").setCancel("再想想").showDialog();
                            ordinaryDialog.setNoOnclickListener(new OrdinaryDialog.onNoOnclickListener() {
                                @Override
                                public void onNoClick() {
                                    ordinaryDialog.dismiss();
                                }
                            });
                            ordinaryDialog.setYesOnclickListener(new OrdinaryDialog.onYesOnclickListener() {
                                @Override
                                public void onYesClick() {
                                    dete(recyclerList.get(position).get("dp_id"));
                                    ordinaryDialog.dismiss();
                                }
                            });

                        }
                    });

                }
            }
        };
        recyclerAdapter.setOnItemClickListener(new PriceWarnAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {


            }
        });
        mList.setLayoutManager(new LinearLayoutManager(this));
        mList.setAdapter(recyclerAdapter);

    }

    /* 获取数据 */
    private void getData(final int pageIndex, final RefreshLayout refreshlayout) {
        Map<String, String> map = new HashMap<>();
        map.put("pageIndex", String.valueOf(pageIndex));
        OKhttptils.post(currtAcxtivity, Config.PERS_PRICE_WARN, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                Log.d(TAG, "success: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject data = jsonObject.getJSONObject("data");
                    JSONArray result = data.getJSONArray("result");
                    pageTotal = data.getInt("pageTotal");
                    for (int i = 0; i < result.length(); i++) {
                        HashMap<String, String> stringStringHashMap = new HashMap<>();
                        ArrayList<HashMap<String, String>> hashMap = new ArrayList<>();
                        JSONObject jsonObject1 = result.getJSONObject(i);
                        String time = jsonObject1.getString("time");
                        stringStringHashMap.put("time", time);
                        JSONArray date = jsonObject1.getJSONArray("date");
                        timearr.add(stringStringHashMap);
                        for (int j = 0; j < date.length(); j++) {
                            HashMap<String, String> hashMap1 = new HashMap<>();
                            JSONObject jsonObject2 = date.getJSONObject(j);
                            hashMap1.put("car_name", jsonObject2.getString("car_name"));
                            hashMap1.put("price", String.valueOf(jsonObject2.getInt("price")));
                            hashMap1.put("dp_oper_date", jsonObject2.getString("dp_oper_date"));
                            hashMap1.put("dp_state", jsonObject2.getString("dp_state"));
                            hashMap1.put("car_sign_date", jsonObject2.getString("car_sign_date"));
                            hashMap1.put("dp_id", jsonObject2.getString("dp_id"));
                            hashMap1.put("car_mileage", String.valueOf(jsonObject2.getInt("car_mileage")));
                            hashMap1.put("dp_price", jsonObject2.getString("dp_price"));
                            hashMap1.put("depreciate", jsonObject2.getString("depreciate"));
                            hashMap1.put("auth_comp_img_head_file_id", jsonObject2.getString("auth_comp_img_head_file_id"));
                            hashMap.add(hashMap1);
                        }
                        dataarr.add(hashMap);
                    }


                    //没有信息图片显示
                    if (dataarr.size() <= 0) {
                        browsingCardealImage.setVisibility(View.VISIBLE);
                    }else {
                        if(refreshlayout!=null){
                            if (pageIndex > pageTotal) {
                                refreshlayout.finishLoadmore(2000);
                            } else {
                                recyclerAdapter.notifyDataSetChanged();
                                refreshlayout.finishRefresh(2000);
                                refreshlayout.finishLoadmore(2000);
                            }
                        }else {
                            recyclerAdapter.notifyDataSetChanged();
                        }
                    }

                    String letter = "";
                    for (int i = 0; i <timearr.size() ; i++) {
                        String firstChar = timearr.get(i).get("time");
                        if (!letter.equals(firstChar)) {
                            Map<String, String> carMap = new HashMap<>();
                            carMap.put("time",timearr.get(i).get("time"));
                            carMap.put("id_carbrand", "letter");
                            carMap.put("istrue", "1");
                            recyclerList.add(carMap);
                            letter = firstChar;
                        }
                        ArrayList<HashMap<String, String>> hashMaps = dataarr.get(i);
                        for (int j = 0; j <dataarr.get(i).size() ; j++) {
                            Map<String, String> carMap1 = new HashMap<>();
                            carMap1.put("car_name",hashMaps.get(j).get("car_name"));
                            carMap1.put("id_carbrand", hashMaps.get(j).get("car_name"));
                            carMap1.put("price", hashMaps.get(j).get("price"));
                            carMap1.put("dp_oper_date", hashMaps.get(j).get("dp_oper_date"));
                            carMap1.put("dp_state", hashMaps.get(j).get("dp_state"));
                            carMap1.put("car_sign_date", hashMaps.get(j).get("car_sign_date"));
                            carMap1.put("dp_id", hashMaps.get(j).get("dp_id"));
                            carMap1.put("car_mileage", hashMaps.get(j).get("car_mileage"));
                            carMap1.put("dp_price", hashMaps.get(j).get("dp_price"));
                            carMap1.put("depreciate", hashMaps.get(j).get("depreciate"));
                            carMap1.put("auth_comp_img_head_file_id", hashMaps.get(j).get("auth_comp_img_head_file_id"));
//                            carMap1.put("image_carbrand", bean.getBrandImg());
//                            carMap1.put("id_carbrand", bean.getBrandId());
                            recyclerList.add(carMap1);
                        }
                    }

                    recyclerAdapter.notifyDataSetChanged();


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void fail(String response) {
                Log.d(TAG, "fail: " + response);
            }
        });
    }
    private void dete(String dp_id) {
        Map<String, String> map = new HashMap<>();
        map.put("dp_id",dp_id);
        OKhttptils.post(PriceWarnActivity.this, Config.DELETESUBSCRIPTION, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                Log.d("aaaaa", "onResponse获取数据: " + response);
                timearr.clear();
                dataarr.clear();
                recyclerList.clear();
                getData(1,null);
                recyclerAdapter.notifyDataSetChanged();

            }

            @Override
            public void fail(String response) {
                Log.d("aaaa", "fail: " + response);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        setTitle("降价提醒");
        setLeftImageClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JumpUtil.newInstance().finishRightTrans(PriceWarnActivity.this);
            }
        });
    }

}
