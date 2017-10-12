package com.cncoderx.recyclerviewhelper.listener;

import android.support.v7.widget.RecyclerView;

import com.cncoderx.recyclerviewhelper.utils.ILoadingView;

/**
 * @author cncoderx
 */
public interface OnLoadMoreListener {
    void load(RecyclerView recyclerView, ILoadingView view);
}
