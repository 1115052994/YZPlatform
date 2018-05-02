package com.xtzhangbinbin.jpq.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xtzhangbinbin.jpq.R;
import com.suke.widget.SwitchButton;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/4/30.
 */

public class CarBeautyAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> arr;
    private onCallBackxiugai onCallBackxiugai;
    private onCallBackshanchu onCallBackshanchu;

    public CarBeautyAdapter(Context context, ArrayList<String> arr) {
        this.context = context;
        this.arr = arr;
    }

    @Override
    public int getCount() {
        return arr.size();
    }

    @Override
    public Object getItem(int position) {
        return arr.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LinearLayout.inflate(context, R.layout.item_car_beauty, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.name.setText(arr.get(position));
        viewHolder.switch_button.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                Log.d("aaaa", "onCheckedChanged: "+isChecked);
            }
        });


        return convertView;
    }
    private interface onCallBackxiugai{
        void getdata();
    }
    private interface onCallBackshanchu{
        void getdata();
    }
    public void xiugai(onCallBackxiugai onCallBackxiugai){
        this.onCallBackxiugai=onCallBackxiugai;
    }
    public void shanchu(onCallBackshanchu onCallBackshanchu){
        this.onCallBackshanchu=onCallBackshanchu;
    }



    static class ViewHolder {
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.type)
        TextView type;
        @BindView(R.id.youhui)
        TextView youhui;
        @BindView(R.id.zhongjia)
        TextView zhongjia;
        @BindView(R.id.dingdan)
        TextView dingdan;
        @BindView(R.id.yishiyong)
        TextView yishiyong;
        @BindView(R.id.weishiyong)
        TextView weishiyong;
        @BindView(R.id.shanchu)
        RelativeLayout shanchu;
        @BindView(R.id.bianji)
        RelativeLayout bianji;
        @BindView(R.id.switch_button)
        SwitchButton switch_button;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
