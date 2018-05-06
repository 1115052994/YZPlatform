package com.xtzhangbinbin.jpq.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xtzhangbinbin.jpq.R;
import com.suke.widget.SwitchButton;
import com.xtzhangbinbin.jpq.config.Config;
import com.xtzhangbinbin.jpq.entity.CarBeautyBean;
import com.xtzhangbinbin.jpq.utils.OKhttptils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/4/30.
 */

public class CarBeautyAdapter extends RecyclerView.Adapter<CarBeautyAdapter.ViewHolder>{
    private Context context;
    private List<CarBeautyBean.DataBean.ResultBean> result;

    public CarBeautyAdapter(Context context, List<CarBeautyBean.DataBean.ResultBean> result) {
        this.context = context;
        this.result = result;
    }

    @Override
    public CarBeautyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CarBeautyAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_car_beauty, parent, false));
    }

    @Override
    public void onBindViewHolder(CarBeautyAdapter.ViewHolder holder, final int position) {
        holder.name.setText(result.get(position).getProd_service_name());
        holder.youhui.setText(String.valueOf(result.get(position).getProd_reduced_price())+"元");
        holder.zhongjia.setText(String.valueOf(result.get(position).getProd_price())+"元");
        //设置默认状态
        switch (result.get(position).getProd_is_publish()){
            case "0":
                holder.switch_button.setChecked(false);
                break;
            case "1":
                holder.switch_button.setChecked(true);
                break;
        }
        holder.switch_button.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                //修改状态为上架
                if(isChecked==true){
                    updatestate(result.get(position).getProd_id(), Config.REVAMPPUBLISH);
                }else {
                    updatestate(result.get(position).getProd_id(), Config.REVAMPSOLDOUT);
                }
            }
        });
        holder.dingdan.setText(String.valueOf(result.get(position).getOrdersum()));
        holder.yishiyong.setText(String.valueOf(result.get(position).getConsume()));
        holder.weishiyong.setText(String.valueOf(result.get(position).getUnused()));
        //编辑
        holder.bianji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return result.size();
    }
    //删除回调刷新接口
    private onCallBack onCallBack;
    public interface onCallBack{
        void getprodid(View view,String prod_id);
    }
    public void getCall(onCallBack onCallBack){
        this.onCallBack=onCallBack;
    }

    //更改上下架状态
    private void updatestate(String prod_id,String url) {
        Map<String, String> map = new HashMap<>();
        map.put("prod_id", prod_id);
        OKhttptils.post((Activity) context, url, map, new OKhttptils.HttpCallBack() {
            @Override
            public String success(String response) {
                com.tencent.mm.opensdk.utils.Log.d("aaaaa", "onResponse获取数据: " + response);
                return response;
            }
            @Override
            public void fail(String response) {
                com.tencent.mm.opensdk.utils.Log.d("aaaa", "fail: " + response);
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,youhui,zhongjia,dingdan,yishiyong,weishiyong,yishiyong_name,weishiyong_name;
        SwitchButton switch_button;
        RelativeLayout bianji;
        public ViewHolder(View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            youhui=itemView.findViewById(R.id.youhui);
            zhongjia=itemView.findViewById(R.id.zhongjia);
            dingdan=itemView.findViewById(R.id.dingdan);
            yishiyong=itemView.findViewById(R.id.yishiyong);
            yishiyong_name=itemView.findViewById(R.id.yishiyong_name);
            weishiyong=itemView.findViewById(R.id.weishiyong);
            weishiyong_name=itemView.findViewById(R.id.weishiyong_name);
            switch_button=itemView.findViewById(R.id.switch_button);
            bianji=itemView.findViewById(R.id.bianji);
        }
    }
}
