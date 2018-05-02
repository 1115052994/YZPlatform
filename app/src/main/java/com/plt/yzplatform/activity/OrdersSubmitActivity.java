package com.plt.yzplatform.activity;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import com.google.gson.Gson;
import com.plt.yzplatform.R;
import com.plt.yzplatform.alipay.AlipayUtil;
import com.plt.yzplatform.base.BaseActivity;
import com.plt.yzplatform.config.Config;
import com.plt.yzplatform.entity.Orders;
import com.plt.yzplatform.entity.OrdersView;
import com.plt.yzplatform.entity.ShowProductDetaile;
import com.plt.yzplatform.gson.factory.GsonFactory;
import com.plt.yzplatform.utils.ActivityUtil;
import com.plt.yzplatform.utils.JumpUtil;
import com.plt.yzplatform.utils.OKhttptils;
import com.plt.yzplatform.utils.Prefs;
import com.plt.yzplatform.utils.StringUtil;
import com.plt.yzplatform.utils.ToastUtil;
import com.plt.yzplatform.view.MyProgressDialog;
import com.plt.yzplatform.wxapi.WXPayEntryActivity;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/4/25 0025.
 */
@SuppressWarnings("all")
public class OrdersSubmitActivity extends BaseActivity {
    @BindView(R.id.orders_submit_paystyle_wechat)
    CheckBox wechat;
    @BindView(R.id.orders_submit_paystyle_zfb)
    CheckBox zfb;
    @BindView(R.id.orders_submit_service_name)
    TextView serviceName;
    @BindView(R.id.orders_submit_comp_name)
    TextView compName;
    @BindView(R.id.orders_submit_price)
    TextView price;
    @BindView(R.id.orders_submit_market_price)
    TextView marketPrice;
    @BindView(R.id.orders_submit_pay_btn)
    Button payBtn;
    /** 微信支付 */
    public final static String PAY_WECHAT = "wechat";
    /** 支付宝支付 */
    public final static String PAY_ALIPAY = "alipay";
    /** 支付成功*/
    public final static String PAY_STATE_SUCCESS = "zfcg";
    //微信支付api对象
    private IWXAPI api;

