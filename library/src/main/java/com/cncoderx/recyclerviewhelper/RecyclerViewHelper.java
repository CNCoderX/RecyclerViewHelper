package com.cncoderx.recyclerviewhelper;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Filter;

import com.cncoderx.recyclerviewhelper.adapter.ProxyAdapter;
import com.cncoderx.recyclerviewhelper.listener.EndlessScrollListener;
import com.cncoderx.recyclerviewhelper.listener.OnItemClickListener;
import com.cncoderx.recyclerviewhelper.listener.OnItemLongClickListener;
import com.cncoderx.recyclerviewhelper.listener.OnLoadMoreListener;
import com.cncoderx.recyclerviewhelper.utils.DividerItemDecoration;
import com.cncoderx.recyclerviewhelper.utils.ILoadingView;
import com.cncoderx.recyclerviewhelper.utils.LoadingView;
import com.cncoderx.recyclerviewhelper.utils.SpacingItemDecoration;

/**
 * @author cncoderx
 */
public class RecyclerViewHelper {

    public static void setLinearLayout(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
    }

    public static void setLinearLayout(RecyclerView recyclerView, int orientation) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), orientation, false));
    }

    public static void setLinearLayout(RecyclerView recyclerView, int orientation, boolean reverseLayout) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), orientation, reverseLayout));
    }

    public static void setGridLayout(RecyclerView recyclerView, int spanCount) {
        recyclerView.setLayoutManager(new GridLayoutManager(recyclerView.getContext(), spanCount));
    }

    public static void setGridLayout(RecyclerView recyclerView, int spanCount, int orientation) {
        recyclerView.setLayoutManager(new GridLayoutManager(recyclerView.getContext(), spanCount, orientation, false));
    }

    public static void setGridLayout(RecyclerView recyclerView, int spanCount, int orientation, boolean reverseLayout) {
        recyclerView.setLayoutManager(new GridLayoutManager(recyclerView.getContext(), spanCount, orientation, reverseLayout));
    }

    public static void setStaggeredGridLayout(RecyclerView recyclerView, int spanCount) {
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL));
    }

    public static void setStaggeredGridLayout(RecyclerView recyclerView, int spanCount, int orientation) {
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(spanCount, orientation));
    }

    public static void setAdapter(RecyclerView recyclerView, RecyclerView.Adapter adapter) {
        if (adapter == null || adapter instanceof ProxyAdapter) {
            recyclerView.setAdapter(adapter);
        } else {
            recyclerView.setAdapter(new ProxyAdapter(adapter));
        }
    }

    public static RecyclerView.Adapter getAdapter(RecyclerView recyclerView) {
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (adapter instanceof ProxyAdapter) {
            return ((ProxyAdapter) adapter).getWrappedAdapter();
        }
        return adapter;
    }

    public static void addHeaderView(RecyclerView recyclerView, View headerView) {
        obtainProxyAdapter(recyclerView, "addHeaderView()").addHeaderView(headerView);
    }

    public static void addHeaderView(RecyclerView recyclerView, View headerView, boolean isFullSpan) {
        obtainProxyAdapter(recyclerView, "addHeaderView()").addHeaderView(headerView, isFullSpan);
    }

    public static void removeHeaderView(RecyclerView recyclerView, View headerView) {
        obtainProxyAdapter(recyclerView, "removeHeaderView()").removeHeaderView(headerView);
    }

    public static void addFooterView(RecyclerView recyclerView, View footerView) {
        obtainProxyAdapter(recyclerView, "addFooterView()").addFooterView(footerView);
    }

    public static void addFooterView(RecyclerView recyclerView, View footerView, boolean isFullSpan) {
        obtainProxyAdapter(recyclerView, "addFooterView()").addFooterView(footerView, isFullSpan);
    }

    public static void removeFooterView(RecyclerView recyclerView, View footerView) {
        obtainProxyAdapter(recyclerView, "removeFooterView()").removeFooterView(footerView);
    }

    public static RecyclerView.ItemDecoration setDivider(RecyclerView recyclerView, Drawable divider, int dividerHeight) {
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(divider, dividerHeight);
        recyclerView.addItemDecoration(itemDecoration);
        return itemDecoration;
    }

    public static RecyclerView.ItemDecoration setSpace(RecyclerView recyclerView, int space) {
        RecyclerView.ItemDecoration itemDecoration = new SpacingItemDecoration(space);
        recyclerView.addItemDecoration(itemDecoration);
        return itemDecoration;
    }

    public static void setOnItemClickListener(RecyclerView recyclerView, OnItemClickListener listener) {
        obtainProxyAdapter(recyclerView, "setOnItemClickListener()").setOnItemClickListener(listener);
    }

    public static void setOnItemLongClickListener(RecyclerView recyclerView, OnItemLongClickListener listener) {
        obtainProxyAdapter(recyclerView, "setOnItemLongClickListener()").setOnItemLongClickListener(listener);
    }

    public static void setLoadMoreListener(RecyclerView recyclerView, OnLoadMoreListener listener) {
        setLoadMoreListener(recyclerView, R.layout.layout_loading_view, listener);
    }

    public static void setLoadMoreListener(RecyclerView recyclerView, @LayoutRes int loadingLayout, OnLoadMoreListener listener) {
        if (listener == null) return;
        if (loadingLayout == 0) {
            recyclerView.addOnScrollListener(new EndlessScrollListener(listener));
        } else {
            ILoadingView loadingView = createLoadingView(recyclerView, loadingLayout, true);
            recyclerView.addOnScrollListener(new EndlessScrollListener(listener, loadingView));
        }
    }

    public static void setLoadMoreListener(RecyclerView recyclerView, ILoadingView loadingView, OnLoadMoreListener listener) {
        if (listener == null) return;
        recyclerView.addOnScrollListener(new EndlessScrollListener(listener, loadingView));
    }

    private static LoadingView createLoadingView(RecyclerView recyclerView, @LayoutRes int layout, boolean attached) {
        Context context = recyclerView.getContext();
        LoadingView loadingView = new LoadingView(context, layout, recyclerView);
        if (attached) {
            ProxyAdapter proxyAdapter = obtainProxyAdapter(recyclerView, "createLoadingView()");
            proxyAdapter.addFooterView(loadingView.getView(), true);
        }
        return loadingView;
    }

    public static void setFilterText(RecyclerView recyclerView, String filterText) {
        Filter filter = obtainProxyAdapter(recyclerView, "setFilterText()").getFilter();
        if (filter != null) {
            filter.filter(filterText);
        }
    }

    public static void setSwipable(RecyclerView recyclerView, boolean swipable) {
        obtainProxyAdapter(recyclerView, "setSwipable()").setSwipable(swipable);
    }

