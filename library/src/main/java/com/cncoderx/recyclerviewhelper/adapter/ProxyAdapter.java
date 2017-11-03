package com.cncoderx.recyclerviewhelper.adapter;

import android.support.annotation.IntDef;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.cncoderx.recyclerviewhelper.R;
import com.cncoderx.recyclerviewhelper.listener.OnItemClickListener;
import com.cncoderx.recyclerviewhelper.listener.OnItemLongClickListener;
import com.cncoderx.recyclerviewhelper.utils.ILoadingView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * @author cncoderx
 */
public class ProxyAdapter extends RecyclerView.Adapter implements Filterable {
    private List<FixedViewHolder> mHeaderViewHolders = new ArrayList<>();
    private List<FixedViewHolder> mFooterViewHolders = new ArrayList<>();
    private LoadingViewHolder mLoadViewHolder = null;

    private final RecyclerView.Adapter mAdapter;

    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    public ProxyAdapter(@NonNull RecyclerView.Adapter adapter) {
        mAdapter = adapter;
        setHasStableIds(adapter.hasStableIds());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        final int type = ViewTypeSpec.getType(viewType);
        final int value = ViewTypeSpec.getValue(viewType);

        if (type == ViewTypeSpec.HEADER) {
            viewHolder = mHeaderViewHolders.get(value);
        } else if (type == ViewTypeSpec.FOOTER) {
            viewHolder = mFooterViewHolders.get(value);
        } else if (type == ViewTypeSpec.LOADER){
            viewHolder = mLoadViewHolder;
        } else {
            viewHolder = mAdapter.onCreateViewHolder(parent, viewType);
            ItemClickEvent event = new ItemClickEvent(
                    this, (RecyclerView) parent, viewHolder);
            viewHolder.itemView.setOnClickListener(event);
            viewHolder.itemView.setOnLongClickListener(event);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FixedViewHolder) {
            ((FixedViewHolder) holder).onBind();
        } else {
            int adjPosition = position - mHeaderViewHolders.size();
            mAdapter.onBindViewHolder(holder, adjPosition);
        }
    }

    @Override
    public int getItemCount() {
        final int itemCount = mAdapter.getItemCount();
        if (itemCount == 0 && mLoadViewHolder != null) {
            mLoadViewHolder.mState = ILoadingView.STATE_HIDDEN;
        }
        if (mLoadViewHolder == null || mLoadViewHolder.getState() == ILoadingView.STATE_GONE) {
            return mHeaderViewHolders.size() + mFooterViewHolders.size() + itemCount;
        } else {
            return mHeaderViewHolders.size() + mFooterViewHolders.size() + itemCount + 1;
        }
    }

