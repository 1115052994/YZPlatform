package com.xtzhangbinbin.jpq;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.xtzhangbinbin.jpq.config.Config;
import com.xtzhangbinbin.jpq.utils.FileUtils;
import com.xtzhangbinbin.jpq.utils.NetUtil;
import com.xtzhangbinbin.jpq.utils.PhotoUtils;
import com.xtzhangbinbin.jpq.utils.Prefs;
import com.xtzhangbinbin.jpq.utils.ToastUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.io.ByteArrayOutputStream;

import okhttp3.Call;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        //assertEquals(4, 2 + 2);
        OkHttpUtils.post()
                .url(Config.GET_BASE64)
                .addHeader("user_token","96730A47BBCD8F345203CFAB9A2CA83ABDD25AE0426DCFB3ECD9DC3D956DA3A601719BC10114E399002F03384B081C6F0EC270098992C56EDF3946BEBEAA85CD")
                .addParams("file_id", "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        //ToastUtil.noNAR(context);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.i("response",response);
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
                                Log.i("response",PhotoUtils.base64ToBytes(file_content).length/1024+"");
//                                Bitmap bit = PhotoUtils.base64ToBitmap(file_content);
//                                Log.i("wechat", "压缩之前图片的大小" + (bit.getByteCount() / 1024) + "KB宽度为"
//                                        + bit.getWidth() + "高度为" + bit.getHeight());
//                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                                bit.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//                                byte[] bytes = baos.toByteArray();
////                                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//                                BitmapFactory.Options options = new BitmapFactory.Options();
//                                options.inJustDecodeBounds = true;
//                                BitmapFactory.decodeByteArray(bytes,0,bytes.length,options);
//                                options.inSampleSize = computeSampleSize(options, -1, 512*256);
//                                Log.i("wechat","inSampleSize="+options.inSampleSize);
//                                options.inJustDecodeBounds = false;
//                                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length,options);
//                                Log.i("wechat", "压缩后图片的大小" + (bitmap.getByteCount() / 1024) + "KB宽度为"
//                                        + bitmap.getWidth() + "高度为" + bitmap.getHeight());
//                                if (bitmap!=null) {
//                                    icon.setImageBitmap(bitmap);
//                                    FileUtils.getInstance(context).saveFile(file_id,bitmap);
//                                }
//                                else
//                                    icon.setImageResource(R.drawable.qy_heat);

                            } else {
                                //ToastUtil.noNAR(context);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

}