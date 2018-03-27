package com.albertou.study.picturemark.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;

/**
 * Created by Albert on 2018/3/27.
 */

public class TextInputView extends android.support.v7.widget.AppCompatEditText {


    public TextInputView(Context context) {
        super(context);
    }

    public TextInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TextInputView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if (focused) {
            setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);//设置软件盘不全屏
        }
    }
}
