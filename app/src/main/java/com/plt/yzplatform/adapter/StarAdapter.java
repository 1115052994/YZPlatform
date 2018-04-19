package com.plt.yzplatform.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.plt.yzplatform.R;
import com.plt.yzplatform.view.CircleImageView;

import java.util.ArrayList;
import java.util.List;

public class StarAdapter extends BaseAdapter {
    private Context context;
    private CallStar callRenewal;
    private List<ArrayList<String>> arr;

    public StarAdapter(Context context, List<ArrayList<String>> arr) {
        this.context = context;
        this.arr = arr;
    }

    @Override
    public int getCount() {
        return arr.size();
    }

    @Override
    public Object getItem(int i) {
        return arr.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if(view==null){
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.star_item, null);
            viewHolder.item_head_image=view.findViewById(R.id.star_item_head_image);
            viewHolder.item_name=view.findViewById(R.id.star_item_name);
            viewHolder.item_introduce=view.findViewById(R.id.star_item_introduce);
            viewHolder.item_lilayout1=view.findViewById(R.id.item_lilayout1);
            viewHolder.item_lilayout2=view.findViewById(R.id.item_lilayout2);
            view.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) view.getTag();
        }
        viewHolder.item_name.setText(arr.get(i).get(0));
        viewHolder.item_introduce.setText(arr.get(i).get(1));
        viewHolder.item_lilayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, i+"删除", Toast.LENGTH_SHORT).show();
                arr.remove(i);
                if(callRenewal!=null){
                    callRenewal.Call(view);
                }
            }
        });

        viewHolder.item_lilayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, i+"编辑", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
    class ViewHolder{
        CircleImageView item_head_image;
        TextView item_name,item_introduce;
        RelativeLayout item_lilayout1,item_lilayout2;
    }
    public void getcall(CallStar callRenewal){
        this.callRenewal=callRenewal;
    }

}
