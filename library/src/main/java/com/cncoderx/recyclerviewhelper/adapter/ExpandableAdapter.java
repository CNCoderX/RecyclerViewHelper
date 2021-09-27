package com.cncoderx.recyclerviewhelper.adapter;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;

import com.cncoderx.recyclerviewhelper.listener.OnChildClickListener;
import com.cncoderx.recyclerviewhelper.listener.OnChildLongClickListener;
import com.cncoderx.recyclerviewhelper.listener.OnGroupClickListener;
import com.cncoderx.recyclerviewhelper.listener.OnGroupLongClickListener;

/**
 * @author cncoderx
 */
public abstract class ExpandableAdapter<GVH extends RecyclerView.ViewHolder, CVH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter {
    public static final int VIEW_TYPE_GROUP = 0;
    public static final int VIEW_TYPE_CHILD = 1;

    private OnGroupClickListener onGroupClickListener;
    private OnChildClickListener onChildClickListener;
    private OnGroupLongClickListener onGroupLongClickListener;
    private OnChildLongClickListener onChildLongClickListener;

    private SparseBooleanArray mCollapsedPositions = new SparseBooleanArray();

    public ExpandableAdapter() {
        this.onGroupClickListener = new DefaultOnGroupClickListener();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        if (viewType == VIEW_TYPE_GROUP) {
            viewHolder = onCreateGroupViewHolder(parent, viewType);
            GroupClickListener listener = new GroupClickListener((RecyclerView) parent, viewHolder);
            viewHolder.itemView.setOnClickListener(listener);
            viewHolder.itemView.setOnLongClickListener(listener);
        } else {
            viewHolder = onCreateChildViewHolder(parent, viewType);
            ChildClickListener listener = new ChildClickListener((RecyclerView) parent, viewHolder);
            viewHolder.itemView.setOnClickListener(listener);
            viewHolder.itemView.setOnLongClickListener(listener);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int[] positions = getRelativePositions(position);
        if (positions == null)
            return;

        if (positions.length == 1) {
            onBindGroupViewHolder((GVH) holder, positions[0]);
            return;
        }

        if (positions.length == 2) {
            onBindChildViewHolder((CVH) holder, positions[0], positions[1]);
        }
    }

    @Override
    public final int getItemCount() {
        int groupCount = getGroupCount();
        int itemCount = groupCount;
        for (int i = 0; i < groupCount; i++) {
            if (!isCollapsed(i)) {
                itemCount += getChildCount(i);
            }
        }
        return itemCount;
    }

    @Override
    public long getItemId(int position) {
        int[] positions = getRelativePositions(position);
        if (positions == null)
            return 0;

        if (positions.length == 1)
            return getGroupId(positions[0]);

        if (positions.length == 2)
            return getChildId(positions[0], positions[1]);

        return 0;
    }

    public abstract GVH onCreateGroupViewHolder(ViewGroup parent, int viewType);

    public abstract void onBindGroupViewHolder(GVH holder, int groupPosition);

    public abstract int getGroupCount();

    public long getGroupId(int groupPosition) {
        return 0;
    }

    public abstract CVH onCreateChildViewHolder(ViewGroup parent, int viewType);

    public abstract void onBindChildViewHolder(CVH holder, int groupPosition, int childPosition);

    public abstract int getChildCount(int groupPosition);

    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public final int getItemViewType(int position) {
        int[] positions = getRelativePositions(position);
        if (positions == null)
            return 0;

        if (positions.length == 1)
            return getGroupViewType(positions[0]);

        if (positions.length == 2)
            return getChildViewType(positions[0], positions[1]);

        return 0;
    }

    public final int getGroupViewType(int groupPosition) {
        return VIEW_TYPE_GROUP;
    }

    public int getChildViewType(int groupPosition, int childPosition) {
        return VIEW_TYPE_CHILD;
    }

    protected int[] getRelativePositions(int position) {
        int currentPosition = 0;
        int groupCount = getGroupCount();
        for (int i = 0; i < groupCount; i++) {
            if (currentPosition == position) {
                return new int[] {i};
            }
            currentPosition++;
            if (isCollapsed(i)) continue;

            int childCount = getChildCount(i);
            for (int j = 0; j < childCount; j++) {
                if (currentPosition == position) {
                    return new int[] {i, j};
                }
                currentPosition++;
            }
        }
        return null;
    }

    public boolean isCollapsed(int groupPosition) {
        return mCollapsedPositions.get(groupPosition);
    }

    public boolean collapse(int groupPosition) {
        if (isCollapsed(groupPosition)) return false;

        int childCount = getChildCount(groupPosition);
        int startPos = getAbsoluteGroupPosition(groupPosition) + 1;
        notifyItemRangeRemoved(startPos, childCount);
        mCollapsedPositions.put(groupPosition, true);
        return true;
    }

    public boolean expand(int groupPosition) {
        if (!isCollapsed(groupPosition)) return false;

        int childCount = getChildCount(groupPosition);
        int startPos = getAbsoluteGroupPosition(groupPosition) + 1;
        notifyItemRangeInserted(startPos, childCount);
        mCollapsedPositions.put(groupPosition, false);
        return true;
    }

    public void collapseAll() {
        int groupCount = getGroupCount();
        for (int i = 0; i < groupCount; i++) {
            mCollapsedPositions.put(i, true);
        }
        notifyDataSetChanged();
    }

    public void expandAll() {
        int groupCount = getGroupCount();
        for (int i = 0; i < groupCount; i++) {
            mCollapsedPositions.put(i, false);
        }
        notifyDataSetChanged();
    }

    private int getAbsoluteGroupPosition(int groupPosition) {
        int position = 0;
        for (int i = 0; i < groupPosition; i++) {
            position++;
            if (!isCollapsed(i)) {
                position += getChildCount(i);
            }
        }
        return position;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager != null && layoutManager instanceof GridLayoutManager) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            final int spanCount = gridLayoutManager.getSpanCount();
            final GridLayoutManager.SpanSizeLookup spanSizeLookup = gridLayoutManager.getSpanSizeLookup();
            if (spanSizeLookup != null) {
                gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {

                    @Override
                    public int getSpanSize(int position) {
                        return getItemViewType(position) == VIEW_TYPE_GROUP ?
                                spanCount : spanSizeLookup.getSpanSize(position);
                    }
                });
            }
        }
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams lParams = holder.itemView.getLayoutParams();
        if (lParams instanceof StaggeredGridLayoutManager.LayoutParams) {
            boolean isFullSpan = getItemViewType(holder.getAdapterPosition()) == VIEW_TYPE_GROUP;
            ((StaggeredGridLayoutManager.LayoutParams) lParams).setFullSpan(isFullSpan);
        }
    }

    public OnGroupClickListener getOnGroupClickListener() {
        return onGroupClickListener;
    }

    public void setOnGroupClickListener(OnGroupClickListener listener) {
        onGroupClickListener = listener;
    }

    public OnChildClickListener getOnChildClickListener() {
        return onChildClickListener;
    }

    public void setOnChildClickListener(OnChildClickListener listener) {
        onChildClickListener = listener;
    }

    public OnGroupLongClickListener getOnGroupLongClickListener() {
        return onGroupLongClickListener;
    }

    public void setOnGroupLongClickListener(OnGroupLongClickListener listener) {
        onGroupLongClickListener = listener;
    }

    public OnChildLongClickListener getOnChildLongClickListener() {
        return onChildLongClickListener;
    }

    public void setOnChildLongClickListener(OnChildLongClickListener listener) {
        onChildLongClickListener = listener;
    }

    class GroupClickListener implements View.OnClickListener, View.OnLongClickListener {
        final RecyclerView mParent;
        final RecyclerView.ViewHolder mViewHolder;

        public GroupClickListener(RecyclerView parent, RecyclerView.ViewHolder viewHolder) {
            mParent = parent;
            mViewHolder = viewHolder;
        }

        @Override
        public void onClick(View v) {
            long id = mViewHolder.getItemId();
            int position = mViewHolder.getLayoutPosition();
            if (onGroupClickListener != null) {
                int[] positions = getRelativePositions(position);
                onGroupClickListener.onGroupClick(mParent, v, positions[0], id);
            }
        }

        @Override
        public boolean onLongClick(View v) {
            long id = mViewHolder.getItemId();
            int position = mViewHolder.getLayoutPosition();
            if (onGroupLongClickListener != null) {
                int[] positions = getRelativePositions(position);
                return onGroupLongClickListener.onGroupLongClick(mParent, v, positions[0], id);
            }
            return false;
        }
    }

    class ChildClickListener implements View.OnClickListener, View.OnLongClickListener {
        final RecyclerView mParent;
        final RecyclerView.ViewHolder mViewHolder;

        public ChildClickListener(RecyclerView parent, RecyclerView.ViewHolder viewHolder) {
            mParent = parent;
            mViewHolder = viewHolder;
        }

        @Override
        public void onClick(View v) {
            long id = mViewHolder.getItemId();
            int position = mViewHolder.getLayoutPosition();
            if (onChildClickListener != null) {
                int[] positions = getRelativePositions(position);
                onChildClickListener.onChildClick(mParent, v, positions[0], positions[1], id);
            }
        }

        @Override
        public boolean onLongClick(View v) {
            long id = mViewHolder.getItemId();
            int position = mViewHolder.getLayoutPosition();
            if (onChildLongClickListener != null) {
                int[] positions = getRelativePositions(position);
                return onChildLongClickListener.onChildLongClick(mParent, v, positions[0], positions[1], id);
            }
            return false;
        }
    }

    class DefaultOnGroupClickListener implements OnGroupClickListener {

        @Override
        public void onGroupClick(RecyclerView parent, View v, int groupPosition, long id) {
            if (isCollapsed(groupPosition)) {
                expand(groupPosition);
            } else {
                collapse(groupPosition);
            }
        }
    }
}
