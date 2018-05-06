package com.xtzhangbinbin.jpq.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.config.Config;
import com.xtzhangbinbin.jpq.entity.QueryCarList;
import com.xtzhangbinbin.jpq.utils.NetUtil;
import com.xtzhangbinbin.jpq.utils.OKhttptils;
import com.xtzhangbinbin.jpq.utils.PhotoUtils;
import com.xtzhangbinbin.jpq.utils.ToastUtil;
import com.xtzhangbinbin.jpq.view.OrdinaryDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2018/4/20.
 */
public class CarListAdapter extends BaseAdapter {
    private Context context;
    private List<QueryCarList.DataBean.ResultBean> result;
    public CallCollect callcollect;
    private HashMap<String,String> map=new HashMap<>();

    public CarListAdapter(Context context, List<QueryCarList.DataBean.ResultBean> result) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if(convertView==null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_collect_car, null);
            viewHolder.car_deal_image=convertView.findViewById(R.id.car_deal_image);
            viewHolder.car_deal_tv=convertView.findViewById(R.id.car_deal_tv);
            viewHolder.car_deal_km=convertView.findViewById(R.id.car_deal_km);
            viewHolder.car_deal_price=convertView.findViewById(R.id.car_deal_price);
            viewHolder.car_deal_dial=convertView.findViewById(R.id.car_deal_dial);
            viewHolder.car_deal_delete=convertView.findViewById(R.id.car_deal_delete);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        viewHolder.car_deal_dial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转电话
                Intent intent=new Intent("android.intent.action.CALL", Uri.parse("tel:"+result.get(position).getCar_phone()));
                context.startActivity(intent);
            }
        });
        viewHolder.car_deal_tv.setText(result.get(position).getCar_name());
        viewHolder.car_deal_km.setText(String.valueOf(result.get(position).getCar_mileage()));
        viewHolder.car_deal_price.setText(String.valueOf(result.get(position).getCar_price()));
        getBitmap(result.get(position).getCar_1_file_id(),viewHolder.car_deal_image);

        viewHolder.car_deal_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Toast.makeText(context, position+"删除", Toast.LENGTH_SHORT).show();
                final OrdinaryDialog ordinaryDialog = OrdinaryDialog.newInstance(context).setMessage1("温馨提示").setMessage2("  删除后不可恢复，确定清除？").setCancel("取消").setConfirm("确定").showDialog();
                ordinaryDialog.setNoOnclickListener(new OrdinaryDialog.onNoOnclickListener() {
                    @Override
                    public void onNoClick() {
                        ordinaryDialog.dismiss();
                    }
                });
                ordinaryDialog.setYesOnclickListener(new OrdinaryDialog.onYesOnclickListener() {
                    @Override
                    public void onYesClick() {
                        if(callcollect!=null){
                            if (NetUtil.isNetAvailable(context)) {
                                    map.clear();
                                    map.put("coll_id",result.get(position).getColl_id());
                                    OKhttptils.post((Activity) context, Config.REMOVECOLL, map, new OKhttptils.HttpCallBack() {
                                        @Override
                                        public String success(String response) {
                                            Log.i("aaaa", "删除: " + response);
                                            if(callcollect!=null){
                                                callcollect.getCallcollect(view,0,position);
                                            }

                                            return response;
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


        return convertView;
    }

    class ViewHolder{
        ImageView car_deal_image;
        TextView car_deal_tv,car_deal_km,car_deal_price;
        LinearLayout car_deal_dial,car_deal_delete;
    }
    public void getCallCar(CallCollect callcollect){
        this.callcollect=callcollect;
    }
    public void getBitmap(final String file_id, final ImageView circleImageView){
        //通过ID获得图片
        if(file_id!=null){
            if (NetUtil.isNetAvailable(context)) {
                map.clear();
                map.put("file_id",file_id);
                OKhttptils.post((Activity) context, Config.GET_BASE64, map, new OKhttptils.HttpCallBack() {
                    @Override
                    public String success(String response) {
                        Log.w("aaa", "onResponse获取base64: " + response );
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("status").equals("1")) {
                                String data = jsonObject.getString("data");
                                JSONObject object = new JSONObject(data);
                                String file_content = object.getString("file_content");
                                if (file_content.contains("base64,"))
                                    file_content = file_content.split("base64,")[1];
                                Bitmap bitmap = PhotoUtils.base64ToBitmap(file_content);
                                Log.d("aaaa", "file_id: "+bitmap);
                                if (bitmap!=null)
                                    circleImageView.setImageBitmap(bitmap);
                                else
                                    circleImageView.setImageResource(R.drawable.qy_heat);
                            } else {
                                ToastUtil.noNAR(context);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        return response;
                    }
                    @Override
                    public void fail(String response) {
                        Toast.makeText(context, "获取失败", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                ToastUtil.noNetAvailable(context);
            }
        }
    }

}
