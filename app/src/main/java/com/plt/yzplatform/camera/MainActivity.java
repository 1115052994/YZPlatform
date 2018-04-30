package com.plt.yzplatform.camera;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.plt.yzplatform.R;
import com.plt.yzplatform.camera.test.TestActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private Button bt_name;
    private Button bt_url;
    private ZoomImagView photo_previews=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_main);
        photo_previews= (ZoomImagView) findViewById(R.id.photo_previews);
        bt_name= (Button) findViewById(R.id.bt_name);
        bt_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int checkCallPhonePermission = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA);
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},222);
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.CAMERA},222);
                if(checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.CAMERA},222);
                    return;
                }else{
                    Intent intent=new Intent(MainActivity.this,PhotographActivity.class);
                    startActivityForResult(intent,0);
                }
            }
        });

        bt_url = (Button) findViewById(R.id.bt_url);
        bt_url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.this.startActivity(new Intent(MainActivity.this, TestActivity.class));
            }
        });
    }
    /**
     * 截取出前面一段filePath,用来封装压缩的文件
     * @param filePath
     * @return
     */
    private String getSubFilePath(String filePath) {
        String[] path = filePath.split("/");
        StringBuilder subFilePath = new StringBuilder();
        for (int i = 0; i < path.length; i++) {
            if (i == path.length - 1){
                continue;
            }
            subFilePath = subFilePath.append(path[i]).append("/");
        }
        return subFilePath.toString();
    }


    /**
     * 将文件转换为byte[]
     * @param file
     * @return
     * @throws IOException
     */
    private  byte[] getBytes(File file){
        byte[] buffer = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1024 * 8);
            byte[] b = new byte[1024*8];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data==null){
            return;
        }
        Bundle extras = data.getExtras();
        if(extras!=null){
            String path = extras.getString("maximgPath");
            if(path!=null){
                File file = new File(path);
                byte[] byimag =getBytes(file);
                Bitmap map= ImageTools.byteToBitmap(byimag);
                photo_previews.setImageBitmap(map);
            }
        }
    }
}