    @Override
    public int getItemViewType(int position) {
        final int numHeaderView = mHeaderViewHolders.size();
        final int numFooterView = mFooterViewHolders.size();

        if (position < numHeaderView)
            return ViewTypeSpec.makeItemViewTypeSpec(position, ViewTypeSpec.HEADER);

        final int adjPosition = position - numHeaderView;
        final int itemCount = mAdapter.getItemCount();
        if (mLoadViewHolder != null && mLoadViewHolder.getState() != ILoadingView.STATE_GONE
                && adjPosition == itemCount + numFooterView) {
            return ViewTypeSpec.makeItemViewTypeSpec(0, ViewTypeSpec.LOADER);
        }
        if (adjPosition >= itemCount)
            return ViewTypeSpec.makeItemViewTypeSpec(adjPosition - itemCount, ViewTypeSpec.FOOTER);

        int itemViewType = mAdapter.getItemViewType(adjPosition);
        if (itemViewType < 0 || itemViewType > (1 << ViewTypeSpec.TYPE_SHIFT) - 1) {
            throw new IllegalArgumentException("Invalid item view type: RecyclerView.Adapter.getItemViewType return " + itemViewType);
        }
        return itemViewType;
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        mAdapter.onDetachedFromRecyclerView(recyclerView);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        mAdapter.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager != null && layoutManager instanceof GridLayoutManager) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            final int spanCount = gridLayoutManager.getSpanCount();
            final GridLayoutManager.SpanSizeLookup spanSizeLookup = gridLayoutManager.getSpanSizeLookup();
            if (spanSizeLookup != null) {
                gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {

                    @Override
                    public int getSpanSize(int position) {
                        return isFullSpan(position) ? spanCount : spanSizeLookup.getSpanSize(position);
                    }
                });
            }
        }
    }

    private boolean isFullSpan(int position) {
        final int numHeaderView = mHeaderViewHolders.size();
        final int numFooterView = mFooterViewHolders.size();

        if (position < numHeaderView) {
            return mHeaderViewHolders.get(position).isFullSpan;
        }

        final int adjPosition = position - numHeaderView;
        final int itemCount = mAdapter.getItemCount();
        if (mLoadViewHolder != null && mLoadViewHolder.getState() != ILoadingView.STATE_GONE
                && adjPosition == itemCount + numFooterView) {
            return mLoadViewHolder.isFullSpan;
        }
        if (adjPosition >= itemCount) {
            return mFooterViewHolders.get(adjPosition - itemCount).isFullSpan;
        }
        return false;
    }

    @Override
    public void unregisterAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        mAdapter.unregisterAdapterDataObserver(observer);
    }

    @Override
    public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        mAdapter.registerAdapterDataObserver(observer);
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        if (holder instanceof FixedViewHolder) return;
        mAdapter.onViewDetachedFromWindow(holder);
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        if (holder instanceof FixedViewHolder) {
            ViewGroup.LayoutParams lParams = holder.itemView.getLayoutParams();
            if (lParams instanceof StaggeredGridLayoutManager.LayoutParams) {
                boolean isFullSpan = isFullSpan(holder.getAdapterPosition());
                ((StaggeredGridLayoutManager.LayoutParams) lParams).setFullSpan(isFullSpan);
            }
            return;
        }
        mAdapter.onViewAttachedToWindow(holder);

    }

    @Override
    public boolean onFailedToRecycleView(RecyclerView.ViewHolder holder) {
        if (holder instanceof FixedViewHolder) return false;
        return mAdapter.onFailedToRecycleView(holder);
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        if (holder instanceof FixedViewHolder) return;
        mAdapter.onViewRecycled(holder);
    }

    @Override
    public long getItemId(int position) {
        int adjPosition = position - mHeaderViewHolders.size();
        if (adjPosition >= 0 && adjPosition < mAdapter.getItemCount())
            return mAdapter.getItemId(adjPosition);

        return RecyclerView.NO_ID;
    }

