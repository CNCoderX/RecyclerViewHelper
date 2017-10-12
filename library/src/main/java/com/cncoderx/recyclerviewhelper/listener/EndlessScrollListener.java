package com.cncoderx.recyclerviewhelper.listener;

import android.support.v7.widget.LinearLayoutManager;
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
        } else {
            throw new RuntimeException(
                    "Unsupported LayoutManager used. Valid ones are LinearLayoutManager, GridLayoutManager and StaggeredGridLayoutManager");
        }
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        if ((visibleItemCount > 0 && newState == RecyclerView.SCROLL_STATE_IDLE
                && lastVisibleItemPosition >= totalItemCount - 1)) {
            if (mLoadingView != null) {
                if (mLoadingView.getState() == ILoadingView.STATE_HIDDEN) {
                    if (onLoadMoreListener != null)
                        onLoadMoreListener.load(recyclerView, mLoadingView);
                }
            } else {
                if (onLoadMoreListener != null)
                    onLoadMoreListener.load(recyclerView, null);
            }
        }
    }
}