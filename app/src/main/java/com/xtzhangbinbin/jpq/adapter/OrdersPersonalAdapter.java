package com.xtzhangbinbin.jpq.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.activity.AddProductActivity;
import com.xtzhangbinbin.jpq.activity.OrdersPersonalViewActivity;
import com.xtzhangbinbin.jpq.activity.OrdersSubmitActivity;
import com.xtzhangbinbin.jpq.activity.OrdersUseActivity;
import com.xtzhangbinbin.jpq.config.Config;
import com.xtzhangbinbin.jpq.entity.Orders;
import com.xtzhangbinbin.jpq.enums.OrderPersState;
import com.xtzhangbinbin.jpq.fragment.orderspersonal.OrdersPersonalAllFragment;
import com.xtzhangbinbin.jpq.gson.factory.GsonFactory;
import com.xtzhangbinbin.jpq.utils.JumpUtil;
import com.xtzhangbinbin.jpq.utils.NetUtil;
import com.xtzhangbinbin.jpq.utils.OKhttptils;
import com.xtzhangbinbin.jpq.utils.ToastUtil;
import com.xtzhangbinbin.jpq.view.OrdinaryDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/4/21.
 */

public class OrdersPersonalAdapter extends BaseAdapter {
    private Context context;
    private List<Orders.DataBean.ResultBean> result;
    private CallBrowsing callBrowsing;
    private Map<String, String> map = new HashMap<>();

    public OrdersPersonalAdapter(Context context, List<Orders.DataBean.ResultBean> result) {
        this.context = context;
        this.result = result;

    }

    @Override
    public int getCount() {
        return result.size();
    }