//    @Override
//    public void setHasStableIds(boolean hasStableIds) {
//        mAdapter.setHasStableIds(hasStableIds);
//    }

    private boolean isFixedViewType(int viewType) {
        final int type = ViewTypeSpec.getType(viewType);
        return type == ViewTypeSpec.HEADER || type == ViewTypeSpec.FOOTER;
    }

    public void addHeaderView(@NonNull View view) {
        addHeaderView(view, true);
    }

    public void addHeaderView(@NonNull View view, boolean isFullSpan) {
        if (addFixedViewHolder(view, isFullSpan, mHeaderViewHolders)) {
            mAdapter.notifyDataSetChanged();
            if (mAdapter instanceof ObjectAdapter) {
                int offset = mHeaderViewHolders.size();
                ((ObjectAdapter) mAdapter).setPositionOffset(offset);
            }
        }
    }

    public void removeHeaderView(@NonNull View view) {
        if (removeFixedViewHolder(view, mHeaderViewHolders)) {
            mAdapter.notifyDataSetChanged();
            if (mAdapter instanceof ObjectAdapter) {
                int offset = mHeaderViewHolders.size();
                ((ObjectAdapter) mAdapter).setPositionOffset(offset);
            }
        }
    }

    public void addFooterView(@NonNull View view) {
        addFooterView(view, true);
    }

    public void addFooterView(@NonNull View view, boolean isFullSpan) {
        if (addFixedViewHolder(view, isFullSpan, mFooterViewHolders)) {
            mAdapter.notifyDataSetChanged();
        }
    }

    public void removeFooterView(@NonNull View view) {
        if (removeFixedViewHolder(view, mFooterViewHolders)) {
            mAdapter.notifyDataSetChanged();
        }
    }

    private boolean addFixedViewHolder(View view, boolean isFullSpan, List<FixedViewHolder> where) {
        return where.add(new FixedViewHolder(view, isFullSpan));
    }

    private boolean removeFixedViewHolder(View view, List<FixedViewHolder> where) {
        for (int i = 0, len = where.size(); i < len; i++) {
            FixedViewHolder viewHolder = where.get(i);
            if (viewHolder.itemView == view) {
                where.remove(i);
                return true;
            }
        }
        return false;
    }

    public LoadingViewHolder createLoadViewHolder(@NonNull View view, boolean isFullSpan) {
        mLoadViewHolder = new LoadingViewHolder(this, view, isFullSpan);
        return mLoadViewHolder;
    }

    public ILoadingView getLoadingView() {
        return mLoadViewHolder;
    }

    private void notifyLoadingViewInserted() {
        int position = mHeaderViewHolders.size() + mFooterViewHolders.size() + mAdapter.getItemCount();
        mAdapter.notifyItemInserted(position);
    }

    private void notifyLoadingViewRemoved() {
        int position = mHeaderViewHolders.size() + mFooterViewHolders.size() + mAdapter.getItemCount();
        mAdapter.notifyItemRemoved(position);
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public OnItemLongClickListener getOnItemLongClickListener() {
        return onItemLongClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.onItemLongClickListener = listener;
    }

    public RecyclerView.Adapter getWrappedAdapter() {
        return mAdapter;
    }

    @Override
    public Filter getFilter() {
        return mAdapter instanceof Filterable ? ((Filterable) mAdapter).getFilter() : null;
    }

    static class ViewTypeSpec {
        static final int TYPE_SHIFT = 30;
        static final int TYPE_MASK  = 0x3 << TYPE_SHIFT;

        public static final int UNSPECIFIED = 0 << TYPE_SHIFT;
        public static final int HEADER = 1 << TYPE_SHIFT;
        public static final int FOOTER = 2 << TYPE_SHIFT;
        public static final int LOADER = 3 << TYPE_SHIFT;

        @IntDef({UNSPECIFIED, HEADER, FOOTER, LOADER})
        @Retention(RetentionPolicy.SOURCE)
        public @interface ViewTypeSpecMode {}

        public static int makeItemViewTypeSpec(@IntRange(from = 0, to = (1 << TYPE_SHIFT) - 1) int value,
                                               @ViewTypeSpecMode int type) {
            return (value & ~TYPE_MASK) | (type & TYPE_MASK);
        }

        @ViewTypeSpecMode
        public static int getType(int viewType) {
            //noinspection ResourceType
            return (viewType & TYPE_MASK);
        }

        public static int getValue(int viewType) {
            return (viewType & ~TYPE_MASK);
        }
    }

    static class FixedViewHolder extends RecyclerView.ViewHolder {
        boolean isFullSpan;

        public FixedViewHolder(View view, boolean fullSpan) {
            super(view);
            isFullSpan = fullSpan;
        }

        public void onBind() {
        }
    }

    static class LoadingViewHolder extends FixedViewHolder implements ILoadingView {
        private View vShow;
        private View vError;
        private View vEnd;

        private int mState;

        final WeakReference<ProxyAdapter> wrefAdapter;

        public LoadingViewHolder(ProxyAdapter adapter, View view, boolean fullSpan) {
            super(view, fullSpan);
            wrefAdapter = new WeakReference<>(adapter);
            vShow = view.findViewById(R.id.loading_view_show);
            vError = view.findViewById(R.id.loading_view_error);
            vEnd = view.findViewById(R.id.loading_view_end);
            hidden();
        }

        @Override
        public void show() {
            mState = STATE_SHOW;
            itemView.setVisibility(View.VISIBLE);
            vShow.setVisibility(View.VISIBLE);
            vError.setVisibility(View.GONE);
            vEnd.setVisibility(View.GONE);
        }

        @Override
        public void hidden() {
            mState = STATE_HIDDEN;
            itemView.setVisibility(View.GONE);
            vShow.setVisibility(View.GONE);
            vError.setVisibility(View.GONE);
            vEnd.setVisibility(View.GONE);
        }

        @Override
        public void error() {
            mState = STATE_ERROR;
            itemView.setVisibility(View.VISIBLE);
            vShow.setVisibility(View.GONE);
            vError.setVisibility(View.VISIBLE);
            vEnd.setVisibility(View.GONE);
        }

        @Override
        public void end() {
            mState = STATE_END;
            itemView.setVisibility(View.VISIBLE);
            vShow.setVisibility(View.GONE);
            vError.setVisibility(View.GONE);
            vEnd.setVisibility(View.VISIBLE);
        }

        @Override
        public void gone() {
            mState = STATE_GONE;
            itemView.setVisibility(View.GONE);
            vShow.setVisibility(View.GONE);
            vError.setVisibility(View.GONE);
            vEnd.setVisibility(View.GONE);

            ProxyAdapter adapter = wrefAdapter.get();
            if (adapter != null) {
                adapter.notifyLoadingViewRemoved();
            }
        }

        @Override
        public int getState() {
            return mState;
        }

        @Override
        public View getView() {
            return itemView;
        }
    }

    void notifyItemClickListener(RecyclerView parent, View view, int position, long id) {
        int adjPosition = position - mHeaderViewHolders.size();
        if (onItemClickListener != null)
            onItemClickListener.onItemClick(parent, view, adjPosition, id);
    }

    boolean notifyItemLongClickListener(RecyclerView parent, View view, int position, long id) {
        int adjPosition = position - mHeaderViewHolders.size();
        if (onItemLongClickListener != null)
            return onItemLongClickListener.onItemLongClick(parent, view, adjPosition, id);

        return false;
    }

    static class ItemClickEvent implements View.OnClickListener, View.OnLongClickListener {
        final WeakReference<ProxyAdapter> wrefAdapter;
        final WeakReference<RecyclerView> wrefParent;
        final WeakReference<RecyclerView.ViewHolder> wrefViewHolder;

        public ItemClickEvent(ProxyAdapter adapter, RecyclerView parent, RecyclerView.ViewHolder viewHolder) {
            this.wrefAdapter = new WeakReference<>(adapter);
            this.wrefParent = new WeakReference<>(parent);
            this.wrefViewHolder = new WeakReference<>(viewHolder);
        }

        @Override
        public void onClick(View v) {
            ProxyAdapter adapter = wrefAdapter.get();
            RecyclerView parent = wrefParent.get();
            RecyclerView.ViewHolder viewHolder = wrefViewHolder.get();
            if (adapter != null && parent != null && viewHolder != null) {
                int position = viewHolder.getLayoutPosition();
                long id = viewHolder.getItemId();
                adapter.notifyItemClickListener(parent, v, position, id);
            }
        }

        @Override
        public boolean onLongClick(View v) {
            ProxyAdapter adapter = wrefAdapter.get();
            RecyclerView parent = wrefParent.get();
            RecyclerView.ViewHolder viewHolder = wrefViewHolder.get();
            if (adapter != null && parent != null && viewHolder != null) {
                int position = viewHolder.getLayoutPosition();
                long id = viewHolder.getItemId();
                return adapter.notifyItemLongClickListener(parent, v, position, id);
            }
            return false;
        }
    }
}
