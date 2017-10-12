package com.cncoderx.recyclerviewhelper.utils;

import android.view.View;

/**
 * @author cncoderx
 */
public interface ILoadingView {
    public static final int STATE_HIDDEN    = 0;
    public static final int STATE_SHOW      = 1;
    public static final int STATE_ERROR     = 2;
    public static final int STATE_END       = 3;

    void show();
    void hidden();
    void error();
    void end();
    int getState();
    View getView();
}
