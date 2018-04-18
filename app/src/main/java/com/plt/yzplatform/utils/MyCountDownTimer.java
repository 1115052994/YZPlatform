package com.plt.yzplatform.utils;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.Button;

import com.plt.yzplatform.R;

public class MyCountDownTimer extends android.os.CountDownTimer {
    //private TextView textView;
    private Button button;
///
    /**
     * @param textView   The TextView
     *
     *
     * @param millisInFuture The number of millis in the future from the call
     *       to {@link #start()} until the countdown is done and {@link #onFinish()}
     *       is called.
     * @param countDownInterval The interval along the way to receiver
     *       {@link #onTick(long)} callbacks.
     */
    public MyCountDownTimer(Button button, long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
        this.button = button;
    }
//
    /**
     * 倒计时期间会调用
     * @param millisUntilFinished
     */
    @Override
    public void onTick(long millisUntilFinished) {
        button.setClickable(false); //设置不可点击
        button.setText(  millisUntilFinished / 1000 + "秒后重新获取"); //设置倒计时时间
        button.setBackgroundResource(R.drawable.usercenter_shape_btn_normal); //设置按钮为灰色，这时是不能点击的

        SpannableString spannableString = new SpannableString(button.getText().toString()); //获取按钮上的文字
        ForegroundColorSpan span = new ForegroundColorSpan(Color.RED);
        /**
         * public void setSpan(Object what, int star, int end, int flags) {
         * 主要是start跟end，start是起始位置,无论中英文，都算一个。
         * 从0开始计算起。end是结束位置，所以处理的文字，包含开始位置，但不包含结束位置。
         */
        spannableString.setSpan(span, 0, 2, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);//将倒计时的时间设置为红色
        button.setText(spannableString);
    }

    /**
     * 倒计时完成后调用
     */
    @Override
    public void onFinish() {
        button.setText("重新获取验证码");
        button.setClickable(true);//重新获得点击
        button.setBackgroundResource(R.drawable.usercenter_shape_btn_normal); //还原背景色
    }
}