//    public static void setOnGroupClickListener(RecyclerView recyclerView, OnGroupClickListener listener) {
//        obtainExpandableAdapter(recyclerView, "setOnGroupClickListener").setOnGroupClickListener(listener);
//    }
//
//    public static void setOnGroupLongClickListener(RecyclerView recyclerView, OnGroupLongClickListener listener) {
//        obtainExpandableAdapter(recyclerView, "setOnGroupLongClickListener").setOnGroupLongClickListener(listener);
//    }
//
//    public static void setOnChildClickListener(RecyclerView recyclerView, OnChildClickListener listener) {
//        obtainExpandableAdapter(recyclerView, "setOnChildClickListener").setOnChildClickListener(listener);
//    }
//
//    public static void setOnChildLongClickListener(RecyclerView recyclerView, OnChildLongClickListener listener) {
//        obtainExpandableAdapter(recyclerView, "setOnChildLongClickListener").setOnChildLongClickListener(listener);
//    }

    private static ProxyAdapter obtainProxyAdapter(RecyclerView recyclerView, String methodName) {
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (adapter == null) {
            throw new IllegalStateException("you must set adapter by RecyclerViewHelper.setAdapter() before call " + methodName);
        }
        if (!(adapter instanceof ProxyAdapter)) {
            throw new IllegalStateException("call RecyclerViewHelper.setAdapter() instead of RecyclerView.setAdapter()");
        }
        return (ProxyAdapter) adapter;
    }

//    private static ExpandableAdapter obtainExpandableAdapter(RecyclerView recyclerView, String methodName) {
//        RecyclerView.Adapter adapter = recyclerView.getAdapter();
//        if (adapter == null) {
//            throw new IllegalStateException("you must set adapter by RecyclerViewHelper.setAdapter() before call " + methodName);
//        }
//        if (!(adapter instanceof ExpandableAdapter)) {
//            throw new IllegalStateException("current adapter must instance of ExpandableAdapter");
//        }
//        return (ExpandableAdapter) adapter;
//    }
}
