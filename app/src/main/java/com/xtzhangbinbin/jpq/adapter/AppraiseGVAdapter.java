package com.xtzhangbinbin.jpq.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xtzhangbinbin.jpq.AppraiseInterface;
import com.xtzhangbinbin.jpq.R;

import java.util.List;

public class AppraiseGVAdapter extends BaseAdapter {

    private List<String> list;
    private Context context;
    public AppraiseGVAdapter(Context context,List<String> list){
        this.context = context;
        this.list = list;
    }
    @Override
    public int getCount() {
        return list==null?0:list.size();
    }

    @Override
    public String getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        AppraiseGVAdapter.ViewHolder viewHolder;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_appraise_tag,null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (AppraiseGVAdapter.ViewHolder) convertView.getTag();
        }
        if (appraiseInterface!=null){
            final ViewGroup viewGroup = convertView.findViewById(R.id.bg_appraise);
            viewGroup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (appraiseInterface!=null)
                        appraiseInterface.onClick(v,position);
                }
            });

        }
        TextView tv = viewHolder.getTv();
        tv.setText(list.get(position));
        return convertView;
    }

    static class ViewHolder{
        TextView tv;
        public ViewHolder(View view){
            tv = view.findViewById(R.id.tv_appraise);
        }
        public TextView getTv() {
            return tv;
        }
    }

    AppraiseInterface appraiseInterface;
    public void setOnItemClickListener(AppraiseInterface appraiseInterface){
        this.appraiseInterface = appraiseInterface;
    }
}
