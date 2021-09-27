package com.cncoderx.recyclerviewhelper.utils;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author cncoderx
 */
@Deprecated
public class DividerItemDecoration extends RecyclerView.ItemDecoration {
    private Drawable mDivider;
    private int mDividerHeight;

    public DividerItemDecoration(Drawable divider, int dividerHeight) {
        mDivider = divider;
        mDividerHeight = dividerHeight;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (mDivider == null) return;
        if (!(parent.getLayoutManager() instanceof LinearLayoutManager)) return;
        LinearLayoutManager layoutManager = (LinearLayoutManager) parent.getLayoutManager();
        int orientation = layoutManager.getOrientation();
        if (orientation == LinearLayoutManager.VERTICAL) {
            final int left = parent.getPaddingLeft();
            final int right = parent.getWidth() - parent.getPaddingRight();
            final int childCount = parent.getChildCount();
            for (int i = 1; i < childCount; i++) {
                final View child = parent.getChildAt(i);
                if (child.getVisibility() == View.GONE) continue;
                final ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
                final int bottom = child.getTop() - params.topMargin + Math.round(ViewCompat.getTranslationY(child));
                final int top = bottom - mDividerHeight;
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        } else {
            final int top = parent.getPaddingTop();
            final int bottom = parent.getHeight() - parent.getPaddingBottom();
            final int childCount = parent.getChildCount();
            for (int i = 1; i < childCount; i++) {
                final View child = parent.getChildAt(i);
                if (child.getVisibility() == View.GONE) continue;
                final ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
                final int right = child.getLeft() - params.leftMargin + Math.round(ViewCompat.getTranslationX(child));
                final int left = right - mDividerHeight;
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        final int adapterPosition = parent.getChildAdapterPosition(view);
        final int layoutPosition = parent.getChildLayoutPosition(view);
        if (!(parent.getLayoutManager() instanceof LinearLayoutManager)) return;
        LinearLayoutManager layoutManager = (LinearLayoutManager) parent.getLayoutManager();
        if (adapterPosition > 0 || adapterPosition == -1 && layoutPosition != 0) {
            int orientation = layoutManager.getOrientation();
            if (orientation == LinearLayoutManager.VERTICAL) {
                outRect.top = mDividerHeight;
            } else {
                outRect.left = mDividerHeight;
            }
        }
    }
}