    String payStyle = PAY_WECHAT;
    //商品ID，添加默认值为58
    private String pro_id = "64";
    //订单ID
    private String order_id;
    //产品对象
    ShowProductDetaile product;
    //订单对象
    OrdersView orders;
    private MyProgressDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_submit);
        ButterKnife.bind(this);
        ActivityUtil.addActivity(this);
        init();

    }

    public void init() {
        //注册微信API对象
        api = WXAPIFactory.createWXAPI(this, Config.WECHAT_APP_ID);
        api.registerApp(Config.WECHAT_APP_ID);

        marketPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        wechat.setChecked(true);
        wechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wechat.setChecked(true);
                payStyle = PAY_WECHAT;
                zfb.setChecked(false);
            }
        });
        zfb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zfb.setChecked(true);
                payStyle = PAY_ALIPAY;
                wechat.setChecked(false);
            }
        });
        /**
         * 从服务商详情页跳转过来的支付
         */
        if (!StringUtil.isEmpty(getIntent().getStringExtra("pro_id"))) {
            pro_id = getIntent().getStringExtra("pro_id");
            initDataByProduct();
        }
        /**
         * 从订单列表跳转过来的支付
         */
        if (!StringUtil.isEmpty(getIntent().getStringExtra("order_id"))) {
            order_id = getIntent().getStringExtra("order_id");
            initDataByOrders();
        }


        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(null != order_id){
                    //说明是从订单列表过来的，要改订单
                    createSignMap(order_id);
                } else {
                    //说明是从商家详情过来的，要添加订单
                    createOrders();
                }
            }
        });

        payCallback();
    }

    /**
     * 创建订单
     */
    public void createOrders(){
        if(null != product){
            Map<String, String> map = new HashMap<>();
            map.put("order_comp_id", product.getData().getResult().getComp_id());
            map.put("order_comp_user_id", product.getData().getResult().getUser_id());
            map.put("order_price", String.valueOf(product.getData().getResult().getProd_reduced_price()));
            map.put("order_comp_prod_id", String.valueOf(product.getData().getResult().getProd_id()));
            //设置订单号默认为null
            OKhttptils.post(this, Config.ORDERS_CREATE, map, new OKhttptils.HttpCallBack() {
                @Override
                public void success(String response) {
                    try{
                        JSONObject object = new JSONObject(response);
                        if(null != object && null != object.getJSONObject("data")) {
                            //创建订单成功后，返回唯一订单编号
                            String ordersNum = object.getJSONObject("data").getString("result");
                            //创建签名所需的map
                            createSignMap(ordersNum);
                        }
                    } catch(JSONException e){
                        e.printStackTrace();
                        ToastUtil.show(OrdersSubmitActivity.this, "订单创建失败，请稍候重试！！");
                    }
                }

                @Override
                public void fail(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        ToastUtil.show(OrdersSubmitActivity.this, "订单创建失败，请稍候重试！！");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            ToastUtil.show(OrdersSubmitActivity.this, "订单创建失败，请稍候重试！！");
        }
    }

    /**
     * 创建签名所需使用的支付信息
     * @return
     */
    public void createSignMap(String ordersNum){

        switch (payStyle){
            //支付宝流程
            case PAY_ALIPAY:
                alipay(ordersNum);
                break;
            //微信流程
            case PAY_WECHAT:
                wechatPay(ordersNum);
                break;
        }

    }

    /**
     * 支付宝支付方法
     * @param ordersNum
     */
    public void alipay(String ordersNum){
        //创建支付宝签名的信息
        Map<String ,String> map = new HashMap<>();
        map.put("timeout_express", "30");//超时时间
        map.put("product_code", product.getData().getResult().getProd_id());//产品编号
//        map.put("total_amount", String.valueOf(product.getData().getResult().getProd_reduced_price()));//商品优惠后的支付价格
        map.put("total_amount", "0.01");//商品单价
        map.put("subject", product.getData().getResult().getProd_service_name());//支付时显示的标题
        StringBuilder body = new StringBuilder();
        body.append(product.getData().getResult().getAuth_comp_name()).append("-").append(product.getData().getResult().getProd_service_name());
        map.put("body", body.toString());//备注说明信息
        map.put("out_trade_no", ordersNum);//唯一订单编号
        AlipayUtil.getInstance(OrdersSubmitActivity.this).payV2(map);
    }

    /**
     * 根据商品ID初始化数据
     */
    public void initDataByProduct() {
        /**
         * 根据商品id获取对应商品信息
         */
        Map<String, String> map = new HashMap<>();
        map.put("prod_id", pro_id);
        OKhttptils.post(this, Config.ORDERS_SHOW_PRODUCT_BYID, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                Gson gson = GsonFactory.create();
                product = gson.fromJson(response, ShowProductDetaile.class);
                if (null != product) {
                    serviceName.setText(product.getData().getResult().getProd_service_name());
                    compName.setText(product.getData().getResult().getAuth_comp_name());
                    price.setText(new DecimalFormat("#.#").format(product.getData().getResult().getProd_reduced_price()));
                    marketPrice.setText("原价：" + new DecimalFormat("#.#").format(product.getData().getResult().getProd_price()) + "元");
                }
            }

            @Override
            public void fail(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    ToastUtil.show(OrdersSubmitActivity.this, jsonObject.getString("message"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 根据订单ID初始化数据
     */
    public void initDataByOrders() {
        /**
         * 根据商品id获取对应商品信息
         */
        Map<String, String> map = new HashMap<>();
        map.put("order_id", order_id);
        OKhttptils.post(this, Config.ORDERS_GET_BYCODE, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                Gson gson = GsonFactory.create();
                orders = gson.fromJson(response, OrdersView.class);
                product = gson.fromJson(response, ShowProductDetaile.class);
                //将从订单中取出的产品ID放入产品对象
                product.getData().getResult().setProd_id(orders.getData().getResult().getOrder_comp_prod_id());
                //根据订单信息构建出商品对象，以便于信息展示和下一步的支付操作
                if (null != product) {
                    serviceName.setText(product.getData().getResult().getProd_service_name());
                    compName.setText(product.getData().getResult().getAuth_comp_name());
                    price.setText(new DecimalFormat("#.#").format(product.getData().getResult().getProd_reduced_price()));
                    marketPrice.setText("原价：" + new DecimalFormat("#.#").format(product.getData().getResult().getProd_price()) + "元");
                }
            }

            @Override
            public void fail(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    ToastUtil.show(OrdersSubmitActivity.this, jsonObject.getString("message"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 支付宝支付完成后，确认订单
     * @param orderNum
     * @param money
     */
    public void ordersSuccess(String orderNum, String money){
        /**
         * 根据商品id获取对应商品信息
         */
        Map<String, String> map = new HashMap<>();
        map.put("order_id", orderNum);
//        map.put("money", money);
        OKhttptils.post(this, Config.ORDERS_PAY_SUCCESS, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                Log.w("test", response);
            }

            @Override
            public void fail(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    ToastUtil.show(OrdersSubmitActivity.this, jsonObject.getString("message"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 保存支付日志
     * @param obj
     */
    public void createPayLog(JSONObject obj) throws JSONException{
        /**
         * 根据商品id获取对应商品信息
         */
        Map<String, String> map = new HashMap<>();
        map.put("pay_order_id", obj.getString("out_trade_no"));//平台订单主键
        map.put("pay_type", payStyle);//支付方式
        map.put("trade_status", PAY_STATE_SUCCESS);//支付状态
        map.put("out_trade_no", obj.getString("out_trade_no"));//商户订单号，暂与平台订单主键存同样的值
        map.put("trade_no", obj.getString("trade_no"));//支付平台成生的订单号
        map.put("seller_id", obj.getString("seller_id"));//收款人(卖方)在平台中的编号
        map.put("buyer_id", "");//暂无
        map.put("buyer_pay_amount", obj.getString("total_amount"));//支付金额
        map.put("gmt_payment_date", obj.getString("timestamp"));//支付时间
        OKhttptils.post(this, Config.ORDERS_PAY_LOG, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                Log.w("test", response);
            }

            @Override
            public void fail(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    ToastUtil.show(OrdersSubmitActivity.this, jsonObject.getString("message"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 微信支付
     * @param orderNum
     */
    public void wechatPay(final String orderNum){
        dialog = MyProgressDialog.createDialog(this);
        dialog.setMessage("正在调起支付，请稍候");
        dialog.show();
        Map<String, String> map = new HashMap<>();
        map.put("order_id", orderNum);
        map.put("price", String.valueOf(product.getData().getResult().getProd_reduced_price()));
        map.put("body", product.getData().getResult().getAuth_comp_name() + "-" + product.getData().getResult().getProd_service_name());
        //设置订单号默认为null
        OKhttptils.post(this, Config.ORDERS_WECCHAT_CREATE, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                dialog.dismiss();
                try{
                    JSONObject object = new JSONObject(response);
                    Log.w("test", response);
                    if(null != object && "1".equals(object.getString("status"))){
                        JSONObject result = object.getJSONObject("data").getJSONObject("result");
                        if(null != result){
                            PayReq req = new PayReq();
                            //req.appId = "wxf8b4f85f3a794e77";  // 测试用appId
                            req.appId			= result.getString("appid");
                            req.partnerId		= result.getString("partnerid");
                            req.packageValue    = result.getString("package");
                            req.prepayId		= result.getString("prepayid");
                            req.nonceStr		= result.getString("noncestr");
                            req.timeStamp		= result.getString("timestamp");
                            req.sign			= result.getString("sign");
                            //保留订单编号
                            Prefs.with(getApplicationContext()).write("wechat_order_id", orderNum);
//                          Toast.makeText(OrdersSubmitActivity.this, "正常调起支付", Toast.LENGTH_SHORT).show();
                            // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                            api.sendReq(req);
                            OrdersSubmitActivity.this.finish();
//                           Log.w("test", api.sendReq(req) + "");
                        }
                    } else {
                        ToastUtil.show(OrdersSubmitActivity.this, "订单创建失败，请稍候重试！！");
                    }
                } catch(JSONException e){
                    e.printStackTrace();
                    ToastUtil.show(OrdersSubmitActivity.this, "订单创建失败，请稍候重试！！");
                }
            }

            @Override
            public void fail(String response) {
                dialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    ToastUtil.show(OrdersSubmitActivity.this, "订单创建失败，请稍候重试！！");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 两种支付方式支付后的回调函数
     */
    public void payCallback(){
        /**
         * 支付宝支付后的回调
         */
        AlipayUtil.getInstance(this).setPayReturn(new AlipayUtil.AlipayReturn() {
            @Override
            public void payReturn(boolean result, String msg) {
//                Log.w("test","这是返回值:" + result + "," + msg);
                //支付成功，确认订单
                if(result){
                    try {
                        JSONObject obj = new JSONObject(msg);
                        if(null != obj){
                            JSONObject response = obj.getJSONObject("alipay_trade_app_pay_response");
                            //确认订单支付成功
                            ordersSuccess(response.getString("out_trade_no"), String.valueOf(product.getData().getResult().getProd_reduced_price()));
                            //保存支付流水
                            createPayLog(response);
                            Bundle bundle = new Bundle();
                            bundle.putDouble("price", product.getData().getResult().getProd_reduced_price());
                            bundle.putString("payStyle", payStyle.equals(PAY_WECHAT) ? "微信支付" : "支付宝支付");
                            JumpUtil.newInstance().jumpRight(OrdersSubmitActivity.this, OrdersPaySuccessActivity.class, bundle);
                            OrdersSubmitActivity.this.finish();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                    ordersSuccess(product.getData().getResult().geto);
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        setTitle("提交订单");
    }
}
