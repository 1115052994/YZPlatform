package com.xtzhangbinbin.jpq.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xtzhangbinbin.jpq.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/4/30.
 */

public class WeisjAdapter extends BaseAdapter {
    private Context context;

    public WeisjAdapter(Context context) {
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
        ViewHolder viewHolder;
        if(convertView==null){
            convertView = LinearLayout.inflate(context, R.layout.item_weisj_list, null);
            viewHolder= new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }



        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.weisj_image)
        ImageView weisjImage;
        @BindView(R.id.weisj_name)
        TextView weisjName;
        @BindView(R.id.weisj_time)
        TextView weisjTime;
        @BindView(R.id.weisj_shanchu)
        RelativeLayout weisjShanchu;
        @BindView(R.id.weisj_bianji)
        RelativeLayout weisjBianji;
        @BindView(R.id.weisj_cjh)
        TextView weisjCjh;
        @BindView(R.id.weisj_sh)
        TextView weisjSh;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
