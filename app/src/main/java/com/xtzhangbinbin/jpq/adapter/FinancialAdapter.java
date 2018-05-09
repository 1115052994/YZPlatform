package com.xtzhangbinbin.jpq.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.activity.WebView;
import com.xtzhangbinbin.jpq.entity.FinancialBean;
import com.xtzhangbinbin.jpq.utils.OKhttptils;
import com.xtzhangbinbin.jpq.utils.RoundedImageView;

import java.util.List;

/**
 * Created by Administrator on 2018/5/4.
 */

public class FinancialAdapter extends  RecyclerView.Adapter<FinancialAdapter.ViewHolder>{
    private Context context;
    private List<FinancialBean.DataBean.ResultBean> result;

    public FinancialAdapter(Context context, List<FinancialBean.DataBean.ResultBean> result) {
        this.context = context;
        this.result = result;
    }

    @Override
    public FinancialAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FinancialAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_car_financial, parent, false));
    }

    @Override
    public void onBindViewHolder(FinancialAdapter.ViewHolder holder, final int position) {
        String jr_tags_cn = result.get(position).getJr_tags_cn();
        String[] split = jr_tags_cn.split(",");
        for (int i = 0; i <split.length ; i++) {
            View inflate = View.inflate(context, R.layout.textzdy, null);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            if(i==0){
                layoutParams.setMargins(0, 0, 0, 0);//4个参数按顺序分别是左上右下
            }else {
                layoutParams.setMargins(5, 0, 0, 0);//4个参数按顺序分别是左上右下
            }

            TextView text = inflate.findViewById(R.id.text);
            text.setLayoutParams(layoutParams);
            text.setText(split[i]);
            holder.relayout.addView(inflate);
        }
        Log.d("aaaaaa", "onBindViewHolder: "+result.get(position).getJr_name());
        holder.name.setText(result.get(position).getJr_name());
        holder.message.setText(result.get(position).getJr_desc());
        holder.ll.setText(String.valueOf(result.get(position).getJr_visit_count())+"人浏览");
        OKhttptils.getPic(context,result.get(position).getJr_img_url(),holder.imageView);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WebView.class);
                intent.putExtra("Url",result.get(position).getJr_turn_url());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return result.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView ll,message,name;
        private LinearLayout relayout;
        private RoundedImageView imageView;
        private LinearLayout layout;
        public ViewHolder(View itemView) {
            super(itemView);
            ll=itemView.findViewById(R.id.ll);
            message=itemView.findViewById(R.id.message);
            name=itemView.findViewById(R.id.name);
            relayout=itemView.findViewById(R.id.relayout);
            imageView=itemView.findViewById(R.id.image);
            layout=itemView.findViewById(R.id.layout);
        }
    }
}
