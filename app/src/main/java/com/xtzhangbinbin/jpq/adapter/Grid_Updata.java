package com.xtzhangbinbin.jpq.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.activity.CarPhotoActivity;
import com.xtzhangbinbin.jpq.entity.CarPhotos;
import com.xtzhangbinbin.jpq.utils.JumpUtil;
import com.xtzhangbinbin.jpq.utils.OKhttptils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2018/4/29.
 */

public class Grid_Updata extends BaseAdapter {
    private List<HashMap<String, String>> arr = new ArrayList<>();
    private Context context;
    private HashMap<String, String> map;
    private int a ;
    private List<CarPhotos> carPhotos;

    public Grid_Updata(List<HashMap<String, String>> arr, Context context,int a,List<CarPhotos> carPhotos) {
        this.arr = arr;
        this.context = context;
        this.a = a ;
        this.carPhotos = carPhotos;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_grid, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        map = arr.get(position);
            for (String key : map.keySet()) {
                if (map.get(key) == null ){
                    Picasso.with(context).load(R.drawable.no_pic).into(viewHolder.grid_item_image);
                    viewHolder.grid_item_text.setText(key);
                }else {
                    viewHolder.grid_item_text.setText(key);
                    OKhttptils.getPic(context, map.get(key), viewHolder.grid_item_image);
                }
            }

            viewHolder.grid_item_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();

                    bundle.putInt("position",position+a);
                    bundle.putSerializable("photo_ids", (Serializable) carPhotos);
                    bundle.putString("type","修改");
                    JumpUtil.newInstance().jumpRight(context, CarPhotoActivity.class,bundle);
                }
            });
        return convertView;
    }

    public static class ViewHolder {
        public View rootView;
        public ImageView grid_item_image;
        public TextView grid_item_text;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.grid_item_image = (ImageView) rootView.findViewById(R.id.mPic);
            this.grid_item_text = (TextView) rootView.findViewById(R.id.mText);
        }

    }
}
