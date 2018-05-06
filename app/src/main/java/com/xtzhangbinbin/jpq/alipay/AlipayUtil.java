package com.xtzhangbinbin.jpq.alipay;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.xtzhangbinbin.jpq.config.Config;
import com.xtzhangbinbin.jpq.utils.OKhttptils;
import com.xtzhangbinbin.jpq.utils.ToastUtil;
import com.xtzhangbinbin.jpq.view.MyProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 *  重要说明:
 *  
 *  这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
 *  真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
 *  防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险； 
 */
public class AlipayUtil {
	
	/** 支付宝支付业务：入参app_id */
	public static final String APPID = "2018031602385813";
	
	/** 支付宝账户登录授权业务：入参pid值 */
	public static final String PID = "";
	/** 支付宝账户登录授权业务：入参target_id值 */
	public static final String TARGET_ID = "";

	/** 商户私钥，pkcs8格式 */
	/** 如下私钥，RSA2_PRIVATE 或者 RSA_PRIVATE 只需要填入一个 */
	/** 如果商户两个都设置了，优先使用 RSA2_PRIVATE */
	/** RSA2_PRIVATE 可以保证商户交易在更加安全的环境下进行，建议使用 RSA2_PRIVATE */
	/** 获取 RSA2_PRIVATE，建议使用支付宝提供的公私钥生成工具生成， */
	/** 工具地址：https://doc.open.alipay.com/docs/doc.htm?treeId=291&articleId=106097&docType=1 */
	public static final String RSA2_PRIVATE = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCkBsEx4Aby84HgkBEYkdHCGdZBm8kzSo9br1HyNsauc+Lab7MFQgFkWZi/b+WkBaU8TukSoAeiyFzJ8Qx/WmO/C7KdfceP80ugQYRf6PbuAMNC9VICrafkCMm2bpNJd/B3brFmFwX5bQn6l1GQ+pmCitlwL2PoD+qHj7scvBeDjbF6kBCkoT1wthr7RPdgdtxzEmDKyhc0PVigVertrcdS1p8s+U+J2AL799bmpc1Hh1/dqylNyGuW42W9Yxhb8tfEBpqNt0ROxVVzK9W7EraTs+xJg+IpeABQPL7m+ahjhTpjzK5s1QgYZt/OUZKq0wpCty8LCbNjnn11m/FXxfRTAgMBAAECggEAfktJI/rnvtcpsFudHjrgtEAqjZfC4oBNQ34JbaMpdZd7G0+YGrtI0Rh/mR8yRQmnwXjM0u9vEbKOZ1r3kXNqi+YY4FTBfKALIdhwcVyyDG1/6yVywOcu91Cg55vaQl5cQ8lyRDYD/porbcxnHGcoHbCFXXDPzxW6MijEHIri3AHTAwrcmheK7zFwvjNXdYtPv+nnA4DUIfd2gpSjzSGB2b3E1gRTv1pbW8LiepG5JAYR0EQETHatN7oMfb4tgb4f6M+cTj9jfxbkivrTzJ60+qBt5+cjF1Il9r5EZmyDM3xirK/1ks5jEnx3hT4KhDEzaICYMwme+aaCnK8Zl88FiQKBgQD5JsDndhhkWduIhtN37+lEXddWaOFrOzed0c/m2O5kzKt47rQ5mz/8cFx2tWlIl5LEkspU3MtUYwM+PfU0EgroR/vGqImlDy6DwlTLf7cELkHX+RBM+y4wfTnoakv1VsEg14BqBqNySfjFbe9yC7lq8cZaZ+ugm5gOiRYRoGLW7QKBgQCoiPzGqkErFDlVzmgLBRaQiGedQV1sejBRKBcsl4dYmsxX1ck1eAMC3dzrrFuDGcbeA4tjiMg9SQjgPtF/iOwh/7xJ3RfRkoY0xs4gn3CxIFNwf4aWSh5rSJzCPclDnx7ZTOO4z4OYL7TZX+8bxn6/uwL/1R1xuGWqooGM001QPwKBgAXbwGtw3m/1oPDb4rsOPBjsb/TLMIm58bvXRCE0dp6OJXYJftlf6ihAaBwuddRMJBpVLUbDjlBkp53eDRb1WaZOkv//1IS6D6jvd9j1HtONVuUCKTz2Jwbi2uq5HSIesRnZl15m248DaEMUK64t0mVOXBCiIus63V9yH7+4WrqdAoGAM/mDEvVggZuMDwVHmtZn3gzi/2EwIYsRKYYWl37nk5CxogjhM2cebpzjS57s3eJuLPuk2f5N2nCSly1oSE9+mwGWHKVdehkY4F/LI85n34gbk18Qr3qOJQHQriqFzh/dUKBgvhbo+18DA+KNB+DQJ+rRgvpDSVXXrKbPExH7DtsCgYATvrNF2uyfW9VcNNuVOlQvS4n6AhM/lcOkZegN/xwv1ax3FMAoyratArtIrgwAOL0wutII0/VzgX/AKtc8PCJDDSC/Sn1dzcxqyoOglMcEDgqEiHEs4s4QGc+0KRugP0ntnboPjSXAfRUuIB8Ok4BHTuw7ytUmtQg67YDe0NmF9g==";
	public static final String RSA_PRIVATE = "";
	
