package com.plt.yzplatform.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.plt.yzplatform.R;
import com.plt.yzplatform.adapter.BrandRecyclerAdapter;
import com.plt.yzplatform.adapter.MultiTypeSupport;
import com.plt.yzplatform.adapter.ViewHolder;
import com.plt.yzplatform.base.BaseActivity;
import com.plt.yzplatform.config.Config;
import com.plt.yzplatform.entity.CarChexiBean;
import com.plt.yzplatform.entity.CarSearchBrandBean;
import com.plt.yzplatform.utils.CommonUtils;
import com.plt.yzplatform.utils.OKhttptils;
import com.plt.yzplatform.view.LetterSideBar;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ModelsChoose extends BaseActivity {


    @BindView(R.id.recyclerView_carBrand)
    RecyclerView recyclerViewCarBrand;
    @BindView(R.id.parent)
    CoordinatorLayout parent;
    @BindView(R.id.letter_side_bar)
    LetterSideBar letterSideBar;
    // 字母A-Z
    private List<Map<String, String>> recyclerList = new ArrayList<>();
    private BrandRecyclerAdapter recyclerAdapter;
    private String name;

    //通过品牌搜车系
    private List<CarChexiBean.DataBean.ResultBean.TrainListBean> searchChexiList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_models_choose);
        ButterKnife.bind(this);
        initView();
        // 通过字母获得品牌
        getCarBrandDictByLetter();
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
    }
    private void initView() {
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
                moveToPosition(index);
            }
        });

        recyclerAdapter = new BrandRecyclerAdapter(this, recyclerList, new MultiTypeSupport() {
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
                        Picasso.with(ModelsChoose.this).load(Uri.parse(url)).into(brand);
                    }
                    if (recyclerList.get(position).get("tv_carbrand") != null) {
                        name.setText(recyclerList.get(position).get("tv_carbrand"));
                    }

                }
            }
        };
        recyclerAdapter.setOnItemClickListener(new BrandRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                // 搜索车系
                Log.i("carbrand", position + "---" + recyclerList.get(position).get("tv_carbrand"));
                getChexiByBrand(recyclerList.get(position).get("tv_carbrand"), recyclerList.get(position).get("id_carbrand"),recyclerList.get(position).get("image_carbrand"));
            }
        });
        recyclerViewCarBrand.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCarBrand.setAdapter(recyclerAdapter);
        // 滑动事让字母条滚动
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


    // 通过品牌查出车系弹出popwindow
    List<Map<String, String>> carList = new ArrayList<>();

    private void popSearchChexi(final String brandName, String brandId,final String imageId) {
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
//        window.showAsDropDown(search, width / 4, CommonUtils.dip2px(BrandChoice.this, 25));
        // 或者也可以调用此方法显示PopupWindow，其中：
        // 第一个参数是PopupWindow的父View，第二个参数是PopupWindow相对父View的位置，
        // 第三和第四个参数分别是PopupWindow相对父View的x、y偏移
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
        lp.verticalMargin = CommonUtils.dip2px(this, 30);
        lp.alpha = 0.9f;
        this.getWindow().setAttributes(lp);
        //设置点击监听
        BrandRecyclerAdapter adapter = new BrandRecyclerAdapter(this, carList, new MultiTypeSupport() {
            @Override
            public int getLayoutId(Object item, int position) {
                if (0 == position) {
                    //TODO
                    return R.layout.item_popupwindow;
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
                    String url = Config.BASE_URL +  Config.Y +imageId;
                    imageView.setImageBitmap(null);
                    Picasso.with(ModelsChoose.this).load(url).into(imageView);
                } else {
                    TextView tv_carbrand = holder.getView(R.id.tv_carbrand);
                    TextView tv_num = holder.getView(R.id.tv_num);
                    ImageView imageView = holder.getView(R.id.image_carbrand);
                    tv_carbrand.setText(map.get("tv_carbrand"));
                    tv_num.setText(map.get("tv_num"));
                    //if(!"".equals(map.get("image_carbrand"))) {
                    String url = Config.BASE_URL + Config.Y + map.get("image_carbrand");
                    imageView.setImageBitmap(null);
                    Picasso.with(ModelsChoose.this).load(url).into(imageView);
                    //}
                }
            }
        };
        adapter.setOnItemClickListener(new BrandRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                if (position != 0) {
                    Log.d("aaaaa", "onClick: " + carList.get(position).get("tv_carbrand") + "------" + carList.get(position).get("id_carbrand"));
                    selected(carList.get(position).get("tv_carbrand"), carList.get(position).get("id_carbrand"),CarChoose.class);
                    window.dismiss();
                }
            }
        });
        recyclerView.setAdapter(adapter);
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
    private void getChexiByBrand(final String brandName, final String brandId,final String imageId) {
        // 保存选中的品牌
        selectedBrand.clear();
        selectedBrand.put("tv_carbrand", brandName);
        selectedBrand.put("id_carbrand", brandId);
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
                        popSearchChexi(brandName, brandId,imageId);
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

    private void selected(String tv_carbrand, String id_carbrand,Class clas) {
        Intent intent = new Intent(this, clas);
        Bundle bundle = new Bundle();
        bundle.putString("tv_carbrand", tv_carbrand);
        bundle.putString("id_carbrand", id_carbrand);
        Log.d("aaaaa", "selected: "+tv_carbrand+"-----------"+id_carbrand);
        if(!name.isEmpty()){
            intent.putExtra("name",name);
        }
        intent.putExtra("bundle", bundle);
        startActivity(intent);
        finish();
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

}
