package com.xtzhangbinbin.jpq.adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xtzhangbinbin.jpq.AppraiseInterface;
import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.config.Config;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;

public class BrandGVAdapter extends BaseAdapter {

    private List<Map<String,String>> list;
    private Context context;
    public BrandGVAdapter(Context context,List<Map<String,String>> list){
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
        BrandGVAdapter.ViewHolder viewHolder;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_gv_carbrand,null);
            viewHolder = new BrandGVAdapter.ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (BrandGVAdapter.ViewHolder) convertView.getTag();
        }
        if (appraiseInterface!=null){
            //final ViewGroup viewGroup = convertView.findViewById(R.id.gv_ly);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(appraiseInterface!=null)
                        appraiseInterface.onClick(v,position);
                }
            });
        }
        // 品牌搜索
        TextView tv = viewHolder.getTv();
        if (list.get(position).get("tv_carbrand")!=null) {
            tv.setText(list.get(position).get("tv_carbrand"));
        }
        ImageView car =viewHolder.getCar();
        if(list.get(position).get("image_carbrand")!=null) {
            String url = Config.BASE_URL + Config.Y + list.get(position).get("image_carbrand");
            car.setImageBitmap(null);
//            Picasso.with(context).load(Uri.parse(url)).into(car);
            Picasso.get().load(Uri.parse(url)).into(car);
        }
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

