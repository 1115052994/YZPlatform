package com.xtzhangbinbin.jpq.activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.zxing.WriterException;
import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.base.BaseActivity;
import com.xtzhangbinbin.jpq.config.Config;
import com.xtzhangbinbin.jpq.entity.OrderQRCode;
import com.xtzhangbinbin.jpq.utils.ActivityUtil;
import com.xtzhangbinbin.jpq.utils.OKhttptils;
import com.xtzhangbinbin.jpq.utils.ToastUtil;
import com.xtzhangbinbin.jpq.zxing.encode.CodeCreator;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import butterknife.BindView;
import butterknife.ButterKnife;
/**
 * Created by Administrator on 2018/5/1 0001.
 */
@SuppressWarnings("all")
public class OrdersUseActivity extends BaseActivity {

    //订单编号
    private String order_id = "92a00c10a8444bfea1abf5b95b8f9a16";
    @BindView(R.id.orders_use_comp_name)
    TextView orders_use_comp_name;
    @BindView(R.id.orders_use_service_name)
    TextView orders_use_service_name;
    @BindView(R.id.orders_use_date)
    TextView orders_use_date;
    @BindView(R.id.orders_use_price)
    TextView orders_use_price;
    @BindView(R.id.orders_use_token_code)
    TextView orders_use_token_code;
    @BindView(R.id.orders_use_qrcode)
    ImageView orders_use_qrcode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_use);
        ButterKnife.bind(this);
        ActivityUtil.addActivity(this);
        initData();
    }

    public void initData(){
        if(null != this.getIntent().getStringExtra("order_id")){
            order_id = this.getIntent().getStringExtra("order_id");
        }

        Map<String, String> map = new HashMap<>();
        map.put("order_id", order_id);
        //设置订单号默认为null
        OKhttptils.post(this, Config.ORDERS_QUERY_QRCODE, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                try{
                    JSONObject object = new JSONObject(response);
                    Gson gson = new Gson();
                    OrderQRCode orderQRCode = gson.fromJson(response, OrderQRCode.class);
                    orders_use_comp_name.setText(orderQRCode.getData().getResult().getAuth_comp_name());
                    orders_use_service_name.setText(orderQRCode.getData().getResult().getProd_service_name());
                    orders_use_date.setText(orderQRCode.getData().getResult().getOrder_date());
                    orders_use_price.setText("￥:" + orderQRCode.getData().getResult().getOrder_price());
                    orders_use_token_code.setText(orderQRCode.getData().getResult().getOrder_token_code());

                    //size给固定的值，避免使用iamgeview.getWidth,导致二维码模糊
                    try {
                        Bitmap bitmap = CodeCreator.createQRCode(orderQRCode.getData().getResult().getOrder_qrcode());
                        orders_use_qrcode.setImageBitmap(bitmap);
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                } catch(JSONException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void fail(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    ToastUtil.show(OrdersUseActivity.this, "当前订单已失效！！");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        setTitle("订单详情");
    }

}
