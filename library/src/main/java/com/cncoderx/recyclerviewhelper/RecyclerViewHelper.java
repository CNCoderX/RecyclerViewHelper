package com.cncoderx.recyclerviewhelper;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Filter;

import com.cncoderx.recyclerviewhelper.adapter.DelegateAdapter;
import com.cncoderx.recyclerviewhelper.listener.EndlessScrollListener;
import com.cncoderx.recyclerviewhelper.listener.OnItemClickListener;
import com.cncoderx.recyclerviewhelper.listener.OnItemLongClickListener;
import com.cncoderx.recyclerviewhelper.listener.OnLoadMoreListener;
import com.cncoderx.recyclerviewhelper.utils.DividerItemDecoration;
import com.cncoderx.recyclerviewhelper.utils.ILoadingView;
import com.cncoderx.recyclerviewhelper.utils.SpacingItemDecoration;

/**
 * @author cncoderx
 */
public class RecyclerViewHelper {

    public static void setLinearLayout(@NonNull RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
    }

    public static void setLinearLayout(@NonNull RecyclerView recyclerView, int orientation) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), orientation, false));
    }

    public static void setLinearLayout(@NonNull RecyclerView recyclerView, int orientation, boolean reverseLayout) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), orientation, reverseLayout));
    }

    public static void setGridLayout(@NonNull RecyclerView recyclerView, int spanCount) {
        recyclerView.setLayoutManager(new GridLayoutManager(recyclerView.getContext(), spanCount));
    }

    public static void setGridLayout(@NonNull RecyclerView recyclerView, int spanCount, int orientation) {
        recyclerView.setLayoutManager(new GridLayoutManager(recyclerView.getContext(), spanCount, orientation, false));
    }

    public static void setGridLayout(@NonNull RecyclerView recyclerView, int spanCount, int orientation, boolean reverseLayout) {
        recyclerView.setLayoutManager(new GridLayoutManager(recyclerView.getContext(), spanCount, orientation, reverseLayout));
    }

    public static void setStaggeredGridLayout(@NonNull RecyclerView recyclerView, int spanCount) {
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL));
    }

    public static void setStaggeredGridLayout(@NonNull RecyclerView recyclerView, int spanCount, int orientation) {
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(spanCount, orientation));
    }

    public static void setAdapter(@NonNull RecyclerView recyclerView, RecyclerView.Adapter adapter) {
        if (adapter == null || adapter instanceof DelegateAdapter) {
            recyclerView.setAdapter(adapter);
        } else {
            recyclerView.setAdapter(new DelegateAdapter(adapter));
        }
    }

    public static RecyclerView.Adapter getAdapter(@NonNull RecyclerView recyclerView) {
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (adapter instanceof DelegateAdapter) {
            return ((DelegateAdapter) adapter).getWrappedAdapter();
        }
        return adapter;
    }

    public static void addHeaderView(@NonNull RecyclerView recyclerView, View headerView) {
        obtainProxyAdapter(recyclerView, "addHeaderView()").addHeaderView(headerView);
    }

    public static void addHeaderView(@NonNull RecyclerView recyclerView, View headerView, boolean isFullSpan) {
        obtainProxyAdapter(recyclerView, "addHeaderView()").addHeaderView(headerView, isFullSpan);
    }

    public static void removeHeaderView(@NonNull RecyclerView recyclerView, View headerView) {
        obtainProxyAdapter(recyclerView, "removeHeaderView()").removeHeaderView(headerView);
    }

    public static void addFooterView(@NonNull RecyclerView recyclerView, View footerView) {
        obtainProxyAdapter(recyclerView, "addFooterView()").addFooterView(footerView);
    }

    public static void addFooterView(@NonNull RecyclerView recyclerView, View footerView, boolean isFullSpan) {
        obtainProxyAdapter(recyclerView, "addFooterView()").addFooterView(footerView, isFullSpan);
    }

    public static void removeFooterView(@NonNull RecyclerView recyclerView, View footerView) {
        obtainProxyAdapter(recyclerView, "removeFooterView()").removeFooterView(footerView);
    }

    @Deprecated
    public static RecyclerView.ItemDecoration setDivider(@NonNull RecyclerView recyclerView, Drawable divider, int dividerHeight) {
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(divider, dividerHeight);
        recyclerView.addItemDecoration(itemDecoration);
        return itemDecoration;
    }

    @Deprecated
    public static RecyclerView.ItemDecoration setSpace(@NonNull RecyclerView recyclerView, int space) {
        RecyclerView.ItemDecoration itemDecoration = new SpacingItemDecoration(space);
        recyclerView.addItemDecoration(itemDecoration);
        return itemDecoration;
    }

    public static void setOnItemClickListener(@NonNull RecyclerView recyclerView, OnItemClickListener listener) {
        obtainProxyAdapter(recyclerView, "setOnItemClickListener()").setOnItemClickListener(listener);
    }

    public static void setOnItemLongClickListener(@NonNull RecyclerView recyclerView, OnItemLongClickListener listener) {
        obtainProxyAdapter(recyclerView, "setOnItemLongClickListener()").setOnItemLongClickListener(listener);
    }

    public static void setLoadMoreListener(@NonNull RecyclerView recyclerView, OnLoadMoreListener listener) {
        setLoadMoreListener(recyclerView, R.layout.layout_loading_view, listener);
    }

    public static void setLoadMoreListener(@NonNull RecyclerView recyclerView, @LayoutRes int loadingLayout, OnLoadMoreListener listener) {
        if (listener == null) return;
        if (loadingLayout == 0) {
            recyclerView.addOnScrollListener(new EndlessScrollListener(listener));
        } else {
            ILoadingView loadingView = createLoadingView(recyclerView, loadingLayout, true);
            recyclerView.addOnScrollListener(new EndlessScrollListener(listener, loadingView));
        }
    }

    public static void setLoadMoreListener(@NonNull RecyclerView recyclerView, ILoadingView loadingView, OnLoadMoreListener listener) {
        if (listener == null) return;
        recyclerView.addOnScrollListener(new EndlessScrollListener(listener, loadingView));
    }

    private static ILoadingView createLoadingView(@NonNull RecyclerView recyclerView, @LayoutRes int layout, boolean attached) {
        Context context = recyclerView.getContext();
        DelegateAdapter delegateAdapter = obtainProxyAdapter(recyclerView, "createLoadingView()");
        View view = LayoutInflater.from(context).inflate(layout, recyclerView, false);
        return delegateAdapter.createLoadViewHolder(view, true);
    }

    public static ILoadingView getLoadingView(@NonNull RecyclerView recyclerView) {
        DelegateAdapter delegateAdapter = obtainProxyAdapter(recyclerView, "showLoading()");
        return delegateAdapter.getLoadingView();
    }

    public static void setFilterText(@NonNull RecyclerView recyclerView, String filterText) {
        Filter filter = obtainProxyAdapter(recyclerView, "setFilterText()").getFilter();
        if (filter != null) {
            filter.filter(filterText);
        }
    }

    private static DelegateAdapter obtainProxyAdapter(@NonNull RecyclerView recyclerView, String methodName) {
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (adapter == null) {
            throw new IllegalStateException("you must set adapter by RecyclerViewHelper.setAdapter() before call " + methodName);
        }
        if (!(adapter instanceof DelegateAdapter)) {
            throw new IllegalStateException("call RecyclerViewHelper.setAdapter() instead of RecyclerView.setAdapter()");
        }
        return (DelegateAdapter) adapter;
    }
}
