package com.xtzhangbinbin.jpq.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.adapter.CommonRecyclerAdapter;
import com.xtzhangbinbin.jpq.adapter.MultiTypeSupport;
import com.xtzhangbinbin.jpq.adapter.ViewHolder;
import com.xtzhangbinbin.jpq.base.BaseActivity;
import com.xtzhangbinbin.jpq.config.Config;
import com.xtzhangbinbin.jpq.utils.JumpUtil;
import com.xtzhangbinbin.jpq.utils.OKhttptils;
import com.xtzhangbinbin.jpq.utils.Prefs;
import com.xtzhangbinbin.jpq.view.LetterSideBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WeizhangCity extends BaseActivity {

    @BindView(R.id.recyclerView_city)
    RecyclerView recyclerViewCity;
    @BindView(R.id.letter_side_bar)
    LetterSideBar letterSideBar;
    @BindView(R.id.letter)
    RelativeLayout letter;

    private CommonRecyclerAdapter rvAdapter;
    private List<String> list = new ArrayList<>();
    private List<String> listId = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weizhang_city);
        ButterKnife.bind(this);
        initView();
        getData();
    }
    @Override
    protected void onStart() {
        super.onStart();
        setTitle("选择城市");
        setLeftImageClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JumpUtil.newInstance().finishRightTrans(WeizhangCity.this);
            }
        });
    }
    private void initView() {
        // 滚动字母条
        letter.getBackground().setAlpha(60);
        letterSideBar.setOnLetterTouchListener(new LetterSideBar.LetterTouchListener() {
            @Override
            public void touch(CharSequence letter, boolean isTouch) {
                String s = letter.toString();
                int index = list.indexOf(s);
                //recyclerViewCity.scrollToPosition(index);
                moveToPosition(index);
            }
        });
        /* recycler绑定adapter*/
//        list.add("A");
//        list.add("北京");
//        list.add("北京");
//        list.add("北京");
        rvAdapter = new CommonRecyclerAdapter(this, list,
                new MultiTypeSupport<String>() {

                    @Override
                    public int getLayoutId(String item, int position) {
                        if (item.matches("[A-Z]")) {
                            return R.layout.item_letter;
                        } else {
                            return R.layout.item_citys;
                        }
                    }
                }) {
            @Override
            public void convert(ViewHolder holder, Object item, int position) {
                if (((String) item).matches("[A-Z]")) {
                    TextView tv = holder.getView(R.id.letter);
                    tv.setText((String) item);
                } else {
                    TextView tv = holder.getView(R.id.city);
                    tv.setText((String) item);
                }
            }
        };
        //添加监听事件
        rvAdapter.setOnItemClickListener(new CommonRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                if (!list.get(position).matches("[A-Z]")){
                    seleted(listId.get(position),list.get(position));
                }
            }
        });
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        // 设置布局管理器
        recyclerViewCity.setLayoutManager(layoutManager);
        recyclerViewCity.setAdapter(rvAdapter);
        //recyclerViewCity.setNestedScrollingEnabled(false);
        recyclerViewCity.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                GridLayoutManager mLinearLayoutManager = (GridLayoutManager) recyclerViewCity.getLayoutManager();
                if (move) {
                    move = false;
                    //获取要置顶的项在当前屏幕的位置，mIndex是记录的要置顶项在RecyclerView中的位置
                    int n = mIndex - mLinearLayoutManager.findFirstVisibleItemPosition();
                    if (0 <= n && n < recyclerViewCity.getChildCount()) {
                        //获取要置顶的项顶部离RecyclerView顶部的距离
                        int top = recyclerViewCity.getChildAt(n).getTop();
                        //最后的移动
                        recyclerViewCity.scrollBy(0, top);
//                        //滑动字母条
//                        letterSideBar.setFirstVisibleLetter(list.get(mIndex));
                    }
                }
                //滚动字母条
                int first = mLinearLayoutManager.findFirstVisibleItemPosition();
                int last = mLinearLayoutManager.findLastVisibleItemPosition();
                for (int i = first; i < last; i++) {
                    if (list.get(i).toString().matches("[A-Z]")) {
                        letterSideBar.setVisibleLetter(list.get(i).toString());
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
        GridLayoutManager mLinearLayoutManager = (GridLayoutManager) recyclerViewCity.getLayoutManager();
        int firstItem = mLinearLayoutManager.findFirstVisibleItemPosition();
        int lastItem = mLinearLayoutManager.findLastVisibleItemPosition();
        //然后区分情况
        if (n <= firstItem) {
            //当要置顶的项在当前显示的第一个项的前面时
            recyclerViewCity.scrollToPosition(n);
        } else if (n <= lastItem) {
            //当要置顶的项已经在屏幕上显示时
            int top = recyclerViewCity.getChildAt(n - firstItem).getTop();
            recyclerViewCity.scrollBy(0, top);
        } else {
            //当要置顶的项在当前显示的最后一项的后面时
            recyclerViewCity.scrollToPosition(n);
            //这里这个变量是用在RecyclerView滚动监听里面的
            move = true;
        }
    }


    private void getData(){
        Map<String,String> map = new HashMap<>();
        OKhttptils.post(this, Config.GETCITY, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                Log.i("getCity",response);
                try {
                    JSONObject object = new JSONObject(response);
                    if ("1".equals(object.getString("status"))){
                        JSONObject data = object.getJSONObject("data");
                        JSONArray array = data.getJSONArray("result");
                        list.clear();
                        listId.clear();
                        String str = "A";
                        list.add(str);
                        listId.add("");
                        for (int i=0;i<array.length();i++){
                            JSONObject object1 = array.getJSONObject(i);
                            String letter = object1.getString("dict_5");
                            String cityName = object1.getString("dict_desc");
                            String cityId = object1.getString("dict_3");
                            if (!str.equals(letter)){
                                list.add(letter);
                                str = letter;
                                listId.add("");
                            }
                            listId.add(cityId);
                            list.add(cityName);
                        }
                    }
                    rvAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void fail(String response) {

            }
        });
    }

    private void seleted(String cityId,String name) {
        if (backListener!=null)
          backListener.backData(cityId,name);
        JumpUtil.newInstance().finishRightTrans(WeizhangCity.this);
    }


    static BackListener backListener;
    public static void setOnBackListener( BackListener listener){
        backListener = listener;
    }
    public interface BackListener{
        void backData(String cityId,String name);
    }
}
