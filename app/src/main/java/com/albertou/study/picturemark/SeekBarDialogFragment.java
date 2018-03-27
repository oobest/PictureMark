package com.albertou.study.picturemark;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.SeekBar;

/**
 * Created by Albert on 2018/3/27.
 */

public class SeekBarDialogFragment extends DialogFragment {

    private static final String ARG_TITLE ="title";

    private static final String ARG_VALUE ="value";

    private static final String ARG_MAX ="max";

    private static final String ARG_FLAG="flag";

    private Callback mCallback;

    public static DialogFragment newInstance(String title, int value, int max, int flag){
        DialogFragment fragment = new SeekBarDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_TITLE,title);
        bundle.putInt(ARG_VALUE, value);
        bundle.putInt(ARG_MAX, max);
        bundle.putInt(ARG_FLAG, flag);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallback = (Callback) getActivity();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        Bundle bundle = getArguments();
        builder.setTitle(bundle.getString(ARG_TITLE));

        final SeekBar seekBar = new SeekBar(getActivity());
        seekBar.setMax(bundle.getInt(ARG_MAX));
        seekBar.setProgress(bundle.getInt(ARG_VALUE));
        builder.setView(seekBar);

        final int flag = bundle.getInt(ARG_FLAG);

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(mCallback!=null){
                    mCallback.onCallback(seekBar.getProgress(), flag);
                }
            }
        });
        builder.setNegativeButton("取消", null);
        return builder.create();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    public interface Callback{
        void onCallback(int value, int flag);
    }
}
