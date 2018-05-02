package com.xtzhangbinbin.jpq.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.base.BaseActivity;
import com.xtzhangbinbin.jpq.config.Config;
import com.xtzhangbinbin.jpq.entity.CompServiceType;
import com.xtzhangbinbin.jpq.entity.ShowProductDetaile;
import com.xtzhangbinbin.jpq.gson.factory.GsonFactory;
import com.xtzhangbinbin.jpq.utils.JumpUtil;
import com.xtzhangbinbin.jpq.utils.OKhttptils;
import com.xtzhangbinbin.jpq.utils.ToastUtil;
import com.xtzhangbinbin.jpq.view.OrdinaryDialog;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/* 修改商家产品 */
public class ShowProductDetailActivity extends BaseActivity {

    private static final String TAG = "修改商家产品";
    @BindView(R.id.mType)
    TextView mType;
    @BindView(R.id.mName)
    EditText mName;
    @BindView(R.id.mYouhui)
    EditText mYouhui;
    @BindView(R.id.product_list)
    ListView productList;
    @BindView(R.id.mTotal)
    TextView mTotal;

    private String prod_id;
    private String service_id;

    private List<String> stringList = new ArrayList<>();//服务类型list
    private List<CompServiceType.DataBean.ResultBean> beanList = new ArrayList<>();
    private ArrayAdapter arrayAdapter;

    private PopupWindow popupWindow;
    private String item_name;
    private String item_price;
    private double sTotal = 0.00;//总价

    private ShowProductDetaile.DataBean.ResultBean.ProductItemListBean itemListBean;
    private List<ShowProductDetaile.DataBean.ResultBean.ProductItemListBean> itemListBeans = new ArrayList<>();
    private ShowDetailAdapter adapter;

    private ShowProductDetaile.DataBean.ResultBean resultBean;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 001:
                    itemListBeans = (List<ShowProductDetaile.DataBean.ResultBean.ProductItemListBean>) msg.obj;
                    if (msg.arg1 == 1) {
                        sTotal = 0.00;
                        for (int i = 0; i < itemListBeans.size(); i++) {
                            itemListBean = itemListBeans.get(i);
                            sTotal = sTotal + itemListBean.getItem_price();
                            DecimalFormat df = new DecimalFormat("#0.00");
                            mTotal.setText("¥" + df.format(sTotal));
                        }
                        adapter = new ShowDetailAdapter(itemListBeans);
                        productList.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    } else if (msg.arg1 == 2) {
                        sTotal = 0.00;
                        if (itemListBeans.size() == 0) {
                            DecimalFormat df = new DecimalFormat("#0.00");
                            mTotal.setText("¥" + df.format(sTotal));
                        } else {
                            for (int i = 0; i < itemListBeans.size(); i++) {
                                itemListBean = itemListBeans.get(i);
                                sTotal = sTotal + itemListBean.getItem_price();
                                DecimalFormat df = new DecimalFormat("#0.00");
                                mTotal.setText("¥" + df.format(sTotal));
                            }
                        }
                        adapter = new ShowDetailAdapter(itemListBeans);
                        productList.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }

                    break;
                case 002:
                    resultBean = (ShowProductDetaile.DataBean.ResultBean) msg.obj;
                    service_id = resultBean.getProd_service_type_item();
                    mType.setText(resultBean.getService_type_item_name_detail());
                    mName.setText(resultBean.getProd_service_name());
                    mYouhui.setText(String.valueOf(resultBean.getProd_reduced_price()));
                    mTotal.setText(String.valueOf(resultBean.getProd_price()));
                    break;
                case 003:
                    beanList = (List<CompServiceType.DataBean.ResultBean>) msg.obj;
                    for (int i = 0; i < beanList.size(); i++) {
                        stringList.add(beanList.get(i).getServerDesc());
                    }
                    initPopupWindow();
                    break;
                case 004:
                    itemListBean = (ShowProductDetaile.DataBean.ResultBean.ProductItemListBean) msg.obj;
                    int a = msg.arg1;
                    itemListBeans.set(a, itemListBean);
                    sTotal = 0.00;
                    for (int i = 0; i < itemListBeans.size(); i++) {
                        itemListBean = itemListBeans.get(i);
                        sTotal = sTotal + itemListBean.getItem_price();
                        DecimalFormat df = new DecimalFormat("#0.00");
                        mTotal.setText("¥" + df.format(sTotal));
                    }
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_product_detail);
        ButterKnife.bind(this);
