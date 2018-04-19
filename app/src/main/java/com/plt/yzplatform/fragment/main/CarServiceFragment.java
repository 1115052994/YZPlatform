package com.plt.yzplatform.fragment.main;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.plt.yzplatform.R;
import com.plt.yzplatform.adapter.MainGridViewAdapter;
import com.plt.yzplatform.adapter.MainViewPagerAdapter;
import com.plt.yzplatform.config.Config;
import com.plt.yzplatform.entity.CarServiceImage;
import com.plt.yzplatform.entity.Model;
import com.plt.yzplatform.gson.factory.GsonFactory;
import com.plt.yzplatform.utils.NetUtil;
import com.plt.yzplatform.utils.PhotoUtils;
import com.plt.yzplatform.utils.Prefs;
import com.plt.yzplatform.utils.ToastUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.squareup.picasso.Picasso;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;

/**
 * Created by glp on 2018/4/18.
 * 描述：车服务
 */

public class CarServiceFragment extends Fragment {
    private static final String TAG = "车服";
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.viewpager)
    ViewPager mPager;
    @BindView(R.id.ll_dot)
    LinearLayout mLlDot;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    Unbinder unbinder;
    private View view;
    private List<String> images = new ArrayList<>();

    private List<View> mPagerList;
    private List<Model> mDatas;
    private LayoutInflater inflater;
    //总的页数
    private int pageCount;
    //每一页显示的个数
    private int pageSize = 10;
    // 当前显示的是第几页
    private int curIndex = 0;
    private List<CarServiceImage.DataBean.ResultBean.BannerListBean> bannerList = new ArrayList<>();

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 001:
                    bannerList = (List<CarServiceImage.DataBean.ResultBean.BannerListBean>) msg.obj;
                    initBanner();
                    break;
                case 002:
                    break;
            }
        }
    };

    /* 填充banner */
    private void initBanner() {
        /**
         *  "bannerList": [
         {
         "adve_location": "03",
         "adve_turn_type": "外部url",
         "adve_name": "12341",
         "adve_state": "1",
         "adve_turn_url": "wqwq",
         "adve_oper_user_id": "6",
         "adve_id": "16",
         "adve_file_id": "1c817c1b748f4dfa82a23c17b90641ca",
         "adve_turn_android_url": "wqwq",
         "adve_order": 4,
         "adve_turn_ios_url": "wqwq",
         "adve_oper_date": "2018-04-17 10:45:12"
         }
         ],
         * */
        for (int i = 0; i < bannerList.size() ; i++) {
            CarServiceImage.DataBean.ResultBean.BannerListBean listBean = bannerList.get(i);
            String file_id = listBean.getAdve_file_id();
//            getPic(getContext(),file_id,);
        }
        //banner
        banner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                //此处可以自行选择，我直接用的Picasso
                Log.i("banner", "path=" + (String) path);
                Picasso.with(getContext()).load((String) path).into(imageView);
            }
        });
        //广告图片从服务器中获取 如果当地没有广告 则显示默认的广告图
        images.add(getUriFromDrawableRes(getContext(), R.drawable.text).toString());
        images.add(getUriFromDrawableRes(getContext(), R.drawable.text).toString());
        images.add(getUriFromDrawableRes(getContext(), R.drawable.text).toString());
        images.add(getUriFromDrawableRes(getContext(), R.drawable.text).toString());
        banner.setImages(images);
        banner.start();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_car_service, null);
        }
        unbinder = ButterKnife.bind(this, view);
        /* 初始化数据源 */
        initData();
        initView();
        return view;
    }

    /* 初始化数据源 */
    private void initData() {
        if (NetUtil.isNetAvailable(getContext())) {
            OkHttpUtils.post()
                    .url(Config.GET_SERVICE_IMG)
                    .addHeader("user_token", Prefs.with(getContext()).read("user_token"))
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            ToastUtil.noNAR(getContext());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            Log.d(TAG, "onResponse: " + response);
                            /**
                             * {
                             "data": {
                             "result": {
                             "bannerList": [
                             {
                             "adve_location": "03",
                             "adve_turn_type": "外部url",
                             "adve_name": "12341",
                             "adve_state": "1",
                             "adve_turn_url": "wqwq",
                             "adve_oper_user_id": "6",
                             "adve_id": "16",
                             "adve_file_id": "1c817c1b748f4dfa82a23c17b90641ca",
                             "adve_turn_android_url": "wqwq",
                             "adve_order": 4,
                             "adve_turn_ios_url": "wqwq",
                             "adve_oper_date": "2018-04-17 10:45:12"
                             }
                             ],
                             "itemList": [
                             {
                             "serverLinkType": null,
                             "serverDesc": "洗车",
                             "serverImgPath": null,
                             "serverUrl": null,
                             "serverId": "YZcompcfwlxxcxc"
                             },
                             {
                             "serverLinkType": "inner",
                             "serverDesc": "维修保养",
                             "serverImgPath": "/res/service_icon/1.jpg",
                             "serverUrl": "/service_url/123",
                             "serverId": "YZcompcfwlxwxbywxby"
                             }
                             ]
                             }
                             },
                             "message": "",
                             "status": "1"
                             }
                             * */
                            Gson gson = GsonFactory.create();
                            CarServiceImage serviceImage = gson.fromJson(response, CarServiceImage.class);
                            if ("1".equals(serviceImage.getStatus())) {
                                List<CarServiceImage.DataBean.ResultBean.BannerListBean> bannerList = serviceImage.getData().getResult().getBannerList();
                                Message message = new Message();
                                message.what = 001;
                                message.obj = bannerList;
                                handler.sendMessage(message);
                            } else {
                                ToastUtil.show(getContext(), serviceImage.getMessage());
                            }
                        }
                    });
        } else {
            ToastUtil.noNetAvailable(getContext());
        }

        /* 首页分类 */
        mDatas = new ArrayList<>();
        //小于标题的长度
        for (int i = 0; i < 12; i++) {
            Model model = new Model("汽车美容" + i, PhotoUtils.getBitmapFromRecrous(getContext(), R.drawable.logo));
            mDatas.add(model);
        }
        /* 列表 */
        for (int i = 0; i < 40; i++) {
//            test.add("i=" + i);
        }
