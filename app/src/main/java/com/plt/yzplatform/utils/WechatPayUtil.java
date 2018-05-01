package com.plt.yzplatform.utils;

/**
 * Created by Administrator on 2018/4/30 0030.
 */

/**
 * 微信支付功工具类，负责接口回调
 */
public class WechatPayUtil {
    public WechatPayReturn wechatPayReturn;

    public interface WechatPayReturn{
        public void payReturn(boolean result);
    }

    public void setWechatPayReturn(WechatPayReturn wechatPayReturn){
        this.wechatPayReturn = wechatPayReturn;
    }
}