//        Bundle bundle = getIntent().getExtras();
//        bundle.getString("prod_id");
        prod_id = "62";
        getData();
    }

    /* 设置服务类型popupwindow */
    private void initPopupWindow() {
        View view = LayoutInflater.from(this).inflate(R.layout.popup_down_list, null);
        RelativeLayout layout = view.findViewById(R.id.rl_icon_popup);
        ListView popupList = view.findViewById(R.id.popup_list);
        arrayAdapter = new ArrayAdapter(this, R.layout.item_popup_down_item, stringList);
        popupList.setAdapter(arrayAdapter);
        popupWindow = new PopupWindow(view, RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT, true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);

        popupList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mType.setText(stringList.get(i));
                service_id = stringList.get(i);
                popupWindow.dismiss();
            }
        });
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
    }

    /* 获取服务类型 */
    private void getSercive() {
        Map<String, String> map = new HashMap<>();
        OKhttptils.post(ShowProductDetailActivity.this, Config.GET_COMP_SERVICE_TYPE, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                Log.i(TAG, "onResponse: " + response);
                /**
                 * {"data":{"result":[{"dic_id":"YZcompcfwlxwxby","dic_value":"维修保养"},{"dic_id":"YZcompcfwlxxc","dic_value":"洗车"},{"dic_id":"YZcompcfwlxjx","dic_value":"驾校"},{"dic_id":"YZcompcfwlxfwjg","dic_value":"服务机构"},{"dic_id":"YZcompcfwlxjcz","dic_value":"监测站"},{"dic_id":"YZcompcfwlxqcgz","dic_value":"汽车改装"}]},"message":"","status":"1"}
                 * */
                Gson gson = GsonFactory.create();
                CompServiceType type = gson.fromJson(response, CompServiceType.class);
                List<CompServiceType.DataBean.ResultBean> beanList = type.getData().getResult();
                Message message = new Message();
                message.what = 003;
                message.obj = beanList;
                handler.sendMessage(message);
            }

            @Override
            public void fail(String response) {
                ToastUtil.noNAR(ShowProductDetailActivity.this);
            }
        });

    }

    /* 获取数据 */
    private void getData() {
        Map<String, String> map = new HashMap<>();
        map.put("prod_id", prod_id);
        OKhttptils.post(ShowProductDetailActivity.this, Config.SHOW_PRODUCT, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                Log.i(TAG, "success: " + response);
                Gson gson = GsonFactory.create();
                ShowProductDetaile productDetaile = gson.fromJson(response, ShowProductDetaile.class);
                //显示adapter
                List<ShowProductDetaile.DataBean.ResultBean.ProductItemListBean> itemListBeans = productDetaile.getData().getResult().getProductItemList();
                Message message = new Message();
                message.what = 001;
                message.obj = itemListBeans;
                message.arg1 = 1;
                handler.sendMessage(message);

                //显示类型、名称、优惠价格、总价
                ShowProductDetaile.DataBean.ResultBean resultBean = productDetaile.getData().getResult();
                Message message1 = new Message();
                message1.what = 002;
                message1.obj = resultBean;
                handler.sendMessage(message1);

            }

            @Override
            public void fail(String response) {
                Log.e(TAG, "fail: " + response);
            }
        });

    }

    @OnClick({R.id.mType, R.id.mAdd, R.id.uploading})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mType:
                stringList.clear();
                getSercive();
                break;
            case R.id.mAdd:
                Bundle bundle = new Bundle();
                bundle.putString("type", "新增");
                JumpUtil.newInstance().jumpRight(this, ProductServiceActivity.class, 1, bundle);
                break;
            case R.id.uploading:
                uploading();
                break;
        }
    }

    /* 修改数据 */
    private void uploading() {
        String stype = service_id;
        String sname = mName.getText().toString().trim();
        String sYouhui = setNumberText(mYouhui);
        String s = itemListBeans.toString().replaceAll("=", ":");
        Log.e(TAG, "uploading看看是森么: " + s);

        if (!stype.isEmpty() && !sname.isEmpty() && !sYouhui.isEmpty() && itemListBeans.size() != 0) {
            if (Double.valueOf(sYouhui) <= Double.valueOf(sTotal)) {
                Map<String, String> map = new HashMap<>();
                map.put("prod_service_type_item", stype);
                map.put("prod_service_name", sname);
                map.put("prod_reduced_price", sYouhui);
                map.put("itemList", s);
                map.put("prod_id", prod_id);

                OKhttptils.post(ShowProductDetailActivity.this, Config.UPDATE_PRODUCT, map, new OKhttptils.HttpCallBack() {
                    @Override
                    public void success(String response) {
                        Log.i(TAG, "success提交: " + response);
                        /**
                         * {"data":{},"message":"","status":"1"}
                         */
                        ToastUtil.show(ShowProductDetailActivity.this, "提交成功");
                    }

                    @Override
                    public void fail(String response) {
                        ToastUtil.noNAR(ShowProductDetailActivity.this);
                    }
                });

            } else {
                ToastUtil.show(ShowProductDetailActivity.this, "优惠价格不得高于总价");
            }
        } else {
            ToastUtil.show(ShowProductDetailActivity.this, "请添加完整后提交");
        }
    }


    /* 设置adapter */
    class ShowDetailAdapter extends BaseAdapter {
        private List<ShowProductDetaile.DataBean.ResultBean.ProductItemListBean> itemListBeans;

        public ShowDetailAdapter(List<ShowProductDetaile.DataBean.ResultBean.ProductItemListBean> itemListBeans) {
            this.itemListBeans = itemListBeans;
        }

        @Override
        public int getCount() {
            return itemListBeans.size();
        }

        @Override
        public Object getItem(int i) {
            return itemListBeans.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        public void remove(int i) {
            itemListBeans.remove(i);
            adapter.notifyDataSetChanged();
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            ViewHolder holder = null;
            if (view == null) {
                view = LayoutInflater.from(ShowProductDetailActivity.this).inflate(R.layout.item_product_list, null);
                holder = new ViewHolder(view);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            holder.mPrice.setText(String.valueOf(itemListBeans.get(i).getItem_price()));
            holder.mName.setText(itemListBeans.get(i).getItem_name());
            holder.mDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final OrdinaryDialog dialog = OrdinaryDialog.newInstance(ShowProductDetailActivity.this).setMessage1("删除").setMessage2("确定删除该服务项吗？").showDialog();
                    dialog.setYesOnclickListener(new OrdinaryDialog.onYesOnclickListener() {
                        @Override
                        public void onYesClick() {
                            itemListBeans.remove(i);
                            Message message = new Message();
                            message.what = 001;
                            message.obj = itemListBeans;
                            message.arg1 = 2;
                            handler.sendMessage(message);
                            dialog.dismiss();
                        }
                    });

                    dialog.setNoOnclickListener(new OrdinaryDialog.onNoOnclickListener() {
                        @Override
                        public void onNoClick() {
                            dialog.dismiss();
                        }
                    });

//                    remove(i);
//                    adapter.notifyDataSetChanged();
                }
            });
            holder.mDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    item_name = itemListBeans.get(i).getItem_name();
                    item_price = String.valueOf(itemListBeans.get(i).getItem_price());
                    createupDatePopup(i);
                }
            });
            return view;
        }

        class ViewHolder {
            public View rootView;
            public TextView mName;
            public TextView mPrice;
            public ImageView mDelete;
            public LinearLayout mDetails;

            public ViewHolder(View rootView) {
                this.rootView = rootView;
                this.mName = (TextView) rootView.findViewById(R.id.mName);
                this.mPrice = (TextView) rootView.findViewById(R.id.mPrice);
                this.mDelete = (ImageView) rootView.findViewById(R.id.mDelete);
                this.mDetails = (LinearLayout) rootView.findViewById(R.id.mDetails);
            }

        }

    }

    /* 新建服务项的名称+价格popupwindow */
    private void createNewPopup() {
        View view = LayoutInflater.from(this).inflate(R.layout.popup_edit_down, null);
        RelativeLayout layout = view.findViewById(R.id.rl_icon_popup);
        ImageView mChange = view.findViewById(R.id.mChange);
        final TextView mName = view.findViewById(R.id.mName);
        final EditText mPrice = view.findViewById(R.id.mPrice);
        TextView mOk = view.findViewById(R.id.mOk);
        popupWindow = new PopupWindow(view, RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT, true);
        popupWindow.setOutsideTouchable(true);
        mName.setText(item_name);
        mPrice.setText(item_price);
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });

        mChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                Bundle bundle = new Bundle();
                bundle.putString("type", "新增");
                JumpUtil.newInstance().jumpRight(ShowProductDetailActivity.this, ProductServiceActivity.class, 1, bundle);
            }
        });

        mOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowProductDetaile.DataBean.ResultBean.ProductItemListBean itemListBean = new ShowProductDetaile.DataBean.ResultBean.ProductItemListBean();
                String sName = mName.getText().toString().trim();
                String sPrice = setNumberText(mPrice);
                if (!sName.isEmpty() && !sPrice.isEmpty()) {
                    itemListBean.setItem_name(sName);
                    itemListBean.setItem_price(Double.valueOf(sPrice));
                    itemListBeans.add(itemListBean);
                    Message message = new Message();
                    message.what = 001;
                    message.obj = itemListBeans;
                    message.arg1 = 1;
                    handler.sendMessage(message);
                    popupWindow.dismiss();
                } else {
                    ToastUtil.show(ShowProductDetailActivity.this, "请填写完整后保存");
                }
            }
        });

    }


    /* 弹出修改信息popupwindow */
    private void createupDatePopup(final int i) {
        View view = LayoutInflater.from(this).inflate(R.layout.popup_edit_down, null);
        RelativeLayout layout = view.findViewById(R.id.rl_icon_popup);
        ImageView mChange = view.findViewById(R.id.mChange);
        final TextView mName = view.findViewById(R.id.mName);
        final EditText mPrice = view.findViewById(R.id.mPrice);
        setNumberText(mPrice);
        TextView mOk = view.findViewById(R.id.mOk);
        popupWindow = new PopupWindow(view, RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT, true);
        popupWindow.setOutsideTouchable(true);
        mName.setText(item_name);
        mPrice.setText(item_price);
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtil.show(ShowProductDetailActivity.this, "点什么点");
                popupWindow.dismiss();
            }
        });

        mChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                Bundle bundle = new Bundle();
                bundle.putString("type", "修改");
                bundle.putInt("index", i);
                JumpUtil.newInstance().jumpRight(ShowProductDetailActivity.this, ProductServiceActivity.class, 2, bundle);
            }
        });

        mOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowProductDetaile.DataBean.ResultBean.ProductItemListBean itemListBean = new ShowProductDetaile.DataBean.ResultBean.ProductItemListBean();
                String sName = mName.getText().toString().trim();
                String sPrice = setNumberText(mPrice);
                if (!sName.isEmpty() && !sPrice.isEmpty()) {
                    itemListBean.setItem_name(sName);
                    DecimalFormat df = new DecimalFormat("#0.00");
                    itemListBean.setItem_price(Double.valueOf(sPrice));
                    Message message = new Message();
                    message.what = 004;
                    message.arg1 = i;
                    message.obj = itemListBean;
                    handler.sendMessage(message);
                    popupWindow.dismiss();
                } else {
                    ToastUtil.show(ShowProductDetailActivity.this, "请填写完整后保存");
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            Bundle bundle = data.getExtras();
            switch (requestCode) {
                case 1:
                    item_name = bundle.getString("service_type");
                    item_price = "";
                    Log.i(TAG, "onActivityResult: " + item_name);
                    createNewPopup();
                    break;

                case 2:
                    item_name = bundle.getString("service_type");
                    item_price = "";
                    int index = bundle.getInt("index");
                    createupDatePopup(index);
                    break;
            }
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        setTitle("产品维护");
        setLeftImageClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JumpUtil.newInstance().finishRightTrans(ShowProductDetailActivity.this);
            }
        });
    }
}
