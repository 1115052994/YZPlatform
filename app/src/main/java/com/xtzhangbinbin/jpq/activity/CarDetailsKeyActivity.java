package com.xtzhangbinbin.jpq.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.base.BaseActivity;
import com.xtzhangbinbin.jpq.config.Config;
import com.xtzhangbinbin.jpq.utils.JumpUtil;
import com.xtzhangbinbin.jpq.utils.MapList;
import com.xtzhangbinbin.jpq.utils.MyProperUtil;
import com.xtzhangbinbin.jpq.utils.OKhttptils;
import com.xtzhangbinbin.jpq.utils.ToastUtil;
import com.xtzhangbinbin.jpq.view.MyProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

/* 车辆详参 */
public class CarDetailsKeyActivity extends BaseActivity {
    private static final String TAG = "车辆详参";
    @BindView(R.id.mList)
    ExpandableListView mList;
    @BindView(R.id.default_image)
    ImageView default_image;
    private String model_id;
    private String car_name;
    private Activity currtActivity;

    private Map<String, String> results = new LinkedHashMap<>();
    private List<String> parents = new ArrayList<>();


    private Map<String, String> child = new LinkedHashMap<>();

    private Map<String, Map<String, String>> dataset = new LinkedHashMap<>();

    private MyExpandableListViewAdapter adapter;
    private Properties properties;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_details_key);
        ButterKnife.bind(this);
        currtActivity = this;
        Bundle bundle = getIntent().getExtras();
        model_id = bundle.getString("model_id");
        car_name = bundle.getString("car_name");
        dialog = MyProgressDialog.createDialog(this);
        dialog.setMessage("正在加载数据，请稍候！");
        getData();
    }

    /* 获取数据 */
    private void getData() {
        dialog.show();
        final Map<String, String> map = new HashMap<>();
        map.put("car_id", model_id);
        OKhttptils.post(currtActivity, Config.CAR_DETAIL_KEY, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                Log.i(TAG, "success: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String data = jsonObject.getString("data");
                    JSONObject object = new JSONObject(data);
                    String result = object.getString("result");
                    JSONObject object1 = new JSONObject(result);
                    String result2 = object1.getString("result");

                    //json转map
                    results = json2map(result2);
                    if (null != results) {
                        default_image.setVisibility(View.GONE);
                        Set<String> keys = results.keySet();
                        Iterator<String> iter = keys.iterator();
                        while (iter.hasNext()) {
                            String s = iter.next();
                            parents.add(s);
                        }

                        JSONObject object2 = new JSONObject(result2);
                        for (int i = 0; i < parents.size(); i++) {
                            String s = object2.getString(parents.get(i));
                            child = json2map(s);

                            properties = MyProperUtil.getProperties(getApplicationContext());

                            MapList mapList = new MapList();
                            mapList.putAll(child);
                            for (int j = 0; j < mapList.size(); j++) {
                                String newKey = MyProperUtil.getValue(getApplicationContext(), (String) mapList.getKey(j));
                                child.put(newKey, String.valueOf(Html.fromHtml((String) mapList.getValue(j))));
                                child.remove(mapList.getKey(j));
                            }
                            child.remove("img");
                            dataset.put(parents.get(i), child);

                        }

                    /* ======  */

                        mList.setGroupIndicator(null);
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
                    } else {
                        default_image.setVisibility(View.VISIBLE);
                        mList.setVisibility(View.GONE);
                    }
                    closeDialog();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void fail(String response) {
                closeDialog();
                default_image.setVisibility(View.VISIBLE);
                Log.d(TAG, "fail: " + response);
//                try {
//                    JSONObject object = new JSONObject(response);
//                    ToastUtil.show(currtActivity, object.getString("message"));
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        setTitle("车辆详参");
        setLeftImageClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JumpUtil.newInstance().finishRightTrans(currtActivity);
            }
        });
    }


    /* 设置adapter 注：MapList的使用获取下标 */
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
            return dataset.get(parents.get(i)).size();
        }

        @Override
        public Object getGroup(int i) {
            return dataset.get(parents.get(i));
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
                LayoutInflater inflater = (LayoutInflater) currtActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.item_car_detail_key_parent, null);
            }
            view.setTag(R.layout.item_car_detail_key_parent, i);
            view.setTag(R.layout.item_car_details_key_child, -1);
            TextView textView = view.findViewById(R.id.mParent);
            textView.setText(parents.get(i));
            return view;
        }

        @Override
        public View getChildView(final int i, final int i1, boolean b, View view, ViewGroup viewGroup) {
            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) currtActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.item_car_details_key_child, null);
            }
            view.setTag(R.layout.item_car_detail_key_parent, i);
            view.setTag(R.layout.item_car_details_key_child, i1);
            final TextView tv = view.findViewById(R.id.mName);
            TextView textView = view.findViewById(R.id.mTip);
            child = dataset.get(parents.get(i));
            MapList mapList = new MapList();
            mapList.putAll(child);
            if(!"图片".equals((String) mapList.getKey(i1))){
                tv.setText((String) mapList.getKey(i1));
                textView.setText((String) mapList.getValue(i1));
            } else {
                tv.setText("车型");
                textView.setText(car_name);
            }
            return view;
        }

        @Override
        public boolean isChildSelectable(int i, int i1) {
            return true;
        }

    }


}
