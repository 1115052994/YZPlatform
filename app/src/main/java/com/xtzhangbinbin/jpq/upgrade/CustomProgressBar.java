package com.xtzhangbinbin.jpq.upgrade;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import com.xtzhangbinbin.jpq.R;

import java.text.DecimalFormat;


public class CustomProgressBar extends ProgressBar {

    private Context mContext;
    private Paint mPaint;
    private PorterDuffXfermode mPorterDuffXfermode;
    private float mProgress;
    private int mState;

    // IconTextProgressBar的状态
    private static final int STATE_DEFAULT = 101;
    private static final int STATE_DOWNLOADING = 102;
    private static final int STATE_PAUSE = 103;
    private static final int STATE_DOWNLOAD_FINISH = 104;
    // IconTextProgressBar的文字大小(sp)
    private static final float TEXT_SIZE_SP = 12f;
    // IconTextProgressBar的图标与文字间距(dp)
    private static final float ICON_TEXT_SPACING_DP = 5f;

    public CustomProgressBar(Context context) {
        super(context, null, android.R.attr.progressBarStyleHorizontal);
        mContext = context;
        init();
    }

    public CustomProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    /**
     * 设置下载状态
     */
    public synchronized void setState(int state) {
        mState = state;
        invalidate();
    }

    /**
     * 设置下载进度
     */
    public synchronized void setProgress(float progress) {
        super.setProgress((int) progress);
//        LogUtil.w("pp:" + progress * 100);
        mProgress = progress / getMax() * 100;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        switch (mState) {
            case STATE_DEFAULT:
                drawIconAndText(canvas, STATE_DEFAULT, false);
                break;

            case STATE_DOWNLOADING:
                drawIconAndText(canvas, STATE_DOWNLOADING, false);
                break;

            case STATE_PAUSE:
                drawIconAndText(canvas, STATE_PAUSE, false);
                break;

            case STATE_DOWNLOAD_FINISH:
                drawIconAndText(canvas, STATE_DOWNLOAD_FINISH, false);
                break;

            default:
                drawIconAndText(canvas, STATE_DEFAULT, false);
                break;
        }
    }

    private void init() {
        setIndeterminate(false);
        setIndeterminateDrawable(mContext.getResources().getDrawable(android.R.drawable.progress_indeterminate_horizontal));
        setProgressDrawable(mContext.getResources().getDrawable(R.drawable.progressbar_bg));

        mPaint = new Paint();
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setTextAlign(Paint.Align.LEFT);
        mPaint.setTextSize(MeasureUtil.sp2px(mContext, TEXT_SIZE_SP));
        mPaint.setTypeface(Typeface.MONOSPACE);
        mPaint.setColor(mContext.getResources().getColor(R.color.theme_coloer));

        mPorterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
    }

    private void initForState(int state) {
        switch (state) {
            case STATE_DEFAULT:
                setProgress(100);
                mPaint.setColor(mContext.getResources().getColor(R.color.theme_coloer));
                break;

            case STATE_DOWNLOADING:
                mPaint.setColor(mContext.getResources().getColor(R.color.theme_coloer));
                break;

            case STATE_PAUSE:
                mPaint.setColor(mContext.getResources().getColor(R.color.theme_coloer));
                break;

            case STATE_DOWNLOAD_FINISH:
                mPaint.setColor(mContext.getResources().getColor(R.color.theme_coloer));
                break;

            default:
                setProgress(100);
                mPaint.setColor(mContext.getResources().getColor(R.color.theme_coloer));
                break;
        }
    }

    private void drawIconAndText(Canvas canvas, int state, boolean onlyText) {
        initForState(state);

        String text = getText(state);
        Rect textRect = new Rect();
        mPaint.getTextBounds(text, 0, text.length(), textRect);

        if (onlyText) {
            // 仅绘制文字
            float textX = (getWidth() / 2) - textRect.centerX();
            float textY = (getHeight() / 2) - textRect.centerY();
            canvas.drawText(text, textX, textY, mPaint);
        } else {
            // 绘制图标和文字

            float textX = (getWidth() / 2) -
                    getOffsetX(textRect.centerX(), ICON_TEXT_SPACING_DP, true);
            float textY = (getHeight() / 2) - textRect.centerY();
            canvas.drawText(text, textX, textY, mPaint);


            if (state == STATE_DEFAULT) return;

            Bitmap bufferBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            Canvas bufferCanvas = new Canvas(bufferBitmap);
            bufferCanvas.drawText(text, textX, textY, mPaint);
            // 设置混合模式
            mPaint.setXfermode(mPorterDuffXfermode);
            mPaint.setColor(mContext.getResources().getColor(R.color.white));
            RectF rectF = new RectF(0, 0, getWidth() * mProgress / 100, getHeight());
            // 绘制源图形
            bufferCanvas.drawRect(rectF, mPaint);
            // 绘制目标图
            canvas.drawBitmap(bufferBitmap, 0, 0, null);
            // 清除混合模式
            mPaint.setXfermode(null);
            if (!bufferBitmap.isRecycled()) {
                bufferBitmap.recycle();
            }
        }
    }

    private String getText(int state) {
        String text;
        switch (state) {
            case STATE_DEFAULT:
                text = "下载";
                break;

            case STATE_DOWNLOADING:
                DecimalFormat decimalFormat = new DecimalFormat("#0");
                text = decimalFormat.format(mProgress) + "%";
                break;

            case STATE_PAUSE:
                text = "继续";
                break;

            case STATE_DOWNLOAD_FINISH:
                text = "下载完毕";
                break;

            default:
                text = "下载";
                break;
        }
        return text;
    }

    private float getOffsetX(float textHalfWidth, float spacing, boolean isText) {
        float totalWidth = MeasureUtil.dip2px(mContext, spacing) + textHalfWidth * 2;
        // 文字偏移量
        if (isText) return totalWidth / 2 - spacing;
        // 图标偏移量
        return totalWidth / 2;
    }

}
