package com.cncoderx.recyclerviewhelper.utils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @author cncoderx
 */
public class SpacingItemDecoration extends RecyclerView.ItemDecoration {
    private int mHalfSpace;
    private boolean hasPadding;

    public SpacingItemDecoration(int space) {
        mHalfSpace = space / 2;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (!hasPadding) {
            hasPadding = true;
            parent.setPadding(mHalfSpace, mHalfSpace, mHalfSpace, mHalfSpace);
        }
        outRect.set(mHalfSpace, mHalfSpace, mHalfSpace, mHalfSpace);
    }
}
