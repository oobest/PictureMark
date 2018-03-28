package com.albertou.study.picturemark.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by Albert on 2018/3/27.
 */

public abstract class SubPainter {

    protected Paint mPaint;

    public SubPainter() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.RED);
    }

    public void setColor(int color){
        mPaint.setColor(color);
    }


    public abstract void draw(Canvas canvas);

    /**
     * 主要用于保存图片文件时使用
     * @param canvas
     * @param ratio
     */
    public abstract void draw(Canvas canvas, float ratio);
}
