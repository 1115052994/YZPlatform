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

public class ServiceListAdapter extends BaseAdapter {
    Context context;

    public ServiceListAdapter(Context context) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_serviceprovider_car, null);
            viewHolder.service_provider_image=convertView.findViewById(R.id.service_provider_image);
            viewHolder.service_provider_tv=convertView.findViewById(R.id.service_provider_tv);
            viewHolder.service_provider_km=convertView.findViewById(R.id.service_provider_km);
            viewHolder.service_provider_dial=convertView.findViewById(R.id.service_provider_dial);
            viewHolder.service_provider_delete=convertView.findViewById(R.id.service_provider_delete);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }

        return convertView;
    }
    class ViewHolder{
        ImageView service_provider_image;
        TextView service_provider_tv,service_provider_km;
        LinearLayout service_provider_dial,service_provider_delete;
    }
}
