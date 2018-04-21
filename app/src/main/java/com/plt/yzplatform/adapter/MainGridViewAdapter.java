package com.plt.yzplatform.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.plt.yzplatform.R;
import com.plt.yzplatform.config.Config;
import com.plt.yzplatform.entity.CarServiceImage;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by glp on 2018/4/19.
 * 描述：
 */

public class MainGridViewAdapter extends BaseAdapter {
    private List<CarServiceImage.DataBean.ResultBean.ItemListBean> mDatas;
    private LayoutInflater inflater;
    private Context context;
    /**
     * 页数下标,从0开始(当前是第几页)
     */
    private int curIndex;
    /**
     * 每一页显示的个数
     */
    private int pageSize;

    public MainGridViewAdapter(Context context, List<CarServiceImage.DataBean.ResultBean.ItemListBean> mDatas, int curIndex, int pageSize) {
        inflater = LayoutInflater.from(context);
        this.mDatas = mDatas;
        this.curIndex = curIndex;
        this.pageSize = pageSize;
        this.context = context;
    }

    /**
     * 先判断数据集的大小是否足够显示满本页,如果够，则直接返回每一页显示的最大条目个数pageSize,如果不够，则有几项就返回几,(也就是最后一页的时候就显示剩余item)
     */
    @Override
    public int getCount() {
        return mDatas.size() > (curIndex + 1) * pageSize ? pageSize : (mDatas.size() - curIndex * pageSize);

    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position + curIndex * pageSize);
    }

    @Override
    public long getItemId(int position) {
        return position + curIndex * pageSize;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_gridview, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tv = (TextView) convertView.findViewById(R.id.textView);
            viewHolder.iv = (ImageView) convertView.findViewById(R.id.imageView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        /**
         * 在给View绑定显示的数据时，计算正确的position = position + curIndex * pageSize
         */
        int pos = position + curIndex * pageSize;
        viewHolder.tv.setText(mDatas.get(pos).getServerDesc());
        Picasso.with(context).load(Config.BASE_URL + Config.Y + mDatas.get(pos).getServerImgPath()).into(viewHolder.iv);
        /* 下载好的图片需要缓存到本地 */
//        viewHolder.iv.setImageBitmap(mDatas.get(pos).getServerImgPath());
        return convertView;
    }


    class ViewHolder {
        public TextView tv;
        public ImageView iv;
    }
}
