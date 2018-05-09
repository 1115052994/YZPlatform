package com.xtzhangbinbin.jpq.fragment.main;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.activity.AccessCar;
import com.xtzhangbinbin.jpq.activity.CarCredit;
import com.xtzhangbinbin.jpq.activity.CarProduct;
import com.xtzhangbinbin.jpq.activity.CarProductSX;
import com.xtzhangbinbin.jpq.activity.CityActivity;
import com.xtzhangbinbin.jpq.activity.CompanyCenterActivity;
import com.xtzhangbinbin.jpq.activity.ETC;
import com.xtzhangbinbin.jpq.activity.LoginActivity;
import com.xtzhangbinbin.jpq.activity.MainActivity;
import com.xtzhangbinbin.jpq.activity.PersonalCenterActivity;
import com.xtzhangbinbin.jpq.activity.PoiAroundSearchActivity;
import com.xtzhangbinbin.jpq.activity.SOS;
import com.xtzhangbinbin.jpq.activity.WeizhangQuery;
import com.xtzhangbinbin.jpq.activity.ZhongLife;
import com.xtzhangbinbin.jpq.config.Config;
import com.xtzhangbinbin.jpq.utils.JumpUtil;
import com.xtzhangbinbin.jpq.utils.OKhttptils;
import com.xtzhangbinbin.jpq.utils.Prefs;
import com.xtzhangbinbin.jpq.utils.StringUtil;
import com.xtzhangbinbin.jpq.zxing.android.CaptureActivity;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.functions.Consumer;

/**
 * Created by glp on 2018/4/18.
 * 描述：主页
 */
