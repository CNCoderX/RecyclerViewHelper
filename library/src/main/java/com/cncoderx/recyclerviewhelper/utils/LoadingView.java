package com.cncoderx.recyclerviewhelper.utils;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cncoderx.recyclerviewhelper.R;

/**
 * @author cncoderx
 */
public class LoadingView implements ILoadingView {
    View view;
    View vShow;
    View vError;
    View vEnd;

    private int mState;

    public LoadingView(Context context, @LayoutRes int resource, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(resource, parent, false);
        vShow = view.findViewById(R.id.loading_view_show);
        vError = view.findViewById(R.id.loading_view_error);
        vEnd = view.findViewById(R.id.loading_view_end);
        hidden();
    }

    @Override
    public void show() {
        mState = STATE_SHOW;
        view.setVisibility(View.VISIBLE);
        vShow.setVisibility(View.VISIBLE);
        vError.setVisibility(View.GONE);
        vEnd.setVisibility(View.GONE);
    }

    @Override
    public void hidden() {
        mState = STATE_HIDDEN;
        view.setVisibility(View.GONE);

        ViewGroup.LayoutParams lParams = view.getLayoutParams();
        lParams.height = 0;
        view.setLayoutParams(lParams);
    }

    @Override
    public void error() {
        mState = STATE_ERROR;
        view.setVisibility(View.VISIBLE);
        vShow.setVisibility(View.GONE);
        vError.setVisibility(View.VISIBLE);
        vEnd.setVisibility(View.GONE);
    }

//    @Override
//    public void error(int duration) {
//        error();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                hidden();
//            }
//        }, duration);
//    }

    @Override
    public void end() {
        mState = STATE_END;
        view.setVisibility(View.VISIBLE);
        vShow.setVisibility(View.GONE);
        vError.setVisibility(View.GONE);
        vEnd.setVisibility(View.VISIBLE);
    }

    @Override
    public int getState() {
        return mState;
    }

    @Override
    public View getView() {
        return view;
    }
}
