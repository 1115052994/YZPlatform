package com.xtzhangbinbin.jpq.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.config.Config;
import com.xtzhangbinbin.jpq.entity.QueryEvaluate;
import com.xtzhangbinbin.jpq.utils.NetUtil;
import com.xtzhangbinbin.jpq.utils.OKhttptils;
import com.xtzhangbinbin.jpq.view.CircleImageView;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/4/22.
 */

public class EvaluationAdapter extends RecyclerView.Adapter<EvaluationAdapter.ViewHolder> {
    private Context context;
    private List<QueryEvaluate.DataBean.ResultBean> result;
    private Map<String, String> map = new HashMap<>();
    private ArrayList<String> arr= new ArrayList<>();
    private ArrayList<String> pj=new ArrayList<>();

    public EvaluationAdapter(Context context, List<QueryEvaluate.DataBean.ResultBean> result) {
        this.context = context;
        this.result = result;
    }

    @Override
    public EvaluationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new EvaluationAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_evaluation, parent, false));
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(EvaluationAdapter.ViewHolder holder, int position) {
        Log.d("aaaaaa", "aaaaaaaaaa"+result.get(position).getLog_id());
        if(result.size()==4){
            for (int i = 0; i <result.size() ; i++) {
                Log.d("aaaaaa", "onRefresh: "+result.get(i).getLog_3());
            }
            Log.d("aaaaaa", "success: "+result.size());
        }
        OKhttptils.getPicByHttp(context,result.get(position).getPers_head_file_id(),holder.evaluation_image);
        holder.evaluation_list_star5.setImageResource(R.drawable.pj_hstar);
        holder.evaluate_layout.removeAllViews();
        holder.evaluation_list_appraiseTime.setText(result.get(position).getLog_date());

        holder.evaluation_list_appraise.setText(result.get(position).getLog_4());
        arr.clear();
        String[] split = result.get(position).getLog_3().split(",");
        for (int i = 0; i <split.length ; i++) {
            arr.add(split[i]);
        }
        if(arr.size()==split.length){
            getDictionaries(Config.GETVALUE,arr,holder.evaluate_layout);
        }
        holder.evaluation_list_phone.setText(result.get(position).getPers_nickname());
        holder.evaluation_list_star5.setImageResource(R.drawable.pj_hstar);
        holder.evaluation_list_star4.setImageResource(R.drawable.pj_hstar);
        holder.evaluation_list_star3.setImageResource(R.drawable.pj_hstar);
        holder.evaluation_list_star2.setImageResource(R.drawable.pj_hstar);
        holder.evaluation_list_star1.setImageResource(R.drawable.pj_hstar);
        switch (result.get(position).getLog_2()) {
            case "5":
                holder.evaluation_list_star5.setImageResource(R.drawable.pj_pinkstar);
            case "4":
                holder.evaluation_list_star4.setImageResource(R.drawable.pj_pinkstar);
            case "3":
                holder.evaluation_list_star3.setImageResource(R.drawable.pj_pinkstar);
            case "2":
                holder.evaluation_list_star2.setImageResource(R.drawable.pj_pinkstar);
            case "1":
                holder.evaluation_list_star1.setImageResource(R.drawable.pj_pinkstar);
                break;
        }
    }
    public void getDictionaries(String url, final ArrayList<String> arr, final TagFlowLayout evaluate_layout){
        pj.clear();
        Log.d("bbbbbb", "getDictionaries: "+arr.size());
        for (int i = 0; i <arr.size() ; i++) {
            map.clear();
            map.put("dict_id",arr.get(i));
            if (NetUtil.isNetAvailable(context)) {
                OKhttptils.post((Activity) context, url, map, new OKhttptils.HttpCallBack() {
                    @Override
                    public void success(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject data = jsonObject.getJSONObject("data");
                            String str = data.get("result").toString();
                            Log.d("bbbbbb", "success: -----------------------"+str);

                            pj.add(str);
                            Log.d("bbbbbb", "success: "+pj.size());
                            if(pj.size()==arr.size()){
                                for (int i = 0; i <pj.size() ; i++) {
                                    evaluate_layout.setAdapter(new TagAdapter<String>(pj)
                                    {
                                        final LayoutInflater mInflater = LayoutInflater.from(context);
                                        @Override
                                        public View getView(FlowLayout parent, int position, String s)
                                        {
                                            TextView tv = (TextView) mInflater.inflate(R.layout.item_evaluate_type, evaluate_layout, false);
                                            tv.setText(s);
                                            return tv;
                                        }
                                    });
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void fail(String response) {
                        Toast.makeText(context, "查询失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }




    }


    @Override
    public int getItemCount() {
        return result.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TagFlowLayout evaluate_layout;
        private CircleImageView evaluation_image;
        private TextView evaluation_list_phone,evaluation_list_appraiseTime,evaluation_list_appraise;
        private ImageView evaluation_list_star1,evaluation_list_star2,evaluation_list_star3,evaluation_list_star4,evaluation_list_star5;
        public ViewHolder(View itemView) {
            super(itemView);
            evaluation_image=itemView.findViewById(R.id.evaluation_image);
            evaluation_list_phone=itemView.findViewById(R.id.evaluation_list_phone);
            evaluation_list_appraiseTime=itemView.findViewById(R.id.evaluation_list_appraiseTime);
            evaluation_list_appraise=itemView.findViewById(R.id.evaluation_list_appraise);
            evaluation_list_star1=itemView.findViewById(R.id.evaluation_list_star1);
            evaluation_list_star2=itemView.findViewById(R.id.evaluation_list_star2);
            evaluation_list_star3=itemView.findViewById(R.id.evaluation_list_star3);
            evaluation_list_star4=itemView.findViewById(R.id.evaluation_list_star4);
            evaluation_list_star5=itemView.findViewById(R.id.evaluation_list_star5);
            evaluate_layout=itemView.findViewById(R.id.evaluate_layout);
        }
    }
}
