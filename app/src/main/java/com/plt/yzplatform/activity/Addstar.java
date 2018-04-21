package com.plt.yzplatform.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.plt.yzplatform.R;
import com.plt.yzplatform.base.BaseActivity;
import com.plt.yzplatform.config.Config;
import com.plt.yzplatform.utils.JumpUtil;
import com.plt.yzplatform.utils.NetUtil;
import com.plt.yzplatform.utils.PhotoUtils;
import com.plt.yzplatform.utils.Prefs;
import com.plt.yzplatform.utils.ToastUtil;
import com.plt.yzplatform.view.CircleImageView;
import com.plt.yzplatform.view.OrdinaryDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class Addstar extends BaseActivity {
    @BindView(R.id.addition_image)
    CircleImageView additionImage;
    @BindView(R.id.addition_ed_name)
    EditText additionEdName;
    @BindView(R.id.addition_ed_introduce)
    EditText additionEdIntroduce;
    @BindView(R.id.addition_layout)
    LinearLayout additionLayout;
    private String file_id;
    private String file_id2;
    private String id;
    private String text = "示例 精通汽车工作原理以及各种故障排查和解决，有较强的... （最多输入150个字）";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_star);
        ButterKnife.bind(this);
        id = getIntent().getStringExtra("id");
        additionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (additionEdName.getText().toString().length() != 0 && additionEdIntroduce.getText().toString().length() != 0) {
                    if(id==null){
                        PostData();
                    }
                    if(id!=null){
                        final OrdinaryDialog ordinaryDialog = OrdinaryDialog.newInstance(Addstar.this).setCancel("暂不更新").setMessage1("修改信息").setMessage2("信息将会提交到服务器，确定提交？").showDialog();
                        ordinaryDialog.setNoOnclickListener(new OrdinaryDialog.onNoOnclickListener() {
                            @Override
                            public void onNoClick() {
                                ordinaryDialog.dismiss();
                            }
                        });
                        ordinaryDialog.setYesOnclickListener(new OrdinaryDialog.onYesOnclickListener() {
                            @Override
                            public void onYesClick() {
                                if (NetUtil.isNetAvailable(Addstar.this)) {
                                    OkHttpUtils.post()
                                            .url(Config.UPDATASTAR)
                                            .addHeader("user_token", Prefs.with(Addstar.this).read("user_token"))
                                            .addParams("staff_name",additionEdName.getText().toString())
                                            .addParams("staff_photo_file_id",file_id)
                                            .addParams("staff_reco","2")
                                            .addParams("staff_info",additionEdIntroduce.getText().toString())
                                            .addParams("staff_id",id)
                                            .build()
                                            .execute(new StringCallback() {
                                                @Override
                                                public void onError(Call call, Exception e, int id) {
                                                    ToastUtil.noNAR(Addstar.this);
                                                }

                                                @Override
                                                public void onResponse(String response, int id) {
                                                    try {
                                                        JSONObject obj = new JSONObject(response);
                                                        String status = obj.get("status").toString();
                                                        if(status.equals("1")){
                                                            Intent intent = new Intent();
                                                            setResult(4, intent);
                                                            finish();
                                                        }
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }

                                                }
                                            });
                                }
                                ordinaryDialog.dismiss();
                            }
                        });

                    }
                } else {
                    Toast.makeText(Addstar.this, "不能为空" + additionEdName.getText().toString() + additionEdIntroduce.getText().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        if(id!=null){
            file_id = getIntent().getStringExtra("file_id");
            getBitmap(file_id,additionImage);
            additionEdName.setText(getIntent().getStringExtra("name"));
            additionEdIntroduce.setText(getIntent().getStringExtra("info"));
        }else {
            //给字体设置大小
            int end = text.length();
            SpannableString textSpan = new SpannableString(text);
            textSpan.setSpan(new AbsoluteSizeSpan(34), 0, 2, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            textSpan.setSpan(new AbsoluteSizeSpan(32), 2, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            additionEdIntroduce.setHint(textSpan);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(id==null){
            //给头部修改文字
            setTitle("添加明星员工");
        }else {
            setTitle("编辑明星员工");
        }
        setLeftImageClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JumpUtil.newInstance().finishRightTrans(Addstar.this);
            }
        });
    }
    //将相机图片上传到服务器 并且生成图片id
    @OnClick(R.id.addition_image)
    public void onViewClicked() {
        upDataPicture(this,additionImage,null,"员工头像");
        file_id2 = Prefs.with(getApplicationContext()).read("员工头像");
        Log.d("aaaaa", "图片1:"+file_id2);
    }
    //将数据上传到服务器
    private void PostData() {
        if (NetUtil.isNetAvailable(this)) {
            if(file_id2!=null){
                OkHttpUtils.post()
                        .url(Config.SAVESTAR)
                        .addHeader("user_token", Prefs.with(getApplicationContext()).read("user_token"))
                        .addParams("staff_name",additionEdName.getText().toString())
                        .addParams("staff_photo_file_id",file_id2)
                        .addParams("staff_reco","2")
                        .addParams("staff_info",additionEdIntroduce.getText().toString())
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                ToastUtil.noNAR(Addstar.this);
                            }
                            @Override
                            public void onResponse(String response, int id) {
                                try {
                                    Log.d("aaaaa", "图片2: "+file_id2);
                                    JSONObject obj = new JSONObject(response);
                                    String status = obj.get("status").toString();
                                    if(status.equals("1")){
                                        Intent intent = new Intent();
                                        setResult(1, intent);
                                        finish();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
            }else {
                Toast.makeText(this, "请选择头像", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void getBitmap(final String file_id, final CircleImageView circleImageView){
        //通过ID获得图片
        if(file_id!=null){
            if (NetUtil.isNetAvailable(Addstar.this)) {
                OkHttpUtils.post()
                        .url(Config.GET_BASE64)
                        .addHeader("user_token", Prefs.with(Addstar.this).read("user_token"))
                        .addParams("file_id", file_id)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                ToastUtil.noNAR(Addstar.this);
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                Log.w("aaa", "onResponse获取base64: " + response );
                                /**
                                 * {"data":{"file_content":"/9j/4AAQSkZJRgABAQAAAQABAAD/2wBDAAEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEB\nAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQH/2wBDAQEBAQEBAQEBAQEBAQEBAQEBAQEB\nAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQH/wAARCAHgAeADASIA\nAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQA\nAAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3\nODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWm\np6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4 Tl5ufo6erx8vP09fb3 Pn6/8QAHwEA\nAwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSEx\nBhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElK\nU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3\nuLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3 Pn6/9oADAMBAAIRAxEAPwD7q0jT\n927cd2MduvX368ds445557SwtPJDd3OPbozfXrgcfXnkZzdOj2buc569unX1/D8j612GnW3mbu2M\nepz97H8Xofr053A5/CMPa6vt73f 9b9T9M5ve309P6ZYs7Rxv29SV/RjzknAHynOfzz13bS035Qo\nSgx6c8uMcHJz8uM /JGTV62tThsdeP5tk9fYd/XnkA7ENvhZT9AevqRj16Dnj3ycHOVObhfVrs10\n1d u1ld qWrTOdVlK7XNdel//Snv6/Mpxacu0ny 47n/AD/nqacbBVST936eueGfHfr8o9eD7HO6\nkLsD5fGOSMcnOQDyT/dP/wBfqZjaOc89/UZ/i9/f XPBzF33f3v/ADNDk3t32n/9R4Lj1PbnPp6n\nOKD2zjzuf7vI r/989c89eehFdsLP/Wb139M/wDj CeTjPOP1Bzxjz2j/vuvb cg9O/b3/vdaRlh\nvhf NHMTWu4Bm /Hjg47MR1ye2D19ec1g3gESnzH6f8ALPg9z9f69eTkA11Nxbsglijh8 RseTHH\nkdS/OSfRe5/iPPBzzd1FDCHnup/OuRnEWP8AU/MwPOfx/LkkgjxszzHCZfRdSrbSyu07W99K2uvd\nJaNNN3Wr  4K4PzTi3Fv2P8AsOEV7ZpzOzac m6um99dtnKV K1CzuLrzQ8 y248iKMCCebBfIye\nR/Ceee2ehrEittOtFJx5cn/LKOQ5xyw7/XP9CCK273V45YZYU5f/AFQPPqw7nvjPqMEA8k1hWejX\nN3MqP5hmlH7tI8joWxxk56n36fMSRX55js7q41SpJ/7HbXe1 aWlm762W9t1rqf19wtkuS8J4D2e\nFw/1KN0nv3aaevS71W musmRW0807yQlJAVlHleXk4yW755yMe4BUZYhq qfhJ zX4m IR/tW88z\nw5oTgeTeXlp5Nxeg5z9ktMnHIzjvg9xlva/2ff2ftDsol8TeM7SO81JIraW00y4iBt7P5mz83TkY\nYg56hcggtX2fcX0VrapbWSeRBbYii8uIweXy4PU5GCvTBJOOcA5eDwlJ3q4nbpK1la8tdWmrPbXR\ntJN 9f8AP M/EussX9QyC31uy/4VUlveVrXa0slra22rk234dpv7J3wV0O3H9radJr90Ij5suoL5\n46vntj259Tg5DZW  D3wOWzfTU8B HGtz/BPoelT5wzjOL044Cgc9 55J9BvNZOx1keSSPjB6Z5Y\nZ4OQCSOpOMDrljXC6hqURnNwXHlyDPGeeSBzkHgq3/fRySRz1TzOlhl7PCYdYPol3fvK2rta7TUV\n2W97r86wOP4pqVHVxed5pv8A9Da93edn5tNbarXe92/nnx3 zF zdrSr5vwm8HNIn72K4h8P6bEO\nrji5ayYA8HBHfHJYsT8c/Fz4KeHvDdpdr4Q1P xrb7JPF/Z32Qz2/VsY xY789eM9SAGP3b4k8U2\ndt5 bo db4zHg/Ny46A8YHX3B5JJJ QvGuptqE2rzzzRiKb7N9k6/wAJcccnHXGCcZ9SRWFLG5hj\naj9q3jnaNndX0c XdvWydtXu7Wcfe/f/AA8z7iiFWhh8bneZYvB4F3tmeaLMo6vT3VLT4W/5rdWm\nfhx4g HXhz4UeJL9/GVtGn9oXVzdQ IfJ 3aRD9rvG/4  cWOccH6ZyVLV7N4W8M6QwhurVbe9t7\n3bLa3NuLQQT22ZB9qsxn0/8ArEnmvoT4oeAtP8UaPqMNzFG4uMAP5P7gEnUf9L6n 6e QCec4rT/\nAOCd3wD0D4vfCfxJLrutR6XpXgj4teNfBWm3luBPqlxp9s m6tZjOc/KNTBOSAAfvMQ2f3KWd1sR\nwpLNauJX1rArKcrzWSae8pxvvrq47q90173xH99Yvxyy3CcD/wCsnEuafUv7HeU5TmaXM7vM1OeV\nRSu3tCbSvZ3Sv0l4Vf8AhXRfMlhmsLdJvN/1scXH3nHTnsPqeM4IyWr4Y0u1ibdDbqkmBC78d39T\n34PXIORyd1fsBH wd8GJ5o3m8c KpSkolkijS2M82C/XFnz2zzxkgEndn6M H37Lf7Ong2eK7Twp\nb K9UixJFqXilW1E2WC PsdoH02yU4IyVYMDgk4YV8fU4kT1hi35K0rv4uiVvXX W7a0PxrN/pk \nHmV5fKrldPinOsXZuOWPK45XHm5pWTeZzlNc7mm1ytpWbvJH872ifCzxr4u12DS/h74I8Q ODPL5\nUsXh/wAO3OpCIjcT/ptpYgcBNx5yAB1ALH6yl/4JzftQRaPDr9v8DtcuTcRCWWxgvvDk qwncwx/\nZP2/rkcjPJPXK1/Rroeq2el2KWWiWen6Lp8ZMMdlo/2WzsPs 49bSzbP057qOCpz3eleIZVkYLIQ\n5hXnzcjgnOfmyOoz HGRzzR4hqYhOisS8HK6s aPw3lZtWbUtddWno7NpH4FxN9P/wAU6lWj/qrw\nVwnkuU4NRUVmuZZxmma5nK80/eyyrlClHSyioxaX2mkk/wCNbx18GvGXgm vrHxn4B8W DWtwDKf\nEPhzVdNtzcEnBF6LH zz8oznnAKjOCGbx 48PPbW11JBP9qS3x 7j6cFh6d8Z/mTgE/3mWWqaTrN\nlJpOu2Vpqlpc4iu7O/jtb2xkwZACLW9Ofm4yPoDuBYn5k KP/BPT9jn4ywXUmqfDWw8L63cxjyvE\nXgh5/DuoMMEHFray/YbvhT1045YlsFi273sDjcZUT9hiXjE2rKzaSvUT1tslezS1cmul39twV 0k\nwmFq4bCeIfh3mOUbKea8KZr/AGhdqTi TKM2nlmltW3nGYOKS92Unr/HTpmjaPc2KyXKW6TSRDPl\n5xDjeB39wfyJJIrgLnR7a3M/mW1uqiW4Pl/8Cb/S uecfz5O3Nfuv8fv CJHxf0d7nW/2dvHum O\nbF2nKeGvE0tj4a8RQQjcGFpqbSNo15kksF1AWIUK26QkR1 THin9m39oL4b63ceHfiT8MvFnhzUI\nZvIQ6jp1w1vIfmP2q01cN9gvbHBDY0/nay54KlvUw0qWBeLrYvM/qdrJvNOXLPtSs290/JtNLlSj\ndM/unwt kB4KeJ9LFY/g3xNyrF4yXKlwrmv\
                                 */
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
                                        ToastUtil.noNAR(Addstar.this);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
            } else {
                ToastUtil.noNetAvailable(Addstar.this);
            }
        }
    }
}
