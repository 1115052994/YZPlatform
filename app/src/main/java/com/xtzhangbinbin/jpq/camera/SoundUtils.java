package com.xtzhangbinbin.jpq.camera;

import android.content.Context;
import android.media.MediaPlayer;

import com.xtzhangbinbin.jpq.R;

/**
 * 作者:
 * <p/>
 * 工号: ${USER_NAME}
 */
public class SoundUtils {
    //定义左右声道的音量大小
    public final static float LEFT_VOLUME = 1.0f;
    public final static float RIGHT_VOLUME = 1.0f;
    /**
     *
     * @Title: playerScanOkWav
     * @Description: R.raw.scan_ok
     * @author
     * @date 2
     * @param @param context    设定文件
     * @param type 0：扫描；1：拍照
     * @return void    返回类型
     * @throws
     */
    public final static void playerScanOkWav(Context context, int type){
        int sound = R.raw.scan_ok;
        if(type == 1){
            sound = R.raw.camera;
        }
        MediaPlayer mediaPlayer = MediaPlayer.create(context,sound);
        mediaPlayer.setVolume(LEFT_VOLUME, RIGHT_VOLUME);
        mediaPlayer.start();
    }
}