    @Override
    public Object getItem(int position) {
        return result.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        //获取单个订单数据
        final Orders.DataBean.ResultBean orders = result.get(position);
        if(convertView==null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_orderspersonal, null);
            viewHolder.orders_personal_item_comp_name = convertView.findViewById(R.id.orders_personal_item_comp_name);
            viewHolder.orders_personal_item_state = convertView.findViewById(R.id.orders_personal_item_state);
            viewHolder.orders_personal_item_service_name = convertView.findViewById(R.id.orders_personal_item_service_name);
            viewHolder.orders_personal_item_createdate = convertView.findViewById(R.id.orders_personal_item_createdate);
            viewHolder.orders_personal_item_price = convertView.findViewById(R.id.orders_personal_item_price);
            viewHolder.orders_personal_item_market_price = convertView.findViewById(R.id.orders_personal_item_market_price);
            viewHolder.orders_personal_item_pay = convertView.findViewById(R.id.orders_personal_item_pay);
            viewHolder.orders_personal_item_cancel = convertView.findViewById(R.id.orders_personal_item_cancel);
            viewHolder.orders_personal_item_use = convertView.findViewById(R.id.orders_personal_item_use);
            viewHolder.orders_personal_item_btn_layout = convertView.findViewById(R.id.orders_personal_item_btn_layout);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (OrdersPersonalAdapter.ViewHolder) convertView.getTag();
        }
        viewHolder.orders_personal_item_comp_name.setText(orders.getAuth_comp_name());
        viewHolder.orders_personal_item_service_name.setText(orders.getProd_service_name());
        viewHolder.orders_personal_item_createdate.setText("下单时间：" + orders.getOrder_date());
        viewHolder.orders_personal_item_price.setText("金额：￥" + new DecimalFormat("#0.##").format(orders.getOrder_price()) + "元");
        viewHolder.orders_personal_item_market_price.setText("原价：" + new DecimalFormat("#0.##").format(orders.getProd_price()) + "元");
        viewHolder.orders_personal_item_market_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        OrderPersState state = OrderPersState.valueOf(orders.getOrder_state_pers());
        switch(state){
            case wzf:
                viewHolder.orders_personal_item_state.setText("未支付");
                viewHolder.orders_personal_item_pay.setVisibility(View.VISIBLE);
                viewHolder.orders_personal_item_pay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle = new Bundle();
                        bundle.putString("order_id", orders.getOrder_id());
                        JumpUtil.newInstance().jumpRight(context, OrdersSubmitActivity.class, bundle);
                    }
                });
                viewHolder.orders_personal_item_cancel.setVisibility(View.VISIBLE);
                viewHolder.orders_personal_item_use.setVisibility(View.GONE);
                viewHolder.orders_personal_item_btn_layout.setVisibility(View.VISIBLE);
                viewHolder.orders_personal_item_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final OrdinaryDialog dialog = OrdinaryDialog.newInstance(context).setMessage1("取消订单").setMessage2("您确定要取消该订单吗？").showDialog();
                        dialog.setYesOnclickListener(new OrdinaryDialog.onYesOnclickListener() {
                            @Override
                            public void onYesClick() {
                                if (NetUtil.isNetAvailable(context)) {
                                    Map<String ,String> map = new HashMap<>();
                                    map.put("order_id", orders.getOrder_id());
                                    OKhttptils.post((Activity) context, Config.ORDERS_CANCEL, map, new OKhttptils.HttpCallBack() {
                                        @Override
                                        public void success(String response) {
                                            try {
                                                JSONObject obj = new JSONObject(response);
                                                if("1".equals(obj.getString("status"))){
                                                    ToastUtil.show(context, "订单取消成功!");
                                                    orders.setOrder_state_pers(OrderPersState.yqx.toString());
                                                    notifyDataSetChanged();
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        @Override
                                        public void fail(String response) {
                                            Toast.makeText(context, "查询失败", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
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
                break;
            case yqx:
                viewHolder.orders_personal_item_state.setText("已取消");
                viewHolder.orders_personal_item_pay.setVisibility(View.GONE);
                viewHolder.orders_personal_item_cancel.setVisibility(View.GONE);
                viewHolder.orders_personal_item_use.setVisibility(View.GONE);
                viewHolder.orders_personal_item_btn_layout.setVisibility(View.GONE);
                break;
            case yzfwsy:
                viewHolder.orders_personal_item_state.setText("已支付未使用");
                viewHolder.orders_personal_item_pay.setVisibility(View.GONE);
                viewHolder.orders_personal_item_cancel.setVisibility(View.GONE);
                viewHolder.orders_personal_item_use.setVisibility(View.VISIBLE);
                viewHolder.orders_personal_item_use.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle = new Bundle();
                        bundle.putString("order_id", orders.getOrder_id());
                        JumpUtil.newInstance().jumpRight(context, OrdersUseActivity.class, bundle);
                    }
                });
                viewHolder.orders_personal_item_btn_layout.setVisibility(View.VISIBLE);
                break;
            case ysy:
                viewHolder.orders_personal_item_state.setText("已使用");
                viewHolder.orders_personal_item_pay.setVisibility(View.GONE);
                viewHolder.orders_personal_item_cancel.setVisibility(View.GONE);
                viewHolder.orders_personal_item_use.setVisibility(View.GONE);
                viewHolder.orders_personal_item_btn_layout.setVisibility(View.GONE);
                break;
            case sqtkz:
                viewHolder.orders_personal_item_state.setText("申请退款中");
                break;
            case ytk:
                viewHolder.orders_personal_item_state.setText("已退款");
                break;
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("order_id", orders.getOrder_id());
                JumpUtil.newInstance().jumpRight(context, OrdersPersonalViewActivity.class, bundle);
            }
        });

        return convertView;
    }
    class ViewHolder{
        TextView orders_personal_item_comp_name;//服务商名称
        TextView orders_personal_item_state;//订单状态
        TextView orders_personal_item_service_name;//服务名称
        TextView orders_personal_item_createdate;//下单时间
        TextView orders_personal_item_price;//单价
        TextView orders_personal_item_market_price;//市场价
        Button orders_personal_item_pay;//支付按钮
        Button orders_personal_item_cancel;//取消按钮
        Button orders_personal_item_use;//使用按钮
        LinearLayout orders_personal_item_btn_layout;//按钮layout
    }


}