public class MainFragment extends Fragment{
    Unbinder unbinder;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.mLocation)
    TextView mLocation;
    private Fragmentwsby wxbyListener;

    // 轮播图
    private List<String> bannersImage = new ArrayList<>();

    //定位需要的声明
    private AMapLocationClient mLocationClient = null;//定位发起端
    private AMapLocationClientOption mLocationOption = null;//定位参数
    //标识，用于判断是否只显示一次定位信息和用户重新定位
    private boolean isFirstLoc = true;
    private RxPermissions rxPermission;

    private View view;
    private double lat,lon;
    private String user_type;
    private String cityId ="";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_main1, container, false);
        }
        unbinder = ButterKnife.bind(this, view);
        getData();
        initView();
        //initLoc();
        return view;
    }

    private void initView() {
        // 城市
        String city = Prefs.with(getContext()).read("city");
        if (city!=null&&!"".equals(city)){
            mLocation.setText(city);
            // 获取城市ID
            String cityid = Prefs.with(getContext()).read("cityId");
            lat = Double.parseDouble(Prefs.with(getContext()).read("lat"));
            lon = Double.parseDouble(Prefs.with(getContext()).read("lon"));
            if (cityid!=null&&!"".equals(cityid)) {
                cityId = cityid;
            }else{
                getCityId(city);
            }
        }
        // banner
        banner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                OKhttptils.getPic(getActivity(), (String) path, imageView);
            }
        });
    }

    private void getData() {
        Map<String, String> map = new HashMap<>();
        OKhttptils.post(getActivity(), Config.SELECTADVEMAIN, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                Log.i("getData===", response);
                try {
                    JSONObject object = new JSONObject(response);
                    JSONObject data = object.getJSONObject("data");
                    JSONArray result = data.getJSONArray("result");
                    bannersImage.clear();
                    for (int i = 0; i < result.length(); i++) {
                        JSONObject object1 = result.getJSONObject(i);
                        String fileId = object1.getString("adve_file_id");
                        bannersImage.add(fileId);
                    }
                    if (banner!=null) {
                        banner.setImages(bannersImage);
                        banner.start();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void fail(String response) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.image_scan, R.id.image_man, R.id.ly_etc, R.id.ly_wby, R.id.ly_jyz, R.id.ly_jjjy, R.id.ly_wzcx, R.id.ly_yxc, R.id.ly_xydk, R.id.ly_cyp, R.id.ly_mc, R.id.ly_gj, R.id.image_czcl, R.id.yzfw, R.id.image_xyd, R.id.image_cyp, R.id.image_csh, R.id.image_wxby, R.id.image_xc, R.id.image_qcmr, R.id.image_pprm, R.id.image_rmsx, R.id.image_zxc, R.id.image_cmzy, R.id.iamge_etzy, R.id.iamge_qczd, R.id.iamge_xcjly, R.id.iamge_dcld,R.id.mLocation,R.id.image_ms,R.id.image_xxyl,R.id.image_shfw})
    public void onViewClicked(View view) {
        MainActivity mainActivity = (MainActivity) getActivity();
        switch (view.getId()) {
            case R.id.image_scan:
                Log.w("test", "扫描：" + Prefs.with(getContext()).read("user_token"));
                if(!StringUtil.isEmpty(Prefs.with(getContext()).read("user_token"))){
                    JumpUtil.newInstance().jumpRight(getContext(), CaptureActivity.class);
                } else {
                    JumpUtil.newInstance().jumpRight(getContext(), LoginActivity.class);
                }
                break;
            case R.id.image_man:
                user_type = Prefs.with(getContext()).read("user_type");
                if (user_type.isEmpty()){
                    JumpUtil.newInstance().jumpRight(getContext(), LoginActivity.class);
                }else if (user_type.equals("comp")){
                    JumpUtil.newInstance().jumpRight(getContext(), CompanyCenterActivity.class);
                }else if (user_type.equals("pers")){
                    /* 跳转到个人中心 */
                    JumpUtil.newInstance().jumpRight(getContext(), PersonalCenterActivity.class);
                }
                break;
            case R.id.ly_etc:
                JumpUtil.newInstance().jumpRight(getContext(), ETC.class);
                break;
            case R.id.ly_wby:
                String dict_id = "YZcompcfwlxwxbywxby";
                wxbyListener.pross(dict_id);
                break;
            case R.id.ly_jyz:
                // 加油站
                Bundle bundle = new Bundle();
                bundle.putDouble("lat",lat);
                bundle.putDouble("lon",lon);
                JumpUtil.newInstance().jumpLeft(getActivity(), PoiAroundSearchActivity.class,bundle);
                break;
            case R.id.ly_jjjy:
                JumpUtil.newInstance().jumpRight(getActivity(), SOS.class);
                break;
            case R.id.ly_wzcx:
                if(null != Prefs.with(getContext()).read("user_token")&&!"".equals(Prefs.with(getContext()).read("user_token"))){
                    // 违章查询
                    JumpUtil.newInstance().jumpLeft(getActivity(), WeizhangQuery.class);
                } else {
                    JumpUtil.newInstance().jumpRight(getContext(), LoginActivity.class);
                }
                break;
            case R.id.ly_yxc:
                // 优选车
                mainActivity.switchToCarBuy("pprm");
                break;
            case R.id.ly_xydk:
                JumpUtil.newInstance().jumpRight(getContext(), CarCredit.class);
                break;
            case R.id.ly_cyp:
                // 车用品
                JumpUtil.newInstance().jumpLeft(getActivity(), CarProduct.class);
                break;
            case R.id.ly_mc:
                //卖车
                mainActivity.switchToCarSell("2");
                break;
            case R.id.ly_gj:
                if(null != Prefs.with(getContext()).read("user_token")&&!"".equals(Prefs.with(getContext()).read("user_token"))){
                    // 估价
                    JumpUtil.newInstance().jumpRight(getContext(), AccessCar.class);
                } else {
                    JumpUtil.newInstance().jumpRight(getContext(), LoginActivity.class);
                }
                break;
            /*case R.id.image_czcl:
                break;
            case R.id.yzfw:
                break;*/
            case R.id.image_xyd:
                JumpUtil.newInstance().jumpRight(getContext(), CarCredit.class);
                break;
            case R.id.image_cyp:
                // 车用品
                JumpUtil.newInstance().jumpLeft(getActivity(), CarProduct.class);
                break;
            case R.id.image_csh:
                // 车生活
                mainActivity.switchToLife();
                break;
            case R.id.image_wxby:
                break;
            case R.id.image_xc:
                break;
            case R.id.image_qcmr:
                break;
            case R.id.image_pprm:
                // 品牌热卖
                mainActivity.switchToCarBuy("pprm");
                break;
            case R.id.image_rmsx:
                mainActivity.switchToCarBuy("rmsx");
                // 入门首选
                break;
            case R.id.image_zxc:
                mainActivity.switchToCarBuy("zxc");
                // 准新车
                break;
            case R.id.image_cmzy:
                // 车meizhiyou
                mainActivity.switchToCarBuy("cmzy");
                break;
            case R.id.image_ms:
                // meishi
                Bundle ms = new Bundle();
                ms.putString("cityId",cityId);
                ms.putString("String","YZzshms");
                JumpUtil.newInstance().jumpLeft(getActivity(), ZhongLife.class,ms );
                break;
            case R.id.image_xxyl:
                // 休闲
                Bundle xxyl = new Bundle();
                xxyl.putString("cityId",cityId);
                xxyl.putString("String","YZzshxxyl");
                JumpUtil.newInstance().jumpLeft(getActivity(), ZhongLife.class, xxyl);
                break;
            case R.id.image_shfw:
                // 生活服务
                Bundle shfw = new Bundle();
                shfw.putString("cityId",cityId);
                shfw.putString("String","YZzshshfu");
                JumpUtil.newInstance().jumpLeft(getActivity(), ZhongLife.class, shfw);
                break;
            case R.id.iamge_etzy:
                // 车用品 儿童座椅
                JumpUtil.newInstance().jumpLeft(getActivity(),CarProductSX.class,"YZcarYPcyplxetzy");
                break;
            case R.id.iamge_qczd:
                // 汽车坐垫
                JumpUtil.newInstance().jumpLeft(getActivity(),CarProductSX.class,"YZcarYPcyplxqczd");
                break;
            case R.id.iamge_xcjly:
                // 行车记录仪
                JumpUtil.newInstance().jumpLeft(getActivity(),CarProductSX.class,"YZcarYPcyplxxcjly");
                break;
            case R.id.iamge_dcld:
                // 倒车雷达
                JumpUtil.newInstance().jumpLeft(getActivity(),CarProductSX.class,"YZcarYPcyplxdcld");
                break;
            case R.id.mLocation:
                // 城市
                JumpUtil.newInstance().jumpRight(getContext(), CityActivity.class);
                CityActivity.setOnBackListener(new CityActivity.BackListener() {
                    @Override
                    public void backData(String city) {
                        mLocation.setText(city);
                        // 获取城市ID
                        getCityId(city);
                    }
                });
                break;
        }
    }


    /* 维修保养回调函数 */
    public interface  Fragmentwsby{
        void pross(String s);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof  Fragmentwsby){
            wxbyListener = (Fragmentwsby) activity;
        }else {
            try {
                throw new IllegalAccessException("activity must implements Fragmengtwxby");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }


    //通过城市名获取城市Id
    public void getCityId(String cityName) {
        Map<String, String> map = new HashMap<>();
        map.put("cityName", cityName);
        OKhttptils.post(getActivity(), Config.GET_CITY_ID, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    JSONObject data = object.getJSONObject("data");
                    JSONObject result = data.getJSONObject("result");
                    cityId = result.getString("city_id");
//                    /**
//                     * 存储城市Id
//                     */
//                    Prefs.with(getActivity()).remove("cityId");
//                    Prefs.with(getActivity()).write("cityId",cityId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void fail(String response) {

            }
        });
    }
}
