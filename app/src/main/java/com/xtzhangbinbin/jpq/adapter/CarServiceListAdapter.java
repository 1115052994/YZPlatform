package com.xtzhangbinbin.jpq.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.activity.CompDetail;
import com.xtzhangbinbin.jpq.config.Config;
import com.xtzhangbinbin.jpq.entity.CarServiceList;
import com.xtzhangbinbin.jpq.utils.JumpUtil;
import com.xtzhangbinbin.jpq.utils.NetUtil;
import com.xtzhangbinbin.jpq.utils.OKhttptils;
import com.xtzhangbinbin.jpq.utils.PhotoUtils;
import com.xtzhangbinbin.jpq.utils.Prefs;
import com.xtzhangbinbin.jpq.utils.ToastUtil;
import com.xtzhangbinbin.jpq.view.ZQImageViewRoundOval;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by glp on 2018/4/20.
 * 描述：
 */

public class CarServiceListAdapter extends RecyclerView.Adapter<CarServiceListAdapter.ViewHolder> {
    private List<CarServiceList.DataBean.ResultBean> beanList = new ArrayList<>();
    private Context context;
    private CarServiceList.DataBean.ResultBean resultBean;

    public CarServiceListAdapter(List<CarServiceList.DataBean.ResultBean> beanList, Context context) {
        this.beanList = beanList;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_main_service_list, parent, false);
        ViewHolder holder = new ViewHolder(view,true);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        resultBean = beanList.get(position);
        String file_id = resultBean.getAuth_comp_img_head_file_id();
        String comp_name = resultBean.getAuth_comp_name();
        int grade =resultBean.getComp_eval_level();
        String distance = String.valueOf(resultBean.getApart());
        String comp_address = resultBean.getAuth_comp_addr();
        //2为认证过 1为未通过认证
        String auth_state = resultBean.getAuth_state();
        final String comp_id = resultBean.getComp_id();
        OKhttptils.getPic(context,file_id,holder.mIcon);
        holder.mName.setText(comp_name);
//        holder.mark.setNumStars(grade);
        holder.mark.setRating(grade);
        if ("1".equals(auth_state)){
            holder.isR.setImageDrawable(context.getResources().getDrawable(R.drawable.cf_hsrz));
        }else {
            holder.isR.setImageDrawable(context.getResources().getDrawable(R.drawable.cf_rz));
        }
        holder.mDistance.setText(distance + "km");
        holder.mAddress.setText(comp_address);
        holder.mDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CompDetail.class);
                intent.putExtra("comp_id",comp_id);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return beanList.size() == 0 ? 0 :beanList.size();
    }

    public void insert(CarServiceList.DataBean.ResultBean resultBean, int position) {
        beanList.add(position,resultBean);
        notifyDataSetChanged();
    }

    public void remove(int position) {
        beanList.remove(position);
        notifyDataSetChanged();
    }

    public void clear() {
        beanList.clear();
        notifyDataSetChanged();
    }

    public CarServiceList.DataBean.ResultBean getItem(int position) {
        if (position < beanList.size()) {
            return beanList.get(position);
        } else {
            return null;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View rootView;
        public ZQImageViewRoundOval mIcon;
        public TextView mName;
        public RatingBar mark;
        public TextView mDistance;
        public TextView mAddress;
        public LinearLayout mDetails;
        public ImageView isR;

        public ViewHolder(View rootView,boolean isItem) {
            super(rootView);
            if (isItem) {
                this.rootView = rootView;
                this.mIcon = (ZQImageViewRoundOval) rootView.findViewById(R.id.mIcon);
                mIcon.setType(ZQImageViewRoundOval.TYPE_ROUND);
                mIcon.setRoundRadius(5);
                this.mName = (TextView) rootView.findViewById(R.id.mName);
                this.mark = (RatingBar) rootView.findViewById(R.id.mark);
                this.mDistance = (TextView) rootView.findViewById(R.id.mDistance);
                this.mAddress = (TextView) rootView.findViewById(R.id.mAddress);
                this.mDetails = (LinearLayout) rootView.findViewById(R.id.mDetails);
                this.isR = (ImageView) rootView.findViewById(R.id.isR);
            }
        }

    }

}
