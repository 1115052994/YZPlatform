package com.xtzhangbinbin.jpq.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.activity.LoanDetails;
import com.xtzhangbinbin.jpq.activity.MessageEntry;
import com.xtzhangbinbin.jpq.entity.CreditBean;
import com.xtzhangbinbin.jpq.entity.LoanDetailsBean;
import com.xtzhangbinbin.jpq.utils.OKhttptils;
import com.xtzhangbinbin.jpq.utils.RoundedImageView;

import java.util.List;

/**
 * Created by Administrator on 2018/5/4.
 */

public class LoanDetailsAdapter extends  RecyclerView.Adapter<LoanDetailsAdapter.ViewHolder>{
    private Context context;
    private  List<LoanDetailsBean.DataBean.ResultBean> result;

    public LoanDetailsAdapter(Context context, List<LoanDetailsBean.DataBean.ResultBean> result) {
        this.context = context;
        this.result = result;
        Log.d("aaaaa", "FinancialAdapter: "+result);
    }

    @Override
    public LoanDetailsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LoanDetailsAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loan_details, parent, false));
    }

    @Override
    public void onBindViewHolder(LoanDetailsAdapter.ViewHolder holder, final int position) {

        Log.d("aaaaaa", "onBindViewHolder: "+result.get(position).getDict_9());
        holder.name.setText(result.get(position).getDict_desc());
        holder.message.setText(result.get(position).getDict_9());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MessageEntry.class);
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
        private TextView message,name;
        private RelativeLayout layout;
        public ViewHolder(View itemView) {
            super(itemView);
            message=itemView.findViewById(R.id.message);
            name=itemView.findViewById(R.id.name);
            layout=itemView.findViewById(R.id.layout);
        }
    }
}
