package com.xtzhangbinbin.jpq.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.activity.UpdateCar;
import com.xtzhangbinbin.jpq.entity.WeisjBean;
import com.xtzhangbinbin.jpq.utils.OKhttptils;
import com.xtzhangbinbin.jpq.view.ZQImageViewRoundOval;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/4/30.
 */

public class WeisjAdapter extends RecyclerView.Adapter<WeisjAdapter.ViewHolder> {
    private Context context;
    private List<WeisjBean.DataBean.ResultBean> result;

    public WeisjAdapter(Context context, List<WeisjBean.DataBean.ResultBean> result) {
        this.context = context;
        this.result = result;
    }

    @Override
    public WeisjAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_weisj_list, parent, false));
    }

    @Override
    public void onBindViewHolder(WeisjAdapter.ViewHolder holder, final int position) {
        WeisjBean.DataBean.ResultBean resultBean = result.get(position);
        holder.price.setText(new DecimalFormat("#0.00").format(Double.valueOf(resultBean.getCar_price()) ));
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
        }
        holder.weisj_image.setType(ZQImageViewRoundOval.TYPE_ROUND);
        holder.weisj_image.setRoundRadius(5);
        Log.d("aaaaa", "onBindViewHolder: "+resultBean.getCar_price());

        if(result.get(position).getCar_1_icon_file_id()!=null){
            //通过id得到图片
            OKhttptils.getPic(context,result.get(position).getCar_1_icon_file_id(),holder.weisj_image);
        }else {
            holder.weisj_image.setImageResource(R.drawable.aa1);
        }
        holder.weisj_bianji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UpdateCar.class);
                intent.putExtra("carid",String.valueOf(result.get(position).getCar_id()));
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return result.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView weisj_name,weisj_time,price,weisj_cjh,weisj_sh;
        private ZQImageViewRoundOval weisj_image;
        private RelativeLayout weisj_bianji;
        public ViewHolder(View itemView) {
            super(itemView);
            weisj_name=itemView.findViewById(R.id.weisj_name);
            weisj_time=itemView.findViewById(R.id.weisj_time);
            price=itemView.findViewById(R.id.price);
            weisj_cjh=itemView.findViewById(R.id.weisj_cjh);
            weisj_sh=itemView.findViewById(R.id.weisj_sh);
            weisj_image=itemView.findViewById(R.id.weisj_image);
            weisj_bianji=itemView.findViewById(R.id.weisj_bianji);
        }
    }
}
