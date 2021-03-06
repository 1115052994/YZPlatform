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

public class AdvanceSXCarMoreAdapter extends BaseAdapter{
    private List<String> list;
    private Context context;
    public AdvanceSXCarMoreAdapter(Context context,List<String> list){
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
        AdvanceSXCarMoreAdapter.ViewHolder viewHolder;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_gv_carsxmore,null);
            viewHolder = new AdvanceSXCarMoreAdapter.ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (AdvanceSXCarMoreAdapter.ViewHolder) convertView.getTag();
        }

        TextView tv = viewHolder.getTv();
        tv.setText(list.get(position));

        final ViewGroup viewGroup = convertView.findViewById(R.id.bg);
        if (parent.getChildCount() == position) {
            //里面就是正常的position
            if (position == 0){
                //viewGroup.setSelected(true);
                if (appraiseInterface!=null){
                    appraiseInterface.onClick(viewGroup,0);
                }
            }
            // 选中第几个
            if (this.index!=0&&this.index == position){
                if (appraiseInterface != null) {
                    appraiseInterface.onClick(viewGroup, this.index);
                }
            }
        }

        if (appraiseInterface!=null){
            viewGroup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (appraiseInterface!=null)
                        appraiseInterface.onClick(v,position);
                }
            });
        }
        return convertView;
    }

    static class ViewHolder{
        TextView tv;
        public ViewHolder(View view){
            tv = view.findViewById(R.id.tv_type);
        }
        public TextView getTv() {
            return tv;
        }
    }

    AppraiseInterface appraiseInterface;
    public void setOnItemClickListener(AppraiseInterface appraiseInterface){
        this.appraiseInterface = appraiseInterface;
    }

    private int index = 0;
    public void setPosition(int position){
        this.index = position;
    }
}
