package com.plt.yzplatform.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.plt.yzplatform.AppraiseInterface;
import com.plt.yzplatform.R;
import com.plt.yzplatform.activity.CarBrandSearch;
import com.plt.yzplatform.config.Config;
import com.plt.yzplatform.utils.OKhttptils;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;

public class AdvanceSXCarAdapter extends BaseAdapter {

    private List<Map<String,String>> list;
    private Context context;
    public AdvanceSXCarAdapter(Context context,List<Map<String,String>> list){
        this.context = context;
        this.list = list;
    }
    @Override
    public int getCount() {
        return list==null?0:list.size();
    }

    @Override
    public Map<String,String> getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        AdvanceSXCarAdapter.ViewHolder viewHolder;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_gv_sxtypecar,null);
            viewHolder = new AdvanceSXCarAdapter.ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (AdvanceSXCarAdapter.ViewHolder) convertView.getTag();
        }
        final ViewGroup viewGroup = convertView.findViewById(R.id.gv_ly);
        if (position == 0){
            //viewGroup.setSelected(true);
            if(appraiseInterface!=null){
                appraiseInterface.onClick(viewGroup,0);
            }
        }
        if (appraiseInterface!=null){
            viewGroup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(appraiseInterface!=null)
                        appraiseInterface.onClick(v,position);
                }
            });
        }
        TextView tv = viewHolder.getTv();
        tv.setText(list.get(position).get("paramName"));
        ImageView car =viewHolder.getCar();
        String url = Config.BASE_URL + Config.Y + list.get(position).get("img");
        car.setImageBitmap(null);
        Picasso.with(context).load(Uri.parse(url)).into(car);
        return convertView;
    }

    static class ViewHolder{
        TextView tv;
        ImageView car;
        public ViewHolder(View view){
            tv = view.findViewById(R.id.tv_carType);
            car = view.findViewById(R.id.image_car);
        }
        public TextView getTv() {
            return tv;
        }

        public ImageView getCar() {
            return car;
        }
    }

    AppraiseInterface appraiseInterface;
    public void setOnItemClickListener(AppraiseInterface appraiseInterface){
        this.appraiseInterface = appraiseInterface;
    }
}