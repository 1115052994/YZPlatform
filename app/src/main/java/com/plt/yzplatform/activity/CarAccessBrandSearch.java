package com.plt.yzplatform.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.plt.yzplatform.R;
import com.plt.yzplatform.adapter.CommonRecyclerAdapter;
import com.plt.yzplatform.adapter.MultiTypeSupport;
import com.plt.yzplatform.adapter.ViewHolder;
import com.plt.yzplatform.base.BaseActivity;
import com.plt.yzplatform.config.Config;
import com.plt.yzplatform.entity.AccessBrandBean;
import com.plt.yzplatform.utils.JumpUtil;
import com.plt.yzplatform.utils.OKhttptils;
import com.plt.yzplatform.view.LetterSideBar;

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

public class CarAccessBrandSearch extends BaseActivity {

    @BindView(R.id.edit_search)
    EditText editSearch;
    @BindView(R.id.recyclerView_carBrand)
    RecyclerView recyclerViewCarBrand;
    @BindView(R.id.letter_side_bar)
    LetterSideBar letterSideBar;

    // 字母A-Z (id_carbrand,tv_carbrand)
    private List<Map<String, String>> recyclerList = new ArrayList<>();
    private CommonRecyclerAdapter recyclerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_access_brand_search);
        Window win = getWindow();
        WindowManager.LayoutParams params = win.getAttributes();
        win.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        ButterKnife.bind(this);
        initView();
        getData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setTitle("选择品牌");
        setLeftImageClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JumpUtil.newInstance().finishRightTrans(CarAccessBrandSearch.this);
            }
        });
    }
    private void initView(){
        // 搜索框
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!"".equals(s.toString())) {
                    // 模糊搜索品牌
                    //getSearchBrand(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

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
        recyclerAdapter = new CommonRecyclerAdapter(this, recyclerList, new MultiTypeSupport() {
            @Override
            public int getLayoutId(Object item, int position) {
                String letter = recyclerList.get(position).get("id_carbrand");
                if ("letter".equals(letter)) {
                    return R.layout.item_carletter;
                } else {
                    return R.layout.item_access_carbrandorchexi;
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
                    TextView name = holder.getView(R.id.tv_carbrand);
                    if (recyclerList.get(position).get("tv_carbrand") != null) {
                        name.setText(recyclerList.get(position).get("tv_carbrand"));
                    }

                }
            }
        };
        recyclerAdapter.setOnItemClickListener(new CommonRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                String letter = recyclerList.get(position).get("id_carbrand");
                if (!"letter".equals(letter)) {
                    // 搜索车系
                    JumpUtil.newInstance().jumpRight(CarAccessBrandSearch.this,CarAccessChexiSearch.class,recyclerList.get(position).get("id_carbrand"));
                    JumpUtil.newInstance().finishRightTrans(CarAccessBrandSearch.this);
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
    private void getData(){
//        Map<String,String> map = new HashMap<>();
//        map.put("id_carbrand","letter");
//        map.put("tv_carbrand","A");
//        recyclerList.add(map);
//        Map<String,String> map1 = new HashMap<>();
//        map1.put("id_carbrand","123");
//        map1.put("tv_carbrand","奔驰");
//        recyclerList.add(map1);
        getBrand();
    }

    private void getBrand(){
        Map<String, String> map = new HashMap<>();
        OKhttptils.post(this, Config.GETBRAND, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    JSONObject data = object.getJSONObject("data");
                    JSONObject result = data.getJSONObject("result");
                    Gson gson = new Gson();
                    recyclerList.clear();
                    Map<String,Map<String,String>> map= gson.fromJson(result.toString(),HashMap.class);
                    for(String s:map.keySet()) {
                        //Log.i("getBrand", "==="+s);
                        Map<String,String> letter = new HashMap<>();
                        letter.put("id_carbrand","letter");
                        letter.put("tv_carbrand",s);
                        recyclerList.add(letter);
                        JSONArray array = result.getJSONArray(s);
                        for (int i = 0;i<array.length();i++){
                            JSONObject object1 = (JSONObject) array.get(i);
                            //Log.i("getBrand.object1",object1.toString());
                            AccessBrandBean bean = gson.fromJson(object1.toString(), AccessBrandBean.class);
                            Map<String,String> brand = new HashMap<>();
                            brand.put("id_carbrand",bean.getBid());
                            brand.put("tv_carbrand",bean.getBname());
                            recyclerList.add(brand);
                        }
                    }
                    recyclerAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void fail(String response) {

            }
        });
    }

    @OnClick(R.id.cancle)
    public void onViewClicked() {
        JumpUtil.newInstance().finishRightTrans(this);
    }
}
