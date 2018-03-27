package com.albertou.study.picturemark.view;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;

/**
 * Created by Albert on 2018/3/27.
 */

public class PathPainter extends SubPainter{

    private Path mPath;

    private float pX;

    private float pY;

    public PathPainter() {
        super();
        mPath = new Path();
        mPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawPath(mPath, mPaint);
    }

    @Override
    public void draw(Canvas canvas, float ratio) {
        Matrix matrix = new Matrix();
        matrix.postScale(ratio, ratio, 0,0);
        Path path = new Path();
        mPath.transform(matrix,path);

        Paint paint = new Paint(mPaint);
        paint.setStrokeWidth(mPaint.getStrokeWidth()*ratio);

        canvas.drawPath(path, paint);
    }


    public void moveTo(float x, float y){
        mPath.moveTo(x, y);
        pX = x;
        pY = y;
    }

    public void quadTo(float x, float y){
        mPath.quadTo(pX, pY,x, y);
        pX = x;
        pY = y;
    }

    public void setStrokeWidth(float width){
        mPaint.setStrokeWidth(width);
    }

    public float getStrokeWidth(){
        return mPaint.getStrokeWidth();
    }
}
