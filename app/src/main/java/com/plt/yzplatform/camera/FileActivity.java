package com.plt.yzplatform.camera;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.plt.yzplatform.R;

/**
 * Created by Administrator on 2018/4/25 0025.
 */

public class FileActivity extends Activity {

    com.plt.yzplatform.camera.util.FileUtils fileUtils;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testfile);

        fileUtils = new com.plt.yzplatform.camera.util.FileUtils(FileActivity.this);

        Button createDir = (Button) findViewById(R.id.createDir);
        createDir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                fileUtils.createFile("headicon");
//                fileUtils.deleteFile("headicon");
//                fileUtils.createDir("aaa/bbb/ccc");
//                Log.w("test", fileUtils.deleteDir("aaa/bbb/ccc") + "");
//                Log.w("test", fileUtils.isFileExits("headicon") + "");

//                try {
//                    fileUtils.saveFile("testdir/bbbccc.png", getAssets().open("carmodel/cemian.png"));
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }

//                try {
//                    Bitmap bitmap = BitmapFactory.decodeStream(getAssets().open("carmodel/dangwei.png"));
//                    Bitmap test = BitmapFactory.decodeFile(fileUtils.getFile("defabc/testabc.png").getPath());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }

//                File file = fileUtils.getFile("defabc/testabc.png");
//                Log.w("test", file.exists() + "存在");
//                Log.w("test", fileUtils.saveFile("aaa/bbb.png", file).exists() + "新文件");
            }
        });
    }



}
