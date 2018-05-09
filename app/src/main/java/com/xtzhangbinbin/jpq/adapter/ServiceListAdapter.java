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
import com.xtzhangbinbin.jpq.entity.QueryCollectServer;
import com.xtzhangbinbin.jpq.utils.NetUtil;
import com.xtzhangbinbin.jpq.utils.OKhttptils;
import com.xtzhangbinbin.jpq.utils.PhotoUtils;
import com.xtzhangbinbin.jpq.utils.ToastUtil;
import com.xtzhangbinbin.jpq.view.OrdinaryDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/4/20.
 */

public class ServiceListAdapter extends BaseAdapter {
    private Context context;
    private List<QueryCollectServer.DataBean.ResultBean> result;
    private CallCollect callCollect;
    private Map<String, String> map = new HashMap<>();

    public ServiceListAdapter(Context context, List<QueryCollectServer.DataBean.ResultBean> result) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_serviceprovider_car, null);
            viewHolder.service_provider_image=convertView.findViewById(R.id.service_provider_image);
            viewHolder.service_provider_tv=convertView.findViewById(R.id.service_provider_tv);
            viewHolder.service_provider_km=convertView.findViewById(R.id.service_provider_km);
            viewHolder.service_provider_dial=convertView.findViewById(R.id.service_provider_dial);
            viewHolder.service_provider_delete=convertView.findViewById(R.id.service_provider_delete);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        viewHolder.service_provider_tv.setText(result.get(position).getAuth_comp_name());
        viewHolder.service_provider_km.setText(result.get(position).getAuth_comp_addr());
        getBitmap(result.get(position).getAuth_comp_img_head_file_id(),viewHolder.service_provider_image);

        viewHolder.service_provider_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                    final OrdinaryDialog ordinaryDialog = OrdinaryDialog.newInstance(context).setMessage1("删除服务商收藏").setMessage2("删除后不可恢复，确定清除？").showDialog();
                    ordinaryDialog.setNoOnclickListener(new OrdinaryDialog.onNoOnclickListener() {
                        @Override
                        public void onNoClick() {
                            ordinaryDialog.dismiss();
                        }
                    });
                    ordinaryDialog.setYesOnclickListener(new OrdinaryDialog.onYesOnclickListener() {
                        @Override
                        public void onYesClick() {
                            if(callCollect!=null){
                                if (NetUtil.isNetAvailable(context)) {
                                    map.clear();
                                    map.put("coll_id",result.get(position).getColl_id());
                                    OKhttptils.post((Activity) context, Config.REMOVECOLL, map, new OKhttptils.HttpCallBack() {
                                        @Override
                                        public void success(String response) {
                                            callCollect.getCallcollect(view,1,position);
                                            notifyDataSetChanged();
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
        viewHolder.service_provider_dial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转电话
                Intent intent=new Intent("android.intent.action.CALL", Uri.parse("tel:"+result.get(position).getAuth_comp_phone()));
                context.startActivity(intent);
            }
        });
        return convertView;
    }
    class ViewHolder{
        ImageView service_provider_image;
        TextView service_provider_tv,service_provider_km;
        LinearLayout service_provider_dial,service_provider_delete;
    }
    public void getServerCall(CallCollect callCollect){
        this.callCollect=callCollect;
    }
    public void getBitmap(final String file_id, final ImageView circleImageView){
        //通过ID获得图片
        if(file_id!=null){
            if (NetUtil.isNetAvailable(context)) {
                map.clear();
                map.put("file_id",file_id);
                OKhttptils.post((Activity) context, Config.GET_BASE64, map, new OKhttptils.HttpCallBack() {
                        @Override
                        public void success(String response) {
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
