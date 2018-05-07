package com.xtzhangbinbin.jpq.upgrade;

/**
 * Created by Administrator on 2017/9/14 0014.
 */

public interface UpgradeProgressListener {
    /**
     * @param progress     已经下载或上传字节数
     * @param total        总字节数
     * @param done         是否完成
     */
    void onProgress(long progress, long total, boolean done);
}
