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
import android.view.WindowManager;
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
import com.xtzhangbinbin.jpq.entity.AddServiceTypeList;
import com.xtzhangbinbin.jpq.entity.CompServiceType;
import com.xtzhangbinbin.jpq.gson.factory.GsonFactory;
import com.xtzhangbinbin.jpq.utils.JumpUtil;
import com.xtzhangbinbin.jpq.utils.OKhttptils;
import com.xtzhangbinbin.jpq.utils.ToastUtil;
import com.xtzhangbinbin.jpq.view.MyProgressDialog;
import com.xtzhangbinbin.jpq.view.OrdinaryDialog;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/* 产品添加 */
@SuppressWarnings("all")
public class AddProductActivity extends BaseActivity {
    private static final String TAG = "产品添加";
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

    private String service_id;

    private PopupWindow popupWindow;
    private ArrayAdapter adapter;
    private List<CompServiceType.DataBean.ResultBean> beanList = new ArrayList<>();
    private List<String> stringList = new ArrayList<>();
    private List<String> stringId = new ArrayList<>();

    private ServiceAdapter serviceAdapter;//服务细项列表，提交时不能为空
    private List<AddServiceTypeList> addServiceTypeLists = new ArrayList<>();
    private String item_name;//添加服务项后返回的服务项名称，并弹出popupwindow
    private String item_price;
    private AddServiceTypeList addServiceTypeList = new AddServiceTypeList();
    private double sTotal = 0.00;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 001:
                    beanList = (List<CompServiceType.DataBean.ResultBean>) msg.obj;
                    for (int i = 0; i < beanList.size(); i++) {
                        stringList.add(beanList.get(i).getServerDesc());
                        stringId.add(beanList.get(i).getServerId());
                    }
                    if (stringList.size() == 0) {
                        ToastUtil.show(AddProductActivity.this, "还没进行企业设置");
                    } else {
                        initPopupWindow();
                    }
                    break;
                case 002:
                    addServiceTypeLists = (List<AddServiceTypeList>) msg.obj;
                    if (msg.arg1 == 1) {
                        sTotal = 0.00;
                        for (int i = 0; i < addServiceTypeLists.size(); i++) {
                            addServiceTypeList = addServiceTypeLists.get(i);
                            sTotal = sTotal + Double.valueOf(addServiceTypeList.getItem_price());
                            DecimalFormat df = new DecimalFormat("#0.00");
                            mTotal.setText("¥" + df.format(sTotal));
                        }
                        serviceAdapter = new ServiceAdapter(addServiceTypeLists);
                        productList.setAdapter(serviceAdapter);
                        serviceAdapter.notifyDataSetChanged();
                    } else if (msg.arg1 == 2) {
                        sTotal = 0.00;
                        //删除
                        if (addServiceTypeLists.size() == 0){
                            //删除掉最后一个服务小项时
                            DecimalFormat df = new DecimalFormat("#0.00");
                            mTotal.setText("¥" + df.format(sTotal));
                        }else {
                            for (int i = 0; i < addServiceTypeLists.size(); i++) {
                                addServiceTypeList = addServiceTypeLists.get(i);
                                sTotal = sTotal + Double.valueOf(addServiceTypeList.getItem_price());
                                DecimalFormat df = new DecimalFormat("#0.00");
                                mTotal.setText("¥" + df.format(sTotal));
                            }
                        }
                        serviceAdapter = new ServiceAdapter(addServiceTypeLists);
                        productList.setAdapter(serviceAdapter);
                        serviceAdapter.notifyDataSetChanged();
                    }
                    break;
                case 003:
                    addServiceTypeList = (AddServiceTypeList) msg.obj;
                    int i = msg.arg1;
                    addServiceTypeLists.set(i, addServiceTypeList);
                    sTotal = 0.00;
                    for (int j = 0; j < addServiceTypeLists.size() ; j++) {
                        addServiceTypeList = addServiceTypeLists.get(i);
                        sTotal = sTotal + Double.valueOf(addServiceTypeList.getItem_price());
                        DecimalFormat df = new DecimalFormat("#0.00");
                        mTotal.setText("¥" + df.format(sTotal));
                    }
                    serviceAdapter.notifyDataSetChanged();
                    break;
            }

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        ButterKnife.bind(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog = MyProgressDialog.createDialog(this);
    }


    @OnClick({R.id.mType, R.id.mAdd, R.id.uploading})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mType:
                //服务类型弹出底部popupwindow
                stringList.clear();
                getSercive();
