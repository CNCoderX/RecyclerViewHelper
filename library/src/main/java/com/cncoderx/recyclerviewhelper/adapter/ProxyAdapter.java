package com.cncoderx.recyclerviewhelper.adapter;

import android.support.annotation.IntDef;
import android.support.annotation.IntRange;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.cncoderx.recyclerviewhelper.listener.OnItemClickListener;
import com.cncoderx.recyclerviewhelper.listener.OnItemLongClickListener;
import com.cncoderx.recyclerviewhelper.utils.SwipeItemManager;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

/**
 * @author cncoderx
 */
public class ProxyAdapter extends RecyclerView.Adapter implements Filterable {
    List<FixedViewInfo> mHeaderViewInfos = new ArrayList<>();
    List<FixedViewInfo> mFooterViewInfos = new ArrayList<>();

    final RecyclerView.Adapter mAdapter;

    private RecyclerView mRecyclerView;
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    private boolean isSwipable = false;
    private SwipeItemManager mSwipeItemManager;

    public ProxyAdapter(RecyclerView.Adapter adapter) {
        if (adapter == null) {
            throw new IllegalArgumentException();
        }
        mAdapter = adapter;
        setHasStableIds(adapter.hasStableIds());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        final int type = ViewTypeSpec.getType(viewType);
        final int value = ViewTypeSpec.getValue(viewType);

        if (type == ViewTypeSpec.HEADER) {
            viewHolder = new FixedViewHolder(mHeaderViewInfos.get(value).view);
        } else if (type == ViewTypeSpec.FOOTER) {
            viewHolder = new FixedViewHolder(mFooterViewInfos.get(value).view);
        } else {
            viewHolder = mAdapter.onCreateViewHolder(parent, viewType);
            if (isSwipable) {
                obtainSwipeItemManger().bindViewHolder(viewHolder);
            }
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FixedViewHolder) {
            ((FixedViewHolder) holder).onBind();
        } else {
            int adjPosition = position - mHeaderViewInfos.size();
            mAdapter.onBindViewHolder(holder, adjPosition);
        }
    }

    @Override
    public int getItemCount() {
        final int itemCount = mAdapter.getItemCount();
        if (mSwipeItemManager != null) mSwipeItemManager.setItemCount(itemCount);
        return mHeaderViewInfos.size() + mFooterViewInfos.size() + itemCount;
    }

    @Override
    public int getItemViewType(int position) {
        final int numHeaderView = mHeaderViewInfos.size();
//        final int numFooterView = mFooterViewInfos.size();

        if (position < numHeaderView)
            return ViewTypeSpec.makeItemViewTypeSpec(position, ViewTypeSpec.HEADER);

        final int adjPosition = position - numHeaderView;
        final int itemCount = mAdapter.getItemCount();
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
        mRecyclerView = null;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
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
        final int numHeaderView = mHeaderViewInfos.size();

        if (position < numHeaderView) {
            return mHeaderViewInfos.get(position).isFullSpan;
        }

        final int adjPosition = position - numHeaderView;
        final int itemCount = mAdapter.getItemCount();
        if (adjPosition >= itemCount) {
            return mFooterViewInfos.get(adjPosition - itemCount).isFullSpan;
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

        if (onItemClickListener != null)
            holder.itemView.setOnClickListener(null);

        if (onItemLongClickListener != null)
            holder.itemView.setOnLongClickListener(null);
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

        if (onItemClickListener != null) {
            final int position = holder.getLayoutPosition();
            final long id = holder.getItemId();
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null)
                        onItemClickListener.onItemClick(mRecyclerView, v, position, id);
                }
            });
        }

        if (onItemLongClickListener != null) {
            final int position = holder.getLayoutPosition();
            final long id = holder.getItemId();
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemLongClickListener != null)
                        return onItemLongClickListener.onItemLongClick(mRecyclerView, v, position, id);
                    return false;
                }
            });
        }
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
        int adjPosition = position - mHeaderViewInfos.size();
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

    public void addHeaderView(View view) {
        addHeaderView(view, true);
    }

    public void addHeaderView(View view, boolean isFullSpan) {
        if (addFixedViewInfo(view, isFullSpan, mHeaderViewInfos)) {
            mAdapter.notifyDataSetChanged();
            if (mAdapter instanceof ObjectAdapter) {
                int offset = mHeaderViewInfos.size();
                ((ObjectAdapter) mAdapter).setPositionOffset(offset);
            }
        }
    }

    public void removeHeaderView(View view) {
        if (removeFixedViewInfo(view, mHeaderViewInfos)) {
            mAdapter.notifyDataSetChanged();
            if (mAdapter instanceof ObjectAdapter) {
                int offset = mHeaderViewInfos.size();
                ((ObjectAdapter) mAdapter).setPositionOffset(offset);
            }
        }
    }

    public void addFooterView(View view) {
        addFooterView(view, true);
    }

    public void addFooterView(View view, boolean isFullSpan) {
        if (addFixedViewInfo(view, isFullSpan, mFooterViewInfos)) {
            mAdapter.notifyDataSetChanged();
        }
    }

    public void removeFooterView(View view) {
        if (removeFixedViewInfo(view, mFooterViewInfos)) {
            mAdapter.notifyDataSetChanged();
        }
    }

    private boolean addFixedViewInfo(View view, boolean isFullSpan, List<FixedViewInfo> where) {
        FixedViewInfo viewInfo = new FixedViewInfo();
        viewInfo.view = view;
        viewInfo.isFullSpan = isFullSpan;
        return where.add(viewInfo);
    }

    private boolean removeFixedViewInfo(View view, List<FixedViewInfo> where) {
        for (int i = 0, len = where.size(); i < len; i++) {
            FixedViewInfo info = where.get(i);
            if (info.view == view) {
                where.remove(i);
                return true;
            }
        }
        return false;
    }

    public boolean isSwipable() {
        return isSwipable;
    }

    public void setSwipable(boolean swipable) {
        isSwipable = swipable;
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

    SwipeItemManager obtainSwipeItemManger() {
        if (mSwipeItemManager == null) {
            mSwipeItemManager = new SwipeItemManager();
        }
        return mSwipeItemManager;
    }

    @Override
    public Filter getFilter() {
        return mAdapter instanceof Filterable ? ((Filterable) mAdapter).getFilter() : null;
    }

    static class FixedViewInfo {
        View view;
        boolean isFullSpan;
    }

    static class ViewTypeSpec {
        static final int TYPE_SHIFT = 30;
        static final int TYPE_MASK  = 0x3 << TYPE_SHIFT;

        public static final int UNSPECIFIED = 0 << TYPE_SHIFT;
        public static final int HEADER = 1 << TYPE_SHIFT;
        public static final int FOOTER = 2 << TYPE_SHIFT;

        @IntDef({UNSPECIFIED, HEADER, FOOTER})
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

    public static class FixedViewHolder extends RecyclerView.ViewHolder {

        public FixedViewHolder(View itemView) {
            super(itemView);
            setIsRecyclable(false);
        }

        public void onBind() {

        }
    }
}
