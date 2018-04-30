package com.plt.yzplatform.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.plt.yzplatform.R;

/**
 * Created by glp on 2018/4/29.
 * 描述：
 */

public class OnlineOrderDialog extends Dialog {
    private Button mOk;
    private EditText mPhone;
    private TextView mTime;
    private onYesOnclickListener mYesOnclickListener;   // 确定按钮被点击了的监听器
    private onTimePickerListener mTimePickerListener;

    private static OnlineOrderDialog mDialog;
    public RelativeLayout mLayout;

    public OnlineOrderDialog setYesOnclickListener(onYesOnclickListener onYesOnclickListener) {
        mYesOnclickListener = onYesOnclickListener;
        return mDialog;
    }

    public OnlineOrderDialog setTimePickerListener(onTimePickerListener timePickerListener){
        mTimePickerListener = timePickerListener;
        return mDialog;
    }

    public static OnlineOrderDialog newInstance(Context context) {
        mDialog = new OnlineOrderDialog(context);
        return mDialog;
    }


    public OnlineOrderDialog(@NonNull Context context) {
        super(context, R.style.ordinaryDialog);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_online_order);
        setCanceledOnTouchOutside(false);

        mDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        initView();     // 初始化界面控件
        initEvent();    // 初始化界面控件的事件
    }

    private void initEvent() {
        mOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mYesOnclickListener != null) {
                    mYesOnclickListener.onYesClick();
                }
            }
        });

        mTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTimePickerListener != null){
                    mTimePickerListener.onTimePiker();
                }
            }
        });
    }


    private void initView() {
        mPhone = (EditText) findViewById(R.id.mPhone);
        mTime = (TextView) findViewById(R.id.mTime);
        mOk = (Button) findViewById(R.id.mOk);

    }

    public OnlineOrderDialog showDialog(){
        mDialog.show();
        return mDialog;
    }

    public String getPhone(){
        return mPhone.getText().toString().trim();
    }

    public String getTime(){
        return mTime.getText().toString().trim();
    }

    public void setTime(String s){
        mTime.setText(s);
    }


    public interface onYesOnclickListener {
        void onYesClick();
    }

    public interface onTimePickerListener{
        void onTimePiker();
    }
}
