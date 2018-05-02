package com.xtzhangbinbin.jpq.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
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
import com.xtzhangbinbin.jpq.entity.QueryCarRecord;
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
 * Created by Administrator on 2018/4/21.
 */

public class BrowsingAdapter extends BaseAdapter {
    private Context context;
    private List<QueryCarRecord.DataBean.ResultBean> result;
    private CallBrowsing callBrowsing;
    private Map<String, String> map = new HashMap<>();

    public BrowsingAdapter(Context context, List<QueryCarRecord.DataBean.ResultBean> result) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_browsing, null);
            viewHolder.browsing_image=convertView.findViewById(R.id.browsing_image);
            viewHolder.browsing_tv=convertView.findViewById(R.id.browsing_tv);
            viewHolder.browsing_km=convertView.findViewById(R.id.browsing_km);
            viewHolder.browsing_delete=convertView.findViewById(R.id.browsing_delete);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (BrowsingAdapter.ViewHolder) convertView.getTag();
        }
        viewHolder.browsing_tv.setText(result.get(position).getCar_name());
        viewHolder.browsing_km.setText("浏览时间: "+result.get(position).getLog_date());
        //接口没有返回图片id
        getBitmap(result.get(position).getCar_1_file_id(),viewHolder.browsing_image);
        viewHolder.browsing_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Toast.makeText(context, position+"删除", Toast.LENGTH_SHORT).show();
                final OrdinaryDialog ordinaryDialog = OrdinaryDialog.newInstance(context).setMessage1("删除记录").setMessage2("删除后不可恢复，确定清除？").showDialog();
                ordinaryDialog.setNoOnclickListener(new OrdinaryDialog.onNoOnclickListener() {
                    @Override
                    public void onNoClick() {
                        ordinaryDialog.dismiss();
                    }
                });
                ordinaryDialog.setYesOnclickListener(new OrdinaryDialog.onYesOnclickListener() {
                    @Override
                    public void onYesClick() {
                        if(callBrowsing!=null){
                            if (NetUtil.isNetAvailable(context)) {
                                map.clear();
                                map.put("log_id",result.get(position).getLog_id());  //别忘记改
                                OKhttptils.post((Activity) context, Config.BROWSEDLOG, map, new OKhttptils.HttpCallBack() {
                                    @Override
                                    public void success(String response) {
                                        Log.i("aaaa", "删除: " +result.get(position).getLog_id()+response);
                                        callBrowsing.getCallBrowsing(view,"");
                                    }
                                    @Override
                                    public void fail(String response) {
                                        Log.d("aaaa", "删除失败"+result.get(position).getLog_id());
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
        ImageView browsing_image;
        TextView browsing_tv,browsing_km;
        LinearLayout browsing_delete;
    }
    public void getBrowsingCall(CallBrowsing callBrowsing){
        this.callBrowsing=callBrowsing;
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

