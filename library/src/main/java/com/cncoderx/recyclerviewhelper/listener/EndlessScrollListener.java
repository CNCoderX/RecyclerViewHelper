package com.cncoderx.recyclerviewhelper.listener;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.cncoderx.recyclerviewhelper.utils.ILoadingView;

import java.util.Arrays;

/**
 * @author cncoderx
 */
public class EndlessScrollListener extends RecyclerView.OnScrollListener {
    private int lastVisibleItemPosition;
    private int[] positions;
    private int oldSpanCount;
    private int orientation;

    private OnLoadMoreListener onLoadMoreListener;
    private ILoadingView mLoadingView;

    public EndlessScrollListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public EndlessScrollListener(OnLoadMoreListener onLoadMoreListener, ILoadingView loadingView) {
        this.onLoadMoreListener = onLoadMoreListener;
        this.mLoadingView = loadingView;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
            lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
            orientation = linearLayoutManager.getOrientation();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager staggeredLayoutManager = (StaggeredGridLayoutManager) layoutManager;
            int spanCount = staggeredLayoutManager.getSpanCount();
            if (spanCount == oldSpanCount) {
                positions = staggeredLayoutManager.findLastVisibleItemPositions(positions);
            } else {
                positions = staggeredLayoutManager.findLastVisibleItemPositions(null);
                oldSpanCount = spanCount;
            }
            Arrays.sort(positions);
            lastVisibleItemPosition = positions[spanCount - 1];
            orientation = staggeredLayoutManager.getOrientation();
        } else {
            throw new RuntimeException(
                    "Unsupported LayoutManager used. Valid ones are LinearLayoutManager, GridLayoutManager and StaggeredGridLayoutManager");
        }
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        int itemCount = layoutManager.getItemCount();
        if (newState == RecyclerView.SCROLL_STATE_IDLE
                && canScroll(recyclerView)
                && lastVisibleItemPosition >= itemCount - 1) {
            if (mLoadingView != null) {
                if (mLoadingView.getState() == ILoadingView.STATE_HIDDEN) {
                    mLoadingView.show();
                    if (onLoadMoreListener != null) {
                        onLoadMoreListener.load(recyclerView, mLoadingView);
                    }
                }
            } else {
                if (onLoadMoreListener != null)
                    onLoadMoreListener.load(recyclerView, null);
            }
        }
    }

    private boolean canScroll(View v) {
        if (orientation == OrientationHelper.VERTICAL) {
            return v.canScrollVertically(-1);
        }
        if (orientation == OrientationHelper.HORIZONTAL) {
            return v.canScrollHorizontally(-1);
        }
        return true;
    }
}