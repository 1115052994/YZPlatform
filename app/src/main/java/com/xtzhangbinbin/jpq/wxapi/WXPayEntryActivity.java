package com.xtzhangbinbin.jpq.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.xtzhangbinbin.jpq.activity.OrdersPaySuccessActivity;
import com.xtzhangbinbin.jpq.activity.OrdersPersonalActivity;
import com.xtzhangbinbin.jpq.config.Config;
import com.xtzhangbinbin.jpq.entity.OrdersView;
import com.xtzhangbinbin.jpq.enums.OrderPersState;
import com.xtzhangbinbin.jpq.utils.JumpUtil;
import com.xtzhangbinbin.jpq.utils.OKhttptils;
import com.xtzhangbinbin.jpq.utils.Prefs;
import com.xtzhangbinbin.jpq.utils.ToastUtil;
import com.xtzhangbinbin.jpq.view.MyProgressDialog;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

    private static WXPayEntryActivity activity;
    private MyProgressDialog dialog;

    private IWXAPI api;
    private int count = 0;

    public static WXPayEntryActivity getInstance() {
        if (activity == null) {
            activity = new WXPayEntryActivity();
        }
        return activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        api = WXAPIFactory.createWXAPI(this, Config.WECHAT_APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
//		Log.w("test", "new intent");
        setIntent(intent);
        api.handleIntent(intent, this);
//		Log.w("test", intent.getDataString());
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
//		Log.d("test", "onPayFinish, errCode = " + resp.errCode + resp.transaction);
        dialog = MyProgressDialog.createDialog(this);
        dialog.setMessage("正在处理订单，请稍候");
        dialog.show();
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if (resp.errCode == 0) {
//				Prefs.with(getApplicationContext()).write("wechat_order_id", orderNum);
                new Thread() {
                    @Override
                    public void run() {
                        //暂停2秒查询，以保证查询结果更新到服务器
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
                        queryOrderStatus();
                    }
                }.start();
            } else {
                ToastUtil.show(getApplicationContext(), "支付失败！请稍候再试");
                dialog.dismiss();
                WXPayEntryActivity.this.finish();
            }
        } else {
            dialog.dismiss();
            WXPayEntryActivity.this.finish();
        }
    }

    /**
     * 查询订单状态
     * 因为微信支付的支付结果响应是发送到服务器端的，而此处只能得到是否支付完成，不区分成功或失败。
     * 所以，需要根据订单状态来确认是否支付成功
     */
    public void queryOrderStatus() {
        /**
         * 根据商品id获取对应商品信息
         */
        Map<String, String> map = new HashMap<>();
        map.put("order_id", Prefs.with(getApplicationContext()).read("wechat_order_id"));
//        Log.w("test", Prefs.with(getApplicationContext()).read("wechat_order_id"));
        OKhttptils.post(this, Config.ORDERS_GET_BYCODE, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                Gson gson = new Gson();
                OrdersView ordersView = gson.fromJson(response, OrdersView.class);
                //判断订单状态是否是支付成功
                if (OrderPersState.yzfwsy.toString().equals(ordersView.getData().getResult().getOrder_state_pers())) {
                    Bundle bundle = new Bundle();
                    bundle.putString("payStyle", "微信支付");
                    bundle.putDouble("price", ordersView.getData().getResult().getProd_reduced_price());
                    JumpUtil.newInstance().jumpRight(getApplicationContext(), OrdersPaySuccessActivity.class, bundle);
                    dialog.dismiss();
                    WXPayEntryActivity.this.finish();
                } else {
                    //如果订单是未支付状态，再次进行查询，最多查询3次
                    if(count < 3){
                        count++;
                        //休眠1.5秒后再次查询
                        new Thread(){
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1500);
                                    queryOrderStatus();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }.start();
                    } else {
                        JumpUtil.newInstance().jumpRight(getApplicationContext(), OrdersPersonalActivity.class);
                        WXPayEntryActivity.this.finish();
                    }
                }

            }

            @Override
            public void fail(String response) {
                dialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    ToastUtil.show(WXPayEntryActivity.this, jsonObject.getString("message"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(null != dialog && dialog.isShowing()){
            dialog.dismiss();
        }
    }
}