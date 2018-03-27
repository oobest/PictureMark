package com.albertou.study.picturemark.view;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * 画文字
 * Created by Albert on 2018/3/27.
 */

public class TextPainter extends SubPainter {

    private String mText;

    private float px;

    private float py;

    public TextPainter() {
        super();
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    public void draw(Canvas canvas) {
        Rect rect = new Rect();
        mPaint.getTextBounds(mText, 0, mText.length(), rect);
        canvas.drawText(mText, px-rect.width()/2, py-rect.height()/2, mPaint);
    }

    @Override
    public void draw(Canvas canvas, float ratio) {
        Rect rect = new Rect();
        mPaint.getTextBounds(mText, 0, mText.length(), rect);
        Paint paint = new Paint(mPaint);
        paint.setTextSize(mPaint.getTextSize()*ratio);
        canvas.drawText(mText, (px-rect.width()/2)*ratio, (py-rect.height()/2)*ratio, paint);
    }


    public void setText(String text) {
        mText = text;
    }

    public void moveTo(float x, float y) {
        this.px = this.px + x;
        this.py = this.py + y;
    }

    public void setTextSize(float textSize) {
        mPaint.setTextSize(textSize);
    }

    public float getTextSize(){
        return mPaint.getTextSize();
    }
}
