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
import com.xtzhangbinbin.jpq.view.ZQImageViewRoundOval;

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
        viewHolder.browsing_tv.setText(result.get(position).getAuth_comp_name());
        viewHolder.browsing_km.setText("浏览时间: "+result.get(position).getLog_date());
        viewHolder.browsing_image.setType(ZQImageViewRoundOval.TYPE_ROUND);
        viewHolder.browsing_image.setRoundRadius(5);
        OKhttptils.getPic(context,result.get(position).getAuth_comp_img_head_file_id(),viewHolder.browsing_image);
        viewHolder.browsing_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Toast.makeText(context, position+"删除", Toast.LENGTH_SHORT).show();
                final OrdinaryDialog ordinaryDialog = OrdinaryDialog.newInstance(context).setMessage1("温馨提示").setMessage2("  删除后不可恢复，确定清除？").showDialog();
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
                                        callBrowsing.getCallBrowsing(view,"",position);

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
        ZQImageViewRoundOval browsing_image;
        TextView browsing_tv,browsing_km;
        LinearLayout browsing_delete;
    }
    public interface CallBrowsing {
        void getCallBrowsing(View view,String is,int position);
    }

    public void getBrowsingCall(CallBrowsing callBrowsing){
        this.callBrowsing=callBrowsing;
    }
}

