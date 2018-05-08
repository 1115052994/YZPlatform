package com.xtzhangbinbin.jpq.fragment.orderscomp;


import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.activity.OrdersPersonalViewActivity;
import com.xtzhangbinbin.jpq.adapter.CommonRecyclerAdapter;
import com.xtzhangbinbin.jpq.adapter.ViewHolder;
import com.xtzhangbinbin.jpq.config.Config;
import com.xtzhangbinbin.jpq.entity.OrdersCompInfo;
import com.xtzhangbinbin.jpq.gson.factory.GsonFactory;
import com.xtzhangbinbin.jpq.utils.JumpUtil;
import com.xtzhangbinbin.jpq.utils.NetUtil;
import com.xtzhangbinbin.jpq.utils.OKhttptils;
import com.xtzhangbinbin.jpq.view.CircleImageView;
import com.xtzhangbinbin.jpq.view.MyProgressDialog;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressWarnings("all")
public class OrdersCompUnUseFragment extends Fragment {
    @BindView(R.id.orders_comp_default_image)
    ImageView defaultImage;
//    @BindView(R.id.orders_comp_listview)
//    ListView ordersListView;
    Unbinder unbinder;
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private Context context;
    private CommonRecyclerAdapter ordersCompAdapter;
    private List<OrdersCompInfo.DataBean.ResultBean> result = new ArrayList<>();
    private int pageIndex = 1;//第几页
    private int pageCount;//总页数
    private MyProgressDialog dialog;
    public OrdersCompUnUseFragment(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_orders_comp_all, container, false);
        unbinder = ButterKnife.bind(this, inflate);
        result = new ArrayList<>();
        defaultImage.setVisibility(View.GONE);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                pageIndex = 1;
                result.clear();
                getOrders(Config.ORDERS_GET_COMP_LIST, 1, refreshlayout);
            }
        });
        smartRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                getOrders(Config.ORDERS_GET_COMP_LIST, ++pageIndex, refreshlayout);
            }
        });
        dialog = MyProgressDialog.createDialog(context);
        dialog.setMessage("正在加载数据，请稍候");
        dialog.show();
        initAdapter();
        getOrders(Config.ORDERS_GET_COMP_LIST, 1, null);
        return inflate;
    }

    public void getOrders(String url, final int pageIndex, final RefreshLayout refreshlayout) {
        Map<String, String> map = new HashMap<>();
        map.put("state","wxf");
        map.put("pageIndex", String.valueOf(pageIndex));
        map.put("pageSize", "");
        if (NetUtil.isNetAvailable(context)) {
            OKhttptils.post((Activity) context, url, map, new OKhttptils.HttpCallBack() {
                @Override
                public void success(String response) {
                    Gson gson = GsonFactory.create();
                    OrdersCompInfo carCompInfo = gson.fromJson(response, OrdersCompInfo.class);
                    List<OrdersCompInfo.DataBean.ResultBean> result2 = carCompInfo.getData().getResult();
                    pageCount = carCompInfo.getData().getPageCount();
                    result.addAll(result2);

                    //没有信息图片显示
                    if (result.size() <= 0) {
                        defaultImage.setVisibility(View.VISIBLE);
                    } else {
                        if (refreshlayout != null) {
                            if (pageIndex > pageCount) {
                                refreshlayout.finishLoadmore(2000);
                            } else {
                                ordersCompAdapter.notifyDataSetChanged();
                                refreshlayout.finishRefresh(2000);
                                refreshlayout.finishLoadmore(2000);
                            }
                        } else {
                            ordersCompAdapter.notifyDataSetChanged();
                        }
                    }

                    if(null != dialog && dialog.isShowing()){
                        dialog.dismiss();
                    }

                }

                @Override
                public void fail(String response) {
                    if(null != dialog && dialog.isShowing()){
                        dialog.dismiss();
                    }
                    defaultImage.setVisibility(View.VISIBLE);
                    Log.w("test", response);
                    Toast.makeText(context, "查询失败", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void initAdapter(){
        ordersCompAdapter = new CommonRecyclerAdapter(context, result, R.layout.item_orderscomp_list) {
            @Override
            public void convert(ViewHolder holder, Object item, int position) {
                OrdersCompInfo.DataBean.ResultBean orders = result.get(position);
                CircleImageView icon = (CircleImageView) holder.getView(R.id.item_orders_comp_icon);
                OKhttptils.getPicByHttp(context, orders.getPers_head_file_id(), icon);
                TextView item_orders_comp_phone = holder.getView(R.id.item_orders_comp_phone);
                item_orders_comp_phone.setText(orders.getPers_phone().substring(0,3) + "****" + orders.getPers_phone().substring(7));
                TextView item_orders_comp_status = holder.getView(R.id.item_orders_comp_status);
                item_orders_comp_status.setText("wxf".equals(orders.getOrder_state_comp()) ? "未消费" : "已消费");
                TextView item_orders_comp_service_type = holder.getView(R.id.item_orders_comp_service_type);
                item_orders_comp_service_type.setText(orders.getProd_service_name());
                TextView item_orders_comp_service_name = holder.getView(R.id.item_orders_comp_service_name);
                item_orders_comp_service_name.setText(orders.getProd_service_name());
                TextView item_orders_comp_date = holder.getView(R.id.item_orders_comp_date);
                item_orders_comp_date.setText(orders.getOrder_date());
                TextView item_orders_comp_price = holder.getView(R.id.item_orders_comp_price);
                item_orders_comp_price.setText("金额:" + new DecimalFormat("#0.00").format(orders.getOrder_price()));
                TextView item_orders_comp_market_price = holder.getView(R.id.item_orders_comp_market_price);
                item_orders_comp_market_price.setText("原价：" + new DecimalFormat("#0.00").format(orders.getProd_price()));
                item_orders_comp_market_price.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG );
            }
        };
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(ordersCompAdapter);
        ordersCompAdapter.setOnItemClickListener(new CommonRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Bundle bundle = new Bundle();
                bundle.putString("order_id", result.get(position).getOrder_id());
                JumpUtil.newInstance().jumpRight(context, OrdersPersonalViewActivity.class, bundle);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        if(null != dialog && dialog.isShowing()){
            dialog.dismiss();
        }
    }
}
