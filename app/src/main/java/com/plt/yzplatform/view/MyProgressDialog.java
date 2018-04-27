package com.plt.yzplatform.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;

import com.plt.yzplatform.R;


/**
 * Created by Administrator on 2017/8/23 0023.
 */

public class MyProgressDialog extends Dialog {
    private static MyProgressDialog myProgressDialog = null;
    public MyProgressDialog(Context context) {
        super(context);
    }

    public MyProgressDialog(Context context, int theme) {
        super(context, theme);
    }

    public static MyProgressDialog createDialog(Context context){
        myProgressDialog = new MyProgressDialog(context, R.style.CustomProgressDialog);
        myProgressDialog.setContentView(R.layout.myprogressdialog);
        myProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        myProgressDialog.setCancelable(false);

        return myProgressDialog;
    }

    public MyProgressDialog setMessage(String strMessage){
        TextView tvMsg = (TextView)myProgressDialog.findViewById(R.id.myprogressdialog_message);

        if (tvMsg != null){
            tvMsg.setText(strMessage);
        }

        return myProgressDialog;
    }
}
