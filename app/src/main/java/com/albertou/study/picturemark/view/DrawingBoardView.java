package com.albertou.study.picturemark.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 绘画板
 * Created by Albert on 2018/3/27.
 */

public class DrawingBoardView extends android.support.v7.widget.AppCompatImageView {

    private static final String TAG = "DrawingBoardView";

    private List<SubPainter> mPainters;

    private SubPainter mCurrentPainter;

    private DrawingType mDrawingType;

    private float mTextSize = 60;

    private float mStrokeWidth = 10;

    private Bitmap mBitmap;

    public DrawingBoardView(Context context) {
        this(context, null);
    }

    public DrawingBoardView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawingBoardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPainters = new ArrayList<>();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Iterator<SubPainter> iterator = mPainters.iterator();
        while (iterator.hasNext()) {
            SubPainter painter = iterator.next();
            painter.draw(canvas);
        }
    }

    private float pX;
    private float pY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (mDrawingType == DrawingType.PATH) {
                    PathPainter pathPainter = new PathPainter();
                    pathPainter.setStrokeWidth(mStrokeWidth);
                    mCurrentPainter = pathPainter;
                    mPainters.add(pathPainter);
                    pathPainter.moveTo(event.getX(), event.getY());
                } else if (mDrawingType == DrawingType.TEXT) {
                    pX = event.getX();
                    pY = event.getY();
                } else if(mDrawingType == DrawingType.RECT) {
                    PointF current = new PointF(event.getX(), event.getY());
                    RectPainter painter = new RectPainter(current,false);
                    painter.setStrokeWidth(mStrokeWidth);
                    mCurrentPainter = painter;
                    mPainters.add(painter);
                }else if(mDrawingType == DrawingType.OVAL){
                    PointF current = new PointF(event.getX(), event.getY());
                    RectPainter painter = new RectPainter(current,true);
                    painter.setStrokeWidth(mStrokeWidth);
                    mCurrentPainter = painter;
                    mPainters.add(painter);
                }

                break;
            case MotionEvent.ACTION_MOVE:
                if (mCurrentPainter instanceof PathPainter) {
                    ((PathPainter) mCurrentPainter).quadTo(event.getX(), event.getY());
                } else if (mCurrentPainter instanceof TextPainter) {
                    ((TextPainter) mCurrentPainter).moveTo(event.getX() - pX, event.getY() - pY);
                    pX = event.getX();
                    pY = event.getY();
                } else if(mCurrentPainter instanceof RectPainter){
                    PointF current = new PointF(event.getX(), event.getY());
                    ((RectPainter) mCurrentPainter).setCurrent(current);
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        invalidate();
        return true;
    }


    /**
     * 添加文字画工
     *
     * @param text
     */
    public void addTextPainter(String text) {
        TextPainter painter = new TextPainter();
        painter.setText(text);
        painter.moveTo(getWidth() / 2, getHeight() / 2);
        painter.setTextSize(mTextSize);
        mCurrentPainter = painter;
        mDrawingType = DrawingType.TEXT;
        mPainters.add(painter);
        invalidate();
    }

    public void setTextSize(int textSize) {
        if (mCurrentPainter instanceof TextPainter) {
            ((TextPainter) mCurrentPainter).setTextSize(textSize);
            invalidate();
        }
        mTextSize = textSize;
    }

    public float getTextSize() {
        if (mCurrentPainter instanceof TextPainter) {
            mTextSize = ((TextPainter) mCurrentPainter).getTextSize();
        }
        return mTextSize;
    }

    public void setStrokeWidth(int strokeWidth) {
        if (mCurrentPainter instanceof PathPainter) {
            ((PathPainter) mCurrentPainter).setStrokeWidth(strokeWidth);
            invalidate();
        }
        mStrokeWidth = strokeWidth;
    }

    public float getStrokeWidth() {
        if (mCurrentPainter instanceof PathPainter) {
            mStrokeWidth = ((PathPainter) mCurrentPainter).getStrokeWidth();
        }
        return mStrokeWidth;
    }

    /**
     * 添加路径画工
     */
    public void addPathPainter() {
        mDrawingType = DrawingType.PATH;
    }

    public void addRectPainter(){
        mDrawingType = DrawingType.RECT;
    }

    public void addOvalPainter(){
        mDrawingType = DrawingType.OVAL;
    }

    /**
     * 撤销
     */
    public void undo() {
        mPainters.remove(mCurrentPainter);
        if (mPainters.size() > 0) {
            mCurrentPainter = mPainters.get(mPainters.size() - 1);
        }
        invalidate();
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        this.mBitmap = bm;
        mPainters.clear();
    }

    /**
     * 子线程处理
     *
     * @param file
     * @return
     */
    public boolean save(File file) {
        Bitmap bitmap = getBitmap();
        return true;
    }

    public Bitmap getBitmap() {
        Bitmap resultBitmap = Bitmap.createBitmap(mBitmap.getWidth(),
                mBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(resultBitmap);

        canvas.drawBitmap(mBitmap, new Matrix(), null);
        float ratio = mBitmap.getWidth() / (getWidth()* 1F);
        Iterator<SubPainter> iterator = mPainters.iterator();
        while (iterator.hasNext()) {
            SubPainter painter = iterator.next();
            painter.draw(canvas, ratio);
        }
        return resultBitmap;
    }
}
