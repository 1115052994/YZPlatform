package com.plt.yzplatform.activity;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.plt.yzplatform.R;
import com.plt.yzplatform.base.BaseActivity;
import com.plt.yzplatform.config.Config;
import com.plt.yzplatform.entity.CompAddServiceType;
import com.plt.yzplatform.gson.factory.GsonFactory;
import com.plt.yzplatform.utils.JumpUtil;
import com.plt.yzplatform.utils.OKhttptils;
import com.plt.yzplatform.utils.ToastUtil;

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
    AutoCompleteTextView mSearch;
    @BindView(R.id.mList)
    ExpandableListView mList;
    private String type;
    private int index;

    private List<CompAddServiceType.DataBean.ResultBean> beanList = new ArrayList<>();
    private List<String> parentList = new ArrayList<>();
    private List<String> childList = new ArrayList<>();
    private Map<String, List<String>> dataset = new HashMap<>();
    private MyExpandableListViewAdapter adapter;
    private ArrayAdapter<String> arrayAdapter;
    private String [] arr;


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
                        dataset.put(parentList.get(i), childList);
                    }
                    initAdapter();
                    arr = (String[]) childList.toArray(new String [childList.size()]);
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_service);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        type = bundle.getString("type");
        index = bundle.getInt("index");
//        arrayAdapter = new ArrayAdapter<String>(this,R.layout.item_product_search_item,arr);
//        mSearch.setAdapter(arrayAdapter);
        getData();
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
            public void success(String response) {
                Log.e(TAG, "success: " + response);
                Gson gson = GsonFactory.create();
                CompAddServiceType addServiceType = gson.fromJson(response, CompAddServiceType.class);
                List<CompAddServiceType.DataBean.ResultBean> beanList = addServiceType.getData().getResult();
                Message message = new Message();
                message.what = 001;
                message.obj = beanList;
                handler.sendMessage(message);
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
            if (view == null){
                LayoutInflater inflater = (LayoutInflater) ProductServiceActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.item_parent_type,null);
            }
            view.setTag(R.layout.item_parent_type,i);
            view.setTag(R.layout.item_product_service,-1);
            List<Drawable> drawables = new ArrayList<>();
            drawables.add(ProductServiceActivity.this.getResources().getDrawable(R.drawable.product_maintain));
            drawables.add(ProductServiceActivity.this.getResources().getDrawable(R.drawable.product_water));
            drawables.add(ProductServiceActivity.this.getResources().getDrawable(R.drawable.product_beauty));
            TextView textView = view.findViewById(R.id.mParent);
            textView.setText(parentList.get(i));
            drawables.get(i).setBounds(0,0,drawables.get(i).getMinimumWidth(),drawables.get(i).getMinimumHeight());
            textView.setCompoundDrawables(drawables.get(i),null,null,null);
            return view;
        }

        @Override
        public View getChildView(final int i, final int i1, boolean b, View view, ViewGroup viewGroup) {
            if (view == null){
                LayoutInflater inflater = (LayoutInflater) ProductServiceActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.item_product_service,null);
            }
            view.setTag(R.layout.item_parent_type,i);
            view.setTag(R.layout.item_product_service,i1);
            final TextView tv = view.findViewById(R.id.mItem);
            tv.setText(dataset.get(parentList.get(i)).get(i1));
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    ToastUtil.show(ProductServiceActivity.this,"点击了" + i1);
                    Bundle bundle = new Bundle();
                    bundle.putString("service_type",dataset.get(parentList.get(i)).get(i1));
                    if (type.equals("新增")) {
                        JumpUtil.newInstance().finishRightTrans(ProductServiceActivity.this, bundle, 1);
                    }else if (type.equals("修改")) {
                        bundle.putInt("index",index);
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



/*======================================*/

//    //设置父节点(章目录)不可点击
//        directotyLv.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
//        @Override
//        public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
//            return true;//返回true,表示不可点击
//        }
//    });


//    //设置列表默认全部展开
//        for (int i = 0; i < adapter.getGroupCount(); i++) {
//        directotyLv.expandGroup(i);
//    }

////去除父节点图标
//        directotyLv.setGroupIndicator(null);

    }

}
