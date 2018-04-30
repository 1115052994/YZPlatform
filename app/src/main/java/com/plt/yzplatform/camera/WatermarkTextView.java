package com.plt.yzplatform.camera;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

import com.plt.yzplatform.R;

/**
 * 作者: Created by 328798 on 2017/6/9.
 * <p/>
 * 图片开单拍照时水印提示文字控件
 *
 *
 * 工号: ${USER_NAME}
 */
@SuppressLint("AppCompatCustomView")
public class WatermarkTextView extends TextView {

    private static final int DEFAULT_DEGREES = 0;
    private int mDegrees;

    public WatermarkTextView(Context context) {
        super(context);
    }

    public WatermarkTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setGravity(Gravity.CENTER);
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.WatermarkTextView);
        mDegrees = a.getDimensionPixelSize(R.styleable.WatermarkTextView_degree,
                DEFAULT_DEGREES);
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        canvas.translate(getCompoundPaddingLeft(), getExtendedPaddingTop());
        canvas.rotate(mDegrees, this.getWidth() / 2f, this.getHeight() / 2f);
        super.onDraw(canvas);
        canvas.restore();
    }

    public void setDegrees(int degrees) {
        mDegrees = degrees;
    }
}
