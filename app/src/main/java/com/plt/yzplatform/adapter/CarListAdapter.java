package com.plt.yzplatform.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.plt.yzplatform.R;

/**
 * Created by Administrator on 2018/4/20.
 */

public class CarListAdapter extends BaseAdapter {
    Context context;

    public CarListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if(convertView==null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_collect_car, null);
            viewHolder.car_deal_image=convertView.findViewById(R.id.car_deal_image);
            viewHolder.car_deal_tv=convertView.findViewById(R.id.car_deal_tv);
            viewHolder.car_deal_km=convertView.findViewById(R.id.car_deal_km);
            viewHolder.car_deal_price=convertView.findViewById(R.id.car_deal_price);
            viewHolder.car_deal_dial=convertView.findViewById(R.id.car_deal_dial);
            viewHolder.car_deal_delete=convertView.findViewById(R.id.car_deal_delete);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }

        return convertView;
    }

    class ViewHolder{
        ImageView car_deal_image;
        TextView car_deal_tv,car_deal_km,car_deal_price;
        LinearLayout car_deal_dial,car_deal_delete;
    }
}
