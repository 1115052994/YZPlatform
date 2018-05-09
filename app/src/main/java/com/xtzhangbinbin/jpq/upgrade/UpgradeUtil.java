package com.xtzhangbinbin.jpq.upgrade;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.xtzhangbinbin.jpq.config.Config;
import com.xtzhangbinbin.jpq.entity.VersionInfo;
import com.xtzhangbinbin.jpq.utils.FileUtils;
import com.xtzhangbinbin.jpq.utils.NetUtil;
import com.xtzhangbinbin.jpq.utils.OKhttptils;
import com.xtzhangbinbin.jpq.utils.Prefs;
import com.xtzhangbinbin.jpq.utils.ToastUtil;
import com.zhy.http.okhttp.OkHttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("all")
public class UpgradeUtil {

    private Context context;
    private File apkFile;
    private FileUtils fileUtils;
    private long total;
    public DownloadEvent downloadEvent;
    public static UpgradeUtil upgradeUtil;
    public long current;
    public View view;

    private UpgradeUtil(Context context) {
        this.context = context;
        fileUtils = FileUtils.getInstance(context);
    }

    public static UpgradeUtil getInstance(Context context){
        if(upgradeUtil == null){
            upgradeUtil = new UpgradeUtil(context);
        }
        return upgradeUtil;
    }

    public void upgrade(View view){
        this.view = view;
        checkUpgrade();
    }


    /**
     * 判断是否需要升级
     */
    public void checkUpgrade(){
        PackageManager pm = context.getPackageManager();
        PackageInfo pi = null;
        try {
            pi = pm.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Map<String, String> map = new HashMap<>();
        map.put("versionnum", String.valueOf(pi != null ? pi.versionCode : 1));
        if (NetUtil.isNetAvailable(context)) {
            OKhttptils.post((Activity)context, Config.UPTRADE_CHECK_VERSION, map, new OKhttptils.HttpCallBack() {
                @Override
                public void success(String response) {
                    try {
                        JSONObject obj = new JSONObject(response);
                        if(obj != null && "yes".equals(obj.getJSONObject("data").getString("result"))){
                            getUpgradeInfo();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void fail(String response) {
                    Toast.makeText(context, "查询失败", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    /**
     * 获取升级信息
     */
    public void getUpgradeInfo(){
        Map<String, String> map = new HashMap<>();
        if (NetUtil.isNetAvailable(context)) {
            OKhttptils.post((Activity)context, Config.UPTRADE_GET_VERSIONINFO, map, new OKhttptils.HttpCallBack() {
                @Override
                public void success(String response) {
                    Gson gson = new Gson();
                    VersionInfo info = gson.fromJson(response, VersionInfo.class);
                    //显示升级信息
                    showUpgradeInfo(info);
                }

                @Override
                public void fail(String response) {
                    Log.w("test", response);
                    Toast.makeText(context, "查询失败", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void showUpgradeInfo(VersionInfo info){
        UpgradetWindow upgradetWindow = new UpgradetWindow(context, info);
        upgradetWindow.showAsDropDown(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void install(Context context, File file) {
        if (null != file && file.exists()) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            //判断是否是AndroidN以及更高的版本
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Uri contentUri = FileProvider.getUriForFile(context, "com.xtzhangbinbin.jpq.fileProvider", file);
                intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
            } else {
                intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        }
    }


    public void download(final Context context, String url) {
        if (NetUtil.isNetAvailable(context)) {
            OkHttpUtils.initClient(OKhttptils.client).get()
                    .url(url)      //Prefs.with(context).read("user_token")
//                    .addHeader("user_token", "96730A47BBCD8F345203CFAB9A2CA83AD3A659123EBC29B32086BADC51AE8B70A798AA55557B5D7025D663C94C2C585467FBFD2FB17CABA765D25BA4E4E69D9C")
//                    .addHeader("user_token","96730A47BBCD8F345203CFAB9A2CA83AD3A659123EBC29B32086BADC51AE8B70C998E8650962F0C9F6C39DC0A2C687911337D2E0C74B38C5ED97F690642D4585" )
                    .addHeader("user_token", Prefs.with(context).read("user_token"))
                    .build()
                    .execute(new com.zhy.http.okhttp.callback.Callback() {
                        @Override
                        public Object parseNetworkResponse(okhttp3.Response response, int id) throws Exception {
                            total = response.body().contentLength();
                            saveFile("/upgrade/upgrade.apk", response.body().byteStream());
                            return null;
                        }

                        @Override
                        public void onError(okhttp3.Call call, Exception e, int id) {
                        }

                        @Override
                        public void onResponse(Object response, int id) {
                            total = 0;
                            current = 0;
                        }
                    });
        } else {
            ToastUtil.noNetAvailable(context);
        }
    }

    /**
     * 保存文件到指定路径
     *
     * @param filePath 文件保存路径
     * @param input    文件流
     * @return
     */
    public void saveFile(String filePath, InputStream input) {

        OutputStream out = null;
        try {
            //判断所需保存文件路径中的文件夹是否存在
            if (null != filePath && filePath.contains(File.separator)) {
                //创建路径中所需的文件夹
                String dir = filePath.substring(0, filePath.lastIndexOf(File.separator));
                fileUtils.createDir(dir);
            }
            //创建新文件
            apkFile = fileUtils.createFile(filePath);

            out = new FileOutputStream(apkFile);
            /* 创建操作文件的缓存 */
            byte[] buffer = new byte[20 * 1024];
			/* 将文件读入到缓存当中，如果完成读取，则等于-1 */

            int value = 0;

            while ((value = input.read(buffer)) != -1) {
				/* 将缓存当中的数据写入文件当中 */
                out.write(buffer, 0, value);
                current += value;
                downloadEvent.download(total, current);
            }
            out.flush();
            out.close();
            input.close();
            install(context, apkFile);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(out != null){
                    out.close();
                }
                if(input != null){
                    input.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public interface DownloadEvent{
        public void download(long total, long progress);
    }

    public void setDownloadEvent(DownloadEvent event){
        this.downloadEvent = event;
    }
}
