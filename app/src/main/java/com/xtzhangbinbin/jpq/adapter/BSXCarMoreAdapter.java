package com.xtzhangbinbin.jpq.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xtzhangbinbin.jpq.AppraiseInterface;
import com.xtzhangbinbin.jpq.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/4/25.
 */

public class BSXCarMoreAdapter extends BaseAdapter {
    private List<String> list;
    private Context context;
    private Map<Integer, View> map;
    private int index;

    public BSXCarMoreAdapter(Context context,List<String> list,  int index) {
        this.list = list;
        this.context = context;
        this.index = index;
        map = new HashMap<>();
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

        if(map.containsKey(position)){
            convertView = map.get(position);
        } else {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_gv_carsxmoreone,null);
            map.put(position, convertView);
        }

        final ViewGroup viewGroup = convertView.findViewById(R.id.bg);
        if (position == index){
//            viewGroup.setSelected(true);
            if (appraiseInterface!=null){
                appraiseInterface.onClick(viewGroup,index);
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
        TextView tv = convertView.findViewById(R.id.tv_type);
        tv.setText(list.get(position));
        return convertView;
    }


    AppraiseInterface appraiseInterface;
    public void setOnItemClickListener(AppraiseInterface appraiseInterface){
        this.appraiseInterface = appraiseInterface;
    }
}
