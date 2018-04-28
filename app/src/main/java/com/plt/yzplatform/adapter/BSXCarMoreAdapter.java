package com.plt.yzplatform.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.plt.yzplatform.AppraiseInterface;
import com.plt.yzplatform.R;

import java.util.List;

/**
 * Created by Administrator on 2018/4/25.
 */

public class BSXCarMoreAdapter extends BaseAdapter {
    private List<String> list;
    private Context context;
    public BSXCarMoreAdapter(Context context,List<String> list){
        this.context = context;
        this.list = list;
        Log.w("test", list.size() + "aaaaaaaaa");
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
        ViewHolder viewHolder;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_gv_carsxmoreone,null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final ViewGroup viewGroup = convertView.findViewById(R.id.bg);
        if (position == 0){
            Log.w("test", "aaabbb");
            viewGroup.setSelected(true);
            Log.w("test", ((TextView)viewGroup.getChildAt(0)).getText().toString());
            if (appraiseInterface!=null){
                appraiseInterface.onClick(viewGroup,0);
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
        TextView tv = viewHolder.getTv();
        tv.setText(list.get(position));
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
}
