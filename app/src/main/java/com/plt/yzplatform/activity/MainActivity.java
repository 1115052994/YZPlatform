package com.plt.yzplatform.activity;

import android.os.Bundle;
import android.view.KeyEvent;

import com.plt.yzplatform.R;
import com.plt.yzplatform.base.BaseActivity;
import com.plt.yzplatform.utils.ActivityUtil;
import com.plt.yzplatform.utils.ToastUtil;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityUtil.addActivity(this);
    }

    /* 退出程序 */
    private long firstTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK: {
                long secondTime = System.currentTimeMillis();
                if (secondTime - firstTime > 2000) {
                    ToastUtil.show(this, "再按一次退出程序！");
                    firstTime = secondTime;
                    return true;
                } else {
                    ActivityUtil.finishAll();
                    System.exit(0);
                }
            }
            break;
        }
        return super.onKeyDown(keyCode, event);
    }
}
