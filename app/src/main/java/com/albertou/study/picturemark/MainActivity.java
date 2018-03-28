package com.albertou.study.picturemark;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.albertou.study.picturemark.view.DrawingBoardView;
import com.albertou.study.picturemark.view.TextInputView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import me.iwf.photopicker.PhotoPicker;

public class MainActivity extends AppCompatActivity implements SeekBarDialogFragment.Callback{

    private static final String TAG = "MainActivity";

    private Button mButton;

    private DrawingBoardView mDrawingBoardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButton = findViewById(R.id.button);

        final TextInputView mEditText = findViewById(R.id.editText);
        mDrawingBoardView = findViewById(R.id.drawing_board_view);
        mDrawingBoardView.setAdjustViewBounds(true);
        mDrawingBoardView.setBackgroundColor(Color.GRAY);
//        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
//                if(i == EditorInfo.IME_ACTION_DONE){
//                    String text = mEditText.getText().toString();
//                    if(!TextUtils.isEmpty(text)) {
//                        mDrawingBoardView.addTextPainter(mEditText.getText().toString());
//                    }
//                }
//                return false;
//            }
//        });

//        mEditText.setSoftInputKeyEventBackListener(new TextInputView.SoftInputKeyEventBackListener() {
//            @Override
//            public void callback() {
//                mEditText.setText("");
//                mEditText.setVisibility(View.GONE);
//            }
//        });

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawingBoardView.addPathPainter();
            }
        });

        Button undoButton = findViewById(R.id.undo);
        undoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawingBoardView.undo();
            }
        });

        Button confirm = findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = mEditText.getText().toString();
                if(!TextUtils.isEmpty(text)) {
                    mDrawingBoardView.addTextPainter(mEditText.getText().toString());
                    mEditText.setText("");
                }
//                mDrawingBoardView.setDrawingType(DrawingType.TEXT);
//                mEditText.setVisibility(View.VISIBLE);
//                mEditText.setFocusable(true);
//                mEditText.setFocusableInTouchMode(true);
//                mEditText.requestFocus();
//                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.showSoftInput(mEditText,0);
//                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });

        Button photo = findViewById(R.id.photo);
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhotoPicker.builder()
                        .setPhotoCount(1)
                        .setShowCamera(true)
                        .setShowGif(true)
                        .setPreviewEnabled(false)
                        .start(MainActivity.this, PhotoPicker.REQUEST_CODE);
            }
        });

        Button rect = findViewById(R.id.rect);
        rect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawingBoardView.addRectPainter();
            }
        });

        Button oval = findViewById(R.id.oval);
        oval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawingBoardView.addOvalPainter();
            }
        });

        Button textSize = findViewById(R.id.textSize);
        textSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment fragment = SeekBarDialogFragment.newInstance("字体大小设置", (int) mDrawingBoardView.getTextSize(),100, 0);
                fragment.show(getSupportFragmentManager(), "textSize");
            }
        });

        Button strokeWidth = findViewById(R.id.strokeWidth);
        strokeWidth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment fragment = SeekBarDialogFragment.newInstance("画笔大小设置", (int) mDrawingBoardView.getStrokeWidth(),100, 1);
                fragment.show(getSupportFragmentManager(), "strokeWidth");
            }
        });

        final ImageView imageView = findViewById(R.id.imageView);

        Button save = findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //mDrawingBoardView.setImageBitmap();
                //imageView.setImageBitmap(mDrawingBoardView.getBitmap());

                try {
                    final File file  = createImageFile();
                    mDrawingBoardView.save(file, new SingleObserver<File>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onSuccess(File file) {
                            Glide.with(MainActivity.this).load(file).into(imageView);
                        }

                        @Override
                        public void onError(Throwable e) {

                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + ".jpg";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        if (!storageDir.exists()) {
            if (!storageDir.mkdir()) {
                Log.e("TAG", "Throwing Errors....");
                throw new IOException();
            }
        }

        File image = new File(storageDir, imageFileName);

        // Save a file: path for use with ACTION_VIEW intents
        return image;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PhotoPicker.REQUEST_CODE) {
            if (data != null) {
                ArrayList<String> photos =
                        data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                Log.d(TAG, "onActivityResult: ");
                Glide.with(MainActivity.this).asBitmap().load(photos.get(0)).into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        mDrawingBoardView.setImageBitmap(resource);
                    }
                });
            }
        }
    }

    @Override
    public void onCallback(int value, int flag) {
        if(flag==0) {
            mDrawingBoardView.setTextSize(value);
        }else if(flag == 1){
            mDrawingBoardView.setStrokeWidth(value);
        }
    }

}
