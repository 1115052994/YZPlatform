package com.xtzhangbinbin.jpq.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xtzhangbinbin.jpq.R;

import java.util.List;

/**
 * Created by Administrator on 2018/5/7.
 */

public class ProductGridViewAdapter extends BaseAdapter {
    Context context;
    List<String> list;
    private int selectorPosition = 0;
    public ProductGridViewAdapter(Context context, List<String> list) {
        this.list = list;
        this.context = context;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        convertView = layoutInflater.inflate(R.layout.list_item, null);
        RelativeLayout mRelativeLayout = (RelativeLayout) convertView.findViewById(R.id.ll);
        TextView tvCity =  convertView.findViewById(R.id.tv);
        tvCity.setText(list.get(position));
        //如果当前的position等于传过来点击的position,就去改变他的状态
        if (selectorPosition == position) {
            mRelativeLayout.setBackgroundColor(Color.parseColor("#ff9696"));
            tvCity.setTextColor(Color.parseColor("#ffffff"));
        } else {
            //其他的恢复原来的状态
            mRelativeLayout.setBackgroundResource(R.drawable.xuanzhong);
            tvCity.setTextColor(Color.parseColor("#ff9696"));
        }

        return convertView;
    }
    public void changeState(int pos) {
        selectorPosition = pos;
        notifyDataSetChanged();

    }
}
