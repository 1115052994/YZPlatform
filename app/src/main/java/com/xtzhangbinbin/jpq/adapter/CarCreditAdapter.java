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
import com.xtzhangbinbin.jpq.activity.LoanDetails;
import com.xtzhangbinbin.jpq.activity.WebView;
import com.xtzhangbinbin.jpq.entity.CreditBean;
import com.xtzhangbinbin.jpq.entity.FinancialBean;
import com.xtzhangbinbin.jpq.utils.OKhttptils;
import com.xtzhangbinbin.jpq.utils.RoundedImageView;

import java.util.List;

/**
 * Created by Administrator on 2018/5/4.
 */

public class CarCreditAdapter extends  RecyclerView.Adapter<CarCreditAdapter.ViewHolder>{
    private Context context;
    private  List<CreditBean.DataBean.ResultBean> result;

    public CarCreditAdapter(Context context,  List<CreditBean.DataBean.ResultBean> result) {
        this.context = context;
        this.result = result;
        Log.d("aaaaa", "FinancialAdapter: "+result);
    }

    @Override
    public CarCreditAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CarCreditAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_car_credit, parent, false));
    }

    @Override
    public void onBindViewHolder(CarCreditAdapter.ViewHolder holder, final int position) {

        Log.d("aaaaaa", "onBindViewHolder: "+result.get(position).getDict_9());
        holder.name.setText(result.get(position).getDict_9());
        OKhttptils.getPic(context,result.get(position).getDict_10(),holder.imageView);
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, LoanDetails.class);
                intent.putExtra("dict_id",result.get(position).getDict_id());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return result.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private RoundedImageView imageView;
        public ViewHolder(View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            imageView=itemView.findViewById(R.id.image);
        }
    }
}
