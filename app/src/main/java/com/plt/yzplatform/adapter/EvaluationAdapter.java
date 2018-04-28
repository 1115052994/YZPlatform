package com.plt.yzplatform.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.plt.yzplatform.R;
import com.plt.yzplatform.config.Config;
import com.plt.yzplatform.entity.QueryEvaluate;
import com.plt.yzplatform.utils.NetUtil;
import com.plt.yzplatform.utils.OKhttptils;
import com.plt.yzplatform.view.AutoNextLineLinearlayout;
import com.plt.yzplatform.view.CircleImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/4/22.
 */

public class EvaluationAdapter extends BaseAdapter {
    private Context context;
    private List<QueryEvaluate.DataBean.ResultBean> result;
    private Map<String, String> map = new HashMap<>();
    public EvaluationAdapter(Context context, List<QueryEvaluate.DataBean.ResultBean> result) {
        this.context = context;
        this.result = result;
    }

    @Override
    public int getCount() {
        return result.size();
    }

    @Override
    public Object getItem(int position) {
        return result.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if(convertView==null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_evaluation, null);
            viewHolder.evaluation_image=convertView.findViewById(R.id.evaluation_image);
            viewHolder.evaluation_list_phone=convertView.findViewById(R.id.evaluation_list_phone);
            viewHolder.evaluation_list_appraiseTime=convertView.findViewById(R.id.evaluation_list_appraiseTime);
            viewHolder.evaluation_list_appraise=convertView.findViewById(R.id.evaluation_list_appraise);
            viewHolder.evaluate_layout=convertView.findViewById(R.id.evaluate_layout);
            viewHolder.evaluation_list_star1=convertView.findViewById(R.id.evaluation_list_star1);
            viewHolder.evaluation_list_star2=convertView.findViewById(R.id.evaluation_list_star2);
            viewHolder.evaluation_list_star3=convertView.findViewById(R.id.evaluation_list_star3);
            viewHolder.evaluation_list_star4=convertView.findViewById(R.id.evaluation_list_star4);
            viewHolder.evaluation_list_star5=convertView.findViewById(R.id.evaluation_list_star5);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        //TODO  list有卡顿记得优化
        Log.w("test", viewHolder.evaluation_image.toString());
        if(result.get(position).getPers_head_file_id()!=null)
            OKhttptils.getPic(context,result.get(position).getPers_head_file_id(),viewHolder.evaluation_image);
//        getBitmap(result.get(position).getPers_head_file_id(),viewHolder.evaluation_image);
        viewHolder.evaluation_list_star5.setImageResource(R.drawable.pj_hstar);
        viewHolder.evaluate_layout.removeAllViews();
        viewHolder.evaluation_list_appraiseTime.setText(result.get(position).getLog_date());
        viewHolder.evaluation_list_appraise.setText(result.get(position).getLog_4());
        String[] split = result.get(position).getLog_3().split(",");
        for (int i = 0; i <split.length ; i++) {
            getDictionaries(Config.GETVALUE,split[i],viewHolder.evaluate_layout,i);
        }
        viewHolder.evaluation_list_phone.setText(result.get(position).getPers_nickname());
//        FlowLayout viewById = convertView.findViewById(R.id.evaluation_list_flow);
//        fillAutoSpacingLayout(viewById,split);
        Log.w("test", result.get(position).getLog_2() + ",");
        viewHolder.evaluation_list_star5.setImageResource(R.drawable.pj_hstar);
        viewHolder.evaluation_list_star4.setImageResource(R.drawable.pj_hstar);
        viewHolder.evaluation_list_star3.setImageResource(R.drawable.pj_hstar);
        viewHolder.evaluation_list_star2.setImageResource(R.drawable.pj_hstar);
        viewHolder.evaluation_list_star1.setImageResource(R.drawable.pj_hstar);

        switch (result.get(position).getLog_2()) {
            case "5":
                viewHolder.evaluation_list_star5.setImageResource(R.drawable.pj_pinkstar);
            case "4":
                viewHolder.evaluation_list_star4.setImageResource(R.drawable.pj_pinkstar);
            case "3":
                viewHolder.evaluation_list_star3.setImageResource(R.drawable.pj_pinkstar);
            case "2":
                viewHolder.evaluation_list_star2.setImageResource(R.drawable.pj_pinkstar);
            case "1":
                viewHolder.evaluation_list_star1.setImageResource(R.drawable.pj_pinkstar);
                break;
        }

        return convertView;
    }
    static class ViewHolder{
        AutoNextLineLinearlayout evaluate_layout;
        CircleImageView evaluation_image;
        TextView evaluation_list_phone,evaluation_list_appraiseTime,evaluation_list_appraise;
        ImageView evaluation_list_star1,evaluation_list_star2,evaluation_list_star3,evaluation_list_star4,evaluation_list_star5;
    }

//    public void getBitmap(final String file_id, final ImageView circleImageView){
//        //通过ID获得图片
//        if(file_id!=null){
//            if (NetUtil.isNetAvailable(context)) {
//                map.clear();
//                map.put("file_id",file_id);
//                OKhttptils.post((Activity) context, Config.GET_BASE64, map, new OKhttptils.HttpCallBack() {
//                    @Override
//                    public void success(String response) {
//                        Log.w("aaa", "onResponse获取base64: " + response );
//                        try {
//                            JSONObject jsonObject = new JSONObject(response);
//                            if (jsonObject.getString("status").equals("1")) {
//                                String data = jsonObject.getString("data");
//                                JSONObject object = new JSONObject(data);
//                                String file_content = object.getString("file_content");
//                                if (file_content.contains("base64,"))
//                                    file_content = file_content.split("base64,")[1];
//                                Bitmap bitmap = PhotoUtils.base64ToBitmap(file_content);
//                                Log.d("aaaa", "file_id: "+bitmap);
//                                if (bitmap!=null)
//                                    circleImageView.setImageBitmap(bitmap);
//                                else
//                                    circleImageView.setImageResource(R.drawable.circle_2);
//                            } else {
//                                ToastUtil.noNAR(context);
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    @Override
//                    public void fail(String response) {
//                        Toast.makeText(context, "获取失败", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            } else {
//                ToastUtil.noNetAvailable(context);
//            }
//        }
//    }
    public void getDictionaries(String url, String dict_id, final AutoNextLineLinearlayout evaluate_layout, final int i){
        map.clear();
        map.put("dict_id",dict_id);
        if (NetUtil.isNetAvailable(context)) {
            OKhttptils.post((Activity) context, url, map, new OKhttptils.HttpCallBack() {
                @Override
                public void success(String response) {
                    Log.i("aaaaa", "查询评价: " + response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONObject data = jsonObject.getJSONObject("data");
                        String str = data.get("result").toString();
                        addView1(evaluate_layout,str,null);
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

    //flowLayout
//    private void fillAutoSpacingLayout(FlowLayout flowLayout, String[] str) {
//        flowLayout.setChildSpacing(20);
//        for (String text : str) {
//            TextView textView = buildLabel(text);
//            flowLayout.addView(textView);
//        }
//    }
//
//    private TextView buildLabel(String text) {
//        TextView textView = new TextView(context);
//        textView.setText(text);
//        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
//        textView.setPadding((int)dpToPx(16), (int)dpToPx(2), (int)dpToPx(16), (int)dpToPx(4));
//        textView.setBackgroundResource(R.drawable.label_bg);
//        textView.setTextColor(Color.parseColor("#ff9696"));
//        return textView;
//    }
//
//    private float dpToPx(float dp){
//        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,context.getResources().getDisplayMetrics());
//    }

    private void addView1(LinearLayout linearLayout, final String s, final String id) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.item_evaluate_type, null);
        TextView tv3 = inflate.findViewById(R.id.text3);
        tv3.setText(s);
        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        linearLayout.addView(tv3);
    }
}