//                showPopListView();
                break;
            case R.id.mAdd:
                Bundle bundle = new Bundle();
                bundle.putString("type", "新增");
                JumpUtil.newInstance().jumpRight(this, ProductServiceActivity.class, 1, bundle);
                break;
            case R.id.uploading:
                upLoading();
                break;
        }
    }

    /* 上传信息 */
    private void upLoading() {
        String stype = service_id;
        String sname = mName.getText().toString().trim();
        String sYouhui = setNumberText(mYouhui);
        String s = addServiceTypeLists.toString().replaceAll("=", ":");
        Log.e(TAG, "onViewClicked: " + s);

        if (!stype.isEmpty() && !sname.isEmpty() && !sYouhui.isEmpty() && addServiceTypeLists.size() != 0) {
            if (Double.valueOf(sYouhui) <= Double.valueOf(sTotal)) {
                showDialog("正在提交产品数据");
                Map<String, String> map = new HashMap<>();
                map.put("prod_service_type_item", stype);
                map.put("prod_service_name", sname);
                map.put("prod_reduced_price", sYouhui);
                map.put("itemList", s);
                OKhttptils.post(AddProductActivity.this, Config.ADD_PRODUCT, map, new OKhttptils.HttpCallBack() {
                    @Override
                    public void success(String response) {
                        closeDialog();
                        Log.i(TAG, "success提交: " + response);
                        /**
                         * {"data":{},"message":"","status":"1"}
                         */
                        ToastUtil.show(AddProductActivity.this, "提交成功");
                        AddProductActivity.this.finish();
                    }

                    @Override
                    public void fail(String response) {
                        closeDialog();
                        ToastUtil.noNAR(AddProductActivity.this);
                    }
                });

            } else {
                ToastUtil.show(AddProductActivity.this, "优惠价格不得高于总价");
            }
        } else {
            ToastUtil.show(AddProductActivity.this, "请添加完整后提交");
        }
    }

    /* 获取服务类型 */
    private void getSercive() {
        showDialog("获取服务类型");
        Map<String, String> map = new HashMap<>();
        OKhttptils.post(AddProductActivity.this, Config.GET_COMP_SERVICE_TYPE, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                closeDialog();
                Log.i(TAG, "onResponse: " + response);
                /**
                 * {"data":{"result":[{"dic_id":"YZcompcfwlxwxby","dic_value":"维修保养"},{"dic_id":"YZcompcfwlxxc","dic_value":"洗车"},{"dic_id":"YZcompcfwlxjx","dic_value":"驾校"},{"dic_id":"YZcompcfwlxfwjg","dic_value":"服务机构"},{"dic_id":"YZcompcfwlxjcz","dic_value":"监测站"},{"dic_id":"YZcompcfwlxqcgz","dic_value":"汽车改装"}]},"message":"","status":"1"}
                 * */
                Gson gson = GsonFactory.create();
                CompServiceType type = gson.fromJson(response, CompServiceType.class);
                List<CompServiceType.DataBean.ResultBean> beanList = type.getData().getResult();
                Message message = new Message();
                message.what = 001;
                message.obj = beanList;
                handler.sendMessage(message);
            }

            @Override
            public void fail(String response) {
                closeDialog();
                ToastUtil.noNAR(AddProductActivity.this);
            }
        });

    }

    /* 设置服务类型popupwindow */
    private void initPopupWindow() {
        View view = LayoutInflater.from(this).inflate(R.layout.popup_down_list, null);
        RelativeLayout layout = view.findViewById(R.id.rl_icon_popup);
        ListView popupList = view.findViewById(R.id.popup_list);
        adapter = new ArrayAdapter(this, R.layout.item_popup_down_item, stringList);
        popupList.setAdapter(adapter);
        popupWindow = new PopupWindow(view, RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT, true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);

        popupList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mType.setText(stringList.get(i));
                service_id = stringId.get(i);
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

    @Override
    protected void onStart() {
        super.onStart();
        setTitle("产品添加");
        setLeftImageClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JumpUtil.newInstance().finishRightTrans(AddProductActivity.this);
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

    /* 新建服务项的名称+价格popupwindow */
    private void createNewPopup() {
        View view = LayoutInflater.from(this).inflate(R.layout.popup_edit_down, null);
        RelativeLayout layout = view.findViewById(R.id.rl_icon_popup);
        ImageView mChange = view.findViewById(R.id.mChange);
        final EditText mName = view.findViewById(R.id.mName);
        final EditText mPrice = view.findViewById(R.id.mPrice);

        TextView mOk = view.findViewById(R.id.mOk);
        popupWindow = new PopupWindow(view, RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT, true);
        popupWindow.setOutsideTouchable(true);
        mName.setText(item_name);
        mPrice.setText(item_price);
        hideInputMethod(mPrice);

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
                JumpUtil.newInstance().jumpRight(AddProductActivity.this, ProductServiceActivity.class, 1, bundle);
            }
        });

        mOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick看看添加金额: " + setNumberText(mPrice));
                AddServiceTypeList addServiceTypeList = new AddServiceTypeList();
                String sName = mName.getText().toString().trim();
                String sPrice = setNumberText(mPrice);
                if (!sName.isEmpty() && !sPrice.isEmpty()) {
                    addServiceTypeList.setItem_name(sName);
                    addServiceTypeList.setItem_price(sPrice);
                    addServiceTypeLists.add(addServiceTypeList);
                    Message message = new Message();
                    message.what = 002;
                    message.obj = addServiceTypeLists;
                    message.arg1 = 1;
                    handler.sendMessage(message);
                    popupWindow.dismiss();
                } else {
                    ToastUtil.show(AddProductActivity.this, "请填写完整后保存");
                }
            }
        });

    }

    /* 服务项列表adapter */
    class ServiceAdapter extends BaseAdapter {
        private List<AddServiceTypeList> addServiceTypeLists;

        public ServiceAdapter(List<AddServiceTypeList> addServiceTypeLists) {
            this.addServiceTypeLists = addServiceTypeLists;
        }

        @Override
        public int getCount() {
            return addServiceTypeLists.size();
        }

        @Override
        public Object getItem(int i) {
            return addServiceTypeLists.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        public void remove(int i) {
            addServiceTypeLists.remove(i);
            serviceAdapter.notifyDataSetChanged();
        }


        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            ViewHolder holder = null;
            if (view == null) {
                view = LayoutInflater.from(AddProductActivity.this).inflate(R.layout.item_product_list, null);
                holder = new ViewHolder(view);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            holder.mName.setText(addServiceTypeLists.get(i).getItem_name());
            holder.mPrice.setText(addServiceTypeLists.get(i).getItem_price());
            holder.mDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final OrdinaryDialog dialog = OrdinaryDialog.newInstance(AddProductActivity.this).setMessage1("删除").setMessage2("确定删除该服务项吗").showDialog();
                    dialog.setYesOnclickListener(new OrdinaryDialog.onYesOnclickListener() {
                        @Override
                        public void onYesClick() {
                            Log.i(TAG, "onYesClick哒哒哒: " + addServiceTypeLists.toString());
                            addServiceTypeLists.remove(i);
                            Message message = new Message();
                            message.what = 002;
                            message.obj = addServiceTypeLists;
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

                }
            });
            holder.mDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    item_name = addServiceTypeLists.get(i).getItem_name();
                    item_price = addServiceTypeLists.get(i).getItem_price();
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

    /* 弹出修改信息popupwindow */
    private void createupDatePopup(final int i) {
        View view = LayoutInflater.from(this).inflate(R.layout.popup_edit_down, null);
        RelativeLayout layout = view.findViewById(R.id.rl_icon_popup);
        ImageView mChange = view.findViewById(R.id.mChange);
        final EditText mName = view.findViewById(R.id.mName);
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
//                ToastUtil.show(AddProductActivity.this, "点什么点");
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
                JumpUtil.newInstance().jumpRight(AddProductActivity.this, ProductServiceActivity.class, 2, bundle);
            }
        });

        mOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddServiceTypeList addServiceTypeList = new AddServiceTypeList();
                String sName = mName.getText().toString().trim();
                String sPrice = setNumberText(mPrice);
                if (!sName.isEmpty() && !sPrice.isEmpty()) {
                    addServiceTypeList.setItem_name(sName);
                    addServiceTypeList.setItem_price(sPrice);
                    Message message = new Message();
                    message.what = 003;
                    message.obj = addServiceTypeList;
                    message.arg1 = i;
                    handler.sendMessage(message);
                    popupWindow.dismiss();
                } else {
                    ToastUtil.show(AddProductActivity.this, "请填写完整后保存");
                }
            }
        });
    }

}
