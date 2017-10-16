package com.cncoderx.recyclerviewhelper.utils;

import android.support.v7.widget.RecyclerView;

import com.cncoderx.recyclerviewhelper.R;
import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;

import java.lang.ref.WeakReference;

/**
 * @author cncoderx
 */
public class SwipeItemManager {
    private int mItemCount;
    private int openedPosition = -1;
    private SwipeLayout tempLayout;

    public void bindViewHolder(RecyclerView.ViewHolder holder) {
        SwipeLayout swipeLayout;
        if (holder.itemView instanceof SwipeLayout) {
            swipeLayout = (SwipeLayout) holder.itemView;
        } else {
            swipeLayout = (SwipeLayout) holder.itemView.findViewById(R.id.swipe_layout);
        }
        if (swipeLayout == null)
            throw new IllegalStateException("can not find SwipeLayout in target view");

        swipeLayout.addSwipeListener(new OnSwipeListener(holder));
        swipeLayout.addOnLayoutListener(new OnLayoutListener(holder));
    }

    public void setItemCount(int itemCount) {
        if (mItemCount != itemCount) {
            mItemCount = itemCount;
            openedPosition = -1;
        }
    }

    class OnLayoutListener implements SwipeLayout.OnLayout {
        WeakReference<RecyclerView.ViewHolder> viewHolderRef;

        OnLayoutListener(RecyclerView.ViewHolder holder) {
            viewHolderRef = new WeakReference<>(holder);
        }

        @Override
        public void onLayout(SwipeLayout layout) {
            RecyclerView.ViewHolder holder = viewHolderRef.get();
            if (holder != null) {
                int position = holder.getLayoutPosition();
                if (openedPosition == position) {
                    layout.open(false, false);
                    tempLayout = layout;
                } else {
                    layout.close(false, false);
                }
            }
        }

    }

    class OnSwipeListener extends SimpleSwipeListener {
        WeakReference<RecyclerView.ViewHolder> viewHolderRef;

        OnSwipeListener(RecyclerView.ViewHolder holder) {
            viewHolderRef = new WeakReference<>(holder);
        }

        @Override
        public void onClose(SwipeLayout layout) {
            openedPosition = -1;
            if (tempLayout == layout) {
                tempLayout = null;
            }
        }

        @Override
        public void onStartOpen(SwipeLayout layout) {
            if (tempLayout != null && tempLayout != layout) {
                tempLayout.close();
            }
            tempLayout = layout;
        }

        @Override
        public void onOpen(SwipeLayout layout) {
            RecyclerView.ViewHolder holder = viewHolderRef.get();
            if (holder != null) {
                openedPosition = holder.getLayoutPosition();
            }
        }
    }
}
