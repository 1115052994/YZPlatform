package com.plt.yzplatform.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.plt.yzplatform.R;
import com.plt.yzplatform.utils.ImageUtil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2018/4/29.
 */

public class Grid_Updata extends BaseAdapter {
    private ArrayList<HashMap<String,String>> arr;
    private Context context;

    public Grid_Updata(ArrayList<HashMap<String, String>> arr, Context context) {
        this.arr = arr;
        this.context = context;
    }

    @Override
    public int getCount() {
        return arr.size()+10;
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
        convertView= LinearLayout.inflate(context,R.layout.item_grid,null);
        ImageView image = convertView.findViewById(R.id.grid_item_image);
        TextView text = convertView.findViewById(R.id.grid_item_text);
//        if(arr.get(position).get("image")==null){
//
//        }else {
            //将Drawable转化为Bitmap
            Resources res = context.getResources();
            Bitmap  bitmap = BitmapFactory.decodeResource(res, R.drawable.b_a_banner);
            // 获取圆角图片
            Bitmap roundBitmap = ImageUtil.getRoundedCornerBitmap(bitmap, 10.0f);
            image.setImageBitmap(roundBitmap);
//        }
//        text.setText(arr.get(position).get("text"));
        return convertView;
    }
}
