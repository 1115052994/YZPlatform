package com.xtzhangbinbin.jpq.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xtzhangbinbin.jpq.AppraiseInterface;
import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.adapter.AdvanceSXCarAdapter;
import com.xtzhangbinbin.jpq.adapter.AdvanceSXCarMoreAdapter;
import com.xtzhangbinbin.jpq.base.BaseActivity;
import com.xtzhangbinbin.jpq.config.Config;
import com.xtzhangbinbin.jpq.entity.CarParams;
import com.xtzhangbinbin.jpq.utils.JumpUtil;
import com.xtzhangbinbin.jpq.utils.OKhttptils;
import com.xtzhangbinbin.jpq.view.ExpandableGridView;
import com.xtzhangbinbin.jpq.view.SeekBarPressure;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AdvanceSX extends BaseActivity {

    @BindView(R.id.gv_cx)
    ExpandableGridView gvcx;
    @BindView(R.id.tv_cl)
    TextView tvCl;
    @BindView(R.id.seekBar_cl)
    SeekBarPressure seekBarCl;
    @BindView(R.id.tv_lc)
    TextView tvLc;
    @BindView(R.id.seekBar_lc)
    SeekBarPressure seekBarLc;
    @BindView(R.id.tv_pl)
    TextView tvPl;
    @BindView(R.id.seekBar_pl)
    SeekBarPressure seekBarPl;
    @BindView(R.id.gv_bsx)
    ExpandableGridView gvBsx;
    @BindView(R.id.gv_pfbz)
    ExpandableGridView gvPfbz;
    @BindView(R.id.gv_zws)
    ExpandableGridView gvZws;
    @BindView(R.id.gv_rllx)
    ExpandableGridView gvRllx;
    @BindView(R.id.ly_subscribe)
    LinearLayout lySubscribe;
    @BindView(R.id.tv_city)
    TextView tvCity;
    @BindView(R.id.tv_brand)
    TextView tvBrand;
    @BindView(R.id.submit)
    Button submit;
    //车型
    private List<Map<String, String>> carTypeList = new ArrayList<>();
    private AdvanceSXCarAdapter typeAdapter;
    //变速箱
    private List<String> carBsxList = new ArrayList<>();
    private AdvanceSXCarMoreAdapter bsxAdapter;
    // 排放标准
    private List<String> carPfbzList = new ArrayList<>();
    private AdvanceSXCarMoreAdapter pfbzAdapter;
    // 座位数
    private List<String> carzwsList = new ArrayList<>();
    private AdvanceSXCarMoreAdapter zwsAdapter;
    // 燃料类型
    private List<String> carRllxList = new ArrayList<>();
    private AdvanceSXCarMoreAdapter rllxAdapter;

    // CarParams
    /*
    "img": null,
	"paramName": "油电混合",
	"paramId": "YZcarscreenoilydhe"
     *///车型cx  变速箱bsx   排放标准pfbz   燃油类型rylx   座位数zws
    List<Map<String, String>> cxParamsList = new ArrayList<>();
    List<Map<String, String>> bsxParamsList = new ArrayList<>();
    List<Map<String, String>> rylxParamsList = new ArrayList<>();
    List<Map<String, String>> pfbzParamsList = new ArrayList<>();
    List<Map<String, String>> zwsParamsList = new ArrayList<>();

    // 保留选中position
    private int cxPosition = 0;
    private int bsxPosition = 0;
    private int pfbzPosition = 0;
    private int rylxPosition = 0;
    private int zwsPosition = 0;
    private int clStart = 0;
    private int clEnd = 6;// 不限标记为
    private int lcStart = 0;
    private int lcEnd = 15;// 不限标记为
    private float plStart = 0;
    private float plEnd = 5.0f;// 不限标记为
    // 保留上次选中状态
    private View cx = null;
    private View bsx = null;
    private View pfbz = null;
    private View zws = null;
    private View rllx = null;

    private String type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advance_sx);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        type = "";
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                type = bundle.getString("String");
                if (type != null && !"".equals(type)) {
                    lySubscribe.setVisibility(View.VISIBLE);
                    submit.setText("订阅");
                }
            }
        }
        initView();
        getCarParams();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setTitle("高级筛选");
        setLeftImageClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataBackListener != null)
                    dataBackListener.backData(null);
                JumpUtil.newInstance().finishRightTrans(AdvanceSX.this);
            }
        });
    }

    private void initView() {
        // 车型
        typeAdapter = new AdvanceSXCarAdapter(this, cxParamsList);
        typeAdapter.setOnItemClickListener(new AppraiseInterface() {
            @Override
            public void onClick(View view, int position) {
                //响应单击事件
                if (cx != null) {
                    // 取消之前选中
                    cx.setSelected(false);
                    //设置字体颜色
                    TextView tv = cx.findViewById(R.id.tv_carType);
                    tv.setTextColor(Color.parseColor("#333333"));
                }
                //设置边框颜色
                view.setSelected(true);
                //设置字体颜色
                TextView tv = view.findViewById(R.id.tv_carType);
                tv.setTextColor(Color.parseColor("#f35454"));
                cx = view;
                cxPosition = position;
            }
        });
        gvcx.setAdapter(typeAdapter);

        //变速箱
//        carBsxList.add("不限");
//        carBsxList.add("手动");
//        carBsxList.add("自动");
        bsxAdapter = new AdvanceSXCarMoreAdapter(this, carBsxList);
        bsxAdapter.setOnItemClickListener(new AppraiseInterface() {
            @Override
            public void onClick(View view, int position) {
                //设置响应事件
                if (bsx != null) {
                    // 取消之前选中
                    bsx.setSelected(false);
                    //设置字体颜色
                    TextView tv = bsx.findViewById(R.id.tv_type);
                    tv.setTextColor(Color.parseColor("#333333"));
                }
                view.setSelected(true);
                //设置字体颜色
                TextView tv = view.findViewById(R.id.tv_type);
                tv.setTextColor(Color.parseColor("#f35454"));
                bsx = view;

                bsxPosition = position;
            }
        });
        gvBsx.setAdapter(bsxAdapter);

        // 排放标准
//        carPfbzList.add("不限");
//        carPfbzList.add("国三级以上");
//        carPfbzList.add("国四及以上");
//        carPfbzList.add("国五");
        pfbzAdapter = new AdvanceSXCarMoreAdapter(this, carPfbzList);
        pfbzAdapter.setOnItemClickListener(new AppraiseInterface() {
            @Override
            public void onClick(View view, int position) {
                //设置响应事件
                if (pfbz != null) {
                    // 取消之前选中
                    pfbz.setSelected(false);
                    //设置字体颜色
                    TextView tv = pfbz.findViewById(R.id.tv_type);
                    tv.setTextColor(Color.parseColor("#333333"));
                }
                view.setSelected(true);
                //设置字体颜色
                TextView tv = view.findViewById(R.id.tv_type);
                tv.setTextColor(Color.parseColor("#f35454"));
                pfbz = view;

                pfbzPosition = position;
            }
        });
        gvPfbz.setAdapter(pfbzAdapter);
        // 座位数
//        carzwsList.add("不限");
//        carzwsList.add("2座");
//        carzwsList.add("4座");
//        carzwsList.add("5座");
//        carzwsList.add("7座");
//        carzwsList.add("7座以上");
        zwsAdapter = new AdvanceSXCarMoreAdapter(this, carzwsList);
        zwsAdapter.setOnItemClickListener(new AppraiseInterface() {
            @Override
            public void onClick(View view, int position) {
                //设置响应事件
                if (zws != null) {
                    // 取消之前选中
                    zws.setSelected(false);
                    //设置字体颜色
                    TextView tv = zws.findViewById(R.id.tv_type);
                    tv.setTextColor(Color.parseColor("#333333"));
                }
                view.setSelected(true);
                //设置字体颜色
                TextView tv = view.findViewById(R.id.tv_type);
                tv.setTextColor(Color.parseColor("#f35454"));
                zws = view;

                zwsPosition = position;
            }
        });
        gvZws.setAdapter(zwsAdapter);
        // 燃料类型
//        carRllxList.add("不限");
//        carRllxList.add("汽油");
//        carRllxList.add("柴油");
//        carRllxList.add("电动");
//        carRllxList.add("油电混动");
        rllxAdapter = new AdvanceSXCarMoreAdapter(this, carRllxList);
        rllxAdapter.setOnItemClickListener(new AppraiseInterface() {
            @Override
            public void onClick(View view, int position) {
                //设置响应事件
                if (rllx != null) {
                    // 取消之前选中
                    rllx.setSelected(false);
                    //设置字体颜色
                    TextView tv = rllx.findViewById(R.id.tv_type);
                    tv.setTextColor(Color.parseColor("#333333"));
                }
                view.setSelected(true);
                //设置字体颜色
                TextView tv = view.findViewById(R.id.tv_type);
                tv.setTextColor(Color.parseColor("#f35454"));
                rllx = view;

                rylxPosition = position;
            }
        });
        gvRllx.setAdapter(rllxAdapter);

        // 车龄
        //显示最大值
        seekBarCl.setMaxSize(6);
        //设置分几块区域
        seekBarCl.setMaxCount(7);
        seekBarCl.isInside(true);
        seekBarCl.setOnSeekBarChangeListener(new SeekBarPressure.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBarPressure seekBar, double progressLow, double progressHigh) {
                if (progressLow == 0) {
                    if (progressHigh == 6) {
                        tvCl.setText("不限价格");
                        clStart = 0;
                        clEnd = 6;
                    } else {
                        tvCl.setText((int) progressHigh + "年以内");
                        clStart = 0;
                        clEnd = (int) progressHigh;
                    }
                } else {
                    if (progressHigh == 6) {
                        tvCl.setText((int) progressLow + "年以上");
                        clStart = (int) progressLow;
                        clEnd = 6;
                    } else {
                        tvCl.setText((int) progressLow + "年-" + (int) progressHigh + "年");
                        clStart = (int) progressLow;
                        clEnd = (int) progressHigh;
                    }
                }
            }
        });

        // 里程
        //显示最大值
        seekBarLc.setMaxSize(5);
        //设置分几块区域
        seekBarLc.setMaxCount(6);
        seekBarLc.isInside(false);
        seekBarLc.setOnSeekBarChangeListener(new SeekBarPressure.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBarPressure seekBar, double progressLow, double progressHigh) {
                if (progressLow == 0) {
                    if (progressHigh == 5) {
                        tvLc.setText("不限里程");
                        lcStart = 0;
                        lcEnd = 15;
                    } else {
                        tvLc.setText((int) progressHigh * 3 + "万公里以内");
                        lcStart = 0;
                        lcEnd = (int) progressHigh * 3;
                    }
                } else {
                    if (progressHigh == 5) {
                        tvLc.setText((int) progressLow * 3 + "万公里以上");
                        lcStart = (int) progressLow * 3;
                        lcEnd = 15;
                    } else {
                        tvLc.setText((int) progressLow * 3 + "万公里-" + (int) progressHigh * 3 + "万公里");
                        lcStart = (int) progressLow * 3;
                        lcEnd = (int) progressHigh * 3;
                    }
                }
            }
        });
        // 排量
        //显示最大值
        seekBarPl.setMaxSize(5);
        //设置分几块区域
        seekBarPl.setMaxCount(6);
        seekBarPl.isInside(false);
        seekBarPl.setOnSeekBarChangeListener(new SeekBarPressure.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBarPressure seekBar, double progressLow, double progressHigh) {
                if (progressLow == 0) {
                    if (progressHigh == 5) {
                        tvPl.setText("不限排量");
                        plStart = 0;
                        plEnd = (float) 5.0;
                    } else {
                        tvPl.setText((float) progressHigh + "L以内");
                        plStart = 0;
                        plEnd = (float) progressHigh;
                    }
                } else {
                    if (progressHigh == 5) {
                        tvPl.setText((float) progressLow + "L以上");
                        plStart = (float) progressLow;
                        plEnd = (float) 5.0;
                    } else {
                        tvPl.setText((float) progressLow + "L-" + (float) progressHigh + "L");
                        plStart = (float) progressLow;
                        plEnd = (float) progressHigh;
                    }
                }
            }
        });
    }

    private void getCarParams() {
        Map<String, String> map = new HashMap<>();
        OKhttptils.post(this, Config.GETCARPARAMDICT, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                Log.i("response===", response);
                try {
                    JSONObject object = new JSONObject(response);
                    if ("1".equals(object.getString("status"))) {
                        Gson gson = new Gson();
                        CarParams carParams = gson.fromJson(response, CarParams.class);
                        CarParams.DataBean.ResultBean resultBean = carParams.getData().getResult();
                        //carParams
                        cxParamsList.clear();
                        bsxParamsList.clear();
                        pfbzParamsList.clear();
                        zwsParamsList.clear();
                        rylxParamsList.clear();

                        carTypeList.clear();
                        carBsxList.clear();
                        carPfbzList.clear();
                        carzwsList.clear();
                        carRllxList.clear();
                        //车型  变速箱   排放标准   燃油类型   座位数
                         /*
                        "img": null,
                        "paramName": "油电混合",
                        "paramId": "YZcarscreenoilydhe"
                         */
                        List<CarParams.DataBean.ResultBean.DctListBean> dictList = resultBean.getDctList();
                        for (CarParams.DataBean.ResultBean.DctListBean bean : dictList) {
                            List<CarParams.DataBean.ResultBean.DctListBean.ChildParamListBean> childList = bean.getChildParamList();
                            switch (bean.getParentParamName()) {
                                case "车型":
                                    for (CarParams.DataBean.ResultBean.DctListBean.ChildParamListBean childBean : childList) {
                                        Map<String, String> map = new HashMap<>();
                                        map.put("img", childBean.getImg());
                                        map.put("paramId", childBean.getParamId());
                                        map.put("paramName", childBean.getParamName());
                                        cxParamsList.add(map);
                                    }
                                    typeAdapter.notifyDataSetChanged();
                                    break;
                                case "变速箱":
                                    for (CarParams.DataBean.ResultBean.DctListBean.ChildParamListBean childBean : childList) {
                                        Map<String, String> map = new HashMap<>();
                                        map.put("img", childBean.getImg());
                                        map.put("paramId", childBean.getParamId());
                                        map.put("paramName", childBean.getParamName());
                                        bsxParamsList.add(map);
                                        carBsxList.add(childBean.getParamName());
                                    }
                                    bsxAdapter.notifyDataSetChanged();
                                    break;
                                case "排放标准":
                                    for (CarParams.DataBean.ResultBean.DctListBean.ChildParamListBean childBean : childList) {
                                        Map<String, String> map = new HashMap<>();
                                        map.put("img", childBean.getImg());
                                        map.put("paramId", childBean.getParamId());
                                        map.put("paramName", childBean.getParamName());
                                        carPfbzList.add(childBean.getParamName());
                                        pfbzParamsList.add(map);
                                    }
                                    pfbzAdapter.notifyDataSetChanged();
                                    break;
                                case "燃油类型":
                                    for (CarParams.DataBean.ResultBean.DctListBean.ChildParamListBean childBean : childList) {
                                        Map<String, String> map = new HashMap<>();
                                        map.put("img", childBean.getImg());
                                        map.put("paramId", childBean.getParamId());
                                        map.put("paramName", childBean.getParamName());
                                        rylxParamsList.add(map);
                                        carRllxList.add(childBean.getParamName());
                                    }
                                    rllxAdapter.notifyDataSetChanged();
                                    break;
                                case "座位数":
                                    for (CarParams.DataBean.ResultBean.DctListBean.ChildParamListBean childBean : childList) {
                                        Map<String, String> map = new HashMap<>();
                                        map.put("img", childBean.getImg());
                                        map.put("paramId", childBean.getParamId());
                                        map.put("paramName", childBean.getParamName());
                                        zwsParamsList.add(map);
                                        carzwsList.add(childBean.getParamName());
                                    }
                                    zwsAdapter.notifyDataSetChanged();
                                    break;

                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return response;
            }

            @Override
            public void fail(String response) {

            }
        });
    }

    public void submit1(View view) {
        //车型cx  变速箱bsx   排放标准pfbz   燃油类型rylx   座位数zws
        Map<String, String> map = new HashMap<>();
        map.put("cx", cxPosition == 0 ? "" : cxParamsList.get(cxPosition).get("paramName") + "," + cxParamsList.get(cxPosition).get("paramId"));
        map.put("bsx", bsxPosition == 0 ? "" : bsxParamsList.get(bsxPosition).get("paramName") + "," + bsxParamsList.get(bsxPosition).get("paramId"));
        map.put("pfbz", pfbzPosition == 0 ? "" : pfbzParamsList.get(pfbzPosition).get("paramName") + "," + pfbzParamsList.get(pfbzPosition).get("paramId"));
        map.put("rylx", rylxPosition == 0 ? "" : rylxParamsList.get(rylxPosition).get("paramName") + "," + rylxParamsList.get(rylxPosition).get("paramId"));
        map.put("zws", zwsPosition == 0 ? "" : zwsParamsList.get(zwsPosition).get("paramName") + "," + zwsParamsList.get(zwsPosition).get("paramId"));
        // 车龄cl 里程lc 排量pl
        map.put("cl", clStart + "," + clEnd);
        map.put("lc", lcStart + "," + lcEnd);
        map.put("pl", plStart + "," + plEnd);
        // 订阅新加入
        if (!"".equals(type)) {
            // 城市
            map.put("city", cityName);
            // 品牌(name,id)
            map.put("brand", brandName + "," + brandId);
            map.put("carname", car + "," + carId);
        }
        dataBackListener.backData(map);
        // 关闭
        JumpUtil.newInstance().finishRightTrans(this);
    }

    public void reset1(View view) {
        if (cx != null) {
            // 取消之前选中
            cx.setSelected(false);
            //设置字体颜色
            TextView tv = cx.findViewById(R.id.tv_carType);
            tv.setTextColor(Color.parseColor("#333333"));
        }
        if (bsx != null) {
            // 取消之前选中
            bsx.setSelected(false);
            //设置字体颜色
            TextView tv = bsx.findViewById(R.id.tv_type);
            tv.setTextColor(Color.parseColor("#333333"));
        }
        if (pfbz != null) {
            // 取消之前选中
            pfbz.setSelected(false);
            //设置字体颜色
            TextView tv = pfbz.findViewById(R.id.tv_type);
            tv.setTextColor(Color.parseColor("#333333"));
        }
        if (rllx != null) {
            // 取消之前选中
            rllx.setSelected(false);
            //设置字体颜色
            TextView tv = rllx.findViewById(R.id.tv_type);
            tv.setTextColor(Color.parseColor("#333333"));
        }
        if (zws != null) {
            // 取消之前选中
            zws.setSelected(false);
            //设置字体颜色
            TextView tv = zws.findViewById(R.id.tv_type);
            tv.setTextColor(Color.parseColor("#333333"));
        }
        if (!"".equals(type)){
            tvBrand.setText("不限>");
            tvCity.setText("不限>");
        }
    }

    public static void setOnDataBackListener(DataBackListener listenr) {
        dataBackListener = listenr;
    }
    static DataBackListener dataBackListener;
    public interface DataBackListener {
        void backData(Map<String, String> map);
    }

    // 订阅新加
    private String cityName = "";
    private String brandName = "", brandId = "";
    private String car = "", carId = "";

    @OnClick({R.id.tv_city, R.id.tv_brand})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_city:
                JumpUtil.newInstance().jumpLeft(this, CityActivity.class);
                CityActivity.setOnBackListener(new CityActivity.BackListener() {
                    @Override
                    public void backData(String city) {
                        if (city != null && !"".equals(city)) {
                            tvCity.setText(city);
                            cityName = tvCity.getText().toString();
                        }
                    }
                });
                break;
            case R.id.tv_brand:
                JumpUtil.newInstance().jumpLeft(this, CarBrandSearch.class);
                CarBrandSearch.setOnDataBackListener(new CarBrandSearch.DataBackListener() {
                    @Override
                    public void backData(Map<String, String> brand, Map<String, String> carName) {
                        /**
                         * carMap1.put("tv_carbrand", "A");
                         * carMap1.put("image_carbrand", "");
                         * carMap1.put("id_carbrand", "");
                         */
                        if (brand != null) {
                            String strBrand = brand.get("tv_carbrand");
                            if (strBrand != null && !"".equals(strBrand)) {
                                String str_car = "";
                                if (carName!=null)
                                    str_car = carName.get("tv_carbrand");
                                brandName = strBrand;
                                brandId = brand.get("id_carbrand");
                                if (str_car != null && !"".equals(str_car)) {
                                    tvBrand.setText(strBrand + " " + str_car);
                                    car = str_car;
                                    carId = carName.get("id_carbrand");
                                } else {
                                    tvBrand.setText(strBrand);
                                }
                            }
                        }
                    }
                });
                break;
        }
    }
}