//        CommonRecyclerAdapter adapter = new CommonRecyclerAdapter(this, test, R.layout.item_main_service_list) {
//            @Override
//            public void convert(ViewHolder holder, Object item, int position) {
//                TextView tv = holder.getView(R.id.city);
//                tv.setText((String) item);
//            }
//        };
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerView.setAdapter(adapter);

    }

    /* 初始化界面 */
    private void initView() {

        //viewpager + gridview
        inflater = LayoutInflater.from(getContext());
        //总的页数=总数/每页数量，并取整
        pageCount = (int) Math.ceil(mDatas.size() * 1.0 / pageSize);
        mPagerList = new ArrayList<View>();
        for (int i = 0; i < pageCount; i++) {
            // 每个页面都是inflate出一个新实例
            GridView gridView = (GridView) inflater.inflate(R.layout.gridview, mPager, false);
            gridView.setAdapter(new MainGridViewAdapter(getContext(), mDatas, i, pageSize));
            mPagerList.add(gridView);

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    int pos = position + curIndex * pageSize;
                    Toast.makeText(getContext(), mDatas.get(pos).getName(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        //设置适配器
        mPager.setAdapter(new MainViewPagerAdapter(mPagerList));
        //设置圆点
        setOvalLayout();
    }

    /* 设置圆点 */
    public void setOvalLayout() {
        for (int i = 0; i < pageCount; i++) {
            mLlDot.addView(inflater.inflate(R.layout.dot, null));
        }
        // 默认显示第一页
        mLlDot.getChildAt(0).findViewById(R.id.v_dot)
                .setBackgroundResource(R.drawable.dot_selected);
        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageSelected(int position) {
                // 取消圆点选中
                mLlDot.getChildAt(curIndex)
                        .findViewById(R.id.v_dot)
                        .setBackgroundResource(R.drawable.dot_normal);
                // 圆点选中
                mLlDot.getChildAt(position)
                        .findViewById(R.id.v_dot)
                        .setBackgroundResource(R.drawable.dot_selected);
                curIndex = position;
            }

            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    /* 获取资源中的url */
    public Uri getUriFromDrawableRes(Context context, int id) {
        Resources resources = context.getResources();
        String path = ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                + resources.getResourcePackageName(id) + "/"
                + resources.getResourceTypeName(id) + "/"
                + resources.getResourceEntryName(id);
        return Uri.parse(path);
    }

    /**
     * 通过头像id获取头像
     **/
    String file_content;
    public Bitmap getPic(final Context context, String file_id) {
        if (NetUtil.isNetAvailable(context)) {
            OkHttpUtils.post()
                    .url(Config.GET_BASE64)
                    .addHeader("user_token",Prefs.with(context).read("user_token"))
                    .addParams("file_id", file_id)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            ToastUtil.noNAR(context);
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            Log.w(TAG, "onResponse获取base64: " + response );
                            /**
                             * {"data":{"file_content":"/9j/4AAQSkZJRgABAQAAAQABAAD/2wBDAAEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEB\nAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQH/2wBDAQEBAQEBAQEBAQEBAQEBAQEBAQEB\nAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQH/wAARCAHgAeADASIA\nAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQA\nAAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3\nODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWm\np6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4 Tl5ufo6erx8vP09fb3 Pn6/8QAHwEA\nAwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSEx\nBhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElK\nU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3\nuLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3 Pn6/9oADAMBAAIRAxEAPwD7q0jT\n927cd2MduvX368ds445557SwtPJDd3OPbozfXrgcfXnkZzdOj2buc569unX1/D8j612GnW3mbu2M\nepz97H8Xofr053A5/CMPa6vt73f 9b9T9M5ve309P6ZYs7Rxv29SV/RjzknAHynOfzz13bS035Qo\nSgx6c8uMcHJz8uM /JGTV62tThsdeP5tk9fYd/XnkA7ENvhZT9AevqRj16Dnj3ycHOVObhfVrs10\n1d u1ld qWrTOdVlK7XNdel//Snv6/Mpxacu0ny 47n/AD/nqacbBVST936eueGfHfr8o9eD7HO6\nkLsD5fGOSMcnOQDyT/dP/wBfqZjaOc89/UZ/i9/f XPBzF33f3v/ADNDk3t32n/9R4Lj1PbnPp6n\nOKD2zjzuf7vI r/989c89eehFdsLP/Wb139M/wDj CeTjPOP1Bzxjz2j/vuvb cg9O/b3/vdaRlh\nvhf NHMTWu4Bm /Hjg47MR1ye2D19ec1g3gESnzH6f8ALPg9z9f69eTkA11Nxbsglijh8 RseTHH\nkdS/OSfRe5/iPPBzzd1FDCHnup/OuRnEWP8AU/MwPOfx/LkkgjxszzHCZfRdSrbSyu07W99K2uvd\nJaNNN3Wr  4K4PzTi3Fv2P8AsOEV7ZpzOzac m6um99dtnKV K1CzuLrzQ8 y248iKMCCebBfIye\nR/Ceee2ehrEittOtFJx5cn/LKOQ5xyw7/XP9CCK273V45YZYU5f/AFQPPqw7nvjPqMEA8k1hWejX\nN3MqP5hmlH7tI8joWxxk56n36fMSRX55js7q41SpJ/7HbXe1 aWlm762W9t1rqf19wtkuS8J4D2e\nFw/1KN0nv3aaevS71W musmRW0807yQlJAVlHleXk4yW755yMe4BUZYhq qfhJ zX4m IR/tW88z\nw5oTgeTeXlp5Nxeg5z9ktMnHIzjvg9xlva/2ff2ftDsol8TeM7SO81JIraW00y4iBt7P5mz83TkY\nYg56hcggtX2fcX0VrapbWSeRBbYii8uIweXy4PU5GCvTBJOOcA5eDwlJ3q4nbpK1la8tdWmrPbXR\ntJN 9f8AP M/EussX9QyC31uy/4VUlveVrXa0slra22rk234dpv7J3wV0O3H9radJr90Ij5suoL5\n46vntj259Tg5DZW  D3wOWzfTU8B HGtz/BPoelT5wzjOL044Cgc9 55J9BvNZOx1keSSPjB6Z5Y\nZ4OQCSOpOMDrljXC6hqURnNwXHlyDPGeeSBzkHgq3/fRySRz1TzOlhl7PCYdYPol3fvK2rta7TUV\n2W97r86wOP4pqVHVxed5pv8A9Da93edn5tNbarXe92/nnx3 zF zdrSr5vwm8HNIn72K4h8P6bEO\nrji5ayYA8HBHfHJYsT8c/Fz4KeHvDdpdr4Q1P xrb7JPF/Z32Qz2/VsY xY789eM9SAGP3b4k8U2\ndt5 bo db4zHg/Ny46A8YHX3B5JJJ QvGuptqE2rzzzRiKb7N9k6/wAJcccnHXGCcZ9SRWFLG5hj\naj9q3jnaNndX0c XdvWydtXu7Wcfe/f/AA8z7iiFWhh8bneZYvB4F3tmeaLMo6vT3VLT4W/5rdWm\nfhx4g HXhz4UeJL9/GVtGn9oXVzdQ IfJ 3aRD9rvG/4  cWOccH6ZyVLV7N4W8M6QwhurVbe9t7\n3bLa3NuLQQT22ZB9qsxn0/8ArEnmvoT4oeAtP8UaPqMNzFG4uMAP5P7gEnUf9L6n 6e QCec4rT/\nAOCd3wD0D4vfCfxJLrutR6XpXgj4teNfBWm3luBPqlxp9s m6tZjOc/KNTBOSAAfvMQ2f3KWd1sR\nwpLNauJX1rArKcrzWSae8pxvvrq47q90173xH99Yvxyy3CcD/wCsnEuafUv7HeU5TmaXM7vM1OeV\nRSu3tCbSvZ3Sv0l4Vf8AhXRfMlhmsLdJvN/1scXH3nHTnsPqeM4IyWr4Y0u1ibdDbqkmBC78d39T\n34PXIORyd1fsBH wd8GJ5o3m8c KpSkolkijS2M82C/XFnz2zzxkgEndn6M H37Lf7Ong2eK7Twp\nb K9UixJFqXilW1E2WC PsdoH02yU4IyVYMDgk4YV8fU4kT1hi35K0rv4uiVvXX W7a0PxrN/pk \nHmV5fKrldPinOsXZuOWPK45XHm5pWTeZzlNc7mm1ytpWbvJH872ifCzxr4u12DS/h74I8Q ODPL5\nUsXh/wAO3OpCIjcT/ptpYgcBNx5yAB1ALH6yl/4JzftQRaPDr9v8DtcuTcRCWWxgvvDk qwncwx/\nZP2/rkcjPJPXK1/Rroeq2el2KWWiWen6Lp8ZMMdlo/2WzsPs 49bSzbP057qOCpz3eleIZVkYLIQ\n5hXnzcjgnOfmyOoz HGRzzR4hqYhOisS8HK6s aPw3lZtWbUtddWno7NpH4FxN9P/wAU6lWj/qrw\nVwnkuU4NRUVmuZZxmma5nK80/eyyrlClHSyioxaX2mkk/wCNbx18GvGXgm vrHxn4B8W DWtwDKf\nEPhzVdNtzcEnBF6LH zz8oznnAKjOCGbx 48PPbW11JBP9qS3x 7j6cFh6d8Z/mTgE/3mWWqaTrN\nlJpOu2Vpqlpc4iu7O/jtb2xkwZACLW9Ofm4yPoDuBYn5k KP/BPT9jn4ywXUmqfDWw8L63cxjyvE\nXgh5/DuoMMEHFray/YbvhT1045YlsFi273sDjcZUT9hiXjE2rKzaSvUT1tslezS1cmul39twV 0k\nwmFq4bCeIfh3mOUbKea8KZr/AGhdqTi TKM2nlmltW3nGYOKS92Unr/HTpmjaPc2KyXKW6TSRDPl\n5xDjeB39wfyJJIrgLnR7a3M/mW1uqiW4Pl/8Cb/S uecfz5O3Nfuv8fv CJHxf0d7nW/2dvHum O\nbF2nKeGvE0tj4a8RQQjcGFpqbSNo15kksF1AWIUK26QkR1 THin9m39oL4b63ceHfiT8MvFnhzUI\nZvIQ6jp1w1vIfmP2q01cN9gvbHBDY0/nay54KlvUw0qWBeLrYvM/qdrJvNOXLPtSs290/JtNLlSj\ndM/unwt kB4KeJ9LFY/g3xNyrF4yXKlwrmv\
                             */
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getString("status").equals("1")) {
                                    String data = jsonObject.getString("data");
                                    JSONObject object = new JSONObject(data);
                                    file_content = object.getString("file_content");
                                } else {
                                    ToastUtil.noNAR(context);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } else {
            ToastUtil.noNetAvailable(context);
        }
        return  PhotoUtils.base64ToBitmap(file_content);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
