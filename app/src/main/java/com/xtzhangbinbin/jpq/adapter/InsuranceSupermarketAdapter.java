package com.xtzhangbinbin.jpq.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.xtzhangbinbin.jpq.activity.WebView;
import com.xtzhangbinbin.jpq.entity.SupermarketBean;

import java.util.List;

/**
 * Created by Administrator on 2018/5/4.
 */

public class InsuranceSupermarketAdapter extends BaseAdapter {
    private Context context;
    private List<SupermarketBean.DataBean.ResultBean> result;

    public InsuranceSupermarketAdapter(Context context, List<SupermarketBean.DataBean.ResultBean> result) {
        this.context = context;
        this.result = result;
    }

    @Override
    public int getCount() {
        return result.size();
    }

    @Override
    public Object getItem(int position) {
        return result.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView= LinearLayout.inflate(context,R.layout.item_insurance_supermarket,null);
        TextView tvbxcs = convertView.findViewById(R.id.tvbxcs);
        tvbxcs.setText(result.get(position).getDict_desc());
        RelativeLayout relayout = convertView.findViewById(R.id.relayout);
        relayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WebView.class);
                intent.putExtra("Url",result.get(position).getDict_10());
                context.startActivity(intent);
            }
        });

        return convertView;
    }
}
