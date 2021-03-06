package com.xtzhangbinbin.jpq.activity;
/**
 * @Author jiangjiang
 * @CreateTime 2017/6/26
 * .欢迎界面（实现第一次启动引导界面，后面则自动进入主界面）
 */
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.adapter.VpAdapter;
import com.xtzhangbinbin.jpq.config.Config;
import com.xtzhangbinbin.jpq.utils.OKhttptils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class Guide extends AppCompatActivity implements ViewPager.OnPageChangeListener{
    private ViewPager vPager;
    private VpAdapter vpAdapter;
    private static  String[] imgs = new String[3];
    private ArrayList<ImageView> imageViews;
    private ImageView[] dotViews;//小圆点


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        vPager = (ViewPager)findViewById(R.id.guide_ViewPager);

        getImages();
    }

    /**
     * 把引导页要显示的图片添加到集合中，以传递给适配器，用来显示图片。
     */
    private void initImages(){
        //设置每一张图片都填充窗口
        ViewPager.LayoutParams mParams = new ViewPager.LayoutParams();
        imageViews = new ArrayList<ImageView>();

        for(int i=0; i<imgs.length; i++)
        {
            ImageView iv = new ImageView(this);
            iv.setLayoutParams(mParams);//设置布局
            //iv.setImageBitmap(imgs[i]);//为ImageView添加图片资源
            Log.i("imageId=",imgs[i]);
            OKhttptils.getPic(this,imgs[i],iv);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);//这里也是一个图片的适配
            imageViews.add(iv);
            if (i == imgs.length -1 ){
                //为最后一张图片添加点击事件
                iv.setOnTouchListener(new View.OnTouchListener(){
                    @Override
                    public boolean onTouch(View v, MotionEvent event){
                        //跳转到主界面
                        Intent loginActivity = new Intent(Guide.this, MainActivity.class);
                        startActivity(loginActivity);
                        Guide.this.finish();
                        return true;

                    }
                });
            }
        }

    }

    private void initDots(){
        LinearLayout layout = (LinearLayout)findViewById(R.id.dot_Layout);
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(20, 20);
        mParams.setMargins(10, 0, 10,0);//设置小圆点左右之间的间隔
        dotViews = new ImageView[imgs.length];
        //判断小圆点的数量，从0开始，0表示第一个
        for(int i = 0; i < imageViews.size(); i++)
        {
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(mParams);
            imageView.setImageResource(R.drawable.dotselector);
            if(i== 0)
            {
                imageView.setSelected(true);//默认启动时，选中第一个小圆点
            }
            else {
                imageView.setSelected(false);
            }
            dotViews[i] = imageView;//得到每个小圆点的引用，用于滑动页面时，（onPageSelected方法中）更改它们的状态。
            layout.addView(imageView);//添加到布局里面显示
        }

    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        // TODO Auto-generated method stub

    }

    /**
     * arg0：当前滑动显示页面的索引值，可以根据这个值，来设置相应小圆点的状态。
     */
    @Override
    public void onPageSelected(int arg0) {
        for(int i = 0; i < dotViews.length; i++)
        {
            if(arg0 == i)
            {
                dotViews[i].setSelected(true);
            }
            else {
                dotViews[i].setSelected(false);
            }
        }

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            exitBy2Click();      //调用双击退出函数
        }
        return false;
    }
    /**
     * 双击退出函数
     */
    private static Boolean isExit = false;
    private void exitBy2Click() {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true; // 准备退出
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

        } else {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
            System.exit(0);
        }
    }

    private void getImages(){
        Map<String,String> map = new HashMap<>();
        OKhttptils.post(this, Config.SELECTCAROUSEL, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if ("1".equals(status)){
                        JSONObject data = object.getJSONObject("data");
                        JSONArray array = data.getJSONArray("result");
                        imgs[0] = array.getJSONObject(0).getString("adve_file_id");
                        imgs[1] = array.getJSONObject(1).getString("adve_file_id");
                        imgs[2] = array.getJSONObject(2).getString("adve_file_id");
                        initImages();
                        initDots();
                        vpAdapter = new VpAdapter(imageViews);
                        vPager.setAdapter(vpAdapter);
                        vPager.addOnPageChangeListener(Guide.this);
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

}
