package com.albertou.study.picturemark.view;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

public class RectPainter extends SubPainter {

    private PointF mOrigin;
    private PointF mCurrent;
    private boolean isOval = false; //是否是椭圆

    public RectPainter(PointF origin, boolean isOval) {
        mOrigin = origin;
        mCurrent = origin;
        mPaint.setStyle(Paint.Style.STROKE);
        this.isOval = isOval;
    }

    public void setCurrent(PointF current) {
        mCurrent = current;
    }

    @Override
    public void draw(Canvas canvas) {
        float left = Math.min(mOrigin.x, mCurrent.x);
        float right = Math.max(mOrigin.x, mCurrent.x);
        float top = Math.min(mOrigin.y, mCurrent.y);
        float bottom = Math.max(mOrigin.y, mCurrent.y);
        RectF rectF = new RectF(left, top, right, bottom);


        if (isOval) {
            canvas.drawOval(rectF, mPaint);
        } else {
            canvas.drawRect(rectF, mPaint);
        }
    }

    @Override
    public void draw(Canvas canvas, float ratio) {
        float left = Math.min(mOrigin.x, mCurrent.x) * ratio;
        float right = Math.max(mOrigin.x, mCurrent.x) * ratio;
        float top = Math.min(mOrigin.y, mCurrent.y) * ratio;
        float bottom = Math.max(mOrigin.y, mCurrent.y) * ratio;

        RectF rectF = new RectF(left, top, right, bottom);

        Paint paint = new Paint(mPaint);
        paint.setStrokeWidth(mPaint.getStrokeWidth() * ratio);

        if (isOval) {
            canvas.drawOval(rectF, paint);
        } else {
            canvas.drawRect(rectF, paint);
        }
    }

    public void setStrokeWidth(float width) {
        mPaint.setStrokeWidth(width);
    }
}
