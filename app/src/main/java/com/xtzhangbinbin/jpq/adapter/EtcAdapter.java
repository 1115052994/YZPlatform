package com.xtzhangbinbin.jpq.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.activity.WebView;
import com.xtzhangbinbin.jpq.entity.EtcBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/5/5.
 */

public class EtcAdapter extends BaseAdapter {
    private Context context;
    private List<EtcBean.DataBean.ResultBean> result;

    public EtcAdapter(Context context, List<EtcBean.DataBean.ResultBean> result) {
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
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LinearLayout.inflate(context, R.layout.item_etc, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.name.setText(result.get(position).getCname());
        viewHolder.leftlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WebView.class);
                intent.putExtra("Url",result.get(position).getUrl());
                context.startActivity(intent);
            }
        });
        viewHolder.rightlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent("android.intent.action.CALL", Uri.parse("tel:"+result.get(position).getPhone()));
                context.startActivity(intent);
            }
        });


        return convertView;
    }


    static class ViewHolder {
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.leftname)
        TextView leftname;
        @BindView(R.id.leftlayout)
        LinearLayout leftlayout;
        @BindView(R.id.rightname)
        TextView rightname;
        @BindView(R.id.rightlayout)
        LinearLayout rightlayout;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
