package com.xtzhangbinbin.jpq.upgrade;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.entity.VersionInfo;

import junit.runner.Version;

import java.io.File;


public class UpgradetWindow {
	private Context context;
	private PopupWindow popupWindow ;
	private LinearLayout messageLayout;
	private TextView content;
    private TextView versionName;
	private Button close;
    private Button up;
    private CustomProgressBar bar;
//	private Upgrade upgrade;
	private SharedPreferences sp;
	private SharedPreferences.Editor editor;
	private static final int STATE_DEFAULT = 101;
	private static final int STATE_DOWNLOADING = 102;
	private static final int STATE_PAUSE = 103;
	private static final int STATE_DOWNLOAD_FINISH = 104;
	private int mStateType;
	private UpgradeUtil upgradeUtil;
	private VersionInfo info;


	public UpgradetWindow(Context context, VersionInfo info) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.info = info;
		View view = LayoutInflater.from(context).inflate(R.layout.upgrade, null);
		initView(view);

		popupWindow = new PopupWindow(view,
				LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		popupWindow.setTouchable(true);

		// 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景（很神奇的）
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
			@Override
			public void onDismiss() {
				setBackgroundAlpha(1.0f);
			}
		});
		setBackgroundAlpha(0.5f);
	}

	public void initView(View view){
		content = (TextView) view.findViewById(R.id.upgrade_content);
		content.setText(info.getData().getResult().getApp_version_desc());
        versionName = (TextView) view.findViewById(R.id.upgrade_versionName);
        versionName.setText("新版本号：" + info.getData().getResult().getApp_version_name());
		close = (Button) view.findViewById(R.id.upgrade_close);
		close.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				popupWindow.dismiss();
			}
		});
        up = (Button) view.findViewById(R.id.upgrade_up);
        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
				bar.setVisibility(View.VISIBLE);
				up.setVisibility(View.GONE);
				upgradeUtil.download(context, info.getData().getResult().getApp_version_url());
//				popupWindow.dismiss();
            }
        });

		upgradeUtil = UpgradeUtil.getInstance(context);

		bar = (CustomProgressBar) view.findViewById(R.id.newview_bar);
		mStateType = STATE_DOWNLOADING;
		bar.setState(mStateType);
		upgradeUtil.setDownloadEvent(new UpgradeUtil.DownloadEvent() {
			@Override
			public void download(long total, long progress) {
				bar.setMax((int) total);
				bar.setProgress((float) progress);

				if(total == progress){
					mStateType = STATE_DOWNLOAD_FINISH;
					bar.setState(mStateType);
				}
			}
		});

	}

	//下拉式 弹出 pop菜单 parent 右下角
	public void showAsDropDown(View parent) {
		Log.w("test", null == parent ? "aa" : "bb");
		popupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
		// 使其聚集
		popupWindow.setFocusable(true);
		// 设置允许在外点击消失
		popupWindow.setOutsideTouchable(true);
		//刷新状态
		popupWindow.update();
	}

	//隐藏菜单
	public void dismiss() {
		popupWindow.dismiss();
	}

	/**
	 * 设置添加屏幕的背景透明度
	 *
	 * @param bgAlpha
	 *            屏幕透明度0.0-1.0 1表示完全不透明
	 */
	public void setBackgroundAlpha(float bgAlpha) {
		WindowManager.LayoutParams lp = ((Activity) context).getWindow()
				.getAttributes();
		lp.alpha = bgAlpha;
		((Activity) context).getWindow().setAttributes(lp);

	}

}
