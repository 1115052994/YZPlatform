package com.xtzhangbinbin.jpq.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.suke.widget.SwitchButton;
import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.config.Config;
import com.xtzhangbinbin.jpq.entity.WeisjBean;
import com.xtzhangbinbin.jpq.utils.OKhttptils;
import com.xtzhangbinbin.jpq.view.OrdinaryDialog;
import com.xtzhangbinbin.jpq.view.ZQImageViewRoundOval;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/4/30.
 */

public class YisjAdapter extends RecyclerView.Adapter<YisjAdapter.ViewHolder> {
    private Context context;
    private List<WeisjBean.DataBean.ResultBean> result;

    public YisjAdapter(Context context, List<WeisjBean.DataBean.ResultBean> result) {
        this.context = context;
        this.result = result;
    }

    @Override
    public YisjAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_weisj_list, parent, false));
    }

    @Override
    public void onBindViewHolder(final YisjAdapter.ViewHolder holder, final int position) {
        WeisjBean.DataBean.ResultBean resultBean = result.get(position);
        holder.price.setText(new DecimalFormat("#0.00").format(Double.valueOf(resultBean.getCar_price()) /10000)+"万");
        holder.weisj_name.setText(resultBean.getCar_name());
        holder.weisj_time.setText(resultBean.getCar_sign_date());
        holder.weisj_cjh.setText(resultBean.getCar_vin());
        switch (resultBean.getCar_audit_state()){
            case "1":
                holder.weisj_sh.setText("审核不通过");
                break;
            case "2":
                holder.weisj_sh.setText("审核通过");
                break;
            case "3":
                holder.weisj_sh.setText("未审核");
                break;
            case "4":
                holder.weisj_sh.setText("编辑中");
        }
        holder.switch_button.setChecked(true);
        holder.switch_button.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final SwitchButton view, boolean isChecked) {
                if(isChecked==false){
                    final OrdinaryDialog ordinaryDialog = OrdinaryDialog.newInstance(context).setMessage1("温馨提示").setMessage2("  是否确认修改为下架？").setCancel("取消").setConfirm("确定").showDialog();
                    ordinaryDialog.setNoOnclickListener(new OrdinaryDialog.onNoOnclickListener() {
                        @Override
                        public void onNoClick() {
                            holder.switch_button.setChecked(true);
                            ordinaryDialog.dismiss();
                        }
                    });
                    ordinaryDialog.setYesOnclickListener(new OrdinaryDialog.onYesOnclickListener() {
                        @Override
                        public void onYesClick() {
                            //修改状态为下架
                            Map<String, String> map = new HashMap<>();
                            map.put("car_id", String.valueOf(result.get(position).getCar_id()));
                            map.put("state","yxj");
                            OKhttptils.post((Activity) context, Config.CHANGESTATE, map, new OKhttptils.HttpCallBack() {
                                @Override
                                public void success(String response) {
                                    com.tencent.mm.opensdk.utils.Log.d("aaaaa", "onResponse获取数据: " + response);
                                    ordinaryDialog.dismiss();
                                    if(onCallBack!=null){
                                        onCallBack.getprodid(position);
                                    }

                                }
                                @Override
                                public void fail(String response) {
                                    com.tencent.mm.opensdk.utils.Log.d("aaaa", "fail: " + response);
                                }
                            });
                        }
                    });

                }
            }
        });
        holder.weisj_image.setType(ZQImageViewRoundOval.TYPE_ROUND);
        holder.weisj_image.setRoundRadius(5);
        Log.d("aaaaa", "onBindViewHolder: "+resultBean.getCar_price());
        if(result.get(position).getCar_1_icon_file_id()!=null){
            Log.d("aaaaa", "onBindViewHolder: 图片id"+result.get(position).getCar_1_icon_file_id());
            //通过id得到图片
            OKhttptils.getPic(context,result.get(position).getCar_1_icon_file_id(),holder.weisj_image);
        }else {
            holder.weisj_image.setImageResource(R.drawable.personal_collect_img);
        }
    }
    //回调刷新接口
    private  onCallBack onCallBack;
    public interface onCallBack{
        void getprodid(int position);
    }
    public void getCall(onCallBack onCallBack){
        this.onCallBack=onCallBack;
    }


    @Override
    public int getItemCount() {
        return result.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView weisj_name,weisj_time,price,weisj_cjh,weisj_sh;
        private ZQImageViewRoundOval weisj_image;
        private SwitchButton switch_button;
        public ViewHolder(View itemView) {
            super(itemView);
            weisj_name=itemView.findViewById(R.id.weisj_name);
            weisj_time=itemView.findViewById(R.id.weisj_time);
            price=itemView.findViewById(R.id.price);
            weisj_cjh=itemView.findViewById(R.id.weisj_cjh);
            weisj_sh=itemView.findViewById(R.id.weisj_sh);
            weisj_image=itemView.findViewById(R.id.weisj_image);
            switch_button=itemView.findViewById(R.id.switch_button);
        }
    }
}
