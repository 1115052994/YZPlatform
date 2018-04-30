package com.plt.yzplatform.activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.plt.yzplatform.AppraiseInterface;
import com.plt.yzplatform.R;
import com.plt.yzplatform.adapter.BrandGVAdapter;
import com.plt.yzplatform.adapter.CommonRecyclerAdapter;
import com.plt.yzplatform.adapter.MultiTypeSupport;
import com.plt.yzplatform.adapter.ViewHolder;
import com.plt.yzplatform.base.BaseActivity;
import com.plt.yzplatform.config.Config;
import com.plt.yzplatform.entity.CarChexiBean;
import com.plt.yzplatform.entity.CarHotBrand;
import com.plt.yzplatform.entity.CarSearchBrandBean;
import com.plt.yzplatform.utils.CommonUtils;
import com.plt.yzplatform.utils.JumpUtil;
import com.plt.yzplatform.utils.OKhttptils;
import com.plt.yzplatform.utils.Prefs;
import com.plt.yzplatform.view.ExpandableGridView;
import com.plt.yzplatform.view.LetterSideBar;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class CarBrandSearch extends BaseActivity {

    @BindView(R.id.edit_search)
    EditText editSearch;
    @BindView(R.id.cancle)
    TextView cancle;
    @BindView(R.id.gv_sshis)
    ExpandableGridView gvSshis;
    @BindView(R.id.gv_sshot)
    ExpandableGridView gvSshot;
    @BindView(R.id.recyclerView_carBrand)
    RecyclerView recyclerViewCarBrand;
    @BindView(R.id.search)
    RelativeLayout search;
    @BindView(R.id.letter_side_bar)
    LetterSideBar letterSideBar;

    //搜索历史
    private List<Map<String, String>> hisList = new ArrayList<>();
    private BrandGVAdapter hisAdapter;
    //热门品牌
    private List<Map<String, String>> hotList = new ArrayList<>();
    private BrandGVAdapter hotAdapter;
    // 字母A-Z
    private List<Map<String, String>> recyclerList = new ArrayList<>();
    private CommonRecyclerAdapter recyclerAdapter;

    // 模糊搜索品牌
    private List<Map<String, String>> searchBrandList = new ArrayList<>();
    //通过品牌搜车系
    private List<CarChexiBean.DataBean.ResultBean.TrainListBean> searchChexiList = new ArrayList<>();
    /**
     * carMap1.put("tv_carbrand", "A");
     * carMap1.put("image_carbrand", "");
     * carMap1.put("id_carbrand", "");
     */

    private View hisView = null;
    private View hotView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_brandsearch);
        ButterKnife.bind(this);
        initView();
        getData();
    }

    private void initView() {
        // 搜索框
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!"".equals(s.toString())) {
                    // 模糊搜索品牌
                    getSearchBrand(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // 搜索历史
        String his = Prefs.with(getApplicationContext()).read("historyCarSearch");
        if (his!=null&&!"".equals(his)) {
            String[] history = his.split(",");
            Map<String, String> map = new HashMap<>();
            if (history[0] != null && !"".equals(history[0])) {
                map.put("tv_carbrand", history[0]);
                map.put("id_carbrand", history[1]);
                map.put("image_carbrand", history[2]);
                hisList.add(map);
                Log.i("historyCarSearch","map="+map.toString());
            }
        }
        hisAdapter = new BrandGVAdapter(this, hisList);
        hisAdapter.setOnItemClickListener(new AppraiseInterface() {
            @Override
            public void onClick(View view, int position) {
                //响应单击事件
                //设置边框颜色
//                if(hisView!=null)
//                    hisView.setSelected(false);
//                view.setSelected(true);
//                hisView = view;
//                //设置字体颜色
//                TextView tv = view.findViewById(R.id.tv_carType);
//                tv.setTextColor(Color.parseColor("#f35454"));
                // 搜索车系
                getChexiByBrand(hisList.get(position).get("tv_carbrand"), hisList.get(position).get("id_carbrand"), hisList.get(position).get("image_carbrand"));
            }
        });
        gvSshis.setAdapter(hisAdapter);

        //热门
        hotAdapter = new BrandGVAdapter(this, hotList);
        hotAdapter.setOnItemClickListener(new AppraiseInterface() {
            @Override
            public void onClick(View view, int position) {
                //响应单击事件
                //设置边框颜色
//                if(hotView!=null)
//                    hotView.setSelected(false);
//                view.setSelected(true);
//                hotView = view;
//                //设置字体颜色
//                TextView tv = view.findViewById(R.id.tv_carType);
//                tv.setTextColor(Color.parseColor("#f35454"));
                // 搜索车系
                getChexiByBrand(hotList.get(position).get("tv_carbrand"), hotList.get(position).get("id_carbrand"), hotList.get(position).get("image_carbrand"));
            }
        });
        gvSshot.setAdapter(hotAdapter);


        // A-Z车品牌
        // 字母条监听
        letterSideBar.setOnLetterTouchListener(new LetterSideBar.LetterTouchListener() {
            @Override
            public void touch(CharSequence letter, boolean isTouch) {
                String s = letter.toString();
                Map<String, String> map;
                int index = 0;
                for (int i = 0; i < recyclerList.size(); i++) {
                    map = recyclerList.get(i);
                    if (s.equals(map.get("tv_carbrand"))) {
                        index = i;
                    }
                }
                //recyclerViewCarBrand.scrollToPosition(index);
                moveToPosition(index);
            }
        });
        findViewById(R.id.tv_all).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击了不限品牌
                Log.i("carbrand", "点击了不限品牌");
                selectedBrand.clear();
                selectedBrand.put("tv_carbrand", "");
                selectedBrand.put("id_carbrand", "");
                selectedCarName.clear();
                selectedCarName.put("tv_carbrand", "");
                selectedCarName.put("id_carbrand", "");
                selected();
            }
        });
        recyclerAdapter = new CommonRecyclerAdapter(this, recyclerList, new MultiTypeSupport() {
            @Override
            public int getLayoutId(Object item, int position) {
                String letter = recyclerList.get(position).get("id_carbrand");
                if ("letter".equals(letter)) {
                    return R.layout.item_carletter;
                } else {
                    return R.layout.item_carbrand;
                }
            }
        }) {
            @Override
            public void convert(ViewHolder holder, Object item, int position) {
                if ("letter".equals(recyclerList.get(position).get("id_carbrand"))) {
                    // 字母
                    TextView letter = holder.getView(R.id.car_letter);
                    letter.setText(recyclerList.get(position).get("tv_carbrand"));
                } else {
                    // 品牌
                    ImageView brand = holder.getView(R.id.image_carbrand);
                    TextView name = holder.getView(R.id.tv_carbrand);
                    if (recyclerList.get(position).get("image_carbrand") != null) {
                        String url = Config.BASE_URL + Config.Y + recyclerList.get(position).get("image_carbrand");
                        brand.setImageBitmap(null);
                        Picasso.with(CarBrandSearch.this).load(Uri.parse(url)).into(brand);
                    }
                    if (recyclerList.get(position).get("tv_carbrand") != null) {
                        name.setText(recyclerList.get(position).get("tv_carbrand"));
                    }

                }
            }
        };
        recyclerAdapter.setOnItemClickListener(new CommonRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                // 搜索车系
                String letter = recyclerList.get(position).get("id_carbrand");
                if (!"letter".equals(letter)) {
                    getChexiByBrand(recyclerList.get(position).get("tv_carbrand"), recyclerList.get(position).get("id_carbrand"), recyclerList.get(position).get("image_carbrand"));
                }
            }
        });
        recyclerViewCarBrand.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCarBrand.setAdapter(recyclerAdapter);
        // 滑动让字母条滚动
        recyclerViewCarBrand.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                LinearLayoutManager mLinearLayoutManager = (LinearLayoutManager) recyclerViewCarBrand.getLayoutManager();
                if (move) {
                    move = false;
                    //获取要置顶的项在当前屏幕的位置，mIndex是记录的要置顶项在RecyclerView中的位置
                    int n = mIndex - mLinearLayoutManager.findFirstVisibleItemPosition();
                    if (0 <= n && n < recyclerViewCarBrand.getChildCount()) {
                        //获取要置顶的项顶部离RecyclerView顶部的距离
                        int top = recyclerViewCarBrand.getChildAt(n).getTop();
                        //最后的移动
                        recyclerViewCarBrand.scrollBy(0, top);
//                        //滑动字母条
//                        letterSideBar.setFirstVisibleLetter(list.get(mIndex));
                    }
                }
                //滚动字母条
                int first = mLinearLayoutManager.findFirstVisibleItemPosition();
                int last = mLinearLayoutManager.findLastVisibleItemPosition();
                for (int i = first; i < last; i++) {
                    if (recyclerList.get(i).get("tv_carbrand").toString().matches("[A-Z]")) {
                        letterSideBar.setVisibleLetter(recyclerList.get(i).get("tv_carbrand").toString());
                        break;
                    }
                }
            }
        });
    }


    @OnClick(R.id.cancle)
    public void onViewClicked() {
        dataBackListener.backData(null, null);
        JumpUtil.newInstance().finishRightTrans(this);
    }

    // 模糊搜索品牌
    public void getSearchBrand(String str) {
        Map<String, String> map = new HashMap<>();
        map.put("brandName", str);
        OKhttptils.post(this, Config.GETCARBRANDDICT, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                Log.i("response", response);
                try {
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if ("1".equals(status)) {
                        Gson gson = new Gson();
                        CarSearchBrandBean searchCarBrand = gson.fromJson(response, CarSearchBrandBean.class);
                        List<CarSearchBrandBean.DataBean.ResultBean.BrandListBean> searchCarBrandList = searchCarBrand.getData().getResult().getBrandList();
                        searchBrandList.clear();
                        for (CarSearchBrandBean.DataBean.ResultBean.BrandListBean bean : searchCarBrandList) {
                            Map<String, String> carMap1 = new HashMap<>();
                            carMap1.put("tv_carbrand", bean.getBrandName());
                            carMap1.put("image_carbrand", bean.getBrandImg());
                            carMap1.put("id_carbrand", bean.getBrandId());
                            searchBrandList.add(carMap1);
                        }
                        // 弹出品牌搜索结果
                        popupBrandSearchWindow();
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


    private void popupBrandSearchWindow() {
        // 用于PopupWindow的View
        View contentView = LayoutInflater.from(this).inflate(R.layout.popupwindow, null, false);
        RecyclerView recyclerView = contentView.findViewById(R.id.recyclerView_popup);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        CommonRecyclerAdapter adapter = new CommonRecyclerAdapter(this, searchBrandList, R.layout.item_carbrand) {
            @Override
            public void convert(ViewHolder holder, Object item, int position) {
                TextView tv = holder.getView(R.id.tv_carbrand);
                ImageView imageView = holder.getView(R.id.image_carbrand);
                Map<String, String> map = (Map<String, String>) item;
                tv.setText(map.get("tv_carbrand"));
                //OKhttptils.getPic(CarBrandSearch.this,map.get("image_carbrand"),imageView);
                if (!"".equals(map.get("image_carbrand"))) {
                    String url = Config.BASE_URL + Config.Y + map.get("image_carbrand");
                    imageView.setImageBitmap(null);
                    Picasso.with(CarBrandSearch.this).load(Uri.parse(url)).into(imageView);
                }
            }
        };
        // 创建PopupWindow对象，其中：
        // 第一个参数是用于PopupWindow中的View，第二个参数是PopupWindow的宽度，
        // 第三个参数是PopupWindow的高度，第四个参数指定PopupWindow能否获得焦点
        final PopupWindow window = new PopupWindow(contentView, RelativeLayout.LayoutParams.MATCH_PARENT, /*CommonUtils.dip2px(CityActivity.this,150)*/RelativeLayout.LayoutParams.WRAP_CONTENT, true);
        // 设置PopupWindow的背景
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // 设置PopupWindow是否能响应外部点击事件
        window.setOutsideTouchable(true);
        // 设置PopupWindow是否能响应点击事件
        window.setTouchable(true);
        // 显示PopupWindow，其中：
        // 第一个参数是PopupWindow的锚点，第二和第三个参数分别是PopupWindow相对锚点的x、y偏移
        window.showAsDropDown(search, 0, 0);
        // 或者也可以调用此方法显示PopupWindow，其中：
        // 第一个参数是PopupWindow的父View，第二个参数是PopupWindow相对父View的位置，
        // 第三和第四个参数分别是PopupWindow相对父View的x、y偏移
        // window.showAtLocation(parent, gravity, x, y);
        // 设置背景颜色变暗
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        lp.verticalMargin = CommonUtils.dip2px(this, 30);
        lp.alpha = 0.9f;
        this.getWindow().setAttributes(lp);
        //设置点击监听
        adapter.setOnItemClickListener(new CommonRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                window.dismiss();
                //通过品牌搜索车系
                getChexiByBrand(searchBrandList.get(position).get("tv_carbrand"), searchBrandList.get(position).get("id_carbrand"), searchBrandList.get(position).get("image_carbrand"));
            }
        });
        recyclerView.setAdapter(adapter);
    }

    // 通过品牌查出车系弹出popwindow
    List<Map<String, String>> carList = new ArrayList<>();

    //TODO    修改右边popowindow图片
    private void popSearchChexi(final String brandName, final String brandId, final String imageId) {
        carList.clear();
        Map<String, String> brand = new HashMap<>();
        brand.put("tv_carbrand", brandName);
        brand.put("id_carbrand", brandId);
        carList.add(brand);
        for (CarChexiBean.DataBean.ResultBean.TrainListBean bean : searchChexiList) {
            List<CarChexiBean.DataBean.ResultBean.TrainListBean.CarTrianerMapListBean> carTrianerMapListBean = bean.getCarTrianerMapList();
            for (CarChexiBean.DataBean.ResultBean.TrainListBean.CarTrianerMapListBean car : carTrianerMapListBean) {
                Map<String, String> map = new HashMap<>();
                map.put("tv_carbrand", car.getBrandName());
                map.put("image_carbrand", car.getBrandImg());
                map.put("id_carbrand", car.getBrandId());
                map.put("tv_num", "约" + car.getCarNum() + "辆");
                carList.add(map);
            }
        }
        // 用于PopupWindow的View
        View contentView = LayoutInflater.from(this).inflate(R.layout.popupwindow, null, false);
        RecyclerView recyclerView = contentView.findViewById(R.id.recyclerView_popup);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));//R.layout.item_car_chexi
        // 创建PopupWindow对象，其中：
        // 第一个参数是用于PopupWindow中的View，第二个参数是PopupWindow的宽度，
        // 第三个参数是PopupWindow的高度，第四个参数指定PopupWindow能否获得焦点
        WindowManager wm = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        final PopupWindow window = new PopupWindow(contentView, width / 4 * 3, height/*-search.getHeight()- CommonUtils.getStatusBarHeight(this)*/, true);
        // 设置PopupWindow的背景
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // 设置PopupWindow是否能响应外部点击事件
        window.setOutsideTouchable(true);
        // 设置PopupWindow是否能响应点击事件
        window.setTouchable(true);
        // 显示PopupWindow，其中：
        // 第一个参数是PopupWindow的锚点，第二和第三个参数分别是PopupWindow相对锚点的x、y偏移
        //window.showAsDropDown(search, width / 4, 0);
        // 或者也可以调用此方法显示PopupWindow，其中：
        // 第一个参数是PopupWindow的父View，第二个参数是PopupWindow相对父View的位置，
        // 第三和第四个参数分别是PopupWindow相对父View的x、y偏移
        View parent = (View) search.getParent().getParent();
        window.showAtLocation(parent, Gravity.TOP | Gravity.RIGHT, 0, 0);
        // 设置背景颜色变暗
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        //lp.verticalMargin = CommonUtils.dip2px(this, 30);
        lp.alpha = 0.9f;
        this.getWindow().setAttributes(lp);
        //设置点击监听
        CommonRecyclerAdapter adapter = new CommonRecyclerAdapter(this, carList, new MultiTypeSupport() {
            @Override
            public int getLayoutId(Object item, int position) {
                if (0 == position) {
                    return R.layout.item_brand;
                } else {
                    return R.layout.item_car_chexi;
                }
            }
        }) {
            @Override
            public void convert(ViewHolder holder, Object item, int position) {
                Map<String, String> map = (Map<String, String>) item;
                if (0 == position) {
                    TextView tv_carbrand = holder.getView(R.id.tv_carbrand);
                    tv_carbrand.setText(brandName);
                    ImageView imageView = holder.getView(R.id.image_carbrand);
                    String url = Config.BASE_URL + Config.Y + imageId;
                    Log.i("historyCarSearch","imageId="+imageId);
                    imageView.setImageBitmap(null);
                    Picasso.with(CarBrandSearch.this).load(url).into(imageView);
                    TextView tv_all = holder.getView(R.id.tv_all);
                    tv_all.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //点击了不限车系
                            Log.i("carbrand", "点击了不限车系");
                            selectedCarName.clear();
                            selectedCarName.put("tv_carbrand", "");
                            selectedCarName.put("id_carbrand", "");
                            selected();
                            window.dismiss();
                        }
                    });
                } else {
                    TextView tv_carbrand = holder.getView(R.id.tv_carbrand);
                    TextView tv_num = holder.getView(R.id.tv_num);
                    ImageView imageView = holder.getView(R.id.image_carbrand);
                    tv_carbrand.setText(map.get("tv_carbrand"));
                    tv_num.setText(map.get("tv_num"));
                    //if(!"".equals(map.get("image_carbrand"))) {
                    String url = Config.BASE_URL + Config.Y + map.get("image_carbrand");
                    imageView.setImageBitmap(null);
                    Picasso.with(CarBrandSearch.this).load(url).into(imageView);
                    //}
                }
            }
        };
        adapter.setOnItemClickListener(new CommonRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                if (position != 0) {
                    Log.i("carbrand", position + "--" + carList.get(position).get("tv_carbrand"));
                    selectedCarName.clear();
                    selectedCarName.put("tv_carbrand", carList.get(position).get("tv_carbrand"));
                    selectedCarName.put("id_carbrand", carList.get(position).get("id_carbrand"));
                    selected();
                    window.dismiss();
                }
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void getData() {
        // 获得热门品牌
        getHotBrand();
        // 通过字母获得品牌
        getCarBrandDictByLetter();
    }

    // 获得热门品牌
    private void getHotBrand() {
        Map<String, String> map = new HashMap<>();
        OKhttptils.post(this, Config.GETHOTCARBRANDDICT, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                Log.i("getHotBrand", response);
                JSONObject object = null;
                try {
                    object = new JSONObject(response);
                    String status = object.getString("status");
                    if ("1".equals(status)) {
                        Gson gson = new Gson();
                        CarHotBrand carHotBrand = gson.fromJson(response, CarHotBrand.class);
                        List<CarHotBrand.DataBean.ResultBean.HotBrandListBean> list = carHotBrand.getData().getResult().getHotBrandList();
                        hotList.clear();
                        for (CarHotBrand.DataBean.ResultBean.HotBrandListBean bean : list
                                ) {
                            if (hotList.size() == 10)
                                break;
                            Map<String, String> carMap1 = new HashMap<>();
                            carMap1.put("tv_carbrand", bean.getBrandName());
                            carMap1.put("image_carbrand", bean.getBrandImg());
                            carMap1.put("id_carbrand", bean.getBrandId());
                            hotList.add(carMap1);
                        }
                        hotAdapter.notifyDataSetChanged();
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

    // 查询字母品牌
    private void getCarBrandDictByLetter() {
        Map<String, String> map = new HashMap<>();
        map.put("brandName", "");
        OKhttptils.post(this, Config.GETCARBRANDDICT, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                Log.i("response", response);
                try {
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if ("1".equals(status)) {
                        Gson gson = new Gson();
                        CarSearchBrandBean searchCarBrand = gson.fromJson(response, CarSearchBrandBean.class);
                        List<CarSearchBrandBean.DataBean.ResultBean.BrandListBean> searchCarBrandList = searchCarBrand.getData().getResult().getBrandList();
                        recyclerList.clear();
                        String letter = "";
                        for (CarSearchBrandBean.DataBean.ResultBean.BrandListBean bean : searchCarBrandList) {
                            String firstChar = bean.getFristChar();
                            if (!letter.equals(firstChar)) {
                                Map<String, String> carMap = new HashMap<>();
                                carMap.put("tv_carbrand", bean.getFristChar());
                                carMap.put("image_carbrand", "");
                                carMap.put("id_carbrand", "letter");
                                recyclerList.add(carMap);
                                letter = firstChar;
                            }
                            Map<String, String> carMap1 = new HashMap<>();
                            carMap1.put("tv_carbrand", bean.getBrandName());
                            carMap1.put("image_carbrand", bean.getBrandImg());
                            carMap1.put("id_carbrand", bean.getBrandId());
                            recyclerList.add(carMap1);
                        }
                        recyclerAdapter.notifyDataSetChanged();
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

    // 通过品牌查车系
    private void getChexiByBrand(final String brandName, final String brandId, final String imageId) {
        // 保存选中的品牌
        selectedBrand.clear();
        selectedBrand.put("tv_carbrand", brandName);
        selectedBrand.put("id_carbrand", brandId);
        selectedBrand.put("image_carbrand", imageId);
        Map<String, String> map = new HashMap<>();
        map.put("brandId", brandId);
        OKhttptils.post(this, Config.GETCARTRIANBYCARBRAND, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                JSONObject object = null;
                try {
                    object = new JSONObject(response);
                    String status = object.getString("status");
                    if ("1".equals(status)) {
                        Gson gson = new Gson();
                        CarChexiBean chexi = gson.fromJson(response, CarChexiBean.class);
                        List<CarChexiBean.DataBean.ResultBean.TrainListBean> trainList = chexi.getData().getResult().getTrainList();
                        searchChexiList.clear();
                        for (CarChexiBean.DataBean.ResultBean.TrainListBean bean : trainList) {
                            searchChexiList.add(bean);
                        }
                        popSearchChexi(brandName, brandId, imageId);
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


    /**
     * carMap1.put("tv_carbrand", "A");
     * carMap1.put("image_carbrand", "");
     * carMap1.put("id_carbrand", "");
     */
    // 字符串为空时表示不限
    // 选中了品牌返回数据
    private Map<String, String> selectedBrand = new HashMap<>();
    //private Map<String,String> selectedChexi = new HashMap<>();
    private Map<String, String> selectedCarName = new HashMap<>();

    private void selected() {
        dataBackListener.backData(selectedBrand, selectedCarName);
        JumpUtil.newInstance().finishLeftTrans(this);
        Prefs.with(getApplicationContext()).write("historyCarSearch", selectedBrand.get("tv_carbrand") + "," + selectedBrand.get("id_carbrand") + "," + selectedBrand.get("image_carbrand"));
    }


    // recyclerview滑动到指定位置
    private boolean move = false;
    private int mIndex = 0;

    private void moveToPosition(int n) {
        mIndex = n;
        //先从RecyclerView的LayoutManager中获取第一项和最后一项的Position
        LinearLayoutManager mLinearLayoutManager = (LinearLayoutManager) recyclerViewCarBrand.getLayoutManager();
        int firstItem = mLinearLayoutManager.findFirstVisibleItemPosition();
        int lastItem = mLinearLayoutManager.findLastVisibleItemPosition();
        //然后区分情况
        if (n <= firstItem) {
            //当要置顶的项在当前显示的第一个项的前面时
            recyclerViewCarBrand.scrollToPosition(n);
        } else if (n <= lastItem) {
            //当要置顶的项已经在屏幕上显示时
            int top = recyclerViewCarBrand.getChildAt(n - firstItem).getTop();
            recyclerViewCarBrand.scrollBy(0, top);
        } else {
            //当要置顶的项在当前显示的最后一项的后面时
            recyclerViewCarBrand.scrollToPosition(n);
            //这里这个变量是用在RecyclerView滚动监听里面的
            move = true;
        }
    }


    public static void setOnDataBackListener(DataBackListener listenr) {
        dataBackListener = listenr;
    }

    static DataBackListener dataBackListener;

    public interface DataBackListener {
        void backData(Map<String, String> brand, Map<String, String> carName);
    }
}
