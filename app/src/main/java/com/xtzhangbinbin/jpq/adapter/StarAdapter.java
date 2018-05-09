package com.xtzhangbinbin.jpq.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.config.Config;
import com.xtzhangbinbin.jpq.entity.Querystar;
import com.xtzhangbinbin.jpq.utils.NetUtil;
import com.xtzhangbinbin.jpq.utils.OKhttptils;
import com.xtzhangbinbin.jpq.utils.PhotoUtils;
import com.xtzhangbinbin.jpq.utils.ToastUtil;
import com.xtzhangbinbin.jpq.view.CircleImageView;
import com.xtzhangbinbin.jpq.view.OrdinaryDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StarAdapter extends BaseAdapter {
    private Context context;
    private String staff_id;
    private CallStar callRenewal;
    private String staff_photo_file_id;
    private Map<String, String> map = new HashMap<>();
    private List<Querystar.DataBean.ResultBean> result;

    public StarAdapter(Context context, List<Querystar.DataBean.ResultBean> result) {
        this.context = context;
        this.result = result;
    }

    @Override
    public int getCount() {
        return result.size();
    }

    @Override
    public Object getItem(int i) {
        return result.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        if(view==null){
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.item_star, null);
            viewHolder.item_head_image=view.findViewById(R.id.star_item_head_image);
            viewHolder.item_name=view.findViewById(R.id.star_item_name);
            viewHolder.item_introduce=view.findViewById(R.id.star_item_introduce);
            viewHolder.item_lilayout1=view.findViewById(R.id.item_lilayout1);
            viewHolder.item_lilayout2=view.findViewById(R.id.item_lilayout2);
            viewHolder.item_estimate=view.findViewById(R.id.item_estimate);
            view.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) view.getTag();
        }
        Log.d("getView", "getView: "+result.get(i).getStaff_name());
        viewHolder.item_name.setText("姓名：" + result.get(i).getStaff_name());
        viewHolder.item_introduce.setText("介绍：" + result.get(i).getStaff_info());
        OKhttptils.getPicByHttp(context, result.get(i).getStaff_photo_file_id(), viewHolder.item_head_image);
        Log.w("test", result.get(i).getStaff_state() + "......");
        switch (result.get(i).getStaff_state()){
            case "1":
                viewHolder.item_estimate.setText("审核不通过");
                break;
            case "2":
                viewHolder.item_estimate.setText("审核通过");
                break;
            case "3":
                viewHolder.item_estimate.setText("待审核");
                break;
        }

        //删除点击事件
        viewHolder.item_lilayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                staff_id = result.get(i).getStaff_id();
                staff_photo_file_id = result.get(i).getStaff_photo_file_id();
                final OrdinaryDialog ordinaryDialog = OrdinaryDialog.newInstance(context).setMessage1("删除明星员工").setMessage2("删除后不可恢复，确定清除？").showDialog();
                ordinaryDialog.setNoOnclickListener(new OrdinaryDialog.onNoOnclickListener() {
                    @Override
                    public void onNoClick() {
                        ordinaryDialog.dismiss();
                    }
                });
                ordinaryDialog.setYesOnclickListener(new OrdinaryDialog.onYesOnclickListener() {
                    @Override
                    public void onYesClick() {
                        if(callRenewal!=null){
                            if (NetUtil.isNetAvailable(context)) {
                                map.clear();
                                map.put("staff_id",staff_id);
                                map.put("staff_photo_file_id",staff_photo_file_id);
                                OKhttptils.post((Activity) context, Config.DELETESTAR, map, new OKhttptils.HttpCallBack() {
                                    @Override
                                    public void success(String response) {
                                        Log.i("aaaa", "删除: " + response);
                                        if(callRenewal!=null){
                                            callRenewal.Call(view,staff_id,-1,null,i);
                                        }

                                    }
                                    @Override
                                    public void fail(String response) {
                                        Toast.makeText(context, "删除失败", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                        ordinaryDialog.dismiss();
                    }
                });

            }
        });
        //编辑点击事件
        viewHolder.item_lilayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                staff_id = result.get(i).getStaff_id();
                callRenewal.Call(view,staff_id,i,result.get(i).getStaff_photo_file_id(),i);
            }
        });
        return view;
    }
    class ViewHolder{
        CircleImageView item_head_image;
        TextView item_name,item_introduce,item_estimate;
        RelativeLayout item_lilayout1,item_lilayout2;
    }
    public void getcall(CallStar callRenewal){
        this.callRenewal=callRenewal;
    }



}