	private static final int SDK_PAY_FLAG = 1;
	private static final int SDK_AUTH_FLAG = 2;

	private Activity context;
	private static AlipayUtil alipayUtil;
	private MyProgressDialog dialog;
	private AlipayReturn alipayReturn;


	private AlipayUtil(Activity context){
		this.context = context;
	}

	public static AlipayUtil getInstance(Activity context){
		if(null == alipayUtil){
			alipayUtil = new AlipayUtil(context);
		}
		return alipayUtil;
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@SuppressWarnings("unused")
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SDK_PAY_FLAG: {
				@SuppressWarnings("unchecked")
				PayResult payResult = new PayResult((Map<String, String>) msg.obj);
				/**
				 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
				 */
				String resultInfo = payResult.getResult();// 同步返回需要验证的信息
				String resultStatus = payResult.getResultStatus();
				// 判断resultStatus 为9000则代表支付成功
				if (TextUtils.equals(resultStatus, "9000")) {
					// 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
					Toast.makeText(context, "支付成功", Toast.LENGTH_SHORT).show();
					alipayReturn.payReturn(true, resultInfo);
				} else {
					// 该笔订单真实的支付结果，需要依赖服务端的异步通知。
					Toast.makeText(context, "支付失败", Toast.LENGTH_SHORT).show();
					alipayReturn.payReturn(false, resultInfo);
				}

				break;
			}
			default:
				break;
			}
		};
	};
	/**
	 * 支付宝支付业务
	 */
	public void payV2(Map<String, String> map) {
		dialog = MyProgressDialog.createDialog(context);
		dialog.setMessage("正在调起支付，请稍候");
		dialog.show();
		if (TextUtils.isEmpty(APPID) || (TextUtils.isEmpty(RSA2_PRIVATE) && TextUtils.isEmpty(RSA_PRIVATE))) {
			new AlertDialog.Builder(context).setTitle("警告").setMessage("需要配置APPID | RSA_PRIVATE")
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialoginterface, int i) {
							//
							context.finish();
						}
					}).show();
			return;
		}
		/*
		异步到服务器端获取签名
		 */
		OKhttptils.post(context, Config.ORDERS_ALIPAY_SIGNINFO, map, new OKhttptils.HttpCallBack() {
			@Override
			public void success(String response) {
				dialog.dismiss();
				try {
					JSONObject object = new JSONObject(response);
					if(null != object){
						//获取签名信息
						final String signInfo = object.getJSONObject("data").getString("result");
						Runnable payRunnable = new Runnable() {

							@Override
							public void run() {
								PayTask alipay = new PayTask(context);
								Map<String, String> result = alipay.payV2(signInfo, true);
//								Log.i("msp", result.toString());

								Message msg = new Message();
								msg.what = SDK_PAY_FLAG;
								msg.obj = result;
								mHandler.sendMessage(msg);
							}
						};

						Thread payThread = new Thread(payRunnable);
						payThread.start();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				} finally{
					close();
				}

            }

			@Override
			public void fail(String response) {
				dialog.dismiss();
				try {
					JSONObject jsonObject = new JSONObject(response);
					ToastUtil.show(context, jsonObject.getString("message"));
				} catch (JSONException e) {
					e.printStackTrace();
				} finally{
					close();
				}
			}
		});
	}

	public void close(){
		if(null != dialog && dialog.isShowing()){
			dialog.dismiss();
		}
	}

	public interface AlipayReturn{
		public void payReturn(boolean result, String msg);
	}

	public void setPayReturn(AlipayReturn alipayReturn){
		this.alipayReturn = alipayReturn;
	}
}


