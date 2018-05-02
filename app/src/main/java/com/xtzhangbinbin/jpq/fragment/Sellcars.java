package com.xtzhangbinbin.jpq.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.config.Config;
import com.xtzhangbinbin.jpq.utils.NetUtil;
import com.xtzhangbinbin.jpq.utils.OKhttptils;
import com.xtzhangbinbin.jpq.view.OrdinaryDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class Sellcars extends Fragment {

    @BindView(R.id.Sellcars_tv)
    TextView SellcarsTv;
    @BindView(R.id.Sellcars_ed)
    EditText SellcarsEd;
    @BindView(R.id.Sellcars_button)
    Button SellcarsButton;
    @BindView(R.id.radio01)
    RadioButton radio01;
    @BindView(R.id.radio02)
    RadioButton radio02;
    Unbinder unbinder;
    private HashMap<String, String> map = new HashMap<>();

    public Sellcars() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_sellcars, container, false);
        unbinder = ButterKnife.bind(getActivity());
        PostSellcars();
        SellcarsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String trim = SellcarsEd.getText().toString().trim();
                if (trim.length() == 11) {
                    PostSellcarsPhone(trim);
                } else if (trim.length() == 0) {
                    Toast.makeText(getContext(), "请输入号码", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "号码格式不对", Toast.LENGTH_SHORT).show();
                }
            }
        });
        radio01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转电话
                Intent intent=new Intent("android.intent.action.CALL", Uri.parse("tel:"+"4001198698"));
                getActivity().startActivity(intent);
            }
        });

        return inflate;
    }

    public void PostSellcars() {
        if (NetUtil.isNetAvailable(getContext())) {
            OKhttptils.post(getActivity(), Config.SELLCOUNT, null, new OKhttptils.HttpCallBack() {
                @Override
                public void success(String response) {
                    Log.i("aaaaa", "查询卖车申请次数: " + response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONObject data = jsonObject.getJSONObject("data");
                        SellcarsTv.setText(String.valueOf(data.getInt("result")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void fail(String response) {
                    Toast.makeText(getContext(), "查询失败", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void PostSellcarsPhone(String phone) {
        map.clear();
        if (NetUtil.isNetAvailable(getContext())) {
            map.put("sell_phone", phone);
            OKhttptils.post(getActivity(), Config.SAVESELLER, map, new OKhttptils.HttpCallBack() {
                @Override
                public void success(String response) {
                    try {
                        Log.i("aaaaa", "查询二手车收藏: " + response);
                        JSONObject jsonObject = new JSONObject(response);
                        JSONObject data = jsonObject.getJSONObject("data");
                        int state = data.getInt("state");
                        Log.d("aaaaa", "是否预约成功: "+state+"--------");
                        if(state==1){
                            PostIntent("恭喜您预约成功！",null);
                        }else if(state==0){
                            PostIntent("您已经预约了！",null);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void fail(String response) {
                    Toast.makeText(getContext(), "预约失败", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    public void PostIntent(String mesage, final Class clas){
        PostSellcars();
        final OrdinaryDialog ordinaryDialog = OrdinaryDialog.newInstance(getContext()).setMessage1("温馨提示").setMessage2("   "+mesage).setConfirm("知道了").setCancel("返回首页").showDialog();
        ordinaryDialog.setNoOnclickListener(new OrdinaryDialog.onNoOnclickListener() {
            @Override
            public void onNoClick() {
                ordinaryDialog.dismiss();
//                startActivity(new Intent(Sellcars.this,clas));
                Toast.makeText(getContext(), "我跳到主页了", Toast.LENGTH_SHORT).show();
            }
        });
        ordinaryDialog.setYesOnclickListener(new OrdinaryDialog.onYesOnclickListener() {
            @Override
            public void onYesClick() {
                ordinaryDialog.dismiss();
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
