package com.xtzhangbinbin.jpq.activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.base.BaseActivity;
import com.xtzhangbinbin.jpq.config.Config;
import com.xtzhangbinbin.jpq.entity.CompAddServiceType;
import com.xtzhangbinbin.jpq.gson.factory.GsonFactory;
import com.xtzhangbinbin.jpq.utils.CommonUtils;
import com.xtzhangbinbin.jpq.utils.JumpUtil;
import com.xtzhangbinbin.jpq.utils.OKhttptils;
import com.xtzhangbinbin.jpq.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/* 产品添加-服务项 */
public class ProductServiceActivity extends BaseActivity {

    private static final String TAG = "添加服务项";
    @BindView(R.id.mSearch)
    EditText mSearch;
    @BindView(R.id.mList)
    ExpandableListView mList;
    @BindView(R.id.search)
    RelativeLayout search;
    private String type;
    private int index;

    private List<CompAddServiceType.DataBean.ResultBean> beanList = new ArrayList<>();
    private List<String> parentList = new ArrayList<>();
    private List<String> childList = new ArrayList<>();
    private Map<String, List<String>> dataset = new HashMap<>();
    private MyExpandableListViewAdapter adapter;
    private ArrayAdapter searchAdapter;

    private List<String> s1 = new ArrayList<>();
    private List<String> s2 = new ArrayList<>();
    private List<String> s3 = new ArrayList<>();
    private List<String> keywords = new ArrayList<>();
    private PopupWindow popupWindow;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 001:
                    beanList = (List<CompAddServiceType.DataBean.ResultBean>) msg.obj;
                    for (int i = 0; i < beanList.size(); i++) {
                        parentList.add(beanList.get(i).getTypeName());
                        childList = beanList.get(i).getSuppostList();
                        for (int j = 0; j < childList.size(); j++) {
                            keywords.add(childList.get(i));
                        }
                        dataset.put(parentList.get(i), childList);
                    }
                    Log.d(TAG, "handleMessage: " + dataset.toString());
                    initAdapter();
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_service);
        ButterKnife.bind(this);
        Window win = getWindow();
        WindowManager.LayoutParams params = win.getAttributes();
        win.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        Bundle bundle = getIntent().getExtras();
        type = bundle.getString("type");
        index = bundle.getInt("index");
        getData();
        initView();
    }

    private void initView() {
        mSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, final int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String suppostWord = mSearch.getText().toString();
                if (!suppostWord.isEmpty()){
                    Map<String, String> map = new HashMap<>();
                    Log.i(TAG, "getData关键词: " + suppostWord);
                    map.put("suppostWord", suppostWord);
                    OKhttptils.post(ProductServiceActivity.this, Config.ADD_COMP_SERVICE_TYPE, map, new OKhttptils.HttpCallBack() {
                        @Override
                        public String success(String response) {
                            keywords.clear();
                            Log.e(TAG, "success: " + response);
                            Gson gson = GsonFactory.create();
                            CompAddServiceType addServiceType = gson.fromJson(response, CompAddServiceType.class);
                            List<CompAddServiceType.DataBean.ResultBean> beanList = addServiceType.getData().getResult();
                            s1 = beanList.get(0).getSuppostList();
                            s2 = beanList.get(1).getSuppostList();
                            s3 = beanList.get(2).getSuppostList();
                            for (int j = 0; j < s1.size() ; j++) {
                                keywords.add(s1.get(j));
                            }
                            Log.e(TAG, "success关键词: " + keywords.toString());

                            for (int j = 0; j < s2.size() ; j++) {
                                keywords.add(s2.get(j));
                            }
                            for (int j = 0; j < s3.size(); j++) {
                                keywords.add(s3.get(j));
                            }

                            Log.e(TAG, "success关键词: " + s1.toString());
                            Log.e(TAG, "success关键词: " + s2.toString());
                            Log.e(TAG, "success关键词: " + s3.toString());
                            Log.e(TAG, "success关键词: " + keywords.toString());
                            if (keywords.size() > 0) {
                                showPopup();
                            } else {
                                ToastUtil.show(ProductServiceActivity.this, "未查询到相关信息");
                            }
                            return response;
                        }

                        @Override
                        public void fail(String response) {
                            ToastUtil.noNAR(ProductServiceActivity.this);
                        }
                    });
                }
            }
        });
    }

    /* 弹出搜索list */
    private void showPopup() {
        View view = LayoutInflater.from(this).inflate(R.layout.popup_service_search, null, false);
        ListView listView = view.findViewById(R.id.mList);
        SearchAdapter mAdapter = new SearchAdapter(keywords);
        listView.setAdapter(mAdapter);
        popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT,
                CommonUtils.dip2px(ProductServiceActivity.this,200), false);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchable(true);
        popupWindow.showAsDropDown(search, 0, 0);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = ProductServiceActivity.this.getWindow().getAttributes();
                lp.alpha = 1f;
                ProductServiceActivity.this.getWindow().setAttributes(lp);
            }
        });
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        lp.verticalMargin = CommonUtils.dip2px(ProductServiceActivity.this, 30);
        lp.alpha = 0.9f;
        this.getWindow().setAttributes(lp);
        listView.setAdapter(mAdapter);
    }


    /* 填充adapter */
    private void initAdapter() {

        mList.setGroupIndicator(null);
        Log.d(TAG, "initAdapter: " + dataset.get(parentList.get(0)));
        Log.i(TAG, "initAdapter: " + childList.size());
        adapter = new MyExpandableListViewAdapter();
        mList.setAdapter(adapter);
        //设置列表默认全部展开
        for (int i = 0; i < adapter.getGroupCount(); i++) {
            mList.expandGroup(i);
        }

        //设置父节点(章目录)不可点击
        mList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;//返回true,表示不可点击
            }
        });
    }

    /* 获取数据 */
    private void getData() {
        Map<String, String> map = new HashMap<>();
        String suppostWord = mSearch.getText().toString();
        Log.i(TAG, "getData: " + suppostWord);
        map.put("suppostWord", "");
        OKhttptils.post(this, Config.ADD_COMP_SERVICE_TYPE, map, new OKhttptils.HttpCallBack() {
            @Override
            public String success(String response) {
                Log.e(TAG, "success: " + response);
                Gson gson = GsonFactory.create();
                CompAddServiceType addServiceType = gson.fromJson(response, CompAddServiceType.class);
                List<CompAddServiceType.DataBean.ResultBean> beanList = addServiceType.getData().getResult();
                Message message = new Message();
                message.what = 001;
                message.obj = beanList;
                handler.sendMessage(message);
                return response;
            }

            @Override
            public void fail(String response) {
                ToastUtil.noNAR(ProductServiceActivity.this);
            }
        });
    }

    @OnClick({R.id.left_back, R.id.mQuit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left_back:
                JumpUtil.newInstance().finishRightTrans(ProductServiceActivity.this);
                break;
            case R.id.mQuit:

                break;
        }
    }


    class MyExpandableListViewAdapter extends BaseExpandableListAdapter {

        public MyExpandableListViewAdapter() {
        }

        @Override
        public int
        getGroupCount() {
            return dataset.size();
        }

        @Override
        public int getChildrenCount(int i) {
            return dataset.get(parentList.get(i)).size();
        }

        @Override
        public Object getGroup(int i) {
            return dataset.get(parentList.get(i));
        }

        @Override
        public Object getChild(int i, int i1) {
            return dataset.get(i).get(i1);
        }

        @Override
        public long getGroupId(int i) {
            return i;
        }

        @Override
        public long getChildId(int i, int i1) {
            return i1;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) ProductServiceActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.item_parent_type, null);
            }
            view.setTag(R.layout.item_parent_type, i);
            view.setTag(R.layout.item_product_service, -1);
            TextView textView = view.findViewById(R.id.mParent);
            textView.setText(parentList.get(i));
            for (int j = 0; j < parentList.size(); j++) {
                switch (j) {
                    case 0:
                        Drawable d1 = ProductServiceActivity.this.getResources().getDrawable(R.drawable.product_maintain);
                        d1.setBounds(0, 0, d1.getMinimumWidth(), d1.getMinimumHeight());
                        textView.setCompoundDrawables(d1, null, null, null);
                        break;
                    case 1:
                        Drawable d2 = ProductServiceActivity.this.getResources().getDrawable(R.drawable.product_water);
                        d2.setBounds(0, 0, d2.getMinimumWidth(), d2.getMinimumHeight());

                        textView.setCompoundDrawables(d2, null, null, null);
                        break;
                    case 2:
                        Drawable d3 = ProductServiceActivity.this.getResources().getDrawable(R.drawable.product_beauty);
                        d3.setBounds(0, 0, d3.getMinimumWidth(), d3.getMinimumHeight());

                        textView.setCompoundDrawables(d3, null, null, null);
                        break;
                }
            }
            return view;
        }

        @Override
        public View getChildView(final int i, final int i1, boolean b, View view, ViewGroup viewGroup) {
            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) ProductServiceActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.item_product_service, null);
            }
            view.setTag(R.layout.item_parent_type, i);
            view.setTag(R.layout.item_product_service, i1);
            final TextView tv = view.findViewById(R.id.mItem);
            tv.setText(dataset.get(parentList.get(i)).get(i1));
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putString("service_type", dataset.get(parentList.get(i)).get(i1));
                    if (type.equals("新增")) {
                        JumpUtil.newInstance().finishRightTrans(ProductServiceActivity.this, bundle, 1);
                    } else if (type.equals("修改")) {
                        bundle.putInt("index", index);
                        JumpUtil.newInstance().finishRightTrans(ProductServiceActivity.this, bundle, 2);
                    }
                }
            });
            return view;
        }

        @Override
        public boolean isChildSelectable(int i, int i1) {
            return true;
        }

    }

    class SearchAdapter extends BaseAdapter {
        private List<String> keywords;

        public SearchAdapter(List<String> keywords) {
            this.keywords = keywords;
        }

        @Override
        public int getCount() {
            return keywords.size();
        }

        @Override
        public Object getItem(int i) {
            return keywords.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            ViewHolder holder = null;
            if (view == null) {
                view = LinearLayout.inflate(ProductServiceActivity.this, R.layout.item_product_search_item, null);
                holder = new ViewHolder(view);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            holder.mItem.setText(keywords.get(i));
            holder.mItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupWindow.dismiss();
                    hideInputMethod(mSearch);
                    Bundle bundle = new Bundle();
                    bundle.putString("service_type", keywords.get(i));
                    if (type.equals("新增")) {
                        JumpUtil.newInstance().finishRightTrans(ProductServiceActivity.this, bundle, 1);
                    } else if (type.equals("修改")) {
                        bundle.putInt("index", index);
                        JumpUtil.newInstance().finishRightTrans(ProductServiceActivity.this, bundle, 2);
                    }
                }
            });
            return view;
        }

        class ViewHolder {
            @BindView(R.id.mItem)
            TextView mItem;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }
}
