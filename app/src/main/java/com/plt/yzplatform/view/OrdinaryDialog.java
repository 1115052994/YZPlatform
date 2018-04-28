package com.plt.yzplatform.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.plt.yzplatform.R;


/**
 * 作者: CoolTone
 * 描述: 自定义普通 Dialog
 * 用于单句提示、双句居中提示
 * 链条式设置赋值、监听
 * 可设置确定、取消的文本提示
 */
public class OrdinaryDialog extends Dialog {
    private TextView mYes;  // 确定
    private TextView mNo;   // 取消

    private TextView mTitle;  // 消息提示文本
    private TextView mContent;  // 消息提示文本
    private String sTitle;    // 外界描述的消息文本
    private String sContent;    // 外界描述的消息文本

    private String sYes, sNo;    // 确定文本 取消文本的显示内容

    private onNoOnclickListener mNoOnclickListener;     // 取消按钮被点击了的监听器
    private onYesOnclickListener mYesOnclickListener;   // 确定按钮被点击了的监听器

    private static OrdinaryDialog mDialog;  // 链条式方法
    public RelativeLayout mLayout;

    public OrdinaryDialog setNoOnclickListener(onNoOnclickListener onNoOnclickListener) {
        mNoOnclickListener = onNoOnclickListener;
        return mDialog;
    }

    public OrdinaryDialog setYesOnclickListener(onYesOnclickListener onYesOnclickListener) {
        mYesOnclickListener = onYesOnclickListener;
        return mDialog;
    }

    public static OrdinaryDialog newInstance(Context context) {
        mDialog = new OrdinaryDialog(context);
        return mDialog;
    }

    private OrdinaryDialog(Context context) {
        super(context, R.style.ordinaryDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_ordinary);

//        Window window = getWindow();
//        WindowManager.LayoutParams lp = window.getAttributes();
//        lp.alpha = 0.9f;
//        window.setAttributes(lp);

        setCanceledOnTouchOutside(false);   // 按空白处

        mDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        initView();     // 初始化界面控件
        initData();     // 初始化界面数据
        initEvent();    // 初始化界面控件的事件
    }

    private void initEvent() {
        mYes.setOnClickListener(new View.OnClickListener() {//确定 点击的外界监听
            @Override
            public void onClick(View v) {
                if (mYesOnclickListener != null) {
                    mYesOnclickListener.onYesClick();
                }
            }
        });
        mNo.setOnClickListener(new View.OnClickListener() {//取消 点击的外界监听
            @Override
            public void onClick(View v) {
                if (mNoOnclickListener != null) {
                    mNoOnclickListener.onNoClick();
                }
            }
        });
    }

    private void initData() {   // 外界设置控件显示
        if (sTitle != null) {
            mTitle.setText(sTitle);
        }

        if (sContent != null) {
            mContent.setText(sContent);
        }
        mContent.setVisibility(sContent != null ? View.VISIBLE : View.GONE);

        if (sYes != null) {
            mYes.setText(sYes);
        }
        if (sNo != null) {
            mNo.setText(sNo);
        }
    }

    private void initView() {
        mYes = (TextView) findViewById(R.id.tv_ordinary_confirm);
        mNo = (TextView) findViewById(R.id.tv_ordinary_cancel);
        mTitle = (TextView) findViewById(R.id.tv_ordinary_title);
        mContent = (TextView) findViewById(R.id.tv_ordinary_content2);
        mLayout = ((RelativeLayout) findViewById(R.id.rl_ordinary_layout));
    }

    // 设置信息文本
    public OrdinaryDialog setMessage1(String message) {
        sTitle = message;
        return mDialog;
    }

    // 设置信息文本
    public OrdinaryDialog setMessage2(String message) {
        sContent = message;
        return mDialog;
    }

    // 设置确定文本
    public OrdinaryDialog setConfirm(String confirm) {
        sYes = confirm;
        return mDialog;
    }

    // 设置取消文本
    public OrdinaryDialog setCancel(String cancel) {
        sNo = cancel;
        return mDialog;
    }

    public OrdinaryDialog showDialog() {
        mDialog.show();
        return mDialog;
    }

    public interface onYesOnclickListener {
        void onYesClick();
    }

    public interface onNoOnclickListener {
        void onNoClick();
    }
}
